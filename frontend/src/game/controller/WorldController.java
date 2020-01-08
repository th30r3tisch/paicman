package game.controller;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import game.view.World;

import java.io.IOException;
import java.util.ArrayList;

public class WorldController {

    private ArrayList<Shape> nodes;

    /**
     * Inject the stage from {@link World}
     */
    public WorldController() {
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

    public ArrayList<Shape> getObjects(){
        nodes = new ArrayList<>();
        try {
            ConnectionController.getMap();
        } catch (IOException e){

        }

        return nodes;
    }
}
