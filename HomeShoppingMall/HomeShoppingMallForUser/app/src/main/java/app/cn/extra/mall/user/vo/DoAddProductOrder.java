package app.cn.extra.mall.user.vo;

/**
 * Created by Administrator on 2018/3/10 0010.
 */

public class DoAddProductOrder {

    /**
     * errorString :
     * flag : true
     * data : {"payCode":"20180329114244107606","poId":"3d302476-3303-11e8-a3b1-4ccc6a3b62ec","status":"true"}
     */

    private String errorString;
    private String flag;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * payCode : 20180329114244107606
         * poId : 3d302476-3303-11e8-a3b1-4ccc6a3b62ec
         * status : true
         */

        private String payCode;
        private String poId;
        private String status;

        public String getPayCode() {
            return payCode;
        }

        public void setPayCode(String payCode) {
            this.payCode = payCode;
        }

        public String getPoId() {
            return poId;
        }

        public void setPoId(String poId) {
            this.poId = poId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
