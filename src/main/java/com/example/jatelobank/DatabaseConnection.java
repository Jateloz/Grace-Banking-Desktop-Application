package com.example.jatelobank;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;

@Component
public class DatabaseConnection implements AutoCloseable{
    public java.sql.Connection conn;

    public Connection getConn() {
        String url= "jdbc:mysql://localhost:3306/Grace_Bank";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url,"root","Jatelo");
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }

        return conn;
    }

    @Override
    public void close() throws Exception {
        if (conn != null && !conn.isClosed()){
            conn.close();
        }
    }
}
