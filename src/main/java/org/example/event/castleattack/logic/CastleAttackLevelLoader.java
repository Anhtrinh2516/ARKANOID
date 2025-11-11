package org.example.event.castleattack.logic;

import org.example.event.base.EventLevelLoader;
import org.example.logic.Brick;

import java.util.ArrayList;
import java.util.List;

// đọc file level
public class CastleAttackLevelLoader extends EventLevelLoader {

    private static final CastleAttackLevelLoader INSTANCE = new CastleAttackLevelLoader();

    public static List<Brick> loadCastleLevel(int levelIndex, double paneWidth) {
        return INSTANCE.loadLevel(levelIndex, paneWidth);
    }

    @Override
    protected String getEventFolder() {
        return "castle";
    }
    
    @Override
    protected String getFilePrefix() {
        return "castle";
    }

    @Override
    protected Brick createBrickFromChar(char ch, int col, int row) {
        if (ch == 'G' || ch == 'g') {
            return new GateBrick(createBrickRect(col, row));
        } else if (ch == 'A' || ch == 'a') {
            return new CanonBrick(createBrickRect(col, row));
        } else if (ch == 'B' || ch == 'b') {
            return new BrickWall(createBrickRect(col, row));
        } else {
            return null;
        }
    }

    @Override
    protected List<Brick> createFallbackLevel(int levelIndex) {
        List<Brick> bricks = new ArrayList<>();
        
        bricks.add(new GateBrick(createBrickRect(0, 0)));
        bricks.add(new GateBrick(createBrickRect(11, 0)));
        
        for (int row = 1; row <= 2; row++) {
            for (int col = 1; col < 11; col += 2) {
                bricks.add(new CanonBrick(createBrickRect(col, row)));
            }
        }
        
        for (int row = 3; row <= 4; row++) {
            for (int col = 0; col < 12; col++) {
                if (col % 2 == 0) {
                    bricks.add(new BrickWall(createBrickRect(col, row)));
                }
            }
        }
        
        return bricks;
    }
}
