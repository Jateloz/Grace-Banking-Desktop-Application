package com.example.jatelobank.ActivityWindow;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.Item;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class DashboardController implements Initializable {
    public Label username;
    public Label balance;
    public Label increaseInBalance;
    public Label income;
    public Label expense;
    public Label savingsIncrease;
    public Label savings;

    public WebSocketClient webSocketClient;
    public ScatterChart<String,Number> scatterChart;
    public ImageView imageView;
    public javafx.scene.layout.VBox vBoxContainer;
    XYChart.Series<String,Number> series = new XYChart.Series<>();
    XYChart.Series<String,Number> series2 = new XYChart.Series<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            username.setText(currentUser.getUserName());
            double bal = currentUser.getSavingsAmount() + currentUser.getBudgetAmount() + currentUser.getCheckingAmount();
            balance.setText("USD " + bal);
            savings.setText("USD " + currentUser.getSavingsAmount());
            income.setText("USD " + currentUser.getIncome());
            expense.setText("USD " + currentUser.getExpense());

            //call the balance increase in percentage
            balanceIncreasePercentage();

            //call the increase in savings method
            savingsIncreasePercentage();

            //call the method to   load the vbox
            loadVBox();
        }

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

        //call the scatterChart method
        ScatterChart();

        connectWebSocket();
    }

    public void connectWebSocket(){
        try{
            webSocketClient = new WebSocketClient(new URI("ws://localhost:3306/ws")){

                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    System.out.println("Connected to web server");

                }

                @Override
                public void onMessage(String s) {

                    Platform.runLater(() ->{

                        System.out.println(s);
                        updateUIWithNewData(s);
                    });
                }

                private void updateUIWithNewData(String s) {

                    balance.setText(s);
                }

                @Override
                public void onClose(int i, String s, boolean b) {

                    System.out.println("Disconnected from web socket");
                }

                @Override
                public void onError(Exception e) {

                    e.printStackTrace();
                }
            };
            webSocketClient.connect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeWebSocket(){
        if (webSocketClient != null){
            webSocketClient.close();
        }
    }

    public void ScatterChart(){
        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String acc = current.getAccNo();

            DatabaseConnection connection = new DatabaseConnection();
            Connection connection1 = connection.getConn();

            String query = "select Revenue,Date from Revenue where AccountNumber='"+acc+"'";

            //query expense
            String query2 = "select Expense from Expense where AccountSending='"+acc+"'";
            try {
                Statement stm = connection1.createStatement();
                ResultSet rs = stm.executeQuery(query);

                //query expense
                Statement stm2 = connection1.createStatement();
                ResultSet rs2 = stm2.executeQuery(query2);

                while (rs.next() && rs2.next()){
                    double amt = rs.getDouble("Revenue");
                    String date = rs.getString("Date");
                    double amt2 = rs2.getDouble("Expense");
                    series.getData().add(new XYChart.Data<>(date,amt));
                    series2.getData().add(new XYChart.Data<>(date,amt2));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            scatterChart.getData().addAll(series,series2);
        }
    }

    //method to calculate percentage increase in balance
    public void balanceIncreasePercentage(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String acc = current.getAccNo();
            //query the checking account to get the amount in it
            String queryChecking = "select Amount,Expense,Income from CheckingAccount where AccountNumber='"+acc+"'";

            //query the savings acc to get the amount available in it
            String querySavings = "select Amount from SavingsAccount where AccountNumber='"+acc+"'";

            //query the savings acc to get the amount available in it
            String queryBudget = "select Amount from BudgetAccount where AccountNumber='"+acc+"'";


            try {
                //query the checking account to get the amount in it
                PreparedStatement pstChecking = connection1.prepareStatement(queryChecking);
                ResultSet rsChecking = pstChecking.executeQuery();

                //query the savings acc to get the amount available in it
                PreparedStatement pstSavings = connection1.prepareStatement(querySavings);
                ResultSet rsSavings = pstSavings.executeQuery();

                //query the savings acc to get the amount available in it
                PreparedStatement pstBudget = connection1.prepareStatement(queryBudget);
                ResultSet rsBudget = pstBudget.executeQuery();

                while (rsChecking.next() && rsSavings.next() && rsBudget.next()){
                    //get the total amount in checking acc and the expense
                    double checkingAmount = rsChecking.getDouble("Amount");
                    double expenseAmount = rsChecking.getDouble("Expense");
                    double incomeAmount = rsChecking.getDouble("Income");

                    //get the total amount in savings acc
                    double savingsAmount = rsSavings.getDouble("Amount");

                    //get the total amount in budget acc
                    double budgetAmount = rsBudget.getDouble("Amount");

                    //sum  the totals of the three accounts
                    double total = checkingAmount + savingsAmount + budgetAmount;

                    DecimalFormat df = new DecimalFormat("0.00");
                    double percentage = (((total - expenseAmount) + incomeAmount) / total) * 100;

                    increaseInBalance.setText(df.format(percentage)+"%");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //method to calculate percentage increase in savings
    public void savingsIncreasePercentage(){
        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String acc = current.getAccNo();
            //query the savings account to get the amount ,withdrawn and that deposited
            String queryChecking = "select Amount,Deposited,Withdrawn from SavingsAccount where AccountNumber='"+acc+"'";

            try {
                //query the savings account
                PreparedStatement pstSavings = connection1.prepareStatement(queryChecking);
                ResultSet rsSavings = pstSavings.executeQuery();

                while (rsSavings.next()){
                    //get the total amount in savings acc ,withdrawn and deposited
                    double checkingAmount = rsSavings.getDouble("Amount");
                    double depositedAmount = rsSavings.getDouble("Deposited");
                    double withdrawnAmount = rsSavings.getDouble("Withdrawn");

                    DecimalFormat df = new DecimalFormat("0.00");
                    double percentage = (((checkingAmount - withdrawnAmount) + depositedAmount) / checkingAmount) * 100;

                    savingsIncrease.setText(df.format(percentage)+"%");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //method to load data in the dashboard into the VBox
    public void loadVBox(){
        List<Item> itemList = new ArrayList<>();

        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String acc = current.getAccNo();
            String query = "SELECT * FROM toOtherAccount WHERE accountNumberSending='" + acc + "' ORDER BY amount DESC";

            try {
                Statement stm = connection1.createStatement();
                ResultSet rst = stm.executeQuery(query);

                while (rst.next()){

                    Item item = new Item(rst.getString("BeneficiaryName"),rst.getString("AccReceiving"),rst.getDouble("Amount"));
                    itemList.add(item);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        for (Item item : itemList){
            Label label = new Label(item.getBenefiniciaryName()+"    "+item.getAccount()+"     $"+item.getAmount()+"\n");
            label.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-border-color: black; -fx-border-width: 1px;");
            vBoxContainer.getChildren().add(label);
        }
    }
}
