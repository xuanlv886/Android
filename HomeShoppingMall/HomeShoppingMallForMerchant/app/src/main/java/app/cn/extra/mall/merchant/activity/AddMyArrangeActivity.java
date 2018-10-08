package app.cn.extra.mall.merchant.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
 * Description 添加我的安排页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class AddMyArrangeActivity extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.et_sacontent)
    EditText etSacontent;
    private SharePreferenceUtil sharePreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_arrange);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(AddMyArrangeActivity.this,
                Constants.SAVE_USER);
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
     * 提交按钮执行的操作
     */
    private void doSubmit() {
        if (TextUtils.isEmpty(etSacontent.getText().toString().trim())) {
            showShortToast(AddMyArrangeActivity.this, getResources().getString(
                    R.string.inputMyArrange));
            return;
        }
        msAddMyArrangePort();
    }

    /**
     * 商户添加我的安排接口
     */
    private void msAddMyArrangePort() {
        if (Utils.isNetworkAvailable(AddMyArrangeActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.msAddMyArrange)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
                    .addParams("saContent", etSacontent.getText().toString().trim())
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
                            showShortToast(AddMyArrangeActivity.this,
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
                                    showShortToast(AddMyArrangeActivity.this, getResources().getString(
                                            R.string.submitSuccess));
                                    finish();
                                } else {
                                    showShortToast(AddMyArrangeActivity.this,
                                            commonVo.getData().getErrorString());
                                }
                            } else {
                                showShortToast(AddMyArrangeActivity.this,
                                        commonVo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(AddMyArrangeActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }
}
