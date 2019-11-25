package game.model;

import javafx.scene.Node;

import java.awt.geom.Point2D;
import java.io.Serializable;

public abstract class Unit implements Serializable {

    private int life;

    private Node view;
    private Point2D velocity;

    public Integer getLife() {
        return life;
    }

    public void setLife(Integer life) {
        this.life = life;
    }

    //update method for updating position
    public void update(){
        view.setTranslateX(view.getTranslateX() + velocity.getX());
        view.setTranslateX(view.getTranslateY() + velocity.getY());
    }
}
