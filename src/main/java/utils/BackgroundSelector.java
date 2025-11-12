package utils;

public class BackgroundSelector {
    private static final String FALLBACK_IMAGE = "/images/bg-retrospace.png";

    public static String getBackgroundUrl(int levelIndex) {
        String url;

        switch (levelIndex) {
            case 2:
                url = StoryImagePicker.getSpecificImage("chap", "chap2.png");
                break;
            case 8:
                url = StoryImagePicker.getSpecificImage("chap", "chap8.jpg");
                break;
            case 10:
                url = StoryImagePicker.getSpecificImage("chap", "chap10.jpg");
                break;
            default:
                url = StoryImagePicker.pickRandom("bg");
                break;
        }

        if (url == null) {
            System.err
                    .println("BackgroundSelector: Không tìm thấy ảnh cho level " + levelIndex + ". Dùng ảnh dự phòng.");
            try {
                url = BackgroundSelector.class.getResource(FALLBACK_IMAGE).toExternalForm();
            } catch (Exception e) {
                System.err.println("BackgroundSelector: Ảnh dự phòng cũng lỗi!");
                return null;
            }
        }

        return url;
    }
}