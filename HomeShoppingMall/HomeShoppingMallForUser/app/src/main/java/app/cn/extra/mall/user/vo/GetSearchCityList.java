package app.cn.extra.mall.user.vo;

public class GetSearchCityList {

    /**
     * errorString :
     * flag : true
     * data : {"acId":"2","errorString":"","acCity":"北京市","status":"true"}
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
         * acId : 2
         * errorString :
         * acCity : 北京市
         * status : true
         */

        private String acId;
        private String errorString;
        private String acCity;
        private String status;
        private String acCode;
        /**
         * 城市拼音首字母（自己添加的，后台并没有返回，出现问题时要注意）
         */
        private String acSortLetters;

        public String getAcCode() {
            return acCode;
        }

        public void setAcCode(String acCode) {
            this.acCode = acCode;
        }

        public String getAcSortLetters() {
            return acSortLetters;
        }

        public void setAcSortLetters(String acSortLetters) {
            this.acSortLetters = acSortLetters;
        }

        public String getAcId() {
            return acId;
        }

        public void setAcId(String acId) {
            this.acId = acId;
        }

        public String getErrorString() {
            return errorString;
        }

        public void setErrorString(String errorString) {
            this.errorString = errorString;
        }

        public String getAcCity() {
            return acCity;
        }

        public void setAcCity(String acCity) {
            this.acCity = acCity;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
