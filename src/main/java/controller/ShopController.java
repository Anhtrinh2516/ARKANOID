package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import core.MainApp;

import java.util.ArrayList;
import java.util.List;

public class ShopController {

    @FXML private Label coinsLabel, messageLabel;
    @FXML private Label wideItemLabel, lifeItemLabel, slowItemLabel;
    @FXML private Button backButton;

    // Tabs
    @FXML private VBox shopTab;
    @FXML private VBox skinsTab;
    @FXML private ScrollPane shopScrollPane;
    @FXML private ScrollPane skinsScrollPane;

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

        // Set default tab - show shop, hide skins
        selectShopTab();
    }

    @FXML
    private void selectShopTab() {
        // Show shop content, hide skins content
        if (shopScrollPane != null) {
            shopScrollPane.setVisible(true);
            shopScrollPane.setManaged(true);
        }
        if (skinsScrollPane != null) {
            skinsScrollPane.setVisible(false);
            skinsScrollPane.setManaged(false);
        }

        // Update tab styling
        if (shopTab != null) {
            shopTab.setStyle("-fx-padding: 25 15;" +
                    "-fx-background-color: linear-gradient(to right, rgba(0, 212, 255, 0.25), rgba(0, 212, 255, 0.08));" +
                    "-fx-border-color: #00d4ff;" +
                    "-fx-border-width: 0 4 2 0;" +
                    "-fx-cursor: hand;");

            if (shopTab.getChildren().size() > 1) {
                ((Label) shopTab.getChildren().get(1)).setStyle("-fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #00d4ff; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 212, 255, 0.6), 8, 0, 0, 0);");
            }
            if (shopTab.getChildren().size() > 2) {
                ((Label) shopTab.getChildren().get(2)).setStyle("-fx-font-size: 9px; -fx-text-fill: #94a3b8;");
            }
        }

        if (skinsTab != null) {
            skinsTab.setStyle("-fx-padding: 25 15;" +
                    "-fx-background-color: rgba(55, 65, 81, 0.3);" +
                    "-fx-border-color: #4b5563;" +
                    "-fx-border-width: 0 0 2 0;" +
                    "-fx-cursor: hand;");

            if (skinsTab.getChildren().size() > 1) {
                ((Label) skinsTab.getChildren().get(1)).setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #9ca3af;");
            }
            if (skinsTab.getChildren().size() > 2) {
                ((Label) skinsTab.getChildren().get(2)).setStyle("-fx-font-size: 9px; -fx-text-fill: #6b7280;");
            }
        }
    }

    @FXML
    private void selectSkinsTab() {
        // Hide shop content, show skins content
        if (shopScrollPane != null) {
            shopScrollPane.setVisible(false);
            shopScrollPane.setManaged(false);
        }
        if (skinsScrollPane != null) {
            skinsScrollPane.setVisible(true);
            skinsScrollPane.setManaged(true);
        }

        // Update tab styling
        if (skinsTab != null) {
            skinsTab.setStyle("-fx-padding: 25 15;" +
                    "-fx-background-color: linear-gradient(to right, rgba(255, 0, 255, 0.25), rgba(255, 0, 255, 0.08));" +
                    "-fx-border-color: #ff00ff;" +
                    "-fx-border-width: 0 4 2 0;" +
                    "-fx-cursor: hand;");

            if (skinsTab.getChildren().size() > 1) {
                ((Label) skinsTab.getChildren().get(1)).setStyle("-fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #ff00ff; " +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 0, 255, 0.6), 8, 0, 0, 0);");
            }
            if (skinsTab.getChildren().size() > 2) {
                ((Label) skinsTab.getChildren().get(2)).setStyle("-fx-font-size: 9px; -fx-text-fill: #94a3b8;");
            }
        }

        if (shopTab != null) {
            shopTab.setStyle("-fx-padding: 25 15;" +
                    "-fx-background-color: rgba(55, 65, 81, 0.3);" +
                    "-fx-border-color: #4b5563;" +
                    "-fx-border-width: 0 0 2 0;" +
                    "-fx-cursor: hand;");

            if (shopTab.getChildren().size() > 1) {
                ((Label) shopTab.getChildren().get(1)).setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #9ca3af;");
            }
            if (shopTab.getChildren().size() > 2) {
                ((Label) shopTab.getChildren().get(2)).setStyle("-fx-font-size: 9px; -fx-text-fill: #6b7280;");
            }
        }
    }

    private void initializeSkins() {
        // Clear existing children
        paddleSkinBox.getChildren().clear();
        ballSkinBox.getChildren().clear();
        paddleContainers.clear();
        ballContainers.clear();

        // Create paddle skins grid (2 rows × 5 cols)
        javafx.scene.layout.GridPane paddleGrid = new javafx.scene.layout.GridPane();
        paddleGrid.setHgap(12);
        paddleGrid.setVgap(12);
        paddleGrid.setAlignment(javafx.geometry.Pos.CENTER);

        SkinManager.PaddleSkin[] paddleSkins = SkinManager.PaddleSkin.values();
        for (int i = 0; i < paddleSkins.length; i++) {
            SkinManager.PaddleSkin skin = paddleSkins[i];
            StackPane container = createPaddleSkinPreview(skin);
            paddleContainers.add(container);
            
            int row = i / 5;
            int col = i % 5;
            paddleGrid.add(container, col, row);
        }
        paddleSkinBox.getChildren().add(paddleGrid);

        // Create ball skins grid (2 rows × 5 cols)
        javafx.scene.layout.GridPane ballGrid = new javafx.scene.layout.GridPane();
        ballGrid.setHgap(12);
        ballGrid.setVgap(12);
        ballGrid.setAlignment(javafx.geometry.Pos.CENTER);

        SkinManager.BallSkin[] ballSkins = SkinManager.BallSkin.values();
        for (int i = 0; i < ballSkins.length; i++) {
            SkinManager.BallSkin skin = ballSkins[i];
            StackPane container = createBallSkinPreview(skin);
            ballContainers.add(container);
            
            int row = i / 5;
            int col = i % 5;
            ballGrid.add(container, col, row);
        }
        ballSkinBox.getChildren().add(ballGrid);

        updateButtons();
        updatePaddleBorders();
        updateBallBorders();
    }

    private StackPane createPaddleSkinPreview(SkinManager.PaddleSkin skin) {
        StackPane container = new StackPane();
        container.setPrefSize(140, 130);
        container.setStyle("-fx-padding: 12; -fx-cursor: hand;" +
                "-fx-background-color: linear-gradient(to bottom, rgba(30, 41, 59, 0.8), rgba(15, 23, 42, 0.8));" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #475569;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 14;");

        VBox skinBox = new VBox(6);
        skinBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Create paddle preview (image for EVENT, rectangle for others)
        javafx.scene.Node paddlePreview;
        if (skin.type == SkinManager.SkinType.EVENT && skin.imagePath != null) {
            try {
                javafx.scene.image.Image img = new javafx.scene.image.Image(
                    getClass().getResourceAsStream(skin.imagePath)
                );
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(img);
                imageView.setFitWidth(110);
                imageView.setFitHeight(22);
                imageView.setPreserveRatio(false);  // Force exact size
                paddlePreview = imageView;
            } catch (Exception e) {
                // Fallback to rectangle if image not found
                Rectangle rect = new Rectangle(110, 22);
                rect.setFill(javafx.scene.paint.Color.web(skin.fill));
                rect.setStroke(javafx.scene.paint.Color.web(skin.stroke));
                rect.setStrokeWidth(2.5);
                rect.setArcWidth(8);
                rect.setArcHeight(8);
                paddlePreview = rect;
            }
        } else {
            Rectangle rect = new Rectangle(110, 22);
            rect.setFill(javafx.scene.paint.Color.web(skin.fill));
            rect.setStroke(javafx.scene.paint.Color.web(skin.stroke));
            rect.setStrokeWidth(2.5);
            rect.setArcWidth(8);
            rect.setArcHeight(8);
            paddlePreview = rect;
        }

        skinBox.getChildren().add(paddlePreview);

        // Add skin name label with bright color
        Label nameLabel = new Label(skin.name().replace("_", " "));
        nameLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #e2e8f0; -fx-font-weight: bold;");
        skinBox.getChildren().add(nameLabel);

        container.getChildren().add(skinBox);

        // Add lock overlay if not unlocked
        if (!SkinManager.INSTANCE.isPaddleSkinUnlocked(skin)) {
            Label lockLabel = new Label("🔒");
            lockLabel.setStyle("-fx-font-size: 32px;");
            container.getChildren().add(lockLabel);
        }

        container.setOnMouseClicked(e -> {
            selectedPaddle = skin;
            updatePaddleBorders();
            updateButtons();
        });

        return container;
    }

    private StackPane createBallSkinPreview(SkinManager.BallSkin skin) {
        StackPane container = new StackPane();
        container.setPrefSize(140, 130);
        container.setStyle("-fx-padding: 12; -fx-cursor: hand;" +
                "-fx-background-color: linear-gradient(to bottom, rgba(30, 41, 59, 0.8), rgba(15, 23, 42, 0.8));" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: #475569;" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 14;");

        VBox skinBox = new VBox(6);
        skinBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Create ball preview (image for EVENT, circle for others)
        javafx.scene.Node ballPreview;
        if (skin.type == SkinManager.SkinType.EVENT && skin.imagePath != null) {
            try {
                javafx.scene.image.Image img = new javafx.scene.image.Image(
                    getClass().getResourceAsStream(skin.imagePath)
                );
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(img);
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);  // Keep aspect ratio for round ball
                imageView.setSmooth(true);  // Smooth rendering
                ballPreview = imageView;
            } catch (Exception e) {
                // Fallback to circle if image not found
                Circle circle = new Circle(22);
                circle.setFill(javafx.scene.paint.Color.web(skin.color));
                circle.setStroke(javafx.scene.paint.Color.BLACK);
                circle.setStrokeWidth(2.5);
                ballPreview = circle;
            }
        } else {
            Circle circle = new Circle(22);
            circle.setFill(javafx.scene.paint.Color.web(skin.color));
            circle.setStroke(javafx.scene.paint.Color.BLACK);
            circle.setStrokeWidth(2.5);
            ballPreview = circle;
        }

        skinBox.getChildren().add(ballPreview);

        // Add skin name label with bright color
        Label nameLabel = new Label(skin.name().replace("_", " "));
        nameLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #e2e8f0; -fx-font-weight: bold;");
        skinBox.getChildren().add(nameLabel);

        container.getChildren().add(skinBox);

        // Add lock overlay if not unlocked
        if (!SkinManager.INSTANCE.isBallSkinUnlocked(skin)) {
            Label lockLabel = new Label("🔒");
            lockLabel.setStyle("-fx-font-size: 28px;");
            container.getChildren().add(lockLabel);
        }

        container.setOnMouseClicked(e -> {
            selectedBall = skin;
            updateBallBorders();
            updateButtons();
        });

        return container;
    }

    private boolean spend(int cost) {
        if (GameState.INSTANCE.getCoins() < cost) {
            showMessage("❌ Not enough coins!", "#ef4444");
            return false;
        }
        GameState.INSTANCE.addCoins(-cost);
        refreshUI();
        return true;
    }

    private void showMessage(String text, String color) {
        if (messageLabel != null) {
            messageLabel.setText(text);
            messageLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;" +
                    "-fx-text-fill: " + color + ";" +
                    "-fx-padding: 10 20;" +
                    "-fx-background-color: rgba(74, 222, 128, 0.15);" +
                    "-fx-background-radius: 8;" +
                    "-fx-border-color: " + color + ";" +
                    "-fx-border-width: 1.5;" +
                    "-fx-border-radius: 8;");
        }
    }

    private void refreshUI() {
        if (coinsLabel != null) {
            coinsLabel.setText(String.valueOf(GameState.INSTANCE.getCoins()));
        }
        if (messageLabel != null) {
            messageLabel.setText("");
        }

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
            showMessage("✅ Purchased Wide Paddle item! Press 1 in game.", "#4ade80");
            refreshUI();
        }
    }

    @FXML
    private void buyLifeItem() {
        if (spend(5)) {
            GameState.INSTANCE.addLifeItem(1);
            showMessage("✅ Purchased Extra Life item! Press 2 in game.", "#4ade80");
            refreshUI();
        }
    }

    @FXML
    private void buySlowItem() {
        if (spend(4)) {
            GameState.INSTANCE.addSlowItem(1);
            showMessage("✅ Purchased Slow Ball item! Press 3 in game.", "#4ade80");
            refreshUI();
        }
    }

    // === PERMANENT UPGRADES ===
    @FXML
    private void buyPaddleWidth() {
        if (spend(8)) {
            GameState.INSTANCE.addPaddleWidthBonus(20);
            showMessage("✅ Paddle will be wider in all future games!", "#4ade80");
        }
    }

    // === SKIN ACTIONS ===
    @FXML
    private void handlePaddleAction() {
        if (SkinManager.INSTANCE.isPaddleSkinUnlocked(selectedPaddle)) {
            SkinManager.INSTANCE.setPaddleSkin(selectedPaddle);
            showMessage("✅ Paddle skin applied!", "#4ade80");
        } else if (selectedPaddle.type == SkinManager.SkinType.SHOP) {
            if (spend(selectedPaddle.price)) {
                SkinManager.INSTANCE.unlockPaddleSkin(selectedPaddle);
                SkinManager.INSTANCE.setPaddleSkin(selectedPaddle);
                showMessage("✅ Paddle skin purchased and applied!", "#4ade80");
                refreshSkinUI();
            }
        } else {
            showMessage("❌ This skin requires completing an event!", "#ef4444");
        }
        updateButtons();
        updatePaddleBorders();
    }

    @FXML
    private void handleBallAction() {
        if (SkinManager.INSTANCE.isBallSkinUnlocked(selectedBall)) {
            SkinManager.INSTANCE.setBallSkin(selectedBall);
            showMessage("✅ Ball skin applied!", "#4ade80");
        } else if (selectedBall.type == SkinManager.SkinType.SHOP) {
            if (spend(selectedBall.price)) {
                SkinManager.INSTANCE.unlockBallSkin(selectedBall);
                SkinManager.INSTANCE.setBallSkin(selectedBall);
                showMessage("✅ Ball skin purchased and applied!", "#4ade80");
                refreshSkinUI();
            }
        } else {
            showMessage("❌ This skin requires completing an event!", "#ef4444");
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
            SkinManager.PaddleSkin skin = SkinManager.PaddleSkin.values()[i];

            if (skin == SkinManager.INSTANCE.getPaddleSkin()) {
                container.setStyle("-fx-padding: 10; -fx-cursor: hand;" +
                        "-fx-background-color: linear-gradient(to bottom, rgba(251, 191, 36, 0.25), rgba(245, 158, 11, 0.15));" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: #fbbf24;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(251, 191, 36, 0.7), 18, 0, 0, 0);");
            } else if (skin == selectedPaddle) {
                container.setStyle("-fx-padding: 10; -fx-cursor: hand;" +
                        "-fx-background-color: linear-gradient(to bottom, rgba(34, 197, 94, 0.2), rgba(22, 163, 74, 0.1));" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: #22c55e;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 14;");
            } else {
                container.setStyle("-fx-padding: 10; -fx-cursor: hand;" +
                        "-fx-background-color: linear-gradient(to bottom, rgba(30, 41, 59, 0.8), rgba(15, 23, 42, 0.8));" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: #475569;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 14;");
            }
        }
    }

    private void updateBallBorders() {
        for (int i = 0; i < ballContainers.size(); i++) {
            StackPane container = ballContainers.get(i);
            SkinManager.BallSkin skin = SkinManager.BallSkin.values()[i];

            if (skin == SkinManager.INSTANCE.getBallSkin()) {
                container.setStyle("-fx-padding: 10; -fx-cursor: hand;" +
                        "-fx-background-color: linear-gradient(to bottom, rgba(251, 191, 36, 0.25), rgba(245, 158, 11, 0.15));" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: #fbbf24;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(251, 191, 36, 0.7), 18, 0, 0, 0);");
            } else if (skin == selectedBall) {
                container.setStyle("-fx-padding: 10; -fx-cursor: hand;" +
                        "-fx-background-color: linear-gradient(to bottom, rgba(34, 197, 94, 0.2), rgba(22, 163, 74, 0.1));" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: #22c55e;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 14;");
            } else {
                container.setStyle("-fx-padding: 10; -fx-cursor: hand;" +
                        "-fx-background-color: linear-gradient(to bottom, rgba(30, 41, 59, 0.8), rgba(15, 23, 42, 0.8));" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: #475569;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 14;");
            }
        }
    }

    private void updateButtons() {
        // Update paddle button
        if (selectPaddleButton != null) {
            if (!SkinManager.INSTANCE.isPaddleSkinUnlocked(selectedPaddle)) {
                if (selectedPaddle.type == SkinManager.SkinType.SHOP) {
                    selectPaddleButton.setText("💰 BUY (" + selectedPaddle.price + " coins)");
                    selectPaddleButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;" +
                            "-fx-background-color: linear-gradient(to bottom, #f59e0b, #d97706);" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 12 50;" +
                            "-fx-background-radius: 10;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(245, 158, 11, 0.5), 10, 0, 0, 3);");
                } else {
                    selectPaddleButton.setText("⭐ Complete Treasure Hunter Event");
                    selectPaddleButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;" +
                            "-fx-background-color: linear-gradient(to bottom, #8b5cf6, #7c3aed);" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 12 30;" +
                            "-fx-background-radius: 10;" +
                            "-fx-effect: dropshadow(gaussian, rgba(139, 92, 246, 0.4), 8, 0, 0, 2);");
                }
            } else if (selectedPaddle == SkinManager.INSTANCE.getPaddleSkin()) {
                selectPaddleButton.setText("✓ SELECTED");
                selectPaddleButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;" +
                        "-fx-background-color: #64748b;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 12 50;" +
                        "-fx-background-radius: 10;");
            } else {
                selectPaddleButton.setText("SELECT");
                selectPaddleButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;" +
                        "-fx-background-color: linear-gradient(to bottom, #22c55e, #16a34a);" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 12 50;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(34, 197, 94, 0.5), 10, 0, 0, 3);");
            }
        }

        // Update ball button
        if (selectBallButton != null) {
            if (!SkinManager.INSTANCE.isBallSkinUnlocked(selectedBall)) {
                if (selectedBall.type == SkinManager.SkinType.SHOP) {
                    selectBallButton.setText("💰 BUY (" + selectedBall.price + " coins)");
                    selectBallButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;" +
                            "-fx-background-color: linear-gradient(to bottom, #f59e0b, #d97706);" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 12 50;" +
                            "-fx-background-radius: 10;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(245, 158, 11, 0.5), 10, 0, 0, 3);");
                } else {
                    selectBallButton.setText("⭐ Complete Treasure Hunter Event");
                    selectBallButton.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;" +
                            "-fx-background-color: linear-gradient(to bottom, #8b5cf6, #7c3aed);" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 12 30;" +
                            "-fx-background-radius: 10;" +
                            "-fx-effect: dropshadow(gaussian, rgba(139, 92, 246, 0.4), 8, 0, 0, 2);");
                }
            } else if (selectedBall == SkinManager.INSTANCE.getBallSkin()) {
                selectBallButton.setText("✓ SELECTED");
                selectBallButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;" +
                        "-fx-background-color: #64748b;" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 12 50;" +
                        "-fx-background-radius: 10;");
            } else {
                selectBallButton.setText("SELECT");
                selectBallButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;" +
                        "-fx-background-color: linear-gradient(to bottom, #22c55e, #16a34a);" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 12 50;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(34, 197, 94, 0.5), 10, 0, 0, 3);");
            }
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