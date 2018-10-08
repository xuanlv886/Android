package app.cn.extra.mall.merchant.vo;

import java.util.List;

import app.cn.extra.mall.merchant.utils.Util;

public class GetUserRequirement {

    /**
     * errorString :
     * flag : true
     * data : {"userRequirementList":[{"urContent":"7766","urBrowserNum":0,"urOfferPrice":10,"roStatus":"1","urGetAddress":"","rtId":"8a7caa94-43aa-11e8-8390-4ccc6a70ac67","urTitle":"7766","urOfferType":0,"urAddress":"56565","urId":"fe43df48-99ef-11e8-b034-4ccc6a3b62ec","sId":"6c684eec-ad07-488b-b921-43485939d396","urCreateTime":"2018-08-07 11:14:27","uId":"37a322f7-1007-46cd-ac37-3dc16f9e92ef","urTrueName":"7766"}],"storeApplyRequirementList":[{"urContent":"装修","urBrowserNum":0,"urOfferPrice":0,"roStatus":"0","urGetAddress":"","rtId":"59bf595a-43ae-11e8-8390-4ccc6a70ac67","urTitle":"防御","urOfferType":1,"urAddress":"狙击","urId":"864b1b88-96c2-11e8-869b-4ccc6a3b62ec","sId":"6c684eec-ad07-488b-b921-43485939d396","urCreateTime":"2018-08-03 10:11:25","uId":"c7ce25bf-7972-42e0-8922-a1f27133ce66","urTrueName":"聂"}],"requirementOrderList":[{"urContent":"拉10个箱子","urBrowserNum":21,"urOfferPrice":500,"roStatus":"7","urGetAddress":"石家庄开发区","rtId":"8a7caa94-43aa-11e8-8390-4ccc6a70ac67","urTitle":"去开发区取货","urOfferType":0,"urAddress":"石家庄市新华区","urId":"8e3e1436-4862-11e8-8390-4ccc6a70ac67","sId":"6c684eec-ad07-488b-b921-43485939d396","urCreateTime":"2018-04-24 16:27:03","uId":"fd911746-7e91-11e8-ae27-4ccc6a70ac67","urTrueName":"小明"}]}
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
        private List<StoreApplyRequirementListBean> storeApplyRequirementList;
        private List<RequirementOrderListBean> requirementOrderList;

        public List<UserRequirementListBean> getUserRequirementList() {
            return userRequirementList;
        }

        public void setUserRequirementList(List<UserRequirementListBean> userRequirementList) {
            this.userRequirementList = userRequirementList;
        }

        public List<StoreApplyRequirementListBean> getStoreApplyRequirementList() {
            return storeApplyRequirementList;
        }

        public void setStoreApplyRequirementList(List<StoreApplyRequirementListBean> storeApplyRequirementList) {
            this.storeApplyRequirementList = storeApplyRequirementList;
        }

        public List<RequirementOrderListBean> getRequirementOrderList() {
            return requirementOrderList;
        }

        public void setRequirementOrderList(List<RequirementOrderListBean> requirementOrderList) {
            this.requirementOrderList = requirementOrderList;
        }

        public static class UserRequirementListBean {
            /**
             * urContent : 7766
             * urBrowserNum : 0
             * urOfferPrice : 10.0
             * roStatus : 1
             * urGetAddress :
             * rtId : 8a7caa94-43aa-11e8-8390-4ccc6a70ac67
             * urTitle : 7766
             * urOfferType : 0
             * urAddress : 56565
             * urId : fe43df48-99ef-11e8-b034-4ccc6a3b62ec
             * sId : 6c684eec-ad07-488b-b921-43485939d396
             * urCreateTime : 2018-08-07 11:14:27
             * uId : 37a322f7-1007-46cd-ac37-3dc16f9e92ef
             * urTrueName : 7766
             */

