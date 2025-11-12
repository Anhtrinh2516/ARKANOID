package event.base;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import controller.GameState;
import logic.Ball;
import logic.Brick;
import logic.Paddle;

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

    protected void onUpdate() {}
    protected void onLevelComplete(int levelIdx) {}
    protected void onLifeLost() {}

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
        if (ball.isAttached()) ball.adjustLaunchAngle(-5);
    }

    public void adjustAimRight() {
        if (ball.isAttached()) ball.adjustLaunchAngle(5);
    }

    public void launchBall() {
        ball.launch();
    }

    public Ball getBall() {
        return ball;
    }

    public void update() {
        if (gameOver) return;

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

        ball.move();

        double r = ball.getR();
        double W = GAME_AREA_WIDTH;
        double H = GAME_AREA_HEIGHT;
        double HUD_HEIGHT = 20.0;

        // tường trái phải
        if (ball.getX() - r <= 0) {
            ball.bounceX();
            ball.getNode().setCenterX(r + 1);
        }
        if (ball.getX() + r >= W) {
            ball.bounceX();
            ball.getNode().setCenterX(W - r - 1);
        }

        // tường trên
        if (ball.getY() - r <= HUD_HEIGHT) {
            ball.bounceY();
            ball.getNode().setCenterY(HUD_HEIGHT + r + 1);
        }

        handlePaddleCollision(r);

        // rơi xuống
        if (ball.getY() + r >= H) {
            loseLife();
            return;
        }

        // xử lý va chạm gạch
        checkBrickCollision();

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

    // fix bug bóng xuyên góc gạch
    private void checkBrickCollision() {
        double bx = ball.getX();
        double by = ball.getY();
        double br = ball.getR();
        double dx = ball.getDx();
        double dy = ball.getDy();

        Brick hitBrick = null;
        double minDist = 9999;
        int hitSide = -1; // 0=top, 1=bottom, 2=left, 3=right

        // tìm gạch gần nhất đang chạm
        for (Brick brick : bricks) {
            if (brick.isDestroyed()) continue;

            Node bn = brick.getNode();
            double x1 = bn.getBoundsInParent().getMinX();
            double x2 = bn.getBoundsInParent().getMaxX();
            double y1 = bn.getBoundsInParent().getMinY();
            double y2 = bn.getBoundsInParent().getMaxY();

            // check có chạm ko
            double nearX = bx;
            if (bx < x1) nearX = x1;
            if (bx > x2) nearX = x2;
            
            double nearY = by;
            if (by < y1) nearY = y1;
            if (by > y2) nearY = y2;

            double distX = bx - nearX;
            double distY = by - nearY;
            double dist = Math.sqrt(distX * distX + distY * distY);

            if (dist > br) continue; // ko chạm

            // chạm rồi, tính xem chạm cạnh nào
            if (dist < minDist) {
                minDist = dist;
                hitBrick = brick;

                // check góc
                boolean atCorner = (nearX == x1 || nearX == x2) && 
                                   (nearY == y1 || nearY == y2);

                if (atCorner) {
                    // chạm góc thì xét theo vận tốc
                    if (Math.abs(dx) > Math.abs(dy)) {
                        hitSide = (bx < (x1 + x2) / 2) ? 2 : 3; // left or right
                    } else {
                        hitSide = (by < (y1 + y2) / 2) ? 0 : 1; // top or bottom
                    }
                } else {
                    // chạm cạnh bình thường
                    double overTop = (by + br) - y1;
                    double overBot = y2 - (by - br);
                    double overLeft = (bx + br) - x1;
                    double overRight = x2 - (bx - br);

                    double minOver = Math.min(Math.min(overTop, overBot), 
                                             Math.min(overLeft, overRight));

                    if (minOver == overTop) hitSide = 0;
                    else if (minOver == overBot) hitSide = 1;
                    else if (minOver == overLeft) hitSide = 2;
                    else hitSide = 3;
                }
            }
        }

        // xử lý va chạm
        if (hitBrick != null) {
            // bounce
            if (hitSide == 0 || hitSide == 1) {
                ball.bounceY();
            } else {
                ball.bounceX();
            }

            // đẩy bóng ra ngoài tránh dính
            Node bn = hitBrick.getNode();
            double x1 = bn.getBoundsInParent().getMinX();
            double x2 = bn.getBoundsInParent().getMaxX();
            double y1 = bn.getBoundsInParent().getMinY();
            double y2 = bn.getBoundsInParent().getMaxY();

            if (hitSide == 0) {
                ball.getNode().setCenterY(y1 - br - 1);
            } else if (hitSide == 1) {
                ball.getNode().setCenterY(y2 + br + 1);
            } else if (hitSide == 2) {
                ball.getNode().setCenterX(x1 - br - 1);
            } else {
                ball.getNode().setCenterX(x2 + br + 1);
            }

            // phá gạch
            boolean destroyed = hitBrick.onHit();
            if (destroyed) {
                score += hitBrick.getScoreValue();
                scoreCallback.accept(score);
                onBrickDestroyed(hitBrick);
            }
        }
    }

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
        if (gameOver) return;

        lives--;
        if (livesCallback != null) livesCallback.accept(lives);

        if (lives <= 0) {
            gameOver = true;
            int finalScore = score;
            int finalLevel = level;

            Platform.runLater(() -> {
                ui.GameOverDialog.show(finalScore, finalLevel);
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

    public void autoCompleteLevel() {
        System.out.println("⏭ Auto-completing level...");
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

    public int getCurrentLevel() { return level; }
    
    public void restoreGameState(int score, int lives) {
        this.score = score;
        this.lives = lives;
        updateHUD();
    }
    
    public void setScore(int newScore) {
        this.score = newScore;
        scoreCallback.accept(score);
    }
    
    public void setMoney(int newMoney) {
        GameState.INSTANCE.setCoins(newMoney);
    }
    
    public int getScore() { return score; }
    public int getMoney() { return GameState.INSTANCE.getCoins(); }
}