package game.controller;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import game.view.World;

public class WorldController {

    private Stage stage;

    /**
     * Inject the stage from {@link World}
     */
    public WorldController(Stage stage) {
        this.stage = stage;
    }

    /** @return a ScrollPane which scrolls the layout. */
    public ScrollPane createScrollPane(Pane layout) {
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPannable(true);
        scroll.setContent(layout);
        return scroll;
    }
}
