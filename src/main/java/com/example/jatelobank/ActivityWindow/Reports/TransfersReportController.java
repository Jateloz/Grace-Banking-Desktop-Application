package com.example.jatelobank.ActivityWindow.Reports;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.transform.Scale;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class TransfersReportController implements Initializable {
    public TableView<TransfersUser> transfersTable;
    public TableColumn<TransfersUser,String> accNumberColumn;
    public TableColumn<TransfersUser,Double> amountColumn;
    public TableColumn<TransfersUser,Date> dateColumn;
    public TableColumn<TransfersUser,String> accReceivingColumn;
    public TableColumn<TransfersUser,String> transferPurposeColumn;
    public TableColumn<TransfersUser,String> beneficiaryNameColumn;
    public TableColumn<TransfersUser,String> beneficiaryEmailColumn;
    ObservableList<TransfersUser> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        accReceivingColumn.setCellValueFactory(new PropertyValueFactory<>("accountReceiving"));
        transferPurposeColumn.setCellValueFactory(new PropertyValueFactory<>("transferPurpose"));
        beneficiaryNameColumn.setCellValueFactory(new PropertyValueFactory<>("beneficiaryName"));
        beneficiaryEmailColumn.setCellValueFactory(new PropertyValueFactory<>("beneficiaryEmail"));

        loadUserData();
    }

    public void handleDownloadReport(ActionEvent event) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(null)) {
            double scaleX = printerJob.getJobSettings().getPageLayout().getPrintableWidth() / transfersTable.getBoundsInParent().getWidth();
            double scaleY = printerJob.getJobSettings().getPageLayout().getPrintableHeight() / transfersTable.getBoundsInParent().getHeight();
            double scale = Math.min(scaleX, scaleY);
            transfersTable.getTransforms().add(new Scale(scale, scale));
            printReport(printerJob);
        }
    }

    public void loadUserData(){
        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String currentUser = current.getAccNo();
            String userName = current.getUserName();

            observableList.clear();
            DatabaseConnection connection = new DatabaseConnection();
            Connection connection1 = connection.getConn();

            String query1 = "SELECT * FROM toOtherAccount where AccountNumberSending='"+currentUser+"'";

            try {
                Statement stm = connection1.createStatement();
                ResultSet rs = stm.executeQuery(query1);

                while (rs.next()) {
                    String accountNumber = rs.getString("AccountNumberSending");
                    double amount = rs.getDouble("Amount");
                    Date date = rs.getDate("Date");
                    String accountReceiving = rs.getString("AccReceiving");
                    String transferPurpose = rs.getString("TransferPurpose");
                    String beneficiaryName = rs.getString("BeneficiaryName");
                    String beneficiaryEmail = rs.getString("BeneficiaryEmail");

                    observableList.add(new TransfersUser(accountNumber,amount,date,accountReceiving,transferPurpose,beneficiaryName,beneficiaryEmail));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            transfersTable.setItems(observableList);
        }
    }

    //method for printing job
    @SneakyThrows
    public void printReport(PrinterJob printerJob){
        boolean success = printerJob.printPage(transfersTable);
        if (success){
            printerJob.endJob();
        }
    }
}
