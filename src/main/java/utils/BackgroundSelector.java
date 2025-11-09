package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Chọn background cho game:
 * - Nếu USE_STORYMODE = true → chọn ngẫu nhiên ảnh từ storymode/bg,
 * storymode/art_theo_chap, hoặc storymode
 * - Nếu false → fallback ảnh mặc định bg-retrospace(1).png
 */
public class BackgroundSelector {
    private static final boolean USE_STORYMODE = true; // hiện tại luôn dùng storymode
    private static final Random random = new Random();

    public static String getBackgroundUrl() {
        if (USE_STORYMODE) {
            // Danh sách thư mục để chọn
            List<String> folders = new ArrayList<>();
            folders.add("bg");
            folders.add("chap");

            // Chọn ngẫu nhiên một folder trong 2 cái
            String chosenFolder = folders.get(random.nextInt(folders.size()));
            String url = StoryImagePicker.pickRandom(chosenFolder);

            if (url != null)
                return url;
        }

        // fallback
        return BackgroundSelector.class.getResource("/images/bg-retrospace(1).png").toExternalForm();
    }
}
