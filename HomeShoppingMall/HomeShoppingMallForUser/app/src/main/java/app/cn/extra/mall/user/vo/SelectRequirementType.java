package app.cn.extra.mall.user.vo;

import java.util.List;

public class SelectRequirementType {

    /**
     * errorString :
     * flag : true
     * data : {"requirementTypeList":[{"rtId":"59bf595a-43ae-11e8-8390-4ccc6a70ac67","rtName":"房屋装修"},{"rtId":"8a7ca80e-43aa-11e8-8390-4ccc6a70ac67","rtName":"家具安装"},{"rtId":"8a7caa94-43aa-11e8-8390-4ccc6a70ac67","rtName":"取货送货"}]}
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
        private List<RequirementTypeListBean> requirementTypeList;

        public List<RequirementTypeListBean> getRequirementTypeList() {
            return requirementTypeList;
        }

        public void setRequirementTypeList(List<RequirementTypeListBean> requirementTypeList) {
            this.requirementTypeList = requirementTypeList;
        }

        public static class RequirementTypeListBean {
            /**
             * rtId : 59bf595a-43ae-11e8-8390-4ccc6a70ac67
             * rtName : 房屋装修
             */

            private String rtId;
            private String rtName;

            public String getRtId() {
                return rtId;
            }

            public void setRtId(String rtId) {
                this.rtId = rtId;
            }

            public String getRtName() {
                return rtName;
            }

            public void setRtName(String rtName) {
                this.rtName = rtName;
            }
        }
    }
}
