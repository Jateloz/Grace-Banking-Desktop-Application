package com.example.jatelobank.AllExpenseIncome;

import com.example.jatelobank.ActivityWindow.TransactionController;
import com.example.jatelobank.DatabaseConnection;
import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    public TableView<UserTable> tableView;
    public TableColumn<UserTable, Date> date;
    public TableColumn<UserTable,String> accountNumber;
    public TableColumn<UserTable,Double> amount;
    public ObservableList<UserTable> list = FXCollections.observableArrayList();
    public Label searchLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        accountNumber.setCellValueFactory(new PropertyValueFactory<>("account"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        //call the searchData method
        //searchData();
    }



    //the method to search the data from the database
    @SneakyThrows
    public void searchData(){
        TransactionController transactionController = new TransactionController();

        if (transactionController.searchBar.getText().isEmpty()){
            searchLabel.setText("Please enter the account you wish to search.");
            return;
        }

            User current = SessionManager.getInstance().getCurrentUser();
            if (current != null){
                String acc = current.getAccNo();

                list.clear();

                String search = transactionController.searchBar.getText();
                DatabaseConnection connection = new DatabaseConnection();
                Connection connection1 = connection.getConn();
                String query = "select '"+search+"' from Revenue where AccountSending='"+acc+"'";
                //String query = "select * from Revenue where AccountSending='"+acc+"' and (AccountNumber like ? or revenue like ?";

                PreparedStatement pst = connection1.prepareStatement(query);
                pst.setString(1,"%"+search+"%");
                pst.setString(2,"%"+search+"%");

                //Statement stm = connection1.createStatement();
                ResultSet rst = pst.executeQuery();
                while (rst.next()) {
                    String date = rst.getDate("Date").toString();
                    String accou = rst.getString("AccountNumber");
                    double amt = rst.getDouble("Revenue");

                    list.add(new UserTable(date,accou,amt));
                }
                tableView.setItems(list);

                rst.close();
            }
    }
}
