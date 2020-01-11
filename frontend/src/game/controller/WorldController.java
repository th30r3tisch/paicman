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
import javafx.scene.shape.Line;
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

    public void addNodes(ArrayList<TreeNode> nodes) {
        for (TreeNode node : nodes) {
            wm.addNodes(node);
        }
    }

    public ArrayList<TreeNode> getTreeNodes() {
        return wm.getTreeNodes();
    }

    private Town currentSelect;

    public ArrayList<Shape> setUpShapes() {
        ArrayList<Shape> shapes = new ArrayList<>();
        for (TreeNode tn : wm.getTreeNodes()) {
            if (tn instanceof Town) {

                //System.out.println("can be dragged");
                Town town = (Town) tn;
                Shape shape = town.create();
                //todo check if playertown and add events
                Player player = ConnectionController.getPlayer();

                if(currentSelect == town){
                    shape.setStrokeWidth(3);
                    shape.setStroke(Color.RED);
                }
                //if (t.getOwner() == player) {
                    final Delta dragDelta = new Delta();
                    shape.setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            // record a delta distance for the drag and drop operation.
                            dragDelta.x = shape.getLayoutX() - mouseEvent.getSceneX();
                            dragDelta.y = shape.getLayoutY() - mouseEvent.getSceneY();
                            shape.setCursor(Cursor.MOVE);
                            world.updateTownDisplay(town);
                            System.out.println("click event");

                            if(currentSelect == null) {
                                currentSelect = town;
                                shape.setStrokeWidth(3);
                                shape.setStroke(Color.RED);
                            } else if(currentSelect != town){
                                town.addConqueredByTown(currentSelect);
                                //TODO add check if legal move
                                for (Town attacker: town.getConqueredByTowns()) {
                                    Line line = new Line(attacker.getX(), attacker.getY(), town.getX(), town.getY());
                                    line.setStrokeWidth(5);
                                    lineList.add(line);
                                }
                                currentSelect = null;
                            }
                            else currentSelect = null;

                        }
                    });
                shapes.add(shape);
            } else {
                shapes.add(tn.create());
            }
        }
        wm.setShapes(shapes);
        return shapes;
    }

    ArrayList<Shape> lineList = new ArrayList<>();
    public void startUpdater() {

        Thread thread = new Thread(() -> {
            Runnable updater = () -> {
                if(!group.getChildren().equals(setUpShapes())){
                    group.getChildren().clear();
                    group.getChildren().addAll(setUpShapes());
                    group.getChildren().addAll(lineList);
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
