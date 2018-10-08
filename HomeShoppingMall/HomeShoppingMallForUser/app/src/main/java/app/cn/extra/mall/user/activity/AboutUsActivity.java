package app.cn.extra.mall.user.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.tbruyelle.rxpermissions.RxPermissions;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.Downloader;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.utils.Util;
import app.cn.extra.mall.user.utils.VersionUtil;
import app.cn.extra.mall.user.vo.CheckUpdate;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * 关于我们
 */
public class AboutUsActivity extends BaseActivty {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
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
    SharePreferenceUtil sharePreferenceUtil = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(AboutUsActivity.this, Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        downloader = new Downloader(AboutUsActivity.this);
        rxPermissions = new RxPermissions(this);
    }

    @OnClick({R.id.img_back, R.id.rl_feedback, R.id.rl_check_update, R.id.rl_customer_service_center})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_feedback:
                intent = new Intent(AboutUsActivity.this, FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_check_update:
                // 获取版本号 检查更新
                VersionPort();
                break;
            case R.id.rl_customer_service_center:
                intent = new Intent(AboutUsActivity.this, CustomerServiceCenterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void VersionPort() {
        if (Utils.isNetworkAvailable(AboutUsActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            String versionCodes = "" + VersionUtil.getLocalVersion(AboutUsActivity.this);
            OkHttpUtils
                    .post()
                    .url(Constants.checkUpdate)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("vType", Constants.APP_TYPE)
                    .addParams("vBuildCode", Util.getVerName(AboutUsActivity.this))
                    .addParams("vSystemType", 0 + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            checkUpdate = new CheckUpdate();
                            checkUpdate = Utils.parserJsonResult(response, CheckUpdate.class);
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
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
                                CustomToast.showToast(AboutUsActivity.this,
                                        checkUpdate.getErrorString(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            CustomToast.showToast(AboutUsActivity.this,
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
        final Dialog alertDialog = new android.support.v7.app.AlertDialog.Builder(this, R.style.Theme_Transparent).
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
                showShortToast(AboutUsActivity.this, getResources()
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
                                showShortToast(AboutUsActivity.this, getResources()
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
            showShortToast(AboutUsActivity.this, getResources()
                    .getString(R.string.downloading));
        }
    }
//    private void showUpdateDialog() {
//        // TODO Auto-generated method stub
//
//        // 取得自定义View
//        LayoutInflater layoutInflater = LayoutInflater.from(AboutUsActivity.this);
//        View view = layoutInflater.inflate(R.layout.dialog_common_text, null);
//        view.setAlpha(0.8f);
//        final Dialog alertDialog = new AlertDialog.Builder(AboutUsActivity.this,
//                R.style.Theme_Transparent).
//                setView(view).
//                create();
////        alertDialog.setCancelable(false);
////        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.show();
//        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
//        lp.dimAmount = 0.2f;
//        alertDialog.getWindow().setAttributes(lp);
//        TextView tv_delete = (TextView) view.findViewById(R.id.tv_delete);
//        tv_delete.setText("当前已是最新版本");
//    }
//
//    private void showVersionDialog(final Version.DataBean data) {
//        // 取得自定义View
//        LayoutInflater layoutInflater = LayoutInflater.from(this);
//        View view = layoutInflater.inflate(R.layout.dialog_version_manage, null);
//        view.setAlpha(1f);
//        TextView content = (TextView) view.findViewById(R.id.content);
//        TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
//        TextView tv_never_remind = (TextView) view.findViewById(R.id.tv_never_reminds);
//        TextView tv_tip = (TextView) view.findViewById(R.id.tv_tip);
//        LinearLayout ll_update = (LinearLayout) view.findViewById(R.id.ll_update);
//        RelativeLayout rl_update = (RelativeLayout) view.findViewById(R.id.rl_update);
//        TextView tv_update = (TextView) view.findViewById(R.id.tv_update);
//        final Dialog alertDialog = new AlertDialog.Builder(this, R.style.Theme_Transparent).
//                setView(view).
//                create();
//        if (TextUtils.isEmpty(data.getVIfMust())) {
//            rl_update.setVisibility(View.GONE);
//            ll_update.setVisibility(View.VISIBLE);
//        } else {
//            alertDialog.setCancelable(false);
//            alertDialog.setCanceledOnTouchOutside(false);
//            ll_update.setVisibility(View.GONE);
//            rl_update.setVisibility(View.VISIBLE);
//        }
//
//        alertDialog.show();
//        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
//        lp.dimAmount = 0.2f;
//        alertDialog.getWindow().setAttributes(lp);
//
//
//        tv_tip.setText("有可用的新版本" + data.getVCode());
//        content.setText(data.getVContent());
//        tv_yes.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                alertDialog.dismiss();
//            }
//        });
//        tv_never_remind.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //启动服务
//                Intent service = new Intent(AboutUsActivity.this, UpdateServices.class);
//                service.putExtra("url", data.getVUrl());
//                startService(service);
//                alertDialog.dismiss();
//            }
//        });
//        tv_update.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                //启动服务
//                Intent service = new Intent(AboutUsActivity.this, UpdateServices.class);
//                service.putExtra("url", data.getVUrl());
//                startService(service);
//                alertDialog.dismiss();
//            }
//        });
//    }
}
