package game.view;
import game.controller.WorldController;
import game.model.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Shape;

import java.util.ArrayList;

public class World implements ViewInterface {

    private Scene world;
    private WorldController controller;
    private int windowWidth = 500;
    private int windowHeight = 400;
    private ArrayList<Player> playerList;

    public World(WorldController wc) { this.controller = wc; }

    @Override
    public Scene getScene() {
        Group root = new Group();
        ArrayList<Shape> nodes = controller.createNodes();
        root.getChildren().addAll(controller.createNodes());
        controller.checkShapeIntersection(nodes.get(nodes.size() - 1));

        world = new Scene(root, windowWidth, windowHeight);
        //game loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                //add update method
            }
        };
        timer.start();
        return world;
    }

    //maybe move away from class for mvc
    private void onUpdate(){
        //TODO
        //CHeck ech players units and check if they collide with each other for eg damage calc
            //they kill each other
            //if collide with town decrease towns hp
        //check if collectors collide with materials or food
            //if yes then let them go back to town
    }
}
