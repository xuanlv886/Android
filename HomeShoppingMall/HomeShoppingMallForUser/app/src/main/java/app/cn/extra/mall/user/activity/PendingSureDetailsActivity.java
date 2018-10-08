package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nim.uikit.api.NimUIKit;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.PendingSureDetailsEvent;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.AddUserProductRequirement;
import app.cn.extra.mall.user.vo.DelUserRequirement;
import app.cn.extra.mall.user.vo.DeleteStoreApplyRequirement;
import app.cn.extra.mall.user.vo.GetUserRequirementDetail;
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
 * 待确认-订单详情
 */
public class PendingSureDetailsActivity extends BaseActivty {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private VaryViewHelperController varyViewHelperController;
    private CommonAdapter<GetUserRequirementDetail.DataBean.StoreApplyRequirementListBean> adapter;
    /**
     * 当前页，用于分页获取数据 从1开始
     */
    private int currentPage = 1;
    /**
     * 判断是下拉更新还是上拉加载更多 false--下拉更新， true--上拉加载更多
     */
    private boolean loadMore = false;
    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 用于存放临时数据
     */
    List<GetUserRequirementDetail.DataBean.StoreApplyRequirementListBean> storeAppleRequirementListBeanList = null;
    String urId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_sure_details);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initView();
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
    public void PendingSureDetailsEventBus(PendingSureDetailsEvent event) {
//        addUserProductRequirement(event.getsId(), event.getRoId());
        finish();
    }

    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(MyDemandActivity.context,
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
        Intent intent = getIntent();
        urId = intent.getStringExtra("urId");
        getUserRequirementDetail(urId);

    }

    @OnClick({R.id.img_back, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_delete:
                tvDelete.setEnabled(false);
                delUserRequirement(urId);
                break;
            default:
                break;
        }
    }

    /**
     * 删除需求
     */
    private void delUserRequirement(String urId) {
        if (Utils.isNetworkAvailable(PendingSureDetailsActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.delUserRequirement)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("urId", urId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            tvDelete.setEnabled(true);
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            tvDelete.setEnabled(true);

                            DelUserRequirement delUserRequirement = new DelUserRequirement();
                            delUserRequirement = Utils.parserJsonResult(response,
                                    DelUserRequirement.class);
                            if (Constants.OK.equals(delUserRequirement.getFlag())) {
                                if (Constants.OK.equals(delUserRequirement.getData().getStatus())) {
                                    showLongToast(PendingSureDetailsActivity.this, "删除成功！");
                                    finish();
                                } else {
                                    showLongToast(PendingSureDetailsActivity.this, delUserRequirement.getData().getErrorString());
                                }

                            } else {
                                showLongToast(PendingSureDetailsActivity.this, delUserRequirement.getErrorString());
                                tvDelete.setEnabled(true);
                            }
                        }
                    });
        } else {
            showLongToast(PendingSureDetailsActivity.this, Constants.NETWORK_ERROR);
            tvDelete.setEnabled(true);
        }
    }

    /**
     * 获取需求详情
     */
    private void getUserRequirementDetail(String urId) {
        if (Utils.isNetworkAvailable(PendingSureDetailsActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.getUserRequirementDetail)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("urId", urId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            e.printStackTrace();
                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }

                            /**
                             * 恢复显示数据的View
                             */
                            varyViewHelperController.restore();
                            GetUserRequirementDetail getUserRequirementDetail = new GetUserRequirementDetail();
                            getUserRequirementDetail = Utils.parserJsonResult(response,
                                    GetUserRequirementDetail.class);
                            if (Constants.OK.equals(getUserRequirementDetail.getFlag())) {
                                storeAppleRequirementListBeanList = getUserRequirementDetail.getData()
                                        .getStoreApplyRequirementList();
                                setUserRequirementDetailData(getUserRequirementDetail.getData());
                                initHeader(getUserRequirementDetail.getData());
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
    private void setUserRequirementDetailData(GetUserRequirementDetail.DataBean dataBean) {
        adapter = new CommonAdapter<GetUserRequirementDetail.DataBean.StoreApplyRequirementListBean>(
                MyDemandActivity.context, R.layout.list_item_pending_sure_details, storeAppleRequirementListBeanList) {
            @Override
            protected void convert(ViewHolder holder,
                                   GetUserRequirementDetail.DataBean.StoreApplyRequirementListBean
                                           storeAppleRequirementListBean, int position) {
                RequestOptions requestOptions = new RequestOptions()
                        .error(R.drawable.ic_exception)
                        .fallback(R.drawable.ic_exception);
                Glide.with(MyDemandActivity.context)
                        .load(storeAppleRequirementListBean.getPicName() + "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(PendingSureDetailsActivity.this)))
                        .apply(requestOptions)
                        .into((ImageView) holder.getView(R.id.iv_pic));
                switch (storeAppleRequirementListBean.getSLevel()) {
                    case 1:
                        holder.setImageResource(R.id.iv_star1, R.mipmap.icon_s2);
                        holder.setImageResource(R.id.iv_star2, R.mipmap.icon_s1);
                        holder.setImageResource(R.id.iv_star3, R.mipmap.icon_s1);
                        holder.setImageResource(R.id.iv_star4, R.mipmap.icon_s1);
                        holder.setImageResource(R.id.iv_star5, R.mipmap.icon_s1);
                        break;
                    case 2:
                        holder.setImageResource(R.id.iv_star1, R.mipmap.icon_s2);
                        holder.setImageResource(R.id.iv_star2, R.mipmap.icon_s2);
                        holder.setImageResource(R.id.iv_star3, R.mipmap.icon_s1);
                        holder.setImageResource(R.id.iv_star4, R.mipmap.icon_s1);
                        holder.setImageResource(R.id.iv_star5, R.mipmap.icon_s1);
                        break;
                    case 3:
                        holder.setImageResource(R.id.iv_star1, R.mipmap.icon_s2);
                        holder.setImageResource(R.id.iv_star2, R.mipmap.icon_s2);
                        holder.setImageResource(R.id.iv_star3, R.mipmap.icon_s2);
                        holder.setImageResource(R.id.iv_star4, R.mipmap.icon_s1);
                        holder.setImageResource(R.id.iv_star5, R.mipmap.icon_s1);
                        break;
                    case 4:
                        holder.setImageResource(R.id.iv_star1, R.mipmap.icon_s2);
                        holder.setImageResource(R.id.iv_star2, R.mipmap.icon_s2);
                        holder.setImageResource(R.id.iv_star3, R.mipmap.icon_s2);
                        holder.setImageResource(R.id.iv_star4, R.mipmap.icon_s2);
                        holder.setImageResource(R.id.iv_star5, R.mipmap.icon_s1);
                        break;
                    case 5:
                        holder.setImageResource(R.id.iv_star1, R.mipmap.icon_s2);
                        holder.setImageResource(R.id.iv_star2, R.mipmap.icon_s2);
                        holder.setImageResource(R.id.iv_star3, R.mipmap.icon_s2);
                        holder.setImageResource(R.id.iv_star4, R.mipmap.icon_s2);
                        holder.setImageResource(R.id.iv_star5, R.mipmap.icon_s2);
                        break;
                    default:
                        holder.setImageResource(R.id.iv_star1, R.mipmap.icon_s1);
                        holder.setImageResource(R.id.iv_star2, R.mipmap.icon_s1);
                        holder.setImageResource(R.id.iv_star3, R.mipmap.icon_s1);
                        holder.setImageResource(R.id.iv_star4, R.mipmap.icon_s1);
                        holder.setImageResource(R.id.iv_star5, R.mipmap.icon_s1);
                        break;
                }
                if (!TextUtils.isEmpty(storeAppleRequirementListBean.getSName())) {
                    if (10 < storeAppleRequirementListBean.getSName().length()) {
                        holder.setText(R.id.tv_shop_name,
                                storeAppleRequirementListBean.getSName()
                                        .substring(0, 8) + "···");
                    } else {
                        holder.setText(R.id.tv_shop_name,
                                storeAppleRequirementListBean.getSName());
                    }
                } else {
                    holder.setText(R.id.tv_shop_name, "");
                }
                holder.setText(R.id.tv_create_time,
                        "申请时间：" + storeAppleRequirementListBean.getSarCreateTime());
//                if ("null".equals(storeAppleRequirementListBean.getFinishedNum())) {
//                    holder.setText(R.id.tv_order_num,
//                            "已完成0单");
//                } else {
                holder.setText(R.id.tv_order_num,
                        "已完成" + storeAppleRequirementListBean.getFinishedNum() + "单");
//                }
                if (!TextUtils.isEmpty(storeAppleRequirementListBean.getSDescribe())) {
                    if (10 < storeAppleRequirementListBean.getSDescribe().length()) {
                        holder.setText(R.id.tv_content,
                                storeAppleRequirementListBean.getSDescribe()
                                        .substring(0, 8) + "···");
                    } else {
                        holder.setText(R.id.tv_content,
                                storeAppleRequirementListBean.getSDescribe());
                    }
                } else {
                    holder.setText(R.id.tv_content, "");
                }
                holder.setText(R.id.tv_money, "报价钱数：¥" + storeAppleRequirementListBean.getSarPrice() + "元");
                holder.getView(R.id.rl_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        deleteStoreApplyRequirement(dataBean.getRoId(), storeAppleRequirementListBean.getSId());
                    }
                });
                holder.getView(R.id.iv_phone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL,
                                Uri.parse("tel:" + storeAppleRequirementListBean.getSTel()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                holder.getView(R.id.img_message).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(storeAppleRequirementListBean.getAccId())) {
                            NimUIKit.startP2PSession(PendingSureDetailsActivity.this,
                                    storeAppleRequirementListBean.getAccId(), null);
                        }
                    }
                });

                holder.getView(R.id.rl_sure).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PendingSureDetailsActivity.this, PayDemandActivity.class);
                        intent.putExtra("sId", storeAppleRequirementListBean.getSId());
                        intent.putExtra("roId", dataBean.getRoId());
                        intent.putExtra("rtId", dataBean.getRtId());
                        intent.putExtra("sName", storeAppleRequirementListBean.getSName());
                        if (!TextUtils.isEmpty("" + storeAppleRequirementListBean.getSarPrice())) {
                            intent.putExtra("price", "" + storeAppleRequirementListBean.getSarPrice());
                        } else if (!TextUtils.isEmpty("" + dataBean.getRoTotalPrice())) {
                            intent.putExtra("price", dataBean.getRoTotalPrice());
                        }
                        startActivity(intent);
//                        addUserProductRequirement(dataBean.getSId(), dataBean.getRoId());
                    }
                });
            }
        };
        /**
         * 推荐商品点击事件
         */
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                Utils.LogE(recommendProductListBeanList.get(position - 1).getPName());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 设置header
     */
    private void initHeader(GetUserRequirementDetail.DataBean dataBean) {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        /**
         * 设置RecyclerViewHeader
         */
        View view = LayoutInflater.from(MyDemandActivity.context).inflate(
                R.layout.pending_sure_details_header_view, null);
        TextView tvDemandType = view.findViewById(R.id.tv_demand_type);
        TextView tvDemandTitle = view.findViewById(R.id.tv_demand_title);
        TextView tvDemendContent = view.findViewById(R.id.tv_demend_content);
        TextView tvSendTime = view.findViewById(R.id.tv_send_time);
        TextView tvDemandMoney = view.findViewById(R.id.tv_demand_money);
        TextView tvUserName = view.findViewById(R.id.tv_user_name);
        TextView tvUserPhone = view.findViewById(R.id.tv_user_phone);
        TextView tvUserAddress = view.findViewById(R.id.tv_user_address);
        TextView tvGetAddress = view.findViewById(R.id.tv_get_address);

        tvDemandType.setText("需求类别：" + dataBean.getRtName());
        tvDemandTitle.setText("需求标题：" + dataBean.getUrTitle());
        tvDemendContent.setText("需求描述：" + dataBean.getUrContent());
        tvSendTime.setText("发布时间：" + dataBean.getUrCreateTime());
        // 需商家报价
        if (1 == dataBean.getUrOfferType()) {
            tvDemandMoney.setText("悬赏金额：需商家报价");
        } else {
            tvDemandMoney.setText("悬赏金额：" + "¥" + dataBean.getUrOfferPrice());
        }

        tvUserName.setText("发  布  人：" + dataBean.getUrTrueName());
        tvUserPhone.setText("联系电话：" + dataBean.getUrTel());
        tvUserAddress.setText("居住地址：" + dataBean.getUrAddress());
        if ("取货送货".equals(dataBean.getRtName())) {
            tvGetAddress.setVisibility(View.VISIBLE);
            tvGetAddress.setText("取货地址：" + dataBean.getUrGetAddress());
        } else {
            tvGetAddress.setVisibility(View.GONE);
        }

        mHeaderAndFooterWrapper.addHeaderView(view);
        recyclerView.setAdapter(mHeaderAndFooterWrapper);
    }

    /**
     * 需求选择商铺--移除
     */
    private void deleteStoreApplyRequirement(String roId, String sId) {
        if (Utils.isNetworkAvailable(PendingSureDetailsActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.deleteStoreApplyRequirement)
                    .addParams("roId", roId)
                    .addParams("sId", sId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
//                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            /**
                             * 恢复显示数据的View
                             */
//                            varyViewHelperController.restore();

                            DeleteStoreApplyRequirement deleteStoreApplyRequirement = new DeleteStoreApplyRequirement();
                            deleteStoreApplyRequirement = Utils.parserJsonResult(response,
                                    DeleteStoreApplyRequirement.class);
                            if (Constants.OK.equals(deleteStoreApplyRequirement.getFlag())) {
                                showLongToast(PendingSureDetailsActivity.this, "移除成功！");
                                if (storeAppleRequirementListBeanList.size() > 1) {
                                    getUserRequirementDetail(urId);
                                }
                            } else {
                                showLongToast(PendingSureDetailsActivity.this, deleteStoreApplyRequirement.getErrorString());
                            }
                        }
                    });
        } else {
//            varyViewHelperController.showNetworkPoorView();
        }
    }

    /**
     * 需求选择商铺--确认
     */
    private void addUserProductRequirement(String sId, String roId) {
        if (Utils.isNetworkAvailable(PendingSureDetailsActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.addUserProductRequirement)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sId)
                    .addParams("roId", roId)
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

                            AddUserProductRequirement addUserProductRequirement = new AddUserProductRequirement();
                            addUserProductRequirement = Utils.parserJsonResult(response,
                                    AddUserProductRequirement.class);
                            if (Constants.OK.equals(addUserProductRequirement.getFlag())) {
                                showLongToast(PendingSureDetailsActivity.this, "确认成功！");
                                finish();
                            }
                        }
                    });
        } else {
            showLongToast(PendingSureDetailsActivity.this, Constants.NETWORK_ERROR);
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 线性布局
         */
        LinearLayoutManager layoutManage = new LinearLayoutManager(PendingSureDetailsActivity.this);
        recyclerView.setLayoutManager(layoutManage);
//        /**
//         * 添加分隔线
//         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(PendingSureDetailsActivity.this,
//                DividerItemDecoration.VERTICAL));
    }

    /**
     * 初始化SwipeRefreshLayout
     */
    private void initRefreshLayout() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        /**
         * 禁用刷新
         */
        refreshLayout.setEnabled(false);
    }
}
