package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SelectEventController {

    @FXML private ScrollPane eventScrollPane;
    @FXML private HBox eventChapterBox;
    @FXML private Button backButton;
    @FXML private Text userInfoText;

    public void initialize() {
        setBackground();
        makeScrollBarNice();
        loadChapters();
    }

    private void setBackground() {
        eventScrollPane.getParent().setStyle(
            "-fx-background-image: url('/event_background/event_background.png');" +
            "-fx-background-size: cover;" +
            "-fx-background-position: center;"
        );
    }

    private void makeScrollBarNice() {
        eventScrollPane.setStyle(
            "-fx-background: transparent;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;"
        );
    }

    private void loadChapters() {
        String[] names = {"Universe", "Treasure Hunter", "Castle Attack", "Casino", "Penaldo", "Coming Soon"};
        String[] images = {
            "/event_background/universe_background.png",
            "/event_background/deepsea_background.png",
            "/event_background/castle_background.png",
            "/event_background/casino_background.png",
            "/event_background/penalty_background.png",
            "/event_background/comingsoon_background.png"
        };
        
        for (int i = 0; i < names.length; i++) {
            VBox box = makeChapterBox(names[i], images[i], i + 1);
            eventChapterBox.getChildren().add(box);
        }
    }

    private VBox makeChapterBox(String name, String imagePath, int number) {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(15);
        box.setPrefSize(280, 420);
        box.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-background-radius: 12;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 15, 0, 0, 5);" +
            "-fx-cursor: hand;"
        );
        
        StackPane imageBox = new StackPane();
        imageBox.setPrefSize(260, 360);
        imageBox.setStyle(
            "-fx-background-image: url('" + imagePath + "');" +
            "-fx-background-size: cover;" +
            "-fx-background-position: center;" +
            "-fx-background-radius: 12;"
        );
        
        Rectangle corners = new Rectangle(260, 360);
        corners.setArcWidth(24);
        corners.setArcHeight(24);
        imageBox.setClip(corners);
        corners.widthProperty().bind(imageBox.widthProperty());
        corners.heightProperty().bind(imageBox.heightProperty());

        Label nameLabel = new Label(name);
        nameLabel.setStyle(
            "-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 6 12;" +
            "-fx-background-color: rgba(0,0,0,0.4);" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 6, 0, 0, 2);"
        );
        StackPane.setAlignment(nameLabel, Pos.BOTTOM_CENTER);
        StackPane.setMargin(nameLabel, new Insets(0, 0, 10, 0));

        imageBox.getChildren().add(nameLabel);
        box.getChildren().add(imageBox);
        
        box.setOnMouseClicked(e -> openChapter(number));
        
        return box;
    }

    private void openChapter(int num) {
        System.out.println("Mở chapter " + num);
    }


    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
