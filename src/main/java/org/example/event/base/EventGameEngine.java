package org.example.event.base;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.example.controller.GameState;
import org.example.logic.Ball;
import org.example.logic.Brick;
import org.example.logic.Paddle;

import java.util.*;
import java.util.function.IntConsumer;

public abstract class EventGameEngine {

    protected static final int levelDelay = 1500;
    protected static final double GAME_AREA_WIDTH = 888.0;
    protected static final double GAME_AREA_HEIGHT = 708.0;

    protected final AnchorPane pane;
    protected final Paddle paddle;
    protected final Ball ball;

    protected final List<Brick> bricks = new ArrayList<>();

    protected int score = 0;
    protected int lives = 3;
    protected int level = 1;
    protected boolean levelDone = false;
    protected long levelTime = 0;
    protected boolean gameOver = false;

    protected IntConsumer scoreCallback;
    protected IntConsumer livesCallback;
    protected IntConsumer levelCallback;
    protected Runnable winCallback;

    public EventGameEngine(AnchorPane pane, Rectangle paddleRect, Circle ballCircle,
                           IntConsumer scoreCb, IntConsumer livesCb, IntConsumer levelCb) {
        this.pane = pane;
        this.paddle = new Paddle(paddleRect);
        this.ball = new Ball(ballCircle);
        this.scoreCallback = scoreCb;
        this.livesCallback = livesCb;
        this.levelCallback = levelCb;

        ball.initializeArrow(pane);
        updateHUD();
    }
    
    public void setOnWinCallback(Runnable callback) {
        this.winCallback = callback;
    }

    protected abstract List<Brick> loadLevelBricks(int levelIdx);

    protected abstract void onBrickDestroyed(Brick brick);

    protected abstract boolean checkWinCondition();

    protected abstract int getLevelCompleteBonus();

    protected abstract int getLevelCompleteCoins();
    
    protected abstract boolean isFinalLevel(int levelIdx);

    protected void onUpdate() {
    }

    protected void onLevelComplete(int levelIdx) {
    }

    protected void onLifeLost() {
    }

    public void loadLevel(int levelIdx) {
        this.level = levelIdx;
        levelCallback.accept(level);
        levelDone = false;
        gameOver = false;

        for (Brick brick : bricks) pane.getChildren().remove(brick.getNode());
        bricks.clear();

        List<Brick> created = loadLevelBricks(levelIdx);
        bricks.addAll(created);
        for (Brick brick : bricks) pane.getChildren().add(brick.getNode());

        resetBallAndPaddle();
    }

    public void movePaddleLeft() {
        paddle.moveLeft(0);
        if (ball.isAttached()) {
            ball.updateAttachment(
                paddle.getNode().getX(),
                paddle.getNode().getWidth(),
                paddle.getNode().getY()
            );
        }
    }

    public void movePaddleRight() {
        paddle.moveRight(GAME_AREA_WIDTH);
        if (ball.isAttached()) {
            ball.updateAttachment(
                paddle.getNode().getX(),
                paddle.getNode().getWidth(),
                paddle.getNode().getY()
            );
        }
    }

    public void adjustAimLeft() {
        if (ball.isAttached()) {
            ball.adjustLaunchAngle(-5);
        }
    }

    public void adjustAimRight() {
        if (ball.isAttached()) {
            ball.adjustLaunchAngle(5);
        }
    }

    public void launchBall() {
        ball.launch();
    }

    public Ball getBall() {
        return ball;
    }

