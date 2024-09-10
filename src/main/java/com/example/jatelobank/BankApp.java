package com.example.jatelobank;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BankApp extends Application{
    private static ConfigurableApplicationContext context;


    @Override
    public void start(Stage stage) {

        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/LoginWindow.fxml"));
            Parent root = fxmlLoader.load();

            javafx.scene.image.Image img = new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Grace Bank");
            stage.getIcons().add(img);
            stage.setResizable(false);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
            launch();

    }

    @Override
    public void init(){
            context = new SpringApplicationBuilder(SpringBootApp.class).run();

    }

    @Override
    public void stop(){
        context.close();
        
    }
}
