package app.cn.extra.mall.user.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import app.cn.extra.mall.user.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;

public class RevisePendingOrderActivity extends BaseActivty {
    @BindView(R.id.et_demand_type)
    EditText etDemandType;
    @BindView(R.id.et_demand_title)
    EditText etDemandTitle;
    @BindView(R.id.et_demend_content)
    EditText etDemendContent;
    @BindView(R.id.et_send_time)
    EditText etSendTime;
    @BindView(R.id.et_demand_money)
    EditText etDemandMoney;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.et_user_address)
    EditText etUserAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_pending_order);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

    }

    @OnClick({R.id.img_back, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_sure:
                break;
            default:
                break;
        }
    }
}
