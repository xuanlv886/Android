package app.cn.extra.mall.user.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nim.uikit.common.util.string.MD5;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.adapter.recyclerview.wrapper.LoadMoreWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.activity.ChangeCityActivity;
import app.cn.extra.mall.user.activity.GoodsDetailActivity;
import app.cn.extra.mall.user.activity.LoginActivity;
import app.cn.extra.mall.user.activity.SearchShopActivity;
import app.cn.extra.mall.user.activity.SendDemandActivity;
import app.cn.extra.mall.user.activity.ShopActivity;
import app.cn.extra.mall.user.event.IndexFragmentEvent;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.GlideImageLoader;
import app.cn.extra.mall.user.utils.NotificationsUtils;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.GetMainInterface;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
public class IndexFragment extends BaseFragment {

    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_publish)
    TextView tvPublish;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private SharePreferenceUtil sharePreferenceUtil;
    private View mView;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private LoadMoreWrapper mLoadMoreWrapper;
    private VaryViewHelperController varyViewHelperController;
    private CommonAdapter<GetMainInterface.DataBean.RecommendProductListBean> adapter = null;
    /**
     * 当前页，用于分页获取数据 从1开始
     */
    private int currentPage = 1;
    /**
     * 用于存放临时推荐商品数据
     */
    private List<GetMainInterface.DataBean.RecommendProductListBean> recommendProductListBeanList = null;
    private AbortableFuture<LoginInfo> loginRequest;
    private GetMainInterface getMainInterface = null;

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
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_index, container, false);
        unbinder = ButterKnife.bind(this, mView);

        sharePreferenceUtil = new SharePreferenceUtil(getActivity(),
                Constants.SAVE_USER);
        initView();
        return mView;
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
        if (!TextUtils.isEmpty(event.getAcCity())) {
            tvCity.setText(event.getAcCity());
        } else {
            tvCity.setText("石家庄市");
        }
        if (recommendProductListBeanList != null) {
            recommendProductListBeanList.clear();
        }
        currentPage = 1;
        getMainInterfacePort();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        if (!TextUtils.isEmpty(sharePreferenceUtil.getCheckCityName())) {
            tvCity.setText(sharePreferenceUtil.getCheckCityName());
        } else if (!TextUtils.isEmpty(sharePreferenceUtil.getCityName())) {
            tvCity.setText(sharePreferenceUtil.getCityName());
        } else {
            tvCity.setText("石家庄市");
        }
        /**
         * 设置异常情况界面
         */
        varyViewHelperController = createCaseViewHelperController(recyclerView);
        varyViewHelperController.setUpRefreshViews(varyViewHelperController.getErrorView(),
                varyViewHelperController.getNetworkPoorView(), varyViewHelperController.getEmptyView());

        initRecyclerView();
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        currentPage = 1;
        getMainInterfacePort();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 网格布局
         */
        GridLayoutManager layoutManage = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManage);
        /**
         * 添加分隔线
         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
//                DividerItemDecoration.VERTICAL));
    }

    /**
     * 获取主页面相关数据接口
     * p--当前页
     * ps--页大小
     */
    private void getMainInterfacePort() {
        if (Utils.isNetworkAvailable(getActivity())) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            String acId = "";
            if (!TextUtils.isEmpty(sharePreferenceUtil.getCheckAcId())) {
                acId = sharePreferenceUtil.getCheckAcId();
            } else if (!TextUtils.isEmpty(sharePreferenceUtil.getAcId())) {
                acId = sharePreferenceUtil.getAcId();
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getMainInterface)
                    .addParams("acId", acId)
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

                            getMainInterface = new GetMainInterface();
                            getMainInterface = Utils.parserJsonResult(response,
                                    GetMainInterface.class);
                            if (Constants.OK.equals(getMainInterface.getFlag())) {
                                if (null != getMainInterface.getData()
                                        .getRecommendProductList()
                                        && 0 < getMainInterface.getData()
                                        .getRecommendProductList().size()) {
                                    if (null != recommendProductListBeanList && recommendProductListBeanList.size() > 0) {
                                        for (int i = 0; i < getMainInterface.getData()
                                                .getRecommendProductList().size(); i++) {
                                            recommendProductListBeanList.add(getMainInterface.getData()
                                                    .getRecommendProductList().get(i));
                                        }
                                        mLoadMoreWrapper.notifyDataSetChanged();
                                    } else {
                                        initRecyclerView();
                                        recommendProductListBeanList = getMainInterface.getData()
                                                .getRecommendProductList();
                                    }
                                } else {
                                    showLongToast(getActivity(),
                                            "没有更多推荐商品了！");
                                }
                                setRecommendProductData();
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
    private void setRecommendProductData() {
        if (1 == currentPage && null != recommendProductListBeanList &&
                recommendProductListBeanList.size() > 0) {
            adapter = new CommonAdapter<GetMainInterface.DataBean.RecommendProductListBean>(
                    getActivity(), R.layout.grid_item_product, recommendProductListBeanList) {
                @Override
                protected void convert(ViewHolder holder,
                                       GetMainInterface.DataBean.RecommendProductListBean
                                               recommendProductListBean, int position) {
                    RequestOptions requestOptions = new RequestOptions()
                            .error(R.drawable.ic_exception)
                            .fallback(R.drawable.ic_exception);
                    Glide.with(getActivity())
                            .load(recommendProductListBean.getUrl() + "?x-oss-process=image/resize,w_"
                                    + (Utils.getScreenWidth(getActivity())))
                            .apply(requestOptions)
                            .into((ImageView) holder.getView(R.id.img_item_product));
                    if (!TextUtils.isEmpty(recommendProductListBean.getPName())) {
                        if (12 < recommendProductListBean.getPName().length()) {
                            holder.setText(R.id.tv_item_product_name,
                                    recommendProductListBean.getPName()
                                            .substring(0, 10) + "···");
                        } else {
                            holder.setText(R.id.tv_item_product_name,
                                    recommendProductListBean.getPName());
                        }
                    } else {
                        holder.setText(R.id.tv_item_product_name, "");
                    }
                    holder.setText(R.id.tv_item_product_price,
                            "¥" + recommendProductListBean.getpNowPrice() + "");
                    holder.setText(R.id.tv_item_product_browse_num,
                            recommendProductListBean.getpBrowseNum() + "次浏览");
                }
            };
            setRecyclerViewHeaderAndFooter(getMainInterface.getData());

            mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);
            mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
            mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    currentPage += 1;
                    getMainInterfacePort();
                }
            });
            recyclerView.setAdapter(mLoadMoreWrapper);
            /**
             * 推荐商品点击事件
             */
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                Utils.LogE(recommendProductListBeanList.get(position - 1).getPName());
                    Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                    intent.putExtra("pid", recommendProductListBeanList.get(position - 1).getPId());
                    startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
        }

    }

    /**
     * 设置RecyclerViewHeaderAndFooter
     */
    private void setRecyclerViewHeaderAndFooter(GetMainInterface.DataBean datas) {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
        /**
         * 设置RecyclerViewHeader
         */
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.index_head_view, null);
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
        for (int i = 0; i < datas.getSlidePicList().size(); i++) {
            urls.add(datas.getSlidePicList().get(i).getPName() + "?x-oss-process=image/resize,w_"
                    + (Utils.getScreenWidth(getActivity())));
        }
        banner.setImages(urls);
        banner.start();

        /**
         * 设置推荐的商品类别
         */
        RecyclerView rv_product_type = view.findViewById(R.id.rv_product_type);
        /**
         * 网格布局
         */
        GridLayoutManager layoutManage = new GridLayoutManager(getActivity(), 4);
        rv_product_type.setLayoutManager(layoutManage);
        CommonAdapter<GetMainInterface.DataBean
                .ProductTypeListBean> productTypeAdapter = new CommonAdapter<GetMainInterface.DataBean
                .ProductTypeListBean>(getActivity(), R.layout.grid_item_product_type,
                datas.getProductTypeList()) {
            @Override
            protected void convert(ViewHolder holder,
                                   GetMainInterface.DataBean.ProductTypeListBean
                                           productTypeListBean,
                                   int position) {
                if (!TextUtils.isEmpty(productTypeListBean.getPtName())) {
                    if ("家具".equals(productTypeListBean.getPtName())) {
                        holder.setImageResource(R.id.img_item_product_type,
                                R.mipmap.icon_jiaju);
                    }
                    if ("家居饰品".equals(productTypeListBean.getPtName())) {
                        holder.setImageResource(R.id.img_item_product_type,
                                R.mipmap.icon_jiajushipin);
                    }
                    if ("家装主材".equals(productTypeListBean.getPtName())) {
                        holder.setImageResource(R.id.img_item_product_type,
                                R.mipmap.icon_jiajuzhucai);
                    }
                    if ("床上用品".equals(productTypeListBean.getPtName())) {
                        holder.setImageResource(R.id.img_item_product_type,
                                R.mipmap.icon_chuangshang);
                    }
                    if ("居家布艺".equals(productTypeListBean.getPtName())) {
                        holder.setImageResource(R.id.img_item_product_type,
                                R.mipmap.icon_buyi);
                    }
                    if ("装修定制".equals(productTypeListBean.getPtName())) {
                        holder.setImageResource(R.id.img_item_product_type,
                                R.mipmap.icon_dingzhi);
                    }
                    holder.setText(R.id.tv_item_product_type_name,
                            productTypeListBean.getPtName());
                } else {
                    holder.setImageResource(R.id.img_item_product_type,
                            R.mipmap.ic_exception);
                    holder.setText(R.id.tv_item_product_type_name, "");
                }
            }
        };
        rv_product_type.setAdapter(productTypeAdapter);
        /**
         * 推荐商品类别点击事件
         */
        productTypeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Utils.LogE(datas.getProductTypeList().get(position).getPtName());
                Intent intent = new Intent(getActivity(), SearchShopActivity.class);
                intent.putExtra("ptId", datas.getProductTypeList().get(position).getPtId());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        /**
         * 设置推荐店铺
         * 需根据返回的推荐店铺的个数判断使用哪个布局
         */
        if (null != datas.getRecommendStoreList()) {
            RelativeLayout rl_one = view.findViewById(R.id.rl_one);
            LinearLayout ll_two = view.findViewById(R.id.ll_two);
            LinearLayout ll_three = view.findViewById(R.id.ll_three);
            /**
             * 返回1个推荐店铺
             */
            if (1 == datas.getRecommendStoreList().size()) {
                rl_one.setVisibility(View.VISIBLE);
                ll_two.setVisibility(View.GONE);
                ll_three.setVisibility(View.GONE);
                ImageView img_item_one_pic = view.findViewById(R.id.img_item_one_pic);
                TextView tv_item_one_content = view.findViewById(R.id.tv_item_one_content);
                TextView tv_item_one_title = view.findViewById(R.id.tv_item_one_title);
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.ic_exception)
                        .error(R.drawable.ic_exception)
                        .fallback(R.drawable.ic_exception);
                Glide.with(getActivity())
                        .load(datas.getRecommendStoreList().get(0).getPicName() + "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(getActivity())))
                        .apply(requestOptions)
                        .into(img_item_one_pic);
                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(0).getRsTitle())) {
//                    if (12 < datas.getRecommendStoreList().get(0).getRsTitle().length()) {
//                        tv_item_one_title.setText(datas.getRecommendStoreList().get(0).getRsTitle()
//                                .substring(0, 10) + "···");
//                    } else {
                    tv_item_one_title.setText(datas.getRecommendStoreList().get(0).getRsTitle());
//                    }
                } else {
                    tv_item_one_title.setText("");
                }
