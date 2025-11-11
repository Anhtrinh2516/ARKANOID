package org.example.event.treasurehunter.logic;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;

public class Coin extends Circle {
    
    private double speed;
    private double angle = 0;
    private boolean collected = false;
    
    public Coin(double x, double y) {
        super(x, y, 8);
        this.speed = 3;
        makeGold();
    }
    
    public Coin(double x, double y, double speed) {
        super(x, y, 8);
        this.speed = speed;
        makeGold();
    }
    
    private void makeGold() {
        RadialGradient gradient = new RadialGradient(
            0, 0, 0.3, 0.3, 0.5, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.rgb(255, 223, 0)),
            new Stop(0.5, Color.rgb(255, 193, 37)),
            new Stop(1, Color.rgb(218, 165, 32))
        );
        setFill(gradient);
        
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(255, 215, 0, 0.6));
        shadow.setRadius(8);
        shadow.setSpread(0.3);
        setEffect(shadow);
        
        setStroke(Color.rgb(184, 134, 11));
        setStrokeWidth(1.5);
    }
    
    public void update() {
        if (collected) return;
        
        setCenterY(getCenterY() + speed);
        
        angle += 5;
        if (angle >= 360) angle = 0;
        setRotate(angle);
        
        double shine = 0.85 + 0.15 * Math.sin(Math.toRadians(angle * 2));
        setOpacity(shine);
    }
    
    public void collect() {
        collected = true;
        setScaleX(1.5);
        setScaleY(1.5);
        setOpacity(0.3);
    }
    
    public boolean isCollected() {
        return collected;
    }
    
    public boolean isOutOfBounds(double height) {
        return getCenterY() - getRadius() > height;
    }
    
    public int getScoreValue() {
        return 10;
    }
    
    public int getCoinValue() {
        return 1;
    }
}
