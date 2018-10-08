package app.cn.extra.mall.user.vo;

/**
 * Description
 * Data 2018/7/28-16:19
 * Content
 *
 * @author L
 */
public class WxPayUnifiedOrder {


    /**
     * errorString :
     * flag : true
     * data : {"errorString":"","packageValue":"Sign=WXPay","appId":"wx9dd0426a34957774","sign":"260ADFFE78F80AFB9FCF2733AF19401A","partnerId":"1509739471","nonceStr":"9et46W0zcWx20BuR","status":"true","timestamp":1532915732621,"prepay_id":"wx30095534126758991253e2220678969737"}
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
         * packageValue : Sign=WXPay
         * appId : wx9dd0426a34957774
         * sign : 260ADFFE78F80AFB9FCF2733AF19401A
         * partnerId : 1509739471
         * nonceStr : 9et46W0zcWx20BuR
         * status : true
         * timestamp : 1532915732621
         * prepay_id : wx30095534126758991253e2220678969737
         */

        private String errorString;
        private String packageValue;
        private String appId;
        private String sign;
        private String partnerId;
        private String nonceStr;
        private String status;
        private String timestamp;
        private String prepay_id;

        public String getErrorString() {
            return errorString;
        }

        public void setErrorString(String errorString) {
            this.errorString = errorString;
        }

        public String getPackageValue() {
            return packageValue;
        }

        public void setPackageValue(String packageValue) {
            this.packageValue = packageValue;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getPartnerId() {
            return partnerId;
        }

        public void setPartnerId(String partnerId) {
            this.partnerId = partnerId;
        }

        public String getNonceStr() {
            return nonceStr;
        }

        public void setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getPrepay_id() {
            return prepay_id;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }
    }
}
