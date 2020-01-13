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
        Player player = ConnectionController.getPlayer();
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

                shape.setOnMousePressed(mouseEvent -> {
                    shape.setCursor(Cursor.MOVE);
                    world.updateTownDisplay(town);
                    System.out.println("click event");
                    if (currentSelect == null) {
                        currentSelect = town;
                        shape.setStrokeWidth(3);
                        shape.setStroke(Color.RED);
                    } else if (currentSelect != town) {
                        //previous town was selected that belongs to player attack if not reselect to new town
                        if((currentSelect.getOwner() != null && currentSelect.getOwner().getName().equals(player.getName())) && !town.getConqueredByTowns().contains(currentSelect)) {
                            town.addConqueredByTown(currentSelect);
                            currentSelect = null;
                        } else {
                            currentSelect = town;
                        }
                    } else currentSelect = null;
                });

                if (town.getConqueredByTowns().size() > 0) {
                    for (Town attacker : town.getConqueredByTowns()) {
                        Line line = new Line(attacker.getX(), attacker.getY(), town.getX(), town.getY());
                        line.setOnMouseClicked(mouseEvent -> {
                            MouseButton button = mouseEvent.getButton();
                            switch (button) {
                                case SECONDARY:

                                    ArrayList<TreeNode> treeNodes = wm.getTreeNodes();
                                    Town attacker1 = null;
                                    Town attacked = null;
                                    int counter = 0;
                                    while (attacker1 == null || attacked == null) {
                                        if (treeNodes.get(counter) instanceof Town) {
                                            Town currentTown = (Town) treeNodes.get(counter);
                                            if (currentTown.getX() == line.getStartX() && currentTown.getY() == line.getStartY()){
                                                attacker1 = currentTown;
                                            }
                                            else if (currentTown.getX() == line.getEndX() && currentTown.getY() == line.getEndY())
                                                attacked = currentTown;
                                        }
                                        counter++;
                                    }
                                    //check if you are eligible to abort attack
                                    if(attacker.getOwner().getName().equals(player.getName()))
                                        attacked.removeConqueredByTown(attacker1);
                                    break;
                                default:
                                    System.out.println("something click");
                            }
                        });
                        //todo add owner color to to line later maybe
                        line.setStrokeWidth(8);
                        line.setStroke(player.getFXColor());
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
        Player player = ConnectionController.getPlayer();
        //System.out.println("player name:" + player.getName());
        //.out.println("player color:" + player.getFXColor());
        for (TreeNode treeNode : wm.getTreeNodes()) {
            if (treeNode instanceof Town) {
                Town town = (Town) treeNode;
                ArrayList<AbstractMap.SimpleEntry<Town, Long>> conquerors = town.getConqueredByTownEntries();
                ArrayList<Town> toRemove = new ArrayList<>();

                //check if town is under attack
                if(conquerors != null && conquerors.size() > 0) {
                    for (AbstractMap.SimpleEntry<Town, Long> conqueror : conquerors) {

                        //every passing two seconds town loses health
                        Town conquerorTown = conqueror.getKey();
                        if ((System.currentTimeMillis() - conqueror.getValue().longValue()) % 3000 >= 2000) {
                            //update attack damage
                            //if its an enemy decrease health
                            //if players own town increase town health
                            if(town.getOwner() == null || !conquerorTown.getOwner().getName().equals(town.getOwner().getName()) ) {
                                town.setLife(town.getLife() - 1);
                            } else {
                                town.setLife(town.getLife() + 1);
                            }
                            conquerorTown.setLife(conqueror.getKey().getLife() - 1);
                            //if health of attacker reaches 0 stop attack
                            //if conqueror has 0 or less hp, attack will be aborted
                            if (conquerorTown.getLife() <= 0) {
                                toRemove.add(conqueror.getKey());
                            }

                            //town is conquered abort all attacks and change owner
                            if(town.getLife() <= 0){
                                town.removeAllConquerors();
                                town.changeOwnership(conquerorTown.getOwner());
                                toRemove.clear();
                                break;
                            }
                        }
                    }
                    //remove all attacker, that can not attack anymore
                    if(toRemove.size() > 0){
                        for (Town remove : toRemove) {
                            town.removeConqueredByTown(remove);
                        }
                    }
                }
                if (town.getOwner() != null && (System.currentTimeMillis() - startTime)  % 5000 >= 4000) {
                    town.setLife(town.getLife() + 1);
                }
            }
        }
    }


    public Scene getScene() {
        return world.getScene();
    }
}
