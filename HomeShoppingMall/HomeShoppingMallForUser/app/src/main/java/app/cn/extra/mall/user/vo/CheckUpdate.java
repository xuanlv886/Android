package app.cn.extra.mall.user.vo;

/**
 * Description
 * Data 2018/7/19-16:10
 * Content
 *
 * @author L
 */
public class CheckUpdate {

    /**
     * errorString :
     * flag : true
     * data : {"vCreateTime":"2018-07-19 15:10:35","vContent":"修复系统bug","isNeedUpdate":"true","vCode":"2018072001","vFileSize":"20M","vIfMust":0,"vUrl":"https://cfkjcommon1.oss-cn-beijing.aliyuncs.com/timeshare/apk/merchant-2018051601.apk"}
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
         * vCreateTime : 2018-07-19 15:10:35
         * vContent : 修复系统bug
         * isNeedUpdate : true
         * vCode : 2018072001
         * vFileSize : 20M
         * vIfMust : 0
         * vUrl : https://cfkjcommon1.oss-cn-beijing.aliyuncs.com/timeshare/apk/merchant-2018051601.apk
         */

        private String vCreateTime;
        private String vContent;
        private String isNeedUpdate;
        private String vCode;
        private String vFileSize;
        private int vIfMust;
        private String vUrl;

        public String getVCreateTime() {
            return vCreateTime;
        }

        public void setVCreateTime(String vCreateTime) {
            this.vCreateTime = vCreateTime;
        }

        public String getVContent() {
            return vContent;
        }

        public void setVContent(String vContent) {
            this.vContent = vContent;
        }

        public String getIsNeedUpdate() {
            return isNeedUpdate;
        }

        public void setIsNeedUpdate(String isNeedUpdate) {
            this.isNeedUpdate = isNeedUpdate;
        }

        public String getVCode() {
            return vCode;
        }

        public void setVCode(String vCode) {
            this.vCode = vCode;
        }

        public String getVFileSize() {
            return vFileSize;
        }

        public void setVFileSize(String vFileSize) {
            this.vFileSize = vFileSize;
        }

        public int getVIfMust() {
            return vIfMust;
        }

        public void setVIfMust(int vIfMust) {
            this.vIfMust = vIfMust;
        }

        public String getVUrl() {
            return vUrl;
        }

        public void setVUrl(String vUrl) {
            this.vUrl = vUrl;
        }
    }
}
