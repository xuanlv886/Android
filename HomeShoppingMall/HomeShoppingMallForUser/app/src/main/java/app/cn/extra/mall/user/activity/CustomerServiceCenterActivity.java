package app.cn.extra.mall.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import app.cn.extra.mall.user.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;

/**
 * 客服中心
 */
public class CustomerServiceCenterActivity extends BaseActivty {
    @BindView(R.id.tv_qq)
    TextView tvQQ;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_center);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
    }
}
