package com.example.jatelobank.ActivityWindow.Reports;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.print.PrinterJob;
import javafx.scene.transform.Scale;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class InvestmentsReportsSearchController {

    @FXML
    public TableColumn<InvestmentUser, Integer> colProductId;
    @FXML
    public TableColumn<InvestmentUser, String> colAccountNumber;
    @FXML
    public TableColumn<InvestmentUser, Double> colValue;
    @FXML
    public TableColumn<InvestmentUser, String> colProduct;
    @FXML
    public TableColumn<InvestmentUser, Date> colDateInvested;
    @FXML
    public TableView<InvestmentUser> tableView;

    private ObservableList<InvestmentUser> observableList = FXCollections.observableArrayList();
    private static final Logger logger = Logger.getLogger(InvestmentsReportsSearchController.class.getName());

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up TableView columns
        colProductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAccountNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        colValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        colDateInvested.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    // Method to filter and load data based on selected date range
    @SneakyThrows
    public void filterData(LocalDate fromDate, LocalDate toDate) {
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null && fromDate != null && toDate != null) {
            String accountNumber = currentUser.getAccNo();

            // Set up database connection
            DatabaseConnection databaseConnection = new DatabaseConnection();
            try (Connection connection = databaseConnection.getConn()) {
                String query = "SELECT id, AccountNumber, Value, Product, Date " +
                        "FROM Investments WHERE AccountNumber = ? AND Value  BETWEEN ? AND ?";

                try (PreparedStatement pst = connection.prepareStatement(query)) {
                    pst.setString(1, accountNumber);
                    pst.setDate(2, Date.valueOf(fromDate));
                    pst.setDate(3, Date.valueOf(toDate));

                    // Execute the query and get the results
                    try (ResultSet rs = pst.executeQuery()) {
                        observableList.clear(); // Clear previous results

                        while (rs.next()) {
                            int transId = rs.getInt("id");
                            String accNumber = rs.getString("AccountNumber");
                            double value = rs.getDouble("Value");
                            String product = rs.getString("Product");
                            Date dateInvested = rs.getDate("Date");

                            // Add results to the observable list for TableView
                            Platform.runLater(() -> {
                                observableList.add(new InvestmentUser(transId, accNumber, value, product, dateInvested));
                                tableView.setItems(observableList); // Update TableView
                            });
                        }
                    }
                }
            } catch (SQLException e) {
                logger.severe("Database error: " + e.getMessage());
                showError("Database error", e.getMessage());
            }
        } else {
            showError("Invalid Data", "Please select a valid date range.");
        }
    }

    // Method to show error alerts
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method for printing the report
    @SneakyThrows
    public void handleDownloadReport(ActionEvent event) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(null)) {
            double scaleX = printerJob.getJobSettings().getPageLayout().getPrintableWidth() / tableView.getBoundsInParent().getWidth();
            double scaleY = printerJob.getJobSettings().getPageLayout().getPrintableHeight() / tableView.getBoundsInParent().getHeight();
            double scale = Math.min(scaleX, scaleY);
            tableView.getTransforms().add(new Scale(scale, scale));
            printReport(printerJob);
        }
    }

    // Method for printing the content
    @SneakyThrows
    private void printReport(PrinterJob printerJob) {
        boolean success = printerJob.printPage(tableView);
        if (success) {
            printerJob.endJob();
        }
    }
}
