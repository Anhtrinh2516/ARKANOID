package org.example.logic;

import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class LevelLoader {

    public static List<Brick> createLevel(int levelIndex, double paneWidth) {
        List<Brick> list = new ArrayList<>();

        switch (levelIndex) {
            case 1 -> {
                // Level 1: Khởi động - 3 hàng đơn giản
                list.addAll(createRows(3, 8, 1, false, 50, 70, 25, 80, 10));
            }
            case 2 -> {
                // Level 2: Bắt đầu thử thách - thêm gạch 2 hit
                list.addAll(createRows(2, 8, 2, false, 100, 70, 25, 60, 10));
                list.addAll(createRows(2, 8, 1, false, 50, 70, 25, 130, 10));
            }
            case 3 -> {
                // Level 3: Xuất hiện gạch bất diệt
                list.addAll(createRows(2, 9, 2, false, 100, 65, 25, 60, 10));
                list.addAll(createRows(1, 9, 1, true, 0, 65, 25, 120, 10)); // indestructible
                list.addAll(createRows(2, 9, 1, false, 50, 65, 25, 155, 10));
            }
            case 4 -> {
                // Level 4: Gạch 3 hit xuất hiện
                list.addAll(createRows(2, 9, 3, false, 150, 60, 25, 60, 10));
                list.addAll(createRows(2, 9, 2, false, 100, 60, 25, 120, 10));
                list.addAll(createRows(1, 9, 1, true, 0, 60, 25, 180, 10));
            }
            case 5 -> {
                // Level 5: Hình chữ V ngược
                list.addAll(createDiagonalPattern(5, 3, false, 100, 60, 25, 80, true));
                list.addAll(createDiagonalPattern(5, 3, false, 100, 60, 25, 80, false));
                list.addAll(createRows(1, 7, 2, false, 100, 60, 25, 220, 10));
            }
            case 6 -> {
                // Level 6: Tường gạch bất diệt ở giữa
                list.addAll(createRows(3, 4, 2, false, 100, 60, 25, 60, 10));
                list.addAll(createRows(3, 1, 1, true, 0, 60, 25, 60, 10, 280)); // tường giữa
                list.addAll(createRows(3, 4, 2, false, 100, 60, 25, 60, 10, 350));
            }
            case 7 -> {
                // Level 7: Hình kim cương
                list.addAll(createDiamondPattern(7, 150, 60, 25, 100));
            }
            case 8 -> {
                // Level 8: Nhiều gạch 3 hit + bất diệt xen kẽ
                list.addAll(createRows(3, 10, 3, false, 150, 55, 25, 60, 8));
                list.addAll(createRows(1, 10, 1, true, 0, 55, 25, 145, 8));
                list.addAll(createRows(2, 10, 2, false, 100, 55, 25, 180, 8));
            }
            case 9 -> {
                // Level 9: Hình chữ X
                list.addAll(createXPattern(9, 2, 100, 55, 25, 70));
            }
            case 10 -> {
                // Level 10: Mê cung
                list.addAll(createMazePattern(150, 50, 25, 60));
            }
            case 11 -> {
                // Level 11: Sóng gạch
                for (int row = 0; row < 5; row++) {
                    int hits = (row % 2 == 0) ? 3 : 2;
                    boolean indestr = (row == 2);
                    list.addAll(createRows(1, 9, hits, indestr, hits * 50, 60, 25, 60 + row * 35, 10));
                }
            }
            case 12 -> {
                // Level 12: Tháp cao
                list.addAll(createPyramidPattern(10, 3, 150, 50, 25, 60));
            }
            case 13 -> {
                // Level 13: Hình vuông lồng nhau
                list.addAll(createSquarePattern(3, 100, 55, 25, 70));
            }
            case 14 -> {
                // Level 14: Gạch 3 hit mọi nơi + bất diệt xen kẽ
                list.addAll(createRows(6, 10, 3, false, 150, 50, 22, 60, 8));
                // Thêm bất diệt xen kẽ ngẫu nhiên
                for (int i = 0; i < 15; i++) {
                    double x = 40 + (i % 10) * 60;
                    double y = 80 + (i / 10) * 30;
                    Rectangle rect = new Rectangle(x, y, 50, 22);
                    list.add(new Brick(rect, 1, true, 0));
                }
            }
            case 15 -> {
                // Level 15: Hình trái tim (thử thách)
                list.addAll(createHeartPattern(3, 150, 45, 22, 70));
            }
            case 16 -> {
                // Level 16: Boss final - chaos hoàn toàn
                list.addAll(createRows(4, 11, 3, false, 200, 45, 22, 60, 6));
                list.addAll(createRows(2, 11, 1, true, 0, 45, 22, 155, 6));
                list.addAll(createRows(3, 11, 3, false, 150, 45, 22, 215, 6));
                // Thêm gạch bất diệt tạo chướng ngại
                for (int i = 0; i < 20; i++) {
                    double x = 50 + (i % 11) * 50;
                    double y = 100 + (i / 11) * 40;
                    if (i % 3 == 0) {
                        Rectangle rect = new Rectangle(x, y, 45, 22);
                        list.add(new Brick(rect, 1, true, 0));
                    }
                }
            }
            default -> {
                // Sau level 16, loop lại với độ khó tăng dần
                int loopLevel = ((levelIndex - 1) % 16) + 1;
                list.addAll(createLevel(loopLevel, paneWidth));
                // Tăng độ khó: biến một số gạch thành 3 hit
                for (Brick b : list) {
                    if (!b.isIndestructible() && Math.random() < 0.3) {
                        // Hack: tạo brick mới với hits tăng
                        Rectangle oldRect = b.getNode();
                        Rectangle newRect = new Rectangle(oldRect.getX(), oldRect.getY(),
                                oldRect.getWidth(), oldRect.getHeight());
                        Brick newBrick = new Brick(newRect, 3, false, 200);
                        list.set(list.indexOf(b), newBrick);
                    }
                }
            }
        }
        return list;
    }


    private static List<Brick> createRows(int rows, int cols, int hits, boolean indestructible,
                                          int score, double brickWidth, double brickHeight,
                                          double startY, double gap) {
        return createRows(rows, cols, hits, indestructible, score, brickWidth, brickHeight, startY, gap, 40);
    }

    private static List<Brick> createRows(int rows, int cols, int hits, boolean indestructible,
                                          int score, double brickWidth, double brickHeight,
                                          double startY, double gap, double startX) {
        List<Brick> list = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double x = startX + c * (brickWidth + gap);
                double y = startY + r * (brickHeight + gap);
                Rectangle rect = new Rectangle(x, y, brickWidth, brickHeight);
                list.add(new Brick(rect, hits, indestructible, score));
            }
        }
        return list;
    }


    private static List<Brick> createDiagonalPattern(int rows, int hits, boolean indestr,
                                                     int score, double width, double height,
                                                     double startY, boolean leftToRight) {
        List<Brick> list = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            double x = leftToRight ? 50 + r * 60 : 400 - r * 60;
            double y = startY + r * 35;
            Rectangle rect = new Rectangle(x, y, width, height);
            list.add(new Brick(rect, hits, indestr, score));
        }
        return list;
    }

    private static List<Brick> createDiamondPattern(int size, int score, double width,
                                                    double height, double startY) {
        List<Brick> list = new ArrayList<>();
        int centerX = 400;


        for (int r = 0; r < size / 2; r++) {
            for (int c = -r; c <= r; c++) {
                double x = centerX + c * (width + 10) - width / 2;
                double y = startY + r * (height + 10);
                Rectangle rect = new Rectangle(x, y, width, height);
                list.add(new Brick(rect, 2, false, score));
            }
        }


        for (int r = size / 2; r >= 0; r--) {
            for (int c = -r; c <= r; c++) {
                double x = centerX + c * (width + 10) - width / 2;
                double y = startY + (size - r) * (height + 10);
                Rectangle rect = new Rectangle(x, y, width, height);
                list.add(new Brick(rect, 2, false, score));
            }
        }

        return list;
    }


    private static List<Brick> createXPattern(int size, int hits, int score,
                                              double width, double height, double startY) {
        List<Brick> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {

            double x1 = 80 + i * 55;
            double y1 = startY + i * 30;
            Rectangle rect1 = new Rectangle(x1, y1, width, height);
            list.add(new Brick(rect1, hits, false, score));


            double x2 = 500 - i * 55;
            double y2 = startY + i * 30;
            Rectangle rect2 = new Rectangle(x2, y2, width, height);
            list.add(new Brick(rect2, hits, false, score));
        }
        return list;
    }


    private static List<Brick> createMazePattern(int score, double width,
                                                 double height, double startY) {
        List<Brick> list = new ArrayList<>();
        int[][] maze = {
                {1,1,1,1,1,1,1,1,1},
                {1,0,0,0,1,0,0,0,1},
                {1,0,1,0,1,0,1,0,1},
                {1,0,1,0,0,0,1,0,1},
                {1,0,1,1,2,1,1,0,1},
                {1,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1}
        };

        for (int r = 0; r < maze.length; r++) {
            for (int c = 0; c < maze[r].length; c++) {
                if (maze[r][c] > 0) {
                    double x = 100 + c * (width + 8);
                    double y = startY + r * (height + 8);
                    Rectangle rect = new Rectangle(x, y, width, height);
                    boolean indestr = (maze[r][c] == 2);
                    int hits = indestr ? 1 : 2;
                    list.add(new Brick(rect, hits, indestr, indestr ? 0 : score));
                }
            }
        }
        return list;
    }


    private static List<Brick> createPyramidPattern(int rows, int hits, int score,
                                                    double width, double height, double startY) {
        List<Brick> list = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            int bricksInRow = rows - r;
            double offsetX = r * (width + 10) / 2;
            for (int c = 0; c < bricksInRow; c++) {
                double x = 150 + offsetX + c * (width + 10);
                double y = startY + r * (height + 10);
                Rectangle rect = new Rectangle(x, y, width, height);
                list.add(new Brick(rect, hits, false, score));
            }
        }
        return list;
    }


    private static List<Brick> createSquarePattern(int hits, int score,
                                                   double width, double height, double startY) {
        List<Brick> list = new ArrayList<>();


        for (int i = 0; i < 9; i++) {
            list.add(new Brick(new Rectangle(80 + i * 60, startY, width, height), hits, false, score));
            list.add(new Brick(new Rectangle(80 + i * 60, startY + 200, width, height), hits, false, score));
            if (i > 0 && i < 8) {
                list.add(new Brick(new Rectangle(80, startY + i * 25, width, height), hits, false, score));
                list.add(new Brick(new Rectangle(560, startY + i * 25, width, height), hits, false, score));
            }
        }

        for (int i = 0; i < 5; i++) {
            list.add(new Brick(new Rectangle(200 + i * 60, startY + 75, width, height), 2, false, score));
            list.add(new Brick(new Rectangle(200 + i * 60, startY + 125, width, height), 2, false, score));
        }

        return list;
    }

    private static List<Brick> createHeartPattern(int hits, int score,
                                                  double width, double height, double startY) {
        List<Brick> list = new ArrayList<>();
        int[][] heart = {
                {0,1,1,0,0,1,1,0},
                {1,1,1,1,1,1,1,1},
                {1,1,1,1,1,1,1,1},
                {0,1,1,1,1,1,1,0},
                {0,0,1,1,1,1,0,0},
                {0,0,0,1,1,0,0,0},
                {0,0,0,0,0,0,0,0}
        };

        for (int r = 0; r < heart.length; r++) {
            for (int c = 0; c < heart[r].length; c++) {
                if (heart[r][c] == 1) {
                    double x = 200 + c * (width + 8);
                    double y = startY + r * (height + 8);
                    Rectangle rect = new Rectangle(x, y, width, height);
                    list.add(new Brick(rect, hits, false, score));
                }
            }
        }
        return list;
    }
}