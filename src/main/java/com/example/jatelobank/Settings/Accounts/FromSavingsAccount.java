package com.example.jatelobank.Settings.Accounts;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Component
public class FromSavingsAccount implements Initializable {
    public TextField accNumber;
    public TextField amount;
    public DatePicker date;
    public PasswordField password;
    public Label label;
    public Button transferButton;
    public DatabaseConnection connection = new DatabaseConnection();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
                String query = "select Amount from SavingsAccount where AccountNumber = '"+acc+"'";

                while (passwordRetrieval.next()){

                    //retrieve password from users
                    String pass = passwordRetrieval.getString("Password");

                    //retrieving amount from savings account
                    PreparedStatement ps = connection1.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()){
                        //retrieve amount from checking account
                        double savingAmount = rs.getDouble("Amount");

                        if (accNumber.getText().isEmpty()){
                            label.setText("Account number cannot be empty");
                        } else if (!accNumber.getText().equals(acc)) {
                            label.setText("Invalid account number");
                        } else if (amount.getText().isEmpty()) {
                            label.setText("Enter amount to transfer");
                        } else if (Double.parseDouble(amount.getText()) > savingAmount) {
                            label.setText("Insufficient account balance to make the transfer of "+amount.getText());
                        } else if (savingAmount <= 0) {
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
                            label.setText("Transferred  successfully to your checking account!");
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

            //retrieve amount from savings  acc first
            String query = "select Amount from SavingsAccount where AccountNumber = '"+acc+"'";

            //retrieve amount from  Checking  acc
            String queryRetrieve = "select Amount from CheckingAccount where AccountNumber = '"+acc+"'";

            //retrieve income from checking  acc
            String queryIncome = "select Income from CheckingAccount where AccountNumber = '"+acc+"'";

            //retrieve withdrawn from savings acc
            String queryWithdrawn = "select Withdrawn from SavingsAccount where AccountNumber = '"+acc+"'";

            //update the savings account
            String updateBudget = "update SavingsAccount set Amount = ?,Date = ?,Withdrawn = ? where AccountNumber = '" +acc+ "'";

            //update the checking account
            String updateChecking = "update CheckingAccount set Amount = ?,Acc = ?,Date = ? where AccountNumber = '"+acc+"'";

            //insert data into toSavingsAccount
            String FromSavings = "insert into FromSavingsAccount (AccountNumber,Amount,Date) values (?,?,?)";

            try {

                //retrieving amount from savings account
                PreparedStatement ps = connection1.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                //retrieving amount from checking account
                PreparedStatement retrieveSavingsAmount = connection1.prepareStatement(queryRetrieve);
                ResultSet rsRetrieveCheckingAmount = retrieveSavingsAmount.executeQuery();

                //retrieve income from checking account
                PreparedStatement getExpe = connection1.prepareStatement(queryIncome);
                ResultSet rsIncome = getExpe.executeQuery();

                //retrieve withdrawn from savings account
                PreparedStatement retrieveWithdrawn = connection1.prepareStatement(queryWithdrawn);
                ResultSet withdraw = retrieveWithdrawn.executeQuery();

                //insert the data into fromSavingsAccount
                PreparedStatement fromSave = connection1.prepareStatement(FromSavings);
                fromSave.setString(1,acc);
                fromSave.setDouble(2, Double.parseDouble(amount.getText()));
                LocalDate localDate3 = date.getValue();
                if (localDate3 != null){
                    Date local = Date.valueOf(localDate3);
                    fromSave.setDate(3,local);
                }else {
                    throw new IllegalArgumentException("Error");
                }
                fromSave.executeUpdate();


                while (rs.next() &&  rsRetrieveCheckingAmount.next() && rsIncome.next() && withdraw.next()){
                    //retrieve amount from savings account
                    double savingsAmount = rs.getDouble("Amount");

                    //retrieve amount from checking account
                    double checkingAmount = rsRetrieveCheckingAmount.getDouble("Amount");

                    //retrieve withdrawn
                    double withdrawn = withdraw.getDouble("Withdrawn");



                    //get amount field and change to double
                    double amountField = Double.parseDouble(amount.getText());

                    //updating savings account
                    double amountTotal1 = savingsAmount - amountField;
                    double totalWithdrawn = amountField + withdrawn;
                    PreparedStatement psUpdateBudget = connection1.prepareStatement(updateBudget);
                    psUpdateBudget.setDouble(1,amountTotal1);
                    LocalDate localDate = date.getValue();
                    if (localDate != null){
                        Date local = Date.valueOf(localDate);
                        psUpdateBudget.setDate(2,local);
                    }else {
                        throw new IllegalArgumentException("Error");
                    }
                    psUpdateBudget.setDouble(3,totalWithdrawn);
                    psUpdateBudget.executeUpdate();

                    //update checking account
                    PreparedStatement checkingUpdate = connection1.prepareStatement(updateChecking);
                    double amountTotal2 = checkingAmount + amountField;
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
}
