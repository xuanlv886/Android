package app.cn.extra.mall.merchant.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.view.FlowTagView;
import app.cn.extra.mall.merchant.view.NewsExpandableListView;
import app.cn.extra.mall.merchant.view.ViewHolder;
import app.cn.extra.mall.merchant.vo.GetProductDetails;
import app.cn.extra.mall.merchant.vo.ShoppingCarProduct;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.Utils;

/**
 * 便捷购物 - 商品详情 -商品属性选择
 */
public class GoodsClassifyDialog extends Activity {
    private GetProductDetails goods;
    private String pid;
    private int states;
    private LayoutInflater mInflater = null;
    private SharePreferenceUtil sp;
    @BindView(R.id.elv_goods)
    NewsExpandableListView elv_goods;
    @BindView(R.id.cancles)
    ImageView cancles;
    @BindView(R.id.img_goods_pic)
    ImageView img_goods_pic;
    @BindView(R.id.img_add)
    ImageView img_add;
    @BindView(R.id.img_jian)
    ImageView img_jian;
    @BindView(R.id.rl_buy_nums)
    RelativeLayout rl_buy_nums;
    @BindView(R.id.ll_bottoms)
    LinearLayout ll_bottoms;
    @BindView(R.id.tv_money)
    TextView tv_money;
    @BindView(R.id.tv_stock)
    TextView tv_stock;
    @BindView(R.id.tv_goods_classify)
    TextView tv_goods_classify;
    @BindView(R.id.tv_buy)
    TextView tv_buy;
    @BindView(R.id.et_buy_num)
    EditText et_buy_num;
    @BindView(R.id.bt_back)
    Button bt_back;
    @BindView(R.id.tv_finish)
    TextView tv_finish;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.fl_layout)
    FrameLayout fl_layout;
    @BindView(R.id.rl_layout)
    RelativeLayout rl_layout;
    private NormalGridAdapter normalGridAdapter;
    private HashMap<Integer, Integer> map;
    private List<HashMap<Integer, Integer>> tags = new ArrayList<HashMap<Integer, Integer>>();
    private List<ShoppingCarProduct> lists = new ArrayList<ShoppingCarProduct>();
    private ExpandableAdapter adapter;
    private int currentCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_goods_classify_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        ButterKnife.bind(this);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mInflater = LayoutInflater.from(GoodsClassifyDialog.this);
        Bundle bundle = this.getIntent().getExtras();
        goods = (GetProductDetails) bundle.getSerializable("params");
        pid = bundle.getString("pid");
        states = bundle.getInt("states");
        tv_finish.setBackgroundColor(getResources().getColor(R.color.blue));
        fl_layout.setBackgroundColor(Color.TRANSPARENT);
        rl_layout.setBackgroundColor(Color.TRANSPARENT);
        bt_back.setBackgroundColor(Color.TRANSPARENT);
        initView();
        initData();
    }

    private void initView() {
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        sp = new SharePreferenceUtil(GoodsClassifyDialog.this, Constants.SAVE_USER);
        elv_goods.setGroupIndicator(null);  // 去掉箭头
        if (states == 2) {
            rl_buy_nums.setVisibility(View.VISIBLE);
        } else {
            rl_buy_nums.setVisibility(View.GONE);
        }
    }

    private void initData() {
//        List<GetProductDetails.DataBean.PropertysList2Bean> name = goods.getData().getPropertysList2();
//        if (goods.getData().getPicList() != null
//                && 0 < goods.getData().getPicList().size()) {
//            Picasso.with(this).load(goods.getData().getPicList().get(0).getPicName()
//            ).into(img_goods_pic);
//        } else {
//            Picasso.with(this).load(R.drawable.ic_exception).into(img_goods_pic);
//        }
        if (goods.getData().getPicList() != null && goods.getData().getPicList().size() > 0) {
            RequestOptions requestOptions = new RequestOptions()
                    .error(R.drawable.ic_exception)
                    .fallback(R.drawable.ic_exception);
            Glide.with(GoodsClassifyDialog.this)
                    .load(goods.getData().getPicList().get(0).getPicName() + "?x-oss-process=image/resize,w_"
                            + (Utils.getScreenWidth(GoodsClassifyDialog.this)))
                    .apply(requestOptions)
                    .into(img_goods_pic);
        }
        tv_money.setText("¥ " + goods.getData().getPNowPrice());

        tv_stock.setText("库存" + goods.getData().getPStockNum() + "件");
//        if (!TextUtils.isEmpty(goods.getData().getPpropertyname())) {
//            if (goods.getData().getPpropertyname().length() > 29) {
//                tv_goods_classify.setText("请选择  " + goods.getData().getPpropertyname()
//                        .substring(0, 22) + "  分类");
//            } else {
//                tv_goods_classify.setText("请选择  " + goods.getData().getPpropertyname() + "  分类");
//            }
//        } else {
        tv_goods_classify.setText("请选择商品属性分类");
//        }
        adapter = new ExpandableAdapter(goods.getData().getPropertysList2(), goods.getData().getPropertysList1());
        elv_goods.setAdapter(adapter);
        // 首次加载全部展开
        if (goods.getData().getPropertysList1() != null && goods.getData().getPropertysList1().size() > 0) {
            for (int i = 0; i < goods.getData().getPropertysList1().size(); i++) {
                elv_goods.expandGroup(i);
            }
        } else if (goods.getData().getPropertysList2() != null && goods.getData().getPropertysList2().size() > 0) {
            // 首次加载全部展开
            for (int i = 0; i < goods.getData().getPropertysList2().size(); i++) {
                elv_goods.expandGroup(i);
            }
        }
    }

    @OnClick({R.id.cancles, R.id.img_jian, R.id.img_add, R.id.bt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancles:
                finish();
                break;
            case R.id.img_jian:
                if (currentCount == 1) {
                    return;
                }
                currentCount--;
                et_buy_num.setText(currentCount + "");
                break;
            case R.id.img_add:
                currentCount++;
                et_buy_num.setText(currentCount + "");
                break;
            case R.id.bt_back:
                finish();
                break;
            default:
                break;

        }

    }

    // 商品属性适配器
    private class ExpandableAdapter extends BaseExpandableListAdapter {

        private List<GetProductDetails.DataBean.PropertysList2Bean> data2;
        private List<GetProductDetails.DataBean.PropertysList1Bean> data1;
        private ViewHolder holder;
        private LayoutInflater mInflater = null;


        public ExpandableAdapter(List<GetProductDetails.DataBean.PropertysList2Bean> data2, List<GetProductDetails.DataBean.PropertysList1Bean> data1) {
            this.mInflater = LayoutInflater.from(GoodsClassifyDialog.this);
            this.data2 = data2;
            this.data1 = data1;
        }

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            if (data1 != null && data1.size() > 0) {
                return data1.size();
            } else if (data2 != null && data2.size() > 0) {
                return data2.size();
            }
            return 0;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub

            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            if (data1 != null && data1.size() > 0) {
                return data1.get(groupPosition);
            } else if (data2 != null && data2.size() > 0) {
                return data2.get(groupPosition);
            }
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View view, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (view == null) {
                view = mInflater.inflate(R.layout.relase_goods_fill_group, null);
            }
            TextView text = view.findViewById(R.id.tv_relase_goods_fill_group_title);
            ImageView img = view.findViewById(R.id.img_relase_goods_fill_group_more);
            ImageView img_compelete = view.findViewById(R.id.img_compelete);
            if (data1 != null && data1.size() > 0) {
                text.setText(data1.get(groupPosition).getName());
            } else if (data2 != null && data2.size() > 0) {
                text.setText(data2.get(groupPosition).getName());
            }
            return view;
        }


        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewGroup item = null;
            item = (ViewGroup) mInflater.inflate(
                    R.layout.relase_goods_fill_child, null); //载入gridview布局
            FlowTagView grid = item.findViewById(R.id.gv_child_inside);// 获取girdview的节点
            normalGridAdapter = new NormalGridAdapter(groupPosition);
            grid.setAdapter(normalGridAdapter);
