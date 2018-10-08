package app.cn.extra.mall.user.vo;

import java.util.List;

import app.cn.extra.mall.user.utils.Util;

public class GetUserFootPrint {

    /**
     * errorString :
     * flag : true
     * data : {"userFootprintList":[{"pDescribe":"餐巾啊啊啊啊","uId":"7d604f38-5ba6-4445-bc30-5b80a29b8541","ufCreateTime":"2018-08-18 11:52:03","pName":"餐巾","ufId":"10e39937-a29a-11e8-b7d3-4ccc6a3b62ec","pId":"ef3b4321-2ed5-4995-9ace-930d10b9b6e8","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180613165254136.jpg","pOriginalPrice":123,"pNowPrice":0.01},{"pDescribe":"测试床测试床测试床测试床","uId":"7d604f38-5ba6-4445-bc30-5b80a29b8541","ufCreateTime":"2018-08-18 16:14:10","pName":"测试床","ufId":"aeeeb637-a2be-11e8-b7d3-4ccc6a3b62ec","pId":"ac940421-4c8f-43f8-9ac3-d623c900a83e","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180709142339752.png","pOriginalPrice":100,"pNowPrice":98}]}
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
        private List<UserFootprintListBean> userFootprintList;

        public List<UserFootprintListBean> getUserFootprintList() {
            return userFootprintList;
        }

        public void setUserFootprintList(List<UserFootprintListBean> userFootprintList) {
            this.userFootprintList = userFootprintList;
        }

        public static class UserFootprintListBean {
            /**
             * pDescribe : 餐巾啊啊啊啊
             * uId : 7d604f38-5ba6-4445-bc30-5b80a29b8541
             * ufCreateTime : 2018-08-18 11:52:03
             * pName : 餐巾
             * ufId : 10e39937-a29a-11e8-b7d3-4ccc6a3b62ec
             * pId : ef3b4321-2ed5-4995-9ace-930d10b9b6e8
             * picName : https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180613165254136.jpg
             * pOriginalPrice : 123.0
             * pNowPrice : 0.01
             */

            private String pDescribe;
            private String uId;
            private String ufCreateTime;
            private String pName;
            private String ufId;
            private String pId;
            private String picName;
            private double pOriginalPrice;
            private double pNowPrice;

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

            public String getUfCreateTime() {
                return ufCreateTime;
            }

            public void setUfCreateTime(String ufCreateTime) {
                this.ufCreateTime = ufCreateTime;
            }

            public String getPName() {
                return pName;
            }

            public void setPName(String pName) {
                this.pName = pName;
            }

            public String getUfId() {
                return ufId;
            }

            public void setUfId(String ufId) {
                this.ufId = ufId;
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
        }
    }
}
