package com.example.jatelobank.ActivityWindow.Reports;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.transform.Scale;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class TransactionsReportSearchController implements Initializable {
    public TableView<TransactionUser> reportTableView;
    public TableColumn<TransactionUser,Integer> transactionIdColumn;
    public TableColumn<TransactionUser, Date> dateColumn;
    public TableColumn<TransactionUser,Double> amountColumn;
    public TableColumn<TransactionUser,String> accountNumberColumn;
    public TableColumn<TransactionUser,String> transactionTypeColumn;
    private ObservableList<TransactionUser> observableList = FXCollections.observableArrayList();
    public static final Logger logger = Logger.getLogger(InvestmentsReportsSearchController.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));
    }

    @SneakyThrows
    public void handleDownloadReport(ActionEvent event) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(null)) {
            double scaleX = printerJob.getJobSettings().getPageLayout().getPrintableWidth() / reportTableView.getBoundsInParent().getWidth();
            double scaleY = printerJob.getJobSettings().getPageLayout().getPrintableHeight() / reportTableView.getBoundsInParent().getHeight();
            double scale = Math.min(scaleX, scaleY);
            reportTableView.getTransforms().add(new Scale(scale, scale));
            printReport(printerJob);
        }
    }
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @SneakyThrows
    private void printReport(PrinterJob printerJob) {
        boolean success = printerJob.printPage(reportTableView);
        if (success) {
            printerJob.endJob();
        }
    }

    public void filterData(LocalDate fromDate, LocalDate toDate) {
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null && fromDate != null && toDate != null) {
            String currentAccNo = currentUser.getAccNo();
            observableList.clear();

            String expenseQuery = "SELECT * FROM expense WHERE AccountSending = ? AND Date BETWEEN ? AND ?";
            String revenueQuery = "SELECT * FROM revenue WHERE AccountNumber = ? AND Date BETWEEN ? AND ?";

            try {
                DatabaseConnection dbConn = new DatabaseConnection();
                Connection conn = dbConn.getConn();
                PreparedStatement expenseStmt = conn.prepareStatement(expenseQuery);
                PreparedStatement revenueStmt = conn.prepareStatement(revenueQuery);

                    // Set parameters for expense query
                    expenseStmt.setString(1, currentAccNo);
                    expenseStmt.setDate(2, java.sql.Date.valueOf(fromDate));
                    expenseStmt.setDate(3, java.sql.Date.valueOf(toDate));

                    try (ResultSet rs = expenseStmt.executeQuery()) {
                        while (rs.next()) {
                            String transId = String.valueOf(rs.getInt("id"));
                            String date = rs.getString("Date");
                            String amt = String.valueOf(rs.getDouble("Expense"));
                            String accNo = rs.getString("AccountNUmber");
                            observableList.add(new TransactionUser(transId, date, amt, accNo, "Expense"));
                        }
                    }

                    // Set parameters for revenue query
                    revenueStmt.setString(1, currentAccNo);
                    revenueStmt.setDate(2, java.sql.Date.valueOf(fromDate));
                    revenueStmt.setDate(3, java.sql.Date.valueOf(toDate));

                    try (ResultSet rs = revenueStmt.executeQuery()) {
                        while (rs.next()) {
                            String transId = String.valueOf(rs.getInt("id"));
                            String date = rs.getString("Date");
                            String amt = String.valueOf(rs.getDouble("Revenue"));
                            String accNo = rs.getString("AccountSending");
                            observableList.add(new TransactionUser(transId, date, amt, accNo, "Revenue"));
                        }
                    }

                } catch (SQLException e) {
                    logger.severe("Database error: " + e.getMessage());
                    showError("Database error", e.getMessage());
                }

                reportTableView.setItems(observableList);
            } else {
                showError("Invalid Data", "Please select a valid date range.");
            }

    }
}
