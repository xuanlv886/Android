package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;

import org.greenrobot.eventbus.EventBus;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.SelectAuthenticationTypeEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.IMLogin;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.CommonVo;
import app.cn.extra.mall.merchant.vo.GetCaptcha;
import app.cn.extra.mall.merchant.vo.UpdateUserPhoneIdAndReturnUserInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 手机验证页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class PhoneAuthActivity extends BaseActivty {

    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.et_captcha)
    EditText etCaptcha;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    /**
     * 临时存储手机号
     */
    private String tel = "";
    /**
     * 临时存储用户id
     */
    private String uId = "";
    /**
     * 临时存储获取的验证码
     */
    private String captchaCode = "";
    private SharePreferenceUtil sharePreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(PhoneAuthActivity.this, Constants.SAVE_USER);
        Intent intent = getIntent();
        tel = intent.getStringExtra("tel");
        uId = intent.getStringExtra("uId");
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        doGetCaptcha();
    }

    @OnClick({R.id.tv_next, R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                doSubmit();
                break;
            case R.id.img_back:
                finish();
                break;
            default:
        }
    }

    /**
     * 提交按钮执行的操作
     */
    private void doSubmit() {
        /**
         * 校验验证码
         */
        if (TextUtils.isEmpty(etCaptcha.getText().toString().trim())) {
            showShortToast(PhoneAuthActivity.this,
                    getResources().getString(R.string.captchaEmpty));
            return;
        } else {
            if (!captchaCode.equals(etCaptcha.getText().toString().trim())) {
                showShortToast(PhoneAuthActivity.this,
                        getResources().getString(R.string.captchaError));
                return;
            }
        }
//        updateUserPhoneIdAndReturnUserInfoPort();
        /**
         * 验证成功，保存数据并跳转至主界面
         */
        switch (sharePreferenceUtil.getPassWordFlag()) {
            case Constants.FORGET_PASSWORD:
                retrievePswPort();
                break;
            case Constants.REVISE_PASSWORD:
                EventBus.getDefault().post(new SelectAuthenticationTypeEvent());
                finish();
                break;
            default:
                updateUserPhoneIdAndReturnUserInfoPort();
                break;
        }
    }

    /**
     * 更新用户设备ID并返回用户信息接口
     */
    private void updateUserPhoneIdAndReturnUserInfoPort() {
        if (Utils.isNetworkAvailable(PhoneAuthActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.updateUserPhoneIdAndReturnUserInfo)
                    .addParams("uId", uId)
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
                            showShortToast(PhoneAuthActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            UpdateUserPhoneIdAndReturnUserInfo data = new UpdateUserPhoneIdAndReturnUserInfo();
                            data = Utils.parserJsonResult(response, UpdateUserPhoneIdAndReturnUserInfo.class);
                            if (Constants.OK.equals(data.getFlag())) {
                                if (Constants.OK.equals(data.getData().getStatus())) {
                                    /**
                                     * 验证成功，保存数据并跳转至主界面
                                     */
                                    sharePreferenceUtil.setStoreAcId(data.getData().getAcId());
                                    sharePreferenceUtil.setUID(uId);
                                    sharePreferenceUtil.setUAccount(tel);
                                    sharePreferenceUtil.setSID(data.getData().getSId());
                                    sharePreferenceUtil.setSType(data.getData().getSType() + "");
                                    sharePreferenceUtil.setPhoneId(data.getData().getuPhoneId());
                                    /**
                                     * 云信登录
                                     */
                                    IMLogin imLogin = new IMLogin(PhoneAuthActivity.this);
                                    imLogin.login(0);
                                } else {
                                    showShortToast(PhoneAuthActivity.this,
                                            data.getData().getErrorString());
                                }
                            } else {
                                showShortToast(PhoneAuthActivity.this,
                                        data.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(PhoneAuthActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 找回密码接口
     */
    private void retrievePswPort() {
        if (Utils.isNetworkAvailable(PhoneAuthActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.retrievePsw)
                    .addParams("uAccount", sharePreferenceUtil.getUAccount())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            /**
                             * 接口调用出错
                             */
                            showShortToast(PhoneAuthActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Utils.LogJson(response);
                            CommonVo commonVo = new CommonVo();
                            commonVo = Utils.parserJsonResult(response, CommonVo.class);
                            if (Constants.OK.equals(commonVo.getFlag())) {
                                if (Constants.OK.equals(commonVo.getData().getStatus())) {
                                    showShortToast(PhoneAuthActivity.this,
                                            getResources().getString(R.string.captchaTip2));
                                    EventBus.getDefault().post(new SelectAuthenticationTypeEvent());
                                    finish();
                                } else {
                                    showShortToast(PhoneAuthActivity.this,
                                            commonVo.getData().getErrorString());
                                }
                            } else {
                                showShortToast(PhoneAuthActivity.this,
                                        commonVo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(PhoneAuthActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 获取验证码
     */
    private void doGetCaptcha() {
        /**
         * 验证手机号合法性
         */
        if (Utils.isValidPhoneNumber(tel)) {
            getCaptchaPort();
        } else {
            showShortToast(PhoneAuthActivity.this,
                    getResources().getString(R.string.portError));
        }
    }

    /**
     * 获取短信验证码接口
     */
    private void getCaptchaPort() {
        if (Utils.isNetworkAvailable(PhoneAuthActivity.this)) {
            showShortToast(PhoneAuthActivity.this,
                    getResources().getString(R.string.captchaTip));
            OkHttpUtils
                    .post()
                    .url(Constants.getCaptcha)
                    .addParams("uTel", tel)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            /**
                             * 接口调用出错
                             */
                            showShortToast(PhoneAuthActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Utils.LogJson(response);
                            GetCaptcha getCaptcha = new GetCaptcha();
                            getCaptcha = Utils.parserJsonResult(response, GetCaptcha.class);
                            if (Constants.OK.equals(getCaptcha.getFlag())) {
                                captchaCode = getCaptcha.getData().getCAPTCHA();
                                System.out.println("CAPTCHA:" + captchaCode);
                            }
                        }
                    });
        } else {
            showShortToast(PhoneAuthActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 跳转至主界面
     */
    private void gotoMainActivity() {
        Intent intent = new Intent();
        intent.setClass(PhoneAuthActivity.this, MainActivity.class);
        startActivity(intent);
        closeAll();
    }
}
