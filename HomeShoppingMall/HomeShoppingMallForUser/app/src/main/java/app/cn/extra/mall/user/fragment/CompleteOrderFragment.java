package app.cn.extra.mall.user.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.activity.OrderDetailsActivity;
import app.cn.extra.mall.user.activity.OrderManagementActivity;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.DelProductOrder;
import app.cn.extra.mall.user.vo.GetProductOrder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cfkj.app.cn.cfkjcommonlib.common.BaseFragment;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import cfkj.app.cn.cfkjcommonlib.view.ReplaceViewHelper.VaryViewHelperController;
import okhttp3.Call;

/**
 * 已完成订单
 */

public class CompleteOrderFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {
    private SharePreferenceUtil sharePreferenceUtil;
    private View mView;
    private VaryViewHelperController varyViewHelperController;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    Unbinder unbinder;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private CommonAdapter<GetProductOrder.DataBean.ProductOrderListBean> adapter;
    /**
     * 当前页，用于分页获取数据 从1开始
     */
    private int currentPage = 1;
    /**
     * 判断是下拉更新还是上拉加载更多 false--下拉更新， true--上拉加载更多
     */
    private boolean loadMore = false;
    /**
     * 用于存放临时数据
     */
    List<GetProductOrder.DataBean.ProductOrderListBean> productOrderListBeanList = null;

