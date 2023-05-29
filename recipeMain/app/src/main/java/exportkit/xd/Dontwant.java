package exportkit.xd;

import androidx.annotation.NonNull;
import androidx.room.Entity;


@Entity(primaryKeys = {"id", "irdnt_nm"})
public class Dontwant {
    @NonNull
    private String id;
    @NonNull
    private String irdnt_nm;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getIrdnt_nm() {
        return irdnt_nm;
    }

    public void setIrdnt_nm(@NonNull String irdnt_nm) {
        this.irdnt_nm = irdnt_nm;
    }
}
