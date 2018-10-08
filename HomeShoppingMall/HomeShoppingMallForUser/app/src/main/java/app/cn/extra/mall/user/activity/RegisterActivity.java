package app.cn.extra.mall.user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.im.ContactHttpClient;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.PhoneUtil;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.CountDownTimerUtil;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * 注册页
 */
public class RegisterActivity extends BaseActivty {
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.phone_layout)
    LinearLayout phoneLayout;
    @BindView(R.id.get_code_layout)
    RelativeLayout getCodeLayout;
    @BindView(R.id.phoneNum_text)
    TextView textPhoneNum;
    @BindView(R.id.num_text)
    TextView textNum;
    @BindView(R.id.againBtn)
    Button againBtn;
    @BindView(R.id.et_phoneNum)
    EditText etPhoneNum;
    @BindView(R.id.password_layout)
    LinearLayout layoutPassword;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_again)
    EditText etPasswordAgain;
    @BindView(R.id.nextBtn)
    Button nextBtn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    /**
     * 用来判断当前注册进度
     */
    int flag = 1;
    /**
     * 输入手机号
     */
    final int INPUT_PHONE_NUM = 1;
    /**
     * 输入验证码
     */
    final int INPUT_VERIFICATION_CODE = 2;
    /**
     * 设置密码
     */
    final int SET_PASSWORD = 3;
    /**
     * 声明倒计时工具类对象
     */
    private CountDownTimerUtil mCountDownTimerUtil;
    private int countDownInterval = 0;
    /**
     * 密码最少位数
     */
    int minPasswrodLength = 6;
    /**
     * 手机号
     */
    String phone = "";
    /**
     * 获取到的验证码
     */
    String CAPTCHA = "";
    SharePreferenceUtil sharePreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(RegisterActivity.this, Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
    }

    @OnClick({R.id.img_back, R.id.nextBtn, R.id.againBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.nextBtn:
                clickNextBtn(flag);
                break;
            case R.id.againBtn:
                setAgainBtnClickable(false);
                initCountDownTimer();
                break;
            default:
                break;
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
        textNum.setText(String.format("%d秒后重试", millisInFuture));
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
                    textNum.setText(String.format("%d秒后重试", pMillisUntilFinished / 1000));
                })
                // 结束倒计时执行
                .setFinishDelegate(() -> setAgainBtnClickable(true));
    }

    /**
     * 设置重新获取验证码按钮显示效果与是否可以点击
     *
     * @param i i=true可以点击；i=false不可点击
     */
    private void setAgainBtnClickable(boolean i) {
        if (i) {
            textNum.setText("请重试");
            againBtn.setBackgroundResource(R.drawable.line_btn_blue);
            againBtn.setTextColor(getResources().getColor(R.color.blue));
            againBtn.setEnabled(true);
        } else {
            againBtn.setBackgroundResource(R.drawable.five_gray_bg_circle);
            againBtn.setTextColor(getResources().getColor(R.color.white));
            againBtn.setEnabled(false);
        }
    }

    /**
     * 设置下一步按钮显示效果与是否可以点击
     *
     * @param i i=true可以点击；i=false不可点击
     */
    private void setNextBtnClickable(boolean i) {
        if (i) {
            nextBtn.setBackgroundResource(R.drawable.five_radius_btn_blue);
            nextBtn.setTextColor(getResources().getColor(R.color.white));
            nextBtn.setEnabled(true);
        } else {
            nextBtn.setBackgroundResource(R.drawable.five_gray_bg_circle);
            nextBtn.setTextColor(getResources().getColor(R.color.white));
            nextBtn.setEnabled(false);
        }
    }

    /**
     * 通过判断传入的flag值进行不同的处理
     *
     * @param f
     */
    private void clickNextBtn(int f) {
        setNextBtnClickable(false);
        switch (f) {
            case INPUT_PHONE_NUM:
                String phoneNum = etPhoneNum.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    showShortToast(RegisterActivity.this, "请输入您的手机号！");
                    setNextBtnClickable(true);
                    break;
                }
                if (!PhoneUtil.isChinaPhoneLegal(phoneNum)) {
                    showShortToast(RegisterActivity.this, "手机号格式错误，请重新输入！");
                    setNextBtnClickable(true);
                    break;
                }
                //正确
                phone = phoneNum;
                UR_CAPTCHA(phoneNum);
                break;
            case INPUT_VERIFICATION_CODE:
                String verificationCode = etPhoneNum.getText().toString().trim();
                if (TextUtils.isEmpty(verificationCode)) {
                    showShortToast(RegisterActivity.this, "请输入验证码！");
                    setNextBtnClickable(true);
                    break;
                }
                if (!CAPTCHA.equals(verificationCode)) {
                    showShortToast(RegisterActivity.this, "验证码错误！");
                    setNextBtnClickable(true);
                    break;
                }
                returnTrue(INPUT_VERIFICATION_CODE, "", null);
                break;
            case SET_PASSWORD:
                String password = etPassword.getText().toString().trim();
                String againPassword = etPasswordAgain.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    showShortToast(RegisterActivity.this, "请输入密码！");
                    setNextBtnClickable(true);
                    break;
                }
                if (TextUtils.isEmpty(againPassword)) {
                    showShortToast(RegisterActivity.this, "请再次输入密码！");
                    setNextBtnClickable(true);
                    break;
                }
                if (password.length() < minPasswrodLength) {
                    showShortToast(RegisterActivity.this, "请输入6位以上的密码！");
                    setNextBtnClickable(true);
                    break;
                }
                if (!password.equals(againPassword)) {
                    showShortToast(RegisterActivity.this, "请输入相同的密码！");
                    setNextBtnClickable(true);
                    break;
                }
                if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
                //正确
                userRegistered(phone, password);
                break;
            default:
                break;
        }
    }

    /**
     * 用户注册接口
     *
     * @param uAccount  手机号
     * @param uPassword 密码
     */
    private void userRegistered(String uAccount, String uPassword) {
        if (Utils.isNetworkAvailable(RegisterActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.userRegistered)
                    .addParams("gAppType", Constants.APP_TYPE)
                    .addParams("uAccount", uAccount + "")
                    .addParams("uPassword", uPassword + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Utils.LogE(e.toString());
                            setNextBtnClickable(true);
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String flag = jsonObject.getString("flag");
                                String data = jsonObject.getString("data");
                                JSONObject jsonObject1 = new JSONObject(data);
                                if (Constants.OK.equals(flag)) {
                                    if (jsonObject1.getBoolean("status")) {
                                        CustomToast.showToast(RegisterActivity.this, "注册成功！", Toast.LENGTH_LONG);
                                        returnTrue(SET_PASSWORD, "", jsonObject);
                                    } else {
                                        String errorString = jsonObject1.getString("errorString");
                                        showShortToast(RegisterActivity.this, errorString);
                                        setNextBtnClickable(true);
                                    }
                                } else {
                                    String errorString = jsonObject.getString("errorString");
                                    showShortToast(RegisterActivity.this, errorString);
                                    setNextBtnClickable(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            showShortToast(RegisterActivity.this, Constants.NETWORK_ERROR);
            setNextBtnClickable(true);
            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
        }
    }

    /**
     * 注册短信验证接口
     *
     * @param uTel 手机号
     */
    private void UR_CAPTCHA(String uTel) {
        if (Utils.isNetworkAvailable(RegisterActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.UR_CAPTCHA)
                    .addParams("gAppType", Constants.APP_TYPE)
                    .addParams("uTel", uTel + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Utils.LogE(e.toString());
                            setNextBtnClickable(true);
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
                                    System.out.println("CAPTCHA:" + CAPTCHA);
                                    CustomToast.showToast(RegisterActivity.this,
                                            getResources().getString(R.string.captchaTip), Toast.LENGTH_SHORT);
                                    returnTrue(INPUT_PHONE_NUM, uTel, null);
                                } else {
                                    String errorString = jsonObject.getString("errorString");
                                    showShortToast(RegisterActivity.this, errorString);
                                    setNextBtnClickable(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            showShortToast(RegisterActivity.this, Constants.NETWORK_ERROR);
            setNextBtnClickable(true);
        }
    }

    /**
     * 返回正确时调用
     *
     * @param f        flag
     * @param phoneNum 手机号
     */
    private void returnTrue(int f, String phoneNum, JSONObject jsonObject) {
        switch (f) {
            case INPUT_PHONE_NUM:
                getCodeLayout.setVisibility(View.VISIBLE);
                etPhoneNum.setText("");
                etPhoneNum.setHint("输入验证码");
                textPhoneNum.setText(phoneNum);
                imageView.setBackgroundResource(R.mipmap.icon_zhuce2);
                nextBtn.setText("下一步");
                setNextBtnClickable(true);
                initCountDownTimer();
                flag = INPUT_VERIFICATION_CODE;
                break;
            case INPUT_VERIFICATION_CODE:
                phoneLayout.setVisibility(View.GONE);
                layoutPassword.setVisibility(View.VISIBLE);
                imageView.setBackgroundResource(R.mipmap.icon_zhece3);
                nextBtn.setText("注册");
                setNextBtnClickable(true);
                flag = SET_PASSWORD;
                break;
            case SET_PASSWORD:
                try {
                    String data = jsonObject.getString("data");
                    JSONObject jsonObject1 = new JSONObject(data);
                    /**
                     * 网易云
                     */
                    sharePreferenceUtil.saveUserToken(jsonObject1.getString("token"));
                    sharePreferenceUtil.saveAccid(jsonObject1.getString("accid"));
                    /**
                     * user
                     */
                    sharePreferenceUtil.setUID(jsonObject1.getString("uId"));
                    sharePreferenceUtil.setUTel(phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                closeAll();
                break;
            default:
                break;
        }
    }

    /**
     * ***************************************** 网易云注册 **************************************
     */

    private void register() {
        if (!NetworkUtil.isNetAvailable(RegisterActivity.this)) {
            Toast.makeText(RegisterActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        DialogMaker.showProgressDialog(this, getString(R.string.registering), false);

        // 注册流程
        final String account = "";
        final String nickName = "";
        final String password = "";

        ContactHttpClient.getInstance().register(account, nickName, password, new ContactHttpClient.ContactHttpCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                switchMode();  // 切换回登录
                DialogMaker.dismissProgressDialog();
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                Toast.makeText(RegisterActivity.this, R.string.register_failed, Toast.LENGTH_SHORT)
                        .show();
                DialogMaker.dismissProgressDialog();
            }
        });
    }
}
