package com.example.jatelobank.ActivityWindow.Reports;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class InvestmentsReportController implements Initializable {
    public TableView tableView;
    public TableColumn colProductId;
    public TableColumn colAccountNumber;
    public TableColumn colValue;
    public TableColumn colProduct;
    public TableColumn colDateInvested;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleDownloadReport(ActionEvent event) {

    }
}
