package controller;

public class GameState {
    public static final GameState INSTANCE = new GameState();

    private int coins = 100;
    private int paddleWidthBonus = 0;
    private double ballSpeedScale = 1;

    // Inventory for shop items
    private int wideItemCount = 0;
    private int lifeItemCount = 0;
    private int slowItemCount = 0;

    private GameState() {}

    public int getCoins() {
        return coins;
    }

    public void addCoins(int delta) {
        coins += delta;
        if (coins < 0) coins = 0;
    }

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

    // === INVENTORY MANAGEMENT ===

    public int getWideItemCount() {
        return wideItemCount;
    }

    public void addWideItem(int count) {
        wideItemCount += count;
    }

    public boolean useWideItem() {
        if (wideItemCount > 0) {
            wideItemCount--;
            return true;
        }
        return false;
    }

    public int getLifeItemCount() {
        return lifeItemCount;
    }

    public void addLifeItem(int count) {
        lifeItemCount += count;
    }

    public boolean useLifeItem() {
        if (lifeItemCount > 0) {
            lifeItemCount--;
            return true;
        }
        return false;
    }

    public int getSlowItemCount() {
        return slowItemCount;
    }

    public void addSlowItem(int count) {
        slowItemCount += count;
    }

    public boolean useSlowItem() {
        if (slowItemCount > 0) {
            slowItemCount--;
            return true;
        }
        return false;
    }
}