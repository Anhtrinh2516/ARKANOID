package core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static Stage stage;

    @Override
    public void start(Stage s) throws Exception {
        stage = s;
        stage.setResizable(false);
        showMainMenu();
    }

    public static void showMainMenu() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/MainMenu.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        stage.setTitle("SPACE BREAKER");
        stage.setScene(scene);
        stage.show();
    }

    public static void showLevelSelect() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/SelectLevel.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        stage.setTitle("SPACE BREAKER");
        stage.setScene(scene);
        stage.show();
    }

    public static void showGame(int lvl) throws Exception {
        showGame(lvl, false);
    }

    public static void showGame(int lvl, boolean cont) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Game.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        GameController c = loader.getController();
        c.startLevel(lvl, cont);
        stage.setTitle("SPACE BREAKER - Level " + lvl);
        stage.setScene(scene);
        stage.show();
    }

    public static void showShop() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Shop.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        stage.setTitle("SPACE BREAKER - Shop");
        stage.setScene(scene);
        stage.show();
    }

    public static void showSettings() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Settings.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        stage.setTitle("SPACE BREAKER - Settings");
        stage.setScene(scene);
        stage.show();
    }

    public static void showSelectEvent() throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/SelectEvent.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        stage.setTitle("SPACE BREAKER - Events");
        stage.setScene(scene);
        stage.show();
    }

    public static void showTreasureHunter(int lvl) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/TreasureHunter.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        
        event.treasurehunter.controller.TreasureHunterController c = loader.getController();
        c.getTreasureEngine().setOnWinCallback(c::showEventWinDialog);
        c.startLevel(lvl);
        
        stage.setTitle("TREASURE HUNTER - Level " + lvl);
        stage.setScene(scene);
        stage.show();
    }

    public static void showPenaldo(int lvl) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Penaldo.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        
        event.penaldo.controller.PenaldoController c = loader.getController();
        c.getPenaldoEngine().setOnWinCallback(c::showEventWinDialog);
        c.startLevel(lvl);
        
        stage.setTitle("PENALDO - Level " + lvl);
        stage.setScene(scene);
        stage.show();
    }

    public static void showUniverse(int lvl) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Universe.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        
        event.universe.controller.UniverseController c = loader.getController();
        c.getUniverseEngine().setOnWinCallback(c::showEventWinDialog);
        c.startLevel(lvl);
        
        stage.setTitle("UNIVERSE - Level " + lvl);
        stage.setScene(scene);
        stage.show();
    }

    public static void showCastle(int lvl) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fxml/Castle.fxml"));
        Scene scene = new Scene(loader.load(), 1148, 708);
        
        event.castleattack.CastleAttackController c = loader.getController();
        c.getCastleEngine().setOnWinCallback(c::showEventWinDialog);
        c.startLevel(lvl);
        
        stage.setTitle("CASTLE ATTACK - Level " + lvl);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}