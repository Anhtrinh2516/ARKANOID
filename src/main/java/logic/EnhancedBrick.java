package logic;

import javafx.animation.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enhanced Brick with multi-hit system and crack effects
 */
public class EnhancedBrick extends StackPane {

    private final int maxHits;
    private int currentHits;
    private final Rectangle mainRect;
    private final Rectangle glowRect;
    private final List<Line> cracks = new ArrayList<>();
    private final Color baseColor;
    private final Random random = new Random();

    private static final Color[] TIER_COLORS = {
            Color.web("#00ff00"), // 1 hit - Green
            Color.web("#ffff00"), // 2 hits - Yellow
            Color.web("#ff8800"), // 3 hits - Orange
            Color.web("#ff0000"), // 4 hits - Red
            Color.web("#ff00ff") // 5+ hits - Magenta
    };

    public EnhancedBrick(double x, double y, double width, double height, int hits) {
        this.maxHits = Math.max(1, hits);
        this.currentHits = 0;
        this.baseColor = TIER_COLORS[Math.min(maxHits - 1, TIER_COLORS.length - 1)];

        setLayoutX(x);
        setLayoutY(y);
        setPrefSize(width, height);

        // Glow layer (outer)
        glowRect = new Rectangle(width, height);
        glowRect.setArcWidth(8);
        glowRect.setArcHeight(8);
        glowRect.setFill(baseColor);
        glowRect.setOpacity(0.3);
        DropShadow glow = new DropShadow();
        glow.setColor(baseColor);
        glow.setRadius(15);
        glow.setSpread(0.6);
        glowRect.setEffect(glow);

        // Main brick body with gradient
        mainRect = new Rectangle(width - 4, height - 4);
        mainRect.setArcWidth(6);
        mainRect.setArcHeight(6);
        updateBrickGradient();

        // Border highlight
        Rectangle highlight = new Rectangle(width - 8, 3);
        highlight.setArcWidth(4);
        highlight.setArcHeight(4);
        highlight.setFill(Color.WHITE);
        highlight.setOpacity(0.5);
        highlight.setTranslateY(-height / 2 + 5);

        getChildren().addAll(glowRect, mainRect, highlight);

        // Entrance animation
        playSpawnAnimation();
    }

    /**
     * Hit the brick - returns true if brick is destroyed
     */
    public boolean hit() {
        currentHits++;

        // Hit animation
        playHitAnimation();

        if (currentHits < maxHits) {
            // Add crack effect
            addCrack();
            updateBrickGradient();
            return false;
        } else {
            // Brick destroyed
            playDestroyAnimation();
            return true;
        }
    }

    public int getRemainingHits() {
        return maxHits - currentHits;
    }

    public boolean isDestroyed() {
        return currentHits >= maxHits;
    }

    /**
     * Update brick color based on remaining hits
     */
    private void updateBrickGradient() {
        double damagePercent = (double) currentHits / maxHits;

        Color topColor = baseColor.brighter();
        Color midColor = baseColor;
        Color bottomColor = baseColor.darker();

        // Darken as it takes damage
        if (damagePercent > 0) {
            double darkFactor = 1.0 - (damagePercent * 0.4);
            topColor = topColor.deriveColor(0, 1.0, darkFactor, 1.0);
            midColor = midColor.deriveColor(0, 1.0, darkFactor, 1.0);
            bottomColor = bottomColor.deriveColor(0, 1.0, darkFactor, 1.0);
        }

        LinearGradient gradient = new LinearGradient(
                0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, topColor),
                new Stop(0.5, midColor),
                new Stop(1.0, bottomColor));

