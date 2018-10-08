package app.cn.extra.mall.merchant.vo;

import java.util.List;

/**
 * Created by Administrator on 2018/3/8 0008.
 */

public class GetProductEvaluate {

    /**
     * errorString :
     * flag : true
     * data : {"levelNum":5,"productEvaluateList":[{"uId":"7d604f38-5ba6-4445-bc30-5b80a29b8541","peContent":"弹尽粮绝","peId":"1c5d8ce0-a9cf-11e8-be34-4ccc6a3b62ec","picFileName":"profilephoto/","picList":[{"picNo":3,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/evaluate/null"},{"picNo":3,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/evaluate/null"},{"picNo":3,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/evaluate/null"},{"picNo":3,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/evaluate/null"}],"pId":"f7bd6a33-c04a-458c-a8cd-818d6403b36b","uNickName":"U401741","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/profilephoto/15360517170452855.jpg","podId":"f1ee7022-a9ce-11e8-be34-4ccc6a3b62ec","peCreateTime":"2018-08-27 15:59:23"}]}
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
         * levelNum : 5
         * productEvaluateList : [{"uId":"7d604f38-5ba6-4445-bc30-5b80a29b8541","peContent":"弹尽粮绝","peId":"1c5d8ce0-a9cf-11e8-be34-4ccc6a3b62ec","picFileName":"profilephoto/","picList":[{"picNo":3,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/evaluate/null"},{"picNo":3,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/evaluate/null"},{"picNo":3,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/evaluate/null"},{"picNo":3,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/evaluate/null"}],"pId":"f7bd6a33-c04a-458c-a8cd-818d6403b36b","uNickName":"U401741","picName":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/profilephoto/15360517170452855.jpg","podId":"f1ee7022-a9ce-11e8-be34-4ccc6a3b62ec","peCreateTime":"2018-08-27 15:59:23"}]
         */

        private int levelNum;
        private List<ProductEvaluateListBean> productEvaluateList;

        public int getLevelNum() {
            return levelNum;
        }

        public void setLevelNum(int levelNum) {
            this.levelNum = levelNum;
        }

        public List<ProductEvaluateListBean> getProductEvaluateList() {
            return productEvaluateList;
        }

        public void setProductEvaluateList(List<ProductEvaluateListBean> productEvaluateList) {
            this.productEvaluateList = productEvaluateList;
        }

        public static class ProductEvaluateListBean {
            /**
             * uId : 7d604f38-5ba6-4445-bc30-5b80a29b8541
             * peContent : 弹尽粮绝
             * peId : 1c5d8ce0-a9cf-11e8-be34-4ccc6a3b62ec
             * picFileName : profilephoto/
             * picList : [{"picNo":3,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/evaluate/null"},{"picNo":3,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/evaluate/null"},{"picNo":3,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/evaluate/null"},{"picNo":3,"url":"https://hsmcommon.oss-cn-beijing.aliyuncs.com/evaluate/null"}]
             * pId : f7bd6a33-c04a-458c-a8cd-818d6403b36b
             * uNickName : U401741
             * picName : https://hsmcommon.oss-cn-beijing.aliyuncs.com/profilephoto/15360517170452855.jpg
             * podId : f1ee7022-a9ce-11e8-be34-4ccc6a3b62ec
             * peCreateTime : 2018-08-27 15:59:23
             */

            private String uId;
            private String peContent;
            private String peId;
            private String picFileName;
            private String pId;
            private String uNickName;
            private String picName;
            private String podId;
            private String peCreateTime;
            private List<PicListBean> picList;

            public String getUId() {
                return uId;
            }

            public void setUId(String uId) {
                this.uId = uId;
            }

            public String getPeContent() {
                return peContent;
            }

            public void setPeContent(String peContent) {
                this.peContent = peContent;
            }

            public String getPeId() {
                return peId;
            }

            public void setPeId(String peId) {
                this.peId = peId;
            }

            public String getPicFileName() {
                return picFileName;
            }

            public void setPicFileName(String picFileName) {
                this.picFileName = picFileName;
            }

            public String getPId() {
                return pId;
            }

            public void setPId(String pId) {
                this.pId = pId;
            }

            public String getUNickName() {
                return uNickName;
            }

            public void setUNickName(String uNickName) {
                this.uNickName = uNickName;
            }

            public String getPicName() {
                return picName;
            }

            public void setPicName(String picName) {
                this.picName = picName;
            }

            public String getPodId() {
                return podId;
            }

            public void setPodId(String podId) {
                this.podId = podId;
            }

            public String getPeCreateTime() {
                return peCreateTime;
            }

            public void setPeCreateTime(String peCreateTime) {
                this.peCreateTime = peCreateTime;
            }

            public List<PicListBean> getPicList() {
                return picList;
            }

            public void setPicList(List<PicListBean> picList) {
                this.picList = picList;
            }

            public static class PicListBean {
                /**
                 * picNo : 3
                 * url : https://hsmcommon.oss-cn-beijing.aliyuncs.com/evaluate/null
                 */

                private int picNo;
                private String url;

                public int getPicNo() {
                    return picNo;
                }

                public void setPicNo(int picNo) {
                    this.picNo = picNo;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }
    }
}
