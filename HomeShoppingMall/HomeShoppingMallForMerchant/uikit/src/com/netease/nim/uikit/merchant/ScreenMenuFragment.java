package com.netease.nim.uikit.merchant;


import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.activity.P2PMessageActivity;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import cfkj.app.cn.cfkjcommonlib.common.BaseFragment;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * 聊天页面侧滑
 */
public class ScreenMenuFragment extends BaseFragment {

    private View mView;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    SwipyRefreshLayout refreshLayout;
    private CommonAdapter<GetStoreProductListFromUserFootprint.DataBean> adapter;
    private List<GetStoreProductListFromUserFootprint.DataBean> dataBeanList = null;
    /**
     * 聊天对象uid
     */
    String uId = "";

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (mView == null) {
            initView(inflater, container);
        }
        if (getArguments() != null) {
            uId = getArguments().getString("uId");
        }
        initData();
        return mView;
    }

    private void initData() {
        initRefreshLayout();
        initRecyclerView();
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        getStoreProductListFromUserFootprint(uId);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.rightmenu, container, false);
        progressBar = mView.findViewById(R.id.progressBar);
        recyclerView = mView.findViewById(R.id.recyclerView);
        refreshLayout = mView.findViewById(R.id.refreshLayout);
    }

    /**
     * 初始化SwipeRefreshLayout
     */
    private void initRefreshLayout() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        refreshLayout.setEnabled(false);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 设置RecyclerView管理器
         */
        recyclerView.setLayoutManager(new LinearLayoutManager(P2PMessageActivity.context,
                LinearLayoutManager.VERTICAL, false));
//        /**
//         * 添加分隔线
//         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(P2PMessageActivity.context,
//                DividerItemDecoration.VERTICAL));
    }

    /**
     * 获取某用户在某店铺内浏览过的商品列表接口
     */
    private void getStoreProductListFromUserFootprint(String uId) {
        if (Utils.isNetworkAvailable(P2PMessageActivity.context)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getStoreProductListFromUserFootprint)
                    .addParams("uId", uId)
                    .addParams("sId", Constants.SID)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            progressBar.setVisibility(View.GONE);
                            Utils.LogJson(response);
                            GetStoreProductListFromUserFootprint getStoreProductListFromUserFootprint = new GetStoreProductListFromUserFootprint();
                            getStoreProductListFromUserFootprint = Utils.parserJsonResult(response,
                                    GetStoreProductListFromUserFootprint.class);
                            if (Constants.OK.equals(getStoreProductListFromUserFootprint.getFlag())) {
                                dataBeanList = getStoreProductListFromUserFootprint.getData();
                                setStoreProductListFromUserFootprintData();
                            }
                        }
                    });
        } else {
            showShortToast(P2PMessageActivity.context,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 填充列表数据
     */
    private void setStoreProductListFromUserFootprintData() {
        adapter = new CommonAdapter<GetStoreProductListFromUserFootprint.DataBean>(
                P2PMessageActivity.context, R.layout.screen_menu_item, dataBeanList) {
            @Override
            protected void convert(ViewHolder holder,
                                   GetStoreProductListFromUserFootprint.DataBean
                                           dataBean, int position) {
                RequestOptions requestOptions = new RequestOptions()
                        .error(R.drawable.ic_exception)
                        .fallback(R.drawable.ic_exception);
                Glide.with(P2PMessageActivity.context)
                        .load(dataBean.getUrl() + "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(P2PMessageActivity.context)))
                        .apply(requestOptions)
                        .into((ImageView) holder.getView(R.id.iv_pic));
                holder.setText(R.id.tv_title, dataBean.getPName());
                holder.setText(R.id.tv_price, "¥" + dataBean.getPNowPrice());
                TextView tvMoneyAgo = holder.getView(R.id.tv_old_price);
                holder.setText(R.id.tv_old_price, "¥" + dataBean.getPOriginalPrice());
                tvMoneyAgo.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.setText(R.id.tv_content, "库存:" + dataBean.getPStockNum() + "件");
            }
        };
        /**
         * 列表点击事件
         */
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                Intent intent = new Intent();
//                intent.setAction("homeShoppingMallForMerchant.GoodsDetail");
//                String pid = dataBeanList.get(position).getPId();
//                intent.putExtra("pid", pid);
//                startActivity(intent);
                Intent intent = new Intent();
                intent.setAction(Constants.RIGHTMENU_ITEM_CLICK_ACTION);
                intent.setComponent(new ComponentName("app.cn.extra.mall.merchant",
                        "app.cn.extra.mall.merchant.broadcast.MovingTrajectoryReceiver"));
                String pid = dataBeanList.get(position).getPId();
                intent.putExtra("pid", pid);
                P2PMessageActivity.context.sendBroadcast(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
    }
}

