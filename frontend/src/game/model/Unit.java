package game.model;

import java.io.Serializable;

public abstract class Unit implements Serializable {

    private int life;

    public Integer getLife() {
        return life;
    }

    public void setLife(Integer life) {
        this.life = life;
    }
}
