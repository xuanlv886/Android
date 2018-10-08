package app.cn.extra.mall.user.vo;

import java.util.List;

public class GetHotSearch {

    /**
     * errorString :
     * flag : true
     * data : {"hotSearchList":[{"psName":"红木","psType":0,"psId":"ae1fe455-8985-11e8-87a7-4ccc6a3b62ec","psNum":16},{"psName":"学生桌","psType":0,"psId":"bd6a8613-8985-11e8-87a7-4ccc6a3b62ec","psNum":11},{"psName":"气垫床","psType":0,"psId":"b6aab31f-8985-11e8-87a7-4ccc6a3b62ec","psNum":10},{"psName":"老板椅","psType":0,"psId":"c6aaea6f-8985-11e8-87a7-4ccc6a3b62ec","psNum":10},{"psName":"电脑桌","psType":0,"psId":"d46cd280-8985-11e8-87a7-4ccc6a3b62ec","psNum":10}]}
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
        private List<HotSearchListBean> hotSearchList;

        public List<HotSearchListBean> getHotSearchList() {
            return hotSearchList;
        }

        public void setHotSearchList(List<HotSearchListBean> hotSearchList) {
            this.hotSearchList = hotSearchList;
        }

        public static class HotSearchListBean {
            /**
             * psName : 红木
             * psType : 0
             * psId : ae1fe455-8985-11e8-87a7-4ccc6a3b62ec
             * psNum : 16
             */

            private String psName;
            private int psType;
            private String psId;
            private int psNum;

            public String getPsName() {
                return psName;
            }

            public void setPsName(String psName) {
                this.psName = psName;
            }

            public int getPsType() {
                return psType;
            }

            public void setPsType(int psType) {
                this.psType = psType;
            }

            public String getPsId() {
                return psId;
            }

            public void setPsId(String psId) {
                this.psId = psId;
            }

            public int getPsNum() {
                return psNum;
            }

            public void setPsNum(int psNum) {
                this.psNum = psNum;
            }
        }
    }
}
