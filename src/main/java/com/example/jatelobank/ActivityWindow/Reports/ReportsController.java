package com.example.jatelobank.ActivityWindow.Reports;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportsController implements Initializable {
    public VBox mainContent;
    public TableView reportTable;
    public TableColumn columnReportName;
    public TableColumn columnDate;
    public TableColumn columnStatus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleDownloadReport(ActionEvent event) {

    }

    public void showTransactionReport(ActionEvent event) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/Activity/Reports/TransactionReport.fxml")));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);

            e.printStackTrace();
        }
        mainContent.getChildren().add(root);
    }

    public void showInvestmentReport(ActionEvent event) {

    }

    public void showUserAccountReport(ActionEvent event) {

    }

    public void showTransfersReport(ActionEvent event) {

    }
}
