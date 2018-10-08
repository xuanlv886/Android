package app.cn.extra.mall.user.vo;

import java.util.List;

import app.cn.extra.mall.user.utils.Util;

/**
 * Created by Administrator on 2018/4/18 0018.
 */

public class GetQuickView {
    /**
     * errorString :
     * flag : true
     * data : {"productList":[{"pDescribe":"红木打造，低调奢华","pSaleNum":0,"pName":"红木餐桌","serviceList":"","pId":"3aa5554b-4867-11e8-8390-4ccc6a70ac67","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/1.jpg","pOriginalPrice":0,"pNowPrice":4800}]}
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
        private List<ProductListBean> productList;

        public List<ProductListBean> getProductList() {
            return productList;
        }

        public void setProductList(List<ProductListBean> productList) {
            this.productList = productList;
        }

        public static class ProductListBean {
            /**
             * pDescribe : 红木打造，低调奢华
             * pSaleNum : 0
             * pName : 红木餐桌
             * serviceList :
             * pId : 3aa5554b-4867-11e8-8390-4ccc6a70ac67
             * picName : https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/1.jpg
             * pOriginalPrice : 0.0
             * pNowPrice : 4800.0
             */

            private String pDescribe;
            private int pSaleNum;
            private String pName;
            private String serviceList;
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

            public int getPSaleNum() {
                return pSaleNum;
            }

            public void setPSaleNum(int pSaleNum) {
                this.pSaleNum = pSaleNum;
            }

            public String getPName() {
                return pName;
            }

            public void setPName(String pName) {
                this.pName = pName;
            }

            public String getServiceList() {
                return serviceList;
            }

            public void setServiceList(String serviceList) {
                this.serviceList = serviceList;
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
