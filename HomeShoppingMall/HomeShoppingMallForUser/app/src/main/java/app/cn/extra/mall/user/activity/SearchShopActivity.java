package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;

import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.view.CustomDialog;
import app.cn.extra.mall.user.view.NewsGridView;
import app.cn.extra.mall.user.view.ViewHolder;
import app.cn.extra.mall.user.vo.GetProductType;
import app.cn.extra.mall.user.vo.GetProductTypeDetail;
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

/**
 * 分类搜索页面
 */
public class SearchShopActivity extends BaseActivty {
    @BindView(R.id.relativeLayout1)
    RelativeLayout relativeLayout1;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rl_shop)
    RelativeLayout rlShop;
    @BindView(R.id.lv_classify)
    ListView lvClassify;
    @BindView(R.id.elv_shopList)
    ExpandableListView elvShopList;
    @BindView(R.id.ll_service_load)
    LinearLayout llServiceLoad;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private VaryViewHelperController varyViewHelperController;
    SharePreferenceUtil sp = null;
    //    private CirclePageIndicator mIndicator;
    private VaryViewHelperController helperController;
    private ClassifyAdapter adapter;
    private ExpertsListViewAdapter expertsListViewAdapter;
    private int pos = 0;
    private CustomDialog customDialog = new CustomDialog();
    /**
     * 大类实体
     */
    private GetProductType.DataBean Marketdatas;
    /**
     * 商品底层
     */
    private List<GetProductTypeDetail.DataBean> Detaildata;
    /**
     * 首页跳转携带的大类id
     */
    String ptId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);
        ButterKnife.bind(this);
        sp = new SharePreferenceUtil(SearchShopActivity.this, Constants.SAVE_USER);
        Intent intent = getIntent();
        ptId = intent.getStringExtra("ptId");
//        markeiditem = "0796071a-5c6e-11e7-8a4d-00163e2e094a";
//        openStyle = intent.getIntExtra("openStyle", 0);
//        commId = intent.getStringExtra("commId");
        initView();
        helperController = createCaseViewHelperController(rlShop);
        helperController.setUpRefreshViews(helperController.getErrorView(), helperController.getNetworkPoorView());
        initData();
    }

    @Override
    public void clickEvent() {
        showLongToast(SearchShopActivity.this, "正在刷新中...");
        getProductType();
    }

    private void initView() {
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
//        rlSearch = findViewById(R.id.rl_search);
//        rlShop = findViewById(R.id.rl_shop);
//        etSearch = findViewById(R.id.et_search);
//        etSearch.setOnClickListener(this);
//        elvShopList = findViewById(elv_shopList);
//        lvClassify = findViewById(lv_classify);
//        llServiceLoad = findViewById(R.id.ll_service_load);
        lvClassify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                adapter.notifyDataSetChanged();
                //商品小类接口
                String productId = Marketdatas.getProductTypeList().get(position).getPtId();
//                String productName = Marketdatas.getProductTypeList().get(position).getPtName();
//                markeNames.clear();
//                markeNames.add("热门品牌");
//                markeNames.add(markeName);
                getProductTypeDetailByPtId(productId, pos);
            }
        });
