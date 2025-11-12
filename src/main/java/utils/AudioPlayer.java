package utils;

/**
 * AudioPlayer (Static Class)
 */
public class AudioPlayer {


    public static void initialize() {
        SoundManager.INSTANCE.loadSounds();
    }

    public static void playBounce() {
        SoundManager.INSTANCE.playSoundEffect("bounce");
    }

    public static void playDestroyed() {
        SoundManager.INSTANCE.playSoundEffect("destroyed");
    }

    public static void playLevelComplete() {
        SoundManager.INSTANCE.playSoundEffect("levelComplete");
    }

    public static void playPaddleBounce() {
        SoundManager.INSTANCE.playSoundEffect("paddleBounce");
    }

    public static void shutdown() {
        SoundManager.INSTANCE.stopAllSounds();
    }
}