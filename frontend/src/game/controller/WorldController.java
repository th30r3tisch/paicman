package game.controller;

import game.model.TreeNode;
import game.model.WorldModel;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import game.view.World;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorldController {

    private static Logger LOGGER = Logger.getLogger("InfoLogging");
    public StackPane stack = new StackPane();
    public WorldModel wm;
    public Group group;

    /**
     * Inject the stage from {@link World}
     */
    public WorldController() {
        this.wm = new WorldModel();
        startUpdater();
        this.group = new Group();
    }

    /**
     * @return a ScrollPane which scrolls the layout.
     */
    public ScrollPane createScrollPane(Pane layout) {
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPannable(true);
        scroll.setContent(layout);
        return scroll;
    }

    public void getObjects() {
        try {
            ConnectionController.mapRequest();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not rend request: ", e);
        }
    }

    public void setNodes(ArrayList<TreeNode> nodes) {
        for (TreeNode node : nodes) {
            wm.addShape(node.create());
        }
    }

    public void startUpdater(){
        Thread thread = new Thread(() -> {
            Runnable updater = () -> {
                group.getChildren().clear();
                group.getChildren().addAll( wm.getShapes());
            };

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
                // UI update is run on the Application thread
                Platform.runLater(updater);
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