    private int tag = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_refund_order, container, false);
        unbinder = ButterKnife.bind(this, mView);
        sharePreferenceUtil = new SharePreferenceUtil(OrderManagementActivity.context,
                Constants.SAVE_USER);
        tag = 0;
        initView();
        return mView;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (1 == tag) {
            currentPage = 1;
            loadMore = false;
            getProductOrder();
        }
    }
    /**
     * 初始化界面内元素
     */
    private void initView() {
        /**
         * 设置异常情况界面
         */
        varyViewHelperController = createCaseViewHelperController(refreshLayout);
        varyViewHelperController.setUpRefreshViews(varyViewHelperController.getErrorView(),
                varyViewHelperController.getNetworkPoorView(),varyViewHelperController.getEmptyView());
        initRefreshLayout();
        initRecyclerView();
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        getProductOrder();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 线性布局
         */
        LinearLayoutManager layoutManage = new LinearLayoutManager(OrderManagementActivity.context);
        recyclerView.setLayoutManager(layoutManage);
//        /**
//         * 添加分隔线
//         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(OrderManagementActivity.context,
//                DividerItemDecoration.VERTICAL));
    }

    /**
     * 初始化SwipeRefreshLayout
     */
    private void initRefreshLayout() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        refreshLayout.setOnRefreshListener(this);
    }

    /**
     * 点击异常情况界面，重新调用接口刷新数据
     */
    @Override
    public void clickEvent() {
        super.clickEvent();
        getProductOrder();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            currentPage = 1;
            loadMore = false;
            /**
             * 下拉刷新首页数据
             */
            getProductOrder();
            refreshLayout.setRefreshing(false);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            currentPage += 1;
            loadMore = true;
            /**
             * 上拉加载首页数据
             */
            getProductOrder();
            refreshLayout.setRefreshing(false);
        }
    }

    /**
     * 获取页面数据接口
     * p--当前页
     * ps--页大小
     */
    private void getProductOrder() {
        if (Utils.isNetworkAvailable(OrderManagementActivity.context)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.getProductOrder)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("status", Constants.STATUS_COMPLETEORDER)
                    .addParams("currentPage", currentPage + "")
                    .addParams("size", Constants.PAGE_SIZE + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            /**
                             * 恢复显示数据的View
                             */
                            varyViewHelperController.restore();

                            GetProductOrder getProductOrder = new GetProductOrder();
                            getProductOrder = Utils.parserJsonResult(response,
                                    GetProductOrder.class);
                            if (Constants.OK.equals(getProductOrder.getFlag())) {
                                if (loadMore) {
                                    if (null != getProductOrder.getData()
                                            .getProductOrderList()
                                            && 0 < getProductOrder.getData()
                                            .getProductOrderList().size()) {
                                        for (int i = 0; i < getProductOrder.getData()
                                                .getProductOrderList().size(); i++) {
                                            productOrderListBeanList.add(getProductOrder.getData()
                                                    .getProductOrderList().get(i));
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        showLongToast(OrderManagementActivity.context,
                                                "没有更多订单了！");
                                    }

                                } else {
                                    if (null != getProductOrder.getData()
                                            .getProductOrderList()
                                            && 0 < getProductOrder.getData()
                                            .getProductOrderList().size()) {
                                        productOrderListBeanList = getProductOrder.getData()
                                                .getProductOrderList();
                                        setRecommendProductData();
                                    }else {
                                        varyViewHelperController.showEmptyView();
                                    }
                                }
                                tag = 1;
                            } else {
                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
            varyViewHelperController.showNetworkPoorView();
        }
    }

    /**
     * 填充数据
     */
    private void setRecommendProductData() {
        adapter = new CommonAdapter<GetProductOrder.DataBean.ProductOrderListBean>(
                OrderManagementActivity.context, R.layout.order_item, productOrderListBeanList) {
            @Override
            protected void convert(ViewHolder holder, GetProductOrder.DataBean.ProductOrderListBean productOrderListBean, int position) {
                RequestOptions requestOptions = new RequestOptions()
                        .error(R.drawable.ic_exception)
                        .fallback(R.drawable.ic_exception);
                Glide.with(OrderManagementActivity.context)
                        .load(productOrderListBean.getPicName()+ "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(getActivity())))
                        .apply(requestOptions)
                        .into((ImageView) holder.getView(R.id.iv_img));
                if (!TextUtils.isEmpty(productOrderListBean.getSName())) {
                    if (12 < productOrderListBean.getSName().length()) {
                        holder.setText(R.id.tv_shop_name,
                                productOrderListBean.getSName()
                                        .substring(0, 10) + "···");
                    } else {
                        holder.setText(R.id.tv_shop_name,
                                productOrderListBean.getSName());
                    }
                } else {
                    holder.setText(R.id.tv_shop_name, "");
                }
                holder.setText(R.id.tv_type, "已完成");
                holder.setVisible(R.id.item_true_btn, false);
                holder.setVisible(R.id.item_false_btn, false);
                holder.setText(R.id.tv_goods_name, productOrderListBean.getPName());
                if (!TextUtils.isEmpty(productOrderListBean.getPodProperty())) {
                    if (19 < productOrderListBean.getPodProperty().length()) {
                        holder.setText(R.id.tv_goods_content,
                                productOrderListBean.getPodProperty()
                                        .substring(0, 16) + "···");
                    } else {
                        holder.setText(R.id.tv_goods_content,
                                productOrderListBean.getPodProperty());
                    }
                } else {
                    if (19 < productOrderListBean.getPDescribe().length()) {
                        holder.setText(R.id.tv_goods_content,
                                productOrderListBean.getPDescribe()
                                        .substring(0, 16) + "···");
                    } else {
                        holder.setText(R.id.tv_goods_content,
                                productOrderListBean.getPDescribe());
                    }
                }
                holder.setText(R.id.tv_order_num, productOrderListBean.getPoOrderId());
                holder.setText(R.id.tv_all_num, "共" + productOrderListBean.getPodNum() + "件商品");
                holder.setText(R.id.tv_goods_money,
                        "¥" + productOrderListBean.getPodPrice() + "");
                holder.setText(R.id.tv_all_money,
                        "合计：¥" + productOrderListBean.getPoTotalPrice() + "");
//                holder.setText(R.id.tv_item_product_browse_num,
//                        productOrderListBean.getPoNum() + "次浏览");
                holder.getView(R.id.item_true_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                holder.getView(R.id.item_false_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        };
        /**
         * 点击事件
         */
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(OrderManagementActivity.context, OrderDetailsActivity.class);
                intent.putExtra("poId", productOrderListBeanList.get(position).getPoId());
                intent.putExtra("sName", productOrderListBeanList.get(position).getSName());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void showDialog(final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(OrderManagementActivity.context);
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(OrderManagementActivity.context, R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        TextView tv_live_content = view.findViewById(R.id.tv_live_content);
        tv_live_content.setText("确定要删除订单吗？");
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
                delProductOrderPort(position, alertDialog);
            }
        });
    }

    /**
     * 删除订单
     *
     * @param position    position
     * @param alertDialog dialog
     */
    private void delProductOrderPort(int position, final Dialog alertDialog) {
        if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
        String uid = sharePreferenceUtil.getUID();
        String oid = productOrderListBeanList.get(position).getPId();
        if (Utils.isNetworkAvailable(OrderManagementActivity.context)) {
            OkHttpUtils
                    .post()
                    .url(Constants.delProductOrder)
                    .addParams("cId", uid)
                    .addParams("oId", oid)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            DelProductOrder delProductOrder = new DelProductOrder();
                            delProductOrder = Utils.parserJsonResult(response, DelProductOrder.class);
                            if ("true".equals(delProductOrder.getFlag())) {
                                if (Constants.OK.equals(delProductOrder.getData().getStatus())) {
                                    getProductOrder();
                                    CustomToast.showToast(OrderManagementActivity.context, "删除订单成功!", Toast.LENGTH_LONG);
                                    alertDialog.dismiss();
                                } else {
                                    getProductOrder();
                                    CustomToast.showToast(OrderManagementActivity.context,
                                            delProductOrder.getData().getErrorString(), Toast.LENGTH_LONG);
                                    alertDialog.dismiss();
                                }
                            } else {
                                alertDialog.dismiss();
                                CustomToast.showToast(OrderManagementActivity.context,
                                        delProductOrder.getErrorString(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
            CustomToast.showToast(OrderManagementActivity.context,
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);

        }
    }
}
