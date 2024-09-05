package com.example.jatelobank.ActivityWindow;

import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TransferController implements Initializable {
    public Label userName;
    public Label balance;
    public Label accountNumber;
    public Label userNam;
    public BorderPane bp;
    public Label savingAccButton;
    public Label budgetAccButton;
    public Label otherAccButton;
    public Label fromSavingAccButton;
    public Label fromBudgetAccButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null){
            userName.setText(currentUser.getUserName());
            userNam.setText(currentUser.getUserName());
            accountNumber.setText(currentUser.getAccNo());
            balance.setText("USD "+ currentUser.getCheckingAmount());
        }

        //load the savings of the border pane on opening the transfer window
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/Settings/Accounts/ToSavingsAccount.fxml")));
        } catch (IOException e) {
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            throw new RuntimeException(e);
        }
        bp.setCenter(root);
    }

    public void savingAccButt(MouseEvent mouseEvent) {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/Settings/Accounts/ToSavingsAccount.fxml")));
        } catch (IOException e) {
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            throw new RuntimeException(e);
        }
        bp.setCenter(root);
    }

    public void budgetAccButt(MouseEvent mouseEvent) {

        Parent root = null;
        try {
            root  = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/Settings/Accounts/ToBudgetAccount.fxml")));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp.setCenter(root);
    }

    public void otherAccButt(MouseEvent mouseEvent) {

        Parent root = null;
        try {
            root  = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/Settings/Accounts/OtherAccount.fxml")));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp.setCenter(root);
    }

    public void fromSavingAccButt(MouseEvent mouseEvent) {

        Parent root = null;
        try {
            root  = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/Settings/Accounts/FromSavingsAccount.fxml")));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp.setCenter(root);
    }

    public void fromBudgetAccButt(MouseEvent mouseEvent) {

        Parent root = null;
        try {
            root  = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/Settings/Accounts/FromBudgetAccount.fxml")));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp.setCenter(root);
    }
}
