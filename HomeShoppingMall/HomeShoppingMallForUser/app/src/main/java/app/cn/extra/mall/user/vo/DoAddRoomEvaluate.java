package app.cn.extra.mall.user.vo;

/**
 * Created by Administrator on 2018/3/17 0017.
 */

public class DoAddRoomEvaluate {

    /**
     * errorString :
     * flag : true
     * data : {"status":"true"}
     */

    private String errorString;
    private String flag;
    private DoAddEvaluate.DataBean data;

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

    public DoAddEvaluate.DataBean getData() {
        return data;
    }

    public void setData(DoAddEvaluate.DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * status : true
         */

        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
