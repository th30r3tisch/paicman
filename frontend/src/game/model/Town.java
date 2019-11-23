package game.model;

import javafx.scene.paint.Color;

import java.io.Serializable;

public class Town implements Serializable {

    private Player owner;
    private int life;

    //change later to owners color
    private Color color = Color.GREEN;

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Integer getLife() {
        return life;
    }

    public void setLife(Integer life) {
        this.life = life;
    }

    public Color getColor() { return color; }

    public void setColor(Color color) {
        this.color = color;
    }

}
