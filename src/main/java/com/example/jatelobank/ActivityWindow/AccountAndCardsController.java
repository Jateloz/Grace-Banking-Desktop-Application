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
    private final DatabaseConnection connection = new DatabaseConnection();
    private final Connection connection1 = connection.getConn();


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
        }
    }

    public void csvView2Butt(MouseEvent mouseEvent) {

        if (!isMasked){
            csv2.setText("784");
        }else {
            csv2.setText("***");
        }
        isMasked = !isMasked;
    }

    public void csvView1Butt(MouseEvent mouseEvent) {
        if (!isMasked){
            csv1.setText("123");
        }else {
            csv1.setText("***");
        }
        isMasked = !isMasked;
    }
}
