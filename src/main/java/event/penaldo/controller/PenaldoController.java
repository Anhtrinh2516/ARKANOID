package event.penaldo.controller;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import event.base.EventGameController;
import event.penaldo.logic.PenaldoEngine;
import logic.Ball;
import core.MainApp;

public class PenaldoController extends EventGameController {

    private PenaldoEngine eng;

    @Override
    protected Object createEngine(AnchorPane p, Rectangle pad, Circle b) {
        eng = new PenaldoEngine(
            p, pad, b,
            this::updateScoreUI,
            this::updateLivesUI,
            this::updateLevelUI
        );
        return eng;
    }

    @Override
    protected void applyTheme() {
        javafx.application.Platform.runLater(() -> {
            String bg = "-fx-background-color: #1a3a1a;" +
                           "-fx-background-image: url('/event/backgrounds/penaldo_background.png');" +
                           "-fx-background-size: cover;" +
                           "-fx-background-position: center;";
            
            anchorPane.setStyle(bg);

            try {
                Image img = new Image(getClass().getResourceAsStream("/event/skins/penaldo/penaldo_ball.png"));
                ImagePattern p = new ImagePattern(img);
                ball.setFill(p);
                ball.setStroke(Color.rgb(0, 0, 0, 0.5));
                ball.setStrokeWidth(1.5);
                
                javafx.scene.effect.DropShadow g = new javafx.scene.effect.DropShadow();
                g.setColor(Color.rgb(255, 255, 255, 0.3));
                g.setRadius(8);
                g.setSpread(0.3);
                ball.setEffect(g);
            } catch (Exception e) {
                ball.setFill(Color.WHITE);
                ball.setStroke(Color.BLACK);
                ball.setStrokeWidth(2);
            }

            try {
                Image img = new Image(getClass().getResourceAsStream("/event/skins/penaldo/penaldo_paddle.png"));
                ImagePattern p = new ImagePattern(img);
                paddle.setFill(p);
                paddle.setStroke(Color.rgb(0, 100, 255, 0.5));
                paddle.setStrokeWidth(2);
                
                javafx.scene.effect.DropShadow g = new javafx.scene.effect.DropShadow();
                g.setColor(Color.rgb(0, 150, 255, 0.3));
                g.setRadius(6);
                g.setSpread(0.2);
                paddle.setEffect(g);
            } catch (Exception e) {
                paddle.setFill(Color.rgb(0, 100, 255));
            }
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
    
    public PenaldoEngine getPenaldoEngine() {
        return eng;
    }
    
    @Override
    protected String getPaddleSkinPath() {
        return "/event/skins/penaldo/penaldo_paddle.png";
    }
    
    @Override
    protected String getBallSkinPath() {
        return "/event/skins/penaldo/penaldo_ball.png";
    }

    @Override
    protected String getStartLevelHint() {
        return "⚽ SCORE A GOAL! Destroy all footballs! | [Q/E] Aim | [SPACE] Launch | [←→] Move | [N] Skip | [P] Pause";
    }

    @Override
    protected void onBackPressed() {
        try {
            MainApp.showSelectEvent();
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
}
