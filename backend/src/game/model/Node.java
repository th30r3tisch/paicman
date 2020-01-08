package game.model;

import java.io.Serializable;

public class Node implements Serializable {
    int x,y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean inRange(int startX, int startY, int endX, int endY){
        return (this.x >= startX  && this.x <= endX
                && this.y >= startY && this.y <= endY);
    }

    Node(){ }
}
