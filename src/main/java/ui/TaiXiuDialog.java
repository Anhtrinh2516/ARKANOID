package ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Random;

public class TaiXiuDialog {
    
    private final Stage dialog;
    private final int score;
    private final int money;
    private final Random rand = new Random();
    
    private Label resultLabel;
    private Label numberLabel;
    private Button taiButton;
    private Button xiuButton;
    
    private boolean played = false;
    private int finalScore;
    private int finalMoney;
    
    public TaiXiuDialog(int score, int money) {
        this.score = score;
        this.money = money;
        this.finalScore = score;
        this.finalMoney = money;
        
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setTitle("Tài Xỉu - Penaldo");
        
        VBox root = makeContent();
        
        Scene scene = new Scene(root, 500, 500);
        scene.setFill(Color.TRANSPARENT);
        dialog.setScene(scene);
        
        dialog.setOnShown(e -> {
            double x = dialog.getOwner() != null 
                ? dialog.getOwner().getX() + (1148 - 500) / 2 
                : (javafx.stage.Screen.getPrimary().getVisualBounds().getWidth() - 500) / 2;
            double y = dialog.getOwner() != null
                ? dialog.getOwner().getY() + (708 - 500) / 2
                : (javafx.stage.Screen.getPrimary().getVisualBounds().getHeight() - 500) / 2;
            dialog.setX(x);
            dialog.setY(y);
        });
    }
    
