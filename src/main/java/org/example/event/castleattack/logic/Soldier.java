package org.example.event.castleattack.logic;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

public class Soldier extends Rectangle {
    
    private static final double SPEED = 3.0;
    private static final double WIDTH = 35.0; 
    private static final double HEIGHT = 52.0; 
    
    private boolean active = true;
    
    public Soldier(double x, double y) {
        super(x, y, WIDTH, HEIGHT);
        
        try {
            Image img = new Image(getClass().getResourceAsStream("/event/skins/castle/castle_soldier.png"));
            setFill(new ImagePattern(img));
        } catch (Exception e) {
            LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(180, 50, 50)),
                new Stop(0.5, Color.rgb(150, 30, 30)),
                new Stop(1, Color.rgb(100, 20, 20))
            );
            setFill(gradient);
        }
        
        setStroke(null);
        
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        shadow.setRadius(5);
        shadow.setSpread(0.3);
        setEffect(shadow);
        
        setArcWidth(4);
        setArcHeight(4);
    }
    
    public void update() {
        if (!active) return;
        setY(getY() + SPEED);
    }
    
    public boolean isOutOfBounds(double height) {
        return getY() > height;
    }
    
    public void deactivate() {
        active = false;
    }
    
    public boolean isActive() {
        return active;
    }
}
