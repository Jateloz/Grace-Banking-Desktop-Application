package com.example.jatelobank;

import com.example.jatelobank.ActivityWindow.ActivityWindow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
public class LoginWindow implements Initializable {
    @FXML
    public TextField AccNumberField;
    @FXML
    public javafx.scene.control.PasswordField PasswordField;
    @FXML
    public Button LoginButton;
    @FXML
    public Hyperlink RegisterButton;
    @FXML
    public AnchorPane ap;
    @FXML
    public Label label;



    DatabaseConnection connection = new DatabaseConnection();
    public FontAwesomeIconView AdminButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    //when pressed take us to the home window
    @SneakyThrows
    public void LoginButt(ActionEvent event){

        if (PasswordField.getText().isEmpty() || AccNumberField.getText().isEmpty()){
            label.setText("Please enter the credentials");

        }else {
            validateLogins();

            //load the activity window with the data
            loadDashboard();
            // Close the login window
            Stage stage1 = (Stage) LoginButton.getScene().getWindow();
            stage1.close();


        }
    }

    //takes us to the registration window
    @SneakyThrows
    public void RegisterButt(ActionEvent event) {

            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/RegistrationWindow.fxml")));
            Stage stage = new Stage();
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
            stage.getIcons().add(img);
            stage.setScene(new Scene(root));
            stage.setTitle("Grace Bank");
            stage.setResizable(true);
            stage.show();

            //close the previous stage
            Stage stage1;
            stage1 = (Stage) RegisterButton.getScene().getWindow();
            stage1.close();
    }

    //a method to validate the logins
    @SneakyThrows
    public void validateLogins(){
        Connection connection1 = connection.getConn();

        // retrieve hashed password SHA-256
        String password = hashedPassword(PasswordField.getText());

        String query = "select * from Users where AccountNumber = ? and Password = ?";
        String query1 = "select * from CheckingAccount where AccountNumber = ?";
        String query2 = "select * from SavingsAccount where AccountNumber = ?";
        String query3 = "select * from BudgetAccount where AccountNumber = ?";

        //select accNo and password
        PreparedStatement ps = connection1.prepareStatement(query);
        ps.setString(1, AccNumberField.getText());
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        //select * from checking acc
        PreparedStatement pst = connection1.prepareStatement(query1);
        pst.setString(1,AccNumberField.getText());
        ResultSet rst = pst.executeQuery();

        //select * from savings acc
        PreparedStatement pst2 = connection1.prepareStatement(query2);
        pst2.setString(1,AccNumberField.getText());
        ResultSet rst2 = pst2.executeQuery();

        //select * from budget acc
        PreparedStatement pst3 = connection1.prepareStatement(query3);
        pst3.setString(1,AccNumberField.getText());
        ResultSet rst3 = pst3.executeQuery();

        if (rs.next() && rst.next() && rst2.next() && rst3.next()) {
            String username = rs.getString("FirstName");
            String username2 = rs.getString("LastName");
            String name = username + " "+ username2;
            String acc = rs.getString("AccountNumber");

            double CheckingAmount = Double.parseDouble(rst.getString("Amount"));
            double SavingsAmount = Double.parseDouble(rst2.getString("Amount"));
            double BudgetAmount = Double.parseDouble(rst3.getString("Amount"));

            //get the income and expense from the Checking acc
            double income = rst.getDouble("Income");
            double expense = rst.getDouble("Expense");

            //call the session manager class for login
            SessionManager.getInstance().login(new User(name,acc,CheckingAmount,SavingsAmount,BudgetAmount,income,expense));

        } else {
            label.setText("Login failed");
        }

    }

    //hash the password method
    @SneakyThrows
    public static String hashedPassword(String password){

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : bytes){
                sb.append(String.format("%02x",b));
            }
            return sb.toString();

    }

    //press the bank icon to go to the admin side
    @SneakyThrows
    public void AdminButt(MouseEvent mouseEvent){

        Parent root;

        root = FXMLLoader.load(getClass().getResource("/Fxml/Admin/AdminWindow.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        javafx.scene.image.Image img = new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.setTitle("Grace Bank");
        stage.getIcons().add(img);
        stage.show();

        Stage stage1 = (Stage) AdminButton.getScene().getWindow();
        stage1.close();
    }

    //load the dashboard in the activity window class
    @SneakyThrows
    private void loadDashboard(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Activity/Activity.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        javafx.scene.image.Image img = new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);

        // Fetch data and update dashboard
        ActivityWindow activityWindow = fxmlLoader.getController();
        activityWindow.dashboard();

        stage.show();
    }
}
