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
import app.cn.extra.mall.merchant.fragment.AllFragment;
import app.cn.extra.mall.merchant.fragment.AlreadyRefundOrderFragment;
import app.cn.extra.mall.merchant.fragment.CompleteOrderFragment;
import app.cn.extra.mall.merchant.fragment.PendingCollectGoodsFragment;
import app.cn.extra.mall.merchant.fragment.PendingDeliveryFragment;
import app.cn.extra.mall.merchant.fragment.PendingEvaluateFragment;
import app.cn.extra.mall.merchant.fragment.PendingPaymentFragment;
import app.cn.extra.mall.merchant.fragment.UnRefundOrderFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseFragment;

/**
 * 订单管理
 */
public class OrderManagementActivity extends FragmentActivity {
    private List<BaseFragment> fragments;
    @BindView(R.id.vPager)
    ViewPager mViewPager;
    @BindView(R.id.indicator)
    TabPageIndicator mIndicator;
    private TextView[] titles;
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
