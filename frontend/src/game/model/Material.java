package game.model;

import com.sun.prism.paint.Paint;
import javafx.scene.paint.Color;

import java.io.Serializable;

public class Material implements Serializable {


    private MaterialType materialType;

    private int weight;

    private Color color;


    public Material(MaterialType materialType) {
        this.materialType = materialType;
        if( materialType == MaterialType.CRAFTING){
            this.weight = 4;
             color = Color.BEIGE;
        } else {
            weight = 1;
            color = Color.CORAL;
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

    public Color getColor() {
        return color;
    }
}
