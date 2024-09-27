package com.example.jatelobank.ActivityWindow;

import com.example.jatelobank.SessionManager;
import com.example.jatelobank.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class ShareIBANController implements Initializable {
    public FontAwesomeIconView whatsappButton;
    public FontAwesomeIconView instagramButton;
    public FontAwesomeIconView xButton;
    public FontAwesomeIconView facebookButton;
    public FontAwesomeIconView emailButton;
    public FontAwesomeIconView telegramButton;
    public FontAwesomeIconView skypeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void whatsappButt(MouseEvent mouseEvent) throws Exception {
        User current = SessionManager.getInstance().getCurrentUser();
        if(current != null){
            String acc = current.getAccNo();
            String name = current.getUserName();


            String message = "Account Number : "+acc +"\n"+"Account holder names : "+name;
            String messageEncode = java.net.URLEncoder.encode(message, StandardCharsets.UTF_8);

            String url = "https://api.whatsapp.com/send?text="+messageEncode;

            openWebpage(url);

        }
    }

    public void instagramButt(MouseEvent mouseEvent) throws Exception {
        User current = SessionManager.getInstance().getCurrentUser();
        if(current != null){
            String acc = current.getAccNo();
            String name = current.getUserName();


            String message = "Account Number : "+acc +"\n"+"Account holder names : "+name;
            String messageEncode = java.net.URLEncoder.encode(message, StandardCharsets.UTF_8);

            String url = "https://api.instagram.com/send?text="+messageEncode;

            openWebpage(url);

        }
    }

    public void xButt(MouseEvent mouseEvent) throws Exception {
        User current = SessionManager.getInstance().getCurrentUser();
        if(current != null){
            String acc = current.getAccNo();
            String name = current.getUserName();


            String message = "Account Number : "+acc +"\n"+"Account holder names : "+name;
            String messageEncode = java.net.URLEncoder.encode(message, StandardCharsets.UTF_8);

            String url = "https://twitter.com/intent/tweet?text="+messageEncode;

            openWebpage(url);

        }
    }

    public void facebookButt(MouseEvent mouseEvent) throws Exception {
        User current = SessionManager.getInstance().getCurrentUser();
        if(current != null){
            String acc = current.getAccNo();
            String name = current.getUserName();


            String message = "Account Number : "+acc +"\n"+"Account holder names : "+name;
            String messageEncode = java.net.URLEncoder.encode(message, StandardCharsets.UTF_8);

            String url = "https://facebook.com/sharer/sharer.php?u="+messageEncode;

            openWebpage(url);

        }
    }

    public void emailButt(MouseEvent mouseEvent) throws Exception {
        User current = SessionManager.getInstance().getCurrentUser();
        if(current != null){
            String acc = current.getAccNo();
            String name = current.getUserName();


            String message = "Account Number : "+acc +"\n"+"Account holder names : "+name;
            String messageEncode = java.net.URLEncoder.encode(message, StandardCharsets.UTF_8);

            String url = "mailto:?subject=Banking-Information&body="+messageEncode;

            openWebpage(url);
        }
    }

    public void telegramButt(MouseEvent mouseEvent) throws Exception {
        User current = SessionManager.getInstance().getCurrentUser();
        if(current != null){
            String acc = current.getAccNo();
            String name = current.getUserName();


            String message = "Account Number : "+acc +"\n"+"Account holder names : "+name;
            String messageEncode = java.net.URLEncoder.encode(message, StandardCharsets.UTF_8);

            String url = "https://t.me/share/url?url=Jateloz&text="+messageEncode;

            openWebpage(url);

        }
    }

    public void skypeButt(MouseEvent mouseEvent) throws Exception {
        User current = SessionManager.getInstance().getCurrentUser();
        if(current != null){
            String acc = current.getAccNo();
            String name = current.getUserName();


            String message = "Account Number : "+acc +"\n"+"Account holder names : "+name;
            String messageEncode = java.net.URLEncoder.encode(message, StandardCharsets.UTF_8);

            String url = "https://web.skype.com/share?url=Jatelo&text="+messageEncode;

            openWebpage(url);

        }
    }

    //make the platform to be desktop supported first to open the web browser
    public void openWebpage(String url) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            // Windows
            Runtime.getRuntime().exec(new String[] {"rundll32", "url.dll,FileProtocolHandler", url});
        } else if (os.contains("mac")) {
            // macOS
            Runtime.getRuntime().exec(new String[] {"open", url});
        } else if (os.contains("nix") || os.contains("nux")) {
            // Linux or Unix-based OS
            Runtime.getRuntime().exec(new String[] {"xdg-open", url});
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + os);
        }
    }
}
