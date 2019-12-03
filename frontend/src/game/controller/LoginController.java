package game.controller;

import game.Game;
import game.model.Player;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import game.model.SceneName;

public class LoginController {

    private Stage stage;
    private ConnectionController cc;
    private Player player;

    /** Inject the stage from {@link game.view.Login} */
    public LoginController(Stage stage) {
        this.stage = stage;
    }

    /** Submit name and load game world */
    public void submitName(ActionEvent e, CharSequence nickname, CharSequence serverIP) {
        player = new Player();
        player.setName(nickname.toString());
        cc = new ConnectionController(player, serverIP.toString());
        Thread t = new Thread(cc);
        t.start();
        //if (cc.isConnected()){
            stage.setScene(Game.getScenes().get(SceneName.WORLD));
        //}
    }
}
