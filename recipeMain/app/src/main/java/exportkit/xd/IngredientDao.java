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
public interface IngredientDao {
    @Insert
    void setInsertIngredient(Ingredient ingredient);

    @Update
    void setUpdateIngredient(Ingredient ingredient);

    @Delete
    void setDeleteIngredient(Ingredient ingredient);

    //Query
    @Query("SELECT * FROM Ingredient")
    List<Ingredient> getIngredientAll();

}
