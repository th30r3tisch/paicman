package game.model.world_objects;

import java.io.Serializable;

public class Node implements Serializable {
    int x,y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    Node(){ }
}
