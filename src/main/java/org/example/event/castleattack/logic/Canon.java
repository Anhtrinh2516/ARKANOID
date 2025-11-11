package org.example.event.castleattack.logic;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

public class Canon extends Circle {
    
    private static final double SPEED = 4.5;
    private static final double SIZE = 10.0; 
    private boolean active = true;
    
    public Canon(double x, double y) {
        super(x, y, SIZE);
        
        try {
            Image img = new Image(getClass().getResourceAsStream("/event/skins/castle/castle_canon.png"));
            setFill(new ImagePattern(img));
        } catch (Exception e) {
            setFill(Color.rgb(50, 50, 50));
        }
        
        setStroke(null);
        
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.6));
        shadow.setRadius(6);
        shadow.setSpread(0.3);
        setEffect(shadow);
    }
    
    public void update() {
        if (!active) return;
        setCenterY(getCenterY() + SPEED);
    }
    
    public boolean isOutOfBounds(double height) {
        return getCenterY() + SIZE > height;
    }
    
    public void deactivate() {
        active = false;
    }
    
    public boolean isActive() {
        return active;
    }
}
