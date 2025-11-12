package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ComingSoonDialog {

    public static void show(Stage owner) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setTitle("Coming Soon");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50, 70, 50, 70));
        root.setPrefWidth(500);
        root.setPrefHeight(350);
        root.setStyle(StyleConstants.GRADIENT_BORDER_STYLE);
        root.setEffect(StyleConstants.createGlowEffect());

        Label title = new Label("COMING SOON");
        title.setFont(Font.font("System", FontWeight.BOLD, 42));
        title.setTextFill(Color.web(StyleConstants.COLOR_CYAN));
        title.setEffect(StyleConstants.createButtonGlow(Color.web(StyleConstants.COLOR_CYAN), 15));

        Label subtitle = new Label("This feature will be available soon!");
        subtitle.setFont(Font.font("System", FontWeight.NORMAL, 18));
        subtitle.setTextFill(Color.web("#cbd5e1"));

        Button okBtn = new Button("OK");
        okBtn.setFont(Font.font("System", FontWeight.BOLD, 16));
        okBtn.setPrefWidth(150);
        okBtn.setStyle(StyleConstants.BUTTON_PRIMARY_STYLE);
        okBtn.setEffect(StyleConstants.createButtonGlow(Color.rgb(34, 197, 94, 0.6), 12));

        okBtn.setOnMouseEntered(e -> okBtn.setStyle(StyleConstants.BUTTON_PRIMARY_HOVER_STYLE));
        okBtn.setOnMouseExited(e -> okBtn.setStyle(StyleConstants.BUTTON_PRIMARY_STYLE));
        okBtn.setOnAction(e -> dialog.close());

        root.getChildren().addAll(title, subtitle, okBtn);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}
