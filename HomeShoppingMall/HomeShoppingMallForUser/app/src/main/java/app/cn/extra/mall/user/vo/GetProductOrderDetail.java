package app.cn.extra.mall.user.vo;

import java.io.Serializable;
import java.util.List;

import app.cn.extra.mall.user.utils.Util;

public class GetProductOrderDetail implements Serializable {

    /**
     * errorString :
     * flag : true
     * data : {"poDeliverTime":"","poDeliverName":"小河","poPayCode":"20180818140135690545","poDeliverTel":"18181163315","psId":"f1f2215c-43a9-11e8-8390-4ccc6a70ac67","poPayTime":"","poId":"15b3547b-c6e6-43d2-ac6c-5f920fe42924","sId":"0e3817b1-154c-4595-9f4b-aac8eb2c2842","poTotalPrice":0.01,"poDeliverAddress":"裕华路22号","uId":"749f7879-afc1-43ef-913c-5783d6e21d99","poOverTime":"","poDel":1,"orderDetails":[{"pDescribe":"餐巾啊啊啊啊","podProperty":"颜色分类:灰色;","pName":"餐巾","podNum":1,"pId":"ef3b4321-2ed5-4995-9ace-930d10b9b6e8","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180613165254136.jpg","podPrice":0.01,"podId":"2b959d29-a2ac-11e8-b7d3-4ccc6a3b62ec","podEvaluate":0,"picId":"8659effc-8000-11e8-ae27-4ccc6a70ac67"}],"poSendTime":"","poCreateTime":"2018-08-18 14:01:39","poDeliverCompany":"","poStatus":0,"poDeliverCode":""}
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

    public static class DataBean implements Serializable {
        /**
         * poDeliverTime :
         * poDeliverName : 小河
         * poPayCode : 20180818140135690545
         * poOrderId:20180818140135690545
         * poDeliverTel : 18181163315
         * psId : f1f2215c-43a9-11e8-8390-4ccc6a70ac67
         * poPayTime :
         * poId : 15b3547b-c6e6-43d2-ac6c-5f920fe42924
         * sId : 0e3817b1-154c-4595-9f4b-aac8eb2c2842
         * poTotalPrice : 0.01
         * poDeliverAddress : 裕华路22号
         * uId : 749f7879-afc1-43ef-913c-5783d6e21d99
         * poOverTime :
         * poDel : 1
         * orderDetails : [{"pDescribe":"餐巾啊啊啊啊","podProperty":"颜色分类:灰色;","pName":"餐巾","podNum":1,"pId":"ef3b4321-2ed5-4995-9ace-930d10b9b6e8","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180613165254136.jpg","podPrice":0.01,"podId":"2b959d29-a2ac-11e8-b7d3-4ccc6a3b62ec","podEvaluate":0,"picId":"8659effc-8000-11e8-ae27-4ccc6a70ac67"}]
         * poSendTime :
         * poCreateTime : 2018-08-18 14:01:39
         * poDeliverCompany :
         * poStatus : 0
         * poDeliverCode :
         */

        private String poDeliverTime;
        private String poDeliverName;
        private String poPayCode;
        private String poOrderId;
        private String poDeliverTel;
        private String psId;
        private String poPayTime;
        private String poId;
        private String sId;
        private double poTotalPrice;
        private String poDeliverAddress;
        private String uId;
        private String poOverTime;
        private int poDel;
        private String poSendTime;
        private String poCreateTime;
        private String poDeliverCompany;
        private int poStatus;
        private String poDeliverCode;
        private String accid;
        private List<OrderDetailsBean> orderDetails;

        public String getPoOrderId() {
            return poOrderId;
        }

        public void setPoOrderId(String poOrderId) {
            this.poOrderId = poOrderId;
        }

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getPoDeliverTime() {
            return poDeliverTime;
        }

        public void setPoDeliverTime(String poDeliverTime) {
            this.poDeliverTime = poDeliverTime;
        }

        public String getPoDeliverName() {
            return poDeliverName;
        }

        public void setPoDeliverName(String poDeliverName) {
            this.poDeliverName = poDeliverName;
        }

        public String getPoPayCode() {
            return poPayCode;
        }

        public void setPoPayCode(String poPayCode) {
            this.poPayCode = poPayCode;
        }

        public String getPoDeliverTel() {
            return poDeliverTel;
        }

        public void setPoDeliverTel(String poDeliverTel) {
            this.poDeliverTel = poDeliverTel;
        }

        public String getPsId() {
            return psId;
        }

