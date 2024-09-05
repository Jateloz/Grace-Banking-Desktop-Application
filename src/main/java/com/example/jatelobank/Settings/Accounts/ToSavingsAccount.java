package com.example.jatelobank.Settings.Accounts;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;

@Component
public class ToSavingsAccount {
    public TextField accNumber;
    public TextField amount;
    public DatePicker date;
    public PasswordField password;
    public Label label;
    public Button transferButton;
    public DatabaseConnection connection = new DatabaseConnection();

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

    public void transfer(){

        Connection connection1 = connection.getConn();

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null) {

            String acc = current.getAccNo();

            //retrieve amount from  checking acc first
            String query = "select Amount from CheckingAccount where AccountNumber = '"+acc+"'";


            //retrieve amount from  savings  acc
            String queryRetrieve = "select Amount from SavingsAccount where AccountNumber = '"+acc+"'";


            //retrieve expense from checking  acc
            String queryExpense = "select Expense from CheckingAccount where AccountNumber = '"+acc+"'";


            //retrieve deposit from Savings acc
            String queryDeposited = "select Deposited from SavingsAccount where AccountNumber = '"+acc+"'";

            //update the savings account
            String updateSavings = "update SavingsAccount set Amount = ?,Date = ?,Deposited = ? where AccountNumber = '" +acc+ "'";


            //update the checking account
            String updateChecking = "update CheckingAccount set Amount = ?,Acc = ?,Date = ?,Expense = ? where AccountNumber = '"+acc+"'";

            try {

                //retrieving amount from checking account
                PreparedStatement ps = connection1.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                //retrieving amount from savings account
                PreparedStatement retrieveSavingsAmount = connection1.prepareStatement(queryRetrieve);
                ResultSet rsRetrieveSavingsAmount = retrieveSavingsAmount.executeQuery();

                //retrieve expense from checking account
                PreparedStatement getExpe = connection1.prepareStatement(queryExpense);
                ResultSet rsExpense = getExpe.executeQuery();

                //retrieve deposits from savings account
                PreparedStatement retrieveDeposits = connection1.prepareStatement(queryDeposited);
                ResultSet deposits = retrieveDeposits.executeQuery();


                while (rs.next() &&  rsRetrieveSavingsAmount.next() && rsExpense.next() && deposits.next()){
                    //retrieve amount from checking account
                    double checkingAmount = rs.getDouble("Amount");

                    //retrieve amount from savings account
                    double savingAmount = rsRetrieveSavingsAmount.getDouble("Amount");

                    //retrieve expense from checking
                    double expenseAmount = rsExpense.getDouble("Expense");

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
                    double expense = expenseAmount + amountField;
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
                    checkingUpdate.setDouble(4,expense);
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
