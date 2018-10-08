package app.cn.extra.mall.user.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.fragment.CompletedFragment;
import app.cn.extra.mall.user.fragment.ConductingFragment;
import app.cn.extra.mall.user.fragment.DeliveryFragment;
import app.cn.extra.mall.user.fragment.EvaluatedFragment;
import app.cn.extra.mall.user.fragment.PendingInspectionGoodsFragment;
import app.cn.extra.mall.user.fragment.PendingOrderFragment;
import app.cn.extra.mall.user.fragment.PendingSureFragment;
import app.cn.extra.mall.user.fragment.PickUpGoodsFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseFragment;
import cfkj.app.cn.cfkjcommonlib.view.StatusBar.StatusBarUtil;

/**
 * 我的需求
 */
public class MyDemandActivity extends FragmentActivity {
    private List<BaseFragment> fragments;
    @BindView(R.id.vPager)
    ViewPager mViewPager;
    //    @BindView(R.id.iv_tab_bottom_img)
//    ImageView cursorIv;
    @BindView(R.id.tv_pending_order)
    TextView tvPendingOrder;
    @BindView(R.id.tv_pending_sure)
    TextView tvPendingSure;
    @BindView(R.id.tv_conducting)
    TextView tvConducting;
    @BindView(R.id.tv_completed)
    TextView tvCompleted;
    @BindView(R.id.tv_release)
    TextView tvRelease;
    @BindView(R.id.indicator)
    TabPageIndicator mIndicator;
    private TextView[] titles;

    /**
     * 偏移量（手机屏幕宽度 / 选项卡总数 - 选项卡长度） / 2
     */
    private int offset = 0;

    /**
     * 下划线图片宽度
     */
    private int lineWidth;

    /**
     * 当前选项卡的位置
     */
    private int current_index = 0;

    /**
     * 选项卡总数
     */
    private static final int TAB_COUNT = 3;
    /**
     * 待接单
     */
    private static final int TAB_0 = 0;
    /**
     * 待确认
     */
    private static final int TAB_1 = 1;
    /**
     * 进行中
     */
    private static final int TAB_2 = 2;
    /**
     * 已完成
     */
    private static final int TAB_3 = 3;
    public static Context context;
    private String[] type = new String[]{"待接单", "待确认", "进行中", "已完成","取货中","待验货","送货中","已评价"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_demand);
        ButterKnife.bind(this);
        context = this;
//        setStatusBar();
//        initImageView();
        initVPager();
    }

    /**
     * 设置沉浸式状态栏
     */
    protected void setStatusBar() {
        StatusBarUtil.setColor(this,
                getResources().getColor(R.color.white), 1);
    }

    /**
     * 初始化底部下划线
     */
    private void initImageView() {
        // 获取图片宽度
        lineWidth = BitmapFactory.decodeResource(getResources(), R.drawable.under_line).getWidth();
        // Android提供的DisplayMetrics可以很方便的获取屏幕分辨率
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels; // 获取分辨率宽度
        offset = (screenW / TAB_COUNT - lineWidth) / 2;  // 计算偏移值
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        // 设置下划线初始位置
//        cursorIv.setImageMatrix(matrix);
    }

    /**
     * 初始化ViewPager并添加监听事件
     */
    private void initVPager() {
        fragments = new ArrayList<>();
        fragments.add(new PendingOrderFragment());
        fragments.add(new PendingSureFragment());
        fragments.add(new ConductingFragment());
        fragments.add(new CompletedFragment());
        fragments.add(new PickUpGoodsFragment());
        fragments.add(new PendingInspectionGoodsFragment());
        fragments.add(new DeliveryFragment());
        fragments.add(new EvaluatedFragment());
        mViewPager.setAdapter(new OrderTypeAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);
        mIndicator.setViewPager(mViewPager);
//        titles = new TextView[]{tvPendingOrder, tvPendingSure, tvConducting, tvCompleted};
//        mViewPager.setOffscreenPageLimit(titles.length);
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
////            int one = offset * 2 + lineWidth;
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
////                // 下划线开始移动前的位置
////                float fromX = one * current_index;
////                // 下划线移动完毕后的位置
////                float toX = one * position;
////                Animation animation = new TranslateAnimation(fromX, toX, 0, 0);
////                // 设置为true，防止下划线返回，也就是动画终止时让View停留在最后一帧，不然会下划线又会回到没有执行之前的状态
////                // 如果不设置该属性，则下划线会回到动画开始的位置
////                animation.setFillAfter(true);
////                animation.setDuration(500);
////                // 给图片添加动画
////                cursorIv.startAnimation(animation);
//                // 当前Tab的字体变成红色
//                titles[position].setTextColor(getResources().getColor(R.color.blue));
//                titles[current_index].setTextColor(Color.BLACK);
//                current_index = position;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
    }

    @OnClick({R.id.img_back, R.id.tv_release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
//            case R.id.tv_pending_order:
//                // 避免重复加载
//                if (mViewPager.getCurrentItem() != TAB_0) {
//                    mViewPager.setCurrentItem(TAB_0);
//                }
//                break;
//            case R.id.tv_pending_sure:
//                if (mViewPager.getCurrentItem() != TAB_1) {
//                    mViewPager.setCurrentItem(TAB_1);
//                }
//                break;
//            case R.id.tv_conducting:
//                if (mViewPager.getCurrentItem() != TAB_2) {
//                    mViewPager.setCurrentItem(TAB_2);
//                }
//                break;
//            case R.id.tv_completed:
//                if (mViewPager.getCurrentItem() != TAB_3) {
//                    mViewPager.setCurrentItem(TAB_3);
//                }
//                break;
            case R.id.tv_release:
                Intent intent = new Intent(MyDemandActivity.this, SendDemandActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    class OrderTypeAdapter extends FragmentPagerAdapter {

        public OrderTypeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return type[position];
        }
    }
}
