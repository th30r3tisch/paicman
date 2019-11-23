package game.model;

import java.io.Serializable;
import java.util.ArrayList;

public class World implements Serializable {

    private ArrayList<Town> towns;

    public ArrayList<Town> getTowns() {
        return towns;
    }

    public void setTowns(ArrayList<Town> towns) {
        this.towns = towns;
    }
}
