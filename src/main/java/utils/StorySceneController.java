package utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.List;

public class StorySceneController {

    @FXML
    private AnchorPane storyRootPane;
    @FXML
    private ImageView backgroundImageView;
    @FXML
    private Label storyTextLabel;
    @FXML
    private Button skipButton;

    private List<String> paragraphs;
    private Timeline pacingTimeline;
    private int currentParagraphIndex = 0;
    private boolean isTyping = false;
    private final double TYPING_SPEED = 0.05;

    @FXML
    public void initialize() {
        storyTextLabel.setText("");
        storyRootPane.setOnMouseClicked(e -> handleNextParagraph());
    }

    public void loadAndStartStory(int levelId) {

        String bgUrl = BackgroundSelector.getBackgroundUrl(levelId);

        if (bgUrl != null) {
            Image bgImage = new Image(bgUrl);
            backgroundImageView.setImage(bgImage);

            backgroundImageView.fitWidthProperty().bind(storyRootPane.widthProperty());
            backgroundImageView.fitHeightProperty().bind(storyRootPane.heightProperty());
        } else {
            System.err.println("StorySceneController: Không thể load ảnh nền cho level " + levelId);
        }

        this.paragraphs = StoryLoader.loadStory(levelId);
        if (paragraphs.isEmpty()) {
            System.err.println("StorySceneController: Không load được nội dung story cho level " + levelId);
            closeStoryScene();
            return;
        }
        currentParagraphIndex = 0;
        startTypingPacing();
    }

    private void startTypingPacing() {
        if (currentParagraphIndex >= paragraphs.size()) {
            closeStoryScene();
            return;
        }

        String originalParagraph = paragraphs.get(currentParagraphIndex);
        String paragraph = "    " + originalParagraph;

        storyTextLabel.setText("");
        isTyping = true;

        if (pacingTimeline != null)
            pacingTimeline.stop();
        pacingTimeline = new Timeline();

        final StringBuilder displayedText = new StringBuilder();
        for (int i = 0; i < paragraph.length(); i++) {
            final int charIndex = i;
            KeyFrame kf = new KeyFrame(Duration.seconds(i * TYPING_SPEED), e -> {
                displayedText.append(paragraph.charAt(charIndex));
                storyTextLabel.setText(displayedText.toString());
                if (charIndex == paragraph.length() - 1) {
                }
            });
            pacingTimeline.getKeyFrames().add(kf);
        }

        pacingTimeline.setOnFinished(e -> {
            isTyping = false;
        });

        pacingTimeline.play();
    }

    private void handleNextParagraph() {
        if (isTyping) {
            if (pacingTimeline != null)
                pacingTimeline.stop();
            String originalParagraph = paragraphs.get(currentParagraphIndex);
            storyTextLabel.setText("    " + originalParagraph);
            isTyping = false;
        } else {
            currentParagraphIndex++;
            startTypingPacing();
        }
    }

    @FXML
    private void handleSkipAll() {
        closeStoryScene();
    }

    private void closeStoryScene() {
        if (pacingTimeline != null)
            pacingTimeline.stop();
        Stage stage = (Stage) storyRootPane.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }
}