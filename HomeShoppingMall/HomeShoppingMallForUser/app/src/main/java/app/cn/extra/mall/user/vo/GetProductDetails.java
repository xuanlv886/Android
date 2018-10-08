package app.cn.extra.mall.user.vo;

import java.io.Serializable;
import java.util.List;

import app.cn.extra.mall.user.utils.Util;

/**
 * Created by Administrator on 2018/3/8 0008.
 */

public class GetProductDetails implements Serializable {

    /**
     * errorString :
     * flag : true
     * data : {"pDescribe":"红木打造，低调奢华","isCollection":"false","pOriginalPrice":5000,"pNowPrice":4800,"ppropertyNames":["设计元素"],"sId":"168eb9f4-46ac-11e8-8390-4ccc6a70ac67","errorString":"","pStockNum":25,"pName":"红木餐桌","propertysList1":[{"values":[{"id":"8aedffa0-3487-11e7-add7-00163e2e094a","value":"不规则体结构"}],"name":"家具结构"},{"values":[{"id":"76db3370-377a-11e7-add7-00163e2e094a","value":"小熊维尼"}],"name":"图案"},{"values":[{"id":"8aede28e-3487-11e7-add7-00163e2e094a","value":"艺术风格型"}],"name":"款式定位"},{"values":[{"id":"8aedec04-3487-11e7-add7-00163e2e094a","value":"拆装"}],"name":"附加功能"},{"values":[{"id":"3f051ad7-33ec-11e7-add7-00163e2e094a","value":"白色"}],"name":"颜色分类"},{"values":[{"id":"8aeda4f7-3487-11e7-add7-00163e2e094a","value":"卡通风"}],"name":"风格"},{"values":[{"id":"8aed84ef-3487-11e7-add7-00163e2e094a","value":"书房"}],"name":"适用空间"},{"values":[{"id":"8aeddfbd-3487-11e7-add7-00163e2e094a","value":"提供安装说明书"}],"name":"安装说明详情"},{"values":[{"id":"8aedfb7e-3487-11e7-add7-00163e2e094a","value":"否"}],"name":"是否可定制"}],"picList":[{"picNo":0,"picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/1.jpg","picId":"0a4f3783-4e7f-11e8-8390-4ccc6a70ac67"},{"picNo":1,"picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/2.jpeg","picId":"0a51b2b4-4e7f-11e8-8390-4ccc6a70ac67"},{"picNo":2,"picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/3.jpg","picId":"0a51b3f8-4e7f-11e8-8390-4ccc6a70ac67"},{"picNo":3,"picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/4.jpg","picId":"0a51b429-4e7f-11e8-8390-4ccc6a70ac67"},{"picNo":4,"picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/5.jpg","picId":"0a51b457-4e7f-11e8-8390-4ccc6a70ac67"}],"propertysList2":[{"values":[{"id":"8aede79e-3487-11e7-add7-00163e2e094a","value":"罗马柱"},{"id":"8aede466-3487-11e7-add7-00163e2e094a","value":"原木"}],"name":"设计元素"}],"pBrowseNum":504,"html":""}
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

    public static class DataBean implements Serializable{
        /**
         * pDescribe : 红木打造，低调奢华
         * isCollection : false
         * pOriginalPrice : 5000.0
         * pNowPrice : 4800.0
         * ppropertyNames : ["设计元素"]
         * sId : 168eb9f4-46ac-11e8-8390-4ccc6a70ac67
         * errorString :
         * pStockNum : 25
         * pName : 红木餐桌
         * propertysList1 : [{"values":[{"id":"8aedffa0-3487-11e7-add7-00163e2e094a","value":"不规则体结构"}],"name":"家具结构"},{"values":[{"id":"76db3370-377a-11e7-add7-00163e2e094a","value":"小熊维尼"}],"name":"图案"},{"values":[{"id":"8aede28e-3487-11e7-add7-00163e2e094a","value":"艺术风格型"}],"name":"款式定位"},{"values":[{"id":"8aedec04-3487-11e7-add7-00163e2e094a","value":"拆装"}],"name":"附加功能"},{"values":[{"id":"3f051ad7-33ec-11e7-add7-00163e2e094a","value":"白色"}],"name":"颜色分类"},{"values":[{"id":"8aeda4f7-3487-11e7-add7-00163e2e094a","value":"卡通风"}],"name":"风格"},{"values":[{"id":"8aed84ef-3487-11e7-add7-00163e2e094a","value":"书房"}],"name":"适用空间"},{"values":[{"id":"8aeddfbd-3487-11e7-add7-00163e2e094a","value":"提供安装说明书"}],"name":"安装说明详情"},{"values":[{"id":"8aedfb7e-3487-11e7-add7-00163e2e094a","value":"否"}],"name":"是否可定制"}]
         * picList : [{"picNo":0,"picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/1.jpg","picId":"0a4f3783-4e7f-11e8-8390-4ccc6a70ac67"},{"picNo":1,"picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/2.jpeg","picId":"0a51b2b4-4e7f-11e8-8390-4ccc6a70ac67"},{"picNo":2,"picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/3.jpg","picId":"0a51b3f8-4e7f-11e8-8390-4ccc6a70ac67"},{"picNo":3,"picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/4.jpg","picId":"0a51b429-4e7f-11e8-8390-4ccc6a70ac67"},{"picNo":4,"picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/5.jpg","picId":"0a51b457-4e7f-11e8-8390-4ccc6a70ac67"}]
         * propertysList2 : [{"values":[{"id":"8aede79e-3487-11e7-add7-00163e2e094a","value":"罗马柱"},{"id":"8aede466-3487-11e7-add7-00163e2e094a","value":"原木"}],"name":"设计元素"}]
         * pBrowseNum : 504
         * html :
         */

