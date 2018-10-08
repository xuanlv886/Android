package app.cn.extra.mall.merchant.vo;

import java.util.List;

import app.cn.extra.mall.merchant.utils.Util;

/**
 * Description
 * Data 2018/7/14-18:38
 * Content
 *
 * @author L
 */
public class GetAllRequirement {


    /**
     * errorString :
     * flag : true
     * data : [{"urContent":"刷墙","urCreateTime":"2018-04-25 16:24:23","urBrowserNum":10,"urOfferPrice":100,"roStatus":"5541e00a-4865-11e8-8390-4ccc6a70ac67","rtId":"59bf595a-43ae-11e8-8390-4ccc6a70ac67","urTitle":"装修房子","rtName":"房屋装修","roId":"5541e00a-4865-11e8-8390-4ccc6a70ac67","urId":"2eb0510b-4862-11e8-8390-4ccc6a70ac67"}]
     */

    private String errorString;
    private String flag;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * urContent : 刷墙
         * urCreateTime : 2018-04-25 16:24:23
         * urBrowserNum : 10
         * urOfferPrice : 100.0
         * roStatus : 0
         * rtId : 59bf595a-43ae-11e8-8390-4ccc6a70ac67
         * urTitle : 装修房子
         * rtName : 房屋装修
         * roId : 5541e00a-4865-11e8-8390-4ccc6a70ac67
         * urId : 2eb0510b-4862-11e8-8390-4ccc6a70ac67
         */

        private String urContent;
        private String urCreateTime;
        private int urBrowserNum;
        private double urOfferPrice;
        private String urOfferType;
        private int roStatus;
        private String rtId;
        private String urTitle;
        private String rtName;
        private String roId;
        private String urId;
        private int applyStatus;
        private String urGetAddress;

        public String getUrGetAddress() {
            return urGetAddress;
        }

        public void setUrGetAddress(String urGetAddress) {
            this.urGetAddress = urGetAddress;
        }

        public int getApplyStatus() {
            return applyStatus;
        }

        public void setApplyStatus(int applyStatus) {
            this.applyStatus = applyStatus;
        }

        public String getUrOfferType() {
            return urOfferType;
        }

        public void setUrOfferType(String urOfferType) {
            this.urOfferType = urOfferType;
        }

        public String getUrContent() {
            return urContent;
        }

        public void setUrContent(String urContent) {
            this.urContent = urContent;
        }

        public String getUrCreateTime() {
            return urCreateTime;
        }

        public void setUrCreateTime(String urCreateTime) {
            this.urCreateTime = urCreateTime;
        }

        public int getUrBrowserNum() {
            return urBrowserNum;
        }

        public void setUrBrowserNum(int urBrowserNum) {
            this.urBrowserNum = urBrowserNum;
        }

        public String getUrOfferPrice() {
            return Util.doubleToTwoDecimal(urOfferPrice);
        }

        public void setUrOfferPrice(double urOfferPrice) {
            this.urOfferPrice = urOfferPrice;
        }

        public int getRoStatus() {
            return roStatus;
        }

        public void setRoStatus(int roStatus) {
            this.roStatus = roStatus;
        }

        public String getRtId() {
            return rtId;
        }

        public void setRtId(String rtId) {
            this.rtId = rtId;
        }

        public String getUrTitle() {
            return urTitle;
        }

        public void setUrTitle(String urTitle) {
            this.urTitle = urTitle;
        }

        public String getRtName() {
            return rtName;
        }

        public void setRtName(String rtName) {
            this.rtName = rtName;
        }

        public String getRoId() {
            return roId;
        }

        public void setRoId(String roId) {
            this.roId = roId;
        }

        public String getUrId() {
            return urId;
        }

        public void setUrId(String urId) {
            this.urId = urId;
        }
    }
}
