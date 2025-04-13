package com.example.jatelobank.ActivityWindow.Reports;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UserAccountReport implements Initializable {
    public GridPane gridPane;
    public Label lblUserName;
    public Label lblAccNo;
    public Label lblCheckingAmount;
    public Label lblSavingsAmount;
    public Label lblBudgetAmount;
    public Label lblIncome;
    public Label lblExpense;
    public VBox mainContent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadUserData();
    }

    public void handleDownloadReport(ActionEvent event) {
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null && printerJob.showPrintDialog(null)) {
            double scaleX = printerJob.getJobSettings().getPageLayout().getPrintableWidth() / gridPane.getBoundsInParent().getWidth();
            double scaleY = printerJob.getJobSettings().getPageLayout().getPrintableHeight() / gridPane.getBoundsInParent().getHeight();
            double scale = Math.min(scaleX, scaleY);
            gridPane.getTransforms().add(new Scale(scale, scale));
            printReport(printerJob);
        }
    }

    public void loadUserData(){
        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String currentUser = current.getAccNo();
            String userName = current.getUserName();

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


                double income1 = 0;
                double expense2 = 0;
                while (rs.next() && rs2.next() && rs3.next() && rs4.next() && rs5.next()) {
                    double checking = rs.getDouble("Amount");
                    double savings = rs2.getDouble("Amount");
                    double budget = rs3.getDouble("Amount");

                    double income = rs4.getDouble("Revenue");
                    income1 += income;
                    double expense = rs5.getDouble("Expense");
                    expense2 += expense;


                    lblUserName.setText(userName);
                    lblAccNo.setText(currentUser);
                    lblCheckingAmount.setText(String.valueOf(checking));
                    lblSavingsAmount.setText(String.valueOf(savings));
                    lblBudgetAmount.setText(String.valueOf(budget));
                    lblIncome.setText(String.valueOf(income1));
                    lblExpense.setText(String.valueOf(expense));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //method for printing job
    @SneakyThrows
    public void printReport(PrinterJob printerJob){
        boolean success = printerJob.printPage(gridPane);
        if (success){
            printerJob.endJob();
        }
    }
}
