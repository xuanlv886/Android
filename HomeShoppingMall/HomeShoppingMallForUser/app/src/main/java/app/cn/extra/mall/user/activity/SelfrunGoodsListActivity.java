package app.cn.extra.mall.user.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.fragment.ScreenMenuFragment;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.utils.Util;
import app.cn.extra.mall.user.view.CustomDialog;
import app.cn.extra.mall.user.view.PopMenu;
import app.cn.extra.mall.user.view.ViewHolder;
import app.cn.extra.mall.user.vo.DogetProductByFilter;
import app.cn.extra.mall.user.vo.Ppid;
import app.cn.extra.mall.user.vo.SelfrunMarketProListGetProperty;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import cfkj.app.cn.cfkjcommonlib.view.ReplaceViewHelper.VaryViewHelperController;

/**
 * 商品列表页面
 */
public class SelfrunGoodsListActivity extends SlidingFragmentActivity implements SwipyRefreshLayout.OnRefreshListener, View.OnClickListener {
    //    private String marketName;
    private TextView tv_title, selfTvNature, selfTvDelivery, selfTvBrand, selfTvCrowd, tvPaixu, tv_quick, tv_shaixuan;
    private RelativeLayout rlPaixu, rlQuick, rlShaixuan;
    private SlidingMenu menu;
    private ImageView img_back, imgPaixu;
    private PopMenu popMenu;
    private LinearLayout llServiecLoad;
    private LinearLayout llTemp;
    private View viewTemp;
    RecyclerView recyclerView;
    SwipyRefreshLayout refreshLayout;
    ProgressBar progressBar;
    private CommonAdapter<DogetProductByFilter.DataBean.ProductListBean> adapater;
    private NatureAdapater mAdapater;
    private List<Integer> tags = new ArrayList<>();
    private List<Integer> tags1 = new ArrayList<>();
    private List<Integer> tags2 = new ArrayList<>();
    private List<Integer> tags3 = new ArrayList<>();
    private List<HashMap<Integer, String>> nature = new ArrayList<HashMap<Integer, String>>();
    private int nature_tag = 0, delivery_tag = 0, brand_tag = 0, crowd_tag = 0;
    private PopupWindow popupWindow;
    private CustomDialog customDialog = new CustomDialog();
    private String markeiditem;
    private SelfrunMarketProListGetProperty.DataBean proData;
    private int TagsIndex;
    private String parameterData;
    private SharePreferenceUtil sp;
    private String brandName = "";
    private String sortType = 0 + "";
    private HashMap<Integer, String> map;
    private String BrandName;
    private ArrayList<String> list = new ArrayList<>();
    private Ppid ppid;
    /**
     * 当前页，用于分页获取数据 从1开始
     */
    private int currentPage = 1;
    /**
     * 判断是下拉更新还是上拉加载更多 false--下拉更新， true--上拉加载更多
     */
    private boolean loadMore = false;
    private String ptdId;
    private List<DogetProductByFilter.DataBean.ProductListBean> datas;
    private String Tags;
    private String saleSort = "";
    private final String ACTION_CHANGE_ARROW = "com.action.change.arrow";  // 改变箭头方向
    public static Context context;
    String looseName = "";
    private VaryViewHelperController varyViewHelperController;
    public String[] actions = {"有数据", "暂无数据", "加载中",
            "数据获取失败", "网络链接异常", "网络链接不稳定"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfrun_goods_list);
        context = this;
        Intent intent = getIntent();

