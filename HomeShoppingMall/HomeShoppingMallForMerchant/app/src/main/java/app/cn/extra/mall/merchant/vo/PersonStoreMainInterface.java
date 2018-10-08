package app.cn.extra.mall.merchant.vo;

/**
 * Description
 * Data 2018/7/14-13:39
 * Content
 *
 * @author L
 */
public class PersonStoreMainInterface {

    /**
     * errorString :
     * flag : true
     * data : {"yestodayCompleteMoney":0,"firstOfRequirementPrice":"需求类别：房屋装修   标题：装修房子   单价：100.0","firstRoNum":"需求类别：房屋装修(共1单，合计：100.0)","uNickName":"lalallalala","doingOrderNum":1,"sLevel":1,"myArrangeNum":6,"errorString":"","unConfirmOrderNum":0,"totalCompleteMoney":150.12,"completeOrderNum":2,"firstRtNameOfMoney":"需求类别：房屋装修(合计：100.0)","todayCompleteMoney":0,"status":"true"}
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
         * yestodayCompleteMoney : 0.0
         * firstOfRequirementPrice : 需求类别：房屋装修   标题：装修房子   单价：100.0
         * firstRoNum : 需求类别：房屋装修(共1单，合计：100.0)
         * uNickName : lalallalala
         * sName: fdfd
         * doingOrderNum : 1
         * sLevel : 1
         * myArrangeNum : 6
         * errorString :
         * unConfirmOrderNum : 0
         * totalCompleteMoney : 150.12
         * completeOrderNum : 2
         * firstRtNameOfMoney : 需求类别：房屋装修(合计：100.0)
         * todayCompleteMoney : 0.0
         * status : true
         * url :
         */

        private double yestodayCompleteMoney;
        private String firstOfRequirementPrice;
        private String firstRoNum;
        private String uNickName;
        private String sName;
        private int doingOrderNum;
        private int sLevel;
        private int myArrangeNum;
        private String errorString;
        private int unConfirmOrderNum;
        private double totalCompleteMoney;
        private int completeOrderNum;
        private String firstRtNameOfMoney;
        private double todayCompleteMoney;
        private String status;
        private String url;

        public String getsName() {
            return sName;
        }

        public void setsName(String sName) {
            this.sName = sName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public double getYestodayCompleteMoney() {
            return yestodayCompleteMoney;
        }

        public void setYestodayCompleteMoney(double yestodayCompleteMoney) {
            this.yestodayCompleteMoney = yestodayCompleteMoney;
        }

        public String getFirstOfRequirementPrice() {
            return firstOfRequirementPrice;
        }

        public void setFirstOfRequirementPrice(String firstOfRequirementPrice) {
            this.firstOfRequirementPrice = firstOfRequirementPrice;
        }

        public String getFirstRoNum() {
            return firstRoNum;
        }

        public void setFirstRoNum(String firstRoNum) {
            this.firstRoNum = firstRoNum;
        }

        public String getUNickName() {
            return uNickName;
        }

        public void setUNickName(String uNickName) {
            this.uNickName = uNickName;
        }

        public int getDoingOrderNum() {
            return doingOrderNum;
        }

        public void setDoingOrderNum(int doingOrderNum) {
            this.doingOrderNum = doingOrderNum;
        }

        public int getSLevel() {
            return sLevel;
        }

        public void setSLevel(int sLevel) {
            this.sLevel = sLevel;
        }

        public int getMyArrangeNum() {
            return myArrangeNum;
        }

        public void setMyArrangeNum(int myArrangeNum) {
            this.myArrangeNum = myArrangeNum;
        }

        public String getErrorString() {
            return errorString;
        }

        public void setErrorString(String errorString) {
            this.errorString = errorString;
        }

        public int getUnConfirmOrderNum() {
            return unConfirmOrderNum;
        }

        public void setUnConfirmOrderNum(int unConfirmOrderNum) {
            this.unConfirmOrderNum = unConfirmOrderNum;
        }

        public double getTotalCompleteMoney() {
            return totalCompleteMoney;
        }

        public void setTotalCompleteMoney(double totalCompleteMoney) {
            this.totalCompleteMoney = totalCompleteMoney;
        }

        public int getCompleteOrderNum() {
            return completeOrderNum;
        }

        public void setCompleteOrderNum(int completeOrderNum) {
            this.completeOrderNum = completeOrderNum;
        }

        public String getFirstRtNameOfMoney() {
            return firstRtNameOfMoney;
        }

        public void setFirstRtNameOfMoney(String firstRtNameOfMoney) {
            this.firstRtNameOfMoney = firstRtNameOfMoney;
        }

        public double getTodayCompleteMoney() {
            return todayCompleteMoney;
        }

        public void setTodayCompleteMoney(double todayCompleteMoney) {
            this.todayCompleteMoney = todayCompleteMoney;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
