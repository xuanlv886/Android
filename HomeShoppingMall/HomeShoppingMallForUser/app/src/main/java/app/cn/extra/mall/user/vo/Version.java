package app.cn.extra.mall.user.vo;

/**
 * Created by Administrator on 2018/4/18 0018.
 */

public class Version {

    /**
     * errorString :
     * flag : true
     * data : {"vId":"100M","vType":1,"vCode":"2018-04-17 00:00:00","vCreateTime":"跳转路径","vIfMust":"2018041717","vFileSize":"版本更新说明","vContent":"false","vBuildCode":"false","vSystemType":"false","vUrl":"false"}
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
         * vId : 100M
         * vType : 1
         * vCode : 2018-04-17 00:00:00
         * vCreateTime : 跳转路径
         * vIfMust : 2018041717
         * vFileSize : 版本更新说明
         * vContent : false
         * vBuildCode : false
         * vSystemType : false
         * vUrl : false
         */

        private String vId;
        private int vType;
        private String vCode;
        private String vCreateTime;
        private String vIfMust;
        private String vFileSize;
        private String vContent;
        private String vBuildCode;
        private String vSystemType;
        private String vUrl;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getVId() {
            return vId;
        }

        public void setVId(String vId) {
            this.vId = vId;
        }

        public int getVType() {
            return vType;
        }

        public void setVType(int vType) {
            this.vType = vType;
        }

        public String getVCode() {
            return vCode;
        }

        public void setVCode(String vCode) {
            this.vCode = vCode;
        }

        public String getVCreateTime() {
            return vCreateTime;
        }

        public void setVCreateTime(String vCreateTime) {
            this.vCreateTime = vCreateTime;
        }

        public String getVIfMust() {
            return vIfMust;
        }

        public void setVIfMust(String vIfMust) {
            this.vIfMust = vIfMust;
        }

        public String getVFileSize() {
            return vFileSize;
        }

        public void setVFileSize(String vFileSize) {
            this.vFileSize = vFileSize;
        }

        public String getVContent() {
            return vContent;
        }

        public void setVContent(String vContent) {
            this.vContent = vContent;
        }

        public String getVBuildCode() {
            return vBuildCode;
        }

        public void setVBuildCode(String vBuildCode) {
            this.vBuildCode = vBuildCode;
        }

        public String getVSystemType() {
            return vSystemType;
        }

        public void setVSystemType(String vSystemType) {
            this.vSystemType = vSystemType;
        }

        public String getVUrl() {
            return vUrl;
        }

        public void setVUrl(String vUrl) {
            this.vUrl = vUrl;
        }
    }
}
