package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.utils.Util;
import app.cn.extra.mall.user.vo.AddPayPassword;
import app.cn.extra.mall.user.vo.GetCAPTCHA;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * 设置支付密码页面
 */
public class PayPasswordActivity extends BaseActivty {
    @BindView(R.id.et_pay_psw)
    EditText etPayPsw;
    @BindView(R.id.et_pay_psw_again)
    EditText etPayPswAgain;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_get_code)
    TextView tvGetCode;
    @BindView(R.id.btn_ok)
    Button btnOk;
    private int Limit = 6;
    private int PhoneLimit = 11;
    private TimeCount time;
    private SharePreferenceUtil sp;
    private String code = "";
    private String BindTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_password);
        ButterKnife.bind(this);
        time = new TimeCount(60000, 1000);
        Intent intent = getIntent();
        BindTel = intent.getStringExtra("BindTel");
        initView();
        initData();
    }

    private void initView() {
        sp = new SharePreferenceUtil(PayPasswordActivity.this, Constants.SAVE_USER);
        etPayPsw.addTextChangedListener(PswTextWatcher);
        etPayPswAgain.addTextChangedListener(AgaTextWatcher);
        etPhone.addTextChangedListener(PhoneTextWatcher);
    }

    private void initData() {

    }

    private void addPayPasswordPort() {
        String cid = sp.getUID();
        String password = etPayPsw.getText().toString().trim();
        String passwordAgain = etPayPswAgain.getText().toString().trim();
        String etcode = etCode.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        if (password.length() < 6) {
            showLongToast(PayPasswordActivity.this, "支付密码不能小于6位！");
            return;
        }
        if (password.length() > 6) {
            showLongToast(PayPasswordActivity.this, "支付密码不能大于6位！");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showLongToast(PayPasswordActivity.this, "请先输入支付密码！");
            return;
        }
        if (!password.equals(passwordAgain)) {
            showLongToast(PayPasswordActivity.this, "两次输入的支付密码不一致！");
            return;
        }
        if ("".equals(etcode)) {
            showLongToast(PayPasswordActivity.this, "验证码不能为空!");
            return;
        }
        if (!code.equals(etcode)) {
            showLongToast(PayPasswordActivity.this, "验证码输入错误!");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            showLongToast(PayPasswordActivity.this, "手机号码不能为空！");
            return;
        }
        if (Utils.isNetworkAvailable(PayPasswordActivity.this)) {
            OkHttpUtils
                    .post()
                    .url("")
                    .addParams("cId", cid)
                    .addParams("newPayPassword", password)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            AddPayPassword addPayPassword = new AddPayPassword();
                            addPayPassword = Utils.parserJsonResult(response, AddPayPassword.class);
                            if ("true".equals(addPayPassword.getFlag())) {
                                if (addPayPassword.getData().getErrorString() != null) {
                                    showLongToast(PayPasswordActivity.this, addPayPassword.getData()
                                            .getErrorString());
                                } else {
                                    showLongToast(PayPasswordActivity.this, "设置支付密码成功！");
                                    finish();
                                }

                            } else {
                                showLongToast(PayPasswordActivity.this, addPayPassword.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(PayPasswordActivity.this, Constants.NETWORK_ERROR);

        }
    }

    @OnClick({R.id.btn_ok, R.id.tv_get_code, R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                addPayPasswordPort();
                break;
            case R.id.tv_get_code:
                getCAPTCHAPort();
                break;
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
    }

    private void getCAPTCHAPort() {
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showLongToast(PayPasswordActivity.this, "手机号码不能为空！");
            return;
        }
        if (!BindTel.equals(phone)) {
            showLongToast(PayPasswordActivity.this, "输入的手机号不是账号绑定的手机号！");
            return;
        }
        if (Utils.isNetworkAvailable(PayPasswordActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getCaptcha)
                    .addParams("phoneNum", phone)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            GetCAPTCHA getCAPTCHA = new GetCAPTCHA();
                            getCAPTCHA = Utils.parserJsonResult(response, GetCAPTCHA.class);
                            if ("true".equals(getCAPTCHA.getFlag())) {
                                time.start();
                                showLongToast(PayPasswordActivity.this, "验证码已发送，请注意查收！");
                                code = getCAPTCHA.getData().getCAPTCHA();
                            } else {
                                showLongToast(PayPasswordActivity.this, getCAPTCHA.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(PayPasswordActivity.this, Constants.NETWORK_ERROR);

        }
    }

    /**
     * 监听输入的密码格式是否正确
     */
    TextWatcher AgaTextWatcher = new TextWatcher() {
        private CharSequence temp;
        int editStart = 0;
        int editEnd = 0;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            temp = charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (temp.length() > Limit) {
                editStart = etPayPswAgain.getSelectionStart();
                editEnd = etPayPswAgain.getSelectionEnd();
                showLongToast(PayPasswordActivity.this, "最多可输入" + Limit + "个字！");
                editable.delete(editStart - 1, editEnd);
            }

        }
    };
    /**
     * 监听输入的密码格式是否正确
     */
    TextWatcher PswTextWatcher = new TextWatcher() {
        private CharSequence temp;
        int editStart = 0;
        int editEnd = 0;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            temp = charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (temp.length() > Limit) {
                editStart = etPayPsw.getSelectionStart();
                editEnd = etPayPsw.getSelectionEnd();
                showLongToast(PayPasswordActivity.this, "最多可输入" + Limit + "个字！");
                editable.delete(editStart - 1, editEnd);
            }

        }
    };
    /**
     * 监听输入的手机号格式是否正确
     */
    TextWatcher PhoneTextWatcher = new TextWatcher() {
        private CharSequence temp;
        int editStart = 0;
        int editEnd = 0;

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            temp = charSequence;
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (temp.length() == PhoneLimit) {
                String phone = editable.toString();
                if (!Util.isMobileNO(phone)) {
                    showLongToast(PayPasswordActivity.this, "手机号格式有误");
                }
            }
            if (temp.length() > PhoneLimit) {
                editStart = etPhone.getSelectionStart();
                editEnd = etPhone.getSelectionEnd();
                showLongToast(PayPasswordActivity.this, "最多可输入" + PhoneLimit + "个字！");
                editable.delete(editStart - 1, editEnd);
            }

        }
    };

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvGetCode.setTextColor(Color.parseColor("#B6B6D8"));
            tvGetCode.setClickable(false);
            tvGetCode.setText(millisUntilFinished / 1000 + "秒后重新获取");
        }

        @Override
        public void onFinish() {
            tvGetCode.setText("重新获取验证码");
            tvGetCode.setClickable(true);
            tvGetCode.setTextColor(getResources()
                    .getColor(R.color.blue));


        }
    }
}
