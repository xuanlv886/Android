package app.cn.extra.mall.merchant.vo;

import java.util.List;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class GoodsPictures {

    /**
     * pTag : 传空字符串即可
     * pics : [{"pName":"图片名称","pNo":0},{"pName":"图片名称","pNo":1},{"pName":"图片名称","pNo":2}]
     */

    private String pTag;
    private List<PicsBean> pics;

    public String getPTag() {
        return pTag;
    }

    public void setPTag(String pTag) {
        this.pTag = pTag;
    }

    public List<PicsBean> getPics() {
        return pics;
    }

    public void setPics(List<PicsBean> pics) {
        this.pics = pics;
    }

    public static class PicsBean {
        /**
         * pName : 图片名称
         * pNo : 0
         */

        private String pName;
        private int pNo;

        public String getPName() {
            return pName;
        }

        public void setPName(String pName) {
            this.pName = pName;
        }

        public int getPNo() {
            return pNo;
        }

        public void setPNo(int pNo) {
            this.pNo = pNo;
        }
    }
}
