package app.cn.extra.mall.merchant.vo;

import java.util.List;

/**
 * Description
 * Data 2018/7/28-11:10
 * Content
 *
 * @author L
 */
public class GetPayStyle {

    /**
     * errorString :
     * flag : true
     * data : {"payStyleList":[{"psName":"微信支付","psId":"f1f2215c-43a9-11e8-8390-4ccc6a70ac67","url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/common/wx.png"},{"psName":"支付宝支付","psId":"f1f2231e-43a9-11e8-8390-4ccc6a70ac67","url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/common/alipay.png"}]}
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
        private List<PayStyleListBean> payStyleList;

        public List<PayStyleListBean> getPayStyleList() {
            return payStyleList;
        }

        public void setPayStyleList(List<PayStyleListBean> payStyleList) {
            this.payStyleList = payStyleList;
        }

        public static class PayStyleListBean {
            /**
             * psName : 微信支付
             * psId : f1f2215c-43a9-11e8-8390-4ccc6a70ac67
             * url : https://hsmcommon.oss-cn-beijing.aliyuncs.com/common/wx.png
             */

            private String psName;
            private String psId;
            private String url;

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

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
