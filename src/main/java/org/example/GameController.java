package org.example;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.example.logic.*;
import org.example.controller.GameState;

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
    @FXML private Label powerUpsLabel;
    @FXML private Button backButton;

    @FXML private Button item1Button;
    @FXML private Button item2Button;
    @FXML private Button item3Button;
    @FXML private Button pauseButton;

    private final Random rng = new Random();
    private final Set<KeyCode> activeKeys = new HashSet<>();
    private final List<PowerUp> activePowerUps = new ArrayList<>();

    private GameEngine engine;
    private int currentLevel = 1;  // FIXED: Added missing field

    private boolean isPaused = false;

    private Timeline item1Timeline;
    private Timeline item2Timeline;
    private Timeline item3Timeline;

    // ========== HELPER METHODS ==========

    private void resetPaddlePosition() {
        paddle.setX((920 - paddle.getWidth()) / 2);
        paddle.setY(570);
        paddle.setLayoutX(0);
        paddle.setLayoutY(0);
        paddle.setTranslateX(0);
        paddle.setTranslateY(0);
        paddle.setScaleX(1.0);
        paddle.setScaleY(1.0);
        paddle.setRotate(0);
        paddle.setVisible(true);
        paddle.setOpacity(1.0);

        System.out.println("Paddle reset to X: " + paddle.getX() + ", Y: " + paddle.getY());
    }

    private void updateCoinsUI() {
        if (coinsLabel != null) {
            coinsLabel.setText(String.valueOf(GameState.INSTANCE.getCoins()));
        }
    }

    private void updateItemButtons() {
        if (item1Button != null) {
            int count = GameState.INSTANCE.getWideItemCount();
            item1Button.setText("1 (" + count + ")");
            item1Button.setDisable(count == 0);
        }
        if (item2Button != null) {
            int count = GameState.INSTANCE.getLifeItemCount();
            item2Button.setText("2 (" + count + ")");
            item2Button.setDisable(count == 0);
        }
        if (item3Button != null) {
            int count = GameState.INSTANCE.getSlowItemCount();
            item3Button.setText("3 (" + count + ")");
            item3Button.setDisable(count == 0);
        }
    }

    // ========== LEVEL MANAGEMENT ==========

    public void startLevel(int levelIndex) {
        this.currentLevel = levelIndex;  // FIXED: Store current level
        resetPaddlePosition();

        engine.loadLevel(levelIndex);

        int bonus = GameState.INSTANCE.getPaddleWidthBonus();
        if (bonus > 0) {
            paddle.setWidth(100 + bonus);
        } else {
            paddle.setWidth(100);
        }

        updateCoinsUI();
        updateItemButtons();
        if (levelLabel != null) {
            levelLabel.setText(String.valueOf(levelIndex));
        }

        javafx.application.Platform.runLater(() -> {
            resetPaddlePosition();
            if (bonus > 0) {
                paddle.setWidth(100 + bonus);
            }
        });
    }

    // ========== POWER-UP SYSTEM ==========

    private void maybeSpawnPowerUpAt(double x, double y) {
        int p = rng.nextInt(100);
        PowerUpType type = null;

        if (p < 40) {
            if (p < 15) type = PowerUpType.COIN;
            else if (p < 25) type = PowerUpType.EXTRA_LIFE;
            else if (p < 35) type = PowerUpType.EXPAND_PADDLE;
            else type = PowerUpType.SLOW_BALL;
        }

        if (type != null) {
            PowerUp pu = new PowerUp(type, x, y);
            activePowerUps.add(pu);
            anchorPane.getChildren().add(pu);
        }
    }

    private void updatePowerUps() {
        Iterator<PowerUp> it = activePowerUps.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            p.update();

            if (p.getCenterY() > anchorPane.getHeight() + 20) {
                anchorPane.getChildren().remove(p);
                it.remove();
                continue;
            }

            if (p.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
                applyPowerUp(p.getType());
                anchorPane.getChildren().remove(p);
                it.remove();
            }
        }
    }

    private void applyPowerUp(PowerUpType type) {
        switch (type) {
            case COIN -> {
                GameState.INSTANCE.addCoins(1);
                updateCoinsUI();
                System.out.println("Collected coin! Coins = " + GameState.INSTANCE.getCoins());
            }
            case EXTRA_LIFE -> {
                System.out.println("Collected Extra Life power-up!");
            }
            case EXPAND_PADDLE -> {
                double oldWidth = paddle.getWidth();
                paddle.setWidth(oldWidth + 40);
                System.out.println("Paddle expanded!");
                Timeline t = new Timeline(new KeyFrame(Duration.seconds(10),
                        e -> paddle.setWidth(oldWidth)));
                t.setCycleCount(1);
                t.play();
            }
            case SLOW_BALL -> {
                System.out.println("Ball slowed down!");
            }
        }
    }

    // ========== SHOP ITEMS ==========

    private void useShopItem(int itemNumber) {
        if (isPaused) return;

        switch (itemNumber) {
            case 1 -> {
                if (!GameState.INSTANCE.useWideItem()) {
                    System.out.println("No Wide Item available! Buy from shop first.");
                    return;
                }

                if (item1Timeline != null) {
                    item1Timeline.stop();
                }

                double oldWidth = paddle.getWidth();
                paddle.setWidth(oldWidth + 50);
                System.out.println("Item 1 activated: Paddle expanded for 10 seconds!");

                item1Timeline = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
                    paddle.setWidth(oldWidth);
                    System.out.println("Item 1 expired!");
                    item1Timeline = null;
                }));
                item1Timeline.setCycleCount(1);
                item1Timeline.play();

                updateItemButtons();
            }
            case 2 -> {
                if (!GameState.INSTANCE.useLifeItem()) {
                    System.out.println("No Life Item available! Buy from shop first.");
                    return;
                }

                updateItemButtons();
                System.out.println("Item 2 activated: Extra life!");
            }
            case 3 -> {
                if (!GameState.INSTANCE.useSlowItem()) {
                    System.out.println("No Slow Item available! Buy from shop first.");
                    return;
                }

                if (item3Timeline != null) {
                    item3Timeline.stop();
                }

                double currentDx = engine.getBall().getDx();
                double currentDy = engine.getBall().getDy();
                engine.getBall().setDx(currentDx * 0.5);
                engine.getBall().setDy(currentDy * 0.5);
                System.out.println("Item 3 activated: Ball slowed for 10 seconds!");

                item3Timeline = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
                    engine.getBall().setDx(currentDx);
                    engine.getBall().setDy(currentDy);
                    System.out.println("Item 3 expired!");
                    item3Timeline = null;
                }));
                item3Timeline.setCycleCount(1);
                item3Timeline.play();

                updateItemButtons();
            }
        }
    }

    // ========== GAME CONTROL ==========

    private void togglePause() {
        isPaused = !isPaused;
        if (pauseButton != null) {
            pauseButton.setText(isPaused ? "▶" : "⏸");
        }
        System.out.println("Game " + (isPaused ? "paused" : "resumed"));
    }

    // ========== INITIALIZATION ==========

    public void initialize() {
        engine = new GameEngine(anchorPane, paddle, ball,
                score -> {
                    if (scoreLabel != null) {
                        scoreLabel.setText(String.valueOf(score));
                    }
                },
                l -> {
                    if (livesLabel != null) {
                        livesLabel.setText(String.valueOf(l));
                    }
                },
                level -> {
                    if (levelLabel != null) {
                        levelLabel.setText(String.valueOf(level));
                    }
                }
        );

        engine.setPowerUpUpdateCallback(powerUpText -> {
            if (powerUpsLabel != null) {
                powerUpsLabel.setText(powerUpText);
            }
        });

        engine.loadLevel(1);

        updateCoinsUI();
        updateItemButtons();

        javafx.application.Platform.runLater(() -> {
            resetPaddlePosition();
        });

        // Button handlers
        if (backButton != null) {
            backButton.setOnAction(e -> {
                try {
                    org.example.MainApp.showMainMenu();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            backButton.setFocusTraversable(false);
        }

        if (item1Button != null) {
            item1Button.setOnAction(e -> {
                useShopItem(1);
                anchorPane.requestFocus();
            });
            item1Button.setFocusTraversable(false);
        }

        if (item2Button != null) {
            item2Button.setOnAction(e -> {
                useShopItem(2);
                anchorPane.requestFocus();
            });
            item2Button.setFocusTraversable(false);
        }

        if (item3Button != null) {
            item3Button.setOnAction(e -> {
                useShopItem(3);
                anchorPane.requestFocus();
            });
            item3Button.setFocusTraversable(false);
        }

        if (pauseButton != null) {
            pauseButton.setOnAction(e -> {
                togglePause();
                anchorPane.requestFocus();
            });
            pauseButton.setFocusTraversable(false);
        }

        // Keyboard handlers
        anchorPane.setOnKeyPressed(e -> {
            activeKeys.add(e.getCode());

            if (e.getCode() == KeyCode.DIGIT1) {
                useShopItem(1);
            } else if (e.getCode() == KeyCode.DIGIT2) {
                useShopItem(2);
            } else if (e.getCode() == KeyCode.DIGIT3) {
                useShopItem(3);
            } else if (e.getCode() == KeyCode.P) {
                togglePause();
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

        // Game loop
        AnimationTimer timer = new AnimationTimer() {
            private long lastCoinUpdate = 0;

            @Override
            public void handle(long now) {
                if (!isPaused) {
                    if (activeKeys.contains(KeyCode.LEFT))  engine.movePaddleLeft();
                    if (activeKeys.contains(KeyCode.RIGHT)) engine.movePaddleRight();

                    engine.update();
                    updatePowerUps();
                }

                if (now - lastCoinUpdate > 500_000_000) {
                    updateCoinsUI();
                    updateItemButtons();
                    lastCoinUpdate = now;
                }

                if (!paddle.isVisible()) {
                    paddle.setVisible(true);
                }

                if (paddle.getOpacity() < 1.0) {
                    paddle.setOpacity(1.0);
                }
            }
        };
        timer.start();

        // Apply gradient to paddle
        javafx.application.Platform.runLater(() -> {
            javafx.scene.paint.LinearGradient gradient = new javafx.scene.paint.LinearGradient(
                    0, 0, 0, 1, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                    new javafx.scene.paint.Stop(0.0, javafx.scene.paint.Color.web("#00ffff")),
                    new javafx.scene.paint.Stop(0.5, javafx.scene.paint.Color.web("#00d4ff")),
                    new javafx.scene.paint.Stop(1.0, javafx.scene.paint.Color.web("#0080ff"))
            );
            paddle.setFill(gradient);
        });
    }
}