//                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(0).getRsTitle())) {
//                    tv_item_one_title.setText(datas.getRecommendStoreList()
//                            .get(0).getRsTitle());
//                }
                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(0).getRsContent())) {
//                    if (12 < datas.getRecommendStoreList().get(0).getRsContent().length()) {
//                        tv_item_one_content.setText(datas.getRecommendStoreList().get(0).getRsContent()
//                                .substring(0, 10) + "···");
//                    } else {
                    tv_item_one_content.setText(datas.getRecommendStoreList().get(0).getRsContent());
//                    }
                } else {
                    tv_item_one_content.setText("");
                }
//                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(0).getRsContent())) {
//                    tv_item_one_content.setText(datas.getRecommendStoreList()
//                            .get(0).getRsContent());
//                }
                rl_one.setOnClickListener(v -> {
//                    showLongToast(getActivity(), "推荐店铺1");
                    Intent intent = new Intent(getActivity(), ShopActivity.class);
                    intent.putExtra("sId", datas.getRecommendStoreList().get(0).getSId());
                    startActivity(intent);
                });
            }
            /**
             * 返回2个推荐店铺
             */
            if (2 == datas.getRecommendStoreList().size()) {
                rl_one.setVisibility(View.GONE);
                ll_two.setVisibility(View.VISIBLE);
                ll_three.setVisibility(View.GONE);
                ImageView img_item_two_left_pic = view.findViewById(
                        R.id.img_item_two_left_pic);
                TextView tv_item_two_left_content = view.findViewById(
                        R.id.tv_item_two_left_content);
                TextView tv_item_two_left_title = view.findViewById(
                        R.id.tv_item_two_left_title);
                ImageView img_item_two_right_pic = view.findViewById(
                        R.id.img_item_two_right_pic);
                TextView tv_item_two_right_content = view.findViewById(
                        R.id.tv_item_two_right_content);
                TextView tv_item_two_right_title = view.findViewById(
                        R.id.tv_item_two_right_title);
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.ic_exception)
                        .error(R.drawable.ic_exception)
                        .fallback(R.drawable.ic_exception);
                Glide.with(getActivity())
                        .load(datas.getRecommendStoreList().get(0).getPicName() + "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(getActivity())))
                        .apply(requestOptions)
                        .into(img_item_two_left_pic);
                Glide.with(getActivity())
                        .load(datas.getRecommendStoreList().get(1).getPicName() + "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(getActivity())))
                        .apply(requestOptions)
                        .into(img_item_two_right_pic);
                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(0).getRsTitle())) {
//                    if (12 < datas.getRecommendStoreList().get(0).getRsTitle().length()) {
//                        tv_item_two_left_title.setText(datas.getRecommendStoreList().get(0).getRsTitle()
//                                .substring(0, 10) + "···");
//                    } else {
                    tv_item_two_left_title.setText(datas.getRecommendStoreList().get(0).getRsTitle());
//                    }
                } else {
                    tv_item_two_left_title.setText("");
                }
