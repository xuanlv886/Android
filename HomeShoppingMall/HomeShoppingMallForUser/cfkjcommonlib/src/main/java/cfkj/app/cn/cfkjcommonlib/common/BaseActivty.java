package cfkj.app.cn.cfkjcommonlib.common;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cfkj.app.cn.cfkjcommonlib.R;
import cfkj.app.cn.cfkjcommonlib.view.ReplaceViewHelper.VaryViewHelperController;
import cfkj.app.cn.cfkjcommonlib.view.StatusBar.StatusBarUtil;

/**
 * Description Activity基类
 * Data 2018/4/21-14:03
 * Content
 *
 * @author lzy
 */

public class BaseActivty extends AppCompatActivity {

    public static final String[] actions = {"有数据", "暂无数据", "加载中",
            "数据获取失败", "网络链接异常", "网络链接不稳定"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setStatusBar();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        ActivityCollector.addActivity(this);
    }

    /**
     * 设置沉浸式状态栏
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this,
                getResources().getColor(R.color.transparent), 1);
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

    View.OnClickListener refreshListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            clickEvent();
        }
    };

    public void clickEvent() {

    }

    private View getView(String action, int color) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_varyview, null);
        TextView tvAction = view.findViewById(R.id.action);
        tvAction.setText(action);
        tvAction.setTextColor(color);
        return view;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public void closeAll() {
        ActivityCollector.finishAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    /**
     * 封装长Toast
     *
     * @param msg
     */
    public void showLongToast(Context context, String msg) {
        CustomToast.showToast(context, msg, Toast.LENGTH_LONG);
    }

    /**
     * 封装短Toast
     *
     * @param msg
     */
    public void showShortToast(Context context, String msg) {
        CustomToast.showToast(context, msg, Toast.LENGTH_SHORT);
    }


}
