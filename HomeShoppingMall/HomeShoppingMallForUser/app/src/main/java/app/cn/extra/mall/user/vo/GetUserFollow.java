package app.cn.extra.mall.user.vo;

import java.util.List;

public class GetUserFollow {

    /**
     * errorString :
     * flag : true
     * data : {"userAttentionList":[{"uaId":"f3a38b3f-5d95-11e8-o00e-4ccc6a3b62ec","uId":"f3a38b3f-5d95-11e8-a00e-4ccc6a3b62ec","uaCreateTime":"2018-05-21 13:53:00","sLeve":1,"pName":"4.jpg","sName":"企业店铺1","sId":"168eb9f4-46ac-11e8-8390-4ccc6a70ac67"}]}
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
        private List<UserAttentionListBean> userAttentionList;

        public List<UserAttentionListBean> getUserAttentionList() {
            return userAttentionList;
        }

        public void setUserAttentionList(List<UserAttentionListBean> userAttentionList) {
            this.userAttentionList = userAttentionList;
        }

        public static class UserAttentionListBean {
            /**
             * uaId : f3a38b3f-5d95-11e8-o00e-4ccc6a3b62ec
             * uId : f3a38b3f-5d95-11e8-a00e-4ccc6a3b62ec
             * uaCreateTime : 2018-05-21 13:53:00
             * sLeve : 1
             * pName : 4.jpg
             * sName : 企业店铺1
             * sId : 168eb9f4-46ac-11e8-8390-4ccc6a70ac67
             */

            private String uaId;
            private String uId;
            private String uaCreateTime;
            private int sLeve;
            private String pName;
            private String sName;
            private String sId;

            public String getUaId() {
                return uaId;
            }

            public void setUaId(String uaId) {
                this.uaId = uaId;
            }

            public String getUId() {
                return uId;
            }

            public void setUId(String uId) {
                this.uId = uId;
            }

            public String getUaCreateTime() {
                return uaCreateTime;
            }

            public void setUaCreateTime(String uaCreateTime) {
                this.uaCreateTime = uaCreateTime;
            }

            public int getSLeve() {
                return sLeve;
            }

            public void setSLeve(int sLeve) {
                this.sLeve = sLeve;
            }

            public String getPName() {
                return pName;
            }

            public void setPName(String pName) {
                this.pName = pName;
            }

            public String getSName() {
                return sName;
            }

            public void setSName(String sName) {
                this.sName = sName;
            }

            public String getSId() {
                return sId;
            }

            public void setSId(String sId) {
                this.sId = sId;
            }
        }
    }
}
