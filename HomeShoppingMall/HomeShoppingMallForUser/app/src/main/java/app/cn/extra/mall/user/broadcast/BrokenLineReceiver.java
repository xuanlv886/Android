package app.cn.extra.mall.user.broadcast;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.activity.LoginActivity;
import app.cn.extra.mall.user.activity.MainActivity;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.utils.Util;
import cfkj.app.cn.cfkjcommonlib.common.ActivityCollector;

/**
 * Description 后台广播接收器，用来显示互踢弹窗
 * Data 2018/7/26-11:37
 * Content
 *
 * @author L
 */
public class BrokenLineReceiver extends BroadcastReceiver {

    private SharePreferenceUtil sharePreferenceUtil;

    private Context mContext = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Constants.BROKEN_LINE_ACTION.equals(intent.getAction())) {
            sharePreferenceUtil = new SharePreferenceUtil(context, Constants.SAVE_USER);
            mContext = context;
            showDialog();
        }
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
                gotoMain(alertDialog);
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
     * 清空存储的用户信息并跳转至首页
     */
    private void gotoMain(Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
//        sharePreferenceUtil.setBrokenLineTag("");
        sharePreferenceUtil.setUID("");
        sharePreferenceUtil.setCityName("");
        sharePreferenceUtil.setOrderType("");
        sharePreferenceUtil.setOrderData("");
        sharePreferenceUtil.setPassWordFlag("");
        sharePreferenceUtil.setAcId("");
        sharePreferenceUtil.setFirstOpen("");
        sharePreferenceUtil.setPayCode("");
        sharePreferenceUtil.setSecurity("");
        sharePreferenceUtil.setTotalMoney("");
        sharePreferenceUtil.setVersion("");
        sharePreferenceUtil.setVersionPath("");
        Intent intent = new Intent();
        intent.setClass(mContext, MainActivity.class);
        mContext.startActivity(intent);
        ActivityCollector.finishAll();
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
        sharePreferenceUtil.setOrderType("");
        sharePreferenceUtil.setOrderData("");
        sharePreferenceUtil.setPassWordFlag("");
        sharePreferenceUtil.setAcId("");
        sharePreferenceUtil.setFirstOpen("");
        sharePreferenceUtil.setPayCode("");
        sharePreferenceUtil.setSecurity("");
        sharePreferenceUtil.setTotalMoney("");
        sharePreferenceUtil.setVersion("");
        sharePreferenceUtil.setVersionPath("");
        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        intent.putExtra("intentTag", 1);
        mContext.startActivity(intent);
        ActivityCollector.finishAll();
    }
}
