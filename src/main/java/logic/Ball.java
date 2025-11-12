package logic;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class Ball {
    private final Circle node;
    private Polygon arrow;
    private Pane parentPane;
    private double dx;
    private double dy;
    private final double baseSpeed;

    // Ball state management
    private boolean isAttached = true;
    private double attachOffsetX = 0;
    private double launchAngle = 90; // Góc phóng (độ), 90 = thẳng đứng lên, 0 = ngang phải, 180 = ngang trái

    private static final double MIN_ANGLE = 30;
    private static final double MAX_ANGLE = 150;
    private static final double MIN_VERTICAL_SPEED = 1.5;
    private static final double MAX_SPEED = 8.0;
    private static final double MIN_SPEED = 2.0;

    public Ball(Circle node) {
        this.node = node;
        this.baseSpeed = 3.0;
        this.dx = 0;
        this.dy = -baseSpeed;
        this.isAttached = true;
    }

    public void initializeArrow(Pane pane) {
        this.parentPane = pane;

        // Tạo mũi tên chỉ hướng
        arrow = new Polygon();
        arrow.getPoints().addAll(
                0.0, -25.0,    // Đỉnh mũi tên
                -8.0, -10.0,   // Góc trái
                -3.0, -10.0,   // Cạnh trái thân
                -3.0, 5.0,     // Đáy trái
                3.0, 5.0,      // Đáy phải
                3.0, -10.0,    // Cạnh phải thân
                8.0, -10.0     // Góc phải
        );
        arrow.setFill(Color.YELLOW);
        arrow.setStroke(Color.ORANGE);
        arrow.setStrokeWidth(2);
        arrow.setOpacity(0.8);
        arrow.setMouseTransparent(true);

        // Ẩn arrow ban đầu
        arrow.setVisible(false);

        // Thêm arrow vào pane
        pane.getChildren().add(arrow);

        System.out.println("Arrow initialized for ball");
    }

    public Circle getNode() {
        return node;
    }

    public Polygon getArrow() {
        return arrow;
    }

    public double getX() {
        return node.getCenterX();
    }

    public double getY() {
        return node.getCenterY();
    }

    public double getR() {
        return node.getRadius();
    }

    // ===== ATTACHMENT SYSTEM =====
    public boolean isAttached() {
        return isAttached;
    }

    public void attach(double paddleX, double paddleWidth) {
        isAttached = true;
        attachOffsetX = 0;
        dx = 0;
        dy = 0;
        launchAngle = 90; // Reset góc về thẳng đứng
        if (arrow != null) {
            arrow.setVisible(true);
            updateArrowRotation();
        }
    }

    public void launch() {
        if (isAttached) {
            System.out.println("🚀 Launching ball from attached state...");
            isAttached = false;

            if (arrow != null) {
                arrow.setVisible(false);
                System.out.println("   Arrow hidden");
            }

            // Chuyển góc độ sang radian
            // launchAngle: 90° = thẳng lên, 0° = phải, 180° = trái
            double angleRad = Math.toRadians(launchAngle);

            // Tính vận tốc dựa trên góc
            // cos(angle) cho dx (ngang), sin(angle) cho dy (dọc)
            dx = baseSpeed * Math.cos(angleRad);
            dy = -baseSpeed * Math.sin(angleRad); // Âm vì trục Y hướng xuống

            // Đảm bảo dy luôn âm (đi lên) và có tốc độ tối thiểu
            if (dy > 0) dy = -dy; // Force negative
            if (Math.abs(dy) < MIN_VERTICAL_SPEED) {
                dy = -MIN_VERTICAL_SPEED;
            }

            System.out.println("   Ball launched at angle " + launchAngle + "° - dx=" +
                    String.format("%.2f", dx) + ", dy=" + String.format("%.2f", dy));
        } else {
            System.out.println("Ball is already flying!");
        }
    }

    public void updateAttachment(double paddleX, double paddleWidth, double paddleY) {
        if (isAttached) {
            // Ball dính ở giữa paddle, phía trên
            double ballX = paddleX + paddleWidth / 2 + attachOffsetX;
            double ballY = paddleY - getR() - 2;

            node.setCenterX(ballX);
            node.setCenterY(ballY);

            // Cập nhật vị trí mũi tên
            if (arrow != null && arrow.isVisible()) {
                arrow.setLayoutX(ballX);
                arrow.setLayoutY(ballY);
            }
        }
    }

    public void adjustLaunchAngle(double deltaAngle) {
        System.out.println("adjustLaunchAngle called with delta=" + deltaAngle);
        System.out.println("   isAttached=" + isAttached + ", arrow=" + (arrow != null ? "exists" : "NULL"));

        if (!isAttached) {
            System.out.println("   Ball is not attached!");
            return;
        }

        if (arrow == null) {
            System.out.println("   Arrow is NULL!");
            return;
        }

        double oldAngle = launchAngle;
        launchAngle -= deltaAngle;

        // Giới hạn góc từ 30° đến 150°
        if (launchAngle < MIN_ANGLE) launchAngle = MIN_ANGLE;
        if (launchAngle > MAX_ANGLE) launchAngle = MAX_ANGLE;

        System.out.println("   Old angle: " + String.format("%.0f", oldAngle) + "° → New angle: " + String.format("%.0f", launchAngle) + "°");

        // Update arrow rotation
        updateArrowRotation();
    }

    public void setLaunchAngle(double angle) {
        if (isAttached) {
            launchAngle = angle;

            // Giới hạn góc
            if (launchAngle < MIN_ANGLE) launchAngle = MIN_ANGLE;
            if (launchAngle > MAX_ANGLE) launchAngle = MAX_ANGLE;

            updateArrowRotation();
        }
    }

    public double getLaunchAngle() {
        return launchAngle;
    }

    private void updateArrowRotation() {
        if (arrow != null && arrow.isVisible()) {
            // launchAngle: 90° = lên, 0° = phải, 180° = trái
            // JavaFX rotation: 0° = phải, -90° = lên
            // Chuyển đổi: rotation = -(launchAngle - 90)
            double rotation = -(launchAngle - 90);
            arrow.setRotate(rotation);

            System.out.println("   Arrow rotated to " + String.format("%.0f", rotation) + "° (launch angle: " + String.format("%.0f", launchAngle) + "°)");
        }
    }

    // ===== Getter/Setter vận tốc =====
    public void setVelocity(double dx, double dy) {
        if (!isAttached) {
            this.dx = dx;
            this.dy = dy;
            ensureMinimumVerticalSpeed();
        }
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDx(double dx) {
        if (!isAttached) {
            this.dx = dx;
        }
    }

    public void setDy(double dy) {
        if (!isAttached) {
            this.dy = dy;
            ensureMinimumVerticalSpeed();
        }
    }

    private void ensureMinimumVerticalSpeed() {
        if (Math.abs(dy) < MIN_VERTICAL_SPEED) {
            dy = dy < 0 ? -MIN_VERTICAL_SPEED : MIN_VERTICAL_SPEED;
            System.out.println("Vertical speed adjusted to minimum: " + dy);
        }
    }

    public void move() {
        if (!isAttached) {
            node.setCenterX(node.getCenterX() + dx);
            node.setCenterY(node.getCenterY() + dy);
        }
    }

    public void bounceX() {
        if (!isAttached) {
            dx = -dx;
        }
    }

    public void bounceY() {
        if (!isAttached) {
            dy = -dy;
        }
    }

    public void bounceWithAngle(double hitPosition) {
        if (!isAttached) {
            // Tính góc dựa trên vị trí va chạm
            // hitPosition 0.0 = trái (150°), 0.5 = giữa (90°), 1.0 = phải (30°)
            double angle = MAX_ANGLE - hitPosition * (MAX_ANGLE - MIN_ANGLE);
            double angleRad = Math.toRadians(angle);

            // Tính tốc độ mới giữ nguyên độ lớn
            double speed = Math.sqrt(dx * dx + dy * dy);
            dx = speed * Math.cos(angleRad);
            dy = -speed * Math.sin(angleRad); // Luôn âm (đi lên)

            // Đảm bảo tốc độ dọc tối thiểu
            ensureMinimumVerticalSpeed();

            System.out.println("Bounced at position " + String.format("%.2f", hitPosition) +
                    " -> angle " + String.format("%.1f", angle) + " deg" +
                    " -> dx=" + String.format("%.2f", dx) +
                    ", dy=" + String.format("%.2f", dy));
        }
    }

    public void reset(double paddleX, double paddleWidth, double paddleY) {
        System.out.println("Resetting ball to attached state...");

        isAttached = true;
        attachOffsetX = 0;
        launchAngle = 90; // Reset góc về thẳng đứng

        // Đặt ball ở giữa paddle
        double ballX = paddleX + paddleWidth / 2;
        double ballY = paddleY - getR() - 2;

        node.setCenterX(ballX);
        node.setCenterY(ballY);

        dx = 0;
        dy = 0;

        // Hiện mũi tên và cập nhật vị trí
        if (arrow != null) {
            arrow.setVisible(true);
            arrow.setLayoutX(ballX);
            arrow.setLayoutY(ballY);
            updateArrowRotation();
            System.out.println("   Arrow shown and positioned at (" + ballX + ", " + ballY + ")");
        } else {
            System.out.println("   Arrow is null!");
        }

        System.out.println("   Ball reset to (" +
                String.format("%.1f", ballX) + ", " +
                String.format("%.1f", ballY) + ")");
    }

    public void clampPosition(double minX, double maxX, double minY, double maxY) {
        if (!isAttached) {
            if (node.getCenterX() - getR() < minX)
                node.setCenterX(minX + getR());
            if (node.getCenterX() + getR() > maxX)
                node.setCenterX(maxX - getR());
            if (node.getCenterY() - getR() < minY)
                node.setCenterY(minY + getR());
            if (node.getCenterY() + getR() > maxY)
                node.setCenterY(maxY - getR());
        }
    }

    public void speedUp(double factor) {
        if (!isAttached) {
            dx *= factor;
            dy *= factor;

            double max = 12.0;
            double min = 1.5;

            dx = clamp(dx, -max, max, min);
            dy = clamp(dy, -max, max, min);

            ensureMinimumVerticalSpeed();
        }
    }

    private double clamp(double v, double minVal, double maxVal, double minAbs) {
        if (v > maxVal) v = maxVal;
        if (v < minVal) v = minVal;
        if (Math.abs(v) < minAbs) v = (v < 0 ? -minAbs : minAbs);
        return v;
    }

    public void limitSpeed() {
        if (!isAttached) {
            double speed = Math.sqrt(dx * dx + dy * dy);
            if (speed > MAX_SPEED) {
                double f = MAX_SPEED / speed;
                dx *= f;
                dy *= f;
            } else if (speed < MIN_SPEED) {
                double f = MIN_SPEED / speed;
                dx *= f;
                dy *= f;
            }
        }
    }

    public void moveWithCCD() {
        if (!isAttached) {
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < 5) {
                node.setCenterX(node.getCenterX() + dx);
                node.setCenterY(node.getCenterY() + dy);
            } else {
                int steps = (int) Math.ceil(dist / 3);
                steps = Math.min(steps, 5);
                double sx = dx / steps;
                double sy = dy / steps;
                for (int i = 0; i < steps; i++) {
                    node.setCenterX(node.getCenterX() + sx);
                    node.setCenterY(node.getCenterY() + sy);
                }
            }
        }
    }

    public void normalizeAngle() {
        if (!isAttached) {
            double angle = Math.toDegrees(Math.atan2(-dy, dx));
            if (angle < 0) angle += 360;
            
            boolean fix = false;
            if (angle < MIN_ANGLE && angle > 0) {
                angle = MIN_ANGLE;
                fix = true;
            } else if (angle > (180 - MIN_ANGLE) && angle < 180) {
                angle = 180 - MIN_ANGLE;
                fix = true;
            } else if (angle > 180 && angle < (180 + MIN_ANGLE)) {
                angle = 180 + MIN_ANGLE;
                fix = true;
            } else if (angle > (360 - MIN_ANGLE)) {
                angle = 360 - MIN_ANGLE;
                fix = true;
            }
            
            if (fix) {
                double speed = Math.sqrt(dx * dx + dy * dy);
                double rad = Math.toRadians(angle);
                dx = speed * Math.cos(rad);
                dy = -speed * Math.sin(rad);
                ensureMinimumVerticalSpeed();
            }
        }
    }
}