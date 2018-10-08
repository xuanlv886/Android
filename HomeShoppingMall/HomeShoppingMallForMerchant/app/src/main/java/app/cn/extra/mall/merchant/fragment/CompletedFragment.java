package app.cn.extra.mall.merchant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.Wave;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.activity.CompletedDetailsActivity;
import app.cn.extra.mall.merchant.activity.MyDemandActivity;
import app.cn.extra.mall.merchant.event.CompletedFragmentEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.GetUserRequirement;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cfkj.app.cn.cfkjcommonlib.common.BaseFragment;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import cfkj.app.cn.cfkjcommonlib.view.ReplaceViewHelper.VaryViewHelperController;
import okhttp3.Call;

/**
 * 已完成
 */
public class CompletedFragment extends BaseFragment
        implements SwipyRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    Unbinder unbinder;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private SharePreferenceUtil sharePreferenceUtil;
    private View mView;
    private VaryViewHelperController varyViewHelperController;
    private CommonAdapter<GetUserRequirement.DataBean.UserRequirementListBean> adapter;
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
    List<GetUserRequirement.DataBean.UserRequirementListBean> userRequirementBeanList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_completed, container, false);
        unbinder = ButterKnife.bind(this, mView);
        sharePreferenceUtil = new SharePreferenceUtil(MyDemandActivity.context,
                Constants.SAVE_USER);
        initView();
        return mView;
    }
    @Override
    public void onResume() {
        super.onResume();
        currentPage = 1;
        loadMore = false;
        getUserRequirement();
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
        getUserRequirement();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 线性布局
         */
        LinearLayoutManager layoutManage = new LinearLayoutManager(MyDemandActivity.context);
        recyclerView.setLayoutManager(layoutManage);
//        /**
//         * 添加分隔线
//         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(MyDemandActivity.context,
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
     * 获取页面数据接口
     * p--当前页
     * ps--页大小
     */
    private void getUserRequirement() {
        if (Utils.isNetworkAvailable(MyDemandActivity.context)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getStoreRequirement)
                    .addParams("roStatus", Constants.STATUS_DEMAND_COMPLETED)
                    .addParams("sId", sharePreferenceUtil.getSID())
                    .addParams("currentPage", currentPage + "")
                    .addParams("size", Constants.PAGE_SIZE + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 恢复显示数据的View
                             */
                            varyViewHelperController.restore();
                            Utils.LogJson(response);
                            GetUserRequirement getUserRequirement = new GetUserRequirement();
                            getUserRequirement = Utils.parserJsonResult(response,
                                    GetUserRequirement.class);
                            if (Constants.OK.equals(getUserRequirement.getFlag())) {
                                if (loadMore) {
                                    if (null != getUserRequirement.getData()
                                            .getUserRequirementList()
                                            && 0 < getUserRequirement.getData()
                                            .getUserRequirementList().size()) {
                                        for (int i = 0; i < getUserRequirement.getData()
                                                .getUserRequirementList().size(); i++) {
                                            userRequirementBeanList.add(getUserRequirement.getData()
                                                    .getUserRequirementList().get(i));
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        showLongToast(MyDemandActivity.context,
                                                "没有更多需求了！");
                                        currentPage = 1;
                                    }

                                } else {
                                    if (null != getUserRequirement.getData()
                                            .getUserRequirementList()
                                            && 0 < getUserRequirement.getData()
                                            .getUserRequirementList().size()) {
                                        userRequirementBeanList = getUserRequirement.getData()
                                                .getUserRequirementList();
                                        if (userRequirementBeanList != null) {
                                            setRecommendProductData();
                                        }
                                    } else {
                                        varyViewHelperController.showEmptyView();
                                    }
                                }
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
        adapter = new CommonAdapter<GetUserRequirement.DataBean.UserRequirementListBean>(
                MyDemandActivity.context, R.layout.list_item_demand, userRequirementBeanList) {
            @Override
            protected void convert(ViewHolder holder, GetUserRequirement.DataBean.UserRequirementListBean userRequirementBean, int position) {
                if (!TextUtils.isEmpty(userRequirementBean.getUrTitle())) {
                    holder.setText(R.id.tv_demand_title,
                            "需求标题：" + userRequirementBean.getUrTitle());
                } else {
                    holder.setText(R.id.tv_demand_title, "需求标题：");
                }
                holder.setText(R.id.tv_demand_type, "已完成");
                holder.setText(R.id.tv_demend_content, "需求描述：" + userRequirementBean.getUrContent());
                holder.setText(R.id.tv_send_time, "发布时间：" + userRequirementBean.getUrCreateTime());
//                holder.getView(R.id.tv_confirm_time).setVisibility(View.VISIBLE);
                holder.setText(R.id.tv_confirm_time, "确认时间：" + userRequirementBean.getRoConfirmTime());
//                holder.getView(R.id.tv_complete_time).setVisibility(View.VISIBLE);
                holder.setText(R.id.tv_complete_time, "完成时间：" + userRequirementBean.getRoOverTime());
                holder.getView(R.id.tv_demand_num).setVisibility(View.GONE);
                holder.setText(R.id.tv_demand_money, "悬赏金额：¥" + userRequirementBean.getUrOfferPrice());
                if ("取货送货".equals(userRequirementBean.getRtName())) {
                    holder.setVisible(R.id.ll_demand_get_address, true);
                    holder.setText(R.id.tv_demand_get_address, "取货地址：" + userRequirementBean.getUrGetAddress());
                } else {
                    holder.setVisible(R.id.ll_demand_get_address, false);
                }
            }
        };
        /**
         * 点击事件
         */
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(MyDemandActivity.context, CompletedDetailsActivity.class);
                intent.putExtra("urId", userRequirementBeanList.get(position).getUrId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * 点击异常情况界面，重新调用接口刷新数据
     */
    @Override
    public void clickEvent() {
        super.clickEvent();
        getUserRequirement();
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
            getUserRequirement();
            refreshLayout.setRefreshing(false);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            currentPage += 1;
            loadMore = true;
            /**
             * 上拉加载首页数据
             */
            getUserRequirement();
            refreshLayout.setRefreshing(false);
        }
    }
}
