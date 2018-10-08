package app.cn.extra.mall.user.vo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/21 0021.
 */

public class SelfrunMarketProListGetProperty {

    /**
     * errorString :
     * flag : true
     * data : {"size":3,"propertyList":[{"propertyName":"材质","propertyValueList":[{"propertyValue":"不锈钢","propertyId":"8aed8df3-3487-11e7-add7-00163e2e094a"},{"propertyValue":"人造棉","propertyId":"4c9e6c97-3456-11e7-add7-00163e2e094a"},{"propertyValue":"人造水晶","propertyId":"8aed8e6e-3487-11e7-add7-00163e2e094a"},{"propertyValue":"合成树脂","propertyId":"8aed8ee6-3487-11e7-add7-00163e2e094a"}]},{"propertyName":"颜色分类","propertyValueList":[{"propertyValue":"乳白色","propertyId":"3f0518bc-33ec-11e7-add7-00163e2e094a"},{"propertyValue":"白色","propertyId":"3f051ad7-33ec-11e7-add7-00163e2e094a"},{"propertyValue":"米白色","propertyId":"3f051b65-33ec-11e7-add7-00163e2e094a"}]},{"propertyName":"品相","propertyValueList":[{"propertyValue":"七品以下","propertyId":"e906593a-36da-11e7-add7-00163e2e094a"},{"propertyValue":"九品","propertyId":"e90659c6-36da-11e7-add7-00163e2e094a"}]}],"brandList":["刚刚","时尚"]}
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
         * size : 3
         * propertyList : [{"propertyName":"材质","propertyValueList":[{"propertyValue":"不锈钢","propertyId":"8aed8df3-3487-11e7-add7-00163e2e094a"},{"propertyValue":"人造棉","propertyId":"4c9e6c97-3456-11e7-add7-00163e2e094a"},{"propertyValue":"人造水晶","propertyId":"8aed8e6e-3487-11e7-add7-00163e2e094a"},{"propertyValue":"合成树脂","propertyId":"8aed8ee6-3487-11e7-add7-00163e2e094a"}]},{"propertyName":"颜色分类","propertyValueList":[{"propertyValue":"乳白色","propertyId":"3f0518bc-33ec-11e7-add7-00163e2e094a"},{"propertyValue":"白色","propertyId":"3f051ad7-33ec-11e7-add7-00163e2e094a"},{"propertyValue":"米白色","propertyId":"3f051b65-33ec-11e7-add7-00163e2e094a"}]},{"propertyName":"品相","propertyValueList":[{"propertyValue":"七品以下","propertyId":"e906593a-36da-11e7-add7-00163e2e094a"},{"propertyValue":"九品","propertyId":"e90659c6-36da-11e7-add7-00163e2e094a"}]}]
         * brandList : ["刚刚","时尚"]
         */

        private int size;
        private List<PropertyListBean> propertyList;
        private List<String> brandList;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public List<PropertyListBean> getPropertyList() {
            return propertyList;
        }

        public void setPropertyList(List<PropertyListBean> propertyList) {
            this.propertyList = propertyList;
        }

        public List<String> getBrandList() {
            return brandList;
        }

        public void setBrandList(List<String> brandList) {
            this.brandList = brandList;
        }

        public static class PropertyListBean {
            /**
             * propertyName : 材质
             * propertyValueList : [{"propertyValue":"不锈钢","propertyId":"8aed8df3-3487-11e7-add7-00163e2e094a"},{"propertyValue":"人造棉","propertyId":"4c9e6c97-3456-11e7-add7-00163e2e094a"},{"propertyValue":"人造水晶","propertyId":"8aed8e6e-3487-11e7-add7-00163e2e094a"},{"propertyValue":"合成树脂","propertyId":"8aed8ee6-3487-11e7-add7-00163e2e094a"}]
             */

            private String propertyName;
            private List<PropertyValueListBean> propertyValueList;

            public String getPropertyName() {
                return propertyName;
            }

            public void setPropertyName(String propertyName) {
                this.propertyName = propertyName;
            }

            public List<PropertyValueListBean> getPropertyValueList() {
                return propertyValueList;
            }

            public void setPropertyValueList(List<PropertyValueListBean> propertyValueList) {
                this.propertyValueList = propertyValueList;
            }

            public static class PropertyValueListBean {
                /**
                 * propertyValue : 不锈钢
                 * propertyId : 8aed8df3-3487-11e7-add7-00163e2e094a
                 */

                private String propertyValue;
                private String propertyId;

                public String getPropertyValue() {
                    return propertyValue;
                }

                public void setPropertyValue(String propertyValue) {
                    this.propertyValue = propertyValue;
                }

                public String getPropertyId() {
                    return propertyId;
                }

                public void setPropertyId(String propertyId) {
                    this.propertyId = propertyId;
                }
            }
        }
    }
}
