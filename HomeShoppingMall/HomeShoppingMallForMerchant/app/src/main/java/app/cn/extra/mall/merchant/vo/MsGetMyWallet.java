package app.cn.extra.mall.merchant.vo;

/**
 * Description
 * Data 2018/7/28-9:59
 * Content
 *
 * @author L
 */
public class MsGetMyWallet {

    /**
     * errorString :
     * flag : true
     * data : {"errorString":"","uwAlreadyToCash":0,"uwApplyToFreeDepositMoney":0,"uwApplyToCash":0,"uwLeftMoney":0,"uwDeposit":0,"status":"true"}
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
         * errorString :
         * uwAlreadyToCash : 0.0
         * uwApplyToFreeDepositMoney : 0.0
         * uwApplyToCash : 0.0
         * uwLeftMoney : 0.0
         * uwDeposit : 0.0
         * status : true
         * sProductServiceCharge
         * sRequirementServiceCharge
         */

        private String errorString;
        private double uwAlreadyToCash;
        private double uwApplyToFreeDepositMoney;
        private double uwApplyToCash;
        private double uwLeftMoney;
        private double uwDeposit;
        private String status;
        private String sProductServiceCharge;
        private String sRequirementServiceCharge;

        public String getsProductServiceCharge() {
            return sProductServiceCharge;
        }

        public void setsProductServiceCharge(String sProductServiceCharge) {
            this.sProductServiceCharge = sProductServiceCharge;
        }

        public String getsRequirementServiceCharge() {
            return sRequirementServiceCharge;
        }

        public void setsRequirementServiceCharge(String sRequirementServiceCharge) {
            this.sRequirementServiceCharge = sRequirementServiceCharge;
        }

        public String getErrorString() {
            return errorString;
        }

        public void setErrorString(String errorString) {
            this.errorString = errorString;
        }

        public double getUwAlreadyToCash() {
            return uwAlreadyToCash;
        }

        public void setUwAlreadyToCash(double uwAlreadyToCash) {
            this.uwAlreadyToCash = uwAlreadyToCash;
        }

        public double getUwApplyToFreeDepositMoney() {
            return uwApplyToFreeDepositMoney;
        }

        public void setUwApplyToFreeDepositMoney(double uwApplyToFreeDepositMoney) {
            this.uwApplyToFreeDepositMoney = uwApplyToFreeDepositMoney;
        }

        public double getUwApplyToCash() {
            return uwApplyToCash;
        }

        public void setUwApplyToCash(double uwApplyToCash) {
            this.uwApplyToCash = uwApplyToCash;
        }

        public double getUwLeftMoney() {
            return uwLeftMoney;
        }

        public void setUwLeftMoney(double uwLeftMoney) {
            this.uwLeftMoney = uwLeftMoney;
        }

        public double getUwDeposit() {
            return uwDeposit;
        }

        public void setUwDeposit(double uwDeposit) {
            this.uwDeposit = uwDeposit;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
