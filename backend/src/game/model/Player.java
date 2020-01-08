package game.model;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private static final long serialVersionUID = 5848343439467124184L;

    private String name;
    private Color color;
    private ArrayList<Town> ownedTowns;

    public ArrayList<Town> getOwnedTowns() { return ownedTowns; }
    public void setOwnedTown(Town town) { this.ownedTowns.add(town); }
    public void removeOwnedTown(Town town){ }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
