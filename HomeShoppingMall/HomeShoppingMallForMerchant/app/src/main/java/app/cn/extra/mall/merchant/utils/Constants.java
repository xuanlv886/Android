package app.cn.extra.mall.merchant.utils;

/**
 * Description
 * Data 2018/4/21-14:37
 * Content
 *
 * @author lzy
 */
public class Constants {

    /**
     * 保存应用信息的xml文件名
     */
    public static final String SAVE_USER = "HMMERCHANT";

    /**
     * 自定义的Provider
     */
    public static String PROVIDER_NAME = "app.cn.extra.mall.merchant.utils.MyProvider";
    /**
     * 互踢Action
     */
    public static final String BROKEN_LINE_ACTION = "app.cn.extra.mall.merchant.broken.line.action";

    /**
     * 计时Action
     */
    public static final String TIME_CHANGED_ACTION = "app.cn.extra.mall.merchant.time.changed.action";
    /**
     * 聊天右侧列表点击Action
     */
    public static final String RIGHTMENU_ITEM_CLICK_ACTION = "app.cn.extra.mall.merchant.rightmenu.item.click.action";

    /**
     * 自定义的Service名称
     */
    public static String SERVICE_NAME = "app.cn.extra.mall.merchant.service.MovingTrajectoryService";

    /**
     * 无网络Toast提示语
     */
    public static final String NETWORK_ERROR = "您好像进入了没有网络链接的异次元···";
    /**
     * 忘记密码标识
     */
    public static final String FORGET_PASSWORD = "forget_password";
    /**
     * 修改密码标识
     */
    public static final String REVISE_PASSWORD = "revise_password";

    /**
     * 客户端类型 0--用户端，1--商户端
     */
    public static final String APP_TYPE = "1";

    /**
     * 个人身份商户
     */
    public static final String M_PERSONAL = "0";
    /**
     * 企业身份商户
     */
    public static final String M_COMPANY = "1";

    /**
     * 接口返回的数据正常
     */
    public static final String OK = "true";
    /**
     * 通用返回数据的条数
     */
    public static final int PAGE_SIZE = 16;
    /**
     * 设置图片存储的文件夹
     */
    public static final String PHOTO_DIR = "photo/";
    /**
     * 设置自定义生成的图片的后缀
     */
    public static final String IMG_TYPE = ".jpg";
    /**
     * 设置应用安装包的后缀
     */
    public static final String APK_TYPE = ".apk";
    /**
     * 设置自定义的数据传输格式
     */
    public static final String DATA_TYPE = "JSON";
    /**
     * 支付方式名称
     */
    public static final String WX_PAY = "微信支付";
    public static final String ALI_PAY = "支付宝支付";
    /**
     * 支付方式id
     */
    public static final String WX_PSID = "f1f2215c-43a9-11e8-8390-4ccc6a70ac67";
    public static final String ALI_PSID = "f1f2231e-43a9-11e8-8390-4ccc6a70ac67";
    /**
     * 微信支付appid
     */
    public static final String WX_APP_ID_MERCHANT = "wx9dd0426a34957774";

    /**
     * 高德地图调用WEB API KEY
     * 调用方法：https://lbs.amap.com/api/webservice/guide/api/georegeo
     * http://restapi.amap.com/v3/geocode/geo?address=&output=JSON&key=
     */
    public static final String AMAP_WEB_KEY = "5171da4ec1beb220053af33e742f7ea0";
    /**
     * 高德WEB 地理编码API--通过地址获取经纬度接口
     */
    public static final String getLonLat = "http://restapi.amap.com/v3/geocode/geo";
    //---------------------------------阿里云相关配置----------------------------------------
    /**
     * 图片根链接
     */
    public static final String PIC_BASE_URL = "https://hsmcommon.oss-cn-beijing.aliyuncs.com/";
    /**
     * 店铺图片路径
     */
    public static final String STORE_PIC = "store/";
    /**
     * 商品图片路径
     */
    public static final String PRODUCT_PIC = "product/";
    /**
     * 商品图片详情图片路径
     */
    public static final String PRODUCT_HTML_PIC = "product/detail/";
    /**
     * 商品类别图片路径
     */
    public static final String PRODUCT_TYPE_PIC = "product/type/";
    /**
     * 需求相关图片路径
     */
    public static final String REQUIREMENT_PIC = "requirement/";
    /**
     * 通用图片路径（支付方式图片）
     */
    public static final String COMMON_PIC = "common/";
    /**
     * 用户头像路径
     */
    public static final String PROFILE_PIC = "profilephoto/";
    /**
     * 主页面轮播图片路径
     */
    public static final String SLIDE_PIC = "slide/";
    /**
     * 引导页图片路径
     */
    public static final String GUIDE_PIC = "guide/";
    /**
     * 评价图片路径
     */
    public static final String EVALUATE_PIC = "evaluate/";

