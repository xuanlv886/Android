package app.cn.extra.mall.user.vo;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19 0019.
 */

public class IndexTag {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * key : 1
         * value : 0
         */

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
}
