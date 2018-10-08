package app.cn.extra.mall.merchant.vo;

import java.util.List;

/**
 * Description
 * Data 2018/7/18-14:55
 * Content
 *
 * @author L
 */
public class GetSafetyQuestion {

    /**
     * errorString :
     * flag : true
     * data : {"safetyQuestionList":[{"sqName":"你小时候最喜欢哪一本书？","sqPosition":1,"sqId":"e9fb09f8-43ac-11e8-8390-4ccc6a70ac67"},{"sqName":"你的理想工作是什么？","sqPosition":1,"sqId":"e9fb0c36-43ac-11e8-8390-4ccc6a70ac67"},{"sqName":"你童年时代的绰号是什么？","sqPosition":1,"sqId":"e9fb0c8d-43ac-11e8-8390-4ccc6a70ac67"},{"sqName":"你拥有的第一辆车是什么型号？","sqPosition":1,"sqId":"e9fb0cab-43ac-11e8-8390-4ccc6a70ac67"},{"sqName":"你在学生时代最喜欢哪个歌手或乐队？","sqPosition":1,"sqId":"e9fb0cc9-43ac-11e8-8390-4ccc6a70ac67"},{"sqName":"你在学生时代最喜欢哪个电影明星或角色？","sqPosition":1,"sqId":"e9fb0ce4-43ac-11e8-8390-4ccc6a70ac67"}]}
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
        private List<SafetyQuestionListBean> safetyQuestionList;

        public List<SafetyQuestionListBean> getSafetyQuestionList() {
            return safetyQuestionList;
        }

        public void setSafetyQuestionList(List<SafetyQuestionListBean> safetyQuestionList) {
            this.safetyQuestionList = safetyQuestionList;
        }

        public static class SafetyQuestionListBean {
            /**
             * sqName : 你小时候最喜欢哪一本书？
             * sqPosition : 1
             * sqId : e9fb09f8-43ac-11e8-8390-4ccc6a70ac67
             */

            private String sqName;
            private int sqPosition;
            private String sqId;

            public String getSqName() {
                return sqName;
            }

            public void setSqName(String sqName) {
                this.sqName = sqName;
            }

            public int getSqPosition() {
                return sqPosition;
            }

            public void setSqPosition(int sqPosition) {
                this.sqPosition = sqPosition;
            }

            public String getSqId() {
                return sqId;
            }

            public void setSqId(String sqId) {
                this.sqId = sqId;
            }
        }
    }
}
