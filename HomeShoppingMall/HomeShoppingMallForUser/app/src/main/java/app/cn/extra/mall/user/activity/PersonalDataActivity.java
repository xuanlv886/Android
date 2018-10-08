package app.cn.extra.mall.user.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nim.uikit.merchant.GetUserMemoName;
import com.netease.nim.uikit.merchant.SetUserMemoName;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.GetShop;
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
 * 个人资料
 */
public class PersonalDataActivity extends BaseActivty {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.iv_shop_pic)
    ImageView ivShopPic;
    @BindView(R.id.tv_remark_gray)
    TextView tvRemarkGray;
    @BindView(R.id.tv_shop_name)
    TextView tvShopName;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private VaryViewHelperController varyViewHelperController;
    SharePreferenceUtil sharePreferenceUtil = null;
    private CommonAdapter<GetShop.DataBean.ProductListBean> adapter;
    /**
     * 用于存放临时商品数据
     */
    List<GetShop.DataBean.ProductListBean> productListBeanList = null;
    String sId = "";
    /**
     * 商铺实体
     */
    GetShop getShop = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        sId = com.netease.nim.uikit.merchant.Constants.SID;
        sharePreferenceUtil = new SharePreferenceUtil(PersonalDataActivity.this, Constants.SAVE_USER);
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
        getStoreDetail(sId, "", "0", "");
        getUserMemoName();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 网格布局
         */
        GridLayoutManager layoutManage = new GridLayoutManager(PersonalDataActivity.this,
                2);
        recyclerView.setLayoutManager(layoutManage);
        /**
         * 添加分隔线
         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(PersonalDataActivity.this,
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

    /**
     * 获取某用户为别人设置的备注名
     */
    private void getUserMemoName() {
        if (Utils.isNetworkAvailable(PersonalDataActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getUserMemoName)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            GetUserMemoName getUserMemoName = new GetUserMemoName();
                            getUserMemoName = Utils.parserJsonResult(response,
                                    GetUserMemoName.class);
                            if (Constants.OK.equals(getUserMemoName.getFlag())) {
                                for (int i = 0; i < getUserMemoName.getData().size(); i++) {
                                    if (getUserMemoName.getData().get(i).getUmnToId().contains(com.netease.nim.uikit.merchant.Constants.UID)) {
                                        tvRemarkGray.setText(getUserMemoName.getData().get(i).getUmnName());
                                    }
                                }
                            }
                        }
                    });
        }
    }

    /**
     * 获取店铺详情接口
     * s--商铺id
     * ptdId--商品类别
     * saleSort--销量排序
     * priceSort--价格排序
     * p--当前页
     * ps--页大小
     */
    private void getStoreDetail(String sId, String ptdId, String saleSort, String priceSort) {
        if (Utils.isNetworkAvailable(PersonalDataActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.getStoreDetail)
                    .addParams("sId", sId)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("ptdId", ptdId)
                    .addParams("saleSort", saleSort)
                    .addParams("priceSort", priceSort)
                    .addParams("currentPage", 1 + "")
                    .addParams("size", 3 + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            /**
                             * 恢复显示数据的View
                             */
                            varyViewHelperController.restore();

                            getShop = new GetShop();
                            getShop = Utils.parserJsonResult(response,
                                    GetShop.class);
                            if (Constants.OK.equals(getShop.getFlag())) {
                                if (!TextUtils.isEmpty(getShop.getData().getSName())) {
                                    tvShopName.setText(getShop.getData().getSName());
                                }
                                RequestOptions requestOptions = new RequestOptions()
                                        .placeholder(R.drawable.ic_exception)
                                        .error(R.drawable.ic_exception)
                                        .fallback(R.drawable.ic_exception);
                                Glide.with(PersonalDataActivity.this)
                                        .load(getShop.getData().getPicName()+ "?x-oss-process=image/resize,w_"
                                                + (Utils.getScreenWidth(PersonalDataActivity.this)))
                                        .apply(requestOptions)
                                        .into(ivShopPic);
                                productListBeanList = getShop.getData()
                                        .getProductList();
                                setShopData();
                                setRecyclerViewHeaderAndFooter();
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
     * 填充商品数据
     */
    private void setShopData() {
        adapter = new CommonAdapter<GetShop.DataBean.ProductListBean>(
                PersonalDataActivity.this, R.layout.grid_item_shop, productListBeanList) {
            @Override
            protected void convert(ViewHolder holder,
                                   GetShop.DataBean.ProductListBean
                                           productListBeanList, int position) {
                RequestOptions requestOptions = new RequestOptions()
                        .error(R.drawable.ic_exception)
                        .fallback(R.drawable.ic_exception);
                Glide.with(PersonalDataActivity.this)
                        .load(productListBeanList.getPicName()+ "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(PersonalDataActivity.this)))
                        .apply(requestOptions)
                        .into((ImageView) holder.getView(R.id.img_item_product));
                if (!TextUtils.isEmpty(productListBeanList.getPName())) {
                    if (12 < productListBeanList.getPName().length()) {
                        holder.setText(R.id.tv_item_product_name,
                                productListBeanList.getPName()
                                        .substring(0, 10) + "···");
                    } else {
                        holder.setText(R.id.tv_item_product_name,
                                productListBeanList.getPName());
                    }
                } else {
                    holder.setText(R.id.tv_item_product_name, "");
                }
                holder.setText(R.id.tv_item_product_price,
                        "¥" + productListBeanList.getPNowPrice() + "");
                holder.setText(R.id.tv_item_product_num, productListBeanList.getPBrowseNum() + "人浏览");
            }
        };
        /**
         * 推荐商品点击事件
         */
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(PersonalDataActivity.this, GoodsDetailActivity.class);
                String pid = productListBeanList.get(position).getPId();
                intent.putExtra("pid", pid);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    /**
     * 设置RecyclerViewHeaderAndFooter
     */
    private void setRecyclerViewHeaderAndFooter() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        /**
         * 设置RecyclerViewFooter
         */
        View footerView = LayoutInflater.from(PersonalDataActivity.this).inflate(
                R.layout.common_foot_view, null);
        TextView tips = footerView.findViewById(R.id.tips);
        tips.setText("查看更多");
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoShopActivity();
            }
        });
        mHeaderAndFooterWrapper.addFootView(footerView);
        recyclerView.setAdapter(mHeaderAndFooterWrapper);
    }

    @OnClick({R.id.img_back, R.id.tv_go_in_shop, R.id.rl_remark})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_go_in_shop:
                gotoShopActivity();
                break;
            case R.id.rl_remark:
                showDialog(sId);
                break;
            default:
                break;
        }
    }

    private void showDialog(String toUid) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(com.netease.nim.uikit.R.layout.dialog_set_memo_name, null);
        final Dialog alertDialog = new AlertDialog.Builder(this, com.netease.nim.uikit.R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(com.netease.nim.uikit.R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(com.netease.nim.uikit.R.id.tv__live_never_reminds);
        EditText et_live_content = view.findViewById(com.netease.nim.uikit.R.id.et_live_content);
        tv__live_never_reminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tv_live_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String memoName = et_live_content.getText().toString().trim();
                if (TextUtils.isEmpty(memoName)) {
                    Toast.makeText(PersonalDataActivity.this, "请填写备注名！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //为用户设置备注名
                setUserMemoName(alertDialog, com.netease.nim.uikit.merchant.Constants.UID, toUid, memoName);
            }
        });
    }

    /**
     * 为用户设置备注名
     */
    private void setUserMemoName(final Dialog dialog, String fromUid, String toUid, String memoName) {
        if (Utils.isNetworkAvailable(this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.setUserMemoName)
                    .addParams("fromUid", fromUid)
                    .addParams("toUid", toUid)
                    .addParams("memoName", memoName)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            System.out.println("Exception:" + e.toString());
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            System.out.println("response:" + response);

                            SetUserMemoName setUserMemoName = new SetUserMemoName();
                            setUserMemoName = Utils.parserJsonResult(response, SetUserMemoName.class);
                            if (com.netease.nim.uikit.merchant.Constants.OK.equals(setUserMemoName.getFlag())) {
                                if (Constants.OK.equals(setUserMemoName.getData().getStatus())) {
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }
                                    Toast.makeText(PersonalDataActivity.this, "修改备注名成功！", Toast.LENGTH_SHORT).show();
                                    tvRemarkGray.setText(memoName);
                                } else {
                                    Toast.makeText(PersonalDataActivity.this,
                                            setUserMemoName.getData().getErrorString(), Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                Toast.makeText(PersonalDataActivity.this,
                                        setUserMemoName.getErrorString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(PersonalDataActivity.this, Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 跳转店铺页面
     */
    private void gotoShopActivity() {
        Intent intent = new Intent(PersonalDataActivity.this, ShopActivity.class);
        intent.putExtra("sId", sId);
        startActivity(intent);
    }
}
