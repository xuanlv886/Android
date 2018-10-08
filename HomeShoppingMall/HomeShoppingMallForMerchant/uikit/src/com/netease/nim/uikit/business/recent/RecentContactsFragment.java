package com.netease.nim.uikit.business.recent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.MerchantStaticInfo;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.common.badger.Badger;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog;
import com.netease.nim.uikit.common.ui.drop.DropCover;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nim.uikit.common.ui.recyclerview.listener.SimpleClickListener;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nim.uikit.merchant.Constants;
import com.netease.nim.uikit.merchant.GetUserMemoName;
import com.netease.nim.uikit.merchant.SetUserMemoName;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import okhttp3.Call;

import static com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog.onSeparateItemClickListener;

/**
 * 最近联系人列表(会话列表)
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class RecentContactsFragment extends TFragment {

    // 置顶功能可直接使用，也可作为思路，供开发者充分利用RecentContact的tag字段
    public static final long RECENT_TAG_STICKY = 1; // 联系人置顶tag

    // view
    private RecyclerView recyclerView;

    private View emptyBg;

    private TextView emptyHint;

    // data
    private List<RecentContact> items;

    private Map<String, RecentContact> cached; // 暂缓刷上列表的数据（未读数红点拖拽动画运行时用）

    private RecentContactAdapter adapter;

    private boolean msgLoaded = false;

    private RecentContactsCallback callback;

    private UserInfoObserver userInfoObserver;
    private int tag = 0;

    public static RecentContactsFragment newInstance(String param1, String sId) {
        MerchantStaticInfo.sId = sId;
        RecentContactsFragment fragment = new RecentContactsFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        msgLoaded = false;
        if (isAdded()) {
            findViews();
            initMessageList();
            requestMessages(true);
            registerObservers(true);
            registerDropCompletedListener(true);
            registerOnlineStateChangeListener(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nim_recent_contacts, container, false);
    }

    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        boolean empty = items.isEmpty() && msgLoaded;
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
        emptyHint.setHint("还没有会话，在通讯录中找个人聊聊吧！");
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * 更新用户备注名
         */
        if (1 == tag) {
            msgLoaded = false;
            requestMessages(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerDropCompletedListener(false);
        registerOnlineStateChangeListener(false);
    }

    /**
     * 查找页面控件
     */
    private void findViews() {
        recyclerView = findView(R.id.recycler_view);
        emptyBg = findView(R.id.emptyBg);
        emptyHint = findView(R.id.message_list_empty_hint);
    }

    /**
     * 初始化消息列表
     */
    private void initMessageList() {
        items = new ArrayList<>();
        cached = new HashMap<>(3);

        // adapter
        adapter = new RecentContactAdapter(recyclerView, items);
        initCallBack();
        adapter.setCallback(callback);

        // recyclerView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(touchListener);

        // ios style
        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        // drop listener
        DropManager.getInstance().setDropListener(new DropManager.IDropListener() {
            @Override
            public void onDropBegin() {
                touchListener.setShouldDetectGesture(false);
            }

            @Override
            public void onDropEnd() {
                touchListener.setShouldDetectGesture(true);
            }
        });
    }

    private void initCallBack() {
        if (callback != null) {
            return;
        }
        callback = new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {

            }

            @Override
            public void onUnreadCountChange(int unreadCount) {

            }

            @Override
            public void onItemClick(RecentContact recent) {
                if (recent.getSessionType() == SessionTypeEnum.Team) {
                    NimUIKit.startTeamSession(getActivity(), recent.getContactId());
                } else if (recent.getSessionType() == SessionTypeEnum.P2P) {
                    NimUIKit.startP2PSession(getActivity(), recent.getContactId());
                }
            }

            @Override
            public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                return null;
            }
        };
    }

    private SimpleClickListener<RecentContactAdapter> touchListener = new SimpleClickListener<RecentContactAdapter>() {
        @Override
        public void onItemClick(RecentContactAdapter adapter, View view, int position) {
            if (callback != null) {
                RecentContact recent = adapter.getItem(position);
                callback.onItemClick(recent);
            }
        }

        @Override
        public void onItemLongClick(RecentContactAdapter adapter, View view, int position) {
            showLongClickMenu(adapter.getItem(position), position);
        }

        @Override
        public void onItemChildClick(RecentContactAdapter adapter, View view, int position) {

        }

        @Override
        public void onItemChildLongClick(RecentContactAdapter adapter, View view, int position) {

        }
    };

    OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            notifyDataSetChanged();
        }
    };

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
    }

    private void showLongClickMenu(final RecentContact recent, final int position) {
        CustomAlertDialog alertDialog = new CustomAlertDialog(getActivity());
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForLayoutPosition(position);
        TextView tvName = viewHolder.itemView.findViewById(R.id.tv_nickname);
        alertDialog.setTitle(tvName.getText().toString());
//        alertDialog.setTitle(UserInfoHelper.getUserTitleName(recent.getContactId(), recent.getSessionType()));
        String title = getString(R.string.main_msg_list_delete_chatting);
        alertDialog.addItem(title, new onSeparateItemClickListener() {
            @Override
            public void onClick() {
                // 删除会话，删除后，消息历史被一起删除
                NIMClient.getService(MsgService.class).deleteRecentContact(recent);
                NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), recent.getSessionType());
                adapter.remove(position);

                postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        refreshMessages(true);
                    }
                });
            }
        });

