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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.AddressEvent;
import app.cn.extra.mall.user.event.CommodityBuyEvent;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.DelUserDeliverAddress;
import app.cn.extra.mall.user.vo.GetUserDeliverAddress;
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
 * 地址管理
 */
public class AddressActivity extends BaseActivty implements SwipyRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.add_btn)
    Button add_btn;
    private SharePreferenceUtil sharePreferenceUtil;
    private CommonAdapter<GetUserDeliverAddress.DataBean.UserDeliverAddressListBean> adapter;
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
    List<GetUserDeliverAddress.DataBean.UserDeliverAddressListBean> userDeliverAddressListBeanList = null;
    private VaryViewHelperController varyViewHelperController;
    String addressTags = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        add_btn.setBackgroundColor(getResources().getColor(R.color.blue));
        Intent intent = getIntent();
        addressTags = intent.getStringExtra("addressTags");
        sharePreferenceUtil = new SharePreferenceUtil(AddressActivity.this,
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
        getUserDeliverAddress();
    }

    /**
     * 获取页面数据接口
     * p--当前页
     * ps--页大小
     */
    private void getUserDeliverAddress() {
        if (Utils.isNetworkAvailable(AddressActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getUserDeliverAddress)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("page", currentPage + "")
                    .addParams("pageSize", Constants.PAGE_SIZE + "")
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
                            GetUserDeliverAddress getUserDeliverAddress = new GetUserDeliverAddress();
                            getUserDeliverAddress = Utils.parserJsonResult(response,
                                    GetUserDeliverAddress.class);
                            if (Constants.OK.equals(getUserDeliverAddress.getFlag())) {
                                if (loadMore) {
                                    if (null != getUserDeliverAddress.getData()
                                            .getUserDeliverAddressList()
                                            && 0 < getUserDeliverAddress.getData()
                                            .getUserDeliverAddressList().size()) {
                                        for (int i = 0; i < getUserDeliverAddress.getData()
                                                .getUserDeliverAddressList().size(); i++) {
                                            userDeliverAddressListBeanList.add(getUserDeliverAddress.getData()
                                                    .getUserDeliverAddressList().get(i));
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        showLongToast(MyDemandActivity.context,
                                                "没有更多地址了！");
                                    }

                                } else {
                                    if (null != getUserDeliverAddress.getData()
                                            .getUserDeliverAddressList()
                                            && 0 < getUserDeliverAddress.getData()
                                            .getUserDeliverAddressList().size()) {
                                        userDeliverAddressListBeanList = getUserDeliverAddress.getData()
                                                .getUserDeliverAddressList();
                                        setData();
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
    private void setData() {
        adapter = new CommonAdapter<GetUserDeliverAddress.DataBean.UserDeliverAddressListBean>(
                AddressActivity.this, R.layout.list_item_address, userDeliverAddressListBeanList) {
            @Override
            protected void convert(ViewHolder holder, GetUserDeliverAddress.DataBean.UserDeliverAddressListBean userDeliverAddressListBean, int position) {
                if (!TextUtils.isEmpty(userDeliverAddressListBean.getUdaTrueName())) {
                    if (12 < userDeliverAddressListBean.getUdaTrueName().length()) {
                        holder.setText(R.id.tv_name,
                                userDeliverAddressListBean.getUdaTrueName()
                                        .substring(0, 10) + "···");
                    } else {
                        holder.setText(R.id.tv_name,
                                userDeliverAddressListBean.getUdaTrueName());
                    }
                } else {
                    holder.setText(R.id.tv_name, "");
                }
                holder.setText(R.id.tv_phone, userDeliverAddressListBean.getUdaTel());
                holder.setText(R.id.tv_address, userDeliverAddressListBean.getUdaAddress());
                /**
                 * 判断是否是默认地址
                 * 0不是 1是
                 */
                switch (userDeliverAddressListBean.getUdaDefault()) {
                    case 0:
                        holder.getView(R.id.iv_box).setBackgroundResource(R.mipmap.icon_box);
                        break;
                    case 1:
                        holder.getView(R.id.iv_box).setBackgroundResource(R.mipmap.icon_box2);
                        break;
                    default:
                        break;
                }
                holder.getView(R.id.rl_check).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setDefaultUserDeliverAddress(userDeliverAddressListBean.getUdaId());
                    }
                });
                holder.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showRemoveDialog(userDeliverAddressListBean.getUdaId());
                    }
                });
//                holder.getView(R.id.tv_edit).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(AddressActivity.this, EditAddressActivity.class);
//                        intent.putExtra("udaId", userDeliverAddressListBean.getUdaId());
//                        intent.putExtra("udaAddress", userDeliverAddressListBean.getUdaAddress());
//                        intent.putExtra("udaTel", userDeliverAddressListBean.getUdaTel());
//                        intent.putExtra("udaTrueName", userDeliverAddressListBean.getUdaTrueName());
//                        startActivity(intent);
//                    }
//                });
            }
        };
        /**
         * 点击事件
         */
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if ("1".equals(addressTags)) {
                    EventBus.getDefault().post(new CommodityBuyEvent(userDeliverAddressListBeanList.get(position).getUdaTrueName(), "0", userDeliverAddressListBeanList.get(position).getUdaAddress(), userDeliverAddressListBeanList.get(position).getUdaTel(), userDeliverAddressListBeanList.get(position).getUdaId(), "1"));
                    finish();
                } else {
                    Intent intent = new Intent(AddressActivity.this, EditAddressActivity.class);
                    intent.putExtra("udaId", userDeliverAddressListBeanList.get(position).getUdaId());
                    intent.putExtra("udaAddress", userDeliverAddressListBeanList.get(position).getUdaAddress());
                    intent.putExtra("udaTel", userDeliverAddressListBeanList.get(position).getUdaTel());
                    intent.putExtra("udaTrueName", userDeliverAddressListBeanList.get(position).getUdaTrueName());
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * 是否删除弹窗
     *
     * @param id 足迹店铺id
     */
    private void showRemoveDialog(final String id) {
        LayoutInflater layoutInflater = LayoutInflater.from(AddressActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(AddressActivity.this, R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        TextView tv_live_content = view.findViewById(R.id.tv_live_content);
        tv_live_content.setText("确定要删除这条收货地址吗？");

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
                delUserDeliverAddress(id, alertDialog);
            }
        });

    }

    /**
     * 设置默认地址
     *
     * @param id 收货地址id
     */
    private void setDefaultUserDeliverAddress(String id) {
        if (Utils.isNetworkAvailable(AddressActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.setDefaultUserDeliverAddress)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("udaId", id)
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
                            DelUserDeliverAddress delUserDeliverAddress = new DelUserDeliverAddress();
                            delUserDeliverAddress = Utils.parserJsonResult(response,
                                    DelUserDeliverAddress.class);
                            if (Constants.OK.equals(delUserDeliverAddress.getFlag())) {
                                if (Constants.OK.equals(delUserDeliverAddress.getData().getStatus())) {
                                    getUserDeliverAddress();
                                    adapter.notifyDataSetChanged();
                                    showLongToast(AddressActivity.this, "设置成功！");
                                } else {
                                    showLongToast(AddressActivity.this, delUserDeliverAddress.getData().getErrorString());
                                }

                            } else {
                                showLongToast(AddressActivity.this, delUserDeliverAddress.getErrorString());
//                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
//            varyViewHelperController.showNetworkPoorView();
            showLongToast(getApplicationContext(), "设置失败！" + Constants.NETWORK_ERROR);
        }
    }

    /**
     * 删除收货地址
     *
     * @param id          收货地址id
     * @param alertDialog alertDialog
     */
    private void delUserDeliverAddress(String id, final Dialog alertDialog) {
        if (Utils.isNetworkAvailable(AddressActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.delUserDeliverAddress)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("udaId", id)
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
                            DelUserDeliverAddress delUserDeliverAddress = new DelUserDeliverAddress();
                            delUserDeliverAddress = Utils.parserJsonResult(response,
                                    DelUserDeliverAddress.class);
                            if (Constants.OK.equals(delUserDeliverAddress.getFlag())) {
                                if (Constants.OK.equals(delUserDeliverAddress.getData().getStatus())) {
                                    getUserDeliverAddress();
                                    adapter.notifyDataSetChanged();
                                    showLongToast(AddressActivity.this, "删除成功！");
                                    alertDialog.dismiss();
                                } else {
                                    showLongToast(AddressActivity.this, delUserDeliverAddress.getData().getErrorString());
                                }

                            } else {
                                showLongToast(AddressActivity.this, delUserDeliverAddress.getErrorString());
//                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
//            varyViewHelperController.showNetworkPoorView();
            showLongToast(getApplicationContext(), "删除失败！" + Constants.NETWORK_ERROR);
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 线性布局
         */
        LinearLayoutManager layoutManage = new LinearLayoutManager(AddressActivity.this);
        recyclerView.setLayoutManager(layoutManage);
//        /**
//         * 添加分隔线
//         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(AddressActivity.this,
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

    @OnClick({R.id.img_back, R.id.add_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.add_btn:
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
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
            getUserDeliverAddress();
            refreshLayout.setRefreshing(false);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            currentPage += 1;
            loadMore = true;
            /**
             * 上拉加载首页数据
             */
            getUserDeliverAddress();
            refreshLayout.setRefreshing(false);
        }
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
    public void addressEventBus(AddressEvent event) {
        /**
         * 添加成功刷新
         */
        currentPage = 1;
        loadMore = false;
        getUserDeliverAddress();
        refreshLayout.setRefreshing(false);
    }
}
