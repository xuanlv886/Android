package app.cn.extra.mall.merchant.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.vo.GetProductDetails;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 便捷购物 - 商品详情 -商品参数
 */
public class GoodsPropertyDialog extends Activity {
    @BindView(R.id.cancle)
    ImageView cancle;
    private GetProductDetails goods;
    private String pid;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    private CommonAdapter<GetProductDetails.DataBean.PropertysList1Bean> list1BeanCommonAdapter;
    private CommonAdapter<GetProductDetails.DataBean.PropertysList2Bean> list2BeanCommonAdapter;
    @BindView(R.id.bt_back)
    Button btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_goods_property_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        ButterKnife.bind(this);
        Bundle bundle = this.getIntent().getExtras();
        goods = (GetProductDetails) bundle.getSerializable("params");
        pid = bundle.getString("pid");
        initView();
        if (goods.getData().getPropertysList1() != null && goods.getData().getPropertysList1().size() > 0) {
            initList1Data();
        } else if(goods.getData().getPropertysList2() != null && goods.getData().getPropertysList2().size() > 0){
            initList2Data();
        }
    }

    private void initView() {
        initRefreshLayout();
        initRecyclerView();
    }

    private void initList1Data() {
        List<GetProductDetails.DataBean.PropertysList1Bean> datas = goods.getData().getPropertysList1();
        list1BeanCommonAdapter = new CommonAdapter<GetProductDetails.DataBean.PropertysList1Bean>(GoodsPropertyDialog.this, R.layout.list_item_goods_classify, datas) {
            @Override
            protected void convert(ViewHolder holder, GetProductDetails.DataBean.PropertysList1Bean propertysList1Bean, int position) {
                if (!TextUtils.isEmpty(datas.get(position).getName())) {
                    holder.setText(R.id.tv_list_item_goods_classify_key, datas.get(position).getName());
                } else {
                    holder.setText(R.id.tv_list_item_goods_classify_key, "");
                }

                if (!TextUtils.isEmpty(datas.get(position).getValues().get(0).getValue())) {
                    holder.setText(R.id.tv_list_item_goods_classify_value, datas.get(position).getValues().get(0).getValue());
                } else {
                    holder.setText(R.id.tv_list_item_goods_classify_value, "");
                }
            }
        };
        recyclerView.setAdapter(list1BeanCommonAdapter);
    }

    private void initList2Data() {
        List<GetProductDetails.DataBean.PropertysList2Bean> datas = goods.getData().getPropertysList2();
        list2BeanCommonAdapter = new CommonAdapter<GetProductDetails.DataBean.PropertysList2Bean>(GoodsPropertyDialog.this, R.layout.list_item_goods_classify, datas) {
            @Override
            protected void convert(ViewHolder holder, GetProductDetails.DataBean.PropertysList2Bean propertysList2Bean, int position) {
                if (!TextUtils.isEmpty(datas.get(position).getName())) {
                    holder.setText(R.id.tv_list_item_goods_classify_key, datas.get(position).getName());
                } else {
                    holder.setText(R.id.tv_list_item_goods_classify_key, "");
                }

                if (!TextUtils.isEmpty(datas.get(position).getValues().get(0).getValue())) {
                    holder.setText(R.id.tv_list_item_goods_classify_value, datas.get(position).getValues().get(0).getValue());
                } else {
                    holder.setText(R.id.tv_list_item_goods_classify_value, "");
                }
            }
        };
        recyclerView.setAdapter(list2BeanCommonAdapter);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 线性布局
         */
        LinearLayoutManager layoutManage = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManage);
//        /**
//         * 添加分隔线
//         */
//        countryLvcountry.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
//                DividerItemDecoration.HORIZONTAL));
    }

    /**
     * 初始化SwipeRefreshLayout
     */
    private void initRefreshLayout() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        /**
         * 禁用刷新
         */
        refreshLayout.setEnabled(false);

    }

    @OnClick({R.id.cancle, R.id.bt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancle:
                finish();
                break;
            case R.id.bt_back:
                finish();
                break;
            default:
                break;
        }
    }
}
