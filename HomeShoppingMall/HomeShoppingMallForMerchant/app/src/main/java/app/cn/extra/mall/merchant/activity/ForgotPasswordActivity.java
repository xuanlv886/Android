package app.cn.extra.mall.merchant.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.vo.CommonVo;
import app.cn.extra.mall.merchant.vo.GetCaptcha;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.CountDownTimerUtil;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 忘记密码页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class ForgotPasswordActivity extends BaseActivty {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.et_tel)
    EditText etTel;
    @BindView(R.id.btn_captcha)
    Button btnCaptcha;
    @BindView(R.id.et_captcha)
    EditText etCaptcha;
    @BindView(R.id.btn_retrieve)
    Button btnRetrieve;

    /**
     * 声明倒计时工具类对象
     */
    private CountDownTimerUtil mCountDownTimerUtil;
    /**
     * 临时存储短信验证码
     */
    private String captchaCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_back, R.id.btn_captcha, R.id.btn_retrieve})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_captcha:
                doGetCaptcha();
                break;
            case R.id.btn_retrieve:
                doRetrieve();
                break;
                default:
        }
    }

    /**
     * 点击找回密码按钮执行的操作
     */
    private void doRetrieve() {
        /**
         * 验证手机号合法性
         */
        if (!Utils.isValidPhoneNumber(etTel.getText().toString().trim())) {
            showShortToast(ForgotPasswordActivity.this,
                    getResources().getString(R.string.phoneNumError));
            return;
        }
        /**
         * 验证验证码
         */
        if (TextUtils.isEmpty(etCaptcha.getText().toString().trim())) {
            showShortToast(ForgotPasswordActivity.this,
                    getResources().getString(R.string.captchaEmpty));
            return;
        } else {
            if (!captchaCode.equals(etCaptcha.getText().toString().trim())) {
                showShortToast(ForgotPasswordActivity.this,
                        getResources().getString(R.string.captchaError));
                return;
            }
        }
        retrievePswPort();
    }

    /**
     * 找回密码接口
     */
    private void retrievePswPort() {
        if (Utils.isNetworkAvailable(ForgotPasswordActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.retrievePsw)
                    .addParams("uAccount", etTel.getText().toString().trim())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            /**
                             * 接口调用出错
                             */
                            showShortToast(ForgotPasswordActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Utils.LogJson(response);
                            CommonVo commonVo = new CommonVo();
                            commonVo = Utils.parserJsonResult(response, CommonVo.class);
                            if (Constants.OK.equals(commonVo.getFlag())) {
                                if (Constants.OK.equals(commonVo.getData().getStatus())) {
                                    showShortToast(ForgotPasswordActivity.this,
                                            getResources().getString(R.string.captchaTip2));
                                    finish();
                                } else {
                                    showShortToast(ForgotPasswordActivity.this,
                                            commonVo.getData().getErrorString());
                                }
                            } else {
                                showShortToast(ForgotPasswordActivity.this,
                                        commonVo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(ForgotPasswordActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 点击获取验证码执行的操作
     */
    private void doGetCaptcha() {
        /**
         * 验证手机号合法性
         */
        if (Utils.isValidPhoneNumber(etTel.getText().toString().trim())) {
            initCountDownTimer();
            getCaptchaPort();
        } else {
            showShortToast(ForgotPasswordActivity.this,
                    getResources().getString(R.string.phoneNumError));
        }
    }

    /**
     * 初始化计时器相关配置
     */
    @SuppressLint("DefaultLocale")
    private void initCountDownTimer() {
        // 倒计时总时间为60S
        int millisInFuture = 60;
        // 将时间显示到界面
        btnCaptcha.setText(millisInFuture + "秒后重新获取");
        // 开始倒计时,传入总时间和每一秒要倒计时的时间
        countDown(millisInFuture , 1);
        // 开始执行
        mCountDownTimerUtil.start();
    }

    /**
     * 开始倒计时,传入总时间和每一秒要倒计时的时间
     */
    @SuppressLint("DefaultLocale")
    private void countDown(final long millisInFuture , long countDownInterval) {
        mCountDownTimerUtil = CountDownTimerUtil.getCountDownTimer()
                // 倒计时总时间
                .setMillisInFuture(millisInFuture * 1000)
                // 每隔多久回调一次onTick
                .setCountDownInterval(countDownInterval * 1000)
                // 每回调一次onTick执行
                .setTickDelegate(pMillisUntilFinished -> {
                    // 将时间显示到界面
                    btnCaptcha.setTextColor(getResources().getColor(R.color.graytext));
                    btnCaptcha.setBackgroundResource(R.drawable.shape_btn_gray);
                    btnCaptcha.setClickable(false);
                    btnCaptcha.setText(pMillisUntilFinished / 1000 + "秒后重新获取");
                })
                // 倒计时结束
                .setFinishDelegate(() -> {
                    btnCaptcha.setTextColor(getResources().getColor(R.color.white));
                    btnCaptcha.setBackgroundResource(R.drawable.shape_btn_blue);
                    btnCaptcha.setClickable(true);
                    btnCaptcha.setText("重新获取验证码");
                });
    }

    /**
     * 获取短信验证码接口
     */
    private void getCaptchaPort() {
        if (Utils.isNetworkAvailable(ForgotPasswordActivity.this)) {
            showShortToast(ForgotPasswordActivity.this,
                    getResources().getString(R.string.captchaTip));
            OkHttpUtils
                    .post()
                    .url(Constants.getCaptcha)
                    .addParams("uTel", etTel.getText().toString().trim())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            /**
                             * 接口调用出错
                             */
                            showShortToast(ForgotPasswordActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Utils.LogJson(response);
                            GetCaptcha getCaptcha = new GetCaptcha();
                            getCaptcha = Utils.parserJsonResult(response, GetCaptcha.class);
                            if (Constants.OK.equals(getCaptcha.getFlag())) {
                                captchaCode = getCaptcha.getData().getCAPTCHA();
                            }
                        }
                    });
        } else {
            showShortToast(ForgotPasswordActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mCountDownTimerUtil) {
            mCountDownTimerUtil.cancel();
        }
    }
}
