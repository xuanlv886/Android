package app.cn.extra.mall.user.vo;

import java.util.List;

public class GetUserDeliverAddress {

    /**
     * errorString :
     * flag : true
     * data : {"userDeliverAddressList":[{"uId":"f3a38b3f-5d95-11e8-a00e-4ccc6a3b62ec","udaId":"0392653b-5d9c-11e8-a00e-4ccc6a3b62ec","udaTrueName":"阿龙","udaDefault":1,"udaAddress":"西美806","udaTel":"13785189288"},{"uId":"f3a38b3f-5d95-11e8-a00e-4ccc6a3b62ec","udaId":"0eaec9d5-5d9c-11e8-a00e-4ccc6a3b62ec","udaTrueName":"阿龙","udaDefault":1,"udaAddress":"西美soho","udaTel":"13785189288"},{"uId":"f3a38b3f-5d95-11e8-a00e-4ccc6a3b62ec","udaId":"f5169153-5d9b-11e8-a00e-4ccc6a3b62ec","udaTrueName":"阿龙","udaDefault":0,"udaAddress":"宝翠商务","udaTel":"13785189288"}]}
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
             * uId : f3a38b3f-5d95-11e8-a00e-4ccc6a3b62ec
             * udaId : 0392653b-5d9c-11e8-a00e-4ccc6a3b62ec
             * udaTrueName : 阿龙
             * udaDefault : 1
             * udaAddress : 西美806
             * udaTel : 13785189288
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
