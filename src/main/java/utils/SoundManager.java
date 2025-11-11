package utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * SoundManager (Singleton)
 * Bộ máy âm thanh chạy nền, chịu trách nhiệm tải, phát nhạc nền (OGG)
 * và quản lý các hiệu ứng âm thanh (WAV).
 * File này không cần gọi trực tiếp, AudioPlayer sẽ gọi nó.
 */
public class SoundManager {

    public static final SoundManager INSTANCE = new SoundManager();

    private MediaPlayer backgroundMusicPlayer;
    private final Map<String, AudioClip> soundEffects = new HashMap<>();

    private static final String SOUND_DIR = "/sound/";
    private static final String BACKGROUND_MUSIC = "SoundBgr.ogg";
    private static final String[] SFX_FILES = {
            "bounce.wav",
            "destroyed.wav",
            "levelComplete.wav",
            "paddleBounce.wav"
    };

    private SoundManager() {
        // Private constructor cho Singleton
    }

    /**
     * Tải tất cả âm thanh.
     * Tự động phát nhạc nền (OGG) và tải hiệu ứng (WAV).
     */
    public void loadSounds() {
        try {
            // 1. Tải và phát nhạc nền (OGG)
            URL musicUrl = getClass().getResource(SOUND_DIR + BACKGROUND_MUSIC);
            if (musicUrl != null) {
                Media media = new Media(musicUrl.toExternalForm());
                backgroundMusicPlayer = new MediaPlayer(media);
                backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lặp lại vô hạn
                backgroundMusicPlayer.setVolume(0.5); // Chỉnh âm lượng nhạc nền (ví dụ 50%)
                backgroundMusicPlayer.play();
                System.out.println("SoundManager: Đã phát nhạc nền " + BACKGROUND_MUSIC);
            } else {
                System.err.println("SoundManager: Không tìm thấy file nhạc nền: " + BACKGROUND_MUSIC);
            }

            // 2. Tải trước các hiệu ứng âm thanh (WAV)
            for (String sfxFile : SFX_FILES) {
                URL sfxUrl = getClass().getResource(SOUND_DIR + sfxFile);
                if (sfxUrl != null) {
                    AudioClip clip = new AudioClip(sfxUrl.toExternalForm());
                    // Lấy tên file làm key (ví dụ: "bounce")
                    String key = sfxFile.replace(".wav", "");
                    soundEffects.put(key, clip);
                    System.out.println("SoundManager: Đã tải SFX: " + sfxFile);
                } else {
                    System.err.println("SoundManager: Không tìm thấy file SFX: " + sfxFile);
                }
            }
        } catch (Exception e) {
            System.err.println("SoundManager: Lỗi khi tải âm thanh!");
            e.printStackTrace();
        }
    }

    /**
     * Phát một hiệu ứng âm thanh (WAV) dựa theo tên.
     * @param name Tên của âm thanh (ví dụ: "bounce")
     */
    public void playSoundEffect(String name) {
        AudioClip clip = soundEffects.get(name);
        if (clip != null) {
            clip.play(); // Phát âm thanh
        } else {
            System.err.println("SoundManager: Yêu cầu phát âm thanh không tồn tại: " + name);
        }
    }

    /**
     * Dừng toàn bộ âm thanh (khi thoát game).
     */
    public void stopAllSounds() {
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }
        for (AudioClip clip : soundEffects.values()) {
            clip.stop();
        }
        soundEffects.clear();
        System.out.println("SoundManager: Đã dừng toàn bộ âm thanh.");
    }
}