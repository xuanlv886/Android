package com.netease.nim.uikit.merchant;

import java.util.List;

public class GetStoreProductListFromUserFootprint {

    /**
     * errorString :
     * flag : true
     * data : [{"pStockNum":25,"pName":"红木餐桌","pId":"3aa5554b-4867-11e8-8390-4ccc6a70ac67","pOriginalPrice":5000,"pNowPrice":4800,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/1.jpg"}]
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
         * pStockNum : 25
         * pName : 红木餐桌
         * pId : 3aa5554b-4867-11e8-8390-4ccc6a70ac67
         * pOriginalPrice : 5000.0
         * pNowPrice : 4800.0
         * url : https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/1.jpg
         */

        private int pStockNum;
        private String pName;
        private String pId;
        private double pOriginalPrice;
        private double pNowPrice;
        private String url;

        public int getPStockNum() {
            return pStockNum;
        }

        public void setPStockNum(int pStockNum) {
            this.pStockNum = pStockNum;
        }

        public String getPName() {
            return pName;
        }

        public void setPName(String pName) {
            this.pName = pName;
        }

        public String getPId() {
            return pId;
        }

        public void setPId(String pId) {
            this.pId = pId;
        }

        public double getPOriginalPrice() {
            return pOriginalPrice;
        }

        public void setPOriginalPrice(double pOriginalPrice) {
            this.pOriginalPrice = pOriginalPrice;
        }

        public double getPNowPrice() {
            return pNowPrice;
        }

        public void setPNowPrice(double pNowPrice) {
            this.pNowPrice = pNowPrice;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
