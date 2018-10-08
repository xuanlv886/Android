package app.cn.extra.mall.merchant.vo;

import java.util.List;

import app.cn.extra.mall.merchant.utils.Util;

public class GetShop {

    /**
     * errorString :
     * flag : true
     * data : {"isAttention":"false","productPtdNameList":[{"ptdName":"餐巾"},{"ptdName":"床单"}],"picList":[{"picId":" 392347b7-81d1-11e8-ae27-4ccc6a70ac67 ","picNo":"1","picName":"https:20180707183149398.png "}],"sName":"测试企业","accid":"cfabd2db82c144729280056a0810b835","picName":"","productList":[{"pDescribe":"餐巾啊啊啊啊","pName":"餐巾","pBrowseNum":22,"ptdName":"餐巾","pId":"ef3b4321-2ed5-4995-9ace-930d10b9b6e8","ptdId":"050301","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180613165254136.jpg","pOriginalPrice":123,"pNowPrice":111},{"pDescribe":"测试","pName":"测试","pBrowseNum":0,"ptdName":"床单","pId":"f7bd6a33-c04a-458c-a8cd-818d6403b36b","ptdId":"0404","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180613170018783.png","pOriginalPrice":200,"pNowPrice":100}]}
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
        /**
         * isAttention : false
         * productPtdNameList : [{"ptdName":"餐巾"},{"ptdName":"床单"}]
         * picList : [{"picId":" 392347b7-81d1-11e8-ae27-4ccc6a70ac67 ","picNo":"1","picName":"https:20180707183149398.png "}]
         * sName : 测试企业
         * accid : cfabd2db82c144729280056a0810b835
         * picName :
         * productList : [{"pDescribe":"餐巾啊啊啊啊","pName":"餐巾","pBrowseNum":22,"ptdName":"餐巾","pId":"ef3b4321-2ed5-4995-9ace-930d10b9b6e8","ptdId":"050301","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180613165254136.jpg","pOriginalPrice":123,"pNowPrice":111},{"pDescribe":"测试","pName":"测试","pBrowseNum":0,"ptdName":"床单","pId":"f7bd6a33-c04a-458c-a8cd-818d6403b36b","ptdId":"0404","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180613170018783.png","pOriginalPrice":200,"pNowPrice":100}]
         */

        private String isAttention;
        private String sName;
        private String accid;
        private String picName;
        private List<ProductPtdNameListBean> productPtdNameList;
        private List<PicListBean> picList;
        private List<ProductListBean> productList;

        public String getIsAttention() {
            return isAttention;
        }

        public void setIsAttention(String isAttention) {
            this.isAttention = isAttention;
        }

        public String getSName() {
            return sName;
        }

        public void setSName(String sName) {
            this.sName = sName;
        }

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getPicName() {
            return picName;
        }

        public void setPicName(String picName) {
            this.picName = picName;
        }

        public List<ProductPtdNameListBean> getProductPtdNameList() {
            return productPtdNameList;
        }

        public void setProductPtdNameList(List<ProductPtdNameListBean> productPtdNameList) {
            this.productPtdNameList = productPtdNameList;
        }

        public List<PicListBean> getPicList() {
            return picList;
        }

        public void setPicList(List<PicListBean> picList) {
            this.picList = picList;
        }

        public List<ProductListBean> getProductList() {
            return productList;
        }

        public void setProductList(List<ProductListBean> productList) {
            this.productList = productList;
        }

        public static class ProductPtdNameListBean {
            /**
             * ptdName : 餐巾
             */

            private String ptdName;
            private String ptdId;

            public String getPtdId() {
                return ptdId;
            }

            public void setPtdId(String ptdId) {
                this.ptdId = ptdId;
            }

            public String getPtdName() {
                return ptdName;
            }

            public void setPtdName(String ptdName) {
                this.ptdName = ptdName;
            }
        }

        public static class PicListBean {
            /**
             * picId :  392347b7-81d1-11e8-ae27-4ccc6a70ac67
             * picNo : 1
             * picName : https:20180707183149398.png
             */

            private String picId;
            private String picNo;
            private String picName;

            public String getPicId() {
                return picId;
            }

            public void setPicId(String picId) {
                this.picId = picId;
            }

            public String getPicNo() {
                return picNo;
            }

            public void setPicNo(String picNo) {
                this.picNo = picNo;
            }

            public String getPicName() {
                return picName;
            }

            public void setPicName(String picName) {
                this.picName = picName;
            }
        }

        public static class ProductListBean {
            /**
             * pDescribe : 餐巾啊啊啊啊
             * pName : 餐巾
             * pBrowseNum : 22
             * ptdName : 餐巾
             * pId : ef3b4321-2ed5-4995-9ace-930d10b9b6e8
             * ptdId : 050301
             * picName : https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180613165254136.jpg
             * pOriginalPrice : 123.0
             * pNowPrice : 111.0
             */

            private String pDescribe;
            private String pName;
            private int pBrowseNum;
            private String ptdName;
            private String pId;
            private String ptdId;
            private String picName;
            private double pOriginalPrice;
            private double pNowPrice;

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

            public int getPBrowseNum() {
                return pBrowseNum;
            }

            public void setPBrowseNum(int pBrowseNum) {
                this.pBrowseNum = pBrowseNum;
            }

            public String getPtdName() {
                return ptdName;
            }

            public void setPtdName(String ptdName) {
                this.ptdName = ptdName;
            }

            public String getPId() {
                return pId;
            }

            public void setPId(String pId) {
                this.pId = pId;
            }

            public String getPtdId() {
                return ptdId;
            }

            public void setPtdId(String ptdId) {
                this.ptdId = ptdId;
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
