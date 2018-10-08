package app.cn.extra.mall.user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.AllEvent;
import app.cn.extra.mall.user.event.CommodityBuyEvent;
import app.cn.extra.mall.user.event.PendingPaymentEvent;
import app.cn.extra.mall.user.event.TrolleyEvent;
import app.cn.extra.mall.user.fragment.PayKeyboardFragment;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.MD5Utils;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.utils.Util;
import app.cn.extra.mall.user.utils.aliPay.OrderInfoUtil2_0;
import app.cn.extra.mall.user.utils.aliPay.PayResult;
import app.cn.extra.mall.user.view.NewsListView;
import app.cn.extra.mall.user.view.PayKeyboardView;
import app.cn.extra.mall.user.view.ViewHolder;
import app.cn.extra.mall.user.vo.AilPayQueryProductOrder;
import app.cn.extra.mall.user.vo.AliPayUnifiedOrder;
import app.cn.extra.mall.user.vo.AliPaybean;
import app.cn.extra.mall.user.vo.DoAddProductOrder;
import app.cn.extra.mall.user.vo.DoAddProductOrderByTrolley;
import app.cn.extra.mall.user.vo.DoGetMyReceiverAddress;
import app.cn.extra.mall.user.vo.GetCustomer;
import app.cn.extra.mall.user.vo.GetOrderData;
import app.cn.extra.mall.user.vo.GetPayStyle;
import app.cn.extra.mall.user.vo.GetProductPayResult;
import app.cn.extra.mall.user.vo.JudgePaymentPasswords;
import app.cn.extra.mall.user.vo.ShoppingCarProduct;
import app.cn.extra.mall.user.vo.WxPayOrder;
import app.cn.extra.mall.user.vo.WxPayUnifiedOrder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

import static app.cn.extra.mall.user.utils.Constants.RSA2_PRIVATE;

/**
 * 支付界面
 */
