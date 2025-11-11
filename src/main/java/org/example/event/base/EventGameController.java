package org.example.event.base;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.example.controller.GameState;
import org.example.logic.Ball;

import java.util.*;

public abstract class EventGameController {

    @FXML protected AnchorPane anchorPane;
    @FXML protected Rectangle paddle;
    @FXML protected Circle ball;
    
    @FXML protected Label scoreLabel;
    @FXML protected Label livesLabel;
    @FXML protected Label levelLabel;
    @FXML protected Label hintLabel;
    @FXML protected Label coinsLabel;
    @FXML protected Button backButton;
    @FXML protected Button pauseButton;

    protected static final double GAME_WIDTH = 888.0;
    protected static final double GAME_HEIGHT = 708.0;

    protected final Set<KeyCode> activeKeys = new HashSet<>();
    protected Object engine;
    protected int currentScore = 0;
    protected int currentLives = 3;
    protected int currentLevel = 1;
    protected boolean isPaused = false;
    protected AnimationTimer gameTimer;

    protected abstract Object createEngine(AnchorPane pane, Rectangle paddle, Circle ball);
    
    protected abstract void applyTheme();
    
    protected abstract Ball getEngineBall();
    
    protected abstract void updateEngine();
    
    protected abstract void movePaddleLeft();
    
    /**
     * Di chuyển paddle phải
     */
    protected abstract void movePaddleRight();
    
    /**
     * Điều chỉnh góc ngắm trái
     */
    protected abstract void adjustAimLeft();
    
    /**
     * Điều chỉnh góc ngắm phải
     */
    protected abstract void adjustAimRight();
    
    protected abstract void launchBall();
    
    protected abstract void loadEngineLevel(int levelIdx);
    
    protected abstract String getPaddleSkinPath();
    
    protected abstract String getBallSkinPath();

    protected void applySkinWithGlow(javafx.scene.shape.Shape shape, String imagePath, 
                                     Color fallbackColor, Color glowColor, 
                                     double glowRadius, double glowSpread) {
        try {
            Image img = new Image(getClass().getResourceAsStream(imagePath));
            shape.setFill(new ImagePattern(img));
        } catch (Exception e) {
            shape.setFill(fallbackColor);
        }
        
        shape.setStroke(glowColor);
        shape.setStrokeWidth(2);
        
        javafx.scene.effect.DropShadow glow = new javafx.scene.effect.DropShadow();
        glow.setColor(glowColor);
        glow.setRadius(glowRadius);
        glow.setSpread(glowSpread);
        shape.setEffect(glow);
    }

    protected void onBackPressed() {
        try {
            org.example.MainApp.showSelectEvent();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    protected String getStartLevelHint() {
        return "🎮 [Q/E] Aim | [SPACE] Launch | [←→] Move | [N] Skip Level | [P] Pause";
    }

    protected void resetPaddlePosition() {
        paddle.setX((GAME_WIDTH - paddle.getWidth()) / 2);
        paddle.setY(GAME_HEIGHT - 58);
        paddle.setLayoutX(0);
        paddle.setLayoutY(0);
        paddle.setTranslateX(0);
        paddle.setTranslateY(0);
        paddle.setScaleX(1.0);
        paddle.setScaleY(1.0);
        paddle.setRotate(0);
        paddle.setVisible(true);
        paddle.setOpacity(1.0);
    }

    protected void updateCoinsUI() {
        if (coinsLabel != null) {
            coinsLabel.setText(String.valueOf(GameState.INSTANCE.getCoins()));
        }
    }
    
    protected void updateScoreUI(int score) {
        currentScore = score;
        if (scoreLabel != null) {
            scoreLabel.setText(String.valueOf(score));
        }
    }
    
    protected void updateLivesUI(int lives) {
        currentLives = lives;
        if (livesLabel != null) {
            livesLabel.setText(String.valueOf(lives));
        }
    }
    
    protected void updateLevelUI(int level) {
        currentLevel = level;
        if (levelLabel != null) {
            levelLabel.setText(String.valueOf(level));
        }
    }

    public void startLevel(int levelIdx) {
        this.currentLevel = levelIdx;
        resetPaddlePosition();
        loadEngineLevel(levelIdx);

        if (hintLabel != null) {
            hintLabel.setText(getStartLevelHint());
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5),
                    e -> hintLabel.setText("")));
            timeline.play();
        }

