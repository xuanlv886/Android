package app.cn.extra.mall.user.vo;

public class GetPersonCenter {

    /**
     * errorString :
     * flag : true
     * data : {"userCollectionNum":2,"userAttentionNum":0,"footPrintNum":2,"uNickName":"Lanmiaotaoqi","userEvaluate":1,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/profilephoto/1532057482.jpg"}
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
         * userCollectionNum : 2
         * userAttentionNum : 0
         * footPrintNum : 2
         * uNickName : Lanmiaotaoqi
         * userEvaluate : 1
         * url : https://hsmcommon.oss-cn-beijing.aliyuncs.com/profilephoto/1532057482.jpg
         */

        private int userCollectionNum;
        private int userAttentionNum;
        private int footPrintNum;
        private String uNickName;
        private int userEvaluate;
        private String url;

        public int getUserCollectionNum() {
            return userCollectionNum;
        }

        public void setUserCollectionNum(int userCollectionNum) {
            this.userCollectionNum = userCollectionNum;
        }

        public int getUserAttentionNum() {
            return userAttentionNum;
        }

        public void setUserAttentionNum(int userAttentionNum) {
            this.userAttentionNum = userAttentionNum;
        }

        public int getFootPrintNum() {
            return footPrintNum;
        }

        public void setFootPrintNum(int footPrintNum) {
            this.footPrintNum = footPrintNum;
        }

        public String getUNickName() {
            return uNickName;
        }

        public void setUNickName(String uNickName) {
            this.uNickName = uNickName;
        }

        public int getUserEvaluate() {
            return userEvaluate;
        }

        public void setUserEvaluate(int userEvaluate) {
            this.userEvaluate = userEvaluate;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
