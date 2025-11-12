package event.treasurehunter.controller;

import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import event.base.EventGameController;
import event.treasurehunter.logic.TreasureHunterEngine;
import logic.Ball;
import core.MainApp;

public class TreasureHunterController extends EventGameController {

    private TreasureHunterEngine eng;

    @Override
    protected Object createEngine(AnchorPane p, Rectangle pad, Circle b) {
        eng = new TreasureHunterEngine(
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
            String bg = "-fx-background-color: #0a1628;" +
                           "-fx-background-image: url('/event/backgrounds/treasure_hunter_background.png');" +
                           "-fx-background-size: cover;" +
                           "-fx-background-position: center;";
            
            anchorPane.setStyle(bg);

            try {
                Image img = new Image(getClass().getResourceAsStream("/event/skins/treasure_hunter/treasure_hunter_ball.png"));
                ImagePattern p = new ImagePattern(img);
                ball.setFill(p);
                ball.setStroke(Color.BLACK);
                ball.setStrokeWidth(1);
                
                javafx.scene.effect.DropShadow g = new javafx.scene.effect.DropShadow();
                g.setColor(Color.rgb(150, 220, 255, 0.3));
                g.setRadius(8);
                g.setSpread(0.3);
                ball.setEffect(g);
            } catch (Exception e) {
                ball.setFill(Color.rgb(150, 220, 255));
            }

            try {
                Image img = new Image(getClass().getResourceAsStream("/event/skins/treasure_hunter/treasure_hunter_paddle.png"));
                ImagePattern p = new ImagePattern(img);
                paddle.setFill(p);
                paddle.setStroke(Color.BLACK);
                paddle.setStrokeWidth(1);
                
                javafx.scene.effect.DropShadow g = new javafx.scene.effect.DropShadow();
                g.setColor(Color.rgb(255, 150, 200, 0.3));
                g.setRadius(6);
                g.setSpread(0.2);
                paddle.setEffect(g);
            } catch (Exception e) {
                paddle.setFill(Color.rgb(255, 100, 180));
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
    
    public TreasureHunterEngine getTreasureEngine() {
        return eng;
    }
    
    @Override
    protected String getPaddleSkinPath() {
        return "/event/skins/treasure_hunter/treasure_hunter_paddle.png";
    }
    
    @Override
    protected String getBallSkinPath() {
        return "/event/skins/treasure_hunter/treasure_hunter_ball.png";
    }

    @Override
    protected String getStartLevelHint() {
        return "🏴‍☠️ Destroy all TREASURES! | [Q/E] Aim | [SPACE] Launch | [←→] Move | [N] Skip | [P] Pause";
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
