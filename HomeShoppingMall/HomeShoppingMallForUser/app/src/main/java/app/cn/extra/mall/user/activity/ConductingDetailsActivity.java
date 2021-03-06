package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nim.uikit.api.NimUIKit;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.GetUserRequirementDetail;
import app.cn.extra.mall.user.vo.UpdateRequirementOrderStatus;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * 进行中-订单详情
 */
public class ConductingDetailsActivity extends BaseActivty {
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
    @BindView(R.id.tv_get_address)
    TextView tvGetAddress;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.iv_phone)
    ImageView ivPhone;
    @BindView(R.id.img_message)
    ImageView imgMessage;
    private SharePreferenceUtil mSharePreferenceUtil;
    String urId = "";
    GetUserRequirementDetail getUserRequirementDetail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conducting_details);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        mSharePreferenceUtil = new SharePreferenceUtil(ConductingDetailsActivity.this, Constants.SAVE_USER);
        Intent intent = getIntent();
        urId = intent.getStringExtra("urId");
        getUserRequirementDetail(urId);
    }

    /**
     * 获取需求详情
     */
    private void getUserRequirementDetail(String urId) {
        if (Utils.isNetworkAvailable(ConductingDetailsActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
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
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
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

                                RequestOptions requestOptions = new RequestOptions()
                                        .error(R.drawable.ic_exception)
                                        .fallback(R.drawable.ic_exception);
                                Glide.with(ConductingDetailsActivity.this)
                                        .load(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getPicName()
                                                + "?x-oss-process=image/resize,w_150")
                                        .apply(requestOptions)
                                        .into(ivPic);
                                switch (getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getSLevel()) {
                                    case 1:
                                        ivStar1.setImageResource(R.mipmap.icon_s2);
                                        ivStar2.setImageResource(R.mipmap.icon_s1);
                                        ivStar3.setImageResource(R.mipmap.icon_s1);
                                        ivStar4.setImageResource(R.mipmap.icon_s1);
                                        ivStar5.setImageResource(R.mipmap.icon_s1);
                                        break;
                                    case 2:
                                        ivStar1.setImageResource(R.mipmap.icon_s2);
                                        ivStar2.setImageResource(R.mipmap.icon_s2);
                                        ivStar3.setImageResource(R.mipmap.icon_s1);
                                        ivStar4.setImageResource(R.mipmap.icon_s1);
                                        ivStar5.setImageResource(R.mipmap.icon_s1);
                                        break;
                                    case 3:
                                        ivStar1.setImageResource(R.mipmap.icon_s2);
                                        ivStar2.setImageResource(R.mipmap.icon_s2);
                                        ivStar3.setImageResource(R.mipmap.icon_s2);
                                        ivStar4.setImageResource(R.mipmap.icon_s1);
                                        ivStar5.setImageResource(R.mipmap.icon_s1);
                                        break;
                                    case 4:
                                        ivStar1.setImageResource(R.mipmap.icon_s2);
                                        ivStar2.setImageResource(R.mipmap.icon_s2);
                                        ivStar3.setImageResource(R.mipmap.icon_s2);
                                        ivStar4.setImageResource(R.mipmap.icon_s2);
                                        ivStar5.setImageResource(R.mipmap.icon_s1);
                                        break;
                                    case 5:
                                        ivStar1.setImageResource(R.mipmap.icon_s2);
                                        ivStar2.setImageResource(R.mipmap.icon_s2);
                                        ivStar3.setImageResource(R.mipmap.icon_s2);
                                        ivStar4.setImageResource(R.mipmap.icon_s2);
                                        ivStar5.setImageResource(R.mipmap.icon_s2);
                                        break;
                                    default:
                                        ivStar1.setImageResource(R.mipmap.icon_s1);
                                        ivStar2.setImageResource(R.mipmap.icon_s1);
                                        ivStar3.setImageResource(R.mipmap.icon_s1);
                                        ivStar4.setImageResource(R.mipmap.icon_s1);
                                        ivStar5.setImageResource(R.mipmap.icon_s1);
                                        break;
                                }
                                if (!TextUtils.isEmpty(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getSName())) {
                                    if (10 < getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getSName().length()) {
                                        tvShopName.setText(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getSName()
                                                .substring(0, 8) + "···");
                                    } else {
                                        tvShopName.setText(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getSName());
                                    }
                                } else {
                                    tvShopName.setText("");
                                }
                                tvCreateTime.setText("申请时间：" + getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getSarCreateTime());
//                                if ("null".equals(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getFinishedNum())) {
//                                    tvOrderNum.setText("已完成0单");
//                                } else {
                                tvOrderNum.setText("已完成" + getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getFinishedNum() + "单");
//                                }
                                if (!TextUtils.isEmpty(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getSDescribe())) {
                                    if (10 < getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getSDescribe().length()) {
                                        tvContent.setText(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getSDescribe()
                                                .substring(0, 8) + "···");
                                    } else {
                                        tvContent.setText(getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getSDescribe());
                                    }
                                } else {
                                    tvContent.setText("");
                                }
//                                tvMoney.setText("报价钱数：¥" + getUserRequirementDetail.getData().getStoreApplyRequirementList().get(0).getSarPrice() + "元");
                            }
                        }
                    });
        } else {
            showLongToast(ConductingDetailsActivity.this, Constants.NETWORK_ERROR);
        }
    }

    /**
     * 确认需求
     */
    private void updateRequirementOrderStatus(String roId) {
        if (Utils.isNetworkAvailable(ConductingDetailsActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.updateRequirementOrderStatus)
                    .addParams("roId", roId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            UpdateRequirementOrderStatus updateRequirementOrderStatus = new UpdateRequirementOrderStatus();
                            updateRequirementOrderStatus = Utils.parserJsonResult(response,
                                    UpdateRequirementOrderStatus.class);
                            if (Constants.OK.equals(updateRequirementOrderStatus.getFlag())) {
                                if (Constants.OK.equals(updateRequirementOrderStatus.getData().getStatus())) {
                                    showLongToast(ConductingDetailsActivity.this, "确认成功！");
                                    finish();
                                } else {
                                    showLongToast(ConductingDetailsActivity.this, updateRequirementOrderStatus.getData().getErrorString());
                                }
                            } else {
                                showLongToast(ConductingDetailsActivity.this, updateRequirementOrderStatus.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(ConductingDetailsActivity.this, Constants.NETWORK_ERROR);
        }
    }

    @OnClick({R.id.img_back, R.id.tv_confirm_complete, R.id.iv_phone, R.id.img_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_confirm_complete:
                updateRequirementOrderStatus(getUserRequirementDetail.getData().getRoId());
                break;
            case R.id.iv_phone:
                Intent intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + getUserRequirementDetail.getData()
                                .getStoreApplyRequirementList().get(0).getSTel()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.img_message:
                if (!TextUtils.isEmpty(getUserRequirementDetail.getData()
                        .getStoreApplyRequirementList().get(0).getAccId())) {
                    NimUIKit.startP2PSession(ConductingDetailsActivity.this,
                            getUserRequirementDetail.getData()
                                    .getStoreApplyRequirementList().get(0).getAccId(), null);
                }
                break;
            default:
                break;
        }
    }

}
