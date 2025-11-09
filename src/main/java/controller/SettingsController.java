package controller;

import core.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.ComboBox;

public class SettingsController {

    @FXML private Slider volumeSlider;
    @FXML private ComboBox<String> difficultyCombo;

    public void initialize() {
        // Load settings đã lưu (nếu có)
        if (difficultyCombo != null) {
            difficultyCombo.getItems().addAll("Easy", "Normal", "Hard");
            difficultyCombo.setValue("Normal");
        }

        if (volumeSlider != null) {
            // Có thể thêm listener để lưu volume
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                System.out.println("Volume: " + newVal.intValue() + "%");
                // TODO: Áp dụng volume vào game
            });
        }
    }

    @FXML
    private void backToMenu() {
        try {
            MainApp.showMainMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}