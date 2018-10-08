package app.cn.extra.mall.merchant.vo;

import java.util.List;

/**
 * Description
 * Data 2018/7/27-10:43
 * Content
 *
 * @author L
 */
public class MsGetMyArrangeListBySId {

    /**
     * errorString :
     * flag : true
     * data : [{"saContent":"324234","saCreateTime":"2018-06-05 16:47:54","uTrueName":"小明","sName":"个人店铺1","saId":"8e9a5cfd-689d-11e8-8390-4ccc6a70ac67"},{"saContent":"232","saCreateTime":"2018-05-28 17:48:40","uTrueName":"小明","sName":"个人店铺1","saId":"a9c19861-625c-11e8-8390-4ccc6a70ac67"},{"saContent":"56冻死个 规范化代发货规范化风格","saCreateTime":"2018-06-05 16:48:53","uTrueName":"小明","sName":"个人店铺1","saId":"b19e14d5-689d-11e8-8390-4ccc6a70ac67"},{"saContent":"地方大师傅似的过分的事f发士大夫","saCreateTime":"2018-06-05 16:49:13","uTrueName":"小明","sName":"个人店铺1","saId":"bd95ea7f-689d-11e8-8390-4ccc6a70ac67"},{"saContent":"23432432432424234","saCreateTime":"2018-06-05 16:49:34","uTrueName":"小明","sName":"个人店铺1","saId":"ca278844-689d-11e8-8390-4ccc6a70ac67"},{"saContent":"234324嗯温热温热我","saCreateTime":"2018-06-05 16:49:58","uTrueName":"小明","sName":"个人店铺1","saId":"d84d5a8b-689d-11e8-8390-4ccc6a70ac67"}]
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
         * saContent : 324234
         * saCreateTime : 2018-06-05 16:47:54
         * uTrueName : 小明
         * sName : 个人店铺1
         * saId : 8e9a5cfd-689d-11e8-8390-4ccc6a70ac67
         */

        private String saContent;
        private String saCreateTime;
        private String uTrueName;
        private String sName;
        private String saId;

        public String getSaContent() {
            return saContent;
        }

        public void setSaContent(String saContent) {
            this.saContent = saContent;
        }

        public String getSaCreateTime() {
            return saCreateTime;
        }

        public void setSaCreateTime(String saCreateTime) {
            this.saCreateTime = saCreateTime;
        }

        public String getUTrueName() {
            return uTrueName;
        }

        public void setUTrueName(String uTrueName) {
            this.uTrueName = uTrueName;
        }

        public String getSName() {
            return sName;
        }

        public void setSName(String sName) {
            this.sName = sName;
        }

        public String getSaId() {
            return saId;
        }

        public void setSaId(String saId) {
            this.saId = saId;
        }
    }
}
