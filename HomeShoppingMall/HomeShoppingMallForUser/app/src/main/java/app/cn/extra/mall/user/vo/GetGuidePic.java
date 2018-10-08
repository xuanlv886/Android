package app.cn.extra.mall.user.vo;

import java.util.List;

/**
 * Description
 * Data 2018/6/12-16:02
 * Content
 *
 * @author lzy
 */
public class GetGuidePic {

    /**
     * errorString :
     * flag : true
     * data : {"pics":[{"pNo":0,"url":"http://cfkjcommon1.oss-cn-beijing.aliyuncs.com/test/guide/user01.png"},{"pNo":1,"url":"http://cfkjcommon1.oss-cn-beijing.aliyuncs.com/test/guide/user02.png"},{"pNo":2,"url":"http://cfkjcommon1.oss-cn-beijing.aliyuncs.com/test/guide/user03.png"}],"gEdition":1,"isShow":1}
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
         * pics : [{"pNo":0,"url":"http://cfkjcommon1.oss-cn-beijing.aliyuncs.com/test/guide/user01.png"},{"pNo":1,"url":"http://cfkjcommon1.oss-cn-beijing.aliyuncs.com/test/guide/user02.png"},{"pNo":2,"url":"http://cfkjcommon1.oss-cn-beijing.aliyuncs.com/test/guide/user03.png"}]
         * gEdition : 1
         * isShow : 1
         */

        private int gEdition;
        private int isShow;
        private List<PicsBean> pics;

        public int getGEdition() {
            return gEdition;
        }

        public void setGEdition(int gEdition) {
            this.gEdition = gEdition;
        }

        public int getIsShow() {
            return isShow;
        }

        public void setIsShow(int isShow) {
            this.isShow = isShow;
        }

        public List<PicsBean> getPics() {
            return pics;
        }

        public void setPics(List<PicsBean> pics) {
            this.pics = pics;
        }

        public static class PicsBean {
            /**
             * pNo : 0
             * url : http://cfkjcommon1.oss-cn-beijing.aliyuncs.com/test/guide/user01.png
             */

            private int pNo;
            private String url;

            public int getPNo() {
                return pNo;
            }

            public void setPNo(int pNo) {
                this.pNo = pNo;
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
