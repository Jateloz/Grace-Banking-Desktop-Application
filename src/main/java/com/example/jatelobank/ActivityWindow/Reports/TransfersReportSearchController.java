package com.example.jatelobank.ActivityWindow.Reports;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class TransfersReportSearchController implements Initializable {
    public TableView<TransfersUser> transfersTable;
    public TableColumn<TransfersUser, String> accNumberColumn;
    public TableColumn<TransfersUser, Double> amountColumn;
    public TableColumn<TransfersUser, Date> dateColumn;
    public TableColumn<TransfersUser, String> accReceivingColumn;
    public TableColumn<TransfersUser, String> transferPurposeColumn;
    public TableColumn<TransfersUser, String> beneficiaryNameColumn;
    public TableColumn<TransfersUser, String> beneficiaryEmailColumn;
    public static final Logger logger = Logger.getLogger(InvestmentsReportsSearchController.class.getName());
    private final ObservableList<TransfersUser> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        accReceivingColumn.setCellValueFactory(new PropertyValueFactory<>("accountReceiving"));
        transferPurposeColumn.setCellValueFactory(new PropertyValueFactory<>("transferPurpose"));
        beneficiaryNameColumn.setCellValueFactory(new PropertyValueFactory<>("beneficiaryName"));
        beneficiaryEmailColumn.setCellValueFactory(new PropertyValueFactory<>("beneficiaryEmail"));
    }

    public void handleDownloadReport(ActionEvent event) {

    }

    public void filterData(LocalDate fromDate, LocalDate toDate) {
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null && fromDate != null && toDate != null) {
            String current = currentUser.getAccNo();

            observableList.clear();

            String query = "SELECT * FROM toOtherAccount WHERE AccountNumberSending = ? AND Date BETWEEN ? AND ?";

            try {
                DatabaseConnection dbConn = new DatabaseConnection();

                Connection conn = dbConn.getConn();
                PreparedStatement pstmt = conn.prepareStatement(query);

                pstmt.setString(1, current);
                pstmt.setDate(2, java.sql.Date.valueOf(fromDate));
                pstmt.setDate(3, java.sql.Date.valueOf(toDate));

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String accountNumber = rs.getString("AccountNumberSending");
                        double amount = rs.getDouble("Amount");
                        Date date = rs.getDate("Date");
                        String accountReceiving = rs.getString("AccReceiving");
                        String transferPurpose = rs.getString("TransferPurpose");
                        String beneficiaryName = rs.getString("BeneficiaryName");
                        String beneficiaryEmail = rs.getString("BeneficiaryEmail");

                        observableList.add(new TransfersUser(accountNumber, amount, date, accountReceiving, transferPurpose, beneficiaryName, beneficiaryEmail));
                    }
                }

            } catch (SQLException e) {
                logger.severe("Database error: " + e.getMessage());
                showError("Database error", e.getMessage());
            }
        }
        transfersTable.setItems(observableList);
    }


    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
