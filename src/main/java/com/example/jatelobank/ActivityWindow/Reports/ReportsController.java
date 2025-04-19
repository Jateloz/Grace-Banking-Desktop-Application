package com.example.jatelobank.ActivityWindow.Reports;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.SneakyThrows;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportsController implements Initializable {

    public VBox mainContent;
    public DatePicker dateFrom;
    public DatePicker dateTo;

    private static final Logger logger = Logger.getLogger(ReportsController.class.getName());
    public Button searchButton;
    public Button transactionSearchButton;
    public Button TransferSearchButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadReport("/Fxml/Activity/Reports/TransactionReport.fxml");
    }

    private void loadReport(String reportPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(reportPath));
            Parent root = loader.load();
            mainContent.getChildren().setAll(root);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load report", e);
            showError("Failed to load report", e.getMessage());
        }
    }

    public void showTransactionReport(ActionEvent event) {
        loadReport("/Fxml/Activity/Reports/TransactionReport.fxml");
    }

    public void showInvestmentReport(ActionEvent event) {
        loadReport("/Fxml/Activity/Reports/InvestmentsReport.fxml");
    }

    public void showUserAccountReport(ActionEvent event) {
        loadReport("/Fxml/Activity/Reports/UserAccountsReport.fxml");
    }

    public void showTransfersReport(ActionEvent event) {
        loadReport("/Fxml/Activity/Reports/TransfersReport.fxml");
    }

    @SneakyThrows
    public void searchButt(ActionEvent event) {
        LocalDate fromDate = dateFrom.getValue();
        LocalDate toDate = dateTo.getValue();

        if (fromDate != null && toDate != null) {
            if (!fromDate.isAfter(toDate)) {
                loadInvestmentsReportSearch(fromDate, toDate);
            } else {
                showError("Invalid Date Range", "From date cannot be after To date.");
            }
        } else {
            showError("Invalid Date Selection", "Please select a valid date range.");
        }
    }

    private void loadInvestmentsReportSearch(LocalDate fromDate, LocalDate toDate) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Activity/Reports/InvestmentsReportSearch.fxml"));
            Parent root = loader.load();
            InvestmentsReportsSearchController controller = loader.getController();
            controller.filterData(fromDate, toDate);
            mainContent.getChildren().clear();
            mainContent.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error", "An error occurred while loading the report.");
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void transactionSearchButt(ActionEvent event) {
        LocalDate fromDate = dateFrom.getValue();
        LocalDate toDate = dateTo.getValue();

        if (fromDate != null && toDate != null) {
            if (!fromDate.isAfter(toDate)) {
                loadTransactionsReportSearch(fromDate, toDate);
            } else {
                showError("Invalid Date Range", "From date cannot be after To date.");
            }
        } else {
            showError("Invalid Date Selection", "Please select a valid date range.");
        }
    }

    public void TransferSearchButt(ActionEvent event) {
        LocalDate fromDate = dateFrom.getValue();
        LocalDate toDate = dateTo.getValue();

        if (fromDate != null && toDate != null) {
            if (!fromDate.isAfter(toDate)) {
                loadTransfersReportSearch(fromDate, toDate);
            } else {
                showError("Invalid Date Range", "From date cannot be after To date.");
            }
        } else {
            showError("Invalid Date Selection", "Please select a valid date range.");
        }
    }
    private void loadTransactionsReportSearch(LocalDate fromDate, LocalDate toDate) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Activity/Reports/TransactionsReportSearch.fxml"));
            Parent root = loader.load();
            TransactionsReportSearchController controller = loader.getController();
            controller.filterData(fromDate, toDate);
            mainContent.getChildren().clear();
            mainContent.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error", "An error occurred while loading the report.");
        }
    }
    private void loadTransfersReportSearch(LocalDate fromDate, LocalDate toDate) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Activity/Reports/TransfersReportSearch.fxml"));
            Parent root = loader.load();
            TransfersReportSearchController controller = loader.getController();
            controller.filterData(fromDate, toDate);
            mainContent.getChildren().clear();
            mainContent.getChildren().add(root);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Error", "An error occurred while loading the report.");
        }
    }
}