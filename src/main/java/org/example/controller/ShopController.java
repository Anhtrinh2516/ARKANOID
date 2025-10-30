package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.example.MainApp;

import java.util.ArrayList;
import java.util.List;

public class ShopController {

    @FXML private Label coinsLabel, messageLabel;
    @FXML private Label wideItemLabel, lifeItemLabel, slowItemLabel;

    // Skin UI elements
    @FXML private HBox paddleSkinBox;
    @FXML private HBox ballSkinBox;
    @FXML private Button selectPaddleButton;
    @FXML private Button selectBallButton;

    private SkinManager.PaddleSkin selectedPaddle;
    private SkinManager.BallSkin selectedBall;

    private List<StackPane> paddleContainers = new ArrayList<>();
    private List<StackPane> ballContainers = new ArrayList<>();

    public void initialize() {
        // Initialize skin selection
        selectedPaddle = SkinManager.INSTANCE.getPaddleSkin();
        selectedBall = SkinManager.INSTANCE.getBallSkin();

        // Initialize UI
        refreshUI();

        // Only initialize skins if the UI elements exist
        if (paddleSkinBox != null && ballSkinBox != null) {
            initializeSkins();
        }
    }

    private void initializeSkins() {
        // Create paddle skin previews
        for (SkinManager.PaddleSkin skin : SkinManager.PaddleSkin.values()) {
            StackPane container = new StackPane();
            container.setStyle("-fx-padding: 8; -fx-cursor: hand;");

            Rectangle rect = new Rectangle(90, 18);
            rect.setFill(javafx.scene.paint.Color.web(skin.fill));
            rect.setStroke(javafx.scene.paint.Color.web(skin.stroke));
            rect.setStrokeWidth(2);
            rect.setArcWidth(6);
            rect.setArcHeight(6);

            container.getChildren().add(rect);

            if (!SkinManager.INSTANCE.isPaddleSkinUnlocked(skin)) {
                Label lockLabel = new Label("🔒");
                lockLabel.setStyle("-fx-font-size: 20px;");
                container.getChildren().add(lockLabel);
            }

            container.setOnMouseClicked(e -> {
                selectedPaddle = skin;
                updatePaddleBorders();
                updateButtons();
            });

            paddleContainers.add(container);
            paddleSkinBox.getChildren().add(container);
        }

        // Create ball skin previews
        for (SkinManager.BallSkin skin : SkinManager.BallSkin.values()) {
            StackPane container = new StackPane();
            container.setStyle("-fx-padding: 8; -fx-cursor: hand;");

            Circle circle = new Circle(18);
            circle.setFill(javafx.scene.paint.Color.web(skin.color));
            circle.setStroke(javafx.scene.paint.Color.BLACK);
            circle.setStrokeWidth(2);

            container.getChildren().add(circle);

            if (!SkinManager.INSTANCE.isBallSkinUnlocked(skin)) {
                Label lockLabel = new Label("🔒");
                lockLabel.setStyle("-fx-font-size: 18px;");
                container.getChildren().add(lockLabel);
            }

            container.setOnMouseClicked(e -> {
                selectedBall = skin;
                updateBallBorders();
                updateButtons();
            });

            ballContainers.add(container);
            ballSkinBox.getChildren().add(container);
        }

        updateButtons();
        updatePaddleBorders();
        updateBallBorders();
    }

    private boolean spend(int cost) {
        if (GameState.INSTANCE.getCoins() < cost) {
            messageLabel.setText("❌ Not enough coins!");
            messageLabel.setStyle("-fx-text-fill: #ff4444;");
            return false;
        }
        GameState.INSTANCE.addCoins(-cost);
        refreshUI();
        return true;
    }

    private void refreshUI() {
        coinsLabel.setText("💰 " + GameState.INSTANCE.getCoins());
        messageLabel.setText("");

        if (wideItemLabel != null) {
            wideItemLabel.setText("Owned: " + GameState.INSTANCE.getWideItemCount());
        }
        if (lifeItemLabel != null) {
            lifeItemLabel.setText("Owned: " + GameState.INSTANCE.getLifeItemCount());
        }
        if (slowItemLabel != null) {
            slowItemLabel.setText("Owned: " + GameState.INSTANCE.getSlowItemCount());
        }
    }

