package game.model.map;

import game.controller.ConnectionController;
import game.model.Player;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.io.Serializable;
import java.util.ArrayList;

public class Town extends TreeNode implements Serializable {
    private static final long serialVersionUID = -6888113389669325015L;

    private Player owner;
    private int life;
    private Color color;
    private ArrayList<Town> conqueredByTowns = new ArrayList<>();;

    public ArrayList<Town> getConqueredByTowns() { return conqueredByTowns; }
    public void addConqueredByTown(Town town){
        ArrayList<TreeNode> list = new ArrayList();
        list.add(town);
        list.add(this);
        ConnectionController.attackRequest(list);
        if(conqueredByTowns == null) conqueredByTowns = new ArrayList<>();
        this.conqueredByTowns.add(town);
    }

    public void removeConqueredByTown(Town town) {
        ArrayList<TreeNode> list = new ArrayList();
        list.add(town);
        list.add(this);
        ConnectionController.removeAttackRequest(list);
        System.out.println("town in list? " + this.getConqueredByTowns().contains(town));
        this.conqueredByTowns.remove(town);
    }

    public Town(Player conqueror, int x, int y) {
        this.life = 20;
        this.owner = conqueror;
        this.color = conqueror.getFXColor();
        this.x = x;
        this.y = y;
    }

    public Player getOwner() { return owner; }

    public void setOwner(Player conqueror) {
        this.owner = conqueror;
        this.color = conqueror.getFXColor();
    }

    public int getLife() { return life; }
    public void setLife(int life) { this.life = life; }

    public Color getColor() { return color; }

    @Override
    public Shape create(){
        int radius = 0;
        if (life < 100){
            radius = 30;
        } else if (life > 100 && life < 1000){
            radius = 50;
        }
        Circle c = new Circle(this.x,this.y, radius);
        c.setFill(Color.GRAY);
        return c;
    }
}