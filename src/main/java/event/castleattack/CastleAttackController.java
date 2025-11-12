package event.castleattack;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import event.base.EventGameController;
import event.castleattack.logic.CastleAttackEngine;
import logic.Ball;
import core.MainApp;

public class CastleAttackController extends EventGameController {

    private CastleAttackEngine eng;

    @Override
    protected Object createEngine(AnchorPane p, Rectangle pad, Circle b) {
        eng = new CastleAttackEngine(
            p, pad, b,
            this::updateScoreUI,
            this::updateLivesUI,
            this::updateLevelUI
        );
        
        eng.setOnPaddleHitByCanonCallback(this::onCanonHit);
        eng.setOnPaddleHitBySoldierCallback(this::onSoldierHit);
        
        return eng;
    }

    @Override
    protected void applyTheme() {
        javafx.application.Platform.runLater(() -> {
            anchorPane.setStyle("-fx-background-color: linear-gradient(to bottom, #3A3A3A 0%, #1F1F1F 100%);" +
                               "-fx-background-image: url('/event/backgrounds/castle_background.png');" +
                               "-fx-background-size: cover; -fx-background-position: center;");
            
            applySkinWithGlow(ball, "/event/skins/castle/castle_ball.png", 
                     Color.rgb(70, 70, 70), Color.rgb(255, 215, 0), 8.0, 0.4);
            
            applySkinWithGlow(paddle, "/event/skins/castle/castle_paddle.png", 
                     Color.rgb(139, 90, 43), Color.rgb(218, 165, 32), 6.0, 0.3);
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
    
    public CastleAttackEngine getCastleEngine() {
        return eng;
    }
    
    @Override
    protected String getPaddleSkinPath() {
        return "/event/skins/castle/castle_paddle.png";
    }
    
    @Override
    protected String getBallSkinPath() {
        return "/event/skins/castle/castle_ball.png";
    }

    @Override
    protected String getStartLevelHint() {
        return "🏰 Destroy all GATES! 💣 Canon bricks shoot canons | 🚪 Gates spawn soldiers | [Q/E] Aim | [SPACE] Launch | [←→] Move";
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
    
    private void onCanonHit() {
        currentLives--;
        updateLivesUI(currentLives);
        
        if (hintLabel != null) {
            hintLabel.setText("💣 CANON HIT! -1 LIFE");
            new javafx.animation.Timeline(new javafx.animation.KeyFrame(
                javafx.util.Duration.seconds(2), e -> hintLabel.setText(""))).play();
        }
    }
    
    private void onSoldierHit() {
        currentLives--;
        updateLivesUI(currentLives);
        
        if (hintLabel != null) {
            hintLabel.setText("⚔️ SOLDIER HIT! -1 LIFE");
            new javafx.animation.Timeline(new javafx.animation.KeyFrame(
                javafx.util.Duration.seconds(2), e -> hintLabel.setText(""))).play();
        }
    }
}
