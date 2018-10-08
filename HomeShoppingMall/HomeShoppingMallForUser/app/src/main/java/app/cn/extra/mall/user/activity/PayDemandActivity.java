package app.cn.extra.mall.user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.github.ybq.android.spinkit.style.Wave;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.CommodityBuyEvent;
import app.cn.extra.mall.user.event.PendingSureDetailsEvent;
import app.cn.extra.mall.user.fragment.PayKeyboardFragment;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.utils.aliPay.PayResult;
import app.cn.extra.mall.user.view.NewsListView;
import app.cn.extra.mall.user.view.ViewHolder;
import app.cn.extra.mall.user.vo.AilPayQueryProductOrder;
import app.cn.extra.mall.user.vo.AliPayUnifiedOrder;
import app.cn.extra.mall.user.vo.AliPaybean;
import app.cn.extra.mall.user.vo.DoAddProductOrder;
import app.cn.extra.mall.user.vo.GetCustomer;
import app.cn.extra.mall.user.vo.GetPayStyle;
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

/**
 * 需求支付界面
 */
public class PayDemandActivity extends BaseActivty {
    @BindView(R.id.ib_back)
    ImageView ibBack;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    private SharePreferenceUtil mSharePreferenceUtil;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private int Goodsize = 0;
    @BindView(R.id.lv_pay_type)
    NewsListView lvPayType;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
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
    String roId = "";
    String rtId = "";
    String sName = "";
    String price = "";
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
                        CustomToast.showToast(PayDemandActivity.this,
                                "支付失败", Toast.LENGTH_SHORT);
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
        setContentView(R.layout.activity_pay_demand);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
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
        EventBus.getDefault().post(new PendingSureDetailsEvent(sId, roId));
        finish();
    }

    private void initView() {
        Intent intent = getIntent();
        sId = intent.getStringExtra("sId");
        roId = intent.getStringExtra("roId");
        rtId = intent.getStringExtra("rtId");
        sName = intent.getStringExtra("sName");
        price = intent.getStringExtra("price");
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        scrollView.smoothScrollTo(0, 20);
        mSharePreferenceUtil = new SharePreferenceUtil(PayDemandActivity.this, Constants.SAVE_USER);
        tvMoney.setText("合计：¥" + price);
        tvShopName.setText(sName);
        tvPay.setBackgroundColor(getResources().getColor(R.color.blue));
        lvPayType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                money = "";
                ccId = "";
                tvMoney.setText("合计：¥" + price);
                payMoney = Double.valueOf(price);
                OriginalPrice = payMoney;
                index = i;
                payAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {
        api = WXAPIFactory.createWXAPI(PayDemandActivity.this, Constants.APP_ID);
        api.registerApp(Constants.APP_ID);
        //获取支付方式
        getPayStylePort();

    }

    @OnClick({R.id.ib_back, R.id.tv_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.tv_pay: //支付
                if (!TextUtils.isEmpty(psId)) {
                    //获取订单号
                    doAddProductOrderPort();
                } else {
                    showLongToast(PayDemandActivity.this, "请先选择支付方式！");
                }

                break;
            default:
                break;
        }
    }

    /**
     * 支付宝支付成功后的回调
     */
    private void ailPayQueryProductOrderPort(String trade_no) {
        if (Utils.isNetworkAvailable(PayDemandActivity.this)) {
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
                    .addParams("orderType", "1")
                    /**
                     * appType--平台类型0--用户端，1--商户端，2--WEB管理端
                     */
                    .addParams("appType", "0")
                    .addParams("orderData", "{\"sId\": " + "\"" + sId + "\"" + ",\"roId\": " + "\"" + roId + "\"" + ",\"rtId\": " + "\"" + rtId + "\"}")
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
                                if (Constants.OK.equals(ailPayQueryProductOrder.getData().getStatus())) {
                                    Intent intent = new Intent();
                                    intent.setClass(PayDemandActivity.this, PaymentSuccessActivity.class);
                                    intent.putExtra("tags", "");
                                    intent.putExtra("sId", sId);
                                    intent.putExtra("roId", roId);
                                    startActivity(intent);
                                } else {
                                    showLongToast(PayDemandActivity.this, ailPayQueryProductOrder.getData().getErrorString());
                                }

                            } else {
                                showLongToast(PayDemandActivity.this, ailPayQueryProductOrder.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(PayDemandActivity.this, Constants.NETWORK_ERROR);

        }
    }

    /**
     * 获取支付方式
     */
    private void getPayStylePort() {
        if (Utils.isNetworkAvailable(PayDemandActivity.this)) {
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
                                showLongToast(PayDemandActivity.this, getPayStyle.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(PayDemandActivity.this, Constants.NETWORK_ERROR);

        }
    }

    /**
     * 立即购买 获取订单号
     */
    private void doAddProductOrderPort() {
        if (Utils.isNetworkAvailable(PayDemandActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getOrderPayCode)
                    .addParams("uId", mSharePreferenceUtil.getUID())
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
                                if(Constants.OK.equals(productOrder.getData().getStatus())) {
                                    payCode = productOrder.getData().getPayCode();
                                    poid.clear();
                                    poid.add(productOrder.getData().getPoId());
                                    if (Constants.WX_PAY.equals(payDatas.getPayStyleList().get(index).getPsName())) {
                                        wxPayUnifiedOrderPort();
                                    } else if (Constants.ALI_PAY.equals(payDatas.getPayStyleList().get(index).getPsName())) {
                                        aliPayUnifiedOrderPort();
                                    }
                                }
                            } else {
                                showLongToast(PayDemandActivity.this, productOrder.getErrorString());
                            }
                        }
                    });
        } else {

            showLongToast(PayDemandActivity.this, Constants.NETWORK_ERROR);

        }
    }


    /**
     * 支付宝支付统一下单接口
     */
    private void aliPayUnifiedOrderPort() {
        if (Utils.isNetworkAvailable(PayDemandActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.ailPayUnifiedOrder)
                    .addParams("uId", mSharePreferenceUtil.getUID())
                    .addParams("payCode", payCode)
                    .addParams("totalMoney", "" + payMoney)
                    /**
                     * orderType--订单类型0--商品订单，1--需求订单，2--缴纳保障金
                     */
                    .addParams("orderType", "1")
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
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            showShortToast(PayDemandActivity.this, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }

                            AliPayUnifiedOrder aliPayUnifiedOrder = new AliPayUnifiedOrder();
                            aliPayUnifiedOrder = Utils.parserJsonResult(response, AliPayUnifiedOrder.class);
                            if (Constants.OK.equals(aliPayUnifiedOrder.getFlag())) {
                                if (Constants.OK.equals(aliPayUnifiedOrder.getData().getStatus())) {
                                    toAliPay(aliPayUnifiedOrder.getData());
                                } else {
                                    showShortToast(PayDemandActivity.this,
                                            aliPayUnifiedOrder.getErrorString());
                                }
                            } else {
                                showShortToast(PayDemandActivity.this,
                                        aliPayUnifiedOrder.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(PayDemandActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 微信支付统一下单接口
     */
    private void wxPayUnifiedOrderPort() {
        if (Utils.isNetworkAvailable(PayDemandActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.wxPayUnifiedOrder)
                    .addParams("uId", mSharePreferenceUtil.getUID())
                    .addParams("payCode", payCode)
                    .addParams("totalMoney", "" + payMoney)
                    /**
                     * orderType--订单类型0--商品订单，1--需求订单，2--缴纳保障金
                     */
                    .addParams("orderType", "1")
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
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            showShortToast(PayDemandActivity.this, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }

                            WxPayUnifiedOrder wxPayUnifiedOrder = new WxPayUnifiedOrder();
                            wxPayUnifiedOrder = Utils.parserJsonResult(response, WxPayUnifiedOrder.class);
                            if (Constants.OK.equals(wxPayUnifiedOrder.getFlag())) {
                                if (Constants.OK.equals(wxPayUnifiedOrder.getData().getStatus())) {
                                    toWxPay(wxPayUnifiedOrder.getData());
                                } else {
                                    showShortToast(PayDemandActivity.this,
                                            wxPayUnifiedOrder.getData().getErrorString());
                                }
                            } else {
                                showShortToast(PayDemandActivity.this,
                                        wxPayUnifiedOrder.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(PayDemandActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    private void toAliPay(AliPayUnifiedOrder.DataBean dataBean) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(PayDemandActivity.this);
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
        mSharePreferenceUtil.setOrderType("1");
        mSharePreferenceUtil.setOrderData("{\"sId\": " + "\"" + sId + "\"" + ",\"roId\": " + "\"" + roId + "\"" + ",\"rtId\": " + "\"" + rtId + "\"}");
    }

    private class PayTypeAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private final GetPayStyle.DataBean datas;

        private PayTypeAdapter(GetPayStyle.DataBean payDatas) {
            this.mInflater = LayoutInflater.from(PayDemandActivity.this);
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
}
