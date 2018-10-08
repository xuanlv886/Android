package app.cn.extra.mall.merchant.vo;

/**
 * Description
 * Data 2018/7/28-16:06
 * Content
 *
 * @author L
 */
public class GetOrderPayCode {

    /**
     * errorString :
     * flag : true
     * data : {"errorString":"","payCode":"20180720121139657276","status":"true"}
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
         * errorString :
         * payCode : 20180720121139657276
         * status : true
         */

        private String errorString;
        private String payCode;
        private String status;

        public String getErrorString() {
            return errorString;
        }

        public void setErrorString(String errorString) {
            this.errorString = errorString;
        }

        public String getPayCode() {
            return payCode;
        }

        public void setPayCode(String payCode) {
            this.payCode = payCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
