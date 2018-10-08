package app.cn.extra.mall.merchant.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.activity.RequirementDetailsActivity;
import app.cn.extra.mall.merchant.event.IndexFragmentEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.GlideCircleTransformWithBorder;
import app.cn.extra.mall.merchant.utils.NotificationsUtils;
import app.cn.extra.mall.merchant.utils.RequirementOrderStatus;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.CompanyStoreMainInterface;
import app.cn.extra.mall.merchant.vo.GetAllRequirement;
import app.cn.extra.mall.merchant.vo.PersonStoreMainInterface;
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
 * Description 首页Fragment
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class IndexFragment extends BaseFragment implements SwipyRefreshLayout.OnRefreshListener {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    Unbinder unbinder;

    private View mView;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
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
    private AbortableFuture<LoginInfo> loginRequest;
    private Activity activity;
    private int tag = 0;

    public static IndexFragment newInstance(String param1) {
        IndexFragment fragment = new IndexFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_index, container, false);
        unbinder = ButterKnife.bind(this, mView);
        if (activity == null) {
            activity = getActivity();
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
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
    public void IndexFragmentEventBus(IndexFragmentEvent event) {
        getData();
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
        currentPage = 1;
        loadMore = false;
        getData();
        /**
         * 通知悬浮设置提示框
         * 0显示 1不显示
         */
        if (sharePreferenceUtil.getNotificatonPermissionTag() == 0) {
            showNotificationPermissionDialog();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (1 == tag) {
            getData();
        }
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
        /**
         * 添加分隔线
         */
        recyclerView.addItemDecoration(new DividerItemDecoration(activity,
                DividerItemDecoration.VERTICAL));
    }

    /**
     * 获取个人商户主界面相关数据接口
     */
    private void personStoreMainInterfacePort() {
        if (Utils.isNetworkAvailable(activity)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.personStoreMainInterface)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
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
                            PersonStoreMainInterface personStoreMainInterface
                                    = new PersonStoreMainInterface();
                            personStoreMainInterface = Utils.parserJsonResult(response,
                                    PersonStoreMainInterface.class);
                            if (Constants.OK.equals(personStoreMainInterface.getFlag())) {
                                if (Constants.OK.equals(personStoreMainInterface.getData().getStatus())) {
                                    setPersonalRecyclerViewHeaderAndFooter(personStoreMainInterface.getData());
                                } else {
                                    varyViewHelperController.showErrorView();
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
     * 获取企业商户主界面相关数据接口
     */
    private void companyStoreMainInterfacePort() {
        if (Utils.isNetworkAvailable(activity)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.companyStoreMainInterface)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
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
                            CompanyStoreMainInterface companyStoreMainInterface
                                    = new CompanyStoreMainInterface();
                            companyStoreMainInterface = Utils.parserJsonResult(response,
                                    CompanyStoreMainInterface.class);
                            if (Constants.OK.equals(companyStoreMainInterface.getFlag())) {
                                if (Constants.OK.equals(companyStoreMainInterface.getData().getStatus())) {
                                    setCompanyRecyclerViewHeaderAndFooter(companyStoreMainInterface.getData());
                                } else {
                                    varyViewHelperController.showErrorView();
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
                    .addParams("hotSort", "0")
                    .addParams("timeSort", "")
                    .addParams("priceSort", "")
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
                                    }

                                } else {
                                    hotRequirementList = getAllRequirement.getData();
                                    setHotRequirementData();
                                }
                            } else {
                                varyViewHelperController.showErrorView();
                            }
                            tag = 1;
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
        if (Constants.M_PERSONAL.equals(sharePreferenceUtil.getSType())) {
            personStoreMainInterfacePort();
        } else if (Constants.M_COMPANY.equals(sharePreferenceUtil.getSType())) {
            companyStoreMainInterfacePort();
        }
        adapter = new CommonAdapter<GetAllRequirement.DataBean>(
                activity, R.layout.list_item_requirement, hotRequirementList) {
            @Override
            protected void convert(ViewHolder holder,
                                   GetAllRequirement.DataBean
                                           requirementBean, int position) {
                holder.setText(R.id.tv_requirement_type,
                        requirementBean.getRtName());
                for (RequirementOrderStatus info : RequirementOrderStatus.values()) {
                    if (requirementBean.getRoStatus() == info.getId()) {
                        holder.setText(R.id.tv_requirement_order_status,
                                info.getName());
                    }
                }
                holder.setText(R.id.tv_requirement_title,
                        getResources().getString(R.string.requirementTitle)
                                + requirementBean.getUrTitle());
                holder.setText(R.id.tv_requirement_content,
                        getResources().getString(R.string.requirementContent)
                                + requirementBean.getUrContent());
                holder.setText(R.id.tv_requirement_create_time,
                        getResources().getString(R.string.requirementCreateTime)
                                + requirementBean.getUrCreateTime());
                // 需商家报价
                if ("1".equals(requirementBean.getUrOfferType())) {
                    holder.setText(R.id.tv_requirement_money,
                            getResources().getString(R.string.requirementMoney)
                                    + "需商家报价");
                } else {
                    holder.setText(R.id.tv_requirement_money,
                            getResources().getString(R.string.requirementMoney)
                                    + "¥" + requirementBean.getUrOfferPrice());
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
                intent.putExtra("roStatus", "" + hotRequirementList.get(position - 1).getRoStatus());
                intent.putExtra("roId", hotRequirementList.get(position - 1).getRoId());
                intent.putExtra("urId", hotRequirementList.get(position - 1).getUrId());
                intent.putExtra("applyStatus", hotRequirementList.get(position - 1).getApplyStatus());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 设置个人商户RecyclerViewHeaderAndFooter
     */
    private void setPersonalRecyclerViewHeaderAndFooter(PersonStoreMainInterface.DataBean data) {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        /**
         * 设置RecyclerViewHeader
         */
        View view = LayoutInflater.from(activity).inflate(
                R.layout.index_head_view_personal, null);
        /**
         * 向HeaderView中填充数据
         */
        RelativeLayout rl_header = view.findViewById(R.id.rl_header);
        ImageView img_header = view.findViewById(R.id.img_header);
        TextView tv_sname = view.findViewById(R.id.tv_sname);
        TextView tv_nick_name = view.findViewById(R.id.tv_nick_name);
        TextView tv_completeOrderNum = view.findViewById(R.id.tv_completeOrderNum);
        TextView tv_unConfirmOrderNum = view.findViewById(R.id.tv_unConfirmOrderNum);
        TextView tv_doingOrderNum = view.findViewById(R.id.tv_doingOrderNum);
        TextView tv_yestodayCompleteMoney = view.findViewById(R.id.tv_yestodayCompleteMoney);
        TextView tv_todayCompleteMoney = view.findViewById(R.id.tv_todayCompleteMoney);
        TextView tv_totalCompleteMoney = view.findViewById(R.id.tv_totalCompleteMoney);
        TextView tv_myArrangeNum = view.findViewById(R.id.tv_myArrangeNum);
        TextView tv_sLevel = view.findViewById(R.id.tv_sLevel);
        TextView tv_firstRtNameOfMoney = view.findViewById(R.id.tv_firstRtNameOfMoney);
        TextView tv_firstOfRequirementPrice = view.findViewById(R.id.tv_firstOfRequirementPrice);
        TextView tv_firstRoNum = view.findViewById(R.id.tv_firstRoNum);
        rl_header.setBackgroundColor(getResources().getColor(R.color.blue));
        if (!TextUtils.isEmpty(data.getUrl())) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .fallback(R.mipmap.profile)
                    .transform(new GlideCircleTransformWithBorder(activity, 2,
                            activity.getResources().getColor(R.color.blue)));
            Glide.with(activity).load(data.getUrl() + "?x-oss-process=image/resize,h_150")
                    .apply(requestOptions)
                    .into(img_header);
        }
        tv_sname.setText(data.getsName());
        tv_nick_name.setText(data.getUNickName());
        tv_completeOrderNum.setText(data.getCompleteOrderNum() + "");
        tv_unConfirmOrderNum.setText(data.getUnConfirmOrderNum() + "");
        tv_doingOrderNum.setText(data.getDoingOrderNum() + "");
        tv_yestodayCompleteMoney.setText(data.getYestodayCompleteMoney() + "");
        tv_todayCompleteMoney.setText(data.getTodayCompleteMoney() + "");
        tv_totalCompleteMoney.setText(data.getTotalCompleteMoney() + "");
        tv_myArrangeNum.setText(data.getMyArrangeNum() + "");
        tv_sLevel.setText(data.getSLevel() + "");
        tv_firstRtNameOfMoney.setText(data.getFirstRtNameOfMoney());
        tv_firstOfRequirementPrice.setText(data.getFirstOfRequirementPrice());
        tv_firstRoNum.setText(data.getFirstRoNum());
        mHeaderAndFooterWrapper.addHeaderView(view);
        recyclerView.setAdapter(mHeaderAndFooterWrapper);
    }

    /**
     * 设置企业商户RecyclerViewHeaderAndFooter
     */
    private void setCompanyRecyclerViewHeaderAndFooter(CompanyStoreMainInterface.DataBean data) {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        /**
         * 设置RecyclerViewHeader
         */
        View view = LayoutInflater.from(activity).inflate(
                R.layout.index_head_view_company, null);
        /**
         * 向HeaderView中填充数据
         */
        RelativeLayout rl_header = view.findViewById(R.id.rl_header);
        ImageView img_header = view.findViewById(R.id.img_header);
        TextView tv_sname = view.findViewById(R.id.tv_sname);
        TextView tv_nick_name = view.findViewById(R.id.tv_nick_name);
        TextView tv_todayProductOrderNum = view.findViewById(R.id.tv_todayProductOrderNum);
        TextView tv_unReundProductOrderNum = view.findViewById(R.id.tv_unReundProductOrderNum);
        TextView tv_unPayProductOrderNum = view.findViewById(R.id.tv_unPayProductOrderNum);
        TextView tv_unDeliverProductOrderNum = view.findViewById(R.id.tv_unDeliverProductOrderNum);
        TextView tv_completeProductOrderNum = view.findViewById(R.id.tv_completeProductOrderNum);
        TextView tv_yestodayProductOrderMoney = view.findViewById(R.id.tv_yestodayProductOrderMoney);
        TextView tv_todayProductOrderMoney = view.findViewById(R.id.tv_todayProductOrderMoney);
        TextView tv_productOrderMoney = view.findViewById(R.id.tv_productOrderMoney);
        TextView tv_completeOrderNum = view.findViewById(R.id.tv_completeOrderNum);
        TextView tv_unConfirmOrderNum = view.findViewById(R.id.tv_unConfirmOrderNum);
        TextView tv_doingOrderNum = view.findViewById(R.id.tv_doingOrderNum);
        TextView tv_yestodayCompleteMoney = view.findViewById(R.id.tv_yestodayCompleteMoney);
        TextView tv_todayCompleteMoney = view.findViewById(R.id.tv_todayCompleteMoney);
        TextView tv_totalCompleteMoney = view.findViewById(R.id.tv_totalCompleteMoney);
        TextView tv_myArrangeNum = view.findViewById(R.id.tv_myArrangeNum);
        TextView tv_sLevel = view.findViewById(R.id.tv_sLevel);
        TextView tv_firstRtNameOfMoney = view.findViewById(R.id.tv_firstRtNameOfMoney);
        TextView tv_firstOfRequirementPrice = view.findViewById(R.id.tv_firstOfRequirementPrice);
        TextView tv_firstRoNum = view.findViewById(R.id.tv_firstRoNum);
        rl_header.setBackgroundColor(getResources().getColor(R.color.blue));
        if (!TextUtils.isEmpty(data.getUrl())) {
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .fallback(R.mipmap.profile)
                    .transform(new GlideCircleTransformWithBorder(activity, 2,
                            activity.getResources().getColor(R.color.white)));
            Glide.with(activity).load(data.getUrl() + "?x-oss-process=image/resize,h_150")
                    .apply(requestOptions)
                    .into(img_header);
        }
        tv_sname.setText(data.getSName());
        tv_nick_name.setText(data.getUNickName());
        tv_todayProductOrderNum.setText(data.getTodayProductOrderNum() + "");
        tv_unReundProductOrderNum.setText(data.getUnReundProductOrderNum() + "");
        tv_unPayProductOrderNum.setText(data.getUnPayProductOrderNum() + "");
        tv_unDeliverProductOrderNum.setText(data.getUnDeliverProductOrderNum() + "");
        tv_completeProductOrderNum.setText(data.getCompleteProductOrderNum() + "");
        tv_yestodayProductOrderMoney.setText(data.getYestodayProductOrderMoney() + "");
        tv_todayProductOrderMoney.setText(data.getTodayProductOrderMoney() + "");
        tv_productOrderMoney.setText(data.getProductOrderMoney() + "");
        tv_completeOrderNum.setText(data.getCompleteOrderNum() + "");
        tv_unConfirmOrderNum.setText(data.getUnConfirmOrderNum() + "");
        tv_doingOrderNum.setText(data.getDoingOrderNum() + "");
        tv_yestodayCompleteMoney.setText(data.getYestodayCompleteMoney() + "");
        tv_todayCompleteMoney.setText(data.getTodayCompleteMoney() + "");
        tv_totalCompleteMoney.setText(data.getTotalCompleteMoney() + "");
        tv_myArrangeNum.setText(data.getMyArrangeNum() + "");
        tv_sLevel.setText(data.getSLevel() + "");
        tv_firstRtNameOfMoney.setText(data.getFirstRtNameOfMoney());
        if (16 < data.getFirstOfRequirementPrice().length()) {
            tv_firstOfRequirementPrice.setText(data.getFirstOfRequirementPrice()
                    .substring(0, 14) + "···");
        } else {
            tv_firstOfRequirementPrice.setText(data.getFirstOfRequirementPrice());
        }

        tv_firstRoNum.setText(data.getFirstRoNum());
        mHeaderAndFooterWrapper.addHeaderView(view);
        recyclerView.setAdapter(mHeaderAndFooterWrapper);
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

    /**
     * 通知栏权限已打开时不处理，未打开时显示是否去设置弹窗
     */
    private void setNotificationPermission() {
        if (NotificationsUtils.isNotificationEnabled(activity)) {
            if (sharePreferenceUtil.getNotificatonPermissionTag() == 0) {
                showNotificationPermissionDialog();
            }
        }
    }

    /**
     * 通知栏权限跳转弹窗
     */
    private void showNotificationPermissionDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(activity, R.style.Theme_Transparent).
                setView(view).
                setCancelable(false).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        tv_live_yes.setText("不再提醒");
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        tv__live_never_reminds.setText("我知道了");
        TextView tv_live_content = view.findViewById(R.id.tv_live_content);
        tv_live_content.setText("为了您更好的使用体验，请前往\"设置\"--\"应用管理\"进行应用通知设置。");

        tv__live_never_reminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePreferenceUtil.setNotificatonPermissionTag(0);
                alertDialog.dismiss();
            }
        });
        tv_live_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharePreferenceUtil.setNotificatonPermissionTag(1);
                alertDialog.dismiss();
            }
        });

    }
}
