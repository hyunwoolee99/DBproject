package exportkit.xd;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"recipe_ID", "step_no"})
public class Step {
    @NonNull
    private int recipe_ID;
    @NonNull
    private int step_no;

    private String step_DC;
    private String image_URL;
    private String step_tip;


    public int getRecipe_ID() {
        return recipe_ID;
    }

    public void setRecipe_ID(int recipe_ID) {
        this.recipe_ID = recipe_ID;
    }

    public int getStep_no() {
        return step_no;
    }

    public void setStep_no(int step_no) {
        this.step_no = step_no;
    }

    public String getStep_DC() {
        return step_DC;
    }

    public void setStep_DC(String step_DC) {
        this.step_DC = step_DC;
    }

    public String getImage_URL() {
        return image_URL;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public String getStep_tip() {
        return step_tip;
    }

    public void setStep_tip(String step_tip) {
        this.step_tip = step_tip;
    }
}
