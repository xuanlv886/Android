package app.cn.extra.mall.merchant.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Description 存储APP数据
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class SharePreferenceUtil {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SharePreferenceUtil(Context context, String file) {
        sp = context.getSharedPreferences(file, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    /**
     * 临时存储是跳转到修改密码还是忘记密码
     */
    public void setPassWordFlag(String PassWordFlag) {
        editor.putString("PassWordFlag", PassWordFlag);
        editor.commit();
    }

    public String getPassWordFlag() {
        return sp.getString("PassWordFlag", "");
    }

    /**
     * 保存用户UID
     *
     * @param UID
     */
    public void setUID(String UID) {
        editor.putString("UID", UID);
        editor.commit();
    }

    public String getUID() {
        return sp.getString("UID", "");
    }

    /**
     * 保存商户是否通过审核
     *
     * @param SChecked
     */
    public void setSChecked(String SChecked) {
        editor.putString("SChecked", SChecked);
        editor.commit();
    }

    public String getSChecked() {
        return sp.getString("SChecked", "");
    }

    /**
     * 保存用户账号
     *
     * @param UAccount
     */
    public void setUAccount(String UAccount) {
        editor.putString("UAccount", UAccount);
        editor.commit();
    }

    public String getUAccount() {
        return sp.getString("UAccount", "");
    }

    /**
     * 保存商户SID
     *
     * @param SID
     */
    public void setSID(String SID) {
        editor.putString("SID", SID);
        editor.commit();
    }

    public String getSID() {
        return sp.getString("SID", "");
    }

    /**
     * 保存商户类型 0--个人商户，1--企业商户
     *
     * @param SType
     */
    public void setSType(String SType) {
        editor.putString("SType", SType);
        editor.commit();
    }

    public String getSType() {
        return sp.getString("SType", "");
    }

    /**
     * 保存设备ID
     *
     * @param PhoneId
     */
    public void setPhoneId(String PhoneId) {
        editor.putString("PhoneId", PhoneId);
        editor.commit();
    }

    public String getPhoneId() {
        return sp.getString("PhoneId", "");
    }

    /**
     * 判断用户是否第一次打开本APP
     *
     * @param FirstOpen
     */
    public void setFirstOpen(String FirstOpen) {
        editor.putString("FirstOpen", FirstOpen);
        editor.commit();
    }

    public String getFirstOpen() {
        return sp.getString("FirstOpen", "");
    }

    /**
     * 引导页版本
     *
     * @param GuideEdition
     */
    public void setGuideEdition(int GuideEdition) {
        editor.putInt("GuideEdition", GuideEdition);
        editor.commit();
    }

    public int getGuideEdition() {
        return sp.getInt("GuideEdition", 0);
    }

    /**
     * 保存商户当前所在城市的主键标识
     *
     * @param LocationAcId
     */
    public void setLocationAcId(String LocationAcId) {
        editor.putString("LocationAcId", LocationAcId);
        editor.commit();
    }

    public String getLocationAcId() {
        return sp.getString("LocationAcId", "");
    }

    /**
     * 保存商户店铺所在城市的主键标识
     *
     * @param StoreAcId
     */
    public void setStoreAcId(String StoreAcId) {
        editor.putString("StoreAcId", StoreAcId);
        editor.commit();
    }

    public String getStoreAcId() {
        return sp.getString("StoreAcId", "");
    }

    /**
     * 保存用户所在的城市名称
     *
     * @param CityName
     */
    public void setCityName(String CityName) {
        editor.putString("CityName", CityName);
        editor.commit();
    }

    public String getCityName() {
        return sp.getString("CityName", "");
    }

    /**
     * 临时存储支付订单号
     *
     * @param PayCode
     */
    public void setPayCode(String PayCode) {
        editor.putString("PayCode", PayCode);
        editor.commit();
    }

    public String getPayCode() {
        return sp.getString("PayCode", "");
    }

    /**
     * 临时存储支付金额
     *
     * @param TotalMoney
     */
    public void setTotalMoney(String TotalMoney) {
        editor.putString("TotalMoney", TotalMoney);
        editor.commit();
    }

    public String getTotalMoney() {
        return sp.getString("TotalMoney", "");
    }

    /**
     * 网易云账号
     */
    public void saveUserAccount(String account) {
        editor.putString("account", account);
        editor.commit();
    }

    public String getUserAccount() {
        return sp.getString("account", "");
    }

    /**
     * 网易云token
     */
    public void saveUserToken(String token) {
        editor.putString("token", token);
        editor.commit();
    }

    public String getUserToken() {
        return sp.getString("token", "");
    }

    /**
     * 网易云id
     */
    public void saveAccid(String accid) {
        editor.putString("accid", accid);
        editor.commit();
    }

    public String getAccid() {
        return sp.getString("accid", "");
    }

    /**
     * 临时存储状态，用来判断是否可以显示互踢弹窗
     */
    public void setBrokenLineTag(String BrokenLine) {
        editor.putString("BrokenLine", BrokenLine);
        editor.commit();
    }

    public String getBrokenLineTag() {
        return sp.getString("BrokenLine", "");
    }

    /**
     * 临时存储状态，用来判断是否可以显示通知权限弹窗
     */
    public void setNotificatonPermissionTag(int NotificatonPermission) {
        editor.putInt("NotificatonPermission", NotificatonPermission);
        editor.commit();
    }

    public int getNotificatonPermissionTag() {
        return sp.getInt("NotificatonPermission", 0);
    }
}
