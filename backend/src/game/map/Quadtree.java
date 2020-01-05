package game.map;

import game.model.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Quadtree {
    final int MAX_CAPACITY =4;
    int level = 0;
    List<Node> nodes;
    Quadtree northWest = null;
    Quadtree northEast = null;
    Quadtree southWest = null;
    Quadtree southEast = null;
    Boundry boundry;
    private static Logger LOGGER = Logger.getLogger("InfoLogging");

    public Quadtree(int level, Boundry boundry) {
        this.level = level;
        nodes = new ArrayList<Node>();
        this.boundry = boundry;
    }

    /* Traveling the Graph using Depth First Search*/
    static public void dfs(Quadtree tree) {
        if (tree == null)
            return;

        System.out.printf("\nLevel = %d [X1=%d Y1=%d] \t[X2=%d Y2=%d] ",
                tree.level, tree.boundry.getxMin(), tree.boundry.getyMin(),
                tree.boundry.getxMax(), tree.boundry.getyMax());

        for (Node node : tree.nodes) {
            System.out.printf(" \n\t  x=%d y=%d", node.getX(), node.getY());
        }
        dfs(tree.northWest);
        dfs(tree.northEast);
        dfs(tree.southWest);
        dfs(tree.southEast);
    }

    void split() {
        int xOffset = this.boundry.getxMin()
                + (this.boundry.getxMax() - this.boundry.getxMin()) / 2;
        int yOffset = this.boundry.getyMin()
                + (this.boundry.getyMax() - this.boundry.getyMin()) / 2;

        northWest = new Quadtree(this.level + 1, new Boundry(
                this.boundry.getxMin(), this.boundry.getyMin(), xOffset,
                yOffset));
        northEast = new Quadtree(this.level + 1, new Boundry(xOffset,
                this.boundry.getyMin(), this.boundry.getxMax(), yOffset));
        southWest = new Quadtree(this.level + 1, new Boundry(
                this.boundry.getxMin(), xOffset, xOffset,
                this.boundry.getyMax()));
        southEast = new Quadtree(this.level + 1, new Boundry(xOffset, yOffset,
                this.boundry.getxMax(), this.boundry.getyMax()));

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

        // Check coordinates belongs to which partition
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
}