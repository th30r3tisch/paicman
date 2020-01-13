package game.controller;

import game.model.Player;
import game.model.map.Quadtree;
import game.model.map.Town;
import game.model.map.TreeNode;
import game.model.map.WorldModel;

import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import game.view.World;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
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

    public void addQuadTree(Quadtree quadtree) {
        this.wm.setQuadtree(quadtree);
    }

    private Town currentSelect;

    /**
     * set up logic vor villages on Map
     * @return the object on map
     */
    public ArrayList<Shape> setUpShapes() {
        ArrayList<Shape> shapes = new ArrayList<>();
        for (TreeNode tn : wm.getTreeNodes()) {
            if (tn instanceof Town) {

                //System.out.println("can be dragged");
                Town town = (Town) tn;
                Shape shape = town.create();
                //todo check if playertown and add events
                Player player = ConnectionController.getPlayer();

                if (currentSelect == town) {
                    shape.setStrokeWidth(5);
                    shape.setStroke(Color.RED);
                    world.updateTownDisplay(town);
                }

                //if (t.getOwner() == player) {
                shape.setOnMousePressed(mouseEvent -> {
                    shape.setCursor(Cursor.MOVE);
                    world.updateTownDisplay(town);
                    System.out.println("click event");
                    if (currentSelect == null) {
                        currentSelect = town;
                        shape.setStrokeWidth(3);
                        shape.setStroke(Color.RED);
                    } else if (currentSelect != town) {
                        town.addConqueredByTown(currentSelect);
                        //TODO add check if legal move
                        currentSelect = null;
                    } else currentSelect = null;
                });

                if (town.getConqueredByTowns().size() > 0) {
                    for (Town attacker : town.getConqueredByTowns()) {
                        Line line = new Line(attacker.getX(), attacker.getY(), town.getX(), town.getY());
                        line.setOnMouseClicked(mouseEvent -> {
                            MouseButton button = mouseEvent.getButton();
                            switch (button) {
                                case SECONDARY:
                                    System.out.println("right click");
                                    ArrayList<TreeNode> treeNodes = wm.getTreeNodes();
                                    Town attacker1 = null;
                                    Town attacked = null;
                                    int counter = 0;
                                    while (attacker1 == null || attacked == null) {
                                        if (treeNodes.get(counter) instanceof Town) {
                                            Town currentTown = (Town) treeNodes.get(counter);
                                            System.out.println("line y " + currentTown.getY());
                                            if (currentTown.getX() == line.getStartX() && currentTown.getY() == line.getStartY())
                                                attacker1 = currentTown;
                                            else if (currentTown.getX() == line.getEndX() && currentTown.getY() == line.getEndY())
                                                attacked = currentTown;
                                        }
                                        counter++;
                                    }
                                    attacked.removeConqueredByTown(attacker1);
                                    break;
                                default:
                                    System.out.println("something click");
                            }
                        });
                        line.setStrokeWidth(8);
                        shapes.add(line);
                    }
                }
                shapes.add(shape);
            } else {
                shapes.add(tn.create());
            }
        }
        wm.setShapes(shapes);
        return shapes;
    }

    public void startUpdater() {
        long startTime = System.currentTimeMillis();
        Thread thread = new Thread(() -> {
            Runnable updater = () -> {
                if (!group.getChildren().equals(setUpShapes())) {
                    group.getChildren().clear();
                    //update health of all villages
                    updateHealth(startTime);
                    group.getChildren().addAll(setUpShapes());
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


    /**
     * Update health of villages according to attack/recover ticks
     * @param startTime
     */

    private void updateHealth(long startTime) {

        //find all towns
        for (TreeNode treeNode : wm.getTreeNodes()) {
            if (treeNode instanceof Town) {
                Town town = (Town) treeNode;
                ArrayList<AbstractMap.SimpleEntry<Town, Long>> conquerers = town.getConqueredByTownEntries();
                ArrayList<Town> toRemove = new ArrayList<>();

                //check if town is under attack
                if(conquerers != null && conquerers.size() > 0) {
                    for (AbstractMap.SimpleEntry<Town, Long> conquerer : conquerers) {

                        //every passing two seconds town loses health
                        if ((System.currentTimeMillis() - conquerer.getValue().longValue()) % 3000 >= 2000) {
                            //update attack damage
                            town.setLife(town.getLife() - 1);
                            conquerer.getKey().setLife(conquerer.getKey().getLife() - 1);
                            //if health of attacker reaches 0 stop attack
                            //if conqueror has 0 or less hp, attack will be abortet
                            if (conquerer.getKey().getLife() <= 0) {
                                toRemove.add(conquerer.getKey());
                            }

                            //TODO if town has 0 hp abort all attack and flag and set owner
                            if(town.getLife() <= 0){
                                break;
                            }
                        }
                    }
                    //if town was defeatet remove all attacker
                    if(town.getLife() <= 0){
                        town.getConqueredByTowns().forEach(attacker -> {
                            town.removeConqueredByTown(attacker);
                        });
                    } else {
                        //remove all attacker, that can not attack anymore
                        for (Town remove : toRemove) {
                            town.removeConqueredByTown(remove);
                        }
                    }
                }
                if ((System.currentTimeMillis() - startTime)  % 5000 >= 4000) {
                    town.setLife(town.getLife() + 1);
                }
            }
        }
    }


    public Scene getScene() {
        return world.getScene();
    }
}
