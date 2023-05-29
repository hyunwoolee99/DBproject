package exportkit.xd;

import androidx.room.Dao;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

@Dao
public interface QueryDao {

    @RawQuery
    List<Integer> getsearchIngredient(SupportSQLiteQuery query);

    @RawQuery
    List<Integer> getSearchbyName(SupportSQLiteQuery query);

    @RawQuery
    List<Step> getStepbyID(SupportSQLiteQuery query);

    @RawQuery
    List<String> getUserIdDuplicate(SupportSQLiteQuery query);

    @RawQuery
    List<User> getUser(SupportSQLiteQuery query);

    @RawQuery
    Recipe getRecipebyID(SupportSQLiteQuery query);

}
