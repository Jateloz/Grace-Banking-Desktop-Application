package com.example.jatelobank.ActivityWindow.Reports;

import lombok.Data;

import java.sql.Date;

@Data
public class InvestmentUser {
    private int id;
    private String accountNumber;
    private double value;
    private String product;
    private Date date;

    public InvestmentUser(int id, String accountNumber, double value, String product, Date date) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.value = value;
        this.product = product;
        this.date = date;
    }
}
