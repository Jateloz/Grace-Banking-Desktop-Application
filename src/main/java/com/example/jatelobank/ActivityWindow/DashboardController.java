package com.example.jatelobank.ActivityWindow;

import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Label;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URL;
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
    public BarChart chart;

    public WebSocketClient webSocketClient;

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
        }

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
}
