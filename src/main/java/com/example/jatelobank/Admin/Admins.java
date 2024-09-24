package com.example.jatelobank.Admin;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity
public class Admins {

    @Id
    private int ID;
    private String FirstName;
    private String LastName;
    private String Email;
    private String PhoneNumber;
    private String Age;


    public Admins() {
    }

    public Admins(String firstName, String lastName, String email, String phoneNumber, String age) {
        FirstName = firstName;
        LastName = lastName;
        Email = email;
        PhoneNumber = phoneNumber;
        Age = age;
    }
}
