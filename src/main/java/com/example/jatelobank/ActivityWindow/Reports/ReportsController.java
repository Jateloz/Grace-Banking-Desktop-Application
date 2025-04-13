package com.example.jatelobank.ActivityWindow.Reports;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import lombok.SneakyThrows;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportsController implements Initializable {
    public VBox mainContent;
    public DatePicker dateFrom;
    public DatePicker dateTo;
    public TextField searchField;

    private static final Logger logger = Logger.getLogger(ReportsController.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadReport("/Fxml/Activity/Reports/TransactionReport.fxml");
    }

    private void loadReport(String reportPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(reportPath)));
            mainContent.getChildren().setAll(root);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load report", e);
            showError("Failed to load report: ", e.getMessage());
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
        // Get the selected dates from the date pickers
        LocalDate fromDate = dateFrom.getValue();
        LocalDate toDate = dateTo.getValue();

        // Validate that both dates are selected and the fromDate is not after toDate
        if (fromDate != null && toDate != null) {
            if (!fromDate.isAfter(toDate)) {
                // Proceed to load the InvestmentsReportsSearch.fxml and filter data
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
            // Load the InvestmentsReportSearch.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Activity/Reports/InvestmentsReportSearch.fxml"));
            Parent root = loader.load();
            InvestmentsReportsSearchController controller = loader.getController();

            // Call the filterData method with the selected date range
            controller.filterData(fromDate, toDate);

            // Clear current content and add the new content
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
}
