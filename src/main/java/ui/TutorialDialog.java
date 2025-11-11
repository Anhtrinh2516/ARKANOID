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
            // 1. Tải FXML
            FXMLLoader loader = new FXMLLoader(TutorialDialog.class.getResource("/fxml/TutorialDialog.fxml"));
            Pane root = loader.load();

            // 2. Tạo Stage (Cửa sổ)
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.TRANSPARENT); // Cửa sổ trong suốt
            dialog.setTitle("How to Play");

            // 3. Tạo Scene
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT); // Nền scene trong suốt

            // 4. Lấy Controller và truyền Stage vào (để Controller có thể tự đóng)
            TutorialDialogController controller = loader.getController();
            controller.setStage(dialog);

            // 5. Hiển thị
            dialog.setScene(scene);
            dialog.showAndWait();

        } catch (IOException e) {
            System.err.println("Không thể tải TutorialDialog.fxml!");
            e.printStackTrace();
        }
    }
}