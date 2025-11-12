package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StoryLoader {

    private static final String STORY_BASE_PATH = "/storymode/story/";
    private static final String END_MARKER = "end";

    private static final int MAX_CHARS_PER_PARAGRAPH = 280;

    private static final Pattern SENTENCE_SPLITTER = Pattern.compile("([^.!?]+\\.)|([^.!?]+[!?])");

    private static String getStoryFileName(int levelId) {
        if (levelId >= 1 && levelId <= 16) {
            return levelId + ".txt";
        }
        return null;
    }

    public static List<String> loadStory(int levelId) {
        String fileName = getStoryFileName(levelId);
        if (fileName == null) {
            System.err.println("Không tìm thấy file story cho Level ID: " + levelId);
            return List.of();
        }

        List<String> paragraphs = new ArrayList<>();
        String fullPath = STORY_BASE_PATH + fileName;

        try (InputStream is = StoryLoader.class.getResourceAsStream(fullPath)) {

            if (is == null) {
                System.err.println("StoryLoader: File not found at resource path: " + fullPath);
                return List.of();
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;

                while ((line = reader.readLine()) != null) {

                    int markerIndex = line.indexOf("ttt");

                    if (markerIndex != -1) {

                        String content = line.substring(markerIndex + 3).trim();

                        if (content.equalsIgnoreCase(END_MARKER)) {
                            break;
                        }

                        if (content.isEmpty()) {
                            continue;
                        }

                        if (content.length() <= MAX_CHARS_PER_PARAGRAPH) {
                            paragraphs.add(content);
                        } else {
                            splitAndAddParagraphs(content, paragraphs);
                        }
                    }
                }

            }

        } catch (Exception e) {
            System.err.println("Lỗi khi đọc file story: " + fullPath);
            e.printStackTrace();
            return List.of();
        }

        return paragraphs;
    }

    private static void splitAndAddParagraphs(String longParagraph, List<String> paragraphs) {
        Matcher matcher = SENTENCE_SPLITTER.matcher(longParagraph);
        StringBuilder currentChunk = new StringBuilder();

        int lastEnd = 0;
        while (matcher.find()) {
            String sentence = matcher.group().trim(); // Lấy cả câu
            lastEnd = matcher.end();

            if (!currentChunk.isEmpty() && currentChunk.length() + sentence.length() + 1 > MAX_CHARS_PER_PARAGRAPH) {
                paragraphs.add(currentChunk.toString().trim());
                currentChunk = new StringBuilder(sentence);
            } else {
                if (!currentChunk.isEmpty()) {
                    currentChunk.append(" ");
                }
                currentChunk.append(sentence);
            }
        }

        String remaining = longParagraph.substring(lastEnd).trim();
        if (!remaining.isEmpty()) {
            if (!currentChunk.isEmpty() && currentChunk.length() + remaining.length() + 1 > MAX_CHARS_PER_PARAGRAPH) {
                paragraphs.add(currentChunk.toString().trim());
                paragraphs.add(remaining);
            } else {
                if (!currentChunk.isEmpty()) {
                    currentChunk.append(" ");
                }
                currentChunk.append(remaining);
            }
        }

        if (!currentChunk.isEmpty()) {
            paragraphs.add(currentChunk.toString().trim());
        }
    }
}