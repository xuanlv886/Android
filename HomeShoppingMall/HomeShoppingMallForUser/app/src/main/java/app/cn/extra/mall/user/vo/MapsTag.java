package app.cn.extra.mall.user.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/19 0019.
 */

public class MapsTag implements Serializable {
    private int key;
    private int value;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
