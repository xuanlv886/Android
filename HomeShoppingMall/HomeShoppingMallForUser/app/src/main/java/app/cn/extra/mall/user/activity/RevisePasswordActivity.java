package app.cn.extra.mall.user.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.PhoneUtil;
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
 * 修改密码
 */
public class RevisePasswordActivity extends BaseActivty {
    @BindView(R.id.et_phoneNum)
    EditText etPhoneNum;
    @BindView(R.id.et_oldPassword)
    EditText etOldPassword;
    @BindView(R.id.et_newPassword)
    EditText etNewPassword;
    @BindView(R.id.et_newPasswordAgain)
    EditText etNewPasswordAgain;
    @BindView(R.id.retrievePasswordBtn)
    Button retrievePasswordBtn;
    /**
     * 密码最少位数
     */
    int minPasswrodLength = 6;
    private SharePreferenceUtil sharePreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_password);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(RevisePasswordActivity.this, Constants.SAVE_USER);
    }

    @OnClick({R.id.img_back, R.id.retrievePasswordBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.retrievePasswordBtn:
                setRetrievePasswordBtnClickable(false);
                String uId = sharePreferenceUtil.getUID();
                String phoneNum = etPhoneNum.getText().toString().trim();
                String oldPssword = etOldPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String newPasswordAgain = etNewPasswordAgain.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    showShortToast(RevisePasswordActivity.this, "请输入手机号！");
                    setRetrievePasswordBtnClickable(true);
                    break;
                }
                if (!PhoneUtil.isChinaPhoneLegal(phoneNum)) {
                    showShortToast(RevisePasswordActivity.this, "手机号格式错误，请重新输入！");
                    setRetrievePasswordBtnClickable(true);
                    break;
                }
                if (TextUtils.isEmpty(oldPssword)) {
                    showShortToast(RevisePasswordActivity.this, "请输入当前密码！");
                    setRetrievePasswordBtnClickable(true);
                    break;
                }
                if (oldPssword.length() < minPasswrodLength) {
                    showShortToast(RevisePasswordActivity.this, "当前密码错误！");
                    setRetrievePasswordBtnClickable(true);
                    break;
                }
                if (TextUtils.isEmpty(newPassword)) {
                    showShortToast(RevisePasswordActivity.this, "请输入新密码！");
                    setRetrievePasswordBtnClickable(true);
                    break;
                }
                if (newPassword.length() < minPasswrodLength) {
                    showShortToast(RevisePasswordActivity.this, "新密码格式错误！！");
                    setRetrievePasswordBtnClickable(true);
                    break;
                }
                if (TextUtils.isEmpty(newPasswordAgain)) {
                    showShortToast(RevisePasswordActivity.this, "请再次输入新密码！");
                    setRetrievePasswordBtnClickable(true);
                    break;
                }
                if (!newPasswordAgain.equals(newPassword)) {
                    showShortToast(RevisePasswordActivity.this, "两次设置的密码不一致！");
                    setRetrievePasswordBtnClickable(true);
                    break;
                }
                updateUserPassword(uId, phoneNum, oldPssword, newPassword);
                break;
            default:
                break;
        }
    }

    /**
     * 修改密码接口
     *
     * @param uId
     * @param uTel         手机号
     * @param uPassword
     * @param uNewPassword
     */
    private void updateUserPassword(String uId, String uTel, String uPassword, String uNewPassword) {
        if (Utils.isNetworkAvailable(RevisePasswordActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.updateUserPassword)
                    .addParams("gAppType", Constants.APP_TYPE)
                    .addParams("uId", uId + "")
                    .addParams("uAccount", uTel + "")
                    .addParams("uPassword", uPassword + "")
                    .addParams("uNewPassword", uNewPassword + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Utils.LogE(e.toString());
                            setRetrievePasswordBtnClickable(true);
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String flag = jsonObject.getString("flag");
                                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("data"));
                                if (Constants.OK.equals(flag)) {
                                    if(jsonObject1.getBoolean("status")) {
                                        showShortToast(RevisePasswordActivity.this, "修改密码成功！");
                                        finish();
                                    }else {
                                        String errorString = jsonObject1.getString("errorString");
                                        showShortToast(RevisePasswordActivity.this, errorString);
                                        setRetrievePasswordBtnClickable(true);
                                    }

                                } else {
                                    String errorString = jsonObject.getString("errorString");
                                    showShortToast(RevisePasswordActivity.this, errorString);
                                    setRetrievePasswordBtnClickable(true);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            showShortToast(RevisePasswordActivity.this, Constants.NETWORK_ERROR);
            setRetrievePasswordBtnClickable(true);
        }
    }

    /**
     * 设置确定按钮显示效果与是否可以点击
     *
     * @param i i=true可以点击；i=false不可点击
     */
    private void setRetrievePasswordBtnClickable(boolean i) {
        if (i) {
            retrievePasswordBtn.setBackgroundResource(R.drawable.shape_btn_blue);
            retrievePasswordBtn.setTextColor(getResources().getColor(R.color.white));
            retrievePasswordBtn.setEnabled(true);
        } else {
            retrievePasswordBtn.setBackgroundResource(R.drawable.gray_bg_circle);
            retrievePasswordBtn.setTextColor(getResources().getColor(R.color.white));
            retrievePasswordBtn.setEnabled(false);
        }
    }
}
