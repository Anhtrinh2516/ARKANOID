package org.example.logic;

/**
 * Lớp để quản lý power-up đang hoạt động với timer
 */
public class ActivePowerUp {
    private final PowerUpType type;
    private final long startTime;
    private final long duration; // milliseconds
    private boolean isActive;

    public ActivePowerUp(PowerUpType type, long durationMs) {
        this.type = type;
        this.startTime = System.currentTimeMillis();
        this.duration = durationMs;
        this.isActive = true;
    }

    public PowerUpType getType() {
        return type;
    }

    public long getTimeRemaining() {
        long elapsed = System.currentTimeMillis() - startTime;
        long remaining = duration - elapsed;
        return Math.max(0, remaining);
    }

    public boolean isExpired() {
        return getTimeRemaining() <= 0;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }

    public int getTimeRemainingSeconds() {
        return (int) (getTimeRemaining() / 1000);
    }
}
