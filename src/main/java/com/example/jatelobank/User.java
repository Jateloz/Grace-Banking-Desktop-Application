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
}
