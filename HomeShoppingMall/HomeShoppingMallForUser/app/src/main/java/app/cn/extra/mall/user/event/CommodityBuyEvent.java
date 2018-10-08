package app.cn.extra.mall.user.event;

/**
 * Description
 * Data 2018/7/9-11:01
 * Content
 *
 * @author L
 */
public class CommodityBuyEvent {
    private String addressName;
    private String tags;
    private String address;
    private String addressTel;
    private String addressId;
    private String addressTags;

    public CommodityBuyEvent() {

    }

    public CommodityBuyEvent(String addressName, String tags, String address, String addressTel, String addressId, String addressTags) {
        this.addressName = addressName;
        this.tags = tags;
        this.address = address;
        this.addressTel = addressTel;
        this.addressId = addressId;
        this.addressTags = addressTags;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressTel() {
        return addressTel;
    }

    public void setAddressTel(String addressTel) {
        this.addressTel = addressTel;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressTags() {
        return addressTags;
    }

    public void setAddressTags(String addressTags) {
        this.addressTags = addressTags;
    }
}
