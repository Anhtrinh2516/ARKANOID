package org.example.controller;


public class GameState {
    public static final GameState INSTANCE = new GameState();

    private int coins = 0;
    private int paddleWidthBonus = 0;
    private double ballSpeedScale = 1;

    private GameState() {}

    public int getCoins() { return coins; }
    public void addCoins(int delta) { coins += delta; if (coins < 0) coins = 0; }

    public int getPaddleWidthBonus() {
        return paddleWidthBonus;
    }
    public void addPaddleWidthBonus(int bonus) {
        paddleWidthBonus += bonus;
    }

    public double getBallSpeedScale() {
        return ballSpeedScale;
    }
    public void setBallSpeedScale(double s) {
        ballSpeedScale = s;
    }
}
