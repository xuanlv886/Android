package app.cn.extra.mall.user.vo;

import java.util.List;

/**
 * Description
 * Data 2018/7/12-13:39
 * Content
 *
 * @author L
 */
public class MsGetSafetyQuestionOfUser {

    /**
     * errorString :
     * flag : true
     * data : [{"sqName":"你小时候最喜欢哪一本书？","sqPosition":1,"sqId":"e9fb09f8-43ac-11e8-8390-4ccc6a70ac67"},{"sqName":"你的第一个上司叫什么名字？","sqPosition":2,"sqId":"e9fb0cfa-43ac-11e8-8390-4ccc6a70ac67"},{"sqName":"你上小学时最喜欢的老师姓什么？","sqPosition":3,"sqId":"e9fb0e0e-43ac-11e8-8390-4ccc6a70ac67"}]
     */

    private String errorString;
    private String flag;
    private List<DataBean> data;

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sqName : 你小时候最喜欢哪一本书？
         * sqPosition : 1
         * sqId : e9fb09f8-43ac-11e8-8390-4ccc6a70ac67
         */

        private String sqName;
        private int sqPosition;
        private String sqId;

        public String getSqName() {
            return sqName;
        }

        public void setSqName(String sqName) {
            this.sqName = sqName;
        }

        public int getSqPosition() {
            return sqPosition;
        }

        public void setSqPosition(int sqPosition) {
            this.sqPosition = sqPosition;
        }

        public String getSqId() {
            return sqId;
        }

        public void setSqId(String sqId) {
            this.sqId = sqId;
        }
    }
}
