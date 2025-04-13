package com.example.jatelobank.ActivityWindow;

import com.almasb.fxgl.core.collection.Array;
import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class InvestmentsController implements Initializable {
    public Label userName;
    public Label totalInvested;
    public Label noOfInvestments;
    public Label returnRate;
    public BarChart<String,Number> investmentsChart;
    public BarChart<String,Number> revenueChart;
    public ListView<String> listView;
    public TableView tableView;
    public ImageView imageView;
    public FlowPane flowPane;
    XYChart.Series<String,Number> dataseries = new XYChart.Series<>();
    ObservableList<String> list;
    XYChart.Series<String,Number> dataSeries2 = new XYChart.Series<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DatabaseConnection connection = new DatabaseConnection();
        Connection connection1 = connection.getConn();

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null){
            String acc = currentUser.getAccNo();
            userName.setText(currentUser.getUserName());


            //checking total number of investments
            String total = "select count(*) as total from Investments where AccountNumber= '"+acc+"'";
            try {
                Statement stm = connection1.createStatement();
                ResultSet rs = stm.executeQuery(total);
                if (rs.next()){
                    noOfInvestments.setText(String.valueOf(rs.getInt("total")));
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            //checking total investments
            String totalInvestments = "select sum(Value) as total from Investments where AccountNumber= '"+acc+"'";
            try {
                Statement stm = connection1.createStatement();
                ResultSet rs = stm.executeQuery(totalInvestments);
                if (rs.next()){
                    totalInvested.setText("$ "+rs.getDouble("total"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }


            updateStockData();
            startLiveUpdate();
        }

        //load the image in th imageView
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
        //load the investment chart
        investmentsChart();

        //load the revenue chart
        revenueChart();

        //set the listView
        setList();
    }

    public void investmentsChart(){
        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String acc = current.getAccNo();

            DatabaseConnection connection = new DatabaseConnection();
            Connection connection1 = connection.getConn();

            String query = "select Value,Product from Investments where AccountNumber='"+acc+"'";
            try {
                Statement stm = connection1.createStatement();
                ResultSet rs = stm.executeQuery(query);

                while (rs.next()){
                    double amt = rs.getDouble("Value");
                    String prod = rs.getString("Product");
                    dataseries.getData().add(new XYChart.Data<>(prod,amt));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            investmentsChart.getData().add(dataseries);
        }
    }

    public void revenueChart(){
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
                    dataSeries2.getData().add(new XYChart.Data<>(dat,amt));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            revenueChart.getData().add(dataSeries2);
        }
    }

    public void setList(){

        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getConn();

        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String acc = current.getAccNo();

            list = FXCollections.observableArrayList();

            String query = "Select * from Investments where AccountNumber='"+acc+"'";

            try {
                Statement stm = connection.createStatement();
                ResultSet rs = stm.executeQuery(query);

                while (rs.next()){

                    list.add("Product ID : "+rs.getInt("id"));
                    list.add("Account No : "+rs.getString("AccountNumber"));
                    list.add("Product value : $"+rs.getDouble("Value"));
                    list.add("Product invested in : "+rs.getString("Product"));
                    list.add("Date invested : "+rs.getDate("Date"));
                    list.add("\n");

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            listView.setItems(list);
        }
    }

    //methods for flowPane for the stocks section
    private void updateStockData(){
        /**
        final Random random = new Random();
        final List<String> stockNames = List.of("AAPL","GOGL","AMZN","TSLA","MSFT","BTC","NFLX","NVDA","AMD","META","BABA","JPM","XOM","CVX","TSM","INTC","JATS","V","MA","GS","BAC","HD","SBUX","IBM","JNJ");

        flowPane.getChildren().clear();
        for (String stock : stockNames){
            double price =  100 + random.nextDouble() * 50;
            Label stockLabel = new Label(stock + " :$"+String.format("%.2f",price));
            VBox stockBox = new VBox(stockLabel);
            stockBox.setPrefWidth(112);
            stockBox.setPrefHeight(20);
            stockBox.setStyle("-fx-border-color:cyan;" +
                    "-fx-border-radius:10;" +
                    "-fx-padding:15;" +
                    "-fx-background-color:white;" +
                    "-fx-background-radius:10;" +
                    "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.2),10,0,2,2);");
            flowPane.getChildren().add(stockBox);
        }**/

        final List<String> stockSymbols = List.of("AAPL","GOGL","AMZN","TSLA","MSFT","BTC","NFLX","NVDA","AMD","META","BABA","JPM","XOM","CVX","TSM","INTC","JATS","V","MA","GS","BAC","HD","SBUX","IBM","JNJ");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        flowPane.getChildren().clear();
        for (String symbol : stockSymbols){
            executorService.submit(() ->{
                final String price = fetchStockPrices(symbol);
                Platform.runLater(() -> {
                    Label stockLabel = new Label(symbol + ": $"+price);
                    VBox stockBox = new VBox(stockLabel);
                    stockBox.setPrefWidth(112);
                    stockBox.setPrefHeight(20);
                    stockBox.setStyle("-fx-border-color:cyan;" +
                            "-fx-border-radius:10;" +
                            "-fx-padding:15;" +
                            "-fx-background-color:white;" +
                            "-fx-background-radius:10;" +
                            "-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.2),10,0,2,2);");
                    flowPane.getChildren().add(stockBox);
                });
            });
        }
    }

    private String fetchStockPrices(String symbol) {
        try {
            String url = "https://finance.yahoo.com/quote/"+symbol;
            Document document = Jsoup.connect(url).get();
            Elements priceElements = document.select("fin-streamer[data-field=regularMarketPrice]");
            if (!priceElements.isEmpty()){
                return priceElements.first().text();
            }else {
                return "N/A";
            }
        }catch (IOException e){
            return "Error";
        }
    }

    private void startLiveUpdate(){
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() ->{
                    flowPane.getChildren().clear();
                    updateStockData();
                });
            }
        },0,1000000);
    }
}
