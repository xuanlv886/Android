package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.view.PopMenu;
import app.cn.extra.mall.user.vo.SelectByloosename;
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
 * 商品查询列表页面
 */
public class SearchGoodActivity extends BaseActivty implements SwipyRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    @BindView(R.id.rl_classify)
    RelativeLayout rlClassify;
    @BindView(R.id.rl_sale)
    RelativeLayout rlSale;
    @BindView(R.id.rl_price)
    RelativeLayout rlPrice;
    @BindView(R.id.img_classify)
    ImageView imgClassify;
    @BindView(R.id.img_sale)
    ImageView imgSale;
    @BindView(R.id.img_price)
    ImageView imgPrice;
    @BindView(R.id.tv_fenlei)
    TextView tvFenlei;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    /**
     * saleSort	String	0--销量降序,1--销量升序
     */
    String saleSort = "";
    /**
     * priceSort String	0--价格降序,1--价格升序
     */
    String priceSort = "";
    /**
     * 分类选项弹出框
     */
    private PopMenu popMenu;
    /**
     * 当前页，用于分页获取数据 从1开始
     */
    private int currentPage = 1;
    /**
     * 判断是下拉更新还是上拉加载更多 false--下拉更新， true--上拉加载更多
     */
    private boolean loadMore = false;
    /**
     * 小类id
     */
    String productTypeId = "";
    List<SelectByloosename.DataBean.ProductTypeBean> typeList = null;
    List<SelectByloosename.DataBean.ProductMessageBean> list = null;
    CommonAdapter<SelectByloosename.DataBean.ProductMessageBean> adapter;
    private SharePreferenceUtil sharePreferenceUtil;
    private String shopName = "";
    private VaryViewHelperController varyViewHelperController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_good);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        shopName = intent.getStringExtra("shopName");
        sharePreferenceUtil = new SharePreferenceUtil(SearchGoodActivity.this,
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
        selectBylooseName();
    }

    private void selectBylooseName() {
        if (null != progressBar) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (Utils.isNetworkAvailable(SearchGoodActivity.this)) {
            String size = 10 + "";
            OkHttpUtils
                    .post()
                    .url(Constants.selectBylooseName)
                    .addParams("looseName", shopName)
                    .addParams("ptdId", productTypeId)
                    .addParams("priceSort", priceSort)
                    .addParams("saleSort", saleSort)
                    .addParams("currentPage", currentPage + "")
                    .addParams("size", size)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            e.printStackTrace();
                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            varyViewHelperController.restore();
                            SelectByloosename selectByloosename = new SelectByloosename();
                            selectByloosename = Utils.parserJsonResult(response, SelectByloosename.class);
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            if ("true".equals(selectByloosename.getFlag())) {
                                if (typeList == null || typeList.size() == 0) {
                                    typeList = selectByloosename.getData().getProductType();
                                }
                                if (loadMore) {
                                    if (null != selectByloosename.getData()
                                            .getProductMessage()
                                            && 0 < selectByloosename.getData()
                                            .getProductMessage().size()) {
                                        for (int i = 0; i < selectByloosename.getData()
                                                .getProductMessage().size(); i++) {
                                            list.add(selectByloosename.getData()
                                                    .getProductMessage().get(i));
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        showLongToast(SearchGoodActivity.this,
                                                "没有更多推荐商品了！");
                                    }

                                } else {
                                    if (selectByloosename.getData().getProductMessage() != null && selectByloosename.getData().getProductMessage().size() > 0) {
                                        list = selectByloosename.getData()
                                                .getProductMessage();
                                        setData();
                                    } else {
                                        varyViewHelperController.showEmptyView();
                                    }
                                }
                                recyclerView.setAdapter(adapter);
                            } else {
                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
            if (null != progressBar) {
                progressBar.setVisibility(View.GONE);
            }
            varyViewHelperController.showNetworkErrorView();
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 网格布局
         */
        GridLayoutManager layoutManage = new GridLayoutManager(SearchGoodActivity.this, 2);
        recyclerView.setLayoutManager(layoutManage);
        /**
         * 添加分隔线
         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(SearchGoodActivity.this,
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

    @OnClick({R.id.img_back, R.id.rl_classify, R.id.rl_sale, R.id.rl_price})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_classify:
                //分类
                btnClassifyClicked(view, imgClassify, tvFenlei);
                break;
            case R.id.rl_sale:
                //销量
                btnSaleClicked(imgSale);
                break;
            case R.id.rl_price:
                //价格
                btnPriceClicked(imgPrice);
                break;
            default:
                break;
        }
    }

    private int getScreenWidth() {
        // TODO Auto-generated method stub
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        return dm.widthPixels; // 屏幕宽（像素，如：480px）
    }

    private void btnClassifyClicked(View v, ImageView img_classify, TextView tv_fenlei) {

        img_classify.setImageResource(R.mipmap.icon_fenleixiala_sel);
        final ArrayList<String> data = new ArrayList<String>();
        data.add("全部");
        //添加数据
        for (int i = 0; i < typeList.size(); i++) {
            data.add(typeList.get(i).getName());
        }
        // 初始化弹出菜单	 POPMENU
        popMenu = new PopMenu(this, getScreenWidth() / 3, data);
        popMenu.setOnItemClickListener(new PopMenu.OnItemClickListener() {

            @Override
            public void onItemClick(int index) {
                // TODO Auto-generated method stub
                img_classify.setImageResource(R.mipmap.icon_fenleixiala_nor);
                if (!TextUtils.isEmpty(data.get(index))
                        && 6 <= data.get(index).length()) {
                    tv_fenlei.setText(data.get(index).substring(0, 5) + "···");
                } else {
                    tv_fenlei.setText(data.get(index));
                }

                if (index == 0) {
                    productTypeId = "";
                } else {
                    productTypeId = typeList.get(index - 1).getId();
                }
                //更新数据
                currentPage = 1;
                loadMore = false;
                selectBylooseName();
                adapter.notifyDataSetChanged();
            }
        });
        popMenu.showAsDropDown(v);
    }

    // 销量
    private void btnSaleClicked(ImageView img_sale) {
        switch (saleSort) {
            case "": // 灰色->从高到低
                imgClassify.setImageResource(R.mipmap.icon_fenleixiala_nor);
                imgPrice.setImageResource(R.mipmap.icon_xiaoliang_nor);
                img_sale.setImageResource(R.mipmap.icon_xiaoliang_gaodaodi);
                saleSort = "0";
                priceSort = "";
                currentPage = 1;
                loadMore = false;
                selectBylooseName();
                break;
            case "0": // 从高到低->从低到高
                imgClassify.setImageResource(R.mipmap.icon_fenleixiala_nor);
                imgPrice.setImageResource(R.mipmap.icon_xiaoliang_nor);
                img_sale.setImageResource(R.mipmap.icon_xiaoliang_didaogao);
                saleSort = "1";
                priceSort = "";
                currentPage = 1;
                loadMore = false;
                selectBylooseName();
                break;
            case "1": // 从低到高->灰色
//                img_sale.setImageResource(R.mipmap.icon_xiaoliang_nor);
//                saleSort = "";
//                currentPage = 1;
//                loadMore = false;
//                selectBylooseName();
                imgClassify.setImageResource(R.mipmap.icon_fenleixiala_nor);
                imgPrice.setImageResource(R.mipmap.icon_xiaoliang_nor);
                img_sale.setImageResource(R.mipmap.icon_xiaoliang_gaodaodi);
                saleSort = "0";
                priceSort = "";
                currentPage = 1;
                loadMore = false;
                selectBylooseName();
                break;

            default:
                break;
        }
    }

    // 价格
    private void btnPriceClicked(ImageView img_price) {
        switch (priceSort) {
            case "": // 灰色->从低到高
                imgClassify.setImageResource(R.mipmap.icon_fenleixiala_nor);
                imgSale.setImageResource(R.mipmap.icon_xiaoliang_nor);
                img_price.setImageResource(R.mipmap.icon_xiaoliang_gaodaodi);
                priceSort = "0";
                saleSort = "";
                currentPage = 1;
                loadMore = false;
                selectBylooseName();
                break;
            case "0": // 从低到高->从高到低
                imgClassify.setImageResource(R.mipmap.icon_fenleixiala_nor);
                imgSale.setImageResource(R.mipmap.icon_xiaoliang_nor);
                img_price.setImageResource(R.mipmap.icon_xiaoliang_didaogao);
                priceSort = "1";
                saleSort = "";
                currentPage = 1;
                loadMore = false;
                selectBylooseName();
                break;
            case "1": // 从高到低->灰色
//                img_price.setImageResource(R.mipmap.icon_xiaoliang_nor);
//                priceSort = "";
//                currentPage = 1;
//                loadMore = false;
//                selectBylooseName();
                imgClassify.setImageResource(R.mipmap.icon_fenleixiala_nor);
                imgSale.setImageResource(R.mipmap.icon_xiaoliang_nor);
                img_price.setImageResource(R.mipmap.icon_xiaoliang_gaodaodi);
                priceSort = "0";
                saleSort = "";
                currentPage = 1;
                loadMore = false;
                selectBylooseName();
                break;

            default:
                break;
        }
    }

    private void setData() {
        adapter = new CommonAdapter<SelectByloosename.DataBean.ProductMessageBean>(SearchGoodActivity.this, R.layout.grid_item_store, list) {
            @Override
            protected void convert(ViewHolder holder, SelectByloosename.DataBean.ProductMessageBean productListBean,
                                   int position) {
                holder.getView(R.id.tv_grid_item_store_buy).setBackgroundColor(getResources().getColor(R.color.blue));
                if (!TextUtils.isEmpty(list.get(position).getProPrice() + "")) {
                    holder.setText(R.id.tv_grid_item_store_moneys, "¥ " + list.get(position).getProPrice());
                } else {
                    holder.setText(R.id.tv_grid_item_store_moneys, "");
                }
                holder.setTextColor(R.id.tv_grid_item_store_moneys, getResources().getColor(R.color.orange));
                holder.setText(R.id.tv_grid_item_store_buy, "立即购买");


                if (!TextUtils.isEmpty(list.get(position).getProName())) {
                    holder.setText(R.id.tv_grid_item_store_name, list.get(position).getProName());
                } else {
                    holder.setText(R.id.tv_grid_item_store_name, "");
                }
                RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) holder.getView(R.id.img_grid_item_store).getLayoutParams();
                linearParams.width = getScreenWidth() / 2 - 10;
                linearParams.height = getScreenWidth() / 2 - 10;
                holder.getView(R.id.img_grid_item_store).setLayoutParams(linearParams);
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.ic_exception)
                        .error(R.drawable.ic_exception)
                        .fallback(R.drawable.ic_exception);
                Glide.with(SearchGoodActivity.this)
                        .load(list.get(position).getUrl()
                                + "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(SearchGoodActivity.this)))
                        .apply(requestOptions)
                        .into((ImageView) holder.getView(R.id.img_grid_item_store));
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(SearchGoodActivity.this, GoodsDetailActivity.class);
                String pid = list.get(position).getProductId();
                intent.putExtra("pid", pid);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            currentPage = 1;
            loadMore = false;
            /**
             * 下拉刷新数据
             */
            selectBylooseName();
            refreshLayout.setRefreshing(false);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            currentPage += 1;
            loadMore = true;
            /**
             * 上拉加载数据
             */
            selectBylooseName();
            refreshLayout.setRefreshing(false);
        }
    }
}
