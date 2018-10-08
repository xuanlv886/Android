package app.cn.extra.mall.merchant.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.GlideCircleTransformWithBorder;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.utils.ZoomTutorial;
import app.cn.extra.mall.merchant.view.NewsGridView;
import app.cn.extra.mall.merchant.view.ViewPagerFixed;
import app.cn.extra.mall.merchant.vo.GetProductEvaluate;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import cfkj.app.cn.cfkjcommonlib.view.ReplaceViewHelper.VaryViewHelperController;
import okhttp3.Call;
import uk.co.senab.photoview.PhotoViewAttacher;

public class AllevaluateActivity extends BaseActivty implements SwipyRefreshLayout.OnRefreshListener {
    @BindView(R.id.ib_back_vpf)
    ImageButton ibBackVpf;
    @BindView(R.id.img_back)
    ImageView ibBack;
    private int level = 3;
    private String pid;
    @BindView(R.id.tv_all_pingjia)
    TextView tv_all_pingjia;
    @BindView(R.id.tv_good_pingjia)
    TextView tv_good_pingjia;
    @BindView(R.id.tv_normal_pingjia)
    TextView tv_normal_pingjia;
    @BindView(R.id.tv_bad_pingjia)
    TextView tv_bad_pingjia;
    @BindView(R.id.tv_pingjia)
    TextView tv_pingjia;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;

    private EvaluationAdapter mEvaluationAdapter;
    @BindView(R.id.viewPagerfixed)
    ViewPagerFixed expandedView;
    private RelativeLayout goods_rl_index;
    @BindView(R.id.tv_currents)
    TextView tv_currents;
    @BindView(R.id.tv_alls)
    TextView tv_alls;
    private FrameLayout containerView;
    private int PicIndex;
    private SharePreferenceUtil sharePreferenceUtil;
    private GetProductEvaluate.DataBean evaluateNum;
    private List<GetProductEvaluate.DataBean.ProductEvaluateListBean> datas;
    private int state;
    private String raId;
    /**
     * 当前页，用于分页获取数据 从1开始
     */
    private int currentPage = 1;
    /**
     * 判断是下拉更新还是上拉加载更多 false--下拉更新， true--上拉加载更多
     */
    private boolean loadMore = false;
    private VaryViewHelperController varyViewHelperController;
    private CommonAdapter<GetProductEvaluate.DataBean.ProductEvaluateListBean> adapter;
    /**
     * 展示用评价条数
     */
    private int evaNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allevaluate);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        state = intent.getIntExtra("state", 0);
        raId = intent.getStringExtra("raId");
        pid = intent.getStringExtra("pid");
        initView();
    }

    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(AllevaluateActivity.this, Constants.SAVE_USER);
        /**
         * 设置异常情况界面
         */
        varyViewHelperController = createCaseViewHelperController(refreshLayout);
        varyViewHelperController.setUpRefreshViews(varyViewHelperController.getErrorView(),
                varyViewHelperController.getNetworkPoorView(),varyViewHelperController.getEmptyView());
        initRefreshLayout();
        initRecyclerView();
        initData(level);

    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 网格布局
         */
        LinearLayoutManager layoutManage = new LinearLayoutManager(AllevaluateActivity.this);
        recyclerView.setLayoutManager(layoutManage);
//        /**
//         * 添加分隔线
//         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(AllevaluateActivity.this,
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

    private void initData(int level) {
//        if (state == 1) {
//            //获取房间评价
//            getRoomEvaluatePort(level);
//        } else {
//            //获取商品评价
        GetProductEvaluatePort(level);
//        }

    }

    @OnClick({R.id.img_back, R.id.tv_all_pingjia, R.id.tv_good_pingjia, R.id.tv_normal_pingjia, R.id.tv_bad_pingjia, R.id.ib_back_vpf})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_all_pingjia://全部评价
                tv_all_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_blue_bg));
                tv_all_pingjia.setTextColor(getResources().getColor(R.color.blue));
                tv_good_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tv_good_pingjia.setTextColor(Color.parseColor("#999999"));
                tv_normal_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tv_normal_pingjia.setTextColor(Color.parseColor("#999999"));
                tv_bad_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tv_bad_pingjia.setTextColor(Color.parseColor("#999999"));

//                if (state == 1) {
//                    tv_pingjia.setText("酒店评价（0）");
//                } else {
                tv_pingjia.setText("商品评价（0）");
//                }
                level = 3;
                currentPage = 1;
                loadMore = false;
                evaNum = 0;
                //全部的评价
                initData(level);
                break;
            case R.id.tv_good_pingjia://好评
                tv_all_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tv_all_pingjia.setTextColor(Color.parseColor("#999999"));
                tv_good_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_blue_bg));
                tv_good_pingjia.setTextColor(getResources().getColor(R.color.blue));
                tv_normal_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tv_normal_pingjia.setTextColor(Color.parseColor("#999999"));
                tv_bad_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tv_bad_pingjia.setTextColor(Color.parseColor("#999999"));
//                if (state == 1) {
//                    tv_pingjia.setText("酒店评价（0）");
//                } else {
                tv_pingjia.setText("商品评价（0）");
