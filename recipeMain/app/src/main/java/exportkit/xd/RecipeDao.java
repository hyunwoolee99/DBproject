package exportkit.xd;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    void setInsertRecipe(Recipe recipe);

    @Update
    void setUpdateRecipe(Recipe recipe);

    @Delete
    void setDeleteRecipe(Recipe recipe);

    //Query
    @Query("SELECT * FROM Recipe")
    List<Recipe> getRecipeAll();
}
