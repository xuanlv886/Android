package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.auth.LoginInfo;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.IMLogin;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.StoreLogin;
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

    @BindView(R.id.tv_reg)
    TextView tvReg;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_forget)
    TextView tvForget;

    private SharePreferenceUtil sharePreferenceUtil;
    private AbortableFuture<LoginInfo> loginRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(LoginActivity.this,
                Constants.SAVE_USER);
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
        if (!TextUtils.isEmpty(sharePreferenceUtil.getUAccount())) {
            etAccount.setText(sharePreferenceUtil.getUAccount());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUserAccount();
    }

    @OnClick({R.id.tv_reg, R.id.loginBtn, R.id.tv_forget})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_reg:
                gotoSelectMerchantTypeActivity();
                break;
            case R.id.loginBtn:
                doLogin();
                break;
            case R.id.tv_forget:
                gotoForgotPasswordActivity();
                break;
            default:
        }
    }

    /**
     * 跳转至选择要注册的商户类型页
     */
    private void gotoSelectMerchantTypeActivity() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, SelectMerchantTypeActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转至选择身份验证方式界面
     */
    private void gotoSelectAuthenticationTypeActivity(String uId, String tel) {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, SelectAuthenticationTypeActivity.class);
        intent.putExtra("uId", uId);
        intent.putExtra("tel", tel);
        startActivity(intent);
    }

    /**
     * 跳转至忘记密码界面
     */
    private void gotoForgotPasswordActivity() {
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    /**
     * 登录
     */
    private void doLogin() {
        if (!Utils.isValidPhoneNumber(etAccount.getText().toString().trim())) {
            showShortToast(LoginActivity.this, getResources().getString(
                    R.string.phoneNumError));
            return;
        }
        if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            showShortToast(LoginActivity.this, getResources().getString(
                    R.string.passwordError));
            return;
        }
        storeLoginPort();
    }

    /**
     * 商户端登录接口
     */
    private void storeLoginPort() {
        if (Utils.isNetworkAvailable(LoginActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.storeLogin)
                    .addParams("uAccount", etAccount.getText().toString().trim())
                    .addParams("uPassword", etPassword.getText().toString().trim())
                    .addParams("uPhoneId", sharePreferenceUtil.getPhoneId())
                    .addParams("acId", sharePreferenceUtil.getLocationAcId())
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
                            showShortToast(LoginActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            StoreLogin storeLogin = new StoreLogin();
                            storeLogin = Utils.parserJsonResult(response, StoreLogin.class);
                            if (Constants.OK.equals(storeLogin.getFlag())) {
                                /**
                                 * 网易云
                                 */
                                sharePreferenceUtil.saveUserToken(storeLogin.getData().getToken());
                                sharePreferenceUtil.saveAccid(storeLogin.getData().getAccid());
                                if (Constants.OK.equals(storeLogin.getData().getStatus())) {
                                    /**
                                     * 登录成功，保存相关数据并跳转至主界面
                                     */
                                    sharePreferenceUtil.setUID(storeLogin.getData().getUId());
                                    sharePreferenceUtil.setStoreAcId(storeLogin.getData().getAcId());
                                    sharePreferenceUtil.setSID(storeLogin.getData().getSId());
                                    sharePreferenceUtil.setSType(storeLogin.getData().getSType() + "");
                                    sharePreferenceUtil.setUAccount(etAccount.getText().toString().trim());
                                    sharePreferenceUtil.setSChecked(storeLogin.getData().getsChecked() + "");
                                    /**
                                     * 云信登录
                                     */
                                    IMLogin imLogin = new IMLogin(LoginActivity.this);
                                    imLogin.login(0);
                                } else {
                                    showShortToast(LoginActivity.this,
                                            storeLogin.getData().getErrorString());
                                    /**
                                     * 判断该商户是否通过审核
                                     * --未通过：提示返回的信息，不做其他处理
                                     * --已通过：根据返回的needVerification字段判断是否需要进行身份验证
                                     */
                                    if ("1".equals(storeLogin.getData().getsChecked())) {
                                        if (Constants.OK.equals(storeLogin.getData()
                                                .getNeedVerification())) {
                                            /**
                                             * 进行身份验证
                                             */
                                            gotoSelectAuthenticationTypeActivity(storeLogin.getData().getUId(),
                                                    etAccount.getText().toString().trim());
                                        } else {
                                            Utils.LogE("status为false，needVerification也为false。不会出现这种情况。");
                                        }
                                    }

                                }
//                                /**
//                                 * 登录成功，保存相关数据并跳转至主界面
//                                 */
//                                sharePreferenceUtil.setUID(storeLogin.getData().getUId());
//                                sharePreferenceUtil.setStoreAcId(storeLogin.getData().getAcId());
//                                sharePreferenceUtil.setSID(storeLogin.getData().getSId());
//                                sharePreferenceUtil.setSType(storeLogin.getData().getSType() + "");
//                                sharePreferenceUtil.setUAccount(etAccount.getText().toString().trim());
//                                /**
//                                 * 网易云
//                                 */
//                                sharePreferenceUtil.saveUserToken(storeLogin.getData().getToken());
//                                sharePreferenceUtil.saveAccid(storeLogin.getData().getAccid());
//                                showShortToast(LoginActivity.this, getResources().getString(
//                                        R.string.loginSuccess));
//                                gotoMainActivity();
                            } else {
                                showShortToast(LoginActivity.this,
                                        storeLogin.getErrorString());

                            }
                        }
                    });
        } else {
            showShortToast(LoginActivity.this, Constants.NETWORK_ERROR);
        }
    }
}
