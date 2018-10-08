package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nim.uikit.api.NimUIKit;

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

public class DemandDetailsActivity extends BaseActivty {
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
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;
    @BindView(R.id.iv_star1)
    ImageView ivStar1;
    @BindView(R.id.iv_star2)
    ImageView ivStar2;
    @BindView(R.id.iv_star3)
    ImageView ivStar3;
    @BindView(R.id.iv_star4)
    ImageView ivStar4;
    @BindView(R.id.iv_star5)
    ImageView ivStar5;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.tv_confirm_time)
    TextView tvConfirmTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.again_order_btn)
    Button bottomBtn;
    @BindView(R.id.tv_get_address)
    TextView tvGetAddress;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_message)
    ImageView imgMessage;
    @BindView(R.id.tv_evaluate_level)
    TextView tvEvaluateLevel;
    @BindView(R.id.tv_evaluate_content)
    TextView tvEvaluateContent;
    @BindView(R.id.tv_evaluate_time)
    TextView tvEvaluateTime;
    @BindView(R.id.ll_evaluate)
    LinearLayout llEvaluate;
    private SharePreferenceUtil mSharePreferenceUtil;
    String urId = "";
    /**
     * 页面跳转状态
     */
    String intentFlag = "";
    GetUserRequirementDetail getUserRequirementDetail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_details);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        mSharePreferenceUtil = new SharePreferenceUtil(DemandDetailsActivity.this, Constants.SAVE_USER);
        Intent intent = getIntent();
        urId = intent.getStringExtra("urId");
        intentFlag = intent.getStringExtra("intentFlag");
        setBtnShowAboutStatus(intentFlag);
        getUserRequirementDetail(urId);
    }

    /**
     * 获取需求详情
     */
    private void getUserRequirementDetail(String urId) {
        if (Utils.isNetworkAvailable(DemandDetailsActivity.this)) {
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
                                tvDemandType.setText("需求类别：" + getUserRequirementDetail.getData().getRtName());
                                tvDemandTitle.setText("需求标题：" + getUserRequirementDetail.getData().getUrTitle());
                                tvDemandContent.setText("需求描述：" + getUserRequirementDetail.getData().getUrContent());
                                tvSendTime.setText("发布时间：" + getUserRequirementDetail.getData().getUrCreateTime());
                                tvConfirmTime.setText("确认时间：" + getUserRequirementDetail.getData().getRoConfirmTime());
                                tvDemandMoney.setText("悬赏金额：" + "¥" + getUserRequirementDetail.getData().getUrOfferPrice());
                                tvUserName.setText("发  布  人：" + getUserRequirementDetail.getData().getUrTrueName());
                                tvUserPhone.setText("联系电话：" + getUserRequirementDetail.getData().getUrTel());
                                tvUserAddress.setText("居住地址：" + getUserRequirementDetail.getData().getUrAddress());
                                if ("取货送货".equals(getUserRequirementDetail.getData().getRtName())) {
                                    tvGetAddress.setVisibility(View.VISIBLE);
                                    tvGetAddress.setText("取货地址：" + getUserRequirementDetail.getData().getUrGetAddress());
                                } else {
                                    tvGetAddress.setVisibility(View.GONE);
                                }
                                // 评论内容不为空
                                if (!TextUtils.isEmpty(getUserRequirementDetail.getData()
                                        .getRoeCreateTime())) {
                                    tvEvaluateLevel.setText("评论等级：" + getUserRequirementDetail
                                            .getData().getRoeLevel() + "星");
                                    tvEvaluateContent.setText("评论内容：" + getUserRequirementDetail
                                            .getData().getRoeContent());
                                    tvEvaluateTime.setText("评论时间：" + getUserRequirementDetail
                                            .getData().getRoeCreateTime());
                                }
//                                RequestOptions requestOptions = new RequestOptions()
//                                        .error(R.drawable.ic_exception)
//                                        .fallback(R.drawable.ic_exception);
//                                Glide.with(MyDemandActivity.context)
//                                        .load(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getPicName())
//                                        .apply(requestOptions)
//                                        .into(ivPic);
//                                switch (getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getsLevel()) {
//                                    case 1:
//                                        ivStar1.setImageResource(R.mipmap.icon_s2);
//                                        ivStar2.setImageResource(R.mipmap.icon_s1);
//                                        ivStar3.setImageResource(R.mipmap.icon_s1);
//                                        ivStar4.setImageResource(R.mipmap.icon_s1);
//                                        ivStar5.setImageResource(R.mipmap.icon_s1);
//                                        break;
//                                    case 2:
//                                        ivStar1.setImageResource(R.mipmap.icon_s2);
//                                        ivStar2.setImageResource(R.mipmap.icon_s2);
//                                        ivStar3.setImageResource(R.mipmap.icon_s1);
//                                        ivStar4.setImageResource(R.mipmap.icon_s1);
//                                        ivStar5.setImageResource(R.mipmap.icon_s1);
//                                        break;
//                                    case 3:
//                                        ivStar1.setImageResource(R.mipmap.icon_s2);
//                                        ivStar2.setImageResource(R.mipmap.icon_s2);
//                                        ivStar3.setImageResource(R.mipmap.icon_s2);
//                                        ivStar4.setImageResource(R.mipmap.icon_s1);
//                                        ivStar5.setImageResource(R.mipmap.icon_s1);
//                                        break;
//                                    case 4:
//                                        ivStar1.setImageResource(R.mipmap.icon_s2);
//                                        ivStar2.setImageResource(R.mipmap.icon_s2);
//                                        ivStar3.setImageResource(R.mipmap.icon_s2);
//                                        ivStar4.setImageResource(R.mipmap.icon_s2);
//                                        ivStar5.setImageResource(R.mipmap.icon_s1);
//                                        break;
//                                    case 5:
//                                        ivStar1.setImageResource(R.mipmap.icon_s2);
//                                        ivStar2.setImageResource(R.mipmap.icon_s2);
//                                        ivStar3.setImageResource(R.mipmap.icon_s2);
//                                        ivStar4.setImageResource(R.mipmap.icon_s2);
//                                        ivStar5.setImageResource(R.mipmap.icon_s2);
//                                        break;
//                                    default:
//                                        ivStar1.setImageResource(R.mipmap.icon_s1);
//                                        ivStar2.setImageResource(R.mipmap.icon_s1);
//                                        ivStar3.setImageResource(R.mipmap.icon_s1);
//                                        ivStar4.setImageResource(R.mipmap.icon_s1);
//                                        ivStar5.setImageResource(R.mipmap.icon_s1);
//                                        break;
//                                }
//                                if (!TextUtils.isEmpty(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getsName())) {
//                                    if (12 < getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getsName().length()) {
//                                        tvShopName.setText(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getsName()
//                                                .substring(0, 10) + "···");
//                                    } else {
//                                        tvShopName.setText(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getsName());
//                                    }
//                                } else {
//                                    tvShopName.setText("");
//                                }
//                                tvCreateTime.setText("申请时间:" + getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getSarCreateTime());
//                                if (getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getFinshedNum() == null || "null".equals(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getFinshedNum())) {
//                                    tvOrderNum.setText("已完成0单");
//                                } else {
//                                    tvOrderNum.setText("已完成" + getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getFinshedNum() + "单");
//                                }
//                                if (!TextUtils.isEmpty(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getsDescribe())) {
//                                    if (12 < getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getsDescribe().length()) {
//                                        tvContent.setText(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getsDescribe()
//                                                .substring(0, 10) + "···");
//                                    } else {
//                                        tvContent.setText(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getsDescribe());
//                                    }
//                                } else {
//                                    tvContent.setText("");
//                                }
//                                tvMoney.setText("报价钱数: ￥" + getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getSarPrice() + "元");
                            }
                        }
                    });
        } else {
            showLongToast(DemandDetailsActivity.this, "链接失败，请检查您的网络！");
        }
    }


    @OnClick({R.id.img_back, R.id.again_order_btn, R.id.iv_phone, R.id.img_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.again_order_btn:
                clickAgainAboutStatus(intentFlag);
                break;
            case R.id.iv_phone:
                break;
            case R.id.img_message:
                if (getUserRequirementDetail != null) {
                    NimUIKit.startP2PSession(DemandDetailsActivity.this,
                            getUserRequirementDetail.getData().getAccId(), null);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 根据页面跳转状态判断通知用户验货按钮点击后逻辑
     *
     * @param intentFlag
     */
    private void clickAgainAboutStatus(String intentFlag) {
        Intent intent = null;
        switch (intentFlag) {
            case Constants.STATUS_DEMAND_PICKUPGOODS:
                intent = new Intent(DemandDetailsActivity.this, NotificationPickUpGoodsActivity.class);
                intent.putExtra("roId", getUserRequirementDetail.getData().getRoId());
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 根据页面跳转状态判断按钮显示
     *
     * @param intentFlag
     */
    private void setBtnShowAboutStatus(String intentFlag) {
        switch (intentFlag) {
            case Constants.STATUS_DEMAND_PICKUPGOODS:
                bottomBtn.setVisibility(View.VISIBLE);
                bottomBtn.setText("通知用户验货");
                break;
            case Constants.STATUS_DEMAND_INSPECTIONGOODS:
                bottomBtn.setVisibility(View.GONE);
                break;
            case Constants.STATUS_DEMAND_DELIVERY:
                bottomBtn.setVisibility(View.GONE);
                break;
            case Constants.STATUS_DEMAND_EVALUATED:
                bottomBtn.setVisibility(View.GONE);
                /**
                 * 显示评论内容
                 */
                llEvaluate.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
