package app.cn.extra.mall.user.vo;

import java.util.List;

import app.cn.extra.mall.user.utils.Util;

/**
 * Created by Administrator on 2018/3/1 0001.
 */

public class SelectByloosename {

    /**
     * errorString :
     * flag : true
     * data : {"productMessage":[{"payNum":"0","productId":"3aa5554b-4867-11e8-8390-4ccc6a70ac67","proName":"红木餐桌","proPrice":"4800.00"}],"productType":[{"name":"餐桌+餐椅","id":"0101040109"}]}
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
        private List<ProductMessageBean> productMessage;
        private List<ProductTypeBean> productType;

        public List<ProductMessageBean> getProductMessage() {
            return productMessage;
        }

        public void setProductMessage(List<ProductMessageBean> productMessage) {
            this.productMessage = productMessage;
        }

        public List<ProductTypeBean> getProductType() {
            return productType;
        }

        public void setProductType(List<ProductTypeBean> productType) {
            this.productType = productType;
        }

        public static class ProductMessageBean {
            /**
             * payNum : 0
             * productId : 3aa5554b-4867-11e8-8390-4ccc6a70ac67
             * proName : 红木餐桌
             * proPrice : 4800.00
             */

            private String payNum;
            private String productId;
            private String proName;
            private double proPrice;
            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getPayNum() {
                return payNum;
            }

            public void setPayNum(String payNum) {
                this.payNum = payNum;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getProName() {
                return proName;
            }

            public void setProName(String proName) {
                this.proName = proName;
            }

            public String getProPrice() {
                return Util.doubleToTwoDecimal(proPrice);
            }

            public void setProPrice(double proPrice) {
                this.proPrice = proPrice;
            }
        }

        public static class ProductTypeBean {
            /**
             * name : 餐桌+餐椅
             * id : 0101040109
             */

            private String name;
            private String id;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        }
    }
}
