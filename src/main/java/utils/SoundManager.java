package utils;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    private SoundManager() {}


    public void loadSounds() {
        try {
            URL musicUrl = getClass().getResource(SOUND_DIR + BACKGROUND_MUSIC);
            if (musicUrl != null) {
                Media media = new Media(musicUrl.toExternalForm());
                backgroundMusicPlayer = new MediaPlayer(media);
                backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); 
                backgroundMusicPlayer.setVolume(0.5); 
                backgroundMusicPlayer.play();
                System.out.println("SoundManager: Đã phát nhạc nền " + BACKGROUND_MUSIC);
            } else {
                System.err.println("SoundManager: Không tìm thấy file nhạc nền: " + BACKGROUND_MUSIC);
            }

            for (String sfxFile : SFX_FILES) {
                URL sfxUrl = getClass().getResource(SOUND_DIR + sfxFile);
                if (sfxUrl != null) {
                    AudioClip clip = new AudioClip(sfxUrl.toExternalForm());
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

    public void playSoundEffect(String name) {
        AudioClip clip = soundEffects.get(name);
        if (clip != null) {
            clip.play(); 
        } else {
            System.err.println("SoundManager: Yêu cầu phát âm thanh không tồn tại: " + name);
        }
    }

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