package logic;

import javafx.scene.shape.Rectangle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {

    // Constants cho layout
    private static double GAME_AREA_WIDTH = 888.0;
    private static final double SIDE_MARGIN = 10.0;
    private static final double BRICK_GAP_X = 2.0;
    private static final double BRICK_GAP_Y = 4.0;
    private static final int BRICKS_PER_ROW = 12;
    private static double BRICK_WIDTH = 69.5;
    private static final double BRICK_HEIGHT = 32.0;

    private static void recalculateBrickWidth(double paneWidth) {
        GAME_AREA_WIDTH = paneWidth;
        BRICK_WIDTH = (GAME_AREA_WIDTH - 2 * SIDE_MARGIN - (BRICKS_PER_ROW - 1) * BRICK_GAP_X) / BRICKS_PER_ROW;
    }

    private static List<Brick> createSimpleFallbackLevel(int levelIndex) {
        System.out.println("Creating simple fallback level " + levelIndex);
        List<Brick> list = new ArrayList<>();
        
        // Tạo 3-5 hàng gạch đơn giản với độ khó tăng dần
        int rows = Math.min(3 + (levelIndex - 1) / 2, 5);
        int hits = Math.min(1 + (levelIndex - 1) / 3, 3);
        int score = hits * 50;
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < BRICKS_PER_ROW; c++) {
                double x = calculateBrickX(c);
                double y = calculateBrickY(r);
                Rectangle rect = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                list.add(new Brick(rect, hits, false, score));
            }
        }
        
        return list;
    }

    // Helper để tính toán vị trí X của gạch (căn giữa)
    private static double calculateBrickX(int col) {
        double totalBricksWidth = BRICKS_PER_ROW * BRICK_WIDTH + (BRICKS_PER_ROW - 1) * BRICK_GAP_X;
        double startX = (GAME_AREA_WIDTH - totalBricksWidth) / 2;
        return startX + col * (BRICK_WIDTH + BRICK_GAP_X);
    }

    // Helper để tính toán vị trí Y của gạch
    private static double calculateBrickY(int row) {
        double startY = 20.0;
        return startY + row * (BRICK_HEIGHT + BRICK_GAP_Y);
    }

    public static List<Brick> loadLevelFromFile(int levelIndex, double paneWidth) {
        recalculateBrickWidth(paneWidth);
        
        String filename = "/levels/level" + levelIndex + ".txt";
        List<Brick> list = new ArrayList<>();
        
        try (InputStream is = LevelLoader.class.getResourceAsStream(filename);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            
            if (is == null) {
                System.out.println("Level file not found: " + filename);
                System.out.println("Falling back to simple level");
                return createSimpleFallbackLevel(levelIndex);
            }
            
            System.out.println("Loading level from file: " + filename);
            
            String line;
            int row = 0;
            
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                // Parse từng ký tự trong dòng
                for (int col = 0; col < Math.min(line.length(), BRICKS_PER_ROW); col++) {
                    char c = line.charAt(col);
                    
                    int hits = 0;
                    boolean indestructible = false;
                    int score = 0;
                    
                    switch (c) {
                        case '0' -> {
                            continue; 
                        }
                        case '1' -> {
                            hits = 1;
                            score = 50;
                        }
                        case '2' -> {
                            hits = 2;
                            score = 100;
                        }
                        case '3' -> {
                            hits = 3;
                            score = 150;
                        }
                        case 'X', 'x' -> {
                            hits = 1;
                            indestructible = true;
                            score = 0;
                        }
                        default -> {
                            continue;
                        }
                    }
                    
                    // Tạo gạch
                    double x = calculateBrickX(col);
                    double y = calculateBrickY(row);
                    Rectangle rect = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                    list.add(new Brick(rect, hits, indestructible, score));
                }
                
                row++;
            }
            
            System.out.println("Loaded " + list.size() + " bricks from file (rows: " + row + ")");
            
        } catch (Exception e) {
            System.out.println("Error loading level file: " + e.getMessage());
            System.out.println("Falling back to simple level");
            return createSimpleFallbackLevel(levelIndex);
        }
        
        return list;
    }
}