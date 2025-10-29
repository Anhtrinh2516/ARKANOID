package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.MainApp;

public class ShopController {

    @FXML private Label coinsLabel, messageLabel;
    @FXML private Label wideItemLabel, lifeItemLabel, slowItemLabel;

    public void initialize() {
        refreshUI();
    }

    private boolean spend(int cost) {
        if (GameState.INSTANCE.getCoins() < cost) {
            messageLabel.setText("Not enough coins!");
            return false;
        }
        GameState.INSTANCE.addCoins(-cost);
        refreshUI();
        return true;
    }

    private void refreshUI() {
        coinsLabel.setText("Coins: " + GameState.INSTANCE.getCoins());
        messageLabel.setText("");

        // Update item counts if labels exist
        if (wideItemLabel != null) {
            wideItemLabel.setText("Owned: " + GameState.INSTANCE.getWideItemCount());
        }
        if (lifeItemLabel != null) {
            lifeItemLabel.setText("Owned: " + GameState.INSTANCE.getLifeItemCount());
        }
        if (slowItemLabel != null) {
            slowItemLabel.setText("Owned: " + GameState.INSTANCE.getSlowItemCount());
        }
    }

    // === NEW: Buy consumable items ===
    @FXML
    private void buyWideItem() {
        if (spend(3)) {
            GameState.INSTANCE.addWideItem(1);
            messageLabel.setText("Purchased Wide Paddle item! Press 1 to use in game.");
            refreshUI();
        }
    }

    @FXML
    private void buyLifeItem() {
        if (spend(5)) {
            GameState.INSTANCE.addLifeItem(1);
            messageLabel.setText("Purchased Extra Life item! Press 2 to use in game.");
            refreshUI();
        }
    }

    @FXML
    private void buySlowItem() {
        if (spend(4)) {
            GameState.INSTANCE.addSlowItem(1);
            messageLabel.setText("Purchased Slow Ball item! Press 3 to use in game.");
            refreshUI();
        }
    }

    // === OLD: Permanent upgrades ===
    @FXML
    private void buyLife() {
        if (spend(5)) {
            messageLabel.setText("Purchased +1 Life (apply in next level)!");
        }
    }

    @FXML
    private void buyPaddleWidth() {
        if (spend(8)) {
            GameState.INSTANCE.addPaddleWidthBonus(20);
            messageLabel.setText("Paddle will be wider in game!");
        }
    }

    @FXML
    private void buyBallSpeed() {
        if (spend(6)) {
            GameState.INSTANCE.setBallSpeedScale(
                    GameState.INSTANCE.getBallSpeedScale() * 1.1
            );
            messageLabel.setText("Ball will move faster!");
        }
    }

    @FXML
    private void backToMenu() {
        try {
            MainApp.showMainMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}