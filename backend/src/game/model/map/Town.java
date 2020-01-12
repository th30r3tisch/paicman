package game.model.map;

import game.model.Player;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Town extends TreeNode implements Serializable {

    private Player owner;
    private int life;
    private Color color;
    private ArrayList<Town> conqueredByTowns;

    public ArrayList<Town> getConqueredByTowns() { return conqueredByTowns; }
    public void addConqueredByTown(Town town) { this.conqueredByTowns.add(town); }
    public void removeConqueredByTown(Town town) { }

    public Town(int x, int y) {
        this.life = 20;
        this.x = x;
        this.y = y;
    }

    public Player getOwner() { return owner; }

    public void setOwner(Player conqueror) {
        this.owner = conqueror;
        this.color = conqueror.getColor();
    }

    public int getLife() { return life; }
    public void setLife(int life) { this.life = life; }

    public Color getColor() { return color; }
}
