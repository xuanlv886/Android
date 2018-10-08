package app.cn.extra.mall.user.vo;

/**
 * Created by Administrator on 2017/5/10 0010.
 */

public class GoodsPictures {
    private String pName;
    private int  pNo;

    public GoodsPictures(String pName, int pNo) {
        this.pName = pName;
        this.pNo = pNo;
    }

    public String getpName() {
        return pName;
    }
    public void setpName(String pName) {
        this.pName = pName;
    }

    public int getpNo() {
        return pNo;
    }

    public void setpNo(int pNo) {
        this.pNo = pNo;
    }
}
