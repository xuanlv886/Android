package app.cn.extra.mall.merchant.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.activity.MyWalletActivity;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.CommonVo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;


public class WXPayEntryActivity extends BaseActivty implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
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

    private IWXAPI api;

    private SharePreferenceUtil sharePreferenceUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sharePreferenceUtil = new SharePreferenceUtil(WXPayEntryActivity.this,
                Constants.SAVE_USER);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        ButterKnife.bind(this);
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID_MERCHANT, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (0 == resp.errCode) {
                wxPayOrderQueryPort();
            } else {
                Utils.LogE(resp.errCode + "");
                clearTempOrderInfo();
                showShortToast(WXPayEntryActivity.this, getResources()
                        .getString(R.string.payCancle));
                gotoMyWalletActivity();
            }
        }
    }

    /**
     * 查询微信订单支付结果接口
     */
    private void wxPayOrderQueryPort() {
        if (Utils.isNetworkAvailable(WXPayEntryActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.wxPayOrderQuery)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("payCode", sharePreferenceUtil.getPayCode())
                    .addParams("totalMoney", sharePreferenceUtil.getTotalMoney())
                    /**
                     * orderType--订单类型0--商品订单，1--需求订单，2--缴纳保障金
                     */
                    .addParams("orderType", "2")
                    /**
                     * appType--平台类型0--用户端，1--商户端，2--WEB管理端
                     */
                    .addParams("appType", "1")
                    .addParams("orderData", "{\"sId\": " + "\"" + sharePreferenceUtil.getSID()
                            + "\"}")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(WXPayEntryActivity.this, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            CommonVo commonVo = new CommonVo();
                            commonVo = Utils.parserJsonResult(response, CommonVo.class);
                            if (Constants.OK.equals(commonVo.getFlag())) {
                                if (Constants.OK.equals(commonVo.getData().getStatus())) {
                                    img.setVisibility(View.VISIBLE);
                                    tv.setVisibility(View.VISIBLE);
                                    btnOk.setVisibility(View.VISIBLE);
                                    clearTempOrderInfo();
                                } else {
                                    showShortToast(WXPayEntryActivity.this,
                                            getResources().getString(R.string.payFailed));
                                    gotoMyWalletActivity();
                                }
                            } else {
                                showShortToast(WXPayEntryActivity.this,
                                        commonVo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(WXPayEntryActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    @OnClick({R.id.img_back, R.id.btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                gotoMyWalletActivity();
                break;
            case R.id.btn_ok:
                gotoMyWalletActivity();
                break;
            default:
                break;
        }
    }

    /**
     * 清理SharePreference中临时存储的订单相关信息
     */
    private void clearTempOrderInfo() {
        sharePreferenceUtil.setTotalMoney("");
        sharePreferenceUtil.setPayCode("");
    }

    /**
     * 跳转至钱包界面
     */
    private void gotoMyWalletActivity() {
        Intent intent = new Intent();
        intent.setClass(WXPayEntryActivity.this, MyWalletActivity.class);
        startActivity(intent);
        finish();
    }
}