    private VBox makeContent() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1a1a2e 0%, #16213e 100%);" +
                     "-fx-background-radius: 15;" +
                     "-fx-border-color: #00d4ff;" +
                     "-fx-border-width: 3;" +
                     "-fx-border-radius: 15;" +
                     "-fx-effect: dropshadow(gaussian, rgba(0, 212, 255, 0.8), 20, 0.5, 0, 0);");
        
        Label title = new Label("🎲 LUCK OR LOSS PENALDO 🎲");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#FFD700"));
        title.setEffect(makeStroke(Color.BLACK));
        
        Label info = new Label("Đoán số từ 1-72: TRÊN hay DƯỚI 36?");
        info.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        info.setTextFill(Color.WHITE);
        info.setEffect(makeStroke(Color.BLACK));
        
        Label stake = new Label(String.format("💰 Đặt cược: %d điểm + %d xu", score, money));
        stake.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        stake.setTextFill(Color.web("#00FF00"));
        stake.setEffect(makeStroke(Color.BLACK));
        
        numberLabel = new Label("❓");
        numberLabel.setFont(Font.font("Arial", FontWeight.BOLD, 80));
        numberLabel.setTextFill(Color.web("#FF00FF"));
        numberLabel.setEffect(makeStroke(Color.BLACK));
        
        resultLabel = new Label("");
        resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        resultLabel.setTextFill(Color.YELLOW);
        resultLabel.setWrapText(true);
        resultLabel.setMaxWidth(450);
        resultLabel.setAlignment(Pos.CENTER);
        resultLabel.setEffect(makeStroke(Color.BLACK));
        
        HBox btns = new HBox(20);
        btns.setAlignment(Pos.CENTER);
        
        taiButton = makeBtn(" OVER 36.5 ", "#00FF00");
        xiuButton = makeBtn(" UNDER 36.5 ", "#FF0000");
        
        taiButton.setOnAction(e -> play(true));
        xiuButton.setOnAction(e -> play(false));
        
        btns.getChildren().addAll(xiuButton, taiButton);
        
        root.getChildren().addAll(title, info, stake, numberLabel, btns, resultLabel);
        
        return root;
    }
    
    private Button makeBtn(String text, String color) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        btn.setTextFill(Color.WHITE);
        btn.setStyle(String.format(
            "-fx-background-color: %s;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 15 30;" +
            "-fx-cursor: hand;", color
        ));
        btn.setEffect(makeStroke(Color.BLACK));
        
        btn.setOnMouseEntered(e -> btn.setStyle(String.format(
            "-fx-background-color: derive(%s, 20%%);" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 15 30;" +
            "-fx-cursor: hand;" +
            "-fx-scale-x: 1.05;" +
            "-fx-scale-y: 1.05;", color
        )));
        
        btn.setOnMouseExited(e -> btn.setStyle(String.format(
            "-fx-background-color: %s;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 15 30;" +
            "-fx-cursor: hand;", color
        )));
        
        return btn;
    }
    
    private DropShadow makeStroke(Color color) {
        DropShadow shadow = new DropShadow();
        shadow.setColor(color);
        shadow.setRadius(2);
        shadow.setSpread(1.0);
        shadow.setOffsetX(0);
        shadow.setOffsetY(0);
        return shadow;
    }
    
    private void play(boolean over) {
        if (played) return;
        played = true;
        
        taiButton.setDisable(true);
        xiuButton.setDisable(true);
        
        int num = rand.nextInt(72) + 1;
        
        numberLabel.setText(String.valueOf(num));
        
        boolean win = (over && num > 36) || (!over && num <= 36);
        
        if (win) {
            finalScore = score * 2;
            finalMoney = money * 2;
            
            resultLabel.setText(String.format("🎉 YOU WIN! Số là %d\n✅ +%d điểm, +%d xu", 
                num, score, money));
            resultLabel.setTextFill(Color.web("#00FF00"));
        } else {
            finalScore = 0;
            finalMoney = 0;
            
            resultLabel.setText(String.format("💀 LOSER! Số là %d\n❌ Mất %d điểm, %d xu", 
                num, score, money));
            resultLabel.setTextFill(Color.web("#FF0000"));
        }
        
        Button ok = makeBtn("✅ OK", "#FFD700");
        ok.setOnAction(e -> dialog.close());
        
        VBox root = (VBox) dialog.getScene().getRoot();
        root.getChildren().add(ok);
    }
    
    public int[] showAndWait() {
        dialog.showAndWait();
        return new int[]{finalScore, finalMoney};
    }
    
    public static boolean showAskDialog() {
        Stage ask = new Stage();
        ask.initModality(Modality.APPLICATION_MODAL);
        ask.initStyle(StageStyle.UNDECORATED);
        
        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1a1a2e 0%, #16213e 100%);" +
                     "-fx-background-radius: 15;" +
                     "-fx-border-color: #FFD700;" +
                     "-fx-border-width: 3;" +
                     "-fx-border-radius: 15;");
        
        Label title = new Label("⚽ PENALDO CASINO ⚽");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#FFD700"));
        
        Label question = new Label(" DO U WANT SOME X2?");
        question.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        question.setTextFill(Color.WHITE);
        question.setWrapText(true);
        question.setMaxWidth(400);
        question.setAlignment(Pos.CENTER);

        Label hint = new Label("( PLAY TAIXIU - WIN x2, LOSS LOSE ALL)");
        hint.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        hint.setTextFill(Color.web("#AAAAAA"));
        
        HBox btns = new HBox(20);
        btns.setAlignment(Pos.CENTER);
        
        Button yes = new Button("✅ YES");
        Button no = new Button("❌ NO");
        
        for (Button btn : new Button[]{yes, no}) {
            btn.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            btn.setTextFill(Color.WHITE);
            btn.setStyle("-fx-background-color: #00d4ff;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 15 40;" +
                        "-fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: derive(#00d4ff, 20%);" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 15 40;" +
                "-fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #00d4ff;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 15 40;" +
                "-fx-cursor: hand;"));
        }
        
        yes.setOnAction(e -> ask.close());
        no.setOnAction(e -> ask.close());
        
        btns.getChildren().addAll(no, yes);
        root.getChildren().addAll(title, question, hint, btns);
        
        Scene scene = new Scene(root, 500, 300);
        scene.setFill(Color.TRANSPARENT);
        ask.setScene(scene);
        
        ask.showAndWait();
        return true;
    }
}
