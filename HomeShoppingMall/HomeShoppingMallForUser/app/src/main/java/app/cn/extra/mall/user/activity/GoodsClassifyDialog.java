package app.cn.extra.mall.user.activity;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.view.FlowTagView;
import app.cn.extra.mall.user.view.NewsExpandableListView;
import app.cn.extra.mall.user.view.ViewHolder;
import app.cn.extra.mall.user.vo.DoAddProductToTrolley;
import app.cn.extra.mall.user.vo.GetProductDetails;
import app.cn.extra.mall.user.vo.ProProetry;
import app.cn.extra.mall.user.vo.ShoppingCarProduct;
import app.cn.extra.mall.user.vo.ShoppingCarStore;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

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
        adapter = new ExpandableAdapter(goods.getData().getPropertysList2());
        elv_goods.setAdapter(adapter);
        if (goods.getData().getPropertysList2() != null && goods.getData().getPropertysList2().size() > 0) {
            // 首次加载全部展开
            for (int i = 0; i < goods.getData().getPropertysList2().size(); i++) {
                elv_goods.expandGroup(i);
            }
        }
    }


    @OnClick({R.id.cancles, R.id.tv_buy, R.id.ll_bottoms, R.id.img_jian, R.id.img_add, R.id.bt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancles:
                finish();
                break;
            case R.id.tv_buy:
                PayGoods();
                break;
            case R.id.ll_bottoms:
                if (null != progressBar) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                if (states == 2) {
                    PayGoods();
                } else {
                    if (goods.getData().getPStockNum() > 0) {
                        AddCartPort();
                    } else {
                        CustomToast.showToast(GoodsClassifyDialog.this,
                                "该商品暂时无货，无法加入购物车！", Toast.LENGTH_SHORT);
                    }
                }
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

    private void PayGoods() {
        //商品属性  转换成josn字符串
        List<ProProetry> ppid = new ArrayList<>();
        String proproetry = "";
        for (HashMap<Integer, Integer> hm : tags) {
            Set<Integer> set = hm.keySet();
            for (Integer key : set) {
                Integer Stringvalue = hm.get(key);
                String value = goods.getData().getPropertysList2().get(key).getValues().get(Stringvalue).getValue();
                String name = goods.getData().getPropertysList2().get(key).getName();
                proproetry += name + ":" + value + ";";
            }
        }
        if (goods.getData().getPropertysList2().size() != 0 && !TextUtils.isEmpty(proproetry)) {
            if (proproetry.contains(";")) {
                String[] s = proproetry.split(";");
                if (s.length != goods.getData().getPropertysList2().size()) {
                    CustomToast.showToast(GoodsClassifyDialog.this,
                            "请选择商品的属性！", Toast.LENGTH_SHORT);
                    if (null != progressBar) {
                        progressBar.setVisibility(View.GONE);
                    }
                    return;
                }
            }
        }
        if (goods.getData().getPropertysList2().size() != 0 && TextUtils.isEmpty(proproetry)) {
            CustomToast.showToast(GoodsClassifyDialog.this,
                    "请选择商品的属性！", Toast.LENGTH_SHORT);
            return;
        }
        String uids = sp.getUID();
        if (TextUtils.isEmpty(uids)) {
            Intent intentlogin = new Intent();
            intentlogin.setClass(GoodsClassifyDialog.this, LoginActivity.class);
            startActivity(intentlogin);
            CustomToast.showToast(GoodsClassifyDialog.this,
                    "请先登录账号在进行购买！", Toast.LENGTH_SHORT);
            return;
        }
        if (goods.getData().getPStockNum() <= 0) {
            CustomToast.showToast(GoodsClassifyDialog.this,
                    "该商品暂时无货，无法立即购买！", Toast.LENGTH_SHORT);
            return;
        }
        Intent intentbuy = new Intent();
        intentbuy.setClass(GoodsClassifyDialog.this, CommodityBuyActivity.class);
        Bundle bundle = new Bundle();
        List<ShoppingCarProduct> list = new ArrayList<ShoppingCarProduct>();
        ShoppingCarStore store = new ShoppingCarStore();
        store.setSid(0);
        ShoppingCarProduct product = new ShoppingCarProduct();
        product.setPro_name(goods.getData().getPName());
        product.setProperty(proproetry);
        int num = Integer.parseInt(et_buy_num.getText().toString());
        product.setNum(num);
        product.setSprice(Double.valueOf(goods.getData().getPNowPrice()));
        product.setOnlinePrice(Double.valueOf(goods.getData().getPOriginalPrice()));
        product.setIntegralPrice(Double.valueOf(goods.getData().getPNowPrice()));
        product.setPic_url(goods.getData().getPicList().get(0).getPicName());
        product.setsId(goods.getData().getSId());
        product.setPro_id(pid);
        list.add(product);
        bundle.putInt("state", 0);
        bundle.putSerializable("goodsDetail", (Serializable) list);
        intentbuy.putExtras(bundle);
        startActivity(intentbuy);
        list.clear();
        finish();
    }

    private void AddCartPort() {
        String uid = sp.getUID();
        if (TextUtils.isEmpty(uid)) {
            Intent intent = new Intent();
            intent.setClass(GoodsClassifyDialog.this, LoginActivity.class);
            startActivity(intent);
            CustomToast.showToast(GoodsClassifyDialog.this,
                    "请先登录账号！", Toast.LENGTH_SHORT);
            if (null != progressBar) {
                progressBar.setVisibility(View.GONE);
            }
            return;
        }
        if (Utils.isNetworkAvailable(GoodsClassifyDialog.this)) {
            //商品属性  转换成josn字符串
            List<ProProetry> ppid = new ArrayList<>();
            String proproetry = "";
            for (HashMap<Integer, Integer> hm : tags) {
                Set<Integer> set = hm.keySet();
                for (Integer key : set) {
                    Integer Stringvalue = hm.get(key);
                    String value = goods.getData().getPropertysList2().get(key).getValues().get(Stringvalue).getValue();
                    String name = goods.getData().getPropertysList2().get(key).getName();
                    proproetry += name + ":" + value + ";";
                }
            }
            if (goods.getData().getPropertysList2().size() != 0 && !TextUtils.isEmpty(proproetry)) {
                if (proproetry.contains(";")) {
                    String[] s = proproetry.split(";");
                    if (s.length != goods.getData().getPropertysList2().size()) {
                        CustomToast.showToast(GoodsClassifyDialog.this,
                                "请选择商品的属性！", Toast.LENGTH_SHORT);
                        if (null != progressBar) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return;
                    }
                }
            }
            if (goods.getData().getPropertysList2().size() != 0 && TextUtils.isEmpty(proproetry)) {
                CustomToast.showToast(GoodsClassifyDialog.this,
                        "请选择商品的属性！", Toast.LENGTH_SHORT);
                if (null != progressBar) {
                    progressBar.setVisibility(View.GONE);
                }
                return;
            }
            if (Utils.isNetworkAvailable(GoodsClassifyDialog.this)) {
                OkHttpUtils
                        .post()
                        .url(Constants.addUserTrolley)
                        .addParams("uId", uid)
                        .addParams("utProductProperty", proproetry)
                        .addParams("pId", pid)
                        .addParams("sId", goods.getData().getSId())
                        .addParams("utProductNum", "1")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                if (null != progressBar) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                DoAddProductToTrolley toTrolley = new DoAddProductToTrolley();
                                toTrolley = Utils.parserJsonResult(response, DoAddProductToTrolley.class);
                                if (null != progressBar) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                if ("true".equals(toTrolley.getFlag())) {
                                    if (Constants.OK.equals(toTrolley.getData().getStatus())) {
                                        CustomToast.showToast(GoodsClassifyDialog.this, "加入购物车成功", Toast.LENGTH_LONG);
                                        finish();
                                    }

                                } else {
                                    CustomToast.showToast(GoodsClassifyDialog.this, toTrolley.getErrorString(), Toast.LENGTH_LONG);
                                }
                            }
                        });
            } else {
                if (null != progressBar) {
                    progressBar.setVisibility(View.GONE);
                }
                CustomToast.showToast(GoodsClassifyDialog.this, Constants.NETWORK_ERROR, Toast.LENGTH_LONG);
            }
        }
    }

    // 商品属性适配器
    private class ExpandableAdapter extends BaseExpandableListAdapter {

        private List<GetProductDetails.DataBean.PropertysList2Bean> data2;
        private ViewHolder holder;
        private LayoutInflater mInflater = null;


        public ExpandableAdapter(List<GetProductDetails.DataBean.PropertysList2Bean> data2) {
            this.mInflater = LayoutInflater.from(GoodsClassifyDialog.this);
            this.data2 = data2;
        }

        @Override
        public int getGroupCount() {
            // TODO Auto-generated method stub
            if (data2 != null && data2.size() > 0) {
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
            if (data2 != null && data2.size() > 0) {
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
            if (data2 != null && data2.size() > 0) {
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
            if (goods.getData().getPropertysList2() != null && goods.getData().getPropertysList2().size() > 0) {
                return goods.getData().getPropertysList2().get(groupPosition).getValues().size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            if (goods.getData().getPropertysList2() != null && goods.getData().getPropertysList2().size() > 0) {
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
            if (goods.getData().getPropertysList2() != null && goods.getData().getPropertysList2().size() > 0) {
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
                        } else { // 集合中已有数据
                            // 确定点击的标签是否被选中过
                            for (int i = 0; i < tags.size(); i++) {
                                if (tags.get(i).containsKey(groupPosition)) {
                                    if (tags.get(i).containsValue(m_position)) {
                                        v.setBackgroundDrawable(
                                                getResources().getDrawable(R.drawable.normal_bg));
                                        tags.remove(i);
                                        break;
                                    }
                                    tags.remove(i);
                                }
                            }
                            map.put(groupPosition, m_position);
                            tags.add(tags.size(), map);


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
