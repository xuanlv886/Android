package app.cn.extra.mall.user.vo;

import java.util.List;

public class GetStoreSlide {

    /**
     * errorString :
     * flag : true
     * data : {"picList":[{"pNo":0,"pJump":"","pName":"1.jpg","pId":"6da1b4c4-4905-11e8-8390-4ccc6a70ac67"},{"pNo":1,"pJump":"","pName":"2.jpg","pId":"6da8d096-4905-11e8-8390-4ccc6a70ac67"},{"pNo":2,"pJump":"","pName":"3.jpg","pId":"6da8d1ea-4905-11e8-8390-4ccc6a70ac67"}]}
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
        private List<PicListBean> picList;

        public List<PicListBean> getPicList() {
            return picList;
        }

        public void setPicList(List<PicListBean> picList) {
            this.picList = picList;
        }

        public static class PicListBean {
            /**
             * pNo : 0
             * pJump :
             * pName : 1.jpg
             * pId : 6da1b4c4-4905-11e8-8390-4ccc6a70ac67
             */

            private int pNo;
            private String pJump;
            private String pName;
            private String pId;

            public int getPNo() {
                return pNo;
            }

            public void setPNo(int pNo) {
                this.pNo = pNo;
            }

            public String getPJump() {
                return pJump;
            }

            public void setPJump(String pJump) {
                this.pJump = pJump;
            }

            public String getPName() {
                return pName;
            }

            public void setPName(String pName) {
                this.pName = pName;
            }

            public String getPId() {
                return pId;
            }

            public void setPId(String pId) {
                this.pId = pId;
            }
        }
    }
}
