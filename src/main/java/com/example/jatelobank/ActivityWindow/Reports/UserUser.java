package com.example.jatelobank.ActivityWindow.Reports;


import lombok.Data;

@Data
public class UserUser {
    private String userName;
    private String accountNumber;
    private double checkingAmount;
    private double savingsAmount;
    private double budgetAmount;
    private double income;
    private double expense;

    public UserUser(String userName, String accountNumber, double checkingAmount, double savingsAmount, double budgetAmount, double income, double expense) {
        this.userName = userName;
        this.accountNumber = accountNumber;
        this.checkingAmount = checkingAmount;
        this.savingsAmount = savingsAmount;
        this.budgetAmount = budgetAmount;
        this.income = income;
        this.expense = expense;
    }
}