//                }
                level = 2;
                currentPage = 1;
                loadMore = false;
                evaNum = 0;
                initData(level);
                break;
            case R.id.tv_normal_pingjia://中评
                tv_all_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tv_all_pingjia.setTextColor(Color.parseColor("#999999"));
                tv_good_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tv_good_pingjia.setTextColor(Color.parseColor("#999999"));
                tv_normal_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_blue_bg));
                tv_normal_pingjia.setTextColor(getResources().getColor(R.color.blue));
                tv_bad_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tv_bad_pingjia.setTextColor(Color.parseColor("#999999"));
//                if (state == 1) {
//                    tv_pingjia.setText("酒店评价（0）");
//                } else {
                tv_pingjia.setText("商品评价（0）");
//                }
                currentPage = 1;
                level = 1;
                loadMore = false;
                evaNum = 0;
                initData(level);
                break;
            case R.id.tv_bad_pingjia://差评
                tv_all_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tv_all_pingjia.setTextColor(Color.parseColor("#999999"));
                tv_good_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tv_good_pingjia.setTextColor(Color.parseColor("#999999"));
                tv_normal_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_graw_bg));
                tv_normal_pingjia.setTextColor(Color.parseColor("#999999"));
                tv_bad_pingjia.setBackgroundDrawable(getResources()
                        .getDrawable(R.drawable.shape_text_blue_bg));
                tv_bad_pingjia.setTextColor(getResources().getColor(R.color.blue));
//                if (state == 1) {
//                    tv_pingjia.setText("酒店评价（0）");
//                } else {
                tv_pingjia.setText("商品评价（0）");
