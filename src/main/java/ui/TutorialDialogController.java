package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller cho TutorialDialog.fxml.
 * Xử lý logic.
 */
public class TutorialDialogController {

    @FXML
    private Button closeButton;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleClose() {
        if (stage != null) {
            stage.close();
        }
    }
}