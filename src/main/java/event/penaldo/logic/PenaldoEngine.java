package event.penaldo.logic;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import event.base.EventGameEngine;
import logic.Brick;

import java.util.*;
import java.util.function.IntConsumer;

public class PenaldoEngine extends EventGameEngine {

    private List<GoalkeeperBrick> goalkeeperBricks = new ArrayList<>();

    public PenaldoEngine(AnchorPane pane, Rectangle paddleRect, Circle ballCircle,
                         IntConsumer scoreCallback, IntConsumer livesCallback, IntConsumer levelCallback) {
        super(pane, paddleRect, ballCircle, scoreCallback, livesCallback, levelCallback);
    }

    @Override
    protected List<Brick> loadLevelBricks(int levelIndex) {
        List<Brick> bricks = PenaldoLevelLoader.loadPenaldoLevel(levelIndex, GAME_AREA_WIDTH);
        setupGoalkeepers(bricks);
        return bricks;
    }

    @Override
    protected void onBrickDestroyed(Brick brick) {
    }

    @Override
    protected boolean checkWinCondition() {
        for (Brick brick : bricks) {
            if (brick instanceof FootballBrick && !brick.isDestroyed()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected int getLevelCompleteBonus() {
        return 1000;
    }

    @Override
    protected int getLevelCompleteCoins() {
        return 10;
    }
    
    @Override
    protected boolean isFinalLevel(int levelIndex) {
        return levelIndex >= 1;
    }

    @Override
    protected void onUpdate() {
        updateGoalkeepers();
    }
    
    @Override
    protected void onLevelComplete(int completedLevel) {
        if (completedLevel == 1) {
            controller.SkinManager.INSTANCE.unlockPaddleSkin(
                controller.SkinManager.PaddleSkin.PENALDO
            );
            controller.SkinManager.INSTANCE.unlockBallSkin(
                controller.SkinManager.BallSkin.PENALDO
            );
        }
    }
    
    @Override
    protected void showWinScreen() {
        javafx.application.Platform.runLater(() -> {
            try {
                boolean play = ui.TaiXiuDialog.showAskDialog();
                
                if (play) {
                    int currentScore = getScore();
                    int currentMoney = getMoney();
                    ui.TaiXiuDialog dialog = new ui.TaiXiuDialog(currentScore, currentMoney);
                    int[] result = dialog.showAndWait();
                    setScore(result[0]);
                    setMoney(result[1]);
                }
                super.showWinScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onLifeLost() {
        for (GoalkeeperBrick goalkeeper : goalkeeperBricks) {
            Rectangle rect = goalkeeper.getNode();
            double centerX = (goalkeeper.getMinX() + goalkeeper.getMaxX()) / 2 - rect.getWidth() / 2;
            rect.setX(centerX);
        }
    }

    private void setupGoalkeepers(List<Brick> bricks) {
        goalkeeperBricks.clear();
        
        Map<Integer, List<GoalPostBrick>> goalPosts = new HashMap<>();
        Map<Integer, List<GoalkeeperBrick>> goalkeepers = new HashMap<>();
        
        for (Brick brick : bricks) {
            int row = (int) Math.round((brick.getNode().getY() - 20.0) / 36.0);
            
            if (brick instanceof GoalPostBrick) {
                goalPosts.computeIfAbsent(row, k -> new ArrayList<>()).add((GoalPostBrick) brick);
            } else if (brick instanceof GoalkeeperBrick) {
                goalkeepers.computeIfAbsent(row, k -> new ArrayList<>()).add((GoalkeeperBrick) brick);
                goalkeeperBricks.add((GoalkeeperBrick) brick);
            }
        }
        
        for (Map.Entry<Integer, List<GoalkeeperBrick>> entry : goalkeepers.entrySet()) {
            int row = entry.getKey();
            List<GoalkeeperBrick> rowGoalkeepers = entry.getValue();
            List<GoalPostBrick> rowPosts = goalPosts.get(row);
            
            if (rowPosts != null && rowPosts.size() >= 2) {
                rowPosts.sort(Comparator.comparingDouble(post -> post.getNode().getX()));
                GoalPostBrick leftPost = rowPosts.get(0);
                GoalPostBrick rightPost = rowPosts.get(rowPosts.size() - 1);
                double minX = leftPost.getNode().getX() + leftPost.getNode().getWidth();
                double maxX = rightPost.getNode().getX();
                
                for (GoalkeeperBrick goalkeeper : rowGoalkeepers) {
                    goalkeeper.setBounds(minX, maxX);
                }
            }
        }
    }

    private void updateGoalkeepers() {
        for (GoalkeeperBrick goalkeeper : goalkeeperBricks) {
            if (!goalkeeper.isDestroyed()) {
                goalkeeper.move();
            }
        }
    }
}
