package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;

/**
 * 设置
 */
public class SettingActivity extends BaseActivty {
    SharePreferenceUtil sharePreferenceUtil = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(SettingActivity.this,
                Constants.SAVE_USER);
    }

    @OnClick({R.id.img_back, R.id.rl_account_message, R.id.rl_forget_password, R.id.rl_revise_password, R.id.rl_security_center, R.id.rl_about_us, R.id.login_out_btn})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_account_message:
                intent = new Intent(SettingActivity.this, AccountMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_forget_password:
                sharePreferenceUtil.setPassWordFlag(Constants.FORGET_PASSWORD);
                gotoSelectAuthenticationTypeActivity(sharePreferenceUtil.getUID(), sharePreferenceUtil.getUTel());
                break;
            case R.id.rl_revise_password:
                sharePreferenceUtil.setPassWordFlag(Constants.REVISE_PASSWORD);
                gotoSelectAuthenticationTypeActivity(sharePreferenceUtil.getUID(), sharePreferenceUtil.getUTel());
                break;
            case R.id.rl_security_center:
                intent = new Intent(SettingActivity.this, SafetyCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_about_us:
                intent = new Intent(SettingActivity.this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.login_out_btn:
                NIMClient.getService(AuthService.class).logout();
                quitAccount();
                break;
            default:
                break;
        }
    }

    /**
     * 清空存储的用户信息并跳转至登录界面
     */
    private void quitAccount() {
        NIMClient.getService(AuthService.class).logout();
        sharePreferenceUtil.setBrokenLineTag("");
        sharePreferenceUtil.setUID("");
        sharePreferenceUtil.setCityName("");
        sharePreferenceUtil.setOrderType("");
        sharePreferenceUtil.setOrderData("");
        sharePreferenceUtil.setPassWordFlag("");
        sharePreferenceUtil.setAcId("");
        sharePreferenceUtil.setFirstOpen("");
        sharePreferenceUtil.setPayCode("");
        sharePreferenceUtil.setSecurity("");
        sharePreferenceUtil.setTotalMoney("");
        sharePreferenceUtil.setVersion("");
        sharePreferenceUtil.setVersionPath("");
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, LoginActivity.class);
        intent.putExtra("intentTag", 1);
        startActivity(intent);
        closeAll();
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
}
