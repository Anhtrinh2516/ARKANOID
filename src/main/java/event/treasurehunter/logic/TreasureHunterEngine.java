package event.treasurehunter.logic;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import event.base.EventGameEngine;
import logic.Brick;

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
                coin.setManaged(false);
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
            controller.SkinManager.INSTANCE.unlockPaddleSkin(
                controller.SkinManager.PaddleSkin.TREASURE_HUNTER
            );
            controller.SkinManager.INSTANCE.unlockBallSkin(
                controller.SkinManager.BallSkin.TREASURE_HUNTER
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
        if (coins.isEmpty()) return;
        
        List<Coin> toRemove = new ArrayList<>();
        
        for (Coin coin : coins) {
            if (coin.isCollected()) continue;
            
            coin.update(); 

            if (intersects(coin, paddle.getNode())) {
                collectCoin(coin);
                toRemove.add(coin);
            } else if (coin.isOutOfBounds(GAME_AREA_HEIGHT)) {
                toRemove.add(coin);
            }
        }
        
        if (!toRemove.isEmpty()) {
            coins.removeAll(toRemove);
            javafx.application.Platform.runLater(() -> pane.getChildren().removeAll(toRemove));
        }
    }

    private void collectCoin(Coin coin) {
        coin.collect();
        score += coin.getScoreValue(); 
        scoreCallback.accept(score);
        controller.GameState.INSTANCE.addCoins(coin.getCoinValue()); 
    }
}
