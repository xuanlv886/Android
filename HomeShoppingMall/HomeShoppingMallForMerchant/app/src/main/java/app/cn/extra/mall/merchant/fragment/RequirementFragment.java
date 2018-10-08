package app.cn.extra.mall.merchant.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.activity.MyDemandActivity;
import app.cn.extra.mall.merchant.activity.RequirementDetailsActivity;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.GetAllRequirement;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cfkj.app.cn.cfkjcommonlib.common.BaseFragment;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import cfkj.app.cn.cfkjcommonlib.view.ReplaceViewHelper.VaryViewHelperController;
import okhttp3.Call;

/**
 * Description 需求中心页Fragment
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class RequirementFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {
    @BindView(R.id.rl_newest)
    RelativeLayout rlNewest;
    @BindView(R.id.img_newest)
    ImageView imgNewest;
    @BindView(R.id.tv_newest)
    TextView tvNewest;
    @BindView(R.id.tv_hot)
    TextView tvHot;
    @BindView(R.id.rl_price)
    RelativeLayout rlPrice;
    @BindView(R.id.img_price)
    ImageView imgPrice;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_mine)
    TextView tvMine;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    Unbinder unbinder;

    private View mView;
    private VaryViewHelperController varyViewHelperController;

    /**
     * 当前页，用于分页获取数据 从1开始
     */
    private int currentPage = 1;
    /**
     * 判断是下拉更新还是上拉加载更多 false--下拉更新， true--上拉加载更多
     */
    private boolean loadMore = false;
    private SharePreferenceUtil sharePreferenceUtil;

    private List<GetAllRequirement.DataBean> hotRequirementList = null;
    private CommonAdapter<GetAllRequirement.DataBean> adapter;
    /**
     * 热门排序
     * <p>
     * 为“0”时表示进行热门排序，否则传空字符串
     */
    String hotSort = "0";
    /**
     * 时间排序
     * <p>
     * 0--降序，1--升序
     */
    String timeSort = "";
    /**
     * 价格排序
     * <p>
     * 0--降序，1--升序
     */
    String priceSort = "";
