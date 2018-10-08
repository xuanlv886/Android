package app.cn.extra.mall.user.vo;

import android.os.Parcel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by maqianli on 16/image_6/21.
 */
public class ShoppingCarStore implements Serializable {
    private int sid;
    private String sname;
    private String storeId;
    private  double amount;
    private List items;
    private boolean isChoosed;
    private String nickName;
    private String userName;

    protected ShoppingCarStore(Parcel in) {
        sid = in.readInt();
        sname = in.readString();
        storeId = in.readString();
        amount = in.readDouble();
        isChoosed = in.readByte() != 0;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
    public ShoppingCarStore() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }



    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    @Override
    public String toString() {
        return "ShoppingCarStore{" +
                "amount=" + amount +
                ", sid=" + sid +
                ", sname='" + sname + '\'' +
                ", storeId='" + storeId + '\'' +
                ", items=" + items +
                ", isChoosed=" + isChoosed +
                '}';
    }


}
