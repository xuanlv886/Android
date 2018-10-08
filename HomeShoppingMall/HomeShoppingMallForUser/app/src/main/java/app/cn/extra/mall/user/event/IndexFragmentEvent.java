package app.cn.extra.mall.user.event;

public class IndexFragmentEvent {
    private String acCity;

    public IndexFragmentEvent() {

    }

    public IndexFragmentEvent(String acCity) {
        this.acCity = acCity;
    }

    public String getAcCity() {
        return acCity;
    }

    public void setAcCity(String acCity) {
        this.acCity = acCity;
    }
}
