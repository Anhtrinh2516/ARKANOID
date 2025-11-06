package org.example.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GameOverDialog {

    public static void show(int finalScore, int finalLevel) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setTitle("Game Over");

        // Main container with gradient border
        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40, 50, 40, 50));
        root.setPrefWidth(500);
        root.setPrefHeight(400);

        // Apply gradient border style
        root.setStyle(StyleConstants.GRADIENT_BORDER_STYLE);
        root.setEffect(StyleConstants.createGlowEffect());

        // Title
        Label titleLabel = new Label("💀 GAME OVER");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 42));
        titleLabel.setTextFill(Color.web("#ef4444"));
        DropShadow titleGlow = new DropShadow();
        titleGlow.setColor(Color.web("#ef4444"));
        titleGlow.setRadius(15);
        titleLabel.setEffect(titleGlow);

        // Stats container
        VBox statsBox = new VBox(15);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setStyle(
                "-fx-background-color: rgba(30, 41, 59, 0.6);" +
                        "-fx-background-radius: 12;" +
                        "-fx-padding: 25;" +
                        "-fx-border-color: #475569;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 12;"
        );

        // Score
        HBox scoreBox = new HBox(10);
        scoreBox.setAlignment(Pos.CENTER);
        Label scoreIcon = new Label("🏆");
        scoreIcon.setFont(Font.font(24));
        Label scoreText = new Label("Final Score:");
        scoreText.setFont(Font.font("System", FontWeight.NORMAL, 18));
        scoreText.setTextFill(Color.web("#cbd5e1"));
        Label scoreValue = new Label(String.valueOf(finalScore));
        scoreValue.setFont(Font.font("System", FontWeight.BOLD, 24));
        scoreValue.setTextFill(Color.web(StyleConstants.COLOR_GOLD));
        scoreBox.getChildren().addAll(scoreIcon, scoreText, scoreValue);

        // Level
        HBox levelBox = new HBox(10);
        levelBox.setAlignment(Pos.CENTER);
        Label levelIcon = new Label("🎯");
        levelIcon.setFont(Font.font(24));
        Label levelText = new Label("Level Reached:");
        levelText.setFont(Font.font("System", FontWeight.NORMAL, 18));
        levelText.setTextFill(Color.web("#cbd5e1"));
        Label levelValue = new Label(String.valueOf(finalLevel));
        levelValue.setFont(Font.font("System", FontWeight.BOLD, 24));
        levelValue.setTextFill(Color.web(StyleConstants.COLOR_CYAN));
        levelBox.getChildren().addAll(levelIcon, levelText, levelValue);

        statsBox.getChildren().addAll(scoreBox, levelBox);

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        // Retry button
        Button retryButton = new Button("🔄 TRY AGAIN");
        retryButton.setFont(Font.font("System", FontWeight.BOLD, 16));
        retryButton.setPrefWidth(180);
        retryButton.setStyle(StyleConstants.BUTTON_PRIMARY_STYLE);
        retryButton.setEffect(StyleConstants.createButtonGlow(Color.rgb(34, 197, 94, 0.6), 12));

        retryButton.setOnMouseEntered(e ->
                retryButton.setStyle(StyleConstants.BUTTON_PRIMARY_HOVER_STYLE));
        retryButton.setOnMouseExited(e ->
                retryButton.setStyle(StyleConstants.BUTTON_PRIMARY_STYLE));

        retryButton.setOnAction(e -> {
            dialog.close();
            // Game will auto-restart from GameEngine
        });

        // Main menu button
        Button menuButton = new Button("🏠 MAIN MENU");
        menuButton.setFont(Font.font("System", FontWeight.BOLD, 16));
        menuButton.setPrefWidth(180);
        menuButton.setStyle(StyleConstants.BUTTON_DANGER_STYLE);
        menuButton.setEffect(StyleConstants.createButtonGlow(Color.rgb(239, 68, 68, 0.6), 12));

        menuButton.setOnMouseEntered(e -> {
            menuButton.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #dc2626, #b91c1c);" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 10 40;" +
                            "-fx-background-radius: 10;" +
                            "-fx-cursor: hand;" +
                            "-fx-scale-x: 1.05;" +
                            "-fx-scale-y: 1.05;"
            );
        });
        menuButton.setOnMouseExited(e ->
                menuButton.setStyle(StyleConstants.BUTTON_DANGER_STYLE));

        menuButton.setOnAction(e -> {
            dialog.close();
            try {
                org.example.MainApp.showMainMenu();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        buttonBox.getChildren().addAll(retryButton, menuButton);

        root.getChildren().addAll(titleLabel, statsBox, buttonBox);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}