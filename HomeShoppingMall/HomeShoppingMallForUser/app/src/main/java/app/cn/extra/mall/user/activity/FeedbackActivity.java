package app.cn.extra.mall.user.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.Wave;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.AddUserFeedBack;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivty {
    @BindView(R.id.et_feedback)
    EditText etFeedback;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_func_err)
    Button btnFuncErr;
    @BindView(R.id.btn_product_advice)
    Button btnProductAdvice;
    @BindView(R.id.btn_other_err)
    Button btnOtherErr;
    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 标识选中的意见反馈的类型 0--功能异常，1--产品建议，2--其它问题
     */
    private int which = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        sharePreferenceUtil = new SharePreferenceUtil(FeedbackActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
    }

    /**
     * 意见反馈
     */
    private void addUserFeedBack(String fContent) {
        if (Utils.isNetworkAvailable(FeedbackActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.addUserFeedBack)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("fType", which + "")
                    .addParams("fContent", fContent)
                    .addParams("fAppType", Constants.APP_TYPE)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
//                            varyViewHelperController.showErrorView();
//                            setSaveBtnClickable(true);
                        }

                        @Override
                        public void onResponse(String response, int id) {
//                            setSaveBtnClickable(true);
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 恢复显示数据的View
                             */
//                            varyViewHelperController.restore();
                            AddUserFeedBack addUserFeedBack = new AddUserFeedBack();
                            addUserFeedBack = Utils.parserJsonResult(response,
                                    AddUserFeedBack.class);
                            if (Constants.OK.equals(addUserFeedBack.getFlag())) {
                                if(Constants.OK.equals(addUserFeedBack.getData().getStatus())) {
                                    showLongToast(FeedbackActivity.this, "提交成功！");
                                    finish();
                                }else {
                                    showLongToast(FeedbackActivity.this, addUserFeedBack.getData().getErrorString());
                                }

                            } else {
                                showLongToast(FeedbackActivity.this, addUserFeedBack.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(getApplicationContext(),"提交失败！"+Constants.NETWORK_ERROR);
//            varyViewHelperController.showNetworkPoorView();
//            setSaveBtnClickable(true);
        }
    }

    @OnClick({R.id.img_back, R.id.tv_submit, R.id.btn_func_err, R.id.btn_product_advice, R.id.btn_other_err})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_submit:
                String fContent = etFeedback.getText().toString().trim();
                if (TextUtils.isEmpty(fContent)) {
                    showShortToast(FeedbackActivity.this, "请输入要反馈的意见！");
                    break;
                }
                addUserFeedBack(fContent);
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
                break;
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
