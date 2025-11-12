package ui;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import java.util.Random;

public class CongratsAnimation {

    private static final Random rng = new Random();

    public static void playConfetti(Pane p) {
        for (int i = 0; i < 50; i++) {
            Circle c = new Circle(4);

            Color[] colors = {
                    Color.web("#fbbf24"),
                    Color.web("#00d4ff"),
                    Color.web("#ff00ff"),
                    Color.web("#22c55e"),
                    Color.web("#ef4444"),
                    Color.web("#8b5cf6")
            };
            c.setFill(colors[rng.nextInt(colors.length)]);

            double x = rng.nextDouble() * p.getWidth();
            double y = -20;
            c.setCenterX(x);
            c.setCenterY(y);

            p.getChildren().add(c);

            double endY = p.getHeight() + 20;
            double dur = 2 + rng.nextDouble() * 2;

            TranslateTransition fall = new TranslateTransition(Duration.seconds(dur), c);
            fall.setToY(endY);

            RotateTransition rot = new RotateTransition(Duration.seconds(dur), c);
            rot.setByAngle(360 + rng.nextInt(360));

            TranslateTransition sway = new TranslateTransition(Duration.seconds(1), c);
            sway.setByX(rng.nextDouble() * 100 - 50);
            sway.setCycleCount(TranslateTransition.INDEFINITE);
            sway.setAutoReverse(true);

            FadeTransition fade = new FadeTransition(Duration.seconds(dur), c);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);

            ParallelTransition par = new ParallelTransition(fall, rot, sway, fade);
            par.setDelay(Duration.millis(rng.nextInt(500)));
            par.setOnFinished(e -> p.getChildren().remove(c));
            par.play();
        }
    }

    public static void playText(Pane p) {
        Label txt = new Label("CONGRATULATIONS!");
        txt.setFont(Font.font("System", FontWeight.BOLD, 36));
        txt.setTextFill(Color.web("#fbbf24"));
        txt.setStyle("-fx-effect: dropshadow(gaussian, rgba(251, 191, 36, 0.8), 20, 0, 0, 0);");

        txt.setLayoutX(p.getWidth() / 2 - 250);
        txt.setLayoutY(p.getHeight() / 2 - 100);

        p.getChildren().add(txt);

        ScaleTransition scale = new ScaleTransition(Duration.millis(600), txt);
        scale.setFromX(0.1);
        scale.setFromY(0.1);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.setInterpolator(Interpolator.EASE_OUT);

        RotateTransition rot = new RotateTransition(Duration.millis(100), txt);
        rot.setByAngle(15);
        rot.setCycleCount(6);
        rot.setAutoReverse(true);

        FadeTransition fade = new FadeTransition(Duration.millis(500), txt);
        fade.setDelay(Duration.seconds(2));
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> p.getChildren().remove(txt));

        SequentialTransition seq = new SequentialTransition(
                scale,
                new PauseTransition(Duration.millis(500)),
                rot,
                fade
        );
        seq.play();
    }

    public static void playStars(Pane p) {
        for (int i = 0; i < 20; i++) {
            Label star = new Label("*");
            star.setFont(Font.font(20 + rng.nextInt(20)));

            double x = rng.nextDouble() * p.getWidth();
            double y = rng.nextDouble() * p.getHeight();
            star.setLayoutX(x);
            star.setLayoutY(y);

            p.getChildren().add(star);

            FadeTransition fade = new FadeTransition(Duration.millis(500 + rng.nextInt(500)), star);
            fade.setFromValue(0.3);
            fade.setToValue(1.0);
            fade.setCycleCount(FadeTransition.INDEFINITE);
            fade.setAutoReverse(true);
            fade.setDelay(Duration.millis(rng.nextInt(1000)));

            ScaleTransition scale = new ScaleTransition(Duration.seconds(1), star);
            scale.setFromX(0.8);
            scale.setFromY(0.8);
            scale.setToX(1.2);
            scale.setToY(1.2);
            scale.setCycleCount(ScaleTransition.INDEFINITE);
            scale.setAutoReverse(true);

            ParallelTransition par = new ParallelTransition(fade, scale);
            par.setDelay(Duration.millis(rng.nextInt(2000)));
            par.play();

            Timeline remove = new Timeline(new javafx.animation.KeyFrame(
                    Duration.seconds(3),
                    e -> p.getChildren().remove(star)
            ));
            remove.play();
        }
    }

    public static void playAll(Pane p) {
        playConfetti(p);
        playText(p);
        playStars(p);
    }
}
