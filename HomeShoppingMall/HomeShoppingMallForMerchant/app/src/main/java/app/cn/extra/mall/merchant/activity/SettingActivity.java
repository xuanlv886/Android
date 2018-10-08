package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;

/**
 * Description 设置页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class SettingActivity extends BaseActivty {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.rl_account_info)
    RelativeLayout rlAccountInfo;
    @BindView(R.id.rl_store_info)
    RelativeLayout rlStoreInfo;
    @BindView(R.id.rl_forgot_psw)
    RelativeLayout rlForgotPsw;
    @BindView(R.id.rl_change_psw)
    RelativeLayout rlChangePsw;
    @BindView(R.id.rl_safe_center)
    RelativeLayout rlSafeCenter;
    @BindView(R.id.rl_about_us)
    RelativeLayout rlAboutUs;
    @BindView(R.id.btn_quit)
    Button btnQuit;
    private SharePreferenceUtil sharePreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(SettingActivity.this,
                Constants.SAVE_USER);
    }

    @OnClick({R.id.img_back, R.id.rl_account_info, R.id.rl_store_info, R.id.rl_forgot_psw, R.id.rl_change_psw, R.id.rl_safe_center, R.id.rl_about_us, R.id.btn_quit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_account_info:
                gotoAccountInfoActivity();
                break;
            case R.id.rl_store_info:
                gotoStoreInfoActivity();
                break;
            case R.id.rl_forgot_psw:
                sharePreferenceUtil.setPassWordFlag(Constants.FORGET_PASSWORD);
                gotoSelectAuthenticationTypeActivity(sharePreferenceUtil.getUID(),sharePreferenceUtil.getUAccount());
                break;
            case R.id.rl_change_psw:
                sharePreferenceUtil.setPassWordFlag(Constants.REVISE_PASSWORD);
                gotoSelectAuthenticationTypeActivity(sharePreferenceUtil.getUID(),sharePreferenceUtil.getUAccount());
                break;
            case R.id.rl_safe_center:
                gotoSafetyCenterActivity();
                break;
            case R.id.rl_about_us:
                gotoAboutUsActivity();
                break;
            case R.id.btn_quit:
                quitAccount();
                break;
                default:
        }
    }
    /**
     * 跳转至选择身份验证方式界面
     */
    private void gotoSelectAuthenticationTypeActivity(String uId, String tel) {
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, SelectAuthenticationTypeActivity.class);
        intent.putExtra("uId", uId);
        intent.putExtra("tel", tel);
        startActivity(intent);
    }
    /**
     * 跳转至账号信息界面
     */
    private void gotoAccountInfoActivity() {
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, AccountInfoActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转至商户信息界面
     */
    private void gotoStoreInfoActivity() {
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, StoreInfoActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转至安全中心页
     */
    private void gotoSafetyCenterActivity() {
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, SafetyCenterActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转至关于我们页
     */
    private void gotoAboutUsActivity() {
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, AboutUsActivity.class);
        startActivity(intent);
    }

    /**
     * 清空存储的用户信息并跳转至登录界面
     */
    private void quitAccount() {
        sharePreferenceUtil.setBrokenLineTag("");
        sharePreferenceUtil.setUID("");
        sharePreferenceUtil.setSType("");
        sharePreferenceUtil.setSID("");
        sharePreferenceUtil.setStoreAcId("");
        sharePreferenceUtil.setSChecked("");
        sharePreferenceUtil.setPassWordFlag("");
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, LoginActivity.class);
        startActivity(intent);
        closeAll();
    }
}
