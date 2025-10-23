package org.example.logic;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.example.controller.GameState;
import java.util.*;
import java.util.function.IntConsumer;

public class GameEngine {


    private static final double HUD_HEIGHT = 48.0;

    private final AnchorPane pane;
    private final Paddle paddle;
    private final Ball ball;
    private final Random rng = new Random();

    private final List<Brick> bricks = new ArrayList<>();
    private final List<PowerUp> powerUps = new ArrayList<>();

    private final IntConsumer scoreCb, livesCb, levelCb;
    private Runnable gameOverCb;

    // state
    private int score = 0;
    private int lives = 3;
    private int level = 1;
    private boolean isGameOver = false;

    public GameEngine(AnchorPane pane, Rectangle paddleNode, Circle ballNode,
                      IntConsumer scoreCb, IntConsumer livesCb, IntConsumer levelCb) {
        this.pane = pane;
        this.paddle = new Paddle(paddleNode);
        this.ball = new Ball(ballNode);
        this.scoreCb = scoreCb;
        this.livesCb = livesCb;
        this.levelCb = levelCb;

        updateHUD();
    }

    public void setGameOverCallback(Runnable callback) {
        this.gameOverCb = callback;
    }

    public void loadLevel(int idx) {
        this.level = idx;
        levelCb.accept(level);

        // Xoá bricks & powerUps cũ (KHÔNG đụng paddle/ball)
        for (Brick b : bricks) pane.getChildren().remove(b.getNode());
        bricks.clear();
        for (PowerUp p : powerUps) pane.getChildren().remove(p);
        powerUps.clear();

        // Tạo bricks mới
        List<Brick> created = LevelLoader.createLevel(level, pane.getWidth());
        bricks.addAll(created);
        for (Brick b : bricks) pane.getChildren().add(b.getNode());

        resetBallAndPaddle();
    }

    public void movePaddleLeft()  { paddle.moveLeft(0); }
    public void movePaddleRight() { paddle.moveRight(pane.getWidth()); }

    public void update() {
        // Nếu game over, không update nữa
        if (isGameOver) return;
        
        // Di chuyển bóng
        ball.move();

        double r = ball.getR();
        double W = pane.getWidth();
        double H = pane.getHeight();


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


        if (ball.getY() - r > H) {
            loseLife();
            return;
        }


        if (intersects(ball.getNode(), paddle.getNode())) {

            if (ball.getDy() > 0) {
                ball.bounceY();
                ball.getNode().setCenterY(paddle.getNode().getY() - r - 1);
            }
        }


        for (Brick br : bricks) {
            if (!br.isDestroyed() && intersects(ball.getNode(), br.getNode())) {
                boolean wasDestroyed = br.onHit();
                ball.bounceY();

                if (wasDestroyed) {
                    score += br.getScoreValue();
                    scoreCb.accept(score);

                    maybeSpawnPowerUp(br);

                    System.out.println("Brick destroyed! Checking for powerup drop...");
                }
                break;
            }
        }


        updatePowerUps();

        if (allBreakableDestroyed()) {
            loadLevel(level + 1);
        }
    }

    private void resetBallAndPaddle() {
        Platform.runLater(() -> {
            paddle.resetSize(); // Reset kích thước paddle về ban đầu
            paddle.getNode().setX((pane.getWidth() - paddle.getNode().getWidth()) / 2);
            paddle.getNode().setY(pane.getHeight() - 40);

            double ballX = paddle.getNode().getX() + paddle.getNode().getWidth() / 2;
            double ballY = paddle.getNode().getY() - 20;
            ball.reset(ballX, ballY);
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
        lives--;
        livesCb.accept(lives);
        if (lives <= 0 && !isGameOver) {
            isGameOver = true;
            if (gameOverCb != null) {
                Platform.runLater(gameOverCb);
            }
        } else if (lives > 0) {
            resetBallAndPaddle();
        }
    }

    private void updateHUD() {
        scoreCb.accept(score);
        livesCb.accept(lives);
        levelCb.accept(level);
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
            if (chance < 15) {
                type = PowerUpType.COIN;
            } else if (chance < 10) {
                type = PowerUpType.EXTRA_LIFE;
            } else if (chance < 29) {
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
        switch (p.getType()) {
            case COIN -> {
                score += 50;
                scoreCb.accept(score);
                org.example.controller.GameState.INSTANCE.addCoins(1);
                System.out.println("Nhặt coin! Total coins: " + org.example.controller.GameState.INSTANCE.getCoins());
            }
            case EXTRA_LIFE -> {
                lives++;
                livesCb.accept(lives);
            }
            case EXPAND_PADDLE -> {
                paddle.getNode().setWidth(paddle.getNode().getWidth() + 30);
            }
            case SLOW_BALL -> {
                ball.setDx(ball.getDx() * 0.8);
                ball.setDy(ball.getDy() * 0.8);
            }
        }
    }
}
