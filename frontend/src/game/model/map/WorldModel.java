package game.model.map;

import javafx.scene.shape.Shape;
import java.util.ArrayList;

public class WorldModel {

    private ArrayList<TreeNode> treeNodes = new ArrayList<>();
    private ArrayList<Shape> shapes = new ArrayList<>();

    public ArrayList<Shape> getCreateShapes() {
        ArrayList<Shape> shapes = new ArrayList<>();
        for (TreeNode treeNode: treeNodes) {
           shapes.add(treeNode.create());
        }
        return shapes;
    }

    public ArrayList<TreeNode> getTreeNodes(){
        return treeNodes;
    }

    public void addNodes(TreeNode shape) {
        this.treeNodes.add(shape);
    }

    public void setNodes(ArrayList<TreeNode> nodes){
        treeNodes = nodes;
    }

    public void setShapes(ArrayList<Shape> shapes){
        this.shapes = shapes;
    }

    public ArrayList<Shape> getShapes() {
        return shapes;
    }
}
