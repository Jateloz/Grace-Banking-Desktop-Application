package com.example.jatelobank;

import lombok.Data;

@Data
public class Item {
    private String BenefiniciaryName;
    private String Account;
    private Double amount;

    public Item(String beneficiaryName, String accountReceiving, double amount) {

    }
}