//            grid.setOnItemClickListener(this);

            return item;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }


    }

    // normal 标签GridView
    private class NormalGridAdapter extends BaseAdapter {
        private ViewHolder holder;
        private LayoutInflater mInflater = null;
        private int groupPosition = 0;

        public NormalGridAdapter(int groupPosition) {
            this.mInflater = LayoutInflater.from(GoodsClassifyDialog.this);
            this.groupPosition = groupPosition;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (goods.getData().getPropertysList1() != null && goods.getData().getPropertysList1().size() > 0) {
                return goods.getData().getPropertysList1().get(groupPosition).getValues().size();
            } else if (goods.getData().getPropertysList2() != null && goods.getData().getPropertysList2().size() > 0) {
                return goods.getData().getPropertysList2().get(groupPosition).getValues().size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            if (goods.getData().getPropertysList1() != null && goods.getData().getPropertysList1().size() > 0) {
                return goods.getData().getPropertysList1().get(groupPosition).getValues().get(position);
            } else if (goods.getData().getPropertysList2() != null && goods.getData().getPropertysList2().size() > 0) {
                return goods.getData().getPropertysList2().get(groupPosition).getValues().get(position);
            }
            return null;
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
                arg1 = mInflater.inflate(R.layout.grid_item_relase_goods_fill_common, null);

                holder.tv_grid_item_tag = arg1
                        .findViewById(R.id.tv_grid_item_tag);
                arg1.setTag(holder);
            } else {
                holder = (ViewHolder) arg1.getTag();
            }
            if (goods.getData().getPropertysList1() != null && goods.getData().getPropertysList1().size() > 0) {
                if (!TextUtils.isEmpty(goods.getData().getPropertysList1().get(groupPosition)
                        .getValues().get(position).getValue())) {
                    String value = goods.getData().getPropertysList1().get(groupPosition).getValues().get(position).getValue().replace("\n", "");
                    holder.tv_grid_item_tag.setText(value);
                } else {
                    holder.tv_grid_item_tag.setText("");
                }
            } else if (goods.getData().getPropertysList2() != null && goods.getData().getPropertysList2().size() > 0) {
                if (!TextUtils.isEmpty(goods.getData().getPropertysList2().get(groupPosition)
                        .getValues().get(position).getValue())) {
                    String value = goods.getData().getPropertysList2().get(groupPosition).getValues().get(position).getValue().replace("\n", "");
                    holder.tv_grid_item_tag.setText(value);
                } else {
                    holder.tv_grid_item_tag.setText("");
                }
            }

            if (tags != null && tags.size() > 0) { // 已有被选中的标签
                for (int i = 0; i < tags.size(); i++) {
                    // 确定当前标签是否被选中
                    if (tags.get(i).containsKey(groupPosition)
                            && tags.get(i).containsValue(position)) {
                        holder.tv_grid_item_tag.setBackgroundDrawable(
                                getResources().getDrawable(R.drawable.checked_bg));
                        holder.tv_grid_item_tag.setTextColor(Color.WHITE);
                    }
                }
            }
            ItemListener itemListener = new ItemListener(position, goods);
            holder.tv_grid_item_tag.setOnClickListener(itemListener);
            return arg1;
        }

        class ItemListener implements View.OnClickListener {
            private int m_position;
            private GetProductDetails datas;

            ItemListener(int pos, GetProductDetails data) {
                m_position = pos;
                datas = data;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_grid_item_tag:
                        map = new HashMap<Integer, Integer>(1);
                        if (0 == tags.size()) { // 当前集合为null
                            map.put(groupPosition, m_position);
                            tags.add(0, map);
                            v.setBackgroundDrawable(
                                    getResources().getDrawable(R.drawable.checked_bg));
                            tags.size();
                            Log.e("tags1", tags.toString());
                        } else { // 集合中已有数据
                            // 确定点击的标签是否被选中过
                            Log.e("tagsa", tags.toString());
                            for (int i = 0; i < tags.size(); i++) {
                                if (tags.get(i).containsKey(groupPosition)) {
                                    Log.e("groupPosition", groupPosition + "");
                                    if (tags.get(i).containsValue(m_position)) {
                                        Log.e("m_position", m_position + "");
                                        v.setBackgroundDrawable(
                                                getResources().getDrawable(R.drawable.normal_bg));
                                        tags.remove(i);
                                        break;
                                    }
                                    tags.remove(i);
                                }
                            }
                            Log.e("tagsb", tags.toString());
                            map.put(groupPosition, m_position);
                            tags.add(tags.size(), map);
                            Log.e("tagsc", tags.toString());


                            adapter.notifyDataSetChanged();
                        }

                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }

            }
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
