package game.model;

import java.io.Serializable;

public class Town implements Serializable {

    private Player owner;
    private int life;

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
}
