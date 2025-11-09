package logic;

import javafx.scene.shape.Rectangle;

public class Paddle {
    private final Rectangle node;
    private double speed = 10.0;
    private static final double GAME_AREA_WIDTH = 920.0;
    private static final double GAME_AREA_MIN_X = 0.0;

    public Paddle(Rectangle node) {
        this.node = node;
        // this.originalWidth = node.getWidth();
    }

    public Rectangle getNode() {
        return node;
    }

    public double left() {
        return node.getX();
    }

    public double right() {
        return node.getX() + node.getWidth();
    }

    public void moveLeft(double minX) {
        double newX = node.getX() - speed;
        if (newX < GAME_AREA_MIN_X) {
            newX = GAME_AREA_MIN_X;
        }
        node.setX(newX);
    }

    public void moveRight(double maxX) {
        double newX = node.getX() + speed;

        if (newX + node.getWidth() > GAME_AREA_WIDTH) {
            newX = GAME_AREA_WIDTH - node.getWidth();
        }
        node.setX(newX);
    }

    public void expand() {
        node.setWidth(Math.min(node.getWidth() + 40, 220));
    }

    public void shrink() {
        node.setWidth(Math.max(node.getWidth() - 40, 70));
    }

    public void setSpeed(double v) {
        this.speed = v;
    }
}