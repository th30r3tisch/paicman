package game;

import game.controller.LoginController;
import game.controller.WorldController;
import game.controller.WorldScene;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import game.model.SceneName;
import game.view.Login;
import game.view.World;

import java.util.HashMap;

public class Game extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginController lc = new LoginController(primaryStage);

        primaryStage.setTitle("Conquer all towns");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> System.exit(0));
        primaryStage.setScene(new Login(lc).getScene());
        primaryStage.show();
    }
}