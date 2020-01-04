package game.model;

import java.io.Serializable;

public class Player implements Serializable {

    private String name;
    private String color;

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
