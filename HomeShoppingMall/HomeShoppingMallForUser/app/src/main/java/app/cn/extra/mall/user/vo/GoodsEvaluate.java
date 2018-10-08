package app.cn.extra.mall.user.vo;

import java.util.List;

public class GoodsEvaluate {

    private List<EvaluateDataBean> evaluateData;

    public List<EvaluateDataBean> getEvaluateData() {
        return evaluateData;
    }

    public void setEvaluateData(List<EvaluateDataBean> evaluateData) {
        this.evaluateData = evaluateData;
    }

    public static class EvaluateDataBean {
        /**
         * pId : 779762e2-07e2-11e8-9d14-4ccc6a3b62ec
         * podId : 779762e2-07e2-11e8-9d14-4ccc6a3b62ec
         * peLevel : 0
         * peContent : 好好好
         * picList : [{"pNo":"0","pName":"54545.jpg"},{"pNo":"1","pName":"5455.png"}]
         */

        private String pId;
        private String podId;
        private String peLevel;
        private String peContent;
        private List<PicListBean> picList;

        public String getPId() {
            return pId;
        }

        public void setPId(String pId) {
            this.pId = pId;
        }

        public String getPodId() {
            return podId;
        }

        public void setPodId(String podId) {
            this.podId = podId;
        }

        public String getPeLevel() {
            return peLevel;
        }

        public void setPeLevel(String peLevel) {
            this.peLevel = peLevel;
        }

        public String getPeContent() {
            return peContent;
        }

        public void setPeContent(String peContent) {
            this.peContent = peContent;
        }

        public List<PicListBean> getPicList() {
            return picList;
        }

        public void setPicList(List<PicListBean> picList) {
            this.picList = picList;
        }

        public static class PicListBean {
            /**
             * pNo : 0
             * pName : 54545.jpg
             */

            private String pNo;
            private String pName;

            public String getPNo() {
                return pNo;
            }

            public void setPNo(String pNo) {
                this.pNo = pNo;
            }

            public String getPName() {
                return pName;
            }

            public void setPName(String pName) {
                this.pName = pName;
            }
        }
    }
}
