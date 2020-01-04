package game.model;

import java.awt.*;

public class Town {

    private Player owner;
    private int life;
    private Color color;

    public Town(Player conqueror) {
        this.life = 20;
        this.owner = conqueror;
        this.color = conqueror.getColor();
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
