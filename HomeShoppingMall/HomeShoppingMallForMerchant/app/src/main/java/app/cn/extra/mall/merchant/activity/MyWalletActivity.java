package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.MsGetMyWallet;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 我的钱包页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class MyWalletActivity extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_available_balance)
    TextView tvAvailableBalance;
    @BindView(R.id.tv_frozen_safeguards)
    TextView tvFrozenSafeguards;
    @BindView(R.id.tv_apply_to_balance)
    TextView tvApplyToBalance;
    @BindView(R.id.tv_unfrozen_safeguards)
    TextView tvUnfrozenSafeguards;
    @BindView(R.id.tv_already_balance)
    TextView tvAlreadyBalance;
    @BindView(R.id.rl_to_balance)
    RelativeLayout rlToBalance;
    @BindView(R.id.rl_present_record)
    RelativeLayout rlPresentRecord;
    @BindView(R.id.rl_unfrozen_safeguards)
    RelativeLayout rlUnfrozenSafeguards;
    @BindView(R.id.tv_product_charge)
    TextView tvProductCharge;
    @BindView(R.id.rl_product_charge)
    RelativeLayout rlProductCharge;
    @BindView(R.id.tv_requirement_charge)
    TextView tvRequirementCharge;
    @BindView(R.id.rl_requirement_charge)
    RelativeLayout rlRequirementCharge;
    private SharePreferenceUtil sharePreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        msGetMyWalletPort();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(MyWalletActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        if ("0".equals(sharePreferenceUtil.getSType())) {
            rlProductCharge.setVisibility(View.GONE);
        }
        msGetMyWalletPort();
    }

    /**
     * 获取某商户的钱包相关数据接口
     */
    private void msGetMyWalletPort() {
        if (Utils.isNetworkAvailable(MyWalletActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.msGetMyWallet)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            MsGetMyWallet wallet = new MsGetMyWallet();
                            wallet = Utils.parserJsonResult(response, MsGetMyWallet.class);
                            if (Constants.OK.equals(wallet.getFlag())) {
                                if (Constants.OK.equals(wallet.getData().getStatus())) {
                                    setWalletData(wallet.getData());
                                } else {
                                    showShortToast(MyWalletActivity.this,
                                            wallet.getData().getErrorString());
                                }
                            } else {
                                showShortToast(MyWalletActivity.this,
                                        wallet.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(MyWalletActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 填充钱包数据
     *
     * @param data
     */
    private void setWalletData(MsGetMyWallet.DataBean data) {
        /**
         * 可用余额
         */
        tvAvailableBalance.setText(data.getUwLeftMoney() + "");
        /**
         * 冻结的保障金
         */
        tvFrozenSafeguards.setText(data.getUwDeposit() + "");
        /**
         * 申请提现的金额
         */
        tvApplyToBalance.setText(data.getUwApplyToCash() + "");
        /**
         * 申请解冻的保障金
         */
        tvUnfrozenSafeguards.setText(data.getUwApplyToFreeDepositMoney() + "");
        /**
         * 已提现的金额
         */
        tvAlreadyBalance.setText(data.getUwAlreadyToCash() + "元");
        /**
         * 商品交易手续费
         */
        tvProductCharge.setText(data.getsProductServiceCharge() + "%");
        /**
         * 需求交易手续费
         */
        tvRequirementCharge.setText(data.getsRequirementServiceCharge() + "%");
    }

    @OnClick({R.id.img_back, R.id.tv_add, R.id.rl_to_balance, R.id.rl_present_record, R.id.rl_unfrozen_safeguards})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_add:
                gotoPaySafeguardsActivity();
                break;
            case R.id.rl_to_balance:
                gotoApplyToBalanceActivity();
                break;
            case R.id.rl_present_record:
                gotoPresentRecordActivity();
                break;
            case R.id.rl_unfrozen_safeguards:
                gotoThawSafeguardsActivity();
                break;
            default:
        }
    }

    /**
     * 跳转至申请解冻保障金界面
     */
    private void gotoThawSafeguardsActivity() {
        Intent intent = new Intent();
        intent.setClass(MyWalletActivity.this, ThawSafeguardsActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转至缴纳保障金界面
     */
    private void gotoPaySafeguardsActivity() {
        Intent intent = new Intent();
        intent.setClass(MyWalletActivity.this, PaySafeguardsActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转至申请提现界面
     */
    private void gotoApplyToBalanceActivity() {
        Intent intent = new Intent();
        intent.setClass(MyWalletActivity.this, ApplyToBalanceActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转至提现记录界面
     */
    private void gotoPresentRecordActivity() {
        Intent intent = new Intent();
        intent.setClass(MyWalletActivity.this, PresentRecordActivity.class);
        startActivity(intent);
    }
}
