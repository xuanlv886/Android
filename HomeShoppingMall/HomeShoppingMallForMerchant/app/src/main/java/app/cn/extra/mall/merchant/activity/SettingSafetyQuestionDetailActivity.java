package app.cn.extra.mall.merchant.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.SettingSafetyQuestionEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.CommonVo;
import app.cn.extra.mall.merchant.vo.GetSafetyQuestion;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 设置安全问题详情页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class SettingSafetyQuestionDetailActivity extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.sp_one)
    Spinner spOne;
    @BindView(R.id.et_one)
    EditText etOne;
    @BindView(R.id.sp_two)
    Spinner spTwo;
    @BindView(R.id.et_two)
    EditText etTwo;
    @BindView(R.id.sp_three)
    Spinner spThree;
    @BindView(R.id.et_three)
    EditText etThree;
    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 临时存储第一个密保问题的所有标识
     */
    private List<String> sqIds_one = new ArrayList<String>();
    /**
     * 临时存储第二个密保问题的所有标识
     */
    private List<String> sqIds_two = new ArrayList<String>();
    /**
     * 临时存储第三个密保问题的所有标识
     */
    private List<String> sqIds_three = new ArrayList<String>();
    /**
     * 临时存储选中的第一个密保问题的标识
     */
    private String sqId_one = "";
    /**
     * 临时存储选中的第二个密保问题的标识
     */
    private String sqId_two = "";
    /**
     * 临时存储选中的第三个密保问题的标识
     */
    private String sqId_three = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_safety_question_detail);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化机界面内元素
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(SettingSafetyQuestionDetailActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        for (int i = 1; i < 4; i++) {
            getSafetyQuestionPort(i);
        }
        spOne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sqId_one = sqIds_one.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spTwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sqId_two = sqIds_two.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spThree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sqId_three = sqIds_three.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 获取用户安全问题接口
     *
     * @param position
     */
    private void getSafetyQuestionPort(int position) {
        if (Utils.isNetworkAvailable(SettingSafetyQuestionDetailActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getSafetyQuestion)
                    .addParams("sqPosition", position + "")
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
                            showShortToast(SettingSafetyQuestionDetailActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            GetSafetyQuestion questions = new GetSafetyQuestion();
                            questions = Utils.parserJsonResult(response, GetSafetyQuestion.class);
                            if (Constants.OK.equals(questions.getFlag())) {
                                if (0 < questions.getData().getSafetyQuestionList().size()) {
                                    List<String> safetyQuestions = new ArrayList<String>();
                                    ArrayAdapter<String> adapter = null;
                                    for (int i = 0; i < questions.getData()
                                            .getSafetyQuestionList().size(); i++) {
                                        safetyQuestions.add(questions.getData()
                                                .getSafetyQuestionList().get(i).getSqName());
                                        switch (position) {
                                            case 1:
                                                sqIds_one.add(questions.getData()
                                                        .getSafetyQuestionList().get(i).getSqId());
                                                break;
                                            case 2:
                                                sqIds_two.add(questions.getData()
                                                        .getSafetyQuestionList().get(i).getSqId());
                                                break;
                                            case 3:
                                                sqIds_three.add(questions.getData()
                                                        .getSafetyQuestionList().get(i).getSqId());
                                                break;
                                            default:
                                        }
                                    }
                                    adapter = new ArrayAdapter<String>(
                                            SettingSafetyQuestionDetailActivity.this,
                                            android.R.layout.simple_spinner_item,
                                            safetyQuestions);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    switch (position) {
                                        case 1:
                                            spOne.setAdapter(adapter);
                                            sqId_one = questions.getData()
                                                    .getSafetyQuestionList().get(0).getSqId();
                                            break;
                                        case 2:
                                            spTwo.setAdapter(adapter);
                                            sqId_two = questions.getData()
                                                    .getSafetyQuestionList().get(0).getSqId();
                                            break;
                                        case 3:
                                            spThree.setAdapter(adapter);
                                            sqId_three = questions.getData()
                                                    .getSafetyQuestionList().get(0).getSqId();
                                            break;
                                        default:
                                    }
                                }
                            } else {
                                showShortToast(SettingSafetyQuestionDetailActivity.this,
                                        questions.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(SettingSafetyQuestionDetailActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    @OnClick({R.id.img_back, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_submit:
                doSubmit();
                break;
            default:
        }
    }

    /**
     * 点击提交按钮执行的操作
     */
    private void doSubmit() {
        /**
         * 判断输入项等是否为空
         */
        if (TextUtils.isEmpty(sqId_one) || TextUtils.isEmpty(sqId_two)
                || TextUtils.isEmpty(sqId_three)) {
            showShortToast(SettingSafetyQuestionDetailActivity.this,
                    getResources().getString(R.string.portError));
            return;
        }
        if (TextUtils.isEmpty(etOne.getText().toString().trim())
                || TextUtils.isEmpty(etTwo.getText().toString().trim())
                || TextUtils.isEmpty(etThree.getText().toString().trim())) {
            showShortToast(SettingSafetyQuestionDetailActivity.this,
                    getResources().getString(R.string.yourAnswerEmpty));
            return;
        }
        if (etOne.getText().toString().trim().length() < 2
                || etTwo.getText().toString().trim().length() < 2
                || etThree.getText().toString().trim().length() < 2) {
            showShortToast(SettingSafetyQuestionDetailActivity.this, "您填写的密保答案过短！");
            return;
        }
        updateUserSecretQuestionPort();
    }

    /**
     * 设置账户密保接口
     */
    private void updateUserSecretQuestionPort() {
        if (Utils.isNetworkAvailable(SettingSafetyQuestionDetailActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.updateUserSecretQuestion)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("uFirstSqId", sqId_one)
                    .addParams("uFirstSqAnswer", etOne.getText().toString().trim())
                    .addParams("uSecondSqId", sqId_two)
                    .addParams("uSecondSqAnswer", etTwo.getText().toString().trim())
                    .addParams("uThirdSqId", sqId_three)
                    .addParams("uThirdSqAnswer", etThree.getText().toString().trim())
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
                            showShortToast(SettingSafetyQuestionDetailActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            CommonVo commonVo = new CommonVo();
                            commonVo = Utils.parserJsonResult(response, CommonVo.class);
                            if (Constants.OK.equals(commonVo.getFlag())) {
                                if (Constants.OK.equals(commonVo.getData().getStatus())) {
                                    showShortToast(SettingSafetyQuestionDetailActivity.this,
                                            getResources().getString(R.string.settingSuccess));
                                    EventBus.getDefault().post(new SettingSafetyQuestionEvent());
                                    finish();
                                } else {
                                    showShortToast(SettingSafetyQuestionDetailActivity.this,
                                            commonVo.getData().getErrorString());
                                }
                            } else {
                                showShortToast(SettingSafetyQuestionDetailActivity.this,
                                        commonVo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(SettingSafetyQuestionDetailActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }
}
