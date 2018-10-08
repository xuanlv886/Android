package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.AllFragmentEvent;
import app.cn.extra.mall.merchant.event.PendingDeliveryFragmentEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.vo.AffirmProductRefund;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * 去发货
 */
public class DeliveryActivity extends BaseActivty {
    @BindView(R.id.et_company)
    EditText etCompany;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    String poId = "";
    String intentFlag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        poId = intent.getStringExtra("poId");
        intentFlag = intent.getStringExtra("intentFlag");
    }

    /**
     * 去发货
     */
    private void merchantSend(String poId, String poDeliverCompany, String poDeliverCode) {
        if (Utils.isNetworkAvailable(DeliveryActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.merchantSend)
                    .addParams("poId", poId)
                    .addParams("poDeliverCompany", poDeliverCompany)
                    .addParams("poDeliverCode", poDeliverCode)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 接口调用出错
                             */
                            showShortToast(DeliveryActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            AffirmProductRefund affirmProductRefund = new AffirmProductRefund();
                            affirmProductRefund = Utils.parserJsonResult(response, AffirmProductRefund.class);
                            if (Constants.OK.equals(affirmProductRefund.getFlag())) {
                                if (affirmProductRefund.getData().isStatus()) {
                                    showShortToast(DeliveryActivity.this,
                                            "发货成功");
                                    switch (intentFlag) {
                                        case "AllFragment":
                                            EventBus.getDefault().post(new AllFragmentEvent());
                                            break;
                                        case "PendingDeliveryFragment":
                                            EventBus.getDefault().post(new PendingDeliveryFragmentEvent());
                                            break;
                                        default:
                                            break;
                                    }
                                    finish();
                                } else {
                                    showShortToast(DeliveryActivity.this,
                                            affirmProductRefund.getData().getErrorString());
                                }
                            } else {
                                showShortToast(DeliveryActivity.this,
                                        affirmProductRefund.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(DeliveryActivity.this, Constants.NETWORK_ERROR);
        }
    }

    @OnClick({R.id.img_back, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_submit:
                String poDeliverCompany = etCompany.getText().toString().trim();
                String poDeliverCode = etNum.getText().toString().trim();
                if (TextUtils.isEmpty(poDeliverCompany)) {
                    showShortToast(DeliveryActivity.this,
                            "请填写快递公司");
                    break;
                }
                if (TextUtils.isEmpty(poDeliverCode)) {
                    showShortToast(DeliveryActivity.this,
                            "请填写快递单号");
                    break;
                }
                merchantSend(poId, poDeliverCompany, poDeliverCode);
                break;
            default:
                break;
        }
    }
}
