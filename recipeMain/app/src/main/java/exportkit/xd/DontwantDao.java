package exportkit.xd;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DontwantDao {
    @Insert
    void setInsertDontwant(Dontwant dontwant);

    @Update
    void setUpdateDontwant(Dontwant dontwant);

    @Delete
    void setDeleteDontwant(Dontwant dontwant);

    @Query("select * from Dontwant")
    List<Dontwant> getDontwantAll();
}
