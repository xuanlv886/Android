package app.cn.extra.mall.merchant.activity;

import android.os.Bundle;
import android.widget.ImageView;

import app.cn.extra.mall.merchant.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;

/**
 * Description 客服中心页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class CustomerServiceCenterActivity extends BaseActivty {

    @BindView(R.id.img_back)
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_center);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }
}
