package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.CommonVo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 安全中心页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class SafetyCenterActivity extends BaseActivty {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.rl_safety_question)
    RelativeLayout rlSafetyQuestion;
    private SharePreferenceUtil sharePreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_center);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(SafetyCenterActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        msGetStatusOfSafetyQuestionsPort();
    }

    /**
     * 是否设置过密保问题接口
     */
    private void msGetStatusOfSafetyQuestionsPort() {
        if (Utils.isNetworkAvailable(SafetyCenterActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.msGetStatusOfSafetyQuestions)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            /**
                             * 接口调用出错
                             */
                            showShortToast(SafetyCenterActivity.this,
                                    getResources().getString(R.string.portError));
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
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
                                    tvStatus.setText(getResources().getString(
                                            R.string.alreadySetting));
                                } else {
                                    tvStatus.setText(getResources().getString(
                                            R.string.unSetting));
                                }
                            } else {
                                showShortToast(SafetyCenterActivity.this,
                                        commonVo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(SafetyCenterActivity.this, Constants.NETWORK_ERROR);
            if (null != progressBar) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.img_back, R.id.rl_safety_question})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_safety_question:
                judgeGotoWhichActivity();
                break;
            default:
        }
    }

    /**
     * 判断要跳转的界面
     */
    private void judgeGotoWhichActivity() {
        if (TextUtils.isEmpty(tvStatus.getText().toString().trim())) {
            showShortToast(SafetyCenterActivity.this,
                    getResources().getString(R.string.portError));
            return;
        }
        /**
         * 未设置密保问题
         */
        if (getResources().getString(R.string.unSetting).equals(
                tvStatus.getText().toString().trim())) {
            gotoSettingSafetyQuestionActivity();
        }
        /**
         * 已设置密保问题
         */
        if (getResources().getString(R.string.alreadySetting).equals(
                tvStatus.getText().toString().trim())) {
            gotoChangeSafetyQuestionActivity();
        }
    }

    /**
     * 跳转至设置密保问题界面
     */
    private void gotoSettingSafetyQuestionActivity() {
        Intent intent = new Intent();
        intent.setClass(SafetyCenterActivity.this,
                SettingSafetyQuestionActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转至修改密保问题界面
     */
    private void gotoChangeSafetyQuestionActivity() {
        Intent intent = new Intent();
        intent.setClass(SafetyCenterActivity.this,
                ChangeSafetyQuestionActivity.class);
        startActivity(intent);
        finish();
    }

}
