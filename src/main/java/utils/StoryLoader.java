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

    private static final String STORY_BASE_PATH = "/storrymode/story/eng/";
    private static final String END_MARKER = "end";
    // Ngưỡng ký tự ước tính cho 6 dòng text (55 ký tự/dòng * 6 dòng = 330)
    private static final int MAX_CHARS_PER_PARAGRAPH = 330;

    private static String getStoryFileName(int levelId) {
        if (levelId >= 1 && levelId <= 17) {
            return levelId + ".txt";
        }
        return null;
    }

    public static List<String> loadStory(int levelId) {
        String fileName = getStoryFileName(levelId);
        if (fileName == null) return List.of();

        List<String> paragraphs = new ArrayList<>();
        String fullPath = STORY_BASE_PATH + fileName;

        try (InputStream is = StoryLoader.class.getResourceAsStream(fullPath)) {
            if (is == null) {
                System.err.println("StoryLoader: File not found: " + fullPath);
                return List.of();
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().equalsIgnoreCase(END_MARKER)) break;
                    if (line.startsWith("\t")) {
                        String rawParagraph = line.substring(1);
                        paragraphs.addAll(splitLongParagraph(rawParagraph));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }

        return paragraphs;
    }

    /**
     * Chia một đoạn văn dài thành nhiều đoạn nhỏ hơn CHỈ dựa trên dấu chấm.
     */
    private static List<String> splitLongParagraph(String text) {
        List<String> result = new ArrayList<>();
        if (text.length() <= MAX_CHARS_PER_PARAGRAPH) {
            result.add(text.trim());
            return result;
        }

        // Regex CHỈ tìm dấu chấm kết thúc câu (.) theo sau là khoảng trắng
        Pattern sentenceEnd = Pattern.compile("(\\.)\\s+");
        Matcher matcher = sentenceEnd.matcher(text);

        int lastBreak = 0;
        int currentStart = 0;

        while (matcher.find()) {
            int potentialBreak = matcher.end();
            if (potentialBreak - currentStart <= MAX_CHARS_PER_PARAGRAPH) {
                lastBreak = potentialBreak;
            } else {
                if (lastBreak > currentStart) {
                    result.add(text.substring(currentStart, lastBreak).trim());
                    currentStart = lastBreak;
                    if (potentialBreak - currentStart <= MAX_CHARS_PER_PARAGRAPH) {
                         lastBreak = potentialBreak;
                    }
                } else {
                    // Trường hợp một câu quá dài không có dấu chấm
                    int forcedBreak = findSpaceBreak(text, currentStart, MAX_CHARS_PER_PARAGRAPH);
                    result.add(text.substring(currentStart, forcedBreak).trim());
                    currentStart = forcedBreak;
                    lastBreak = currentStart;
                    matcher.reset(text.substring(currentStart));
                }
            }
        }

        if (currentStart < text.length()) {
             String remaining = text.substring(currentStart);
             while (remaining.length() > MAX_CHARS_PER_PARAGRAPH) {
                 int forcedBreak = findSpaceBreak(remaining, 0, MAX_CHARS_PER_PARAGRAPH);
                 result.add(remaining.substring(0, forcedBreak).trim());
                 remaining = remaining.substring(forcedBreak);
             }
             result.add(remaining.trim());
        }

        return result;
    }

    private static int findSpaceBreak(String text, int start, int maxLen) {
        int end = Math.min(start + maxLen, text.length());
        int lastSpace = text.lastIndexOf(' ', end);
        if (lastSpace > start) {
            return lastSpace + 1;
        }
        return end;
    }
}