package app.cn.extra.mall.user.vo;

import java.util.List;

/**
 * Created by Administrator on 2018/4/18 0018.
 */

public class ParameterData {

    /**
     * service : ["提供发票","保修服务","提供发票"]
     * lowPrice : 120
     * highPrice : 550
     * properties : ["14","2","5","17","4"]
     */

    private String lowPrice;
    private String highPrice;
    private List<String> service;
    private List<String> properties;

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public List<String> getService() {
        return service;
    }

    public void setService(List<String> service) {
        this.service = service;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }
}
