package app.cn.extra.mall.user.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.view.PopMenu;
import app.cn.extra.mall.user.vo.GetSafetyQuestion;
import app.cn.extra.mall.user.vo.UpdateUserSecretQuestion;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * 设置密保问题
 */
public class SetSecurityActivity extends BaseActivty {
    @BindView(R.id.tv_problem1)
    TextView tvProblem1;
    @BindView(R.id.tv_problem2)
    TextView tvProblem2;
    @BindView(R.id.tv_problem3)
    TextView tvProblem3;
    @BindView(R.id.et_answer1)
    EditText etAnswer1;
    @BindView(R.id.et_answer2)
    EditText etAnswer2;
    @BindView(R.id.et_answer3)
    EditText etAnswer3;
    @BindView(R.id.set_btn)
    Button setBtn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 安全问题位置
     */
    final String POSITION_ONE = "1";
    final String POSITION_TWO = "2";
    final String POSITION_THREE = "3";
    /**
     * 问题一临时数据
     */
    List<GetSafetyQuestion.DataBean> list_one = null;
    /**
     * 问题二临时数据
     */
    List<GetSafetyQuestion.DataBean> list_two = null;
    /**
     * 问题三l临时数据
     */
    List<GetSafetyQuestion.DataBean> list_three = null;
    /**
     * 问题弹出框
     */
    private PopMenu popMenu;
    /**
     * 问题id
     */
    String sqIdOne = "";
    String sqIdTwo = "";
    String sqIdThree = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_security);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        getSafetyQuestion(POSITION_ONE);
    }

    /**
     * 获取页面数据接口
     * p--当前页
     * ps--页大小
     */
    private void getSafetyQuestion(String sqPosition) {
        if (Utils.isNetworkAvailable(MyDemandActivity.context)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.getSafetyQuestion)
                    .addParams("sqPosition", sqPosition)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
//                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
//                            /**
//                             * 恢复显示数据的View
//                             */
//                            varyViewHelperController.restore();

                            GetSafetyQuestion getSafetyQuestion = new GetSafetyQuestion();
                            getSafetyQuestion = Utils.parserJsonResult(response,
                                    GetSafetyQuestion.class);
                            if (Constants.OK.equals(getSafetyQuestion.getFlag())) {
                                switch (sqPosition) {
                                    case POSITION_ONE:
                                        list_one.add(getSafetyQuestion.getData());
                                        tvProblem1.setText(getSafetyQuestion.getData().getSafetyQuestionList().get(0).getSqName());
                                        getSafetyQuestion(POSITION_TWO);
                                        break;
                                    case POSITION_TWO:
                                        list_two.add(getSafetyQuestion.getData());
                                        tvProblem2.setText(getSafetyQuestion.getData().getSafetyQuestionList().get(0).getSqName());
                                        getSafetyQuestion(POSITION_THREE);
                                        break;
                                    case POSITION_THREE:
                                        list_three.add(getSafetyQuestion.getData());
                                        tvProblem3.setText(getSafetyQuestion.getData().getSafetyQuestionList().get(0).getSqName());
                                        break;
                                    default:
                                        break;
                                }

                            } else {
//                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
//            varyViewHelperController.showNetworkPoorView();
        }
    }

    /**
     * 设置密保问题接口
     */
    private void updateUserSecretQuestion(String uId, String uFirstSqId, String uFirstSqAnswer, String uSecondSqId, String uSecondSqAnswer, String uThirdSqId, String uThirdSqAnswer) {
        if (Utils.isNetworkAvailable(MyDemandActivity.context)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.updateUserSecretQuestion)
                    .addParams("uId", uId)
                    .addParams("uFirstSqId", uFirstSqId)
                    .addParams("uFirstSqAnswer", uFirstSqAnswer)
                    .addParams("uSecondSqId", uSecondSqId)
                    .addParams("uSecondSqAnswer", uSecondSqAnswer)
                    .addParams("uThirdSqId", uThirdSqId)
                    .addParams("uThirdSqAnswer", uThirdSqAnswer)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
//                            varyViewHelperController.showErrorView();
                            setBtnClickable(true);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            setBtnClickable(true);
//                            /**
//                             * 恢复显示数据的View
//                             */
//                            varyViewHelperController.restore();

                            UpdateUserSecretQuestion updateUserSecretQuestion = new UpdateUserSecretQuestion();
                            updateUserSecretQuestion = Utils.parserJsonResult(response,
                                    UpdateUserSecretQuestion.class);
                            if (Constants.OK.equals(updateUserSecretQuestion.getFlag())) {
                                if(Constants.OK.equals(updateUserSecretQuestion.getData().getStatus())) {
                                    showLongToast(SetSecurityActivity.this, "设置成功！");
                                    finish();
                                }else {
                                    showLongToast(SetSecurityActivity.this, updateUserSecretQuestion.getData().getErrorString());
                                    setBtnClickable(true);
                                }

                            } else {
                                showLongToast(SetSecurityActivity.this, updateUserSecretQuestion.getErrorString());
                                setBtnClickable(true);
//                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
            setBtnClickable(true);
//            varyViewHelperController.showNetworkPoorView();
        }
    }

    private int getScreenWidth() {
        // TODO Auto-generated method stub
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        return dm.widthPixels; // 屏幕宽（像素，如：480px）
    }

    /**
     * 问题弹窗
     *
     * @param v
     * @param tvQuestion
     * @param postion
     */
    private void QuestionClicked(View v, TextView tvQuestion, String postion) {
        Drawable drawable = getResources().getDrawable(R.mipmap.icon_go);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvQuestion.setCompoundDrawables(null, null, drawable, null);
        final ArrayList<String> data = new ArrayList<String>();
        //添加数据
        switch (postion) {
            case POSITION_ONE:
                for (int i = 0; i < list_one.get(0).getSafetyQuestionList().size(); i++) {
                    data.add(i, list_one.get(0).getSafetyQuestionList().get(i).getSqName());
                }
                break;
            case POSITION_TWO:
                for (int i = 0; i < list_two.get(0).getSafetyQuestionList().size(); i++) {
                    data.add(i, list_two.get(0).getSafetyQuestionList().get(i).getSqName());
                }
                break;
            case POSITION_THREE:
                for (int i = 0; i < list_three.get(0).getSafetyQuestionList().size(); i++) {
                    data.add(i, list_three.get(0).getSafetyQuestionList().get(i).getSqName());
                }
                break;
            default:
                break;
        }
        // 初始化弹出菜单	 POPMENU
        popMenu = new PopMenu(this, getScreenWidth() / 3, data);
        popMenu.setOnItemClickListener(new PopMenu.OnItemClickListener() {

            @Override
            public void onItemClick(int index) {
                // TODO Auto-generated method stub
                Drawable drawable = getResources().getDrawable(R.mipmap.icon_right);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tvQuestion.setCompoundDrawables(null, null, drawable, null);
                if (!TextUtils.isEmpty(data.get(index))
                        && 6 <= data.get(index).length()) {
                    tvQuestion.setText(data.get(index).substring(0, 5) + "···");
                } else {
                    tvQuestion.setText(data.get(index));
                }

                switch (postion) {
                    case POSITION_ONE:
                        sqIdOne = list_one.get(0).getSafetyQuestionList().get(index).getSqId();
                        break;
                    case POSITION_TWO:
                        sqIdTwo = list_two.get(0).getSafetyQuestionList().get(index).getSqId();
                        break;
                    case POSITION_THREE:
                        sqIdThree = list_three.get(0).getSafetyQuestionList().get(index).getSqId();
                        break;
                    default:
                        break;
                }
            }
        });
        popMenu.showAsDropDown(v);
    }

    @OnClick({R.id.img_back, R.id.tv_problem1, R.id.tv_problem2, R.id.tv_problem3, R.id.set_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_problem1:
                QuestionClicked(tvProblem1, tvProblem1, POSITION_ONE);
                break;
            case R.id.tv_problem2:
                QuestionClicked(tvProblem2, tvProblem2, POSITION_TWO);
                break;
            case R.id.tv_problem3:
                QuestionClicked(tvProblem3, tvProblem3, POSITION_THREE);
                break;
            case R.id.set_btn:
                setBtnClickable(false);
                String answerOne = etAnswer1.getText().toString().trim();
                String answerTwo = etAnswer2.getText().toString().trim();
                String answerThree = etAnswer3.getText().toString().trim();
                if (TextUtils.isEmpty(answerOne) || TextUtils.isEmpty(answerTwo) || TextUtils.isEmpty(answerThree)) {
                    showShortToast(SetSecurityActivity.this, "请输入答案！");
                    setBtnClickable(true);
                    break;
                }
                updateUserSecretQuestion(sharePreferenceUtil.getUID(), sqIdOne, answerOne, sqIdTwo, answerTwo, sqIdThree, answerThree);
                break;
            default:
                break;
        }
    }

    /**
     * 设置按钮是否可以点击与显示效果
     *
     * @param b b=true可以点击；b=false不可点击
     */
    private void setBtnClickable(boolean b) {
        if (b) {
            setBtn.setBackgroundResource(R.drawable.shape_btn_blue);
            setBtn.setTextColor(getResources().getColor(R.color.white));
            setBtn.setEnabled(true);
        } else {
            setBtn.setBackgroundResource(R.drawable.gray_bg_circle);
            setBtn.setTextColor(getResources().getColor(R.color.white));
            setBtn.setEnabled(false);
        }
    }
}
