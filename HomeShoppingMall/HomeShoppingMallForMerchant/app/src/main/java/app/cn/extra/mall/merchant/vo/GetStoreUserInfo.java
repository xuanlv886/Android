package app.cn.extra.mall.merchant.vo;

import java.util.List;

/**
 * Description
 * Data 2018/7/16-16:49
 * Content
 *
 * @author L
 */
public class GetStoreUserInfo {

    /**
     * errorString :
     * flag : true
     * data : {"uEmail":"","sLeader":"大明","uTrueName":"大明","sName":"企业店铺1","uSex":1,"profile":"","sLevel":4,"uNickName":"","uBirthday":"","uTel":"18132653082","errorString":"","sDescribe":"企业身份店铺","sAddress":"河北省石家庄市裕华区西美SOHO","storePics":[{"pNo":0,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/store/20180716145350720513.png"},{"pNo":2,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/store/20180716145507655814.png"}],"sLegal":"法人姓名","sTel":"18132653082","pTag":"d8c128f4-46a9-11e8-8390-4ccc6a70ac67","status":"true"}
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
         * uEmail :
         * sLeader : 大明
         * uTrueName : 大明
         * sName : 企业店铺1
         * uSex : 1
         * profile :
         * sLevel : 4
         * uNickName :
         * uBirthday :
         * uTel : 18132653082
         * errorString :
         * sDescribe : 企业身份店铺
         * sAddress : 河北省石家庄市裕华区西美SOHO
         * storePics : [{"pNo":0,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/store/20180716145350720513.png"},{"pNo":2,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/store/20180716145507655814.png"}]
         * sLegal : 法人姓名
         * sTel : 18132653082
         * pTag : d8c128f4-46a9-11e8-8390-4ccc6a70ac67
         * status : true
         */

        private String uEmail;
        private String sLeader;
        private String uTrueName;
        private String sName;
        private int uSex;
        private String profile;
        private int sLevel;
        private String uNickName;
        private String uBirthday;
        private String uTel;
        private String errorString;
        private String sDescribe;
        private String sAddress;
        private String sLegal;
        private String sTel;
        private String pTag;
        private String status;
        private List<StorePicsBean> storePics;

        public String getUEmail() {
            return uEmail;
        }

        public void setUEmail(String uEmail) {
            this.uEmail = uEmail;
        }

        public String getSLeader() {
            return sLeader;
        }

        public void setSLeader(String sLeader) {
            this.sLeader = sLeader;
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

        public int getUSex() {
            return uSex;
        }

        public void setUSex(int uSex) {
            this.uSex = uSex;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public int getSLevel() {
            return sLevel;
        }

        public void setSLevel(int sLevel) {
            this.sLevel = sLevel;
        }

        public String getUNickName() {
            return uNickName;
        }

        public void setUNickName(String uNickName) {
            this.uNickName = uNickName;
        }

        public String getUBirthday() {
            return uBirthday;
        }

        public void setUBirthday(String uBirthday) {
            this.uBirthday = uBirthday;
        }

        public String getUTel() {
            return uTel;
        }

        public void setUTel(String uTel) {
            this.uTel = uTel;
        }

        public String getErrorString() {
            return errorString;
        }

        public void setErrorString(String errorString) {
            this.errorString = errorString;
        }

        public String getSDescribe() {
            return sDescribe;
        }

        public void setSDescribe(String sDescribe) {
            this.sDescribe = sDescribe;
        }

        public String getSAddress() {
            return sAddress;
        }

        public void setSAddress(String sAddress) {
            this.sAddress = sAddress;
        }

        public String getSLegal() {
            return sLegal;
        }

        public void setSLegal(String sLegal) {
            this.sLegal = sLegal;
        }

        public String getSTel() {
            return sTel;
        }

        public void setSTel(String sTel) {
            this.sTel = sTel;
        }

        public String getPTag() {
            return pTag;
        }

        public void setPTag(String pTag) {
            this.pTag = pTag;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<StorePicsBean> getStorePics() {
            return storePics;
        }

        public void setStorePics(List<StorePicsBean> storePics) {
            this.storePics = storePics;
        }

        public static class StorePicsBean {
            /**
             * pNo : 0
             * url : https://hsmcommon.oss-cn-beijing.aliyuncs.com/store/20180716145350720513.png
             */

            private int pNo;
            private String url;

            public int getPNo() {
                return pNo;
            }

            public void setPNo(int pNo) {
                this.pNo = pNo;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
