package app.cn.extra.mall.user.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.GlideImageLoader;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.GetGuidePic;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.CountDownTimerUtil;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 引导页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class GuideActivity extends BaseActivty {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_promptly_next)
    TextView tvPromptlyNext;
    @BindView(R.id.tv_next)
    TextView tvNext;
    private SharePreferenceUtil sharePreferenceUtil;
    private GetGuidePic getGuidePic;
    /**
     * 声明倒计时工具类对象
     */
    private CountDownTimerUtil mCountDownTimerUtil;
    private int countDownInterval = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(GuideActivity.this,
                Constants.SAVE_USER);
        initBannerStyle();
        getGuidePicPort();
        initCountDownTimer();
    }

    /**
     * 初始化计时器相关配置
     */
    @SuppressLint("DefaultLocale")
    private void initCountDownTimer() {
        // 倒计时总时间为8S
        int millisInFuture = 8;
        // 每隔1s回调一次onTick
        countDownInterval = 1;
        // 将时间显示到界面
        tvNext.setText(String.format("跳过 %dS", millisInFuture));
        // 开始倒计时,传入总时间和每一秒要倒计时的时间
        countDown(millisInFuture, countDownInterval);
        // 开始执行
        mCountDownTimerUtil.start();
    }

    /**
     * 开始倒计时,传入总时间和每一秒要倒计时的时间
     */
    @SuppressLint("DefaultLocale")
    private void countDown(final long millisInFuture, long countDownInterval) {
        mCountDownTimerUtil = CountDownTimerUtil.getCountDownTimer()
                // 倒计时总时间
                .setMillisInFuture(millisInFuture * 1000)
                // 每隔多久回调一次onTick
                .setCountDownInterval(countDownInterval * 1000)
                // 每回调一次onTick执行
                .setTickDelegate(pMillisUntilFinished -> {
                    // 将时间显示到界面
                    tvNext.setText(String.format("跳过 %dS", pMillisUntilFinished / 1000));
                })
                // 结束倒计时执行
                .setFinishDelegate(() -> gotoMainActivity());
    }

    /**
     * 配置Banner相关属性
     */
    private void initBannerStyle() {
        /**
         * 显示圆形指示器
         */
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        /**
         * 指示器位置
         */
        banner.setIndicatorGravity(BannerConfig.CENTER);
        /**
         * 设置是否自动轮播，默认为自动
         */
        banner.isAutoPlay(false);
        /**
         * 设置图片加载器
         */
        banner.setImageLoader(new GlideImageLoader());
        /**
         * 设置滑动监听
         */
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == getGuidePic.getData().getPics().size() - 1) {
                    tvPromptlyNext.setVisibility(View.VISIBLE);
                } else {
                    tvPromptlyNext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 获取引导页相关数据接口
     * 在此处用以判断是否要展示引导页
     */
    private void getGuidePicPort() {
        if (Utils.isNetworkAvailable(GuideActivity.this)) {
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
                            /**
                             * 接口调用出错
                             */
                            gotoMainActivity();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            getGuidePic = new GetGuidePic();
                            getGuidePic = Utils.parserJsonResult(response, GetGuidePic.class);
                            if (Constants.OK.equals(getGuidePic.getFlag())) {
                                sharePreferenceUtil.setGuideEdition(getGuidePic.getData()
                                        .getGEdition());
                                List<String> urls = new ArrayList<String>();
                                if (null != getGuidePic.getData().getPics()) {
                                    for (int i = 0; i < getGuidePic.getData().getPics().size(); i++) {
                                        urls.add(getGuidePic.getData().getPics().get(i).getUrl());
                                    }
                                }
                                banner.setImages(urls);
                                banner.start();
                            } else {
                                gotoMainActivity();
                            }
                        }
                    });
        } else {
            showShortToast(GuideActivity.this, Constants.NETWORK_ERROR);
            gotoMainActivity();
        }
    }

    @OnClick({R.id.tv_promptly_next, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_promptly_next:
                gotoMainActivity();
                break;
            case R.id.tv_next:
                gotoMainActivity();
                break;
            default:
        }
    }

    /**
     * 判断用户是否登陆过，并根据判断结果跳转至对应界面
     */
    private void judgeUserLogged() {
        if (!TextUtils.isEmpty(sharePreferenceUtil.getUID())) {
            gotoMainActivity();
        } else {
            gotoLoginActivity();
        }
    }

    /**
     * 跳转至主界面
     */
    private void gotoMainActivity() {
        Intent intent = new Intent();
        intent.setClass(GuideActivity.this, MainActivity.class);
        startActivity(intent);
        closeAll();
    }

    /**
     * 跳转至登录界面
     */
    private void gotoLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(GuideActivity.this, LoginActivity.class);
        startActivity(intent);
        closeAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCountDownTimerUtil.cancel();
    }
}
