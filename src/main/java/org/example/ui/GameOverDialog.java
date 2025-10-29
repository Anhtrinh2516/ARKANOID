package org.example.ui;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class GameOverDialog {

    public static void show(int finalScore, int finalLevel) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setTitle("Game Over");

        // Root container
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.95);");

        // Animated background stars
        Pane starsPane = createStarField();

        // Main content
        VBox content = new VBox(30);
        content.setAlignment(Pos.CENTER);
        content.setMaxWidth(700);
        content.setMaxHeight(600);

        // Game Over title with subtle effect
        Label titleLabel = new Label("MISSION FAILED");
        titleLabel.setStyle(
                "-fx-font-size: 72px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: linear-gradient(to right, #e76f51, #f4a261); " +
                        "-fx-effect: dropshadow(gaussian, rgba(231, 111, 81, 0.4), 20, 0.5, 0, 0);"
        );

        // Subtle divider
        Rectangle divider1 = createGlowingDivider(500, Color.web("#5a8fb7"));

        // Score section with subtle glass effect
        VBox scoreBox = new VBox(15);
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setStyle(
                "-fx-background-color: rgba(90, 143, 183, 0.15); " +
                        "-fx-border-color: rgba(90, 143, 183, 0.6); " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 20; " +
                        "-fx-background-radius: 20; " +
                        "-fx-padding: 40; " +
                        "-fx-effect: dropshadow(gaussian, rgba(90, 143, 183, 0.3), 15, 0.4, 0, 0);"
        );

        Label scoreTitle = new Label("FINAL SCORE");
        scoreTitle.setStyle(
                "-fx-font-size: 24px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #7eb3d4; " +
                        "-fx-effect: dropshadow(gaussian, rgba(126, 179, 212, 0.3), 5, 0.3, 0, 0);"
        );

        Label scoreValue = new Label(String.valueOf(finalScore));
        scoreValue.setStyle(
                "-fx-font-size: 80px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #e8f0f5; " +
                        "-fx-effect: dropshadow(gaussian, rgba(126, 179, 212, 0.4), 10, 0.4, 0, 0);"
        );

        // Add subtle glow animation to score
        Glow glow = new Glow(0.3);
        scoreValue.setEffect(glow);

        Timeline glowAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0.1)),
                new KeyFrame(Duration.seconds(1.5), new KeyValue(glow.levelProperty(), 0.4))
        );
        glowAnimation.setCycleCount(Timeline.INDEFINITE);
        glowAnimation.setAutoReverse(true);
        glowAnimation.play();

        scoreBox.getChildren().addAll(scoreTitle, scoreValue);

        // Level reached section
        HBox levelBox = new HBox(15);
        levelBox.setAlignment(Pos.CENTER);
        levelBox.setStyle(
                "-fx-background-color: rgba(168, 135, 182, 0.15); " +
                        "-fx-border-color: rgba(168, 135, 182, 0.6); " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 15; " +
                        "-fx-background-radius: 15; " +
                        "-fx-padding: 20 40; " +
                        "-fx-effect: dropshadow(gaussian, rgba(168, 135, 182, 0.3), 15, 0.4, 0, 0);"
        );

        Label levelTitle = new Label("LEVEL REACHED");
        levelTitle.setStyle(
                "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #b8a0c4;"
        );

        Label levelValue = new Label(String.valueOf(finalLevel));
        levelValue.setStyle(
                "-fx-font-size: 48px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #e8e0ed; " +
                        "-fx-effect: dropshadow(gaussian, rgba(168, 135, 182, 0.3), 8, 0.3, 0, 0);"
        );

        levelBox.getChildren().addAll(levelTitle, levelValue);

        // Subtle divider
        Rectangle divider2 = createGlowingDivider(500, Color.web("#a887b6"));

        // Buttons section
        HBox buttonBox = new HBox(30);
        buttonBox.setAlignment(Pos.CENTER);

        Button playAgainBtn = createNeonButton("🔄 PLAY AGAIN", "#5ba584", "#4a8c6f");
        Button mainMenuBtn = createNeonButton("🏠 MAIN MENU", "#d17a7a", "#b86868");

        playAgainBtn.setOnAction(e -> {
            dialog.close();
            try {
                org.example.MainApp.showGame(1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        mainMenuBtn.setOnAction(e -> {
            dialog.close();
            try {
                org.example.MainApp.showMainMenu();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        buttonBox.getChildren().addAll(playAgainBtn, mainMenuBtn);

        // Hint text
        Label hintLabel = new Label("Press R to restart • ESC for menu");
        hintLabel.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-text-fill: rgba(255, 255, 255, 0.35); " +
                        "-fx-font-style: italic;"
        );

        // Add all elements to content
        content.getChildren().addAll(
                titleLabel,
                divider1,
                scoreBox,
                levelBox,
                divider2,
                buttonBox,
                hintLabel
        );

        // Add parallax stars and content to root
        root.getChildren().addAll(starsPane, content);

        // Entrance animation
        content.setOpacity(0);
        content.setScaleX(0.8);
        content.setScaleY(0.8);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), content);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(600), content);
        scaleIn.setFromX(0.8);
        scaleIn.setFromY(0.8);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);
        scaleIn.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition entrance = new ParallelTransition(fadeIn, scaleIn);
        entrance.setDelay(Duration.millis(200));
        entrance.play();

        // Create scene
        Scene scene = new Scene(root, 1148, 708);
        scene.setFill(Color.TRANSPARENT);

        // Keyboard shortcuts
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case R -> playAgainBtn.fire();
                case ESCAPE -> mainMenuBtn.fire();
            }
        });

        dialog.setScene(scene);
        dialog.centerOnScreen();
        dialog.showAndWait();
    }

    private static Pane createStarField() {
        Pane pane = new Pane();
        pane.setMouseTransparent(true);

        // Create animated stars
        for (int i = 0; i < 100; i++) {
            Circle star = new Circle(
                    Math.random() * 1148,
                    Math.random() * 708,
                    Math.random() * 2 + 0.5,
                    Color.WHITE
            );
            star.setOpacity(Math.random() * 0.7 + 0.3);

            // Twinkling animation
            FadeTransition twinkle = new FadeTransition(
                    Duration.seconds(Math.random() * 3 + 1),
                    star
            );
            twinkle.setFromValue(star.getOpacity());
            twinkle.setToValue(Math.random() * 0.3);
            twinkle.setCycleCount(Timeline.INDEFINITE);
            twinkle.setAutoReverse(true);
            twinkle.play();

            pane.getChildren().add(star);
        }

        // Add shooting stars occasionally
        Timeline shootingStars = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> createShootingStar(pane))
        );
        shootingStars.setCycleCount(Timeline.INDEFINITE);
        shootingStars.play();

        return pane;
    }

    private static void createShootingStar(Pane pane) {
        Circle star = new Circle(3, Color.web("#a8c5d8"));
        star.setOpacity(0);
        DropShadow trail = new DropShadow(15, Color.web("#a8c5d8"));
        trail.setSpread(0.5);
        star.setEffect(trail);

        double startX = Math.random() * 1148;
        double startY = Math.random() * 300;
        star.setLayoutX(startX);
        star.setLayoutY(startY);

        pane.getChildren().add(star);

        // Shooting animation
        TranslateTransition shoot = new TranslateTransition(Duration.seconds(2), star);
        shoot.setByX(400);
        shoot.setByY(300);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), star);
        fadeIn.setToValue(0.7);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(800), star);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(1.2));

        ParallelTransition parallel = new ParallelTransition(shoot, fadeIn, fadeOut);
        parallel.setOnFinished(e -> pane.getChildren().remove(star));
        parallel.play();
    }

    private static Rectangle createGlowingDivider(double width, Color color) {
        Rectangle rect = new Rectangle(width, 3);
        rect.setFill(color);
        rect.setArcWidth(3);
        rect.setArcHeight(3);

        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setRadius(10);
        glow.setSpread(0.4);
        rect.setEffect(glow);

        // Subtle pulse animation
        Timeline pulse = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(glow.radiusProperty(), 8)),
                new KeyFrame(Duration.seconds(2), new KeyValue(glow.radiusProperty(), 12))
        );
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        return rect;
    }

    private static Button createNeonButton(String text, String color1, String color2) {
        Button btn = new Button(text);
        btn.setPrefSize(250, 70);
        btn.setStyle(
                "-fx-font-size: 20px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-color: linear-gradient(to bottom, " + color1 + ", " + color2 + "); " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-color: rgba(255, 255, 255, 0.4); " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 15; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 10, 0.4, 0, 2);"
        );

        // Hover effects
        btn.setOnMouseEntered(e -> {
            btn.setScaleX(1.05);
            btn.setScaleY(1.05);
            btn.setStyle(
                    "-fx-font-size: 20px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-color: linear-gradient(to bottom, " + color1 + ", " + color2 + "); " +
                            "-fx-background-radius: 15; " +
                            "-fx-border-color: rgba(255, 255, 255, 0.6); " +
                            "-fx-border-width: 2; " +
                            "-fx-border-radius: 15; " +
                            "-fx-cursor: hand; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 15, 0.5, 0, 3);"
            );
        });

        btn.setOnMouseExited(e -> {
            btn.setScaleX(1.0);
            btn.setScaleY(1.0);
            btn.setStyle(
                    "-fx-font-size: 20px; " +
                            "-fx-font-weight: bold; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-color: linear-gradient(to bottom, " + color1 + ", " + color2 + "); " +
                            "-fx-background-radius: 15; " +
                            "-fx-border-color: rgba(255, 255, 255, 0.4); " +
                            "-fx-border-width: 2; " +
                            "-fx-border-radius: 15; " +
                            "-fx-cursor: hand; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 10, 0.4, 0, 2);"
            );
        });

        // Subtle pulse animation
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), btn);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.02);
        pulse.setToY(1.02);
        pulse.setCycleCount(Timeline.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        return btn;
    }
}