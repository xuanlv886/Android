package app.cn.extra.mall.user.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.ChangeSafetyQuestionEvent;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.CommonVo;
import app.cn.extra.mall.user.vo.GetSafetyQuestion;
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
 * Description 修改安全问题详情页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class ChangeSafetyQuestionDetailActivity extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
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
    @BindView(R.id.ll_old_question)
    LinearLayout llOldQuestion;
    @BindView(R.id.tv_one)
    TextView tvOne;
    @BindView(R.id.sp_one)
    Spinner spOne;
    @BindView(R.id.et_one)
    EditText etOne;
    @BindView(R.id.tv_two)
    TextView tvTwo;
    @BindView(R.id.sp_two)
    Spinner spTwo;
    @BindView(R.id.et_two)
    EditText etTwo;
    @BindView(R.id.tv_three)
    TextView tvThree;
    @BindView(R.id.sp_three)
    Spinner spThree;
    @BindView(R.id.et_three)
    EditText etThree;
    @BindView(R.id.ll_new_question)
    LinearLayout llNewQuestion;

    private SharePreferenceUtil sharePreferenceUtil;
    private MsGetSafetyQuestionOfUser data = null;

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
        setContentView(R.layout.activity_change_safety_question_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(ChangeSafetyQuestionDetailActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        msGetSafetyQuestionOfUserPort();
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
     * 获取某用户已设置的密保问题的接口
     */
    private void msGetSafetyQuestionOfUserPort() {
        if (Utils.isNetworkAvailable(ChangeSafetyQuestionDetailActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.msGetSafetyQuestionOfUser)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 接口调用出错
                             */
                            showShortToast(ChangeSafetyQuestionDetailActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
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
                                showShortToast(ChangeSafetyQuestionDetailActivity.this,
                                        data.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(ChangeSafetyQuestionDetailActivity.this,
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
                if (View.VISIBLE == llOldQuestion.getVisibility()) {
                    doAuth();
                } else {
                    doSubmit();
                }
                break;
            default:
        }
    }

    /**
     * 验证按钮执行的操作
     */
    private void doAuth() {
        /**
         * 验证是否成功获取了密保问题的数据
         */
        if (null == data.getData() || 0 == data.getData().size()) {
            showShortToast(ChangeSafetyQuestionDetailActivity.this, getResources()
                    .getString(R.string.authError));
            return;
        }
        /**
         * 验证安全问题的答案是否均已填写
         */
        if (TextUtils.isEmpty(etAnswerFirst.getText().toString().trim())) {
            showShortToast(ChangeSafetyQuestionDetailActivity.this, getResources()
                    .getString(R.string.yourAnswerEmpty));
            return;
        }
        if (TextUtils.isEmpty(etAnswerSecond.getText().toString().trim())) {
            showShortToast(ChangeSafetyQuestionDetailActivity.this, getResources()
                    .getString(R.string.yourAnswerEmpty));
            return;
        }
        if (TextUtils.isEmpty(etAnswerThird.getText().toString().trim())) {
            showShortToast(ChangeSafetyQuestionDetailActivity.this, getResources()
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
     * 点击提交按钮执行的操作
     */
    private void doSubmit() {
        /**
         * 判断输入项等是否为空
         */
        if (TextUtils.isEmpty(sqId_one) || TextUtils.isEmpty(sqId_two)
                || TextUtils.isEmpty(sqId_three)) {
            showShortToast(ChangeSafetyQuestionDetailActivity.this,
                    getResources().getString(R.string.portError));
            return;
        }
        if (TextUtils.isEmpty(etOne.getText().toString().trim())
                || TextUtils.isEmpty(etTwo.getText().toString().trim())
                || TextUtils.isEmpty(etThree.getText().toString().trim())) {
            showShortToast(ChangeSafetyQuestionDetailActivity.this,
                    getResources().getString(R.string.yourAnswerEmpty));
            return;
        }
        if (etOne.getText().toString().trim().length() < 2
                || etTwo.getText().toString().trim().length() < 2
                || etThree.getText().toString().trim().length() < 2) {
            showShortToast(ChangeSafetyQuestionDetailActivity.this, "您填写的密保答案过短！");
            return;
        }
        updateUserSecretQuestionPort();
    }

    /**
     * 设置账户密保接口
     */
    private void updateUserSecretQuestionPort() {
        if (Utils.isNetworkAvailable(ChangeSafetyQuestionDetailActivity.this)) {
            if (null != progressBar) {
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
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 接口调用出错
                             */
                            showShortToast(ChangeSafetyQuestionDetailActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            CommonVo commonVo = new CommonVo();
                            commonVo = Utils.parserJsonResult(response, CommonVo.class);
                            if (Constants.OK.equals(commonVo.getFlag())) {
                                if (Constants.OK.equals(commonVo.getData().getStatus())) {
                                    showShortToast(ChangeSafetyQuestionDetailActivity.this,
                                            getResources().getString(R.string.updateSuccess));
                                    EventBus.getDefault().post(new ChangeSafetyQuestionEvent());
                                    finish();
                                } else {
                                    showShortToast(ChangeSafetyQuestionDetailActivity.this,
                                            commonVo.getData().getErrorString());
                                }
                            } else {
                                showShortToast(ChangeSafetyQuestionDetailActivity.this,
                                        commonVo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(ChangeSafetyQuestionDetailActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }


    /**
     * 校验某用户已设置的安全问题接口
     */
    private void msCheckSafetyQuestionOfUserPort(String questionData) {
        if (Utils.isNetworkAvailable(ChangeSafetyQuestionDetailActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.msCheckSafetyQuestionOfUser)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("questionData", questionData)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 接口调用出错
                             */
                            showShortToast(ChangeSafetyQuestionDetailActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            CommonVo commonVo = new CommonVo();
                            commonVo = Utils.parserJsonResult(response, CommonVo.class);
                            if (Constants.OK.equals(commonVo.getFlag())) {
                                if (Constants.OK.equals(commonVo.getData().getStatus())) {
                                    /**
                                     * 旧密保问题验证成功,展示设置新密保问题界面
                                     */
                                    llOldQuestion.setVisibility(View.GONE);
                                    llNewQuestion.setVisibility(View.VISIBLE);
                                    for (int i = 1; i < 4; i++) {
                                        getSafetyQuestionPort(i);
                                    }
                                } else {
                                    if (null != progressBar) {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    showShortToast(ChangeSafetyQuestionDetailActivity.this,
                                            commonVo.getData().getErrorString());
                                }
                            } else {
                                if (null != progressBar) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                showShortToast(ChangeSafetyQuestionDetailActivity.this,
                                        commonVo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(ChangeSafetyQuestionDetailActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 获取用户安全问题接口
     *
     * @param position
     */
    private void getSafetyQuestionPort(int position) {
        if (Utils.isNetworkAvailable(ChangeSafetyQuestionDetailActivity.this)) {
            if (null != progressBar) {
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
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 接口调用出错
                             */
                            showShortToast(ChangeSafetyQuestionDetailActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
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
                                            ChangeSafetyQuestionDetailActivity.this,
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
                                showShortToast(ChangeSafetyQuestionDetailActivity.this,
                                        questions.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(ChangeSafetyQuestionDetailActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }
}
