package app.cn.extra.mall.merchant.vo;

/**
 * Description
 * Data 2018/7/7-18:10
 * Content
 *
 * @author L
 */
public class GetCaptcha {


    /**
     * errorString :
     * flag : true
     * data : {"CAPTCHA":"702371"}
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
         * CAPTCHA : 702371
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
