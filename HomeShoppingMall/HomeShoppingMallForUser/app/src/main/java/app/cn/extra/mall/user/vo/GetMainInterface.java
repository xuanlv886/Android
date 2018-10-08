package app.cn.extra.mall.user.vo;

import java.util.List;

import app.cn.extra.mall.user.utils.Util;

/**
 * Description
 * Data 2018/6/15-15:22
 * Content
 *
 * @author lzy
 */
public class GetMainInterface {

    /**
     * errorString :
     * flag : true
     * data : {"recommendStoreList":[{"rsTitle":"推荐标题","rsContent":"推荐内容","picName":"4.jpg","sId":"168eb9f4-46ac-11e8-8390-4ccc6a70ac67"}],"recommendProductList":[{"pDescribe":"椅子","pName":"椅子","pId":"d6ecf0f5-891f-4a32-943f-6bc149a11d22","picName":"20180613160013396.png","pOriginalPrice":200,"pNowPrice":100},{"pDescribe":"测试股","pName":"测试股","pId":"12302472-ffde-4e94-99df-1cfc79d6f74f","picName":"20180613170320896.png","pOriginalPrice":123,"pNowPrice":111}],"slidePicList":[{"pNo":0,"pName":"20.jpg","pId":"b90543da-706b-11e8-8d97-4ccc6a70ac67"},{"pNo":1,"pName":"21.jpg","pId":"b90b1bf5-706b-11e8-8d97-4ccc6a70ac67"}],"productTypeList":[{"ptName":"家具","ptId":"01"},{"ptName":"家居饰品","ptId":"02"},{"ptName":"家装主材","ptId":"03"},{"ptName":"床上用品","ptId":"04"}]}
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
        private List<RecommendStoreListBean> recommendStoreList;
        private List<RecommendProductListBean> recommendProductList;
        private List<SlidePicListBean> slidePicList;
        private List<ProductTypeListBean> productTypeList;

        public List<RecommendStoreListBean> getRecommendStoreList() {
            return recommendStoreList;
        }

        public void setRecommendStoreList(List<RecommendStoreListBean> recommendStoreList) {
            this.recommendStoreList = recommendStoreList;
        }

        public List<RecommendProductListBean> getRecommendProductList() {
            return recommendProductList;
        }

        public void setRecommendProductList(List<RecommendProductListBean> recommendProductList) {
            this.recommendProductList = recommendProductList;
        }

        public List<SlidePicListBean> getSlidePicList() {
            return slidePicList;
        }

        public void setSlidePicList(List<SlidePicListBean> slidePicList) {
            this.slidePicList = slidePicList;
        }

        public List<ProductTypeListBean> getProductTypeList() {
            return productTypeList;
        }

        public void setProductTypeList(List<ProductTypeListBean> productTypeList) {
            this.productTypeList = productTypeList;
        }

        public static class RecommendStoreListBean {
            /**
             * rsTitle : 推荐标题
             * rsContent : 推荐内容
             * picName : 4.jpg
             * sId : 168eb9f4-46ac-11e8-8390-4ccc6a70ac67
             */

            private String rsTitle;
            private String rsContent;
            private String picName;
            private String sId;

            public String getRsTitle() {
                return rsTitle;
            }

            public void setRsTitle(String rsTitle) {
                this.rsTitle = rsTitle;
            }

            public String getRsContent() {
                return rsContent;
            }

            public void setRsContent(String rsContent) {
                this.rsContent = rsContent;
            }

            public String getPicName() {
                return picName;
            }

            public void setPicName(String picName) {
                this.picName = picName;
            }

            public String getSId() {
                return sId;
            }

            public void setSId(String sId) {
                this.sId = sId;
            }
        }

        public static class RecommendProductListBean {
            /**
             * pName : 椅子
             * pId : d6ecf0f5-891f-4a32-943f-6bc149a11d22
             * url : 20180613160013396.png
             * pOriginalPrice : 200
             * pNowPrice : 100
             * pBrowseNum : 10
             */

            private String pName;
            private String pId;
            private String url;
            private double pOriginalPrice;
            private double pNowPrice;
            private int pBrowseNum;

            public int getpBrowseNum() {
                return pBrowseNum;
            }

            public void setpBrowseNum(int pBrowseNum) {
                this.pBrowseNum = pBrowseNum;
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

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getpOriginalPrice() {
                return Util.doubleToTwoDecimal(pOriginalPrice);
            }

            public void setpOriginalPrice(double pOriginalPrice) {
                this.pOriginalPrice = pOriginalPrice;
            }

            public String getpNowPrice() {
                return Util.doubleToTwoDecimal(pNowPrice);
            }

            public void setpNowPrice(double pNowPrice) {
                this.pNowPrice = pNowPrice;
            }
        }

        public static class SlidePicListBean {
            /**
             * pNo : 0
             * pName : 20.jpg
             * pId : b90543da-706b-11e8-8d97-4ccc6a70ac67
             */

            private int pNo;
            private String pName;
            private String pId;

            public int getPNo() {
                return pNo;
            }

            public void setPNo(int pNo) {
                this.pNo = pNo;
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
        }

        public static class ProductTypeListBean {
            /**
             * ptName : 家具
             * ptId : 01
             */

            private String ptName;
            private String ptId;

            public String getPtName() {
                return ptName;
            }

            public void setPtName(String ptName) {
                this.ptName = ptName;
            }

            public String getPtId() {
                return ptId;
            }

            public void setPtId(String ptId) {
                this.ptId = ptId;
            }
        }
    }
}
