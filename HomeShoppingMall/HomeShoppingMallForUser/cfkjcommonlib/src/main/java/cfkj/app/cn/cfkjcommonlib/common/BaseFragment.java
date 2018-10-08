package cfkj.app.cn.cfkjcommonlib.common;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cfkj.app.cn.cfkjcommonlib.R;
import cfkj.app.cn.cfkjcommonlib.view.ReplaceViewHelper.VaryViewHelperController;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {


    public static final String[] actions = {"有数据", "暂无数据", "加载中",
            "数据获取失败", "网络链接异常", "网络链接不稳定"};

    public BaseFragment() {
        // Required empty public constructor
    }

    public VaryViewHelperController createCaseViewHelperController(View view) {
        return new VaryViewHelperController(view)
                .setUpEmptyView(getView(actions[1], Color.parseColor("#ff33b5e5")))
                .setUpLoadingView(getView(actions[2], Color.parseColor("#ffff4444")))
                .setUpErrorView(getView(actions[3], Color.parseColor("#ffffbb33")))
                .setUpNetworkErrorView(getView(actions[4], Color.parseColor("#ff99cc00")))
                .setUpNetworkPoorView(getView(actions[5], Color.parseColor("#ffff4444")))
                .setUpRefreshListener(refreshListener);
    }

    View.OnClickListener refreshListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            clickEvent();
        }
    };

    public void clickEvent() {

    }

    private View getView(String action, int color) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_varyview, null);
        TextView tvAction = view.findViewById(R.id.action);
        tvAction.setText(action);
        tvAction.setTextColor(color);
        return view;
    }

    /**
     * 封装长Toast
     * @param msg
     */
    public void showLongToast(Context context, String msg) {
        CustomToast.showToast(context, msg, Toast.LENGTH_LONG);
    }
    /**
     * 封装短Toast
     * @param msg
     */
    public void showShortToast(Context context,String msg) {
        CustomToast.showToast(context, msg, Toast.LENGTH_SHORT);
    }


}