//        rlSearch.setOnClickListener(this);
    }


    @OnClick({R.id.img_back, R.id.et_search})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.et_search:
                Intent intent = new Intent();
                intent.setClass(this, SearchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void initData() {
        //商场大类接口
//        customDialog.showRoundProcessDialog(this, R.layout.dialog_loading);

        getProductType();

    }

    /**
     * 商品大类接口
     */
    private void getProductType() {
        if (Utils.isNetworkAvailable(SearchShopActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.getProductType)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            customDialog.dismissDialog();
                            helperController.showErrorView();
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }

                            helperController.restore();
                            GetProductType dogetProductType = new GetProductType();
                            dogetProductType = Utils.parserJsonResult(response, GetProductType.class);
//                            if (customDialog != null && customDialog.isShow()) {
//                                customDialog.dismissDialog();
//                            }
                            if ("true".equals(dogetProductType.getFlag())) {
                                Marketdatas = dogetProductType.getData();
                                adapter = new ClassifyAdapter(Marketdatas);
                                lvClassify.setAdapter(adapter);
                                String productId = "";
                                if (TextUtils.isEmpty(ptId)) {
                                    // 默认展示第一个类别
                                    productId = Marketdatas.getProductTypeList().get(0).getPtId();
                                } else {
                                    //根据首页传值展示
                                    for (int i = 0; i < Marketdatas.getProductTypeList().size(); i++) {
                                        if (ptId.equals(Marketdatas.getProductTypeList().get(i).getPtId())) {
                                            productId = Marketdatas.getProductTypeList().get(i).getPtId();
                                            pos = i;
                                        }
                                    }
                                }
//                                String productName = Marketdatas.getProductTypeList().get(0).getPtName();
                                // 获取品牌和小类
                                getProductTypeDetailByPtId(productId, 0);
                            } else {
                                showLongToast(SearchShopActivity.this, dogetProductType.getErrorString());
                                helperController.showEmptyView();
                            }
                        }
                    });
        } else {
//            if (customDialog != null && customDialog.isShow()) {
//                customDialog.dismissDialog();
//            }
            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
            helperController.showErrorView();
            showLongToast(SearchShopActivity.this, Constants.NETWORK_ERROR);
        }
    }

    /**
     * 商品小类接口
     *
     * @param marketId 商品大类id
     * @param pos      大类列表点击位置
     */
    private void getProductTypeDetailByPtId(final String marketId, final int pos) {
        if (Utils.isNetworkAvailable(SearchShopActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.getProductTypeDetailByPtId)
                    .addParams("ptId", marketId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            GetProductTypeDetail detailByPtId = new GetProductTypeDetail();
                            detailByPtId = Utils.parserJsonResult(response, GetProductTypeDetail.class);
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            if ("true".equals(detailByPtId.getFlag())) {
                                Detaildata = detailByPtId.getData();
                                expertsListViewAdapter = new ExpertsListViewAdapter(Detaildata, pos);
                                elvShopList.setAdapter(expertsListViewAdapter);
                                elvShopList.setEmptyView(llServiceLoad);
                                elvShopList.setGroupIndicator(null);  // 去掉箭头
                                // 首次加载全部展开
                                for (int i = 0; i < Detaildata.size(); i++) {
                                    elvShopList.expandGroup(i);
                                }

                                // 不能点击group收缩
                                elvShopList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                                    @Override
                                    public boolean onGroupClick(ExpandableListView parent, View v,
                                                                int groupPosition, long id) {
                                        return true;
                                    }
                                });
                                // 加载顶部广告轮播图
//                                if (datas != null) {
//                                    datas.clear();
//                                }
//                                datas = Detaildata.getBigTypePics();
//                                processData();
                            } else {
                                CustomToast.showToast(SearchShopActivity.this,
                                        detailByPtId.getErrorString(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            CustomToast.showToast(SearchShopActivity.this,
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);
        }
    }

    /**
     * 左侧listview 大类列表
     */
    private class ClassifyAdapter extends BaseAdapter {
        private GetProductType.DataBean datas;
        private ViewHolder holder;
        private LayoutInflater mInflater = null;

        public ClassifyAdapter(GetProductType.DataBean data) {
            this.mInflater = LayoutInflater.from(SearchShopActivity.this);
            this.datas = data;
        }

        @Override
        public int getCount() {
            return datas.getProductTypeList().size();
        }

        @Override
        public Object getItem(int position) {
            return datas.getProductTypeList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View arg1, ViewGroup parent) {
            holder = new ViewHolder();
            if (arg1 == null) {
                arg1 = mInflater.inflate(R.layout.list_item_big_type, null);

                holder.tv_list_item_big_type = (TextView) arg1
                        .findViewById(R.id.tv_list_item_big_type);
                arg1.setTag(holder);
            } else {
                holder = (ViewHolder) arg1.getTag();
            }

            if (pos == position) {
                arg1.setBackgroundResource(R.color.gray);
                holder.tv_list_item_big_type.setTextColor(
                        getResources().getColor(R.color.blue));
            } else {
                arg1.setBackgroundResource(R.color.white);
                holder.tv_list_item_big_type.setTextColor(
                        getResources().getColor(R.color.black));
            }
            if (!TextUtils.isEmpty(datas.getProductTypeList().get(position).getPtName())) {
                holder.tv_list_item_big_type.setText(datas.getProductTypeList().get(position).getPtName());
            }
            return arg1;
        }
    }

    /**
     * ExpertsListView的适配器
     */
    private class ExpertsListViewAdapter extends BaseExpandableListAdapter {
        private final List<GetProductTypeDetail.DataBean> detaildata;
        private final int pos;
        private LayoutInflater mInflater = null;

        public ExpertsListViewAdapter(List<GetProductTypeDetail.DataBean> detaildata, int pos) {
            this.mInflater = LayoutInflater.from(SearchShopActivity.this);
            this.detaildata = detaildata;
            this.pos = pos;
        }

        @Override
        public int getGroupCount() {
            return detaildata.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupPosition;

        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View view, ViewGroup parent) {
            if (view == null) {
                view = mInflater.inflate(R.layout.industry_common_group, null);
            }
            TextView text = view.findViewById(R.id.tv_industry_common_group_title);

            if (detaildata.size() != 0) {
                text.setText(detaildata.get(groupPosition).getPtdName());
            }
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View view, ViewGroup parent) {
            //载入gridview布局
            ViewGroup item = (ViewGroup) mInflater.inflate(
                    R.layout.industry_common_child, null);
            // 获取girdview的节点
            NewsGridView grid = item.findViewById(R.id.gv_child_inside);
            grid.setAdapter(new ExpertGridAdapter(groupPosition, detaildata));
            return item;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    /**
     * ExpandableListView内GridView的适配器
     */
    private class ExpertGridAdapter extends BaseAdapter {
        private final int groupPosition;
        private final List<GetProductTypeDetail.DataBean> detaildata;
        private ViewHolder holder;
        private LayoutInflater mInflater = null;
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_exception)
                .error(R.drawable.ic_exception)
                .fallback(R.drawable.ic_exception);

        public ExpertGridAdapter(int groupPosition, List<GetProductTypeDetail.DataBean> detaildata) {
            this.detaildata = detaildata;
            this.mInflater = LayoutInflater.from(SearchShopActivity.this);
            this.groupPosition = groupPosition;
        }

        @Override
        public int getCount() {
            return detaildata.get(groupPosition).getTypelist2().size();
        }

        @Override
        public Object getItem(int position) {
            return detaildata.get(groupPosition).getTypelist2().get(position);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View arg1, ViewGroup parent) {
            holder = new ViewHolder();
            if (arg1 == null) {
                arg1 = mInflater.inflate(R.layout.grid_item_product_type_detail, null);
                holder.img_grid_item_product_pic = arg1
                        .findViewById(R.id.img_grid_item_product_pic);
                holder.tv_grid_item_product_name = arg1
                        .findViewById(R.id.tv_grid_item_product_name);
                arg1.setTag(holder);
            } else {
                holder = (ViewHolder) arg1.getTag();
            }


//            if (!TextUtils.isEmpty(detaildata.get(groupPosition).getTypelist2()
//                    .get(position).getUrl())) {
            Glide.with(SearchShopActivity.this).load(detaildata.get(groupPosition).getTypelist2()
                    .get(position).getUrl()+ "?x-oss-process=image/resize,w_"
                    + (Utils.getScreenWidth(SearchShopActivity.this)))
                    .apply(requestOptions)
                    .into(holder.img_grid_item_product_pic);
//            } else {
//                Picasso.with(SearchShopActivity.this).load(R.mipmap.icon_daishangjia_nor).into
//                        (holder.img_grid_item_product_pic);
//            }
            holder.tv_grid_item_product_name.setText(detaildata.get(groupPosition)
                    .getTypelist2().get(position).getPtdName());


            arg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(SearchShopActivity.this, SelfrunGoodsListActivity.class);
                    intent.putExtra("ptdId", detaildata.get(groupPosition).getTypelist2()
                            .get(position).getPtdId());
                    intent.putExtra("looseName", detaildata.get(groupPosition).getPtdName());
                    startActivity(intent);
                }
            });
            return arg1;
        }

    }
}
