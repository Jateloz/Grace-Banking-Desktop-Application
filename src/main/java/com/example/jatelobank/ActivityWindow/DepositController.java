package com.example.jatelobank.ActivityWindow;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DepositController implements Initializable {
    public TextField depositAmount;
    public Button cancelButton;
    public Button depositButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void cancelButt(ActionEvent event) {
        depositAmount.setText("");
    }

    public void depositButt(ActionEvent event) {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null) {

            String acc = current.getAccNo();


            String query = "select * from checkingAccount where AccountNumber='"+acc+"'";
            //update the checking account
            String updateChecking = "update CheckingAccount set Amount = ? where AccountNumber = '" + acc + "'";


            try {
                Statement stm = connection1.createStatement();
                ResultSet rst = stm.executeQuery(query);
                while (rst.next()){
                    double amount = rst.getDouble("Amount");
                    double amount2 = Double.parseDouble(depositAmount.getText());

                    //update checking account
                    PreparedStatement checkingUpdate = connection1.prepareStatement(updateChecking);
                    double amountTotal = amount + amount2;
                    checkingUpdate.setDouble(1, amountTotal);
                    checkingUpdate.executeUpdate();

                    //clear the text field after transaction is successful
                    depositAmount.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
    }
}
