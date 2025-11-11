package org.example.event.penaldo.logic;

import org.example.event.base.EventLevelLoader;
import org.example.logic.Brick;
import java.util.ArrayList;
import java.util.List;

public class PenaldoLevelLoader extends EventLevelLoader {
    
    private static final PenaldoLevelLoader INSTANCE = new PenaldoLevelLoader();

    public static List<Brick> loadPenaldoLevel(int levelIndex, double paneWidth) {
        return INSTANCE.loadLevel(levelIndex, paneWidth);
    }

    @Override
    protected String getEventFolder() {
        return "penaldo";
    }
    
    @Override
    protected String getFilePrefix() {
        return "penaldo";
    }

    @Override
    protected Brick createBrickFromChar(char ch, int col, int row) {
        if (ch == 'G' || ch == 'g') {
            return new GoalPostBrick(createBrickRect(col, row));
        } else if (ch == 'K' || ch == 'k') {
            return new GoalkeeperBrick(createBrickRect(col, row), 0, 888);
        } else if (ch == '1') {
            return new FootballBrick(createBrickRect(col, row));
        }
        return null;
    }

    @Override
    protected List<Brick> createFallbackLevel(int levelIndex) {
        List<Brick> bricks = new ArrayList<>();
        
        for (int col = 0; col < cols; col++) {
            if (col == 3 || col == 7) {
                bricks.add(new GoalPostBrick(createBrickRect(col, 0)));
            } else if (col >= 4 && col <= 6) {
                bricks.add(new FootballBrick(createBrickRect(col, 0)));
            }
        }
        
        for (int row = 1; row <= 2; row++) {
            bricks.add(new GoalPostBrick(createBrickRect(3, row)));
            bricks.add(new GoalPostBrick(createBrickRect(7, row)));
        }
        
        bricks.add(new GoalPostBrick(createBrickRect(3, 3)));
        bricks.add(new GoalkeeperBrick(createBrickRect(5, 3), 0, 888));
        bricks.add(new GoalPostBrick(createBrickRect(7, 3)));
        
        return bricks;
    }
}
