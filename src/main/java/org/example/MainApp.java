package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.event.treasurehunter.controller.TreasureHunterController;
import org.example.event.penaldo.controller.PenaldoController;
import org.example.event.universe.controller.UniverseController;
import org.example.event.castleattack.CastleAttackController;

public class MainApp extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        mainStage.setResizable(false);
        showMainMenu();
    }

    public static void showMainMenu() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/MainMenu.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        mainStage.setTitle("SPACE BREAKER - Main Menu");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void showLevelSelect() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/SelectLevel.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        mainStage.setTitle("SPACE BREAKER - Select Level");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void showGame(int levelIndex) throws Exception {
        showGame(levelIndex, false);
    }

    public static void showGame(int levelIndex, boolean continueGame) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Game.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        GameController controller = loader.getController();
        controller.startLevel(levelIndex, continueGame);
        mainStage.setTitle("SPACE BREAKER - Level " + levelIndex + (continueGame ? " (Continued)" : ""));
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void showShop() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Shop.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        mainStage.setTitle("SPACE BREAKER - Shop & Skins");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void showSettings() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Settings.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        mainStage.setTitle("SPACE BREAKER - Settings");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void showSelectEvent() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/SelectEvent.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        mainStage.setTitle("SPACE BREAKER - Select Event");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void showTreasureHunter(int levelIndex) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/TreasureHunter.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        TreasureHunterController controller = loader.getController();
        controller.getTreasureEngine().setOnWinCallback(controller::showEventWinDialog);
        controller.startLevel(levelIndex);
        mainStage.setTitle(" TREASURE HUNTER ");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void showPenaldo(int levelIndex) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Penaldo.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        PenaldoController controller = loader.getController();
        controller.getPenaldoEngine().setOnWinCallback(controller::showEventWinDialog);
        controller.startLevel(levelIndex);
        mainStage.setTitle(" PENALDO ");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void showUniverse(int levelIndex) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Universe.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        UniverseController controller = loader.getController();
        controller.getUniverseEngine().setOnWinCallback(controller::showEventWinDialog);
        controller.startLevel(levelIndex);
        mainStage.setTitle(" UNIVERSE ");
        mainStage.setScene(scene);
        mainStage.show();
    }
    
    public static void showCastle(int levelIndex) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Castle.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        CastleAttackController controller = loader.getController();
        controller.getCastleEngine().setOnWinCallback(controller::showEventWinDialog);
        controller.startLevel(levelIndex);
        mainStage.setTitle("CASTLE ATTACK");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
