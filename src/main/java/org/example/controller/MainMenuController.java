package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.example.MainApp;

public class MainMenuController {

    @FXML private Button playButton;
    @FXML private Button eventButton;
    @FXML private Button shopButton;
    @FXML private Button skinButton;
    @FXML private Button settingsButton;
    @FXML private Button exitButton;

    public void initialize() {
        // Play - Đến màn chọn level
        playButton.setOnAction(e -> {
            try {
                MainApp.showLevelSelect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Event - Để sau này thêm chế độ chơi sự kiện
        eventButton.setOnAction(e -> {
            System.out.println("EVENT mode - Coming soon!");
            // TODO: Thêm chế độ chơi Event sau
        });

        // Shop
        shopButton.setOnAction(e -> {
            try {
                MainApp.showShop();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Skins
        skinButton.setOnAction(e -> {
            try {
                MainApp.showSkins();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Settings
        settingsButton.setOnAction(e -> {
            try {
                MainApp.showSettings();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Exit
        exitButton.setOnAction(e -> System.exit(0));
    }
}
