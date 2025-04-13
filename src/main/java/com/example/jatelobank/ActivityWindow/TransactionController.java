package com.example.jatelobank.ActivityWindow;

import com.example.jatelobank.AllExpenseIncome.SearchController;
import com.example.jatelobank.AllExpenseIncome.UserTable;
import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TransactionController implements Initializable {
    public Label userName;
    public Label percentage;
    public Label balance2;
    public Label availableBalance;
    public Label income;
    public Label expense;
    public Label balance;
    public AreaChart<String,Number> statsChart;
    public TextField searchBar;
    public Button searchButton;
    public Label allButton;
    public Label expenseButton;
    public Label incomeButton;
    public Label userNamez;
    public BorderPane bp;
    public ImageView imageView;
    public LineChart<String,Number> ExpenseLineChart;
    public LineChart<String,Number> RevenueLineChart;
    XYChart.Series<String,Number> series = new XYChart.Series<>();
    XYChart.Series<String ,Number> expSerie = new XYChart.Series<>();
    XYChart.Series<String,Number> revSerie = new XYChart.Series<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null){
            userName.setText(currentUser.getUserName());
            userNamez.setText(currentUser.getUserName());

            //set the balances
            balance2.setText("USD "+currentUser.getCheckingAmount());
            availableBalance.setText("USD "+currentUser.getCheckingAmount());
            income.setText("USD "+currentUser.getIncome());
            expense.setText("USD "+currentUser.getExpense());
            balance.setText("USD "+currentUser.getCheckingAmount());

            //load the all tab
            Parent root = null;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/AllExpenseIncome/All.fxml")));

            }catch (Exception e){
                Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
                e.printStackTrace();
            }
            bp.setCenter(root);

            //load the image into the imageView
            DatabaseConnection connection = new DatabaseConnection();
            Connection connection1 = connection.getConn();
            User current = SessionManager.getInstance().getCurrentUser();
            if (current != null){
                String acc = current.getAccNo();

                String query = "select Image from Users where AccountNumber = '"+acc+"'";
                try {
                    PreparedStatement ps = connection1.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()){
                        Blob img  = rs.getBlob("Image");
                        if (img != null){
                            InputStream inputStream = img.getBinaryStream();
                            Image image = new Image(inputStream);
                            imageView.setImage(image);
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            //load the charts
            statisticsChart();
            setExpenseLineChart();
            setRevenueLineChart();
        }
    }

    @SneakyThrows
    public void searchButt(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/AllExpenseIncome/Search.fxml"));
        Parent root = loader.load();

        SearchController searchController = loader.getController();

        searchController.setSearchQuery(searchBar.getText().trim());

        bp.setCenter(root);
    }

    public void expenseButt(MouseEvent mouseEvent) {

        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/AllExpenseIncome/Expense.fxml")));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp.setCenter(root);
    }

    public void incomeButton(MouseEvent mouseEvent) {

        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/AllExpenseIncome/Income.fxml")));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp.setCenter(root);
    }

    public void statisticsChart(){
            User current = SessionManager.getInstance().getCurrentUser();

            if (current != null) {
                String acc = current.getAccNo();
                DatabaseConnection connection = new DatabaseConnection();
                Connection connection1 = connection.getConn();

                String queryRevenue = "SELECT Revenue, Date FROM Revenue WHERE AccountNumber='" + acc + "'";
                String queryExpense = "SELECT Expense, Date FROM Expense WHERE AccountNumber='" + acc + "'";

                XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
                revenueSeries.setName("Revenue");

                XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
                expenseSeries.setName("Expense");

                try {
                    Statement stm = connection1.createStatement();
                    ResultSet rsRevenue = stm.executeQuery(queryRevenue);

                    while (rsRevenue.next()) {
                        double revenue = rsRevenue.getDouble("Revenue");
                        String date = rsRevenue.getDate("Date").toString();
                        revenueSeries.getData().add(new XYChart.Data<>(date, revenue));
                    }

                    Statement stm1 = connection1.createStatement();
                    ResultSet rsExpense = stm1.executeQuery(queryExpense);

                    while (rsExpense.next()) {
                        double expense = rsExpense.getDouble("Expense");
                        String date = rsExpense.getDate("Date").toString();
                        expenseSeries.getData().add(new XYChart.Data<>(date, expense));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                statsChart.getData().clear();
                statsChart.getData().addAll(revenueSeries, expenseSeries);
            }
    }

    public void allButton(MouseEvent mouseEvent) {

        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/AllExpenseIncome/All.fxml")));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp.setCenter(root);
    }

    private void setExpenseLineChart(){
        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String acc = current.getAccNo();

            DatabaseConnection connection = new DatabaseConnection();
            Connection connection1 = connection.getConn();

            String query = "select Expense,Date from Expense where AccountNumber='"+acc+"'";
            try {
                Statement stm = connection1.createStatement();
                ResultSet rs = stm.executeQuery(query);

                while (rs.next()){
                    double amt = rs.getDouble("Expense");
                    String dat = String.valueOf(rs.getDate("Date"));
                    expSerie.getData().add(new XYChart.Data<>(dat,amt));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            ExpenseLineChart.getData().clear();
            ExpenseLineChart.getData().add(expSerie);
        }
    }
    private void setRevenueLineChart(){
        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String acc = current.getAccNo();

            DatabaseConnection connection = new DatabaseConnection();
            Connection connection1 = connection.getConn();

            String query = "select Revenue,Date from Revenue where AccountNumber='"+acc+"'";
            try {
                Statement stm = connection1.createStatement();
                ResultSet rs = stm.executeQuery(query);

                while (rs.next()){
                    double amt = rs.getDouble("Revenue");
                    String dat = String.valueOf(rs.getDate("Date"));
                    revSerie.getData().add(new XYChart.Data<>(dat,amt));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            RevenueLineChart.getData().clear();
            RevenueLineChart.getData().add(revSerie);
        }
    }
}
