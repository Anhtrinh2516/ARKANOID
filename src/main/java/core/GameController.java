package core;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import logic.*;
import controller.GameState;
import controller.GameStateManager;
import controller.SkinManager;
import storycontroller.StorySceneController;

import java.util.*;

public class GameController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Rectangle paddle;
    @FXML
    private Circle ball;

    @FXML
    private Label scoreLabel;
    @FXML
    private Label livesLabel;
    @FXML
    private Label levelLabel;
    @FXML
    private Label hintLabel;
    @FXML
    private Label coinsLabel;
    @FXML
    private Label powerUpsLabel;
    @FXML
    private Button backButton;

    @FXML
    private Button item1Button;
    @FXML
    private Button item2Button;
    @FXML
    private Button item3Button;
    @FXML
    private Button pauseButton;

    private static final double W = 888.0;
    private static final double H = 708.0;

    private final Random rng = new Random();
    private final Set<KeyCode> keys = new HashSet<>();
    private final List<PowerUp> powerUps = new ArrayList<>();

    private GameEngine engine;
    private int lvl = 1;
    private int score = 0;
    private int lives = 3;

    private boolean paused = false;
    private AnimationTimer timer;

    private Timeline t1;
    private Timeline t2;
    private Timeline t3;

    private void resetPaddle() {
        paddle.setX((W - paddle.getWidth()) / 2);
        paddle.setY(H - 58);
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

    private void updateCoins() {
        if (coinsLabel != null) {
            coinsLabel.setText(String.valueOf(GameState.INSTANCE.getCoins()));
        }
    }

    private void updateItems() {
        if (item1Button != null) {
            int c = GameState.INSTANCE.getWideItemCount();
            item1Button.setDisable(c == 0);
            item1Button.setOpacity(c == 0 ? 0.5 : 1.0);
        }
        if (item2Button != null) {
            int c = GameState.INSTANCE.getLifeItemCount();
            item2Button.setDisable(c == 0);
            item2Button.setOpacity(c == 0 ? 0.5 : 1.0);
        }
        if (item3Button != null) {
            int c = GameState.INSTANCE.getSlowItemCount();
            item3Button.setDisable(c == 0);
            item3Button.setOpacity(c == 0 ? 0.5 : 1.0);
        }
    }

    private void applyShop() {
        javafx.application.Platform.runLater(() -> {
            SkinManager.PaddleSkin ps = SkinManager.INSTANCE.getPaddleSkin();

            if (ps.type == SkinManager.SkinType.EVENT && ps.imagePath != null) {
                try {
                    javafx.scene.image.Image img = new javafx.scene.image.Image(
                            getClass().getResourceAsStream(ps.imagePath));
                    paddle.setFill(new javafx.scene.paint.ImagePattern(img));
                    paddle.setStroke(javafx.scene.paint.Color.TRANSPARENT);
                    paddle.setStrokeWidth(0);
                } catch (Exception e) {
                    paddle.setFill(javafx.scene.paint.Color.web(ps.fill));
                    paddle.setStroke(javafx.scene.paint.Color.web(ps.stroke));
                    paddle.setStrokeWidth(3);
                }
            } else {
                paddle.setFill(javafx.scene.paint.Color.web(ps.fill));
                paddle.setStroke(javafx.scene.paint.Color.web(ps.stroke));
                paddle.setStrokeWidth(3);
            }

            SkinManager.BallSkin bs = SkinManager.INSTANCE.getBallSkin();

            if (bs.type == SkinManager.SkinType.EVENT && bs.imagePath != null) {
                try {
                    javafx.scene.image.Image img = new javafx.scene.image.Image(
                            getClass().getResourceAsStream(bs.imagePath));
                    ball.setFill(new javafx.scene.paint.ImagePattern(img));
                    ball.setStroke(javafx.scene.paint.Color.TRANSPARENT);
                    ball.setStrokeWidth(0);
                } catch (Exception e) {
                    ball.setFill(javafx.scene.paint.Color.web(bs.color));
                    ball.setStroke(javafx.scene.paint.Color.BLACK);
                    ball.setStrokeWidth(2);
                }
            } else {
                ball.setFill(javafx.scene.paint.Color.web(bs.color));
                ball.setStroke(javafx.scene.paint.Color.BLACK);
                ball.setStrokeWidth(2);
            }
        });
    }

    public void startLevel(int l) {
        startLevel(l, false);
    }
    
    public void startClassicMode() {
        engine.setGameMode("CLASSIC");
        this.lvl = 1;
        this.score = 0;
        this.lives = 3;
        startLevel(1, false);
    }
    
    public void startLevel(int l, boolean cont) {
        this.lvl = l;

        if (cont && GameStateManager.INSTANCE.hasGameInProgress()) {
            score = GameStateManager.INSTANCE.getSavedScore();
            lives = GameStateManager.INSTANCE.getSavedLives();

            engine.loadLevel(l);
            engine.restoreGameState(score, lives);

            paddle.setX(GameStateManager.INSTANCE.getSavedPaddleX());
            ball.setCenterX(GameStateManager.INSTANCE.getSavedBallX());
            ball.setCenterY(GameStateManager.INSTANCE.getSavedBallY());
            engine.getBall().setDx(GameStateManager.INSTANCE.getSavedBallDx());
            engine.getBall().setDy(GameStateManager.INSTANCE.getSavedBallDy());
        } else {
            resetPaddle();
            engine.loadLevel(l);
            engine.restoreGameState(score, lives);

            if (hintLabel != null) {
                hintLabel.setText("[Q/E] Aim | [SPACE] Launch | [Arrow] Move");
                Timeline t = new Timeline(new KeyFrame(Duration.seconds(4),
                        e -> hintLabel.setText("")));
                t.play();
            }
        }

        int bonus = GameState.INSTANCE.getPaddleWidthBonus();
        paddle.setWidth(bonus > 0 ? 100 + bonus : 100);

        applyShop();
        updateCoins();
        updateItems();
        if (levelLabel != null) {
            levelLabel.setText(String.valueOf(l));
        }
    }

    private void updatePowerUps() {
        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            p.update();

            if (p.getCenterY() > anchorPane.getHeight() + 20) {
                anchorPane.getChildren().remove(p);
                it.remove();
                continue;
            }

            if (p.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
                applyPower(p.getType());
                anchorPane.getChildren().remove(p);
                it.remove();
            }
        }
    }

    private void applyPower(PowerUpType type) {
        switch (type) {
            case COIN -> {
                GameState.INSTANCE.addCoins(1);
                updateCoins();
            }
            case EXTRA_LIFE -> {
                lives++;
                engine.restoreGameState(score, lives);
                if (livesLabel != null) {
                    livesLabel.setText(String.valueOf(lives));
                }
            }
            case EXPAND_PADDLE -> {
                double w = paddle.getWidth();
                paddle.setWidth(w + 40);
                Timeline t = new Timeline(new KeyFrame(Duration.seconds(10),
                        e -> paddle.setWidth(w)));
                t.setCycleCount(1);
                t.play();
            }
            case SLOW_BALL -> {
                double dx = engine.getBall().getDx();
                double dy = engine.getBall().getDy();
                engine.getBall().setDx(dx * 0.7);
                engine.getBall().setDy(dy * 0.7);

                Timeline t = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
                    engine.getBall().setDx(dx);
                    engine.getBall().setDy(dy);
                }));
                t.setCycleCount(1);
                t.play();
            }
            case MULTIBALL -> {
            }
        }
    }

    private void useItem(int n) {
        if (paused)
            return;

        switch (n) {
            case 1 -> {
                if (!GameState.INSTANCE.useWideItem()) {
                    if (hintLabel != null) {
                        hintLabel.setText("No Wide Paddle items!");
                        Timeline t = new Timeline(new KeyFrame(Duration.seconds(2),
                                e -> hintLabel.setText("")));
                        t.play();
                    }
                    return;
                }

                if (t1 != null)
                    t1.stop();

                double w = paddle.getWidth();
                paddle.setWidth(w + 50);

                if (hintLabel != null)
                    hintLabel.setText("Wide Paddle activated!");

                t1 = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
                    paddle.setWidth(w);
                    if (hintLabel != null) {
                        hintLabel.setText("Wide Paddle expired");
                        Timeline t = new Timeline(new KeyFrame(Duration.seconds(2),
                                ev -> hintLabel.setText("")));
                        t.play();
                    }
                    t1 = null;
                }));
                t1.setCycleCount(1);
                t1.play();

                updateItems();
            }
            case 2 -> {
                if (!GameState.INSTANCE.useLifeItem()) {
                    if (hintLabel != null) {
                        hintLabel.setText("No Extra Life items!");
                        Timeline t = new Timeline(new KeyFrame(Duration.seconds(2),
                                e -> hintLabel.setText("")));
                        t.play();
                    }
                    return;
                }

                lives++;
                engine.restoreGameState(score, lives);

                if (livesLabel != null) {
                    livesLabel.setText(String.valueOf(lives));
                }

                if (hintLabel != null) {
                    hintLabel.setText("Extra Life used!");
                    Timeline t = new Timeline(new KeyFrame(Duration.seconds(2),
                            e -> hintLabel.setText("")));
                    t.play();
                }

                updateItems();
            }
            case 3 -> {
                if (!GameState.INSTANCE.useSlowItem()) {
                    if (hintLabel != null) {
                        hintLabel.setText("No Slow Ball items!");
                        Timeline t = new Timeline(new KeyFrame(Duration.seconds(2),
                                e -> hintLabel.setText("")));
                        t.play();
                    }
                    return;
                }

                if (t3 != null)
                    t3.stop();

                double dx = engine.getBall().getDx();
                double dy = engine.getBall().getDy();
                engine.getBall().setDx(dx * 0.6);
                engine.getBall().setDy(dy * 0.6);

                if (hintLabel != null)
                    hintLabel.setText("Slow Ball activated!");

                t3 = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
                    if (Math.signum(engine.getBall().getDx()) == Math.signum(dx)) {
                        engine.getBall().setDx(dx);
                        engine.getBall().setDy(dy);
                    }
                    if (hintLabel != null) {
                        hintLabel.setText("Slow Ball expired");
                        Timeline t = new Timeline(new KeyFrame(Duration.seconds(2),
                                ev -> hintLabel.setText("")));
                        t.play();
                    }
                    t3 = null;
                }));
                t3.setCycleCount(1);
                t3.play();

                updateItems();
            }
        }
    }

    private void togglePause() {
        paused = !paused;
        if (pauseButton != null) {
            pauseButton.setText(paused ? "▶" : "⏸");
        }

        if (hintLabel != null) {
            if (paused) {
                hintLabel.setText("PAUSED - Press P");
            } else {
                hintLabel.setText("RESUMED");
                Timeline t = new Timeline(new KeyFrame(Duration.seconds(2),
                        e -> hintLabel.setText("")));
                t.play();
            }
        }
    }

    public void initialize() {
        controller.SoundManager.INSTANCE.playGameMusic();
        engine = new GameEngine(anchorPane, paddle, ball,
                s -> {
                    score = s;
                    if (scoreLabel != null) scoreLabel.setText(String.valueOf(s));
                },
                l -> {
                    lives = l;
                    if (livesLabel != null) livesLabel.setText(String.valueOf(l));
                },
                lv -> {
                    lvl = lv;
                    if (levelLabel != null) levelLabel.setText(String.valueOf(lv));
                },
                completedLevel -> {
                    handleLevelComplete(completedLevel);
                }
        );

        engine.setPowerUpUpdateCallback(txt -> {
            if (powerUpsLabel != null)
                powerUpsLabel.setText(txt);
        });

        engine.loadLevel(1);
        resetPaddle();
        applyShop();

        int bonus = GameState.INSTANCE.getPaddleWidthBonus();
        if (bonus > 0)
            paddle.setWidth(100 + bonus);

        updateCoins();
        updateItems();

        if (backButton != null) {
            backButton.setOnAction(e -> {
                saveGame();
                if (timer != null)
                    timer.stop();
                try {
                    MainApp.showMainMenu();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            backButton.setFocusTraversable(false);
        }

        if (item1Button != null) {
            item1Button.setOnAction(e -> {
                useItem(1);
                anchorPane.requestFocus();
            });
            item1Button.setFocusTraversable(false);
        }

        if (item2Button != null) {
            item2Button.setOnAction(e -> {
                useItem(2);
                anchorPane.requestFocus();
            });
            item2Button.setFocusTraversable(false);
        }

        if (item3Button != null) {
            item3Button.setOnAction(e -> {
                useItem(3);
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

        anchorPane.setOnKeyPressed(e -> {
            keys.add(e.getCode());

            if (e.getCode() == KeyCode.SPACE) {
                if (engine.getBall().isAttached()) {
                    engine.launchBall();
                    if (hintLabel != null) {
                        hintLabel.setText("Ball launched!");
                        Timeline t = new Timeline(new KeyFrame(Duration.seconds(1.5),
                                ev -> hintLabel.setText("")));
                        t.play();
                    }
                }
            } else if (e.getCode() == KeyCode.P) {
                togglePause();
            } else if (e.getCode() == KeyCode.DIGIT1 || e.getCode() == KeyCode.NUMPAD1) {
                useItem(1);
            } else if (e.getCode() == KeyCode.DIGIT2 || e.getCode() == KeyCode.NUMPAD2) {
                useItem(2);
            } else if (e.getCode() == KeyCode.DIGIT3 || e.getCode() == KeyCode.NUMPAD3) {
                useItem(3);
            }
        });

        anchorPane.setOnKeyReleased(e -> keys.remove(e.getCode()));

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

        timer = new AnimationTimer() {
            private long lastUpdate = 0;
            private long lastAim = 0;

            @Override
            public void handle(long now) {
                if (!paused) {
                    if (keys.contains(KeyCode.LEFT) || keys.contains(KeyCode.A))
                        engine.movePaddleLeft();
                    if (keys.contains(KeyCode.RIGHT) || keys.contains(KeyCode.D))
                        engine.movePaddleRight();

                    if (engine.getBall().isAttached()) {
                        if (now - lastAim > 50_000_000) {
                            if (keys.contains(KeyCode.Q)) {
                                engine.adjustAimLeft();
                                lastAim = now;
                            }
                            if (keys.contains(KeyCode.E)) {
                                engine.adjustAimRight();
                                lastAim = now;
                            }
                        }
                    }

                    engine.update();
                    updatePowerUps();
                }

                if (now - lastUpdate > 500_000_000) {
                    updateCoins();
                    updateItems();
                    lastUpdate = now;
                }

                if (!paddle.isVisible())
                    paddle.setVisible(true);
                if (paddle.getOpacity() < 1.0)
                    paddle.setOpacity(1.0);
            }
        };
        timer.start();
    }

    private void handleLevelComplete(int completedLevel) {
        int nextLevel = completedLevel + 1;
        showStoryScene(completedLevel);
        startLevel(nextLevel, false);
    }

    private void showStoryScene(int levelIndex) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StoryScene.fxml"));
            AnchorPane storyRoot = loader.load();
            Object storyControllerObj = loader.getController();

            Stage storyStage = new Stage();
            storyStage.initModality(Modality.APPLICATION_MODAL);
            storyStage.initOwner(anchorPane.getScene().getWindow());
            storyStage.setScene(new Scene(storyRoot, 1148, 708));
            storyStage.setTitle("Story - Level " + levelIndex);
            storyStage.setResizable(false);

            if (storyControllerObj instanceof storycontroller.StorySceneController) {
                ((storycontroller.StorySceneController) storyControllerObj).loadAndStartStory(levelIndex);
            } else {
                System.err.println("Không thể tìm thấy StorySceneController hoặc tên class không đúng.");
            }

            storyStage.showAndWait();

        } catch (Exception e) {
            System.err.println("GameController: Lỗi khi load StoryScene cho level " + levelIndex);
            e.printStackTrace();
        }
    }

    private void saveGame() {
        if (lives > 0) {
            GameStateManager.INSTANCE.save(
                    lvl,
                    score,
                    lives,
                    ball.getCenterX(),
                    ball.getCenterY(),
                    engine.getBall().getDx(),
                    engine.getBall().getDy(),
                    paddle.getX());
        } else {
            GameStateManager.INSTANCE.clear();
        }
    }
}
