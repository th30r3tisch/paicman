package game.controller;

import game.model.Player;
import game.model.Town;
import game.model.TreeNode;
import game.model.WorldModel;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import game.view.World;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorldController {

    private static Logger LOGGER = Logger.getLogger("InfoLogging");
    public StackPane stack = new StackPane();
    public WorldModel wm;
    public Group group;
    public World world;

    /**
     * Inject the stage from {@link World}
     */
    public WorldController() {
        this.wm = new WorldModel();
        startUpdater();
        this.group = new Group();
        world = new World(this);
    }

    /**
     * @return a ScrollPane which scrolls the layout.
     */
    public ScrollPane createScrollPane(Pane layout) {
        ScrollPane scroll = new ScrollPane();
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setPannable(false);
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
            wm.addNodes(node);
        }
    }

    public ArrayList<TreeNode> getTreeNodes() {
        return wm.getTreeNodes();
    }

    public ArrayList<Shape> setUpShapes() {
        ArrayList<Shape> shapes = new ArrayList<>();
        for (TreeNode tn : wm.getTreeNodes()) {
            if (tn instanceof Town) {

                //System.out.println("can be dragged");
                Town t = (Town) tn;
                Shape s = t.create();
                //todo check if playertown and add events
                Player player = ConnectionController.getPlayer();
                //if (t.getOwner() == player) {
                    final Delta dragDelta = new Delta();
                    s.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            // record a delta distance for the drag and drop operation.
                            dragDelta.x = s.getLayoutX() - mouseEvent.getSceneX();
                            dragDelta.y = s.getLayoutY() - mouseEvent.getSceneY();
                            s.setCursor(Cursor.NONE);
                            world.updateTownDisplay(t);

                            //todo colo
                            s.setStrokeWidth(3);
                            s.setStroke(Color.RED);
                        }
                    });

                    s.setOnMouseDragged(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            s.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                            s.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                            checkShapeIntersection(s);
                        }
                    });
                //}
                shapes.add(s);
            } else {
                shapes.add(tn.create());
            }
        }
        wm.setShapes(shapes);
        return shapes;
    }

    public void startUpdater() {

        Thread thread = new Thread(() -> {
            Runnable updater = () -> {
                if(!group.getChildren().equals(setUpShapes())){
                    group.getChildren().clear();
                    group.getChildren().addAll(setUpShapes());
                    System.out.println("updating");
                }
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

    public void checkShapeIntersection(Shape block) {
        boolean collisionDetected = false;
        //System.out.println("moving");
        ArrayList<Shape> shapes = wm.getShapes();
        for (Shape static_bloc :  shapes){
            if (static_bloc != block) {
                System.out.println("check block " + static_bloc.getTranslateX());
                static_bloc.setFill(static_bloc.getFill());

                Shape intersect = Shape.intersect(block, static_bloc);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    collisionDetected = true;
                    System.out.println("intersection");
                }
            }
        }

        if (collisionDetected) {
            block.setFill(Color.BLUE);
        } else {
            int i = shapes.indexOf(block);
        }
    }


    public Scene getScene(){
        return world.getScene();
    }


    class Delta {
        double x, y;
    }

}
