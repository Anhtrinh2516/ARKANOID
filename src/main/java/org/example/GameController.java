package org.example;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.example.logic.*;
import org.example.controller.GameState;
import org.example.controller.SkinManager;

import java.util.*;

public class GameController {

    @FXML private AnchorPane anchorPane;
    @FXML private Rectangle paddle;
    @FXML private Circle ball;

    @FXML private Label scoreLabel;
    @FXML private Label livesLabel;
    @FXML private Label levelLabel;
    @FXML private Label hintLabel;
    @FXML private Label coinsLabel;
    @FXML private Button backButton;

    private final Random rng = new Random();
    private final Set<KeyCode> activeKeys = new HashSet<>();
    private final List<PowerUp> activePowerUps = new ArrayList<>();

    private GameEngine engine;

    private int lives = 3;
    private int currentLevel = 1;

    // Khởi tạo level
    public void startLevel(int levelIndex) {
        currentLevel = levelIndex;
        engine.loadLevel(levelIndex);

        // Áp dụng bonus từ Shop
        int bonus = GameState.INSTANCE.getPaddleWidthBonus();
        paddle.setWidth(paddle.getWidth() + bonus);

        double scale = GameState.INSTANCE.getBallSpeedScale();


        updateCoinsUI();
        livesLabel.setText("Lives: " + lives);
        levelLabel.setText("Level: " + levelIndex);
    }

    // Khi gạch bị phá
    private void onBrickDestroyed(Brick b) {
        maybeSpawnPowerUpAt(
                b.getNode().getBoundsInParent().getMinX() + b.getNode().getBoundsInParent().getWidth() / 2,
                b.getNode().getBoundsInParent().getMinY() + b.getNode().getBoundsInParent().getHeight() / 2
        );
    }


    // Sinh item rơi
    private void maybeSpawnPowerUpAt(double x, double y) {
        int p = rng.nextInt(100);
        PowerUpType type = null;

        if (p < 40) { // 40% có item
            if (p < 15) type = PowerUpType.COIN;          // 15%
            else if (p < 25) type = PowerUpType.EXTRA_LIFE; // 10%
            else if (p < 35) type = PowerUpType.EXPAND_PADDLE; // 10%
            else type = PowerUpType.SLOW_BALL;             // 5%
        }

        if (type != null) {
            PowerUp pu = new PowerUp(type, x, y);
            activePowerUps.add(pu);
            anchorPane.getChildren().add(pu);
        }
    }

    // Update item rơi trong game loop
    private void updatePowerUps() {
        Iterator<PowerUp> it = activePowerUps.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            p.update();

            // item rơi ra ngoài màn
            if (p.getCenterY() > anchorPane.getHeight() + 20) {
                anchorPane.getChildren().remove(p);
                it.remove();
                continue;
            }

            // check va chạm với paddle
            if (p.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
                applyPowerUp(p.getType());
                anchorPane.getChildren().remove(p);
                it.remove();
            }
        }
    }


    // Áp dụng hiệu ứng khi nhặt item
    private void applyPowerUp(PowerUpType type) {
        switch (type) {
            case COIN -> {
                GameState.INSTANCE.addCoins(1);
                updateCoinsUI();
                System.out.println("Nhặt coin! Coins = " + GameState.INSTANCE.getCoins());
            }
            case EXTRA_LIFE -> {
                lives++;
                livesLabel.setText("Lives: " + lives);
                System.out.println("Thêm mạng! Lives = " + lives);
            }
            case EXPAND_PADDLE -> {
                double oldWidth = paddle.getWidth();
                paddle.setWidth(oldWidth + 40);
                System.out.println("Paddle to hơn!");
                Timeline t = new Timeline(new KeyFrame(Duration.seconds(10),
                        e -> paddle.setWidth(oldWidth)));
                t.setCycleCount(1);
                t.play();
            }
            case SLOW_BALL -> {
                System.out.println("Bóng chậm lại!");
            }
        }
    }

    private void updateCoinsUI() {
        coinsLabel.setText("Coins: " + GameState.INSTANCE.getCoins());
    }

    private void applySkins() {
        SkinManager.PaddleSkin pSkin = SkinManager.INSTANCE.getPaddleSkin();
        paddle.setFill(javafx.scene.paint.Color.web(pSkin.fill));
        paddle.setStroke(javafx.scene.paint.Color.web(pSkin.stroke));
        
        SkinManager.BallSkin bSkin = SkinManager.INSTANCE.getBallSkin();
        ball.setFill(javafx.scene.paint.Color.web(bSkin.color));
    }

    public void initialize() {
        // Áp dụng skin
        applySkins();
        
        // Khởi tạo Engine
        engine = new GameEngine(anchorPane, paddle, ball,
                score -> scoreLabel.setText("Score: " + score),
                l -> livesLabel.setText("Lives: " + l),
                level -> levelLabel.setText("Level: " + level)
        );
        
        // Set callback Game Over
        engine.setGameOverCallback(this::showGameOverDialog);
        
        engine.loadLevel(1); // mặc định Level 1

        updateCoinsUI();

        if (backButton != null) {
            backButton.setOnAction(e -> {
                try {
                    org.example.MainApp.showMainMenu();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }

        anchorPane.setOnKeyPressed(e -> activeKeys.add(e.getCode()));
        anchorPane.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));

        anchorPane.sceneProperty().addListener((obs, o, s) -> {
            if (s != null) {
                s.setOnMouseClicked(ev -> anchorPane.requestFocus());
                anchorPane.requestFocus();
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            private long lastCoinUpdate = 0;

            @Override public void handle(long now) {
                if (activeKeys.contains(KeyCode.LEFT))  engine.movePaddleLeft();
                if (activeKeys.contains(KeyCode.RIGHT)) engine.movePaddleRight();

                engine.update();

                updatePowerUps();

                if (now - lastCoinUpdate > 500_000_000) {
                    updateCoinsUI();
                    lastCoinUpdate = now;
                }
            }
        };
        timer.start();
    }

    private void showGameOverDialog() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("GAME OVER");
        alert.setHeaderText("Game Over!");
        alert.setContentText("You ran out of lives. What would you like to do?");

        ButtonType tryAgain = new ButtonType("Try Again");
        ButtonType selectLevel = new ButtonType("Select Level");
        ButtonType mainMenu = new ButtonType("Main Menu");

        alert.getButtonTypes().setAll(tryAgain, selectLevel, mainMenu);

        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent()) {
            if (result.get() == tryAgain) {
                engine.resetGame();
                engine.loadLevel(currentLevel);
            } else if (result.get() == selectLevel) {
                try {
                    MainApp.showLevelSelect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (result.get() == mainMenu) {
                try {
                    MainApp.showMainMenu();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
