package app.cn.extra.mall.merchant.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.activity.MainActivity;
import app.cn.extra.mall.merchant.im.DemoCache;
import app.cn.extra.mall.merchant.im.Preferences;
import app.cn.extra.mall.merchant.im.UserPreferences;
import cfkj.app.cn.cfkjcommonlib.common.ActivityCollector;

/**
 * Description
 * Data 2018/9/5-19:10
 * Content
 *
 * @author L
 */
public class IMLogin {
    private AbortableFuture<LoginInfo> loginRequest;
    private SharePreferenceUtil sharePreferenceUtil = null;
    private Context mContext;

    public IMLogin(Context context) {
        this.mContext = context;
    }

    /**
     * ***************************************** 网易云登录 **************************************
     * tag 0 登录相关页面
     * tag 1 其他
     */

    public void login(int tag) {
        sharePreferenceUtil = new SharePreferenceUtil(mContext, Constants.SAVE_USER);
        sharePreferenceUtil.setBrokenLineTag("");
//        DialogMaker.showProgressDialog(getActivity(), null, getString(R.string.logining), true, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                if (loginRequest != null) {
//                    loginRequest.abort();
//                    onLoginDone();
//                }
//            }
//        }).setCanceledOnTouchOutside(false);

        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
        final String account = sharePreferenceUtil.getAccid();
        final String token = sharePreferenceUtil.getUserToken();
        // 登录
        loginRequest = NimUIKit.login(new LoginInfo(account, token), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                com.netease.nim.uikit.merchant.Constants.UID = sharePreferenceUtil.getUID();
                com.netease.nim.uikit.merchant.Constants.SID = sharePreferenceUtil.getSID();
                com.netease.nim.uikit.merchant.Constants.sType = sharePreferenceUtil.getSType();
                DemoCache.setAccount(account);
                saveLoginInfo(account, token);

                // 初始化消息提醒配置
                initNotificationConfig();
                if (tag == 0) {
                    Toast.makeText(mContext, mContext.getResources().getString(
                            R.string.loginSuccess), Toast.LENGTH_SHORT).show();
                    gotoMainActivity();
                }
            }

            @Override
            public void onFailed(int code) {
                onLoginDone(tag);
                if (code == 302 || code == 404) {
                    Toast.makeText(mContext, R.string.login_failed, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(mContext, R.string.login_exception, Toast.LENGTH_LONG).show();
                onLoginDone(tag);
            }
        });
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

    private void onLoginDone(int tag) {
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
        if (tag == 0) {
            Toast.makeText(mContext, mContext.getResources().getString(
                    R.string.loginSuccess), Toast.LENGTH_SHORT).show();
            gotoMainActivity();
        }
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    /**
     * 跳转至主界面
     */
    private void gotoMainActivity() {
        Intent intent = new Intent();
        intent.setClass(mContext, MainActivity.class);
        mContext.startActivity(intent);
        ActivityCollector.finishAll();
    }
}
