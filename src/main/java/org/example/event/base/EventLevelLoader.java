package org.example.event.base;

import javafx.scene.shape.Rectangle;
import org.example.logic.Brick;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class EventLevelLoader {

    private static double gameWidth = 888.0;
    private static final double margin = 10.0;
    private static final double gapX = 2.0;
    private static final double gapY = 4.0;
    protected static final int cols = 12;
    private static double brickW = 69.5;
    protected static final double brickH = 32.0;

    private static void updateWidth(double width) {
        gameWidth = width;
        brickW = (gameWidth - 2 * margin - (cols - 1) * gapX) / cols;
    }

    protected static double calculateBrickX(int col) {
        double totalW = cols * brickW + (cols - 1) * gapX;
        double startX = (gameWidth - totalW) / 2;
        return startX + col * (brickW + gapX);
    }

    protected static double calculateBrickY(int row) {
        double startY = 5.0;
        return startY + row * (brickH + gapY);
    }

    protected static Rectangle createBrickRect(int col, int row) {
        double x = calculateBrickX(col);
        double y = calculateBrickY(row);
        return new Rectangle(x, y, brickW, brickH);
    }

    protected static Brick createWallBrick(int col, int row) {
        Rectangle rect = createBrickRect(col, row);
        return new Brick(rect, 1, true, 0);
    }

    protected abstract String getEventFolder();

    protected String getFilePrefix() {
        return getEventFolder().replace("_", "");
    }

    protected abstract Brick createBrickFromChar(char ch, int col, int row);

    protected abstract List<Brick> createFallbackLevel(int levelIndex);

    public List<Brick> loadLevel(int levelIdx, double width) {
        updateWidth(width);
        
        String filePath = "/event/levels/" + getEventFolder() + "/" + getFilePrefix() + levelIdx + ".txt";
        List<Brick> bricks = new ArrayList<>();
        
        try (InputStream stream = getClass().getResourceAsStream(filePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            
            if (stream == null) {
                return createFallbackLevel(levelIdx);
            }
            
            String line;
            int rowIdx = 0;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                for (int col = 0; col < Math.min(line.length(), cols); col++) {
                    char ch = line.charAt(col);
                    
                    Brick brick = createBrickFromChar(ch, col, rowIdx);
                    
                    if (brick != null) {
                        bricks.add(brick);
                    }
                }
                
                rowIdx++;
            }
            
        } catch (Exception e) {
            return createFallbackLevel(levelIdx);
        }
        
        return bricks;
    }
}
