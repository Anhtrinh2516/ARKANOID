package org.example.event.treasurehunter.logic;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.example.event.base.EventGameEngine;
import org.example.logic.Brick;

import java.util.*;
import java.util.function.IntConsumer;

public class TreasureHunterEngine extends EventGameEngine {

    private List<Coin> coins = new ArrayList<>();

    public TreasureHunterEngine(AnchorPane pane, Rectangle paddleNode, Circle ballNode,
                                IntConsumer scoreCallback, IntConsumer livesCallback, IntConsumer levelCallback) {
        super(pane, paddleNode, ballNode, scoreCallback, livesCallback, levelCallback);
    }

    @Override
    protected List<Brick> loadLevelBricks(int levelIndex) {
        return TreasureHunterLevelLoader.loadTreasureLevel(levelIndex, GAME_AREA_WIDTH);
    }

    @Override
    protected void onBrickDestroyed(Brick brick) {
        if (brick instanceof TreasureBrick) {
            TreasureBrick treasure = (TreasureBrick) brick;
            List<Coin> droppedCoins = treasure.dropCoins();
            coins.addAll(droppedCoins);
            for (Coin coin : droppedCoins) {
                pane.getChildren().add(coin);
            }
        }
    }

    @Override
    protected boolean checkWinCondition() {
        for (Brick brick : bricks) {
            if (brick instanceof TreasureBrick && !brick.isDestroyed()) {
                return false; 
            }
        }
        return true; 
    }

    @Override
    protected int getLevelCompleteBonus() {
        return 500;
    }

    @Override
    protected int getLevelCompleteCoins() {
        return 5;
    }
    
    @Override
    protected boolean isFinalLevel(int levelIndex) {
        return levelIndex >= 1;
    }

    @Override
    protected void onUpdate() {
        updateCoins();
    }

    @Override
    protected void onLevelComplete(int completedLevel) {
        if (completedLevel == 1) {
            org.example.controller.SkinManager.INSTANCE.unlockPaddleSkin(
                org.example.controller.SkinManager.PaddleSkin.TREASURE_HUNTER
            );
            org.example.controller.SkinManager.INSTANCE.unlockBallSkin(
                org.example.controller.SkinManager.BallSkin.TREASURE_HUNTER
            );
        }
    }

    @Override
    protected void onLifeLost() {
        for (Coin coin : coins) {
            pane.getChildren().remove(coin);
        }
        coins.clear();
    }

    private void updateCoins() {
        Iterator<Coin> iterator = coins.iterator();
        while (iterator.hasNext()) {
            Coin coin = iterator.next();
            
            if (coin.isCollected()) continue;
            
            coin.update(); 

            if (intersects(coin, paddle.getNode())) {
                collectCoin(coin);
                pane.getChildren().remove(coin);
                iterator.remove();
                continue;
            }

            if (coin.isOutOfBounds(GAME_AREA_HEIGHT)) {
                pane.getChildren().remove(coin);
                iterator.remove();
            }
        }
    }

    private void collectCoin(Coin coin) {
        coin.collect();
        score += coin.getScoreValue(); 
        scoreCallback.accept(score);
        org.example.controller.GameState.INSTANCE.addCoins(coin.getCoinValue()); 
    }
}
