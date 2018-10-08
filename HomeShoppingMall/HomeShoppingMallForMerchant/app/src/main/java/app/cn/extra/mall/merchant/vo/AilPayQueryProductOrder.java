package app.cn.extra.mall.merchant.vo;

/**
 * Created by Administrator on 2018/3/29 0029.
 */

public class AilPayQueryProductOrder {

    /**
     * errorString :
     * flag : true
     * data : {"tradeStatus":"交易支付成功","status":"true"}
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
         * tradeStatus : 交易支付成功
         * status : true
         */
        private String errorString;
        private String tradeStatus;
        private String status;

        public String getErrorString() {
            return errorString;
        }

        public void setErrorString(String errorString) {
            this.errorString = errorString;
        }

        public String getTradeStatus() {
            return tradeStatus;
        }

        public void setTradeStatus(String tradeStatus) {
            this.tradeStatus = tradeStatus;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
