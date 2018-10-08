package app.cn.extra.mall.merchant.vo;

/**
 * Description
 * Data 2018/6/15-12:03
 * Content
 *
 * @author lzy
 */
public class GetAreaCode {

    /**
     * errorString :
     * flag : true
     * data : {"acId":"10","status":"true"}
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
         * acId : 10
         * status : true
         */

        private String acId;
        private String status;

        public String getAcId() {
            return acId;
        }

        public void setAcId(String acId) {
            this.acId = acId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
