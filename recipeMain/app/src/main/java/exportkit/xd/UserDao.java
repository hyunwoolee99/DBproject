package exportkit.xd;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void setInsertUser(User user);

//    @Query("INSERT INTO User VALUES(/*ID*/,/*pw*/,/*name*/)")
//    void InsertUser(User user);

    @Delete
    void setDeleteUser(User user);

//    @Query("DELETE FROM User WHERE id=/*ID*/")
//    void DeleteUser(User user);

    @Update
    void setUpdateUser(User user);

//    @Query("UPDATE User SET password=/*pw*/ WHERE id=/*ID*/")
//    void UpdateUser(User user);

    @Query("SELECT * FROM User")
    List<User> getUserAll();
}

