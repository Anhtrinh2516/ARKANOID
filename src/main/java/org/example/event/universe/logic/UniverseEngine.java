package org.example.event.universe.logic;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.example.event.base.EventGameEngine;
import org.example.logic.Brick;

import java.util.*;
import java.util.function.IntConsumer;

public class UniverseEngine extends EventGameEngine {

    private final List<AlienBrick> alienBricks = new ArrayList<>();
    private final List<ShipBrick> shipBricks = new ArrayList<>();
    private final List<Laser> lasers = new ArrayList<>();
    private final Random random = new Random();
    
    private Runnable laserHitCallback;

    public UniverseEngine(AnchorPane pane, Rectangle paddleRect, Circle ballCircle,
                          IntConsumer scoreCallback, IntConsumer livesCallback, IntConsumer levelCallback) {
        super(pane, paddleRect, ballCircle, scoreCallback, livesCallback, levelCallback);
    }
    
    public void setOnPaddleHitCallback(Runnable callback) {
        this.laserHitCallback = callback;
    }

    @Override
    public void loadLevel(int levelIdx) {
        if (levelIdx == 1) {
            lives = 3;
            score = 0;
            if (livesCallback != null) livesCallback.accept(lives);
            if (scoreCallback != null) scoreCallback.accept(score);
        }
        super.loadLevel(levelIdx);
    }

    @Override
    protected List<Brick> loadLevelBricks(int levelIdx) {
        List<Brick> bricks = UniverseLevelLoader.loadUniverseLevel(levelIdx, GAME_AREA_WIDTH);
        
        alienBricks.clear();
        shipBricks.clear();
        for (Brick brick : bricks) {
            if (brick instanceof AlienBrick) {
                alienBricks.add((AlienBrick) brick);
            } else if (brick instanceof ShipBrick) {
                ShipBrick ship = (ShipBrick) brick;
                ship.setBounds(0, GAME_AREA_WIDTH);
                shipBricks.add(ship);
            }
        }
        
        return bricks;
    }

    @Override
    protected void onBrickDestroyed(Brick brick) {
    }

    @Override
    protected boolean checkWinCondition() {
        for (Brick brick : bricks) {
            if (brick instanceof AlienBrick && !brick.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected int getLevelCompleteBonus() {
        return 1500;
    }

    @Override
    protected int getLevelCompleteCoins() {
        return 15;
    }
    
    @Override
    protected boolean isFinalLevel(int levelIdx) {
        return levelIdx >= 1;
    }

    @Override
    protected void onUpdate() {
        updateAliens();
        updateShips();
        updateLasers();
    }

    @Override
    protected void onLevelComplete(int levelIdx) {
        if (levelIdx == 1) {
            org.example.controller.SkinManager.INSTANCE.unlockPaddleSkin(
                org.example.controller.SkinManager.PaddleSkin.UNIVERSE
            );
            org.example.controller.SkinManager.INSTANCE.unlockBallSkin(
                org.example.controller.SkinManager.BallSkin.UNIVERSE
            );
        }
    }

    @Override
    protected void onLifeLost() {
        javafx.application.Platform.runLater(() -> pane.getChildren().removeAll(lasers));
        lasers.clear();
    }

    private void updateAliens() {
        for (AlienBrick alien : alienBricks) {
            if (alien.isDestroyed()) continue;
            if (alien.canShoot() && random.nextDouble() < 0.2) {
                shootLaser(alien);
            }
        }
    }
    
    private void updateShips() {
        for (ShipBrick ship : shipBricks) {
            if (ship.isDestroyed()) continue;
            
            double oldX = ship.getNode().getX();
            ship.move();
            
            if (checkShipCollision(ship)) {
                ship.getNode().setX(oldX);
                ship.reverseDirection();
            }
        }
    }
    
    private boolean checkShipCollision(ShipBrick ship) {
        Rectangle rect = ship.getNode();
        double left = rect.getX();
        double right = rect.getX() + rect.getWidth();
        double top = rect.getY();
        double bottom = rect.getY() + rect.getHeight();
        
        return bricks.stream()
            .filter(brick -> brick != ship && !brick.isDestroyed())
            .anyMatch(brick -> {
                Rectangle other = brick.getNode();
                double otherLeft = other.getX();
                double otherRight = other.getX() + other.getWidth();
                double otherTop = other.getY();
                double otherBottom = other.getY() + other.getHeight();
                
                boolean horizontalOverlap = right > otherLeft && left < otherRight;
                boolean verticalNear = Math.abs(top - otherTop) < 5 || 
                                      Math.abs(bottom - otherBottom) < 5 ||
                                      (top >= otherTop && bottom <= otherBottom) ||
                                      (otherTop >= top && otherBottom <= bottom);
                
                return horizontalOverlap && verticalNear;
            });
    }

    private void shootLaser(AlienBrick alien) {
        Laser laser = new Laser(alien.getCenterX(), alien.getCenterY());
        lasers.add(laser);
        javafx.application.Platform.runLater(() -> pane.getChildren().add(laser));
    }

    private void updateLasers() {
        if (lasers.isEmpty()) return;
        
        List<Laser> toRemove = new ArrayList<>();
        
        for (Laser laser : lasers) {
            if (!laser.isActive() || laser.isOutOfBounds(GAME_AREA_HEIGHT)) {
                toRemove.add(laser);
                continue;
            }
            
            laser.update();
            
            if (checkLaserHit(laser)) {
                if (laserHitCallback != null) laserHitCallback.run();
                laser.deactivate();
                toRemove.add(laser);
            }
        }
        
        if (!toRemove.isEmpty()) {
            javafx.application.Platform.runLater(() -> pane.getChildren().removeAll(toRemove));
            lasers.removeAll(toRemove);
        }
    }

    private boolean checkLaserHit(Laser laser) {
        Rectangle paddleRect = paddle.getNode();
        return laser.getBoundsInParent().intersects(paddleRect.getBoundsInParent());
    }
}
