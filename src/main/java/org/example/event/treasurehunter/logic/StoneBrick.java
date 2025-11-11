package org.example.event.treasurehunter.logic;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import org.example.logic.Brick;

public class StoneBrick extends Brick {
    private static final int HITS = 4;
    
    public StoneBrick(Rectangle node) {
        super(node, HITS, false, 0);
    }
    
    @Override
    protected void refreshColor() {
        Rectangle rect = getNode();
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5);
        shadow.setSpread(0.3);
        shadow.setColor(Color.rgb(0, 0, 0, 0.4));
        
        try {
            Image img = new Image(getClass().getResourceAsStream("/event/skins/treasure_hunter/treasure_hunter_stone_brick.png"));
            rect.setFill(new ImagePattern(img));
            
            if (hitsRemaining == 4) { 
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(2);
            } else if (hitsRemaining == 3) { 
                rect.setStroke(Color.SKYBLUE);
                rect.setStrokeWidth(2.5);
                shadow.setColor(Color.rgb(100, 50, 50, 0.5)); 
            } else if (hitsRemaining == 2) { 
                rect.setStroke(Color.RED);
                rect.setStrokeWidth(3);
                shadow.setColor(Color.rgb(150, 50, 50, 0.5)); 
            } else if (hitsRemaining == 1) { 
                rect.setStroke(Color.WHITE);
                rect.setStrokeWidth(3.5);
                shadow.setColor(Color.rgb(200, 70, 60, 0.6)); 
                shadow.setRadius(6); 
            } else {
                rect.setVisible(false);
            }
        } catch (Exception e) {
            rect.setFill(Color.GRAY);
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(2);
        }
        rect.setEffect(shadow);
    }
}
