package app.cn.extra.mall.user.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.StatusCode;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.GlideCircleTransformWithBorder;
import app.cn.extra.mall.user.utils.IMLogin;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.utils.Util;
import app.cn.extra.mall.user.utils.ZoomTutorial;
import app.cn.extra.mall.user.utils.html.HtmlUtils;
import app.cn.extra.mall.user.view.MyScrollView;
import app.cn.extra.mall.user.view.NewsGridView;
import app.cn.extra.mall.user.view.NewsListView;
import app.cn.extra.mall.user.view.ViewHolder;
import app.cn.extra.mall.user.vo.AddUserCollection;
import app.cn.extra.mall.user.vo.DelUserCollection;
import app.cn.extra.mall.user.vo.DoAddProductToTrolley;
import app.cn.extra.mall.user.vo.GetProductDetails;
import app.cn.extra.mall.user.vo.GetProductEvaluate;
import app.cn.extra.mall.user.vo.GetRecmandProductByType;
import app.cn.extra.mall.user.vo.ShoppingCarProduct;
import app.cn.extra.mall.user.vo.ShoppingCarStore;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import cfkj.app.cn.cfkjcommonlib.view.StatusBar.StatusBarUtil;
import okhttp3.Call;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 便捷购物 商品详情
 */
public class GoodsDetailActivity extends BaseActivty {
    private SharePreferenceUtil mSharePreferenceUtil;
    private String pid;
    private String cid;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.fl_top)
    FrameLayout flTop;
    @BindView(R.id.ib_back_vpf)
    ImageButton ibBackVpf;
    @BindView(R.id.view_classify)
    View viewClassify;
    @BindView(R.id.rl_goods_property)
    RelativeLayout rlGoodsProperty;
    @BindView(R.id.rl_relative)
    RelativeLayout rlRelative;
    @BindView(R.id.rl_relative1)
    RelativeLayout rlRelative1;
    @BindView(R.id.rl_relative2)
    RelativeLayout rlRelative2;
    @BindView(R.id.rl_relative3)
    RelativeLayout rlRelative3;
    @BindView(R.id.rl_relative4)
    RelativeLayout rlRelative4;
    @BindView(R.id.rl_goods_classify)
    RelativeLayout rlGoodsClassify;
    @BindView(R.id.rl_title_cote)
    RelativeLayout rlTitleCote;
    @BindView(R.id.goods_rl_index)
    RelativeLayout goodsRlIndex;
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_money_ago)
    TextView tvMoneyAgo;
    @BindView(R.id.tv_money_now)
    TextView tvMoneyNow;
    @BindView(R.id.tv_html_goods_detail)
    TextView tvHtmlGoodsDetail;
    @BindView(R.id.tv_buy_num)
    TextView tvBuyNum;
    @BindView(R.id.tv_goods_introduce)
    TextView tvGoodsIntorduce;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.textView9)
    TextView textView9;
    @BindView(R.id.tv_all_pingjia)
    TextView tvAllPingJia;
    @BindView(R.id.tv_good_pingjia)
    TextView tvGoodPingJia;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_normal_pingjia)
    TextView tvNormalPingJia;
    @BindView(R.id.tv_bad_pingjia)
    TextView tvBadPingJia;
    @BindView(R.id.tv_pingjia)
    TextView tvPingjia;
    @BindView(R.id.tv_add_more)
    TextView tvAddMore;
    @BindView(R.id.tv_goods)
    TextView tvGoods;
    @BindView(R.id.tv_evaluate)
    TextView tvEvaluate;
    @BindView(R.id.tv_details)
    TextView tvDetails;
    @BindView(R.id.tv_recommend)
    TextView tvRecommend;
    @BindView(R.id.tv_goods_classify)
    TextView tvGoodsClassify;
    @BindView(R.id.tv_alls)
    TextView tvAlls;
    @BindView(R.id.tv_currents)
    TextView tvCurrents;
    @BindView(R.id.img_all)
    TextView imgAll;
    @BindView(R.id.ll_service_load)
    LinearLayout llServiceLoad;
    @BindView(R.id.ll_evalua_cut)
    LinearLayout llevaluaCut;
    @BindView(R.id.ll_detail_cut)
    LinearLayout llDetailCut;
    @BindView(R.id.ll_recommend_cut)
    LinearLayout llRecommendCut;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private GetProductDetails.DataBean dateProduct;
    @BindView(R.id.goods_view_send)
    View goodsViewSend;
    @BindView(R.id.goods_view_fa)
    View goodsVireFa;
    @BindView(R.id.goods_view_bao)
    View goodsViewBao;
    @BindView(R.id.goods_view_back)
    View goodsViewBack;
    @BindView(R.id.goods_view_tui)
    View GoodsViewTui;
    @BindView(R.id.rl_goods)
    RelativeLayout rl_goods;
    @BindView(R.id.rl_evaluate)
    RelativeLayout rl_evaluate;
    @BindView(R.id.rl_details)
    RelativeLayout rl_details;
    @BindView(R.id.rl_recommend)
    RelativeLayout rl_recommend;
    @BindView(R.id.lv_pingjia)
    NewsListView lvPingjia;
    @BindView(R.id.gv_recommend_goods)
    NewsGridView gvRecommendGoods;
    @BindView(R.id.iv_good_pic)
    ImageView ivGoodPic;
    @BindView(R.id.iv_goods)
    ImageView ivGoods;
    @BindView(R.id.iv_backs)
    ImageView ivBack;
    @BindView(R.id.iv_evaluate)
    ImageView ivEvaluate;
    @BindView(R.id.iv_details)
    ImageView ivDetails;
    @BindView(R.id.iv_recommend)
    ImageView ivRecommend;
    @BindView(R.id.img_car)
    ImageView imgCar;
    @BindView(R.id.myScrollView)
    MyScrollView myScrollView;
    @BindView(R.id.viewPagerfixed)
    ViewPager expandedView;
    @BindView(R.id.rl_collection)
    RelativeLayout rl_collection;
    @BindView(R.id.iv_collection)
    ImageView iv_collection;
    private boolean isRefersh = true;
    private FrameLayout containerView;
    private int level = 3;
    private GetProductDetails productDetails;
    private int states = 0;
    private int page = 1;
    private GetProductEvaluate.DataBean EvaluateData;
    private GoodsPingjiaAdapter goodsPingjiaAdapter;
    private RecommendAdapter adapter;
    private List<GetRecmandProductByType.DataBean.ProductListBean> recommendDatas;
    private ScheduledExecutorService scheduledExecutorService;
    private int tag = 0;
    private int EvaluaTop;
    private int DetailTop;
    private int RecommendTop;
    private int height;
    private int PicIndex;
    //装 点 的ImageView数组
    private ImageView[] tips;
    // 用于装载 点 的ViewGroup
    @BindView(R.id.viewGroup)
    ViewGroup group;
    // 用以存储图片url
    public ArrayList<String> imgIdArray = new ArrayList<String>();
    //装ImageView数组
    private ImageView[] mImageViews;
    // 当前图片的索引号
    private int currentItem = 0;
    // 切换当前显示的图片
    @SuppressLint("HandlerLeak")

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        tvBuy.setBackgroundColor(getResources().getColor(R.color.blue));
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tips = null;
        imgIdArray.clear();
        mImageViews = null;
        group.removeAllViews();

    }

    @OnClick({R.id.iv_backs, R.id.tv_buy, R.id.img_all, R.id.img_car, R.id.ib_back_vpf, R.id.tv_all_pingjia, R.id.tv_good_pingjia, R.id.tv_normal_pingjia, R.id.tv_bad_pingjia, R.id.rl_goods_classify, R.id.rl_goods_property, R.id.rl_goods, R.id.rl_evaluate, R.id.rl_details, R.id.rl_recommend, R.id.iv_good_pic, R.id.rl_shop, R.id.rl_message, R.id.rl_collection})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.rl_collection:
                if (productDetails == null) {
                    break;
                }
                if (TextUtils.isEmpty(mSharePreferenceUtil.getUID())) {
                    intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    switch (productDetails.getData().getIsCollection()) {
                        case "true":
                            showRemoveDialog(pid);
                            break;
                        case "false":
                            addUserCollection(pid);
                            break;
                    }
                }
                break;
            case R.id.rl_shop:
                intent = new Intent(GoodsDetailActivity.this, ShopActivity.class);
                intent.putExtra("sId", productDetails.getData().getSId());
                startActivity(intent);
                break;
            case R.id.rl_message:
                // 打开单聊界面
                if (productDetails != null) {
                    if (TextUtils.isEmpty(mSharePreferenceUtil.getUID())) {
                        intent = new Intent(GoodsDetailActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        StatusCode status = NIMClient.getStatus();
                        if (status == StatusCode.UNLOGIN) {
                            IMLogin imLogin = new IMLogin(GoodsDetailActivity.this);
                            imLogin.login(1);
                        }
                        NimUIKit.startP2PSession(GoodsDetailActivity.this, productDetails.getData().getAccid(), null);
                    }
                }
                break;
            case R.id.tv_all_pingjia:
                tvAllPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_blue_bg));
                tvAllPingJia.setTextColor(getResources().getColor(R.color.blue));
                tvGoodPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tvGoodPingJia.setTextColor(Color.parseColor("#999999"));
                tvNormalPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tvNormalPingJia.setTextColor(Color.parseColor("#999999"));
                tvBadPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tvBadPingJia.setTextColor(Color.parseColor("#999999"));
                tvPingjia.setText("商品评价（0）");
                level = 3;
                //全部的评价
                GetProductEvaluatePort("");
                break;
            case R.id.tv_good_pingjia:
                tvAllPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tvAllPingJia.setTextColor(Color.parseColor("#999999"));
                tvGoodPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_blue_bg));
                tvGoodPingJia.setTextColor(getResources().getColor(R.color.blue));
                tvNormalPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tvNormalPingJia.setTextColor(Color.parseColor("#999999"));
                tvBadPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tvBadPingJia.setTextColor(Color.parseColor("#999999"));
                tvPingjia.setText("商品评价（0）");
                level = 2;
                GetProductEvaluatePort("" + level);
                break;
            case R.id.tv_normal_pingjia:
                tvAllPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tvAllPingJia.setTextColor(Color.parseColor("#999999"));
                tvGoodPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tvGoodPingJia.setTextColor(Color.parseColor("#999999"));
                tvNormalPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_blue_bg));
                tvNormalPingJia.setTextColor(getResources().getColor(R.color.blue));
                tvBadPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tvBadPingJia.setTextColor(Color.parseColor("#999999"));
                tvPingjia.setText("商品评价（0）");
                level = 1;
                GetProductEvaluatePort("" + level);
                break;
            case R.id.tv_bad_pingjia:
                tvAllPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tvAllPingJia.setTextColor(Color.parseColor("#999999"));
                tvGoodPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tvGoodPingJia.setTextColor(Color.parseColor("#999999"));
                tvNormalPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tvNormalPingJia.setTextColor(Color.parseColor("#999999"));
                tvBadPingJia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_blue_bg));
                tvBadPingJia.setTextColor(getResources().getColor(R.color.blue));
                tvPingjia.setText("商品评价（0）");
                level = 0;
                GetProductEvaluatePort("" + level);
                break;
            // 商品分类
            case R.id.rl_goods_classify:
                showGoodsClassifyDialog();
                break;
            // 商品参数
            case R.id.rl_goods_property:
                showGoodsPropertyDialog();
                break;
            //加入购物车
            case R.id.img_car:
                AddShopCar();
                break;
            case R.id.tv_buy:
                if (dateProduct.getPStockNum() > 0) {
                    String uids = mSharePreferenceUtil.getUID();
                    if (!TextUtils.isEmpty(uids)) {
                        if (dateProduct.getPropertysList2() != null && dateProduct.getPropertysList2().size() != 0) {
                            states = 2;
                            showGoodsClassifyDialog();
                        } else {
                            payGoods();
                        }
                    } else {
                        Intent intentlogin = new Intent();
                        intentlogin.setClass(GoodsDetailActivity.this, LoginActivity.class);
                        startActivity(intentlogin);
                        CustomToast.showToast(GoodsDetailActivity.this,
                                "请先登录账号在进行购买！", Toast.LENGTH_SHORT);
                    }
                } else {
                    CustomToast.showToast(GoodsDetailActivity.this,
                            "该商品暂时无货，无法立即购买！", Toast.LENGTH_SHORT);
                }
                break;
            case R.id.iv_backs:
                finish();
                break;
            case R.id.img_all:
                intent = new Intent();
                intent.setClass(GoodsDetailActivity.this, AllevaluateActivity.class);
                intent.putExtra("pid", pid);
                startActivity(intent);
                break;
            case R.id.rl_goods://宝贝
                myScrollView.smoothScrollTo(0, 0);
                ivGoods.setVisibility(View.VISIBLE);
                ivEvaluate.setVisibility(View.GONE);
                ivDetails.setVisibility(View.GONE);
                ivRecommend.setVisibility(View.GONE);
                break;
            case R.id.rl_evaluate:
                int evaluate = EvaluaTop - Util.dip2px(this, 90);
                myScrollView.smoothScrollTo(0, evaluate);
                ivGoods.setVisibility(View.GONE);
                ivEvaluate.setVisibility(View.VISIBLE);
                ivDetails.setVisibility(View.GONE);
                ivRecommend.setVisibility(View.GONE);
                break;
            case R.id.rl_details:
                int details = DetailTop - Util.dip2px(this, 90);
                myScrollView.smoothScrollTo(0, details);
                ivGoods.setVisibility(View.GONE);
                ivEvaluate.setVisibility(View.GONE);
                ivDetails.setVisibility(View.VISIBLE);
                ivRecommend.setVisibility(View.GONE);
                break;
            case R.id.rl_recommend:
                int recommend = RecommendTop - Util.dip2px(this, 90);
                myScrollView.smoothScrollTo(0, recommend);
                ivGoods.setVisibility(View.GONE);
                ivEvaluate.setVisibility(View.GONE);
                ivDetails.setVisibility(View.GONE);
                ivRecommend.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_good_pic:
                setViewPagerAndZoom(ivGoodPic, 0);
                break;
            case R.id.ib_back_vpf:
                if (expandedView.getVisibility() == View.VISIBLE) {
                    expandedView.setVisibility(View.GONE);
                    goodsRlIndex.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    private void initView() {
        myScrollView.setOnTouchListener(new TouchListenerImpl());
//        myScrollView.smoothScrollTo(0, 20);
        mSharePreferenceUtil = new SharePreferenceUtil(GoodsDetailActivity.this, Constants.SAVE_USER);
        cid = mSharePreferenceUtil.getUID();
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) flTop.getLayoutParams();
        linearParams.height = Utils.getScreenWidth(GoodsDetailActivity.this);
        height = Utils.getScreenWidth(GoodsDetailActivity.this);
        flTop.setLayoutParams(linearParams);

        tvGoods.setTextColor(Color.argb(0, 255, 255, 255));
        tvEvaluate.setTextColor(Color.argb(0, 255, 255, 255));
        tvDetails.setTextColor(Color.argb(0, 255, 255, 255));
        tvRecommend.setTextColor(Color.argb(0, 255, 255, 255));
        rlTitleCote.getBackground().setAlpha(0);
        ivGoodPic.setAlpha(0);
        ivGoods.getBackground().setAlpha(0);
        myScrollView.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int y) {
                if (y <= 0) {
                    rlTitleCote.getBackground().setAlpha(0);
                    ivGoodPic.setAlpha(0);
                    ivBack.getBackground().setAlpha(255);
                    ivGoods.getBackground().setAlpha(0);
                    tvGoods.setTextColor(Color.argb(0, 255, 255, 255));
                    tvEvaluate.setTextColor(Color.argb(0, 255, 255, 255));
                    tvDetails.setTextColor(Color.argb(0, 255, 255, 255));
                    tvRecommend.setTextColor(Color.argb(0, 255, 255, 255));
                    ivGoods.setVisibility(View.VISIBLE);
                    ivEvaluate.setVisibility(View.GONE);
                    ivDetails.setVisibility(View.GONE);
                    ivRecommend.setVisibility(View.GONE);
                } else if (y > 0 && y <= height) {
                    float scale = (float) y / height;
                    float alpha = (255 * scale);
                    float fScale = 1 - scale;
                    float fAlpah = (255 * fScale);
                    rlTitleCote.getBackground().setAlpha((int) alpha);
                    ivGoodPic.setAlpha((int) alpha);
                    ivBack.getBackground().setAlpha((int) fAlpah);
                    ivGoods.getBackground().setAlpha((int) alpha);
                    tvGoods.setTextColor(Color.argb((int) alpha, 255, 255, 255));
                    tvEvaluate.setTextColor(Color.argb((int) alpha, 255, 255, 255));
                    tvDetails.setTextColor(Color.argb((int) alpha, 255, 255, 255));
                    tvRecommend.setTextColor(Color.argb((int) alpha, 255, 255, 255));
                    ivGoods.setVisibility(View.VISIBLE);
                    ivEvaluate.setVisibility(View.GONE);
                    ivDetails.setVisibility(View.GONE);
                    ivRecommend.setVisibility(View.GONE);

                } else {
                    rlTitleCote.getBackground().setAlpha(255);
                    ivGoodPic.setAlpha(255);
                    ivBack.getBackground().setAlpha(0);
                    ivGoods.getBackground().setAlpha(255);
                    tvGoods.setTextColor(Color.argb(255, 255, 255, 255));
                    tvEvaluate.setTextColor(Color.argb(255, 255, 255, 255));
                    tvDetails.setTextColor(Color.argb(255, 255, 255, 255));
                    tvRecommend.setTextColor(Color.argb(255, 255, 255, 255));
                    if (y >= RecommendTop - Util.dip2px(GoodsDetailActivity.this, 90)) {
                        ivGoods.setVisibility(View.GONE);
                        ivEvaluate.setVisibility(View.GONE);
                        ivDetails.setVisibility(View.GONE);
                        ivRecommend.setVisibility(View.VISIBLE);
                    } else if (y >= DetailTop - Util.dip2px(GoodsDetailActivity.this, 90)) {
                        ivGoods.setVisibility(View.GONE);
                        ivEvaluate.setVisibility(View.GONE);
                        ivDetails.setVisibility(View.VISIBLE);
                        ivRecommend.setVisibility(View.GONE);
                    } else if (y >= EvaluaTop - Util.dip2px(GoodsDetailActivity.this, 90)) {
                        ivGoods.setVisibility(View.GONE);
                        ivEvaluate.setVisibility(View.VISIBLE);
                        ivDetails.setVisibility(View.GONE);
                        ivRecommend.setVisibility(View.GONE);
                    } else {
                        ivGoods.setVisibility(View.VISIBLE);
                        ivEvaluate.setVisibility(View.GONE);
                        ivDetails.setVisibility(View.GONE);
                        ivRecommend.setVisibility(View.GONE);
                    }
                }
            }

        });

        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
    }

    private void initData() {
        //获取商品详情
        getProductDetailsPort();
    }

    @Override
    protected void onStart() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 8,
                TimeUnit.SECONDS);
        super.onStart();
    }

    @Override
    protected void onStop() {
        // 当Activity不可见的时候停止切换
        scheduledExecutorService.shutdown();
        super.onStop();
    }

    /**
     * 是否删除弹窗
     *
     * @param id 商品id
     */
    private void showRemoveDialog(final String id) {
        LayoutInflater layoutInflater = LayoutInflater.from(GoodsDetailActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(GoodsDetailActivity.this, R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        TextView tv_live_content = view.findViewById(R.id.tv_live_content);
        tv_live_content.setText("确定要取消收藏吗？");

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
                delUserCollection(id, alertDialog);
            }
        });

    }

    /**
     * 删除收藏
     */
    private void delUserCollection(String id, final Dialog alertDialog) {
        if (Utils.isNetworkAvailable(GoodsDetailActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.delUserCollection)
                    .addParams("uId", mSharePreferenceUtil.getUID())
                    .addParams("pId", id)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            /**
                             * 恢复显示数据的View
                             */
                            DelUserCollection delUserCollection = new DelUserCollection();
                            delUserCollection = Utils.parserJsonResult(response,
                                    DelUserCollection.class);
                            if (Constants.OK.equals(delUserCollection.getFlag())) {
                                if (Constants.OK.equals(delUserCollection.getData().getStatus())) {
                                    showLongToast(GoodsDetailActivity.this, "取消收藏成功！");
                                    alertDialog.dismiss();
                                    iv_collection.setBackgroundResource(R.mipmap.icon_collection1);
                                    productDetails.getData().setIsCollection("false");
                                }
                            } else {
                                showLongToast(GoodsDetailActivity.this, delUserCollection.getErrorString());
                            }
                        }
                    });
        } else {

        }
    }

    /**
     * 添加收藏
     */
    private void addUserCollection(String pId) {
        if (Utils.isNetworkAvailable(GoodsDetailActivity.this)) {
            rl_collection.setEnabled(false);
            OkHttpUtils
                    .post()
                    .url(Constants.addUserCollection)
                    .addParams("uId", mSharePreferenceUtil.getUID())
                    .addParams("pId", pId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            rl_collection.setEnabled(true);
//                            varyViewHelperController.showErrorView();
//                            setSaveBtnClickable(true);
                        }

                        @Override
                        public void onResponse(String response, int id) {
//                            setSaveBtnClickable(true);
                            rl_collection.setEnabled(true);
                            /**
                             * 恢复显示数据的View
                             */
//                            varyViewHelperController.restore();
                            AddUserCollection addUserCollection = new AddUserCollection();
                            addUserCollection = Utils.parserJsonResult(response,
                                    AddUserCollection.class);
                            if (Constants.OK.equals(addUserCollection.getFlag())) {
                                if (Constants.OK.equals(addUserCollection.getData().getStatus())) {
                                    showLongToast(GoodsDetailActivity.this, "收藏商品成功！");
                                    iv_collection.setBackgroundResource(R.mipmap.icon_collection2);
                                    productDetails.getData().setIsCollection("true");
                                } else {
                                    showLongToast(GoodsDetailActivity.this, addUserCollection.getData().getErrorString());
                                }

                            } else {
                                showLongToast(GoodsDetailActivity.this, addUserCollection.getErrorString());
//                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
            showShortToast(GoodsDetailActivity.this, "收藏失败！" + Constants.NETWORK_ERROR);
            rl_collection.setEnabled(true);
        }
    }
//    /**
//     * 删除收藏
//     */
//    private void delUserCollection(String id) {
//        if (Utils.isNetworkAvailable(GoodsDetailActivity.this)) {
//            rl_collection.setEnabled(false);
//            OkHttpUtils
//                    .post()
//                    .url(Constants.delUserCollection)
//                    .addParams("uId", mSharePreferenceUtil.getUID())
//                    .addParams("ucIdList", id)
//                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                    .build()
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e, int id) {
//                            rl_collection.setEnabled(true);
//                        }
//
//                        @Override
//                        public void onResponse(String response, int id) {
//                            rl_collection.setEnabled(true);
//
//                            DelUserCollection delUserCollection = new DelUserCollection();
//                            delUserCollection = Utils.parserJsonResult(response,
//                                    DelUserCollection.class);
//                            if (Constants.OK.equals(delUserCollection.getFlag())) {
//                                showLongToast(GoodsDetailActivity.this, "删除成功！");
//                                iv_collection.setBackgroundResource(R.mipmap.icon_s1);
//                            } else {
//                                showLongToast(GoodsDetailActivity.this, delUserCollection.getErrorString());
//                                rl_collection.setEnabled(true);
//                            }
//                        }
//                    });
//        } else {
//            rl_collection.setEnabled(true);
//        }
//    }

    /**
     * 换行切换任务
     *
     * @author Administrator
     */
    private class ScrollTask implements Runnable {

        @Override
        public void run() {
            synchronized (viewPager) {
                // 动态更新界面内各部分高度
                EvaluaTop = llevaluaCut.getTop();
                DetailTop = llDetailCut.getTop();
                RecommendTop = llRecommendCut.getTop();
                // 商品展示图片轮播
                currentItem = (currentItem + 1) % dateProduct.getPicList().size();
                // 通过Handler切换图片
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    //滑动监听
    private class TouchListenerImpl implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    break;
                case MotionEvent.ACTION_MOVE:
                    int scrollY = view.getScrollY();
                    int height = view.getHeight();
                    int scrollViewMeasuredHeight = myScrollView.getChildAt(0).getMeasuredHeight();
                    if ((scrollY + height) == scrollViewMeasuredHeight) {
                        tvAddMore.setVisibility(View.VISIBLE);
                        if (isRefersh = true) {
                            page += 1;
                            getRecmandProductByTypePort(productDetails.getData().getPtdId());
                            isRefersh = false;
                        }
                    }
                    break;

                default:
                    break;
            }
            return false;
        }

    }

    private void payGoods() {

        Intent intentbuy = new Intent();
        intentbuy.setClass(GoodsDetailActivity.this, CommodityBuyActivity.class);
        Bundle bundle = new Bundle();
        List<ShoppingCarProduct> list = new ArrayList<ShoppingCarProduct>();
        ShoppingCarStore store = new ShoppingCarStore();
        store.setSid(0);
        ShoppingCarProduct product = new ShoppingCarProduct();
        product.setPro_name(dateProduct.getPName());
        product.setNum(1);
        product.setSprice(Double.valueOf(dateProduct.getPNowPrice()));
        product.setOnlinePrice(Double.valueOf(dateProduct.getPOriginalPrice()));
        product.setIntegralPrice(Double.valueOf(dateProduct.getPNowPrice()));
        product.setPic_url(dateProduct.getPicList().get(0).getPicName());
        product.setsId(productDetails.getData().getSId());
        product.setPro_id(pid);
        list.add(product);
        bundle.putInt("state", 0);
        bundle.putSerializable("goodsDetail", (Serializable) list);
        intentbuy.putExtras(bundle);
        startActivity(intentbuy);
        list.clear();
        finish();


    }

    /**
     * 加入购物车
     */
    private void AddShopCar() {
        if (dateProduct.getPStockNum() > 0) {
            if (dateProduct.getPropertysList2() != null) {
                if (dateProduct.getPropertysList2().size() != 0) {
                    states = 1;
                    showGoodsClassifyDialog();

                } else {
                    AddCartPort();
                }
            } else if (dateProduct.getPropertysList1() != null) {
                AddCartPort();
            }


        } else {
            CustomToast.showToast(GoodsDetailActivity.this,
                    "该商品暂时无货，无法加入购物车！", Toast.LENGTH_SHORT);
        }
    }

    private void AddCartPort() {
        String uid = mSharePreferenceUtil.getUID();
        if (TextUtils.isEmpty(uid)) {
            Intent intent = new Intent();
            intent.setClass(GoodsDetailActivity.this, LoginActivity.class);
            startActivity(intent);
            CustomToast.showToast(GoodsDetailActivity.this, "请先登录账号！", Toast.LENGTH_LONG);
            return;
        }
        if (Utils.isNetworkAvailable(GoodsDetailActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.addUserTrolley)
                    .addParams("uId", uid)
                    .addParams("utProductProperty", "")
                    .addParams("pId", pid)
                    .addParams("sId", productDetails.getData().getSId())
                    .addParams("utProductNum", "1")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            DoAddProductToTrolley toTrolley = new DoAddProductToTrolley();
                            toTrolley = Utils.parserJsonResult(response, DoAddProductToTrolley.class);
                            if ("true".equals(toTrolley.getFlag())) {
                                if (Constants.OK.equals(toTrolley.getData().getStatus())) {
                                    showLongToast(GoodsDetailActivity.this, "加入购物车成功！");
                                }

                            } else {
                                showLongToast(GoodsDetailActivity.this, toTrolley.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(GoodsDetailActivity.this, "加入购物车失败！" + Constants.NETWORK_ERROR);
        }
    }

    /**
     * 获取推荐商品
     */
    private void getRecmandProductByTypePort(String ptdId) {
        int size = 10;
        if (Utils.isNetworkAvailable(GoodsDetailActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getRecommendProductList)
                    .addParams("uId", mSharePreferenceUtil.getUID())
                    .addParams("ptdId", ptdId)
                    .addParams("size", size + "")
                    .addParams("currentPage", page + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            GetRecmandProductByType productByType = new GetRecmandProductByType();
                            productByType = Utils.parserJsonResult(response, GetRecmandProductByType.class);
                            if ("true".equals(productByType.getFlag())) {
                                if (recommendDatas != null) {
                                    recommendDatas.addAll(productByType.getData().getProductList());
                                    adapter.notifyDataSetChanged();
                                    if (productByType.getData().getProductList().size() == 0) {
                                        tvAddMore.setText("没有更多推荐商品！");
                                        tvAddMore.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    recommendDatas = productByType.getData().getProductList();
                                    adapter = new RecommendAdapter(recommendDatas);
                                    gvRecommendGoods.setAdapter(adapter);
//                                    myScrollView.smoothScrollTo(0, 0);
                                }

                            } else {
                                showLongToast(GoodsDetailActivity.this, productByType.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(GoodsDetailActivity.this, Constants.NETWORK_ERROR);
        }
    }

    /**
     * 获取商品详情
     */
    private void getProductDetailsPort() {
        if (Utils.isNetworkAvailable(GoodsDetailActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getProductInfo)
                    .addParams("pId", pid)
                    .addParams("uId", cid)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            productDetails = new GetProductDetails();
                            productDetails = Utils.parserJsonResult(response, GetProductDetails.class);
                            if ("true".equals(productDetails.getFlag())) {
                                dateProduct = productDetails.getData();
                                processData(dateProduct);
                            } else {
                                showLongToast(GoodsDetailActivity.this, productDetails.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(GoodsDetailActivity.this, Constants.NETWORK_ERROR);
        }
    }

    private void processData(GetProductDetails.DataBean datas) {
        if (datas == null) {
            return;
        }
        switch (datas.getIsCollection()) {
            case "true":
                iv_collection.setBackgroundResource(R.mipmap.icon_collection2);
                break;
            case "false":
                iv_collection.setBackgroundResource(R.mipmap.icon_collection1);
                break;
            default:
                iv_collection.setBackgroundResource(R.mipmap.icon_collection1);
                break;
        }
        if (datas.getPicList() != null && datas.getPicList().size() > 0) {
            Picasso.with(GoodsDetailActivity.this).load(datas.getPicList().get(0).getPicName())
                    .placeholder(R.drawable.ic_exception).into(ivGoodPic);
        }
        String str = "";
        for (int i = 0; i < datas.getPpropertyNames().size(); i++) {
            str += datas.getPpropertyNames().get(i) + ",";
        }
        if (!TextUtils.isEmpty(str)) {
            if (str.length() > 14) {
                tvGoodsClassify.setText("请选择：" + str.substring(0, 14) + "  分类");
            } else {
                tvGoodsClassify.setText("请选择：" + str + "  分类");
            }
        } else {
            rlGoodsClassify.setVisibility(View.GONE);
            viewClassify.setVisibility(View.GONE);
        }

        if (datas.getPropertysList1() == null || datas.getPropertysList1().size() == 0) {
            rlGoodsProperty.setVisibility(View.GONE);
        } else {
            rlGoodsProperty.setVisibility(View.VISIBLE);
        }
        tvGoodsName.setText(datas.getPName());


        tvMoneyAgo.setText("原价：" + datas.getPOriginalPrice());
        tvMoneyNow.setText("¥" + datas.getPNowPrice());
        tvMoneyNow.setTextColor(getResources().getColor(R.color.orange));
        tvMoneyAgo.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        if (!TextUtils.isEmpty(datas.getHtml())) {
            tvHtmlGoodsDetail.setText(HtmlUtils.getHtml(GoodsDetailActivity.this, tvHtmlGoodsDetail, datas.getHtml()));
        } else {
            llServiceLoad.setVisibility(View.VISIBLE);
            tvHtmlGoodsDetail.setVisibility(View.GONE);
        }


        tvBuyNum.setText(datas.getPBrowseNum() + "人浏览");

        if (!TextUtils.isEmpty(datas.getPDescribe())) {
            tvGoodsIntorduce.setText(datas.getPDescribe());
        } else {
            tvGoodsIntorduce.setVisibility(View.GONE);
        }

        if (datas.getPropertysList1() != null && datas.getPropertysList1().size() != 0) {
            for (int i = 0; i < datas.getPropertysList1().size(); i++) {
                if (datas.getPropertysList1().get(i).equals(textView2.getText().toString())) {
                    rlRelative.setVisibility(View.VISIBLE);
                    goodsViewSend.setVisibility(View.VISIBLE);
                }
                if (datas.getPropertysList1().get(i).equals(textView3.getText().toString())) {
                    rlRelative1.setVisibility(View.VISIBLE);
                    GoodsViewTui.setVisibility(View.VISIBLE);
                }
                if (datas.getPropertysList1().get(i).equals(textView5.getText().toString())) {
                    rlRelative4.setVisibility(View.VISIBLE);
                    goodsViewBack.setVisibility(View.VISIBLE);
                }
                if (datas.getPropertysList1().get(i).equals(textView7.getText().toString())) {
                    rlRelative2.setVisibility(View.VISIBLE);
                    goodsVireFa.setVisibility(View.VISIBLE);
                }
                if (datas.getPropertysList1().get(i).equals(textView9.getText().toString())) {
                    rlRelative3.setVisibility(View.VISIBLE);
                    goodsViewBao.setVisibility(View.VISIBLE);
                }
            }
        }

        //获取商品评价
        GetProductEvaluatePort("");
        //推荐商品接口
        getRecmandProductByTypePort(datas.getPtdId());
        // 设置Adapter
        viewPager.setAdapter(new AdsAdapter(datas.getPicList()));
        // 设置监听，主要是设置 点 的背景
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                currentItem = arg0;
                setImageBackground(arg0);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

        });
        // 初始化顶部广告栏数据
        if (mImageViews != null) {
            mImageViews = new ImageView[0];
        }
        if (imgIdArray != null) {
            imgIdArray.clear();
        }
        mImageViews = new ImageView[datas.getPicList().size()];
        for (int i = 0; i < datas.getPicList().size(); i++) {
            imgIdArray.add(datas.getPicList().get(i).getPicName());
        }
        setViewPager();
        if (null != progressBar) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.mipmap.lunbodian_sel);
            } else {
                tips[i].setBackgroundResource(R.mipmap.lunbodian_nor);
            }
        }
    }

    // 设置顶部广告栏viewpager
    @SuppressWarnings("deprecation")
    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    private void setViewPager() {
        if (group != null) {
            group.removeAllViews();
        }
        // 将 点 加入到ViewGroup中
        tips = new ImageView[mImageViews.length];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.mipmap.lunbodian_sel);
            } else {
                tips[i].setBackgroundResource(R.mipmap.lunbodian_nor);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            group.addView(imageView, layoutParams);
        }

        // 将图片装载到数组中
        if (imgIdArray.size() == 1) {
            mImageViews = new ImageView[1];
            for (int i = 0; i < (mImageViews.length); i++) {
                ImageView imageView = new ImageView(this);
                mImageViews[i] = imageView;
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(R.drawable.ic_exception);

            }
            group.setVisibility(View.GONE);
            viewPager.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    // TODO Auto-generated method stub

                    viewPager.getParent().requestDisallowInterceptTouchEvent(
                            true);

                    return false;
                }
            });
        } else {
            mImageViews = new ImageView[imgIdArray.size()];
            if (group.getVisibility() == View.GONE) {
                group.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < mImageViews.length; i++) {
                ImageView imageView = new ImageView(this);
                mImageViews[i] = imageView;
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageResource(R.drawable.ic_exception);
            }
        }

    }

    // 顶部广告ViewPager的适配器
    private class AdsAdapter extends PagerAdapter {


        private final List<GetProductDetails.DataBean.PicListBean> datas;

        public AdsAdapter(List<GetProductDetails.DataBean.PicListBean> pics) {
            this.datas = pics;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        /**
         * 载入图片进去，
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = new ImageView(GoodsDetailActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
//            imageView.setImageResource(R.mipmap.content_zhezhao_nor);
            container.addView(imageView);
//            if (!TextUtils.isEmpty(datas.get(position).getPicName())) {
//                Picasso.with(GoodsDetailActivity.this)
//                        .load(datas.get(position).getPicName())
//                        .placeholder(R.drawable.ic_exception)
//                        .transform(transformation)
//                        .into(imageView);
//            } else {
//                Picasso.with(GoodsDetailActivity.this)
//                        .load(R.drawable.ic_exception)
//                        .into(imageView);
//            }
            RequestOptions requestOptions = new RequestOptions()
                    .error(R.drawable.ic_exception)
                    .fallback(R.drawable.ic_exception);
            Glide.with(GoodsDetailActivity.this)
                    .load(datas.get(position).getPicName() + "?x-oss-process=image/resize,w_"
                            + (Utils.getScreenWidth(GoodsDetailActivity.this)))
                    .apply(requestOptions)
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setViewPagerAndZoom(imageView, position);
                }
            });

            return imageView;
        }

    }

    Transformation transformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {
            if ((double) source.getWidth() > (double) source.getHeight()) { // 宽大于高
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (Utils.getScreenWidth(GoodsDetailActivity.this) * aspectRatio);
                if (targetHeight != 0) {
                    Bitmap result = Bitmap.createScaledBitmap(source,
                            Utils.getScreenWidth(GoodsDetailActivity.this),
                            targetHeight, false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                } else {
                    return source;
                }
            } else { // 高大于宽
                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (Utils.getScreenWidth(GoodsDetailActivity.this) * aspectRatio);
                if (targetWidth != 0) {
                    Bitmap result = Bitmap.createScaledBitmap(source,
                            targetWidth, Utils.getScreenWidth(GoodsDetailActivity.this), false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                } else {
                    return source;
                }
            }
        }

        @Override
        public String key() {
            return "transformation" + " desiredWidth";
        }
    };


    private void setViewPagerAndZoom(View v, int position) {

        //最外层的容器，用来计算
        containerView = (FrameLayout) ((Activity) this).findViewById(R.id.containers);
        //实现放大缩小类，传入当前的容器和要放大展示的对象
        ZoomTutorial mZoomTutorial = new ZoomTutorial(containerView, expandedView);

        ViewPageAdapters adapter = new ViewPageAdapters(this, dateProduct.getPicList(), mZoomTutorial);
        expandedView.setAdapter(adapter);
        expandedView.setCurrentItem(position);
        expandedView.setVisibility(View.GONE);
        // 通过传入Id来从小图片扩展到大图，开始执行动画
        mZoomTutorial.zoomImageFromThumb(v);
        goodsRlIndex.setVisibility(View.VISIBLE);
        expandedView.setCurrentItem(position); // 设置展示图片开始的位置
        expandedView.setOffscreenPageLimit(3);// 设置展示图片的缓存个数
        tvAlls.setText(" / " + dateProduct.getPicList().size());
        if (dateProduct.getPicList().size() == 0) {
            tvCurrents.setText(0 + "");
        } else {
            tvCurrents.setText(position + 1 + "");
        }
        expandedView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

                if (dateProduct.getPicList().size() == 0) {
                    tvCurrents.setText(0 + "");
                } else {
                    tvCurrents.setText(position + 1 + "");
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub


            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub


            }
        });
    }

    //加载大图的viewpage
    private class ViewPageAdapters extends PagerAdapter {

        private final List<GetProductDetails.DataBean.PicListBean> datas;
        private Context context;
        private SparseArray<View> cacheView;
        private ZoomTutorial mZoomTutorial;


        public ViewPageAdapters(GoodsDetailActivity context,
                                List<GetProductDetails.DataBean.PicListBean> pics, ZoomTutorial mZoomTutorial) {
            this.datas = pics;
            this.context = context;
            this.cacheView = new SparseArray<View>(datas.size());
            this.mZoomTutorial = mZoomTutorial;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            View view = cacheView.get(position);
            PicIndex = position;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.vp_item_image, container, false);
                view.setTag(position);
                ImageView image = view.findViewById(R.id.image);
                final PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(image);
                photoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        expandedView.setVisibility(View.GONE);
                        goodsRlIndex.setVisibility(View.GONE);
                    }

                    @Override
                    public void onOutsidePhotoTap() {
                        expandedView.setVisibility(View.GONE);
                        goodsRlIndex.setVisibility(View.GONE);
                    }
                });
                if (!TextUtils.isEmpty(datas.get(position).getPicName())) {
//                    RequestOptions requestOptions = new RequestOptions()
//                            .error(R.drawable.ic_exception)
//                            .fallback(R.drawable.ic_exception);
//                    Glide.with(context)
//                            .load(datas.get(position).getPicName())
//                            .apply(requestOptions)
//                            .into(image);
//                    photoViewAttacher.update();

                    Picasso.with(context).load(datas.get(position).getPicName())
                            .placeholder(R.drawable.ic_exception).into(image, new Callback() {
                        @Override
                        public void onSuccess() {
//									mZoomTutorial.closeZoomAnim(position);
                            photoViewAttacher.update();
                        }

                        @Override
                        public void onError() {

                        }
                    });
                } else {
                    Picasso.with(context).load(R.drawable.ic_exception)
                            .into(image);
//                    RequestOptions requestOptions = new RequestOptions()
//                            .error(R.drawable.ic_exception)
//                            .fallback(R.drawable.ic_exception);
//                    Glide.with(context)
//                            .load(datas.get(position).getPicName())
//                            .apply(requestOptions)
//                            .into(image);
                }

                cacheView.put(position, view);

            }
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && expandedView.getVisibility() == View.VISIBLE) {
            expandedView.setVisibility(View.GONE);
            goodsRlIndex.setVisibility(View.GONE);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && expandedView.getVisibility() == View.GONE) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取评价接口
     *
     * @param level
     */
    private void GetProductEvaluatePort(String level) {
        String size = 1 + "";
        if (Utils.isNetworkAvailable(GoodsDetailActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getProductEvaluate)
                    .addParams("pId", pid)
                    .addParams("peLevel", level)
                    .addParams("size", size)
                    .addParams("currentPage", 1 + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            GetProductEvaluate getProductEvaluate = new GetProductEvaluate();
                            getProductEvaluate = Utils.parserJsonResult(response, GetProductEvaluate.class);
                            if ("true".equals(getProductEvaluate.getFlag())) {
                                EvaluateData = getProductEvaluate.getData();
                                tvPingjia.setText("商品评价" + "(" + EvaluateData.getLevelNum() + ")");
                                goodsPingjiaAdapter = new GoodsPingjiaAdapter(EvaluateData);
                                lvPingjia.setAdapter(goodsPingjiaAdapter);
                                if (EvaluateData.getProductEvaluateList().size() == 0) {
                                    imgAll.setVisibility(View.GONE);
                                } else {
                                    imgAll.setVisibility(View.VISIBLE);
                                }
                            } else {
                                showLongToast(GoodsDetailActivity.this, getProductEvaluate.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(GoodsDetailActivity.this, Constants.NETWORK_ERROR);
        }
    }

    /**
     * 推荐商品列表
     */
    private class RecommendAdapter extends BaseAdapter {


        private final List<GetRecmandProductByType.DataBean.ProductListBean> datas;
        private ViewHolder holder;
        private LayoutInflater mInflater = null;

        public RecommendAdapter(List<GetRecmandProductByType.DataBean.ProductListBean> datas) {
            this.mInflater = LayoutInflater.from(GoodsDetailActivity.this);
            this.datas = datas;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return datas.size() == 0 ? 0 : datas.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View arg1, ViewGroup parent) {
            // TODO Auto-generated method stub
            holder = new ViewHolder();
            if (arg1 == null) {
                arg1 = mInflater.inflate(R.layout.grid_item_store, null);
                holder.img_grid_item_store = arg1
                        .findViewById(R.id.img_grid_item_store);
                holder.tv_grid_item_store_name = arg1
                        .findViewById(R.id.tv_grid_item_store_name);
                holder.tv_grid_item_store_buy = arg1
                        .findViewById(R.id.tv_grid_item_store_buy);
                holder.tv_grid_item_store_moneys = arg1
                        .findViewById(R.id.tv_grid_item_store_moneys);
                arg1.setTag(holder);
            } else {
                holder = (ViewHolder) arg1.getTag();
            }

            if (!TextUtils.isEmpty(datas.get(position).getPName())) {
                holder.tv_grid_item_store_name.setText(datas.get(position).getPName());
            } else {
                holder.tv_grid_item_store_name.setText("");
            }
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) holder.img_grid_item_store.getLayoutParams();
            linearParams.width = Utils.getScreenWidth(GoodsDetailActivity.this) / 2 - 10;
            linearParams.height = Utils.getScreenWidth(GoodsDetailActivity.this) / 2 - 10;
            holder.img_grid_item_store.setLayoutParams(linearParams);

            if (!TextUtils.isEmpty(datas.get(position).getPicName())) {
                Picasso.with(GoodsDetailActivity.this).load(datas.get(position).getPicName()
                        + "?x-oss-process=image/resize,w_"
                        + (Utils.getScreenWidth(GoodsDetailActivity.this) / 2 - 10))
                        .placeholder(R.drawable.ic_exception)
                        .into(holder.img_grid_item_store);
            } else {
                Picasso.with(GoodsDetailActivity.this).load(R.drawable.ic_exception).into(holder.img_grid_item_store);
            }

            holder.tv_grid_item_store_moneys.setText("¥" + datas.get(position).getPNowPrice());
            holder.tv_grid_item_store_buy.setText("立即购买");


            holder.tv_grid_item_store_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!TextUtils.isEmpty(mSharePreferenceUtil.getUID())) {
                        Intent intent = new Intent(GoodsDetailActivity.this, GoodsDetailActivity.class);
                        String pid = datas.get(position).getPId();
                        intent.putExtra("pid", pid);
                        startActivity(intent);
                    } else {
                        CustomToast.showToast(GoodsDetailActivity.this,
                                "尚未登录，请先登录您的账号！", Toast.LENGTH_SHORT);
                        Intent msg = new Intent();
                        msg.setClass(GoodsDetailActivity.this, LoginActivity.class);
                        startActivity(msg);
                    }


                }
            });
            arg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GoodsDetailActivity.this, GoodsDetailActivity.class);
                    String pid = datas.get(position).getPId();
                    intent.putExtra("pid", pid);
                    startActivity(intent);
                    tag = 1;
                }
            });

            isRefersh = true;
            return arg1;
        }

    }

    /**
     * 商品评价适配器
     */
    private class GoodsPingjiaAdapter extends BaseAdapter {

        private final GetProductEvaluate.DataBean datas;
        private ViewHolder holder;
        private LayoutInflater mInflater = null;

        public GoodsPingjiaAdapter(GetProductEvaluate.DataBean evaluateData) {
            this.mInflater = LayoutInflater.from(GoodsDetailActivity.this);
            this.datas = evaluateData;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return datas.getProductEvaluateList().size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return datas.getProductEvaluateList().get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View arg1, ViewGroup parent) {
            // TODO Auto-generated method stub
            holder = new ViewHolder();
            if (arg1 == null) {
                arg1 = mInflater.inflate(R.layout.list_item_goods_pingjia, null);

                holder.img_item_goods_pingjia_header = arg1
                        .findViewById(R.id.img_item_goods_pingjia_header);
                holder.tv_item_goods_pingjia_name = arg1
                        .findViewById(R.id.tv_item_goods_pingjia_name);
                holder.tv_item_goods_pingjia_content = arg1
                        .findViewById(R.id.tv_item_goods_pingjia_content);
                holder.tv_item_goods_reply_content = arg1
                        .findViewById(R.id.tv_item_goods_reply_content);
                holder.gv_pingjia_pic = arg1
                        .findViewById(R.id.gv_pingjia_pic);

                arg1.setTag(holder);
            } else {
                holder = (ViewHolder) arg1.getTag();
            }
            holder.tv_item_goods_pingjia_name.setText(datas.getProductEvaluateList().get(position).getUNickName());
            holder.tv_item_goods_pingjia_content.setText(datas.getProductEvaluateList().get(position).getPeContent());
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.mipmap.profile)
                    .error(R.mipmap.profile)
                    .fallback(R.mipmap.profile)
                    .transform(new GlideCircleTransformWithBorder(GoodsDetailActivity.this,
                            2,
                            getResources().getColor(R.color.blue)));
            Glide.with(GoodsDetailActivity.this)
                    .load(datas.getProductEvaluateList().get(position)
                            .getPicName() + "?x-oss-process=image/resize,h_150")
                    .apply(requestOptions)
                    .into(holder.img_item_goods_pingjia_header);
//            Picasso.with(GoodsDetailActivity.this).load(datas.getProductEvaluateList().get(position)
//                    .getPicName())
//                    .placeholder(R.mipmap.wode_head_sel)
//                    .transform(new CircleTransform())
//                    .into(holder.img_item_goods_pingjia_header);

            arg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(GoodsDetailActivity.this, AllevaluateActivity.class);
                    intent.putExtra("pid", pid);
                    intent.putExtra("state", 2);
                    startActivity(intent);
                }
            });

