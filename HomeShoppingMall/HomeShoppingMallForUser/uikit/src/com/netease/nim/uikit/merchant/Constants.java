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
     * 用户端UID
     */
    public static String MYUID = "";
    /**
     * UID
     */
    public static String UID = "";
    /**
     * 当前聊天商户的sType
     */
    public static String sType = "";
    /**
     * SID
     */
    public static String SID = "";
    /**
     * 会话列表刷新
     */
    public static final int LIST_REFRESH = 10001;
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
//    public static final String API_HOST_SHARE = "http://192.168.0.113:8080/HomeShoppingMall/";
    /**
     * 为用户设置备注名
     */
    public static final String setUserMemoName = API_HOST_JAVA + "setUserMemoName";
    /**
     * 获取某用户为别人设置的备注名
     */
    public static final String getUserMemoName = API_HOST_JAVA + "getUserMemoName";
}
