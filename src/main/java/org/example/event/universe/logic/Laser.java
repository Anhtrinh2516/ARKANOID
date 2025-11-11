package org.example.event.universe.logic;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Laser extends Rectangle {
    
    private static final double SPEED = 5.0;
    private static final double WIDTH = 8.0;
    private static final double HEIGHT = 30.0;
    private boolean active = true;
    
    public Laser(double x, double y) {
        super(x - WIDTH/2, y, WIDTH, HEIGHT);
        
        try {
            Image img = new Image(getClass().getResourceAsStream("/event/skins/universe/universe_laser.png"));
            setFill(new ImagePattern(img));
        } catch (Exception e) {
            setFill(Color.rgb(255, 50, 50));
        }
        
        setStroke(null);
        
        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(255, 0, 0, 0.5));
        glow.setRadius(6);
        glow.setSpread(0.3);
        setEffect(glow);
    }
    
    public void update() {
        if (!active) return;
        setY(getY() + SPEED);
    }
    
    public boolean isOutOfBounds(double height) {
        return getY() > height;
    }
    
    public void deactivate() {
        this.active = false;
    }
    
    public boolean isActive() {
        return active;
    }
}