        mainRect.setFill(gradient);
    }

    /**
     * Add a crack line to the brick
     */
    private void addCrack() {
        double width = getPrefWidth();
        double height = getPrefHeight();

        // Random crack pattern
        double startX = random.nextDouble() * width - width / 2;
        double startY = random.nextDouble() * height - height / 2;
        double endX = startX + (random.nextDouble() * 30 - 15);
        double endY = startY + (random.nextDouble() * 30 - 15);

        Line crack = new Line(startX, startY, endX, endY);
        crack.setStroke(Color.BLACK);
        crack.setStrokeWidth(1.5);
        crack.setOpacity(0);

        getChildren().add(crack);
        cracks.add(crack);

        // Animate crack appearing
        FadeTransition ft = new FadeTransition(Duration.millis(200), crack);
        ft.setFromValue(0);
        ft.setToValue(0.8);
        ft.play();
    }

    /**
     * Spawn animation when brick appears
     */
    private void playSpawnAnimation() {
        setScaleX(0);
        setScaleY(0);
        setOpacity(0);

        ScaleTransition st = new ScaleTransition(Duration.millis(300), this);
        st.setFromX(0);
        st.setFromY(0);
        st.setToX(1.0);
        st.setToY(1.0);
        st.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition ft = new FadeTransition(Duration.millis(300), this);
        ft.setFromValue(0);
        ft.setToValue(1.0);

        ParallelTransition pt = new ParallelTransition(st, ft);
        pt.setDelay(Duration.millis(random.nextInt(100)));
        pt.play();
    }

    /**
     * Hit animation - shake and flash
     */
    private void playHitAnimation() {
        // Flash effect
        Glow glow = new Glow(0.8);
        setEffect(glow);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, e -> setEffect(glow)),
                new KeyFrame(Duration.millis(100), e -> setEffect(null)));
        timeline.play();

        // Shake animation
        double originalX = getLayoutX();
        Timeline shake = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(layoutXProperty(), originalX)),
                new KeyFrame(Duration.millis(50), new KeyValue(layoutXProperty(), originalX - 3)),
                new KeyFrame(Duration.millis(100), new KeyValue(layoutXProperty(), originalX + 3)),
                new KeyFrame(Duration.millis(150), new KeyValue(layoutXProperty(), originalX - 2)),
                new KeyFrame(Duration.millis(200), new KeyValue(layoutXProperty(), originalX)));
        shake.play();
    }

    /**
     * Destroy animation - explode effect
     */
    private void playDestroyAnimation() {
        // Scale down and fade out
        ScaleTransition st = new ScaleTransition(Duration.millis(200), this);
        st.setToX(0.1);
        st.setToY(0.1);
        st.setInterpolator(Interpolator.EASE_IN);

        FadeTransition ft = new FadeTransition(Duration.millis(200), this);
        ft.setToValue(0);

        // Rotation for dramatic effect
        RotateTransition rt = new RotateTransition(Duration.millis(200), this);
        rt.setByAngle(180);

        ParallelTransition pt = new ParallelTransition(st, ft, rt);
        pt.setOnFinished(e -> {
            if (getParent() != null) {
                ((javafx.scene.layout.Pane) getParent()).getChildren().remove(this);
            }
        });
        pt.play();

        // Create particle effect (optional - simplified version)
        createParticles();
    }

    /**
     * Create simple particle effect on destroy
     */
    private void createParticles() {
        if (getParent() == null)
            return;

        javafx.scene.layout.Pane parent = (javafx.scene.layout.Pane) getParent();
        double centerX = getLayoutX() + getPrefWidth() / 2;
        double centerY = getLayoutY() + getPrefHeight() / 2;

        for (int i = 0; i < 8; i++) {
            Rectangle particle = new Rectangle(4, 4, baseColor);
            particle.setArcWidth(2);
            particle.setArcHeight(2);
            particle.setLayoutX(centerX);
            particle.setLayoutY(centerY);

            parent.getChildren().add(particle);

            double angle = Math.PI * 2 * i / 8;
            double distance = 50;
            double targetX = centerX + Math.cos(angle) * distance;
            double targetY = centerY + Math.sin(angle) * distance;

            TranslateTransition tt = new TranslateTransition(Duration.millis(400), particle);
            tt.setToX(targetX - centerX);
            tt.setToY(targetY - centerY);

            FadeTransition ft = new FadeTransition(Duration.millis(400), particle);
            ft.setToValue(0);

            ParallelTransition pt = new ParallelTransition(tt, ft);
            pt.setOnFinished(e -> parent.getChildren().remove(particle));
            pt.play();
        }
    }

    /**
     * Get the score value of this brick
     */
    public int getScoreValue() {
        return maxHits * 10;
    }
}