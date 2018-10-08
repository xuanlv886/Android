package app.cn.extra.mall.user.utils;

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
     * 保存用户手机号
     */
    public void setUTel(String UTel) {
        editor.putString("UTel", UTel);
        editor.commit();
    }

    public String getUTel() {
        return sp.getString("UTel", "");
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
     * 保存用户所在城市的行政区划主键标识
     *
     * @param AcId
     */
    public void setAcId(String AcId) {
        editor.putString("AcId", AcId);
        editor.commit();
    }

    public String getAcId() {
        return sp.getString("AcId", "");
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
     * 保存用户选择所在城市的行政区划主键标识
     *
     * @param AcId
     */
    public void setCheckAcId(String AcId) {
        editor.putString("CheckAcId", AcId);
        editor.commit();
    }

    public String getCheckAcId() {
        return sp.getString("CheckAcId", "");
    }

    /**
     * 保存用户选择所在的城市名称
     *
     * @param CityName
     */
    public void setCheckCityName(String CityName) {
        editor.putString("CheckCityName", CityName);
        editor.commit();
    }

    public String getCheckCityName() {
        return sp.getString("CheckCityName", "");
    }

    /**
     * 判断是否设置密保
     *
     * @param Security
     */
    public void setSecurity(String Security) {
        editor.putString("Security", Security);
        editor.commit();
    }

    public String getSecurity() {
        return sp.getString("Security", "");
    }

    // 保存用户是否更新版本
    public void setVersion(String version) {
        editor.putString("version", version);
        editor.commit();
    }

    public String getVersion() {
        return sp.getString("version", "");
    }

    // 保存用户更新版本的路径
    public void setVersionPath(String versionPath) {
        editor.putString("versionPath", versionPath);
        editor.commit();
    }

    public String getVersionPath() {
        return sp.getString("versionPath", "");
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
     * 临时存储支付订单类型
     */
    public void setOrderType(String OrderType) {
        editor.putString("OrderType", OrderType);
        editor.commit();
    }

    public String getOrderType() {
        return sp.getString("OrderType", "");
    }

    /**
     * 临时存储支付订单详情
     */
    public void setOrderData(String OrderData) {
        editor.putString("OrderData", OrderData);
        editor.commit();
    }

    public String getOrderData() {
        return sp.getString("OrderData", "");
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
