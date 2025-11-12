package storycontroller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoryImagePicker {

    private static final Random random = new Random();

    public static String pickRandom(String folder) {
        try {
            String resourcePath = "/storymode/" + folder;
            URL dirUrl = StoryImagePicker.class.getResource(resourcePath);
            if (dirUrl == null) {
                System.err.println("StoryImagePicker: Không tìm thấy thư mục: " + resourcePath);
                return null;
            }

            File dir = new File(dirUrl.toURI());
            if (!dir.exists() || !dir.isDirectory()) {
                System.err.println("StoryImagePicker: Đường dẫn không phải thư mục: " + dir.getPath());
                return null;
            }

            List<File> images = new ArrayList<>();
            File[] files = dir.listFiles();
            if (files == null) {
                System.err.println("StoryImagePicker: Không đọc được file trong thư mục: " + dir.getPath());
                return null;
            }

            for (File f : files) {
                String name = f.getName().toLowerCase();
                if (f.isFile() && (name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"))) {
                    images.add(f);
                }
            }

            if (images.isEmpty()) {
                System.err.println("StoryImagePicker: Không tìm thấy ảnh trong: " + resourcePath);
                return null;
            }

            File chosen = images.get(random.nextInt(images.size()));

            return StoryImagePicker.class.getResource(resourcePath + "/" + chosen.getName()).toExternalForm();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSpecificImage(String folder, String filename) {
        try {
            String resourcePath = "/storymode/" + folder + "/" + filename;
            URL imageUrl = StoryImagePicker.class.getResource(resourcePath);
            if (imageUrl == null) {
                System.err.println("StoryImagePicker: Không tìm thấy file ảnh cụ thể: " + resourcePath);
                return null;
            }
            return imageUrl.toExternalForm();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}