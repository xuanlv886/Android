package app.cn.extra.mall.merchant.vo;

/**
 * Description
 * Data 2018/7/14-16:25
 * Content
 *
 * @author L
 */
public class CompanyStoreMainInterface {

    /**
     * errorString :
     * flag : true
     * data : {"todayProductOrderNum":0,"yestodayCompleteMoney":0,"firstOfRequirementPrice":"暂无","sName":"企业店铺1","unReundProductOrderNum":0,"firstRoNum":"需求类别：房屋装修(共0单，合计：0.0)","uNickName":"","sLevel":4,"doingOrderNum":0,"yestodayProductOrderMoney":0,"myArrangeNum":3,"ProductOrderMoney":4800,"url":"","unPayProductOrderNum":0,"unDeliverProductOrderNum":0,"errorString":"","unConfirmOrderNum":0,"todayProductOrderMoney":0,"totalCompleteMoney":0,"completeOrderNum":0,"firstRtNameOfMoney":"需求类别：房屋装修(合计：0.0)","todayCompleteMoney":0,"completeProductOrderNum":0,"status":"true"}
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
         * todayProductOrderNum : 0
         * yestodayCompleteMoney : 0.0
         * firstOfRequirementPrice : 暂无
         * sName : 企业店铺1
         * unReundProductOrderNum : 0
         * firstRoNum : 需求类别：房屋装修(共0单，合计：0.0)
         * uNickName :
         * sLevel : 4
         * doingOrderNum : 0
         * yestodayProductOrderMoney : 0.0
         * myArrangeNum : 3
         * ProductOrderMoney : 4800.0
         * url :
         * unPayProductOrderNum : 0
         * unDeliverProductOrderNum : 0
         * errorString :
         * unConfirmOrderNum : 0
         * todayProductOrderMoney : 0.0
         * totalCompleteMoney : 0.0
         * completeOrderNum : 0
         * firstRtNameOfMoney : 需求类别：房屋装修(合计：0.0)
         * todayCompleteMoney : 0.0
         * completeProductOrderNum : 0
         * status : true
         */

        private int todayProductOrderNum;
        private double yestodayCompleteMoney;
        private String firstOfRequirementPrice;
        private String sName;
        private int unReundProductOrderNum;
        private String firstRoNum;
        private String uNickName;
        private int sLevel;
        private int doingOrderNum;
        private double yestodayProductOrderMoney;
        private int myArrangeNum;
        private double ProductOrderMoney;
        private String url;
        private int unPayProductOrderNum;
        private int unDeliverProductOrderNum;
        private String errorString;
        private int unConfirmOrderNum;
        private double todayProductOrderMoney;
        private double totalCompleteMoney;
        private int completeOrderNum;
        private String firstRtNameOfMoney;
        private double todayCompleteMoney;
        private int completeProductOrderNum;
        private String status;

        public int getTodayProductOrderNum() {
            return todayProductOrderNum;
        }

        public void setTodayProductOrderNum(int todayProductOrderNum) {
            this.todayProductOrderNum = todayProductOrderNum;
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

        public String getSName() {
            return sName;
        }

        public void setSName(String sName) {
            this.sName = sName;
        }

        public int getUnReundProductOrderNum() {
            return unReundProductOrderNum;
        }

        public void setUnReundProductOrderNum(int unReundProductOrderNum) {
            this.unReundProductOrderNum = unReundProductOrderNum;
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

        public int getSLevel() {
            return sLevel;
        }

        public void setSLevel(int sLevel) {
            this.sLevel = sLevel;
        }

        public int getDoingOrderNum() {
            return doingOrderNum;
        }

        public void setDoingOrderNum(int doingOrderNum) {
            this.doingOrderNum = doingOrderNum;
        }

        public double getYestodayProductOrderMoney() {
            return yestodayProductOrderMoney;
        }

        public void setYestodayProductOrderMoney(double yestodayProductOrderMoney) {
            this.yestodayProductOrderMoney = yestodayProductOrderMoney;
        }

        public int getMyArrangeNum() {
            return myArrangeNum;
        }

        public void setMyArrangeNum(int myArrangeNum) {
            this.myArrangeNum = myArrangeNum;
        }

        public double getProductOrderMoney() {
            return ProductOrderMoney;
        }

        public void setProductOrderMoney(double ProductOrderMoney) {
            this.ProductOrderMoney = ProductOrderMoney;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getUnPayProductOrderNum() {
            return unPayProductOrderNum;
        }

        public void setUnPayProductOrderNum(int unPayProductOrderNum) {
            this.unPayProductOrderNum = unPayProductOrderNum;
        }

        public int getUnDeliverProductOrderNum() {
            return unDeliverProductOrderNum;
        }

        public void setUnDeliverProductOrderNum(int unDeliverProductOrderNum) {
            this.unDeliverProductOrderNum = unDeliverProductOrderNum;
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

        public double getTodayProductOrderMoney() {
            return todayProductOrderMoney;
        }

        public void setTodayProductOrderMoney(double todayProductOrderMoney) {
            this.todayProductOrderMoney = todayProductOrderMoney;
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

        public int getCompleteProductOrderNum() {
            return completeProductOrderNum;
        }

        public void setCompleteProductOrderNum(int completeProductOrderNum) {
            this.completeProductOrderNum = completeProductOrderNum;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
