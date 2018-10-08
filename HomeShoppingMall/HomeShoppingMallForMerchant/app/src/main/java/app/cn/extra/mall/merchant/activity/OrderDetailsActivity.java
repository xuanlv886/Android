package app.cn.extra.mall.merchant.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nim.uikit.api.NimUIKit;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.AilTradeRefund;
import app.cn.extra.mall.merchant.vo.DelStoreProductOrder;
import app.cn.extra.mall.merchant.vo.GetProductOrderDetail;
import app.cn.extra.mall.merchant.vo.WxTradeRefund;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import cfkj.app.cn.cfkjcommonlib.view.ReplaceViewHelper.VaryViewHelperController;
import okhttp3.Call;

public class OrderDetailsActivity extends BaseActivty {
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    @BindView(R.id.tv_type_blue)
    TextView tvTypeBlue;
    @BindView(R.id.tv_all_money)
    TextView tvAllMoney;
    @BindView(R.id.rl_contact_seller)
    RelativeLayout rlContactSeller;
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.tv_payment_time)
    TextView tvPaymentTime;
    @BindView(R.id.tv_send_time)
    TextView tvSendTime;
    @BindView(R.id.tv_delivery_time)
    TextView tvDeliveryTime;
    @BindView(R.id.tv_deal_time)
    TextView tvDealTime;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;
    @BindView(R.id.tv_false)
    TextView tv_false;
    @BindView(R.id.tv_true)
    TextView tv_true;
    @BindView(R.id.rl_deliver)
    RelativeLayout rl_deliver;
    @BindView(R.id.tv_deliver_company)
    TextView tv_deliver_company;
    @BindView(R.id.tv_deliver_code)
    TextView tv_deliver_code;
    @BindView(R.id.img_message)
    ImageView imgMessage;
    private SharePreferenceUtil sharePreferenceUtil;
    private VaryViewHelperController varyViewHelperController;
    /**
     * 商品订单主键
     */
    String poId = "";
    String sName = "";
    String psId = "";
    /**
     * 用于存放临时数据
     */
    List<GetProductOrderDetail.DataBean.OrderDetailsBean> orderDetailsBeanList = null;
    private CommonAdapter<GetProductOrderDetail.DataBean.OrderDetailsBean> adapter;
    GetProductOrderDetail getProductOrderDetail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        init();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 线性布局
         */
        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManage);