        private String pDescribe;
        private String isCollection;
        private double pOriginalPrice;
        private double pNowPrice;
        private String sId;
        private String errorString;
        private int pStockNum;
        private String pName;
        private int pBrowseNum;
        private String html;
        private List<String> ppropertyNames;
        private List<PropertysList1Bean> propertysList1;
        private List<PicListBean> picList;
        private List<PropertysList2Bean> propertysList2;
        private String accid;
        private String ptdId;

        public String getPtdId() {
            return ptdId;
        }

        public void setPtdId(String ptdId) {
            this.ptdId = ptdId;
        }

        public String getAccid() {
            return accid;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public String getPDescribe() {
            return pDescribe;
        }

        public void setPDescribe(String pDescribe) {
            this.pDescribe = pDescribe;
        }

        public String getIsCollection() {
            return isCollection;
        }

        public void setIsCollection(String isCollection) {
            this.isCollection = isCollection;
        }

        public String getPOriginalPrice() {
            return Util.doubleToTwoDecimal(pOriginalPrice);
        }

        public void setPOriginalPrice(double pOriginalPrice) {
            this.pOriginalPrice = pOriginalPrice;
        }

        public String getPNowPrice() {
            return Util.doubleToTwoDecimal(pNowPrice);
        }

        public void setPNowPrice(double pNowPrice) {
            this.pNowPrice = pNowPrice;
        }

        public String getSId() {
            return sId;
        }

        public void setSId(String sId) {
            this.sId = sId;
        }

        public String getErrorString() {
            return errorString;
        }

        public void setErrorString(String errorString) {
            this.errorString = errorString;
        }

        public int getPStockNum() {
            return pStockNum;
        }

        public void setPStockNum(int pStockNum) {
            this.pStockNum = pStockNum;
        }

        public String getPName() {
            return pName;
        }

        public void setPName(String pName) {
            this.pName = pName;
        }

        public int getPBrowseNum() {
            return pBrowseNum;
        }

        public void setPBrowseNum(int pBrowseNum) {
            this.pBrowseNum = pBrowseNum;
        }

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        public List<String> getPpropertyNames() {
            return ppropertyNames;
        }

        public void setPpropertyNames(List<String> ppropertyNames) {
            this.ppropertyNames = ppropertyNames;
        }

        public List<PropertysList1Bean> getPropertysList1() {
            return propertysList1;
        }

        public void setPropertysList1(List<PropertysList1Bean> propertysList1) {
            this.propertysList1 = propertysList1;
        }

        public List<PicListBean> getPicList() {
            return picList;
        }

        public void setPicList(List<PicListBean> picList) {
            this.picList = picList;
        }

        public List<PropertysList2Bean> getPropertysList2() {
            return propertysList2;
        }

        public void setPropertysList2(List<PropertysList2Bean> propertysList2) {
            this.propertysList2 = propertysList2;
        }

        public static class PropertysList1Bean implements Serializable{
            /**
             * values : [{"id":"8aedffa0-3487-11e7-add7-00163e2e094a","value":"不规则体结构"}]
             * name : 家具结构
             */

            private String name;
            private List<ValuesBean> values;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<ValuesBean> getValues() {
                return values;
            }

            public void setValues(List<ValuesBean> values) {
                this.values = values;
            }

            public static class ValuesBean implements Serializable{
                /**
                 * id : 8aedffa0-3487-11e7-add7-00163e2e094a
                 * value : 不规则体结构
                 */

                private String id;
                private String value;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }
        }

        public static class PicListBean implements Serializable{
            /**
             * picNo : 0
             * picName : https://hsmcommon.oss-cn-beijing.aliyuncs.com/product/1.jpg
             * picId : 0a4f3783-4e7f-11e8-8390-4ccc6a70ac67
             */

            private int picNo;
            private String picName;
            private String picId;

            public int getPicNo() {
                return picNo;
            }

            public void setPicNo(int picNo) {
                this.picNo = picNo;
            }

            public String getPicName() {
                return picName;
            }

            public void setPicName(String picName) {
                this.picName = picName;
            }

            public String getPicId() {
                return picId;
            }

            public void setPicId(String picId) {
                this.picId = picId;
            }
        }

        public static class PropertysList2Bean implements Serializable{
            /**
             * values : [{"id":"8aede79e-3487-11e7-add7-00163e2e094a","value":"罗马柱"},{"id":"8aede466-3487-11e7-add7-00163e2e094a","value":"原木"}]
             * name : 设计元素
             */

            private String name;
            private List<ValuesBeanX> values;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<ValuesBeanX> getValues() {
                return values;
            }

            public void setValues(List<ValuesBeanX> values) {
                this.values = values;
            }

            public static class ValuesBeanX implements Serializable{
                /**
                 * id : 8aede79e-3487-11e7-add7-00163e2e094a
                 * value : 罗马柱
                 */

                private String id;
                private String value;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }
        }
    }
}
