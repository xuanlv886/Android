package app.cn.extra.mall.user.vo;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20 0020.
 */

public class GetPayStyle {

    /**
     * errorString :
     * flag : true
     * data : {"payStyleList":[{"psName":"支付宝支付","psId":"1"},{"psName":"微信支付","psId":"2"},{"psName":"积分支付","psId":"3"},{"psName":"免费天数","psId":"4"},{"psName":"押金支付(积分)","psId":"5"},{"psName":"押金支付(微信)","psId":"6"},{"psName":"押金支付(支付宝)","psId":"7"}],"payCode":"2018040417362472"}
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
         * payStyleList : [{"psName":"支付宝支付","psId":"1"},{"psName":"微信支付","psId":"2"},{"psName":"积分支付","psId":"3"},{"psName":"免费天数","psId":"4"},{"psName":"押金支付(积分)","psId":"5"},{"psName":"押金支付(微信)","psId":"6"},{"psName":"押金支付(支付宝)","psId":"7"}]
         * payCode : 2018040417362472
         */

        private String payCode;
        private List<PayStyleListBean> payStyleList;

        public String getPayCode() {
            return payCode;
        }

        public void setPayCode(String payCode) {
            this.payCode = payCode;
        }

        public List<PayStyleListBean> getPayStyleList() {
            return payStyleList;
        }

        public void setPayStyleList(List<PayStyleListBean> payStyleList) {
            this.payStyleList = payStyleList;
        }

        public static class PayStyleListBean {
            /**
             * psName : 支付宝支付
             * psId : 1
             */

            private String psName;
            private String psId;

            public String getPsName() {
                return psName;
            }

            public void setPsName(String psName) {
                this.psName = psName;
            }

            public String getPsId() {
                return psId;
            }

            public void setPsId(String psId) {
                this.psId = psId;
            }
        }
    }
}
