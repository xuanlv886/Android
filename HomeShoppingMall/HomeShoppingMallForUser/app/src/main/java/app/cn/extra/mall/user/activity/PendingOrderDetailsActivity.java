package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.PendingOrderDetailsEvent;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.DelUserRequirement;
import app.cn.extra.mall.user.vo.GetUserRequirementDetail;
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
    @BindView(R.id.tv_get_address)
    TextView tvGetAddress;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private SharePreferenceUtil mSharePreferenceUtil;
    GetUserRequirementDetail getUserRequirementDetail = null;
    String urId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order_details);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
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
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.getUserRequirementDetail)
                    .addParams("uId", mSharePreferenceUtil.getUID())
                    .addParams("urId", urId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }

                            getUserRequirementDetail = new GetUserRequirementDetail();
                            getUserRequirementDetail = Utils.parserJsonResult(response,
                                    GetUserRequirementDetail.class);
                            if (Constants.OK.equals(getUserRequirementDetail.getFlag())) {
                                tvDemandType.setText("需求类别：" + getUserRequirementDetail.getData().getRtName());
                                tvDemandTitle.setText("需求标题：" + getUserRequirementDetail.getData().getUrTitle());
                                tvDemendContent.setText("需求描述：" + getUserRequirementDetail.getData().getUrContent());
                                tvSendTime.setText("发布时间：" + getUserRequirementDetail.getData().getUrCreateTime());
                                if (getUserRequirementDetail.getData().getUrOfferType() == 1) {
                                    tvDemandMoney.setText("悬赏金额：需商家报价");
                                } else {
                                    tvDemandMoney.setText("悬赏金额：" + "¥" + getUserRequirementDetail.getData().getUrOfferPrice());
                                }
                                tvUserName.setText("发  布  人：" + getUserRequirementDetail.getData().getUrTrueName());
                                tvUserPhone.setText("联系电话：" + getUserRequirementDetail.getData().getUrTel());
                                tvUserAddress.setText("居住地址：" + getUserRequirementDetail.getData().getUrAddress());
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
            showLongToast(PendingOrderDetailsActivity.this, Constants.NETWORK_ERROR);
        }
    }

    /**
     * 删除需求
     */
    private void delUserRequirement(String urId) {
        if (Utils.isNetworkAvailable(PendingOrderDetailsActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.delUserRequirement)
                    .addParams("uId", mSharePreferenceUtil.getUID())
                    .addParams("urId", urId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            setNextBtnClickable(true);
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            setNextBtnClickable(true);
                            DelUserRequirement delUserRequirement = new DelUserRequirement();
                            delUserRequirement = Utils.parserJsonResult(response,
                                    DelUserRequirement.class);
                            if (Constants.OK.equals(delUserRequirement.getFlag())) {
                                if (Constants.OK.equals(delUserRequirement.getData().getStatus())) {
                                    showLongToast(PendingOrderDetailsActivity.this, "删除成功！");
                                    finish();
                                } else {
                                    showLongToast(PendingOrderDetailsActivity.this, delUserRequirement.getData().getErrorString());
                                }

                            } else {
                                showLongToast(PendingOrderDetailsActivity.this, delUserRequirement.getErrorString());
                                setNextBtnClickable(true);
                            }
                        }
                    });
        } else {
            showLongToast(PendingOrderDetailsActivity.this, Constants.NETWORK_ERROR);
            setNextBtnClickable(true);
        }
    }

    /**
     * 设置删除按钮显示效果与是否可以点击
     *
     * @param i i=true可以点击；i=false不可点击
     */
    private void setNextBtnClickable(boolean i) {
        if (i) {
            btnDelete.setBackgroundResource(R.drawable.five_radius_btn_blue);
            btnDelete.setTextColor(getResources().getColor(R.color.white));
            btnDelete.setEnabled(true);
        } else {
            btnDelete.setBackgroundResource(R.drawable.five_gray_bg_circle);
            btnDelete.setTextColor(getResources().getColor(R.color.white));
            btnDelete.setEnabled(false);
        }
    }

    @OnClick({R.id.img_back, R.id.tv_revise, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_revise:
                Intent intent = new Intent(PendingOrderDetailsActivity.this, SendDemandActivity.class);
                intent.putExtra("userRequirement", getUserRequirementDetail);
                intent.putExtra("into", "PendingOrderDetailsActivity");
                startActivity(intent);
                break;
            case R.id.btn_delete:
                setNextBtnClickable(false);
                delUserRequirement(urId);
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
}
