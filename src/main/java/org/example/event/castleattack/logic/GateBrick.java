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

public class GateBrick extends Brick {
    private static final int HITS = 3;
    private static final int SCORE = 100;
    
    private long lastSpawn;
    private static final Random random = new Random();
    
    public GateBrick(Rectangle rect) {
        super(rect, HITS, false, SCORE);
        this.lastSpawn = System.currentTimeMillis() + random.nextInt(1000) + 2000;
    }
    
    public boolean canSpawnSoldier() {
        long now = System.currentTimeMillis();
        int cooldown = 5000 + random.nextInt(1000);
        if (now - lastSpawn >= cooldown) {
            lastSpawn = now;
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
            Image img = new Image(getClass().getResourceAsStream("/event/skins/castle/gate_brick.png"));
            rect.setFill(new ImagePattern(img));
        } catch (Exception e) {
            LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(120, 80, 50)),
                new Stop(0.5, Color.rgb(90, 60, 40)),
                new Stop(1, Color.rgb(60, 40, 25))
            );
            rect.setFill(gradient);
        }
        
        if (hitsRemaining == 3) {
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(2);
        } else if (hitsRemaining == 2) {
            rect.setStroke(Color.SKYBLUE);
            rect.setStrokeWidth(2.5);
        } else if (hitsRemaining == 1) {
            rect.setStroke(Color.RED);
            rect.setStrokeWidth(3);
        }
        
        Glow glow = new Glow(0.3);
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(90, 60, 40, 0.4));
        shadow.setRadius(6);
        shadow.setSpread(0.2);
        glow.setInput(shadow);
        rect.setEffect(glow);
        
        rect.setArcWidth(8);
        rect.setArcHeight(8);
    }
}
