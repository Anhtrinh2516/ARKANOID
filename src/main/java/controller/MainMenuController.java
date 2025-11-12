package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import core.GameController;
import core.MainApp;

public class MainMenuController {

    @FXML private Button startButton;
    @FXML private Button continueButton;
    @FXML private Button shopButton;
    @FXML private Button settingsButton;
    @FXML private Button leaderboardButton;
    @FXML private Button helpButton;
    @FXML private Button exitButton;

    // Khai báo các Circle levels
    @FXML private Circle level1, level2, level3, level4, level5, level6;
    @FXML private Circle level7, level8, level9, level10, level11, level12;
    @FXML private Circle level13, level14, level15, level16;

    // Khai báo các Label levels
    @FXML private Label label1, label2, label3, label4, label5, label6;
    @FXML private Label label7, label8, label9, label10, label11, label12;
    @FXML private Label label13, label14, label15, label16;

    public void initialize() {
        updateContinue();
        SoundManager.INSTANCE.playMenuMusic();
    }

    private void updateContinue() {
        if (continueButton == null) return;
        
        boolean has = GameStateManager.INSTANCE.hasGameInProgress();
        continueButton.setDisable(!has);
        continueButton.setOpacity(has ? 1.0 : 0.5);

        if (has) {
            int lvl = GameStateManager.INSTANCE.getSavedLevel();
            continueButton.setText("▶ Continue (Lvl " + lvl + ")");
        } else {
            continueButton.setText("▶ Continue");
        }
    }

    @FXML
    private void onLevelClicked(MouseEvent event) {
        try {
            Object src = event.getSource();
            int lvl = 1;

            if (src instanceof Circle) {
                lvl = getLvlCircle((Circle) src);
            } else if (src instanceof Label) {
                lvl = getLvlLabel((Label) src);
            }

            startGameWithLevel(lvl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getLvlCircle(Circle circle) {
        if (circle == level1) return 1;
        if (circle == level2) return 2;
        if (circle == level3) return 3;
        if (circle == level4) return 4;
        if (circle == level5) return 5;
        if (circle == level6) return 6;
        if (circle == level7) return 7;
        if (circle == level8) return 8;
        if (circle == level9) return 9;
        if (circle == level10) return 10;
        if (circle == level11) return 11;
        if (circle == level12) return 12;
        if (circle == level13) return 13;
        if (circle == level14) return 14;
        if (circle == level15) return 15;
        if (circle == level16) return 16;
        return 1;
    }

    private int getLvlLabel(Label label) {
        if (label == label1) return 1;
        if (label == label2) return 2;
        if (label == label3) return 3;
        if (label == label4) return 4;
        if (label == label5) return 5;
        if (label == label6) return 6;
        if (label == label7) return 7;
        if (label == label8) return 8;
        if (label == label9) return 9;
        if (label == label10) return 10;
        if (label == label11) return 11;
        if (label == label12) return 12;
        if (label == label13) return 13;
        if (label == label14) return 14;
        if (label == label15) return 15;
        if (label == label16) return 16;
        return 1;
    }

    private void startGameWithLevel(int level) throws Exception {
        GameStateManager.INSTANCE.clear();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Game.fxml"));
        Parent root = loader.load();

        // Get game controller and start selected level
        GameController controller = loader.getController();
        controller.startLevel(level);

        // Switch scene
        Stage stage = (Stage) shopButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Arkanoid - Level " + level);
    }

    @FXML
    private void startGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SelectMode.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) startButton.getScene().getWindow();
            if (stage.getScene() != null) {
                stage.getScene().setRoot(root);
            } else {
                stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void continueGame() {
        try {
            // Tiếp tục game đã lưu
            if (GameStateManager.INSTANCE.hasGameInProgress()) {
                int savedLevel = GameStateManager.INSTANCE.getSavedLevel();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Game.fxml"));
                Parent root = loader.load();

                GameController controller = loader.getController();
                controller.startLevel(savedLevel, true); // true = continue game

                Stage stage = (Stage) shopButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Arkanoid - Level " + savedLevel + " (Continued)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openShop() {
        try {
            MainApp.showShop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openSettings() {
        try {
            MainApp.showSettings();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openLeaderboard() {
        ui.LeaderboardDialog.show();
    }

    @FXML
    private void openHelp() {
        ui.TutorialDialog.show();
    }

    @FXML
    private void exitGame() {
        System.exit(0);
    }
}
