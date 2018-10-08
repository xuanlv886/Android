package app.cn.extra.mall.user.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.ShapeBadgeItem;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.fragment.IndexFragment;
import app.cn.extra.mall.user.fragment.MineFragment;
import app.cn.extra.mall.user.fragment.TrolleyFragment;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.Downloader;
import app.cn.extra.mall.user.utils.IMLogin;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.utils.Util;
import app.cn.extra.mall.user.utils.VersionUtil;
import app.cn.extra.mall.user.vo.CheckUpdate;
import butterknife.BindView;
import butterknife.ButterKnife;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

import static com.ashokvarma.bottomnavigation.ShapeBadgeItem.SHAPE_OVAL;

/**
 * Description 主页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class MainActivity extends BaseActivty implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;
    private ShapeBadgeItem msgBadgeItem;
    /**
     * 首页
     */
    private IndexFragment indexFragment;
    /**
     * 消息
     */
    private RecentContactsFragment msgFragment;
    /**
     * 购物车
     */
    private TrolleyFragment trolleyFragment;
    /**
     * 我的
     */
    private MineFragment mineFragment;
    SharePreferenceUtil sharePreferenceUtil;
    private long exitTime = 0;
    CheckUpdate checkUpdate = null;
    /**
     * 下载器
     */
    private Downloader downloader;
    /**
     * 动态申请权限
     */
    RxPermissions rxPermissions;
    /**
     * 最新版本提示只显示一次
     * 0 显示
     * 1 不显示
     */
    int showToast = 0;
    private Fragment currentFragment = new Fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(this, Constants.SAVE_USER);
        if (!TextUtils.isEmpty(sharePreferenceUtil.getUID())) {
            com.netease.nim.uikit.merchant.Constants.MYUID = sharePreferenceUtil.getUID();
        }
        InitBottomNavigationBar();
        initIMObserver();
        downloader = new Downloader(MainActivity.this);
        rxPermissions = new RxPermissions(this);
        checkUpdate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 云信监听
     */
    private void initIMObserver() {
        /**
         * 云信消息接收监听
         */
        Observer<List<IMMessage>> incomingMessageObserver =
                new Observer<List<IMMessage>>() {
                    @Override
                    public void onEvent(List<IMMessage> messages) {
                        // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                        if (msgBadgeItem != null) {
                            msgBadgeItem.show();
                        }
                        hangup(MainActivity.this, messages.get(0).getFromNick(), messages.get(0).getContent());
                    }
                };
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
        /**
         * 云信当前登录状态监听
         */
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                new Observer<StatusCode>() {
                    @Override
                    public void onEvent(StatusCode status) {
//                        Toast.makeText(getApplicationContext(), status.toString(), Toast.LENGTH_SHORT).show();
                        if (status == StatusCode.KICKOUT) {
                            // 被踢出后的处理
                            if (TextUtils.isEmpty(sharePreferenceUtil.getBrokenLineTag())) {
                                sharePreferenceUtil.setBrokenLineTag("true");
                                Intent intent = new Intent();
                                intent.setAction(Constants.BROKEN_LINE_ACTION);
                                intent.setComponent(new ComponentName("app.cn.extra.mall.user",
                                        "app.cn.extra.mall.user.broadcast.BrokenLineReceiver"));
                                sendBroadcast(intent);
                            }
                        }
                    }
                }, true);
    }

    /**
     * 横幅通知
     *
     * @param context
     */
    private void hangup(Context context, String name, String content) {
        String id = "my_channel_01";
        String channelName="Channel";
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 1, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(id,
                    channelName, NotificationManager.IMPORTANCE_HIGH);
            Log.i("", mChannel.toString());
            notificationManager.createNotificationChannel(mChannel);
            notification = new Notification.Builder(this)
                    .setChannelId(id)
                    .setContentTitle(name)
                    .setContentText(content)
                    .setContentIntent(pIntent)
                    .setSmallIcon(R.mipmap.ic_launcher).build();
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(name)
                    .setContentText(content)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setOngoing(true)
                    .setContentIntent(pIntent);
            notification = notificationBuilder.build();
        }
        notificationManager.notify(1, notification);
    }

    /**
     * 初始化底部导航栏
     */
    private void InitBottomNavigationBar() {
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setBarBackgroundColor(R.color.white);
        msgBadgeItem = new ShapeBadgeItem()
                .setShape(SHAPE_OVAL)
                .setShapeColor(Color.RED)
                .setEdgeMarginInDp(this, 5)
                .setSizeInDp(this, 7, 7)
                .setAnimationDuration(200)
                .setHideOnSelect(true)
                .hide();
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.tab1_check,
                        getResources().getString(R.string.main_tab_index))
                        .setActiveColorResource(R.color.orange))
                .addItem(new BottomNavigationItem(R.mipmap.tab2_uncheck,
                        getResources().getString(R.string.main_tab_msg))
                        .setBadgeItem(msgBadgeItem)
                        .setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.mipmap.tab3_uncheck,
                        getResources().getString(R.string.main_tab_trolley))
                        .setActiveColorResource(R.color.colorAccent))
                .addItem(new BottomNavigationItem(R.mipmap.tab4_uncheck,
                        getResources().getString(R.string.main_tab_mine))
                        .setActiveColorResource(R.color.teal))
                .setFirstSelectedPosition(0)
                .initialise();
        indexFragment = IndexFragment.newInstance(getResources()
                .getString(R.string.main_tab_index));
        msgFragment = RecentContactsFragment.newInstance(getResources()
                            .getString(R.string.main_tab_msg));
        trolleyFragment = TrolleyFragment.newInstance(getResources()
                .getString(R.string.main_tab_trolley));
        mineFragment = MineFragment.newInstance(getResources()
                .getString(R.string.main_tab_mine));
        setDefaultFragment();
    }

    /**
     * 设置默认显示的Fragment
     */
    private void setDefaultFragment() {
        switchFragment(indexFragment);
    }

    private void setMsgBadgeItem() {
        msgBadgeItem.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onTabSelected(int position) {
        if (msgBadgeItem != null) {
            msgBadgeItem.hide();
        }
        switch (position) {
            case 0:
                switchFragment(indexFragment);
                break;
            case 1:
                StatusCode status = NIMClient.getStatus();
                if (status == StatusCode.UNLOGIN) {
                    IMLogin imLogin = new IMLogin(MainActivity.this);
                    imLogin.login(1);
                }
                if (TextUtils.isEmpty(sharePreferenceUtil.getUID())) {
                    gotoLogin();
                    return;
                }
                switchFragment(msgFragment);
                break;
            case 2:
                if (TextUtils.isEmpty(sharePreferenceUtil.getUID())) {
                    gotoLogin();
                    return;
                }
                switchFragment(trolleyFragment);
                break;
            case 3:
                if (TextUtils.isEmpty(sharePreferenceUtil.getUID())) {
                    gotoLogin();
                    return;
                }
                switchFragment(mineFragment);
                break;
            default:
                break;
        }
    }

    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.fragment_container,
                    targetFragment,targetFragment.getClass().getName());

        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment);
        }
        currentFragment = targetFragment;
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    /**
     * 清空存储的用户信息并跳转至登录界面
     */
    private void gotoLogin() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, LoginActivity.class);
        startActivity(intent);
