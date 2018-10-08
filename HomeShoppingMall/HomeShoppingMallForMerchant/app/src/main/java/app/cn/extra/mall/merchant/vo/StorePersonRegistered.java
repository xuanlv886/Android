package app.cn.extra.mall.merchant.vo;

/**
 * Description
 * Data 2018/7/10-11:22
 * Content
 *
 * @author L
 */
public class StorePersonRegistered {

    /**
     * errorString :
     * flag : true
     * data : {"uId":"527095e0-1c73-4f09-b219-493c7ff1bd16","sType":"0","errorString":"","uPhoneId":"99c35b31-a2d1-43b9-aac8-afad9fc37f2c","status":"true","sId":"0ad26ad9-30e5-43b1-a940-4ce635644575"}
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
         * uPhoneId : 99c35b31-a2d1-43b9-aac8-afad9fc37f2c
         * status : true
         */

        private String errorString;
        private String uPhoneId;
        private String status;
        private String token;
        private String accid;
        private String uId;

        public String getuId() {
            return uId;
        }

        public void setuId(String uId) {
            this.uId = uId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getErrorString() {
            return errorString;
        }

        public void setErrorString(String errorString) {
            this.errorString = errorString;
        }

        public String getUPhoneId() {
            return uPhoneId;
        }

        public void setUPhoneId(String uPhoneId) {
            this.uPhoneId = uPhoneId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }
}
