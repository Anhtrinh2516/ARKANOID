package utils;

/**
 * AudioPlayer (Static Class)
 */
public class AudioPlayer {

    /**
     * Hàm này PHẢI được gọi MỘT LẦN khi game khởi động để tải âm thanh và bật nhạc
     * nền.
     */
    public static void initialize() {
        SoundManager.INSTANCE.loadSounds();
    }

    /**
     * Phát âm thanh khi bóng nảy (chung)
     */
    public static void playBounce() {
        SoundManager.INSTANCE.playSoundEffect("bounce");
    }

    /**
     * Phát âm thanh khi gạch bị phá hủy
     */
    public static void playDestroyed() {
        SoundManager.INSTANCE.playSoundEffect("destroyed");
    }

    /**
     * Phát âm thanh khi hoàn thành level
     */
    public static void playLevelComplete() {
        SoundManager.INSTANCE.playSoundEffect("levelComplete");
    }

    /**
     * Phát âm thanh khi bóng nảy trên paddle
     */
    public static void playPaddleBounce() {
        SoundManager.INSTANCE.playSoundEffect("paddleBounce");
    }

    /**
     * Dừng tất cả nhạc và âm thanh (gọi khi thoát game).
     */
    public static void shutdown() {
        SoundManager.INSTANCE.stopAllSounds();
    }
}