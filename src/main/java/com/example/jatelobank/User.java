package com.example.jatelobank;

import lombok.Data;

@Data
public class User {
    private String userName;
    private String accNo;
    private double CheckingAmount;
    private double SavingsAmount;
    private double BudgetAmount;
    private double income;
    private double expense;

    public User(String userName, String accNo, double Checking, double savingsAmount, double BudgetAmount, double income, double expense) {

        this.userName = userName;
        this.accNo = accNo;
        this.CheckingAmount = Checking;
        this.SavingsAmount = savingsAmount;
        this.BudgetAmount = BudgetAmount;
        this.income = income;
        this.expense = expense;
    }
    //lombok annotation generates the getters and setters (@Data)
}
