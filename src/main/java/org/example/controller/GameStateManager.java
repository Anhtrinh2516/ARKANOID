package org.example.controller;

/**
 * Quản lý trạng thái game để lưu và tiếp tục
 */
public class GameStateManager {
    public static final GameStateManager INSTANCE = new GameStateManager();

    private boolean hasGameInProgress = false;
    private int savedLevel = 1;
    private int savedScore = 0;
    private int savedLives = 3;
    private double savedBallX = 460;
    private double savedBallY = 500;
    private double savedBallDx = 3.0;
    private double savedBallDy = -3.0;
    private double savedPaddleX = 410;

    private GameStateManager() {}

    public void saveGameState(int level, int score, int lives,
                              double ballX, double ballY,
                              double ballDx, double ballDy,
                              double paddleX) {
        this.hasGameInProgress = true;
        this.savedLevel = level;
        this.savedScore = score;
        this.savedLives = lives;
        this.savedBallX = ballX;
        this.savedBallY = ballY;
        this.savedBallDx = ballDx;
        this.savedBallDy = ballDy;
        this.savedPaddleX = paddleX;
        System.out.println("Game saved: Level " + level + ", Score " + score);
    }

    public void clearGameState() {
        this.hasGameInProgress = false;
        this.savedLevel = 1;
        this.savedScore = 0;
        this.savedLives = 3;
        System.out.println("Game state cleared");
    }

    public boolean hasGameInProgress() {
        return hasGameInProgress;
    }

    public int getSavedLevel() { return savedLevel; }
    public int getSavedScore() { return savedScore; }
    public int getSavedLives() { return savedLives; }
    public double getSavedBallX() { return savedBallX; }
    public double getSavedBallY() { return savedBallY; }
    public double getSavedBallDx() { return savedBallDx; }
    public double getSavedBallDy() { return savedBallDy; }
    public double getSavedPaddleX() { return savedPaddleX; }
}