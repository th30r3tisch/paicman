package game.map;

import game.model.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Quadtree {
    final int MAX_CAPACITY =4;
    private int level = 0;
    private List<TreeNode> treeNodes;
    private Quadtree northWest = null;
    private Quadtree northEast = null;
    private Quadtree southWest = null;
    private Quadtree southEast = null;
    private Boundry boundry;
    private static Logger LOGGER = Logger.getLogger("InfoLogging");

    public Quadtree(int level, Boundry boundry) {
        this.level = level;
        treeNodes = new ArrayList<>();
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

    public void insert(TreeNode treeNode) {
        int x = treeNode.getX();
        int y = treeNode.getY();
        if (!this.boundry.inRange(x, y)) {
            return;
        }

        if (treeNodes.size() < MAX_CAPACITY) {
            treeNodes.add(treeNode);
            return;
        }
        // Exceeded the capacity so split it in FOUR
        if (northWest == null) {
            split();
        }

        // Check to which partition coordinates belong
        if (this.northWest.boundry.inRange(x, y))
            this.northWest.insert(treeNode);
        else if (this.northEast.boundry.inRange(x, y))
            this.northEast.insert(treeNode);
        else if (this.southWest.boundry.inRange(x, y))
            this.southWest.insert(treeNode);
        else if (this.southEast.boundry.inRange(x, y))
            this.southEast.insert(treeNode);
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

            for (TreeNode treeNode : tree.treeNodes) {
                if (treeNode.inRange(startX, startY, endX, endY)){
                    System.out.printf(" \n\t  x=%d y=%d", treeNode.getX(), treeNode.getY());
                }
            }
        }
        getAreaContent(tree.northWest, startX, startY, endX, endY);
        getAreaContent(tree.northEast, startX, startY, endX, endY);
        getAreaContent(tree.southWest, startX, startY, endX, endY);
        getAreaContent(tree.southEast, startX, startY, endX, endY);
    }

    public ArrayList<TreeNode> getAllContent(Quadtree tree, int startX, int startY, int endX, int endY){
        ArrayList<TreeNode> wholeMap = new ArrayList<>();
        getContent(tree, startX, startY, endX, endY, wholeMap);
        return wholeMap;
    }

    public void getContent(Quadtree tree, int startX, int startY, int endX, int endY, ArrayList<TreeNode> wholeMap) {
        if (tree == null)
            return;

        System.out.printf("\nLevel = %d [X1=%d Y1=%d] \t[X2=%d Y2=%d] ",
                tree.level, tree.boundry.getxMin(), tree.boundry.getyMin(),
                tree.boundry.getxMax(), tree.boundry.getyMax());

        for (TreeNode treeNode : tree.treeNodes) {
                System.out.printf(" \n\t  x=%d y=%d", treeNode.getX(), treeNode.getY());
                wholeMap.add(treeNode);
        }
        getContent(tree.northWest, startX, startY, endX, endY, wholeMap);
        getContent(tree.northEast, startX, startY, endX, endY, wholeMap);
        getContent(tree.southWest, startX, startY, endX, endY, wholeMap);
        getContent(tree.southEast, startX, startY, endX, endY, wholeMap);
    }
}