package logic;

import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {

    // Constants cho layout đều đặn - optimized for 920px width
    private static final double GAME_AREA_WIDTH = 920.0;
    private static final double SIDE_MARGIN = 10.0;
    private static final double BRICK_WIDTH = 110.0;
    private static final double BRICK_HEIGHT = 30.0;
    private static final double BRICK_GAP_X = 5.0;
    private static final double BRICK_GAP_Y = 5.0;
    private static final int BRICKS_PER_ROW = 8;

    public static List<Brick> createLevel(int levelIndex, double paneWidth) {
        List<Brick> list = new ArrayList<>();

        switch (levelIndex) {
            case 1 -> {
                // Level 1: Khởi động - 3 hàng đơn giản
                list.addAll(createRows(3, BRICKS_PER_ROW, 1, false, 50));
            }
            case 2 -> {
                // Level 2: Bắt đầu thử thách - thêm gạch 2 hit
                list.addAll(createRows(2, BRICKS_PER_ROW, 2, false, 100));
                list.addAll(createRows(2, BRICKS_PER_ROW, 1, false, 50, 2));
            }
            case 3 -> {
                // Level 3: Xuất hiện gạch bất diệt
                list.addAll(createRows(2, BRICKS_PER_ROW, 2, false, 100));
                list.addAll(createRows(1, BRICKS_PER_ROW, 1, true, 0, 2)); // indestructible
                list.addAll(createRows(2, BRICKS_PER_ROW, 1, false, 50, 3));
            }
            case 4 -> {
                // Level 4: Gạch 3 hit xuất hiện
                list.addAll(createRows(2, BRICKS_PER_ROW, 3, false, 150));
                list.addAll(createRows(2, BRICKS_PER_ROW, 2, false, 100, 2));
                list.addAll(createRows(1, BRICKS_PER_ROW, 1, true, 0, 4));
            }
            case 5 -> {
                // Level 5: Hình chữ V ngược
                list.addAll(createDiagonalPattern(5, 3, false, 100, true));
                list.addAll(createDiagonalPattern(5, 3, false, 100, false));
                list.addAll(createRows(1, 6, 2, false, 100, 5));
            }
            case 6 -> {
                // Level 6: Tường gạch bất diệt ở giữa
                list.addAll(createRows(3, 3, 2, false, 100, 0, 0));
                list.addAll(createRows(3, 2, 1, true, 0, 0, 3)); // tường giữa
                list.addAll(createRows(3, 3, 2, false, 100, 0, 5));
            }
            case 7 -> {
                // Level 7: Hình kim cương
                list.addAll(createDiamondPattern(6, 150));
            }
            case 8 -> {
                // Level 8: Nhiều gạch 3 hit + bất diệt xen kẽ
                list.addAll(createRows(3, BRICKS_PER_ROW, 3, false, 150));
                list.addAll(createRows(1, BRICKS_PER_ROW, 1, true, 0, 3));
                list.addAll(createRows(2, BRICKS_PER_ROW, 2, false, 100, 4));
            }
            case 9 -> {
                // Level 9: Hình chữ X
                list.addAll(createXPattern(8, 2, 100));
            }
            case 10 -> {
                // Level 10: Mê cung
                list.addAll(createMazePattern(150));
            }
            case 11 -> {
                // Level 11: Sóng gạch
                for (int row = 0; row < 5; row++) {
                    int hits = (row % 2 == 0) ? 3 : 2;
                    boolean indestr = (row == 2);
                    list.addAll(createRows(1, BRICKS_PER_ROW, hits, indestr, hits * 50, row));
                }
            }
            case 12 -> {
                // Level 12: Tháp cao
                list.addAll(createPyramidPattern(8, 3, 150));
            }
            case 13 -> {
                // Level 13: Hình vuông lồng nhau
                list.addAll(createSquarePattern(3, 100));
            }
            case 14 -> {
                // Level 14: Gạch 3 hit mọi nơi + bất diệt xen kẽ
                list.addAll(createRows(5, BRICKS_PER_ROW, 3, false, 150));
                // Thêm bất diệt xen kẽ
                for (int i = 0; i < 8; i++) {
                    int col = i;
                    int row = 2;
                    if (i % 2 == 0) {
                        double x = calculateBrickX(col);
                        double y = calculateBrickY(row);
                        Rectangle rect = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                        list.add(new Brick(rect, 1, true, 0));
                    }
                }
            }
            case 15 -> {
                // Level 15: Hình trái tim (thử thách)
                list.addAll(createHeartPattern(3, 150));
            }
            case 16 -> {
                // Level 16: Boss final - chaos hoàn toàn
                list.addAll(createRows(4, BRICKS_PER_ROW, 3, false, 200));
                list.addAll(createRows(2, BRICKS_PER_ROW, 1, true, 0, 4));
                list.addAll(createRows(3, BRICKS_PER_ROW, 3, false, 150, 6));
            }
            default -> {
                // Sau level 16, loop lại với độ khó tăng dần
                int loopLevel = ((levelIndex - 1) % 16) + 1;
                list.addAll(createLevel(loopLevel, paneWidth));
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
        double startY = 40.0;
        return startY + row * (BRICK_HEIGHT + BRICK_GAP_Y);
    }

    // Tạo hàng gạch đều đặn
    private static List<Brick> createRows(int rows, int cols, int hits, boolean indestructible, int score) {
        return createRows(rows, cols, hits, indestructible, score, 0);
    }

    private static List<Brick> createRows(int rows, int cols, int hits, boolean indestructible, int score,
            int startRow) {
        return createRows(rows, cols, hits, indestructible, score, startRow, 0);
    }

    private static List<Brick> createRows(int rows, int cols, int hits, boolean indestructible, int score,
            int startRow, int startCol) {
        List<Brick> list = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double x = calculateBrickX(startCol + c);
                double y = calculateBrickY(startRow + r);
                Rectangle rect = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                list.add(new Brick(rect, hits, indestructible, score));
            }
        }
        return list;
    }

    // Các pattern đặc biệt
    private static List<Brick> createDiagonalPattern(int rows, int hits, boolean indestr, int score,
            boolean leftToRight) {
        List<Brick> list = new ArrayList<>();

        for (int r = 0; r < rows; r++) {
            int col = leftToRight ? r : (BRICKS_PER_ROW - 1 - r);
            double x = calculateBrickX(col);
            double y = calculateBrickY(r);
            Rectangle rect = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
            list.add(new Brick(rect, hits, indestr, score));
        }
        return list;
    }

    private static List<Brick> createDiamondPattern(int size, int score) {
        List<Brick> list = new ArrayList<>();
        int centerCol = BRICKS_PER_ROW / 2;
        int startRow = 1;

        // Top half
        for (int r = 0; r < size / 2; r++) {
            for (int c = -r; c <= r; c++) {
                int col = centerCol + c;
                if (col >= 0 && col < BRICKS_PER_ROW) {
                    double x = calculateBrickX(col);
                    double y = calculateBrickY(startRow + r);
                    Rectangle rect = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                    list.add(new Brick(rect, 2, false, score));
                }
            }
        }

        // Bottom half
        for (int r = size / 2 - 1; r >= 0; r--) {
            for (int c = -r; c <= r; c++) {
                int col = centerCol + c;
                if (col >= 0 && col < BRICKS_PER_ROW) {
                    double x = calculateBrickX(col);
                    double y = calculateBrickY(startRow + (size - r - 1));
                    Rectangle rect = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                    list.add(new Brick(rect, 2, false, score));
                }
            }
        }

        return list;
    }

    private static List<Brick> createXPattern(int size, int hits, int score) {
        List<Brick> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            // Left diagonal
            double x1 = calculateBrickX(i);
            double y = calculateBrickY(i);
            Rectangle rect1 = new Rectangle(x1, y, BRICK_WIDTH, BRICK_HEIGHT);
            list.add(new Brick(rect1, hits, false, score));

            // Right diagonal
            double x2 = calculateBrickX(BRICKS_PER_ROW - 1 - i);
            Rectangle rect2 = new Rectangle(x2, y, BRICK_WIDTH, BRICK_HEIGHT);
            list.add(new Brick(rect2, hits, false, score));
        }
        return list;
    }

    private static List<Brick> createMazePattern(int score) {
        List<Brick> list = new ArrayList<>();

        int[][] maze = {
                { 1, 1, 1, 1, 1, 1, 1, 1 },
                { 1, 0, 0, 0, 1, 0, 0, 1 },
                { 1, 0, 1, 0, 1, 0, 1, 1 },
                { 1, 0, 1, 0, 0, 0, 1, 1 },
                { 1, 0, 1, 1, 2, 1, 1, 1 },
                { 1, 0, 0, 0, 0, 0, 0, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 1 }
        };

        for (int r = 0; r < maze.length; r++) {
            for (int c = 0; c < maze[r].length; c++) {
                if (maze[r][c] > 0) {
                    double x = calculateBrickX(c);
                    double y = calculateBrickY(r);
                    Rectangle rect = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                    boolean indestr = (maze[r][c] == 2);
                    int hits = indestr ? 1 : 2;
                    list.add(new Brick(rect, hits, indestr, indestr ? 0 : score));
                }
            }
        }
        return list;
    }

    private static List<Brick> createPyramidPattern(int rows, int hits, int score) {
        List<Brick> list = new ArrayList<>();
        int centerCol = BRICKS_PER_ROW / 2;

        for (int r = 0; r < rows; r++) {
            int bricksInRow = rows - r;
            int startCol = centerCol - bricksInRow / 2;

            for (int c = 0; c < bricksInRow; c++) {
                double x = calculateBrickX(startCol + c);
                double y = calculateBrickY(r);
                Rectangle rect = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                list.add(new Brick(rect, hits, false, score));
            }
        }
        return list;
    }

    private static List<Brick> createSquarePattern(int hits, int score) {
        List<Brick> list = new ArrayList<>();

        // Outer square
        for (int i = 0; i < BRICKS_PER_ROW; i++) {
            // Top row
            double x = calculateBrickX(i);
            Rectangle topRect = new Rectangle(x, calculateBrickY(0), BRICK_WIDTH, BRICK_HEIGHT);
            list.add(new Brick(topRect, hits, false, score));

            // Bottom row
            Rectangle bottomRect = new Rectangle(x, calculateBrickY(5), BRICK_WIDTH, BRICK_HEIGHT);
            list.add(new Brick(bottomRect, hits, false, score));
        }

        // Left and right columns
        for (int i = 1; i < 5; i++) {
            // Left column
            Rectangle leftRect = new Rectangle(calculateBrickX(0), calculateBrickY(i), BRICK_WIDTH, BRICK_HEIGHT);
            list.add(new Brick(leftRect, hits, false, score));

            // Right column
            Rectangle rightRect = new Rectangle(calculateBrickX(BRICKS_PER_ROW - 1), calculateBrickY(i), BRICK_WIDTH,
                    BRICK_HEIGHT);
            list.add(new Brick(rightRect, hits, false, score));
        }

        // Inner square
        for (int i = 2; i < 6; i++) {
            for (int j = 2; j < 4; j++) {
                if (i == 2 || i == 5 || j == 2 || j == 5) {
                    double x = calculateBrickX(j);
                    double y = calculateBrickY(i);
                    Rectangle rect = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                    list.add(new Brick(rect, 2, false, score));
                }
            }
        }

        return list;
    }

    private static List<Brick> createHeartPattern(int hits, int score) {
        List<Brick> list = new ArrayList<>();

        int[][] heart = {
                { 0, 1, 1, 0, 0, 1, 1, 0 },
                { 1, 1, 1, 1, 1, 1, 1, 1 },
                { 1, 1, 1, 1, 1, 1, 1, 1 },
                { 0, 1, 1, 1, 1, 1, 1, 0 },
                { 0, 0, 1, 1, 1, 1, 0, 0 },
                { 0, 0, 0, 1, 1, 0, 0, 0 }
        };

        for (int r = 0; r < heart.length; r++) {
            for (int c = 0; c < heart[r].length; c++) {
                if (heart[r][c] == 1) {
                    double x = calculateBrickX(c);
                    double y = calculateBrickY(r + 1);
                    Rectangle rect = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                    list.add(new Brick(rect, hits, false, score));
                }
            }
        }
        return list;
    }
}