package app.cn.extra.mall.merchant.vo;

/**
 * Description
 * Data 2018/7/12-11:09
 * Content
 *
 * @author L
 */
public class UpdateUserPhoneIdAndReturnUserInfo {

    /**
     * errorString :
     * flag : true
     * data : {"acId":"37","sType":1,"errorString":"","status":"true","sId":"4833b19c-96be-4f2c-8579-a95b6b719efb"}
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
         * acId : 37
         * sType : 1
         * errorString :
         * status : true
         * sId : 4833b19c-96be-4f2c-8579-a95b6b719efb
         */

        private String acId;
        private int sType;
        private String errorString;
        private String status;
        private String sId;
        private String uPhoneId;

        public String getuPhoneId() {
            return uPhoneId;
        }

        public void setuPhoneId(String uPhoneId) {
            this.uPhoneId = uPhoneId;
        }

        public String getAcId() {
            return acId;
        }

        public void setAcId(String acId) {
            this.acId = acId;
        }

        public int getSType() {
            return sType;
        }

        public void setSType(int sType) {
            this.sType = sType;
        }

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

        public String getSId() {
            return sId;
        }

        public void setSId(String sId) {
            this.sId = sId;
        }
    }
}
