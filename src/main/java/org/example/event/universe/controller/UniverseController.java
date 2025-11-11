package org.example.event.universe.controller;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.example.event.base.EventGameController;
import org.example.event.universe.logic.UniverseEngine;
import org.example.logic.Ball;

public class UniverseController extends EventGameController {

    private UniverseEngine eng;

    @Override
    protected Object createEngine(AnchorPane p, Rectangle pad, Circle b) {
        eng = new UniverseEngine(
            p, pad, b,
            this::updateScoreUI,
            this::updateLivesUI,
            this::updateLevelUI
        );
        
        eng.setOnPaddleHitCallback(this::onLaserHit);
        
        return eng;
    }

    @Override
    protected void applyTheme() {
        javafx.application.Platform.runLater(() -> {
            anchorPane.setStyle("-fx-background-color: #0a0a1a;" +
                               "-fx-background-image: url('/event/backgrounds/universe_background.png');" +
                               "-fx-background-size: cover; -fx-background-position: center;");
            
            applySkinWithGlow(ball, "/event/skins/universe/universe_ball.png", 
                     Color.rgb(100, 150, 255), Color.rgb(150, 200, 255, 0.4), 10, 0.4);
            
            applySkinWithGlow(paddle, "/event/skins/universe/universe_paddle.png", 
                     Color.rgb(150, 100, 255), Color.rgb(200, 200, 255, 0.3), 8, 0.2);
        });
    }

    @Override
    protected Ball getEngineBall() {
        return eng.getBall();
    }

    @Override
    protected void updateEngine() {
        eng.update();
    }

    @Override
    protected void movePaddleLeft() {
        eng.movePaddleLeft();
    }

    @Override
    protected void movePaddleRight() {
        eng.movePaddleRight();
    }

    @Override
    protected void adjustAimLeft() {
        eng.adjustAimLeft();
    }

    @Override
    protected void adjustAimRight() {
        eng.adjustAimRight();
    }

    @Override
    protected void launchBall() {
        eng.launchBall();
    }

    @Override
    protected void loadEngineLevel(int lv) {
        eng.loadLevel(lv);
    }
    
    public UniverseEngine getUniverseEngine() {
        return eng;
    }
    
    @Override
    protected String getPaddleSkinPath() {
        return "/event/skins/universe/universe_paddle.png";
    }
    
    @Override
    protected String getBallSkinPath() {
        return "/event/skins/universe/universe_ball.png";
    }

    @Override
    protected String getStartLevelHint() {
        return "🌌 Destroy all ALIENS! Avoid LASERS! | [Q/E] Aim | [SPACE] Launch | [←→] Move | [N] Skip | [P] Pause";
    }

    @Override
    protected void onBackPressed() {
        try {
            org.example.MainApp.showSelectEvent();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void skipToNextLevel() {
        eng.autoCompleteLevel();
        
        if (hintLabel != null) {
            hintLabel.setText("⏭ Auto-completing level...");
            javafx.animation.Timeline t = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(2),
                    e -> hintLabel.setText("")));
            t.play();
        }
    }
    
    private void onLaserHit() {
        currentLives--;
        updateLivesUI(currentLives);
        
        if (hintLabel != null) {
            hintLabel.setText("💥 LASER HIT! -1 LIFE");
            new javafx.animation.Timeline(new javafx.animation.KeyFrame(
                javafx.util.Duration.seconds(2), e -> hintLabel.setText(""))).play();
        }
        
        if (currentLives <= 0) {
            if (gameTimer != null) gameTimer.stop();
            javafx.application.Platform.runLater(() -> {
                org.example.ui.GameOverDialog.show(currentScore, currentLevel);
                currentLives = 3;
                currentScore = 0;
                updateLivesUI(currentLives);
                updateScoreUI(currentScore);
                startLevel(1);
            });
        }
    }
}
