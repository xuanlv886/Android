package app.cn.extra.mall.merchant.vo;

import java.io.Serializable;
import java.util.List;

import app.cn.extra.mall.merchant.utils.Util;

public class GetProductOrder implements Serializable{

    /**
     * errorString :
     * flag : true
     * data : {" productOrderList":[{"pDescribe":"红木打造，低调奢华","poPayCode":"20180728165310979396","sName":"企业店铺1","psId":"f1f2231e-43a9-11e8-8390-4ccc6a70ac67","pId":"7dde1fe6-4867-11e8-8390-4ccc6a70ac67","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/2.jpeg","poId":"d0638d19-8a34-11e8-8131-4ccc6a3b62ec","sId":"168eb9f4-46ac-11e8-8390-4ccc6a70ac67","poTotalPrice":4500,"uId":"fd911746-7e91-11e8-ae27-4ccc6a70ac67","podProperty":"风格：复古","pName":"红木床","poCreateTime":"2018-07-18 10:46:47","poDeliverCompany":"顺丰","poNum":1,"poStatus":5,"podPrice":4500,"podId":"d1ec5afe-8a3c-11e8-8131-4ccc6a3b62ec","podEvaluate":0,"picId":"08e9a310-6fbd-11e8-8d97-4ccc6a70ac67","poDeliverCode":"123456"}]}
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

    public static class DataBean implements Serializable{
        private java.util.List<productOrderListBean> productOrderList;

        public List<productOrderListBean> getProductOrderList() {
            return productOrderList;
        }

        public void setProductOrderList(List<productOrderListBean> productOrderList) {
            this.productOrderList = productOrderList;
        }

        public static class productOrderListBean implements Serializable{
            /**
             * pDescribe : 红木打造，低调奢华
             * poPayCode : 20180728165310979396
             * sName : 企业店铺1
             * psId : f1f2231e-43a9-11e8-8390-4ccc6a70ac67
             * pId : 7dde1fe6-4867-11e8-8390-4ccc6a70ac67
             * picName : https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/2.jpeg
             * poId : d0638d19-8a34-11e8-8131-4ccc6a3b62ec
             * sId : 168eb9f4-46ac-11e8-8390-4ccc6a70ac67
             * poTotalPrice : 4500.0
             * uId : fd911746-7e91-11e8-ae27-4ccc6a70ac67
             * podProperty : 风格：复古
             * pName : 红木床
             * poCreateTime : 2018-07-18 10:46:47
             * poDeliverCompany : 顺丰
             * poNum : 1
             * poStatus : 5
             * podPrice : 4500.0
             * podId : d1ec5afe-8a3c-11e8-8131-4ccc6a3b62ec
             * podEvaluate : 0
             * picId : 08e9a310-6fbd-11e8-8d97-4ccc6a70ac67
             * poDeliverCode : 123456
             */

            private String pDescribe;
            private String poPayCode;
            private String sName;
            private String psId;
            private String pId;
            private String picName;
            private String poId;
            private String sId;
            private double poTotalPrice;
            private String uId;
            private String podProperty;
            private String pName;
            private String poCreateTime;
            private String poDeliverCompany;
            private int podNum;
            private String poStatus;
            private double podPrice;
            private String podId;
            private int podEvaluate;
            private String picId;
            private String poDeliverCode;
            private String poOrderId;

            public String getPoOrderId() {
                return poOrderId;
            }

            public void setPoOrderId(String poOrderId) {
                this.poOrderId = poOrderId;
            }

            public String getPDescribe() {
                return pDescribe;
            }

            public void setPDescribe(String pDescribe) {
                this.pDescribe = pDescribe;
            }

            public String getPoPayCode() {
                return poPayCode;
            }

            public void setPoPayCode(String poPayCode) {
                this.poPayCode = poPayCode;
            }

            public String getSName() {
                return sName;
            }

            public void setSName(String sName) {
                this.sName = sName;
            }

            public String getPsId() {
                return psId;
            }

            public void setPsId(String psId) {
                this.psId = psId;
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

            public String getPoId() {
                return poId;
            }

            public void setPoId(String poId) {
                this.poId = poId;
            }

            public String getSId() {
                return sId;
            }

            public void setSId(String sId) {
                this.sId = sId;
            }

            public String getPoTotalPrice() {
                return Util.doubleToTwoDecimal(poTotalPrice);
            }

            public void setPoTotalPrice(double poTotalPrice) {
                this.poTotalPrice = poTotalPrice;
            }

            public String getUId() {
                return uId;
            }

            public void setUId(String uId) {
                this.uId = uId;
            }

            public String getPodProperty() {
                return podProperty;
            }

            public void setPodProperty(String podProperty) {
                this.podProperty = podProperty;
            }

            public String getPName() {
                return pName;
            }

            public void setPName(String pName) {
                this.pName = pName;
            }

            public String getPoCreateTime() {
                return poCreateTime;
            }

            public void setPoCreateTime(String poCreateTime) {
                this.poCreateTime = poCreateTime;
            }

            public String getPoDeliverCompany() {
                return poDeliverCompany;
            }

            public void setPoDeliverCompany(String poDeliverCompany) {
                this.poDeliverCompany = poDeliverCompany;
            }

            public int getPodNum() {
                return podNum;
            }

            public void setPodNum(int podNum) {
                this.podNum = podNum;
            }

            public String getPoStatus() {
                return poStatus;
            }

            public void setPoStatus(String poStatus) {
                this.poStatus = poStatus;
            }

            public String getPodPrice() {
                return Util.doubleToTwoDecimal(podPrice);
            }

            public void setPodPrice(double podPrice) {
                this.podPrice = podPrice;
            }

            public String getPodId() {
                return podId;
            }

            public void setPodId(String podId) {
                this.podId = podId;
            }

            public int getPodEvaluate() {
                return podEvaluate;
            }

            public void setPodEvaluate(int podEvaluate) {
                this.podEvaluate = podEvaluate;
            }

            public String getPicId() {
                return picId;
            }

            public void setPicId(String picId) {
                this.picId = picId;
            }

            public String getPoDeliverCode() {
                return poDeliverCode;
            }

            public void setPoDeliverCode(String poDeliverCode) {
                this.poDeliverCode = poDeliverCode;
            }

        }
    }
}