private Activity activity;
    public static RequirementFragment newInstance(String param1) {
        RequirementFragment fragment = new RequirementFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_requirement, container, false);
        unbinder = ButterKnife.bind(this, mView);
        if(activity==null) {
            activity=getActivity();
        }
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isAdded()) {
            initView();
        }
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(activity, Constants.SAVE_USER);
        /**
         * 设置异常情况界面
         */
        varyViewHelperController = createCaseViewHelperController(refreshLayout);
        varyViewHelperController.setUpRefreshViews(varyViewHelperController.getErrorView(),
                varyViewHelperController.getNetworkPoorView(), varyViewHelperController.getEmptyView());
        initRefreshLayout();
        initRecyclerView();
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 调起接口，获取数据
     */
    private void getData() {
        getAllRequirementPort();
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
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 设置RecyclerView管理器
         */
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false));
//        /**
//         * 添加分隔线
//         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(activity(),
//                DividerItemDecoration.VERTICAL));
    }

    /**
     * 分页获取满足条件的需求列表接口
     */
    private void getAllRequirementPort() {
        if (Utils.isNetworkAvailable(activity)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getAllRequirement)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
                    /**
                     * 热门排序
                     */
                    .addParams("hotSort", hotSort)
                    .addParams("timeSort", timeSort)
                    .addParams("priceSort", priceSort)
                    .addParams("currentPage", currentPage + "")
                    .addParams("size", Constants.PAGE_SIZE + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 恢复显示数据的View
                             */
                            varyViewHelperController.restore();
                            Utils.LogJson(response);
                            GetAllRequirement getAllRequirement = new GetAllRequirement();

                            getAllRequirement = Utils.parserJsonResult(response,
                                    GetAllRequirement.class);
                            if (Constants.OK.equals(getAllRequirement.getFlag())) {
                                if (loadMore) {
                                    if (null != getAllRequirement.getData()
                                            && 0 < getAllRequirement.getData().size()) {
                                        for (int i = 0; i < getAllRequirement.getData().size(); i++) {
                                            hotRequirementList.add(getAllRequirement.getData()
                                                    .get(i));
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        showLongToast(activity,
                                                getResources().getString(R.string.noMoreHotRequirement));
                                        currentPage = 1;
                                    }

                                } else {
                                    if (null != getAllRequirement.getData()
                                            && 0 < getAllRequirement.getData().size()) {
                                        hotRequirementList = getAllRequirement.getData();
                                        setHotRequirementData();
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
     * 填充热门需求数据
     */
    private void setHotRequirementData() {
        adapter = new CommonAdapter<GetAllRequirement.DataBean>(
                activity, R.layout.list_item_demand, hotRequirementList) {
            @Override
            protected void convert(ViewHolder holder,
                                   GetAllRequirement.DataBean
                                           requirementBean, int position) {
                holder.setVisible(R.id.ll_demand_money, true);
                holder.setText(R.id.tv_demand_type,
                        requirementBean.getRtName());
//                for (RequirementOrderStatus info : RequirementOrderStatus.values()) {
//                    if (requirementBean.getRoStatus() == info.getId()) {
//                        holder.setText(R.id.tv_requirement_order_status, info.getName() + "");
//                    }
//                }
                holder.setText(R.id.tv_demand_title,
                        getResources().getString(R.string.requirementTitle)
                                + requirementBean.getUrTitle());
                holder.setText(R.id.tv_demend_content,
                        getResources().getString(R.string.requirementContent)
                                + requirementBean.getUrContent());
                holder.setText(R.id.tv_send_time,
                        getResources().getString(R.string.requirementCreateTime)
                                + requirementBean.getUrCreateTime());
                if ("1".equals(requirementBean.getUrOfferType())) {
                    holder.setText(R.id.tv_demand_money,
                            "悬赏金额：" + "" + "需商家报价");
                } else {
                    holder.setText(R.id.tv_demand_money,
                            "悬赏金额：" + "¥" + requirementBean.getUrOfferPrice());
                }
                if ("取货送货".equals(requirementBean.getRtName())) {
                    holder.setVisible(R.id.ll_demand_get_address, true);
                    holder.setText(R.id.tv_demand_get_address, "取货地址：" + requirementBean.getUrGetAddress());
                } else {
                    holder.setVisible(R.id.ll_demand_get_address, false);
                }
            }
        };
        /**
         * 热门需求点击事件
         */
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(activity, RequirementDetailsActivity.class);
                intent.putExtra("roStatus", "" + hotRequirementList.get(position).getRoStatus());
                intent.putExtra("roId", hotRequirementList.get(position).getRoId());
                intent.putExtra("urId", hotRequirementList.get(position).getUrId());
                intent.putExtra("applyStatus", hotRequirementList.get(position).getApplyStatus());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.rl_newest, R.id.tv_hot, R.id.rl_price, R.id.tv_mine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_newest:
                btnNewClicked();
                break;
            case R.id.tv_hot:
                btnHotClicked();
                break;
            case R.id.rl_price:
                btnPriceClicked();
                break;
            case R.id.tv_mine:
                Intent intent = new Intent(activity, MyDemandActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    // 热门
    private void btnHotClicked() {
        switch (hotSort) {
            case "": // 灰色->从低到高
                imgNewest.setImageResource(R.mipmap.icon_xiaoliang_nor);
                imgPrice.setImageResource(R.mipmap.icon_xiaoliang_nor);
                tvHot.setTextColor(getResources().getColor(R.color.blue));
                tvNewest.setTextColor(getResources().getColor(R.color.black));
                tvPrice.setTextColor(getResources().getColor(R.color.black));
                priceSort = "";
                hotSort = "0";
                timeSort = "";
                currentPage = 1;
                loadMore = false;
                getData();
                break;
//            case "0": // 从低到高->从高到低
//                imgNewest.setImageResource(R.mipmap.icon_xiaoliang_nor);
//                imgPrice.setImageResource(R.mipmap.icon_xiaoliang_nor);
//                tvHot.setTextColor(getResources().getColor(R.color.black));
//                tvNewest.setTextColor(getResources().getColor(R.color.black));
//                tvPrice.setTextColor(getResources().getColor(R.color.black));
//                priceSort = "";
//                hotSort = "";
//                timeSort = "";
//                currentPage = 1;
//                loadMore = false;
//                getData();
//                break;
            default:
                break;
        }
    }

    // 时间
    private void btnNewClicked() {
        switch (timeSort) {
            case "": // 灰色->从低到高
                imgNewest.setImageResource(R.mipmap.icon_xiaoliang_didaogao);
                imgPrice.setImageResource(R.mipmap.icon_xiaoliang_nor);
                tvHot.setTextColor(getResources().getColor(R.color.black));
                tvNewest.setTextColor(getResources().getColor(R.color.blue));
                tvPrice.setTextColor(getResources().getColor(R.color.black));
                priceSort = "";
                hotSort = "";
                timeSort = "0";
                currentPage = 1;
                loadMore = false;
                getData();
                break;
            case "0": // 从低到高->从高到低
                imgNewest.setImageResource(R.mipmap.icon_xiaoliang_gaodaodi);
                imgPrice.setImageResource(R.mipmap.icon_xiaoliang_nor);
                tvHot.setTextColor(getResources().getColor(R.color.black));
                tvNewest.setTextColor(getResources().getColor(R.color.blue));
                tvPrice.setTextColor(getResources().getColor(R.color.black));
                priceSort = "";
                hotSort = "";
                timeSort = "1";
                currentPage = 1;
                loadMore = false;
                getData();
                break;
//            case "1": // 从高到低->灰色
//                imgNewest.setImageResource(R.mipmap.icon_xiaoliang_nor);
//                tvHot.setTextColor(getResources().getColor(R.color.black));
//                tvNewest.setTextColor(getResources().getColor(R.color.black));
//                tvPrice.setTextColor(getResources().getColor(R.color.black));
//                timeSort = "";
//                currentPage = 1;
//                loadMore = false;
//                getData();
//                break;
            case "1": // 灰色->从低到高
                imgNewest.setImageResource(R.mipmap.icon_xiaoliang_didaogao);
                imgPrice.setImageResource(R.mipmap.icon_xiaoliang_nor);
                tvHot.setTextColor(getResources().getColor(R.color.black));
                tvNewest.setTextColor(getResources().getColor(R.color.blue));
                tvPrice.setTextColor(getResources().getColor(R.color.black));
                priceSort = "";
                hotSort = "";
                timeSort = "0";
                currentPage = 1;
                loadMore = false;
                getData();
                break;
            default:
                break;
        }
    }

    // 价格
    private void btnPriceClicked() {
        switch (priceSort) {
            case "": // 灰色->从低到高
                imgPrice.setImageResource(R.mipmap.icon_xiaoliang_didaogao);
                imgNewest.setImageResource(R.mipmap.icon_xiaoliang_nor);
                tvHot.setTextColor(getResources().getColor(R.color.black));
                tvNewest.setTextColor(getResources().getColor(R.color.black));
                tvPrice.setTextColor(getResources().getColor(R.color.blue));
                priceSort = "0";
                hotSort = "";
                timeSort = "";
                currentPage = 1;
                loadMore = false;
                getData();
                break;
            case "0": // 从低到高->从高到低
                imgPrice.setImageResource(R.mipmap.icon_xiaoliang_gaodaodi);
                imgNewest.setImageResource(R.mipmap.icon_xiaoliang_nor);
                tvHot.setTextColor(getResources().getColor(R.color.black));
                tvNewest.setTextColor(getResources().getColor(R.color.black));
                tvPrice.setTextColor(getResources().getColor(R.color.blue));
                priceSort = "1";
                hotSort = "";
                timeSort = "";
                currentPage = 1;
                loadMore = false;
                getData();
                break;
//            case "1": // 从高到低->灰色
//                imgPrice.setImageResource(R.mipmap.icon_xiaoliang_nor);
//                tvHot.setTextColor(getResources().getColor(R.color.black));
//                tvNewest.setTextColor(getResources().getColor(R.color.black));
//                tvPrice.setTextColor(getResources().getColor(R.color.black));
//                priceSort = "";
//                currentPage = 1;
//                loadMore = false;
//                getData();
//                break;
            case "1": // 灰色->从低到高
                imgPrice.setImageResource(R.mipmap.icon_xiaoliang_didaogao);
                imgNewest.setImageResource(R.mipmap.icon_xiaoliang_nor);
                tvHot.setTextColor(getResources().getColor(R.color.black));
                tvNewest.setTextColor(getResources().getColor(R.color.black));
                tvPrice.setTextColor(getResources().getColor(R.color.blue));
                priceSort = "0";
                hotSort = "";
                timeSort = "";
                currentPage = 1;
                loadMore = false;
                getData();
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
            getData();
            refreshLayout.setRefreshing(false);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            currentPage += 1;
            loadMore = true;
            /**
             * 上拉加载首页数据
             */
            getData();
            refreshLayout.setRefreshing(false);
        }
    }

    /**
     * 点击异常情况界面，重新调用接口刷新数据
     */
    @Override
    public void clickEvent() {
        super.clickEvent();
        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
