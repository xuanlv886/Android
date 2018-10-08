package com.netease.nim.uikit.business.session.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nim.uikit.merchant.Constants;
import com.netease.nim.uikit.merchant.GetUserMemoName;
import com.netease.nim.uikit.merchant.SetUserMemoName;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;


/**
 * 点对点聊天界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class P2PMessageActivity extends BaseMessageActivity {

    private boolean isResume = false;

    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }
        intent.setClass(context, P2PMessageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 单聊特例话数据，包括个人信息，
        requestBuddyInfo();
        displayOnlineState();
        registerObservers(true);
        registerOnlineStateChangeListener(true);
        /**
         * 查询是否有备注名并更新页面
         */
        getUserMemoName();
        getSUserInfo();
    }

    /**
     * 获取当前商户信息并更新本地缓存
     */
    private void getSUserInfo() {
        /**
         * 从本地数据库获取用户信息
         */
        NimUserInfo user = NIMClient.getService(UserService.class).getUserInfo(sessionId);
        if (user != null) {
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(user.getExtension());
                Constants.sType = jsonObject.getString("sType");
                Constants.UID = jsonObject.getString("uId");
                Constants.SID = jsonObject.getString("sId");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            getUserInfoToServer(sessionId);
        }
    }

    /**
     * 从服务器获取用户信息
     *
     * @param account
     */
    private void getUserInfoToServer(String account) {
        List<String> list = new ArrayList<>();
        list.add(account);
        NIMClient.getService(UserService.class).fetchUserInfo(list)
                .setCallback(new RequestCallback<List<NimUserInfo>>() {
                    @Override
                    public void onSuccess(List<NimUserInfo> nimUserInfos) {
                        try {
                            org.json.JSONObject jsonObject = new org.json.JSONObject(nimUserInfos.get(0).getExtension());
                            Constants.sType = jsonObject.getString("sType");
                            Constants.UID = jsonObject.getString("uId");
                            Constants.SID = jsonObject.getString("sId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(int i) {

                    }

                    @Override
                    public void onException(Throwable throwable) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerOnlineStateChangeListener(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;
    }

    /**
     * 获取某用户为别人设置的备注名
     */
    private void getUserMemoName() {
        if (Utils.isNetworkAvailable(P2PMessageActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getUserMemoName)
                    .addParams("uId", Constants.MYUID)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            GetUserMemoName getUserMemoName = new GetUserMemoName();
                            getUserMemoName = Utils.parserJsonResult(response,
                                    GetUserMemoName.class);
                            if (Constants.OK.equals(getUserMemoName.getFlag())) {
                                for (int i = 0; i < getUserMemoName.getData().size(); i++) {
                                    if (getUserMemoName.getData().get(i).getUmnToId().contains(Constants.UID)) {
                                        setTitle(getUserMemoName.getData().get(i).getUmnName());
                                    }
                                }
                            }
                        }
                    });
        }
    }

    private void requestBuddyInfo() {
        setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
    }

    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
    }

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
        }
    };

    private UserInfoObserver uinfoObserver;

    OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            // 更新 toolbar
            if (accounts.contains(sessionId)) {
                // 按照交互来展示
                displayOnlineState();
            }
        }
    };

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
    }

    private void displayOnlineState() {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(sessionId);
        setSubTitle(detailContent);
    }

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        requestBuddyInfo();
                    }
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, true);
    }

    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, false);
        }
    }

    /**
     * 命令消息接收观察者
     */
    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };

    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }

        String content = message.getContent();
        try {
            JSONObject json = JSON.parseObject(content);
            int id = json.getIntValue("id");
            if (id == 1) {
                // 正在输入
                Toast.makeText(P2PMessageActivity.this, "对方正在输入...", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(P2PMessageActivity.this, "command: " + content, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.nim_message_activity;
    }

    @Override
    protected void initToolBar() {
        ToolBarOptions options = new NimToolBarOptions();
        setToolBar(R.id.toolbar, options);
    }

    @Override
    protected void changeRemarks() {
        showDialog(Constants.UID);
    }

    private void showDialog(String toUid) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_set_memo_name, null);
        final Dialog alertDialog = new AlertDialog.Builder(this, R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        EditText et_live_content = view.findViewById(R.id.et_live_content);
        tv__live_never_reminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tv_live_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String memoName = et_live_content.getText().toString().trim();
                if (TextUtils.isEmpty(memoName)) {
                    Toast.makeText(P2PMessageActivity.this, "请填写备注名！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //为用户设置备注名
                setUserMemoName(alertDialog, Constants.MYUID, toUid, memoName);
            }
        });
    }

    /**
     * 为用户设置备注名
     */
    private void setUserMemoName(final Dialog dialog, String fromUid, String toUid, String memoName) {
        if (Utils.isNetworkAvailable(P2PMessageActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.setUserMemoName)
                    .addParams("fromUid", fromUid)
                    .addParams("toUid", toUid)
                    .addParams("memoName", memoName)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            System.out.println("Exception:" + e.toString());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            System.out.println("response:" + response);

                            SetUserMemoName setUserMemoName = new SetUserMemoName();
                            setUserMemoName = Utils.parserJsonResult(response, SetUserMemoName.class);
                            if (Constants.OK.equals(setUserMemoName.getFlag())) {
                                if (Constants.OK.equals(setUserMemoName.getData().getStatus())) {
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(P2PMessageActivity.this, "修改备注名成功！", Toast.LENGTH_SHORT).show();
                                    setTitle(memoName);
                                } else {
                                    Toast.makeText(P2PMessageActivity.this,
                                            setUserMemoName.getData().getErrorString(), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(P2PMessageActivity.this,
                                        setUserMemoName.getErrorString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(P2PMessageActivity.this, Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }
}