//            EvaluationAdapter mEvaluationAdapter =
//                    new EvaluationAdapter(datas.getProductEvaluateList().get(position).getPicList());
//            holder.gv_pingjia_pic.setAdapter(mEvaluationAdapter);

            return arg1;
        }

    }
//    private class EvaluationAdapter extends BaseAdapter{
//
//
//        private final LayoutInflater mInflater;
//        private final List<GetProductEvaluate.DataBean.ProductEvaluateListBean.PicListBean> data;
//        private ViewHolder holder;
//
//        public EvaluationAdapter(List<GetProductEvaluate.DataBean.ProductEvaluateListBean.PicListBean> data) {
//            this.data = data;
//            this.mInflater = LayoutInflater.from(GoodsDetailActivity.this);
//        }
//
//        @Override
//        public int getCount() {
//            if(data.size()>4) {
//                return 4;
//            }else {
//                return data.size();
//            }
//
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return data.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            holder = new ViewHolder();
//            if(convertView == null) {
//                convertView = mInflater.inflate(R.layout.item_pingjia_pic,null);
//                holder.iv_item_pingjia_pic = (ImageView) convertView
//                        .findViewById(R.id.iv_item_pingjia_pic);
//                convertView.setTag(holder);
//            }else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            Picasso.with(GoodsDetailActivity.this).load(data.get(position).getUrl())
//                    .into(holder.iv_item_pingjia_pic);
//            holder.iv_item_pingjia_pic.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setPlViewPagerAndZoom(holder.iv_item_pingjia_pic, position,data);
//                }
//            });
//            return convertView;
//        }
//    }


    /**
     * 设置沉浸式状态栏
     */
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this,
                getResources().getColor(R.color.blue), 1);
    }

    private void showGoodsPropertyDialog() {
        // 取得自定义View
        Bundle bundle = new Bundle();
        bundle.putSerializable("params", productDetails);
        bundle.putString("pid", pid);
        Intent intent = new Intent(GoodsDetailActivity.this, GoodsPropertyDialog.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void showGoodsClassifyDialog() {
        // 取得自定义View
        Bundle bundle = new Bundle();
        bundle.putSerializable("params", productDetails);
        bundle.putString("pid", pid);
        bundle.putInt("states", states);
        Intent intent = new Intent(GoodsDetailActivity.this, GoodsClassifyDialog.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