    public void update() {
        if (gameOver) {
            return;
        }

        if (levelDone) {
            if (System.currentTimeMillis() - levelTime >= levelDelay) {
                if (isFinalLevel(level)) {
                    showWinScreen();
                    gameOver = true;
                } else {
                    loadLevel(level + 1);
                }
            }
            return;
        }

        if (ball.isAttached()) {
            ball.updateAttachment(
                paddle.getNode().getX(),
                paddle.getNode().getWidth(),
                paddle.getNode().getY()
            );
            return;
        }

        // Ball đang bay - physics
        ball.move();

        double r = ball.getR();
        double W = GAME_AREA_WIDTH;
        double H = GAME_AREA_HEIGHT;
        double HUD_HEIGHT = 60.0;

        // Va chạm tường trái/phải
        if (ball.getX() - r <= 0) {
            ball.bounceX();
            ball.getNode().setCenterX(r + 1);
        }

        if (ball.getX() + r >= W) {
            ball.bounceX();
            ball.getNode().setCenterX(W - r - 1);
        }

        // Va chạm trần
        if (ball.getY() - r <= HUD_HEIGHT) {
            ball.bounceY();
            ball.getNode().setCenterY(HUD_HEIGHT + r + 1);
        }

        // Va chạm paddle - Arkanoid style
        handlePaddleCollision(r);

        // Ball rơi xuống - mất mạng
        double ballBottom = ball.getY() + r;
        if (ballBottom >= H) {
            loseLife();
            return;
        }

        // ========== 🔧 VA CHẠM BRICK - FIXED ==========
        for (Brick br : bricks) {
            if (!br.isDestroyed() && intersects(ball.getNode(), br.getNode())) {
                // 🎯 Phát hiện hướng va chạm
                CollisionSide side = detectCollisionSide(br);
                
                boolean wasDestroyed = br.onHit();

                // ✅ Bounce theo đúng hướng
                switch (side) {
                    case TOP:
                    case BOTTOM:
                        ball.bounceY();
                        break;
                    case LEFT:
                    case RIGHT:
                        ball.bounceX();
                        break;
                }

                if (wasDestroyed) {
                    score += br.getScoreValue();
                    scoreCallback.accept(score);
                    
                    onBrickDestroyed(br);
                }
                break;
            }
        }

        onUpdate();

        if (checkWinCondition()) {
            levelDone = true;
            levelTime = System.currentTimeMillis();
            score += getLevelCompleteBonus();
            scoreCallback.accept(score);
            GameState.INSTANCE.addCoins(getLevelCompleteCoins());
            onLevelComplete(level);
        }
    }

    protected enum CollisionSide {
        TOP, BOTTOM, LEFT, RIGHT
    }

    protected CollisionSide detectCollisionSide(Brick brick) {
        Node brickNode = brick.getNode();
        
        double ballCenterX = ball.getX();
        double ballCenterY = ball.getY();
        double ballR = ball.getR();
        
        double brickLeft = brickNode.getBoundsInParent().getMinX();
        double brickRight = brickNode.getBoundsInParent().getMaxX();
        double brickTop = brickNode.getBoundsInParent().getMinY();
        double brickBottom = brickNode.getBoundsInParent().getMaxY();
        
        double brickCenterX = (brickLeft + brickRight) / 2;
        double brickCenterY = (brickTop + brickBottom) / 2;
        
        // Tính độ chênh lệch vị trí
        double deltaX = ballCenterX - brickCenterX;
        double deltaY = ballCenterY - brickCenterY;
        
        // Tính tỷ lệ overlap theo từng trục
        double brickHalfWidth = (brickRight - brickLeft) / 2;
        double brickHalfHeight = (brickBottom - brickTop) / 2;
        
        double overlapX = (brickHalfWidth + ballR) - Math.abs(deltaX);
        double overlapY = (brickHalfHeight + ballR) - Math.abs(deltaY);
        
        // Overlap nhỏ hơn => đó là hướng va chạm
        if (overlapX < overlapY) {
            // Va chạm từ trái hoặc phải
            return deltaX > 0 ? CollisionSide.LEFT : CollisionSide.RIGHT;
        } else {
            // Va chạm từ trên hoặc dưới
            return deltaY > 0 ? CollisionSide.TOP : CollisionSide.BOTTOM;
        }
    }

    // ========== PRIVATE HELPERS ==========

