package com.example.jatelobank;

import lombok.Data;

@Data
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    //private  SessionManager(){}

    public static SessionManager getInstance() {
        if (instance == null){
            instance = new SessionManager();
        }
        return instance;
    }

    public void login(User user){
        this.currentUser=user;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void logout(){
        currentUser = null;
    }
}
