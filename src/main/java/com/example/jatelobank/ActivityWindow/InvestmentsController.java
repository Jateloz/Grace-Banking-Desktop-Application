package com.example.jatelobank.ActivityWindow;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

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
}
