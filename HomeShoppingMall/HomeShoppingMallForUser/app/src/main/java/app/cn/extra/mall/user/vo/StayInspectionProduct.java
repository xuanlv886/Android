package app.cn.extra.mall.user.vo;

import java.util.List;

public class StayInspectionProduct {

    /**
     * errorString :
     * flag : true
     * data : {"roConfirmTime":"2018-04-25 16:49:40","picList":[{"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/requirement/1.jpg"},{"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/requirement/8.jpg"},{"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/requirement/3.jpg"}]}
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
        /**
         * roConfirmTime : 2018-04-25 16:49:40
         * picList : [{"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/requirement/1.jpg"},{"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/requirement/8.jpg"},{"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/requirement/3.jpg"}]
         */

        private String roConfirmTime;
        private List<PicListBean> picList;

        public String getRoConfirmTime() {
            return roConfirmTime;
        }

        public void setRoConfirmTime(String roConfirmTime) {
            this.roConfirmTime = roConfirmTime;
        }

        public List<PicListBean> getPicList() {
            return picList;
        }

        public void setPicList(List<PicListBean> picList) {
            this.picList = picList;
        }

        public static class PicListBean {
            /**
             * url : https://hsmcommon.oss-cn-beijing.aliyuncs.com/requirement/1.jpg
             */

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
