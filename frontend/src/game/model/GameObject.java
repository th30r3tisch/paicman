package game.model;

import game.model.world_objects.WorldObject;
import javafx.scene.Node;

import java.awt.geom.Point2D;
import java.io.Serializable;

public class GameObject implements Serializable {

    private Node view;
    private Point2D velocity;
    private WorldObject object;

    private boolean alive;

    public GameObject(Node view, WorldObject object) {
        this.view = view;
        this.object = object;
    }
}
