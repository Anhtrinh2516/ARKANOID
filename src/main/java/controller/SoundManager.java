package controller;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    public static final SoundManager INSTANCE = new SoundManager();

    private Map<String, AudioClip> soundEffects = new HashMap<>();
    private MediaPlayer backgroundMusic;
    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    private double soundVolume = 0.7;
    private double musicVolume = 0.5;

    // Tên các file âm thanh
    public static final String SOUND_BOUNCE = "bounce.wav";
    public static final String SOUND_DESTROYED = "destroyed.wav";
    public static final String SOUND_LEVEL_COMPLETE = "levelComplete.wav";
    public static final String SOUND_PADDLE_BOUNCE = "paddleBounce.wav";
    public static final String MUSIC_BACKGROUND = "SoundBgr.ogg";

    private SoundManager() {
        loadSounds();
    }

    /**
     * Load tất cả âm thanh vào bộ nhớ
     */
    private void loadSounds() {
        try {
            // Load sound effects
            loadSound(SOUND_BOUNCE, "/sound/bounce.wav");
            loadSound(SOUND_DESTROYED, "/sound/destroyed.wav");
            loadSound(SOUND_LEVEL_COMPLETE, "/sound/levelComplete.wav");
            loadSound(SOUND_PADDLE_BOUNCE, "/sound/paddleBounce.wav");

            System.out.println("✅ Loaded " + soundEffects.size() + " sound effects");
        } catch (Exception e) {
            System.err.println("❌ Error loading sounds: " + e.getMessage());
        }
    }

    /**
     * Load một file âm thanh
     */
    private void loadSound(String key, String path) {
        try {
            URL resource = getClass().getResource(path);
            if (resource != null) {
                AudioClip clip = new AudioClip(resource.toString());
                clip.setVolume(soundVolume);
                soundEffects.put(key, clip);
            } else {
                System.err.println("⚠️ Sound file not found: " + path);
            }
        } catch (Exception e) {
            System.err.println("❌ Error loading sound " + path + ": " + e.getMessage());
        }
    }

    /**
     * Phát âm thanh hiệu ứng
     */
    public void playSound(String soundName) {
        if (!soundEnabled) return;

        AudioClip clip = soundEffects.get(soundName);
        if (clip != null) {
            clip.play();
        }
    }

    /**
     * Phát nhạc nền
     */
    public void playBackgroundMusic() {
        if (!musicEnabled) return;

        try {
            stopBackgroundMusic(); // Dừng nhạc cũ nếu có

            URL resource = getClass().getResource("/sound/" + MUSIC_BACKGROUND);
            if (resource != null) {
                Media media = new Media(resource.toString());
                backgroundMusic = new MediaPlayer(media);
                backgroundMusic.setVolume(musicVolume);
                backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // Lặp vô hạn
                backgroundMusic.play();
                System.out.println("🎵 Background music started");
            }
        } catch (Exception e) {
            System.err.println("❌ Error playing background music: " + e.getMessage());
        }
    }

    /**
     * Dừng nhạc nền
     */
    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
            backgroundMusic = null;
        }
    }

    /**
     * Tạm dừng nhạc nền
     */
    public void pauseBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.pause();
        }
    }

    /**
     * Tiếp tục nhạc nền
     */
    public void resumeBackgroundMusic() {
        if (backgroundMusic != null && musicEnabled) {
            backgroundMusic.play();
        }
    }

    // === GETTERS & SETTERS ===

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled) {
            stopBackgroundMusic();
        } else {
            playBackgroundMusic();
        }
    }

    public double getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(double volume) {
        this.soundVolume = Math.max(0.0, Math.min(1.0, volume));
        // Cập nhật volume cho tất cả sound effects
        for (AudioClip clip : soundEffects.values()) {
            clip.setVolume(this.soundVolume);
        }
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0.0, Math.min(1.0, volume));
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(this.musicVolume);
        }
    }

    /**
     * Cleanup khi đóng game
     */
    public void dispose() {
        stopBackgroundMusic();
        soundEffects.clear();
    }
}