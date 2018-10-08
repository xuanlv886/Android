package app.cn.extra.mall.user.utils;

/**
 * Description 用以存储常量，尽量减少魔法值的存在
 * Data 2018/4/21-14:37
 * Content
 *
 * @author lzy
 */
public class Constants {
    /**
     * 自定义的Provider
     */
    public static String PROVIDER_NAME = "app.cn.extra.mall.user.utils.MyProvider";
    /**
     * 互踢Action
     */
    public static final String BROKEN_LINE_ACTION = "app.cn.extra.mall.user.broken.line.action";
    /**
     * 设置应用安装包的后缀
     */
    public static final String APK_TYPE = ".apk";
    /**
     * 忘记密码标识
     */
    public static final String FORGET_PASSWORD = "forget_password";
    /**
     * 修改密码标识
     */
    public static final String REVISE_PASSWORD = "revise_password";
    /**
     * 保存应用信息的xml文件名
     */
    public static final String SAVE_USER = "USER";
    /**
     * 无网络Toast提示语
     */
    public static final String NETWORK_ERROR = "您好像进入了没有网络链接的异次元···";

    /**
     * 客户端类型 0--用户端，1--商户端
     */
    public static final String APP_TYPE = "0";

    /**
     * 接口返回的数据正常
     */
    public static final String OK = "true";
    /**
     * 通用返回数据的条数
     */
    public static final int PAGE_SIZE = 16;
    /**
     * 店铺图片路径
     */
    public static final String PROFILE_PIC = "profilephoto/";
    /**
     * 评价图片路径
     */
    public static final String EVALUATE_PIC = "evaluate/";
    /**
     * 设置自定义生成的图片的后缀
     */
    public static final String IMG_TYPE = ".jpg";
    public static final String bucketName = "hsmcommon";
    public static final String endpoint = "oss-cn-beijing.aliyuncs.com";
    public static final String PICS_URL = "https://hsmcommon.oss-cn-beijing.aliyuncs.com/";
    public static final String accessKeyId = "LTAIFYiCJtna4hE0";
    public static final String accessKeySecret = "Tq24v1zG0SAg2l09sumIa5LVQUzr4K";
    /**
     * 支付方式名称
     */
    public static final String WX_PAY = "微信支付";
    public static final String ALI_PAY = "支付宝支付";
    /**
     * API 基础地址
     * <p>
     * 注意修改uikit中的基础地址！！！！！
     */
    public static final String API_HOST_JAVA = "http://123.56.218.108:8080/HomeShoppingMall/";
//    public static final String API_HOST_SHARE = "http://123.56.218.108:8080/HomeShoppingMall/";
    /**
     * 获取引导页图片接口
     */
    public static final String getGuidePic = API_HOST_JAVA + "getGuidePic";
    /**
     * 获取行政区划信息接口
     */
    public static final String getAreaCode = API_HOST_JAVA + "getAreaCode";
    /**
     * 获取主页面相关信息接口
     */
    public static final String getMainInterface = API_HOST_JAVA + "getMainInterface";
    /**
     * 用户注册接口
     */
    public static final String userRegistered = API_HOST_JAVA + "userRegistered";
    /**
     * 注册短信验证接口
     */
    public static final String UR_CAPTCHA = API_HOST_JAVA + "getCaptcha";
    /**
     * 登录接口
     */
    public static final String userLogin = API_HOST_JAVA + "userLogin";
    /**
     * 忘记密码短信验证接口
     */
    public static final String FP_CAPTCHA = API_HOST_JAVA + "getCaptcha";
    /**
     * 短信验证接口
     */
    public static final String getCaptcha = API_HOST_JAVA + "getCaptcha";
    /**
     * 修改密码接口
     */
    public static final String updateUserPassword = API_HOST_JAVA + "updateUserPassword";
    /**
     * 获取城市列表接口
     */
    public static final String getCityList = API_HOST_JAVA + "getCityList";
    /**
     * 搜获城市接口
     */
    public static final String getSearchCityList = API_HOST_JAVA + "getSearchCityList";
    /**
     * 重置密码接口
     */
    public static final String resetUserPassword = API_HOST_JAVA + "resetUserPassword";
    /**
     * 获取店铺详情接口
     */
    public static final String getStoreDetail = API_HOST_JAVA + "getStoreDetail";
    /**
     * 店铺轮播图接口
     */
    public static final String getStoreSlideShow = API_HOST_JAVA + "getStoreSlideShow";
    /**
     * 用户关注店铺接口
     */
    public static final String addUserAttention = API_HOST_JAVA + "addUserAttention";
    /**
     * 商品大类接口
     */
    public static final String getProductType = API_HOST_JAVA + "getProductType";
    /**
     * 商品底层类
     */
    public static final String getProductTypeDetailByPtId = API_HOST_JAVA + "getProductTypeDetailByPtId";
    /**
     * 热门搜索接口
     */
    public static final String getHotSearch = API_HOST_JAVA + "getHotSearch";
    /**
     * 商品列表
     */
    public static final String getProductList = API_HOST_JAVA + "getProductList";
    /**
     * 获取商品筛选接口
     */
    public static final String getProductProperty = API_HOST_JAVA + "getProductProperty";
    /**
     * 获取用户所有购物车商品接口
     */
    public static final String getUserTrolley = API_HOST_JAVA + "getUserTrolley";
    /**
     * 获取个人中心接口
     */
    public static final String getPersonCenter = API_HOST_JAVA + "getPersonCenter";
    /**
     * 收藏列表接口
     */
    public static final String getUserCollection = API_HOST_JAVA + "getUserCollection";
    /**
     * 删除收藏接口
     */
    public static final String delUserCollection = API_HOST_JAVA + "delUserCollection";
    /**
     * 获取用户关注店铺接口
     */
    public static final String getUserAttention = API_HOST_JAVA + "getUserAttention";
    /**
     * 删除关注店铺接口
     */
    public static final String delUserAttention = API_HOST_JAVA + "delUserAttention";
    /**
     * 获取用户足迹接口
     */
    public static final String getUserFootPrint = API_HOST_JAVA + "getUserFootPrint";
    /**
     * 删除足迹接口
     */
    public static final String delUserFootPrint = API_HOST_JAVA + "delUserFootPrint";
    /**
     * 获取所有商品订单接口
     */
    public static final String getProductOrder = API_HOST_JAVA + "getProductOrder";
    /**
     * 删除商品订单接口
     */
    public static final String delProductOrder = API_HOST_JAVA + "delProductOrder";
    /**
     * 获取订单详情接口
     */
    public static final String getProductOrderDetail = API_HOST_JAVA + "getProductOrderDetail";
    /**
     * 订单状态 全部
     */
    public static final String STATUS_ALL = "";
    /**
     * 订单状态 待付款
     */
    public static final String STATUS_PENDINGPAYMENT = "0";
    /**
     * 订单状态 待发货
     */
    public static final String STATUS_PENDINGDELIVERY = "1";
    /**
     * 订单状态 待收货
     */
    public static final String STATUS_PENDINGCOLLECTGOODS = "2";
    /**
     * 订单状态 待评价
     */
    public static final String STATUS_PENDINGEVALUATE = "3";
    /**
     * 订单状态 已完成
     */
    public static final String STATUS_COMPLETEORDER = "4";
    /**
     * 订单状态 待退款
     */
    public static final String STATUS_UNREFUNdORDER = "5";
    /**
     * 订单状态 已退款
     */
    public static final String STATUS_ALREADYREFUNDORDER = "6";
    /**
     * 获取我的需求接口
     */
    public static final String getUserRequirement = API_HOST_JAVA + "getUserRequirement";
    /**
     * 需求状态  待接单
     */
    public static final String STATUS_DEMAND_PENDINGORDER = "7";
    /**
     * 需求状态  待确认
     */
    public static final String STATUS_DEMAND_PENDINGSURE = "0";
    /**
     * 需求状态  进行中
     */
    public static final String STATUS_DEMAND_CONDUCTION = "1";
    /**
     * 需求状态  已完成
     */
    public static final String STATUS_DEMAND_COMPLETED = "2";
    /**
     * 需求状态  取货中
     */
    public static final String STATUS_DEMAND_PICKUPGOODS = "3";
    /**
     * 需求状态  待验货
     */
    public static final String STATUS_DEMAND_INSPECTIONGOODS = "4";
    /**
     * 需求状态  送货中
     */
    public static final String STATUS_DEMAND_DELIVERY = "5";
    /**
     * 需求状态  已评价
     */
    public static final String STATUS_DEMAND_EVALUATED = "6";
    /**
     * 获取用户收货地址接口
     */
    public static final String getUserDeliverAddress = API_HOST_JAVA + "getUserDeliverAddress";
    /**
     * 添加收货地址接口
     */
    public static final String addUserDeliverAddress = API_HOST_JAVA + "addUserDeliverAddress";
    /**
     * 修改收货地址接口
     */
    public static final String updateUserDeliverAddress = API_HOST_JAVA + "updateUserDeliverAddress";
    /**
     * 删除收货地址接口
     */
    public static final String delUserDeliverAddress = API_HOST_JAVA + "delUserDeliverAddress";
    /**
     * 设置默认地址
     */
    public static final String setDefaultUserDeliverAddress = API_HOST_JAVA + "setDefaultUserDeliverAddress";
    /**
     * 获取密保问题接口
     */
    public static final String getSafetyQuestion = API_HOST_JAVA + "getSafetyQuestion";
    /**
     * 设置密保接口
     */
    public static final String updateUserSecretQuestion = API_HOST_JAVA + "updateUserSecretQuestion";
    /**
     * 是否设置过密保问题接口
     */
    public static final String msGetStatusOfSafetyQuestions = API_HOST_JAVA + "msGetStatusOfSafetyQuestions";
    /**
     * 获取某用户已设置的安全问题接口
     */
    public static final String msGetSafetyQuestionOfUser = API_HOST_JAVA + "msGetSafetyQuestionOfUser";
    /**
     * 校验某用户已设置的安全问题接口
     */
    public static final String msCheckSafetyQuestionOfUser = API_HOST_JAVA + "msCheckSafetyQuestionOfUser";
    /**
     * 意见反馈接口
     */
    public static final String addUserFeedBack = API_HOST_JAVA + "addUserFeedBack";
    /**
     * 获取商品详情接口
     */
    public static final String getProductInfo = API_HOST_JAVA + "getProductInfo";
    /**
     * 获取推荐商品接口
     */
    public static final String getRecommendProductList = API_HOST_JAVA + "getRecommendProductList";
    /**
     * 获取账户信息
     */
    public static final String getUserInfo = API_HOST_JAVA + "getUserInfo";
    /**
     * 修改个人信息接口
     */
    public static final String updateUserInfo = API_HOST_JAVA + "updateUserInfo";
    /**
     * 获取商品评价接口
     */
    public static final String getProductEvaluate = API_HOST_JAVA + "getProductEvaluate";
    /**
     * 加入购物车
     */
    public static final String addUserTrolley = API_HOST_JAVA + "addUserTrolley";
    /**
     * 获取商品根据关键字
     */
    public static final String selectBylooseName = API_HOST_JAVA + "selectBylooseName";
    /**
     * 删除购物车
     */
    public static final String delUserTrolley = API_HOST_JAVA + "delUserTrolley";
    /**
     * 检查更新
     */
    public static final String checkUpdate = API_HOST_JAVA + "checkUpdate";
    /**
     * 需求详情
     */
    public static final String getUserRequirementDetail = API_HOST_JAVA + "getUserRequirementDetail";
    /**
     * 发布需求
     */
    public static final String addUserRequirement = API_HOST_JAVA + "addUserRequirement";
    /**
     * 获取需求类别
     */
    public static final String selectRequirementType = API_HOST_JAVA + "selectRequirementType";
    /**
     * 修改需求
     */
    public static final String updateUserRequirement = API_HOST_JAVA + "updateUserRequirement";
    /**
     * 删除需求
     */
    public static final String delUserRequirement = API_HOST_JAVA + "delUserRequirement";
    /**
     * 需求选择商铺--确认
     */
    public static final String updateRequirementOrderStatus = API_HOST_JAVA + "updateRequirementOrderStatus";
    /**
     * 需求选择商铺--移除
     */
    public static final String deleteStoreApplyRequirement = API_HOST_JAVA + "deleteStoreApplyRequirement";
    /**
     * 确认需求
     */
    public static final String addUserProductRequirement = API_HOST_JAVA + "addUserProductRequirement";
    /**
     * 添加需求评价
     */
    public static final String addRequirementEvaluate = API_HOST_JAVA + "addRequirementEvaluate";
    /**
     * 获取某商户针对于特定需求订单的移动轨迹列表
     */
    public static final String getMerchantLonLatList = API_HOST_JAVA + "getMerchantLonLatList";
    /**
     * 订单确认收货
     */
    public static final String getConfirmReceipt = API_HOST_JAVA + "getConfirmReceipt";
    /**
     * 商品订单退款
     */
    public static final String getProductRequestRefund = API_HOST_JAVA + "getProductRequestRefund";
    /**
     * 获取确认验货页面数据
     */
    public static final String stayInspectionProduct = API_HOST_JAVA + "stayInspectionProduct";
    /**
     * 确认验货
     */
    public static final String rorderVerification = API_HOST_JAVA + "rorderVerification";
    /**
     * 获取某用户为别人设置的备注名
     */
    public static final String getUserMemoName = API_HOST_JAVA + "getUserMemoName";
    /**
     * 为用户设置备注名
     */
    public static final String setUserMemoName = API_HOST_JAVA + "setUserMemoName";
    /**
     * 获取支付方式
     */
    public static final String getPayStyle = API_HOST_JAVA + "getPayStyle";
    /**
     * 获取支付订单号
     */
    public static final String getOrderPayCode = API_HOST_JAVA + "getOrderPayCode";
    /**
     * 微信支付统一下单接口
     */
    public static final String wxPayUnifiedOrder = API_HOST_JAVA + "wxPayUnifiedOrder";
    /**
     * 查询微信订单支付结果接口
     */
    public static final String wxPayOrderQuery = API_HOST_JAVA + "wxPayOrderQuery";
    /**
     * 支付宝支付统一下单接口
     */
    public static final String ailPayUnifiedOrder = API_HOST_JAVA + "ailPayUnifiedOrder";
    /**
     * 查询支付宝订单支付结果接口
     */
    public static final String aliPayOrderQuery = API_HOST_JAVA + "aliPayOrderQuery";
    /**
     * 微信待付款统一下单
     */
    public static final String wxProductUnPayUnifiedOrder = API_HOST_JAVA + "wxProductUnPayUnifiedOrder";
    /**
     * 支付宝待付款统一下单
     */
    public static final String ailProductUnPayUnifiedOrder = API_HOST_JAVA + "ailProductUnPayUnifiedOrder";
    /**
     * 添加收藏
     */
    public static final String addUserCollection = API_HOST_JAVA + "addUserCollection";
    /**
     * 添加商品评价
     */
    public static final String addProductEvaluate = API_HOST_JAVA + "addProductEvaluate";
    /**
     * 修改用户头像
     */
    public static final String updateUserAvatar = API_HOST_JAVA + "updateUserAvatar";
    /**
     * 找回密码接口
     */
    public static final String retrievePsw = API_HOST_JAVA + "retrievePsw";

