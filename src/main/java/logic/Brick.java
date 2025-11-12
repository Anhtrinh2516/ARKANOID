package logic;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.Image;

import java.util.Random;

public class Brick {
    private final Rectangle node;
    protected int hitsRemaining;  // ← Changed to protected để subclass truy cập
    private final boolean indestructible;
    private final int scoreValue;
    private final int maxHits;  // Store original hits for image selection

    public Brick(Rectangle node, int hitsRemaining, boolean indestructible, int scoreValue) {
        this.node = node;
        this.hitsRemaining = hitsRemaining;
        this.indestructible = indestructible;
        this.scoreValue = scoreValue;
        this.maxHits = hitsRemaining;

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
            if (chance < 10) type = PowerUpType.COIN;
            else if (chance < 20) type = PowerUpType.EXTRA_LIFE;
            else type = PowerUpType.EXPAND_PADDLE;
            return new PowerUp(type, x, y);
        }
        return null;
    }

    public Rectangle getNode() { return node; }
    public boolean isIndestructible() { return indestructible; }
    public boolean isDestroyed() { return hitsRemaining <= 0 && !indestructible; }
    public int getScoreValue() { return scoreValue; }
    public int getHitsRemaining() { return hitsRemaining; } // ← Added getter

    public boolean onHit() {
        if (indestructible) return false;
        hitsRemaining--;
        refreshColor();
        if (hitsRemaining <= 0) {
            node.setVisible(false);
            return true;
        }
        return false;
    }

    protected void refreshColor() {
        // Hiệu ứng phát sáng
        DropShadow glow = new DropShadow();
        glow.setRadius(10);
        glow.setSpread(0.6);

        if (indestructible) {
            // Gạch không thể phá - sử dụng brick_unbreakable_1.png
            try {
                Image img = new Image(getClass().getResourceAsStream("/images/bricks/brick_unbreakable_1.png"));
                node.setFill(new ImagePattern(img));
                node.setStroke(Color.rgb(150, 0, 0));
                node.setStrokeWidth(2);
                glow.setColor(Color.rgb(150, 0, 0, 0.5));
                node.setEffect(glow);
            } catch (Exception e) {
                // Fallback to gradient if image not found
                LinearGradient gradient = new LinearGradient(
                        0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.rgb(50, 50, 50)),
                        new Stop(0.5, Color.rgb(30, 30, 30)),
                        new Stop(1, Color.rgb(40, 40, 40))
                );
                node.setFill(gradient);
                node.setStroke(Color.rgb(150, 0, 0));
                node.setStrokeWidth(2);
                glow.setColor(Color.rgb(150, 0, 0, 0.5));
                node.setEffect(glow);
            }
        } else {
            // Sử dụng hình ảnh dựa trên maxHits (số lần đập ban đầu)
            String imagePath = getBrickImagePath();
            try {
                Image img = new Image(getClass().getResourceAsStream(imagePath));
                node.setFill(new ImagePattern(img));
                
                // Set glow color based on hits remaining
                Color glowColor = switch (hitsRemaining) {
                    case 3 -> Color.rgb(255, 0, 0, 0.7);      // Red
                    case 2 -> Color.rgb(255, 150, 0, 0.7);    // Orange
                    default -> Color.rgb(0, 200, 255, 0.7);   // Cyan
                };
                
                node.setStroke(glowColor.brighter());
                node.setStrokeWidth(2);
                glow.setColor(glowColor);
                node.setEffect(glow);
            } catch (Exception e) {
                // Fallback to gradient if image not found
                switch (hitsRemaining) {
                    case 3 -> {
                        LinearGradient gradient = new LinearGradient(
                                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                                new Stop(0, Color.rgb(255, 50, 50)),
                                new Stop(0.5, Color.rgb(200, 0, 0)),
                                new Stop(1, Color.rgb(150, 0, 0))
                        );
                        node.setFill(gradient);
                        node.setStroke(Color.rgb(255, 100, 100));
                        node.setStrokeWidth(2);
                        glow.setColor(Color.rgb(255, 0, 0, 0.7));
                        node.setEffect(glow);
                    }
                    case 2 -> {
                        LinearGradient gradient = new LinearGradient(
                                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                                new Stop(0, Color.rgb(255, 200, 50)),
                                new Stop(0.5, Color.rgb(255, 150, 0)),
                                new Stop(1, Color.rgb(200, 100, 0))
                        );
                        node.setFill(gradient);
                        node.setStroke(Color.rgb(255, 220, 100));
                        node.setStrokeWidth(2);
                        glow.setColor(Color.rgb(255, 150, 0, 0.7));
                        node.setEffect(glow);
                    }
                    case 1 -> {
                        LinearGradient gradient = new LinearGradient(
                                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                                new Stop(0, Color.rgb(50, 255, 150)),
                                new Stop(0.5, Color.rgb(0, 200, 255)),
                                new Stop(1, Color.rgb(0, 150, 200))
                        );
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
    
    private String getBrickImagePath() {
        return switch (maxHits) {
            case 1 -> "/images/bricks/brick_normal_1.png";
            case 2 -> "/images/bricks/brick_strong_2.png";
            case 3 -> "/images/bricks/brick_strong_3.png";
            default -> "/images/bricks/brick_strong_3.png"; 
        };
    }
}
