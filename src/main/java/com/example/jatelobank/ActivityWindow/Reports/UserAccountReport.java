package com.example.jatelobank.ActivityWindow.Reports;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class UserAccountReport implements Initializable {
    public TableView tableView;
    public TableColumn colUserName;
    public TableColumn colAccNo;
    public TableColumn colCheckingAmount;
    public TableColumn colSavingsAmount;
    public TableColumn colBudgetAmount;
    public TableColumn colIncome;
    public TableColumn colExpense;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handleDownloadReport(ActionEvent event) {

    }
}
