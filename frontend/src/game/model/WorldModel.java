package game.model;

import javafx.scene.shape.Shape;
import java.util.ArrayList;

public class WorldModel {
    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    public void addShape(Shape shape) {
        this.shapes.add(shape);
    }

    private ArrayList<Shape> shapes = new ArrayList<>();
}
