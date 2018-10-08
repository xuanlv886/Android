package app.cn.extra.mall.user.event;

public class EditAddressEvent {
    private String acCity;

    public EditAddressEvent() {

    }

    public EditAddressEvent(String acCity) {
        this.acCity = acCity;
    }

    public String getAcCity() {
        return acCity;
    }

    public void setAcCity(String acCity) {
        this.acCity = acCity;
    }
}
