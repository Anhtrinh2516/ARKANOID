package event.treasurehunter.logic;

import event.base.EventLevelLoader;
import logic.Brick;
import java.util.ArrayList;
import java.util.List;

// đọc file level
public class TreasureHunterLevelLoader extends EventLevelLoader {
    
    private static final TreasureHunterLevelLoader INSTANCE = new TreasureHunterLevelLoader();

    public static List<Brick> loadTreasureLevel(int levelIndex, double paneWidth) {
        return INSTANCE.loadLevel(levelIndex, paneWidth);
    }

    @Override
    protected String getEventFolder() {
        return "treasure_hunter";
    }
    
    @Override
    protected String getFilePrefix() {
        return "treasure"; 
    }

    @Override
    protected Brick createBrickFromChar(char ch, int col, int row) {
        if (ch == 'T' || ch == 't') {
            return new TreasureBrick(createBrickRect(col, row));
        } else if (ch == 'S' || ch == 's') {
            return new StoneBrick(createBrickRect(col, row));
        } else if (ch == 'X' || ch == 'x') {
            return createWallBrick(col, row);
        }
        return null;
    }

    @Override
    protected List<Brick> createFallbackLevel(int levelIndex) {
        List<Brick> bricks = new ArrayList<>();
        int rows = 3 + levelIndex;
        
        for (int row = 0; row < Math.min(rows, 8); row++) {
            for (int col = 0; col < cols; col++) {
                if ((row + col) % 2 == 0) {
                    bricks.add(new TreasureBrick(createBrickRect(col, row)));
                } else {
                    bricks.add(new StoneBrick(createBrickRect(col, row)));
                }
            }
        }
        return bricks;
    }
}
