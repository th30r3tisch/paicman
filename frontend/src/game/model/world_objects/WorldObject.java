package game.model.world_objects;

import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class WorldObject implements Serializable {
    private int size;

    private Color color;
    private Node view;

    public WorldObject(int size, Color color) {
        this.size = size;
        this.color = color;
    }

    public WorldObject() {
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
