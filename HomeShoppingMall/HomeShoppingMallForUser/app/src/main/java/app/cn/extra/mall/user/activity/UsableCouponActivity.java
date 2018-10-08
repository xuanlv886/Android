package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.GetAvailableCoupons;
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

/**
 * 可用优惠券
 */
public class UsableCouponActivity extends BaseActivty implements SwipyRefreshLayout.OnRefreshListener {
    private ImageButton ibBack;
    private TextView tvTitle;
    private String name;
    private Double money;
    private int type = 100;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private SharePreferenceUtil sp;
    private List<GetAvailableCoupons.DataBean.CustomerCouponsListBean> couponData;
    private VaryViewHelperController varyViewHelperController;
    private CommonAdapter<GetAvailableCoupons.DataBean.CustomerCouponsListBean> adapter;
    /**
     * 当前页，用于分页获取数据 从1开始
     */
    private int currentPage = 1;
    /**
     * 判断是下拉更新还是上拉加载更多 false--下拉更新， true--上拉加载更多
     */
    private boolean loadMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usable_coupon);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        money = intent.getDoubleExtra("money", 0.00);
        type = intent.getIntExtra("type", 0);
        initView();
        initData();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        sp = new SharePreferenceUtil(this, Constants.SAVE_USER);
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
        getAvailableCouponsPort();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 网格布局
         */
        GridLayoutManager layoutManage = new GridLayoutManager(UsableCouponActivity.this, 2);
        recyclerView.setLayoutManager(layoutManage);
        /**
         * 添加分隔线
         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(UsableCouponActivity.this,
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

    private void initData() {
        //获取可用的优惠券
        getAvailableCouponsPort();
    }

    /**
     * 获取可用的优惠券
     */
    private void getAvailableCouponsPort() {
        String cid = sp.getUID();
        if (Utils.isNetworkAvailable(UsableCouponActivity.this)) {
            OkHttpUtils
                    .post()
                    .url("")
                    .addParams("cId", cid)
                    .addParams("status", 0 + "")
                    .addParams("ctName", "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            GetAvailableCoupons getAvailableCoupons = new GetAvailableCoupons();
                            getAvailableCoupons = Utils.parserJsonResult(response, GetAvailableCoupons.class);
                            if ("true".equals(getAvailableCoupons.getFlag())) {
                                couponData = getAvailableCoupons.getData().getCustomerCouponsList();
                                for (int i = couponData.size() - 1; i >= 0; i--) {
                                    if (!TextUtils.isEmpty(name)) {
                                        if (!name.equals(couponData.get(i).getNameType()) &&
                                                !"全场通用".equals(couponData.get(i).getNameType())) {
                                            couponData.remove(i);
                                        }
//
                                    }
                                }
                                for (int i = couponData.size() - 1; i >= 0; i--) {
                                    if (!TextUtils.isEmpty(name)) {
                                        if (couponData.get(i).getCtType() == 0) {
                                            if (money < Double.parseDouble(couponData.get(i).getNeedOver())) {
                                                couponData.remove(i);
                                            }
                                        }
                                    }
                                }
                                for (int i = couponData.size() - 1; i >= 0; i--) {
                                    if (!TextUtils.isEmpty(name)) {
                                        if (couponData.get(i).getCtType() != 0) {
                                            if (money < Double.parseDouble(couponData.get(i).getCcMoney())) {
                                                couponData.remove(i);
                                            }
                                        }
                                    }
                                }
                                adapter = new CommonAdapter<GetAvailableCoupons.DataBean.CustomerCouponsListBean>(UsableCouponActivity.this, R.layout.item_unused_coupon, couponData) {
                                    @Override
                                    protected void convert(ViewHolder holder, GetAvailableCoupons.DataBean.CustomerCouponsListBean customerCouponsListBean, int position) {
                                        holder.setText(R.id.tv_coupon_money, customerCouponsListBean.getCcMoney());
                                        holder.setText(R.id.tv_unused_type, customerCouponsListBean.getNameType());
                                        holder.setText(R.id.tv_coupon_content, customerCouponsListBean.getContent());
                                        holder.setText(R.id.tv_unused_time, "起始日期：" + customerCouponsListBean.getStartTime());
                                        holder.setText(R.id.tv_unused_abort_time, "截止日期：" + customerCouponsListBean.getEndTime());
                                        holder.getView(R.id.ll_unused).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String ccId = customerCouponsListBean.getId();
                                                if (type == 0) {
                                                    //便捷购物
                                                    Intent intent = new Intent(UsableCouponActivity.this, CommodityBuyActivity.class);
                                                    intent.putExtra("ccId", ccId);
                                                    intent.putExtra("money", customerCouponsListBean.getCcMoney());
                                                    setResult(0, intent);
                                                    finish();
                                                }

                                            }
                                        });
                                    }
                                };
                                recyclerView.setAdapter(adapter);

                            } else {

                                CustomToast.showToast(UsableCouponActivity.this,
                                        getAvailableCoupons.getErrorString(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            CustomToast.showToast(UsableCouponActivity.this,
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);

        }
    }

    @OnClick(R.id.img_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            currentPage = 1;
            loadMore = false;
            /**
             * 下拉刷新首页数据
             */
            getAvailableCouponsPort();
            refreshLayout.setRefreshing(false);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            currentPage += 1;
            loadMore = true;
            /**
             * 上拉加载首页数据
             */
            getAvailableCouponsPort();
            refreshLayout.setRefreshing(false);
        }
    }
}
