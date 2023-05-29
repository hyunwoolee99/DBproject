package exportkit.xd;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class, Step.class, Ingredient.class, User.class, Bookmark.class, Dontwant.class}, version = 1)
public abstract class RecipeDB extends RoomDatabase {
    public abstract RecipeDao recipeDao();
    public abstract StepDao stepDao();
    public abstract IngredientDao ingredientDao();
    public abstract UserDao userDao();
    public abstract BookmarkDao bookmarkDao();
    public abstract DontwantDao dontwantDao();

    public abstract QueryDao queryDao();
}
