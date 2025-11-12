package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import controller.LeaderboardManager;
import core.MainApp;

public class GameOverDialog {

    public static void show(int score, int lvl) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setTitle("Game Over");

        boolean isTop = LeaderboardManager.INSTANCE.isTop(score);
        int rank = LeaderboardManager.INSTANCE.getRank(score);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40, 50, 40, 50));
        root.setPrefWidth(550);
        root.setPrefHeight(isTop ? 550 : 450);

        root.setStyle(StyleConstants.GRADIENT_BORDER_STYLE);
        root.setEffect(StyleConstants.createGlowEffect());

        Label title = new Label(isTop ? "NEW HIGH SCORE!" : "GAME OVER");
        title.setFont(Font.font("System", FontWeight.BOLD, isTop ? 38 : 42));
        title.setTextFill(Color.web(isTop ? StyleConstants.COLOR_GOLD : "#ef4444"));
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web(isTop ? StyleConstants.COLOR_GOLD : "#ef4444"));
        glow.setRadius(15);
        title.setEffect(glow);

        VBox rankBox = null;
        if (isTop) {
            rankBox = new VBox(5);
            rankBox.setAlignment(Pos.CENTER);
            rankBox.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, rgba(251, 191, 36, 0.2), rgba(245, 158, 11, 0.1));" +
                            "-fx-background-radius: 10;" +
                            "-fx-padding: 15;" +
                            "-fx-border-color: #fbbf24;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 10;");

            String emoji = rank == 1 ? "FIRST" : rank == 2 ? "SECOND" : rank == 3 ? "THIRD" : "TOP 10";
            Label rankTxt = new Label("You ranked #" + rank + " - " + emoji);
            rankTxt.setFont(Font.font("System", FontWeight.BOLD, 20));
            rankTxt.setTextFill(Color.web(StyleConstants.COLOR_GOLD));

            rankBox.getChildren().add(rankTxt);
        }

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

        HBox scoreBox = new HBox(10);
        scoreBox.setAlignment(Pos.CENTER);
        Label scoreIcon = new Label("Score");
        scoreIcon.setFont(Font.font(24));
        Label scoreText = new Label("Final Score:");
        scoreText.setFont(Font.font("System", FontWeight.NORMAL, 18));
        scoreText.setTextFill(Color.web("#cbd5e1"));
        Label scoreVal = new Label(String.format("%,d", score));
        scoreVal.setFont(Font.font("System", FontWeight.BOLD, 24));
        scoreVal.setTextFill(Color.web(StyleConstants.COLOR_GOLD));
        scoreBox.getChildren().addAll(scoreIcon, scoreText, scoreVal);

        HBox levelBox = new HBox(10);
        levelBox.setAlignment(Pos.CENTER);
        Label levelIcon = new Label("Level");
        levelIcon.setFont(Font.font(24));
        Label levelText = new Label("Level Reached:");
        levelText.setFont(Font.font("System", FontWeight.NORMAL, 18));
        levelText.setTextFill(Color.web("#cbd5e1"));
        Label levelVal = new Label(String.valueOf(lvl));
        levelVal.setFont(Font.font("System", FontWeight.BOLD, 24));
        levelVal.setTextFill(Color.web(StyleConstants.COLOR_CYAN));
        levelBox.getChildren().addAll(levelIcon, levelText, levelVal);

        statsBox.getChildren().addAll(scoreBox, levelBox);

        VBox nameBox = null;
        TextField nameField = new TextField();

        if (isTop) {
            nameBox = new VBox(10);
            nameBox.setAlignment(Pos.CENTER);
            nameBox.setStyle(
                    "-fx-background-color: rgba(34, 197, 94, 0.1);" +
                            "-fx-background-radius: 10;" +
                            "-fx-padding: 20;" +
                            "-fx-border-color: #22c55e;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 10;");

            Label prompt = new Label("Enter your name:");
            prompt.setFont(Font.font("System", FontWeight.BOLD, 16));
            prompt.setTextFill(Color.web("#22c55e"));

            nameField.setPromptText("Your Name (max 12 chars)");
            nameField.setMaxWidth(300);
            nameField.setFont(Font.font("System", FontWeight.NORMAL, 16));
            nameField.setStyle(
                    "-fx-background-color: rgba(30, 41, 59, 0.8);" +
                            "-fx-text-fill: white;" +
                            "-fx-prompt-text-fill: #64748b;" +
                            "-fx-padding: 12 15;" +
                            "-fx-background-radius: 8;" +
                            "-fx-border-color: #475569;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 8;");

            nameField.textProperty().addListener((obs, old, val) -> {
                if (val.length() > 12) nameField.setText(old);
            });

            nameBox.getChildren().addAll(prompt, nameField);
        }

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button saveBtn;
        if (isTop) {
            saveBtn = new Button("SAVE & TRY AGAIN");
            saveBtn.setFont(Font.font("System", FontWeight.BOLD, 15));
            saveBtn.setPrefWidth(200);
            saveBtn.setStyle(StyleConstants.BUTTON_PRIMARY_STYLE);
            saveBtn.setEffect(StyleConstants.createButtonGlow(Color.rgb(34, 197, 94, 0.6), 12));

            Button finalSave = saveBtn;
            saveBtn.setOnMouseEntered(e -> finalSave.setStyle(StyleConstants.BUTTON_PRIMARY_HOVER_STYLE));
            saveBtn.setOnMouseExited(e -> finalSave.setStyle(StyleConstants.BUTTON_PRIMARY_STYLE));

            saveBtn.setOnAction(e -> {
                String name = nameField.getText().trim();
                if (name.isEmpty()) name = "ANONYMOUS";
                LeaderboardManager.INSTANCE.add(name, score, lvl);
                dialog.close();
            });
        } else {
            saveBtn = null;
        }

        Button retryBtn = null;
        if (!isTop) {
            retryBtn = new Button("TRY AGAIN");
            retryBtn.setFont(Font.font("System", FontWeight.BOLD, 16));
            retryBtn.setPrefWidth(180);
            retryBtn.setStyle(StyleConstants.BUTTON_PRIMARY_STYLE);
            retryBtn.setEffect(StyleConstants.createButtonGlow(Color.rgb(34, 197, 94, 0.6), 12));

            Button finalRetry = retryBtn;
            retryBtn.setOnMouseEntered(e -> finalRetry.setStyle(StyleConstants.BUTTON_PRIMARY_HOVER_STYLE));
            retryBtn.setOnMouseExited(e -> finalRetry.setStyle(StyleConstants.BUTTON_PRIMARY_STYLE));

            retryBtn.setOnAction(e -> dialog.close());
        }

        Button menuBtn = new Button("MAIN MENU");
        menuBtn.setFont(Font.font("System", FontWeight.BOLD, 16));
        menuBtn.setPrefWidth(180);
        menuBtn.setStyle(StyleConstants.BUTTON_DANGER_STYLE);
        menuBtn.setEffect(StyleConstants.createButtonGlow(Color.rgb(239, 68, 68, 0.6), 12));

        menuBtn.setOnMouseEntered(e -> {
            menuBtn.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #dc2626, #b91c1c);" +
                            "-fx-text-fill: white;" +
                            "-fx-padding: 10 40;" +
                            "-fx-background-radius: 10;" +
                            "-fx-cursor: hand;" +
                            "-fx-scale-x: 1.05;" +
                            "-fx-scale-y: 1.05;"
            );
        });
        menuBtn.setOnMouseExited(e -> menuBtn.setStyle(StyleConstants.BUTTON_DANGER_STYLE));

        menuBtn.setOnAction(e -> {
            dialog.close();
            try {
                MainApp.showMainMenu();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        if (isTop && saveBtn != null) {
            buttonBox.getChildren().addAll(saveBtn, menuBtn);
        } else if (retryBtn != null) {
            buttonBox.getChildren().addAll(retryBtn, menuBtn);
        }

        if (isTop && rankBox != null && nameBox != null) {
            root.getChildren().addAll(title, rankBox, statsBox, nameBox, buttonBox);
        } else {
            root.getChildren().addAll(title, statsBox, buttonBox);
        }

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}