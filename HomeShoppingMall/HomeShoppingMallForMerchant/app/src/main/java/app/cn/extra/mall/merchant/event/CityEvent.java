package app.cn.extra.mall.merchant.event;

/**
 * Description
 * Data 2018/7/9-11:01
 * Content
 *
 * @author L
 */
public class CityEvent {
    private String cityName;
    private String acId;

    public CityEvent(String cityName, String acId) {
        this.cityName = cityName;
        this.acId = acId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAcId() {
        return acId;
    }

    public void setAcId(String acId) {
        this.acId = acId;
    }

    @Override
    public String toString() {
        return "CityEvent{" +
                "cityName='" + cityName + '\'' +
                ", acId='" + acId + '\'' +
                '}';
    }
}
