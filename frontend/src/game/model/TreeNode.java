package game.model;

import javafx.scene.shape.Shape;

import java.io.Serializable;

public abstract class TreeNode implements Serializable {
    private static final long serialVersionUID = -3883735168251464967L;
    float x,y;

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    TreeNode(){ }

    public abstract Shape create();
}
