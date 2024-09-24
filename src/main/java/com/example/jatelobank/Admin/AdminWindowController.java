package com.example.jatelobank.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminWindowController implements Initializable {
    public Button logoutButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void logoutButt(ActionEvent event) {
        Parent root;
        try{
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/LoginWindow.fxml")));
            Stage stage = new Stage();
            stage.setTitle("Grace Bank");
            javafx.scene.image.Image img = new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
            stage.getIcons().add(img);
            stage.setScene(new Scene(root));
            stage.show();

            Stage stage1 = (Stage) logoutButton.getScene().getWindow();
            stage1.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
