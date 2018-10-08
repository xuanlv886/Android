package app.cn.extra.mall.merchant.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.cn.extra.mall.merchant.R;

/**
 * Description 消息页Fragment
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class MsgFragment extends Fragment {


    public static MsgFragment newInstance(String param1) {
        MsgFragment fragment = new MsgFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_msg, container, false);
    }

}
