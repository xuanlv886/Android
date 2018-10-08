package app.cn.extra.mall.merchant.vo;

import java.util.List;

/**
 * Created by Administrator on 2018/3/8 0008.
 */

public class GetRecmandProductByType {

    /**
     * errorString :
     * flag : true
     * data : {"productList":[{"pDescribe":"红木打造，低调奢华","pName":"红木餐桌","pId":"3aa5554b-4867-11e8-8390-4ccc6a70ac67","picName":"http://cfkjcommon1.oss-cn-beijing.aliyuncs.com/test/product/1.jpg","pOriginalPrice":5000,"pNowPrice":4800}]}
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
             * pName : 红木餐桌
             * pId : 3aa5554b-4867-11e8-8390-4ccc6a70ac67
             * picName : http://cfkjcommon1.oss-cn-beijing.aliyuncs.com/test/product/1.jpg
             * pOriginalPrice : 5000
             * pNowPrice : 4800
             */

            private String pDescribe;
            private String pName;
            private String pId;
            private String picName;
            private int pOriginalPrice;
            private int pNowPrice;

            public String getPDescribe() {
                return pDescribe;
            }

            public void setPDescribe(String pDescribe) {
                this.pDescribe = pDescribe;
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

            public String getPicName() {
                return picName;
            }

            public void setPicName(String picName) {
                this.picName = picName;
            }

            public int getPOriginalPrice() {
                return pOriginalPrice;
            }

            public void setPOriginalPrice(int pOriginalPrice) {
                this.pOriginalPrice = pOriginalPrice;
            }

            public int getPNowPrice() {
                return pNowPrice;
            }

            public void setPNowPrice(int pNowPrice) {
                this.pNowPrice = pNowPrice;
            }
        }
    }
}
