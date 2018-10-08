package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * 重置密码
 */
public class ResetPasswordActivity extends BaseActivty {
    @BindView(R.id.et_newPassword)
    EditText etNewPassword;
    @BindView(R.id.et_newPasswordAgain)
    EditText etNewPasswordAgain;
    @BindView(R.id.resetPasswordBtn)
    Button resetPasswordBtn;

    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 手机号
     */
    String uTel = "";
    /**
     * 用户id
     */
    String uId = "";
    /**
     * 密码最少6位
     */
    int minPasswrodLength = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(ResetPasswordActivity.this, Constants.SAVE_USER);
        getPhoneNum();
    }

    /**
     * 获取数据
     */
    public void getPhoneNum() {
        Intent intent = getIntent();
        uTel = intent.getStringExtra("uTel");
        uId = sharePreferenceUtil.getUID();
    }

    @OnClick({R.id.img_back, R.id.resetPasswordBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.resetPasswordBtn:
                setResetPasswordBtnClickable(false);
                String uNewPassword = etNewPassword.getText().toString().trim();
                String uNewPasswordAgain = etNewPasswordAgain.getText().toString().trim();
                if (TextUtils.isEmpty(uNewPassword)) {
                    showShortToast(ResetPasswordActivity.this, "请输入新密码！");
                    setResetPasswordBtnClickable(true);
                    break;
                }
                if (uNewPassword.length() < minPasswrodLength) {
                    showShortToast(ResetPasswordActivity.this, "新密码格式错误！！");
                    setResetPasswordBtnClickable(true);
                    break;
                }
                if (TextUtils.isEmpty(uNewPasswordAgain)) {
                    showShortToast(ResetPasswordActivity.this, "请再次输入新密码！");
                    setResetPasswordBtnClickable(true);
                    break;
                }
                if (!uNewPassword.equals(uNewPasswordAgain)) {
                    showShortToast(ResetPasswordActivity.this, "两次设置的密码不一致！！");
                    setResetPasswordBtnClickable(true);
                    break;
                }
                resetUserPassword(uId, uTel, uNewPassword);
                break;
            default:
                break;
        }
    }

    /**
     * 重置密码接口
     *
     * @param uId          用户id
     * @param uTel         手机号
     * @param uNewPassword 新密码
     */
    private void resetUserPassword(String uId, String uTel, String uNewPassword) {
        if (Utils.isNetworkAvailable(ResetPasswordActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.resetUserPassword)
                    .addParams("gAppType", Constants.APP_TYPE)
                    .addParams("uId", uId + "")
                    .addParams("uTel", uTel + "")
                    .addParams("uNewPassword", uNewPassword + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Utils.LogE(e.toString());
                            setResetPasswordBtnClickable(true);
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String flag = jsonObject.getString("flag");
                                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("data"));
                                if (Constants.OK.equals(flag)) {
                                    if (jsonObject1.getBoolean("status")) {
                                        showShortToast(ResetPasswordActivity.this, "重置密码成功！");
                                        finish();
                                    } else {
                                        String errorString = jsonObject1.getString("errorString");
                                        showShortToast(ResetPasswordActivity.this, errorString);
                                        setResetPasswordBtnClickable(true);
                                    }

                                } else {
                                    String errorString = jsonObject.getString("errorString");
                                    showShortToast(ResetPasswordActivity.this, errorString);
                                    setResetPasswordBtnClickable(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            showShortToast(ResetPasswordActivity.this, Constants.NETWORK_ERROR);
            setResetPasswordBtnClickable(true);
        }
    }

    /**
     * 设置确定按钮显示效果与是否可以点击
     *
     * @param i i=true可以点击；i=false不可点击
     */
    private void setResetPasswordBtnClickable(boolean i) {
        if (i) {
            resetPasswordBtn.setBackgroundResource(R.drawable.line_btn_blue);
            resetPasswordBtn.setTextColor(getResources().getColor(R.color.blue));
            resetPasswordBtn.setEnabled(true);
        } else {
            resetPasswordBtn.setBackgroundResource(R.drawable.five_gray_bg_circle);
            resetPasswordBtn.setTextColor(getResources().getColor(R.color.white));
            resetPasswordBtn.setEnabled(false);
        }
    }
}
