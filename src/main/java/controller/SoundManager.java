package controller;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SoundManager {
    public static final SoundManager INSTANCE = new SoundManager();

    private Map<String, AudioClip> soundEffects = new HashMap<>();
    private MediaPlayer backgroundMusic;
    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    private double soundVolume = 0.7;
    private double musicVolume = 0.5;
    private String currentMusicTrack = null;

    // Tên các file âm thanh
    public static final String SOUND_BOUNCE = "bounce.wav";
    public static final String SOUND_DESTROYED = "destroyed.wav";
    public static final String SOUND_LEVEL_COMPLETE = "Qua_man.wav";
    public static final String SOUND_PADDLE_BOUNCE = "paddle.wav";
    // ✨ NEW SOUNDS
    public static final String SOUND_LIFE_LOST = "matmang.wav";
    public static final String SOUND_POWERUP = "powerup.wav";
    public static final String SOUND_STREAK = "streak.wav";
    public static final String SOUND_GOOD_JOB = "goodjob.wav";
    
    // Nhạc nền
    public static final String MUSIC_MENU = "Arkanoid_sound_menu.wav";
    public static final String MUSIC_GAME = "SoundBgr.ogg";

    private Random rng = new Random();

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
            loadSound(SOUND_DESTROYED, "/sound/gachvo.wav");
            loadSound(SOUND_LEVEL_COMPLETE, "/sound/Qua_man.wav");
            loadSound(SOUND_PADDLE_BOUNCE, "/sound/paddle.wav");
            // ✨ Load sound effects mới
            loadSound(SOUND_LIFE_LOST, "/sound/matmang.wav");
            loadSound(SOUND_POWERUP, "/sound/powerup.wav");
            loadSound(SOUND_GOOD_JOB, "/sound/goodjob.wav");
            loadSound(SOUND_STREAK, "/sound/streak.wav");
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
     * Phát nhạc nền - mặc định phát nhạc game
     */
    public void playBackgroundMusic() {
        playBackgroundMusic(MUSIC_GAME);
    }

    /**
     * Phát nhạc nền theo track cụ thể
     */
    public void playBackgroundMusic(String musicName) {
        if (!musicEnabled) return;

        // Nếu đang phát cùng track thì không làm gì
        if (currentMusicTrack != null && currentMusicTrack.equals(musicName)) {
            if (backgroundMusic != null && backgroundMusic.getStatus() == MediaPlayer.Status.PLAYING) {
                return;
            }
        }

        try {
            stopBackgroundMusic(); // Dừng nhạc cũ nếu có

            URL resource = getClass().getResource("/sound/" + musicName);
            if (resource != null) {
                Media media = new Media(resource.toString());
                backgroundMusic = new MediaPlayer(media);
                backgroundMusic.setVolume(musicVolume);
                backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // Lặp vô hạn
                backgroundMusic.play();
                currentMusicTrack = musicName;
                System.out.println("🎵 Background music started: " + musicName);
            } else {
                System.err.println("⚠️ Music file not found: " + musicName);
            }
        } catch (Exception e) {
            System.err.println("❌ Error playing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Phát nhạc menu
     */
    public void playMenuMusic() {
        playBackgroundMusic(MUSIC_MENU);
    }

    /**
     * Phát nhạc game
     */
    public void playGameMusic() {
        playBackgroundMusic(MUSIC_GAME);
    }

    /**
     * Dừng nhạc nền
     */
    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
            backgroundMusic = null;
            currentMusicTrack = null;
        }
    }

    /**
     * Fade out nhạc nền
     */
    public void fadeOutMusic(double seconds) {
        if (backgroundMusic != null) {
            javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                    new javafx.animation.KeyFrame(
                            javafx.util.Duration.seconds(seconds),
                            new javafx.animation.KeyValue(backgroundMusic.volumeProperty(), 0)
                    )
            );
            timeline.setOnFinished(e -> stopBackgroundMusic());
            timeline.play();
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

    // === CONVENIENCE METHODS ===
    
    public void playBounce() {
        playSound(SOUND_BOUNCE);
    }
    
    public void playPaddleHit() {
        playSound(SOUND_PADDLE_BOUNCE);
    }
    
    public void playBrickDestroy() {
        playSound(SOUND_DESTROYED);
    }
    
    public void playLevelComplete() {
        playSound(SOUND_LEVEL_COMPLETE);
    }
    
    public void playLifeLost() {
        playSound(SOUND_LIFE_LOST);
    }
    
    public void playPowerUpCollect() {
        playSound(SOUND_POWERUP);
    }
    
    public void playStreak() {
        playSound(SOUND_STREAK);
    }
    
    public void playGoodJob() {
        playSound(SOUND_GOOD_JOB);
    }

    /**
     * Cleanup khi đóng game
     */
    public void dispose() {
        stopBackgroundMusic();
        soundEffects.clear();
    }
}
