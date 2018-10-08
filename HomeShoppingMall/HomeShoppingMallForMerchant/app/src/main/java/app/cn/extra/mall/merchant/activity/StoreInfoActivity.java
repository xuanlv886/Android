package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.SingleInfoEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.GlideImageLoader;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.CommonVo;
import app.cn.extra.mall.merchant.vo.GetStoreUserInfo;
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
 * Description 商户信息页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class StoreInfoActivity extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_sName)
    TextView tvSName;
    @BindView(R.id.rl_sName)
    RelativeLayout rlSName;
    @BindView(R.id.tv_sDescribe)
    TextView tvSDescribe;
    @BindView(R.id.rl_sDescribe)
    RelativeLayout rlSDescribe;
    @BindView(R.id.rl_sLegal)
    RelativeLayout rlSLegal;
    @BindView(R.id.tv_sLeader)
    TextView tvSLeader;
    @BindView(R.id.tv_sLegal)
    TextView tvSLegal;
    @BindView(R.id.tv_sTel)
    TextView tvSTel;
    @BindView(R.id.rl_sTel)
    RelativeLayout rlSTel;
    @BindView(R.id.tv_sAddress)
    TextView tvSAddress;
    @BindView(R.id.tv_sLevel)
    TextView tvSLevel;
    @BindView(R.id.tv)
    TextView tv;
    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 临时存储商户账号信息
     */
    private GetStoreUserInfo getStoreUserInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(StoreInfoActivity.this,
                Constants.SAVE_USER);
        /**
         * 个人身份商户
         */
        if (Constants.M_PERSONAL.equals(sharePreferenceUtil.getSType())) {
            rlSLegal.setVisibility(View.GONE);
        }
        /**
         * 注册订阅者
         */
        EventBus.getDefault().register(this);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        getStoreUserInfoPort();
    }

    /**
     * 获取商户个人、店铺相关信息接口
     */
    private void getStoreUserInfoPort() {
        if (Utils.isNetworkAvailable(StoreInfoActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getStoreUserInfo)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(StoreInfoActivity.this,
                                    getResources().getString(
                                            R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            };
                            Utils.LogJson(response);
                            getStoreUserInfo = new GetStoreUserInfo();
                            getStoreUserInfo = Utils.parserJsonResult(response, GetStoreUserInfo.class);
                            if (Constants.OK.equals(getStoreUserInfo.getFlag())) {
                                if (Constants.OK.equals(getStoreUserInfo.getData().getStatus())) {
                                    /**
                                     * 填充数据
                                     */
                                    if (0 < getStoreUserInfo.getData().getStorePics().size()) {
                                        tv.setText(getResources().getString(R.string.storePics));
                                        initBannerSetting();
                                        List<String> urls = new ArrayList<String>();
                                        for (int i = 0; i < getStoreUserInfo.getData().getStorePics()
                                                .size(); i ++) {
                                            urls.add(getStoreUserInfo.getData().getStorePics()
                                                    .get(i).getUrl()+ "?x-oss-process=image/resize,w_"
                                                    + (Utils.getScreenWidth(StoreInfoActivity.this)));
                                        }
                                        banner.setImages(urls);
                                        banner.start();
                                    }
                                    tvSName.setText(getStoreUserInfo.getData().getSName());
                                    if (!TextUtils.isEmpty(getStoreUserInfo.getData().getSDescribe())) {
                                        tvSDescribe.setTextColor(getResources().getColor(
                                                R.color.black));
                                        if (12 < getStoreUserInfo.getData().getSDescribe().length()) {
                                            tvSDescribe.setText(getStoreUserInfo.getData().getSDescribe()
                                                    .substring(0, 12) + "···");
                                        } else {
                                            tvSDescribe.setText(getStoreUserInfo.getData().getSDescribe());
                                        }
                                    } else {
                                        tvSDescribe.setText(getResources().getString(
                                                R.string.unSetting));
                                        tvSDescribe.setTextColor(getResources().getColor(
                                                R.color.graytext));
                                    }
                                    tvSLeader.setText(getStoreUserInfo.getData().getSLeader());
                                    tvSLegal.setText(getStoreUserInfo.getData().getSLegal());
                                    tvSTel.setText(getStoreUserInfo.getData().getSTel());
                                    tvSAddress.setText(getStoreUserInfo.getData().getSAddress());
                                    tvSLevel.setText(getStoreUserInfo.getData().getSLevel() + "星");
                                } else {
                                    showShortToast(StoreInfoActivity.this,
                                            getStoreUserInfo.getData()
                                                    .getErrorString());
                                }
                            } else {
                                showShortToast(StoreInfoActivity.this,
                                        getStoreUserInfo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(StoreInfoActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 初始化Banner相关配置
     */
    private void initBannerSetting() {
        banner.setVisibility(View.VISIBLE);
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
        banner.isAutoPlay(true);
        /**
         * 设置自动轮播时间间隔
         */
        banner.setDelayTime(3000);
        /**
         * 设置图片加载器
         */
        banner.setImageLoader(new GlideImageLoader());
    }

    @OnClick({R.id.img_back, R.id.rl_sName, R.id.rl_sDescribe, R.id.rl_sTel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_sName:
                gotoChangeSingleInfoActivity("sName", tvSName.getText().toString().trim());
                break;
            case R.id.rl_sDescribe:
                try {
                    gotoChangeSingleInfoActivity("sDescribe",
                            getStoreUserInfo.getData().getSDescribe());
                } catch (Exception e) {
                    Utils.LogE(e, "商户店铺描述异常");
                }
                break;
            case R.id.rl_sTel:
                gotoChangeSingleInfoActivity("sTel", tvSTel.getText().toString().trim());
                break;
                default:
        }
    }

    /**
     * 跳转至修改单条信息页
     * @param tag 标识要修改的单条信息的内容
     * @param content 原内容
     */
    private void gotoChangeSingleInfoActivity(String tag, String content) {
        Intent intent = new Intent();
        intent.setClass(StoreInfoActivity.this, ChangeSingleInfoActivity.class);
        intent.putExtra("tag", tag);
        intent.putExtra("content", content);
        startActivity(intent);
    }

    /**
     * 消息处理
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SingleInfoEventBus(SingleInfoEvent event){
        if (!TextUtils.isEmpty(event.getsName())) {
            tvSName.setText(event.getsName());
        }
        if (!TextUtils.isEmpty(event.getsDescribe())) {
            tvSDescribe.setTextColor(getResources().getColor(
                    R.color.black));
            if (12 < event.getsDescribe().length()) {
                tvSDescribe.setText(event.getsDescribe()
                        .substring(0, 12) + "···");
            } else {
                tvSDescribe.setText(event.getsDescribe());
            }
        }
        if (!TextUtils.isEmpty(event.getsTel())) {
            tvSTel.setText(event.getsTel());
        }
        changeStoreUserInfoPort();
    }

    /**
     * 修改商户个人、店铺相关信息接口
     */
    private void changeStoreUserInfoPort() {
        if (Utils.isNetworkAvailable(StoreInfoActivity.this)) {
            String sDescribe = tvSDescribe.getText().toString().trim();
            if (TextUtils.isEmpty(sDescribe)
                    || getResources().getString(R.string.unSetting).equals(sDescribe)) {
                sDescribe = "";
            }
            OkHttpUtils
                    .post()
                    .url(Constants.changeStoreUserInfo)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
                    .addParams("which", "1")
                    .addParams("uNickName", "")
                    .addParams("pTag", "")
                    .addParams("pName", "")
                    .addParams("uSex", "")
                    .addParams("uEmail", "")
                    .addParams("uBirthday", "")
                    .addParams("uTel", "")
                    .addParams("sName", tvSName.getText().toString().trim())
                    .addParams("sDescribe", sDescribe)
                    .addParams("sTel", tvSTel.getText().toString().trim())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            CustomToast.showToast(StoreInfoActivity.this,
                                    getResources().getString(
                                            R.string.portError), Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Utils.LogJson(response);
                            CommonVo commonVo = new CommonVo();
                            commonVo = Utils.parserJsonResult(response, CommonVo.class);
                            if (Constants.OK.equals(commonVo.getFlag())) {
                                if (Constants.OK.equals(commonVo.getData().getStatus())) {
//                                    CustomToast.showToast(AccountInfoActivity.this,
//                                            getResources().getString(R.string.updateSuccess),
//                                            Toast.LENGTH_SHORT);
                                } else {
                                    CustomToast.showToast(StoreInfoActivity.this,
                                            commonVo.getData()
                                                    .getErrorString(), Toast.LENGTH_SHORT);
                                }
                            } else {
                                CustomToast.showToast(StoreInfoActivity.this,
                                        commonVo.getErrorString(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            CustomToast.showToast(StoreInfoActivity.this,
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 注销注册
         */
        EventBus.getDefault().unregister(this);
    }
}
