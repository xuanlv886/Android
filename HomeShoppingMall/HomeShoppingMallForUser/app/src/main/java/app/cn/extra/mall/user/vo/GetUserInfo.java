package app.cn.extra.mall.user.vo;

public class GetUserInfo {

    /**
     * errorString :
     * flag : true
     * data : {"uTel":"","uId":"f3a38b3f-5d95-11e8-a00e-4ccc6a3b62ec","uEmail":"","uTrueName":"","uSex":0,"uNickName":"","uCreateTime":"2018-05-22 15:58:46","uBirthday":"","pTag":"f3a38b75-5d95-11e8-a00e-4ccc6a3b62ec","sId":""}
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
         * uTel :
         * uId : f3a38b3f-5d95-11e8-a00e-4ccc6a3b62ec
         * uEmail :
         * uTrueName :
         * uSex : 0
         * uNickName :
         * uCreateTime : 2018-05-22 15:58:46
         * uBirthday :
         * pTag : f3a38b75-5d95-11e8-a00e-4ccc6a3b62ec
         * sId :
         */

        private String uTel;
        private String uId;
        private String uEmail;
        private String uTrueName;
        private int uSex;
        private String uNickName;
        private String uCreateTime;
        private String uBirthday;
        private String pTag;
        private String sId;
        private String picName;

        public String getPicName() {
            return picName;
        }

        public void setPicName(String picName) {
            this.picName = picName;
        }

        public String getUTel() {
            return uTel;
        }

        public void setUTel(String uTel) {
            this.uTel = uTel;
        }

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }

        public String getUEmail() {
            return uEmail;
        }

        public void setUEmail(String uEmail) {
            this.uEmail = uEmail;
        }

        public String getUTrueName() {
            return uTrueName;
        }

        public void setUTrueName(String uTrueName) {
            this.uTrueName = uTrueName;
        }

        public int getUSex() {
            return uSex;
        }

        public void setUSex(int uSex) {
            this.uSex = uSex;
        }

        public String getUNickName() {
            return uNickName;
        }

        public void setUNickName(String uNickName) {
            this.uNickName = uNickName;
        }

        public String getUCreateTime() {
            return uCreateTime;
        }

        public void setUCreateTime(String uCreateTime) {
            this.uCreateTime = uCreateTime;
        }

        public String getUBirthday() {
            return uBirthday;
        }

        public void setUBirthday(String uBirthday) {
            this.uBirthday = uBirthday;
        }

        public String getPTag() {
            return pTag;
        }

        public void setPTag(String pTag) {
            this.pTag = pTag;
        }

        public String getSId() {
            return sId;
        }

        public void setSId(String sId) {
            this.sId = sId;
        }
    }
}
