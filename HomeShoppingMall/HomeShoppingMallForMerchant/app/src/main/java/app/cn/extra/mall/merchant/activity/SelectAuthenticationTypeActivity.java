package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.SelectAuthenticationTypeEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.CommonVo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 选择身份验证方式界面
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class SelectAuthenticationTypeActivity extends BaseActivty {

    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.btn_tel)
    Button btnTel;
    @BindView(R.id.btn_safety_question)
    Button btnSafetyQuestion;
    /**
     * 用以标识用户选择的身份验证类型 0--尚未进行选择 1--手机验证，2--安全问题验证
     */
    private int whichSelect = 0;
    /**
     * 临时存储用户uId
     */
    private String uId = "";

    /**
     * 临时存储用户手机号
     */
    private String tel = "";
    SharePreferenceUtil sharePreferenceUtil = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_authentication_type);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(this, Constants.SAVE_USER);
        Intent intent = getIntent();
        uId = intent.getStringExtra("uId");
        tel = intent.getStringExtra("tel");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 消息处理
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SelectAuthticationTypeEventBus(SelectAuthenticationTypeEvent event) {
        switch (sharePreferenceUtil.getPassWordFlag()) {
            case Constants.FORGET_PASSWORD:
//                gotoForgotPasswordActivity();
                sharePreferenceUtil.setPassWordFlag("");
                gotoMainActivity();
                break;
            case Constants.REVISE_PASSWORD:
                sharePreferenceUtil.setPassWordFlag("");
                gotoChangePasswordActivity();
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.tv_next, R.id.img_back, R.id.btn_tel, R.id.btn_safety_question})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                doNext();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_tel:
                btnTel.setBackgroundResource(R.drawable.shape_btn_blue);
                btnSafetyQuestion.setBackgroundResource(R.drawable.shape_btn_gray);
                btnTel.setTextColor(getResources().getColor(R.color.white));
                btnSafetyQuestion.setTextColor(getResources().getColor(R.color.black));
                whichSelect = 1;
                break;
            case R.id.btn_safety_question:
                btnSafetyQuestion.setBackgroundResource(R.drawable.shape_btn_blue);
                btnTel.setBackgroundResource(R.drawable.shape_btn_gray);
                btnSafetyQuestion.setTextColor(getResources().getColor(R.color.white));
                btnTel.setTextColor(getResources().getColor(R.color.black));
                whichSelect = 2;
                break;
            default:
        }
    }

    /**
     * 执行后续逻辑，根据注册的商户类型跳转至对应的注册界面
     */
    private void doNext() {
        switch (whichSelect) {
            /**
             * 尚未进行选择
             */
            case 0:
                showShortToast(SelectAuthenticationTypeActivity.this,
                        getResources().getString(R.string.authNotSelect));
                break;
            /**
             * 手机验证
             */
            case 1:
                gotoPhoneAuthActivity();
                break;
            /**
             * 密保问题验证
             */
            case 2:
                if (!TextUtils.isEmpty(uId)) {
                    msGetStatusOfSafetyQuestionsPort();
                } else {
                    showShortToast(SelectAuthenticationTypeActivity.this,
                            getResources().getString(R.string.portError));
                }
                break;
            default:
        }
    }

    /**
     * 是否设置过密保问题接口
     */
    private void msGetStatusOfSafetyQuestionsPort() {
        if (Utils.isNetworkAvailable(SelectAuthenticationTypeActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.msGetStatusOfSafetyQuestions)
                    .addParams("uId", uId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            /**
                             * 接口调用出错
                             */
                            showShortToast(SelectAuthenticationTypeActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Utils.LogJson(response);
                            CommonVo commonVo = new CommonVo();
                            commonVo = Utils.parserJsonResult(response, CommonVo.class);
                            if (Constants.OK.equals(commonVo.getFlag())) {
                                if (Constants.OK.equals(commonVo.getData().getStatus())) {
                                    gotoSatetyQuestionAuthActivity();
                                } else {
                                    showShortToast(SelectAuthenticationTypeActivity.this,
                                            getResources().getString(
                                                    R.string.haveNotSetSafetyQuestion));
                                }
                            } else {
                                showShortToast(SelectAuthenticationTypeActivity.this,
                                        commonVo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(SelectAuthenticationTypeActivity.this, Constants.NETWORK_ERROR);
        }
    }

    /**
     * 跳转至手机验证界面
     */
    private void gotoPhoneAuthActivity() {
        Intent intent = new Intent();
        intent.setClass(SelectAuthenticationTypeActivity.this, PhoneAuthActivity.class);
        intent.putExtra("tel", tel);
        intent.putExtra("uId", uId);
        startActivity(intent);
    }

    /**
     * 跳转至安全问题验证界面
     */
    private void gotoSatetyQuestionAuthActivity() {
        Intent intent = new Intent();
        intent.setClass(SelectAuthenticationTypeActivity.this,
                SafetyQuestionAuthActivity.class);
        intent.putExtra("tel", tel);
        intent.putExtra("uId", uId);
        startActivity(intent);
    }

    /**
     * 跳转至忘记密码页
     */
    private void gotoForgotPasswordActivity() {
        Intent intent = new Intent();
        intent.setClass(SelectAuthenticationTypeActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转至修改密码页
     */
    private void gotoChangePasswordActivity() {
        Intent intent = new Intent();
        intent.setClass(SelectAuthenticationTypeActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转至主界面
     */
    private void gotoMainActivity() {
        Intent intent = new Intent();
        intent.setClass(SelectAuthenticationTypeActivity.this, MainActivity.class);
        startActivity(intent);
        closeAll();
    }
}
