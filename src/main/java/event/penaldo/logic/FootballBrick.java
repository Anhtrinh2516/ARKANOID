package event.penaldo.logic;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import logic.Brick;

public class FootballBrick extends Brick {
    private static final int HITS = 1;
    private static final int SCORE = 10;
    
    public FootballBrick(Rectangle rect) {
        super(rect, HITS, false, SCORE);
    }
    
    @Override
    protected void refreshColor() {
        Rectangle rect = getNode();
        
        if (hitsRemaining <= 0) {
            rect.setVisible(false);
            return;
        }
        
        try {
            Image img = new Image(getClass().getResourceAsStream("/event/skins/penaldo/penaldo_brick.png"));
            rect.setFill(new ImagePattern(img));
        } catch (Exception e) {
            RadialGradient gradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.WHITE),
                new Stop(0.7, Color.rgb(230, 230, 230)),
                new Stop(1, Color.rgb(50, 50, 50))
            );
            rect.setFill(gradient);
        }
        
        rect.setStroke(Color.rgb(30, 30, 30));
        rect.setStrokeWidth(2);
        
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.4));
        shadow.setRadius(6);
        shadow.setSpread(0.3);
        rect.setEffect(shadow);
        
        rect.setArcWidth(6);
        rect.setArcHeight(6);
    }
}
