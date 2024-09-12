package com.example.jatelobank.Settings;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

@Component
public class NotificationController implements Initializable {
    public MenuButton so1Button;
    public MenuButton so2Button;
    public MenuButton so3Button;
    public MenuButton so4Button;
    public ToggleGroup butt;
    public ToggleButton onToggleButton;
    public ToggleButton offToggleButton;
    public AudioClip audioClip;
    public boolean soundAlert = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        onToggleButton.setToggleGroup(butt);
        offToggleButton.setToggleGroup(butt);

        // Initialize menu item actions
        for (MenuItem item : so1Button.getItems()) {
            item.setOnAction(event -> handleMenuSelection(so1Button, item));
        }
        for (MenuItem item : so2Button.getItems()) {
            item.setOnAction(event -> handleMenuSelection(so2Button, item));
        }
        for (MenuItem item : so3Button.getItems()) {
            item.setOnAction(event -> handleMenuSelection(so3Button, item));
        }
        for (MenuItem item : so4Button.getItems()) {
            item.setOnAction(event -> handleMenuSelection(so4Button, item));
        }

        audioClip = new AudioClip(Objects.requireNonNull(getClass().getResource("/Images/Fally Ipupa - 100 (Clip officiel).mp3")).toString());

        // Set default ToggleButton selection
        onToggleButton.setSelected(true);
        soundAlert = true; // Enable sound alerts by default
    }

    public void so1Butt(ActionEvent event) {

        audioClip.play();
    }

    public void so2Butt(ActionEvent event) {

    }

    public void so3Butt(ActionEvent event) {

    }

    public void so4Butt(ActionEvent event) {

    }

    public void onToggleButt(ActionEvent event) {

        if (onToggleButton.isSelected()){
            soundAlert = true;
            audioClip.play();

            System.out.println("Alerts are turned on");
        }
    }

    public void offToggleButt(ActionEvent event) {

        if (offToggleButton.isSelected()){
            soundAlert = false;
            audioClip.stop();
            System.out.println("Alerts are turned off");

        }
    }

    private void handleMenuSelection(MenuButton menuButton, MenuItem selectedItem) {
        System.out.println(menuButton.getText() + " selected: " + selectedItem.getText());
        menuButton.setText(selectedItem.getText());

        triggerNotification();
    }

    private void triggerNotification() {
        if (soundAlert) {
            playNotificationSound();
        } else {
            System.out.println("Notification received, but sound alerts are disabled.");
        }
    }

    // Method to play the notification sound
    private void playNotificationSound() {
        if (audioClip != null) {
            audioClip.play();

            // Create a PauseTransition to stop the sound after 5 seconds
            PauseTransition pause = new PauseTransition(Duration.seconds(3));
            pause.setOnFinished(event -> stopNotificationSound());
            pause.play();

            System.out.println("Playing notification sound...");
        } else {
            System.out.println("Error: Sound file not found.");
        }
    }

    private void stopNotificationSound() {
        // Stop the sound
        if (audioClip.isPlaying()) {
            audioClip.stop();
        }
    }
}
