package app.cn.extra.mall.user.event;

public class AddAddressEvent {
    private String acCity;

    public AddAddressEvent() {

    }

    public AddAddressEvent(String acCity) {
        this.acCity = acCity;
    }

    public String getAcCity() {
        return acCity;
    }

    public void setAcCity(String acCity) {
        this.acCity = acCity;
    }
}
