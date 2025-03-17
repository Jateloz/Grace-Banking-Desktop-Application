package com.example.jatelobank.Settings.Accounts;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.vosk.LibVosk;
import org.vosk.Model;
import org.vosk.Recognizer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Component
public class ToBudgetAccount implements Initializable {
    public TextField accNumber;
    public TextField amount;
    public DatePicker date;
    public PasswordField password;
    public Label label;
    public Button transferButton;
    public DatabaseConnection connection = new DatabaseConnection();
    public FontAwesomeIconView microphoneButton;
    public TextArea microphoneTextArea;
    private Recognizer recognizer;
    private boolean isListening = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeVosk();
    }

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
                            label.setText("Transferred  successfully to your budget account!");
                        }
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void transfer(){

        Connection connection1 = connection.getConn();

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null) {

            String acc = current.getAccNo();

            //retrieve amount from  checking acc first
            String query = "select Amount from CheckingAccount where AccountNumber = '"+acc+"'";


            //retrieve amount from  budget  acc
            String queryRetrieve = "select Amount from BudgetAccount where AccountNumber = '"+acc+"'";

            //retrieve deposit from Savings acc
            String queryDeposited = "select Deposited from BudgetAccount where AccountNumber = '"+acc+"'";

            //update the savings account
            String updateSavings = "update BudgetAccount set Amount = ?,Date = ?,Deposited = ? where AccountNumber = '" +acc+ "'";


            //update the checking account
            String updateChecking = "update CheckingAccount set Amount = ?,Acc = ?,Date = ? where AccountNumber = '"+acc+"'";

            //insert data into toBudgetAccount
            String toBudget = "insert into ToBudgetAccount (AccountNumber,Amount,Date) values (?,?,?)";

            try {

                //retrieving amount from checking account
                PreparedStatement ps = connection1.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                //retrieving amount from budget account
                PreparedStatement retrieveSavingsAmount = connection1.prepareStatement(queryRetrieve);
                ResultSet rsRetrieveSavingsAmount = retrieveSavingsAmount.executeQuery();


                //retrieve deposits from budget account
                PreparedStatement retrieveDeposits = connection1.prepareStatement(queryDeposited);
                ResultSet deposits = retrieveDeposits.executeQuery();

                //insert the dat into toBudgetAccount
                PreparedStatement toBudge = connection1.prepareStatement(toBudget);
                toBudge.setString(1,acc);
                toBudge.setDouble(2, Double.parseDouble(amount.getText()));
                LocalDate localDate3 = date.getValue();
                if (localDate3 != null){
                    Date local = Date.valueOf(localDate3);
                    toBudge.setDate(3,local);
                }else {
                    throw new IllegalArgumentException("Error");
                }
                toBudge.executeUpdate();


                while (rs.next() &&  rsRetrieveSavingsAmount.next() && deposits.next()){
                    //retrieve amount from checking account
                    double checkingAmount = rs.getDouble("Amount");

                    //retrieve amount from budget account
                    double budgetAmount = rsRetrieveSavingsAmount.getDouble("Amount");

                    //retrieve deposits
                    double deposit = deposits.getDouble("Deposited");



                    //get amount field and change to double
                    double amountField = Double.parseDouble(amount.getText());

                    //updating budget account
                    double amountTotal1 = amountField + budgetAmount;
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
        if (isListening){
            isListening = false;
            microphoneTextArea.appendText("Stopped listening.\n");
        }else {
            isListening = true;
            microphoneTextArea.appendText("Listening...\n");

            startVoiceRecognition();
        }
    }

    //speech recognition methods
    @SneakyThrows
    private void initializeVosk(){
        try {
            Model model = new Model("src/main/resources/vosk-model/vosk-model-small-en-us-0.15");
            recognizer = new Recognizer(model,16000);
        }catch (IOException e){
            e.printStackTrace();
            Platform.runLater(() ->
                    microphoneTextArea.appendText("Error initializing vosk.\n")
                    );
        }
    }

    private void startVoiceRecognition(){
        new Thread(() -> {
            try {
                AudioFormat format = new AudioFormat(16000,16,1,true,false);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class,format);
                TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();

                byte[] buffer = new byte[4096];
                while (isListening){
                    int bytesRead = line.read(buffer,0,buffer.length);
                    if (bytesRead > 0 && recognizer.acceptWaveForm(buffer,bytesRead)){
                        String result = recognizer.getResult();
                        Platform.runLater(() ->
                                processVoiceCommand(result)
                        );
                    }
                }

                /**
                while (isListening && line.read(buffer,0,buffer.length) > 0){
                    if(recognizer.acceptWaveForm(buffer,buffer.length)){
                        String result = recognizer.getResult();

                        Platform.runLater(() ->
                                processVoiceCommand(result)
                                );
                    }
                }**/

                line.stop();
                line.close();
            }catch (Exception e){
                e.printStackTrace();
                Platform.runLater(() ->
                        microphoneTextArea.appendText("Error during recognition.\n")
                        );
            }
        }).start();

    }

    private void processVoiceCommand(String command) {
        command = command.toLowerCase().trim();
        if (command.startsWith("account number")){
            String account = command.replaceFirst("account number","").trim();
            Platform.runLater(() ->
                    accNumber.setText(account));
        } else if (command.startsWith("amount")) {
            String amount2 = command.replaceFirst("amount","").trim();
            Platform.runLater(() ->
                    amount.setText(amount2));
        } else if (command.startsWith("password")) {
            String pass = command.replaceFirst("password","").trim();
            Platform.runLater(() ->
                    password.setText(pass));
        } else if (command.startsWith("done")) {
            commitTransaction();
        }
    }

    private void commitTransaction() {
        isListening = false;
        Platform.runLater(() ->{
            String account = accNumber.getText();
            String amount3 = amount.getText();
            String pswd = password.getText();

            microphoneTextArea.appendText("Transaction commited! \n");
            microphoneTextArea.appendText("Account: "+account+" , Amount: "+amount3+"Password: "+pswd+"\n");

            //carrying out transaction
            transfer();
        });
    }
}
