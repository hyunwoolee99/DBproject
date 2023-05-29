package exportkit.xd;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookmarkDao {
    @Insert
    void setInsertBookmark(Bookmark bookmark);

    @Update
    void setUpdateBookmark(Bookmark bookmark);

    @Delete
    void setDeleteBookmark(Bookmark bookmark);

    //Query
    @Query("SELECT * FROM Bookmark")
    List<Bookmark> getBookmarkAll();
}
