package app.cn.extra.mall.merchant.vo;

import java.util.List;

/**
 * Description
 * Data 2018/7/26-16:18
 * Content
 *
 * @author L
 */
public class GetMerchantLonLatList {

    /**
     * errorString :
     * flag : true
     * data : [{"mtLon":"116.3499049793749","mtLat":"39.97617053371078"},{"mtLon":"116.34978804908442","mtLat":"39.97619854213431"},{"mtLon":"116.349674596623","mtLat":"39.97623045687959"},{"mtLon":"116.34955525200917","mtLat":"39.97626931100656"},{"mtLon":"116.34943728748914","mtLat":"39.976285626595036"},{"mtLon":"116.34930864705592","mtLat":"39.97628129172198"}]
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
         * mtLon : 116.3499049793749
         * mtLat : 39.97617053371078
         */

        private String mtLon;
        private String mtLat;

        public String getMtLon() {
            return mtLon;
        }

        public void setMtLon(String mtLon) {
            this.mtLon = mtLon;
        }

        public String getMtLat() {
            return mtLat;
        }

        public void setMtLat(String mtLat) {
            this.mtLat = mtLat;
        }
    }
}
