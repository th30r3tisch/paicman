package controller;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import view.Game;

public class LoginController {

    private Stage stage;

    /** Inject the stage from {@link view.Login} */
    public LoginController(Stage stage) {
        this.stage = stage;
    }

    /** Submit name and load game world */
    public void submitName(ActionEvent event) {
        stage.setScene(new Game(stage).getScene());
    }
}
