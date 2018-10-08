package app.cn.extra.mall.user.vo;

import java.util.List;

import app.cn.extra.mall.user.utils.Util;

public class GetOrderData {

    /**
     * poDeliverName : 收货人姓名
     * poDeliverTel : 收货人联系电话
     * poDeliverAddress : 收货人地址
     * stores : [{"sId":"商品所属的店铺的主键标识","products":[{"pId":"商品主键标识","podNum":"购买的数量","podProperty":"要购买的商品的属性","podPrice":"购买的商品的单价"},{"pId":"商品主键标识","podNum":"购买的数量","podProperty":"要购买的商品的属性","podPrice":"购买的商品的单价"}]},{"sId":"商品所属的店铺的主键标识","products":[{"pId":"商品主键标识","podNum":"购买的数量","podProperty":"要购买的商品的属性","podPrice":"购买的商品的单价"},{"pId":"商品主键标识","podNum":"购买的数量","podProperty":"要购买的商品的属性","podPrice":"购买的商品的单价"}]}]
     */

    private String poDeliverName;
    private String poDeliverTel;
    private String poDeliverAddress;
    private List<StoresBean> stores;

    public String getPoDeliverName() {
        return poDeliverName;
    }

    public void setPoDeliverName(String poDeliverName) {
        this.poDeliverName = poDeliverName;
    }

    public String getPoDeliverTel() {
        return poDeliverTel;
    }

    public void setPoDeliverTel(String poDeliverTel) {
        this.poDeliverTel = poDeliverTel;
    }

    public String getPoDeliverAddress() {
        return poDeliverAddress;
    }

    public void setPoDeliverAddress(String poDeliverAddress) {
        this.poDeliverAddress = poDeliverAddress;
    }

    public List<StoresBean> getStores() {
        return stores;
    }

    public void setStores(List<StoresBean> stores) {
        this.stores = stores;
    }

    public static class StoresBean {
        /**
         * sId : 商品所属的店铺的主键标识
         * products : [{"pId":"商品主键标识","podNum":"购买的数量","podProperty":"要购买的商品的属性","podPrice":"购买的商品的单价"},{"pId":"商品主键标识","podNum":"购买的数量","podProperty":"要购买的商品的属性","podPrice":"购买的商品的单价"}]
         */

        private String sId;
        private List<ProductsBean> products;

        public String getSId() {
            return sId;
        }

        public void setSId(String sId) {
            this.sId = sId;
        }

        public List<ProductsBean> getProducts() {
            return products;
        }

        public void setProducts(List<ProductsBean> products) {
            this.products = products;
        }

        public static class ProductsBean {
            /**
             * pId : 商品主键标识
             * podNum : 购买的数量
             * podProperty : 要购买的商品的属性
             * podPrice : 购买的商品的单价
             */

            private String pId;
            private String podNum;
            private String podProperty;
            private double podPrice;

            public String getPId() {
                return pId;
            }

            public void setPId(String pId) {
                this.pId = pId;
            }

            public String getPodNum() {
                return podNum;
            }

            public void setPodNum(String podNum) {
                this.podNum = podNum;
            }

            public String getPodProperty() {
                return podProperty;
            }

            public void setPodProperty(String podProperty) {
                this.podProperty = podProperty;
            }

            public String getPodPrice() {
                return Util.doubleToTwoDecimal(podPrice);
            }

            public void setPodPrice(double podPrice) {
                this.podPrice = podPrice;
            }
        }
    }
}
