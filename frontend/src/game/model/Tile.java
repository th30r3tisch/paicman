package game.model;

import game.model.world_objects.WorldObject;
import javafx.geometry.Point2D;

import java.io.Serializable;

public class Tile implements Serializable {
    private int width;
    private int height;
    private Point2D position;
    private WorldObject occupation;

    public Tile(int width, int height, Point2D position, WorldObject occupation) {
        this.width = width;
        this.height = height;
        this.position = position;
        this.occupation = occupation;
    }

    public Tile(int width, int height, Point2D position) {
        this.width = width;
        this.height = height;
        this.position = position;
    }

    public WorldObject getOccupation() {
        return occupation;
    }

    public void setOccupation(WorldObject occupation) {
        this.occupation = occupation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point2D getPosition() {
        return position;
    }
}
