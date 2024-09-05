package com.example.jatelobank.Settings.Accounts;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.LocalDate;

@Component
public class OtherAccount {
    public TextField accNoToBeSent;
    public TextField amount;
    public DatePicker dateButton;
    public TextField transferPurpose;
    public TextField beneficiaryName;
    public TextField beneficiaryEmail;
    public TextField payerReference;
    public TextArea informationToBeneficiary;
    public Label label;
    public Button continueButton;

    public void continueButt(ActionEvent event) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){

            String acc = current.getAccNo();
            //query to get the account number sending the money
            String query1 = "select Amount  from CheckingAccount where AccountNumber='" + acc + "'";

            //query to get the account number
            String getAccounNumbers = "select AccountNumber from CheckingAccount";

            try {
                //selecting amount from Checking account
                PreparedStatement pst = connection1.prepareStatement(query1);
                ResultSet rs = pst.executeQuery();

                //getting the account numbers
                PreparedStatement getAccounts = connection1.prepareStatement(getAccounNumbers);
                ResultSet getRST = getAccounts.executeQuery();

                if (rs.next() && getRST.next()){

                    //retrieve amount
                    double amountField = Double.parseDouble(rs.getString("Amount"));

                    //get the account numbers
                    String accountNumbers = getRST.getString("AccountNumber");

                    if (accNoToBeSent.getText().isEmpty()){
                        label.setText("Please enter the account number to receive the money");

                    } else if (accNoToBeSent.getText().equals(acc)) {
                        label.setText("You cannot transfer money into your own account");

                    }/** else if (!accountNumbers.equals(accNoToBeSent.getText())) {
                        label.setText("Account number does not exist!");

                    }**/ else if (amount.getText().isEmpty()){
                        label.setText("Please enter the amount to be sent");

                    } else if (amountField <= 0) {
                        label.setText("You have insufficient funds in your account");

                    } else if (Double.parseDouble(amount.getText()) < Double.parseDouble(amount.getText())) {
                        label.setText("Your account balance is " + current.getCheckingAmount() + " , so you cannot transfer " + Double.parseDouble(amount.getText()));

                    } else if (dateButton.getEditor().getText().isEmpty()) {
                        label.setText("Select the date");

                    } else if (transferPurpose.getText().isEmpty()) {
                        label.setText("Please enter the purpose of the transfer");

                    } else if (beneficiaryName.getText().isEmpty()) {
                        label.setText("Enter the beneficiary's name");

                    } else if (beneficiaryEmail.getText().isEmpty()) {
                        label.setText("Enter the beneficiary's email address");

                    } else if (payerReference.getText().isEmpty()) {
                        label.setText("Input the payer reference number");

                    } else if (informationToBeneficiary.getText().isEmpty()){
                        label.setText("Enter the information to be sent to beneficiary");

                    }else if (!getRST.next()) {
                        label.setText("Account does not exists,please enter a valid account number");

                    } else {

                        transfer();
                        accNoToBeSent.clear();
                        amount.clear();
                        dateButton.getEditor().clear();
                        transferPurpose.clear();
                        beneficiaryName.clear();
                        beneficiaryEmail.clear();
                        payerReference.clear();
                        informationToBeneficiary.clear();
                        label.setText("Transaction successful!");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void transfer() {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null) {
            String acc = current.getAccNo();

            //query for the account making the transfer
            String query = "update CheckingAccount set Amount=?,Acc=?,AmountSent=?,Date=?,TransferPurpose=?,BeneficiaryName=?,BeneficiaryEmail=?,PayersReference=?,InformationToBeneficiary=?,Expense=?  where AccountNumber='" + acc + "'";

            //query for the account receiving the money
            String accountReceivingMoney = "update CheckingAccount set Amount=?,Acc=?,AmountSent=?,Date=?,TransferPurpose=?,BeneficiaryName=?,BeneficiaryEmail=?,PayersReference=?,InformationToBeneficiary=?,Income=?  where AccountNumber=?";

            //query to get the account number sending the money
            String query1 = "select Amount  from CheckingAccount where AccountNumber='" + acc + "'";

            //query to get the income and expense from account sending and receiving the money
            String querySend = "select Expense  from CheckingAccount where AccountNumber='" + acc + "'";
            String queryReceive = "select Amount,Income  from CheckingAccount where AccountNumber=?";

            //query to fill the revenue table on transfer
            String rev = "insert into Revenue (AccountNumber,Revenue,Date) values (?,?,?)";

            //query to fill the expense table on transfer
            String exp = "insert into Expense (AccountNumber,Expense,Date) values (?,?,?)";


            try {

                //get the amount in the text field and convert it to double type
                double amount2 = Double.parseDouble(amount.getText());

                //selecting amount from Checking account
                PreparedStatement pst = connection1.prepareStatement(query1);
                ResultSet rs = pst.executeQuery();

                //selecting Expense from Checking Account
                PreparedStatement pst1 = connection1.prepareStatement(querySend);
                ResultSet rs1 = pst1.executeQuery();

                //selecting income from Checking account
                PreparedStatement pstReceiv = connection1.prepareStatement(queryReceive);
                pstReceiv.setString(1, accNoToBeSent.getText());
                ResultSet rstReceive = pstReceiv.executeQuery();

                //update revenue table
                PreparedStatement reve = connection1.prepareStatement(rev);
                reve.setString(1,accNoToBeSent.getText());
                reve.setDouble(2,amount2);
                LocalDate local1 = dateButton.getValue();
                if (local1 != null){
                    Date dat = Date.valueOf(local1);
                    reve.setDate(3,dat);
                }
                reve.executeUpdate();

                //update expense table
                PreparedStatement expe = connection1.prepareStatement(exp);
                expe.setString(1,accNoToBeSent.getText());
                expe.setDouble(2,amount2);
                LocalDate local2 = dateButton.getValue();
                if (local2 != null){
                    Date dat = Date.valueOf(local2);
                    expe.setDate(3,dat);
                }
                expe.executeUpdate();

                if (rs.next() && rs1.next() && rstReceive.next()) {
                    //retrieve amount
                    double amountField = Double.parseDouble(rs.getString("Amount"));

                    //retrieve expense of the person sending the money
                    double expense = rs1.getDouble("Expense");

                    //retrieve income and Amount of the person receiving the money
                    double income = rstReceive.getDouble("Income");
                    double amount0 = rstReceive.getDouble("Amount");


                    //expense
                    double expens = expense + Double.parseDouble(amount.getText());
                    double amt = amountField - expens;


                    //updating the account for the account sending the money
                    PreparedStatement ps = connection1.prepareStatement(query);
                    ps.setDouble(1, amt);
                    ps.setString(2, accNoToBeSent.getText());
                    ps.setDouble(3, amount2);
                    LocalDate localDate = dateButton.getValue();
                    if (localDate != null) {
                        Date date = Date.valueOf(localDate);
                        ps.setDate(4, date);
                    } else {
                        throw new IllegalArgumentException("Please select a valid date");

                    }
                    ps.setString(5, transferPurpose.getText());
                    ps.setString(6, beneficiaryName.getText());
                    ps.setString(7, beneficiaryEmail.getText());
                    ps.setString(8, payerReference.getText());
                    ps.setString(9, informationToBeneficiary.getText());
                    ps.setDouble(10, expens);
                    ps.executeUpdate();

                    //updating the account receiving the money
                    PreparedStatement pstReceive = connection1.prepareStatement(accountReceivingMoney);

                    //income to be added to the database
                    double incom = Double.parseDouble(amount.getText()) + income;
                    double amountTotal = amount0 + incom;

                    pstReceive.setDouble(1, amountTotal);
                    pstReceive.setString(2, acc);
                    pstReceive.setDouble(3, amount2);
                    LocalDate localDate1 = dateButton.getValue();
                    if (localDate1 != null) {
                        Date date = Date.valueOf(localDate1);
                        pstReceive.setDate(4, date);
                    } else {
                        throw new IllegalArgumentException("Error");
                    }
                    pstReceive.setString(5, transferPurpose.getText());
                    pstReceive.setString(6, beneficiaryName.getText());
                    pstReceive.setString(7, beneficiaryEmail.getText());
                    pstReceive.setString(8, payerReference.getText());
                    pstReceive.setString(9, informationToBeneficiary.getText());
                    pstReceive.setDouble(10, incom);
                    pstReceive.setString(11, accNoToBeSent.getText());
                    pstReceive.executeUpdate();
                    }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
