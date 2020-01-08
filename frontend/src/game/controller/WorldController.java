package game.controller;

import game.model.TreeNode;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import game.view.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorldController {

    private ArrayList<Node> nodes = new ArrayList<>();
    private static Logger LOGGER = Logger.getLogger("InfoLogging");

    /**
     * Inject the stage from {@link World}
     */
    public WorldController() { }

    /** @return a ScrollPane which scrolls the layout. */
    public ScrollPane createScrollPane(Pane layout) {
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPannable(true);
        scroll.setContent(layout);
        return scroll;
    }

    public void getObjects(){
        try {
            ConnectionController.mapRequest();
        } catch (IOException e){
            LOGGER.log(Level.SEVERE, "Could not rend request: ", e);
        }
    }

    public void setNodes(ArrayList<TreeNode> nodes) {
        for (TreeNode node: nodes) {
            this.nodes.add(node.create());
        }
    }
}
