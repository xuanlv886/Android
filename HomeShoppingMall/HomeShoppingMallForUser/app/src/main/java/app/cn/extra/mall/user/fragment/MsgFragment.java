package app.cn.extra.mall.user.fragment;


import android.os.Bundle;

import com.netease.nim.uikit.business.recent.RecentContactsFragment;
import com.netease.nim.uikit.common.activity.UI;

import app.cn.extra.mall.user.R;
import cfkj.app.cn.cfkjcommonlib.common.BaseFragment;

/**
 * Description 消息Fragment
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class MsgFragment extends BaseFragment {
    private RecentContactsFragment fragment;

    public static MsgFragment newInstance(String param1) {
        MsgFragment fragment = new MsgFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addRecentContactsFragment();
    }

    // 将最近联系人列表fragment动态集成进来。
    private void addRecentContactsFragment() {
        fragment = new RecentContactsFragment();
        // 设置要集成联系人列表fragment的布局文件
        fragment.setContainerId(R.id.messages_list_layout);

        final UI activity = (UI) getActivity();

        // 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
        fragment = (RecentContactsFragment) activity.addFragment(fragment);
    }
}
