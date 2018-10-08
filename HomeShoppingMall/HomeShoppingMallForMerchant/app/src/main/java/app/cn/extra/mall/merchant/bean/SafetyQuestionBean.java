package app.cn.extra.mall.merchant.bean;

import java.util.List;

/**
 * Description
 * Data 2018/7/12-14:11
 * Content
 *
 * @author L
 */
public class SafetyQuestionBean {
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sqAnswer : xxx
         * sqPosition : 1
         */

        private String sqAnswer;
        private int sqPosition;

        public String getSqAnswer() {
            return sqAnswer;
        }

        public void setSqAnswer(String sqAnswer) {
            this.sqAnswer = sqAnswer;
        }

        public int getSqPosition() {
            return sqPosition;
        }

        public void setSqPosition(int sqPosition) {
            this.sqPosition = sqPosition;
        }

        @Override
        public String toString() {
            return "{" +
                    "sqAnswer='" + sqAnswer + '\'' +
                    ", sqPosition=" + sqPosition +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "{" +
                "data=" + data +
                '}';
    }
}
