package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.ChangeSafetyQuestionEvent;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;

/**
 * Description 修改安全问题页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class ChangeSafetyQuestionActivity extends BaseActivty {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.btn_change)
    Button btnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_safety_question);
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
    public void ChangeSafetyQuestionEventBus(ChangeSafetyQuestionEvent event) {
        finish();
    }
    @OnClick({R.id.img_back, R.id.btn_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_change:
                gotoChangeSafetyQuestionDetailActivity();
                break;
                default:
        }
    }

    /**
     * 跳转至修改密保问题详情界面
     */
    private void gotoChangeSafetyQuestionDetailActivity() {
        Intent intent = new Intent();
        intent.setClass(ChangeSafetyQuestionActivity.this,
                ChangeSafetyQuestionDetailActivity.class);
        startActivity(intent);
        finish();
    }
}
