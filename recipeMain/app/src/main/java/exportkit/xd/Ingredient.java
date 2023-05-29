package exportkit.xd;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ingredient {
    private int recipe_ID;
    @PrimaryKey(autoGenerate = true)
    private int irdnt_sn = 1;
    private String name;
    private double capacity;
    private String cpciy_unit;
    private int irdnt_TY_code;
    private String irdnt_TY_name;

    public int getRecipe_ID() {
        return recipe_ID;
    }

    public void setRecipe_ID(int recipe_ID) {
        this.recipe_ID = recipe_ID;
    }

    public int getIrdnt_sn() {
        return irdnt_sn;
    }

    public void setIrdnt_sn(int irdnt_sn) {
        this.irdnt_sn = irdnt_sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public String getCpciy_unit() {
        return cpciy_unit;
    }

    public void setCpciy_unit(String cpciy_unit) {
        this.cpciy_unit = cpciy_unit;
    }

    public int getIrdnt_TY_code() {
        return irdnt_TY_code;
    }

    public void setIrdnt_TY_code(int irdnt_TY_code) {
        this.irdnt_TY_code = irdnt_TY_code;
    }

    public String getIrdnt_TY_name() {
        return irdnt_TY_name;
    }

    public void setIrdnt_TY_name(String irdnt_TY_name) {
        this.irdnt_TY_name = irdnt_TY_name;
    }
}
