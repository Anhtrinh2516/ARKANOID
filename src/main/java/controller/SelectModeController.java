package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import core.GameController;
import ui.ComingSoonDialog;

public class SelectModeController {

    @FXML private Button backButton;

    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
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
    private void openStory(MouseEvent event) {
        try {
            GameStateManager.INSTANCE.clear();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Game.fxml"));
            Parent root = loader.load();

            GameController controller = loader.getController();
            controller.startLevel(1);

            Stage stage = (Stage) backButton.getScene().getWindow();
            if (stage.getScene() != null) {
                stage.getScene().setRoot(root);
            } else {
                stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            }
            stage.setTitle("Arkanoid - Level 1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openEvent(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SelectEvent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
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
    private void openClassic(MouseEvent event) {
        try {
            GameStateManager.INSTANCE.clear();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Game.fxml"));
            Parent root = loader.load();

            GameController controller = loader.getController();
            controller.startClassicMode();

            Stage stage = (Stage) backButton.getScene().getWindow();
            if (stage.getScene() != null) {
                stage.getScene().setRoot(root);
            } else {
                stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            }
            stage.setTitle("Arkanoid - Classic Mode");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void openAsia(MouseEvent event) {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            ComingSoonDialog.show(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
