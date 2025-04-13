package com.example.jatelobank.ActivityWindow.Reports;

import lombok.Data;

import java.sql.Date;


@Data
public class TransfersUser {
    private String accountNumber;
    private double amount;
    private Date date;
    private String accountReceiving;
    private String transferPurpose;
    private String beneficiaryName;
    private String beneficiaryEmail;

    public TransfersUser(String accountNumber, double amount, Date date, String accountReceiving, String transferPurpose, String beneficiaryName, String beneficiaryEmail) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.date = date;
        this.accountReceiving = accountReceiving;
        this.transferPurpose = transferPurpose;
        this.beneficiaryName = beneficiaryName;
        this.beneficiaryEmail = beneficiaryEmail;
    }
}
