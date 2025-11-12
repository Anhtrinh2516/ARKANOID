package event.treasurehunter.logic;

import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import logic.Brick;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TreasureBrick extends Brick {
    private static final int HITS = 2;
    private static final int COIN_DROP = 15;
    private Random random = new Random();
    
    public TreasureBrick(Rectangle node) {
        super(node, HITS, false, 0);
    }
    
    @Override
    protected void refreshColor() {
        Rectangle rect = getNode();
        DropShadow shadow = new DropShadow();
        shadow.setRadius(6);
        shadow.setSpread(0.3);
        shadow.setColor(Color.rgb(255, 215, 0, 0.4));
        
        try {
            Image img = new Image(getClass().getResourceAsStream("/event/skins/treasure_hunter/treasure_hunter_chest_brick.png"));
            rect.setFill(new ImagePattern(img));
            
            if (hitsRemaining == 2) {
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(2);
            } else if (hitsRemaining == 1) {
                rect.setStroke(Color.SKYBLUE);
                rect.setStrokeWidth(2.5);
                shadow.setRadius(4);
                shadow.setSpread(0.2);
                shadow.setColor(Color.rgb(255, 150, 150, 0.3));
            } else {
                rect.setVisible(false);
            }
        } catch (Exception e) {
            rect.setFill(Color.rgb(218, 165, 32));
            rect.setStroke(Color.BLACK);
            rect.setStrokeWidth(2);
        }
        rect.setEffect(shadow);
    }
    
    public List<Coin> dropCoins() {
        List<Coin> coinList = new ArrayList<>();
        Rectangle rect = getNode();
        double centerX = rect.getX() + rect.getWidth() / 2;
        double centerY = rect.getY() + rect.getHeight() / 2;
        
        for (int i = 0; i < COIN_DROP; i++) {
            double randomX = centerX + random.nextDouble(-80, 80);
            double randomY = centerY + random.nextDouble(-30, 30);
            double speed = 2.5 + random.nextDouble() * 1.5;
            coinList.add(new Coin(randomX, randomY, speed));
        }
        return coinList;
    }
}
