package org.example.event.castleattack.logic;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.example.event.base.EventGameEngine;
import org.example.logic.Brick;

import java.util.*;
import java.util.function.IntConsumer;

public class CastleAttackEngine extends EventGameEngine {

    private List<CanonBrick> canonBricks = new ArrayList<>();
    private List<GateBrick> gates = new ArrayList<>();
    private List<Canon> canons = new ArrayList<>();
    private List<Soldier> soldiers = new ArrayList<>();
    private Random random = new Random();
    
    private Runnable canonHitCallback;
    private Runnable soldierHitCallback;

    public CastleAttackEngine(AnchorPane pane, Rectangle paddleNode, Circle ballNode,
                              IntConsumer scoreCallback, IntConsumer livesCallback, IntConsumer levelCallback) {
        super(pane, paddleNode, ballNode, scoreCallback, livesCallback, levelCallback);
    }
    
    public void setOnPaddleHitByCanonCallback(Runnable callback) {
        this.canonHitCallback = callback;
    }
    
    public void setOnPaddleHitBySoldierCallback(Runnable callback) {
        this.soldierHitCallback = callback;
    }

    @Override
    public void loadLevel(int levelIndex) {
        javafx.application.Platform.runLater(() -> {
            List<javafx.scene.Node> toRemove = new ArrayList<>();
            for (javafx.scene.Node node : pane.getChildren()) {
                if (node instanceof Canon || node instanceof Soldier) toRemove.add(node);
            }
            pane.getChildren().removeAll(toRemove);
        });
        
        canons.clear();
        soldiers.clear();
        
        if (levelIndex == 1 || lives <= 0) {
            lives = 3;
            score = 0;
            livesCallback.accept(lives);
            scoreCallback.accept(score);
        }
        super.loadLevel(levelIndex);
    }

    @Override
    protected List<Brick> loadLevelBricks(int levelIndex) {
        List<Brick> bricks = CastleAttackLevelLoader.loadCastleLevel(levelIndex, GAME_AREA_WIDTH);
        canonBricks.clear();
        gates.clear();
        for (Brick brick : bricks) {
            if (brick instanceof CanonBrick) canonBricks.add((CanonBrick) brick);
            else if (brick instanceof GateBrick) gates.add((GateBrick) brick);
        }
        return bricks;
    }

    @Override
    protected void onBrickDestroyed(Brick brick) {
    }

    @Override
    protected boolean checkWinCondition() {
        return bricks.stream().allMatch(Brick::isDestroyed);
    }

    @Override
    protected int getLevelCompleteBonus() {
        return 2000;
    }

    @Override
    protected int getLevelCompleteCoins() {
        return 20;
    }
    
    @Override
    protected boolean isFinalLevel(int levelIndex) {
        return levelIndex >= 1;
    }

    @Override
    protected void onUpdate() {
        updateCanonBricks();
        updateGates();
        updateCanons();
        updateSoldiers();
    }

    @Override
    protected void onLevelComplete(int completedLevel) {
        if (completedLevel == 1) {
            org.example.controller.SkinManager.INSTANCE.unlockPaddleSkin(
                org.example.controller.SkinManager.PaddleSkin.CASTLE
            );
            org.example.controller.SkinManager.INSTANCE.unlockBallSkin(
                org.example.controller.SkinManager.BallSkin.CASTLE
            );
        }
    }

    @Override
    protected void onLifeLost() {
        javafx.application.Platform.runLater(() -> {
            List<javafx.scene.Node> toRemove = new ArrayList<>();
            for (javafx.scene.Node node : pane.getChildren()) {
                if (node instanceof Canon || node instanceof Soldier) toRemove.add(node);
            }
            pane.getChildren().removeAll(toRemove);
        });
        canons.clear();
        soldiers.clear();
    }

    private void updateCanonBricks() {
        for (CanonBrick canonBrick : canonBricks) {
            if (canonBrick.isDestroyed()) continue;
            if (canonBrick.canShoot() && random.nextDouble() < 0.25) shootCanon(canonBrick);
        }
    }
    
    private void updateGates() {
        for (GateBrick gate : gates) {
            if (gate.isDestroyed()) continue;
            if (gate.canSpawnSoldier() && random.nextDouble() < 0.3) spawnSoldier(gate);
        }
    }

    private void shootCanon(CanonBrick canonBrick) {
        Canon canon = new Canon(canonBrick.getCenterX(), canonBrick.getCenterY());
        canons.add(canon);
        javafx.application.Platform.runLater(() -> pane.getChildren().add(canon));
    }
    
    private void spawnSoldier(GateBrick gate) {
        Soldier soldier = new Soldier(gate.getCenterX() - 17.5, gate.getCenterY());
        soldiers.add(soldier);
        javafx.application.Platform.runLater(() -> pane.getChildren().add(soldier));
    }

    private void updateCanons() {
        if (canons.isEmpty()) return;
        List<Canon> toRemove = new ArrayList<>();
        
        for (Canon canon : canons) {
            if (!canon.isActive() || canon.isOutOfBounds(GAME_AREA_HEIGHT)) {
                toRemove.add(canon);
                continue;
            }
            canon.update();
            if (checkCanonHit(canon)) {
                if (canonHitCallback != null) canonHitCallback.run();
                canon.deactivate();
                toRemove.add(canon);
            }
        }
        
        if (!toRemove.isEmpty()) {
            javafx.application.Platform.runLater(() -> pane.getChildren().removeAll(toRemove));
            canons.removeAll(toRemove);
        }
    }
    
    private void updateSoldiers() {
        if (soldiers.isEmpty()) return;
        List<Soldier> toRemove = new ArrayList<>();
        
        for (Soldier soldier : soldiers) {
            if (!soldier.isActive() || soldier.isOutOfBounds(GAME_AREA_HEIGHT)) {
                toRemove.add(soldier);
                continue;
            }
            soldier.update();
            if (checkSoldierHit(soldier)) {
                if (soldierHitCallback != null) soldierHitCallback.run();
                soldier.deactivate();
                toRemove.add(soldier);
            }
        }
        
        if (!toRemove.isEmpty()) {
            javafx.application.Platform.runLater(() -> pane.getChildren().removeAll(toRemove));
            soldiers.removeAll(toRemove);
        }
    }

    private boolean checkCanonHit(Canon canon) {
        Rectangle paddleRect = paddle.getNode();
        return canon.getBoundsInParent().intersects(paddleRect.getBoundsInParent());
    }
    
    private boolean checkSoldierHit(Soldier soldier) {
        Rectangle paddleRect = paddle.getNode();
        return soldier.getBoundsInParent().intersects(paddleRect.getBoundsInParent());
    }
}
