package app.cn.extra.mall.user.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nim.uikit.common.util.string.MD5;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.json.JSONException;
import org.json.JSONObject;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.IMLogin;
import app.cn.extra.mall.user.utils.PhoneUtil;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 登录页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class LoginActivity extends BaseActivty {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    /**
     * 密码最少位数
     */
    int minPasswrodLength = 6;
    private SharePreferenceUtil sharePreferenceUtil;
    private AbortableFuture<LoginInfo> loginRequest;
    /**
     * 1退出登录；0其他
     */
    int intentTag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(LoginActivity.this, Constants.SAVE_USER);
        Intent intent = getIntent();
        intentTag = intent.getIntExtra("intentTag", 0);
        showUserAccount();
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
    }

    /**
     * 展示用户登录账号
     */
    private void showUserAccount() {
        if (!TextUtils.isEmpty(sharePreferenceUtil.getUTel())) {
            etAccount.setText(sharePreferenceUtil.getUTel());
        }
    }

    @OnClick({R.id.img_back, R.id.loginBtn, R.id.tv_register, R.id.tv_forget})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_forget:
                intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.img_back:
                if (intentTag == 1) {
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
            case R.id.loginBtn:
                setLoginBtnClickable(false);
                String uAccount = etAccount.getText().toString().trim();
                String uPassword = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(uAccount) && TextUtils.isEmpty(uPassword)) {
                    showShortToast(LoginActivity.this, "请输入您的手机号！");
                    setLoginBtnClickable(true);
                    break;
                }
                if (TextUtils.isEmpty(uAccount)) {
                    showShortToast(LoginActivity.this, "请输入您的手机号！");
                    setLoginBtnClickable(true);
                    break;
                }
                if (TextUtils.isEmpty(uPassword)) {
                    showShortToast(LoginActivity.this, "请输入您的密码！");
                    setLoginBtnClickable(true);
                    break;
                }
                if (!PhoneUtil.isChinaPhoneLegal(uAccount)) {
                    showShortToast(LoginActivity.this, "手机号格式错误，请重新输入！");
                    setLoginBtnClickable(true);
                    break;
                }
                if (uPassword.length() < minPasswrodLength) {
                    showShortToast(LoginActivity.this, "密码格式错误，请重新输入！");
                    setLoginBtnClickable(true);
                    break;
                }
                userLogin(uAccount, uPassword);
                break;
            case R.id.tv_register:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 设置登录按钮样式与是否可以点击
     *
     * @param b b=true 可以点击；b=false 不可点击
     */
    private void setLoginBtnClickable(boolean b) {
        if (b) {
            loginBtn.setBackgroundResource(R.drawable.shape_btn_blue);
            loginBtn.setTextColor(getResources().getColor(R.color.white));
            loginBtn.setEnabled(true);
        } else {
            loginBtn.setBackgroundResource(R.drawable.gray_bg_circle);
            loginBtn.setTextColor(getResources().getColor(R.color.white));
            loginBtn.setEnabled(false);
        }
    }

    /**
     * 登录接口
     *
     * @param uAccount  手机号
     * @param uPassword 密码
     */
    private void userLogin(String uAccount, String uPassword) {
        if (Utils.isNetworkAvailable(LoginActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.userLogin)
                    .addParams("gAppType", Constants.APP_TYPE)
                    .addParams("uAccount", uAccount + "")
                    .addParams("uPassword", uPassword + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Utils.LogE(e.toString());
                            setLoginBtnClickable(true);
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String flag = jsonObject.getString("flag");
                                if (Constants.OK.equals(flag)) {
                                    String data = jsonObject.getString("data");
                                    JSONObject jsonObject1 = new JSONObject(data);
                                    if (jsonObject1.getBoolean("status")) {
                                        String uId = jsonObject1.getString("uId");
                                        String uTel = jsonObject1.getString("uTel");
                                        sharePreferenceUtil.setUID(uId);
                                        sharePreferenceUtil.setUTel(uTel);
                                        /**
                                         * 网易云
                                         */
                                        sharePreferenceUtil.saveUserToken(jsonObject1.getString("token"));
                                        sharePreferenceUtil.saveAccid(jsonObject1.getString("accid"));
                                        sharePreferenceUtil.setBrokenLineTag("");
                                        IMLogin imLogin = new IMLogin(LoginActivity.this);
                                        imLogin.login(0);
                                    } else {
                                        String errorString = jsonObject1.getString("errorString");
                                        showShortToast(LoginActivity.this, errorString);
                                        setLoginBtnClickable(true);
                                    }
                                } else {
                                    String errorString = jsonObject.getString("errorString");
                                    showShortToast(LoginActivity.this, errorString);
                                    setLoginBtnClickable(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                setLoginBtnClickable(true);
                            }
                        }
                    });
        } else {
            showShortToast(LoginActivity.this, Constants.NETWORK_ERROR);
            setLoginBtnClickable(true);
        }
    }

    //DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
    //开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
        String appKey = readAppKey(LoginActivity.this);
        boolean isDemo = "45c6af3c98409b18a84451215d0bdd6e".equals(appKey)
                || "fe416640c8e8a72734219e1847ad2547".equals(appKey);

        return isDemo ? MD5.getStringMD5(password) : password;
    }

    private static String readAppKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (intentTag == 1) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
