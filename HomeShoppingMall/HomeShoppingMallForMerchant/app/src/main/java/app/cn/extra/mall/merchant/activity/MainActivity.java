package app.cn.extra.mall.merchant.activity;

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

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.fragment.IndexFragment;
import app.cn.extra.mall.merchant.fragment.MineFragment;
import app.cn.extra.mall.merchant.fragment.RequirementFragment;
import app.cn.extra.mall.merchant.service.MovingTrajectoryService;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.Downloader;
import app.cn.extra.mall.merchant.utils.IMLogin;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.CheckUpdate;
import butterknife.BindView;
import butterknife.ButterKnife;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
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
     * 需求中心
     */
    private RequirementFragment requirementFragment;

    /**
     * 个人中心
     */
    private MineFragment mineFragment;
    private long exitTime = 0;
    private SharePreferenceUtil sharePreferenceUtil;
    private CheckUpdate checkUpdate;
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
//        msgBottomNavigationItem = new BottomNavigationItem(R.mipmap.tab2_uncheck,
//                getResources().getString(R.string.main_tab_msg));
        sharePreferenceUtil = new SharePreferenceUtil(MainActivity.this, Constants.SAVE_USER);
        if (!TextUtils.isEmpty(sharePreferenceUtil.getUID())) {
            com.netease.nim.uikit.merchant.Constants.UID = sharePreferenceUtil.getUID();
        }
        if (!TextUtils.isEmpty(sharePreferenceUtil.getSID())) {
            com.netease.nim.uikit.merchant.Constants.SID = sharePreferenceUtil.getSID();
        }
        if (!TextUtils.isEmpty(sharePreferenceUtil.getSType())) {
            com.netease.nim.uikit.merchant.Constants.sType = sharePreferenceUtil.getSType();
        }
        InitBottomNavigationBar();
        /**
         * 启动服务
         */
        this.startService(new Intent(MainActivity.this, MovingTrajectoryService.class));
        initIMObserver();
        downloader = new Downloader(MainActivity.this);
        rxPermissions = new RxPermissions(this);
        checkUpdatePort();
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
                        if (status == StatusCode.KICKOUT) {
                            // 被踢出后的处理
                            if (TextUtils.isEmpty(sharePreferenceUtil.getBrokenLineTag())) {
                                sharePreferenceUtil.setBrokenLineTag("true");
                                Intent intent = new Intent();
                                intent.setAction(Constants.BROKEN_LINE_ACTION);
                                intent.setComponent(new ComponentName("app.cn.extra.mall.merchant",
                                        "app.cn.extra.mall.merchant.broadcast.MovingTrajectoryReceiver"));
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
        String id = "my_channel_02";
        String channelName="Channel02";
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

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 如果服务停止，则重启服务
         */
        if (!Utils.isServiceRunning(Constants.SERVICE_NAME, MainActivity.this)) {
            /**
             * 启动服务
             */
            this.startService(new Intent(MainActivity.this, MovingTrajectoryService.class));
        }
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
                        getResources().getString(R.string.main_tab_requirement))
                        .setActiveColorResource(R.color.colorAccent))
                .addItem(new BottomNavigationItem(R.mipmap.tab4_uncheck,
                        getResources().getString(R.string.main_tab_mine))
                        .setActiveColorResource(R.color.teal))
                .setFirstSelectedPosition(0)
                .initialise();
        indexFragment = IndexFragment.newInstance(getResources()
                .getString(R.string.main_tab_index));
        msgFragment = RecentContactsFragment.newInstance(getResources()
                .getString(R.string.main_tab_msg), sharePreferenceUtil.getSID());
        requirementFragment = RequirementFragment.newInstance(getResources()
                .getString(R.string.main_tab_requirement));
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
    public void onTabSelected(int position) {
        if (msgBadgeItem != null) {
            msgBadgeItem.hide();
        }
        switch (position) {
            case 0:
                switchFragment(indexFragment);
                break;
            case 1:
                // 打开单聊界面
                StatusCode status = NIMClient.getStatus();
                if (status == StatusCode.UNLOGIN) {
                    IMLogin imLogin = new IMLogin(MainActivity.this);
                    imLogin.login(1);
                }
                switchFragment(msgFragment);
                break;
            case 2:
                switchFragment(requirementFragment);
                break;
            case 3:
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
     * 检查更新接口
     */
    private void checkUpdatePort() {
        if (Utils.isNetworkAvailable(MainActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.checkUpdate)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("vType", Constants.APP_TYPE)
                    .addParams("vBuildCode", Utils.getVerName(MainActivity.this))
                    .addParams("vSystemType", 0 + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            showShortToast(MainActivity.this,
                                    getResources().getString(
                                            R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Utils.LogJson(response);
                            checkUpdate = new CheckUpdate();
                            checkUpdate = Utils.parserJsonResult(response, CheckUpdate.class);
                            if (Constants.OK.equals(checkUpdate.getFlag())) {
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
                            } else {
                                showShortToast(MainActivity.this,
                                        checkUpdate.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(MainActivity.this,
                    Constants.NETWORK_ERROR);
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
        if (0 == checkUpdate.getData().getVIfMust()) {
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
        tv_never_remind.setOnClickListener(v -> startDownlod());
        tv_update.setOnClickListener(v -> startDownlod());
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
                rxPermissions
                        .request(Manifest.permission.REQUEST_INSTALL_PACKAGES,
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
