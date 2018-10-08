package app.cn.extra.mall.user.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.PhoneUtil;
import app.cn.extra.mall.user.vo.CommonVo;
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
 * 忘记密码
 */
public class ForgetPasswordActivity extends BaseActivty {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.et_phoneNum)
    EditText etPhoneNum;
    @BindView(R.id.et_identifying_code)
    EditText etIdentifyingCode;
    @BindView(R.id.getCodeBtn)
    Button getCodeBtn;
    @BindView(R.id.retrievePasswordBtn)
    Button retrievePasswordBtn;

    /**
     * 声明倒计时工具类对象
     */
    private CountDownTimerUtil mCountDownTimerUtil;
    private int countDownInterval = 0;
    /**
     * 获取到的验证码
     */
    String CAPTCHA = "";

    /**
     * 手机号
     */
    String uTel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_back, R.id.getCodeBtn, R.id.retrievePasswordBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.getCodeBtn:
                setGetCodeBtnClickable(false);
                uTel = etPhoneNum.getText().toString().trim();
                if (TextUtils.isEmpty(uTel)) {
                    showShortToast(ForgetPasswordActivity.this, "请输入手机号！");
                    setGetCodeBtnClickable(true);
                    break;
                }
                if (!PhoneUtil.isChinaPhoneLegal(uTel)) {
                    showShortToast(ForgetPasswordActivity.this, "手机号格式错误，请重新输入！");
                    setGetCodeBtnClickable(true);
                    break;
                }
                FP_CAPTCHA(uTel);
                break;
            case R.id.retrievePasswordBtn:
                setRetrievePasswordBtnClickable(false);
                String verificationCode = etIdentifyingCode.getText().toString().trim();
                if (TextUtils.isEmpty(verificationCode)) {
                    showShortToast(ForgetPasswordActivity.this, "请输入验证码！");
                    setRetrievePasswordBtnClickable(true);
                    break;
                }
                if (!CAPTCHA.equals(verificationCode)) {
                    showShortToast(ForgetPasswordActivity.this, "验证码错误！");
                    setRetrievePasswordBtnClickable(true);
                    break;
                }
//                Intent intent = new Intent(ForgetPasswordActivity.this, ResetPasswordActivity.class);
//                intent.putExtra("uTel", uTel);
//                startActivity(intent);
                retrievePswPort();
                break;
            default:
                break;
        }
    }

    /**
     * 找回密码接口
     */
    private void retrievePswPort() {
        if (Utils.isNetworkAvailable(ForgetPasswordActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.retrievePsw)
                    .addParams("uAccount", uTel)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            /**
                             * 接口调用出错
                             */
                            showShortToast(ForgetPasswordActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            CommonVo commonVo = new CommonVo();
                            commonVo = Utils.parserJsonResult(response, CommonVo.class);
                            if (Constants.OK.equals(commonVo.getFlag())) {
                                if (Constants.OK.equals(commonVo.getData().getStatus())) {
                                    showShortToast(ForgetPasswordActivity.this,
                                            getResources().getString(R.string.captchaTip2));
                                    finish();
                                } else {
                                    showShortToast(ForgetPasswordActivity.this,
                                            commonVo.getData().getErrorString());
                                }
                            } else {
                                showShortToast(ForgetPasswordActivity.this,
                                        commonVo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(ForgetPasswordActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 忘记密码短信验证接口
     *
     * @param uTel 手机号
     */
    private void FP_CAPTCHA(String uTel) {
        if (Utils.isNetworkAvailable(ForgetPasswordActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.FP_CAPTCHA)
                    .addParams("gAppType", Constants.APP_TYPE)
                    .addParams("uTel", uTel + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Utils.LogE(e.toString());
                            setGetCodeBtnClickable(true);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String flag = jsonObject.getString("flag");
                                String data = jsonObject.getString("data");
                                JSONObject jsonObject1 = new JSONObject(data);
                                CAPTCHA = jsonObject1.getString("CAPTCHA");
                                if (Constants.OK.equals(flag)) {
                                    initCountDownTimer();
                                } else {
                                    String errorString = jsonObject.getString("errorString");
                                    showShortToast(ForgetPasswordActivity.this, errorString);
                                    setGetCodeBtnClickable(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            showShortToast(ForgetPasswordActivity.this, Constants.NETWORK_ERROR);
            setGetCodeBtnClickable(true);
        }
    }

    /**
     * 初始化计时器相关配置
     */
    @SuppressLint("DefaultLocale")
    private void initCountDownTimer() {
        // 倒计时总时间为8S
        int millisInFuture = 60;
        // 每隔1s回调一次onTick
        countDownInterval = 1;
        // 将时间显示到界面
        getCodeBtn.setText(String.format("%d秒后重新发送", millisInFuture));
        // 开始倒计时,传入总时间和每一秒要倒计时的时间
        countDown(millisInFuture, countDownInterval);
        // 开始执行
        mCountDownTimerUtil.start();
    }

    /**
     * 开始倒计时,传入总时间和每一秒要倒计时的时间
     */
    @SuppressLint("DefaultLocale")
    private void countDown(final long millisInFuture, long countDownInterval) {
        mCountDownTimerUtil = CountDownTimerUtil.getCountDownTimer()
                // 倒计时总时间
                .setMillisInFuture(millisInFuture * 1000)
                // 每隔多久回调一次onTick
                .setCountDownInterval(countDownInterval * 1000)
                // 每回调一次onTick执行
                .setTickDelegate(pMillisUntilFinished -> {
                    // 将时间显示到界面
                    getCodeBtn.setText(String.format("%d秒后重新发送", pMillisUntilFinished / 1000));
                })
                // 结束倒计时执行
                .setFinishDelegate(() -> setGetCodeBtnClickable(true));
    }

    /**
     * 设置获取验证码按钮显示效果与是否可以点击
     *
     * @param i i=true可以点击；i=false不可点击
     */
    private void setGetCodeBtnClickable(boolean i) {
        if (i) {
            getCodeBtn.setText("获取验证码");
            getCodeBtn.setBackgroundResource(R.drawable.line_btn_blue);
            getCodeBtn.setTextColor(getResources().getColor(R.color.blue));
            getCodeBtn.setEnabled(true);
        } else {
            getCodeBtn.setBackgroundResource(R.drawable.five_gray_bg_circle);
            getCodeBtn.setTextColor(getResources().getColor(R.color.white));
            getCodeBtn.setEnabled(false);
        }
    }

    /**
     * 设置找回密码按钮显示效果与是否可以点击
     *
     * @param i i=true可以点击；i=false不可点击
     */
    private void setRetrievePasswordBtnClickable(boolean i) {
        if (i) {
            retrievePasswordBtn.setBackgroundResource(R.drawable.shape_btn_blue);
            retrievePasswordBtn.setTextColor(getResources().getColor(R.color.white));
            retrievePasswordBtn.setEnabled(true);
        } else {
            retrievePasswordBtn.setBackgroundResource(R.drawable.gray_bg_circle);
            retrievePasswordBtn.setTextColor(getResources().getColor(R.color.white));
            retrievePasswordBtn.setEnabled(false);
        }
    }
}