        updateCoinsUI();
        if (levelLabel != null) {
            levelLabel.setText(String.valueOf(levelIdx));
        }
    }

    protected void togglePause() {
        isPaused = !isPaused;
        if (pauseButton != null) {
            pauseButton.setText(isPaused ? "▶" : "⏸");
        }

        if (hintLabel != null) {
            if (isPaused) {
                hintLabel.setText("⏸ PAUSED - Press P to resume");
            } else {
                hintLabel.setText("▶ RESUMED");
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2),
                        e -> hintLabel.setText("")));
                timeline.play();
            }
        }
    }

    protected void skipToNextLevel() {
        currentLevel++;
        startLevel(currentLevel);
        
        if (hintLabel != null) {
            hintLabel.setText("⏭ Skipped to Level " + currentLevel + " (Press N to skip)");
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3),
                    e -> hintLabel.setText("")));
            timeline.play();
        }
    }

    @FXML
    public void initialize() {
        applyTheme();

        engine = createEngine(anchorPane, paddle, ball);

        resetPaddlePosition();
        updateCoinsUI();

        setupButtonHandlers();

        setupKeyboardHandlers();

        setupGameLoop();
    }

    private void setupButtonHandlers() {
        if (backButton != null) {
            backButton.setOnAction(e -> {
                if (gameTimer != null) {
                    gameTimer.stop();
                }
                onBackPressed();
            });
            backButton.setFocusTraversable(false);
        }

        if (pauseButton != null) {
            pauseButton.setOnAction(e -> {
                togglePause();
                anchorPane.requestFocus();
            });
            pauseButton.setFocusTraversable(false);
        }
    }

    private void setupKeyboardHandlers() {
        anchorPane.setOnKeyPressed(e -> {
            activeKeys.add(e.getCode());

            if (e.getCode() == KeyCode.SPACE) {
                if (getEngineBall().isAttached()) {
                    launchBall();
                    if (hintLabel != null) {
                        hintLabel.setText("🚀 Ball launched!");
                        Timeline t = new Timeline(new KeyFrame(Duration.seconds(2),
                                ev -> hintLabel.setText("")));
                        t.play();
                    }
                }
            } else if (e.getCode() == KeyCode.P) {
                togglePause();
            } else if (e.getCode() == KeyCode.N) {
                skipToNextLevel();
            }
        });

        anchorPane.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));

        anchorPane.sceneProperty().addListener((obs, o, s) -> {
            if (s != null) {
                s.setOnMouseClicked(ev -> anchorPane.requestFocus());
                anchorPane.requestFocus();
            }
        });

        javafx.application.Platform.runLater(() -> {
            anchorPane.requestFocus();
            anchorPane.setFocusTraversable(true);
        });
    }

    private void setupGameLoop() {
        gameTimer = new AnimationTimer() {
            private long lastCoinUpdate = 0;
            private long lastAimUpdate = 0;

            @Override
            public void handle(long now) {
                if (!isPaused) {
                    if (activeKeys.contains(KeyCode.LEFT) || activeKeys.contains(KeyCode.A))
                        movePaddleLeft();
                    if (activeKeys.contains(KeyCode.RIGHT) || activeKeys.contains(KeyCode.D))
                        movePaddleRight();

                    if (getEngineBall().isAttached()) {
                        if (now - lastAimUpdate > 50_000_000) {
                            if (activeKeys.contains(KeyCode.Q)) {
                                adjustAimLeft();
                                lastAimUpdate = now;
                            }
                            if (activeKeys.contains(KeyCode.E)) {
                                adjustAimRight();
                                lastAimUpdate = now;
                            }
                        }
                    }

                    updateEngine();
                }

                if (now - lastCoinUpdate > 500_000_000) {
                    updateCoinsUI();
                    lastCoinUpdate = now;
                }

                if (!paddle.isVisible()) paddle.setVisible(true);
                if (paddle.getOpacity() < 1.0) paddle.setOpacity(1.0);
            }
        };
        gameTimer.start();
    }

    public void showEventWinDialog() {
        Rectangle overlay = new Rectangle(0, 0, 888, 708);
        overlay.setFill(Color.rgb(0, 0, 0, 0.7));
        overlay.setId("winDialogOverlay");
        anchorPane.getChildren().add(overlay);
        
        VBox dialog = createWinDialog();
        dialog.setId("winDialogBox");
        
        dialog.getChildren().addAll(
            createWinTitle(),
            createWinScore(),
            createWinSkins(),
            createWinLabel(),
            createWinButtons()
        );
        
        anchorPane.getChildren().add(dialog);
        isPaused = true;
    }

    private VBox createWinDialog() {
        VBox dialog = new VBox(20);
        dialog.setAlignment(Pos.CENTER);
        dialog.setPrefSize(400, 450);
        dialog.setLayoutX((888 - 400) / 2);
        dialog.setLayoutY((708 - 450) / 2);
        dialog.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #1a2332, #0a1628);" +
            "-fx-border-color: #00CED1; -fx-border-width: 3;" +
            "-fx-border-radius: 15; -fx-background-radius: 15; -fx-padding: 30;"
        );
        
        javafx.scene.effect.DropShadow shadow = new javafx.scene.effect.DropShadow();
        shadow.setColor(Color.rgb(0, 206, 209, 0.5));
        shadow.setRadius(20);
        dialog.setEffect(shadow);
        
        return dialog;
    }

    private Label createWinTitle() {
        Label title = new Label("🏴‍☠️ EVENT COMPLETE! 🏴‍☠️");
        title.setStyle("-fx-font-size: 25px; -fx-font-weight: bold; -fx-text-fill: #FFD700;");
        return title;
    }

    private Label createWinScore() {
        Label score = new Label("Score: " + currentScore);
        score.setStyle("-fx-font-size: 22px; -fx-text-fill: white;");
        return score;
    }

    private HBox createWinSkins() {
        HBox box = new HBox(30);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(createWinPaddle(), createWinBall());
        return box;
    }

    private VBox createWinPaddle() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        
        Rectangle rect = new Rectangle(150, 25);
        try {
            Image img = new Image(getClass().getResourceAsStream(getPaddleSkinPath()));
            rect.setFill(new ImagePattern(img));
        } catch (Exception e) {
            rect.setFill(Color.web("#00CED1"));
        }
        rect.setStroke(Color.web("#FFD700"));
        rect.setStrokeWidth(2);
        rect.setArcWidth(10);
        rect.setArcHeight(10);
        
        javafx.scene.effect.DropShadow glow = new javafx.scene.effect.DropShadow();
        glow.setColor(Color.web("#FFD700"));
        glow.setRadius(15);
        rect.setEffect(glow);
        
        box.getChildren().add(rect);
        return box;
    }

    private VBox createWinBall() {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);
        
        Circle circle = new Circle(28);
        try {
            Image img = new Image(getClass().getResourceAsStream(getBallSkinPath()));
            circle.setFill(new ImagePattern(img));
        } catch (Exception e) {
            circle.setFill(Color.web("#FFD700"));
        }
        circle.setStroke(Color.web("#FFD700"));
        circle.setStrokeWidth(2);
        
        javafx.scene.effect.DropShadow glow = new javafx.scene.effect.DropShadow();
        glow.setColor(Color.web("#FFD700"));
        glow.setRadius(15);
        circle.setEffect(glow);
        
        box.getChildren().add(circle);
        return box;
    }

    private Label createWinLabel() {
        Label label = new Label("✨ SKINS UNLOCKED ✨");
        label.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #FFD700;");
        return label;
    }

    private HBox createWinButtons() {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(createTryAgainBtn(), createMenuBtn());
        return box;
    }

    private Button createTryAgainBtn() {
        Button btn = new Button("TRY AGAIN");
        btn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 30; " +
                    "-fx-background-color: #27ae60; -fx-text-fill: white; " +
                    "-fx-background-radius: 8; -fx-cursor: hand;");
        
        btn.setOnAction(e -> {
            anchorPane.getChildren().removeIf(n -> 
                "winDialogOverlay".equals(n.getId()) || "winDialogBox".equals(n.getId())
            );
            isPaused = false;
            currentLevel = 1;
            startLevel(1);
        });
        
        return btn;
    }

    private Button createMenuBtn() {
        Button btn = new Button("MENU");
        btn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 10 30; " +
                    "-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                    "-fx-background-radius: 8; -fx-cursor: hand;");
        
        btn.setOnAction(e -> {
            if (gameTimer != null) gameTimer.stop();
            onBackPressed();
        });
        
        return btn;
    }
}
