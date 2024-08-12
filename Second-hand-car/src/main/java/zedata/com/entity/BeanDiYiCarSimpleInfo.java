package zedata.com.entity;

/**
 * @author Mr-Glacier
 * @apiNote 第一车 的车辆 简约详情
 */
public class BeanDiYiCarSimpleInfo {
    /**
     * 自构主键
     */
    private int C_ID;
    /**
     * 入库批次
     */
    private String C_wareHouseTime;
    /**
     * 车辆名称
     */
    private String C_carName;
    /**
     * 原始 ID
     */
    private String C_sourceId;
    /**
     * 来源文件名
     */
    private String C_sourceFileName;
    /**
     * 上牌日期
     */
    private String C_licenseDate;
    /**
     * 里程
     */
    private String C_mileage;
    /**
     * 封面图片Url
     */
    private String C_image;
    /**
     * 特色标识
     */
    private String C_tags;
    /**
     * 价格
     */
    private String C_price;
    /**
     * 详情页面的Url
     */
    private String C_detailUrl;

    private String C_downState;
    private String C_downTime;

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

    public String getC_carName() {
        return C_carName;
    }

    public void setC_carName(String c_carName) {
        C_carName = c_carName;
    }

    public String getC_sourceId() {
        return C_sourceId;
    }

    public void setC_sourceId(String c_sourceId) {
        C_sourceId = c_sourceId;
    }

    public String getC_sourceFileName() {
        return C_sourceFileName;
    }

    public void setC_sourceFileName(String c_sourceFileName) {
        C_sourceFileName = c_sourceFileName;
    }

    public String getC_licenseDate() {
        return C_licenseDate;
    }

    public void setC_licenseDate(String c_licenseDate) {
        C_licenseDate = c_licenseDate;
    }

    public String getC_mileage() {
        return C_mileage;
    }

    public void setC_mileage(String c_mileage) {
        C_mileage = c_mileage;
    }

    public String getC_image() {
        return C_image;
    }

    public void setC_image(String c_image) {
        C_image = c_image;
    }

    public String getC_tags() {
        return C_tags;
    }

    public void setC_tags(String c_tags) {
        C_tags = c_tags;
    }

    public String getC_price() {
        return C_price;
    }

    public void setC_price(String c_price) {
        C_price = c_price;
    }

    public String getC_detailUrl() {
        return C_detailUrl;
    }

    public void setC_detailUrl(String c_detailUrl) {
        C_detailUrl = c_detailUrl;
    }

    public String getC_downState() {
        return C_downState;
    }

    public void setC_downState(String c_downState) {
        C_downState = c_downState;
    }

    public String getC_downTime() {
        return C_downTime;
    }

    public void setC_downTime(String c_downTime) {
        C_downTime = c_downTime;
    }
}
