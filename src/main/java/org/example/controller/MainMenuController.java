package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.GameController;
import org.example.MainApp;

public class MainMenuController {

    @FXML private Circle level1, level2, level3, level4, level5, level6, level7, level8;
    @FXML private Circle level9, level10, level11, level12, level13, level14, level15, level16;

    @FXML private Label label1, label2, label3, label4, label5, label6, label7, label8;
    @FXML private Label label9, label10, label11, label12, label13, label14, label15, label16;

    @FXML private Button shopButton, settingsButton, exitButton;

    public void initialize() {
        // Level 1-8
        level1.setOnMouseClicked(e -> goToLevel(1));
        level2.setOnMouseClicked(e -> goToLevel(2));
        level3.setOnMouseClicked(e -> goToLevel(3));
        level4.setOnMouseClicked(e -> goToLevel(4));
        level5.setOnMouseClicked(e -> goToLevel(5));
        level6.setOnMouseClicked(e -> goToLevel(6));
        level7.setOnMouseClicked(e -> goToLevel(7));
        level8.setOnMouseClicked(e -> goToLevel(8));

        // Level 9-16
        level9.setOnMouseClicked(e -> goToLevel(9));
        level10.setOnMouseClicked(e -> goToLevel(10));
        level11.setOnMouseClicked(e -> goToLevel(11));
        level12.setOnMouseClicked(e -> goToLevel(12));
        level13.setOnMouseClicked(e -> goToLevel(13));
        level14.setOnMouseClicked(e -> goToLevel(14));
        level15.setOnMouseClicked(e -> goToLevel(15));
        level16.setOnMouseClicked(e -> goToLevel(16));

        // Menu buttons
        shopButton.setOnAction(e -> openShop());
        settingsButton.setOnAction(e -> openSettings());
        exitButton.setOnAction(e -> System.exit(0));
    }

    @FXML
    private void onLevelClicked(MouseEvent event) {
        Object src = event.getSource();

        // Levels 1-8
        if (src == level1 || src == label1) goToLevel(1);
        else if (src == level2 || src == label2) goToLevel(2);
        else if (src == level3 || src == label3) goToLevel(3);
        else if (src == level4 || src == label4) goToLevel(4);
        else if (src == level5 || src == label5) goToLevel(5);
        else if (src == level6 || src == label6) goToLevel(6);
        else if (src == level7 || src == label7) goToLevel(7);
        else if (src == level8 || src == label8) goToLevel(8);

            // Levels 9-16
        else if (src == level9 || src == label9) goToLevel(9);
        else if (src == level10 || src == label10) goToLevel(10);
        else if (src == level11 || src == label11) goToLevel(11);
        else if (src == level12 || src == label12) goToLevel(12);
        else if (src == level13 || src == label13) goToLevel(13);
        else if (src == level14 || src == label14) goToLevel(14);
        else if (src == level15 || src == label15) goToLevel(15);
        else if (src == level16 || src == label16) goToLevel(16);
    }

    private void goToLevel(int index) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sample.fxml"));
            Parent root = loader.load();

            // Gọi controller của game
            GameController controller = loader.getController();
            controller.startLevel(index);

            // Đổi scene
            Stage stage = (Stage) level1.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Arkanoid - Level " + index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openShop() {
        try {
            MainApp.showShop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openSettings() {
        try {
            MainApp.showSettings();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}