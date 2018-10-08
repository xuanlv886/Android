package app.cn.extra.mall.merchant.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.github.ybq.android.spinkit.style.Wave;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Map;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.CommodityBuyEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.utils.aliPay.PayResult;
import app.cn.extra.mall.merchant.vo.AilPayQueryProductOrder;
import app.cn.extra.mall.merchant.vo.AliPayUnifiedOrder;
import app.cn.extra.mall.merchant.vo.AliPaybean;
import app.cn.extra.mall.merchant.vo.GetOrderPayCode;
import app.cn.extra.mall.merchant.vo.GetPayStyle;
import app.cn.extra.mall.merchant.vo.WxPayUnifiedOrder;
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
 * Description 缴纳保障金页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class PaySafeguardsActivity extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.btn_pay)
    Button btnPay;

    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 临时存储支付订单号
     */
    private String payCode = "";
    private GetPayStyle payStyles = null;
    private CommonAdapter<GetPayStyle.DataBean.PayStyleListBean> adapter;
    /**
     * 选择的支付方式在列表中的位置 默认选中第一个
     */
    private int index = 0;
    private IWXAPI api;
    private static final int SDK_PAY_FLAG = 1;
    private String out_trade_no;
    private String trade_no;
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
                    Log.e("resultInfo", resultInfo);
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
                        CustomToast.showToast(PaySafeguardsActivity.this,
                                "支付失败", Toast.LENGTH_SHORT);
                    }
                    break;
                }

                default:
                    break;
            }
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_safeguards);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
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
        Intent intent = new Intent();
        intent.setClass(PaySafeguardsActivity.this, MyWalletActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(PaySafeguardsActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        initRecyclerView();
        getPayStylePort();
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(etMoney.getText().toString().trim())) {
                    tvMoney.setText(getResources().getString(R.string.CNY)
                            + etMoney.getText().toString().trim());
                } else {
                    tvMoney.setText("");
                }
            }
        });
        api = WXAPIFactory.createWXAPI(PaySafeguardsActivity.this, Constants.WX_APP_ID_MERCHANT,
                false);
        api.registerApp(Constants.WX_APP_ID_MERCHANT);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 设置RecyclerView管理器
         */
        recyclerView.setLayoutManager(new LinearLayoutManager(PaySafeguardsActivity.this,
                LinearLayoutManager.VERTICAL, false));
        /**
         * 添加分隔线
         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(PaySafeguardsActivity.this,
//                DividerItemDecoration.VERTICAL));
    }

    /**
     * 获取支付方式接口
     */
    private void getPayStylePort() {
        if (Utils.isNetworkAvailable(PaySafeguardsActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getPayStyle)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(PaySafeguardsActivity.this, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            payStyles = new GetPayStyle();
                            payStyles = Utils.parserJsonResult(response, GetPayStyle.class);
                            if (Constants.OK.equals(payStyles.getFlag())) {
                                if (0 < payStyles.getData().getPayStyleList().size()) {
                                    setPayStyleData(payStyles.getData().getPayStyleList());
                                }
                            } else {
                                showShortToast(PaySafeguardsActivity.this,
                                        payStyles.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(PaySafeguardsActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 填充支付方式数据
     */
    private void setPayStyleData(List<GetPayStyle.DataBean.PayStyleListBean> payStyleList) {
        adapter = new CommonAdapter<GetPayStyle.DataBean.PayStyleListBean>(
                PaySafeguardsActivity.this, R.layout.list_item_pay_style, payStyleList) {
            @Override
            protected void convert(ViewHolder holder,
                                   GetPayStyle.DataBean.PayStyleListBean
                                           payStyleBean, int position) {
//                RequestOptions requestOptions = new RequestOptions()
//                        .error(R.drawable.ic_exception)
//                        .fallback(R.drawable.ic_exception);
//                Glide.with(PaySafeguardsActivity.this).load(
//                        payStyleBean.getUrl())
//                        .apply(requestOptions)
//                        .into((ImageView) holder.getView(R.id.img_item_pay_style));
                if (payStyleBean.getPsName().contains("支付宝")) {
                    holder.setImageResource(R.id.img_item_pay_style, R.mipmap.ali_logo);
                } else if (payStyleBean.getPsName().contains("微信")) {
                    holder.setImageResource(R.id.img_item_pay_style, R.mipmap.wx_logo);
                }
                holder.setText(R.id.tv_item_pay_style_name, payStyleBean.getPsName());
                if (index == position) {
                    holder.setImageResource(R.id.img_item_pay_style_select, R.mipmap.selected);
                } else {
                    holder.setImageResource(R.id.img_item_pay_style_select, R.mipmap.unselect);
                }
            }
        };
        /**
         * 支付方式点击事件
         */
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                index = position;
                adapter.notifyDataSetChanged();
                Utils.LogE(index + "");
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.img_back, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_pay:
                doPay();
                break;
            default:
        }
    }

    /**
     * 点击缴纳按钮执行的操作
     */
    private void doPay() {
        /**
         * 校验输入的金额
         */
        if (TextUtils.isEmpty(etMoney.getText().toString().trim())) {
            showShortToast(PaySafeguardsActivity.this, getResources().getString(
                    R.string.paySafeguardsEmpty));
            return;
        }
        if (etMoney.getText().toString().trim().startsWith(".")
                || etMoney.getText().toString().trim().endsWith(".")) {
            showShortToast(PaySafeguardsActivity.this, getResources().getString(
                    R.string.paySafeguardsErr));
            return;
        }
        getOrderPayCodePort();
    }

    /**
     * 获取支付订单号接口
     */
    private void getOrderPayCodePort() {
        if (Utils.isNetworkAvailable(PaySafeguardsActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getOrderPayCode)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(PaySafeguardsActivity.this, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            GetOrderPayCode getOrderPayCode = new GetOrderPayCode();
                            getOrderPayCode = Utils.parserJsonResult(response, GetOrderPayCode.class);
                            if (Constants.OK.equals(getOrderPayCode.getFlag())) {
                                if (Constants.OK.equals(getOrderPayCode.getData().getStatus())) {
                                    payCode = getOrderPayCode.getData().getPayCode();
                                    if (payStyles.getData() != null && payStyles.getData().getPayStyleList().size() > 0) {
                                        if (Constants.WX_PAY.equals(payStyles.getData()
                                                .getPayStyleList().get(index).getPsName())) {
                                            wxPayUnifiedOrderPort();
                                        } else if (Constants.ALI_PAY.equals(payStyles.getData()
                                                .getPayStyleList().get(index).getPsName())) {
                                            aliPayUnifiedOrderPort();
                                        }
                                    } else {
                                        Toast.makeText(PaySafeguardsActivity.this, "正在获取支付方式，请稍等", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    showShortToast(PaySafeguardsActivity.this,
                                            getOrderPayCode.getData().getErrorString());
                                }
                            } else {
                                showShortToast(PaySafeguardsActivity.this,
                                        getOrderPayCode.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(PaySafeguardsActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 微信支付统一下单接口
     */
    private void wxPayUnifiedOrderPort() {
        if (Utils.isNetworkAvailable(PaySafeguardsActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.wxPayUnifiedOrder)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("payCode", payCode)
                    .addParams("totalMoney", etMoney.getText().toString().trim())
                    /**
                     * orderType--订单类型0--商品订单，1--需求订单，2--缴纳保障金
                     */
                    .addParams("orderType", "2")
                    /**
                     * appType--平台类型0--用户端，1--商户端，2--WEB管理端
                     */
                    .addParams("appType", "1")
                    .addParams("orderData", "{}")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(PaySafeguardsActivity.this, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            WxPayUnifiedOrder wxPayUnifiedOrder = new WxPayUnifiedOrder();
                            wxPayUnifiedOrder = Utils.parserJsonResult(response, WxPayUnifiedOrder.class);
                            if (Constants.OK.equals(wxPayUnifiedOrder.getFlag())) {
                                if (Constants.OK.equals(wxPayUnifiedOrder.getData().getStatus())) {
                                    toWxPay(wxPayUnifiedOrder.getData());
                                } else {
                                    showShortToast(PaySafeguardsActivity.this,
                                            wxPayUnifiedOrder.getData().getErrorString());
                                }
                            } else {
                                showShortToast(PaySafeguardsActivity.this,
                                        wxPayUnifiedOrder.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(PaySafeguardsActivity.this,
                    Constants.NETWORK_ERROR);
        }
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
        sharePreferenceUtil.setPayCode(payCode);
        sharePreferenceUtil.setTotalMoney(etMoney.getText().toString().trim());
    }

    /**
     * 支付宝支付统一下单接口
     */
    private void aliPayUnifiedOrderPort() {
        if (Utils.isNetworkAvailable(PaySafeguardsActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.ailPayUnifiedOrder)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("payCode", payCode)
                    .addParams("totalMoney", "" + etMoney.getText().toString().trim())
                    /**
                     * orderType--订单类型0--商品订单，1--需求订单，2--缴纳保障金
                     */
                    .addParams("orderType", "2")
                    /**
                     * appType--平台类型0--用户端，1--商户端，2--WEB管理端
                     */
                    .addParams("appType", "1")
                    .addParams("orderData", "{}")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(PaySafeguardsActivity.this, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            AliPayUnifiedOrder aliPayUnifiedOrder = new AliPayUnifiedOrder();
                            aliPayUnifiedOrder = Utils.parserJsonResult(response, AliPayUnifiedOrder.class);
                            if (Constants.OK.equals(aliPayUnifiedOrder.getFlag())) {
                                if (Constants.OK.equals(aliPayUnifiedOrder.getData().getStatus())) {
                                    toAliPay(aliPayUnifiedOrder.getData());
                                } else {
                                    showShortToast(PaySafeguardsActivity.this,
                                            aliPayUnifiedOrder.getErrorString());
                                }
                            } else {
                                showShortToast(PaySafeguardsActivity.this,
                                        aliPayUnifiedOrder.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(PaySafeguardsActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    private void toAliPay(AliPayUnifiedOrder.DataBean dataBean) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(PaySafeguardsActivity.this);
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
     * 支付宝支付成功后的回调
     */
    private void ailPayQueryProductOrderPort(String trade_no) {
        if (Utils.isNetworkAvailable(PaySafeguardsActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.aliPayOrderQuery)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("payCode", payCode)
                    .addParams("tradeNo", trade_no)
                    .addParams("totalMoney", "" + etMoney.getText().toString().trim())
                    /**
                     * orderType--订单类型0--商品订单，1--需求订单，2--缴纳保障金
                     */
                    .addParams("orderType", "2")
                    /**
                     * appType--平台类型0--用户端，1--商户端，2--WEB管理端
                     */
                    .addParams("appType", "1")
                    .addParams("orderData", "{\"sId\": " + "\"" + sharePreferenceUtil.getSID() + "\"" + "}")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("updateProductOrder--", response);
                            AilPayQueryProductOrder ailPayQueryProductOrder = new AilPayQueryProductOrder();
                            ailPayQueryProductOrder = Utils.parserJsonResult(response, AilPayQueryProductOrder.class);
                            if ("true".equals(ailPayQueryProductOrder.getFlag())) {
                                if (Constants.OK.equals(ailPayQueryProductOrder.getData().getStatus())) {
                                    Intent intent = new Intent();
                                    intent.setClass(PaySafeguardsActivity.this, PaymentSuccessActivity.class);
                                    intent.putExtra("tags", "");
                                    startActivity(intent);
                                } else {
                                    showLongToast(PaySafeguardsActivity.this, ailPayQueryProductOrder.getData().getErrorString());
                                }

                            } else {
                                showLongToast(PaySafeguardsActivity.this, ailPayQueryProductOrder.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(PaySafeguardsActivity.this, "链接失败，请检查您的网络！");

        }
    }
}
