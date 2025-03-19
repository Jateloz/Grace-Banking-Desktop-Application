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
import java.util.Date;
import java.util.ResourceBundle;

public class TransactionReportController implements Initializable {
    public TableView<TransactionUser> reportTableView;
    public TableColumn<TransactionUser,Integer> transactionIdColumn;
    public TableColumn<TransactionUser, Date> dateColumn;
    public TableColumn<TransactionUser,Double> amountColumn;
    public TableColumn<TransactionUser,String> accountNumberColumn;
    public TableColumn<TransactionUser,String> transactionTypeColumn;
    private ObservableList<TransactionUser> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        accountNumberColumn.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        transactionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transactionType"));

        //call the method to  load the data into the table view
        loadUserData();
    }

    public void handleDownloadReport(ActionEvent event) {
        javafx.print.PrinterJob printerJob = javafx.print.PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(null)){
            double scaleX = printerJob.getJobSettings().getPageLayout().getPrintableWidth() / reportTableView.getBoundsInParent().getWidth();
            double scaleY =  printerJob.getJobSettings().getPageLayout().getPrintableHeight() / reportTableView.getBoundsInParent().getHeight();
            double scale = Math.min(scaleX,scaleY);
            reportTableView.getTransforms().add(new Scale(scale,scale));
            printReport(printerJob);
        }
    }
    public void loadUserData(){
        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String currentUser = current.getAccNo();
            observableList.clear();
            DatabaseConnection connection = new DatabaseConnection();
            Connection connection1 = connection.getConn();
            String query = "SELECT * FROM expense where AccountSending='"+currentUser+"'";

            try {
                Statement stm = connection1.createStatement();
                ResultSet rs = stm.executeQuery(query);

                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                String transType = resultSetMetaData.getColumnName(rs.findColumn("Expense"));


                while (rs.next()) {
                    String transId = String.valueOf(rs.getInt("id"));
                    String date = rs.getString("Date");
                    String amt = String.valueOf(rs.getDouble("Expense"));
                    String accNo = rs.getString("AccountNUmber");

                    observableList.add(new TransactionUser(transId,date,amt,accNo,transType));
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

            String query1 = "SELECT * FROM revenue where AccountNumber='"+currentUser+"'";
            try {
                Statement stm = connection1.createStatement();
                ResultSet rs = stm.executeQuery(query1);

                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                String transType = resultSetMetaData.getColumnName(rs.findColumn("Revenue"));


                while (rs.next()) {
                    String transId = String.valueOf(rs.getInt("id"));
                    String date = rs.getString("Date");
                    String amt = String.valueOf(rs.getDouble("Revenue"));
                    String accNo = rs.getString("AccountSending");

                    observableList.add(new TransactionUser(transId,date,amt,accNo,transType));
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
            reportTableView.setItems(observableList);
        }
    }

    //method for printing job
    @SneakyThrows
    public void printReport(PrinterJob printerJob){
        boolean success = printerJob.printPage(reportTableView);
        if (success){
            printerJob.endJob();
        }
    }
}
