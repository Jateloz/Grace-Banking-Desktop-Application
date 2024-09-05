package com.example.jatelobank.Settings;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

@Component
public class PasswordsAndSecurityController implements Initializable {
    public TextField pet;
    public TextField dad;
    public TextField mum;
    public TextField hint;
    public PasswordField newPassword1;
    public PasswordField newPassword2;
    public Text label;
    public Button saveButton;
    public PasswordField oldPassword;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void saveButt(ActionEvent event) {
        if (oldPassword.getText().isEmpty()){
            label.setText("Please input your old password");

        } else if (newPassword1.getText().isEmpty()) {
            label.setText("Please input yor new password");

        } else if (newPassword2.getText().isEmpty()) {
            label.setText("please re-enter your new password");

        } else if (!newPassword1.getText().equals(newPassword2.getText())) {
            label.setText("The passwords do no match");

        }else {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connection1 = connection.getConn();

            User currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser != null){
                String acc = currentUser.getAccNo();
                String query = "Select Password from Users where AccountNumber = ?";
                try {
                    PreparedStatement ps = connection1.prepareStatement(query);
                    ps.setString(1,acc);

                    ResultSet rs = ps.executeQuery();
                    if (rs.next()){
                        String pass = rs.getString("Password");
                        if (!hashedPassword(oldPassword.getText()).equals(pass)){
                            label.setText("Input the correct old password");
                        }else {
                            saveNewPassword();
                            label.setText("Password updated successfully");
                            oldPassword.clear();
                            newPassword1.clear();
                            newPassword2.clear();
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }

    public void saveNewPassword(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();

        String query = "update Users set Password = ? where AccountNumber = ?";
        try {

            User currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser != null){
                String acc = currentUser.getAccNo();
                String password = hashedPassword(newPassword2.getText());
                PreparedStatement ps = connection1.prepareStatement(query);
                ps.setString(1,password);
                ps.setString(2,acc);
                ps.executeUpdate();

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hashedPassword(String password){
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
