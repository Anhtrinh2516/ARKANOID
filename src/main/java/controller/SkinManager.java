package controller;

import java.util.HashSet;
import java.util.Set;

public class SkinManager {
    public static final SkinManager INSTANCE = new SkinManager();

    public enum SkinType {
        FREE,
        SHOP,
        EVENT
    }

    public enum PaddleSkin {
        DEFAULT(SkinType.FREE, 0, "#3498db", "#2980b9", "", null),
        FIRE(SkinType.SHOP, 10, "#e74c3c", "#c0392b", "", null),
        GRASS(SkinType.SHOP, 10, "#27ae60", "#229954", "", null),
        GOLD(SkinType.SHOP, 10, "#f39c12", "#d68910", "", null),
        PURPLE(SkinType.SHOP, 10, "#9b59b6", "#8e44ad", "", null),
        
        TREASURE_HUNTER(SkinType.EVENT, 0, "#00CED1", "#008B8B", "🏴‍☠️ Treasure Hunter", "/event/skins/treasure_hunter/treasure_hunter_paddle.png"),
        PENALDO(SkinType.EVENT, 0, "#0064FF", "#0050CC", "Penaldo", "/event/skins/penaldo/penaldo_paddle.png"),
        UNIVERSE(SkinType.EVENT, 0, "#00FF41", "#00CC33", "Universe", "/event/skins/universe/universe_paddle.png"),
        CASINO(SkinType.EVENT, 0, "#FF4500", "#DC143C", "Casino", "/event/skins/casino/casino_paddle.png"),
        CASTLE(SkinType.EVENT, 0, "#00BFFF", "#1E90FF", "Castle", "/event/skins/castle/castle_paddle.png");

        public final SkinType type;
        public final int price;
        public final String fill;
        public final String stroke;
        public final String eventName;
        public final String imagePath;

        PaddleSkin(SkinType type, int price, String fill, String stroke, String eventName, String imagePath) {
            this.type = type;
            this.price = price;
            this.fill = fill;
            this.stroke = stroke;
            this.eventName = eventName;
            this.imagePath = imagePath;
        }
    }

    public enum BallSkin {
        DEFAULT(SkinType.FREE, 0, "#e74c3c", "", null),
        BLUE(SkinType.SHOP, 10, "#3498db", "", null),
        GREEN(SkinType.SHOP, 10, "#2ecc71", "", null),
        GOLD(SkinType.SHOP, 10, "#f1c40f", "", null),
        PINK(SkinType.SHOP, 10, "#e91e63", "", null),
        
        TREASURE_HUNTER(SkinType.EVENT, 0, "#FFD700", "Treasure Hunter", "/event/skins/treasure_hunter/treasure_hunter_ball.png"),
        PENALDO(SkinType.EVENT, 0, "#FFFFFF", "Penaldo", "/event/skins/penaldo/penaldo_ball.png"),
        UNIVERSE(SkinType.EVENT, 0, "#39FF14", "Universe", "/event/skins/universe/universe_ball.png"),
        CASINO(SkinType.EVENT, 0, "#FF6347", "Casino", "/event/skins/casino/casino_ball.png"),
        CASTLE(SkinType.EVENT, 0, "#87CEEB", "Castle", "/event/skins/castle/castle_ball.png");

        public final SkinType type;
        public final int price;
        public final String color;
        public final String eventName;
        public final String imagePath;

        BallSkin(SkinType type, int price, String color, String eventName, String imagePath) {
            this.type = type;
            this.price = price;
            this.color = color;
            this.eventName = eventName;
            this.imagePath = imagePath;
        }
    }

    private PaddleSkin currentPaddleSkin = PaddleSkin.DEFAULT;
    private BallSkin currentBallSkin = BallSkin.DEFAULT;
    
    private Set<PaddleSkin> unlockedPaddleSkins = new HashSet<>();
    private Set<BallSkin> unlockedBallSkins = new HashSet<>();

    private SkinManager() {
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