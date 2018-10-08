package app.cn.extra.mall.user.event;

public class PendingSureDetailsEvent {
    private String sId;
    private String roId;

    public PendingSureDetailsEvent() {

    }

    public PendingSureDetailsEvent(String sId, String roId) {
        this.sId = sId;
        this.roId = roId;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getRoId() {
        return roId;
    }

    public void setRoId(String roId) {
        this.roId = roId;
    }
}
