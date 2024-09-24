package com.example.jatelobank.ActivityWindow;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

@Component
public class AccountAndCardsController implements Initializable {
    public Label userName;
    public Label checkingPercentage;
    public Label chckingBalance;
    public Label accountNo1;
    public Label userName1;
    public Label savingsPercentage;
    public Label savingsBalance;
    public Label accountNo2;
    public Label userName2;
    public Label budgetPercentage;
    public Label budgetBalance;
    public Label accountNo3;
    public Label userName3;
    public Label creditCardBalance;
    public Label creditCardNo;
    public Label userNameCard1;
    public Label date1;
    public Label creditCardNo2;
    public Label date2;
    public Label debitCardBalance;
    public Label debitCardNo;
    public Label userNameCard2;
    public Label date3;
    public Label debitCardNo2;
    public Label date4;
    public FontAwesomeIconView csvView2Button;
    public FontAwesomeIconView csvView1Button;
    public Label csv1;
    public Label csv2;
    private boolean isMasked;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null){
            //setting the usernames
            userName.setText(currentUser.getUserName());
            userName1.setText(currentUser.getUserName());
            userName2.setText(currentUser.getUserName());
            userName3.setText(currentUser.getUserName());
            userNameCard1.setText(currentUser.getUserName());
            userNameCard2.setText(currentUser.getUserName());

            //setting the account number
            accountNo1.setText(currentUser.getAccNo());
            accountNo2.setText(currentUser.getAccNo());
            accountNo3.setText(currentUser.getAccNo());

            //setting the debit and credit card number
            creditCardNo2.setText(currentUser.getAccNo());
            debitCardNo2.setText(currentUser.getAccNo());

            //setting the balances
            chckingBalance.setText("USD "+ currentUser.getCheckingAmount());
            savingsBalance.setText("USD "+ currentUser.getSavingsAmount());
            budgetBalance.setText("USD "+ currentUser.getBudgetAmount());
            debitCardBalance.setText("USD "+ currentUser.getCheckingAmount());

            //call the method to calculate the savings percentage
            savingsPercentage();

            //call the method to calculate the checking percentage
            checkingPercentage();

            //call the method to calculate the budget percentage
            budgetPercentage();
        }
    }

    //a mouse event to set the debit card csv on pressing the eye icon
    public void csvView2Butt(MouseEvent mouseEvent) {
        
        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();
        User current = SessionManager.getInstance().getCurrentUser();
        if(current != null){
            String acc = current.getAccNo();

            //query the database to get the csv number
            String query = "select csvDebit from CheckingAccount where AccountNumber = '"+acc+"'";

            try {
                PreparedStatement pst = connection1.prepareStatement(query);
                ResultSet rs = pst.executeQuery();
                while (rs.next()){
                    String csv = rs.getString("csvDebit");

                    if (!isMasked){
                        csv2.setText(csv);
                    }else {
                        csv2.setText("***");
                    }
                    isMasked = !isMasked;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //a mouse event to set the credit card csv on pressing the eye icon
    public void csvView1Butt(MouseEvent mouseEvent) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();
        User current = SessionManager.getInstance().getCurrentUser();
        if(current != null){
            String acc = current.getAccNo();

            //query the database to get the csv number
            String query = "select csvCredit from CheckingAccount where AccountNumber = '"+acc+"'";

            try {
                PreparedStatement pst = connection1.prepareStatement(query);
                ResultSet rs = pst.executeQuery();
                while (rs.next()){
                    String csv = rs.getString("csvCredit");

                    if (!isMasked){
                        csv1.setText(csv);
                    }else {
                        csv1.setText("***");
                    }
                    isMasked = !isMasked;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //method to calculate percentage increase in savings
    public void savingsPercentage(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String acc = current.getAccNo();
            //query the savings account to get the amount ,withdrawn and that deposited
            String queryChecking = "select Amount,Deposited,Withdrawn from SavingsAccount where AccountNumber='"+acc+"'";

            try {
                //query the savings account
                PreparedStatement pstSavings = connection1.prepareStatement(queryChecking);
                ResultSet rsSavings = pstSavings.executeQuery();

                while (rsSavings.next()){
                    //get the total amount in savings acc ,withdrawn and deposited
                    double savingsAmount = rsSavings.getDouble("Amount");
                    double depositedAmount = rsSavings.getDouble("Deposited");
                    double withdrawnAmount = rsSavings.getDouble("Withdrawn");

                    DecimalFormat df = new DecimalFormat("0.00");
                    double percentage = (((savingsAmount - withdrawnAmount) + depositedAmount) / savingsAmount) * 100;

                    savingsPercentage.setText(df.format(percentage)+"%");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //method to calculate percentage increase in savings
    public void checkingPercentage(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String acc = current.getAccNo();
            //query the savings account to get the amount ,withdrawn and that deposited
            String queryChecking = "select Amount,Income,Expense from CheckingAccount where AccountNumber='"+acc+"'";

            try {
                //query the savings account
                PreparedStatement pstChecking = connection1.prepareStatement(queryChecking);
                ResultSet rsSavings = pstChecking.executeQuery();

                while (rsSavings.next()){
                    //get the total amount in savings acc ,withdrawn and deposited
                    double checkingAmount = rsSavings.getDouble("Amount");
                    double incomeAmount = rsSavings.getDouble("Income");
                    double expenseAmount = rsSavings.getDouble("Expense");

                    DecimalFormat df = new DecimalFormat("0.00");
                    double percentage = (((checkingAmount - expenseAmount) + incomeAmount) / checkingAmount) * 100;

                    checkingPercentage.setText(df.format(percentage)+"%");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //method to calculate percentage increase in budget amount
    public void budgetPercentage(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String acc = current.getAccNo();
            //query the budget account to get the amount ,withdrawn and that deposited
            String queryChecking = "select Amount,Deposited,Withdrawn from BudgetAccount where AccountNumber='"+acc+"'";

            try {
                //query the budget account
                PreparedStatement pstBudget = connection1.prepareStatement(queryChecking);
                ResultSet rsSavings = pstBudget.executeQuery();

                while (rsSavings.next()){
                    //get the total amount in budget acc ,withdrawn and deposited
                    double budgetAmount = rsSavings.getDouble("Amount");
                    double depositedAmount = rsSavings.getDouble("Deposited");
                    double withdrawnAmount = rsSavings.getDouble("Withdrawn");

                    DecimalFormat df = new DecimalFormat("0.00");
                    double percentage = (((budgetAmount - withdrawnAmount) + depositedAmount) / budgetAmount) * 100;

                    budgetPercentage.setText(df.format(percentage)+"%");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