            private String urContent;
            private int urBrowserNum;
            private double urOfferPrice;
            private String roStatus;
            private String urGetAddress;
            private String rtId;
            private String urTitle;
            private int urOfferType;
            private String urAddress;
            private String urId;
            private String sId;
            private String urCreateTime;
            private String roConfirmTime;
            private String roOverTime;
            private String roGetTime;
            private String roVerificationTime;
            private String uId;
            private String urTrueName;
            private String rtName;

            public String getRtName() {
                return rtName;
            }

            public void setRtName(String rtName) {
                this.rtName = rtName;
            }

            public String getRoConfirmTime() {
                return roConfirmTime;
            }

            public void setRoConfirmTime(String roConfirmTime) {
                this.roConfirmTime = roConfirmTime;
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

            public String getRoVerificationTime() {
                return roVerificationTime;
            }

            public void setRoVerificationTime(String roVerificationTime) {
                this.roVerificationTime = roVerificationTime;
            }

            public String getUrContent() {
                return urContent;
            }

            public void setUrContent(String urContent) {
                this.urContent = urContent;
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

            public String getRoStatus() {
                return roStatus;
            }

            public void setRoStatus(String roStatus) {
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
        }

        public static class StoreApplyRequirementListBean {
            /**
             * urContent : 装修
             * urBrowserNum : 0
             * urOfferPrice : 0.0
             * roStatus : 0
             * urGetAddress :
             * rtId : 59bf595a-43ae-11e8-8390-4ccc6a70ac67
             * urTitle : 防御
             * urOfferType : 1
             * urAddress : 狙击
             * urId : 864b1b88-96c2-11e8-869b-4ccc6a3b62ec
             * sId : 6c684eec-ad07-488b-b921-43485939d396
             * urCreateTime : 2018-08-03 10:11:25
             * uId : c7ce25bf-7972-42e0-8922-a1f27133ce66
             * urTrueName : 聂
             */

            private String urContent;
            private int urBrowserNum;
            private double urOfferPrice;
            private String roStatus;
            private String urGetAddress;
            private String rtId;
            private String urTitle;
            private int urOfferType;
            private String urAddress;
            private String urId;
            private String sId;
            private String urCreateTime;
            private String uId;
            private String urTrueName;
            private String num;
            private String rtName;

            public String getRtName() {
                return rtName;
            }

            public void setRtName(String rtName) {
                this.rtName = rtName;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getUrContent() {
                return urContent;
            }

            public void setUrContent(String urContent) {
                this.urContent = urContent;
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

            public String getRoStatus() {
                return roStatus;
            }

            public void setRoStatus(String roStatus) {
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
        }

        public static class RequirementOrderListBean {
            /**
             * urContent : 拉10个箱子
             * urBrowserNum : 21
             * urOfferPrice : 500.0
             * roStatus : 7
             * urGetAddress : 石家庄开发区
             * rtId : 8a7caa94-43aa-11e8-8390-4ccc6a70ac67
             * urTitle : 去开发区取货
             * urOfferType : 0
             * urAddress : 石家庄市新华区
             * urId : 8e3e1436-4862-11e8-8390-4ccc6a70ac67
             * sId : 6c684eec-ad07-488b-b921-43485939d396
             * urCreateTime : 2018-04-24 16:27:03
             * uId : fd911746-7e91-11e8-ae27-4ccc6a70ac67
             * urTrueName : 小明
             */

            private String urContent;
            private int urBrowserNum;
            private double urOfferPrice;
            private String roStatus;
            private String urGetAddress;
            private String rtId;
            private String urTitle;
            private int urOfferType;
            private String urAddress;
            private String urId;
            private String sId;
            private String urCreateTime;
            private String uId;
            private String urTrueName;
            private String rtName;

            public String getRtName() {
                return rtName;
            }

            public void setRtName(String rtName) {
                this.rtName = rtName;
            }

            public String getUrContent() {
                return urContent;
            }

            public void setUrContent(String urContent) {
                this.urContent = urContent;
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

            public String getRoStatus() {
                return roStatus;
            }

            public void setRoStatus(String roStatus) {
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
        }
    }
}