//                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(0).getRsTitle())) {
//                    tv_item_two_left_title.setText(datas.getRecommendStoreList()
//                            .get(0).getRsTitle());
//                }
                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(0).getRsContent())) {
//                    if (12 < datas.getRecommendStoreList().get(0).getRsContent().length()) {
//                        tv_item_two_left_content.setText(datas.getRecommendStoreList().get(0).getRsContent()
//                                .substring(0, 10) + "···");
//                    } else {
                    tv_item_two_left_content.setText(datas.getRecommendStoreList().get(0).getRsContent());
//                    }
                } else {
                    tv_item_two_left_content.setText("");
                }
//                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(0).getRsContent())) {
//                    tv_item_two_left_content.setText(datas.getRecommendStoreList()
//                            .get(0).getRsContent());
//                }
                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(1).getRsTitle())) {
//                    if (8 < datas.getRecommendStoreList().get(1).getRsTitle().length()) {
//                        tv_item_two_right_title.setText(datas.getRecommendStoreList().get(1).getRsTitle()
//                                .substring(0, 7) + "···");
//                    } else {
                    tv_item_two_right_title.setText(datas.getRecommendStoreList().get(1).getRsTitle());
//                    }
                } else {
                    tv_item_two_right_title.setText("");
                }
//                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(1).getRsTitle())) {
//                    tv_item_two_right_title.setText(datas.getRecommendStoreList()
//                            .get(1).getRsTitle());
//                }
                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(1).getRsContent())) {
//                    if (8 < datas.getRecommendStoreList().get(1).getRsContent().length()) {
//                        tv_item_two_right_content.setText(datas.getRecommendStoreList().get(1).getRsContent()
//                                .substring(0, 7) + "···");
//                    } else {
                    tv_item_two_right_content.setText(datas.getRecommendStoreList().get(1).getRsContent());
//                    }
                } else {
                    tv_item_two_right_content.setText("");
                }
