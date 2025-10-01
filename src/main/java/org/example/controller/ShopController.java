package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.MainApp;
import org.example.controller.GameState;

public class ShopController {

    @FXML private Label coinsLabel, messageLabel;

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
    }

    // === Actions ===
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
