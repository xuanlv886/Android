package app.cn.extra.mall.user.activity;

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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.OrderDetailsEvent;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.IMLogin;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.DelProductOrder;
import app.cn.extra.mall.user.vo.GetConfirmReceipt;
import app.cn.extra.mall.user.vo.GetProductOrderDetail;
import app.cn.extra.mall.user.vo.GetProductRequestRefund;
import app.cn.extra.mall.user.vo.ShoppingCarProduct;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
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
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.rl_deliver)
    RelativeLayout rl_deliver;
    @BindView(R.id.tv_deliver_company)
    TextView tv_deliver_company;
    @BindView(R.id.tv_deliver_code)
    TextView tv_deliver_code;

    private SharePreferenceUtil sharePreferenceUtil;
    private VaryViewHelperController varyViewHelperController;
    /**
     * 商品订单主键
     */
    String poId = "";
    String sName = "";
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
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    protected void onDestroy() {
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
    public void OrderDetailsEventBus(OrderDetailsEvent event) {
        finish();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        tv_true.setBackgroundColor(getResources().getColor(R.color.blues));
        /**
         * 线性布局
         */
        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManage);
//        /**
//         * 添加分隔线
//         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL));
    }

    private void init() {
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
        getProductOrderDetail();
    }

    /**
     * 获取页面数据接口
     * p--当前页
     * ps--页大小
     */
    private void getProductOrderDetail() {
        if (Utils.isNetworkAvailable(OrderDetailsActivity.this)) {

            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getProductOrderDetail)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("poId", poId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
//                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
//                            /**
//                             * 恢复显示数据的View
//                             */
//                            varyViewHelperController.restore();

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
            Toast.makeText(getApplicationContext(), Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
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
                tv_true.setText("付款");
                tv_false.setVisibility(View.VISIBLE);
                tv_false.setText("取消订单");
                ll_bottom.setVisibility(View.VISIBLE);
                break;
            case Constants.STATUS_PENDINGDELIVERY:
                tvType.setText("待发货");
                tvTypeBlue.setText("待发货");
                tv_true.setVisibility(View.VISIBLE);
                tv_true.setText("申请退款");
                tv_false.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.VISIBLE);
                break;
            case Constants.STATUS_PENDINGCOLLECTGOODS:
                tvType.setText("待收货");
                tvTypeBlue.setText("待收货");
                tv_true.setVisibility(View.VISIBLE);
                tv_true.setText("确认收货");
                tv_false.setVisibility(View.VISIBLE);
                tv_false.setText("申请退款");
                ll_bottom.setVisibility(View.VISIBLE);
                break;
            case Constants.STATUS_PENDINGEVALUATE:
                tvType.setText("待评价");
                tvTypeBlue.setText("待评价");
                tv_true.setVisibility(View.VISIBLE);
                tv_true.setText("去评价");
                tv_false.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.VISIBLE);
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
                tv_true.setVisibility(View.GONE);
                tv_false.setVisibility(View.GONE);
                ll_bottom.setVisibility(View.GONE);
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
        tvAllMoney.setText("¥" + getProductOrderDetail.getData().getPoTotalPrice());
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
                if (!TextUtils.isEmpty(getProductOrderDetail.getData().getOrderDetails()
                        .get(position).getPodProperty())) {
                    holder.setText(R.id.tv_goods_content, getProductOrderDetail.getData().getOrderDetails()
                            .get(position).getPodProperty());
                } else {
                    holder.setText(R.id.tv_goods_content, getProductOrderDetail.getData().getOrderDetails()
                            .get(position).getPDescribe());
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
                        List<ShoppingCarProduct> list = new ArrayList<ShoppingCarProduct>();
                        for (int i = 0; i < getProductOrderDetail.getData().getOrderDetails().size(); i++) {
                            ShoppingCarProduct shoppingCarProduct = new ShoppingCarProduct();
                            shoppingCarProduct.setPro_name(getProductOrderDetail.getData().getOrderDetails().get(i).getPName());
                            shoppingCarProduct.setProperty(getProductOrderDetail.getData().getOrderDetails().get(i).getPodProperty());
                            shoppingCarProduct.setNum(getProductOrderDetail.getData().getOrderDetails().get(i).getPodNum());
                            shoppingCarProduct.setSprice(Double.parseDouble(getProductOrderDetail.getData().getOrderDetails().get(i).getPodPrice()));
                            if (!TextUtils.isEmpty(getProductOrderDetail.getData().getPoTotalPrice())) {
                                shoppingCarProduct.setIntegralPrice(Double.valueOf(getProductOrderDetail.getData().getPoTotalPrice()));
                            } else {
                                shoppingCarProduct.setIntegralPrice(0.00);
                            }
                            shoppingCarProduct.setOnlinePrice(Double.parseDouble(getProductOrderDetail.getData().getOrderDetails().get(i).getPodPrice()));
                            shoppingCarProduct.setPic_url(getProductOrderDetail.getData().getOrderDetails().get(i).getPicName());
                            shoppingCarProduct.setPro_id(getProductOrderDetail.getData().getPoId());
                            list.add(shoppingCarProduct);
                        }
                        intent = new Intent(OrderDetailsActivity.this, CommodityBuyActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("state", 2);
                        bundle.putString("intentTag", "1");
                        bundle.putSerializable("goodsDetail", (Serializable) list);
                        bundle.putString("totalMoney", "" + getProductOrderDetail.getData().getPoTotalPrice());
                        bundle.putString("poId", getProductOrderDetail.getData().getPoId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                        break;
                    case Constants.STATUS_PENDINGDELIVERY:
                        //申请退款
                        showDialogRefund(getProductOrderDetail);
                        break;
                    case Constants.STATUS_PENDINGCOLLECTGOODS:
                        //确认收货
                        showDialogConfirm(getProductOrderDetail);
                        break;
                    case Constants.STATUS_PENDINGEVALUATE:
                        intent = new Intent(OrderDetailsActivity.this, GoodsEvaluateActivity.class);
                        intent.putExtra("poId", getProductOrderDetail.getData().getPoId());
                        Bundle bundle1 = new Bundle();
                        bundle1.putSerializable("getProductOrderDetail", getProductOrderDetail);
                        intent.putExtras(bundle1);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });
        tv_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                switch ("" + getProductOrderDetail.getData().getPoStatus()) {
                    case Constants.STATUS_PENDINGPAYMENT:
                        showDialog(getProductOrderDetail);
                        break;
                    case Constants.STATUS_PENDINGCOLLECTGOODS:
                        //申请退款
                        showDialogRefund(getProductOrderDetail);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @OnClick({R.id.img_back, R.id.rl_contact_seller, R.id.tv_shop_name})
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
                StatusCode status = NIMClient.getStatus();
                if (status == StatusCode.UNLOGIN) {
                    IMLogin imLogin = new IMLogin(OrderDetailsActivity.this);
                    imLogin.login(1);
                }
                // 打开单聊界面
                if (getProductOrderDetail != null) {
                    NimUIKit.startP2PSession(OrderDetailsActivity.this, getProductOrderDetail.getData().getAccid(), null);
                }
                break;
            default:
                break;
        }
    }

    private void showDialog(GetProductOrderDetail getProductOrderDetail) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(this, R.style.Theme_Transparent).
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
                delProductOrderPort(getProductOrderDetail.getData().getPoId(), alertDialog);
            }
        });
    }

    /**
     * 删除订单
     *
     * @param alertDialog dialog
     */
    private void delProductOrderPort(String poid, final Dialog alertDialog) {

        if (null != progressBar) {
            progressBar.setVisibility(View.VISIBLE);
        }
        String uid = sharePreferenceUtil.getUID();
        if (Utils.isNetworkAvailable(this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.delProductOrder)
                    .addParams("uId", uid)
                    .addParams("poId", poid)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            DelProductOrder delProductOrder = new DelProductOrder();
                            delProductOrder = Utils.parserJsonResult(response, DelProductOrder.class);
                            if ("true".equals(delProductOrder.getFlag())) {
                                if (Constants.OK.equals(delProductOrder.getData().getStatus())) {
                                    CustomToast.showToast(OrderDetailsActivity.this, "删除订单成功!", Toast.LENGTH_LONG);
                                    alertDialog.dismiss();
                                    finish();
                                } else {
                                    CustomToast.showToast(OrderDetailsActivity.this, delProductOrder.getData().getErrorString(), Toast.LENGTH_LONG);
                                }

                            } else {

                                CustomToast.showToast(OrderDetailsActivity.this,
                                        delProductOrder.getErrorString(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            if (null != progressBar) {
                progressBar.setVisibility(View.GONE);
            }
            CustomToast.showToast(this,
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);

        }
    }

    private void showDialogConfirm(GetProductOrderDetail getProductOrderDetail) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(this, R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        TextView tv_live_content = view.findViewById(R.id.tv_live_content);
        tv_live_content.setText("确定要确认收货吗？");
        tv__live_never_reminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tv_live_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确认收货
                getConfirmReceipt(getProductOrderDetail.getData().getPoId(), alertDialog);
            }
        });
    }

    /**
     * 确认收货
     *
     * @param alertDialog dialog
     */
    private void getConfirmReceipt(String poid, final Dialog alertDialog) {
        if (null != progressBar) {
            progressBar.setVisibility(View.VISIBLE);
        }
        String uid = sharePreferenceUtil.getUID();
        if (Utils.isNetworkAvailable(this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getConfirmReceipt)
                    .addParams("uId", uid)
                    .addParams("poId", poid)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            GetConfirmReceipt getConfirmReceipt = new GetConfirmReceipt();
                            getConfirmReceipt = Utils.parserJsonResult(response, GetConfirmReceipt.class);
                            if ("true".equals(getConfirmReceipt.getFlag())) {
                                if (Constants.OK.equals(getConfirmReceipt.getData().getStatus())) {
                                    CustomToast.showToast(OrderDetailsActivity.this, "确认收货成功", Toast.LENGTH_LONG);
                                    if (alertDialog != null) {
                                        alertDialog.dismiss();
                                    }
                                    finish();
                                } else {
                                    CustomToast.showToast(OrderDetailsActivity.this, getConfirmReceipt.getData().getErrorString(), Toast.LENGTH_LONG);
                                }

                            } else {

                                CustomToast.showToast(OrderDetailsActivity.this,
                                        getConfirmReceipt.getErrorString(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            if (null != progressBar) {
                progressBar.setVisibility(View.GONE);
            }
            CustomToast.showToast(this,
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);

        }
    }

    private void showDialogRefund(GetProductOrderDetail getProductOrderDetail) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(this, R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        TextView tv_live_content = view.findViewById(R.id.tv_live_content);
        tv_live_content.setText("确定要申请退款吗？");
        tv__live_never_reminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tv_live_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //申请退款
                getProductRequestRefund(getProductOrderDetail.getData().getPoId(), alertDialog);
            }
        });
    }

    /**
     * 申请退款
     *
     * @param alertDialog dialog
     */
    private void getProductRequestRefund(String poid, final Dialog alertDialog) {
        if (null != progressBar) {
            progressBar.setVisibility(View.VISIBLE);
        }
        String uid = sharePreferenceUtil.getUID();
        if (Utils.isNetworkAvailable(this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getProductRequestRefund)
                    .addParams("uId", uid)
                    .addParams("poId", poid)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            GetProductRequestRefund getProductRequestRefund = new GetProductRequestRefund();
                            getProductRequestRefund = Utils.parserJsonResult(response, GetProductRequestRefund.class);
                            if ("true".equals(getProductRequestRefund.getFlag())) {
                                if (Constants.OK.equals(getProductRequestRefund.getData().getStatus())) {
                                    CustomToast.showToast(OrderDetailsActivity.this, "申请退款成功", Toast.LENGTH_LONG);
                                    alertDialog.dismiss();
                                    finish();
                                } else {

                                    CustomToast.showToast(OrderDetailsActivity.this,
                                            getProductRequestRefund.getData().getErrorString(), Toast.LENGTH_SHORT);
                                }

                            } else {

                                CustomToast.showToast(OrderDetailsActivity.this,
                                        getProductRequestRefund.getErrorString(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            if (null != progressBar) {
                progressBar.setVisibility(View.GONE);
            }
            CustomToast.showToast(this,
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);

        }
    }
}
