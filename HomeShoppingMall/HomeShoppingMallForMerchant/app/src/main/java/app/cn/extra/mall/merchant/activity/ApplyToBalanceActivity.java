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
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.GetAlreadyCash;
import app.cn.extra.mall.merchant.vo.GetPayStyle;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * 申请提现
 */
public class ApplyToBalanceActivity extends BaseActivty {

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
    @BindView(R.id.et_card_num1)
    EditText etCardNum1;
    @BindView(R.id.et_card_num2)
    EditText etCardNum2;

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
        setContentView(R.layout.activity_apply_to_balance);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(ApplyToBalanceActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        initRecyclerView();
        getPayStylePort();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 设置RecyclerView管理器
         */
        recyclerView.setLayoutManager(new LinearLayoutManager(ApplyToBalanceActivity.this,
                LinearLayoutManager.VERTICAL, false));
        /**
         * 添加分隔线
         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(ApplyToBalanceActivity.this,
//                DividerItemDecoration.VERTICAL));
    }

    /**
     * 获取支付方式接口
     */
    private void getPayStylePort() {
        if (Utils.isNetworkAvailable(ApplyToBalanceActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getPayStyle)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(ApplyToBalanceActivity.this, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            payStyles = new GetPayStyle();
                            payStyles = Utils.parserJsonResult(response, GetPayStyle.class);
                            if (Constants.OK.equals(payStyles.getFlag())) {
                                if (0 < payStyles.getData().getPayStyleList().size()) {
                                    for (int i = 0; i < payStyles.getData().getPayStyleList().size(); i++) {
                                        if (payStyles.getData().getPayStyleList().get(i).getPsName().contains("微信")) {
                                            payStyles.getData().getPayStyleList().remove(i);
                                        }
                                    }
                                    setPayStyleData(payStyles.getData().getPayStyleList());
                                }
                            } else {
                                showShortToast(ApplyToBalanceActivity.this,
                                        payStyles.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(ApplyToBalanceActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 填充支付方式数据
     */
    private void setPayStyleData(List<GetPayStyle.DataBean.PayStyleListBean> payStyleList) {
        adapter = new CommonAdapter<GetPayStyle.DataBean.PayStyleListBean>(
                ApplyToBalanceActivity.this, R.layout.list_item_pay_style, payStyleList) {
            @Override
            protected void convert(ViewHolder holder,
                                   GetPayStyle.DataBean.PayStyleListBean
                                           payStyleBean, int position) {
//                RequestOptions requestOptions = new RequestOptions()
//                        .error(R.drawable.ic_exception)
//                        .fallback(R.drawable.ic_exception);
//                Glide.with(ApplyToBalanceActivity.this).load(
//                        payStyleBean.getUrl())
//                        .apply(requestOptions)
//                        .into((ImageView) holder.getView(R.id.img_item_pay_style));
                if ("支付宝支付".equals(payStyleBean.getPsName())) {
                    holder.setText(R.id.tv_item_pay_style_name, "支付宝收款");
                } else {
                    holder.setText(R.id.tv_item_pay_style_name, payStyleBean.getPsName());
                }
                holder.setImageResource(R.id.img_item_pay_style, R.mipmap.ali_logo);
                if (index == position) {
                    psId = payStyleBean.getPsId();
                    holder.setImageResource(R.id.img_item_pay_style_select, R.mipmap.selected);
                } else {
                    holder.setImageResource(R.id.img_item_pay_style_select, R.mipmap.unselect);
                }
            }
        };
        /**
         * 支付方式点击事件
         */
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                index = position;
                adapter.notifyDataSetChanged();
                Utils.LogE(index + "");
//                if (index == 0) {
//                    etCardNum1.setHint("请输入您的微信收款账号");
//                    etCardNum2.setHint("请再次输入您的微信收款账号以确认");
//                } else if (index == 1) {
//                    etCardNum1.setHint("请输入您的支付宝收款账号");
//                    etCardNum2.setHint("请再次输入您的支付宝收款账号以确认");
//                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.img_back, R.id.btn_submit_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_submit_apply:
//             提现
                doApply();
                break;
            default:
        }
    }

    /**
     * 点击申请提现按钮执行的操作
     */
    private void doApply() {
        /**
         * 校验输入的金额
         */
        if (TextUtils.isEmpty(etMoney.getText().toString().trim())) {
            showShortToast(ApplyToBalanceActivity.this, getResources().getString(
                    R.string.applyToBalanceMoneyEmpty));
            return;
        }
        if (etMoney.getText().toString().trim().startsWith(".")
                || etMoney.getText().toString().trim().endsWith(".")) {
            showShortToast(ApplyToBalanceActivity.this, getResources().getString(
                    R.string.paySafeguardsErr));
            return;
        }
        /**
         * 校验卡号
         */
        if (TextUtils.isEmpty(etCardNum1.getText().toString().trim()) || TextUtils.isEmpty(etCardNum1.getText().toString().trim())) {
            showShortToast(ApplyToBalanceActivity.this, getResources().getString(
                    R.string.apply_to_balance_et1));
            return;
        }
        if (!etCardNum1.getText().toString().trim().equals(etCardNum2.getText().toString().trim())) {
            showShortToast(ApplyToBalanceActivity.this, getResources().getString(
                    R.string.apply_to_balance_error));
            return;
        }
        String utcrMoney = etMoney.getText().toString().trim();
        String utcrAccount = etCardNum1.getText().toString().trim();
        getAlreadyCash(psId, utcrMoney, utcrAccount);
    }

    /**
     * 申请提现接口
     */
    private void getAlreadyCash(String psId, String utcrMoney, String utcrAccount) {
        if (Utils.isNetworkAvailable(ApplyToBalanceActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getAlreadyCash)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
                    .addParams("psId", psId)
                    .addParams("utcrMoney", utcrMoney)
                    .addParams("utcrAccount", utcrAccount)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            showShortToast(ApplyToBalanceActivity.this, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            GetAlreadyCash getAlreadyCash = new GetAlreadyCash();
                            getAlreadyCash = Utils.parserJsonResult(response, GetAlreadyCash.class);
                            if (Constants.OK.equals(getAlreadyCash.getFlag())) {
                                if (getAlreadyCash.getData().isStatus()) {
                                    showShortToast(ApplyToBalanceActivity.this, "申请成功");
                                    finish();
                                } else {
                                    showShortToast(ApplyToBalanceActivity.this, getAlreadyCash.getErrorString());
                                }
                            } else {
                                showShortToast(ApplyToBalanceActivity.this,
                                        getAlreadyCash.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(ApplyToBalanceActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }
}
