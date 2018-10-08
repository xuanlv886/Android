package app.cn.extra.mall.user.vo;

import java.util.List;

/**
 * Created by Administrator on 2018/3/9 0009.
 */

public class GetAvailableCoupons {

    /**
     * errorString :
     * flag : true
     * data : {"customerCouponsList":[{"nameType":"生活服务","ctType":1,"ctId":"3","ccMoney":"15","startTime":"2018-3-5","id":"3","endTime":"2018-3-10","needOver":"0","content":"15元红包","status":"0"},{"nameType":"全场通用","ctType":1,"ctId":"4","ccMoney":"15","startTime":"2018-3-5","id":"4","endTime":"2018-3-10","needOver":"0","content":"15元红包","status":"0"},{"nameType":"便捷购物","ctType":1,"ctId":"5","ccMoney":"10","startTime":"2018-3-16","id":"7","endTime":"2018-3-18","needOver":"0","content":"10元红包","status":"0"},{"nameType":"便捷购物","ctType":1,"ctId":"6","ccMoney":"5","startTime":"2018-03-17 00:00:00","id":"8","endTime":"2018-03-20 00:00:00","needOver":"0","content":"5元红包","status":"0"},{"nameType":"便捷购物","ctType":0,"ctId":"7","ccMoney":"50","startTime":"2018-03-17 00:00:00","id":"9","endTime":"2018-04-17 00:00:00","needOver":"100","content":"满100减50","status":"0"}]}
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
        private List<CustomerCouponsListBean> customerCouponsList;

        public List<CustomerCouponsListBean> getCustomerCouponsList() {
            return customerCouponsList;
        }

        public void setCustomerCouponsList(List<CustomerCouponsListBean> customerCouponsList) {
            this.customerCouponsList = customerCouponsList;
        }

        public static class CustomerCouponsListBean {
            /**
             * nameType : 生活服务
             * ctType : 1
             * ctId : 3
             * ccMoney : 15
             * startTime : 2018-3-5
             * id : 3
             * endTime : 2018-3-10
             * needOver : 0
             * content : 15元红包
             * status : 0
             */

            private String nameType;
            private int ctType;
            private String ctId;
            private String ccMoney;
            private String startTime;
            private String id;
            private String endTime;
            private String needOver;
            private String content;
            private String status;

            public String getNameType() {
                return nameType;
            }

            public void setNameType(String nameType) {
                this.nameType = nameType;
            }

            public int getCtType() {
                return ctType;
            }

            public void setCtType(int ctType) {
                this.ctType = ctType;
            }

            public String getCtId() {
                return ctId;
            }

            public void setCtId(String ctId) {
                this.ctId = ctId;
            }

            public String getCcMoney() {
                return ccMoney;
            }

            public void setCcMoney(String ccMoney) {
                this.ccMoney = ccMoney;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getNeedOver() {
                return needOver;
            }

            public void setNeedOver(String needOver) {
                this.needOver = needOver;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}
