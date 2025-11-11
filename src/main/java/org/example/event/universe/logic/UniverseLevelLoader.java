package org.example.event.universe.logic;

import org.example.event.base.EventLevelLoader;
import org.example.logic.Brick;

import java.util.ArrayList;
import java.util.List;

public class UniverseLevelLoader extends EventLevelLoader {

    private static final UniverseLevelLoader INSTANCE = new UniverseLevelLoader();

    public static List<Brick> loadUniverseLevel(int levelIdx, double paneWidth) {
        return INSTANCE.loadLevel(levelIdx, paneWidth);
    }

    @Override
    protected String getEventFolder() {
        return "universe";
    }
    
    @Override
    protected String getFilePrefix() {
        return "universe";
    }

    @Override
    protected Brick createBrickFromChar(char ch, int col, int row) {
        if (ch == 'H' || ch == 'h') {
            return new ShipBrick(createBrickRect(col, row));
        } else if (ch == 'A' || ch == 'a') {
            return new AlienBrick(createBrickRect(col, row));
        } else if (ch == 'S' || ch == 's') {
            return new StoneBrick(createBrickRect(col, row));
        } else {
            return null;
        }
    }

    @Override
    protected List<Brick> createFallbackLevel(int levelIdx) {
        List<Brick> bricks = new ArrayList<>();
        
        for (int col = 1; col < 11; col += 2) {
            bricks.add(new AlienBrick(createBrickRect(col, 0)));
        }
        
        for (int row = 1; row <= 2; row++) {
            for (int col = 0; col < 12; col++) {
                if (col % 3 == 0) {
                    bricks.add(new ShipBrick(createBrickRect(col, row)));
                } else if (col % 3 == 1) {
                    bricks.add(new StoneBrick(createBrickRect(col, row)));
                }
            }
        }
        
        return bricks;
    }
}
