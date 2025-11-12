package ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class TutorialDialog {

    public static void show() {
        try {
            FXMLLoader loader = new FXMLLoader(TutorialDialog.class.getResource("/fxml/TutorialDialog.fxml"));
            Pane root = loader.load();

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.TRANSPARENT); 
            dialog.setTitle("How to Play");

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT); 

            TutorialDialogController controller = loader.getController();
            controller.setStage(dialog);

            dialog.setScene(scene);
            dialog.showAndWait();

        } catch (IOException e) {
            System.err.println("Không thể tải TutorialDialog.fxml!");
            e.printStackTrace();
        }
    }
}