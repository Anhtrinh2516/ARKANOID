package controller;

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
import core.MainApp;

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
            "-fx-background-image: url('/backgrounds/event_background.png');" +
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
        String[] names = {"Universe", "Treasure Hunter", "Castle Attack", "Penaldo", "Casino", "Coming Soon"};
        String[] images = {
            "/event/backgrounds/universe_background.png",
            "/event/backgrounds/treasure_hunter_background.png",
            "/event/backgrounds/castle_background.png",
            "/event/backgrounds/penaldo_background.png",
            "/event/backgrounds/casino_background.png",
            "/event/backgrounds/comingsoon_background.png"
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
        
        try {
            switch (num) {
                case 1 -> {
                    // 🌌 Universe Event
                    System.out.println("🌌 Opening Universe event...");
                    MainApp.showUniverse(1); // Start from level 1
                }
                case 2 -> {
                    // ✨ Treasure Hunter Event
                    System.out.println("🏴‍☠️ Opening Treasure Hunter event...");
                    MainApp.showTreasureHunter(1); // Start from level 1
                }
                case 3 -> {
                    // 🏰 Castle Attack Event
                    System.out.println("🏰 Opening Castle Attack event...");
                    MainApp.showCastle(1); // Start from level 1
                }
                case 4 -> {
                    // ⚽ Penaldo Event
                    System.out.println("⚽ Opening Penaldo event...");
                    MainApp.showPenaldo(1); // Start from level 1
                }
                case 5 -> {
                    // 🎰 Casino - Coming soon
                    System.out.println("🎰 Casino event - Coming soon!");
                }
                default -> {
                    System.out.println("Event coming soon!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Error opening event: " + e.getMessage());
        }
    }


    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SelectMode.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            // Preserve the current window size by reusing the existing Scene root if possible.
            if (stage.getScene() != null) {
                stage.getScene().setRoot(root);
            } else {
                stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
