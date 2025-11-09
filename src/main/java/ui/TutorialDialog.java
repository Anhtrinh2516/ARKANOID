package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TutorialDialog {

        public static void show() {
                Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.setTitle("How to Play");

                // Main container - Fit exactly within game window
                VBox root = new VBox(12);
                root.setAlignment(Pos.TOP_CENTER);
                root.setPadding(new Insets(20, 25, 20, 25));
                root.setPrefWidth(900);
                root.setPrefHeight(648);
                root.setMaxWidth(900);
                root.setMaxHeight(648);
                root.setMinWidth(900);
                root.setMinHeight(648);

                // Apply gradient border style with proper CSS for gradient border
                root.setStyle(
                                "-fx-background-color: linear-gradient(to bottom, rgba(15, 23, 42, 0.98), rgba(10, 14, 39, 0.98));"
                                                +
                                                "-fx-background-radius: 20;" +
                                                "-fx-border-width: 3;" +
                                                "-fx-border-radius: 20;" +
                                                "-fx-border-style: solid;" +
                                                "-fx-border-image-source: linear-gradient(to right, #00d4ff 0%, #8b5cf6 50%, #ff00ff 100%);"
                                                +
                                                "-fx-border-image-slice: 1;");

                // Glow effect
                root.setEffect(StyleConstants.createGlowEffect());

                // Title
                Label title = new Label("🎮 HOW TO PLAY");
                title.setFont(Font.font("System", FontWeight.BOLD, 28));
                title.setTextFill(Color.web("#00d4ff"));
                DropShadow titleGlow = new DropShadow();
                titleGlow.setColor(Color.web("#00d4ff"));
                titleGlow.setRadius(12);
                title.setEffect(titleGlow);

                // Scrollable content
                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setFitToWidth(true);
                scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
                scrollPane.setPrefHeight(530);
                scrollPane.setMaxHeight(530);
                scrollPane.setStyle(
                                "-fx-background: transparent;" +
                                                "-fx-background-color: transparent;" +
                                                "-fx-border-color: transparent;" +
                                                "-fx-focus-color: transparent;" +
                                                "-fx-faint-focus-color: transparent;");

                VBox content = new VBox(12);
                content.setPadding(new Insets(5, 15, 5, 5));
                content.setStyle("-fx-background-color: transparent;");

                // ===== CONTROLS =====
                VBox controlsSection = createSection(
                                "⌨️ CONTROLS",
                                createControl("← / A", "Move paddle left"),
                                createControl("→ / D", "Move paddle right"),
                                createControl("SPACE", "Launch ball"),
                                createControl("ESC", "Pause / Menu"));

                // ===== CONSUMABLE ITEMS =====
                VBox itemsSection = createSection(
                                "🎁 CONSUMABLE ITEMS (Buy from Shop)",
                                createControl("Press 1", "🛸 Wide Paddle - Expand paddle for 10s"),
                                createControl("Press 2", "💖 Extra Life - Gain +1 life instantly"),
                                createControl("Press 3", "🐌 Slow Ball - Slow ball speed for 10s"));

                // ===== POWER-UPS =====
                VBox powerUpsSection = createSection(
                                "⚡ POWER-UPS (Drop from bricks)",
                                createColorInfo("🟡 Gold", "Coin - Earn currency"),
                                createColorInfo("❤️ Red", "Extra Life"),
                                createColorInfo("🔵 Blue", "Expand Paddle (temporary)"),
                                createColorInfo("🟢 Green", "Slow Ball (temporary)"));

                // ===== TIPS =====
                VBox tipsSection = createSection(
                                "💡 TIPS",
                                createTip("• Break bricks to earn coins and power-ups"),
                                createTip("• Buy items from the shop to help you in tough levels"),
                                createTip("• Use items strategically - they don't carry over between deaths!"),
                                createTip("• Permanent upgrades from the shop last forever"));

                content.getChildren().addAll(
                                controlsSection,
                                createSpacer(8),
                                itemsSection,
                                createSpacer(8),
                                powerUpsSection,
                                createSpacer(8),
                                tipsSection);

                scrollPane.setContent(content);

                // Close button
                Button closeButton = new Button("GOT IT!");
                closeButton.setFont(Font.font("System", FontWeight.BOLD, 16));
                closeButton.setPrefWidth(180);
                closeButton.setStyle(
                                "-fx-background-color: linear-gradient(to bottom, #22c55e, #16a34a);" +
                                                "-fx-text-fill: white;" +
                                                "-fx-padding: 10 40;" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-cursor: hand;");

                DropShadow buttonGlow = new DropShadow();
                buttonGlow.setColor(Color.rgb(34, 197, 94, 0.6));
                buttonGlow.setRadius(12);
                closeButton.setEffect(buttonGlow);

                closeButton.setOnAction(e -> dialog.close());

                // Hover effect
                closeButton.setOnMouseEntered(e -> {
                        closeButton.setStyle(
                                        "-fx-background-color: linear-gradient(to bottom, #16a34a, #15803d);" +
                                                        "-fx-text-fill: white;" +
                                                        "-fx-padding: 10 40;" +
                                                        "-fx-background-radius: 10;" +
                                                        "-fx-cursor: hand;" +
                                                        "-fx-scale-x: 1.05;" +
                                                        "-fx-scale-y: 1.05;");
                });

                closeButton.setOnMouseExited(e -> {
                        closeButton.setStyle(
                                        "-fx-background-color: linear-gradient(to bottom, #22c55e, #16a34a);" +
                                                        "-fx-text-fill: white;" +
                                                        "-fx-padding: 10 40;" +
                                                        "-fx-background-radius: 10;" +
                                                        "-fx-cursor: hand;");
                });

                root.getChildren().addAll(title, scrollPane, closeButton);

                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);
                dialog.setScene(scene);
                dialog.showAndWait();
        }

        private static VBox createSection(String title, HBox... items) {
                VBox section = new VBox(8);
                section.setStyle(
                                "-fx-background-color: rgba(30, 41, 59, 0.6);" +
                                                "-fx-background-radius: 10;" +
                                                "-fx-padding: 12;" +
                                                "-fx-border-color: #475569;" +
                                                "-fx-border-width: 1.5;" +
                                                "-fx-border-radius: 10;");

                Label titleLabel = new Label(title);
                titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
                titleLabel.setTextFill(Color.web("#00d4ff"));
                titleLabel.setWrapText(true);

                section.getChildren().add(titleLabel);
                for (HBox item : items) {
                        section.getChildren().add(item);
                }

                return section;
        }

        private static HBox createControl(String key, String description) {
                HBox hbox = new HBox(10);
                hbox.setAlignment(Pos.CENTER_LEFT);

                Label keyLabel = new Label(key);
                keyLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
                keyLabel.setTextFill(Color.web("#fbbf24"));
                keyLabel.setStyle(
                                "-fx-background-color: rgba(251, 191, 36, 0.25);" +
                                                "-fx-padding: 5 10;" +
                                                "-fx-background-radius: 5;" +
                                                "-fx-border-color: #fbbf24;" +
                                                "-fx-border-width: 1.5;" +
                                                "-fx-border-radius: 5;" +
                                                "-fx-min-width: 70;");

                Label descLabel = new Label(description);
                descLabel.setFont(Font.font("System", 11));
                descLabel.setTextFill(Color.web("#cbd5e1"));
                descLabel.setWrapText(true);

                hbox.getChildren().addAll(keyLabel, descLabel);
                return hbox;
        }

        private static HBox createColorInfo(String icon, String description) {
                HBox hbox = new HBox(8);
                hbox.setAlignment(Pos.CENTER_LEFT);

                Label iconLabel = new Label(icon);
                iconLabel.setFont(Font.font("System", 13));
                iconLabel.setMinWidth(50);

                Label descLabel = new Label(description);
                descLabel.setFont(Font.font("System", 11));
                descLabel.setTextFill(Color.web("#cbd5e1"));
                descLabel.setWrapText(true);

                hbox.getChildren().addAll(iconLabel, descLabel);
                return hbox;
        }

        private static HBox createTip(String text) {
                HBox hbox = new HBox();
                hbox.setAlignment(Pos.CENTER_LEFT);

                Label label = new Label(text);
                label.setFont(Font.font("System", 11));
                label.setTextFill(Color.web("#94a3b8"));
                label.setWrapText(true);
                label.setMaxWidth(800);

                hbox.getChildren().add(label);
                return hbox;
        }

        private static Region createSpacer(double height) {
                Region spacer = new Region();
                spacer.setMinHeight(height);
                spacer.setMaxHeight(height);
                return spacer;
        }
}