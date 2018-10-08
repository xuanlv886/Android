package app.cn.extra.mall.merchant.activity;

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

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.fragment.CompletedFragment;
import app.cn.extra.mall.merchant.fragment.ConductingFragment;
import app.cn.extra.mall.merchant.fragment.DeliveryFragment;
import app.cn.extra.mall.merchant.fragment.EvaluatedFragment;
import app.cn.extra.mall.merchant.fragment.PendingInspectionGoodsFragment;
import app.cn.extra.mall.merchant.fragment.PendingSureFragment;
import app.cn.extra.mall.merchant.fragment.PickUpGoodsFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseFragment;

/**
 * 我的需求
 */
public class MyDemandActivity extends FragmentActivity {
    private List<BaseFragment> fragments;
    @BindView(R.id.vPager)
    ViewPager mViewPager;
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
    private String[] type = new String[]{"待确认", "进行中", "已完成","取货中","待验货","送货中","已评价"};

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
     * 初始化ViewPager并添加监听事件
     */
    private void initVPager() {
        fragments = new ArrayList<>();
//        fragments.add(new PendingOrderFragment());
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
