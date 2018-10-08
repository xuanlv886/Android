package app.cn.extra.mall.user.vo;

/**
 * Created by Administrator on 2017/8/23 0023.
 */

public class SelfMarketSearch {
    private int id;
    private String number;

    public SelfMarketSearch(int id, String number) {
        this.id = id;
        this.number = number;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "SelfMarketSearch{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
