package logic;

import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;

public class PowerUp extends Circle {
    private PowerUpType type;
    private double speed = 3.0;
    private double rotation = 0;

    public PowerUp(PowerUpType type, double x, double y) {
        super(x, y, 12);
        this.type = type;


        DropShadow shadow = new DropShadow();
        shadow.setRadius(8);
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        shadow.setColor(Color.rgb(0, 0, 0, 0.7));


        Glow glow = new Glow(0.8);
        shadow.setInput(glow);
        setEffect(shadow);

        // Load image based on powerup type
        String imagePath = getPowerUpImagePath(type);
        try {
            Image img = new Image(getClass().getResourceAsStream(imagePath));
            setFill(new ImagePattern(img));
            
            // Set stroke color based on type
            Color strokeColor = switch (type) {
                case COIN -> Color.DARKGOLDENROD;
                case EXTRA_LIFE -> Color.DARKRED;
                case EXPAND_PADDLE -> Color.DARKBLUE;
                case SLOW_BALL -> Color.DARKGREEN;
                case MULTIBALL -> Color.DARKVIOLET;
            };
            setStroke(strokeColor);
            setStrokeWidth(2.5);
        } catch (Exception e) {
            // Fallback to gradient if image not found
            switch (type) {
                case COIN -> {
                    RadialGradient gradient = new RadialGradient(
                            0, 0, 0.5, 0.5, 0.7, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.YELLOW),
                            new Stop(0.3, Color.GOLD),
                            new Stop(0.7, Color.ORANGE),
                            new Stop(1, Color.DARKGOLDENROD)
                    );
                    setFill(gradient);
                    setStroke(Color.DARKGOLDENROD);
                    setStrokeWidth(3);
                }
                case EXTRA_LIFE -> {
                    RadialGradient gradient = new RadialGradient(
                            0, 0, 0.5, 0.5, 0.7, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.LIGHTCORAL),
                            new Stop(0.5, Color.RED),
                            new Stop(1, Color.DARKRED)
                    );
                    setFill(gradient);
                    setStroke(Color.DARKRED);
                    setStrokeWidth(2.5);
                }
                case EXPAND_PADDLE -> {
                    RadialGradient gradient = new RadialGradient(
                            0, 0, 0.5, 0.5, 0.7, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.LIGHTBLUE),
                            new Stop(0.5, Color.BLUE),
                            new Stop(1, Color.DARKBLUE)
                    );
                    setFill(gradient);
                    setStroke(Color.DARKBLUE);
                    setStrokeWidth(2.5);
                }
                case SLOW_BALL -> {
                    RadialGradient gradient = new RadialGradient(
                            0, 0, 0.5, 0.5, 0.7, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.LIGHTGREEN),
                            new Stop(0.5, Color.GREEN),
                            new Stop(1, Color.DARKGREEN)
                    );
                    setFill(gradient);
                    setStroke(Color.DARKGREEN);
                    setStrokeWidth(2.5);
                }
                case MULTIBALL -> {
                    RadialGradient gradient = new RadialGradient(
                            0, 0, 0.5, 0.5, 0.7, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.LAVENDER),
                            new Stop(0.5, Color.PURPLE),
                            new Stop(1, Color.DARKVIOLET)
                    );
                    setFill(gradient);
                    setStroke(Color.DARKVIOLET);
                    setStrokeWidth(2.5);
                }
            }
        }

        System.out.println("PowerUp created: " + type + " at (" + x + ", " + y + ")");
    }
    
    private String getPowerUpImagePath(PowerUpType type) {
        return switch (type) {
            case COIN -> "/images/powerup/xu.png";
            case EXTRA_LIFE -> "/images/powerup/extra_life.png";
            case EXPAND_PADDLE -> "/images/powerup/expand_paddle.png";
            case SLOW_BALL -> "/images/powerup/slow_ball.png";
            case MULTIBALL -> "/images/powerup/multi_ball.png";
        };
    }

    public void update() {
        setCenterY(getCenterY() + speed);
        if (type == PowerUpType.COIN) {
            rotation += 8;
            setRotate(rotation);

            double scale = 1.0 + 0.1 * Math.sin(rotation * Math.PI / 180);
            setScaleX(scale);
            setScaleY(scale);
        }
    }

    public PowerUpType getType() {
        return type;
    }
}