        if (!TextUtils.isEmpty(intent.getStringExtra("marketName"))) {
            brandName = intent.getStringExtra("marketName");
        }
        markeiditem = intent.getStringExtra("markeiditem");
        ptdId = intent.getStringExtra("ptdId");
        looseName = intent.getStringExtra("looseName");
//        setStatusBar();
        initView();
        // 初始化右侧筛选菜单
        initRightMenu();
        //注册广播
        registerBoradcastReceiver();

    }

    private void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_CHANGE_ARROW);
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_CHANGE_ARROW)) {
                imgPaixu.setImageResource(R.mipmap.icon_fenleixiala_nor);
            }
        }
    };

    private void initRightMenu() {
        Fragment rightMenuFragment = new ScreenMenuFragment();
        Bundle arg = new Bundle();
        arg.putString("ptdId", ptdId);
        arg.putString("sortType", sortType);
        arg.putInt("tags", 0);
        if (list.size() != 0) {
            ppid = new Ppid();
            ppid.setData(list);
            arg.putSerializable("list", ppid);
        }
        rightMenuFragment.setArguments(arg);
        setBehindContentView(R.layout.rightmenu_frame);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.id_right_menu_frame, rightMenuFragment).commit();
        // configure the SlidingMenu
        menu = getSlidingMenu();
        menu.setMode(SlidingMenu.RIGHT);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        menu.setOffsetFadeDegree(0.5f);
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        refreshLayout = (SwipyRefreshLayout) findViewById(R.id.refreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        rlShaixuan = (RelativeLayout) findViewById(R.id.rl_shaixuan);
        rlQuick = (RelativeLayout) findViewById(R.id.rl_quick);
        rlPaixu = (RelativeLayout) findViewById(R.id.rl_paixu);
        llTemp = (LinearLayout) findViewById(R.id.ll_temp);
        tv_shaixuan = (TextView) findViewById(R.id.tv_shaixuan);
        tv_quick = (TextView) findViewById(R.id.tv_quick);
        tvPaixu = (TextView) findViewById(R.id.tv_paixu);
        llServiecLoad = (LinearLayout) findViewById(R.id.ll_service_load);
        rlQuick.setOnClickListener(this);
        sp = new SharePreferenceUtil(SelfrunGoodsListActivity.this, Constants.SAVE_USER);
        selfTvCrowd = (TextView) findViewById(R.id.self_tv_crowd);
        selfTvCrowd.setOnClickListener(this);
        selfTvBrand = (TextView) findViewById(R.id.self_tv_brand);
        selfTvBrand.setOnClickListener(this);
        selfTvDelivery = (TextView) findViewById(R.id.self_tv_delivery);
        selfTvDelivery.setOnClickListener(this);
        selfTvNature = (TextView) findViewById(R.id.self_tv_nature);
        selfTvNature.setOnClickListener(this);
        imgPaixu = (ImageView) findViewById(R.id.img_paixu);
        rlPaixu.setOnClickListener(this);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        rlShaixuan.setOnClickListener(this);
        viewTemp = findViewById(R.id.view_temp);
        sp = new SharePreferenceUtil(SelfrunGoodsListActivity.this, Constants.SAVE_USER);
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
        dogetProductByFilterPort(currentPage, looseName);
    }

    public VaryViewHelperController createCaseViewHelperController(View view) {
        return new VaryViewHelperController(view)
                .setUpEmptyView(getView(actions[1], Color.parseColor("#ff33b5e5")))
                .setUpLoadingView(getView(actions[2], Color.parseColor("#ffff4444")))
                .setUpErrorView(getView(actions[3], Color.parseColor("#ffffbb33")))
                .setUpNetworkErrorView(getView(actions[4], Color.parseColor("#ff99cc00")))
                .setUpNetworkPoorView(getView(actions[5], Color.parseColor("#ffff4444")))
                .setUpRefreshListener(refreshListener);
    }

    View.OnClickListener refreshListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dogetProductByFilterPort(currentPage, looseName);
        }
    };

    private View getView(String action, int color) {
        View view = LayoutInflater.from(this).inflate(cfkj.app.cn.cfkjcommonlib.R.layout.layout_varyview, null);
        TextView tvAction = view.findViewById(cfkj.app.cn.cfkjcommonlib.R.id.action);
        tvAction.setText(action);
        tvAction.setTextColor(color);
        return view;
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 网格布局
         */
        LinearLayoutManager layoutManage = new LinearLayoutManager(SelfrunGoodsListActivity.this);
        recyclerView.setLayoutManager(layoutManage);
//        /**
//         * 添加分隔线
//         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(SelfrunGoodsListActivity.this,
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_shaixuan:
                menu.showMenu();
                Fragment rightMenuFragment = new ScreenMenuFragment();
                Bundle arg = new Bundle();
                arg.putString("ptdId", ptdId);
                arg.putString("sortType", sortType);
                arg.putInt("tags", 1);
                arg.putString("parameterData", parameterData);
                ppid = new Ppid();
                ppid.setData(list);
                arg.putSerializable("list", ppid);
                arg.putString("Tags", Tags);

//                if (proData.getSize() == 3 && tags.size() != 0) {
//                    String BrandName = self_tv_brand.getText().toString();
//                    arg.putString("brand", BrandName);
//                } else {
//                    String BrandName = "";
//                    arg.putString("brand", BrandName);
//                }

                rightMenuFragment.setArguments(arg);
                setBehindContentView(R.layout.rightmenu_frame);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.id_right_menu_frame, rightMenuFragment).commit();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_paixu:
                //综合排序
                butIntegratedClicked(view);
                break;
            case R.id.self_tv_brand:
                if (nature_tag == 0) {
                    if (proData.getSize() != 3) {
                        TagsIndex = 0;
                        showNaturePopup(view, proData, 0, TagsIndex);
                    } else {
                        TagsIndex = 0;
                        showNaturePopup(view, proData, -1, TagsIndex);
                    }

                    selfTvBrand.setBackgroundResource(R.drawable.selfrun_text_bg_nor);
                    nature_tag = 1;
                } else {
                    popupWindow.dismiss();
                    nature_tag = 0;
                    selfTvBrand.setBackgroundResource(R.drawable.selfrun_text_bg);
                }
                break;
            case R.id.self_tv_delivery:
                if (delivery_tag == 0) {
                    if (proData.getSize() != 3) {
                        TagsIndex = 1;
                        showNaturePopup(view, proData, 1, TagsIndex);
                    } else {
                        TagsIndex = 1;
                        showNaturePopup(view, proData, 0, TagsIndex);
                    }
                    selfTvDelivery.setBackgroundResource(R.drawable.selfrun_text_bg_nor);
                    delivery_tag = 1;
                } else {
                    delivery_tag = 0;
                    popupWindow.dismiss();
                    selfTvDelivery.setBackgroundResource(R.drawable.selfrun_text_bg);
                }
                break;
            case R.id.self_tv_nature:
                if (brand_tag == 0) {
                    if (proData.getSize() != 3) {
                        TagsIndex = 2;
                        showNaturePopup(view, proData, 2, TagsIndex);
                    } else {
                        TagsIndex = 2;
                        showNaturePopup(view, proData, 1, TagsIndex);
                    }
                    selfTvNature.setBackgroundResource(R.drawable.selfrun_text_bg_nor);
                    brand_tag = 1;
                } else {
                    brand_tag = 0;
                    popupWindow.dismiss();
                    selfTvNature.setBackgroundResource(R.drawable.selfrun_text_bg);
                }
                break;
            case R.id.self_tv_crowd:
                if (crowd_tag == 0) {
                    if (proData.getSize() != 3) {
                        TagsIndex = 3;
                        showNaturePopup(view, proData, 3, TagsIndex);
                    } else {
                        TagsIndex = 3;
                        showNaturePopup(view, proData, 2, TagsIndex);
                    }
                    selfTvCrowd.setBackgroundResource(R.drawable.selfrun_text_bg_nor);
                    crowd_tag = 1;
                } else {
                    crowd_tag = 0;
                    popupWindow.dismiss();
                    selfTvCrowd.setBackgroundResource(R.drawable.selfrun_text_bg);
                }
                break;
            case R.id.rl_quick:
                if (datas != null && datas.size() != 0) {
                    Intent intent = new Intent();
                    intent.setClass(SelfrunGoodsListActivity.this, QuickActivity.class);
                    intent.putExtra("ptdId", ptdId);
                    intent.putExtra("parameterData", parameterData);
                    startActivity(intent);
                } else {
                    CustomToast.showToast(SelfrunGoodsListActivity.this,
                            "当前类别下暂无商品", Toast.LENGTH_SHORT);
                }

                break;
            default:
                break;
        }
    }

    /**
     * 获取所有商品信息接口
     *
     * @param currentPage 当前页
     * @param looseName   搜索框内容
     */
    private void dogetProductByFilterPort(int currentPage, String looseName) {
        String size = 10 + "";
        String userId = sp.getUID();
        if (TextUtils.isEmpty(parameterData)) {
            parameterData = "";
        }
        if (TextUtils.isEmpty(looseName)) {
            looseName = "";
        }
        if (Utils.isNetworkAvailable(SelfrunGoodsListActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.getProductList)
                    .addParams("ptdId", ptdId)
                    .addParams("priceSort", sortType)
                    .addParams("saleSort", saleSort)
                    .addParams("currentPage", currentPage + "")
                    .addParams("size", size)
                    .addParams("parameterData", parameterData)
                    .addParams("uId", userId)
                    .addParams("looseName", looseName)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(okhttp3.Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            e.printStackTrace();
                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Util.logE("dogetProductByFilter---", response);
                            DogetProductByFilter productByFilter = new DogetProductByFilter();
                            productByFilter = Utils.parserJsonResult(response, DogetProductByFilter.class);
                            /**
                             * 恢复显示数据的View
                             */
                            varyViewHelperController.restore();
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            if ("true".equals(productByFilter.getFlag())) {
                                if (!loadMore) {
                                    datas = productByFilter.getData().getProductList();
                                    if (datas != null && datas.size() > 0) {
                                        adapater = new CommonAdapter<DogetProductByFilter.DataBean.ProductListBean>(SelfrunGoodsListActivity.this, R.layout.list_item_add_brand, datas) {
                                            @Override
                                            protected void convert(com.zhy.adapter.recyclerview.base.ViewHolder holder, DogetProductByFilter.DataBean.ProductListBean productListBean, int position) {
                                                holder.getView(R.id.tv_money_ago).setVisibility(View.VISIBLE);
                                                holder.setText(R.id.tv_money_ago, "¥" + productListBean.getPOriginalPrice() + "");
                                                TextView tvMoneyAgo = holder.getView(R.id.tv_money_ago);
                                                tvMoneyAgo.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                                                holder.setText(R.id.item_tv_shop_price, "¥" + productListBean.getPNowPrice() + "");
                                                holder.setTextColor(R.id.item_tv_shop_price, getResources().getColor(R.color.orange));
                                                holder.setText(R.id.item_tv_shop_recommend, "立即购买");
                                                holder.setText(R.id.item_tv_shop_name, productListBean.getPName());
                                                holder.setText(R.id.item_tv_shop_introduce, productListBean.getPDescribe());
                                                Picasso.with(SelfrunGoodsListActivity.this).load(productListBean.getPicName())
                                                        .placeholder(R.mipmap.ic_exception).into((ImageView) holder.getView(R.id.item_iv_shop_pic));
                                                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        Intent intent = new Intent();
                                                        intent.setClass(SelfrunGoodsListActivity.this, GoodsDetailActivity.class);
                                                        intent.putExtra("pid", productListBean.getPId());
                                                        intent.putExtra("tags", 1);
                                                        startActivity(intent);
                                                    }
                                                });
                                            }
                                        };
                                        recyclerView.setAdapter(adapater);
                                    } else {
                                        varyViewHelperController.showEmptyView();
                                    }
                                } else {
                                    datas.addAll(productByFilter.getData().getProductList());
                                    adapater.notifyDataSetChanged();
                                    if (productByFilter.getData().getProductList().size() == 0) {
                                        CustomToast.showToast(SelfrunGoodsListActivity.this,
                                                "已经到底了，没有更多商品了", Toast.LENGTH_SHORT);
                                    }
                                }
                            } else {
                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
            varyViewHelperController.showNetworkErrorView();
        }
    }


    private void SelfrunMarketProListGetPropertyPort() {
        if (Utils.isNetworkAvailable(SelfrunGoodsListActivity.this)) {
            OkHttpUtils
                    .post()
                    .url("")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addParams("marketId", markeiditem)
                    .addParams("ptdId", ptdId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(okhttp3.Call call, Exception e, int id) {
                            if (customDialog != null && customDialog.isShow()) {
                                customDialog.dismissDialog();
                            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Util.logE("marketProList--", response);
                            SelfrunMarketProListGetProperty marketProList = new SelfrunMarketProListGetProperty();
                            marketProList = Utils.parserJsonResult(response, SelfrunMarketProListGetProperty.class);
                            if (customDialog != null && customDialog.isShow()) {
                                customDialog.dismissDialog();
                            }
                            if ("true".equals(marketProList.getFlag())) {
                                proData = marketProList.getData();
                                if (proData.getBrandList() != null && proData.getBrandList().size() > 0) {
                                    viewTemp.setVisibility(View.VISIBLE);
                                    llTemp.setVisibility(View.VISIBLE);
                                }
                                if (proData.getPropertyList() != null && proData.getPropertyList().size() > 0) {
                                    viewTemp.setVisibility(View.VISIBLE);
                                }
                                if (3 != proData.getSize()) { // 没有品牌
                                    if (proData.getPropertyList().size() == 0) {
                                        viewTemp.setVisibility(View.GONE);
                                    }
                                    for (int i = 0; i < proData.getPropertyList().size(); i++) {
                                        if (4 <= i) {
                                            return;
                                        }
                                        if (0 == i) {
                                            if (!TextUtils.isEmpty(proData.getPropertyList().get(0).getPropertyName())) {
                                                selfTvBrand.setVisibility(View.VISIBLE);
                                                selfTvBrand.setText(proData.getPropertyList().get(0).getPropertyName());
                                            } else {
                                                selfTvBrand.setVisibility(View.GONE);
                                            }
                                        }
                                        if (1 == i) {
                                            if (!TextUtils.isEmpty(proData.getPropertyList().get(1).getPropertyName())) {
                                                selfTvDelivery.setVisibility(View.VISIBLE);
                                                selfTvDelivery.setText(proData.getPropertyList().get(1).getPropertyName());
                                            } else {
                                                selfTvDelivery.setVisibility(View.GONE);
                                            }
                                        }
                                        if (2 == i) {
                                            if (!TextUtils.isEmpty(proData.getPropertyList().get(2).getPropertyName())) {
                                                selfTvNature.setVisibility(View.VISIBLE);
                                                selfTvNature.setText(proData.getPropertyList().get(2).getPropertyName());
                                            } else {
                                                selfTvNature.setVisibility(View.GONE);
                                            }
                                        }
                                        if (3 == i) {
                                            if (!TextUtils.isEmpty(proData.getPropertyList().get(3).getPropertyName())) {
                                                selfTvCrowd.setVisibility(View.VISIBLE);
                                                selfTvCrowd.setText(proData.getPropertyList().get(3).getPropertyName());
                                            } else {
                                                selfTvCrowd.setVisibility(View.GONE);
                                            }
                                        }
                                    }
                                } else { // 有品牌
                                    selfTvBrand.setVisibility(View.VISIBLE);
                                    selfTvBrand.setText("品牌");
                                    for (int i = 0; i < proData.getPropertyList().size(); i++) {
                                        if (4 <= i) {
                                            return;
                                        }
                                        if (0 == i) {
                                            if (proData.getPropertyList().size() != 0) {
                                                selfTvDelivery.setVisibility(View.VISIBLE);
                                                selfTvDelivery.setText(proData.getPropertyList().get(0).getPropertyName());
                                            } else {
                                                selfTvDelivery.setVisibility(View.GONE);
                                            }
                                        }
                                        if (1 == i) {
                                            if (proData.getPropertyList().size() != 0) {
                                                selfTvNature.setVisibility(View.VISIBLE);
                                                selfTvNature.setText(proData.getPropertyList().get(1).getPropertyName());
                                            } else {
                                                selfTvNature.setVisibility(View.GONE);
                                            }
                                        }
                                        if (2 == i) {
                                            if (proData.getPropertyList().size() != 0) {
                                                selfTvCrowd.setVisibility(View.VISIBLE);
                                                selfTvCrowd.setText(proData.getPropertyList().get(2).getPropertyName());
                                            } else {
                                                selfTvCrowd.setVisibility(View.GONE);
                                            }
                                        }
                                    }

                                }
                            } else {
                                CustomToast.showToast(SelfrunGoodsListActivity.this,
                                        marketProList.getErrorString(), Toast.LENGTH_SHORT);

                            }
                        }
                    });
        } else {
            CustomToast.showToast(SelfrunGoodsListActivity.this,
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);
        }
    }

    /**
     * 商品属性的popupwondow
     */
    private void showNaturePopup(View view, final SelfrunMarketProListGetProperty.DataBean proData, int i, final int TagsIndex) {
        View views = View.inflate(this, R.layout.nature_common, null);
        //获取PopupWindow中View的宽高
        popupWindow = new PopupWindow(views, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        views.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setFocusable(true);//popupwindow设置焦点
        popupWindow.setOutsideTouchable(true);//点击外面窗口消失
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        popupWindow.setBackgroundDrawable(dw);
        if (Build.VERSION.SDK_INT >= 24) {
            //获取目标控件在屏幕中的坐标位置
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            if (Build.VERSION.SDK_INT == 25) {
                WindowManager wm = (WindowManager) popupWindow.getContentView().getContext().getSystemService(Context.WINDOW_SERVICE);
                int screenHeight = wm.getDefaultDisplay().getHeight();
                popupWindow.setHeight(screenHeight - location[1] - view.getHeight());
            }
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, y + view.getHeight());
        } else {
            popupWindow.showAsDropDown(view);
        }

        GridView nature_gv_goods = (GridView) views.findViewById(R.id.nature_gv_goods);
        mAdapater = new NatureAdapater(proData, i, TagsIndex);
        nature_gv_goods.setAdapter(mAdapater);
        Button nature_reset = (Button) views.findViewById(R.id.nature_reset);
        Button nature_confirm = (Button) views.findViewById(R.id.nature_confirm);
        ImageView nature_tv = (ImageView) views.findViewById(R.id.nature_button);
        /**
         * 提交
         */
        nature_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map = new HashMap<>(1);
                if (proData.getSize() != 3) {
                    if (tags.size() != 0 && TagsIndex == 0) {
                        for (int i = 0; i < nature.size(); i++) {
                            if (nature.get(i).containsKey(0)) {
                                nature.remove(i);
                            }
                        }
                        map.put(0, proData.getPropertyList().get(0).getPropertyValueList().get(tags.get(0)).getPropertyId());
                        nature.add(map);
                        selfTvBrand.setText(proData.getPropertyList().get(0).getPropertyValueList().get(tags.get(0)).getPropertyValue());
                    }
                    if (tags1.size() != 0 && TagsIndex == 1) {
                        for (int i = 0; i < nature.size(); i++) {
                            if (nature.get(i).containsKey(1)) {
                                nature.remove(i);
                            }
                        }
                        map.put(1, proData.getPropertyList().get(0).getPropertyValueList().get(tags1.get(0)).getPropertyId());
                        nature.add(map);
                        selfTvDelivery.setText(proData.getPropertyList().get(1).getPropertyValueList().get(tags1.get(0)).getPropertyValue());
                    }
                    if (tags2.size() != 0 && TagsIndex == 2) {
                        for (int i = 0; i < nature.size(); i++) {
                            if (nature.get(i).containsKey(2)) {
                                nature.remove(i);
                            }
                        }
                        map.put(2, proData.getPropertyList().get(0).getPropertyValueList().get(tags2.get(0)).getPropertyId());
                        nature.add(map);
                        selfTvNature.setText(proData.getPropertyList().get(2).getPropertyValueList().get(tags2.get(0)).getPropertyValue());
                    }
                    if (tags3.size() != 0 && TagsIndex == 3) {
                        for (int i = 0; i < nature.size(); i++) {
                            if (nature.get(i).containsKey(3)) {
                                nature.remove(i);
                            }
                        }
                        map.put(3, proData.getPropertyList().get(0).getPropertyValueList().get(tags3.get(0)).getPropertyId());
                        nature.add(map);
                        selfTvCrowd.setText(proData.getPropertyList().get(3).getPropertyValueList().get(tags3.get(0)).getPropertyValue());
                    }
                } else {
                    if (tags.size() != 0 && TagsIndex == 0) {
                        selfTvBrand.setText(proData.getBrandList().get(tags.get(0)));
                    }
                    if (tags1.size() != 0 && TagsIndex == 1) {
                        for (int i = 0; i < nature.size(); i++) {
                            if (nature.get(i).containsKey(1)) {
                                nature.remove(i);
                            }
                        }
                        map.put(1, proData.getPropertyList().get(0).getPropertyValueList().get(tags1.get(0)).getPropertyId());
                        nature.add(map);
                        selfTvDelivery.setText(proData.getPropertyList().get(0).getPropertyValueList().get(tags1.get(0)).getPropertyValue());
                    }
                    if (tags2.size() != 0 && TagsIndex == 2) {
                        for (int i = 0; i < nature.size(); i++) {
                            if (nature.get(i).containsKey(2)) {
                                nature.remove(i);
                            }
                        }
                        map.put(2, proData.getPropertyList().get(1).getPropertyValueList().get(tags2.get(0)).getPropertyId());
                        nature.add(map);
                        selfTvNature.setText(proData.getPropertyList().get(1).getPropertyValueList().get(tags2.get(0)).getPropertyValue());
                    }
                    if (tags3.size() != 0 && TagsIndex == 3) {
                        for (int i = 0; i < nature.size(); i++) {
                            if (nature.get(i).containsKey(3)) {
                                nature.remove(i);
                            }
                        }
                        map.put(3, proData.getPropertyList().get(2).getPropertyValueList().get(tags3.get(0)).getPropertyId());
                        nature.add(map);
                        selfTvCrowd.setText(proData.getPropertyList().get(2).getPropertyValueList().get(tags3.get(0)).getPropertyValue());
                    }
                }
                List<String> ppid = new ArrayList<>();
                list.clear();
                for (HashMap<Integer, String> hm : nature) {
                    Set<Integer> set = hm.keySet();
                    for (Integer key : set) {
                        String Stringvalue = hm.get(key);
                        ppid.add(Stringvalue);
                        list.add(Stringvalue);
                    }
                }
                Gson gson = new Gson();
                String toJson = gson.toJson(ppid);
                String name = selfTvBrand.getText().toString().trim();
                if ("品牌".equals(name)) {
                    brandName = "";
                } else if (proData.getSize() != 3) {
                    brandName = "";
                } else {
                    brandName = selfTvBrand.getText().toString().trim();
                }
                DoScreenPort(toJson);
                popupWindow.dismiss();
            }
        });
        nature_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                RestoreBg();
            }
        });
        /**
         * 重置 清空选中项
         */
        nature_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (proData.getSize() != 3) {
                    if (TagsIndex == 0) {
                        tags.clear();
                        for (int i = 0; i < nature.size(); i++) {
                            if (nature.get(i).containsKey(0)) {
                                nature.remove(i);
                            }
                        }
                        selfTvBrand.setText(proData.getPropertyList().get(0).getPropertyName());
                    } else if (TagsIndex == 1) {
                        tags1.clear();
                        for (int i = 0; i < nature.size(); i++) {
                            if (nature.get(i).containsKey(1)) {
                                nature.remove(i);
                            }
                        }
                        selfTvDelivery.setText(proData.getPropertyList().get(1).getPropertyName());
                    } else if (TagsIndex == 2) {
                        tags2.clear();
                        for (int i = 0; i < nature.size(); i++) {
                            if (nature.get(i).containsKey(2)) {
                                nature.remove(i);
                            }
                        }
                        selfTvNature.setText(proData.getPropertyList().get(2).getPropertyName());
                    } else if (TagsIndex == 3) {
                        tags3.clear();
                        for (int i = 0; i < nature.size(); i++) {
                            if (nature.get(i).containsKey(3)) {
                                nature.remove(i);
                            }
                        }
                        selfTvCrowd.setText(proData.getPropertyList().get(3).getPropertyName());
                    }
                } else {
                    if (TagsIndex == 0) {
                        tags.clear();
                        selfTvBrand.setText("品牌");
                    } else if (TagsIndex == 1) {
                        tags1.clear();
                        for (int i = 0; i < nature.size(); i++) {
                            if (nature.get(i).containsKey(1)) {
                                nature.remove(i);
                            }
                        }
                        selfTvDelivery.setText(proData.getPropertyList().get(0).getPropertyName());
                    } else if (TagsIndex == 2) {
                        tags2.clear();
                        for (int i = 0; i < nature.size(); i++) {
                            if (nature.get(i).containsKey(2)) {
                                nature.remove(i);
                            }
                        }
                        selfTvNature.setText(proData.getPropertyList().get(1).getPropertyName());
                    } else if (TagsIndex == 3) {
                        tags3.clear();
                        for (int i = 0; i < nature.size(); i++) {
                            if (nature.get(i).containsKey(3)) {
                                nature.remove(i);
                            }
                        }
                        selfTvCrowd.setText(proData.getPropertyList().get(2).getPropertyName());
                    }
                }
                mAdapater.notifyDataSetChanged();
            }
        });
        //监听popupwindow 消失事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                RestoreBg();
            }
        });


    }

    private void DoScreenPort(String toJson) {
        String str = "";
        String low = "";
        String high = "";
        if (proData.getSize() == 3 && tags.size() != 0) {
            BrandName = selfTvBrand.getText().toString();
        } else {
            BrandName = "";
        }

        String lows = "\"" + low + "\"";
        String highs = "\"" + high + "\"";
        String strs = "\"" + str + "\"";
        String change = "\"" + 0 + "\"";
        String service = "service";
        String lowPrice = "lowPrice";
        String highPrice = "highPrice";
        String properties = "properties";
        String isChange = "isChange";

        String parameterDatas = "{" + "\"" + service + "\"" + ":" + strs + "," + "\"" + lowPrice + "\"" + ":" + lows + "," + "\"" + isChange + "\"" + ":" + change + "," + "\"" + highPrice + "\"" + ":"
                + highs + "," + "\"" + properties + "\"" + ":" + toJson +
                "}";
        this.parameterData = parameterDatas;
        loadMore = false;
        currentPage = 1;
//        SelectAllProductByTypePort(currentPage);
        dogetProductByFilterPort(currentPage, "");
        adapater.notifyDataSetChanged();
    }


    //popupwindow 消失的时候还原背景
    private void RestoreBg() {
        selfTvNature.setBackgroundResource(R.drawable.selfrun_text_bg);
        selfTvDelivery.setBackgroundResource(R.drawable.selfrun_text_bg);
        selfTvBrand.setBackgroundResource(R.drawable.selfrun_text_bg);
        selfTvCrowd.setBackgroundResource(R.drawable.selfrun_text_bg);
        nature_tag = 0;
        delivery_tag = 0;
        brand_tag = 0;
        crowd_tag = 0;
    }

    public void setParameterData(String parameterData) {
        this.parameterData = parameterData;
        if (!TextUtils.isEmpty(parameterData)) {
            loadMore = false;
            currentPage = 1;
//            SelectAllProductByTypePort(currentPage);
            dogetProductByFilterPort(currentPage, "");
            adapater.notifyDataSetChanged();
        }
    }

    public void setTags(String tags) {
        this.Tags = tags;


    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            currentPage = 1;
            loadMore = false;
            /**
             * 下拉刷新数据
             */
//            getMainInterfacePort();
            refreshLayout.setRefreshing(false);
//
//            String label = DateUtils.formatDateTime(
//                    SelfrunGoodsListActivity.this,
//                    System.currentTimeMillis(),
//                    DateUtils.FORMAT_SHOW_TIME
//                            | DateUtils.FORMAT_SHOW_DATE
//                            | DateUtils.FORMAT_ABBREV_ALL);
//
//            // Update the LastUpdatedLabel
//            refreshView.getLoadingLayoutProxy()
//                    .setLastUpdatedLabel(label);
            dogetProductByFilterPort(currentPage, "");
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            currentPage += 1;
            loadMore = true;
            /**
             * 上拉加载数据
             */
//            getMainInterfacePort();
            refreshLayout.setRefreshing(false);

//            isLoadMore = true;
//            currentPage++;
            dogetProductByFilterPort(currentPage, "");
        }
    }

    //下拉 popupWindow 商品属性适配器
    private class NatureAdapater extends BaseAdapter {

        private final SelfrunMarketProListGetProperty.DataBean data;
        private final int index;
        private final int TagsIndex;
        private LayoutInflater mInflater = null;
        private ViewHolder holder;


        public NatureAdapater(SelfrunMarketProListGetProperty.DataBean proData, int i, int TagsIndex) {
            this.data = proData;
            this.index = i;
            this.mInflater = LayoutInflater.from(SelfrunGoodsListActivity.this);
            this.TagsIndex = TagsIndex;
        }

        @Override
        public int getCount() {
            if (data.getSize() == 3 && index == -1) {
                return data.getBrandList().size();
            } else {
                return data.getPropertyList().get(index).getPropertyValueList().size();
            }

        }

        @Override
        public Object getItem(int i) {
            if (data.getSize() == 3 && index == -1) {
                return data.getBrandList().get(i);
            } else {
                return data.getPropertyList().get(index).getPropertyValueList().get(i);
            }
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            holder = new ViewHolder();
            if (view == null) {
                view = mInflater.inflate(R.layout.item_nature, null);
                holder.tv_nature_item_tag = view.findViewById(R.id.tv_nature_item_tag);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            if (data.getSize() == 3 && index == -1) {
                holder.tv_nature_item_tag.setText(data.getBrandList().get(position));
            } else {
                holder.tv_nature_item_tag.setText(data.getPropertyList().get(index)
                        .getPropertyValueList().get(position).getPropertyValue());
            }
            holder.tv_nature_item_tag.setBackgroundDrawable(getResources().getDrawable
                    (R.drawable.normal_bg));
            if (TagsIndex == 0) {
                if (tags != null && tags.size() > 0) { // 已有被选中的标签
                    // 确定当前标签是否被选中
                    if (tags.contains(position)) {
                        holder.tv_nature_item_tag.setBackgroundDrawable(
                                getResources().getDrawable(R.drawable.checked_bg));
                    }

                }
            }
            if (TagsIndex == 1) {
                if (tags1 != null && tags1.size() > 0) { // 已有被选中的标签
                    // 确定当前标签是否被选中
                    if (tags1.contains(position)) {
                        holder.tv_nature_item_tag.setBackgroundDrawable(
                                getResources().getDrawable(R.drawable.checked_bg));
                    }

                }
            }
            if (TagsIndex == 2) {
                if (tags2 != null && tags2.size() > 0) { // 已有被选中的标签
                    // 确定当前标签是否被选中
                    if (tags2.contains(position)) {
                        holder.tv_nature_item_tag.setBackgroundDrawable(
                                getResources().getDrawable(R.drawable.checked_bg));
                    }

                }
            }
            if (TagsIndex == 3) {
                if (tags3 != null && tags3.size() > 0) { // 已有被选中的标签
                    // 确定当前标签是否被选中
                    if (tags3.contains(position)) {
                        holder.tv_nature_item_tag.setBackgroundDrawable(
                                getResources().getDrawable(R.drawable.checked_bg));
                    }

                }
            }


            ItemListener itemListener = new ItemListener(position, data, TagsIndex);
            holder.tv_nature_item_tag.setOnClickListener(itemListener);

            return view;
        }

        class ItemListener implements View.OnClickListener {
            private final int TagsIndex;
            private int m_position;
            private SelfrunMarketProListGetProperty.DataBean datas;

            ItemListener(int pos, SelfrunMarketProListGetProperty.DataBean data, int tagsIndex) {
                m_position = pos;
                datas = data;
                this.TagsIndex = tagsIndex;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_nature_item_tag:
                        if (TagsIndex == 0) {
                            if (0 == tags.size()) {
                                tags.add(m_position);
                                v.setBackgroundDrawable(
                                        getResources().getDrawable(R.drawable.checked_bg));
                                tags.size();
                            } else {
                                tags.clear();
                                tags.add(m_position);
                            }
                        }
                        if (TagsIndex == 1) {
                            if (0 == tags1.size()) {
                                tags1.add(m_position);
                                v.setBackgroundDrawable(
                                        getResources().getDrawable(R.drawable.checked_bg));
                                tags1.size();
                            } else {
                                tags1.clear();
                                tags1.add(m_position);
                            }
                        }
                        if (TagsIndex == 2) {
                            if (0 == tags2.size()) {
                                tags2.add(m_position);
                                v.setBackgroundDrawable(
                                        getResources().getDrawable(R.drawable.checked_bg));
                                tags2.size();
                            } else {
                                tags2.clear();
                                tags2.add(m_position);
                            }
                        }
                        if (TagsIndex == 3) {
                            if (0 == tags3.size()) {
                                tags3.add(m_position);
                                v.setBackgroundDrawable(
                                        getResources().getDrawable(R.drawable.checked_bg));
                                tags3.size();
                            } else {
                                tags3.clear();
                                tags3.add(m_position);
                            }
                        }
                        mAdapater.notifyDataSetChanged();
                        break;
                    default:
                        break;

                }

            }
        }
    }

    private void butIntegratedClicked(View view) {
        imgPaixu.setImageResource(R.mipmap.icon_fenleixiala_sel);
        final ArrayList<String> data = new ArrayList<String>();
        data.add("综合排序");
        data.add("从高到低");
        data.add("从低到高");

        // 初始化弹出菜单	 POPMENU
        popMenu = new PopMenu(this, getScreenWidth() / 3, data);
        popMenu.setOnItemClickListener(new PopMenu.OnItemClickListener() {

            @Override
            public void onItemClick(int index) {
                // TODO Auto-generated method stub
                imgPaixu.setImageResource(R.mipmap.icon_fenleixiala_nor);
                tvPaixu.setText(data.get(index));
                if (tvPaixu.getText().equals("综合排序")) {
                    sortType = "";
                    saleSort = 1 + "";
                } else if (tvPaixu.getText().equals("从高到低")) {
                    sortType = 0 + "";
                    saleSort = "";
                } else if (tvPaixu.getText().equals("从低到高")) {
                    sortType = 1 + "";
                    saleSort = "";
                }
                loadMore = false;
                currentPage = 1;
//                SelectAllProductByTypePort(currentPage);
                dogetProductByFilterPort(currentPage, "");
            }
        });
        popMenu.showAsDropDown(view);
    }

    private int getScreenWidth() {
        // TODO Auto-generated method stub
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        return dm.widthPixels; // 屏幕宽（像素，如：480px）
    }
}
