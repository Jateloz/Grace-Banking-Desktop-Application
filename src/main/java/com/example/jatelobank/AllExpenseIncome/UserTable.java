package com.example.jatelobank.AllExpenseIncome;

import lombok.Data;

@Data
public class UserTable {
    private String date;
    private String account;
    private double amount;

    public UserTable(String date, String account, double amount) {
        this.date = date;
        this.account = account;
        this.amount = amount;
    }

}
