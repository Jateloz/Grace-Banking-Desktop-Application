package com.example.jatelobank.ActivityWindow.Reports;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.transform.Scale;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class InvestmentsReportController implements Initializable {
    public TableView<InvestmentUser> tableView;
    public TableColumn<InvestmentUser, Integer> colProductId;
    public TableColumn<InvestmentUser, String> colAccountNumber;
    public TableColumn<InvestmentUser, Double> colValue;
    public TableColumn<InvestmentUser, String> colProduct;
    public TableColumn<InvestmentUser, Date> colDateInvested;
    public ObservableList<InvestmentUser> observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAccountNumber.setCellValueFactory(new PropertyValueFactory<>("accountNumber"));
        colValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        colProduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        colDateInvested.setCellValueFactory(new PropertyValueFactory<>("date"));

        //call the method to load user data
        loadUserData();
    }

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
    public void loadUserData(){
        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String currentUser = current.getAccNo();
            observableList.clear();
            DatabaseConnection connection = new DatabaseConnection();
            Connection connection1 = connection.getConn();

            String query1 = "SELECT * FROM investments where AccountNumber='"+currentUser+"'";
            try {
                Statement stm = connection1.createStatement();
                ResultSet rs = stm.executeQuery(query1);

                while (rs.next()) {
                    int transId = rs.getInt("id");
                    String accountNumber = rs.getString("AccountNumber");
                    double value = rs.getDouble("Value");
                    String product = rs.getString("Product");
                    Date date = rs.getDate("Date");

                    observableList.add(new InvestmentUser(transId,accountNumber,value,product,date));
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
            tableView.setItems(observableList);
        }
    }
    //method for printing job
    @SneakyThrows
    public void printReport(PrinterJob printerJob){
        boolean success = printerJob.printPage(tableView);
        if (success){
            printerJob.endJob();
        }
    }
}