    // === CONSUMABLE ITEMS ===
    @FXML
    private void buyWideItem() {
        if (spend(3)) {
            GameState.INSTANCE.addWideItem(1);
            messageLabel.setText("✅ Purchased Wide Paddle item! Press 1 in game.");
            messageLabel.setStyle("-fx-text-fill: #00ff00;");
            refreshUI();
        }
    }

    @FXML
    private void buyLifeItem() {
        if (spend(5)) {
            GameState.INSTANCE.addLifeItem(1);
            messageLabel.setText("✅ Purchased Extra Life item! Press 2 in game.");
            messageLabel.setStyle("-fx-text-fill: #00ff00;");
            refreshUI();
        }
    }

    @FXML
    private void buySlowItem() {
        if (spend(4)) {
            GameState.INSTANCE.addSlowItem(1);
            messageLabel.setText("✅ Purchased Slow Ball item! Press 3 in game.");
            messageLabel.setStyle("-fx-text-fill: #00ff00;");
            refreshUI();
        }
    }

    // === PERMANENT UPGRADES ===
    @FXML
    private void buyPaddleWidth() {
        if (spend(8)) {
            GameState.INSTANCE.addPaddleWidthBonus(20);
            messageLabel.setText("✅ Paddle will be wider in game!");
            messageLabel.setStyle("-fx-text-fill: #00ff00;");
        }
    }

    // === SKIN ACTIONS ===
    @FXML
    private void handlePaddleAction() {
        if (SkinManager.INSTANCE.isPaddleSkinUnlocked(selectedPaddle)) {
            SkinManager.INSTANCE.setPaddleSkin(selectedPaddle);
            messageLabel.setText("✅ Paddle skin applied!");
            messageLabel.setStyle("-fx-text-fill: #00ff00;");
        } else if (selectedPaddle.type == SkinManager.SkinType.SHOP) {
            if (spend(selectedPaddle.price)) {
                SkinManager.INSTANCE.unlockPaddleSkin(selectedPaddle);
                SkinManager.INSTANCE.setPaddleSkin(selectedPaddle);
                messageLabel.setText("✅ Paddle skin purchased and applied!");
                messageLabel.setStyle("-fx-text-fill: #00ff00;");
                refreshSkinUI();
            }
        } else {
            messageLabel.setText("❌ This skin requires completing an event!");
            messageLabel.setStyle("-fx-text-fill: #ff4444;");
        }
        updateButtons();
        updatePaddleBorders();
    }

    @FXML
    private void handleBallAction() {
        if (SkinManager.INSTANCE.isBallSkinUnlocked(selectedBall)) {
            SkinManager.INSTANCE.setBallSkin(selectedBall);
            messageLabel.setText("✅ Ball skin applied!");
            messageLabel.setStyle("-fx-text-fill: #00ff00;");
        } else if (selectedBall.type == SkinManager.SkinType.SHOP) {
            if (spend(selectedBall.price)) {
                SkinManager.INSTANCE.unlockBallSkin(selectedBall);
                SkinManager.INSTANCE.setBallSkin(selectedBall);
                messageLabel.setText("✅ Ball skin purchased and applied!");
                messageLabel.setStyle("-fx-text-fill: #00ff00;");
                refreshSkinUI();
            }
        } else {
            messageLabel.setText("❌ This skin requires completing an event!");
            messageLabel.setStyle("-fx-text-fill: #ff4444;");
        }
        updateButtons();
        updateBallBorders();
    }

    private void refreshSkinUI() {
        paddleSkinBox.getChildren().clear();
        ballSkinBox.getChildren().clear();
        paddleContainers.clear();
        ballContainers.clear();

        initializeSkins();
    }

