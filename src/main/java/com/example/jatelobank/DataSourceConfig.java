package com.example.jatelobank;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Configuration(proxyBeanMethods = false)
public class DataSourceConfig {


    public DataSource dataSource(){
        HikariDataSource dataSource = new HikariDataSource();
                dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
                dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/Grace_Bank");
                dataSource.setUsername("root");
                dataSource.setPassword("Jatelo");
                return dataSource;
    }
}
