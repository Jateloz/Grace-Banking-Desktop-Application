package com.example.jatelobank.ActivityWindow;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
    public ComboBox firstFilterButton;
    public AreaChart<String,Number> statsChart;
    public ComboBox secondFilterButton;
    public LineChart lineChart;
    public ListView listView;
    public TextField searchBar;
    public Button searchButton;
    public Label allButton;
    public Label expenseButton;
    public Label incomeButton;
    public Label userNamez;
    public BorderPane bp;
    XYChart.Series<String,Number> series = new XYChart.Series<>();

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
        }

        //load the all tab
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/AllExpenseIncome/All.fxml")));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
        bp.setCenter(root);

        //load the statistics chart
        statisticsChart();

    }

    public void firstFilterButt(ActionEvent event) {

    }

    public void secondFilterButt(ActionEvent event) {

    }

    public void searchButt(ActionEvent event) {

    }

    public void allButt(MouseEvent mouseEvent) {

        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Fxml/AllExpenseIncome/All.fxml")));

        }catch (Exception e){
            Logger.getLogger(ModuleLayer.Controller.class.getName()).log(Level.SEVERE,null,e);
            e.printStackTrace();
        }
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
                    series.getData().add(new XYChart.Data<>(dat,amt));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            statsChart.getData().add(series);
        }
    }
}
