package controller;

public class GameStateManager {
    public static final GameStateManager INSTANCE = new GameStateManager();

    private boolean hasGame = false;
    private int lvl = 1;
    private int score = 0;
    private int lives = 3;
    private double ballX = 460;
    private double ballY = 500;
    private double ballDx = 3.0;
    private double ballDy = -3.0;
    private double padX = 410;

    private GameStateManager() {}

    public void save(int l, int s, int life, double bx, double by, double bdx, double bdy, double px) {
        hasGame = true;
        lvl = l;
        score = s;
        lives = life;
        ballX = bx;
        ballY = by;
        ballDx = bdx;
        ballDy = bdy;
        padX = px;
    }

    public void clear() {
        hasGame = false;
        lvl = 1;
        score = 0;
        lives = 3;
    }

    public boolean hasGameInProgress() { return hasGame; }
    public int getSavedLevel() { return lvl; }
    public int getSavedScore() { return score; }
    public int getSavedLives() { return lives; }
    public double getSavedBallX() { return ballX; }
    public double getSavedBallY() { return ballY; }
    public double getSavedBallDx() { return ballDx; }
    public double getSavedBallDy() { return ballDy; }
    public double getSavedPaddleX() { return padX; }
}