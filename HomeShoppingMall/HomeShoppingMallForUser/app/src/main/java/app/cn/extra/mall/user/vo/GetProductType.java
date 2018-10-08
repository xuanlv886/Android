package app.cn.extra.mall.user.vo;

import java.util.List;

public class GetProductType {

    /**
     * errorString :
     * flag : true
     * data : {"productTypeList":[{"ptName":"家具","ptId":"01"},{"ptName":"家居饰品","ptId":"02"},{"ptName":"家装主材","ptId":"03"},{"ptName":"床上用品","ptId":"04"},{"ptName":"居家布艺","ptId":"05"},{"ptName":"装修定制","ptId":"06"}]}
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
        private List<ProductTypeListBean> productTypeList;

        public List<ProductTypeListBean> getProductTypeList() {
            return productTypeList;
        }

        public void setProductTypeList(List<ProductTypeListBean> productTypeList) {
            this.productTypeList = productTypeList;
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
