package org.example.logic;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.example.controller.GameState;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameEngine {

    private static final double HUD_HEIGHT = 48.0;
    private static final int LEVEL_COMPLETE_DELAY = 1500;
    private static final double GAME_AREA_WIDTH = 920.0;
    private static final double GAME_AREA_HEIGHT = 620.0;

    private final AnchorPane pane;
    private final Paddle paddle;
    private final Ball ball;
    private final Random rng = new Random();

    private final List<Brick> bricks = new ArrayList<>();
    private final List<PowerUp> powerUps = new ArrayList<>();
    private final List<ActivePowerUp> activePowerUps = new ArrayList<>();
    private Consumer<String> powerUpUpdateCb;

    private int score = 0;
    private int lives = 3;
    private int level = 1;
    private boolean levelCompleting = false;
    private long levelCompleteTime = 0;
    private boolean isProcessingLifeLoss = false;  // NEW: Prevent multiple life loss
    private boolean isGameOver = false;  // NEW: Track game over state

    private double originalPaddleWidth = 0;
    private double originalBallDx = 0;
    private double originalBallDy = 0;

    private IntConsumer scoreCb;
    private IntConsumer livesCb;
    private IntConsumer levelCb;

    public GameEngine(AnchorPane pane, Rectangle paddleNode, Circle ballNode,
                      IntConsumer scoreCb, IntConsumer livesCb, IntConsumer levelCb) {
        this.pane = pane;
        this.paddle = new Paddle(paddleNode);
        this.ball = new Ball(ballNode);
        this.scoreCb = scoreCb;
        this.livesCb = livesCb;
        this.levelCb = levelCb;

        this.originalPaddleWidth = paddleNode.getWidth();

        updateHUD();
    }

    public void setPowerUpUpdateCallback(Consumer<String> callback) {
        this.powerUpUpdateCb = callback;
    }

    public void loadLevel(int idx) {
        this.level = idx;
        levelCb.accept(level);
        levelCompleting = false;
        isProcessingLifeLoss = false;  // Reset flag when loading new level
        isGameOver = false;  // Reset game over state

        for (Brick b : bricks) pane.getChildren().remove(b.getNode());
        bricks.clear();
        for (PowerUp p : powerUps) pane.getChildren().remove(p);
        powerUps.clear();

        List<Brick> created = LevelLoader.createLevel(level, pane.getWidth());
        bricks.addAll(created);
        for (Brick b : bricks) pane.getChildren().add(b.getNode());

        resetBallAndPaddle();
    }

    public void movePaddleLeft()  {
        paddle.moveLeft(0);
    }

    public void movePaddleRight() {
        paddle.moveRight(GAME_AREA_WIDTH);
    }

    public Ball getBall() {
        return ball;
    }

    public void update() {
        // Don't update if game is over or processing life loss
        if (isGameOver || isProcessingLifeLoss) {
            return;
        }

        if (levelCompleting) {
            if (System.currentTimeMillis() - levelCompleteTime >= LEVEL_COMPLETE_DELAY) {
                loadLevel(level + 1);
            }
            return;
        }

        ball.move();

        double r = ball.getR();
        double W = GAME_AREA_WIDTH;
        double H = GAME_AREA_HEIGHT;
        double HUD_HEIGHT = 60.0;

        if (ball.getX() - r <= 0) {
            ball.bounceX();
            ball.getNode().setCenterX(r + 1);
        }

        if (ball.getX() + r >= W) {
            ball.bounceX();
            ball.getNode().setCenterX(W - r - 1);
        }

        if (ball.getY() - r <= HUD_HEIGHT) {
            ball.bounceY();
            ball.getNode().setCenterY(HUD_HEIGHT + r + 1);
        }

        // Improved paddle collision - more precise
        double ballCenterX = ball.getX();
        double ballCenterY = ball.getY();
        double ballBottom = ballCenterY + r;

        double paddleLeft = paddle.getNode().getX();
        double paddleRight = paddleLeft + paddle.getNode().getWidth();
        double paddleTop = paddle.getNode().getY();
        double paddleBottom = paddleTop + paddle.getNode().getHeight();

        // Only bounce if ball is moving downward and actually intersects paddle
        if (ball.getDy() > 0) {
            // Check if ball bottom is within paddle Y range
            if (ballBottom >= paddleTop && ballBottom <= paddleBottom + 3) {
                // Check if ball center X is within paddle X range
                if (ballCenterX >= paddleLeft && ballCenterX <= paddleRight) {
                    ball.bounceY();
                    // Position ball just above paddle to prevent sticking
                    ball.getNode().setCenterY(paddleTop - r - 1);
                }
            }
        }

        // Check if ball fell below paddle - with flag protection
        if (ballBottom >= H && !isProcessingLifeLoss) {
            loseLife();
            return;
        }

        for (Brick br : bricks) {
            if (!br.isDestroyed() && intersects(ball.getNode(), br.getNode())) {
                boolean wasDestroyed = br.onHit();
                ball.bounceY();

                if (wasDestroyed) {
                    score += br.getScoreValue();
                    scoreCb.accept(score);
                    maybeSpawnPowerUp(br);
                }
                break;
            }
        }

        updatePowerUps();
        updateActivePowerUps();

        if (allBreakableDestroyed()) {
            levelCompleting = true;
            levelCompleteTime = System.currentTimeMillis();
            score += 500;
            scoreCb.accept(score);
            GameState.INSTANCE.addCoins(2);
        }
    }

    private void resetBallAndPaddle() {
        Platform.runLater(() -> {
            // Reset paddle position - near bottom
            double paddleX = (GAME_AREA_WIDTH - paddle.getNode().getWidth()) / 2;
            double paddleY = 580;  // Near bottom of 620px height
            paddle.getNode().setX(paddleX);
            paddle.getNode().setY(paddleY);

            // Reset ball position - on top of paddle center
            double ballX = paddleX + paddle.getNode().getWidth() / 2;
            double ballY = paddleY - 20;
            ball.reset(ballX, ballY);

            System.out.println("Reset - Paddle: (" + paddleX + ", " + paddleY + "), Ball: (" + ballX + ", " + ballY + ")");

            // Reset processing flag after a short delay to prevent immediate re-trigger
            Timeline resetDelay = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                isProcessingLifeLoss = false;
            }));
            resetDelay.setCycleCount(1);
            resetDelay.play();
        });
    }

    private boolean allBreakableDestroyed() {
        for (Brick b : bricks) {
            if (!b.isIndestructible() && !b.isDestroyed()) return false;
        }
        return true;
    }

    private boolean intersects(Node a, Node b) {
        return a.getBoundsInParent().intersects(b.getBoundsInParent());
    }

    private void loseLife() {
        // Prevent multiple calls
        if (isProcessingLifeLoss || isGameOver) {
            return;
        }

        isProcessingLifeLoss = true;
        lives--;

        if (livesCb != null) {
            livesCb.accept(lives);
        }

        if (lives <= 0) {
            // Game Over
            isGameOver = true;
            int finalScore = score;
            int finalLevel = level;

            Platform.runLater(() -> {
                // Show game over dialog
                org.example.ui.GameOverDialog.show(finalScore, finalLevel);

                // Reset game state after dialog
                lives = 3;
                score = 0;
                isGameOver = false;
                isProcessingLifeLoss = false;
                updateHUD();
                loadLevel(1);
            });
        } else {
            // Lost a life but still have lives remaining
            // Reset ball and paddle immediately without delay
            resetBallAndPaddle();
        }
    }

    private void updateHUD() {
        if (scoreCb != null) scoreCb.accept(score);
        if (livesCb != null) livesCb.accept(lives);
        if (levelCb != null) levelCb.accept(level);
    }

    public void resetGame() {
        lives = 3;
        score = 0;
        isGameOver = false;
        updateHUD();
    }

    public int getCurrentLevel() {
        return level;
    }

    private void maybeSpawnPowerUp(Brick br) {
        int chance = rng.nextInt(100);
        if (chance < 35) {
            PowerUpType type;
            if (chance < 10) {
                type = PowerUpType.COIN;
            } else if (chance < 15) {
                type = PowerUpType.EXTRA_LIFE;
            } else if (chance < 25) {
                type = PowerUpType.EXPAND_PADDLE;
            } else {
                type = PowerUpType.SLOW_BALL;
            }

            PowerUp pu = new PowerUp(
                    type,
                    br.getNode().getBoundsInParent().getCenterX(),
                    br.getNode().getBoundsInParent().getCenterY()
            );
            powerUps.add(pu);
            pane.getChildren().add(pu);
        }
    }

    private void updatePowerUps() {
        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            p.update();

            if (intersects(p, paddle.getNode())) {
                applyPowerUp(p);
                pane.getChildren().remove(p);
                it.remove();
                continue;
            }

            if (p.getCenterY() > pane.getHeight()) {
                pane.getChildren().remove(p);
                it.remove();
            }
        }
    }

    private void applyPowerUp(PowerUp p) {
        PowerUpType type = p.getType();

        switch (type) {
            case COIN -> {
                score += 50;
                if (scoreCb != null) scoreCb.accept(score);
                GameState.INSTANCE.addCoins(1);
            }
            case EXTRA_LIFE -> {
                lives++;
                if (livesCb != null) livesCb.accept(lives);
            }
            case EXPAND_PADDLE -> {
                if (originalPaddleWidth == 0) {
                    originalPaddleWidth = paddle.getNode().getWidth();
                }
                paddle.getNode().setWidth(paddle.getNode().getWidth() + 30);
                activePowerUps.add(new ActivePowerUp(PowerUpType.EXPAND_PADDLE, 10000));
                updatePowerUpUI();
            }
            case SLOW_BALL -> {
                if (originalBallDx == 0) {
                    originalBallDx = ball.getDx();
                    originalBallDy = ball.getDy();
                }
                ball.setDx(ball.getDx() * 0.7);
                ball.setDy(ball.getDy() * 0.7);
                activePowerUps.add(new ActivePowerUp(PowerUpType.SLOW_BALL, 10000));
                updatePowerUpUI();
            }
        }
    }

    private void updateActivePowerUps() {
        Iterator<ActivePowerUp> it = activePowerUps.iterator();
        while (it.hasNext()) {
            ActivePowerUp pu = it.next();

            if (pu.isExpired()) {
                switch (pu.getType()) {
                    case EXPAND_PADDLE -> {
                        paddle.getNode().setWidth(originalPaddleWidth);
                    }
                    case SLOW_BALL -> {
                        ball.setDx(originalBallDx);
                        ball.setDy(originalBallDy);
                    }
                }
                pu.deactivate();
                it.remove();
                updatePowerUpUI();
            }
        }
    }

    private void updatePowerUpUI() {
        if (powerUpUpdateCb != null) {
            if (activePowerUps.isEmpty()) {
                powerUpUpdateCb.accept("None");
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < activePowerUps.size(); i++) {
                    ActivePowerUp pu = activePowerUps.get(i);
                    sb.append(pu.getType().name())
                            .append(" (")
                            .append(pu.getTimeRemainingSeconds())
                            .append("s)");
                    if (i < activePowerUps.size() - 1) {
                        sb.append("\n");
                    }
                }
                powerUpUpdateCb.accept(sb.toString());
            }
        }
    }
}