//        if (indexFragment == null) {
//            indexFragment = IndexFragment.newInstance(getResources()
//                    .getString(R.string.main_tab_index));
//        }
//        transaction.replace(R.id.fragment_container, indexFragment);
        bottomNavigationBar.selectTab(0);
    }

    private void checkUpdate() {
        if (Utils.isNetworkAvailable(MainActivity.this)) {
            String versionCodes = "" + VersionUtil.getLocalVersion(MainActivity.this);
            OkHttpUtils
                    .post()
                    .url(Constants.checkUpdate)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("vType", Constants.APP_TYPE)
                    .addParams("vBuildCode", Util.getVerName(MainActivity.this).trim())
                    .addParams("vSystemType", 0 + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            checkUpdate = new CheckUpdate();
                            checkUpdate = Utils.parserJsonResult(response, CheckUpdate.class);
                            if ("true".equals(checkUpdate.getFlag())) {
                                CheckUpdate.DataBean data = checkUpdate.getData();
                                if (data != null) {
                                    if (Constants.OK.equals(checkUpdate.getData().getIsNeedUpdate())) {
                                        /**
                                         * 需要更新
                                         */
                                        showUpdateDialog();
                                    } else {
                                        /**
                                         * 无需更新
                                         */
                                        if (showToast == 0) {
//                                        showShortToast(MainActivity.this,
//                                                getResources().getString(R.string.notNeedUpdate));
                                            showToast++;
                                        }
                                    }
                                }
                            } else {
                                CustomToast.showToast(MainActivity.this,
                                        checkUpdate.getErrorString(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            CustomToast.showToast(MainActivity.this,
                    "检查更新失败！" + Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);
        }
    }

    /**
     * 弹出更新窗口
     */
    private void showUpdateDialog() {
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_version_manager, null);
        view.setAlpha(1f);
        TextView versionCode = view.findViewById(R.id.versionCode);
        TextView content = view.findViewById(R.id.content);
        TextView tv_yes = view.findViewById(R.id.tv_yes);
        TextView tv_never_remind = view.findViewById(R.id.tv_never_reminds);
        TextView tv_tip = view.findViewById(R.id.tv_tip);
        LinearLayout ll_update = view.findViewById(R.id.ll_update);
        RelativeLayout rl_update = view.findViewById(R.id.rl_update);
        TextView tv_update = view.findViewById(R.id.tv_update);
        final Dialog alertDialog = new AlertDialog.Builder(this, R.style.Theme_Transparent).
                setView(view).
                create();
        /**
         * 是否强制更新 0--否，1--是
         */
        if ("".equals(checkUpdate.getData().getVIfMust()) || "0".equals(checkUpdate.getData().getVIfMust())) {
            rl_update.setVisibility(View.GONE);
            ll_update.setVisibility(View.VISIBLE);
        } else {
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            ll_update.setVisibility(View.GONE);
            rl_update.setVisibility(View.VISIBLE);
        }

        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        alertDialog.getWindow().setAttributes(lp);
        versionCode.setText("版本号：" + checkUpdate.getData().getVCode());
        content.setText(checkUpdate.getData().getVContent());
        tv_yes.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            alertDialog.dismiss();
        });
        tv_never_remind.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startDownlod();
            }
        });
        tv_update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startDownlod();
            }
        });
    }

    /**
     * 执行下载相关操作
     */
    private void startDownlod() {
        /**
         * 适配安卓8.0
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /**
             * 判断是否有安装权限
             */
            boolean flag = getPackageManager()
                    .canRequestPackageInstalls();
            if (flag) {
                downloader.downloadAPK(checkUpdate.getData().getVUrl(),
                        Utils.getFileName(checkUpdate.getData().getVUrl())
                                + Constants.APK_TYPE);
                showShortToast(MainActivity.this, getResources()
                        .getString(R.string.downloading));
            } else {
                /**
                 * 申请相关权限
                 */
                rxPermissions.request(Manifest.permission.REQUEST_INSTALL_PACKAGES,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(granted -> {
                            if (granted) {
                                downloader.downloadAPK(checkUpdate.getData().getVUrl(),
                                        Utils.getFileName(checkUpdate.getData().getVUrl())
                                                + Constants.APK_TYPE);
                                showShortToast(MainActivity.this, getResources()
                                        .getString(R.string.downloading));
                            } else {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                                startActivity(intent);
                            }
                        });
            }
        } else {
            /**
             * 安卓8.0以下版本
             */
            downloader.downloadAPK(checkUpdate.getData().getVUrl(),
                    Utils.getFileName(checkUpdate.getData().getVUrl())
                            + Constants.APK_TYPE);
            showShortToast(MainActivity.this, getResources()
                    .getString(R.string.downloading));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                showShortToast(MainActivity.this,
                        getResources().getString(R.string.quitApp));
                exitTime = System.currentTimeMillis();
            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
