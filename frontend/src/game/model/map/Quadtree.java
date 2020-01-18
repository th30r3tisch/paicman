package game.model.map;

import game.controller.ConnectionController;
import game.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Quadtree implements Serializable {
    private static final long serialVersionUID = 5260956141735471739L;
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
        this.treeNodes = new ArrayList<>();
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
        float x = treeNode.getX();
        float y = treeNode.getY();
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

    public void getAreaContent(Quadtree tree, int startX, int startY, int endX, int endY, ArrayList<TreeNode> wholeMap) {
        if (tree == null) return;

        if( !(startX > tree.boundry.xMax) && !(endX < tree.boundry.xMin) && !(startY > tree.boundry.yMax) && !(endY < tree.boundry.yMin)){
            for (TreeNode treeNode : tree.treeNodes) {
                if (treeNode.inRange(startX, startY, endX, endY)){
                    wholeMap.add(treeNode);
                }
            }
        }
        getAreaContent(tree.northWest, startX, startY, endX, endY, wholeMap);
        getAreaContent(tree.northEast, startX, startY, endX, endY, wholeMap);
        getAreaContent(tree.southWest, startX, startY, endX, endY, wholeMap);
        getAreaContent(tree.southEast, startX, startY, endX, endY, wholeMap);
    }

    public ArrayList<TreeNode> getAllContent(Quadtree tree, int startX, int startY, int endX, int endY){
        ArrayList<TreeNode> wholeMap = new ArrayList<>();
        getAreaContent(tree, startX, startY, endX, endY, wholeMap);
        return wholeMap;
    }

    private void addTownAtk(Quadtree tree, Town deff, Town atk){
        if (tree == null) return;

        if( !(deff.x > tree.boundry.xMax) && !(deff.x < tree.boundry.xMin) && !(deff.y > tree.boundry.yMax) && !(deff.y < tree.boundry.yMin)){
            for (int i = 0; i < tree.treeNodes.size(); i++) {
                if (tree.treeNodes.get(i).isNode(deff.x, deff.y)){
                    Town t = (Town) tree.treeNodes.get(i);
                    if (t.getConqueredByTowns().size() == 0)
                        ((Town) tree.treeNodes.get(i)).addConqueredByTown(atk);
                    else{
                        for (Town tt: deff.getConqueredByTowns() ) {
                            if (tt == atk) return;
                            else ((Town) tree.treeNodes.get(i)).addConqueredByTown(atk);
                        }
                    }
                }
            }
        }
        addTownAtk(tree.northWest, deff, atk);
        addTownAtk(tree.northEast, deff, atk);
        addTownAtk(tree.southWest, deff, atk);
        addTownAtk(tree.southEast, deff, atk);
    }

    private void rmTownAtk(Quadtree tree, Town deff, Town atk){
        if (tree == null) return;

        if( !(deff.x > tree.boundry.xMax) && !(deff.x < tree.boundry.xMin) && !(deff.y > tree.boundry.yMax) && !(deff.y < tree.boundry.yMin)){
            for (int i = 0; i < tree.treeNodes.size(); i++) {
                if (tree.treeNodes.get(i).isNode(deff.x, deff.y)){
                    for (Town tt: deff.getConqueredByTowns() ) {
                        if (tt == atk) ((Town) tree.treeNodes.get(i)).removeConqueredByTown(atk);
                        else return;
                    }
                }
            }
        }
        addTownAtk(tree.northWest, deff, atk);
        addTownAtk(tree.northEast, deff, atk);
        addTownAtk(tree.southWest, deff, atk);
        addTownAtk(tree.southEast, deff, atk);
    }


    private void changeTownOwnership(Quadtree tree ,Player player, Town town){
        if(tree == null || town == null || player == null) return;
        System.out.println("change owner: " + town.getX() + " " + town.getY());
        System.out.println("treenode lenght: " + tree.treeNodes.size());
        for (int i = 0; i < tree.treeNodes.size(); i++){
            System.out.println("current town to check " + tree.treeNodes.get(i).getX() + " " + tree.treeNodes.get(i).getY());
            if(tree.treeNodes.get(i).isNode(town.x, town.y)){
                System.out.println("found conquered town " + tree.treeNodes.get(i).getX());
                Player thisPlayer = ConnectionController.getPlayer();
                if(thisPlayer.getName().equals(town.getOwner().getName()))
                    thisPlayer.removeOwnedTown(town);
                else if(thisPlayer.getName().equals(player.getName()))
                    player.setOwnedTown(town);
                ((Town) tree.treeNodes.get(i)).setOwner(player);
                return;
            }
        }
    }


    public void addUpdateNode(ArrayList<TreeNode> nodes){
        Town inComingAtk = (Town)nodes.get(0);
        Town inComingDeff = (Town)nodes.get(1);
        addTownAtk(this, inComingDeff, inComingAtk);
    }

    public void rmUpdateNode(ArrayList<TreeNode> nodes){
        Town inComingAtk = (Town)nodes.get(0);
        Town inComingDeff = (Town)nodes.get(1);
        rmTownAtk(this, inComingDeff, inComingAtk);
    }

    public void updateTownOwnership(Player player, TreeNode treeNode){
        changeTownOwnership(this, player, ((Town) treeNode));
    }
}