//        title = (isTagSet(recent, RECENT_TAG_STICKY) ? getString(R.string.main_msg_list_clear_sticky_on_top) : getString(R.string.main_msg_list_sticky_on_top));
//        alertDialog.addItem(title, new onSeparateItemClickListener() {
//            @Override
//            public void onClick() {
//                if (isTagSet(recent, RECENT_TAG_STICKY)) {
//                    removeTag(recent, RECENT_TAG_STICKY);
//                } else {
//                    addTag(recent, RECENT_TAG_STICKY);
//                }
//                NIMClient.getService(MsgService.class).updateRecent(recent);
//
//                refreshMessages(false);
//            }
//        });
        alertDialog.addItem("修改备注名", new onSeparateItemClickListener() {
            @Override
            public void onClick() {
                showDialog(recent.getExtension().get("uId").toString(), position);
//                getUserInfoToServer(recent.getContactId(), 0);
            }
        });
        alertDialog.show();
    }

    private void showDialog(String toUid, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_set_memo_name, null);
        final Dialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.Theme_Transparent).
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
                //申请接单
                setUserMemoName(alertDialog, Constants.UID, toUid, memoName, position);
            }
        });
    }

    /**
     * 为用户设置备注名
     */
    private void setUserMemoName(final Dialog dialog, String fromUid, String toUid, String memoName, int position) {
        if (Utils.isNetworkAvailable(getActivity())) {
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

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Utils.LogJson(response);
                            SetUserMemoName setUserMemoName = new SetUserMemoName();
                            setUserMemoName = Utils.parserJsonResult(response, SetUserMemoName.class);
                            if (Constants.OK.equals(setUserMemoName.getFlag())) {
                                if (Constants.OK.equals(setUserMemoName.getData().getStatus())) {
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(getActivity(), "修改备注名成功！", Toast.LENGTH_SHORT).show();
                                    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForLayoutPosition(position);
                                    TextView tvName = viewHolder.itemView.findViewById(R.id.tv_nickname);
                                    tvName.setText(memoName);
                                } else {
                                    Toast.makeText(getActivity(),
                                            setUserMemoName.getData().getErrorString(), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getActivity(),
                                        setUserMemoName.getErrorString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getActivity(), Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取某用户为别人设置的备注名
     */
    private void getUserMemoName(List<RecentContact> loadedRecents) {
        if (Utils.isNetworkAvailable(getActivity())) {
            OkHttpUtils
                    .post()
                    .url(Constants.getUserMemoName)
                    .addParams("uId", Constants.UID)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Utils.LogJson(response);
                            GetUserMemoName getUserMemoName = new GetUserMemoName();
                            getUserMemoName = Utils.parserJsonResult(response,
                                    GetUserMemoName.class);
                            if (Constants.OK.equals(getUserMemoName.getFlag())) {
                                for (int l = 0; l < loadedRecents.size(); l++) {
                                    for (int i = 0; i < getUserMemoName.getData().size(); i++) {
                                        String uId = (String) loadedRecents.get(l).getExtension().get("uId");
                                        if (getUserMemoName.getData().get(i).getUmnToId().equals(uId)) {
                                            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForLayoutPosition(l);
                                            TextView tvName = viewHolder.itemView.findViewById(R.id.tv_nickname);
                                            tvName.setText(getUserMemoName.getData().get(i).getUmnName());
                                        }
                                    }
                                }
                                tag = 1;
                            }
                        }
                    });
        }
    }

    private void addTag(RecentContact recent, long tag) {
        tag = recent.getTag() | tag;
        recent.setTag(tag);
    }

    private void removeTag(RecentContact recent, long tag) {
        tag = recent.getTag() & ~tag;
        recent.setTag(tag);
    }

    private boolean isTagSet(RecentContact recent, long tag) {
        return (recent.getTag() & tag) == tag;
    }

    private List<RecentContact> loadedRecents;

    private void requestMessages(boolean delay) {
        if (msgLoaded) {
            return;
        }
        getHandler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (msgLoaded) {
                    return;
                }
                // 查询最近联系人列表数据
                NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallbackWrapper<List<RecentContact>>() {

                    @Override
                    public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                        if (code != ResponseCode.RES_SUCCESS || recents == null) {
                            return;
                        }
                        loadedRecents = recents;
                        /**
                         * 遍历列表获取用户信息
                         */
                        for (int i = 0; i < loadedRecents.size(); i++) {
                            /**
                             * 从本地数据库获取用户信息
                             */
                            NimUserInfo user = NIMClient.getService(UserService.class).getUserInfo(loadedRecents.get(i).getContactId());
                            if (user != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject(user.getExtension());
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("uId", jsonObject.getString("uId"));
                                    loadedRecents.get(i).setExtension(map);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                getUserInfoToServer(loadedRecents.get(i).getContactId(), i);
                            }
                        }
                        /**
                         * 检查是否有备注名  如果有替换
                         */
                        getUserMemoName(loadedRecents);
                        // 初次加载，更新离线的消息中是否有@我的消息
                        for (RecentContact loadedRecent : loadedRecents) {
                            if (loadedRecent.getSessionType() == SessionTypeEnum.Team) {
                                updateOfflineContactAited(loadedRecent);
                            }
                        }
                        // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                        //
                        msgLoaded = true;
                        if (isAdded()) {
                            onRecentContactsLoaded();
                        }
                    }
                });
            }
        }, delay ? 250 : 0);
    }

    /**
     * 从服务器获取用户信息
     *
     * @param account
     * @param position
     */
    private void getUserInfoToServer(String account, int position) {
        List<String> list = new ArrayList<>();
        list.add(account);
        NIMClient.getService(UserService.class).fetchUserInfo(list)
                .setCallback(new RequestCallback<List<NimUserInfo>>() {
                    @Override
                    public void onSuccess(List<NimUserInfo> nimUserInfos) {
                        try {
                            JSONObject jsonObject = new JSONObject(nimUserInfos.get(0).getExtension());
                            Map<String, Object> map = new HashMap<>();
                            map.put("uId", jsonObject.getString("uId"));
                            loadedRecents.get(position).setExtension(map);
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

    private void onRecentContactsLoaded() {
        items.clear();
        if (loadedRecents != null) {
            items.addAll(loadedRecents);
            loadedRecents = null;
        }
        refreshMessages(true);

        if (callback != null) {
            callback.onRecentContactsLoaded();
        }
    }

    private void refreshMessages(boolean unreadChanged) {
        sortRecentContacts(items);
        notifyDataSetChanged();

        if (unreadChanged) {

            // 方式一：累加每个最近联系人的未读（快）

            int unreadNum = 0;
            for (RecentContact r : items) {
                unreadNum += r.getUnreadCount();
            }

            // 方式二：直接从SDK读取（相对慢）
            //int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();

            if (callback != null) {
                callback.onUnreadCountChange(unreadNum);
            }

            Badger.updateBadgerCount(unreadNum);
        }
    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortRecentContacts(List<RecentContact> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<RecentContact> comp = new Comparator<RecentContact>() {

        @Override
        public int compare(RecentContact o1, RecentContact o2) {
            // 先比较置顶tag
            long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
            if (sticky != 0) {
                return sticky > 0 ? -1 : 1;
            } else {
                long time = o1.getTime() - o2.getTime();
                return time == 0 ? 0 : (time > 0 ? -1 : 1);
            }
        }
    };

    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(messageReceiverObserver, register);
        service.observeRecentContact(messageObserver, register);
        service.observeMsgStatus(statusObserver, register);
        service.observeRecentContactDeleted(deleteObserver, register);

        registerTeamUpdateObserver(register);
        registerTeamMemberUpdateObserver(register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
    }

    /**
     * 注册群信息&群成员更新监听
     */
    private void registerTeamUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
    }

    private void registerTeamMemberUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver, register);
    }

    private void registerDropCompletedListener(boolean register) {
        if (register) {
            DropManager.getInstance().addDropCompletedListener(dropCompletedListener);
        } else {
            DropManager.getInstance().removeDropCompletedListener(dropCompletedListener);
        }
    }

    // 暂存消息，当RecentContact 监听回来时使用，结束后清掉
    private Map<String, Set<IMMessage>> cacheMessages = new HashMap<>();

    //监听在线消息中是否有@我
    private Observer<List<IMMessage>> messageReceiverObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> imMessages) {
            if (imMessages != null) {
                for (IMMessage imMessage : imMessages) {
                    if (!TeamMemberAitHelper.isAitMessage(imMessage)) {
                        continue;
                    }
                    Set<IMMessage> cacheMessageSet = cacheMessages.get(imMessage.getSessionId());
                    if (cacheMessageSet == null) {
                        cacheMessageSet = new HashSet<>();
                        cacheMessages.put(imMessage.getSessionId(), cacheMessageSet);
                    }
                    cacheMessageSet.add(imMessage);
                }
            }
        }
    };

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            if (!DropManager.getInstance().isTouchable()) {
                // 正在拖拽红点，缓存数据
                for (RecentContact r : recentContacts) {
                    cached.put(r.getContactId(), r);
                }

                return;
            }

            onRecentContactChanged(recentContacts);
        }
    };

    private void onRecentContactChanged(List<RecentContact> recentContacts) {
        int index;
        for (RecentContact r : recentContacts) {
            index = -1;
            for (int i = 0; i < items.size(); i++) {
                if (r.getContactId().equals(items.get(i).getContactId())
                        && r.getSessionType() == (items.get(i).getSessionType())) {
                    index = i;
                    break;
                }
            }

            if (index >= 0) {
                items.remove(index);
            }

            items.add(r);
            if (r.getSessionType() == SessionTypeEnum.Team && cacheMessages.get(r.getContactId()) != null) {
                TeamMemberAitHelper.setRecentContactAited(r, cacheMessages.get(r.getContactId()));
            }
        }

        cacheMessages.clear();

        refreshMessages(true);
    }

    DropCover.IDropCompletedListener dropCompletedListener = new DropCover.IDropCompletedListener() {
        @Override
        public void onCompleted(Object id, boolean explosive) {
            if (cached != null && !cached.isEmpty()) {
                // 红点爆裂，已经要清除未读，不需要再刷cached
                if (explosive) {
                    if (id instanceof RecentContact) {
                        RecentContact r = (RecentContact) id;
                        cached.remove(r.getContactId());
                    } else if (id instanceof String && ((String) id).contentEquals("0")) {
                        cached.clear();
                    }
                }

                // 刷cached
                if (!cached.isEmpty()) {
                    List<RecentContact> recentContacts = new ArrayList<>(cached.size());
                    recentContacts.addAll(cached.values());
                    cached.clear();

                    onRecentContactChanged(recentContacts);
                }
            }
        }
    };

    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                RecentContact item = items.get(index);
                item.setMsgStatus(message.getStatus());
                refreshViewHolderByIndex(index);
            }
        }
    };

    Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {
        @Override
        public void onEvent(RecentContact recentContact) {
            if (recentContact != null) {
                for (RecentContact item : items) {
                    if (TextUtils.equals(item.getContactId(), recentContact.getContactId())
                            && item.getSessionType() == recentContact.getSessionType()) {
                        items.remove(item);
                        refreshMessages(true);
                        break;
                    }
                }
            } else {
                items.clear();
                refreshMessages(true);
            }
        }
    };

    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {

        @Override
        public void onUpdateTeams(List<Team> teams) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeam(Team team) {

        }
    };

    TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {
        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> member) {

        }
    };

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            RecentContact item = items.get(i);
            if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
                return i;
            }
        }

        return -1;
    }

    protected void refreshViewHolderByIndex(final int index) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyItemChanged(index);
            }
        });
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }

    private void registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver = new UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    refreshMessages(false);
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, true);
    }

    private void unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, false);
        }
    }

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            refreshMessages(false);
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            refreshMessages(false);
        }
    };

    private void updateOfflineContactAited(final RecentContact recentContact) {
        if (recentContact == null || recentContact.getSessionType() != SessionTypeEnum.Team
                || recentContact.getUnreadCount() <= 0) {
            return;
        }

        // 锚点
        List<String> uuid = new ArrayList<>(1);
        uuid.add(recentContact.getRecentMessageId());

        List<IMMessage> messages = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuid);

        if (messages == null || messages.size() < 1) {
            return;
        }
        final IMMessage anchor = messages.get(0);

        // 查未读消息
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD,
                recentContact.getUnreadCount() - 1, false).setCallback(new RequestCallbackWrapper<List<IMMessage>>() {

            @Override
            public void onResult(int code, List<IMMessage> result, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && result != null) {
                    result.add(0, anchor);
                    Set<IMMessage> messages = null;
                    // 过滤存在的@我的消息
                    for (IMMessage msg : result) {
                        if (TeamMemberAitHelper.isAitMessage(msg)) {
                            if (messages == null) {
                                messages = new HashSet<>();
                            }
                            messages.add(msg);
                        }
                    }

                    // 更新并展示
                    if (messages != null) {
                        TeamMemberAitHelper.setRecentContactAited(recentContact, messages);
                        notifyDataSetChanged();
                    }
                }
            }
        });

    }
}
