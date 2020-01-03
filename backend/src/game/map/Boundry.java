package game.map;

public class Boundry {

    int xMin, yMin, xMax, yMax;

    public Boundry(int xMin, int yMin, int xMax, int yMax) {
        super();
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public boolean inRange(int x, int y) {
        return (x >= this.getxMin() && x <= this.getxMax()
                && y >= this.getyMin() && y <= this.getyMax());
    }

    public int getxMin() { return xMin; }
    public int getyMin() { return yMin; }
    public int getxMax() { return xMax; }
    public int getyMax() { return yMax; }

}
