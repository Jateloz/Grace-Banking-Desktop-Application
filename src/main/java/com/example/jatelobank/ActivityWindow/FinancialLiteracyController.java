package com.example.jatelobank.ActivityWindow;

import com.example.jatelobank.Documents.DocumentsController;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class FinancialLiteracyController implements Initializable {
    public ScrollPane scrollBar;
    public VBox SIOneButton;
    public VBox SIOTwoButton;
    public VBox SIOThreeButton;
    public VBox SIOFourButton;
    public VBox MME1Button;
    public VBox MME2Button;
    public VBox MME3Button;
    public VBox MME4Button;
    public VBox MT1Button;
    public VBox MT2Button;
    public VBox MT3Button;
    public VBox MT4Button;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @SneakyThrows
    public void SIOneButt(MouseEvent mouseEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Documents/Documents.fxml"));
        Parent root = fxmlLoader.load();

        DocumentsController documentsController = fxmlLoader.getController();

        documentsController.openDoc("/Documents/sec-guide-to-savings-and-investing.pdf");
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);
        stage.setTitle("Grace Bank");
        stage.show();

        //closing the previous stage
        //Stage stage1 = (Stage) SIOneButton.getScene().getWindow();
        //stage1.close();
    }

    @SneakyThrows
    public void SIOTwoButt(MouseEvent mouseEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Documents/Documents.fxml"));
        Parent root = fxmlLoader.load();

        DocumentsController documentsController = fxmlLoader.getController();

        documentsController.openDoc("/Documents/inv-beg-guide-row.pdf");
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);
        stage.setTitle("Grace Bank");
        stage.show();
    }

    @SneakyThrows
    public void SIOThreeButt(MouseEvent mouseEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Documents/Documents.fxml"));
        Parent root = fxmlLoader.load();

        DocumentsController documentsController = fxmlLoader.getController();

        documentsController.openDoc("/Documents/Learn to Earn_ A Beginner's Guide to the Basics of Investing and Business ( PDFDrive ).pdf");
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);
        stage.setTitle("Grace Bank");
        stage.show();
    }

    @SneakyThrows
    public void SIOFourButt(MouseEvent mouseEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Documents/Documents.fxml"));
        Parent root = fxmlLoader.load();

        DocumentsController documentsController = fxmlLoader.getController();

        documentsController.openDoc("/Documents/foundations-of-investing-guidebook.pdf");
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);
        stage.setTitle("Grace Bank");
        stage.show();
    }

    @SneakyThrows
    public void MME1Butt(MouseEvent mouseEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Documents/Documents.fxml"));
        Parent root = fxmlLoader.load();

        DocumentsController documentsController = fxmlLoader.getController();

        documentsController.openDoc("/Documents/leopard.pdf");
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);
        stage.setTitle("Grace Bank");
        stage.show();
    }

    @SneakyThrows
    public void MME2Butt(MouseEvent mouseEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Documents/Documents.fxml"));
        Parent root = fxmlLoader.load();

        DocumentsController documentsController = fxmlLoader.getController();

        documentsController.openDoc("/Documents/The Total Money Makeover - Dave Ramsey.pdf");
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);
        stage.setTitle("Grace Bank");
        stage.show();
    }

    @SneakyThrows
    public void MME3Butt(MouseEvent mouseEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Documents/Documents.fxml"));
        Parent root = fxmlLoader.load();

        DocumentsController documentsController = fxmlLoader.getController();

        documentsController.openDoc("/Documents/Personal-Finance-Management-Handbook.pdf");
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);
        stage.setTitle("Grace Bank");
        stage.show();
    }

    @SneakyThrows
    public void MME4Butt(MouseEvent mouseEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Documents/Documents.fxml"));
        Parent root = fxmlLoader.load();

        DocumentsController documentsController = fxmlLoader.getController();

        documentsController.openDoc("/Documents/The-Money-Manual.pdf");
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);
        stage.setTitle("Grace Bank");
        stage.show();
    }

    @SneakyThrows
    public void MT1Butt(MouseEvent mouseEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Documents/Documents.fxml"));
        Parent root = fxmlLoader.load();

        DocumentsController documentsController = fxmlLoader.getController();

        documentsController.openDoc("/Documents/GPO-FCIC.pdf");
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);
        stage.setTitle("Grace Bank");
        stage.show();
    }

    @SneakyThrows
    public void MT2Butt(MouseEvent mouseEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Documents/Documents.fxml"));
        Parent root = fxmlLoader.load();

        DocumentsController documentsController = fxmlLoader.getController();

        documentsController.openDoc("/Documents/40-Money-Management-Tips.pdf");
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);
        stage.setTitle("Grace Bank");
        stage.show();
    }

    @SneakyThrows
    public void MT3Butt(MouseEvent mouseEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Documents/Documents.fxml"));
        Parent root = fxmlLoader.load();

        DocumentsController documentsController = fxmlLoader.getController();

        documentsController.openDoc("/Documents/mhm-toolkit-pdf-editable-version.pdf");
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);
        stage.setTitle("Grace Bank");
        stage.show();
    }

    @SneakyThrows
    public void MT4Butt(MouseEvent mouseEvent) {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Documents/Documents.fxml"));
        Parent root = fxmlLoader.load();

        DocumentsController documentsController = fxmlLoader.getController();

        documentsController.openDoc("/Documents/Money-Youth-2018-EN.pdf");
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);
        stage.setTitle("Grace Bank");
        stage.show();
    }
}
