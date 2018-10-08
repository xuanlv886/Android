package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.GlideImageLoader;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.view.PopMenu;
import app.cn.extra.mall.merchant.vo.GetShop;
import app.cn.extra.mall.merchant.vo.GetStoreSlide;
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
 * 店铺页面
 */
public class ShopActivity extends BaseActivty implements SwipyRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    /**
     * 当前页，用于分页获取数据 从1开始
     */
    private int currentPage = 1;
    /**
     * 判断是下拉更新还是上拉加载更多 false--下拉更新， true--上拉加载更多
     */
    private boolean loadMore = false;
    private SharePreferenceUtil sharePreferenceUtil;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private VaryViewHelperController varyViewHelperController;
    private CommonAdapter<GetShop.DataBean.ProductListBean> adapter;
    /**
     * 用于存放临时商品数据
     */
    List<GetShop.DataBean.ProductListBean> productListBeanList = null;
    /**
     * 商铺id
     */
    String sId = "";
    /**
     * saleSort	String	0--销量降序,1--销量升序
     */
    String saleSort = "";
    /**
     * priceSort String	0--价格降序,1--价格升序
     */
    String priceSort = "";
    /**
     * 商品类别
     */
    String ptdId = "";
    /**
     * 商铺详情页面实体
     */
    GetShop getShop = null;
    /**
     * 关注tv
     */
    TextView followTv = null;
    /**
     * 分类选项弹出框
     */
    private PopMenu popMenu;
    /**
     * 小类id
     */
    String productTypeId = "";
    /**
     * 关注id
     */
    String uaId = "";
    /**
     * 分类
     */
    String fenlei = "";
    /**
     * 用来判断是否是筛选条件更改页面
     * false 不是
     * true 是
     */
    boolean showFlag = false;
    List<GetShop.DataBean.ProductPtdNameListBean> typeList = null;

    private int tag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(ShopActivity.this,
                Constants.SAVE_USER);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (1 == tag) {
            loadMore = false;
            getStoreDetail(sId, ptdId, saleSort, priceSort);
        }
    }

    private void initView() {
        Intent intent = getIntent();
        sId = intent.getStringExtra("sId");
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
        getStoreDetail(sId, ptdId, saleSort, priceSort);
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

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 网格布局
         */
        GridLayoutManager layoutManage = new GridLayoutManager(ShopActivity.this, 2);
        recyclerView.setLayoutManager(layoutManage);
        /**
         * 添加分隔线
         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(ShopActivity.this,
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
     * 获取店铺详情接口
     * s--商铺id
     * ptdId--商品类别
     * saleSort--销量排序
     * priceSort--价格排序
     * p--当前页
     * ps--页大小
     */
    private void getStoreDetail(String sId, String ptdId, String saleSort, String priceSort) {
        if (Utils.isNetworkAvailable(ShopActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getStoreDetail)
                    .addParams("sId", sId)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("ptdId", ptdId)
                    .addParams("saleSort", saleSort)
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
                            getShop = new GetShop();
                            getShop = Utils.parserJsonResult(response,
                                    GetShop.class);
                            if (Constants.OK.equals(getShop.getFlag())) {
                                if (typeList == null || typeList.size() == 0) {
                                    typeList = getShop.getData().getProductPtdNameList();
                                }
                                if (loadMore) {
                                    if (null != getShop.getData()
                                            .getProductList()
                                            && 0 < getShop.getData()
                                            .getProductList().size()) {
                                        for (int i = 0; i < getShop.getData()
                                                .getProductList().size(); i++) {
                                            productListBeanList.add(getShop.getData()
                                                    .getProductList().get(i));
                                        }
                                        adapter.notifyDataSetChanged();
                                    } else {
                                        showLongToast(ShopActivity.this,
                                                "没有更多商品了！");
                                        currentPage = 1;
                                    }

                                } else {
                                    productListBeanList = getShop.getData()
                                            .getProductList();
                                    setShopData();
                                    setRecyclerViewHeaderAndFooter(getShop.getData());
                                }
//                                getStoreSlideShow(sId);
                                if (!TextUtils.isEmpty(getShop.getData().getSName())) {
                                    tvTitle.setText(getShop.getData().getSName());
                                }
                                tag = 1;
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
     * 填充推荐商品数据
     */
    private void setShopData() {
        adapter = new CommonAdapter<GetShop.DataBean.ProductListBean>(
                ShopActivity.this, R.layout.grid_item_shop, productListBeanList) {
            @Override
            protected void convert(ViewHolder holder,
                                   GetShop.DataBean.ProductListBean
                                           productListBeanList, int position) {
                RequestOptions requestOptions = new RequestOptions()
                        .error(R.drawable.ic_exception)
                        .fallback(R.drawable.ic_exception);
                Glide.with(ShopActivity.this)
                        .load(productListBeanList.getPicName() + "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(ShopActivity.this)))
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
                Intent intent = new Intent(ShopActivity.this, GoodsDetailActivity.class);
                String pid = productListBeanList.get(position - 1).getPId();
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
     * 获取商铺banner接口
     * p--当前页
     * ps--页大小
     */
    private void getStoreSlideShow(String sId) {
        if (Utils.isNetworkAvailable(ShopActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getStoreSlideShow)
                    .addParams("sId", sId)
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
                            GetStoreSlide getStoreSlide = new GetStoreSlide();
                            getStoreSlide = Utils.parserJsonResult(response,
                                    GetStoreSlide.class);
                            if (Constants.OK.equals(getStoreSlide.getFlag())) {
//                                setRecyclerViewHeaderAndFooter(getStoreSlide.getData());
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
     * 设置RecyclerViewHeaderAndFooter
     */
    private void setRecyclerViewHeaderAndFooter(GetShop.DataBean datas) {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        /**
         * 设置RecyclerViewHeader
         */
        View view = LayoutInflater.from(ShopActivity.this).inflate(
                R.layout.shop_header, null);
        /**
         * 设置轮播图
         */
        Banner banner = view.findViewById(R.id.banner);
        /**
         * 显示圆形指示器
         */
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        /**
         * 指示器位置
         */
        banner.setIndicatorGravity(BannerConfig.CENTER);
        /**
         * 设置是否自动轮播，默认为自动
         */
        banner.isAutoPlay(true);
        /**
         * 设置自动轮播时间间隔
         */
        banner.setDelayTime(3000);
        /**
         * 设置图片加载器
         */
        banner.setImageLoader(new GlideImageLoader());
        List<String> urls = new ArrayList<String>();
        for (int i = 0; i < datas.getPicList().size(); i++) {
            urls.add(datas.getPicList().get(i).getPicName());
        }
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (urls.size() > 0) {
                    Intent intent = new Intent(ShopActivity.this, PicShowActivity.class);
                    intent.putExtra("url", urls.get(position));
                    startActivity(intent);
                }
            }
        });
//        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                refreshLayout.setEnabled(false);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                refreshLayout.setEnabled(true);
//            }
//        });
        banner.setImages(urls);
        banner.start();
        ImageView logo = view.findViewById(R.id.logo);
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.ic_exception)
                .fallback(R.drawable.ic_exception);
        Glide.with(ShopActivity.this)
                .load(datas.getPicName() + "?x-oss-process=image/resize,w_"
                        + (Utils.getScreenWidth(ShopActivity.this)))
                .apply(requestOptions)
                .into(logo);
        initClassifyLayout(view);
//        /**
//         * 设置RecyclerViewFooter
//         */
//        View footerView = LayoutInflater.from(getActivity()).inflate(
//                R.layout.common_foot_view,null);
        mHeaderAndFooterWrapper.addHeaderView(view);
//        mHeaderAndFooterWrapper.addFootView(footerView);
        recyclerView.setAdapter(mHeaderAndFooterWrapper);
    }

    /**
     * 选项卡按钮
     */
    private void initClassifyLayout(View v) {
        LinearLayout ll = (LinearLayout) v.findViewById(R.id.store_classify);
        /**
         * 分类
         */
        RelativeLayout rl_classify = (RelativeLayout) ll.findViewById(R.id.rl_classify);
        /**
         * 销量
         */
        RelativeLayout rl_sale = (RelativeLayout) ll.findViewById(R.id.rl_sale);
        /**
         * 价格
         */
        RelativeLayout rl_price = (RelativeLayout) ll.findViewById(R.id.rl_price);
        ImageView img_classify = (ImageView) ll.findViewById(R.id.img_classify);
        ImageView img_sale = (ImageView) ll.findViewById(R.id.img_sale);
        ImageView img_price = (ImageView) ll.findViewById(R.id.img_price);
        TextView tv_fenlei = (TextView) ll.findViewById(R.id.tv_fenlei);
        /**
         * 点击事件
         */
        rl_classify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnClassifyClicked(view, img_classify, tv_fenlei);
            }
        });
        rl_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSaleClicked(img_sale);
            }
        });
        rl_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPriceClicked(img_price);
            }
        });
