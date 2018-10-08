package com.netease.nim.uikit.merchant;

/**
 * Description
 * Data 2018/4/21-14:37
 * Content
 *
 * @author lzy
 */
public class Constants {
    /**
     * USERUID
     */
    public static String USERUID = "";
    /**
     * UID
     */
    public static String UID = "";
    /**
     * SID
     */
    public static String SID = "";
    /**
     * sType
     */
    public static String sType = "";
    /**
     * 聊天右侧列表点击Action
     */
    public static final String RIGHTMENU_ITEM_CLICK_ACTION = "app.cn.extra.mall.merchant.rightmenu.item.click.action";
    /**
     * 接口返回的数据正常
     */
    public static final String OK = "true";
    /**
     * 无网络Toast提示语
     */
    public static final String NETWORK_ERROR = "您好像进入了没有网络链接的异次元···";
    /**
     * API 基础地址
     */
    public static final String API_HOST_JAVA = "http://123.56.218.108:8080/HomeShoppingMall/";
    public static final String API_HOST_SHARE = "http://123.56.218.108:8080/HomeShoppingMall/";
    /**
     * 为用户设置备注名
     */
    public static final String setUserMemoName = API_HOST_JAVA + "setUserMemoName";
    /**
     * 获取某用户为别人设置的备注名
     */
    public static final String getUserMemoName = API_HOST_JAVA + "getUserMemoName";
    /**
     * 获取某用户在某店铺内浏览过的商品列表接口
     */
    public static final String getStoreProductListFromUserFootprint = API_HOST_JAVA + "getStoreProductListFromUserFootprint";
}
