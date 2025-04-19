package com.example.jatelobank.Settings.Accounts;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.*;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ToSavingsAccount {
    public TextField accNumber;
    public TextField amount;
    public DatePicker date;
    public PasswordField password;
    public Label label;
    public Button transferButton;
    public DatabaseConnection connection = new DatabaseConnection();
    public FontAwesomeIconView microphoneButton;
    public TextArea microphoneTextArea;
    private boolean isListening = false;

    public void transferButt(ActionEvent event) {

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){

            String acc = current.getAccNo();
            Connection connection1 = connection.getConn();

            //retrieve password from user account
            String retrievePassword = "select Password from Users where AccountNumber = '"+acc+"'";

            try {
                //retrieve password from user account
                PreparedStatement psRetrievePassword = connection1.prepareStatement(retrievePassword);
                ResultSet passwordRetrieval = psRetrievePassword.executeQuery();

                //retrieve amount from  checking acc first
                String query = "select Amount from CheckingAccount where AccountNumber = '"+acc+"'";

                while (passwordRetrieval.next()){

                    //retrieve password from users
                    String pass = passwordRetrieval.getString("Password");

                    //retrieving amount from checking account
                    PreparedStatement ps = connection1.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();



                    while (rs.next()){
                        //retrieve amount from checking account
                        double checkingAmount = rs.getDouble("Amount");

                        if (accNumber.getText().isEmpty()){
                            label.setText("Account number cannot be empty");
                        } else if (!accNumber.getText().equals(acc)) {
                            label.setText("Invalid account number");
                        } else if (amount.getText().isEmpty()) {
                            label.setText("Enter amount to transfer");
                        } else if (Double.parseDouble(amount.getText()) > checkingAmount) {
                            label.setText("Insufficient account balance to make the transfer of "+amount.getText());
                        } else if (checkingAmount <= 0) {
                            label.setText("You have insufficient money in your account!");
                        } else if (date.getEditor().getText().isEmpty()) {
                            label.setText("Select the date");
                        }else if (!hashedPassword(password.getText()).equals(pass)) {
                            label.setText("Invalid password");
                        }else {

                            transfer();
                            accNumber.clear();
                            amount.clear();
                            date.getEditor().clear();
                            password.clear();
                            label.setText("Transferred successfully to your savings account!");
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void recordAudio(File outputFile, int seconds) throws Exception {        AudioFormat format = new AudioFormat(16000, 16, 1, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        if (!AudioSystem.isLineSupported(info)) {
            throw new LineUnavailableException("Microphone not supported.");
        }

        TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
        microphone.open(format);
        microphone.start();

        Thread stopper = new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000);
            } catch (InterruptedException ignored) {}
            microphone.stop();
            microphone.close();
        });

        stopper.start();
        AudioInputStream audioStream = new AudioInputStream(microphone);
        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, outputFile);
    }


    private String transcribeAudio(File file) throws Exception {
        String modelPath = "C:/Users/Jatelo/IdeaProjects/Jatelo-Bank/src/main/resources/vosk-model-small-en-us-0.15";

        try (Model model = new Model(modelPath);
             AudioInputStream ais = AudioSystem.getAudioInputStream(file)) {

            AudioFormat format = ais.getFormat();
            if (format.getSampleRate() != 16000.0f) {
                throw new IllegalArgumentException("Audio must be 16kHz.");
            }

            Recognizer recognizer = new Recognizer(model, format.getSampleRate());
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = ais.read(buffer)) >= 0) {
                recognizer.acceptWaveForm(buffer, bytesRead);
            }

            String resultJson = recognizer.getFinalResult();
            System.out.println("Full JSON from recognizer: " + resultJson);

            JSONObject result = new JSONObject(resultJson);
            String transcript = result.getString("text").trim();

            System.out.println("Transcript: " + transcript);
            return transcript;
        }
    }

    private void processVoiceCommand(String command) {
        microphoneTextArea.appendText("Heard: " + command + "\n");

        // Extract amount as words (e.g., "fifty point five")
        Pattern amountPattern = Pattern.compile("(?i)(?:transfer|amount|send)?\\s*([a-z\\s\\-]+(?:point\\s+[a-z]+)?)");
        Matcher amountMatcher = amountPattern.matcher(command);

        if (amountMatcher.find()) {
            String amountWords = amountMatcher.group(1).replace("point", ".");
            String[] parts = amountWords.split("\\.");
            double main = wordsToNumber(parts[0]);
            double fraction = parts.length > 1 ? wordsToNumber(parts[1]) / Math.pow(10, parts[1].trim().split("\\s+").length) : 0;

            double finalAmount = main + fraction;
            amount.setText(String.format("%.2f", finalAmount));
            microphoneTextArea.appendText("Extracted amount: " + finalAmount + "\n");
        } else {
            microphoneTextArea.appendText("Could not find amount.\n");
        }

        // Extract account number as worded digits (e.g., "one two three")
        Pattern accPattern = Pattern.compile("account(?:\\s+number)?\\s+((?:zero|one|two|three|four|five|six|seven|eight|nine|and|\\s)+)", Pattern.CASE_INSENSITIVE);
        Matcher accMatcher = accPattern.matcher(command);

        if (accMatcher.find()) {
            String accWords = accMatcher.group(1).replaceAll("\\s+", " ").trim();
            String[] wordDigits = accWords.split("\\s+");
            StringBuilder accountNum = new StringBuilder();

            Map<String, String> digitMap = Map.of(
                    "zero", "0", "one", "1", "two", "2", "three", "3", "four", "4",
                    "five", "5", "six", "6", "seven", "7", "eight", "8", "nine", "9"
            );

            for (String word : wordDigits) {
                word = word.trim().toLowerCase();
                if (digitMap.containsKey(word)) {
                    accountNum.append(digitMap.get(word));
                }
            }

            String accNumStr = accountNum.toString();
            microphoneTextArea.appendText("Constructed account number: " + accNumStr + "\n");

            if (accNumStr.length() >= 2) {
                String lastTwoDigits = accNumStr.substring(accNumStr.length() - 2);
                String fullAccountNumber = "11000000000000" + lastTwoDigits;
                accNumber.setText(fullAccountNumber);
                microphoneTextArea.appendText("Extracted account number: " + fullAccountNumber + "\n");
            } else {
                microphoneTextArea.appendText("Account number is too short to extract last two digits.\n");
            }
        } else {
            microphoneTextArea.appendText("Could not find account number.\n");
        }

        date.setValue(LocalDate.now());

        String pass = "Jatelo";
        password.setText(pass);

        DatabaseConnection connection1 = new DatabaseConnection();
        Connection connection2 = connection1.getConn();

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null) {
            String acc = current.getAccNo();
            String retrievePassword = "SELECT Password FROM Users WHERE AccountNumber = ?";
            try {
                PreparedStatement pst = connection2.prepareStatement(retrievePassword);
                pst.setString(1, acc);

                ResultSet passwordRetrieval = pst.executeQuery();
                while (passwordRetrieval.next()) {
                    // String pass = passwordRetrieval.getString("Password");
                    // password.setText(hashedPassword(pass)); // Use if needed
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static double wordsToNumber(String text) {
        text = text.toLowerCase().replaceAll("[^a-z\\s\\-]", "").trim();
        String[] words = text.split("\\s+");

        Map<String, Integer> numberMap = Map.ofEntries(
                Map.entry("zero", 0), Map.entry("one", 1), Map.entry("two", 2), Map.entry("three", 3),
                Map.entry("four", 4), Map.entry("five", 5), Map.entry("six", 6), Map.entry("seven", 7),
                Map.entry("eight", 8), Map.entry("nine", 9), Map.entry("ten", 10), Map.entry("eleven", 11),
                Map.entry("twelve", 12), Map.entry("thirteen", 13), Map.entry("fourteen", 14),
                Map.entry("fifteen", 15), Map.entry("sixteen", 16), Map.entry("seventeen", 17),
                Map.entry("eighteen", 18), Map.entry("nineteen", 19), Map.entry("twenty", 20),
                Map.entry("thirty", 30), Map.entry("forty", 40), Map.entry("fifty", 50),
                Map.entry("sixty", 60), Map.entry("seventy", 70), Map.entry("eighty", 80),
                Map.entry("ninety", 90), Map.entry("hundred", 100), Map.entry("thousand", 1000)
        );

        double result = 0;
        double current = 0;

        for (String word : words) {
            if (!numberMap.containsKey(word)) continue;

            int value = numberMap.get(word);
            if (value == 100 || value == 1000) {
                current *= value;
            } else {
                current += value;
            }
        }

        result += current;
        return result;
    }

    public void transfer(){

        Connection connection1 = connection.getConn();

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null) {

            String acc = current.getAccNo();

            //retrieve amount from  checking acc first
            String query = "select Amount from CheckingAccount where AccountNumber = '"+acc+"'";


            //retrieve amount from  savings  acc
            String queryRetrieve = "select Amount from SavingsAccount where AccountNumber = '"+acc+"'";

            //retrieve deposit from Savings acc
            String queryDeposited = "select Deposited from SavingsAccount where AccountNumber = '"+acc+"'";

            //update the savings account
            String updateSavings = "update SavingsAccount set Amount = ?,Date = ?,Deposited = ? where AccountNumber = '" +acc+ "'";


            //update the checking account
            String updateChecking = "update CheckingAccount set Amount = ?,Acc = ?,Date = ? where AccountNumber = '"+acc+"'";

            //insert data into toSavingsAccount
            String toSavings = "insert into ToSavingsAccount (AccountNumber,Amount,Date) values (?,?,?)";

            try {

                //retrieving amount from checking account
                PreparedStatement ps = connection1.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                //retrieving amount from savings account
                PreparedStatement retrieveSavingsAmount = connection1.prepareStatement(queryRetrieve);
                ResultSet rsRetrieveSavingsAmount = retrieveSavingsAmount.executeQuery();

                //retrieve deposits from savings account
                PreparedStatement retrieveDeposits = connection1.prepareStatement(queryDeposited);
                ResultSet deposits = retrieveDeposits.executeQuery();

                //insert the dat into toBudgetAccount
                PreparedStatement toSave = connection1.prepareStatement(toSavings);
                toSave.setString(1,acc);
                toSave.setDouble(2, Double.parseDouble(amount.getText()));
                LocalDate localDate3 = date.getValue();
                if (localDate3 != null){
                    Date local = Date.valueOf(localDate3);
                    toSave.setDate(3,local);
                }else {
                    throw new IllegalArgumentException("Error");
                }
                toSave.executeUpdate();

                while (rs.next() &&  rsRetrieveSavingsAmount.next() && deposits.next()){
                    //retrieve amount from checking account
                    double checkingAmount = rs.getDouble("Amount");

                    //retrieve amount from savings account
                    double savingAmount = rsRetrieveSavingsAmount.getDouble("Amount");

                    //retrieve deposits
                    double deposit = deposits.getDouble("Deposited");



                    //get amount field and change to double
                    double amountField = Double.parseDouble(amount.getText());

                    //updating saving account
                    double amountTotal1 = amountField + savingAmount;
                    double totalDeposited = amountField + deposit;
                    PreparedStatement psUpdateSavings = connection1.prepareStatement(updateSavings);
                    psUpdateSavings.setDouble(1,amountTotal1);
                    LocalDate localDate = date.getValue();
                    if (localDate != null){
                        Date local = Date.valueOf(localDate);
                        psUpdateSavings.setDate(2,local);
                    }else {
                        throw new IllegalArgumentException("Error");
                    }
                    psUpdateSavings.setDouble(3,totalDeposited);
                    psUpdateSavings.executeUpdate();

                    //update checking account
                    PreparedStatement checkingUpdate = connection1.prepareStatement(updateChecking);
                    double amountTotal2 = checkingAmount - amountField;
                    checkingUpdate.setDouble(1,amountTotal2);
                    checkingUpdate.setString(2,acc);
                    LocalDate localDate1 = date.getValue();
                    if (localDate1 != null){
                           Date local = Date.valueOf(localDate1);
                           checkingUpdate.setDate(3,local);
                    }else {
                        throw new IllegalArgumentException("Error");
                    }
                    checkingUpdate.executeUpdate();

                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
    }

    public String hashedPassword(String password){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes){
                sb.append(String.format("%02x",b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void microphoneButt(MouseEvent mouseEvent) {
        if (isListening) {
            isListening = false;
            microphoneTextArea.appendText("Stopped listening.\n");
        } else {
            isListening = true;
            microphoneTextArea.appendText("Listening...\n");
            new Thread(() -> {
                try {
                    File audioFile = new File("recorded.wav");
                    recordAudio(audioFile, 10); // Record for 10 seconds
                    String transcript = transcribeAudio(audioFile);
                    Platform.runLater(() -> processVoiceCommand(transcript));
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> microphoneTextArea.appendText("Voice recognition failed.\n"));
                }
            }).start();
        }
    }
}
