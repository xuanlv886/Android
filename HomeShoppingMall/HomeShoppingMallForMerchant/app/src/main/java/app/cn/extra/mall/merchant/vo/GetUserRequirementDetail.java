package app.cn.extra.mall.merchant.vo;

import java.io.Serializable;

import app.cn.extra.mall.merchant.utils.Util;

public class GetUserRequirementDetail implements Serializable {

    /**
     * errorString :
     * flag : true
     * data : {"urContent":"拉10个箱子","roConfirmTime":"2018-04-25 16:49:40","urBrowserNum":20,"urOfferPrice":500,"roStatus":5,"urGetAddress":"石家庄开发区","rtId":"8a7caa94-43aa-11e8-8390-4ccc6a70ac67","urTitle":"去开发区取货","urOfferType":0,"urAddress":"石家庄市新华区","roId":"b762a848-4865-11e8-8390-4ccc6a70ac67","roOrderId":"2018042416450001","urId":"8e3e1436-4862-11e8-8390-4ccc6a70ac67","sId":"16869e70-46ac-11e8-8390-4ccc6a70ac67","urCreateTime":"2018-04-24 16:27:03","uId":"fd911746-7e91-11e8-ae27-4ccc6a70ac67","roVerificationTime":"","roCreateTime":"2018-04-25 16:49:40","roTotalPrice":500,"urTrueName":"小明","roOverTime":"","roGetTime":"","pTag":""}
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
         * urContent : 拉10个箱子
         * roConfirmTime : 2018-04-25 16:49:40
         * urBrowserNum : 20
         * urOfferPrice : 500.0
         * roStatus : 5
         * urGetAddress : 石家庄开发区
         * rtId : 8a7caa94-43aa-11e8-8390-4ccc6a70ac67
         * urTitle : 去开发区取货
         * urOfferType : 0
         * urAddress : 石家庄市新华区
         * roId : b762a848-4865-11e8-8390-4ccc6a70ac67
         * roOrderId : 2018042416450001
         * urId : 8e3e1436-4862-11e8-8390-4ccc6a70ac67
         * sId : 16869e70-46ac-11e8-8390-4ccc6a70ac67
         * urCreateTime : 2018-04-24 16:27:03
         * uId : fd911746-7e91-11e8-ae27-4ccc6a70ac67
         * roVerificationTime :
         * roCreateTime : 2018-04-25 16:49:40
         * roTotalPrice : 500.0
         * urTrueName : 小明
         * roOverTime :
         * roGetTime :
         * pTag :
         * accId:5aa67b41ef0a4964aacd2cb9466349bc
         * roeContent
         * roeCreateTime
         * roeLevel
         */

        private String urContent;
        private String roConfirmTime;
        private int urBrowserNum;
        private double urOfferPrice;
        private int roStatus;
        private String urGetAddress;
        private String rtId;
        private String urTitle;
        private int urOfferType;
        private String urAddress;
        private String roId;
        private String roOrderId;
        private String urId;
        private String sId;
        private String urCreateTime;
        private String uId;
        private String roVerificationTime;
        private String roCreateTime;
        private double roTotalPrice;
        private String urTrueName;
        private String roOverTime;
        private String roGetTime;
        private String pTag;
        private String rtName;
        private String urTel;
        private String accId;
        private int roeLevel;
        private String roeCreateTime;
        private String roeContent;

        public int getRoeLevel() {
            return roeLevel;
        }

        public void setRoeLevel(int roeLevel) {
            this.roeLevel = roeLevel;
        }

        public String getRoeCreateTime() {
            return roeCreateTime;
        }

        public void setRoeCreateTime(String roeCreateTime) {
            this.roeCreateTime = roeCreateTime;
        }

        public String getRoeContent() {
            return roeContent;
        }

        public void setRoeContent(String roeContent) {
            this.roeContent = roeContent;
        }

        public String getAccId() {
            return accId;
        }

        public void setAccId(String accId) {
            this.accId = accId;
        }

        public String getRtName() {
            return rtName;
        }

        public void setRtName(String rtName) {
            this.rtName = rtName;
        }

        public String getUrTel() {
            return urTel;
        }

        public void setUrTel(String urTel) {
            this.urTel = urTel;
        }

        public String getUrContent() {
            return urContent;
        }

        public void setUrContent(String urContent) {
            this.urContent = urContent;
        }

        public String getRoConfirmTime() {
            return roConfirmTime;
        }

        public void setRoConfirmTime(String roConfirmTime) {
            this.roConfirmTime = roConfirmTime;
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

        public String getUrGetAddress() {
            return urGetAddress;
        }

        public void setUrGetAddress(String urGetAddress) {
            this.urGetAddress = urGetAddress;
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

        public int getUrOfferType() {
            return urOfferType;
        }

        public void setUrOfferType(int urOfferType) {
            this.urOfferType = urOfferType;
        }

        public String getUrAddress() {
            return urAddress;
        }

        public void setUrAddress(String urAddress) {
            this.urAddress = urAddress;
        }

        public String getRoId() {
            return roId;
        }

        public void setRoId(String roId) {
            this.roId = roId;
        }

        public String getRoOrderId() {
            return roOrderId;
        }

        public void setRoOrderId(String roOrderId) {
            this.roOrderId = roOrderId;
        }

        public String getUrId() {
            return urId;
        }

        public void setUrId(String urId) {
            this.urId = urId;
        }

        public String getSId() {
            return sId;
        }

        public void setSId(String sId) {
            this.sId = sId;
        }

        public String getUrCreateTime() {
            return urCreateTime;
        }

        public void setUrCreateTime(String urCreateTime) {
            this.urCreateTime = urCreateTime;
        }

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }

        public String getRoVerificationTime() {
            return roVerificationTime;
        }

        public void setRoVerificationTime(String roVerificationTime) {
            this.roVerificationTime = roVerificationTime;
        }

        public String getRoCreateTime() {
            return roCreateTime;
        }

        public void setRoCreateTime(String roCreateTime) {
            this.roCreateTime = roCreateTime;
        }

        public double getRoTotalPrice() {
            return roTotalPrice;
        }

        public void setRoTotalPrice(double roTotalPrice) {
            this.roTotalPrice = roTotalPrice;
        }

        public String getUrTrueName() {
            return urTrueName;
        }

        public void setUrTrueName(String urTrueName) {
            this.urTrueName = urTrueName;
        }

        public String getRoOverTime() {
            return roOverTime;
        }

        public void setRoOverTime(String roOverTime) {
            this.roOverTime = roOverTime;
        }

        public String getRoGetTime() {
            return roGetTime;
        }

        public void setRoGetTime(String roGetTime) {
            this.roGetTime = roGetTime;
        }

        public String getPTag() {
            return pTag;
        }

        public void setPTag(String pTag) {
            this.pTag = pTag;
        }
    }
}