//                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(1).getRsContent())) {
//                    tv_item_two_right_content.setText(datas.getRecommendStoreList()
//                            .get(1).getRsContent());
//                }
                img_item_two_left_pic.setOnClickListener(v -> {
//                    showLongToast(getActivity(), "推荐店铺2左边");
                    Intent intent = new Intent(getActivity(), ShopActivity.class);
                    intent.putExtra("sId", datas.getRecommendStoreList().get(0).getSId());
                    startActivity(intent);
                });
                img_item_two_right_pic.setOnClickListener(v -> {
//                    showLongToast(getActivity(), "推荐店铺2右边");
                    Intent intent = new Intent(getActivity(), ShopActivity.class);
                    intent.putExtra("sId", datas.getRecommendStoreList().get(1).getSId());
                    startActivity(intent);
                });
            }
            /**
             * 返回3个推荐店铺
             */
            if (3 == datas.getRecommendStoreList().size()) {
                rl_one.setVisibility(View.GONE);
                ll_two.setVisibility(View.GONE);
                ll_three.setVisibility(View.VISIBLE);
                ImageView img_item_three_left_pic = view.findViewById(
                        R.id.img_item_three_left_pic);
                TextView tv_item_three_left_content = view.findViewById(
                        R.id.tv_item_three_left_content);
                TextView tv_item_three_left_title = view.findViewById(
                        R.id.tv_item_three_left_title);
                ImageView img_item_three_top_pic = view.findViewById(
                        R.id.img_item_three_top_pic);
                TextView tv_item_three_top_content = view.findViewById(
                        R.id.tv_item_three_top_content);
                TextView tv_item_three_top_title = view.findViewById(
                        R.id.tv_item_three_top_title);
                ImageView img_item_three_bottom_pic = view.findViewById(
                        R.id.img_item_three_bottom_pic);
                TextView tv_item_three_bottom_content = view.findViewById(
                        R.id.tv_item_three_bottom_content);
                TextView tv_item_three_bottom_title = view.findViewById(
                        R.id.tv_item_three_bottom_title);
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.ic_exception)
                        .error(R.drawable.ic_exception)
                        .fallback(R.drawable.ic_exception);
                Glide.with(getActivity())
                        .load(datas.getRecommendStoreList().get(0).getPicName() + "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(getActivity())))
                        .apply(requestOptions)
                        .into(img_item_three_left_pic);
                Glide.with(getActivity())
                        .load(datas.getRecommendStoreList().get(1).getPicName() + "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(getActivity())))
                        .apply(requestOptions)
                        .into(img_item_three_top_pic);
                Glide.with(getActivity())
                        .load(datas.getRecommendStoreList().get(2).getPicName() + "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(getActivity())))
                        .apply(requestOptions)
                        .into(img_item_three_bottom_pic);
                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(0).getRsTitle())) {
//                    if (12 < datas.getRecommendStoreList().get(0).getRsTitle().length()) {
//                        tv_item_three_left_title.setText(datas.getRecommendStoreList().get(0).getRsTitle()
//                                .substring(0, 10) + "···");
//                    } else {
                    tv_item_three_left_title.setText(datas.getRecommendStoreList().get(0).getRsTitle());
//                    }
                } else {
                    tv_item_three_left_title.setText("");
                }
//                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(0).getRsTitle())) {
//                    tv_item_three_left_title.setText(datas.getRecommendStoreList()
//                            .get(0).getRsTitle());
//                }
                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(0).getRsContent())) {
//                    if (12 < datas.getRecommendStoreList().get(0).getRsContent().length()) {
//                        tv_item_three_left_content.setText(datas.getRecommendStoreList().get(0).getRsContent()
//                                .substring(0, 10) + "···");
//                    } else {
                    tv_item_three_left_content.setText(datas.getRecommendStoreList().get(0).getRsContent());
//                    }
                } else {
                    tv_item_three_left_content.setText("");
                }
