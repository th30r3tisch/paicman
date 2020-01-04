package game.model;

import java.awt.*;
import java.io.Serializable;

public class Player implements Serializable {

    private String name;
    private Color color;

    public Color getColor() { return color; }

    public void setColor(Color color) { this.color = color; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }
}
