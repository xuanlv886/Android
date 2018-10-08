package app.cn.extra.mall.user.vo;

import java.util.List;

public class GetCityList {

    /**
     * errorString :
     * flag : true
     * data : {"openCityList":[{"acId":"37","ocIsHot":1,"acName":null,"acCity":"石家庄市","acParent":"36","acCode":"130100","ocCreateTime":"2018-05-09 17:25:30","acProvince":"","ocId":"98393a3d-9eaa-4509-9cfd-082a273bb12b"}]}
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
            private String acName;
            private String acCity;
            private String acParent;
            private String acCode;
            private String ocCreateTime;
            private String acProvince;
            private String ocId;
            /**
             * 城市拼音首字母（自己添加的，后台并没有返回，出现问题时要注意）
             */
            private String acSortLetters;

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

            public String getAcName() {
                return acName;
            }

            public void setAcName(String acName) {
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

            public String getAcSortLetters() {
                return acSortLetters;
            }

            public void setAcSortLetters(String acSortLetters) {
                this.acSortLetters = acSortLetters;
            }
        }
    }
}
