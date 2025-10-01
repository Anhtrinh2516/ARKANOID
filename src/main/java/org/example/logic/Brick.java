package org.example.logic;

import javafx.scene.paint.Color;
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
        refreshColor();
    }

    public PowerUp dropPowerUp(double x, double y) {
        Random rand = new Random();
        int chance = rand.nextInt(100); // 0-99
        if (chance < 30) { // 30% rơi đồ
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

    private void refreshColor() {
        if (indestructible) {
            node.setFill(Color.DARKGRAY);
            node.setStroke(Color.BLACK);
        } else {
            switch (hitsRemaining) {
                case 3 -> {
                    node.setFill(Color.ORANGERED);
                    node.setStroke(Color.BLACK);
                    node.setStrokeWidth(1.5);
                }
                case 2 -> {
                    node.setFill(Color.GOLD);
                    node.setStroke(Color.BROWN);
                    node.setStrokeWidth(2);
                }
                case 1 -> {
                    node.setFill(Color.LIGHTGREEN);
                    node.setStroke(Color.DARKGREEN);
                    node.setStrokeWidth(3);
                }
                default -> node.setVisible(false);
            }
        }
    }

}
