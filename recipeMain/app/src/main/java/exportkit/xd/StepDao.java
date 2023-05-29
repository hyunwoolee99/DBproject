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
public interface StepDao {
    @Insert
    void setInsertStep(Step step);

    @Update
    void setUpdateStep(Step step);

    @Delete
    void setDeleteStep(Step step);

    //Query
    @Query("SELECT * FROM Step")
    List<Step> getStepAll();
}
