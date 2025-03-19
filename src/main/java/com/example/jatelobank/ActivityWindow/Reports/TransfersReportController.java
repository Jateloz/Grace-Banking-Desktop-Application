package com.example.jatelobank.ActivityWindow.Reports;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    public TableColumn<TransfersUser,Double> withdrawnAmountColumn;
    public TableColumn<TransfersUser,String> accReceivingColumn;
    public TableColumn<TransfersUser,String> transferPurposeColumn;
    public TableColumn<TransfersUser,String> beneficiaryNameColumn;
    public TableColumn<TransfersUser,String> beneficiaryEmailColumn;
    public TableColumn<TransfersUser,Double> depositedAmountColumn;
    ObservableList<TransfersUser> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        withdrawnAmountColumn.setCellValueFactory(new PropertyValueFactory<>("withdrawnAmount"));
        accReceivingColumn.setCellValueFactory(new PropertyValueFactory<>("accountReceiving"));
        transferPurposeColumn.setCellValueFactory(new PropertyValueFactory<>("transferPurpose"));
        beneficiaryNameColumn.setCellValueFactory(new PropertyValueFactory<>("beneficiaryName"));
        beneficiaryEmailColumn.setCellValueFactory(new PropertyValueFactory<>("beneficiaryEmail"));
        depositedAmountColumn.setCellValueFactory(new PropertyValueFactory<>("depositedAmount"));

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

            String query1 = "SELECT * FROM checkingAccount where AccountNumber='"+currentUser+"'";
            String query2 = "SELECT * FROM savingsAccount where AccountNumber='"+currentUser+"'";
            String query3 = "SELECT * FROM budgetAccount where AccountNumber='"+currentUser+"'";
            String query4 = "SELECT * FROM revenue where AccountNumber='"+currentUser+"'";
            String query5 = "SELECT * FROM expense where AccountNumber='"+currentUser+"'";
            try {
                Statement stm = connection1.createStatement();
                ResultSet rs = stm.executeQuery(query1);

                Statement stm2 = connection1.createStatement();
                ResultSet rs2 = stm2.executeQuery(query2);

                Statement stm3 = connection1.createStatement();
                ResultSet rs3 = stm3.executeQuery(query3);

                Statement stm4 = connection1.createStatement();
                ResultSet rs4 = stm4.executeQuery(query4);

                Statement stm5 = connection1.createStatement();
                ResultSet rs5 = stm5.executeQuery(query5);

                while (rs.next() & rs2.next() & rs3.next() & rs4.next() & rs5.next()) {
                    String accountNumber = rs.getString("");


                    //observableList.add(new TransfersUser());
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
