package exportkit.xd;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Recipe {
    @PrimaryKey(autoGenerate = true) //자동 count
    private int ID = 1; // 하나의 사용자에 대한 고유 ID 값

    private String name;
    private String summary;
    private int nation_code;
    private String nation_name;
    private int TY_code;
    private String TY_name;
    private int time;
    private String time_unit;
    private int calorie;
    private String calorie_unit;
    private int QNT;
    private String QNT_unit;
    private String level;
    private String IRDNT_code;
    private int PC_NM;
    private String PC_NM_unit;
    private String image_URL;
    private String DET_URL;
    // DET_URL 사용시 끝에 ID 넣어서 사용

    Recipe() {
        this.time_unit = "분";
        this.calorie_unit = "Kcal";
        this.QNT_unit = "인분";
        this.PC_NM_unit = "원";
        this.DET_URL = "http://www.okdab.com/consumer/recipe/recipeView.do?recipeSn=";
    }

    //getter & setter

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getNation_code() {
        return nation_code;
    }

    public void setNation_code(int nation_code) {
        this.nation_code = nation_code;
    }

    public String getNation_name() {
        return nation_name;
    }

    public void setNation_name(String nation_name) {
        this.nation_name = nation_name;
    }

    public int getTY_code() {
        return TY_code;
    }

    public void setTY_code(int TY_code) {
        this.TY_code = TY_code;
    }

    public String getTY_name() {
        return TY_name;
    }

    public void setTY_name(String TY_name) {
        this.TY_name = TY_name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTime_unit() {
        return time_unit;
    }

    public void setTime_unit(String time_unit) {
        this.time_unit = time_unit;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public String getCalorie_unit() {
        return calorie_unit;
    }

    public void setCalorie_unit(String calorie_unit) {
        this.calorie_unit = calorie_unit;
    }

    public int getQNT() {
        return QNT;
    }

    public void setQNT(int QNT) {
        this.QNT = QNT;
    }

    public String getQNT_unit() {
        return QNT_unit;
    }

    public void setQNT_unit(String QNT_unit) {
        this.QNT_unit = QNT_unit;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIRDNT_code() {
        return IRDNT_code;
    }

    public void setIRDNT_code(String IRDNT_code) {
        this.IRDNT_code = IRDNT_code;
    }

    public int getPC_NM() {
        return PC_NM;
    }

    public void setPC_NM(int PC_name) {
        this.PC_NM = PC_name;
    }

    public String getPC_NM_unit() {
        return PC_NM_unit;
    }

    public void setPC_NM_unit(String PC_name_unit) {
        this.PC_NM_unit = PC_name_unit;
    }

    public String getImage_URL() {
        return image_URL;
    }

    public void setImage_URL(String image_URL) {
        this.image_URL = image_URL;
    }

    public String getDET_URL() {
        return DET_URL;
    }

    public void setDET_URL(String DET_URL) {
        this.DET_URL = DET_URL;
    }
}
