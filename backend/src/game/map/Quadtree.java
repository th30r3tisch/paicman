package game.map;

import game.model.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Quadtree {
    final int MAX_CAPACITY =4;
    private int level = 0;
    private List<Node> nodes;
    private Quadtree northWest = null;
    private Quadtree northEast = null;
    private Quadtree southWest = null;
    private Quadtree southEast = null;
    private Boundry boundry;
    private static Logger LOGGER = Logger.getLogger("InfoLogging");

    public Quadtree(int level, Boundry boundry) {
        this.level = level;
        nodes = new ArrayList<>();
        this.boundry = boundry;
    }

    void split() {
        int xOffset = this.boundry.getxMin()
                + (this.boundry.getxMax() - this.boundry.getxMin()) / 2;
        int yOffset = this.boundry.getyMin()
                + (this.boundry.getyMax() - this.boundry.getyMin()) / 2;

        northWest = new Quadtree(this.level + 1, new Boundry(
                this.boundry.getxMin(),
                this.boundry.getyMin(),
                xOffset,
                yOffset));
        northEast = new Quadtree(this.level + 1, new Boundry(
                xOffset,
                this.boundry.getyMin(),
                this.boundry.getxMax(),
                yOffset));
        southWest = new Quadtree(this.level + 1, new Boundry(
                this.boundry.getxMin(),
                yOffset,
                xOffset,
                this.boundry.getyMax()));
        southEast = new Quadtree(this.level + 1, new Boundry(
                xOffset,
                yOffset,
                this.boundry.getxMax(),
                this.boundry.getyMax()));

    }

    public void insert(Node node) {
        int x = node.getX();
        int y = node.getY();
        if (!this.boundry.inRange(x, y)) {
            return;
        }

        if (nodes.size() < MAX_CAPACITY) {
            nodes.add(node);
            return;
        }
        // Exceeded the capacity so split it in FOUR
        if (northWest == null) {
            split();
        }

        // Check to which partition coordinates belong
        if (this.northWest.boundry.inRange(x, y))
            this.northWest.insert(node);
        else if (this.northEast.boundry.inRange(x, y))
            this.northEast.insert(node);
        else if (this.southWest.boundry.inRange(x, y))
            this.southWest.insert(node);
        else if (this.southEast.boundry.inRange(x, y))
            this.southEast.insert(node);
        else
            LOGGER.log(Level.SEVERE, "ERROR : Unhandled partition " + x + " " +  y);
    }

    public void getAreaContent(Quadtree tree, int startX, int startY, int endX, int endY) {
        if (tree == null)
            return;

        if( (tree.boundry.inRange(startX, startY) && tree.boundry.inRange(endX, endY) )
                || tree.boundry.xMin <= endX && tree.boundry.xMax <= endX && tree.boundry.yMin <= endY && tree.boundry.yMax <= endY){

            System.out.printf("\nLevel = %d [X1=%d Y1=%d] \t[X2=%d Y2=%d] ",
                    tree.level, tree.boundry.getxMin(), tree.boundry.getyMin(),
                    tree.boundry.getxMax(), tree.boundry.getyMax());

            for (Node node : tree.nodes) {
                if (node.inRange(startX, startY, endX, endY)){
                    System.out.printf(" \n\t  x=%d y=%d", node.getX(), node.getY());
                }
            }
        }
        getAreaContent(tree.northWest, startX, startY, endX, endY);
        getAreaContent(tree.northEast, startX, startY, endX, endY);
        getAreaContent(tree.southWest, startX, startY, endX, endY);
        getAreaContent(tree.southEast, startX, startY, endX, endY);
    }

    public ArrayList<Node> getAllContent(Quadtree tree, int startX, int startY, int endX, int endY){
        ArrayList<Node> wholeMap = new ArrayList<>();
        getContent(tree, startX, startY, endX, endY, wholeMap);
        return wholeMap;
    }

    public void getContent(Quadtree tree, int startX, int startY, int endX, int endY, ArrayList<Node> wholeMap) {
        if (tree == null)
            return;

        System.out.printf("\nLevel = %d [X1=%d Y1=%d] \t[X2=%d Y2=%d] ",
                tree.level, tree.boundry.getxMin(), tree.boundry.getyMin(),
                tree.boundry.getxMax(), tree.boundry.getyMax());

        for (Node node : tree.nodes) {
                System.out.printf(" \n\t  x=%d y=%d", node.getX(), node.getY());
                wholeMap.add(node);
        }
        getContent(tree.northWest, startX, startY, endX, endY, wholeMap);
        getContent(tree.northEast, startX, startY, endX, endY, wholeMap);
        getContent(tree.southWest, startX, startY, endX, endY, wholeMap);
        getContent(tree.southEast, startX, startY, endX, endY, wholeMap);
    }
}