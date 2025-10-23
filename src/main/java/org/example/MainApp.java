package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        showMainMenu(); 
    }

    public static void showMainMenu() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/MainMenu.fxml"));
        Scene scene = new Scene(loader.load());
        mainStage.setTitle("ARKANOID - Main Menu");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void showLevelSelect() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/SelectLevel.fxml"));
        Scene scene = new Scene(loader.load());
        mainStage.setTitle("Select Level");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void showGame(int levelIndex) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/sample.fxml")); // CHÚ Ý đường dẫn
        Scene scene = new Scene(loader.load());
        GameController controller = loader.getController();
        controller.startLevel(levelIndex);
        mainStage.setTitle("Arkanoid v2");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void showShop() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Shop.fxml"));
        Scene scene = new Scene(loader.load());
        mainStage.setScene(scene);
    }

    public static void showSettings() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Settings.fxml"));
        Scene scene = new Scene(loader.load());
        mainStage.setScene(scene);
    }

    public static void showSkins() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Skin.fxml"));
        Scene scene = new Scene(loader.load());
        mainStage.setTitle("Skins");
        mainStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
