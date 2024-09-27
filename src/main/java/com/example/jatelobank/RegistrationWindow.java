package com.example.jatelobank;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegistrationWindow implements Initializable {
    public TextField FirstName;
    public TextField LastName;
    public TextField Email;
    public TextField PhoneNumber;
    public TextField StreetAddress;
    public TextField ZIPCode;
    public TextField City;
    public TextField AccNumber;
    public PasswordField Password;
    public Button RegisterButton;
    public Label label;
    public Label BackButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void RegisterButt(ActionEvent event) throws SQLException, NoSuchAlgorithmException, IOException {
        
        if (FirstName.getText().isEmpty()) {
            label.setText("First Name field cannot be empty");

        } else if (!FirstName.getText().matches("[a-zA-Z]+")) {
            label.setText("Name can only contain letters only");

        } else if (LastName.getText().isEmpty()) {
            label.setText("Last name field cannot be empty");

        } else if (!LastName.getText().matches("[a-zA-Z]+")) {
            label.setText("Invalid name");

        } else if (Email.getText().isEmpty()) {
            label.setText("Email field cannot be empty");

        } else if (!emailValidate(Email.getText())) {
            label.setText("Invalid email");

        } else if (PhoneNumber.getText().isEmpty()) {
            label.setText("Phone number field cannot be empty");

        } else if (!PhoneNumber.getText().matches("\\d+")) {
            label.setText("Invalid phone number");

        } else if (PhoneNumber.getText().length() != 10 || !PhoneNumber.getText().startsWith("07")) {
            label.setText("Invalid phone number");

        } else if (StreetAddress.getText().isEmpty()) {
            label.setText("Street address field cannot be empty");

        } else if (!StreetAddress.getText().matches("^\\d{3,},[a-zA-Z]+$")) {
            label.setText("Invalid street address syntax");

        } else if (ZIPCode.getText().isEmpty()) {
            label.setText("ZIP code field cannot be empty");

        } else if (!ZIPCode.getText().matches("\\d+") || ZIPCode.getText().length() != 5) {
            label.setText("Invalid ZIP code");

        } else if (City.getText().isEmpty()) {
            label.setText("City field cannot be empty");

        } else if (!City.getText().matches("[a-zA-Z]+")) {
            label.setText("Invalid City");

        } else if (AccNumber.getText().isEmpty()) {
            label.setText("Account number field cannot be empty");

        } else if (AccNumber.getText().length() != 16 || !AccNumber.getText().startsWith("1100")) {
            label.setText("Account number must start with 1100 and be 16 numbers long");

        } else if (Password.getText().isEmpty()) {
            label.setText("Password field cannot be empty");

        }else {

            DatabaseConnection connection = new DatabaseConnection();
            Connection connection1 = connection.getConn();
            String query = "select * from Users where AccountNumber = ?";


            PreparedStatement preparedStatement  = connection1.prepareStatement(query);
            preparedStatement.setString(1,AccNumber.getText());

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()){
                String acc = rs.getString("AccountNumber");

                if (AccNumber.getText().equals(acc)){
                    label.setText("Account number already exists and has been taken");

                }
            } else {

                registerUser();

                FirstName.clear();
                LastName.clear();
                Email.clear();
                PhoneNumber.clear();
                StreetAddress.clear();
                ZIPCode.clear();
                City.clear();
                AccNumber.clear();
                Password.clear();
                label.setText("");

                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/LoginWindow.fxml")));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
                stage.setTitle("Grace Bank");
                stage.getIcons().add(img);
                stage.setResizable(false);
                stage.show();

                Stage stage1 ;
                stage1 = (Stage) RegisterButton.getScene().getWindow();
                stage1.close();

                }
            }
        }

    public void registerUser() throws SQLException, NoSuchAlgorithmException {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();

        String query = "insert into Users (FirstName,LastName,Email,PhoneNumber,StreetAddress,ZIPCode,City,AccountNumber,Password) values (?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = connection1.prepareStatement(query);
            ps.setString(1, FirstName.getText());
            ps.setString(2, LastName.getText());
            ps.setString(3, String.valueOf(emailValidate(Email.getText())));
            ps.setString(4, PhoneNumber.getText());
            ps.setString(5, StreetAddress.getText());
            ps.setString(6, ZIPCode.getText());
            ps.setString(7, City.getText());
            ps.setString(8, AccNumber.getText());
            ps.setString(9, hashPassword(Password.getText()));

            ps.executeUpdate();

    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes){
            sb.append(String.format("%02x",b));
        }
        return sb.toString();
    }

    public boolean emailValidate(String email){
        String EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

        Pattern pattern = Pattern.compile(EMAIL);
        Matcher matcher = pattern.matcher(email);

        boolean match = matcher.matches();
        return match;
    }

    public void BackButt(MouseEvent mouseEvent) throws IOException {
        Parent root;

        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/LoginWindow.fxml")));
        Stage stage = new Stage();
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);
        stage.setTitle("Grace Bank");
        stage.setScene(new Scene(root));
        stage.show();

        Stage stage1 = (Stage) BackButton.getScene().getWindow();
        stage1.close();
    }
}