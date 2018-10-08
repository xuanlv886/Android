package app.cn.extra.mall.user.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.tbruyelle.rxpermissions.RxPermissions;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.utils.amap.AMapLocationUtils;
import app.cn.extra.mall.user.vo.GetAreaCode;
import app.cn.extra.mall.user.vo.GetGuidePic;
import butterknife.BindView;
import butterknife.ButterKnife;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 启动页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class SplashActivity extends BaseActivty implements AMapLocationUtils.AMapLocationCallBack{

    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 动态申请权限
     */
    RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(SplashActivity.this,
                Constants.SAVE_USER);
        rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    /**
                     * 渐变展示启动屏
                     */
                    AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
                    aa.setDuration(4500);
                    rlRoot.startAnimation(aa);
                    aa.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation arg0) {
                            /**
                             * 判断用户是否第一次打开APP
                             */
                            if(TextUtils.isEmpty(sharePreferenceUtil.getFirstOpen())) {
                                sharePreferenceUtil.setFirstOpen("false");
                                jumpToGuideActivity();
                            }else { // 不是第一次打开
                                getGuidePicPort();
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationStart(Animation animation) {
                        }

                    });
                    if (granted) {
                        /**
                         * 开启高德地图定位服务
                         */
                        AMapLocationUtils aMapLocationUtils = new AMapLocationUtils(
                                SplashActivity.this);
                        aMapLocationUtils.initLocationClient(SplashActivity.this,
                                true);
                        aMapLocationUtils.startLocation(SplashActivity.this);
                    } else {
                        showLongToast(SplashActivity.this, getResources().getString(
                                R.string.permissionError));
                    }
                });
    }


    /**
     * 获取引导页相关数据接口
     * 在此处用以判断是否要展示引导页
     */
    private void getGuidePicPort() {
        if (Utils.isNetworkAvailable(SplashActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getGuidePic)
                    .addParams("gAppType", Constants.APP_TYPE)
                    .addParams("gEdition", sharePreferenceUtil.getGuideEdition() + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {

                            GetGuidePic getGuidePic = new GetGuidePic();
                            getGuidePic = Utils.parserJsonResult(response, GetGuidePic.class);
                            if (Constants.OK.equals(getGuidePic.getFlag())) {
                                /**
                                 * 判断是否需要展示引导页
                                 */
                                if (1 == getGuidePic.getData().getIsShow()) {
                                    jumpToGuideActivity();
                                } else {
                                    jumpToMainActivity();
                                }
                            }
                        }
                    });
        } else {
            showShortToast(SplashActivity.this, Constants.NETWORK_ERROR);
            jumpToMainActivity();
        }
    }
    /**
     * 判断用户是否登陆过，并根据判断结果跳转至对应界面
     */
    private void judgeUserLogged() {
        if (!TextUtils.isEmpty(sharePreferenceUtil.getUID())) {
            jumpToMainActivity();
        } else {
            jumpToLoginActivity();
        }
    }
    /**
     * 跳转至引导页
     */
    private void jumpToGuideActivity() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, GuideActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 跳转至主界面
     */
    private void jumpToMainActivity() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        closeAll();
    }



    /**
     * 跳转至登录界面
     */
    private void jumpToLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        closeAll();
    }


    /**
     * 获取行政区划信息接口
     */
    private void getAreaCodePort(String acCode) {
        if (TextUtils.isEmpty(acCode)) {
            return;
        }
        if (Utils.isNetworkAvailable(SplashActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getAreaCode)
                    .addParams("acCode", acCode)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {

                            GetAreaCode getAreaCode = new GetAreaCode();
                            getAreaCode = Utils.parserJsonResult(response, GetAreaCode.class);
                            if (Constants.OK.equals(getAreaCode.getFlag())) {
                                if (Constants.OK.equals(getAreaCode.getData().getStatus())) {
                                    sharePreferenceUtil.setAcId(getAreaCode.getData()
                                            .getAcId());
                                }
                            }
                        }
                    });
        }
    }

    /**
     * 获取定位结果成功
     */
    @Override
    public void onSuccess(AMapLocation aMapLocation) {
        getAreaCodePort(aMapLocation.getAdCode());
    }

    /**
     * 获取定位结果失败
     */
    @Override
    public void onError(String errInfo) {
        Utils.LogE(errInfo);
    }
}
