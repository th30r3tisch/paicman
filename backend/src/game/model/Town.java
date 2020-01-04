package game.model;

import java.awt.*;
import java.io.Serializable;

public class Town extends Node implements Serializable {

    private Player owner;
    private int life;
    private Color color;

    public Town(Player conqueror, int x, int y) {
        this.life = 20;
        this.owner = conqueror;
        this.color = conqueror.getColor();
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