        public void setPsId(String psId) {
            this.psId = psId;
        }

        public String getPoPayTime() {
            return poPayTime;
        }

        public void setPoPayTime(String poPayTime) {
            this.poPayTime = poPayTime;
        }

        public String getPoId() {
            return poId;
        }

        public void setPoId(String poId) {
            this.poId = poId;
        }

        public String getSId() {
            return sId;
        }

        public void setSId(String sId) {
            this.sId = sId;
        }

        public String getPoTotalPrice() {
            return Util.doubleToTwoDecimal(poTotalPrice);
        }

        public void setPoTotalPrice(double poTotalPrice) {
            this.poTotalPrice = poTotalPrice;
        }

        public String getPoDeliverAddress() {
            return poDeliverAddress;
        }

        public void setPoDeliverAddress(String poDeliverAddress) {
            this.poDeliverAddress = poDeliverAddress;
        }

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }

        public String getPoOverTime() {
            return poOverTime;
        }

        public void setPoOverTime(String poOverTime) {
            this.poOverTime = poOverTime;
        }

        public int getPoDel() {
            return poDel;
        }

        public void setPoDel(int poDel) {
            this.poDel = poDel;
        }

        public String getPoSendTime() {
            return poSendTime;
        }

        public void setPoSendTime(String poSendTime) {
            this.poSendTime = poSendTime;
        }

        public String getPoCreateTime() {
            return poCreateTime;
        }

        public void setPoCreateTime(String poCreateTime) {
            this.poCreateTime = poCreateTime;
        }

        public String getPoDeliverCompany() {
            return poDeliverCompany;
        }

        public void setPoDeliverCompany(String poDeliverCompany) {
            this.poDeliverCompany = poDeliverCompany;
        }

        public int getPoStatus() {
            return poStatus;
        }

        public void setPoStatus(int poStatus) {
            this.poStatus = poStatus;
        }

        public String getPoDeliverCode() {
            return poDeliverCode;
        }

        public void setPoDeliverCode(String poDeliverCode) {
            this.poDeliverCode = poDeliverCode;
        }

        public List<OrderDetailsBean> getOrderDetails() {
            return orderDetails;
        }

        public void setOrderDetails(List<OrderDetailsBean> orderDetails) {
            this.orderDetails = orderDetails;
        }

        public static class OrderDetailsBean implements Serializable {
            /**
             * pDescribe : 餐巾啊啊啊啊
             * podProperty : 颜色分类:灰色;
             * pName : 餐巾
             * podNum : 1
             * pId : ef3b4321-2ed5-4995-9ace-930d10b9b6e8
             * picName : https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/20180613165254136.jpg
             * podPrice : 0.01
             * podId : 2b959d29-a2ac-11e8-b7d3-4ccc6a3b62ec
             * podEvaluate : 0
             * picId : 8659effc-8000-11e8-ae27-4ccc6a70ac67
             */

            private String pDescribe;
            private String podProperty;
            private String pName;
            private int podNum;
            private String pId;
            private String picName;
            private double podPrice;
            private String podId;
            private int podEvaluate;
            private String picId;

            public String getPDescribe() {
                return pDescribe;
            }

            public void setPDescribe(String pDescribe) {
                this.pDescribe = pDescribe;
            }

            public String getPodProperty() {
                return podProperty;
            }

            public void setPodProperty(String podProperty) {
                this.podProperty = podProperty;
            }

            public String getPName() {
                return pName;
            }

            public void setPName(String pName) {
                this.pName = pName;
            }

            public int getPodNum() {
                return podNum;
            }

            public void setPodNum(int podNum) {
                this.podNum = podNum;
            }

            public String getPId() {
                return pId;
            }

            public void setPId(String pId) {
                this.pId = pId;
            }

            public String getPicName() {
                return picName;
            }

            public void setPicName(String picName) {
                this.picName = picName;
            }

            public String getPodPrice() {
                return Util.doubleToTwoDecimal(podPrice);
            }

            public void setPodPrice(double podPrice) {
                this.podPrice = podPrice;
            }

            public String getPodId() {
                return podId;
            }

            public void setPodId(String podId) {
                this.podId = podId;
            }

            public int getPodEvaluate() {
                return podEvaluate;
            }

            public void setPodEvaluate(int podEvaluate) {
                this.podEvaluate = podEvaluate;
            }

            public String getPicId() {
                return picId;
            }

            public void setPicId(String picId) {
                this.picId = picId;
            }
        }
    }
}
