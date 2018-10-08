package com.netease.nim.uikit.merchant;

import java.util.List;

public class GetUserMemoName {

    /**
     * errorString :
     * flag : true
     * data : [{"umnToId":"1","umnName":"hah"}]
     */

    private String errorString;
    private String flag;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * umnToId : 1
         * umnName : hah
         */

        private String umnToId;
        private String umnName;

        public String getUmnToId() {
            return umnToId;
        }

        public void setUmnToId(String umnToId) {
            this.umnToId = umnToId;
        }

        public String getUmnName() {
            return umnName;
        }

        public void setUmnName(String umnName) {
            this.umnName = umnName;
        }
    }
}