//        /**
//         * 添加分隔线
//         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(OrderDetailsActivity.this,
//                DividerItemDecoration.VERTICAL));
    }

    private void init() {
        tv_true.setBackgroundColor(getResources().getColor(R.color.blue));
        initRecyclerView();
        sharePreferenceUtil = new SharePreferenceUtil(OrderDetailsActivity.this,
                Constants.SAVE_USER);
//        /**
//         * 设置异常情况界面
//         */
//        varyViewHelperController = createCaseViewHelperController(refreshLayout);
//        varyViewHelperController.setUpRefreshViews(varyViewHelperController.getErrorView(),
//                varyViewHelperController.getNetworkPoorView());
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        Intent intent = getIntent();
        poId = intent.getStringExtra("poId");
        sName = intent.getStringExtra("sName");
        psId = intent.getStringExtra("psId");
        getProductOrderDetail();
    }

    /**
     * 获取页面数据接口
     * p--当前页
     * ps--页大小
     */
    private void getProductOrderDetail() {
        if (Utils.isNetworkAvailable(OrderDetailsActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getStoreProductOrderDetail)
                    .addParams("poId", poId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
//                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
//                            /**
//                             * 恢复显示数据的View
//                             */
//                            varyViewHelperController.restore();
                            System.out.println(response);
                            getProductOrderDetail = new GetProductOrderDetail();
                            getProductOrderDetail = Utils.parserJsonResult(response,
                                    GetProductOrderDetail.class);
                            if (Constants.OK.equals(getProductOrderDetail.getFlag())) {
                                orderDetailsBeanList = getProductOrderDetail.getData().getOrderDetails();
                                setData(getProductOrderDetail);
                            } else {
//                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
//            varyViewHelperController.showNetworkPoorView();
        }
    }

    /**
     * 填充页面数据
     *
     * @param getProductOrderDetail 数据实体
     */
    private void setData(GetProductOrderDetail getProductOrderDetail) {
        switch ("" + getProductOrderDetail.getData().getPoStatus()) {
            case Constants.STATUS_ALL:
                break;
            case Constants.STATUS_PENDINGPAYMENT:
                tvType.setText("待付款");
                tvTypeBlue.setText("待付款");
                tv_true.setVisibility(View.VISIBLE);
                tv_true.setText("取消订单");
                tv_false.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.VISIBLE);
                break;
            case Constants.STATUS_PENDINGDELIVERY:
                tvType.setText("待发货");
                tvTypeBlue.setText("待发货");
                tv_true.setVisibility(View.VISIBLE);
                tv_true.setText("去发货");
                tv_false.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.VISIBLE);
                break;
            case Constants.STATUS_PENDINGCOLLECTGOODS:
                tvType.setText("待收货");
                tvTypeBlue.setText("待收货");
                tv_true.setVisibility(View.GONE);
                tv_false.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.GONE);
                break;
            case Constants.STATUS_PENDINGEVALUATE:
                tvType.setText("待评价");
                tvTypeBlue.setText("待评价");
                tv_true.setVisibility(View.GONE);
                tv_false.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.GONE);
                break;
            case Constants.STATUS_COMPLETEORDER:
                tvType.setText("已完成");
                tvTypeBlue.setText("已完成");
                tv_true.setVisibility(View.GONE);
                tv_false.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.GONE);
                break;
            case Constants.STATUS_UNREFUNdORDER:
                tvType.setText("待退款");
                tvTypeBlue.setText("待退款");
                tv_true.setVisibility(View.VISIBLE);
                tv_true.setText("确认退款");
                tv_false.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.VISIBLE);
                break;
            case Constants.STATUS_ALREADYREFUNDORDER:
                tvType.setText("已退款");
                tvTypeBlue.setText("已退款");
                tv_true.setVisibility(View.GONE);
                tv_false.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        tvName.setText("收货人：" + getProductOrderDetail.getData().getPoDeliverName());
        tvPhone.setText(getProductOrderDetail.getData().getPoDeliverTel());
        tvAddress.setText(getProductOrderDetail.getData().getPoDeliverAddress());
        tvShopName.setText(sName);
        tvOrderNum.setText("订单编号：" + getProductOrderDetail.getData().getPoOrderId());
        tvCreateTime.setText("创建时间：" + getProductOrderDetail.getData().getPoCreateTime());
        tvPaymentTime.setText("付款时间：" + getProductOrderDetail.getData().getPoPayTime());
        tvSendTime.setText("发货时间：" + getProductOrderDetail.getData().getPoSendTime());
        tvDeliveryTime.setText("收货时间：" + getProductOrderDetail.getData().getPoDeliverTime());
        tvDealTime.setText("成交时间：" + getProductOrderDetail.getData().getPoOverTime());
        tvAllMoney.setText("" + getProductOrderDetail.getData().getPoTotalPrice());
        if (!TextUtils.isEmpty(getProductOrderDetail.getData().getPoDeliverCompany())) {
            rl_deliver.setVisibility(View.VISIBLE);
            tv_deliver_company.setText("快递公司： " + getProductOrderDetail.getData()
                    .getPoDeliverCompany());
            tv_deliver_code.setText("快递单号： " + getProductOrderDetail.getData()
                    .getPoDeliverCode());
        } else {
            rl_deliver.setVisibility(View.GONE);
        }
        adapter = new CommonAdapter<GetProductOrderDetail.DataBean.OrderDetailsBean>(this, R.layout.list_item_order_details, orderDetailsBeanList) {
            @Override
            protected void convert(ViewHolder holder, GetProductOrderDetail.DataBean.OrderDetailsBean orderDetailsBean, int position) {
                RequestOptions requestOptions = new RequestOptions()
                        .error(R.drawable.ic_exception)
                        .fallback(R.drawable.ic_exception);
                Glide.with(OrderDetailsActivity.this)
                        .load(getProductOrderDetail.getData().getOrderDetails().get(position).getPicName() + "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(OrderDetailsActivity.this)))
                        .apply(requestOptions)
                        .into((ImageView) holder.getView(R.id.iv_img));
                holder.setText(R.id.tv_goods_name, getProductOrderDetail.getData().getOrderDetails().get(position).getPName());
                if (!TextUtils.isEmpty(getProductOrderDetail.getData().getOrderDetails().get(position).getPodProperty())) {
                    if (19 < getProductOrderDetail.getData().getOrderDetails().get(position).getPodProperty().length()) {
                        holder.setText(R.id.tv_goods_content,
                                getProductOrderDetail.getData().getOrderDetails().get(position).getPodProperty()
                                        .substring(0, 16) + "···");
                    } else {
                        holder.setText(R.id.tv_goods_content,
                                getProductOrderDetail.getData().getOrderDetails().get(position).getPodProperty());
                    }
                } else {
                    if (19 < getProductOrderDetail.getData().getOrderDetails().get(position).getPDescribe().length()) {
                        holder.setText(R.id.tv_goods_content,
                                getProductOrderDetail.getData().getOrderDetails().get(position).getPDescribe()
                                        .substring(0, 16) + "···");
                    } else {
                        holder.setText(R.id.tv_goods_content,
                                getProductOrderDetail.getData().getOrderDetails().get(position).getPDescribe());
                    }
                }
                holder.setText(R.id.tv_goods_money, "¥" + getProductOrderDetail.getData().getOrderDetails().get(position).getPodPrice());
                holder.setText(R.id.tv_goods_num, "×" + getProductOrderDetail.getData().getOrderDetails().get(position).getPodNum());
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(OrderDetailsActivity.this, GoodsDetailActivity.class);
                intent.putExtra("pid", getProductOrderDetail.getData().getOrderDetails().get(position).getPId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
        tv_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                switch ("" + getProductOrderDetail.getData().getPoStatus()) {
                    case Constants.STATUS_PENDINGPAYMENT:
//取消订单
                        showDeleteDialog(getProductOrderDetail.getData().getPoId());
                        break;
                    case Constants.STATUS_PENDINGDELIVERY:
//去发货
                        intent = new Intent(OrderDetailsActivity.this, DeliveryActivity.class);
                        intent.putExtra("poId", getProductOrderDetail.getData().getPoId());
                        intent.putExtra("intentFlag", "AllFragment");
                        startActivity(intent);
                        finish();
                        break;
                    case Constants.STATUS_UNREFUNdORDER:
//确认退款
                        showDialog(getProductOrderDetail.getData().getPoId(), getProductOrderDetail.getData().getPoPayCode(), getProductOrderDetail.getData().getPoTotalPrice());
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @OnClick({R.id.img_back, R.id.rl_contact_seller, R.id.tv_shop_name, R.id.img_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_shop_name:
                Intent intent = new Intent(OrderDetailsActivity.this, ShopActivity.class);
                intent.putExtra("sId", getProductOrderDetail.getData().getSId());
                startActivity(intent);
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_contact_seller:
                if (getProductOrderDetail != null) {
                    NimUIKit.startP2PSession(OrderDetailsActivity.this, getProductOrderDetail.getData().getAccId(), null);
                }
                break;
            case R.id.img_message:

                break;
            default:
                break;
        }
    }

    private void showDeleteDialog(String poId) {
        LayoutInflater layoutInflater = LayoutInflater.from(OrderDetailsActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(OrderDetailsActivity.this, R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        TextView tv_live_content = view.findViewById(R.id.tv_live_content);
        tv_live_content.setText("确定要取消订单吗？");
        tv__live_never_reminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tv_live_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消订单
                delStoreProductOrder(poId, alertDialog);
            }
        });
    }

    /**
     * 取消订单
     */
    private void delStoreProductOrder(String poId, final Dialog alertDialog) {
        if (Utils.isNetworkAvailable(OrderDetailsActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.delStoreProductOrder)
                    .addParams("poId", poId)
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
                            showShortToast(OrderDetailsActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            progressBar.setVisibility(View.GONE);
                            Utils.LogJson(response);
                            DelStoreProductOrder delStoreProductOrder = new DelStoreProductOrder();
                            delStoreProductOrder = Utils.parserJsonResult(response, DelStoreProductOrder.class);
                            if (Constants.OK.equals(delStoreProductOrder.getFlag())) {
                                if (Constants.OK.equals(delStoreProductOrder.getData().getStatus())) {
                                    showShortToast(OrderDetailsActivity.this,
                                            "取消订单成功");
                                    alertDialog.dismiss();
                                    finish();
                                } else {
                                    showShortToast(OrderDetailsActivity.this,
                                            delStoreProductOrder.getData().getErrorString());
                                }

                            } else {
                                showShortToast(OrderDetailsActivity.this,
                                        delStoreProductOrder.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(OrderDetailsActivity.this, Constants.NETWORK_ERROR);
        }
    }

    private void showDialog(String poId, String payCode, String totalMoney) {
        LayoutInflater layoutInflater = LayoutInflater.from(OrderDetailsActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(OrderDetailsActivity.this, R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        TextView tv_live_content = view.findViewById(R.id.tv_live_content);
        tv_live_content.setText("确定要确认退款吗？");
        tv__live_never_reminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tv_live_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确认退款
                if (Constants.ALI_PSID.equals(psId)) {
                    ailTradeRefund(poId, payCode, "" + totalMoney, alertDialog);
                } else if (Constants.WX_PSID.equals(psId)) {
                    wxTradeRefund(poId, payCode, "" + totalMoney, alertDialog);
                }
            }
        });
    }

    /**
     * 支付宝确认退款
     */
    private void ailTradeRefund(String poId, String payCode, String totalMoney, final Dialog alertDialog) {
        if (Utils.isNetworkAvailable(OrderDetailsActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.ailTradeRefund)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("poId", poId)
                    .addParams("payCode", payCode)
                    .addParams("totalMoney", totalMoney + "0")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            progressBar.setVisibility(View.GONE);
                            /**
                             * 接口调用出错
                             */
                            showShortToast(OrderDetailsActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            progressBar.setVisibility(View.GONE);
                            Utils.LogJson(response);
                            AilTradeRefund ailTradeRefund = new AilTradeRefund();
                            ailTradeRefund = Utils.parserJsonResult(response, AilTradeRefund.class);
                            if (Constants.OK.equals(ailTradeRefund.getFlag())) {
                                if (Constants.OK.equals(ailTradeRefund.getData().getStatus())) {
                                    showShortToast(OrderDetailsActivity.this,
                                            "确认退款成功");
                                    alertDialog.dismiss();
                                    finish();
                                } else {
                                    showShortToast(OrderDetailsActivity.this,
                                            ailTradeRefund.getData().getErrorString());
                                }

                            } else {
                                showShortToast(OrderDetailsActivity.this,
                                        ailTradeRefund.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(OrderDetailsActivity.this, Constants.NETWORK_ERROR);
        }
    }
    /**
     * 微信确认退款
     */
    private void wxTradeRefund(String poId, String payCode, String totalMoney, final Dialog alertDialog) {
        if (Utils.isNetworkAvailable(OrderDetailsActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.wxTradeRefund)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("poId", poId)
                    .addParams("payCode", payCode)
                    .addParams("totalMoney", totalMoney + "0")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            progressBar.setVisibility(View.GONE);
                            /**
                             * 接口调用出错
                             */
                            showShortToast(OrderDetailsActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            progressBar.setVisibility(View.GONE);
                            Utils.LogJson(response);
                            WxTradeRefund wxTradeRefund = new WxTradeRefund();
                            wxTradeRefund = Utils.parserJsonResult(response, WxTradeRefund.class);
                            if (Constants.OK.equals(wxTradeRefund.getFlag())) {
                                if (Constants.OK.equals(wxTradeRefund.getData().getStatus())) {
                                    showShortToast(OrderDetailsActivity.this,
                                            "确认退款成功");
                                    alertDialog.dismiss();
                                    finish();
                                } else {
                                    showShortToast(OrderDetailsActivity.this,
                                            wxTradeRefund.getData().getErrorString());
                                }

                            } else {
                                showShortToast(OrderDetailsActivity.this,
                                        wxTradeRefund.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(OrderDetailsActivity.this, Constants.NETWORK_ERROR);
        }
    }
}
