package zedata.com.entity;

/**
 * @author Mr-Glacier
 * @apiNote 第一车 区县实体数据
 */
public class BeanDiYiCarCounty {
    private int C_ID;
    /**
     * 入库批次
     */
    private String C_wareHouseTime;
    private String C_countyName;
    private String C_pinyin;
    private String C_countyUrl;
    private String C_downState;
    private String C_countyPage;
    private String C_downTime;

    public String getC_downTime() {
        return C_downTime;
    }

    public void setC_downTime(String c_downTime) {
        C_downTime = c_downTime;
    }

    public int getC_ID() {
        return C_ID;
    }

    public void setC_ID(int c_ID) {
        C_ID = c_ID;
    }

    public String getC_wareHouseTime() {
        return C_wareHouseTime;
    }

    public void setC_wareHouseTime(String c_wareHouseTime) {
        C_wareHouseTime = c_wareHouseTime;
    }

    public String getC_countyName() {
        return C_countyName;
    }

    public void setC_countyName(String c_countyName) {
        C_countyName = c_countyName;
    }

    public String getC_pinyin() {
        return C_pinyin;
    }

    public void setC_pinyin(String c_pinyin) {
        C_pinyin = c_pinyin;
    }

    public String getC_countyUrl() {
        return C_countyUrl;
    }

    public void setC_countyUrl(String c_countyUrl) {
        C_countyUrl = c_countyUrl;
    }

    public String getC_downState() {
        return C_downState;
    }

    public void setC_downState(String c_downState) {
        C_downState = c_downState;
    }

    public String getC_countyPage() {
        return C_countyPage;
    }

    public void setC_countyPage(String c_countyPage) {
        C_countyPage = c_countyPage;
    }
}
