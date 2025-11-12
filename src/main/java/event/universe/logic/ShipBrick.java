package event.universe.logic;

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

import java.util.Random;

public class ShipBrick extends Brick {
    private static final double SPEED = 1.5;
    private static final Random random = new Random();
    
    private double velocityX;
    private double minX = 0;
    private double maxX = 888.0;
    
    public ShipBrick(Rectangle rect) {
        super(rect, 1, true, 0);
        this.velocityX = random.nextBoolean() ? SPEED : -SPEED;
    }
    
    public void move() {
        if (isDestroyed()) return;
        
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
    
    public void setBounds(double min, double max) {
        this.minX = min;
        this.maxX = max;
    }
    
    public void reverseDirection() {
        velocityX = -velocityX;
    }
    
    @Override
    protected void refreshColor() {
        Rectangle rect = getNode();
        
        try {
            Image img = new Image(getClass().getResourceAsStream("/event/skins/universe/universe_ship_brick.png"));
            rect.setFill(new ImagePattern(img));
        } catch (Exception e) {
            LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(200, 200, 220)),
                new Stop(0.5, Color.rgb(150, 150, 170)),
                new Stop(1, Color.rgb(100, 100, 120))
            );
            rect.setFill(gradient);
        }
        
        rect.setStroke(Color.rgb(80, 80, 100, 0.6));
        rect.setStrokeWidth(1.5);
        
        Glow glow = new Glow(0.2);
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(150, 150, 200, 0.3));
        shadow.setRadius(5);
        shadow.setSpread(0.2);
        glow.setInput(shadow);
        rect.setEffect(glow);
        
        rect.setArcWidth(6);
        rect.setArcHeight(6);
    }
}
