package app.cn.extra.mall.merchant.event;

/**
 * Description
 * Data 2018/7/17-14:56
 * Content
 *
 * @author L
 */
public class SingleInfoEvent {

    private String uNickName;
    private String uEmail;
    private String uBirthday;
    private String uTel;
    private String sName;
    private String sDescribe;
    private String sTel;

    public SingleInfoEvent(String uNickName, String uEmail, String uBirthday, String uTel,
                           String sName, String sDescribe, String sTel) {
        this.uNickName = uNickName;
        this.uEmail = uEmail;
        this.uBirthday = uBirthday;
        this.uTel = uTel;
        this.sName = sName;
        this.sDescribe = sDescribe;
        this.sTel = sTel;
    }

    public String getuNickName() {
        return uNickName;
    }

    public void setuNickName(String uNickName) {
        this.uNickName = uNickName;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuBirthday() {
        return uBirthday;
    }

    public void setuBirthday(String uBirthday) {
        this.uBirthday = uBirthday;
    }

    public String getuTel() {
        return uTel;
    }

    public void setuTel(String uTel) {
        this.uTel = uTel;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsDescribe() {
        return sDescribe;
    }

    public void setsDescribe(String sDescribe) {
        this.sDescribe = sDescribe;
    }

    public String getsTel() {
        return sTel;
    }

    public void setsTel(String sTel) {
        this.sTel = sTel;
    }
}
