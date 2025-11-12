package controller;

import core.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.ComboBox;

public class SettingsController {

    @FXML
    private Slider soundVolumeSlider;
    @FXML
    private Slider musicVolumeSlider;
    @FXML
    private CheckBox soundEnabledCheckbox;
    @FXML
    private CheckBox musicEnabledCheckbox;
    @FXML
    private ComboBox<String> difficultyCombo;

    public void initialize() {
        // Kiểm tra null trước khi sử dụng
        if (soundVolumeSlider != null) {
            soundVolumeSlider.setValue(SoundManager.INSTANCE.getSoundVolume() * 100);
            soundVolumeSlider.valueProperty().addListener((obs, old, val) -> {
                SoundManager.INSTANCE.setSoundVolume(val.doubleValue() / 100);
            });
        }

        if (musicVolumeSlider != null) {
            musicVolumeSlider.setValue(SoundManager.INSTANCE.getMusicVolume() * 100);
            musicVolumeSlider.valueProperty().addListener((obs, old, val) -> {
                SoundManager.INSTANCE.setMusicVolume(val.doubleValue() / 100);
            });
        }

        // Sound enabled checkbox
        if (soundEnabledCheckbox != null) {
            soundEnabledCheckbox.setSelected(SoundManager.INSTANCE.isSoundEnabled());
            soundEnabledCheckbox.selectedProperty().addListener((obs, old, val) -> {
                SoundManager.INSTANCE.setSoundEnabled(val);
                if (soundVolumeSlider != null) {
                    soundVolumeSlider.setDisable(!val);
                }
            });
            if (soundVolumeSlider != null) {
                soundVolumeSlider.setDisable(!soundEnabledCheckbox.isSelected());
            }
        }

        // Music enabled checkbox
        if (musicEnabledCheckbox != null) {
            musicEnabledCheckbox.setSelected(SoundManager.INSTANCE.isMusicEnabled());
            musicEnabledCheckbox.selectedProperty().addListener((obs, old, val) -> {
                SoundManager.INSTANCE.setMusicEnabled(val);
                if (musicVolumeSlider != null) {
                    musicVolumeSlider.setDisable(!val);
                }
            });
            if (musicVolumeSlider != null) {
                musicVolumeSlider.setDisable(!musicEnabledCheckbox.isSelected());
            }
        }

        // Difficulty combo box
        if (difficultyCombo != null) {
            difficultyCombo.setValue("Normal");
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