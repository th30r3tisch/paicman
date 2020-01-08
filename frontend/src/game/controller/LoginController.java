package game.controller;

import game.model.Player;
import game.view.World;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginController {

    private Stage stage;
    private ConnectionController cc;
    private WorldController wc;
    private Player player;

    /** Inject the stage from {@link game.view.Login} */
    public LoginController(Stage stage, WorldController wc) {
        this.wc = wc;
        this.stage = stage;
    }

    /** Submit name and load game world */
    public void submitName(ActionEvent e, CharSequence nickname, CharSequence serverIP) {
        player = new Player();
        player.setName(nickname.toString());
        cc = new ConnectionController(player, serverIP.toString(), wc, stage);
        Thread t = new Thread(cc);
        t.start();

    }
}
