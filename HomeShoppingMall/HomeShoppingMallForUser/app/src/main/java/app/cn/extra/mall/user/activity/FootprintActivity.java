package app.cn.extra.mall.user.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import app.cn.extra.mall.user.vo.DelFootPrint;
import app.cn.extra.mall.user.vo.GetUserFootPrint;
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
 * 足迹
 */
public class FootprintActivity extends BaseActivty implements SwipyRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    Unbinder unbinder;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_clean_up)
    TextView tvCleanUp;
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
     * 用于存放临时推荐商品数据
     */
    List<GetUserFootPrint.DataBean.UserFootprintListBean> footprintListBeanList = null;
    String idList = "";
    /**
     * 0单条，1多条
     */
    int clickTag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footprint);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(FootprintActivity.this,
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
        getUserFootPrint();
    }

    /**
     * 获取足迹列表数据
     */
    private void getUserFootPrint() {
        if (Utils.isNetworkAvailable(FootprintActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getUserFootPrint)
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
                            GetUserFootPrint getUserFootPrint = new GetUserFootPrint();
                            getUserFootPrint = Utils.parserJsonResult(response,
                                    GetUserFootPrint.class);
                            if (Constants.OK.equals(getUserFootPrint.getFlag())) {
                                if (loadMore) {
                                    if (null != getUserFootPrint.getData()
                                            .getUserFootprintList()
                                            && 0 < getUserFootPrint.getData()
                                            .getUserFootprintList().size()) {
                                        for (int i = 0; i < getUserFootPrint.getData()
                                                .getUserFootprintList().size(); i++) {
                                            footprintListBeanList.add(getUserFootPrint.getData()
                                                    .getUserFootprintList().get(i));
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        showLongToast(FootprintActivity.this,
                                                "没有更多足迹了！");
//                                        currentPage = 1;
                                    }

                                } else {
                                    if (null != getUserFootPrint.getData()
                                            .getUserFootprintList()
                                            && 0 < getUserFootPrint.getData()
                                            .getUserFootprintList().size()) {
                                        footprintListBeanList = getUserFootPrint.getData()
                                                .getUserFootprintList();
                                        setUserFootprintData();
                                    } else {
                                        varyViewHelperController.showEmptyView();
                                        if (footprintListBeanList != null) {
                                            footprintListBeanList.clear();
                                        }
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
     * 填充足迹数据
     */
    private void setUserFootprintData() {
        adapter = new MyRecyclerViewAdapter(FootprintActivity.this, footprintListBeanList);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 列表adapter
     */
    private class MyRecyclerViewAdapter extends RecyclerSwipeAdapter<MyRecyclerViewAdapter.MyViewHolder> {
        private Context context;
        private List<GetUserFootPrint.DataBean.UserFootprintListBean> list;

        public MyRecyclerViewAdapter(Context context, List<GetUserFootPrint.DataBean.UserFootprintListBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_foot_print, parent, false);
            return new MyRecyclerViewAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyRecyclerViewAdapter.MyViewHolder viewHolder, final int position) {
//            viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            RequestOptions requestOptions = new RequestOptions()
                    .error(R.drawable.ic_exception)
                    .fallback(R.drawable.ic_exception);
            Glide.with(FootprintActivity.this)
                    .load(list.get(position).getPicName() + "?x-oss-process=image/resize,w_"
                            + (Utils.getScreenWidth(FootprintActivity.this)))
                    .apply(requestOptions)
                    .into(viewHolder.iv_pic);
            if (!TextUtils.isEmpty(list.get(position).getPName())) {
//                if (12 < list.get(position).getPName().length()) {
//                    viewHolder.tv_name.setText(list.get(position).getPName()
//                            .substring(0, 10) + "···");
//                } else {
                viewHolder.tv_name.setText(list.get(position).getPName());
//                }
            } else {
                viewHolder.tv_name.setText("");
            }
            viewHolder.tv_money.setText("¥" + list.get(position).getPNowPrice() + "");
            viewHolder.tv_old_money.setText("¥" + list.get(position).getPOriginalPrice() + "");
            viewHolder.tv_old_money.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            if (!TextUtils.isEmpty(list.get(position).getPDescribe())) {
//                if (12 < list.get(position).getPDescribe().length()) {
//                    viewHolder.tv_content.setText(list.get(position).getPDescribe()
//                            .substring(0, 10) + "···");
//                } else {
                viewHolder.tv_content.setText(
                        list.get(position).getPDescribe());
//                }
            } else {
                viewHolder.tv_content.setText("");
            }
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
                    clickTag = 0;
                    showRemoveDialog(list.get(position).getUfId(), viewHolder.swipeLayout);
                }
            });
            viewHolder.rl_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(FootprintActivity.this, GoodsDetailActivity.class);
                    intent.putExtra("pid", list.get(position).getPId());
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
            private TextView tv_name, tv_money, tv_old_money, tv_content;
            private ImageView iv_pic, iv_more;
            private RelativeLayout rl_more;
            private RelativeLayout rl_layout;

            public MyViewHolder(View itemView) {
                super(itemView);
                swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
                ll_cancle = (LinearLayout) itemView.findViewById(R.id.ll_cancle);
                tv_name = (TextView) itemView.findViewById(R.id.tv_name);
                tv_money = (TextView) itemView.findViewById(R.id.tv_money);
                tv_old_money = (TextView) itemView.findViewById(R.id.tv_old_money);
                tv_content = (TextView) itemView.findViewById(R.id.tv_content);
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
     * @param id 足迹店铺id
     * @param sl swipelayout
     */
    private void showRemoveDialog(final String id, final SwipeLayout sl) {
        LayoutInflater layoutInflater = LayoutInflater.from(FootprintActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(FootprintActivity.this, R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        TextView tv_live_content = view.findViewById(R.id.tv_live_content);
        tv_live_content.setText("确定要删除这条足迹吗？");

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
                delUserFootPrint(id, sl, alertDialog);
            }
        });

    }

    /**
     * 删除足迹
     *
     * @param ids         足迹店铺id
     * @param swipeLayout swipeLayout
     * @param alertDialog alertDialog
     */
    private void delUserFootPrint(String ids, final SwipeLayout swipeLayout, final Dialog alertDialog) {
        if (Utils.isNetworkAvailable(FootprintActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.delUserFootPrint)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("ufIdList", ids)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
//                            varyViewHelperController.showErrorView();
                            setCleanUpClickable(true);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            setCleanUpClickable(true);
                            /**
                             * 恢复显示数据的View
                             */
//                            varyViewHelperController.restore();
                            DelFootPrint delFootPrint = new DelFootPrint();
                            delFootPrint = Utils.parserJsonResult(response,
                                    DelFootPrint.class);
                            if (Constants.OK.equals(delFootPrint.getFlag())) {
                                if (Constants.OK.equals(delFootPrint.getData().getStatus())) {
                                    loadMore = false;
                                    currentPage = 1;
                                    getUserFootPrint();
//                                adapter.notifyDataSetChanged();
                                    if (swipeLayout != null) {
                                        swipeLayout.close();
                                    }
                                    if (clickTag == 1) {
                                        showLongToast(getApplicationContext(), "清空成功！");
                                    } else {
                                        showLongToast(FootprintActivity.this, "删除成功！");
                                    }
                                    alertDialog.dismiss();
                                    EventBus.getDefault().post(new MineFragmentEvent());
                                }
                            } else {
                                showLongToast(FootprintActivity.this, delFootPrint.getErrorString());
//                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
            if (clickTag == 1) {
                Toast.makeText(getApplicationContext(), "清空失败！" + Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "删除失败！" + Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
            }
            setCleanUpClickable(true);
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 网格布局
         */
        LinearLayoutManager layoutManage = new LinearLayoutManager(FootprintActivity.this);
        recyclerView.setLayoutManager(layoutManage);
//        /**
//         * 添加分隔线
//         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(FootprintActivity.this,
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
        getUserFootPrint();
    }

    @OnClick({R.id.img_back, R.id.tv_clean_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_clean_up:
                setCleanUpClickable(false);
                if (footprintListBeanList != null && footprintListBeanList.size() > 0) {
                    for (int i = 0; i < footprintListBeanList.size(); i++) {
                        if (i == 0) {
                            clickTag = 0;
                            idList = footprintListBeanList.get(i).getUfId();
                        } else {
                            clickTag = 1;
                            idList = idList + "," + footprintListBeanList.get(i).getUfId();
                        }
                    }
                    showAllRemoveDialog(idList);
                } else {
                    showLongToast(getApplicationContext(), "暂无足迹！");
                    setCleanUpClickable(true);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 是否清空弹窗
     *
     * @param id 足迹店铺id
     */
    private void showAllRemoveDialog(final String id) {
        LayoutInflater layoutInflater = LayoutInflater.from(FootprintActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(FootprintActivity.this, R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        TextView tv_live_content = view.findViewById(R.id.tv_live_content);
        tv_live_content.setText("确定要清空足迹吗？");

        tv__live_never_reminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                setCleanUpClickable(true);
            }
        });
        tv_live_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除信息
                delUserFootPrint(id, null, alertDialog);
            }
        });

    }

    /**
     * 设置清空按钮是否可以点击
     *
     * @param b b=true可以点击;b=false不可点击
     */
    private void setCleanUpClickable(boolean b) {
        if (b) {
            tvCleanUp.setClickable(true);
        } else {
            tvCleanUp.setClickable(false);
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
            getUserFootPrint();
            refreshLayout.setRefreshing(false);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            currentPage += 1;
            loadMore = true;
            /**
             * 上拉加载首页数据
             */
            getUserFootPrint();
            refreshLayout.setRefreshing(false);
        }
    }
}
