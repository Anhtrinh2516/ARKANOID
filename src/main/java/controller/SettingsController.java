package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.ComboBox;
import core.MainApp;

public class SettingsController {

    @FXML private Slider volumeSlider;
    @FXML private ComboBox<String> difficultyCombo;

    public void initialize() {
        if (difficultyCombo != null) {
            difficultyCombo.getItems().addAll("Easy", "Normal", "Hard");
            difficultyCombo.setValue("Normal");
        }

        if (volumeSlider != null) {
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                System.out.println("Volume: " + newVal.intValue() + "%");
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