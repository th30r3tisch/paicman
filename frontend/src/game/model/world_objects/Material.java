package game.model.world_objects;

import javafx.scene.paint.Color;

public class Material extends WorldObject {


    private MaterialType materialType;

    private int weight;


    public Material(MaterialType materialType) {
        this.materialType = materialType;
        setSize(8);
        if( materialType == MaterialType.CRAFTING){
            this.weight = 4;
             setColor(Color.BEIGE);
        } else {
            weight = 1;
           setColor(Color.CORAL);
        }
    }


    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

}
