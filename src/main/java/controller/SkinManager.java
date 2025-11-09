package controller;

import java.util.HashSet;
import java.util.Set;

public class SkinManager {
    public static final SkinManager INSTANCE = new SkinManager();

    public enum SkinType {
        FREE,   // Miễn phí từ đầu
        SHOP,   // Mua bằng coins
        EVENT   // Unlock qua event
    }

    // Danh sách skin
    public enum PaddleSkin {
        DEFAULT(SkinType.FREE, 0, "#3498db", "#2980b9"),
        FIRE(SkinType.SHOP, 10, "#e74c3c", "#c0392b"),
        GRASS(SkinType.SHOP, 10, "#27ae60", "#229954"),
        GOLD(SkinType.SHOP, 10, "#f39c12", "#d68910"),
        PURPLE(SkinType.SHOP, 10, "#9b59b6", "#8e44ad"),
        EVENT_SPECIAL(SkinType.EVENT, 0, "#000000", "#333333"); // Event skin - màu đen tạm

        public final SkinType type;
        public final int price;
        public final String fill;
        public final String stroke;

        PaddleSkin(SkinType type, int price, String fill, String stroke) {
            this.type = type;
            this.price = price;
            this.fill = fill;
            this.stroke = stroke;
        }
    }

    public enum BallSkin {
        DEFAULT(SkinType.FREE, 0, "#e74c3c"),
        BLUE(SkinType.SHOP, 10, "#3498db"),
        GREEN(SkinType.SHOP, 10, "#2ecc71"),
        GOLD(SkinType.SHOP, 10, "#f1c40f"),
        PINK(SkinType.SHOP, 10, "#e91e63"),
        EVENT_SPECIAL(SkinType.EVENT, 0, "#000000"); // Event skin - màu đen tạm

        public final SkinType type;
        public final int price;
        public final String color;

        BallSkin(SkinType type, int price, String color) {
            this.type = type;
            this.price = price;
            this.color = color;
        }
    }

    private PaddleSkin currentPaddleSkin = PaddleSkin.DEFAULT;
    private BallSkin currentBallSkin = BallSkin.DEFAULT;
    
    private Set<PaddleSkin> unlockedPaddleSkins = new HashSet<>();
    private Set<BallSkin> unlockedBallSkins = new HashSet<>();

    private SkinManager() {
        // Unlock skin FREE mặc định
        unlockedPaddleSkins.add(PaddleSkin.DEFAULT);
        unlockedBallSkins.add(BallSkin.DEFAULT);
    }

    public PaddleSkin getPaddleSkin() { return currentPaddleSkin; }
    public void setPaddleSkin(PaddleSkin skin) { this.currentPaddleSkin = skin; }

    public BallSkin getBallSkin() { return currentBallSkin; }
    public void setBallSkin(BallSkin skin) { this.currentBallSkin = skin; }
    
    public boolean isPaddleSkinUnlocked(PaddleSkin skin) {
        return unlockedPaddleSkins.contains(skin);
    }
    
    public boolean isBallSkinUnlocked(BallSkin skin) {
        return unlockedBallSkins.contains(skin);
    }
    
    public void unlockPaddleSkin(PaddleSkin skin) {
        unlockedPaddleSkins.add(skin);
    }
    
    public void unlockBallSkin(BallSkin skin) {
        unlockedBallSkins.add(skin);
    }
}
