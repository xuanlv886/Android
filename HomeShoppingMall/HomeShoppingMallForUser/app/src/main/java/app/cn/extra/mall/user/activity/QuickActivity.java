package app.cn.extra.mall.user.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.utils.Util;
import app.cn.extra.mall.user.view.ViewPagerFixed;
import app.cn.extra.mall.user.vo.GetQuickView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import cfkj.app.cn.cfkjcommonlib.view.StatusBar.StatusBarUtil;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 快速浏览
 */
public class QuickActivity extends BaseActivty {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_current)
    TextView tvCurrent;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_view_shoucang)
    TextView tvViewShoucang;
    @BindView(R.id.tv_add_shoucang)
    TextView tvAddShoucang;
    @BindView(R.id.viewPager)
    ViewPagerFixed viewPager;
    private ViewPageAdapter adapter;
    // 0 未收藏 1已收藏
    private int tag_collect = 0;
    private String storeId;
    private SharePreferenceUtil sharePreferenceUtil;
    private String productTypeId;
    private String sortType = 0 + "";
    private int positions;
    private int index;
    private List<Integer> indexs;
    private String parameterData;
    private String ptdId;
    private List<GetQuickView.DataBean.ProductListBean> datas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        ptdId = intent.getStringExtra("ptdId");
        productTypeId = intent.getStringExtra("productTypeId");
        parameterData = intent.getStringExtra("parameterData");
        initView();
        initData();
    }

    /**
     * 设置沉浸式状态栏
     */
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this,
                getResources().getColor(R.color.black_tab), 1);
    }

    private void initData() {
        dogetProductByFilterPort();
    }

    private void dogetProductByFilterPort() {
        String currentPage = 1 + "";
        String size = 50 + "";
        String userId = sharePreferenceUtil.getUID();
        if (TextUtils.isEmpty(parameterData)) {
            parameterData = "";
        }
        if (Utils.isNetworkAvailable(QuickActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getProductList)
                    .addParams("ptdId", ptdId)
                    .addParams("priceSort", sortType)
                    .addParams("saleSort", "")
                    .addParams("currentPage", currentPage + "")
                    .addParams("size", size)
                    .addParams("parameterData", parameterData)
                    .addParams("uId", userId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(okhttp3.Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Util.logE("dogetProductByFilter---", response);
                            GetQuickView quickView = new GetQuickView();
                            quickView = Utils.parserJsonResult(response, GetQuickView.class);
                            if ("true".equals(quickView.getFlag())) {
                                datas = quickView.getData().getProductList();
                                processData(datas);
                                IndustryStatus(datas);
                            } else {
                                showLongToast(QuickActivity.this,
                                        quickView.getErrorString());
                            }
                        }
                    });
        } else {
            CustomToast.showToast(QuickActivity.this,
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);
        }
    }


    private void processData(final List<GetQuickView.DataBean.ProductListBean> datas) {
        adapter = new ViewPageAdapter(this, datas);
        int position = 0;
        viewPager.setCurrentItem(position); // 设置展示图片开始的位置
        viewPager.setOffscreenPageLimit(3);// 设置展示图片的缓存个数
        tvAll.setText(" / " + datas.size());
        viewPager.setAdapter(adapter);
        if (datas.size() == 0) {
            tvCurrent.setText(0 + "");
        } else {
            tvCurrent.setText(position + 1 + "");

            tvMoney.setText("¥" + datas.get(position).getPNowPrice());

        }

    }

    private void IndustryStatus(final List<GetQuickView.DataBean.ProductListBean> datas) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                if (QuickActivity.this.datas.size() == 0) {
                    tvCurrent.setText(0 + "");
                } else {
                    tvCurrent.setText(position + 1 + "");
                }

                tvMoney.setText("¥" + datas.get(position).getPNowPrice());

                positions = position;
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

    private void initView() {
        // TODO Auto-generated method stub
        indexs = new ArrayList<>();
        sharePreferenceUtil = new SharePreferenceUtil(QuickActivity.this, Constants.SAVE_USER);
        imgBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });


    }


    private class ViewPageAdapter extends PagerAdapter {


        private final List<GetQuickView.DataBean.ProductListBean> datas;
        private Context context;
        private SparseArray<View> cacheView;

        public ViewPageAdapter(QuickActivity context, List<GetQuickView.DataBean.ProductListBean> datas) {
            this.context = context;
            this.datas = datas;
            this.cacheView = new SparseArray<View>(datas.size());
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {

            View view = cacheView.get(position);
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.vp_item_image, container, false);
                view.setTag(position);
                ImageView image = view.findViewById(R.id.image);
                final PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(image);

                if (!TextUtils.isEmpty(datas.get(position).getPicName() + "?x-oss-process=image/resize,w_"
                        + (Utils.getScreenWidth(QuickActivity.this)))) {
                    Picasso.with(context).load(datas.get(position).getPicName())
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
                    Picasso.with(context).load(R.mipmap.ic_exception)
                            .into(image);
                }
//                RequestOptions requestOptions = new RequestOptions()
//                        .error(R.drawable.ic_exception)
//                        .fallback(R.drawable.ic_exception);
//                Glide.with(QuickActivity.this)
//                        .load(datas.get(position).getPicName()).listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        photoViewAttacher.update();
//                        return false;
//                    }
//                }).apply(requestOptions).into(image);
                photoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {

                        Intent intent = new Intent();
                        intent.setClass(QuickActivity.this, GoodsDetailActivity.class);
                        String pid = datas.get(position).getPId();
                        intent.putExtra("pid", pid);
                        startActivity(intent);
                    }

                    @Override
                    public void onOutsidePhotoTap() {

                    }


                });
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
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
