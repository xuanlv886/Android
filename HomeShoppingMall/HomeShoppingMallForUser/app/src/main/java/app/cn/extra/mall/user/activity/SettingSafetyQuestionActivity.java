package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.SettingSafetyQuestionEvent;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;

/**
 * Description 设置安全问题页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class SettingSafetyQuestionActivity extends BaseActivty {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.btn_set)
    Button btnSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_safety_question);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 消息处理
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SettingSafetyQuestionEventBus(SettingSafetyQuestionEvent event) {
        finish();
    }

    @OnClick({R.id.img_back, R.id.btn_set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_set:
                gotoSettingSafetyQuestionDetailActivity();
                break;
            default:
        }
    }

    /**
     * 跳转至设置安全问题详情页
     */
    private void gotoSettingSafetyQuestionDetailActivity() {
        Intent intent = new Intent();
        intent.setClass(SettingSafetyQuestionActivity.this,
                SettingSafetyQuestionDetailActivity.class);
        startActivity(intent);
        finish();
    }
}
