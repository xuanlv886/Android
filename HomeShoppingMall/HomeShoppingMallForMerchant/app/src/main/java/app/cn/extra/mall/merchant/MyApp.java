package app.cn.extra.mall.merchant;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.api.model.session.SessionEventListener;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import app.cn.extra.mall.merchant.activity.PersonalDataActivity;
import app.cn.extra.mall.merchant.im.DemoCache;
import app.cn.extra.mall.merchant.im.NimSDKOptionConfig;
import app.cn.extra.mall.merchant.im.Preferences;
import app.cn.extra.mall.merchant.im.location.NimDemoLocationProvider;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import cfkj.app.cn.cfkjcommonlib.CommonApplication;
import cfkj.app.cn.cfkjcommonlib.common.Utils;

import static com.netease.nimlib.sdk.util.NIMUtil.isMainProcess;

/**
 * Description
 * Data 2018/4/21-14:33
 * Content
 *
 * @author lzy
 */

public class MyApp extends CommonApplication {
    SharePreferenceUtil sharePreferenceUtil = null;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.isDebug = true;
        sharePreferenceUtil = new SharePreferenceUtil(this, Constants.SAVE_USER);
        DemoCache.setContext(this);
        // 4.6.0 开始，第三方推送配置入口改为 SDKOption#mixPushConfig，旧版配置方式依旧支持。
        NIMClient.init(this, loginInfo(), null);

        // 以下逻辑只在主进程初始化时执行
        if (isMainProcess(this)) {
            // 初始化UIKit模块
            initUIKit();
        }

    }

    private void initUIKit() {
        // 初始化
        NimUIKit.init(this, buildUIKitOptions());
        // 可选定制项
        // 注册定位信息提供者类（可选）,如果需要发送地理位置消息，必须提供。
        // demo中使用高德地图实现了该提供者，开发者可以根据自身需求，选用高德，百度，google等任意第三方地图和定位SDK。
        NimUIKit.setLocationProvider(new NimDemoLocationProvider());
        //点击头像跳转
        NimUIKit.setSessionListener(listener);
    }

    SessionEventListener listener = new SessionEventListener() {
        @Override
        public void onAvatarClicked(Context context, IMMessage message) {
            SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(getApplicationContext(), Constants.SAVE_USER);
            if ("1".equals(sharePreferenceUtil.getSType())) {
                Intent intent = new Intent(getApplicationContext(), PersonalDataActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onAvatarLongClicked(Context context, IMMessage message) {
            // 一般用于群组@功能，或者弹出菜单，做拉黑，加好友等功能
        }

        @Override
        public void onAckMsgClicked(Context context, IMMessage message) {

        }
    };

    private UIKitOptions buildUIKitOptions() {
        UIKitOptions options = new UIKitOptions();
        // 设置app图片/音频/日志等缓存目录
        options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(this) + "/app";
        return options;
    }
    private LoginInfo loginInfo() {
        // 从本地读取上次登录成功时保存的用户登录信息
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }
}
