package logic;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Brick {
    private final Rectangle node;
    private int hitsRemaining;
    private final boolean indestructible;
    private final int scoreValue;

    public Brick(Rectangle node, int hitsRemaining, boolean indestructible, int scoreValue) {
        this.node = node;
        this.hitsRemaining = hitsRemaining;
        this.indestructible = indestructible;
        this.scoreValue = scoreValue;

        // Bo góc cho đẹp hơn
        node.setArcWidth(5);
        node.setArcHeight(5);

        refreshColor();
    }

    public PowerUp dropPowerUp(double x, double y) {
        Random rand = new Random();
        int chance = rand.nextInt(100);
        if (chance < 30) {
            PowerUpType type;
            if (chance < 10)
                type = PowerUpType.COIN;
            else if (chance < 20)
                type = PowerUpType.EXTRA_LIFE;
            else
                type = PowerUpType.EXPAND_PADDLE;
            return new PowerUp(type, x, y);
        }
        return null;
    }

    public Rectangle getNode() {
        return node;
    }

    public boolean isIndestructible() {
        return indestructible;
    }

    public boolean isDestroyed() {
        return hitsRemaining <= 0 && !indestructible;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public boolean onHit() {
        if (indestructible)
            return false;
        hitsRemaining--;
        refreshColor();
        if (hitsRemaining <= 0) {
            node.setVisible(false);
            return true;
        }
        return false;
    }

    private void refreshColor() {
        // Hiệu ứng phát sáng
        DropShadow glow = new DropShadow();
        glow.setRadius(10);
        glow.setSpread(0.6);

        if (indestructible) {
            // Gạch không thể phá - màu xám đen với viền đỏ
            LinearGradient gradient = new LinearGradient(
                    0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.rgb(50, 50, 50)),
                    new Stop(0.5, Color.rgb(30, 30, 30)),
                    new Stop(1, Color.rgb(40, 40, 40)));
            node.setFill(gradient);
            node.setStroke(Color.rgb(150, 0, 0));
            node.setStrokeWidth(2);
            glow.setColor(Color.rgb(150, 0, 0, 0.5));
            node.setEffect(glow);
        } else {
            switch (hitsRemaining) {
                case 3 -> {
                    // Đỏ - nguy hiểm nhất
                    LinearGradient gradient = new LinearGradient(
                            0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.rgb(255, 50, 50)),
                            new Stop(0.5, Color.rgb(200, 0, 0)),
                            new Stop(1, Color.rgb(150, 0, 0)));
                    node.setFill(gradient);
                    node.setStroke(Color.rgb(255, 100, 100));
                    node.setStrokeWidth(2);
                    glow.setColor(Color.rgb(255, 0, 0, 0.7));
                    node.setEffect(glow);
                }
                case 2 -> {
                    // Cam/vàng - trung bình
                    LinearGradient gradient = new LinearGradient(
                            0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.rgb(255, 200, 50)),
                            new Stop(0.5, Color.rgb(255, 150, 0)),
                            new Stop(1, Color.rgb(200, 100, 0)));
                    node.setFill(gradient);
                    node.setStroke(Color.rgb(255, 220, 100));
                    node.setStrokeWidth(2);
                    glow.setColor(Color.rgb(255, 150, 0, 0.7));
                    node.setEffect(glow);
                }
                case 1 -> {
                    // Xanh lá/xanh dương - dễ nhất
                    LinearGradient gradient = new LinearGradient(
                            0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.rgb(50, 255, 150)),
                            new Stop(0.5, Color.rgb(0, 200, 255)),
                            new Stop(1, Color.rgb(0, 150, 200)));
                    node.setFill(gradient);
                    node.setStroke(Color.rgb(100, 255, 200));
                    node.setStrokeWidth(2);
                    glow.setColor(Color.rgb(0, 200, 255, 0.7));
                    node.setEffect(glow);
                }
                default -> node.setVisible(false);
            }
        }
    }
}