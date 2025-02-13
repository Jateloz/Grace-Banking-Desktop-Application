package com.example.jatelobank.ActivityWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ActivityWindow implements Initializable {
    public Button dashboardButton;
    public Button TransferButton;
    public Button TransactionButton;
    public Button AccandCreditsButton;
    public Button InvestmentsButton;
    public Button SettingsButton;
    public Button LogoutButton;
    public BorderPane bp;
    public javafx.scene.layout.VBox VBox;
    public Button financialAdvisoryButton;
    public Button financialLiteracyButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void dashboardButt(ActionEvent event) {

        dashboard();
    }

    public void TransferButt(ActionEvent event) {

        Transfer();
    }

    public void TransactionButt(ActionEvent event) {

        transaction();
    }

    public void AccandCreditsButt(ActionEvent event) {

        accountsAndCards();
    }

    public void InvestmentsButt(ActionEvent event) {

        investments();
    }

    public void SettingsButt(ActionEvent event) {

        settings();
    }

    public void LogoutButt(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/LoginWindow.fxml")));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
            stage.getIcons().add(img);
            stage.setTitle("Grace Bank");
            stage.setResizable(false);
            stage.show();

            Stage stage1 = (Stage) LogoutButton.getScene().getWindow();
            stage1.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void dashboard() {
        Parent root;

        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/Dashboard.fxml")));
        } catch (IOException e) {
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException(e);
        }
        bp.setCenter(root);
    }


    public void Transfer(){

        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/Transfer.fxml")));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);

            e.printStackTrace();
        }
        bp.setCenter(root);
    }

    public void transaction(){
        Parent root = null;
        try {

            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/Transaction.fxml")));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp.setCenter(root);
    }

    public void accountsAndCards(){
        Parent root = null;

        try {

            root =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/AccountAndCards.fxml")));
        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp.setCenter(root);
    }

    public void investments(){
        Parent root = null;

        try {

            root =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/Investments.fxml")));
        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp.setCenter(root);
    }

    public void settings(){
        Parent root = null;

        try {

            root =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/Setting.fxml")));
        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp.setCenter(root);
    }

    @SneakyThrows
    public void financialAdvisoryButt(ActionEvent event) {
        Parent root  = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/FinancialAdvisory.fxml")));
        Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE, (String) null);
        bp.setCenter(root);
    }

    @SneakyThrows
    public void financialLiteracyButt(ActionEvent event) {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/FinancialLiteracy.fxml")));
        Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE, (String) null);
        bp.setCenter(root);
    }
}
