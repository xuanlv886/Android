package app.cn.extra.mall.user.vo;

/**
 * Created by Administrator on 2018/4/21 0021.
 */

public class JudgePaymentPasswords {

    /**
     * errorString :
     * flag : true
     * data : {"errorString":"支付密码错误","status":"false"}
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
         * errorString : 支付密码错误
         * status : false
         */

        private String errorString;
        private String status;

        public String getErrorString() {
            return errorString;
        }

        public void setErrorString(String errorString) {
            this.errorString = errorString;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
