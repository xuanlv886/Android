package app.cn.extra.mall.merchant.vo;

import java.util.List;

/**
 * Description
 * Data 2018/7/7-18:44
 * Content
 *
 * @author L
 */
public class GetCityList {

    /**
     * errorString :
     * flag : true
     * data : {"openCityList":[{"acId":"37","ocIsHot":1,"acName":null,"acCity":"石家庄市","acParent":"36","acCode":"130100","ocCreateTime":"2018-05-09 17:25:30","acProvince":"","ocId":"98393a3d-9eaa-4509-9cfd-082a273bb12b"},{"acId":"2459","ocIsHot":1,"acName":null,"acCity":"重庆市","acParent":"1","acCode":"500000","ocCreateTime":"2018-06-11 13:42:06","acProvince":"重庆市","ocId":"aeac135e-aa12-4af4-934c-2ad3b39115b7"},{"acId":"2","ocIsHot":1,"acName":null,"acCity":"北京市","acParent":"1","acCode":"110000","ocCreateTime":"2018-06-16 14:17:29","acProvince":"北京市","ocId":"c1ce6616-1714-4bbf-8f60-25389d7d2ce2"},{"acId":"217","ocIsHot":0,"acName":null,"acCity":"衡水市","acParent":"36","acCode":"131100","ocCreateTime":"2018-06-15 10:31:24","acProvince":"","ocId":"8278b85a-4772-4452-bc4c-c3c92717e68b"},{"acId":"19","ocIsHot":0,"acName":null,"acCity":"天津市","acParent":"1","acCode":"120000","ocCreateTime":"2018-06-08 15:58:26","acProvince":"天津市","ocId":"a05222aa-7135-4c64-a32c-a4107f8c6b46"}]}
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
        private List<OpenCityListBean> openCityList;

        public List<OpenCityListBean> getOpenCityList() {
            return openCityList;
        }

        public void setOpenCityList(List<OpenCityListBean> openCityList) {
            this.openCityList = openCityList;
        }

        public static class OpenCityListBean {
            /**
             * acId : 37
             * ocIsHot : 1
             * acName : null
             * acCity : 石家庄市
             * acParent : 36
             * acCode : 130100
             * ocCreateTime : 2018-05-09 17:25:30
             * acProvince :
             * ocId : 98393a3d-9eaa-4509-9cfd-082a273bb12b
             */

            private String acId;
            private int ocIsHot;
            private Object acName;
            private String acCity;
            private String acParent;
            private String acCode;
            private String ocCreateTime;
            private String acProvince;
            private String ocId;

            public String getAcId() {
                return acId;
            }

            public void setAcId(String acId) {
                this.acId = acId;
            }

            public int getOcIsHot() {
                return ocIsHot;
            }

            public void setOcIsHot(int ocIsHot) {
                this.ocIsHot = ocIsHot;
            }

            public Object getAcName() {
                return acName;
            }

            public void setAcName(Object acName) {
                this.acName = acName;
            }

            public String getAcCity() {
                return acCity;
            }

            public void setAcCity(String acCity) {
                this.acCity = acCity;
            }

            public String getAcParent() {
                return acParent;
            }

            public void setAcParent(String acParent) {
                this.acParent = acParent;
            }

            public String getAcCode() {
                return acCode;
            }

            public void setAcCode(String acCode) {
                this.acCode = acCode;
            }

            public String getOcCreateTime() {
                return ocCreateTime;
            }

            public void setOcCreateTime(String ocCreateTime) {
                this.ocCreateTime = ocCreateTime;
            }

            public String getAcProvince() {
                return acProvince;
            }

            public void setAcProvince(String acProvince) {
                this.acProvince = acProvince;
            }

            public String getOcId() {
                return ocId;
            }

            public void setOcId(String ocId) {
                this.ocId = ocId;
            }
        }
    }
}
