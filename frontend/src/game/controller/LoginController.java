package game.controller;

import game.Game;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import game.model.SceneName;

public class LoginController {

    private Stage stage;
    private ConnectionController cc;

    /** Inject the stage from {@link game.view.Login} */
    public LoginController(Stage stage, ConnectionController cc) {
        this.stage = stage;
        this.cc = cc;
    }

    /** Submit name and load game world */
    public void submitName(ActionEvent e, CharSequence nickname) {
        //cc.login(nickname.toString());
        stage.setScene(Game.getScenes().get(SceneName.WORLD));
    }
}
