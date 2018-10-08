package app.cn.extra.mall.user.vo;

import java.util.List;

/**
 * Created by Administrator on 2018/2/5 0005.
 */

public class GetCustomer {

    /**
     * errorString :
     * flag : true
     * data : {"pSize":"0","fileName":"header/","address":"西美大厦","freeDays":73,"nickName":"忽呼呼1234","customerRoleId":"28c959d6-443f-11e8-9e34-4ccc6a3b62ec","sex":0,"pId":"99","trueName":"true","bindTel":"18132653088","pJump":"0","pName":"1.png","createTime":"12:00","integral":31961,"payPassword":"true","pTag":"999","lifeServiceList":[{"lsId":"147788ea-4538-11e8-ba7f-4ccc6a3b62ec","lsName":"物业缴费"},{"lsId":"22e41b06-4538-11e8-ba7f-4ccc6a3b62ec","lsName":"积分抽奖"},{"lsId":"2fcb4c73-4538-11e8-ba7f-4ccc6a3b62ec","lsName":"租车约车"}]}
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
         * pSize : 0
         * fileName : header/
         * address : 西美大厦
         * freeDays : 73
         * nickName : 忽呼呼1234
         * customerRoleId : 28c959d6-443f-11e8-9e34-4ccc6a3b62ec
         * sex : 0
         * pId : 99
         * trueName : true
         * bindTel : 18132653088
         * pJump : 0
         * pName : 1.png
         * createTime : 12:00
         * integral : 31961
         * payPassword : true
         * pTag : 999
         * lifeServiceList : [{"lsId":"147788ea-4538-11e8-ba7f-4ccc6a3b62ec","lsName":"物业缴费"},{"lsId":"22e41b06-4538-11e8-ba7f-4ccc6a3b62ec","lsName":"积分抽奖"},{"lsId":"2fcb4c73-4538-11e8-ba7f-4ccc6a3b62ec","lsName":"租车约车"}]
         */

        private String pSize;
        private String fileName;
        private String address;
        private int freeDays;
        private String nickName;
        private String customerRoleId;
        private int sex;
        private String pId;
        private String trueName;
        private String bindTel;
        private String pJump;
        private String pName;
        private String createTime;
        private int integral;
        private String payPassword;
        private String pTag;
        private List<LifeServiceListBean> lifeServiceList;

        public String getPSize() {
            return pSize;
        }

        public void setPSize(String pSize) {
            this.pSize = pSize;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getFreeDays() {
            return freeDays;
        }

        public void setFreeDays(int freeDays) {
            this.freeDays = freeDays;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getCustomerRoleId() {
            return customerRoleId;
        }

        public void setCustomerRoleId(String customerRoleId) {
            this.customerRoleId = customerRoleId;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getPId() {
            return pId;
        }

        public void setPId(String pId) {
            this.pId = pId;
        }

        public String getTrueName() {
            return trueName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }

        public String getBindTel() {
            return bindTel;
        }

        public void setBindTel(String bindTel) {
            this.bindTel = bindTel;
        }

        public String getPJump() {
            return pJump;
        }

        public void setPJump(String pJump) {
            this.pJump = pJump;
        }

        public String getPName() {
            return pName;
        }

        public void setPName(String pName) {
            this.pName = pName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public String getPayPassword() {
            return payPassword;
        }

        public void setPayPassword(String payPassword) {
            this.payPassword = payPassword;
        }

        public String getPTag() {
            return pTag;
        }

        public void setPTag(String pTag) {
            this.pTag = pTag;
        }

        public List<LifeServiceListBean> getLifeServiceList() {
            return lifeServiceList;
        }

        public void setLifeServiceList(List<LifeServiceListBean> lifeServiceList) {
            this.lifeServiceList = lifeServiceList;
        }

        public static class LifeServiceListBean {
            /**
             * lsId : 147788ea-4538-11e8-ba7f-4ccc6a3b62ec
             * lsName : 物业缴费
             */

            private String lsId;
            private String lsName;

            public String getLsId() {
                return lsId;
            }

            public void setLsId(String lsId) {
                this.lsId = lsId;
            }

            public String getLsName() {
                return lsName;
            }

            public void setLsName(String lsName) {
                this.lsName = lsName;
            }
        }
    }
}


