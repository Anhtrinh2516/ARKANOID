package org.example.event.universe.logic;

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

public class AlienBrick extends Brick {
    private static final int HITS = 2;
    private static final int SCORE = 50;
    
    private long lastShot;
    private static final Random random = new Random();
    
    public AlienBrick(Rectangle rect) {
        super(rect, HITS, false, SCORE);
        this.lastShot = System.currentTimeMillis() + random.nextInt(1000) + 1000;
    }
    
    public boolean canShoot() {
        long now = System.currentTimeMillis();
        if (now - lastShot >= 3000) {
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
            Image img = new Image(getClass().getResourceAsStream("/event/skins/universe/universe_alien_brick.png"));
            rect.setFill(new ImagePattern(img));
        } catch (Exception e) {
            LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(0, 255, 100)), 
                new Stop(0.5, Color.rgb(0, 200, 80)),  
                new Stop(1, Color.rgb(0, 150, 60))       
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
       
        Glow glow = new Glow(0.3);
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 255, 100, 0.4));
        shadow.setRadius(6);
        shadow.setSpread(0.2);
        glow.setInput(shadow);
        rect.setEffect(glow);
        
        rect.setArcWidth(8);
        rect.setArcHeight(8);
    }
}
