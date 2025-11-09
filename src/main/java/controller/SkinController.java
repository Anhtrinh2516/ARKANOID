package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

import core.MainApp;

public class SkinController {

    @FXML private HBox paddleSkinBox;
    @FXML private HBox ballSkinBox;
    @FXML private Button selectPaddleButton;
    @FXML private Button selectBallButton;
    @FXML private Button backButton;
    @FXML private Label coinsLabel;

    private SkinManager.PaddleSkin selectedPaddle;
    private SkinManager.BallSkin selectedBall;
    
    private List<StackPane> paddleContainers = new ArrayList<>();
    private List<StackPane> ballContainers = new ArrayList<>();

    public void initialize() {
        selectedPaddle = SkinManager.INSTANCE.getPaddleSkin();
        selectedBall = SkinManager.INSTANCE.getBallSkin();

        updateCoinsLabel();

        for (SkinManager.PaddleSkin skin : SkinManager.PaddleSkin.values()) {
            StackPane container = new StackPane();
            
            Rectangle rect = new Rectangle(80, 15);
            rect.setFill(javafx.scene.paint.Color.web(skin.fill));
            rect.setStroke(javafx.scene.paint.Color.web(skin.stroke));
            rect.setStrokeWidth(2);
            rect.setArcWidth(5);
            rect.setArcHeight(5);
            
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

        for (SkinManager.BallSkin skin : SkinManager.BallSkin.values()) {
            StackPane container = new StackPane();
            
            Circle circle = new Circle(15);
            circle.setFill(javafx.scene.paint.Color.web(skin.color));
            circle.setStroke(javafx.scene.paint.Color.BLACK);
            circle.setStrokeWidth(1);
            
            container.getChildren().add(circle);

            if (!SkinManager.INSTANCE.isBallSkinUnlocked(skin)) {
                Label lockLabel = new Label("🔒");
                lockLabel.setStyle("-fx-font-size: 16px;");
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

        selectPaddleButton.setOnAction(e -> handlePaddleAction());
        selectBallButton.setOnAction(e -> handleBallAction());

        backButton.setOnAction(e -> {
            try {
                MainApp.showMainMenu();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        updateButtons();
        updatePaddleBorders();
        updateBallBorders();
    }

    private void handlePaddleAction() {
        if (SkinManager.INSTANCE.isPaddleSkinUnlocked(selectedPaddle)) {
            SkinManager.INSTANCE.setPaddleSkin(selectedPaddle);
        } else if (selectedPaddle.type == SkinManager.SkinType.SHOP) {
            if (GameState.INSTANCE.getCoins() >= selectedPaddle.price) {
                GameState.INSTANCE.addCoins(-selectedPaddle.price);
                SkinManager.INSTANCE.unlockPaddleSkin(selectedPaddle);
                SkinManager.INSTANCE.setPaddleSkin(selectedPaddle);
                refreshUI();
            }
        }
        updateButtons();
        updatePaddleBorders();
        updateCoinsLabel();
    }

    private void handleBallAction() {
        if (SkinManager.INSTANCE.isBallSkinUnlocked(selectedBall)) {
            SkinManager.INSTANCE.setBallSkin(selectedBall);
        } else if (selectedBall.type == SkinManager.SkinType.SHOP) {
            if (GameState.INSTANCE.getCoins() >= selectedBall.price) {
                GameState.INSTANCE.addCoins(-selectedBall.price);
                SkinManager.INSTANCE.unlockBallSkin(selectedBall);
                SkinManager.INSTANCE.setBallSkin(selectedBall);
                refreshUI();
            }
        }
        updateButtons();
        updateBallBorders();
        updateCoinsLabel();
    }

    private void refreshUI() {
        paddleSkinBox.getChildren().clear();
        ballSkinBox.getChildren().clear();
        paddleContainers.clear();
        ballContainers.clear();
        
        for (SkinManager.PaddleSkin skin : SkinManager.PaddleSkin.values()) {
            StackPane container = new StackPane();
            
            Rectangle rect = new Rectangle(80, 15);
            rect.setFill(javafx.scene.paint.Color.web(skin.fill));
            rect.setStroke(javafx.scene.paint.Color.web(skin.stroke));
            rect.setStrokeWidth(2);
            rect.setArcWidth(5);
            rect.setArcHeight(5);
            
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

        // Tạo lại ball skins
        for (SkinManager.BallSkin skin : SkinManager.BallSkin.values()) {
            StackPane container = new StackPane();
            
            Circle circle = new Circle(15);
            circle.setFill(javafx.scene.paint.Color.web(skin.color));
            circle.setStroke(javafx.scene.paint.Color.BLACK);
            circle.setStrokeWidth(1);
            
            container.getChildren().add(circle);
            
            if (!SkinManager.INSTANCE.isBallSkinUnlocked(skin)) {
                Label lockLabel = new Label("🔒");
                lockLabel.setStyle("-fx-font-size: 16px;");
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
        
        updatePaddleBorders();
        updateBallBorders();
    }

    private void updateCoinsLabel() {
        if (coinsLabel != null) {
            coinsLabel.setText("💰 Coins: " + GameState.INSTANCE.getCoins());
        }
    }

    private void updatePaddleBorders() {
        for (int i = 0; i < paddleContainers.size(); i++) {
            StackPane container = paddleContainers.get(i);
            Rectangle rect = (Rectangle) container.getChildren().get(0);
            SkinManager.PaddleSkin skin = SkinManager.PaddleSkin.values()[i];
            
            if (skin == SkinManager.INSTANCE.getPaddleSkin()) {
                rect.setStroke(javafx.scene.paint.Color.GOLD);
                rect.setStrokeWidth(4);
            } else if (skin == selectedPaddle) {
                rect.setStroke(javafx.scene.paint.Color.LIME);
                rect.setStrokeWidth(3);
            } else {
                rect.setStroke(javafx.scene.paint.Color.web(skin.stroke));
                rect.setStrokeWidth(2);
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
            } else if (skin == selectedBall) {
                circle.setStroke(javafx.scene.paint.Color.LIME);
                circle.setStrokeWidth(3);
            } else {
                circle.setStroke(javafx.scene.paint.Color.BLACK);
                circle.setStrokeWidth(1);
            }
        }
    }

    private void updateButtons() {
        if (!SkinManager.INSTANCE.isPaddleSkinUnlocked(selectedPaddle)) {
            if (selectedPaddle.type == SkinManager.SkinType.SHOP) {
                selectPaddleButton.setText("💰 Buy (" + selectedPaddle.price + " coins)");
                selectPaddleButton.setStyle("-fx-font-size: 14px; -fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            } else if (selectedPaddle.type == SkinManager.SkinType.EVENT) {
                selectPaddleButton.setText("⭐ Event Required");
                selectPaddleButton.setStyle("-fx-font-size: 14px; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            }
        } else if (selectedPaddle == SkinManager.INSTANCE.getPaddleSkin()) {
            selectPaddleButton.setText("✓ Selected");
            selectPaddleButton.setStyle("-fx-font-size: 16px; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        } else {
            selectPaddleButton.setText("Select");
            selectPaddleButton.setStyle("-fx-font-size: 16px; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        }

        if (!SkinManager.INSTANCE.isBallSkinUnlocked(selectedBall)) {
            if (selectedBall.type == SkinManager.SkinType.SHOP) {
                selectBallButton.setText("💰 Buy (" + selectedBall.price + " coins)");
                selectBallButton.setStyle("-fx-font-size: 14px; -fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            } else if (selectedBall.type == SkinManager.SkinType.EVENT) {
                selectBallButton.setText("⭐ Event Required");
                selectBallButton.setStyle("-fx-font-size: 14px; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            }
        } else if (selectedBall == SkinManager.INSTANCE.getBallSkin()) {
            selectBallButton.setText("✓ Selected");
            selectBallButton.setStyle("-fx-font-size: 16px; -fx-background-color: #95a5a6; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        } else {
            selectBallButton.setText("Select");
            selectBallButton.setStyle("-fx-font-size: 16px; -fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        }
    }
}
