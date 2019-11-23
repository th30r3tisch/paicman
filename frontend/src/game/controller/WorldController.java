package game.controller;

import game.model.Material;
import game.model.MaterialType;
import game.model.Town;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import game.view.World;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class WorldController {

    private Stage stage;
    private ArrayList<Shape> nodes;
    private ArrayList worldObjects;
    private ConnectionController cc;

    /** Inject the stage from {@link World} */
    public WorldController(Stage stage, ConnectionController cc) {
        this.stage = stage;
        this.cc = cc;
    }

    public ArrayList<Shape> createNodes(){
        Random r = new Random();
        nodes = new ArrayList<>();
        worldObjects = new ArrayList();
        int worldObjectNumber = 10;
        for(int i = 0; i < worldObjectNumber; ){
            int objectDecider = r.nextInt((100 - 1) + 1) + 1;
            int xPos = r.nextInt((450 - 10) + 1) + 10;
            int yPos = r.nextInt((350 - 10) + 1) + 10;
            int size = 20; //r.nextInt((30 - 10) + 1) + 10;
            if(canSet(xPos,yPos)) {
                //TODO add methods that check already existing dots and rerolls so they don't overlap
                if (objectDecider <= 20) {
                    //town
                    Town town = new Town();
                    worldObjects.add(town);
                    nodes.add(new Circle(xPos, yPos, size, town.getColor()));
                } else if (objectDecider > 20 && objectDecider <= 60) {
                    //food
                    Material food = new Material(MaterialType.FOOD);
                    worldObjects.add(food);
                    nodes.add(new Circle(xPos, yPos, size, food.getColor()));
                } else {
                    //crafting maerial
                    Material craftingMaterial = new Material(MaterialType.CRAFTING);
                    worldObjects.add(craftingMaterial);
                    nodes.add(new Circle(xPos, yPos, size, craftingMaterial.getColor()));
                }
                i++;
            }
        }

        for (Shape block : nodes) {
            setObjectListeners(block);
        }
        return nodes;
    }

    private void setObjectListeners(final Shape block) {
        final Delta dragDelta = new Delta();
        block.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = block.getLayoutX() - mouseEvent.getSceneX();
                dragDelta.y = block.getLayoutY() - mouseEvent.getSceneY();
                block.setCursor(Cursor.NONE);
            }
        });
        block.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                block.setCursor(Cursor.HAND);
            }
        });
        block.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                block.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                block.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                checkShapeIntersection(block);
            }
        });
        block.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent mouseEvent) {
                MouseButton button = mouseEvent.getButton();
                switch(button){
                    case SECONDARY:
                        System.out.println("right click");
                        break;
                    case PRIMARY:
                        System.out.println("left click");
                        break;
                    default: System.out.println("something click");
                }
            }
        });

        //Add context menu for options
        Label label = new Label();
        ContextMenu contextMenu = new ContextMenu();

        String menuText="";
        int i = nodes.indexOf(block);
        Object worldObject = null;
        if(i >= 0){
            worldObject = worldObjects.get(i);
            if(Material.class.isInstance(worldObject)){
                menuText = "Harvest";
            }  else {
              menuText = "Conquer";
            }
        }

        MenuItem item1 = new MenuItem(menuText);
        Object finalWorldObject = worldObject;
        item1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if(finalWorldObject != null){
                    if(Material.class.isInstance(finalWorldObject)){
                        Material m = (Material) finalWorldObject;
                    } else {
                        Town t = (Town) finalWorldObject;
                        //replace later with set owner
                        t.setColor(Color.DARKMAGENTA);
                        block.setFill(t.getColor());
                    }
                }
                label.setText("Select Menu Item 1");
            }
        });
        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(item1);


        block.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

            @Override
            public void handle(ContextMenuEvent event) {

                contextMenu.show(block, event.getScreenX(), event.getScreenY());
            }
        });
    }

    public void checkShapeIntersection(Shape block) {
        boolean collisionDetected = false;
        for (Shape static_bloc : nodes) {
            if (static_bloc != block) {
                static_bloc.setFill(static_bloc.getFill());

                Shape intersect = Shape.intersect(block, static_bloc);
                if (intersect.getBoundsInLocal().getWidth() != -1) {
                    collisionDetected = true;
                }
            }
        }

        if (collisionDetected) {
            block.setFill(Color.BLUE);
        } else {
         int i = nodes.indexOf(block);
         if(i >= 0){
         Object worldObject = worldObjects.get(i);
         if(Material.class.isInstance(worldObject)){
             Material m = (Material) worldObject;
             block.setFill(m.getColor());
         }  else {
             Town t = (Town) worldObject;
             block.setFill(t.getColor());
         }
         }
        }
    }
    class Delta { double x, y; }


    private boolean canSet(int x, int y){

        //should check if two points are overlapping, but does nor work atm
        if(nodes.size() == 0) return true;
        AtomicBoolean canSet = new AtomicBoolean(true);
        for(int i = 0; i< nodes.size(); i++){
            Shape node = nodes.get(i);
            Circle c = (Circle) node;
            double xDiff = Math.pow((c.getCenterX() - x), 2);
            double yDiff = Math.pow((c.getCenterX() - x), 2);
            double diff = Math.sqrt(xDiff + yDiff);
            if (diff <= 40){
                canSet.set(false);
            }
        }
        return canSet.get();
    }
}
