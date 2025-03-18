package com.example.jatelobank.ActivityWindow.Reports;

import lombok.Data;

import java.sql.Date;


@Data
public class TransactionUser {
    private int transactionID;
    private Date date;
    private double amount;
    private String accountNumber;
    private String transactionType;

    public TransactionUser(String transactionID, String date, String amount, String accountNumber, String transactionType) {
        this.transactionID = Integer.parseInt(transactionID);
        this.date = Date.valueOf(date);
        this.amount = Double.parseDouble(amount);
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
    }
}
