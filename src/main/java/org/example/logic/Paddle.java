package org.example.logic;

import javafx.scene.shape.Rectangle;

public class Paddle {
    private final Rectangle node;
    private double speed = 6.0;

    public Paddle(Rectangle node) {
        this.node = node;
    }

    public Rectangle getNode() { return node; }
    public double left() { return node.getX(); }
    public double right() { return node.getX() + node.getWidth(); }

    public void moveLeft(double minX) {
        node.setX(Math.max(minX, node.getX() - speed));
    }
    public void moveRight(double maxX) {
        node.setX(Math.min(maxX - node.getWidth(), node.getX() + speed));
    }

    public void expand() { node.setWidth(Math.min(node.getWidth() + 40, 220)); }
    public void shrink() { node.setWidth(Math.max(node.getWidth() - 40, 70)); }
    public void setSpeed(double v) { this.speed = v; }
}
