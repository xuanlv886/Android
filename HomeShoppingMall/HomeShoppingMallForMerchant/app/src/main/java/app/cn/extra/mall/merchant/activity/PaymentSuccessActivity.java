package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.CommodityBuyEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;

/**
 * 支付成功页面
 */
public class PaymentSuccessActivity extends BaseActivty {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv)
    TextView tv;
    @BindView(R.id.btn_ok)
    Button btnOk;
    private String tags;
    SharePreferenceUtil sharePreferenceUtil = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        tags = intent.getStringExtra("tags");
        initView();
        initData();
    }

    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(this, Constants.SAVE_USER);
        img.setVisibility(View.VISIBLE);
        tv.setVisibility(View.VISIBLE);
        btnOk.setVisibility(View.VISIBLE);
        clearTempOrderInfo();
    }

    private void initData() {

    }

    /**
     * 清理SharePreference中临时存储的订单相关信息
     */
    private void clearTempOrderInfo() {
        sharePreferenceUtil.setTotalMoney("");
        sharePreferenceUtil.setPayCode("");
    }

    @OnClick({R.id.img_back, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                EventBus.getDefault().post(new CommodityBuyEvent());
                finish();
                break;
            case R.id.btn_ok:
                EventBus.getDefault().post(new CommodityBuyEvent());
                finish();
                break;
            default:
        }
    }
}
