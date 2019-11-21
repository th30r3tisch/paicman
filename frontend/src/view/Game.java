package view;
import controller.GameController;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Game implements ViewInterface {

    private Scene game;
    private GameController controller;

    /** Must inject a stage */
    public Game(Stage stage) {
        this.controller = new GameController(stage);
    }

    @Override
    public Scene getScene() {
        Group root = new Group();
        ArrayList<Shape> nodes = controller.createNodes();
        root.getChildren().addAll(controller.createNodes());
        controller.checkShapeIntersection(nodes.get(nodes.size() - 1));

        game = new Scene(root, 400, 400);

        return game;
    }
}
