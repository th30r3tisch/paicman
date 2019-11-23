package game;

import game.controller.ConnectionController;
import game.controller.LoginController;
import game.controller.WorldController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import game.model.SceneName;
import game.view.Login;
import game.view.World;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Game extends Application {

    private static HashMap<SceneName, Scene> scenes = new HashMap<>();

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parameters params = getParameters();
        List<String> argsList = params.getRaw();
        ConnectionController cc = new ConnectionController(primaryStage, argsList.get(0));
        LoginController lc = new LoginController(primaryStage, cc);
        WorldController wc = new WorldController(primaryStage, cc);

        scenes.put(SceneName.LOGIN, new Login(lc).getScene()) ;
        scenes.put(SceneName.WORLD, new World(wc).getScene());

        primaryStage.setTitle("Conquer all towns");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setScene(scenes.get(SceneName.LOGIN));

        primaryStage.show();
        //cc.run();
    }

    /** Returns a Map of the scenes by {@link SceneName} */
    public static HashMap<SceneName, Scene> getScenes() {
        return scenes;
    }
}