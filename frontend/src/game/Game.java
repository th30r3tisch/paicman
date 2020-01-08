package game;

import game.controller.LoginController;
import javafx.application.Application;
import javafx.stage.Stage;
import game.view.Login;

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