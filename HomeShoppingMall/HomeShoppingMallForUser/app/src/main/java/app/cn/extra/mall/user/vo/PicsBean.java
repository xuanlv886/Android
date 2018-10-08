package app.cn.extra.mall.user.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * Data 2018/8/24-18:52
 * Content
 *
 * @author L
 */
public class PicsBean implements Serializable {

    private List<Pic> picList = new ArrayList<Pic>();

    public List<Pic> getPicList() {
        return picList;
    }

    public void setPicList(List<Pic> picList) {
        this.picList = picList;
    }
}
