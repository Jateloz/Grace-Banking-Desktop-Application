package com.example.jatelobank.AllExpenseIncome;

import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class Income implements Initializable {
    public TableColumn<UserTable, Date> date;
    public TableColumn<UserTable,String> accountNumber;
    public TableColumn<UserTable,Double> amount;
    public TableView<UserTable> tableView;
    public ObservableList<UserTable> userList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        accountNumber.setCellValueFactory(new PropertyValueFactory<>("account"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        //load the data
        loadUserData();
    }

    public void loadUserData(){
        User current = SessionManager.getInstance().getCurrentUser();
        if (current != null){
            String acc = current.getAccNo();

            userList.clear();
            DatabaseConnection connection = new DatabaseConnection();
            Connection connection1 = connection.getConn();
            String query = "SELECT * FROM Revenue where AccountSending='"+acc+"'";

            try {

                Statement stm = connection1.createStatement();
                ResultSet rst = stm.executeQuery(query);
                while (rst.next()) {
                    String date = String.valueOf(rst.getDate("Date"));
                    String accou = rst.getString("AccountNumber");
                    double amt = rst.getDouble("Revenue");

                    userList.add(new UserTable(date,accou,amt));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            tableView.setItems(userList);
        }
    }
}
