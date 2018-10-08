package app.cn.extra.mall.merchant.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.Wave;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.GetAllAlreadyCashRecord;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import cfkj.app.cn.cfkjcommonlib.view.ReplaceViewHelper.VaryViewHelperController;
import okhttp3.Call;

/**
 * 提现记录
 */
public class PresentRecordActivity extends BaseActivty implements SwipyRefreshLayout.OnRefreshListener {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    private SharePreferenceUtil sharePreferenceUtil;
    private VaryViewHelperController varyViewHelperController;
    private CommonAdapter<GetAllAlreadyCashRecord.DataBean.UserToCashRecordListBean> adapter;
    private List<GetAllAlreadyCashRecord.DataBean.UserToCashRecordListBean> userToCashRecordListBeanList = null;
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
        setContentView(R.layout.activity_present_record);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(PresentRecordActivity.this,
                Constants.SAVE_USER);
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
        getAllAlreadyCashRecord();
    }

    @Override
    public void clickEvent() {
        super.clickEvent();
        getAllAlreadyCashRecord();
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
         * 网格布局
         */
        LinearLayoutManager layoutManage = new LinearLayoutManager(PresentRecordActivity.this);
        recyclerView.setLayoutManager(layoutManage);
//        /**
//         * 添加分隔线
//         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(PresentRecordActivity.this,
//                DividerItemDecoration.VERTICAL));
    }

    /**
     * 获取提现记录列表接口
     */
    private void getAllAlreadyCashRecord() {
        if (Utils.isNetworkAvailable(PresentRecordActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getAllAlreadyCashRecord)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
                    .addParams("size", Constants.PAGE_SIZE + "")
                    .addParams("pages", currentPage + "")
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
                            GetAllAlreadyCashRecord getAllAlreadyCashRecord = new GetAllAlreadyCashRecord();

                            getAllAlreadyCashRecord = Utils.parserJsonResult(response,
                                    GetAllAlreadyCashRecord.class);
                            if (Constants.OK.equals(getAllAlreadyCashRecord.getFlag())) {
                                if (loadMore) {
                                    if (null != getAllAlreadyCashRecord.getData()
                                            && 0 < getAllAlreadyCashRecord.getData().getUserToCashRecordList().size()) {
                                        for (int i = 0; i < getAllAlreadyCashRecord.getData().getUserToCashRecordList().size(); i++) {
                                            userToCashRecordListBeanList.add(getAllAlreadyCashRecord.getData().getUserToCashRecordList().get(i));
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        showLongToast(PresentRecordActivity.this,
                                                "没有更多提现记录了");
                                        currentPage = 1;
                                    }

                                } else {
                                    if (null != getAllAlreadyCashRecord.getData()
                                            && 0 < getAllAlreadyCashRecord.getData().getUserToCashRecordList().size()) {
                                        userToCashRecordListBeanList = getAllAlreadyCashRecord.getData().getUserToCashRecordList();
                                        setMyArrangeData();
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
            varyViewHelperController.showNetworkErrorView();
        }
    }

    /**
     * 填充提现记录数据
     */
    private void setMyArrangeData() {
        adapter = new CommonAdapter<GetAllAlreadyCashRecord.DataBean.UserToCashRecordListBean>(
                PresentRecordActivity.this, R.layout.list_item_present_record, userToCashRecordListBeanList) {
            @Override
            protected void convert(ViewHolder holder,
                                   GetAllAlreadyCashRecord.DataBean.UserToCashRecordListBean
                                           dataBean, int position) {
                holder.setText(R.id.tv_title, dataBean.getPsName() + "-" + dataBean.getUtcrAccount());
                holder.setText(R.id.tv_time, dataBean.getUtcrCreateTime());
                holder.setText(R.id.tv_money, "" + dataBean.getUtcrMoney());
            }
        };
        /**
         * 提现记录点击事件
         */
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                Intent intent = new Intent(PresentRecordActivity.this, MyArrangeDetailActivity.class);
//                intent.putExtra("saId", userToCashRecordListBeanList.get(position).getSaId());
//                intent.putExtra("saCreateTime", userToCashRecordListBeanList.get(position).getSaCreateTime());
//                intent.putExtra("saContent", userToCashRecordListBeanList.get(position).getSaContent());
//                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
    }


    @OnClick({R.id.img_back})
    public void onViewClicked(View view) {
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
            getAllAlreadyCashRecord();
            refreshLayout.setRefreshing(false);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            currentPage += 1;
            loadMore = true;
            /**
             * 上拉加载首页数据
             */
            getAllAlreadyCashRecord();
            refreshLayout.setRefreshing(false);
        }
    }
}
