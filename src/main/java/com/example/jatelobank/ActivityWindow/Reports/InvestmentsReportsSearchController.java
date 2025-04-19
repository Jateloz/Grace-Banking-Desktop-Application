package com.example.jatelobank.ActivityWindow.Reports;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class InvestmentsReportsSearchController implements Initializable {

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

    private final ObservableList<InvestmentUser> observableList = FXCollections.observableArrayList();

    public static final Logger logger = Logger.getLogger(InvestmentsReportsSearchController.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAccountNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        colValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        colDateInvested.setCellValueFactory(new PropertyValueFactory<>("date"));

        tableView.setItems(observableList); // Bind once
    }

    @SneakyThrows
    public void filterData(LocalDate fromDate, LocalDate toDate) {
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null && fromDate != null && toDate != null) {
            String accountNumber = currentUser.getAccNo();
            observableList.clear();

            try (Connection connection = new DatabaseConnection().getConn()) {
                String query = "SELECT id, AccountNumber, Value, Product, Date FROM Investments " +
                        "WHERE AccountNumber = ? AND Date BETWEEN ? AND ?";

                try (PreparedStatement pst = connection.prepareStatement(query)) {
                    pst.setString(1, accountNumber);
                    pst.setDate(2, Date.valueOf(fromDate));
                    pst.setDate(3, Date.valueOf(toDate));

                    try (ResultSet rs = pst.executeQuery()) {
                        while (rs.next()) {
                            InvestmentUser investment = new InvestmentUser(
                                    rs.getInt("id"),
                                    rs.getString("AccountNumber"),
                                    rs.getDouble("Value"),
                                    rs.getString("Product"),
                                    rs.getDate("Date")
                            );
                            observableList.add(investment);
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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

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

    @SneakyThrows
    private void printReport(PrinterJob printerJob) {
        boolean success = printerJob.printPage(tableView);
        if (success) {
            printerJob.endJob();
        }
    }
}