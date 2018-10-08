package com.netease.nim.uikit.business.session.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.merchant.Constants;
import com.netease.nim.uikit.merchant.PopMenu;

import java.util.ArrayList;

/**
 * Created by zhoujianghua on 2015/9/10.
 */
public abstract class BaseMessageActivity extends UI {

    protected String sessionId;

    private SessionCustomization customization;

    private MessageFragment messageFragment;

    protected abstract MessageFragment fragment();

    protected abstract int getContentViewId();

    protected abstract void initToolBar();

    /**
     * 修改备注名
     */
    protected abstract void changeRemarks();

    /**
     * 查看用户浏览记录
     */
    protected abstract void viewUserHistory();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentViewId());
        initToolBar();
        parseIntent();

        messageFragment = (MessageFragment) switchContent(fragment());
    }

    @Override
    public void onBackPressed() {
        if (messageFragment == null || !messageFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (messageFragment != null) {
            messageFragment.onActivityResult(requestCode, resultCode, data);
        }

        if (customization != null) {
            customization.onActivityResult(this, requestCode, resultCode, data);
        }
    }

    private void parseIntent() {
        sessionId = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
        customization = (SessionCustomization) getIntent().getSerializableExtra(Extras.EXTRA_CUSTOMIZATION);

//        if (customization != null) {
        addRightCustomViewOnActionBar(this);
//        }
    }

    // 添加action bar的右侧按钮及响应事件
    private void addRightCustomViewOnActionBar(UI activity) {
//        if (buttons == null || buttons.size() == 0) {
//            return;
//        }

        Toolbar toolbar = getToolBar();
        if (toolbar == null) {
            return;
        }

        LinearLayout view = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.nim_action_bar_custom_view, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        for (final SessionCustomization.OptionsButton button : buttons) {
        ImageView imageView = new ImageView(activity);
        imageView.setImageResource(R.drawable.icon_more);
        imageView.setBackgroundResource(R.drawable.nim_nim_action_bar_button_selector);
        imageView.setPadding(ScreenUtil.dip2px(10), 0, ScreenUtil.dip2px(10), 0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    button.onClick(BaseMessageActivity.this, v, sessionId);
                actionBarRightClicked(v);
            }
        });
        view.addView(imageView, params);
//        }

        toolbar.addView(view, new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.RIGHT | Gravity.CENTER));
    }
    private int getScreenWidth() {
        // TODO Auto-generated method stub
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        return dm.widthPixels; // 屏幕宽（像素，如：480px）
    }
    private void actionBarRightClicked(View v) {
        final ArrayList<String> data = new ArrayList<String>();
        data.add("修改备注名");
        if ("1".equals(Constants.sType)) {
            data.add("用户浏览记录");
        }

        //添加数据

        // 初始化弹出菜单	 POPMENU
        PopMenu popMenu = new PopMenu(this, getScreenWidth() / 3, data);
        popMenu.setOnItemClickListener(new PopMenu.OnItemClickListener() {

            @Override
            public void onItemClick(int index) {
                // TODO Auto-generated method stub
                switch (index) {
                    case 0:
                        changeRemarks();
                        break;
                    case 1:
                        viewUserHistory();
                        break;
                    default:
                }
            }
        });
        popMenu.showAsDropDown(v);
    }
}
