package app.cn.extra.mall.user.vo;

/**
 * Created by Administrator on 2018/3/22 0022.
 */

public class GetCAPTCHA {

    /**
     * errorString :
     * flag : true
     * data : {"CAPTCHA":"249517"}
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
         * CAPTCHA : 249517
         */

        private String CAPTCHA;

        public String getCAPTCHA() {
            return CAPTCHA;
        }

        public void setCAPTCHA(String CAPTCHA) {
            this.CAPTCHA = CAPTCHA;
        }
    }
}