    private void updatePaddleBorders() {
        for (int i = 0; i < paddleContainers.size(); i++) {
            StackPane container = paddleContainers.get(i);
            Rectangle rect = (Rectangle) container.getChildren().get(0);
            SkinManager.PaddleSkin skin = SkinManager.PaddleSkin.values()[i];

            if (skin == SkinManager.INSTANCE.getPaddleSkin()) {
                rect.setStroke(javafx.scene.paint.Color.GOLD);
                rect.setStrokeWidth(4);
                container.setStyle("-fx-padding: 8; -fx-cursor: hand; -fx-background-color: rgba(255,215,0,0.2); -fx-background-radius: 8;");
            } else if (skin == selectedPaddle) {
                rect.setStroke(javafx.scene.paint.Color.LIME);
                rect.setStrokeWidth(3);
                container.setStyle("-fx-padding: 8; -fx-cursor: hand; -fx-background-color: rgba(0,255,0,0.1); -fx-background-radius: 8;");
            } else {
                rect.setStroke(javafx.scene.paint.Color.web(skin.stroke));
                rect.setStrokeWidth(2);
                container.setStyle("-fx-padding: 8; -fx-cursor: hand;");
            }
        }
    }

    private void updateBallBorders() {
        for (int i = 0; i < ballContainers.size(); i++) {
            StackPane container = ballContainers.get(i);
            Circle circle = (Circle) container.getChildren().get(0);
            SkinManager.BallSkin skin = SkinManager.BallSkin.values()[i];

            if (skin == SkinManager.INSTANCE.getBallSkin()) {
                circle.setStroke(javafx.scene.paint.Color.GOLD);
                circle.setStrokeWidth(4);
                container.setStyle("-fx-padding: 8; -fx-cursor: hand; -fx-background-color: rgba(255,215,0,0.2); -fx-background-radius: 8;");
            } else if (skin == selectedBall) {
                circle.setStroke(javafx.scene.paint.Color.LIME);
                circle.setStrokeWidth(3);
                container.setStyle("-fx-padding: 8; -fx-cursor: hand; -fx-background-color: rgba(0,255,0,0.1); -fx-background-radius: 8;");
            } else {
                circle.setStroke(javafx.scene.paint.Color.BLACK);
                circle.setStrokeWidth(2);
                container.setStyle("-fx-padding: 8; -fx-cursor: hand;");
            }
        }
    }

    private void updateButtons() {
        // Update paddle button
        if (!SkinManager.INSTANCE.isPaddleSkinUnlocked(selectedPaddle)) {
            if (selectedPaddle.type == SkinManager.SkinType.SHOP) {
                selectPaddleButton.setText("💰 BUY (" + selectedPaddle.price + " coins)");
                selectPaddleButton.setStyle("-fx-font-size: 14px; -fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            } else if (selectedPaddle.type == SkinManager.SkinType.EVENT) {
                selectPaddleButton.setText("⭐ Event Required");
                selectPaddleButton.setStyle("-fx-font-size: 14px; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 8; -fx-background-radius: 8;");
            }
        } else if (selectedPaddle == SkinManager.INSTANCE.getPaddleSkin()) {
            selectPaddleButton.setText("✓ Selected");
            selectPaddleButton.setStyle("-fx-font-size: 16px; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 8; -fx-background-radius: 8;");
        } else {
            selectPaddleButton.setText("SELECT");
            selectPaddleButton.setStyle("-fx-font-size: 16px; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
        }

        // Update ball button
        if (!SkinManager.INSTANCE.isBallSkinUnlocked(selectedBall)) {
            if (selectedBall.type == SkinManager.SkinType.SHOP) {
                selectBallButton.setText("💰 BUY (" + selectedBall.price + " coins)");
                selectBallButton.setStyle("-fx-font-size: 14px; -fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
            } else if (selectedBall.type == SkinManager.SkinType.EVENT) {
                selectBallButton.setText("⭐ Event Required");
                selectBallButton.setStyle("-fx-font-size: 14px; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 8; -fx-background-radius: 8;");
            }
        } else if (selectedBall == SkinManager.INSTANCE.getBallSkin()) {
            selectBallButton.setText("✓ Selected");
            selectBallButton.setStyle("-fx-font-size: 16px; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 8; -fx-background-radius: 8;");
        } else {
            selectBallButton.setText("SELECT");
            selectBallButton.setStyle("-fx-font-size: 16px; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 8; -fx-background-radius: 8; -fx-cursor: hand;");
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