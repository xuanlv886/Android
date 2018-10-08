package app.cn.extra.mall.user.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.fragment.AllFragment;
import app.cn.extra.mall.user.fragment.AlreadyRefundOrderFragment;
import app.cn.extra.mall.user.fragment.CompleteOrderFragment;
import app.cn.extra.mall.user.fragment.PendingCollectGoodsFragment;
import app.cn.extra.mall.user.fragment.PendingDeliveryFragment;
import app.cn.extra.mall.user.fragment.PendingEvaluateFragment;
import app.cn.extra.mall.user.fragment.PendingPaymentFragment;
import app.cn.extra.mall.user.fragment.UnRefundOrderFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseFragment;
import cfkj.app.cn.cfkjcommonlib.view.StatusBar.StatusBarUtil;

/**
 * 订单管理
 */
public class OrderManagementActivity extends FragmentActivity {
    private List<BaseFragment> fragments;
    @BindView(R.id.vPager)
    ViewPager mViewPager;
//    @BindView(R.id.iv_tab_bottom_img)
//    ImageView cursorIv;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_pending_payment)
    TextView tvPendingPayment;
    @BindView(R.id.tv_pending_delivery)
    TextView tvPendingDelivery;
    @BindView(R.id.tv_pending_collect_goods)
    TextView tvPendingCollectGoods;
    @BindView(R.id.tv_pending_evaluate)
    TextView tvPendingEvaluate;
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
     * 全部
     */
    private static final int TAB_0 = 0;
    /**
     * 待付款
     */
    private static final int TAB_1 = 1;
    /**
     * 待发货
     */
    private static final int TAB_2 = 2;
    /**
     * 待收货
     */
    private static final int TAB_3 = 3;
    /**
     * 待评价
     */
    private static final int TAB_4 = 4;
    public static Context context;
    private String[] type = new String[]{"全部", "待付款", "待发货", "待收货", "待评价", "已完成", "待退款", "已退款"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management);
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

//    /**
//     * 初始化底部下划线
//     */
//    private void initImageView() {
//        // 获取图片宽度
//        lineWidth = BitmapFactory.decodeResource(getResources(), R.drawable.under_line).getWidth();
//        // Android提供的DisplayMetrics可以很方便的获取屏幕分辨率
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenW = dm.widthPixels; // 获取分辨率宽度
//        offset = (screenW / TAB_COUNT - lineWidth) / 2;  // 计算偏移值
//        Matrix matrix = new Matrix();
//        matrix.postTranslate(offset, 0);
//        // 设置下划线初始位置
//        cursorIv.setImageMatrix(matrix);
//    }

    /**
     * 初始化ViewPager并添加监听事件
     */
    private void initVPager() {
        fragments = new ArrayList<>();
        fragments.add(new AllFragment());
        fragments.add(new PendingPaymentFragment());
        fragments.add(new PendingDeliveryFragment());
        fragments.add(new PendingCollectGoodsFragment());
        fragments.add(new PendingEvaluateFragment());
        fragments.add(new CompleteOrderFragment());
        fragments.add(new UnRefundOrderFragment());
        fragments.add(new AlreadyRefundOrderFragment());
        mViewPager.setAdapter(new OrderTypeAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(0);
//        titles = new TextView[]{tvAll, tvPendingPayment, tvPendingDelivery, tvPendingCollectGoods, tvPendingEvaluate};
//        mViewPager.setOffscreenPageLimit(titles.length);
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            int one = offset * 2 + lineWidth;
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                // 下划线开始移动前的位置
//                float fromX = one * current_index;
//                // 下划线移动完毕后的位置
//                float toX = one * position;
//                Animation animation = new TranslateAnimation(fromX, toX, 0, 0);
//                // 设置为true，防止下划线返回，也就是动画终止时让View停留在最后一帧，不然会下划线又会回到没有执行之前的状态
//                // 如果不设置该属性，则下划线会回到动画开始的位置
//                animation.setFillAfter(true);
//                animation.setDuration(500);
//                // 给图片添加动画
//                cursorIv.startAnimation(animation);
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
        mIndicator.setViewPager(mViewPager);
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
