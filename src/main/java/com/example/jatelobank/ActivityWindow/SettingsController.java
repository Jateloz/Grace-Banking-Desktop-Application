package com.example.jatelobank.ActivityWindow;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class SettingsController implements Initializable {
    public BorderPane bp2;
    public Label personalInfoButton;
    public Label PassworandSecurityButton;
    public Label NotificationButton;
    public Label ChoosePlanButton;
    public Label userName;
    public ImageView imageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null){
            userName.setText(currentUser.getUserName());
        }

        //load the image into the imageView
        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();
        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String acc = current.getAccNo();

            String query = "select Image from Users where AccountNumber = '"+acc+"'";
            try {
                PreparedStatement ps = connection1.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    Blob img  = rs.getBlob("Image");
                    if (img != null){
                        InputStream inputStream = img.getBinaryStream();
                        Image image = new Image(inputStream);
                        imageView.setImage(image);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //call the personal info window to open on opening the settings tab
        personalInfor();
    }

    public void personalInfoButt(MouseEvent mouseEvent) {

        personalInfor();
    }

    public void PassworandSecurityButt(MouseEvent mouseEvent) {

        passwordAndSecurity();
    }

    public void NotificationButt(MouseEvent mouseEvent) {

        notification();
    }

    public void ChoosePlanButt(MouseEvent mouseEvent) {

        choosePlan();
    }

    public void notification(){
        Parent root = null;

        try {

            root = FXMLLoader.load(getClass().getResource("/Fxml/Settings/Notification.fxml"));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp2.setCenter(root);
    }

    public void personalInfor(){
        Parent root = null;

        try {

            root = FXMLLoader.load(getClass().getResource("/Fxml/Settings/PersonalInfo.fxml"));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp2.setCenter(root);
    }

    public void choosePlan(){
        Parent root = null;

        try {

            root = FXMLLoader.load(getClass().getResource("/Fxml/Settings/ChoosePlan.fxml"));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp2.setCenter(root);
    }

    public void passwordAndSecurity(){
        Parent root = null;

        try {

            root = FXMLLoader.load(getClass().getResource("/Fxml/Settings/PasswordAndSecurity.fxml"));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp2.setCenter(root);
    }
}
