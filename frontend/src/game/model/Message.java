package game.model;

import game.model.world_objects.Node;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

    private MessageType type;
    private Player player;
    private String note;
    private ArrayList<Node> nodes;

    public MessageType getType() {
        return type;
    }
    public void setType(MessageType type) {
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player p) {
        this.player = p;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public ArrayList<Node> getNodes() { return nodes; }
    public void setNodes(ArrayList<Node> nodes) { this.nodes = nodes; }
}
