package app.cn.extra.mall.merchant.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nim.uikit.api.NimUIKit;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.PendingOrderDetailsEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.GetStoreApplicationRequirement;
import app.cn.extra.mall.merchant.vo.GetUserRequirementDetail;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * 待接单-需求详情
 */
public class PendingOrderDetailsActivity extends BaseActivty {
    @BindView(R.id.tv_demand_type)
    TextView tvDemandType;
    @BindView(R.id.tv_demand_title)
    TextView tvDemandTitle;
    @BindView(R.id.tv_demend_content)
    TextView tvDemendContent;
    @BindView(R.id.tv_send_time)
    TextView tvSendTime;
    @BindView(R.id.tv_demand_money)
    TextView tvDemandMoney;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_phone)
    TextView tvUserPhone;
    @BindView(R.id.tv_user_address)
    TextView tvUserAddress;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(R.id.btn_apply_order)
    Button btnApplyOrder;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_message)
    ImageView imgMessage;
    private SharePreferenceUtil mSharePreferenceUtil;
    GetUserRequirementDetail getUserRequirementDetail = null;
    String urId = "";
    SharePreferenceUtil sharePreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order_details);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        sharePreferenceUtil = new SharePreferenceUtil(PendingOrderDetailsActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        mSharePreferenceUtil = new SharePreferenceUtil(PendingOrderDetailsActivity.this, Constants.SAVE_USER);
        Intent intent = getIntent();
        urId = intent.getStringExtra("urId");
        getUserRequirementDetail(urId);
    }

    /**
     * 获取需求详情
     */
    private void getUserRequirementDetail(String urId) {
        if (Utils.isNetworkAvailable(PendingOrderDetailsActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getStoreRequirementDetail)
                    .addParams("urId", urId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Log.e("response", response);
                            getUserRequirementDetail = new GetUserRequirementDetail();
                            getUserRequirementDetail = Utils.parserJsonResult(response,
                                    GetUserRequirementDetail.class);
                            if (Constants.OK.equals(getUserRequirementDetail.getFlag())) {
                                tvDemandType.setText("需求类别:" + getUserRequirementDetail.getData().getRtName());
                                tvDemandTitle.setText("需求标题:" + getUserRequirementDetail.getData().getUrTitle());
                                tvDemendContent.setText("需求描述:" + getUserRequirementDetail.getData().getUrContent());
                                tvSendTime.setText("发布时间:" + getUserRequirementDetail.getData().getUrCreateTime());
                                tvDemandMoney.setText("悬赏金额:" + getUserRequirementDetail.getData().getUrOfferPrice());
                                // 订单处于待接单,待确认状态时，隐藏用户信息
                                if (7 == getUserRequirementDetail.getData().getRoStatus()
                                        || 0 == getUserRequirementDetail.getData().getRoStatus()) {
                                    tvUserName.setText("发  布  人：***" );
                                    tvUserPhone.setText("联系电话：***********");
                                    tvUserAddress.setText("居住地址：*****************");
                                } else {
                                    tvUserName.setText("发  布  人：" + getUserRequirementDetail.getData().getUrTrueName());
                                    tvUserPhone.setText("联系电话：" + getUserRequirementDetail.getData().getUrTel());
                                    tvUserAddress.setText("居住地址：" + getUserRequirementDetail.getData().getUrAddress());
                                }
                                if (getUserRequirementDetail.getData().getSId() != null && !"".equals(getUserRequirementDetail.getData().getSId())) {
                                    tvText.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    });
        } else {
            showLongToast(PendingOrderDetailsActivity.this, "链接失败，请检查您的网络！");
        }
    }

    /**
     * 设置申请接单按钮显示效果与是否可以点击
     *
     * @param i i=true可以点击；i=false不可点击
     */
    private void setNextBtnClickable(boolean i) {
        if (i) {
            btnApplyOrder.setBackgroundResource(R.drawable.shape_btn_blue);
            btnApplyOrder.setTextColor(getResources().getColor(R.color.white));
            btnApplyOrder.setEnabled(true);
        } else {
            btnApplyOrder.setBackgroundResource(R.drawable.shape_btn_blue);
            btnApplyOrder.setTextColor(getResources().getColor(R.color.white));
            btnApplyOrder.setEnabled(false);
        }
    }

    @OnClick({R.id.img_back, R.id.btn_apply_order, R.id.img_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
//            case R.id.tv_revise:
//                Intent intent = new Intent(PendingOrderDetailsActivity.this, SendDemandActivity.class);
//                intent.putExtra("userRequirement", getUserRequirementDetail);
//                intent.putExtra("into", "PendingOrderDetailsActivity");
//                startActivity(intent);
//                break;
            case R.id.btn_apply_order:
                setNextBtnClickable(false);
//申请接单
                if (getUserRequirementDetail.getData().getUrOfferType() == 0) {
                    getStoreApplicationRequirement("" + getUserRequirementDetail.getData().getUrOfferPrice(), null);
                } else {
                    showDialog();
                }
                break;
            case R.id.img_message:
                if (getUserRequirementDetail != null) {
                    NimUIKit.startP2PSession(PendingOrderDetailsActivity.this,
                            getUserRequirementDetail.getData().getAccId(), null);
                }
                break;
            default:
                break;
        }
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
    public void pendingOrderDetailsEventBus(PendingOrderDetailsEvent event) {
        getUserRequirementDetail(urId);
    }

    private void showDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(PendingOrderDetailsActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_set_price, null);
        final Dialog alertDialog = new AlertDialog.Builder(PendingOrderDetailsActivity.this, R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        EditText et_live_content = view.findViewById(R.id.et_live_content);
        tv__live_never_reminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tv_live_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String quotedPrice = et_live_content.getText().toString().trim();
                if (quotedPrice.startsWith(".")) {
                    showShortToast(PendingOrderDetailsActivity.this, "请填写正确的金额！");
                    alertDialog.dismiss();
                }
                if (quotedPrice.contains(".")) {
                    String[] s = quotedPrice.split("\\.");
                    if (s.length > 1) {
                        if (s[1].length() > 2) {
                            showShortToast(PendingOrderDetailsActivity.this, "请填写正确的金额！");
                            alertDialog.dismiss();
                        }
                    } else {
                        showShortToast(PendingOrderDetailsActivity.this, "请填写正确的金额！");
                        alertDialog.dismiss();
                    }
                }
                //申请接单
                getStoreApplicationRequirement(quotedPrice, alertDialog);
            }
        });
    }

    /**
     * 申请接单接口
     */
    private void getStoreApplicationRequirement(String quotedPrice, final Dialog alertDialog) {
        if (Utils.isNetworkAvailable(PendingOrderDetailsActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getStoreApplicationRequirement)
                    .addParams("sId", mSharePreferenceUtil.getSID())
                    .addParams("quotedPrice", quotedPrice)
                    .addParams("urId", urId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 接口调用出错
                             */
                            showShortToast(PendingOrderDetailsActivity.this,
                                    getResources().getString(R.string.portError));
                            setNextBtnClickable(true);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            setNextBtnClickable(true);
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            GetStoreApplicationRequirement getStoreApplicationRequirement = new GetStoreApplicationRequirement();
                            getStoreApplicationRequirement = Utils.parserJsonResult(response, GetStoreApplicationRequirement.class);
                            if (Constants.OK.equals(getStoreApplicationRequirement.getFlag())) {
                                if (getStoreApplicationRequirement.getData().isStatus()) {
                                    if (alertDialog != null) {
                                        alertDialog.dismiss();
                                    }
                                    showShortToast(PendingOrderDetailsActivity.this,
                                            "申请成功");
                                    finish();
                                } else {
                                    showShortToast(PendingOrderDetailsActivity.this,
                                            getStoreApplicationRequirement.getData().getErrorString());
                                }

                            } else {
                                showShortToast(PendingOrderDetailsActivity.this,
                                        getStoreApplicationRequirement.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(PendingOrderDetailsActivity.this, Constants.NETWORK_ERROR);
            setNextBtnClickable(true);
        }
    }
}