//                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(0).getRsContent())) {
//                    tv_item_three_left_content.setText(datas.getRecommendStoreList()
//                            .get(0).getRsContent());
//                }
                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(1).getRsTitle())) {
//                    if (8 < datas.getRecommendStoreList().get(1).getRsTitle().length()) {
//                        tv_item_three_top_title.setText(datas.getRecommendStoreList().get(1).getRsTitle()
//                                .substring(0, 7) + "···");
//                    } else {
                    tv_item_three_top_title.setText(datas.getRecommendStoreList().get(1).getRsTitle());
//                    }
                } else {
                    tv_item_three_top_title.setText("");
                }
//                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(1).getRsTitle())) {
//                    tv_item_three_top_title.setText(datas.getRecommendStoreList()
//                            .get(1).getRsTitle());
//                }
                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(1).getRsContent())) {
//                    if (8 < datas.getRecommendStoreList().get(1).getRsContent().length()) {
//                        tv_item_three_top_content.setText(datas.getRecommendStoreList().get(1).getRsContent()
//                                .substring(0, 7) + "···");
//                    } else {
                    tv_item_three_top_content.setText(datas.getRecommendStoreList().get(1).getRsContent());
//                    }
                } else {
                    tv_item_three_top_content.setText("");
                }
//                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(1).getRsContent())) {
//                    tv_item_three_top_content.setText(datas.getRecommendStoreList()
//                            .get(1).getRsContent());
//                }
                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(2).getRsTitle())) {
//                    if (8 < datas.getRecommendStoreList().get(2).getRsTitle().length()) {
//                        tv_item_three_bottom_title.setText(datas.getRecommendStoreList().get(2).getRsTitle()
//                                .substring(0, 7) + "···");
//                    } else {
                    tv_item_three_bottom_title.setText(datas.getRecommendStoreList().get(2).getRsTitle());
//                    }
                } else {
                    tv_item_three_bottom_title.setText("");
                }
