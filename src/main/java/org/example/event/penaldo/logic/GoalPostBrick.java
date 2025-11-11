package org.example.event.penaldo.logic;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.logic.Brick;

public class GoalPostBrick extends Brick {
    
    public GoalPostBrick(Rectangle rect) {
        super(rect, 1, true, 0);
    }
    
    @Override
    protected void refreshColor() {
        Rectangle rect = getNode();
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.DARKGRAY);
        rect.setStrokeWidth(2);
        rect.setArcWidth(5);
        rect.setArcHeight(5);
    }
}
