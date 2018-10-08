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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nim.uikit.api.NimUIKit;

import org.greenrobot.eventbus.EventBus;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.PendingOrderFragmentEvent;
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
 * 需求详情
 */
public class RequirementDetailsActivity extends BaseActivty {
    @BindView(R.id.tv_demand_type)
    TextView tvDemandType;
    @BindView(R.id.tv_demand_title)
    TextView tvDemandTitle;
    @BindView(R.id.tv_demend_content)
    TextView tvDemandContent;
    @BindView(R.id.tv_send_time)
    TextView tvSendTime;
    @BindView(R.id.tv_confirm_time)
    TextView tvConfirmTime;
    @BindView(R.id.tv_over_time)
    TextView tvOverTime;
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
    @BindView(R.id.order_btn)
    Button orderBtn;
    @BindView(R.id.rl_set_price)
    RelativeLayout rlSetPrice;
    @BindView(R.id.tv_get_address)
    TextView tvGetAddress;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    /**
     * 需求状态
     */
    String roStatus = "";
    String roId = "";
    String urId = "";
    int applyStatus = 0;
    SharePreferenceUtil mSharePreferenceUtil;
    GetUserRequirementDetail getUserRequirementDetail;
    @BindView(R.id.img_message)
    ImageView imgMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requirement_details);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        mSharePreferenceUtil = new SharePreferenceUtil(RequirementDetailsActivity.this, Constants.SAVE_USER);
        Intent intent = getIntent();
        roStatus = intent.getStringExtra("roStatus");
        roId = intent.getStringExtra("roId");
        urId = intent.getStringExtra("urId");
        applyStatus = intent.getIntExtra("applyStatus", 0);
        setViewAboutStatus();
        getUserRequirementDetail(urId);
    }

    /**
     * 获取需求详情
     */
    private void getUserRequirementDetail(String urId) {
        if (Utils.isNetworkAvailable(RequirementDetailsActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getStoreRequirementDetail)
                    .addParams("uId", mSharePreferenceUtil.getUID())
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
                                tvDemandType.setText("需求类别：" + getUserRequirementDetail.getData().getRtName());
                                tvDemandTitle.setText("需求标题：" + getUserRequirementDetail.getData().getUrTitle());
                                tvDemandContent.setText("需求描述：" + getUserRequirementDetail.getData().getUrContent());
                                tvSendTime.setText("发布时间：" + getUserRequirementDetail.getData().getUrCreateTime());
                                if (getUserRequirementDetail.getData().getUrOfferType() == 1) {
                                    tvDemandMoney.setText("悬赏金额：" + "需商家报价");
                                } else {
                                    tvDemandMoney.setText("悬赏金额：" + "¥" + getUserRequirementDetail.getData().getUrOfferPrice());
                                }
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
                                if (applyStatus == 0) {
                                    orderBtn.setVisibility(View.VISIBLE);
                                } else {
                                    orderBtn.setVisibility(View.GONE);
                                }
                                if ("取货送货".equals(getUserRequirementDetail.getData().getRtName())) {
                                    tvGetAddress.setVisibility(View.VISIBLE);
                                    tvGetAddress.setText("取货地址：" + getUserRequirementDetail.getData().getUrGetAddress());
                                } else {
                                    tvGetAddress.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
        } else {
            showLongToast(RequirementDetailsActivity.this, "链接失败，请检查您的网络！");
        }
    }

    /**
     * 根据需求状态显示页面
     */
    private void setViewAboutStatus() {
        switch (roStatus) {
            case Constants.STATUS_DEMAND_PENDINGORDER:
                tvConfirmTime.setVisibility(View.GONE);
                tvOverTime.setVisibility(View.GONE);
                break;
            case Constants.STATUS_DEMAND_PENDINGSURE:
                tvConfirmTime.setVisibility(View.GONE);
                tvOverTime.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.img_back, R.id.order_btn, R.id.img_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.order_btn:
                if (getUserRequirementDetail.getData().getUrOfferType() == 0) {
                    getStoreApplicationRequirement("" + getUserRequirementDetail.getData().getUrOfferPrice(), null);
                } else {
                    showDialog();
                }
                break;
            case R.id.img_message:
                if (getUserRequirementDetail != null) {
                    NimUIKit.startP2PSession(RequirementDetailsActivity.this,
                            getUserRequirementDetail.getData().getAccId(), null);
                }
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(RequirementDetailsActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_set_price, null);
        final Dialog alertDialog = new AlertDialog.Builder(RequirementDetailsActivity.this, R.style.Theme_Transparent).
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
                    showShortToast(RequirementDetailsActivity.this, "请填写正确的金额！");
                    alertDialog.dismiss();
                }
                if (quotedPrice.contains(".")) {
                    String[] s = quotedPrice.split("\\.");
                    if (s.length > 1) {
                        if (s[1].length() > 2) {
                            showShortToast(RequirementDetailsActivity.this, "请填写正确的金额！");
                            alertDialog.dismiss();
                        }
                    } else {
                        showShortToast(RequirementDetailsActivity.this, "请填写正确的金额！");
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
        if (Utils.isNetworkAvailable(RequirementDetailsActivity.this)) {
            progressBar.setVisibility(View.VISIBLE);
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
                            showShortToast(RequirementDetailsActivity.this,
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
                                    showShortToast(RequirementDetailsActivity.this,
                                            "申请成功");
                                    EventBus.getDefault().post(new PendingOrderFragmentEvent());
                                    finish();
                                } else {
                                    showShortToast(RequirementDetailsActivity.this,
                                            getStoreApplicationRequirement.getData().getErrorString());
                                }

                            } else {
                                showShortToast(RequirementDetailsActivity.this,
                                        getStoreApplicationRequirement.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(RequirementDetailsActivity.this, Constants.NETWORK_ERROR);
            setNextBtnClickable(true);
        }
    }

    /**
     * 设置申请接单按钮显示效果与是否可以点击
     *
     * @param i i=true可以点击；i=false不可点击
     */
    private void setNextBtnClickable(boolean i) {
        if (i) {
            orderBtn.setBackgroundResource(R.drawable.shape_btn_blue);
            orderBtn.setTextColor(getResources().getColor(R.color.white));
            orderBtn.setEnabled(true);
        } else {
            orderBtn.setBackgroundResource(R.drawable.shape_btn_gray);
            orderBtn.setTextColor(getResources().getColor(R.color.white));
            orderBtn.setEnabled(false);
        }
    }

}