    public static final String endpoint = "oss-cn-beijing.aliyuncs.com";
    public static final String accessKeyId = "LTAIFYiCJtna4hE0";
    public static final String accessKeySecret = "Tq24v1zG0SAg2l09sumIa5LVQUzr4K";
    public static final String bucketName = "hsmcommon";

    /**
     * API 基础地址
     * <p>
     * 注意修改uikit中的基础地址！！！！！
     */
    public static final String API_HOST_JAVA = "http://123.56.218.108:8080/HomeShoppingMall/";
//    public static final String API_HOST_JAVA = "http://192.168.0.113:8080/HomeShoppingMall/";
    /**
     * 相机
     */
    public static final int TAKE_CAMERA = 0x000001;
    /**
     * 相册
     */
    public static final int TAKE_PHOTO = 0x000002;
    /**
     * 返回
     */
    public static final int REQUEST_CODE = 0x00000011;

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
     * 获取引导页图片接口
     */
    public static final String getGuidePic = API_HOST_JAVA + "getGuidePic";
    /**
     * 获取行政区划信息接口
     */
    public static final String getAreaCode = API_HOST_JAVA + "getAreaCode";
    /**
     * 获取短信验证码接口
     */
    public static final String getCaptcha = API_HOST_JAVA + "getCaptcha";
    /**
     * 个人身份商户注册接口
     */
    public static final String storePersonRegistered = API_HOST_JAVA + "storePersonRegistered";
    /**
     * 企业身份商户注册接口
     */
    public static final String storeCompanyRegistered = API_HOST_JAVA + "storeCompanyRegistered";
    /**
     * 获取开通服务的城市列表接口
     */
    public static final String getCityList = API_HOST_JAVA + "getCityList";
    /**
     * 商户登录接口
     */
    public static final String storeLogin = API_HOST_JAVA + "storeLogin";
    /**
     * 是否设置过密保问题接口
     */
    public static final String msGetStatusOfSafetyQuestions = API_HOST_JAVA + "msGetStatusOfSafetyQuestions";
    /**
     * 更新用户设备ID并返回用户信息接口
     */
    public static final String updateUserPhoneIdAndReturnUserInfo = API_HOST_JAVA + "updateUserPhoneIdAndReturnUserInfo";
    /**
     * 获取某用户已设置的安全问题接口
     */
    public static final String msGetSafetyQuestionOfUser = API_HOST_JAVA + "msGetSafetyQuestionOfUser";
    /**
     * 校验某用户已设置的安全问题接口
     */
    public static final String msCheckSafetyQuestionOfUser = API_HOST_JAVA + "msCheckSafetyQuestionOfUser";
    /**
     * 找回密码接口
     */
    public static final String retrievePsw = API_HOST_JAVA + "retrievePsw";
    /**
     * 获取个人商户主界面相关数据接口
     */
    public static final String personStoreMainInterface = API_HOST_JAVA + "personStoreMainInterface";
    /**
     * 获取企业商户主界面相关数据接口
     */
    public static final String companyStoreMainInterface = API_HOST_JAVA + "companyStoreMainInterface";
    /**
     * 分页获取满足条件的需求列表接口
     */
    public static final String getAllRequirement = API_HOST_JAVA + "getAllRequirement";
    /**
     * 获取商户个人、店铺相关信息接口
     */
    public static final String getStoreUserInfo = API_HOST_JAVA + "getStoreUserInfo";
    /**
     * 修改商户个人、店铺相关信息接口
     */
    public static final String changeStoreUserInfo = API_HOST_JAVA + "changeStoreUserInfo";
    /**
     * 修改商户身份用户密码接口
     */
    public static final String updateStoreUserPsw = API_HOST_JAVA + "updateStoreUserPsw";
    /**
     * 获取用户安全问题接口
     */
    public static final String getSafetyQuestion = API_HOST_JAVA + "getSafetyQuestion";
    /**
     * 设置账户密保接口
     */
    public static final String updateUserSecretQuestion = API_HOST_JAVA + "updateUserSecretQuestion";
    /**
     * 意见反馈接口
     */
    public static final String addUserFeedBack = API_HOST_JAVA + "addUserFeedBack";
    /**
     * 检查更新接口
     */
    public static final String checkUpdate = API_HOST_JAVA + "checkUpdate";
    /**
     * 提交商户所在位置经纬度接口（一分钟提交一次）
     */
    public static final String submitMerchantLonlat = API_HOST_JAVA + "submitMerchantLonlat";
    /**
     * 获取某商户针对于特定需求订单的移动轨迹列表
     */
    public static final String getMerchantLonLatList = API_HOST_JAVA + "getMerchantLonLatList";
    /**
     * 获取商户我的安排列表接口
     */
    public static final String msGetMyArrangeListBySId = API_HOST_JAVA + "msGetMyArrangeListBySId";
    /**
     * 商户添加我的安排接口
     */
    public static final String msAddMyArrange = API_HOST_JAVA + "msAddMyArrange";
    /**
     * 获取某商户的钱包相关数据接口
     */
    public static final String msGetMyWallet = API_HOST_JAVA + "msGetMyWallet";
    /**
     * 获取支付订单号接口
     */
    public static final String getOrderPayCode = API_HOST_JAVA + "getOrderPayCode";
    /**
     * 获取支付方式接口
     */
    public static final String getPayStyle = API_HOST_JAVA + "getPayStyle";
    /**
     * 微信支付统一下单接口
     */
    public static final String wxPayUnifiedOrder = API_HOST_JAVA + "wxPayUnifiedOrder";
    /**
     * 查询微信订单支付结果接口
     */
    public static final String wxPayOrderQuery = API_HOST_JAVA + "wxPayOrderQuery";
    /**
     * 获取所有商品订单接口
     */
    public static final String getStoreProductOrder = API_HOST_JAVA + "getStoreProductOrder";
    /**
     * 获取我的需求接口
     */
    public static final String getStoreRequirement = API_HOST_JAVA + "getStoreRequirement";
    /**
     * 获取店铺详情接口
     */
    public static final String getStoreDetail = API_HOST_JAVA + "getStoreDetail";
    /**
     * 店铺轮播图接口
     */
    public static final String getStoreSlideShow = API_HOST_JAVA + "getStoreSlideShow";
    /**
     * 获取订单详情接口
     */
    public static final String getStoreProductOrderDetail = API_HOST_JAVA + "getStoreProductOrderDetail";
    /**
     * 获取推荐商品接口
     */
    public static final String getRecommendProductList = API_HOST_JAVA + "getRecommendProductList";
    /**
     * 获取商品详情接口
     */
    public static final String getProductInfo = API_HOST_JAVA + "getProductInfo";
    /**
     * 获取商品评价接口
     */
    public static final String getProductEvaluate = API_HOST_JAVA + "getProductEvaluate";
    /**
     * 需求详情
     */
    public static final String getStoreRequirementDetail = API_HOST_JAVA + "getStoreRequirementDetail";
    /**
     * 删除需求
     */
    public static final String delUserRequirement = API_HOST_JAVA + "delUserRequirement";
    /**
     * 修改商户我的安排接口
     */
    public static final String updateMyArrange = API_HOST_JAVA + "updateMyArrange";
    /**
     * 删除安排
     */
    public static final String delStoreArrange = API_HOST_JAVA + "delStoreArrange";
    /**
     * 确认商品退款
     */
    public static final String affirmProductRefund = API_HOST_JAVA + "affirmProductRefund";
    /**
     * 申请接单
     */
    public static final String getStoreApplicationRequirement = API_HOST_JAVA + "getStoreApplicationRequirement";
    /**
     * 删除商品
     */
    public static final String deleteStoreProduct = API_HOST_JAVA + "deleteStoreProduct";
    /**
     * 通知验货
     */
    public static final String informInspection = API_HOST_JAVA + "informInspection";
    /**
     * 申请提现
     */
    public static final String getAlreadyCash = API_HOST_JAVA + "getAlreadyCash";
    /**
     * 提现记录
     */
    public static final String getAllAlreadyCashRecord = API_HOST_JAVA + "getAllAlreadyCashRecord";
    /**
     * 申请解冻保障金
     */
    public static final String getThawDepositMoney = API_HOST_JAVA + "getThawDepositMoney";
    /**
     * 去发货
     */
    public static final String merchantSend = API_HOST_JAVA + "merchantSend";
    /**
     * 删除商品订单接口
     */
    public static final String delStoreProductOrder = API_HOST_JAVA + "delStoreProductOrder";
    /**
     * 支付宝支付统一下单接口
     */
    public static final String ailPayUnifiedOrder = API_HOST_JAVA + "ailPayUnifiedOrder";
    /**
     * 查询支付宝订单支付结果接口
     */
    public static final String aliPayOrderQuery = API_HOST_JAVA + "aliPayOrderQuery";
    /**
     * 支付宝确认退款
     */
    public static final String ailTradeRefund = API_HOST_JAVA + "ailTradeRefund";
    /**
     * 微信确认退款
     */
    public static final String wxTradeRefund = API_HOST_JAVA + "wxTradeRefund";
}
