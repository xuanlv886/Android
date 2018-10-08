package app.cn.extra.mall.user.activity;

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

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.SelectAuthenticationTypeEvent;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.CommonVo;
import app.cn.extra.mall.user.vo.GetCAPTCHA;
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
//                if (captchaCode.equals(etCaptcha.getText().toString().trim())) {
//                    switch (sharePreferenceUtil.getPassWordFlag()) {
//                        case Constants.FORGET_PASSWORD:
//                            retrievePswPort();
//                            break;
//                        case Constants.REVISE_PASSWORD:
//                            EventBus.getDefault().post(new SelectAuthenticationTypeEvent());
//                            finish();
//                            break;
//                        default:
//                            gotoMainActivity();
//                            break;
//                    }
//                }
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
        switch (sharePreferenceUtil.getPassWordFlag()) {
            case Constants.FORGET_PASSWORD:
                retrievePswPort();
                break;
            case Constants.REVISE_PASSWORD:
                EventBus.getDefault().post(new SelectAuthenticationTypeEvent());
                finish();
                break;
            default:
                gotoMainActivity();
                break;
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
                    .addParams("uAccount", sharePreferenceUtil.getUTel())
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

                            GetCAPTCHA getCaptcha = new GetCAPTCHA();
                            getCaptcha = Utils.parserJsonResult(response, GetCAPTCHA.class);
                            if (Constants.OK.equals(getCaptcha.getFlag())) {
                                captchaCode = getCaptcha.getData().getCAPTCHA();
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
