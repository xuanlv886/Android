package app.cn.extra.mall.user.vo;

import java.util.List;

import app.cn.extra.mall.user.utils.Util;

public class GetUserCollection {

    /**
     * errorString :
     * flag : true
     * data : {"userCollectionList":[{"pDescribe":"餐巾啊啊啊啊","uId":"7d604f38-5ba6-4445-bc30-5b80a29b8541","pName":"餐巾","ucCreateTime":"2018-08-20 11:02:45","pId":"ef3b4321-2ed5-4995-9ace-930d10b9b6e8","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180613165254136.jpg","pOriginalPrice":123,"pNowPrice":0.01,"ucId":"889d3b90-a425-11e8-9fb9-4ccc6a3b62ec"}]}
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
        private List<UserCollectionListBean> userCollectionList;

        public List<UserCollectionListBean> getUserCollectionList() {
            return userCollectionList;
        }

        public void setUserCollectionList(List<UserCollectionListBean> userCollectionList) {
            this.userCollectionList = userCollectionList;
        }

        public static class UserCollectionListBean {
            /**
             * pDescribe : 餐巾啊啊啊啊
             * uId : 7d604f38-5ba6-4445-bc30-5b80a29b8541
             * pName : 餐巾
             * ucCreateTime : 2018-08-20 11:02:45
             * pId : ef3b4321-2ed5-4995-9ace-930d10b9b6e8
             * picName : https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180613165254136.jpg
             * pOriginalPrice : 123.0
             * pNowPrice : 0.01
             * ucId : 889d3b90-a425-11e8-9fb9-4ccc6a3b62ec
             */

            private String pDescribe;
            private String uId;
            private String pName;
            private String ucCreateTime;
            private String pId;
            private String picName;
            private double pOriginalPrice;
            private double pNowPrice;
            private String ucId;

            public String getPDescribe() {
                return pDescribe;
            }

            public void setPDescribe(String pDescribe) {
                this.pDescribe = pDescribe;
            }

            public String getUId() {
                return uId;
            }

            public void setUId(String uId) {
                this.uId = uId;
            }

            public String getPName() {
                return pName;
            }

            public void setPName(String pName) {
                this.pName = pName;
            }

            public String getUcCreateTime() {
                return ucCreateTime;
            }

            public void setUcCreateTime(String ucCreateTime) {
                this.ucCreateTime = ucCreateTime;
            }

            public String getPId() {
                return pId;
            }

            public void setPId(String pId) {
                this.pId = pId;
            }

            public String getPicName() {
                return picName;
            }

            public void setPicName(String picName) {
                this.picName = picName;
            }

            public String getPOriginalPrice() {
                return Util.doubleToTwoDecimal(pOriginalPrice);
            }

            public void setPOriginalPrice(double pOriginalPrice) {
                this.pOriginalPrice = pOriginalPrice;
            }

            public String getPNowPrice() {
                return Util.doubleToTwoDecimal(pNowPrice);
            }

            public void setPNowPrice(double pNowPrice) {
                this.pNowPrice = pNowPrice;
            }

            public String getUcId() {
                return ucId;
            }

            public void setUcId(String ucId) {
                this.ucId = ucId;
            }
        }
    }
}
