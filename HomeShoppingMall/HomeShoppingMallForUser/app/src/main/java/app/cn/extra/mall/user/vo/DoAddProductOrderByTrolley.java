package app.cn.extra.mall.user.vo;

import java.util.List;

/**
 * Created by Administrator on 2018/3/12 0012.
 */

public class DoAddProductOrderByTrolley {

    /**
     * errorString :
     * flag : true
     * data : {"poIdList":[{"poId":"34b25f8f-3316-11e8-a3b1-4ccc6a3b62ec"},{"poId":"34b2f252-3316-11e8-a3b1-4ccc6a3b62ec"}],"payCode":"20180329135830495159","status":"true"}
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
         * poIdList : [{"poId":"34b25f8f-3316-11e8-a3b1-4ccc6a3b62ec"},{"poId":"34b2f252-3316-11e8-a3b1-4ccc6a3b62ec"}]
         * payCode : 20180329135830495159
         * status : true
         */

        private String payCode;
        private String status;
        private List<PoIdListBean> poIdList;

        public String getPayCode() {
            return payCode;
        }

        public void setPayCode(String payCode) {
            this.payCode = payCode;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<PoIdListBean> getPoIdList() {
            return poIdList;
        }

        public void setPoIdList(List<PoIdListBean> poIdList) {
            this.poIdList = poIdList;
        }

        public static class PoIdListBean {
            /**
             * poId : 34b25f8f-3316-11e8-a3b1-4ccc6a3b62ec
             */

            private String poId;

            public String getPoId() {
                return poId;
            }

            public void setPoId(String poId) {
                this.poId = poId;
            }
        }
    }
}
