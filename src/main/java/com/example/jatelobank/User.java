package com.example.jatelobank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String userName;
    private String accNo;
    private double CheckingAmount;
    private double SavingsAmount;
    private double BudgetAmount;
    private double income;
    private double expense;

    /*public User(String userName, String accNo, double check, double savingsAmount, double budgetAmount, double income, double expense) {

        this.userName = userName;
        this.accNo = accNo;
        this.CheckingAmount = check;
        this.SavingsAmount = savingsAmount;
        this.BudgetAmount = budgetAmount;
        this.income = income;
        this.expense = expense;
    }**/

    //lombok annotation generates the getters and setters (@Data)
}
