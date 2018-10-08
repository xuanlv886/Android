package app.cn.extra.mall.user.vo;

import java.io.Serializable;
import java.util.List;

import app.cn.extra.mall.user.utils.Util;

public class GetUserRequirementDetail implements Serializable {

    /**
     * errorString :
     * flag : true
     * data : {"roStatus":0,"urGetAddress":"","rtId":"59bf595a-43ae-11e8-8390-4ccc6a70ac67","urOfferType":1,"urAddress":"1","roId":"c932e35c-a5bc-11e8-9c5a-4ccc6a3b62ec","urCreateTime":"2018-08-22 11:38:08","roVerificationTime":"","storeApplyRequirementList":[{"finishedNum":2,"sDescribe":"","sName":"niemiao","sLevel":0,"picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/store/null.jpg","sarCreateTime":"2018-08-22 11:38:21","sTel":"13739716863","sarPrice":0.01,"sId":"0e3817b1-154c-4595-9f4b-aac8eb2c2842"}],"roOverTime":"","urTel":"2","roGetTime":"","urContent":"6","roConfirmTime":"","urBrowserNum":4,"urOfferPrice":"0.0","urTitle":"333","applyNum":21,"roOrderId":"20180822113808608971","urId":"c932b035-a5bc-11e8-9c5a-4ccc6a3b62ec","sId":"","uId":"7d604f38-5ba6-4445-bc30-5b80a29b8541","roCreateTime":"2018-08-22 11:38:08","roTotalPrice":"0.0","urTrueName":"9","rtName":"房屋装修","pTag":""}
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

    public static class DataBean implements Serializable {
        /**
         * roStatus : 0
         * urGetAddress :
         * rtId : 59bf595a-43ae-11e8-8390-4ccc6a70ac67
         * urOfferType : 1
         * urAddress : 1
         * roId : c932e35c-a5bc-11e8-9c5a-4ccc6a3b62ec
         * urCreateTime : 2018-08-22 11:38:08
         * roVerificationTime :
         * storeApplyRequirementList : [{"finishedNum":2,"sDescribe":"","sName":"niemiao","sLevel":0,"picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/store/null.jpg","sarCreateTime":"2018-08-22 11:38:21","sTel":"13739716863","sarPrice":0.01,"sId":"0e3817b1-154c-4595-9f4b-aac8eb2c2842"}]
         * roOverTime :
         * urTel : 2
         * roGetTime :
         * urContent : 6
         * roConfirmTime :
         * urBrowserNum : 4
         * urOfferPrice : 0.0
         * urTitle : 333
         * applyNum : 21
         * roOrderId : 20180822113808608971
         * urId : c932b035-a5bc-11e8-9c5a-4ccc6a3b62ec
         * sId :
         * uId : 7d604f38-5ba6-4445-bc30-5b80a29b8541
         * roCreateTime : 2018-08-22 11:38:08
         * roTotalPrice : 0.0
         * urTrueName : 9
         * rtName : 房屋装修
         * pTag :
         * roeContent
         * roeCreateTime
         * roeLevel
         */

        private int roStatus;
        private String urGetAddress;
        private String rtId;
        private int urOfferType;
        private String urAddress;
        private String roId;
        private String urCreateTime;
        private String roVerificationTime;
        private String roOverTime;
        private String urTel;
        private String roGetTime;
        private String urContent;
        private String roConfirmTime;
        private int urBrowserNum;
        private double urOfferPrice;
        private String urTitle;
        private int applyNum;
        private String roOrderId;
        private String urId;
        private String sId;
        private String uId;
        private String roCreateTime;
        private double roTotalPrice;
        private String urTrueName;
        private String rtName;
        private String pTag;
        private List<StoreApplyRequirementListBean> storeApplyRequirementList;
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

        public String getUrCreateTime() {
            return urCreateTime;
        }

        public void setUrCreateTime(String urCreateTime) {
            this.urCreateTime = urCreateTime;
        }

        public String getRoVerificationTime() {
            return roVerificationTime;
        }

        public void setRoVerificationTime(String roVerificationTime) {
            this.roVerificationTime = roVerificationTime;
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

        public String getRoGetTime() {
            return roGetTime;
        }

        public void setRoGetTime(String roGetTime) {
            this.roGetTime = roGetTime;
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

        public String getUrTitle() {
            return urTitle;
        }

        public void setUrTitle(String urTitle) {
            this.urTitle = urTitle;
        }

        public int getApplyNum() {
            return applyNum;
        }

        public void setApplyNum(int applyNum) {
            this.applyNum = applyNum;
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

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }

        public String getRoCreateTime() {
            return roCreateTime;
        }

        public void setRoCreateTime(String roCreateTime) {
            this.roCreateTime = roCreateTime;
        }

        public String getRoTotalPrice() {
            return Util.doubleToTwoDecimal(roTotalPrice);
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

        public String getRtName() {
            return rtName;
        }

        public void setRtName(String rtName) {
            this.rtName = rtName;
        }

        public String getPTag() {
            return pTag;
        }

        public void setPTag(String pTag) {
            this.pTag = pTag;
        }

        public List<StoreApplyRequirementListBean> getStoreApplyRequirementList() {
            return storeApplyRequirementList;
        }

        public void setStoreApplyRequirementList(List<StoreApplyRequirementListBean> storeApplyRequirementList) {
            this.storeApplyRequirementList = storeApplyRequirementList;
        }

        public static class StoreApplyRequirementListBean implements Serializable {
            /**
             * finishedNum : 2
             * sDescribe :
             * sName : niemiao
             * sLevel : 0
             * picName : https://hsmcommon.oss-cn-beijing.aliyuncs.com/store/null.jpg
             * sarCreateTime : 2018-08-22 11:38:21
             * sTel : 13739716863
             * sarPrice : 0.01
             * sId : 0e3817b1-154c-4595-9f4b-aac8eb2c2842
             */

            private int finishedNum;
            private String sDescribe;
            private String sName;
            private int sLevel;
            private String picName;
            private String sarCreateTime;
            private String sTel;
            private double sarPrice;
            private String sId;
            private String accId;

            public String getAccId() {
                return accId;
            }

            public void setAccId(String accId) {
                this.accId = accId;
            }

            public int getFinishedNum() {
                return finishedNum;
            }

            public void setFinishedNum(int finishedNum) {
                this.finishedNum = finishedNum;
            }

            public String getSDescribe() {
                return sDescribe;
            }

            public void setSDescribe(String sDescribe) {
                this.sDescribe = sDescribe;
            }

            public String getSName() {
                return sName;
            }

            public void setSName(String sName) {
                this.sName = sName;
            }

            public int getSLevel() {
                return sLevel;
            }

            public void setSLevel(int sLevel) {
                this.sLevel = sLevel;
            }

            public String getPicName() {
                return picName;
            }

            public void setPicName(String picName) {
                this.picName = picName;
            }

            public String getSarCreateTime() {
                return sarCreateTime;
            }

            public void setSarCreateTime(String sarCreateTime) {
                this.sarCreateTime = sarCreateTime;
            }

            public String getSTel() {
                return sTel;
            }

            public void setSTel(String sTel) {
                this.sTel = sTel;
            }

            public String getSarPrice() {
                return Util.doubleToTwoDecimal(sarPrice);
            }

            public void setSarPrice(double sarPrice) {
                this.sarPrice = sarPrice;
            }

            public String getSId() {
                return sId;
            }

            public void setSId(String sId) {
                this.sId = sId;
            }
        }
    }
}
