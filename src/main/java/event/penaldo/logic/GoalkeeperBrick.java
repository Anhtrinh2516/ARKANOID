package event.penaldo.logic;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import logic.Brick;

public class GoalkeeperBrick extends Brick {
    private static final double SPEED = 2.0;
    
    private double velocityX;
    double minX;
    double maxX;
    
    public GoalkeeperBrick(Rectangle rect, double minX, double maxX) {
        super(rect, 1, true, 0);
        this.minX = minX;
        this.maxX = maxX;
        this.velocityX = SPEED;
    }
    
    public void move() {
        Rectangle rect = getNode();
        double newX = rect.getX() + velocityX;
        
        if (newX <= minX) {
            newX = minX;
            velocityX = SPEED;
        } else if (newX + rect.getWidth() >= maxX) {
            newX = maxX - rect.getWidth();
            velocityX = -SPEED;
        }
        
        rect.setX(newX);
    }
    
    public void setBounds(double minX, double maxX) {
        this.minX = minX;
        this.maxX = maxX;
    }
    
    public double getMinX() {
        return minX;
    }
    
    public double getMaxX() {
        return maxX;
    }
    
    @Override
    protected void refreshColor() {
        Rectangle rect = getNode();
        
        try {
            Image img = new Image(getClass().getResourceAsStream("/event/skins/penaldo/penaldo_goalkeeper.png"));
            rect.setFill(new ImagePattern(img));
        } catch (Exception e) {
            LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(0, 180, 0)),
                new Stop(0.5, Color.rgb(0, 150, 0)),
                new Stop(1, Color.rgb(0, 100, 0))
            );
            rect.setFill(gradient);
        }
        
        rect.setStroke(Color.rgb(0, 80, 0));
        rect.setStrokeWidth(3);
        
        Glow glow = new Glow(0.4);
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 255, 0, 0.5));
        shadow.setRadius(10);
        shadow.setSpread(0.4);
        glow.setInput(shadow);
        rect.setEffect(glow);
        
        rect.setArcWidth(8);
        rect.setArcHeight(8);
    }
}
