package app.cn.extra.mall.user.vo;

/**
 * Created by Administrator on 2017/9/22 0022.
 */

public class AliPaybean {

    /**
     * alipay_trade_app_pay_response : {"code":"10000","msg":"Success","app_id":"2018010801686239","auth_app_id":"2018010801686239","charset":"utf-8","timestamp":"2018-03-29 11:37:07","total_amount":"0.01","trade_no":"2018032921001004840560596930","seller_id":"2088621248819263","out_trade_no":"032911362416530"}
     * sign : CFYDlrtv4EQ4+NVzRURknif3XyOPiPMFGPHqMRGuvGqtdQPJ1mBeFd+4YyS6IMeXUZ9CACAATNLT33Fln+0nsmxrV42TQdKmxggB6+zvJjy1Zyj006WD1L4UtuVE2fTk1rYAKy+5VwnYjp0zeoWUM8MpEcOROyOT/yYpmDIRBmmVNWz8OOrtYgwDIJ9J9B8Un6kCq0A4IlIekeiEkrzLMw3fWdqcC0VcYhnOQi0d5Jwq1Auanxtth8hihuM4g05JdzsR/+AGUWRBCfjnR7MjP0C3Ry/YirZlW3dFInms+N7CiE8GLYEV5WP/EyLHCvFYpdNSZrb6/xnRb7DGClK6Hw==
     * sign_type : RSA2
     */

    private AlipayTradeAppPayResponseBean alipay_trade_app_pay_response;
    private String sign;
    private String sign_type;

    public AlipayTradeAppPayResponseBean getAlipay_trade_app_pay_response() {
        return alipay_trade_app_pay_response;
    }

    public void setAlipay_trade_app_pay_response(AlipayTradeAppPayResponseBean alipay_trade_app_pay_response) {
        this.alipay_trade_app_pay_response = alipay_trade_app_pay_response;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_type() {
        return sign_type;
    }

    public void setSign_type(String sign_type) {
        this.sign_type = sign_type;
    }

    public static class AlipayTradeAppPayResponseBean {
        /**
         * code : 10000
         * msg : Success
         * app_id : 2018010801686239
         * auth_app_id : 2018010801686239
         * charset : utf-8
         * timestamp : 2018-03-29 11:37:07
         * total_amount : 0.01
         * trade_no : 2018032921001004840560596930
         * seller_id : 2088621248819263
         * out_trade_no : 032911362416530
         */

        private String code;
        private String msg;
        private String app_id;
        private String auth_app_id;
        private String charset;
        private String timestamp;
        private String total_amount;
        private String trade_no;
        private String seller_id;
        private String out_trade_no;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getApp_id() {
            return app_id;
        }

        public void setApp_id(String app_id) {
            this.app_id = app_id;
        }

        public String getAuth_app_id() {
            return auth_app_id;
        }

        public void setAuth_app_id(String auth_app_id) {
            this.auth_app_id = auth_app_id;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getTrade_no() {
            return trade_no;
        }

        public void setTrade_no(String trade_no) {
            this.trade_no = trade_no;
        }

        public String getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(String seller_id) {
            this.seller_id = seller_id;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }
    }
}
