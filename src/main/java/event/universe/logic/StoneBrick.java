package event.universe.logic;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import logic.Brick;

public class StoneBrick extends Brick {
    private static final int HITS = 3;
    private static final int SCORE = 30;
    
    public StoneBrick(Rectangle rect) {
        super(rect, HITS, false, SCORE);
    }
    
    @Override
    protected void refreshColor() {
        Rectangle rect = getNode();
        
        try {
            Image img = new Image(getClass().getResourceAsStream("/event/skins/universe/universe_stone_brick.png"));
            rect.setFill(new ImagePattern(img));
        } catch (Exception e) {
            LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(120, 120, 120)),
                new Stop(0.5, Color.rgb(80, 80, 80)),
                new Stop(1, Color.rgb(50, 50, 50))
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
        
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(50, 50, 50, 0.3));
        shadow.setRadius(4);
        shadow.setSpread(0.15);
        rect.setEffect(shadow);
        
        rect.setArcWidth(4);
        rect.setArcHeight(4);
    }
}
