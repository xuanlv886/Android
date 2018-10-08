package app.cn.extra.mall.merchant.vo;

import java.util.List;

public class GetAllAlreadyCashRecord {

    /**
     * errorString :
     * flag : true
     * data : {"userToCashRecordList":[{"utcrStatus":1,"utcrId":"5a33d988-55c6-11e8-8390-4ccc6a70ac67","psName":"支付宝支付","utcrCreateTime":"2018-05-11 17:23:16","utcrMoney":100.5,"utcrAccount":"1564654413"},{"utcrStatus":1,"utcrId":"5a466c4e-55c6-11e8-8390-4ccc6a70ac67","psName":"微信支付","utcrCreateTime":"2018-05-12 17:23:16","utcrMoney":200.5,"utcrAccount":"49843141616"}]}
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
        private List<UserToCashRecordListBean> userToCashRecordList;

        public List<UserToCashRecordListBean> getUserToCashRecordList() {
            return userToCashRecordList;
        }

        public void setUserToCashRecordList(List<UserToCashRecordListBean> userToCashRecordList) {
            this.userToCashRecordList = userToCashRecordList;
        }

        public static class UserToCashRecordListBean {
            /**
             * utcrStatus : 1
             * utcrId : 5a33d988-55c6-11e8-8390-4ccc6a70ac67
             * psName : 支付宝支付
             * utcrCreateTime : 2018-05-11 17:23:16
             * utcrMoney : 100.5
             * utcrAccount : 1564654413
             */

            private int utcrStatus;
            private String utcrId;
            private String psName;
            private String utcrCreateTime;
            private double utcrMoney;
            private String utcrAccount;

            public int getUtcrStatus() {
                return utcrStatus;
            }

            public void setUtcrStatus(int utcrStatus) {
                this.utcrStatus = utcrStatus;
            }

            public String getUtcrId() {
                return utcrId;
            }

            public void setUtcrId(String utcrId) {
                this.utcrId = utcrId;
            }

            public String getPsName() {
                return psName;
            }

            public void setPsName(String psName) {
                this.psName = psName;
            }

            public String getUtcrCreateTime() {
                return utcrCreateTime;
            }

            public void setUtcrCreateTime(String utcrCreateTime) {
                this.utcrCreateTime = utcrCreateTime;
            }

            public double getUtcrMoney() {
                return utcrMoney;
            }

            public void setUtcrMoney(double utcrMoney) {
                this.utcrMoney = utcrMoney;
            }

            public String getUtcrAccount() {
                return utcrAccount;
            }

            public void setUtcrAccount(String utcrAccount) {
                this.utcrAccount = utcrAccount;
            }
        }
    }
}