    private void handlePaddleCollision(double r) {
        double ballCenterX = ball.getX();
        double ballCenterY = ball.getY();
        double ballBottom = ballCenterY + r;

        double paddleLeft = paddle.getNode().getX();
        double paddleRight = paddleLeft + paddle.getNode().getWidth();
        double paddleTop = paddle.getNode().getY();
        double paddleBottom = paddleTop + paddle.getNode().getHeight();

        if (ball.getDy() > 0) {
            if (ballBottom >= paddleTop && ballBottom <= paddleBottom + 5) {
                if (ballCenterX >= paddleLeft && ballCenterX <= paddleRight) {
                    double hitPos = (ballCenterX - paddleLeft) / paddle.getNode().getWidth();
                    double angle = Math.toRadians(-150 + hitPos * 120);

                    double speed = Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());
                    ball.setDx(speed * Math.sin(angle));
                    ball.setDy(speed * Math.cos(angle));

                    ball.getNode().setCenterY(paddleTop - r - 1);
                }
            }
        }
    }

    protected void resetBallAndPaddle() {
        Platform.runLater(() -> {
            double paddleX = (GAME_AREA_WIDTH - paddle.getNode().getWidth()) / 2;
            double paddleY = 650;
            paddle.getNode().setX(paddleX);
            paddle.getNode().setY(paddleY);

            ball.reset(paddleX, paddle.getNode().getWidth(), paddleY);
        });
    }

    protected boolean intersects(Node a, Node b) {
        return a.getBoundsInParent().intersects(b.getBoundsInParent());
    }

    protected void loseLife() {
        if (gameOver) {
            return;
        }

        lives--;

        if (livesCallback != null) {
            livesCallback.accept(lives);
        }

        if (lives <= 0) {
            gameOver = true;
            int finalScore = score;
            int finalLevel = level;

            Platform.runLater(() -> {
                org.example.ui.GameOverDialog.show(finalScore, finalLevel);

                lives = 3;
                score = 0;
                gameOver = false;
                updateHUD();
                loadLevel(1);
            });
        } else {
            onLifeLost();
            resetBallAndPaddle();
        }
    }

    protected void updateHUD() {
        if (scoreCallback != null) scoreCallback.accept(score);
        if (livesCallback != null) livesCallback.accept(lives);
        if (levelCallback != null) levelCallback.accept(level);
    }
    
    protected void showWinScreen() {
        if (winCallback != null) {
            Platform.runLater(winCallback);
        }
    }

    // ========== AUTO COMPLETE FOR TESTING ==========
    
    /**
     * 🎯 Auto-complete level - phá hết bricks để test nhanh
     * Dùng cho skip hotkey (N key)
     */
    public void autoCompleteLevel() {
        System.out.println("⏭ Auto-completing level...");
        
        // Phá hết bricks không phá được (tự động skip logic spawn)
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && !brick.isIndestructible()) {
                brick.onHit();
            }
        }
        
        levelDone = true;
        levelTime = System.currentTimeMillis();
        score += getLevelCompleteBonus();
        scoreCallback.accept(score);
        GameState.INSTANCE.addCoins(getLevelCompleteCoins());
        onLevelComplete(level);
    }

    public void resetGame() {
        lives = 3;
        score = 0;
        gameOver = false;
        updateHUD();
    }

    public int getCurrentLevel() {
        return level;
    }

    public void restoreGameState(int score, int lives) {
        this.score = score;
        this.lives = lives;
        updateHUD();
    }
    
    public void setScore(int newScore) {
        this.score = newScore;
        scoreCallback.accept(score);
    }
    
    /**
     * Set money (dùng cho tài xỉu, etc)
     */
    public void setMoney(int newMoney) {
        GameState.INSTANCE.setCoins(newMoney);
    }
    
    /**
     * Get current score
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Get current money
     */
    public int getMoney() {
        return GameState.INSTANCE.getCoins();
    }
}