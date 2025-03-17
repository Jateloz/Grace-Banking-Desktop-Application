package com.example.jatelobank.ActivityWindow.Reports;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionReportController implements Initializable {
    public TableView reportTableView;
    public TableColumn transactionIdColumn;
    public TableColumn dateColumn;
    public TableColumn amountColumn;
    public TableColumn accountNumberColumn;
    public TableColumn transactionTypeColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleDownloadReport(ActionEvent event) {

    }
}
