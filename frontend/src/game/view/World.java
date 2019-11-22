package game.view;
import game.controller.WorldController;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class World implements ViewInterface {

    private Scene world;
    private WorldController controller;

    public World(WorldController wc) { this.controller = wc; }

    @Override
    public Scene getScene() {
        Group root = new Group();
        ArrayList<Shape> nodes = controller.createNodes();
        root.getChildren().addAll(controller.createNodes());
        controller.checkShapeIntersection(nodes.get(nodes.size() - 1));

        world = new Scene(root, 400, 400);

        return world;
    }
}
