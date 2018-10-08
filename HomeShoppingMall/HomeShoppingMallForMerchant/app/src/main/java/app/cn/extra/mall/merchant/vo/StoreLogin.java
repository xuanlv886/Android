package app.cn.extra.mall.merchant.vo;

/**
 * Description
 * Data 2018/7/11-15:10
 * Content
 *
 * @author L
 */
public class StoreLogin {

    /**
     * errorString :
     * flag : true
     * data : {"acId":"37","uId":"194a0051-512e-4a75-b1f6-215e1661d242","sType":1,"errorString":"未通过审核！审核意见：3245435436","sId":"4833b19c-96be-4f2c-8579-a95b6b719efb","needVerification":"true","status":"false"}
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
         * uId : 194a0051-512e-4a75-b1f6-215e1661d242
         * sType : 1
         * errorString : 未通过审核！审核意见：3245435436
         * sId : 4833b19c-96be-4f2c-8579-a95b6b719efb
         * needVerification : true
         * status : false
         */

        private String acId;
        private String uId;
        private int sType;
        private String errorString;
        private String sId;
        private String needVerification;
        private String status;
        private String sChecked;
        private String token;
        private String accid;

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getsChecked() {
            return sChecked;
        }

        public void setsChecked(String sChecked) {
            this.sChecked = sChecked;
        }

        public String getAcId() {
            return acId;
        }

        public void setAcId(String acId) {
            this.acId = acId;
        }

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
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

        public String getSId() {
            return sId;
        }

        public void setSId(String sId) {
            this.sId = sId;
        }

        public String getNeedVerification() {
            return needVerification;
        }

        public void setNeedVerification(String needVerification) {
            this.needVerification = needVerification;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