//                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(2).getRsTitle())) {
//                    tv_item_three_bottom_title.setText(datas.getRecommendStoreList()
//                            .get(2).getRsTitle());
//                }
                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(2).getRsContent())) {
//                    if (8 < datas.getRecommendStoreList().get(2).getRsContent().length()) {
//                        tv_item_three_bottom_content.setText(datas.getRecommendStoreList().get(2).getRsContent()
//                                .substring(0, 7) + "···");
//                    } else {
                    tv_item_three_bottom_content.setText(datas.getRecommendStoreList().get(2).getRsContent());
//                    }
                } else {
                    tv_item_three_bottom_content.setText("");
                }
//                if (!TextUtils.isEmpty(datas.getRecommendStoreList().get(2).getRsContent())) {
//                    tv_item_three_bottom_content.setText(datas.getRecommendStoreList()
//                            .get(2).getRsContent());
//                }
                img_item_three_left_pic.setOnClickListener(v -> {
//                    showLongToast(getActivity(), "推荐店铺3左边");
                    Intent intent = new Intent(getActivity(), ShopActivity.class);
                    intent.putExtra("sId", datas.getRecommendStoreList().get(0).getSId());
                    startActivity(intent);
                });
                img_item_three_top_pic.setOnClickListener(v -> {
//                    showLongToast(getActivity(), "推荐店铺3上边");
                    Intent intent = new Intent(getActivity(), ShopActivity.class);
                    intent.putExtra("sId", datas.getRecommendStoreList().get(1).getSId());
                    startActivity(intent);
                });
                img_item_three_bottom_pic.setOnClickListener(v -> {
//                    showLongToast(getActivity(), "推荐店铺3下边");
                    Intent intent = new Intent(getActivity(), ShopActivity.class);
                    intent.putExtra("sId", datas.getRecommendStoreList().get(2).getSId());
                    startActivity(intent);
                });
            }
        }
        /**
         * 设置RecyclerViewFooter
         */
