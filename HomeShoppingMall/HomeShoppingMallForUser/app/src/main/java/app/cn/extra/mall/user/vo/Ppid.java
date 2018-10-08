package app.cn.extra.mall.user.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

public class Ppid implements Serializable {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Ppid{" +
                "data=" + data +
                '}';
    }
}
