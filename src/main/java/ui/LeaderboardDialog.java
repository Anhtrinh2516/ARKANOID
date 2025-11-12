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
import controller.LeaderboardManager;

import java.util.List;

public class LeaderboardDialog {

    public static void show() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setTitle("Leaderboard");

        VBox root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(30, 40, 30, 40));
        root.setPrefWidth(700);
        root.setPrefHeight(600);
        root.setStyle(StyleConstants.GRADIENT_BORDER_STYLE);
        root.setEffect(StyleConstants.createGlowEffect());

        Label titleLbl = new Label("🏆 LEADERBOARD");
        titleLbl.setFont(Font.font("System", FontWeight.BOLD, 36));
        titleLbl.setTextFill(Color.web(StyleConstants.COLOR_GOLD));
        DropShadow titleGlow = new DropShadow();
        titleGlow.setColor(Color.web(StyleConstants.COLOR_GOLD));
        titleGlow.setRadius(15);
        titleLbl.setEffect(titleGlow);

        Label subLbl = new Label("Top 10 High Scores");
        subLbl.setFont(Font.font("System", FontWeight.NORMAL, 14));
        subLbl.setTextFill(Color.web("#94a3b8"));

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setPrefHeight(400);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");

        VBox box = new VBox(8);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: transparent;");

        HBox header = makeHeader();
        box.getChildren().add(header);

        Region sep = new Region();
        sep.setMinHeight(2);
        sep.setStyle("-fx-background-color: #475569;");
        box.getChildren().add(sep);

        List<LeaderboardManager.Entry> list = LeaderboardManager.INSTANCE.getList();

        if (list.isEmpty()) {
            Label emptyLbl = new Label("🎮 No scores yet!\n\nBe the first to set a record!");
            emptyLbl.setFont(Font.font("System", FontWeight.BOLD, 18));
            emptyLbl.setTextFill(Color.web("#64748b"));
            emptyLbl.setAlignment(Pos.CENTER);
            emptyLbl.setMaxWidth(Double.MAX_VALUE);
            emptyLbl.setWrapText(true);
            VBox.setMargin(emptyLbl, new Insets(80, 0, 0, 0));
            box.getChildren().add(emptyLbl);
        } else {
            for (int i = 0; i < list.size(); i++) {
                LeaderboardManager.Entry e = list.get(i);
                HBox row = makeRow(i + 1, e);
                box.getChildren().add(row);

                if (i < list.size() - 1) {
                    Region space = new Region();
                    space.setMinHeight(5);
                    box.getChildren().add(space);
                }
            }
        }

        scroll.setContent(box);

        Button closeBtn = new Button("CLOSE");
        closeBtn.setFont(Font.font("System", FontWeight.BOLD, 16));
        closeBtn.setPrefWidth(200);
        closeBtn.setStyle(StyleConstants.BUTTON_PRIMARY_STYLE);
        closeBtn.setEffect(StyleConstants.createButtonGlow(Color.rgb(34, 197, 94, 0.6), 12));

        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle(StyleConstants.BUTTON_PRIMARY_HOVER_STYLE));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle(StyleConstants.BUTTON_PRIMARY_STYLE));
        closeBtn.setOnAction(e -> dialog.close());

        root.getChildren().addAll(titleLbl, subLbl, scroll, closeBtn);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private static HBox makeHeader() {
        HBox h = new HBox(15);
        h.setAlignment(Pos.CENTER_LEFT);
        h.setPadding(new Insets(10, 15, 10, 15));
        h.setStyle("-fx-background-color: rgba(100, 116, 139, 0.3); -fx-background-radius: 8;");

        Label rankLbl = new Label("RANK");
        rankLbl.setFont(Font.font("System", FontWeight.BOLD, 12));
        rankLbl.setTextFill(Color.web("#cbd5e1"));
        rankLbl.setMinWidth(60);

        Label nameLbl = new Label("PLAYER");
        nameLbl.setFont(Font.font("System", FontWeight.BOLD, 12));
        nameLbl.setTextFill(Color.web("#cbd5e1"));
        nameLbl.setMinWidth(200);
        HBox.setHgrow(nameLbl, Priority.ALWAYS);

        Label lvlLbl = new Label("LEVEL");
        lvlLbl.setFont(Font.font("System", FontWeight.BOLD, 12));
        lvlLbl.setTextFill(Color.web("#cbd5e1"));
        lvlLbl.setMinWidth(80);
        lvlLbl.setAlignment(Pos.CENTER);

        Label scoreLbl = new Label("SCORE");
        scoreLbl.setFont(Font.font("System", FontWeight.BOLD, 12));
        scoreLbl.setTextFill(Color.web("#cbd5e1"));
        scoreLbl.setMinWidth(100);
        scoreLbl.setAlignment(Pos.CENTER_RIGHT);

        h.getChildren().addAll(rankLbl, nameLbl, lvlLbl, scoreLbl);
        return h;
    }

    private static HBox makeRow(int rank, LeaderboardManager.Entry e) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(15, 20, 15, 20));

        String bg;
        String txt;
        String emoji;
        String glow = "";

        if (rank == 1) {
            bg = "linear-gradient(to right, rgba(251, 191, 36, 0.25), rgba(245, 158, 11, 0.15))";
            txt = "#fbbf24";
            emoji = "🥇";
            glow = "-fx-effect: dropshadow(gaussian, rgba(251, 191, 36, 0.5), 10, 0, 0, 0);";
        } else if (rank == 2) {
            bg = "linear-gradient(to right, rgba(203, 213, 225, 0.25), rgba(148, 163, 184, 0.15))";
            txt = "#cbd5e1";
            emoji = "🥈";
            glow = "-fx-effect: dropshadow(gaussian, rgba(203, 213, 225, 0.4), 8, 0, 0, 0);";
        } else if (rank == 3) {
            bg = "linear-gradient(to right, rgba(205, 127, 50, 0.25), rgba(184, 115, 51, 0.15))";
            txt = "#cd7f32";
            emoji = "🥉";
            glow = "-fx-effect: dropshadow(gaussian, rgba(205, 127, 50, 0.4), 8, 0, 0, 0);";
        } else {
            bg = "rgba(30, 41, 59, 0.5)";
            txt = "#94a3b8";
            emoji = "";
            glow = "";
        }

        row.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 10; -fx-border-color: " + txt + "; -fx-border-width: 2; -fx-border-radius: 10;" + glow);

        VBox rankBox = new VBox(2);
        rankBox.setAlignment(Pos.CENTER);
        rankBox.setMinWidth(70);

        if (!emoji.isEmpty()) {
            Label medal = new Label(emoji);
            medal.setFont(Font.font(24));
            rankBox.getChildren().add(medal);
        }

        Label rankLbl = new Label("#" + rank);
        rankLbl.setFont(Font.font("System", FontWeight.BOLD, 18));
        rankLbl.setTextFill(Color.web(txt));
        rankBox.getChildren().add(rankLbl);

        HBox nameBox = new HBox(8);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        nameBox.setMinWidth(200);
        HBox.setHgrow(nameBox, Priority.ALWAYS);

        Label icon = new Label("👤");
        icon.setFont(Font.font(16));

        Label nameLbl = new Label(e.name);
        nameLbl.setFont(Font.font("System", FontWeight.BOLD, 18));
        nameLbl.setTextFill(Color.web("#e2e8f0"));
        nameBox.getChildren().addAll(icon, nameLbl);

        VBox lvlBox = new VBox(2);
        lvlBox.setAlignment(Pos.CENTER);
        lvlBox.setMinWidth(90);
        lvlBox.setStyle("-fx-background-color: rgba(0, 212, 255, 0.15); -fx-background-radius: 6; -fx-padding: 6 12; -fx-border-color: #00d4ff; -fx-border-width: 1.5; -fx-border-radius: 6;");

        Label lvlTitle = new Label("LEVEL");
        lvlTitle.setFont(Font.font("System", FontWeight.BOLD, 9));
        lvlTitle.setTextFill(Color.web("#00d4ff"));

        Label lvlVal = new Label(String.valueOf(e.lvl));
        lvlVal.setFont(Font.font("System", FontWeight.BOLD, 18));
        lvlVal.setTextFill(Color.web("#00d4ff"));

        lvlBox.getChildren().addAll(lvlTitle, lvlVal);

        VBox scoreBox = new VBox(2);
        scoreBox.setAlignment(Pos.CENTER_RIGHT);
        scoreBox.setMinWidth(120);

        Label trophy = new Label("🏆");
        trophy.setFont(Font.font(16));
        trophy.setAlignment(Pos.CENTER_RIGHT);

        Label scoreLbl = new Label(String.format("%,d", e.score));
        scoreLbl.setFont(Font.font("System", FontWeight.BOLD, 22));
        scoreLbl.setTextFill(Color.web(txt));
        scoreLbl.setAlignment(Pos.CENTER_RIGHT);

        scoreBox.getChildren().addAll(trophy, scoreLbl);

        row.getChildren().addAll(rankBox, nameBox, lvlBox, scoreBox);

        final String origStyle = row.getStyle();
        row.setOnMouseEntered(ev -> {
            row.setStyle("-fx-background-color: " + bg + "; -fx-background-radius: 10; -fx-border-color: " + txt + "; -fx-border-width: 3; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, " + txt + ", 15, 0.7, 0, 0); -fx-scale-x: 1.03; -fx-scale-y: 1.03;");
        });

        row.setOnMouseExited(ev -> {
            row.setStyle(origStyle);
        });

        return row;
    }
}
