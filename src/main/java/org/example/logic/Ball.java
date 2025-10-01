package org.example.logic;

import javafx.scene.shape.Circle;

public class Ball {
    private final Circle node;
    private double dx;
    private double dy;
    private final double baseSpeed;

    public Ball(Circle node) {
        this.node = node;
        this.baseSpeed = 3.0;
        this.dx = baseSpeed;
        this.dy = -baseSpeed;
    }


    public Circle getNode() { return node; }
    public double getX() { return node.getCenterX(); }
    public double getY() { return node.getCenterY(); }
    public double getR() { return node.getRadius(); }

    // ===== Getter/Setter vận tốc =====
    public void setVelocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public double getDx() { return dx; }
    public double getDy() { return dy; }

    public void setDx(double dx) { this.dx = dx; }
    public void setDy(double dy) { this.dy = dy; }


    public void move() {
        node.setCenterX(node.getCenterX() + dx);
        node.setCenterY(node.getCenterY() + dy);
    }

    public void bounceX() { dx = -dx; }
    public void bounceY() { dy = -dy; }


    public void reset(double x, double y) {
        node.setCenterX(x);
        node.setCenterY(y);


        double dir = Math.random() < 0.5 ? -1 : 1;
        setVelocity(baseSpeed * dir, -baseSpeed);
    }

    public void clampPosition(double minX, double maxX, double minY, double maxY) {
        if (node.getCenterX() - getR() < minX) node.setCenterX(minX + getR());
        if (node.getCenterX() + getR() > maxX) node.setCenterX(maxX - getR());
        if (node.getCenterY() - getR() < minY) node.setCenterY(minY + getR());
        if (node.getCenterY() + getR() > maxY) node.setCenterY(maxY - getR());
    }


    public void speedUp(double factor) {
        dx *= factor;
        dy *= factor;

        double max = 12.0;
        double min = 1.5;

        dx = clamp(dx, -max, max, min);
        dy = clamp(dy, -max, max, min);
    }


    private double clamp(double v, double minVal, double maxVal, double minAbs) {
        if (v > maxVal) v = maxVal;
        if (v < minVal) v = minVal;
        if (Math.abs(v) < minAbs) v = (v < 0 ? -minAbs : minAbs);
        return v;
    }
}
