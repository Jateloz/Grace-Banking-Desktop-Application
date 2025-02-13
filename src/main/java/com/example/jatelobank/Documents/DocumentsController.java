package com.example.jatelobank.Documents;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.ResourceBundle;


public class DocumentsController implements Initializable {


    public ScrollPane pdfScrollPane;
    public VBox pdfVBox;
    public FontAwesomeIconView backButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pdfScrollPane.widthProperty().addListener((obs, oldVal, newVal) -> resizeImages(newVal.doubleValue()));

    }

    private void resizeImages(double newWidth) {

        for (var node : pdfVBox.getChildren()) {
            if (node instanceof ImageView) {
                ((ImageView) node).setFitWidth(newWidth - 20);
            }
        }
    }


    public void openDoc(String resourcePath){

        if(resourcePath.toLowerCase().endsWith(".pdf")){
            openPDF(resourcePath);
        }else {
            System.out.println("Unsupported file format");
        }
    }

    @SneakyThrows
    private void openPDF(String resourcePath) {
        try(InputStream inputStream = getClass().getResourceAsStream(resourcePath)){
            if (inputStream == null){
                System.err.println("File not found"+resourcePath);
            }

            //create temporary file for the pdf
            File temp = File.createTempFile("temp_pdf_",".pdf");
            Files.copy(inputStream,temp.toPath(),StandardCopyOption.REPLACE_EXISTING);

            //load the doc using pdfBox
            PDDocument pdDocument = PDDocument.load(temp);
            PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);

            //clear previous pdf pages
            pdfVBox.getChildren().clear();

            //render all pages as images
            final float dpi = 300;

            for (int i = 0; i < pdDocument.getNumberOfPages(); i++){
                BufferedImage bufferedImage  = pdfRenderer.renderImageWithDPI(i,dpi);
                Image image = SwingFXUtils.toFXImage(bufferedImage,null);
                ImageView imageView = new ImageView(image);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setFitWidth(pdfScrollPane.getWidth() - 20);

                pdfVBox.getChildren().add(imageView);

            }
            pdDocument.close();

            pdfScrollPane.setVvalue(0.0);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public void backButt(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Activity/FinancialLiteracy.fxml"));
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/alfons-morales-YLSwjSy7stw-unsplash.jpg")));
        stage.getIcons().add(img);
        stage.setTitle("Grace Bank");
        stage.show();

        Stage stage1 = (Stage) backButton.getScene().getWindow();
        stage1.close();
    }
}
