package org.example.event.castleattack.logic;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import org.example.logic.Brick;

import java.util.Random;

public class CanonBrick extends Brick {
    private static final int HITS = 2;
    private static final int SCORE = 40;
    
    private long lastShot;
    private static final Random random = new Random();
    
    public CanonBrick(Rectangle rect) {
        super(rect, HITS, false, SCORE);
        this.lastShot = System.currentTimeMillis() + random.nextInt(1000) + 1000;
    }
    
    public boolean canShoot() {
        long now = System.currentTimeMillis();
        int cooldown = 3000 + random.nextInt(1000);
        if (now - lastShot >= cooldown) {
            lastShot = now;
            return true;
        }
        return false;
    }
    
    public double getCenterX() {
        Rectangle rect = getNode();
        return rect.getX() + rect.getWidth() / 2;
    }
    
    public double getCenterY() {
        Rectangle rect = getNode();
        return rect.getY() + rect.getHeight() / 2;
    }
    
    @Override
    protected void refreshColor() {
        Rectangle rect = getNode();
        
        try {
            Image img = new Image(getClass().getResourceAsStream("/event/skins/castle/canon_brick.png"));
            rect.setFill(new ImagePattern(img));
        } catch (Exception e) {
            LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(100, 100, 100)),
                new Stop(0.5, Color.rgb(70, 70, 70)),
                new Stop(1, Color.rgb(50, 50, 50))
            );
            rect.setFill(gradient);
        }
        
        if (hitsRemaining == 2) {
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(2);
        } else if (hitsRemaining == 1) {
            rect.setStroke(Color.SKYBLUE);
            rect.setStrokeWidth(2.5);
        }
        
        Glow glow = new Glow(0.2);
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(50, 50, 50, 0.4));
        shadow.setRadius(5);
        shadow.setSpread(0.15);
        glow.setInput(shadow);
        rect.setEffect(glow);
        
        rect.setArcWidth(6);
        rect.setArcHeight(6);
    }
}
