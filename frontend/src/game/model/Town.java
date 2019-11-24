package game.model;

import javafx.scene.paint.Color;


public class Town extends WorldObject {

    private Player owner;
    private int life;

    //change later to owners color
    private Color color = Color.GREEN;

    public Town(Player owner, Color color) {
        this.owner = owner;
        this.color = color;
        setSize(15);
    }

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
