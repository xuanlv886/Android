package app.cn.extra.mall.merchant.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;

import app.cn.extra.mall.merchant.R;
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
 * Description 意见反馈页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class FeedBackActivity extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.btn_func_err)
    Button btnFuncErr;
    @BindView(R.id.btn_product_advice)
    Button btnProductAdvice;
    @BindView(R.id.btn_other_err)
    Button btnOtherErr;
    @BindView(R.id.et_feedback)
    EditText etFeedback;
    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 标识选中的意见反馈的类型 0--功能异常，1--产品建议，2--其它问题
     */
    private int which = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(FeedBackActivity.this, Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
    }

    @OnClick({R.id.img_back, R.id.tv_submit, R.id.btn_func_err, R.id.btn_product_advice, R.id.btn_other_err})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_submit:
                doSubmit();
                break;
            case R.id.btn_func_err:
                which = 0;
                doSelectFeedbackType();
                break;
            case R.id.btn_product_advice:
                which = 1;
                doSelectFeedbackType();
                break;
            case R.id.btn_other_err:
                which = 2;
                doSelectFeedbackType();
                break;
                default:
        }
    }

    /**
     * 提交按钮执行的操作
     */
    private void doSubmit() {
        if (TextUtils.isEmpty(etFeedback.getText().toString().trim())) {
            showShortToast(FeedBackActivity.this,
                    getResources().getString(R.string.describeProblem));
            return;
        }
        addUserFeedBackPort();
    }

    /**
     * 意见反馈接口
     */
    private void addUserFeedBackPort() {
        if (Utils.isNetworkAvailable(FeedBackActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.addUserFeedBack)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("fType", which + "")
                    .addParams("fAppType", Constants.APP_TYPE)
                    .addParams("fContent", etFeedback.getText().toString().trim())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(FeedBackActivity.this,
                                    getResources().getString(
                                            R.string.portError));
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
                                    showShortToast(FeedBackActivity.this,
                                            getResources().getString(R.string.submitSuccess));
                                    finish();
                                } else {
                                    showShortToast(FeedBackActivity.this,
                                            commonVo.getData().getErrorString());
                                }
                            } else {
                                showShortToast(FeedBackActivity.this,
                                        commonVo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(FeedBackActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 根据意见反馈类型的选中状态切换展示样式
     */
    private void doSelectFeedbackType() {
        switch (which) {
            case 0:
                btnFuncErr.setBackgroundResource(R.drawable.shape_btn_blue);
                btnFuncErr.setTextColor(getResources().getColor(R.color.white));
                btnProductAdvice.setBackgroundResource(R.drawable.shape_btn_gray);
                btnProductAdvice.setTextColor(getResources().getColor(R.color.black));
                btnOtherErr.setBackgroundResource(R.drawable.shape_btn_gray);
                btnOtherErr.setTextColor(getResources().getColor(R.color.black));
                break;
            case 1:
                btnFuncErr.setBackgroundResource(R.drawable.shape_btn_gray);
                btnFuncErr.setTextColor(getResources().getColor(R.color.black));
                btnProductAdvice.setBackgroundResource(R.drawable.shape_btn_blue);
                btnProductAdvice.setTextColor(getResources().getColor(R.color.white));
                btnOtherErr.setBackgroundResource(R.drawable.shape_btn_gray);
                btnOtherErr.setTextColor(getResources().getColor(R.color.black));
                break;
            case 2:
                btnFuncErr.setBackgroundResource(R.drawable.shape_btn_gray);
                btnFuncErr.setTextColor(getResources().getColor(R.color.black));
                btnProductAdvice.setBackgroundResource(R.drawable.shape_btn_gray);
                btnProductAdvice.setTextColor(getResources().getColor(R.color.black));
                btnOtherErr.setBackgroundResource(R.drawable.shape_btn_blue);
                btnOtherErr.setTextColor(getResources().getColor(R.color.white));
                break;
                default:
        }
    }
}
