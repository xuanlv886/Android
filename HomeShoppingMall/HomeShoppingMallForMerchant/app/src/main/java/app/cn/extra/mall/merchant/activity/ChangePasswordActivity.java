package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
 * Description 修改密码页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class ChangePasswordActivity extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.et_tel)
    EditText etTel;
    @BindView(R.id.et_old_psw)
    EditText etOldPsw;
    @BindView(R.id.et_new_psw)
    EditText etNewPsw;
    @BindView(R.id.et_new_psw_again)
    EditText etNewPswAgain;
    private SharePreferenceUtil sharePreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(ChangePasswordActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
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
//        /**
//         * 验证手机号合法性
//         */
//        if (!Utils.isValidPhoneNumber(etTel.getText().toString().trim())) {
//            showShortToast(ChangePasswordActivity.this,
//                    getResources().getString(R.string.phoneNumError));
//            return;
//        }
        /**
         * 验证当前密码是否填写
         */
        if (TextUtils.isEmpty(etOldPsw.getText().toString().trim())) {
            showShortToast(ChangePasswordActivity.this,
                    getResources().getString(R.string.currentPswEmpty));
            return;
        }
        /**
         * 验证要设置的密码是否填写
         */
        if (TextUtils.isEmpty(etNewPsw.getText().toString().trim())) {
            showShortToast(ChangePasswordActivity.this,
                    getResources().getString(R.string.newPswEmpty));
            return;
        }
        /**
         * 验证确认设置的密码是否填写
         */
        if (TextUtils.isEmpty(etNewPswAgain.getText().toString().trim())) {
            showShortToast(ChangePasswordActivity.this,
                    getResources().getString(R.string.newPswAgainEmpty));
            return;
        }
        /**
         * 验证两次输入的密码是否一致
         */
        if (!etNewPsw.getText().toString().trim().equals(etNewPswAgain.getText()
                .toString().trim())) {
            showShortToast(ChangePasswordActivity.this,
                    getResources().getString(R.string.pswError));
            return;
        }
        updateStoreUserPswPort();
    }

    /**
     * 修改商户身份用户密码接口
     */
    private void updateStoreUserPswPort() {
        if (Utils.isNetworkAvailable(ChangePasswordActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.updateStoreUserPsw)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
                    .addParams("uAccount", sharePreferenceUtil.getUAccount())
                    .addParams("uPassword", etOldPsw.getText().toString().trim())
                    .addParams("uNewPassword", etNewPswAgain.getText().toString().trim())
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
                            showShortToast(ChangePasswordActivity.this,
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
                                    showShortToast(ChangePasswordActivity.this,
                                            getResources().getString(R.string.updateSuccess));
                                    gotoMainActivity();
                                } else {
                                    showShortToast(ChangePasswordActivity.this,
                                            commonVo.getData().getErrorString());
                                }
                            } else {
                                showShortToast(ChangePasswordActivity.this,
                                        commonVo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(ChangePasswordActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 跳转至主界面
     */
    private void gotoMainActivity() {
        Intent intent = new Intent();
        intent.setClass(ChangePasswordActivity.this, MainActivity.class);
        startActivity(intent);
        closeAll();
    }
}
