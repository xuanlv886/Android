package app.cn.extra.mall.user.vo;

import java.io.Serializable;
import java.util.List;

import app.cn.extra.mall.user.utils.Util;

public class GetProductOrder implements Serializable{

    /**
     * errorString :
     * flag : true
     * data : {"productOrderList":[{"pDescribe":"餐巾啊啊啊啊","poPayCode":"20180820114726845713","sName":"niemiao","podNum":1,"psId":"f1f2231e-43a9-11e8-8390-4ccc6a70ac67","pId":"ef3b4321-2ed5-4995-9ace-930d10b9b6e8","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180613165254136.jpg","poId":"d3044fc7-d091-4b57-aac7-f6541ed5ce3f","sId":"0e3817b1-154c-4595-9f4b-aac8eb2c2842","poTotalPrice":0.01,"uId":"7d604f38-5ba6-4445-bc30-5b80a29b8541","poOrderId":"20180820114726845713","podProperty":"颜色分类:透明;","pName":"餐巾","poCreateTime":"2018-08-20 11:47:26","poDeliverCompany":"","poStatus":0,"podPrice":0,"podId":"c6bdd0a6-a42b-11e8-9fb9-4ccc6a3b62ec","podEvaluate":0,"picId":"8659effc-8000-11e8-ae27-4ccc6a70ac67","poDeliverCode":""}]}
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
        private List<ProductOrderListBean> productOrderList;

        public List<ProductOrderListBean> getProductOrderList() {
            return productOrderList;
        }

        public void setProductOrderList(List<ProductOrderListBean> productOrderList) {
            this.productOrderList = productOrderList;
        }

        public static class ProductOrderListBean {
            /**
             * pDescribe : 餐巾啊啊啊啊
             * poPayCode : 20180820114726845713
             * sName : niemiao
             * podNum : 1
             * psId : f1f2231e-43a9-11e8-8390-4ccc6a70ac67
             * pId : ef3b4321-2ed5-4995-9ace-930d10b9b6e8
             * picName : https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180613165254136.jpg
             * poId : d3044fc7-d091-4b57-aac7-f6541ed5ce3f
             * sId : 0e3817b1-154c-4595-9f4b-aac8eb2c2842
             * poTotalPrice : 0.01
             * uId : 7d604f38-5ba6-4445-bc30-5b80a29b8541
             * poOrderId : 20180820114726845713
             * podProperty : 颜色分类:透明;
             * pName : 餐巾
             * poCreateTime : 2018-08-20 11:47:26
             * poDeliverCompany :
             * poStatus : 0
             * podPrice : 0.0
             * podId : c6bdd0a6-a42b-11e8-9fb9-4ccc6a3b62ec
             * podEvaluate : 0
             * picId : 8659effc-8000-11e8-ae27-4ccc6a70ac67
             * poDeliverCode :
             */

            private String pDescribe;
            private String poPayCode;
            private String sName;
            private int podNum;
            private String psId;
            private String pId;
            private String picName;
            private String poId;
            private String sId;
            private double poTotalPrice;
            private String uId;
            private String poOrderId;
            private String podProperty;
            private String pName;
            private String poCreateTime;
            private String poDeliverCompany;
            private int poStatus;
            private double podPrice;
            private String podId;
            private int podEvaluate;
            private String picId;
            private String poDeliverCode;

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

            public int getPodNum() {
                return podNum;
            }

            public void setPodNum(int podNum) {
                this.podNum = podNum;
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

            public String getPoOrderId() {
                return poOrderId;
            }

            public void setPoOrderId(String poOrderId) {
                this.poOrderId = poOrderId;
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

            public int getPoStatus() {
                return poStatus;
            }

            public void setPoStatus(int poStatus) {
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
