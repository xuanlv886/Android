package app.cn.extra.mall.user.vo;

import java.util.List;

import app.cn.extra.mall.user.utils.Util;

public class GetUserRequirement {

    /**
     * errorString :
     * flag : true
     * data : {"userRequirementList":[{"urContent":"U盘","roConfirmTime":"","urBrowserNum":1,"urOfferPrice":0,"urGetAddress":"","rtId":"59bf595a-43ae-11e8-8390-4ccc6a70ac67","urTitle":"33333","urOfferType":1,"urAddress":"微微一笑","applyNum":1,"urId":"0e9dbadc-a52b-11e8-8f58-4ccc6a3b62ec","sId":"","urCreateTime":"2018-08-21 18:14:59","uId":"7d604f38-5ba6-4445-bc30-5b80a29b8541","urTrueName":"欧了","roOverTime":"","urTel":"35525","rtName":"房屋装修","status":"0"},{"urContent":"6","roConfirmTime":"","urBrowserNum":4,"urOfferPrice":0,"urGetAddress":"","rtId":"59bf595a-43ae-11e8-8390-4ccc6a70ac67","urTitle":"333","urOfferType":1,"urAddress":"1","applyNum":1,"urId":"c932b035-a5bc-11e8-9c5a-4ccc6a3b62ec","sId":"","urCreateTime":"2018-08-22 11:38:08","uId":"7d604f38-5ba6-4445-bc30-5b80a29b8541","urTrueName":"9","roOverTime":"","urTel":"2","rtName":"房屋装修","status":"0"},{"urContent":"我现在","roConfirmTime":"","urBrowserNum":1,"urOfferPrice":0,"urGetAddress":"","rtId":"59bf595a-43ae-11e8-8390-4ccc6a70ac67","urTitle":"呃呃呃","urOfferType":1,"urAddress":"3333","applyNum":1,"urId":"cdb2e6de-a5b7-11e8-9c5a-4ccc6a3b62ec","sId":"","urCreateTime":"2018-08-22 11:02:28","uId":"7d604f38-5ba6-4445-bc30-5b80a29b8541","urTrueName":"9999","roOverTime":"","urTel":"6666","rtName":"房屋装修","status":"0"}]}
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
        private List<UserRequirementListBean> userRequirementList;

        public List<UserRequirementListBean> getUserRequirementList() {
            return userRequirementList;
        }

        public void setUserRequirementList(List<UserRequirementListBean> userRequirementList) {
            this.userRequirementList = userRequirementList;
        }

        public static class UserRequirementListBean {
            /**
             * urContent : U盘
             * roConfirmTime :
             * urBrowserNum : 1
             * urOfferPrice : 0.0
             * urGetAddress :
             * rtId : 59bf595a-43ae-11e8-8390-4ccc6a70ac67
             * urTitle : 33333
             * urOfferType : 1
             * urAddress : 微微一笑
             * applyNum : 1
             * urId : 0e9dbadc-a52b-11e8-8f58-4ccc6a3b62ec
             * sId :
             * urCreateTime : 2018-08-21 18:14:59
             * uId : 7d604f38-5ba6-4445-bc30-5b80a29b8541
             * urTrueName : 欧了
             * roOverTime :
             * urTel : 35525
             * rtName : 房屋装修
             * status : 0
             * roGetTime
             * roVerificationTime
             */

            private String urContent;
            private String roConfirmTime;
            private int urBrowserNum;
            private double urOfferPrice;
            private String urGetAddress;
            private String rtId;
            private String urTitle;
            private int urOfferType;
            private String urAddress;
            private int applyNum;
            private String urId;
            private String sId;
            private String urCreateTime;
            private String uId;
            private String urTrueName;
            private String roOverTime;
            private String urTel;
            private String rtName;
            private String status;
            private String sName;
            private String roVerificationTime;
            private String roGetTime;

            public String getRoVerificationTime() {
                return roVerificationTime;
            }

            public void setRoVerificationTime(String roVerificationTime) {
                this.roVerificationTime = roVerificationTime;
            }

            public String getRoGetTime() {
                return roGetTime;
            }

            public void setRoGetTime(String roGetTime) {
                this.roGetTime = roGetTime;
            }

            public String getSName() {
                return sName;
            }

            public void setSName(String sName) {
                this.sName = sName;
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

            public int getApplyNum() {
                return applyNum;
            }

            public void setApplyNum(int applyNum) {
                this.applyNum = applyNum;
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

            public String getUrTel() {
                return urTel;
            }

            public void setUrTel(String urTel) {
                this.urTel = urTel;
            }

            public String getRtName() {
                return rtName;
            }

            public void setRtName(String rtName) {
                this.rtName = rtName;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
