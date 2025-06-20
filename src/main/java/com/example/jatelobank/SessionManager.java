package com.example.jatelobank;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

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