    /**
     * The MIME type of image
     */
    public static final String MIMETYPE_IMAGE = "image/*";

    /**
     * 微信
     */
    public static final String WEIXIN_PARTERID = "1509738791";
    public static final String APP_ID = "wx2b564260035762ff";
    public static final String WEIXIN_PARTERKEY = "aituankeji18348888899homemallsys";
    public static final String SDPATHLOAD = "/TimeSharePlatFrom/download";//下载apk 目录
    //微信的统一支付接口的地址
    public static final String wxUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2018010801686239";
    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "";
    public static final String RSA2_PRIVATE = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCWZ4ZL+mDxlt/" +
            "hSkI5hBYXYJE9YX4lfCocH8Odk0p1F7iKwa9SuGclLw2HUZK7BnLePZoi72876qdN/n3FFIL3Z/wgaNVZj67z+zvNY3zSUOBgD" +
            "Jdo0aSqUv8kH7qpRZn1Kj8/2D78JfQp1FUHHxKZ2rRg19anOiZxN76SXgFWVmS8cSxzOPvxYRpLMUZQgY4r47tJwMmabZrwItf" +
            "nnjrH/ckjY9MUJw/x5gxGHyD2wU7iHefg5YeDLwl4HPp8mBTLWssHUQfteVtiX33yRnBiwQKDO/5/yq6gDqPtc1nuF9T/e4Zk/" +
            "K9xe00Ft4eK7Nr5GfCVnLVuY+mmOM+9aQLnAgMBAAECggEAMU8zeklV6x6Zu5TK7Wxeib+BHmyOHN/+NkY7rbeSW50xRVyGjEok" +
            "ls48UzbtMh/FHRN5KybwguZFf46F0U0OOup+gGmeOMSgiSC+1HFfhNw7E4JNSXIh2C1ptIAqhEBrXzNAlT/iNmyDBgu07KbFRGe" +
            "tZ2qb1yt7Wvb5paboLVOY+xaRfIB4eM9PCQUBuRj/bIJG6FpxFcfHgJAW/Z77ynwASAh9pnm8+Ms0449bqxtQXDGNg37Vzoww5J" +
            "fnw8jf4ryCMrSG8rdpgipzfVBJ3xO58oltD3esRQuKFdjjfc5wfdyaA0dLoD1oXJvLRc+bdhRiM9G0z5ZNNtUcEPtQsQKBgQDGf" +
            "NwB0djUHohtmCXxdYhh1LEGSDwXiePN51lDo+p1+EFjIjLJ03SK87w4hr20fyF/7J1NH2PYZ3IUjf3CTEQ8HwJ0aXLBHv8CyT0Ch" +
            "/aBulD8V6Rz3DXOQrLRdF/70f1aKEUa3CedevIOZvReNsoR4TWlpGhwqJVj7/lsgzXKuwKBgQDB/AH3DPdXie5KLzgZIzEeW8z8a" +
            "lP+rHfRdmhoa/NUOTtB5xC0DQS83dk3QBShaSwA6VOYGSvJfrOxlkGsMb/qvZfI+9G4+4j/n7xml1Qo551DmXokCt+h8l3bp5RE" +
            "49GtYR/g/curqyzNjJQfnGZyMOQG+mdHsDQ3iainzrZzxQKBgQCls+cXvdB+hV7v320Nv0k0IxQGYpeILx128ASda2J5Sd+Mmyg7" +
            "RcStm/0uU45b+SZGXYqUr/fy2alrb37BjloItvqdpMqgXvgVo1rDmi447wpdH+2yq8xffV87bSQUXqJbefL3EYQg/FrHmzu2y5a2h" +
            "oT629407wiIffaEmKV3sQKBgGErFuDlgdA7qz1Fwdf1pNkMKvYVXNWpFqu5oDsON+jsch5k3B0UmFby2aevVaY0xHIAbns1wJVB/k" +
            "jqdyMGGJhBOBLY8Qi0Fr/VB85r5yBGRKRj8Ka1O6gne5RXsRGA/LrYd9t/xdHMALlFvbzH3xk8HDzSmYFzZfSb4/uKtCHxAoGBALw" +
            "VoNZH0r3f7Y9h8/lwSAD5wz5v9LGCb+xeimfwxt+P+qMJgquiu5gXCA5SIS7rWQpPSPtx6zKvuuYSTRF0h29XjzoPqg9AhBXiNtrG" +
            "syQP0UC98zTbgvf1vO3GJC8Jy92bzIOjlXbk3d5BnDmGc0vtuSng8yUVHmuyzE4jgi1a";
    public static final int TAKE_CAMERA = 0x000001;
    public static final int TAKE_PHOTO = 0x000002;
    public static final int REQUEST_CODE = 0x00000011;
}
