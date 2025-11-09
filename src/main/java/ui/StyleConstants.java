package ui;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

/**
 * Unified style constants for consistent UI across the application
 */
public class StyleConstants {

    // ===== GRADIENT BORDER STYLE =====
    public static final String GRADIENT_BORDER_STYLE = "-fx-background-color: linear-gradient(to bottom, rgba(15, 23, 42, 0.98), rgba(10, 14, 39, 0.98));"
            +
            "-fx-background-radius: 20;" +
            "-fx-border-width: 3;" +
            "-fx-border-radius: 20;" +
            "-fx-border-color: linear-gradient(to right, #00d4ff 0%, #8b5cf6 50%, #ff00ff 100%);";

    // ===== COLORS =====
    public static final String COLOR_CYAN = "#00d4ff";
    public static final String COLOR_PURPLE = "#8b5cf6";
    public static final String COLOR_MAGENTA = "#ff00ff";
    public static final String COLOR_GREEN = "#22c55e";
    public static final String COLOR_GOLD = "#fbbf24";
    public static final String COLOR_RED = "#ef4444";

    // ===== GLOW EFFECTS =====
    public static DropShadow createGlowEffect() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.rgb(0, 212, 255, 0.6));
        glow.setRadius(35);
        glow.setSpread(0.3);
        return glow;
    }

    public static DropShadow createTitleGlow() {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web(COLOR_CYAN));
        glow.setRadius(12);
        return glow;
    }

    public static DropShadow createButtonGlow(Color color, double radius) {
        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setRadius(radius);
        return glow;
    }

    // ===== BUTTON STYLES =====
    public static final String BUTTON_PRIMARY_STYLE = "-fx-background-color: linear-gradient(to bottom, #22c55e, #16a34a);"
            +
            "-fx-text-fill: white;" +
            "-fx-padding: 10 40;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;";

    public static final String BUTTON_PRIMARY_HOVER_STYLE = "-fx-background-color: linear-gradient(to bottom, #16a34a, #15803d);"
            +
            "-fx-text-fill: white;" +
            "-fx-padding: 10 40;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;" +
            "-fx-scale-x: 1.05;" +
            "-fx-scale-y: 1.05;";

    public static final String BUTTON_DANGER_STYLE = "-fx-background-color: linear-gradient(to bottom, #ef4444, #dc2626);"
            +
            "-fx-text-fill: white;" +
            "-fx-padding: 10 40;" +
            "-fx-background-radius: 10;" +
            "-fx-cursor: hand;";

    // ===== SECTION STYLES =====
    public static final String SECTION_STYLE = "-fx-background-color: rgba(30, 41, 59, 0.6);" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 12;" +
            "-fx-border-color: #475569;" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 10;";

    // ===== GAME WINDOW SIZE =====
    public static final double GAME_WIDTH = 920.0;
    public static final double GAME_HEIGHT = 668.0;

    // ===== DIALOG SIZES =====
    public static final double DIALOG_PADDING = 25.0;
    public static final double DIALOG_SPACING = 12.0;

    private StyleConstants() {
        // Private constructor to prevent instantiation
    }
}