package game;

import game.controller.LoginController;
import game.controller.WorldController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import game.model.SceneName;
import game.view.Login;
import game.view.World;

import java.util.HashMap;

public class Game extends Application {

    private static HashMap<SceneName, Scene> scenes = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        LoginController lc = new LoginController(primaryStage);
        WorldController wc = new WorldController(primaryStage);

        scenes.put(SceneName.LOGIN, new Login(lc).getScene()) ;
        scenes.put(SceneName.WORLD, new World(wc).getScene());

        primaryStage.setTitle("Conquer all towns");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setScene(scenes.get(SceneName.LOGIN));

        primaryStage.show();
    }

    /** Returns a Map of the scenes by {@link SceneName} */
    public static HashMap<SceneName, Scene> getScenes() {
        return scenes;
    }
}