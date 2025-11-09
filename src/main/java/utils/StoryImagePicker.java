package utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoryImagePicker {

    private static final Random random = new Random();

    /**
     * Trả về đường dẫn ngẫu nhiên (URL string) của 1 ảnh trong storymode/bg hoặc
     * chap.
     * 
     * @param folder "bg" hoặc "art_theo_chap"
     */
    public static String pickRandom(String folder) {
        try {
            // Lấy thư mục trong resources
            URL dirUrl = StoryImagePicker.class.getResource("/storymode/" + folder);
            if (dirUrl == null)
                return null;

            File dir = new File(dirUrl.toURI());
            if (!dir.exists())
                return null;

            // Lọc file ảnh (jpg/png)
            List<File> images = new ArrayList<>();
            for (File f : dir.listFiles()) {
                if (f.isFile() && (f.getName().endsWith(".png") || f.getName().endsWith(".jpg"))) {
                    images.add(f);
                }
            }

            if (images.isEmpty())
                return null;

            // Chọn ngẫu nhiên
            File chosen = images.get(random.nextInt(images.size()));

            return StoryImagePicker.class.getResource("/storymode/" + folder + "/" + chosen.getName()).toExternalForm();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
