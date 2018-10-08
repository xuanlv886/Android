package app.cn.extra.mall.user.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.github.ybq.android.spinkit.style.Wave;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.MineFragmentEvent;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.DelUserFollow;
import app.cn.extra.mall.user.vo.GetUserFollow;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import cfkj.app.cn.cfkjcommonlib.view.ReplaceViewHelper.VaryViewHelperController;
import okhttp3.Call;

/**
 * 关注
 */
public class FollowActivity extends BaseActivty implements SwipyRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    Unbinder unbinder;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private SharePreferenceUtil sharePreferenceUtil;
    private VaryViewHelperController varyViewHelperController;
    private MyRecyclerViewAdapter adapter;
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
    List<GetUserFollow.DataBean.UserAttentionListBean> userAttentionListBeanList = null;
    int getUserAttentionTag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(FollowActivity.this,
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
        getUserAttention();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getUserAttentionTag == 1) {
            currentPage = 1;
            loadMore = false;
            getUserAttention();
        }
    }

    /**
     * 获取关注列表数据
     */
    private void getUserAttention() {
        if (Utils.isNetworkAvailable(FollowActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getUserAttention)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("page", currentPage + "")
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
                            GetUserFollow getUserFollow = new GetUserFollow();
                            getUserFollow = Utils.parserJsonResult(response,
                                    GetUserFollow.class);
                            if (Constants.OK.equals(getUserFollow.getFlag())) {
                                getUserAttentionTag = 1;
                                if (loadMore) {
                                    if (null != getUserFollow.getData()
                                            .getUserAttentionList()
                                            && 0 < getUserFollow.getData()
                                            .getUserAttentionList().size()) {
                                        for (int i = 0; i < getUserFollow.getData()
                                                .getUserAttentionList().size(); i++) {
                                            userAttentionListBeanList.add(getUserFollow.getData()
                                                    .getUserAttentionList().get(i));
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        showLongToast(FollowActivity.this,
                                                "没有更多关注了！");
//                                        currentPage = 1;
                                    }

                                } else {
                                    if (null != getUserFollow.getData()
                                            .getUserAttentionList()
                                            && 0 < getUserFollow.getData()
                                            .getUserAttentionList().size()) {
                                        userAttentionListBeanList = getUserFollow.getData()
                                                .getUserAttentionList();
                                        setUserAttentionData();
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
     * 填充关注数据
     */
    private void setUserAttentionData() {
        adapter = new MyRecyclerViewAdapter(FollowActivity.this, userAttentionListBeanList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 列表adapter
     */
    private class MyRecyclerViewAdapter extends RecyclerSwipeAdapter<MyRecyclerViewAdapter.MyViewHolder> {
        private Context context;
        private List<GetUserFollow.DataBean.UserAttentionListBean> list;

        public MyRecyclerViewAdapter(Context context, List<GetUserFollow.DataBean.UserAttentionListBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_follow, parent, false);
            return new MyRecyclerViewAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyRecyclerViewAdapter.MyViewHolder viewHolder, final int position) {
//            viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            RequestOptions requestOptions = new RequestOptions()
                    .error(R.drawable.ic_exception)
                    .fallback(R.drawable.ic_exception);
            Glide.with(FollowActivity.this)
                    .load(list.get(position).getPName() + "?x-oss-process=image/resize,w_"
                            + (Utils.getScreenWidth(FollowActivity.this)))
                    .apply(requestOptions)
                    .into(viewHolder.iv_pic);
            if (!TextUtils.isEmpty(list.get(position).getSName())) {
//                if (12 < list.get(position).getSName().length()) {
//                    viewHolder.tv_name.setText(list.get(position).getSName()
//                            .substring(0, 10) + "···");
//                } else {
                viewHolder.tv_name.setText(list.get(position).getSName());
//                }
            } else {
                viewHolder.tv_name.setText("");
            }
            viewHolder.tv_evaluate.setText("商户等级：" + list.get(position).getSLeve());
            viewHolder.rl_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SwipeLayout.Status.Open == viewHolder.swipeLayout.getOpenStatus()) {
                        viewHolder.swipeLayout.close();
                    } else if (SwipeLayout.Status.Close == viewHolder.swipeLayout.getOpenStatus()) {
                        viewHolder.swipeLayout.open();
                    }
                }
            });
            viewHolder.ll_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showRemoveDialog(list.get(position).getSId(), viewHolder.swipeLayout);
                }
            });
            viewHolder.rl_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getUserAttentionTag = 0;
                    Intent intent = new Intent(FollowActivity.this, ShopActivity.class);
                    intent.putExtra("sId", list.get(position).getSId());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return position;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private SwipeLayout swipeLayout;
            private LinearLayout ll_cancle;
            private TextView tv_name, tv_evaluate;
            private ImageView iv_pic, iv_more;
            private RelativeLayout rl_more;
            private RelativeLayout rl_layout;

            public MyViewHolder(View itemView) {
                super(itemView);
                swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
                ll_cancle = (LinearLayout) itemView.findViewById(R.id.ll_cancle);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                tv_evaluate = (TextView) itemView.findViewById(R.id.tv_evaluate);
                iv_more = (ImageView) itemView.findViewById(R.id.iv_more);
                iv_pic = (ImageView) itemView.findViewById(R.id.iv_pic);
                rl_more = (RelativeLayout) itemView.findViewById(R.id.rl_more);
                rl_layout = (RelativeLayout) itemView.findViewById(R.id.rl_layout);
            }
        }
    }

    /**
     * 是否删除弹窗
     *
     * @param id 关注店铺id
     * @param sl swipelayout
     */
    private void showRemoveDialog(final String id, final SwipeLayout sl) {
        LayoutInflater layoutInflater = LayoutInflater.from(FollowActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(FollowActivity.this, R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        TextView tv_live_content = view.findViewById(R.id.tv_live_content);
        tv_live_content.setText("确定要删除这条关注吗？");

        tv__live_never_reminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tv_live_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除信息
                delUserAttention(id, sl, alertDialog);
            }
        });

    }

    /**
     * 删除关注
     *
     * @param id          店铺id
     * @param swipeLayout swipeLayout
     * @param alertDialog alertDialog
     */
    private void delUserAttention(String id, final SwipeLayout swipeLayout, final Dialog alertDialog) {
        if (Utils.isNetworkAvailable(FollowActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.delUserAttention)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", id)
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
                            /**
                             * 恢复显示数据的View
                             */
//                            varyViewHelperController.restore();
                            DelUserFollow delUserFollow = new DelUserFollow();
                            delUserFollow = Utils.parserJsonResult(response,
                                    DelUserFollow.class);
                            if (Constants.OK.equals(delUserFollow.getFlag())) {
                                if (Constants.OK.equals(delUserFollow.getData().getStatus())) {
                                    loadMore = false;
                                    currentPage = 1;
                                    getUserAttention();
//                                adapter.notifyDataSetChanged();
                                    if (swipeLayout != null) {
                                        swipeLayout.close();
                                    }
                                    showLongToast(FollowActivity.this, "取消关注成功！");
                                    alertDialog.dismiss();
                                    EventBus.getDefault().post(new MineFragmentEvent());
                                }
                            } else {
                                showLongToast(FollowActivity.this, delUserFollow.getErrorString());
//                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
//            varyViewHelperController.showNetworkPoorView();
            Toast.makeText(getApplicationContext(), "取消关注失败！" + Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 网格布局
         */
        LinearLayoutManager layoutManage = new LinearLayoutManager(FollowActivity.this);
        recyclerView.setLayoutManager(layoutManage);
//        /**
//         * 添加分隔线
//         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(FollowActivity.this,
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
        getUserAttention();
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
            getUserAttention();
            refreshLayout.setRefreshing(false);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            currentPage += 1;
            loadMore = true;
            /**
             * 上拉加载首页数据
             */
            getUserAttention();
            refreshLayout.setRefreshing(false);
        }
    }
}
