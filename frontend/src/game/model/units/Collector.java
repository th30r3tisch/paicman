package game.model.units;

import game.model.world_objects.Material;

import java.util.ArrayList;

public class Collector extends Unit {

    //weight unit can currently carries
    private int currentWeight;
    private int maxWeight;
    private ArrayList<Material> collectedMaterials;

    public Collector(int maxWeight){
        this.maxWeight = maxWeight;
        collectedMaterials = new ArrayList<Material>();
        currentWeight = 0;
    }

    public int getCurrentWeight() {
        return currentWeight;
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public ArrayList<Material> getCollectedMaterials() {
        return collectedMaterials;
    }

    public void addMaterial(Material material){
        currentWeight += material.getWeight();
        collectedMaterials.add(material);
    }

    public boolean canCollect(Material material){
        return (maxWeight + material.getWeight()) < maxWeight;
    }
}
