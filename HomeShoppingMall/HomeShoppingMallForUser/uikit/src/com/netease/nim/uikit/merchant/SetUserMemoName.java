package com.netease.nim.uikit.merchant;

public class SetUserMemoName {

    /**
     * errorString :
     * flag : true
     * data : {"status":"true","errorString":""}
     */

    private String errorString;
    private String flag;
    private DataBean data;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * status : true
         * errorString :
         */

        private String status;
        private String errorString;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getErrorString() {
            return errorString;
        }

        public void setErrorString(String errorString) {
            this.errorString = errorString;
        }
    }
}