//        View footerView = LayoutInflater.from(getActivity()).inflate(
//                R.layout.common_foot_view,null);
        mHeaderAndFooterWrapper.addHeaderView(view);
//        mHeaderAndFooterWrapper.addFootView(footerView);
    }

    /**
     * 点击异常情况界面，重新调用接口刷新数据
     */
    @Override
    public void clickEvent() {
        super.clickEvent();
        getMainInterfacePort();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_city, R.id.tv_publish, R.id.tv_search})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_city:
                intent = new Intent(getActivity(), ChangeCityActivity.class);
                intent.putExtra("intentTag", "3");
                startActivity(intent);
                break;
            case R.id.tv_publish:
                if (TextUtils.isEmpty(sharePreferenceUtil.getUID())) {
                    quitAccount();
                    return;
                }
                intent = new Intent(getActivity(), SendDemandActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_search:
                intent = new Intent(getActivity(), SearchShopActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 清空存储的用户信息并跳转至登录界面
     */
    private void quitAccount() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 注册网易云未读数监听
     * false 注销
     */
    private void sysMsgUnreadCount() {
        NIMClient.getService(SystemMessageObserver.class)
                .observeUnreadCountChange(sysMsgUnreadCountChangedObserver, true);

    }

    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            // 更新未读数变化
        }
    };

    /**
     * 注册网易云系统通知监听
     * false 注销
     */
    private void systemMessage() {
        NIMClient.getService(SystemMessageObserver.class)
                .observeReceiveSystemMsg(new Observer<SystemMessage>() {
                    @Override
                    public void onEvent(SystemMessage message) {
                        // 收到系统通知，可以做相应操作
                    }
                }, true);
    }

    //DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
    //开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
        String appKey = readAppKey(getActivity());
        boolean isDemo = "45c6af3c98409b18a84451215d0bdd6e".equals(appKey)
                || "fe416640c8e8a72734219e1847ad2547".equals(appKey);

        return isDemo ? MD5.getStringMD5(password) : password;
    }

    private static String readAppKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通知栏权限已打开时不处理，未打开时显示是否去设置弹窗
     */
    private void setNotificationPermission() {
        if (NotificationsUtils.isNotificationEnabled(getActivity())) {
            if (sharePreferenceUtil.getNotificatonPermissionTag() == 0) {
                showNotificationPermissionDialog();
            }
        }
    }

    /**
     * 通知栏权限跳转弹窗
     */
    private void showNotificationPermissionDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(getActivity(), R.style.Theme_Transparent).
                setView(view).
                setCancelable(false).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        tv_live_yes.setText("不在提醒");
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        tv__live_never_reminds.setText("我知道了");
        TextView tv_live_content = view.findViewById(R.id.tv_live_content);
        tv_live_content.setText("为了您更好的使用体验，请前往“设置”--“应用管理”修改应用通知设置。");

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
