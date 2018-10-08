package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nim.uikit.api.NimUIKit;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
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
 * 待确认-订单详情
 */
public class PendingSureDetailsActivity extends BaseActivty {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_demand_type)
    TextView tvDemandType;
    @BindView(R.id.tv_demand_title)
    TextView tvDemandTitle;
    @BindView(R.id.tv_demend_content)
    TextView tvDemandContent;
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
    @BindView(R.id.img_message)
    ImageView imgMessage;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
//    private CommonAdapter<GetUserRequirementDetail.DataBean.StoreApplyRequirementListBean> adapter;
    /**
     * 当前页，用于分页获取数据 从1开始
     */
    private int currentPage = 1;
    /**
     * 判断是下拉更新还是上拉加载更多 false--下拉更新， true--上拉加载更多
     */
    private boolean loadMore = false;
    private SharePreferenceUtil sharePreferenceUtil;
    //    /**
//     * 用于存放临时数据
//     */
//    List<GetUserRequirementDetail.DataBean.StoreApplyRequirementListBean> storeAppleRequirementListBeanList = null;
    String urId = "";
    GetUserRequirementDetail getUserRequirementDetail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_sure_details);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(PendingSureDetailsActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        Intent intent = getIntent();
        urId = intent.getStringExtra("urId");
        getUserRequirementDetail(urId);

    }

    @OnClick({R.id.img_back, R.id.img_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_message:
                if (getUserRequirementDetail != null) {
                    NimUIKit.startP2PSession(PendingSureDetailsActivity.this,
                            getUserRequirementDetail.getData().getAccId(), null);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取需求详情
     */
    private void getUserRequirementDetail(String urId) {
        if (Utils.isNetworkAvailable(PendingSureDetailsActivity.this)) {
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
                            /**
                             * 恢复显示数据的View
                             */
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
                                if ("取货送货".equals(getUserRequirementDetail.getData().getRtName())) {
                                    tvGetAddress.setVisibility(View.VISIBLE);
                                    tvGetAddress.setText("取货地址：" + getUserRequirementDetail.getData().getUrGetAddress());
                                } else {
                                    tvGetAddress.setVisibility(View.GONE);
                                }
                            } else {
                            }
                        }
                    });
        } else {
        }
    }
}
