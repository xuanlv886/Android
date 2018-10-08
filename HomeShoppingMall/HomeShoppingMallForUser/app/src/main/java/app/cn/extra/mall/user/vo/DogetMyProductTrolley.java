package app.cn.extra.mall.user.vo;

import java.util.List;

import app.cn.extra.mall.user.utils.Util;

/**
 * Created by Administrator on 2018/2/11 0011.
 */

public class DogetMyProductTrolley {

    /**
     * errorString :
     * flag : true
     * data : {"userTrolleyList":[{"pName":"红木床","utCreateTime":"2018-07-19 09:48:33","sName":"企业店铺1","utProductNum":1,"pId":"7dde1fe6-4867-11e8-8390-4ccc6a70ac67","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/2.jpeg","utId":"d8830e00-8af5-11e8-9ebb-4ccc6a3b62ec","pOriginalPrice":8000,"utProductProperty":"家具结构:箱框结构;颜色分类:玫红色;设计元素:原木;安装说明详情:提供简单安装工具;","pNowPrice":7800},{"pName":"红木餐桌","utCreateTime":"2018-07-19 15:34:25","sName":"企业店铺1","utProductNum":1,"pId":"3aa5554b-4867-11e8-8390-4ccc6a70ac67","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/1.jpg","utId":"29503fb2-8b26-11e8-9ebb-4ccc6a3b62ec","pOriginalPrice":5000,"utProductProperty":"设计元素:原木;","pNowPrice":4800},{"pName":"红木餐桌","utCreateTime":"2018-07-19 10:17:33","sName":"企业店铺1","utProductNum":2,"pId":"3aa5554b-4867-11e8-8390-4ccc6a70ac67","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/1.jpg","utId":"e5240324-8af9-11e8-9ebb-4ccc6a3b62ec","pOriginalPrice":5000,"utProductProperty":"设计元素:罗马柱;","pNowPrice":4800}]}
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
        private List<UserTrolleyListBean> userTrolleyList;

        public List<UserTrolleyListBean> getUserTrolleyList() {
            return userTrolleyList;
        }

        public void setUserTrolleyList(List<UserTrolleyListBean> userTrolleyList) {
            this.userTrolleyList = userTrolleyList;
        }

        public static class UserTrolleyListBean {
            /**
             * pName : 红木床
             * utCreateTime : 2018-07-19 09:48:33
             * sName : 企业店铺1
             * utProductNum : 1
             * pId : 7dde1fe6-4867-11e8-8390-4ccc6a70ac67
             * picName : https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/2.jpeg
             * utId : d8830e00-8af5-11e8-9ebb-4ccc6a3b62ec
             * pOriginalPrice : 8000.0
             * utProductProperty : 家具结构:箱框结构;颜色分类:玫红色;设计元素:原木;安装说明详情:提供简单安装工具;
             * pNowPrice : 7800.0
             */

            private String pName;
            private String utCreateTime;
            private String sName;
            private int utProductNum;
            private String pId;
            private String picName;
            private String utId;
            private double pOriginalPrice;
            private String utProductProperty;
            private double pNowPrice;
            private String sId;

            public String getsId() {
                return sId;
            }

            public void setsId(String sId) {
                this.sId = sId;
            }

            public String getPName() {
                return pName;
            }

            public void setPName(String pName) {
                this.pName = pName;
            }

            public String getUtCreateTime() {
                return utCreateTime;
            }

            public void setUtCreateTime(String utCreateTime) {
                this.utCreateTime = utCreateTime;
            }

            public String getSName() {
                return sName;
            }

            public void setSName(String sName) {
                this.sName = sName;
            }

            public int getUtProductNum() {
                return utProductNum;
            }

            public void setUtProductNum(int utProductNum) {
                this.utProductNum = utProductNum;
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

            public String getUtId() {
                return utId;
            }

            public void setUtId(String utId) {
                this.utId = utId;
            }

            public String getPOriginalPrice() {
                return Util.doubleToTwoDecimal(pOriginalPrice);
            }

            public void setPOriginalPrice(double pOriginalPrice) {
                this.pOriginalPrice = pOriginalPrice;
            }

            public String getUtProductProperty() {
                return utProductProperty;
            }

            public void setUtProductProperty(String utProductProperty) {
                this.utProductProperty = utProductProperty;
            }

            public String getPNowPrice() {
                return Util.doubleToTwoDecimal(pNowPrice);
            }

            public void setPNowPrice(double pNowPrice) {
                this.pNowPrice = pNowPrice;
            }
        }
    }
}
