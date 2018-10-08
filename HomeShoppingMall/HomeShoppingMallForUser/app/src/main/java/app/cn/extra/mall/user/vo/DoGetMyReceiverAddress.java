package app.cn.extra.mall.user.vo;

import java.util.List;

/**
 * Created by Administrator on 2018/3/5 0005.
 * 获取用户收货地址
 */

public class DoGetMyReceiverAddress {

    /**
     * errorString :
     * flag : true
     * data : {"userDeliverAddressList":[{"uId":"3eec4983-2f24-448c-8e3a-5902c4e631b6","udaId":"619a0c3e-a03a-11e8-a107-4ccc6a3b62ec","udaTrueName":"聂","udaDefault":1,"udaAddress":"石家庄市","udaTel":"13739716863"},{"uId":"3eec4983-2f24-448c-8e3a-5902c4e631b6","udaId":"659e1478-a03b-11e8-a107-4ccc6a3b62ec","udaTrueName":"聂","udaDefault":0,"udaAddress":"西城区","udaTel":"13739716863"}]}
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
        private List<UserDeliverAddressListBean> userDeliverAddressList;

        public List<UserDeliverAddressListBean> getUserDeliverAddressList() {
            return userDeliverAddressList;
        }

        public void setUserDeliverAddressList(List<UserDeliverAddressListBean> userDeliverAddressList) {
            this.userDeliverAddressList = userDeliverAddressList;
        }

        public static class UserDeliverAddressListBean {
            /**
             * uId : 3eec4983-2f24-448c-8e3a-5902c4e631b6
             * udaId : 619a0c3e-a03a-11e8-a107-4ccc6a3b62ec
             * udaTrueName : 聂
             * udaDefault : 1
             * udaAddress : 石家庄市
             * udaTel : 13739716863
             */

            private String uId;
            private String udaId;
            private String udaTrueName;
            private int udaDefault;
            private String udaAddress;
            private String udaTel;

            public String getUId() {
                return uId;
            }

            public void setUId(String uId) {
                this.uId = uId;
            }

            public String getUdaId() {
                return udaId;
            }

            public void setUdaId(String udaId) {
                this.udaId = udaId;
            }

            public String getUdaTrueName() {
                return udaTrueName;
            }

            public void setUdaTrueName(String udaTrueName) {
                this.udaTrueName = udaTrueName;
            }

            public int getUdaDefault() {
                return udaDefault;
            }

            public void setUdaDefault(int udaDefault) {
                this.udaDefault = udaDefault;
            }

            public String getUdaAddress() {
                return udaAddress;
            }

            public void setUdaAddress(String udaAddress) {
                this.udaAddress = udaAddress;
            }

            public String getUdaTel() {
                return udaTel;
            }

            public void setUdaTel(String udaTel) {
                this.udaTel = udaTel;
            }
        }
    }
}
