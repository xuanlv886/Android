package app.cn.extra.mall.merchant.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;
import com.tbruyelle.rxpermissions.RxPermissions;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.Downloader;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.CheckUpdate;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 关于我们页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class AboutUsActivity extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.rl_feedback)
    RelativeLayout rlFeedback;
    @BindView(R.id.rl_check_update)
    RelativeLayout rlCheckUpdate;
    @BindView(R.id.rl_customer_service)
    RelativeLayout rlCustomerService;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(AboutUsActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        downloader = new Downloader(AboutUsActivity.this);
        rxPermissions = new RxPermissions(this);
    }

    @OnClick({R.id.img_back, R.id.rl_feedback, R.id.rl_check_update, R.id.rl_customer_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_feedback:
                gotoFeedBackActivity();
                break;
            case R.id.rl_check_update:
                checkUpdatePort();
                break;
            case R.id.rl_customer_service:
                gotoCustomerServiceCenterActivity();
                break;
                default:
        }
    }

    /**
     * 检查更新接口
     */
    private void checkUpdatePort() {
        if (Utils.isNetworkAvailable(AboutUsActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.checkUpdate)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("vType", Constants.APP_TYPE)
                    .addParams("vBuildCode", Utils.getVerName(AboutUsActivity.this))
                    .addParams("vSystemType", 0 + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(AboutUsActivity.this,
                                    getResources().getString(
                                            R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
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
                                    showShortToast(AboutUsActivity.this,
                                            getResources().getString(R.string.notNeedUpdate));
                                }
                            } else {
                                showShortToast(AboutUsActivity.this,
                                        checkUpdate.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(AboutUsActivity.this,
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
        TextView content =  view.findViewById(R.id.content);
        TextView tv_yes = view.findViewById(R.id.tv_yes);
        TextView tv_never_remind = view.findViewById(R.id.tv_never_reminds);
        TextView tv_tip =  view.findViewById(R.id.tv_tip);
        LinearLayout ll_update =  view.findViewById(R.id.ll_update);
        RelativeLayout rl_update =  view.findViewById(R.id.rl_update);
        TextView tv_update =  view.findViewById(R.id.tv_update);
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
                showShortToast(AboutUsActivity.this, getResources()
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

    /**
     * 跳转至意见反馈页
     */
    private void gotoFeedBackActivity() {
        Intent intent = new Intent();
        intent.setClass(AboutUsActivity.this, FeedBackActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转至客服中心界面
     */
    private void gotoCustomerServiceCenterActivity() {
        Intent intent = new Intent();
        intent.setClass(AboutUsActivity.this,
                CustomerServiceCenterActivity.class);
        startActivity(intent);
    }
}