//        if (showFlag) {
        /**
         * 筛选后销量排序显示
         */
        switch (saleSort) {
            case "0":
                img_sale.setImageResource(R.mipmap.icon_xiaoliang_gaodaodi);
                break;
            case "1":
                img_sale.setImageResource(R.mipmap.icon_xiaoliang_didaogao);
                break;
            case "":
                img_sale.setImageResource(R.mipmap.icon_xiaoliang_nor);
                break;
            default:
                break;
        }
        /**
         * 筛选后价格排序显示
         */
        switch (priceSort) {
            case "0":
                img_price.setImageResource(R.mipmap.icon_xiaoliang_gaodaodi);
                break;
            case "1":
                img_price.setImageResource(R.mipmap.icon_xiaoliang_didaogao);
                break;
            case "":
                img_price.setImageResource(R.mipmap.icon_xiaoliang_nor);
                break;
            default:
                break;
        }
        /**
         * 筛选后分类显示
         */
        img_classify.setImageResource(R.mipmap.icon_fenleixiala_nor);
        if (TextUtils.isEmpty(fenlei)) {
            fenlei = "全部";
        }
        tv_fenlei.setText(fenlei);
//        }
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
            data.add(typeList.get(i).getPtdName());
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
                fenlei = tv_fenlei.getText().toString().trim();
                if (index == 0) {
                    ptdId = "";
                } else {
                    ptdId = typeList.get(index - 1).getPtdId();
                }
                //更新数据
                currentPage = 1;
                loadMore = false;
                showFlag = true;
                getStoreDetail(sId, ptdId, saleSort, priceSort);
                adapter.notifyDataSetChanged();
            }
        });
        popMenu.showAsDropDown(v);
    }

    // 销量
    private void btnSaleClicked(ImageView img_sale) {
        switch (saleSort) {
            case "": // 灰色->从高到低
                img_sale.setImageResource(R.mipmap.icon_xiaoliang_gaodaodi);
                saleSort = "0";
                priceSort = "";
                currentPage = 1;
                loadMore = false;
                showFlag = true;
                getStoreDetail(sId, ptdId, saleSort, priceSort);
                break;
            case "0": // 从高到低->从低到高
                img_sale.setImageResource(R.mipmap.icon_xiaoliang_didaogao);
                saleSort = "1";
                priceSort = "";
                currentPage = 1;
                loadMore = false;
                showFlag = true;
                getStoreDetail(sId, ptdId, saleSort, priceSort);
                break;
            case "1": // 从低到高->灰色
//                img_sale.setImageResource(R.mipmap.icon_xiaoliang_nor);
//                saleSort = "";
//                currentPage = 1;
//                loadMore = false;
//                showFlag = true;
//                getStoreDetail(sId, ptdId, saleSort, priceSort);
                img_sale.setImageResource(R.mipmap.icon_xiaoliang_gaodaodi);
                saleSort = "0";
                priceSort = "";
                currentPage = 1;
                loadMore = false;
                showFlag = true;
                getStoreDetail(sId, ptdId, saleSort, priceSort);
                break;

            default:
                break;
        }
    }

    // 价格
    private void btnPriceClicked(ImageView img_price) {
        switch (priceSort) {
            case "": // 灰色->从低到高
                img_price.setImageResource(R.mipmap.icon_xiaoliang_gaodaodi);
                priceSort = "0";
                saleSort = "";
                currentPage = 1;
                loadMore = false;
                showFlag = true;
                getStoreDetail(sId, ptdId, saleSort, priceSort);
                break;
            case "0": // 从低到高->从高到低
                img_price.setImageResource(R.mipmap.icon_xiaoliang_didaogao);
                priceSort = "1";
                saleSort = "";
                currentPage = 1;
                loadMore = false;
                showFlag = true;
                getStoreDetail(sId, ptdId, saleSort, priceSort);
                break;
            case "1": // 从高到低->灰色
//                img_price.setImageResource(R.mipmap.icon_xiaoliang_nor);
//                priceSort = "";
//                currentPage = 1;
//                loadMore = false;
//                showFlag = true;
//                getStoreDetail(sId, ptdId, saleSort, priceSort);
                img_price.setImageResource(R.mipmap.icon_xiaoliang_gaodaodi);
                priceSort = "0";
                saleSort = "";
                currentPage = 1;
                loadMore = false;
                showFlag = true;
                getStoreDetail(sId, ptdId, saleSort, priceSort);
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
            showFlag = false;
            /**
             * 下拉刷新首页数据
             */
            getStoreDetail(sId, ptdId, saleSort, priceSort);
            refreshLayout.setRefreshing(false);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            currentPage += 1;
            loadMore = true;
            /**
             * 上拉加载首页数据
             */
            getStoreDetail(sId, ptdId, saleSort, priceSort);
            refreshLayout.setRefreshing(false);
        }
    }

    /**
     * 点击异常情况界面，重新调用接口刷新数据
     */
    @Override
    public void clickEvent() {
        super.clickEvent();
    }
}
