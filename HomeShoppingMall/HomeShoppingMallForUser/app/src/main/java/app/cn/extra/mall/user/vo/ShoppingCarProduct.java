package app.cn.extra.mall.user.vo;

import java.io.Serializable;

import app.cn.extra.mall.user.utils.Util;

/**
 * Created by maqianli on 16/image_6/21.
 */
public class ShoppingCarProduct implements Serializable {
    private String sId;
    private String pro_id;
    private String pro_name;
    private String pic_url;
    private String pro_ctid;
    private int num;
    private double sprice;
    //线上支付价
    private double onlinePrice;
    //积分价
    private double integralPrice;
    private double price;
    private boolean isChoosed;
    private String property;
    private String status;
    private int isChange;



    public ShoppingCarProduct() {

    }




    @Override
    public String toString() {
        return "ShoppingCarProduct{" +
                "isChoosed=" + isChoosed +
                ", pro_id='" + pro_id + '\'' +
                ", pro_name='" + pro_name + '\'' +
                ", pic_url='" + pic_url + '\'' +

                ", num=" + num +
                ", sprice=" + sprice +
                ", price=" + price +
                ", sId=" + sId +
                '}';
    }

    public String getOnlinePrice() {
        return Util.doubleToTwoDecimal(onlinePrice);
    }

    public void setOnlinePrice(double onlinePrice) {
        this.onlinePrice = onlinePrice;
    }

    public String getIntegralPrice() {
        return Util.doubleToTwoDecimal(integralPrice);
    }

    public void setIntegralPrice(double integralPrice) {
        this.integralPrice = integralPrice;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public int getisChange() {
        return isChange;
    }

    public void setisChange(int isChange) {
        this.isChange = isChange;
    }
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
    public String getPro_ctid(){
        return pro_ctid;
    }
    public void setPro_ctid(String pro_ctid){
        this.pro_ctid = pro_ctid;
    }
    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getPrice() {
        return Util.doubleToTwoDecimal(price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getSprice() {
        return Util.doubleToTwoDecimal(sprice);
    }

    public void setSprice(double sprice) {
        this.sprice = sprice;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }
}
