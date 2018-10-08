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

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.SelectAuthenticationTypeEvent;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.CommonVo;
import app.cn.extra.mall.user.vo.MsGetSafetyQuestionOfUser;
import app.cn.extra.mall.user.vo.SafetyQuestionBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 安全问题验证页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class SafetyQuestionAuthActivity extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_question_first)
    TextView tvQuestionFirst;
    @BindView(R.id.et_answer_first)
    EditText etAnswerFirst;
    @BindView(R.id.tv_question_second)
    TextView tvQuestionSecond;
    @BindView(R.id.et_answer_second)
    EditText etAnswerSecond;
    @BindView(R.id.tv_question_third)
    TextView tvQuestionThird;
    @BindView(R.id.et_answer_third)
    EditText etAnswerThird;
    /**
     * 临时存储手机号
     */
    private String tel = "";
    /**
     * 临时存储用户id
     */
    private String uId = "";
    private SharePreferenceUtil sharePreferenceUtil;
    private MsGetSafetyQuestionOfUser data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_satety_question_auth);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(SafetyQuestionAuthActivity.this,
                Constants.SAVE_USER);
        Intent intent = getIntent();
        tel = intent.getStringExtra("tel");
        uId = intent.getStringExtra("uId");
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        msGetSafetyQuestionOfUserPort();
    }

    /**
     * 获取某用户已设置的密保问题的接口
     */
    private void msGetSafetyQuestionOfUserPort() {
        if (Utils.isNetworkAvailable(SafetyQuestionAuthActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.msGetSafetyQuestionOfUser)
                    .addParams("uId", uId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            /**
                             * 接口调用出错
                             */
                            showShortToast(SafetyQuestionAuthActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }

                            data = new MsGetSafetyQuestionOfUser();
                            data = Utils.parserJsonResult(response, MsGetSafetyQuestionOfUser.class);
                            if (Constants.OK.equals(data.getFlag())) {
                                /**
                                 * 填充密保问题
                                 */
                                for (int i = 0; i < data.getData().size(); i++) {
                                    switch (data.getData().get(i).getSqPosition()) {
                                        case 1:
                                            tvQuestionFirst.setText("问题1： " + data.getData()
                                                    .get(i).getSqName());
                                            break;
                                        case 2:
                                            tvQuestionSecond.setText("问题2： " + data.getData()
                                                    .get(i).getSqName());
                                            break;
                                        case 3:
                                            tvQuestionThird.setText("问题3： " + data.getData()
                                                    .get(i).getSqName());
                                            break;
                                        default:
                                    }
                                }
                            } else {
                                showShortToast(SafetyQuestionAuthActivity.this,
                                        data.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(SafetyQuestionAuthActivity.this,
                    Constants.NETWORK_ERROR);
        }
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
         * 验证是否成功获取了密保问题的数据
         */
        if (null == data.getData() || 0 == data.getData().size()) {
            showShortToast(SafetyQuestionAuthActivity.this, getResources()
                    .getString(R.string.authError));
            return;
        }
        /**
         * 验证安全问题的答案是否均已填写
         */
        if (TextUtils.isEmpty(etAnswerFirst.getText().toString().trim())) {
            showShortToast(SafetyQuestionAuthActivity.this, getResources()
                    .getString(R.string.yourAnswerEmpty));
            return;
        }
        if (TextUtils.isEmpty(etAnswerSecond.getText().toString().trim())) {
            showShortToast(SafetyQuestionAuthActivity.this, getResources()
                    .getString(R.string.yourAnswerEmpty));
            return;
        }
        if (TextUtils.isEmpty(etAnswerThird.getText().toString().trim())) {
            showShortToast(SafetyQuestionAuthActivity.this, getResources()
                    .getString(R.string.yourAnswerEmpty));
            return;
        }
        /**
         * 组装发送请求的参数
         * 格式：{"data": [{"sqAnswer": "xxx","sqPosition": 1}, {"sqAnswer": "xxx","sqPosition": 2},
         *          {"sqAnswer": "xxx","sqPosition": 3}]}
         */
        SafetyQuestionBean bean = new SafetyQuestionBean();
        List<SafetyQuestionBean.DataBean> answers = new ArrayList<SafetyQuestionBean.DataBean>();
        SafetyQuestionBean.DataBean answer1 = new SafetyQuestionBean.DataBean();
        answer1.setSqAnswer(etAnswerFirst.getText().toString().trim());
        answer1.setSqPosition(1);
        SafetyQuestionBean.DataBean answer2 = new SafetyQuestionBean.DataBean();
        answer2.setSqAnswer(etAnswerSecond.getText().toString().trim());
        answer2.setSqPosition(2);
        SafetyQuestionBean.DataBean answer3 = new SafetyQuestionBean.DataBean();
        answer3.setSqAnswer(etAnswerThird.getText().toString().trim());
        answer3.setSqPosition(3);
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        bean.setData(answers);
        msCheckSafetyQuestionOfUserPort(Utils.ObjectToJson(bean));
    }

    /**
     * 校验某用户已设置的安全问题接口
     */
    private void msCheckSafetyQuestionOfUserPort(String questionData) {
        if (Utils.isNetworkAvailable(SafetyQuestionAuthActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.msCheckSafetyQuestionOfUser)
                    .addParams("uId", uId)
                    .addParams("questionData", questionData)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            /**
                             * 接口调用出错
                             */
                            showShortToast(SafetyQuestionAuthActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            CommonVo commonVo = new CommonVo();
                            commonVo = Utils.parserJsonResult(response, CommonVo.class);
                            if (Constants.OK.equals(commonVo.getFlag())) {
                                if (Constants.OK.equals(commonVo.getData().getStatus())) {
                                    /**
                                     * 密保问题答案正确，根据跳转执行不同逻辑
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
                                            break;
                                    }
                                } else {
                                    if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                                    showShortToast(SafetyQuestionAuthActivity.this,
                                            commonVo.getData().getErrorString());
                                }
                            } else {
                                if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                                showShortToast(SafetyQuestionAuthActivity.this,
                                        commonVo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(SafetyQuestionAuthActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 找回密码接口
     */
    private void retrievePswPort() {
        if (Utils.isNetworkAvailable(SafetyQuestionAuthActivity.this)) {
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
                            showShortToast(SafetyQuestionAuthActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            CommonVo commonVo = new CommonVo();
                            commonVo = Utils.parserJsonResult(response, CommonVo.class);
                            if (Constants.OK.equals(commonVo.getFlag())) {
                                if (Constants.OK.equals(commonVo.getData().getStatus())) {
                                    showShortToast(SafetyQuestionAuthActivity.this,
                                            getResources().getString(R.string.captchaTip2));
                                    EventBus.getDefault().post(new SelectAuthenticationTypeEvent());
                                    finish();
                                } else {
                                    showShortToast(SafetyQuestionAuthActivity.this,
                                            commonVo.getData().getErrorString());
                                }
                            } else {
                                showShortToast(SafetyQuestionAuthActivity.this,
                                        commonVo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(SafetyQuestionAuthActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 跳转至主界面
     */
    private void gotoMainActivity() {
        Intent intent = new Intent();
        intent.setClass(SafetyQuestionAuthActivity.this, MainActivity.class);
        startActivity(intent);
        closeAll();
    }
}
