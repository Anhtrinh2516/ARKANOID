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

    private static final double HUD_HEIGHT = 20.0;
    private static final int LEVEL_COMPLETE_DELAY = 1000;
    private static final double GAME_AREA_WIDTH = 888.0; 
    private static final double GAME_AREA_HEIGHT = 708.0; 

    private final AnchorPane pane;
    private final Paddle paddle;
    private final Ball ball;
    private final List<Ball> extraBalls = new ArrayList<>();
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

        for (Brick b : bricks) pane.getChildren().remove(b.getNode());
        bricks.clear();
        for (PowerUp p : powerUps) pane.getChildren().remove(p);
        powerUps.clear();

        List<Brick> created = LevelLoader.loadLevelFromFile(level, GAME_AREA_WIDTH);
        bricks.addAll(created);
        for (Brick b : bricks) pane.getChildren().add(b.getNode());

        resetBallAndPaddle();
    }

    public void movePaddleLeft()  {
        paddle.moveLeft(0);
        // Cập nhật vị trí ball nếu đang attached
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
        // Cập nhật vị trí ball nếu đang attached
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
            ball.adjustLaunchAngle(-5); // Xoay 5° sang trái
        }
    }

    public void adjustAimRight() {
        if (ball.isAttached()) {
            ball.adjustLaunchAngle(5); // Xoay 5° sang phải
        }
    }

    public void launchBall() {
        ball.launch();
    }

    public Ball getBall() {
        return ball;
    }

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

    if (ball.isAttached()) {
        ball.updateAttachment(
                paddle.getNode().getX(),
                paddle.getNode().getWidth(),
                paddle.getNode().getY()
        );
        return;
    }

    // Di chuyển tất cả balls
    updateBall(ball);
    for (Ball extra : extraBalls) {
        updateBall(extra);
    }

    // Check brick collision NGAY SAU KHI di chuyển
    checkBrickHit(ball);
    for (Ball extra : extraBalls) {
        checkBrickHit(extra);
    }

    // Check ball rơi xuống
    double H = GAME_AREA_HEIGHT;
    double r = ball.getR();

    if (ball.getY() + r >= H) {
        if (extraBalls.isEmpty()) {
            loseLife();
            return;
        } else {
            Ball first = extraBalls.get(0);
            ball.getNode().setCenterX(first.getX());
            ball.getNode().setCenterY(first.getY());
            ball.setVelocity(first.getDx(), first.getDy());
            pane.getChildren().remove(first.getNode());
            extraBalls.remove(0);
        }
    }

    Iterator<Ball> it = extraBalls.iterator();
    while (it.hasNext()) {
        Ball extra = it.next();
        if (extra.getY() + extra.getR() >= H) {
            pane.getChildren().remove(extra.getNode());
            it.remove();
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

    private void updateBall(Ball b) {
    b.move();

    double r = b.getR();
    double W = GAME_AREA_WIDTH;
    double HUD = 20.0;

    // Tường trái
    if (b.getX() - r <= 0) {
        b.bounceX();
        b.getNode().setCenterX(r + 1);
    }

    // Tường phải
    if (b.getX() + r >= W) {
        b.bounceX();
        b.getNode().setCenterX(W - r - 1);
    }

    // Tường trên
    if (b.getY() - r <= HUD) {
        b.bounceY();
        b.getNode().setCenterY(HUD + r + 1);
    }

    // Check paddle collision ở đây để đảm bảo không bị miss
    checkPaddleHit(b);
    }

    private void checkPaddleHit(Ball b) {
    double x = b.getX();
    double y = b.getY();
    double r = b.getR();
    double bottom = y + r;

    double pL = paddle.getNode().getX();
    double pR = pL + paddle.getNode().getWidth();
    double pT = paddle.getNode().getY();
    double pB = pT + paddle.getNode().getHeight();

    if (b.getDy() > 0) {
        if (bottom >= pT && bottom <= pB + 5) {
            if (x >= pL && x <= pR) {
                // QUAN TRỌNG: Giữ nguyên tốc độ hiện tại
                double speed = Math.sqrt(b.getDx() * b.getDx() + b.getDy() * b.getDy());
                
                // Tính góc dựa trên vị trí chạm paddle
                double hitPos = (x - pL) / paddle.getNode().getWidth();
                double angle = Math.toRadians(-150 + hitPos * 120);
                
                // Set lại vận tốc với cùng tốc độ
                b.setDx(speed * Math.sin(angle));
                b.setDy(speed * Math.cos(angle));
                
                // Đẩy ball ra khỏi paddle
                b.getNode().setCenterY(pT - r - 1);
                }
            }
        }
    }

    private void resetBallAndPaddle() {
        Platform.runLater(() -> {
            for (Ball extra : extraBalls) {
                pane.getChildren().remove(extra.getNode());
            }
            extraBalls.clear();

            double paddleX = (GAME_AREA_WIDTH - paddle.getNode().getWidth()) / 2;
            double paddleY = 650;
            paddle.getNode().setX(paddleX);
            paddle.getNode().setY(paddleY);

            ball.reset(
                    paddleX,
                    paddle.getNode().getWidth(),
                    paddleY
            );
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

    private void checkBrickHit(Ball b) {
        double bx = b.getX();
        double by = b.getY();
        double br = b.getR();
        double dx = b.getDx();
        double dy = b.getDy();

        Brick hitBrick = null;
        double minDist = 9999;
        int hitSide = -1;

        for (Brick brick : bricks) {
            if (brick.isDestroyed()) continue;

            Node bn = brick.getNode();
            double x1 = bn.getBoundsInParent().getMinX();
            double x2 = bn.getBoundsInParent().getMaxX();
            double y1 = bn.getBoundsInParent().getMinY();
            double y2 = bn.getBoundsInParent().getMaxY();

            double nearX = bx;
            if (bx < x1) nearX = x1;
            if (bx > x2) nearX = x2;
            
            double nearY = by;
            if (by < y1) nearY = y1;
            if (by > y2) nearY = y2;

            double distX = bx - nearX;
            double distY = by - nearY;
            double dist = Math.sqrt(distX * distX + distY * distY);

            if (dist > br) continue;

            if (dist < minDist) {
                minDist = dist;
                hitBrick = brick;

                boolean atCorner = (nearX == x1 || nearX == x2) && 
                                   (nearY == y1 || nearY == y2);

                if (atCorner) {
                    if (Math.abs(dx) > Math.abs(dy)) {
                        hitSide = (bx < (x1 + x2) / 2) ? 2 : 3;
                    } else {
                        hitSide = (by < (y1 + y2) / 2) ? 0 : 1;
                    }
                } else {
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

        if (hitBrick != null) {
            if (hitSide == 0 || hitSide == 1) {
                b.bounceY();
            } else {
                b.bounceX();
            }

            Node bn = hitBrick.getNode();
            double x1 = bn.getBoundsInParent().getMinX();
            double x2 = bn.getBoundsInParent().getMaxX();
            double y1 = bn.getBoundsInParent().getMinY();
            double y2 = bn.getBoundsInParent().getMaxY();

            if (hitSide == 0) {
                b.getNode().setCenterY(y1 - br - 1);
            } else if (hitSide == 1) {
                b.getNode().setCenterY(y2 + br + 1);
            } else if (hitSide == 2) {
                b.getNode().setCenterX(x1 - br - 1);
            } else {
                b.getNode().setCenterX(x2 + br + 1);
            }

            boolean destroyed = hitBrick.onHit();
            if (destroyed) {
                score += hitBrick.getScoreValue();
                scoreCb.accept(score);
                maybeSpawnPowerUp(hitBrick);
            }
        }
    }

    private void loseLife() {
        if (isGameOver) {
            return;
        }

        lives--;

        if (livesCb != null) {
            livesCb.accept(lives);
        }

        if (lives <= 0) {
            isGameOver = true;
            int finalScore = score;
            int finalLevel = level;

            Platform.runLater(() -> {
                boolean isTop = controller.LeaderboardManager.INSTANCE.isTop(finalScore);
                if (isTop) {
                    ui.CongratsAnimation.playAll(pane);
                }
                
                ui.GameOverDialog.show(finalScore, finalLevel);

                lives = 3;
                score = 0;
                isGameOver = false;
                updateHUD();
                loadLevel(1);
            });
        } else {
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
            } else if (chance < 22) {
                type = PowerUpType.EXPAND_PADDLE;
            } else if (chance < 29) {
                type = PowerUpType.SLOW_BALL;
            } else {
                type = PowerUpType.MULTIBALL;
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
                if (!ball.isAttached()) {
                    boolean hasSlow = false;
                    for (ActivePowerUp ap : activePowerUps) {
                        if (ap.getType() == PowerUpType.SLOW_BALL) {
                            hasSlow = true;
                            break;
                        }
                    }
                    
                    if (!hasSlow) {
                        ball.setDx(ball.getDx() * 0.7);
                        ball.setDy(ball.getDy() * 0.7);
                        activePowerUps.add(new ActivePowerUp(PowerUpType.SLOW_BALL, 10000));
                        updatePowerUpUI();
                    }
                }
            }
            case MULTIBALL -> {
                spawnExtraBalls();
            }
        }
    }

    private void spawnExtraBalls() {
        if (ball.isAttached()) {
            return;
        }

        double x = ball.getX();
        double y = ball.getY();
        double dx = ball.getDx();
        double dy = ball.getDy();
        double r = ball.getR();

        Circle mainNode = ball.getNode();

        for (int i = 0; i < 2; i++) {
            Circle node = new Circle(x, y, r);
            node.setFill(mainNode.getFill());
            node.setStroke(mainNode.getStroke());
            node.setStrokeWidth(mainNode.getStrokeWidth());

            Ball extra = new Ball(node);
            extra.launch();

            double angle = Math.toDegrees(Math.atan2(-dy, dx));
            double offset = (i == 0) ? -30 : 30;
            double newAngle = angle + offset;
            double rad = Math.toRadians(newAngle);

            double spd = Math.sqrt(dx * dx + dy * dy);
            double newDx = spd * Math.cos(rad);
            double newDy = -spd * Math.sin(rad);

            extra.setVelocity(newDx, newDy);

            pane.getChildren().add(node);
            extraBalls.add(extra);
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
                            ball.setDx(ball.getDx() / 0.7);
                            ball.setDy(ball.getDy() / 0.7);
                        }
                    }
                    case EXTRA_LIFE, COIN, MULTIBALL -> {
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