//                }
                currentPage = 1;
                level = 0;
                loadMore = false;
                evaNum = 0;
                initData(level);
                break;
            case R.id.ib_back_vpf:
                if (expandedView.getVisibility() == View.VISIBLE) {
                    expandedView.setVisibility(View.GONE);
                    goods_rl_index.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 获取商品评价
     *
     * @param level
     */
    private void GetProductEvaluatePort(int level) {
        String size = 10 + "";
        if (Utils.isNetworkAvailable(AllevaluateActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getProductEvaluate)
                    .addParams("pId", pid)
                    .addParams("peLevel", level + "")
                    .addParams("size", size)
                    .addParams("currentPage", currentPage + "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("getProductEvaluate", response);
                            /**
                             * 恢复显示数据的View
                             */
                            varyViewHelperController.restore();
                            GetProductEvaluate getProductEvaluate = new GetProductEvaluate();
                            getProductEvaluate = Utils.parserJsonResult(response, GetProductEvaluate.class);
                            if ("true".equals(getProductEvaluate.getFlag())) {
                                evaluateNum = getProductEvaluate.getData();
                                evaNum = evaNum + evaluateNum.getProductEvaluateList().size();
                                tv_pingjia.setText("商品评价" + "(" + evaNum + ")");
                                LoadData();
                            } else {
                                showLongToast(AllevaluateActivity.this, getProductEvaluate.getErrorString());
                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
            varyViewHelperController.showNetworkErrorView();
        }
    }

    private void LoadData() {
        if (!loadMore) {
            datas = evaluateNum.getProductEvaluateList();
            if (datas != null && datas.size() > 0) {
                /**
                 * 商品评价adapter
                 */
                adapter = new CommonAdapter<GetProductEvaluate.DataBean.ProductEvaluateListBean>(AllevaluateActivity.this, R.layout.list_item_goods_pingjia, datas) {
                    @Override
                    protected void convert(ViewHolder holder, GetProductEvaluate.DataBean.ProductEvaluateListBean contentListBean, int position) {
                        holder.setText(R.id.tv_item_goods_pingjia_name, datas.get(position).getUNickName());
                        holder.setText(R.id.tv_item_goods_pingjia_content, datas.get(position).getPeContent());
                        RequestOptions requestOptions = new RequestOptions()
                                .placeholder(R.mipmap.profile)
                                .error(R.mipmap.profile)
                                .fallback(R.mipmap.profile)
                                .transform(new GlideCircleTransformWithBorder(AllevaluateActivity.this, 2,
                                        getResources().getColor(R.color.blue)));
                        Glide.with(AllevaluateActivity.this)
                                .load(datas.get(position)
                                        .getPicName() +"?x-oss-process=image/resize,h_150")
                                .apply(requestOptions)
                                .into((ImageView) holder.getView(R.id.img_item_goods_pingjia_header));
//                    Picasso.with(AllevaluateActivity.this).load(datas.get(position)
//                            .getPicName())
//                            .placeholder(R.mipmap.wode_head_sel)
//                            .transform(new CircleTransform())
//                            .into((ImageView) holder.getView(R.id.img_item_goods_pingjia_header));

//            if (!"null".equals(String.valueOf(datas.get(position).getAnswer()))) {
//                holder.tv_item_goods_reply_content.setVisibility(View.VISIBLE);
//
//                holder.tv_item_goods_reply_content.setText("店铺回复：" + datas.get(position).getAnswer());
//            } else {
//                holder.tv_item_goods_reply_content.setVisibility(View.GONE);
//            }
                        if (datas.get(position).getPicList() != null && datas.get(position).getPicList().size() > 0) {
                            mEvaluationAdapter = new EvaluationAdapter(datas.get(position).getPicList());
                            NewsGridView newsGridView = holder.getView(R.id.gv_pingjia_pic);
                            newsGridView.setAdapter(mEvaluationAdapter);
                        }
                    }
                };
                recyclerView.setAdapter(adapter);
            } else {
                varyViewHelperController.showEmptyView();
            }
        } else {
            datas.addAll(evaluateNum.getProductEvaluateList());
            adapter.notifyDataSetChanged();
            if (evaluateNum.getProductEvaluateList().size() == 0) {
                CustomToast.showToast(AllevaluateActivity.this,
                        "已经到底了，没有更多评论了！", Toast.LENGTH_SHORT);
            }
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
            evaNum = 0;
            initData(level);
            refreshLayout.setRefreshing(false);
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            currentPage += 1;
            loadMore = true;
            /**
             * 上拉加载首页数据
             */
            initData(level);
            refreshLayout.setRefreshing(false);
        }
    }

    private class EvaluationAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private final List<GetProductEvaluate.DataBean.ProductEvaluateListBean.PicListBean> data;

        private ViewHolder holder;


        public EvaluationAdapter(List<GetProductEvaluate.DataBean.ProductEvaluateListBean.PicListBean> picList) {
            this.data = picList;
            this.mInflater = LayoutInflater.from(AllevaluateActivity.this);
        }

        @Override
        public int getCount() {
            return data.size();

        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            holder = new ViewHolder();
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_pingjia_pic, null);
                holder.iv_item_pingjia_pic = convertView
                        .findViewById(R.id.iv_item_pingjia_pic);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            RequestOptions requestOptions = new RequestOptions()
                    .error(R.drawable.ic_exception)
                    .fallback(R.drawable.ic_exception);
            Glide.with(AllevaluateActivity.this)
                    .load(data.get(position).getUrl() + "?x-oss-process=image/resize,w_"
                            + (Utils.getScreenWidth(AllevaluateActivity.this)))
                    .apply(requestOptions)
                    .into(holder.iv_item_pingjia_pic);
            holder.iv_item_pingjia_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setViewPagerAndZoom(holder.iv_item_pingjia_pic, data, position);
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView iv_item_pingjia_pic;
        }
    }


    private void setViewPagerAndZoom(View v, final List<GetProductEvaluate.DataBean.ProductEvaluateListBean.PicListBean> data, int position) {

        //最外层的容器，用来计算
        containerView = (FrameLayout) ((Activity) this).findViewById(R.id.activity_allevaluate);
        //实现放大缩小类，传入当前的容器和要放大展示的对象
        ZoomTutorial mZoomTutorial = new ZoomTutorial(containerView, expandedView);

        ViewPageAdapters adapter = new ViewPageAdapters(this, data, mZoomTutorial);
        expandedView.setAdapter(adapter);
        expandedView.setCurrentItem(position);
        expandedView.setVisibility(View.GONE);
        // 通过传入Id来从小图片扩展到大图，开始执行动画
        mZoomTutorial.zoomImageFromThumb(v);
        goods_rl_index.setVisibility(View.VISIBLE);
        expandedView.setCurrentItem(position); // 设置展示图片开始的位置
        expandedView.setOffscreenPageLimit(3);// 设置展示图片的缓存个数
        tv_alls.setText(" / " + data.size());
        if (data.size() == 0) {
            tv_currents.setText(0 + "");
        } else {
            tv_currents.setText(position + 1 + "");
        }
        expandedView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

                if (data.size() == 0) {
                    tv_currents.setText(0 + "");
                } else {
                    tv_currents.setText(position + 1 + "");
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

        private final List<GetProductEvaluate.DataBean.ProductEvaluateListBean.PicListBean> datas;
        private Context context;
        private SparseArray<View> cacheView;
        private ZoomTutorial mZoomTutorial;


        public ViewPageAdapters(AllevaluateActivity context, List<GetProductEvaluate.DataBean.ProductEvaluateListBean.PicListBean> data, ZoomTutorial mZoomTutorial) {
            this.datas = data;
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
                        goods_rl_index.setVisibility(View.GONE);
                    }

                    @Override
                    public void onOutsidePhotoTap() {
                        expandedView.setVisibility(View.GONE);
                        goods_rl_index.setVisibility(View.GONE);
                    }
                });

                if (!TextUtils.isEmpty(datas.get(position).getUrl())) {
                    Picasso.with(context).load(datas.get(position).getUrl()).placeholder(R.drawable.ic_exception)
                            .into(image, new Callback() {
                                @Override
                                public void onSuccess() {
                                    photoViewAttacher.update();
                                }

                                @Override
                                public void onError() {

                                }
                            });

                } else {
                    Picasso.with(context).load(R.drawable.ic_exception)
                            .into(image);
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
            goods_rl_index.setVisibility(View.GONE);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && expandedView.getVisibility() == View.GONE) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
