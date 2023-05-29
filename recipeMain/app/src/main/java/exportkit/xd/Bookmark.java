package exportkit.xd;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(primaryKeys = {"user_ID", "recipe_ID"})
public class Bookmark {
    @NonNull
    private String user_ID;
    @NonNull
    private int recipe_ID;

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public int getRecipe_ID() {
        return recipe_ID;
    }

    public void setRecipe_ID(int recipe_ID) {
        this.recipe_ID = recipe_ID;
    }
}
