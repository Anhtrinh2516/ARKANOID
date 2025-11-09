package logic;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import controller.GameState;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

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
    private boolean isGameOver = false;

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

        // Khởi tạo mũi tên cho ball
        ball.initializeArrow(pane);

        updateHUD();
    }

    public void setPowerUpUpdateCallback(Consumer<String> callback) {
        this.powerUpUpdateCb = callback;
    }

    public void loadLevel(int idx) {
        this.level = idx;
        levelCb.accept(level);
        levelCompleting = false;
        isGameOver = false;

        for (Brick b : bricks)
            pane.getChildren().remove(b.getNode());
        bricks.clear();
        for (PowerUp p : powerUps)
            pane.getChildren().remove(p);
        powerUps.clear();

        List<Brick> created = LevelLoader.createLevel(level, pane.getWidth());
        bricks.addAll(created);
        for (Brick b : bricks)
            pane.getChildren().add(b.getNode());

        resetBallAndPaddle();
    }

    /**
     * Di chuyển paddle sang trái - ball attached sẽ theo
     */
    public void movePaddleLeft() {
        paddle.moveLeft(0);
        // Cập nhật vị trí ball nếu đang attached
        if (ball.isAttached()) {
            ball.updateAttachment(
                    paddle.getNode().getX(),
                    paddle.getNode().getWidth(),
                    paddle.getNode().getY());
        }
    }

    /**
     * Di chuyển paddle sang phải - ball attached sẽ theo
     */
    public void movePaddleRight() {
        paddle.moveRight(GAME_AREA_WIDTH);
        // Cập nhật vị trí ball nếu đang attached
        if (ball.isAttached()) {
            ball.updateAttachment(
                    paddle.getNode().getX(),
                    paddle.getNode().getWidth(),
                    paddle.getNode().getY());
        }
    }

    /**
     * Điều chỉnh góc phóng bóng khi đang attach
     */
    public void adjustAimLeft() {
        if (ball.isAttached()) {
            ball.adjustLaunchAngle(-5); // Xoay 5° sang trái
        }
    }

    public void adjustAimRight() {
        if (ball.isAttached()) {
            ball.adjustLaunchAngle(5); // Xoay 5° sang phải
        }
    }

    /**
     * Phóng ball (gọi khi nhấn SPACE)
     */
    public void launchBall() {
        ball.launch();
    }

    public Ball getBall() {
        return ball;
    }

    /**
     * Update game logic mỗi frame
     */
    public void update() {
        if (isGameOver) {
            return;
        }

        if (levelCompleting) {
            if (System.currentTimeMillis() - levelCompleteTime >= LEVEL_COMPLETE_DELAY) {
                loadLevel(level + 1);
            }
            return;
        }

        // Nếu ball đang attached, không xử lý va chạm
        if (ball.isAttached()) {
            ball.updateAttachment(
                    paddle.getNode().getX(),
                    paddle.getNode().getWidth(),
                    paddle.getNode().getY());
            return; // Chờ người chơi nhấn SPACE
        }

        // Ball đang bay - xử lý di chuyển và va chạm
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

        // Va chạm paddle - ARKANOID STYLE
        double ballCenterX = ball.getX();
        double ballCenterY = ball.getY();
        double ballBottom = ballCenterY + r;

        double paddleLeft = paddle.getNode().getX();
        double paddleRight = paddleLeft + paddle.getNode().getWidth();
        double paddleTop = paddle.getNode().getY();
        double paddleBottom = paddleTop + paddle.getNode().getHeight();

        // Chỉ bounce khi ball đang rơi xuống (dy > 0) và chạm paddle
        if (ball.getDy() > 0) {
            if (ballBottom >= paddleTop && ballBottom <= paddleBottom + 5) {
                if (ballCenterX >= paddleLeft && ballCenterX <= paddleRight) {
                    // Tính góc bounce dựa trên vị trí va chạm (0.0 = trái, 1.0 = phải)
                    double hitPos = (ballCenterX - paddleLeft) / paddle.getNode().getWidth();

                    // Góc từ -150° (trái) đến -30° (phải)
                    // Arkanoid style: trái = góc âm lớn, phải = góc âm nhỏ
                    double angle = Math.toRadians(-150 + hitPos * 120); // -150° đến -30°

                    // Tính vận tốc mới giữ nguyên tốc độ
                    double speed = Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());
                    ball.setDx(speed * Math.sin(angle));
                    ball.setDy(speed * Math.cos(angle)); // dy luôn âm (đi lên)

                    // Đặt ball phía trên paddle để tránh stuck
                    ball.getNode().setCenterY(paddleTop - r - 1);

                    System.out.println("⚡ Paddle hit at " + String.format("%.2f", hitPos) +
                            " → angle " + String.format("%.1f", Math.toDegrees(angle)) + "°" +
                            " → dx=" + String.format("%.2f", ball.getDx()) +
                            ", dy=" + String.format("%.2f", ball.getDy()));
                }
            }
        }

        // Ball rơi xuống dưới - MẤT MẠNG
        if (ballBottom >= H) {
            loseLife();
            return;
        }

        // Va chạm brick
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

        // Kiểm tra hoàn thành level
        if (allBreakableDestroyed()) {
            levelCompleting = true;
            levelCompleteTime = System.currentTimeMillis();
            score += 500;
            scoreCb.accept(score);
            GameState.INSTANCE.addCoins(2);
        }
    }

    /**
     * Reset ball và paddle về vị trí ban đầu
     * Ball sẽ ở trạng thái ATTACHED
     */
    private void resetBallAndPaddle() {
        Platform.runLater(() -> {
            // Reset paddle position
            double paddleX = (GAME_AREA_WIDTH - paddle.getNode().getWidth()) / 2;
            double paddleY = 580;
            paddle.getNode().setX(paddleX);
            paddle.getNode().setY(paddleY);

            // Reset ball với 3 tham số (paddleX, paddleWidth, paddleY)
            ball.reset(
                    paddleX,
                    paddle.getNode().getWidth(),
                    paddleY);

            System.out.println("🎮 Game ready! Use Arrow Keys to aim, press SPACE to launch!");
        });
    }

    private boolean allBreakableDestroyed() {
        for (Brick b : bricks) {
            if (!b.isIndestructible() && !b.isDestroyed())
                return false;
        }
        return true;
    }

    private boolean intersects(Node a, Node b) {
        return a.getBoundsInParent().intersects(b.getBoundsInParent());
    }

    /**
     * Mất mạng - reset ball về paddle
     */
    private void loseLife() {
        if (isGameOver) {
            return;
        }

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
                ui.GameOverDialog.show(finalScore, finalLevel);

                lives = 3;
                score = 0;
                isGameOver = false;
                updateHUD();
                loadLevel(1);
            });
        } else {
            // Còn mạng - reset ball về paddle (attached mode)
            System.out.println("💔 Life lost! Lives remaining: " + lives);
            resetBallAndPaddle();
        }
    }

    private void updateHUD() {
        if (scoreCb != null)
            scoreCb.accept(score);
        if (livesCb != null)
            livesCb.accept(lives);
        if (levelCb != null)
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
                    br.getNode().getBoundsInParent().getCenterY());
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
                if (scoreCb != null)
                    scoreCb.accept(score);
                GameState.INSTANCE.addCoins(1);
            }
            case EXTRA_LIFE -> {
                lives++;
                if (livesCb != null)
                    livesCb.accept(lives);
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
                if (!ball.isAttached()) {
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
                        if (!ball.isAttached()) {
                            ball.setDx(originalBallDx);
                            ball.setDy(originalBallDy);
                        }
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

    public void restoreGameState(int score, int lives) {
        this.score = score;
        this.lives = lives;
        updateHUD();
        System.out.println("Game state restored: Score=" + score + ", Lives=" + lives);
    }
}