package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.SingleInfoEvent;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;

/**
 * Description 修改单条信息页（账户信息等）
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class ChangeSingleInfoActivity extends BaseActivty {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.et_content)
    EditText etContent;
    /**
     * 用以判断修改的信息
     */
    private String tag = "";
    /**
     * 原内容
     */
    private String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_single_info);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        Intent intent = getIntent();
        tag = intent.getStringExtra("tag");
        content = intent.getStringExtra("content");
        if (!TextUtils.isEmpty(content)) {
            if ("uTel".equals(tag) || "sTel".equals(tag)) {
                etContent.setInputType(InputType.TYPE_CLASS_PHONE);
            }
            etContent.setText(content);
        } else {
            if ("uNickName".equals(tag)) {
                etContent.setHint(getResources().getString(R.string.inputUNickName));
            } else if ("uEmail".equals(tag)) {
                etContent.setHint(getResources().getString(R.string.inputUEmail));
            } else if ("uBirthday".equals(tag)) {
                etContent.setHint(getResources().getString(R.string.inputUBirthday));
            } else if ("uTel".equals(tag)) {
                etContent.setHint(getResources().getString(R.string.inputUTel));
                etContent.setInputType(InputType.TYPE_CLASS_PHONE);
            } else if ("sDescribe".equals(tag)) {
                etContent.setHint(getResources().getString(R.string.inputSDescribe));
            }
        }
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
     * 提交修改的信息
     */
    private void doSubmit() {
        if (TextUtils.isEmpty(etContent.getText().toString().trim())) {
            showShortToast(ChangeSingleInfoActivity.this, getResources().getString(
                    R.string.singleInfoEmpty));
            return;
        }
        /**
         * 未修改内容
         */
        if (content.equals(etContent.getText().toString().trim())) {
            showShortToast(ChangeSingleInfoActivity.this, getResources().getString(
                    R.string.changeNothing));
            return;
        }
        if ("uEmail".equals(tag)) {
            if (!Utils.isValidEmail(etContent.getText().toString().trim())) {
                showShortToast(ChangeSingleInfoActivity.this, getResources().getString(
                        R.string.emailError));
                return;
            }
        }
        if ("uBirthday".equals(tag)) {
            if (!Utils.isValidBirthday(etContent.getText().toString().trim())) {
                showShortToast(ChangeSingleInfoActivity.this, getResources().getString(
                        R.string.birthdayError));
                return;
            }
        }
        if ("uTel".equals(tag)) {
            if (!Utils.isValidPhoneNumber(etContent.getText().toString().trim())) {
                showShortToast(ChangeSingleInfoActivity.this, getResources().getString(
                        R.string.phoneNumError));
                return;
            }
        }
        if ("sTel".equals(tag)) {
            if (!Utils.isValidPhoneNumber(etContent.getText().toString().trim())) {
                showShortToast(ChangeSingleInfoActivity.this, getResources().getString(
                        R.string.phoneNumError));
                return;
            }
        }
        String uNickName = "";
        String uEmail = "";
        String uBirthday = "";
        String uTel = "";
        String sName = "";
        String sDescribe = "";
        String sTel = "";
        if ("uNickName".equals(tag)) {
            uNickName = etContent.getText().toString().trim();
            imUpdateUserInfo(tag, uNickName);
        } else if ("uEmail".equals(tag)) {
            uEmail = etContent.getText().toString().trim();
            imUpdateUserInfo(tag, uEmail);
        } else if ("uBirthday".equals(tag)) {
            uBirthday = etContent.getText().toString().trim();
            imUpdateUserInfo(tag, uBirthday);
        } else if ("uTel".equals(tag)) {
            uTel = etContent.getText().toString().trim();
            imUpdateUserInfo(tag, uTel);
        } else if ("sName".equals(tag)) {
            sName = etContent.getText().toString().trim();
        } else if ("sDescribe".equals(tag)) {
            sDescribe = etContent.getText().toString().trim();
        } else if ("sTel".equals(tag)) {
            sTel = etContent.getText().toString().trim();
        }
        EventBus.getDefault().post(new SingleInfoEvent(uNickName,
                uEmail, uBirthday, uTel, sName, sDescribe, sTel));
        finish();
    }

    private void imUpdateUserInfo(String tag, String value) {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        if ("uNickName".equals(tag)) {
            fields.put(UserInfoFieldEnum.Name, value);
        } else if ("uEmail".equals(tag)) {
            fields.put(UserInfoFieldEnum.EMAIL, value);
        } else if ("uBirthday".equals(tag)) {
            fields.put(UserInfoFieldEnum.BIRTHDAY, value);
        } else if ("uTel".equals(tag)) {
            fields.put(UserInfoFieldEnum.MOBILE, value);
        } else if ("pic".equals(tag)) {
            fields.put(UserInfoFieldEnum.AVATAR, value);
        }
        NIMClient.getService(UserService.class).updateUserInfo(fields)
                .setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int i, Void aVoid, Throwable throwable) {

                    }
                });
    }
}