public class CommodityBuyActivity extends BaseActivty implements PayKeyboardView.OnKeyboardListener {
    @BindView(R.id.ib_back)
    ImageView ibBack;
    @BindView(R.id.tv_address_name)
    TextView tvAddressName;
    @BindView(R.id.tv_address_phone)
    TextView tvAddressPhone;
    @BindView(R.id.tv_address_detail)
    TextView tvAddressDetail;
    @BindView(R.id.tv_address_title)
    TextView tvAddressTitle;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.tv_item_address_edit)
    TextView tvItemAddressEdit;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.rl_prompt)
    RelativeLayout rlPrompt;
    @BindView(R.id.ll_address_adres)
    LinearLayout llAddressAdres;
    private SharePreferenceUtil mSharePreferenceUtil;
    private List<DoGetMyReceiverAddress.DataBean.UserDeliverAddressListBean> addressData;
    @BindView(R.id.ck_item_address_default)
    CheckBox ckItemAddressDefault;
    @BindView(R.id.lv_shop)
    NewsListView lvShop;
    private String recAdrId;
    @BindView(R.id.iv_address_pic)
    ImageView ivAddressPic;
    private String cid;
    private List<ShoppingCarProduct> goodsDetail;
    private GoodsListAdapter adapter;
    private int state;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private int Goodsize = 0;
    private double prices = 0.00;
    private List<Map<String, String>> list = new ArrayList<>();
    private HashMap<String, List<Map<String, String>>> goods = new HashMap<>();
    @BindView(R.id.lv_pay_type)
    NewsListView lvPayType;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private int index = 100;
    private String psId = "", psName = "";
    private PayTypeAdapter payAdapter;
    private GetPayStyle.DataBean payDatas;
    private List<String> poid = new ArrayList<>();
    private int CCCODE = 100;
    private String ccId = "";
    private String money;
    private int stateType;
    private String address;
    private String tel;
    private String name;
    private String APPID = Constants.APPID;
    private String RSA_PRIVATE = "";
    private static final int SDK_PAY_FLAG = 1;
    private int ADDRESS = 200;
    private WxPayOrder wxPayOrder;
    private String xmlInfo;
    private String goodsName = "";
    private IWXAPI api;
    private TreeMap<Object, Object> parameters;
    private String out_trade_no;
    private String trade_no;
    private Double payMoney;
    private Double OriginalPrice;
    PayKeyboardFragment dialogFrag;
    String sId = "";
    String intentTag = "";
    String allMoney = "";
    String poId = "";
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    AliPaybean aliPaybean = Utils.parserJsonResult(resultInfo, AliPaybean.class);
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        out_trade_no = aliPaybean.getAlipay_trade_app_pay_response().getOut_trade_no();
                        trade_no = aliPaybean.getAlipay_trade_app_pay_response().getTrade_no();

                        //阿里支付成功或失败
                        ailPayQueryProductOrderPort(trade_no);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        CustomToast.showToast(CommodityBuyActivity.this,
                                "支付失败", Toast.LENGTH_SHORT);
                        EventBus.getDefault().post(new TrolleyEvent());
                        finish();
                    }
                    break;
                }

                default:
                    break;
            }
        }


    };
    private String payCode;
    private GetCustomer.DataBean customerData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_buy);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Bundle bundle = getIntent().getExtras();
        state = bundle.getInt("state");
        stateType = bundle.getInt("stateType");
        address = bundle.getString("address");
        tel = bundle.getString("tel");
        name = bundle.getString("name");
        payCode = bundle.getString("payCode");
        goodsDetail = (List<ShoppingCarProduct>) bundle.getSerializable("goodsDetail");
        intentTag = bundle.getString("intentTag");
        allMoney = bundle.getString("totalMoney");
        poId = bundle.getString("poId");
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 消息处理
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void CommodityBuyEventBus(CommodityBuyEvent event) {
        if (event.getAddressTags() != null && "1".equals(event.getAddressTags())) {
            String addressName = event.getAddressName();
            String tags = event.getTags();
            String mAddress = event.getAddress();
            String addressTel = event.getAddressTel();
            String addressId = event.getAddressId();
            if ("0".equals(tags)) {
                name = addressName;
                tel = addressTel;
                address = mAddress;
                recAdrId = addressId;
                tvAddressName.setText("姓名 ：" + addressName);
                tvAddressPhone.setText("电话 ：" + addressTel);
                tvAddressDetail.setText("收货地址 ：" + mAddress);
                llAddressAdres.setVisibility(View.VISIBLE);
                ivAddressPic.setVisibility(View.GONE);
                recAdrId = addressId;
            } else {
                llAddressAdres.setVisibility(View.GONE);
                ivAddressPic.setVisibility(View.VISIBLE);
                tvAddressTitle.setText("添加收件人信息");
                recAdrId = "";
            }
        } else {
            EventBus.getDefault().post(new AllEvent());
            EventBus.getDefault().post(new PendingPaymentEvent());
            finish();
        }
    }

    private void initView() {
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        scrollView.smoothScrollTo(0, 20);
        mSharePreferenceUtil = new SharePreferenceUtil(CommodityBuyActivity.this, Constants.SAVE_USER);
        cid = mSharePreferenceUtil.getUID();
        adapter = new GoodsListAdapter(goodsDetail);
        lvShop.setAdapter(adapter);
        rlAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommodityBuyActivity.this, AddressActivity.class);
                intent.putExtra("addressTags", "1");
                startActivityForResult(intent, ADDRESS);
            }
        });
        for (int i = 0; i < goodsDetail.size(); i++) {
            Goodsize += goodsDetail.get(i).getNum();
            prices += goodsDetail.get(i).getNum() * Double.valueOf(goodsDetail.get(i).getSprice());
        }
        DecimalFormat df = new DecimalFormat("0.00");
        tvMoney.setText("合计：¥" + df.format(prices));
        tvPay.setText("结算" + "(" + Goodsize + ")");
        tvPay.setBackgroundColor(getResources().getColor(R.color.blue));
        payMoney = prices;
        for (int p = 0; p < goodsDetail.size(); p++) {
            goodsName += goodsDetail.get(p).getPro_name() + ",";
        }

        goodsName = goodsName.substring(0, goodsName.length() - 1);
        lvPayType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ("积分支付".equals(payDatas.getPayStyleList().get(i).getPsName())) {
                    prices = 0;
                    Goodsize = 0;
                    for (int k = 0; k < goodsDetail.size(); k++) {
                        Goodsize += goodsDetail.get(k).getNum();
                        prices += goodsDetail.get(k).getNum() * Double.valueOf(goodsDetail.get(k).getIntegralPrice());

                    }
                    DecimalFormat df = new DecimalFormat("0.00");
                    money = "";
                    ccId = "";
                    tvPay.setText("结算" + "(" + Goodsize + ")");
                    tvMoney.setText("合计：¥" + df.format(prices));
                    payMoney = prices;
                    OriginalPrice = payMoney;
                } else {
                    prices = 0;
                    Goodsize = 0;
                    for (int j = 0; j < goodsDetail.size(); j++) {
                        Goodsize += goodsDetail.get(j).getNum();
                        prices += goodsDetail.get(j).getNum() * Double.valueOf(goodsDetail.get(j).getSprice());
                    }
                    DecimalFormat df = new DecimalFormat("0.00");
                    money = "";
                    ccId = "";
                    tvMoney.setText("合计：¥" + df.format(prices));
                    payMoney = prices;
                    OriginalPrice = payMoney;
                }
                index = i;
                payAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {
        api = WXAPIFactory.createWXAPI(CommodityBuyActivity.this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        if (stateType != 1) {
            //获取用户收货地址
            doGetMyReceiverAddressPort();
        } else {
            ivAddressPic.setVisibility(View.GONE);
            tvAddressName.setText("姓名 ：" + name);
            tvAddressPhone.setText("电话 ：" + tel);
            tvAddressDetail.setText("收货地址 ：" + address);
            ckItemAddressDefault.setChecked(true);
        }
        //获取支付方式
        getPayStylePort();

    }

    @OnClick({R.id.ib_back, R.id.tv_pay, R.id.ll_address_adres, R.id.tv_item_address_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_pay: //支付
                String address = tvAddressDetail.getText().toString();
                String ip = Util.getIp(CommodityBuyActivity.this);
                String nonce_str = Util.getNonce_str();
                SpliceSign(ip, nonce_str, payCode, goodsName);
                if (!TextUtils.isEmpty(psId)) {
                    if (!TextUtils.isEmpty(address)) {
                        //获取订单号
                        doAddProductOrderPort();
                    } else {
                        showLongToast(CommodityBuyActivity.this, "请填写您的收货信息！");
                    }
                } else {
                    showLongToast(CommodityBuyActivity.this, "请先选择支付方式！");
                }

                break;
//            case R.id.rl_discount_coupon: //优惠券
//                if (!TextUtils.isEmpty(psId)) {
//                    payMoney = OriginalPrice;
//                    Intent coupon = new Intent();
//                    coupon.setClass(CommodityBuyActivity.this, UsableCouponActivity.class);
//                    coupon.putExtra("name", "便捷购物");
//                    coupon.putExtra("money", OriginalPrice);
//                    coupon.putExtra("type", 0);
//                    startActivityForResult(coupon, CCCODE);
//                    startActivity(coupon);
//                } else {
//                    showLongToast(CommodityBuyActivity.this, "请先选择支付方式！");
//                }
//                break;
            case R.id.ll_address_adres:
                Intent intent = new Intent(CommodityBuyActivity.this, AddressActivity.class);
                intent.putExtra("addressTags", "1");
                startActivity(intent);
                break;
            case R.id.tv_item_address_edit:
                Intent edit = new Intent(CommodityBuyActivity.this, AddressActivity.class);
                edit.putExtra("addressTags", "1");
                startActivityForResult(edit, ADDRESS);
                break;
            default:
                break;
        }
    }

    /**
     * 支付宝支付成功后的回调
     */
    private void ailPayQueryProductOrderPort(String trade_no) {
        if (Utils.isNetworkAvailable(CommodityBuyActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.aliPayOrderQuery)
                    .addParams("uId", mSharePreferenceUtil.getUID())
                    .addParams("payCode", payCode)
                    .addParams("tradeNo", trade_no)
                    .addParams("totalMoney", "" + payMoney)
                    /**
                     * orderType--订单类型0--商品订单，1--需求订单，2--缴纳保障金
                     */
                    .addParams("orderType", "0")
                    /**
                     * appType--平台类型0--用户端，1--商户端，2--WEB管理端
                     */
                    .addParams("appType", "0")
                    .addParams("orderData", "{}")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            AilPayQueryProductOrder ailPayQueryProductOrder = new AilPayQueryProductOrder();
                            ailPayQueryProductOrder = Utils.parserJsonResult(response, AilPayQueryProductOrder.class);
                            if ("true".equals(ailPayQueryProductOrder.getFlag())) {
                                if ("true".equals(ailPayQueryProductOrder.getData().getStatus())) {
                                    Intent intent = new Intent();
                                    intent.setClass(CommodityBuyActivity.this, PaymentSuccessActivity.class);
                                    intent.putExtra("tags", "1");
                                    startActivity(intent);
                                } else {
                                    showLongToast(CommodityBuyActivity.this, ailPayQueryProductOrder.getData().getErrorString());
                                }
                            } else {
                                showLongToast(CommodityBuyActivity.this, ailPayQueryProductOrder.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(CommodityBuyActivity.this, Constants.NETWORK_ERROR);

        }
    }

    /**
     * 判断是否设置支付密码
     */
    private void getCustomerPort() {
        String cid = mSharePreferenceUtil.getUID();
        if (Utils.isNetworkAvailable(CommodityBuyActivity.this)) {
            OkHttpUtils
                    .post()
                    .url("")
                    .addParams("cId", cid)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            GetCustomer getCustomer = new GetCustomer();
                            getCustomer = Utils.parserJsonResult(response, GetCustomer.class);
                            if ("true".equals(getCustomer.getFlag())) {
                                customerData = getCustomer.getData();
                                if (customerData.getPayPassword().equals("true")) {
                                    if (dialogFrag == null) {
                                        dialogFrag = new PayKeyboardFragment();
                                        dialogFrag.setTitle("请输入支付密码");
                                        dialogFrag.setOnKeyboardListener(CommodityBuyActivity.this);
                                    }
                                    dialogFrag.show(getFragmentManager(), "payKeyboardDialog");
                                } else {
                                    Intent payPassword = new Intent(CommodityBuyActivity.this, PayPasswordActivity.class);
                                    payPassword.putExtra("BindTel", customerData.getBindTel());
                                    startActivity(payPassword);
                                    showLongToast(CommodityBuyActivity.this, "请先设置支付密码！");
                                }
                            } else {

                                showLongToast(CommodityBuyActivity.this, getCustomer.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(CommodityBuyActivity.this, Constants.NETWORK_ERROR);

        }
    }

    @Override
    public void onComplete(String one, String two, String three, String four, String five, String six) {
        String payPassword = one + two + three + four + five + six;
        //判断支付密码是否正确
        JudgePaymentPasswordPort(payPassword);
    }

    /**
     * 判断支付密码是否正确
     *
     * @param payPassword
     */
    private void JudgePaymentPasswordPort(String payPassword) {
        String cid = mSharePreferenceUtil.getUID();
        if (Utils.isNetworkAvailable(CommodityBuyActivity.this)) {
            OkHttpUtils
                    .post()
                    .url("")
                    .addParams("cId", cid)
                    .addParams("payPassword", payPassword)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            JudgePaymentPasswords paymentPasswords = new JudgePaymentPasswords();
                            paymentPasswords = Utils.parserJsonResult(response, JudgePaymentPasswords.class);
                            if ("true".equals(paymentPasswords.getFlag())) {
                                if (paymentPasswords.getData().getStatus().equals("true")) {
                                    getProductPayResultPort();
                                } else {
                                    showLongToast(CommodityBuyActivity.this, paymentPasswords.getData()
                                            .getErrorString());

                                }
                            } else {
                                showLongToast(CommodityBuyActivity.this, paymentPasswords.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(CommodityBuyActivity.this, Constants.NETWORK_ERROR);

        }
    }

    @Override
    public void onBack() {
        if (dialogFrag != null) {
            dialogFrag.dismiss();
        }
    }


    /**
     * 微信支付
     */
    private void toWXPay() {
        OkHttpUtils.postString().content(xmlInfo).url(Constants.wxUrl).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("onError", e + "");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        String prepay_id = "";
                        String nonce_str = "";
                        //我到现在都不明白服务器返回给我这个值有毛用
                        String sign = "";
                        XmlPullParser parser = Xml.newPullParser();
                        StringReader stringReader = new StringReader(response);
                        try {
                            parser.setInput(stringReader);

                            int eventType = parser.getEventType();

                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                String nodeName = parser.getName();

                                switch (eventType) {
                                    case XmlPullParser.START_TAG:
                                        if ("prepay_id".equals(nodeName)) {
                                            prepay_id = parser.nextText();
                                        } else if ("nonce_str".equals(nodeName)) {
                                            nonce_str = parser.nextText();
                                        } else if ("sign".equals(nodeName)) {
                                            sign = parser.nextText();
                                        }
                                        break;
                                    default:
                                }
                                eventType = parser.next();

                            }

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

//                        mSharePreferenceUtil.setOrderId(payCode);
//                        mSharePreferenceUtil.setPayType("1");
//                        mSharePreferenceUtil.setCcId(ccId);

                        api = WXAPIFactory.createWXAPI(CommodityBuyActivity.this, Constants.APP_ID);
                        api.registerApp(Constants.APP_ID);
                        stringReader.close();
                        PayReq payRequest = new PayReq();
                        SortedMap<Object, Object> params = new TreeMap<Object, Object>();
                        payRequest.appId = Constants.APP_ID;
                        payRequest.partnerId = Constants.WEIXIN_PARTERID;
                        payRequest.prepayId = prepay_id;
                        payRequest.packageValue = "Sign=WXPay";
                        payRequest.nonceStr = nonce_str;
                        payRequest.timeStamp = System.currentTimeMillis() / 1000 + "";
                        params.put("appid", Constants.APP_ID);
                        params.put("noncestr", nonce_str);
                        params.put("package", payRequest.packageValue);
                        params.put("partnerid", Constants.WEIXIN_PARTERID);
                        params.put("prepayid", prepay_id);
                        params.put("timestamp", payRequest.timeStamp);
                        sign = createSign(params);
                        payRequest.sign = sign;
                        api.sendReq(payRequest);
                        finish();
                    }

                });

    }

    /**
     * 支付宝支付
     */
    private void aliPay() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            CustomToast.showToast(CommodityBuyActivity.this,
                    "调起支付宝支付失败！", Toast.LENGTH_SHORT);
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */

        String pay = payCode;
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
//        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2,pay,prices,GoodsNames);

        Map<String, String> params = null;
        if (30 < goodsName.length()) {
            params = OrderInfoUtil2_0.buildOrderParamMap(APPID,
                    rsa2, pay, payMoney, goodsName.substring(0, 30));
        } else {
            params = OrderInfoUtil2_0.buildOrderParamMap(APPID,
                    rsa2, pay, payMoney, goodsName);
        }

        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(CommodityBuyActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public String createSign(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + Constants.WEIXIN_PARTERKEY);
        String sign = MD5Utils.getMd5Value(sb.toString()).toUpperCase();

        return sign;
    }


    /**
     * 获取支付方式
     */
    private void getPayStylePort() {
        if (Utils.isNetworkAvailable(CommodityBuyActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getPayStyle)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            GetPayStyle getPayStyle = new GetPayStyle();
                            getPayStyle = Utils.parserJsonResult(response, GetPayStyle.class);
                            if ("true".equals(getPayStyle.getFlag())) {
                                payDatas = getPayStyle.getData();
                                payAdapter = new PayTypeAdapter(payDatas);
                                lvPayType.setAdapter(payAdapter);
                            } else {
                                showLongToast(CommodityBuyActivity.this, getPayStyle.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(CommodityBuyActivity.this, Constants.NETWORK_ERROR);

        }
    }

    /**
     * 立即购买 获取订单号
     */
    private void doAddProductOrderPort() {
        if (Utils.isNetworkAvailable(CommodityBuyActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getOrderPayCode)
                    .addParams("uId", cid)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            DoAddProductOrder productOrder = new DoAddProductOrder();
                            productOrder = Utils.parserJsonResult(response, DoAddProductOrder.class);
                            if ("true".equals(productOrder.getFlag())) {
                                if (Constants.OK.equals(productOrder.getData().getStatus())) {
                                    payCode = productOrder.getData().getPayCode();
                                    poid.clear();
                                    poid.add(productOrder.getData().getPoId());
                                    if (Constants.WX_PAY.equals(payDatas.getPayStyleList().get(index).getPsName())) {
                                        if ("1".equals(intentTag)) {
                                            wxProductUnPayUnifiedOrder();
                                        } else {
                                            wxPayUnifiedOrderPort();
                                        }
                                    } else if (Constants.ALI_PAY.equals(payDatas.getPayStyleList().get(index).getPsName())) {
                                        if ("1".equals(intentTag)) {
                                            ailProductUnPayUnifiedOrder();
                                        } else {
                                            aliPayUnifiedOrderPort();
                                        }
                                    }
                                }
                            } else {
                                showLongToast(CommodityBuyActivity.this, productOrder.getErrorString());
                            }
                        }
                    });
        } else {

            showLongToast(CommodityBuyActivity.this, "订单生成失败！" + Constants.NETWORK_ERROR);

        }
    }

    /**
     * 按格式整理订单详情数据
     */
    private String getOrderData() {
        /**
         * 用来判断当前sId是否已添加到stores中
         */
        boolean hasFlag = false;
        GetOrderData getOrderData = new GetOrderData();
        List<GetOrderData.StoresBean> stores = new ArrayList<>();
        List<GetOrderData.StoresBean.ProductsBean> products = null;
        getOrderData.setPoDeliverAddress(address.trim());
        getOrderData.setPoDeliverName(name);
        getOrderData.setPoDeliverTel(tel);

        for (int i = 0; i < goodsDetail.size(); i++) {
            for (int m = 0; m < stores.size(); m++) {
                if (goodsDetail.get(i).getsId().equals(stores.get(m).getSId())) {
                    hasFlag = true;
                }
            }
            if (!hasFlag) {
                products = new ArrayList<>();
                GetOrderData.StoresBean storesBean = new GetOrderData.StoresBean();
                storesBean.setSId(goodsDetail.get(i).getsId());
                /**
                 * 循环判断商品属于哪个店铺并添加到对应店铺下
                 */
                for (int j = 0; j < goodsDetail.size(); j++) {
                    if (goodsDetail.get(i).getsId().equals(goodsDetail.get(j).getsId())) {
                        GetOrderData.StoresBean.ProductsBean productsBean = new GetOrderData.StoresBean.ProductsBean();
                        productsBean.setPId(goodsDetail.get(j).getPro_id());
                        productsBean.setPodNum("" + goodsDetail.get(j).getNum());
                        if (!TextUtils.isEmpty(goodsDetail.get(j).getSprice())) {
                            productsBean.setPodPrice(Double.valueOf(goodsDetail.get(j).getSprice()));
                        } else {
                            productsBean.setPodPrice(0.00);
                        }
                        productsBean.setPodProperty(goodsDetail.get(j).getProperty());
                        products.add(productsBean);
                    }
                }
                storesBean.setProducts(products);
                stores.add(storesBean);
            } else {
                hasFlag = false;
            }
        }

//        for (int i = 0; i < goodsDetail.size(); i++) {
//            GetOrderData.StoresBean.ProductsBean productsBean = new GetOrderData.StoresBean.ProductsBean();
//            productsBean.setPId(goodsDetail.get(i).getPro_id());
//            productsBean.setPodNum("" + goodsDetail.get(i).getNum());
//            productsBean.setPodPrice("" + goodsDetail.get(i).getPrice());
//            productsBean.setPodProperty(goodsDetail.get(i).getProperty());
//            products.add(productsBean);
//        }
//        GetOrderData.StoresBean storesBean = new GetOrderData.StoresBean();
//        storesBean.setSId(goodsDetail.get(0).getsId());
//        storesBean.setProducts(products);
//        stores.add(storesBean);
        getOrderData.setStores(stores);
        Gson gson = new Gson();
        System.out.println(gson.toJson(getOrderData));
        return gson.toJson(getOrderData);
    }

    /**
     * 支付宝待支付统一下单接口
     */
    private void ailProductUnPayUnifiedOrder() {
        if (Utils.isNetworkAvailable(CommodityBuyActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.ailProductUnPayUnifiedOrder)
                    .addParams("uId", mSharePreferenceUtil.getUID())
                    .addParams("poId", poId)
                    .addParams("payCode", payCode)
                    .addParams("totalMoney", "" + allMoney)
                    .addParams("orderData", "{\"poDeliverName\": " + "\"" + name + "\"" + ",\"poDeliverTel\": " + "\"" + tel + "\"" + ",\"poDeliverAddress\": " + "\"" + address + "\"" + ",\"stores\": " + "[]" + "}")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(CommodityBuyActivity.this, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            AliPayUnifiedOrder aliPayUnifiedOrder = new AliPayUnifiedOrder();
                            aliPayUnifiedOrder = Utils.parserJsonResult(response, AliPayUnifiedOrder.class);
                            if (Constants.OK.equals(aliPayUnifiedOrder.getFlag())) {
                                if (Constants.OK.equals(aliPayUnifiedOrder.getData().getStatus())) {
                                    toAliPay(aliPayUnifiedOrder.getData());
                                } else {
                                    showShortToast(CommodityBuyActivity.this,
                                            aliPayUnifiedOrder.getErrorString());
                                }
                            } else {
                                showShortToast(CommodityBuyActivity.this,
                                        aliPayUnifiedOrder.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(CommodityBuyActivity.this,
                    "支付失败！" + Constants.NETWORK_ERROR);
        }
    }

    /**
     * 支付宝支付统一下单接口
     */
    private void aliPayUnifiedOrderPort() {
        if (Utils.isNetworkAvailable(CommodityBuyActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.ailPayUnifiedOrder)
                    .addParams("uId", mSharePreferenceUtil.getUID())
                    .addParams("payCode", payCode)
                    .addParams("totalMoney", "" + payMoney)
                    /**
                     * orderType--订单类型0--商品订单，1--需求订单，2--缴纳保障金
                     */
                    .addParams("orderType", "0")
                    /**
                     * appType--平台类型0--用户端，1--商户端，2--WEB管理端
                     */
                    .addParams("appType", "0")
                    .addParams("orderData", getOrderData())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(CommodityBuyActivity.this, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            AliPayUnifiedOrder aliPayUnifiedOrder = new AliPayUnifiedOrder();
                            aliPayUnifiedOrder = Utils.parserJsonResult(response, AliPayUnifiedOrder.class);
                            if (Constants.OK.equals(aliPayUnifiedOrder.getFlag())) {
                                if (Constants.OK.equals(aliPayUnifiedOrder.getData().getStatus())) {
                                    toAliPay(aliPayUnifiedOrder.getData());
                                } else {
                                    showShortToast(CommodityBuyActivity.this,
                                            aliPayUnifiedOrder.getErrorString());
                                }
                            } else {
                                showShortToast(CommodityBuyActivity.this,
                                        aliPayUnifiedOrder.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(CommodityBuyActivity.this,
                    "支付失败！" + Constants.NETWORK_ERROR);
        }
    }

    /**
     * 微信待付款统一下单接口
     */
    private void wxProductUnPayUnifiedOrder() {
        if (Utils.isNetworkAvailable(CommodityBuyActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.wxProductUnPayUnifiedOrder)
                    .addParams("uId", mSharePreferenceUtil.getUID())
                    .addParams("poId", poId)
                    .addParams("payCode", payCode)
                    .addParams("totalMoney", "" + allMoney)
                    .addParams("orderData", "{\"poDeliverName\": " + "\"" + name + "\"" + ",\"poDeliverTel\": " + "\"" + tel + "\"" + ",\"poDeliverAddress\": " + "\"" + address + "\"" + ",\"stores\": " + "[]" + "}")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(CommodityBuyActivity.this, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            WxPayUnifiedOrder wxPayUnifiedOrder = new WxPayUnifiedOrder();
                            wxPayUnifiedOrder = Utils.parserJsonResult(response, WxPayUnifiedOrder.class);
                            if (Constants.OK.equals(wxPayUnifiedOrder.getFlag())) {
                                if (Constants.OK.equals(wxPayUnifiedOrder.getData().getStatus())) {
                                    toWxPay(wxPayUnifiedOrder.getData());
                                } else {
                                    showShortToast(CommodityBuyActivity.this,
                                            wxPayUnifiedOrder.getData().getErrorString());
                                }
                            } else {
                                showShortToast(CommodityBuyActivity.this,
                                        wxPayUnifiedOrder.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(CommodityBuyActivity.this,
                    "支付失败！" + Constants.NETWORK_ERROR);
        }
    }

    /**
     * 微信支付统一下单接口
     */
    private void wxPayUnifiedOrderPort() {
        if (Utils.isNetworkAvailable(CommodityBuyActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.wxPayUnifiedOrder)
                    .addParams("uId", mSharePreferenceUtil.getUID())
                    .addParams("payCode", payCode)
                    .addParams("totalMoney", "" + payMoney)
                    /**
                     * orderType--订单类型0--商品订单，1--需求订单，2--缴纳保障金
                     */
                    .addParams("orderType", "0")
                    /**
                     * appType--平台类型0--用户端，1--商户端，2--WEB管理端
                     */
                    .addParams("appType", "0")
                    .addParams("orderData", getOrderData())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(CommodityBuyActivity.this, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            WxPayUnifiedOrder wxPayUnifiedOrder = new WxPayUnifiedOrder();
                            wxPayUnifiedOrder = Utils.parserJsonResult(response, WxPayUnifiedOrder.class);
                            if (Constants.OK.equals(wxPayUnifiedOrder.getFlag())) {
                                if (Constants.OK.equals(wxPayUnifiedOrder.getData().getStatus())) {
                                    toWxPay(wxPayUnifiedOrder.getData());
                                } else {
                                    showShortToast(CommodityBuyActivity.this,
                                            wxPayUnifiedOrder.getData().getErrorString());
                                }
                            } else {
                                showShortToast(CommodityBuyActivity.this,
                                        wxPayUnifiedOrder.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(CommodityBuyActivity.this,
                    "支付失败！" + Constants.NETWORK_ERROR);
        }
    }

    private void toAliPay(AliPayUnifiedOrder.DataBean dataBean) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(CommodityBuyActivity.this);
                Map<String, String> result = alipay.payV2(dataBean.getOrderInfo(), true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 调起微信支付
     */
    private void toWxPay(WxPayUnifiedOrder.DataBean data) {
        PayReq request = new PayReq();
        request.appId = data.getAppId();
        request.partnerId = data.getPartnerId();
        request.prepayId = data.getPrepay_id();
        request.packageValue = data.getPackageValue();
        request.nonceStr = data.getNonceStr();
        request.timeStamp = data.getTimestamp();
        request.sign = data.getSign();
        Utils.LogE(request.sign);
        api.sendReq(request);
        /**
         * 临时存储订单相关信息，以便供查询方法调用
         */
        mSharePreferenceUtil.setPayCode(payCode);
        mSharePreferenceUtil.setTotalMoney("" + payMoney);
        mSharePreferenceUtil.setOrderType("0");
        mSharePreferenceUtil.setOrderData("{}");
    }

    /**
     * 获取用户收货地址
     */
    private void doGetMyReceiverAddressPort() {
        if (Utils.isNetworkAvailable(CommodityBuyActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getUserDeliverAddress)
                    .addParams("uId", cid)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            DoGetMyReceiverAddress receiverAddress = new DoGetMyReceiverAddress();
                            receiverAddress = Utils.parserJsonResult(response, DoGetMyReceiverAddress.class);
                            if ("true".equals(receiverAddress.getFlag())) {
                                addressData = receiverAddress.getData().getUserDeliverAddressList();
                                if (addressData.size() != 0) {
                                    for (int i = 0; i < addressData.size(); i++) {
                                        if (addressData.get(i).getUdaDefault() == 1) {
                                            ckItemAddressDefault.setChecked(true);
                                            if (!TextUtils.isEmpty(addressData.get(i).getUdaTrueName())) {
                                                if (6 < addressData.get(i).getUdaTrueName().length()) {
                                                    tvAddressName.setText("姓名 ：" + addressData.get(i).getUdaTrueName().substring(0, 6) + "···");
                                                } else {
                                                    tvAddressName.setText("姓名 ：" + addressData.get(i).getUdaTrueName());
                                                }
                                            } else {
                                                tvAddressName.setText("姓名 ：");
                                            }
                                            tvAddressPhone.setText("电话 ：" + addressData.get(i).getUdaTel());
                                            tvAddressDetail.setText("收货地址 ：" + addressData.get(i).getUdaAddress());
                                            name = addressData.get(i).getUdaTrueName();
                                            tel = addressData.get(i).getUdaTel();
                                            address = addressData.get(i).getUdaAddress();
                                            recAdrId = addressData.get(i).getUId();
                                            ivAddressPic.setVisibility(View.GONE);
                                            llAddressAdres.setVisibility(View.VISIBLE);
                                            rlPrompt.setVisibility(View.GONE);
//                                            if (state == 0) {
//                                                //立即购买 生成订单
//                                                doAddProductOrderPort();
//                                            } else {
//                                                //购物车 生成订单
//                                                doAddProductOrderByTrolleyPort(recAdrId);
//                                            }

                                        }
//                                        else {
//                                            ckItemAddressDefault.setChecked(true);
//                                            tvAddressName.setText("姓名 ：" + addressData.get(0).getUdaTrueName());
//                                            tvAddressPhone.setText("电话 ：" + addressData.get(0).getUdaTel());
//                                            tvAddressDetail.setText("收货地址 ：" + addressData.get(0).getUdaAddress());
//                                            name = addressData.get(0).getUdaTrueName();
//                                            tel = addressData.get(0).getUdaTel();
//                                            address = addressData.get(0).getUdaAddress();
//                                            recAdrId = addressData.get(0).getUId();
//                                            ivAddressPic.setVisibility(View.GONE);
//                                            llAddressAdres.setVisibility(View.VISIBLE);
//                                            rlPrompt.setVisibility(View.GONE);
//                                        }
                                    }
                                } else {
                                    tvAddressTitle.setText("添加收件人信息");
                                    ivAddressPic.setVisibility(View.VISIBLE);
                                    rlAddress.setBackgroundColor(Color.WHITE);
                                    llAddressAdres.setVisibility(View.GONE);
                                    rlPrompt.setVisibility(View.GONE);

                                }
                            } else {
                                showLongToast(CommodityBuyActivity.this, receiverAddress.getErrorString());
                            }
                        }
                    });
        } else {

            showLongToast(CommodityBuyActivity.this, Constants.NETWORK_ERROR);

        }
    }

    /**
     * 购物车生成订单
     *
     * @param recAdrId
     */
    private void doAddProductOrderByTrolleyPort(String recAdrId) {
        for (int i = 0; i < goodsDetail.size(); i++) {
            Map<String, String> datas = new HashMap<>();
            datas.put("pNum", goodsDetail.get(i).getNum() + "");
            datas.put("ctId", goodsDetail.get(i).getPro_ctid());
            datas.put("pId", goodsDetail.get(i).getPro_id());
            if (TextUtils.isEmpty(goodsDetail.get(i).getProperty())) {
                datas.put("pProperty", "");
            } else {
                datas.put("pProperty", goodsDetail.get(i).getProperty());
            }
            list.add(datas);
        }
        goods.put("parameterData", list);
        Gson gson = new Gson();
        String parameterData = gson.toJson(goods);

        if (Utils.isNetworkAvailable(CommodityBuyActivity.this)) {
            OkHttpUtils
                    .post()
                    .url("")
                    .addParams("cId", cid)
                    .addParams("rId", recAdrId)
                    .addParams("parameterData", parameterData)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            DoAddProductOrderByTrolley byTrolley = new DoAddProductOrderByTrolley();
                            byTrolley = Utils.parserJsonResult(response, DoAddProductOrderByTrolley.class);
                            if ("true".equals(byTrolley.getFlag())) {
                                if (Constants.OK.equals(byTrolley.getData().getStatus())) {
                                    poid.clear();
                                    for (int i = 0; i < byTrolley.getData().getPoIdList().size(); i++) {
                                        poid.add(byTrolley.getData().getPoIdList().get(i).getPoId());
                                    }
                                    payCode = byTrolley.getData().getPayCode();
                                }
                            } else {
                                showLongToast(CommodityBuyActivity.this, byTrolley.getErrorString());
                            }
                        }
                    });
        } else {

            showLongToast(CommodityBuyActivity.this, Constants.NETWORK_ERROR);

        }
    }

    private void SpliceSign(String ip, String nonce_str, String orderData, String GoodsNames) {
        int allprices = (int) (payMoney * 100);
        wxPayOrder = new WxPayOrder();
        parameters = new TreeMap<Object, Object>();
        //应用签名
        parameters.put("appid", Constants.APP_ID);
        wxPayOrder.setAppid(Constants.APP_ID);
        //商品信息
        if (30 < GoodsNames.length()) {
            parameters.put("body", GoodsNames.substring(0, 30));
            wxPayOrder.setBody(GoodsNames.substring(0, 30));
        } else {
            parameters.put("body", GoodsNames);
            wxPayOrder.setBody(GoodsNames);
        }
        //收款id
        parameters.put("mch_id", Constants.WEIXIN_PARTERID);
        wxPayOrder.setMch_id(Constants.WEIXIN_PARTERID);
        //随机字符串
        parameters.put("nonce_str", nonce_str);
        wxPayOrder.setNonce_str(nonce_str);
        //回调地址
        parameters.put("notify_url", "https://www.baidu.com");
        wxPayOrder.setNotify_url("https://www.baidu.com");
        //订单号
        parameters.put("out_trade_no", orderData);
        wxPayOrder.setOut_trade_no(orderData);
        //IP地址
        parameters.put("spbill_create_ip", ip);
        wxPayOrder.setSpbill_create_ip(ip);
        //金额
        parameters.put("total_fee", allprices);
        wxPayOrder.setTotal_fee(allprices + "");
//        parameters.put("total_fee", 1);
//        wxPayOrder.setTotal_fee(1+"");
        //支付类型
        parameters.put("trade_type", "APP");
        wxPayOrder.setTrade_type("APP");
        //这里就是用上面的方法生成的sign值了
        String sign = createSign(parameters);
        wxPayOrder.setSign(sign);
        xmlInfo = Util.xmlInfo(wxPayOrder);
        xmlInfo.toString();

    }

    /**
     * 积分支付
     */
    private void getProductPayResultPort() {
        String cid = mSharePreferenceUtil.getUID();
        Gson gson = new Gson();
        if (stateType == 1) {
            poid.add(goodsDetail.get(0).getPro_id());
        }
        String poId = gson.toJson(poid);
        if (Utils.isNetworkAvailable(CommodityBuyActivity.this)) {
            OkHttpUtils
                    .post()
                    .url("")
                    .addParams("poId", poId)
                    .addParams("cId", cid)
                    .addParams("totalMoney", payMoney + "")
                    .addParams("psId", "edc3d0d7-59a8-11e8-8b6a-00163e2e094a")
                    .addParams("ccId", ccId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            GetProductPayResult productPayResult = new GetProductPayResult();
                            productPayResult = Utils.parserJsonResult(response, GetProductPayResult.class);
                            if ("true".equals(productPayResult.getFlag())) {
                                if ("true".equals(productPayResult.getData().getStatus())) {
                                    Intent intent = new Intent();
                                    intent.setClass(CommodityBuyActivity.this, PaymentSuccessActivity.class);
                                    intent.putExtra("tags", "1");
                                    startActivity(intent);
                                    if (dialogFrag != null) {
                                        dialogFrag.dismiss();
                                    }
                                    finish();
                                } else {
                                    showLongToast(CommodityBuyActivity.this, productPayResult.getData()
                                            .getErrorString());
                                }

                                if (state == 2) {
//                                    Intent broad = new Intent("android.intent.action.CART_BROADCAST");
//                                    broad.putExtra("data","refresh");
//                                    LocalBroadcastManager.getInstance(CommodityBuyActivity.this).sendBroadcast(broad);
//                                    sendBroadcast(broad);
                                    finish();
                                }

                            } else {
                                showLongToast(CommodityBuyActivity.this, productPayResult.getErrorString());

                            }
                        }
                    });
        } else {

            showLongToast(CommodityBuyActivity.this, Constants.NETWORK_ERROR);

        }
    }


    private class PayTypeAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private final GetPayStyle.DataBean datas;

        private PayTypeAdapter(GetPayStyle.DataBean payDatas) {
            this.mInflater = LayoutInflater.from(CommodityBuyActivity.this);
            this.datas = payDatas;
        }

        @Override
        public int getCount() {
            return datas.getPayStyleList().size();
        }

        @Override
        public Object getItem(int i) {
            return datas.getPayStyleList().get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final ViewHolder holder;
            if (view == null) {
                view = mInflater.inflate(R.layout.item_pay_type, null);
                holder = new ViewHolder();
                holder.iv_item_pay_pic = view.findViewById(R.id.iv_item_pay_pic);
                holder.tv_item_pay_name = view.findViewById(R.id.tv_item_pay_name);
                holder.ck_item_pay_type = view.findViewById(R.id.ck_item_pay_type);
                holder.rl_item_pay_type = view.findViewById(R.id.rl_item_pay_type);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
//            Picasso.with(CarRentalPaymentActivity.this).load().into(holder.iv_item_pay_pic);
            holder.tv_item_pay_name.setText(datas.getPayStyleList().get(i).getPsName());
            if (index == i) {
                holder.ck_item_pay_type.setChecked(true);
                psId = datas.getPayStyleList().get(i).getPsId();
                psName = datas.getPayStyleList().get(i).getPsName();
            } else {
                holder.ck_item_pay_type.setChecked(false);
            }
            return view;
        }
    }

    private class GoodsListAdapter extends BaseAdapter {

        private final LayoutInflater mInflater;
        private final List<ShoppingCarProduct> datas;

        public GoodsListAdapter(List<ShoppingCarProduct> goodsDetail) {
            this.mInflater = LayoutInflater.from(CommodityBuyActivity.this);
            this.datas = goodsDetail;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            return datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                view = mInflater.inflate(R.layout.item_goods_detail, null);
                holder = new ViewHolder();
                holder.img_item_shop_pic = view.findViewById(R.id.item_iv_goods_pic);
                holder.item_tv_goods_name = view.findViewById(R.id.item_tv_goods_name);
                holder.item_tv_goods_price = view.findViewById(R.id.item_tv_goods_price);
                holder.item_tv_goods_introduce = view.findViewById(R.id.item_tv_goods_introduce);
                holder.item_iv_goods_nums = view.findViewById(R.id.item_iv_goods_nums);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }


//            DecimalFormat df = new DecimalFormat("0.00");
//            String price = df.format(datas.get(i).getSprice());
            holder.item_tv_goods_price.setText("¥" + datas.get(i).getSprice());

            holder.item_tv_goods_name.setText(datas.get(i).getPro_name());
            holder.item_tv_goods_introduce.setText(datas.get(i).getProperty());
            RequestOptions requestOptions = new RequestOptions()
                    .error(R.drawable.ic_exception)
                    .fallback(R.drawable.ic_exception);
            if (state == 2) {
                Glide.with(CommodityBuyActivity.this)
                        .load(datas.get(i).getPic_url() + "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(CommodityBuyActivity.this)))
                        .apply(requestOptions)
                        .into(holder.img_item_shop_pic);
            } else {
                Glide.with(CommodityBuyActivity.this)
                        .load(datas.get(i).getPic_url() + "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(CommodityBuyActivity.this)))
                        .apply(requestOptions)
                        .into(holder.img_item_shop_pic);
            }
            if (datas.get(i).getNum() == 0) {
                holder.item_iv_goods_nums.setText("×1");
            } else {
                holder.item_iv_goods_nums.setText("×" + datas.get(i).getNum());
            }
            return view;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CCCODE && resultCode == 0 && data != null) {
            Bundle bundle = data.getExtras();
            ccId = bundle.getString("ccId");
            money = bundle.getString("money");
            DecimalFormat df = new DecimalFormat("0.00");
            Double Money = prices - Double.parseDouble(money);
            if (Money < 0) {
                tvMoney.setText("合计：¥" + 0.00);
                payMoney = 0.00;
            } else {
                tvMoney.setText("合计：¥" + df.format(payMoney - Double.parseDouble(money)));
                payMoney = prices - Double.parseDouble(money);
            }
        }
    }
}
