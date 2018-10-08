package app.cn.extra.mall.merchant.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.Wave;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.zhy.adapter.recyclerview.CommonAdapter;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.GetPayStyle;
import app.cn.extra.mall.merchant.vo.GetThawDepositMoney;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 申请解冻保障金页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class ThawSafeguardsActivity extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_submit_apply)
    Button btnSubmit;

    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 临时存储支付订单号
     */
    private String payCode = "";
    private GetPayStyle payStyles = null;
    private CommonAdapter<GetPayStyle.DataBean.PayStyleListBean> adapter;
    /**
     * 选择的支付方式在列表中的位置 默认选中第一个
     */
    private int index = 0;
    private IWXAPI api;

    String psId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thaw_safeguards);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(ThawSafeguardsActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        initRecyclerView();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 设置RecyclerView管理器
         */
        recyclerView.setLayoutManager(new LinearLayoutManager(ThawSafeguardsActivity.this,
                LinearLayoutManager.VERTICAL, false));
        /**
         * 添加分隔线
         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(ThawSafeguardsActivity.this,
//                DividerItemDecoration.VERTICAL));
    }


    @OnClick({R.id.img_back, R.id.btn_submit_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_submit_apply:
//             申请解冻
                doApply();
                break;
            default:
        }
    }

    /**
     * 点击申请按钮执行的操作
     */
    private void doApply() {
        /**
         * 校验输入的金额
         */
        if (TextUtils.isEmpty(etMoney.getText().toString().trim())) {
            showShortToast(ThawSafeguardsActivity.this, getResources().getString(
                    R.string.thawSafeguardsEmpty));
            return;
        }
        if (etMoney.getText().toString().trim().startsWith(".")
                || etMoney.getText().toString().trim().endsWith(".")) {
            showShortToast(ThawSafeguardsActivity.this, getResources().getString(
                    R.string.paySafeguardsErr));
            return;
        }
        String depositMoney = etMoney.getText().toString().trim();
        getThawDepositMoney("", depositMoney);
    }

    /**
     * 申请解冻保障金接口
     */
    private void getThawDepositMoney(String psId, String depositMoney) {
        if (Utils.isNetworkAvailable(ThawSafeguardsActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getThawDepositMoney)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
                    .addParams("psId", psId)
                    .addParams("depositMoney", depositMoney)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(ThawSafeguardsActivity.this, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            GetThawDepositMoney getThawDepositMoney = new GetThawDepositMoney();
                            getThawDepositMoney = Utils.parserJsonResult(response, GetThawDepositMoney.class);
                            if (Constants.OK.equals(getThawDepositMoney.getFlag())) {
                                if(Constants.OK.equals(getThawDepositMoney.getData().getStatus())) {
                                    showShortToast(ThawSafeguardsActivity.this, "解冻成功！");
                                    finish();
                                }else {
                                    showShortToast(ThawSafeguardsActivity.this,
                                            getThawDepositMoney.getData().getErrorString());
                                }

                            } else {
                                showShortToast(ThawSafeguardsActivity.this,
                                        getThawDepositMoney.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(ThawSafeguardsActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }
}
