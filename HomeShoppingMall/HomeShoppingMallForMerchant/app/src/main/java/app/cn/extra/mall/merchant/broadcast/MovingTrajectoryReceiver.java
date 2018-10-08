package app.cn.extra.mall.merchant.broadcast;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.activity.GoodsDetailActivity;
import app.cn.extra.mall.merchant.activity.LoginActivity;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.utils.Util;
import app.cn.extra.mall.merchant.utils.amap.AMapLocationUtils;
import cfkj.app.cn.cfkjcommonlib.common.ActivityCollector;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 后台计时广播接收器，每1分钟向服务器提交一次商户所在位置的经纬度
 * Data 2018/7/26-11:37
 * Content
 *
 * @author L
 */
public class MovingTrajectoryReceiver extends BroadcastReceiver implements AMapLocationUtils.AMapLocationCallBack {

    private SharePreferenceUtil sharePreferenceUtil;

    private Context mContext = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Constants.TIME_CHANGED_ACTION.equals(intent.getAction())) {
            sharePreferenceUtil = new SharePreferenceUtil(context, Constants.SAVE_USER);
            mContext = context;
            /**
             * 开启高德地图定位服务
             */
            AMapLocationUtils aMapLocationUtils = new AMapLocationUtils(
                    this);
            aMapLocationUtils.initLocationClient(mContext,
                    true);
            aMapLocationUtils.startLocation(mContext);
        } else if (Constants.RIGHTMENU_ITEM_CLICK_ACTION.equals(intent.getAction())) {
            String pid = intent.getStringExtra("pid");
            Intent intentAcitvity = new Intent(context, GoodsDetailActivity.class);
            intentAcitvity.putExtra("pid", pid);
            context.startActivity(intentAcitvity);
        } else if (Constants.BROKEN_LINE_ACTION.equals(intent.getAction())) {
            sharePreferenceUtil = new SharePreferenceUtil(context, Constants.SAVE_USER);
            mContext = context;
            showDialog();
        }
    }

    /**
     * 提交商户所在位置经纬度接口（一分钟提交一次）
     */
    private void submitMerchantLonlatPort(Context context, double lon, double lat) {
        if (Utils.isNetworkAvailable(context)) {
            OkHttpUtils
                    .post()
                    .url(Constants.submitMerchantLonlat)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
                    .addParams("mtLon", lon + "")
                    .addParams("mtLat", lat + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {

                        }
                    });
        } else {
//            CustomToast.showToast(context, Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void onSuccess(AMapLocation aMapLocation) {
        submitMerchantLonlatPort(mContext, aMapLocation.getLongitude(),
                aMapLocation.getLatitude());
    }

    @Override
    public void onError(String errInfo) {
        Utils.LogE(errInfo);
    }

    /**
     * 互踢弹窗
     */
    private void showDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(Util.getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(Util.getActivity(), R.style.Theme_Transparent).
                setView(view).
                setCancelable(false).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        tv_live_yes.setText("重新登录");
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        tv__live_never_reminds.setText("放弃登录");
        TextView tv_live_content = view.findViewById(R.id.tv_live_content);
        tv_live_content.setText("当前账号已在其它设备登录");

        tv__live_never_reminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitAccount(alertDialog);
            }
        });
        tv_live_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除信息
                quitAccount(alertDialog);
            }
        });

    }

    /**
     * 清空存储的用户信息并跳转至登录界面
     */
    private void quitAccount(Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
        sharePreferenceUtil.setBrokenLineTag("");
        sharePreferenceUtil.setUID("");
        sharePreferenceUtil.setCityName("");
        sharePreferenceUtil.setPassWordFlag("");
        sharePreferenceUtil.setPassWordFlag("");
        sharePreferenceUtil.setSChecked("");
        sharePreferenceUtil.setFirstOpen("");
        sharePreferenceUtil.setPayCode("");
        sharePreferenceUtil.setStoreAcId("");
        sharePreferenceUtil.setTotalMoney("");
        sharePreferenceUtil.setSID("");
        sharePreferenceUtil.setSType("");
        sharePreferenceUtil.setGuideEdition(0);
        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        intent.putExtra("intentTag", 1);
        mContext.startActivity(intent);
        ActivityCollector.finishAll();
    }
}
