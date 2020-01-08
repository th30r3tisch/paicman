package game.model;

import javafx.scene.shape.Shape;

import java.io.Serializable;

public abstract class TreeNode implements Serializable {
    private static final long serialVersionUID = 6275678337608919167L;
    int x,y;

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    TreeNode(){ }

    public abstract Shape create();
}
