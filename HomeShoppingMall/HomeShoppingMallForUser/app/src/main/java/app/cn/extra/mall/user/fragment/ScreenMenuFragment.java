package app.cn.extra.mall.user.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.activity.SelfrunGoodsListActivity;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.PhoneUtil;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.utils.Util;
import app.cn.extra.mall.user.view.FlowTagView;
import app.cn.extra.mall.user.view.NewsExpandableListView;
import app.cn.extra.mall.user.view.NewsGridView;
import app.cn.extra.mall.user.view.ViewHolder;
import app.cn.extra.mall.user.vo.DogetProductBasicProperty;
import app.cn.extra.mall.user.vo.IndexTag;
import app.cn.extra.mall.user.vo.MapsTag;
import app.cn.extra.mall.user.vo.ParameterData;
import app.cn.extra.mall.user.vo.Ppid;
import cfkj.app.cn.cfkjcommonlib.common.BaseFragment;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

// 行业类别二级界面--筛选
public class ScreenMenuFragment extends BaseFragment implements OnClickListener {

    private View mView;
    private TextView tv_reset, tv_complete;
    private NewsExpandableListView elv_common;
    private EditText et_low_price, et_high_price;
    private ExpandableAdapter adapter;
    private NormalGridAdapter normalGridAdapter;
    private String ptdId;
    ProgressBar progressBar;
    private LinearLayout ll_price;
    private RelativeLayout rl_support, rl_support_hide, rl_price, rl_button;
    private SupportAdapter adapter_support;
    private ImageView img_support, img_supports;
    private NewsGridView gv_support;
    // 将所有的选中的结果放入集合中
    private List<String> sale = new ArrayList<String>();
    private HashMap<Integer, Integer> map;
    private HashMap<Integer, Integer> ordMap;
    private List<HashMap<Integer, Integer>> tags = new ArrayList<HashMap<Integer, Integer>>();
    private String sortType;
    private SharePreferenceUtil sharePreferenceUtil;
    private SlidingMenu menu;
    private CheckBox add_cb_bargain;
    private int isChange = 0;
    private List<MapsTag> mapsTags;
    private int tagss;
    private Ppid param;
    private String brand;
    private boolean mTags = false;

    private String parameterData;
    private ParameterData Pdatas;
    private DogetProductBasicProperty.DataBean datas;
    private ArrayList<Object> ppid;
    private String Tags;
    private IndexTag indexTag;


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        if (mView == null) {
            initView(inflater, container);
        }
        if (getArguments() != null) {
            ptdId = getArguments().getString("ptdId");
            sortType = getArguments().getString("sortType");
            parameterData = getArguments().getString("parameterData");
            tagss = getArguments().getInt("tags");
            param = (Ppid) getArguments().getSerializable("list");
            Tags = getArguments().getString("Tags");
            brand = getArguments().getString("brand");
            Pdatas = new ParameterData();
            Pdatas = Utils.parserJsonResult(parameterData, ParameterData.class);
            indexTag = new IndexTag();
            indexTag = Utils.parserJsonResult(Tags, IndexTag.class);
        }
        ppid = new ArrayList<>();
        initData();
        return mView;
    }

    private void initData() {
        dogetProductBasicPropertyPort();
        if (!TextUtils.isEmpty(Tags)) {
            for (int i = 0; i < indexTag.getList().size(); i++) {
                ordMap = new HashMap<>();
                ordMap.put(indexTag.getList().get(i).getValue(), indexTag.getList().get(i).getKey());
                tags.add(ordMap);

            }

        }
        if (!TextUtils.isEmpty(parameterData)) {
            et_low_price.setText(Pdatas.getLowPrice());
            et_high_price.setText(Pdatas.getHighPrice());
            sale.addAll(Pdatas.getService());
        }

    }

    /**
     * 获取商品筛选接口
     */
    private void dogetProductBasicPropertyPort() {
        if (Utils.isNetworkAvailable(getContext())) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getProductProperty)
                    .addParams("ptdId", ptdId)
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
                            Util.logE("showProductBasicProperty--------", response);
                            DogetProductBasicProperty basicProperty = new DogetProductBasicProperty();
                            basicProperty = Utils.parserJsonResult(response, DogetProductBasicProperty.class);
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            if ("true".equals(basicProperty.getFlag())) {
                                datas = basicProperty.getData();
                                processData(datas);
                            } else {
                                CustomToast.showToast(getActivity(),
                                        basicProperty.getErrorString(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            CustomToast.showToast(getActivity(),
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);
        }
    }

    private void processData(DogetProductBasicProperty.DataBean datas) {
        adapter = new ExpandableAdapter(datas);
        elv_common.setAdapter(adapter);
        Util.setExpandableListViewHeight(elv_common);
        // 售后保障信息
        adapter_support = new SupportAdapter(datas);
        gv_support.setAdapter(adapter_support);
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        sharePreferenceUtil = new SharePreferenceUtil(SelfrunGoodsListActivity.context, Constants.SAVE_USER);
        mView = inflater.inflate(R.layout.rightmenu, container, false);
        add_cb_bargain = mView.findViewById(R.id.add_cb_bargain);
        add_cb_bargain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    isChange = 1;
                } else {
                    isChange = 0;
                }
            }
        });
        progressBar = mView.findViewById(R.id.progressBar);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        rl_button = mView.findViewById(R.id.rl_button);
        /**
         * 设置底部距离
         */
        if (PhoneUtil.checkDeviceHasNavigationBar(getActivity())) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rl_button.getLayoutParams();
            lp.setMargins(0, 0, 0, PhoneUtil.getNavigationBarHeight(getActivity()));
            rl_button.setLayoutParams(lp);
        }
        tv_reset = mView.findViewById(R.id.tv_reset);
        tv_complete = mView.findViewById(R.id.tv_complete);
        tv_complete.setBackgroundColor(getResources().getColor(R.color.blue));
        et_high_price = mView.findViewById(R.id.et_high_price);
        img_supports = mView.findViewById(R.id.img_supports);
        ll_price = mView.findViewById(R.id.ll_price);
        rl_price = mView.findViewById(R.id.rl_price);
        rl_price.setOnClickListener(this);
        elv_common = mView.findViewById(R.id.elv_common);
        elv_common.setGroupIndicator(null);  // 去掉箭头
        rl_support_hide = mView.findViewById(R.id.rl_support_hide);
        rl_support = mView.findViewById(R.id.rl_support);
        img_support = mView.findViewById(R.id.img_support);
        gv_support = mView.findViewById(R.id.gv_support);
        et_low_price = mView.findViewById(R.id.et_low_price);
        rl_support.setOnClickListener(this);
        tv_reset.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
        elv_common.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Util.setExpandableListViewHeight(elv_common);
                for (int i = 0, count = elv_common
                        .getExpandableListAdapter().getGroupCount(); i < count; i++) {
                    // 关闭其他分组
                    if (groupPosition != i) {
                        elv_common.collapseGroup(i);
                    }
                }
                rl_support_hide.setVisibility(View.GONE);
                img_support.setImageResource(R.mipmap.icon_fenleixiala_nor);

            }
        });
        elv_common.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Util.setExpandableListViewHeight(elv_common);
            }
        });
    }

    // 售后保障信息适配器
    private class SupportAdapter extends BaseAdapter {
        private final DogetProductBasicProperty.DataBean datas;
        private ViewHolder holder;
        private LayoutInflater mInflater = null;

        public SupportAdapter(DogetProductBasicProperty.DataBean datas) {
            this.mInflater = LayoutInflater.from(getContext());
            this.datas = datas;
        }

        @Override
        public int getCount() {
            if (datas.getPropertysList2() != null) {
                return datas.getPropertysList2().size();
            } else if (datas.getPropertysList1() != null) {
                return datas.getPropertysList1().size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return datas.getPropertysList2().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View arg1, ViewGroup parent) {
            holder = new ViewHolder();
            if (arg1 == null) {
                arg1 = mInflater.inflate(R.layout.grid_item_relase_goods_fill_support, null);

                holder.ck_item_support = (CheckBox) arg1
                        .findViewById(R.id.ck_item_support);

                arg1.setTag(holder);
            } else {
                holder = (ViewHolder) arg1.getTag();
            }
            holder.ck_item_support.setText(datas.getPropertysList1().get(position).getName());

            holder.ck_item_support.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if (isChecked) {
                        if (!sale.contains(datas.getPropertysList2().get(position).getName())) {
                            sale.add(datas.getPropertysList2().get(position).getName());
                        }
                    } else {
                        for (int i = 0; i < sale.size(); i++) {
                            if (sale.get(i).equals(datas.getPropertysList2().get(position).getName())) {
                                sale.remove(i);
                            }
                        }

                    }

                }
            });

            if (sale.contains(datas.getPropertysList2().get(position).getName())) {
                holder.ck_item_support.setChecked(true);
            } else {
                holder.ck_item_support.setChecked(false);
            }

            return arg1;

        }

    }

    private class ExpandableAdapter extends BaseExpandableListAdapter {

        private final DogetProductBasicProperty.DataBean datas;
        private ViewHolder holder;
        private LayoutInflater mInflater = null;


        public ExpandableAdapter(DogetProductBasicProperty.DataBean datas) {
            this.mInflater = LayoutInflater.from(getActivity());
            this.datas = datas;
        }

        @Override
        public int getGroupCount() {
            if (datas.getPropertysList2() != null) {
                return datas.getPropertysList2().size();
            } else if (datas.getPropertysList1() != null) {
                return datas.getPropertysList1().size();
            } else {
                return 0;
            }
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return datas.getPropertysList2().get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
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
            holder = new ViewHolder();
            if (view == null) {
                view = mInflater.inflate(R.layout.industry_inside_common_group, null);
                holder.tv_industry_inside_common_group_title = (TextView) view.findViewById(
                        R.id.tv_industry_inside_common_group_title);
                holder.img_industry_inside_common_group_more = (ImageView) view.findViewById(
                        R.id.img_industry_inside_common_group_more);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            if (datas.getPropertysList2() != null) {
                if (!TextUtils.isEmpty(datas.getPropertysList2().get(groupPosition).getName())) {
                    holder.tv_industry_inside_common_group_title.setText(
                            datas.getPropertysList2().get(groupPosition).getName());
                } else {
                    holder.tv_industry_inside_common_group_title.setText("");
                }
            } else if (datas.getPropertysList1() != null) {
                if (!TextUtils.isEmpty(datas.getPropertysList1().get(groupPosition).getName())) {
                    holder.tv_industry_inside_common_group_title.setText(
                            datas.getPropertysList1().get(groupPosition).getName());
                } else {
                    holder.tv_industry_inside_common_group_title.setText("");
                }
            }
            return view;
        }


        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            ViewGroup item = null;
            item = (ViewGroup) mInflater.inflate(
                    R.layout.industry_inside_common_child, null); //载入gridview布局
            FlowTagView grid = item.findViewById(R.id.gv_child_inside);// 获取girdview的节点
            normalGridAdapter = new NormalGridAdapter(groupPosition);
            grid.setAdapter(normalGridAdapter);

            return item;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    // normal 标签GridView
    private class NormalGridAdapter extends BaseAdapter {

        private final int groupPosition;
        private ViewHolder holder;
        private LayoutInflater mInflater = null;

        public NormalGridAdapter(int groupPosition) {
            this.mInflater = LayoutInflater.from(getActivity());
            this.groupPosition = groupPosition;
        }

        @Override
        public int getCount() {
            if (datas.getPropertysList2() != null) {
                return datas.getPropertysList2().get(groupPosition).getValues().size();
            } else if (datas.getPropertysList1() != null) {
                return datas.getPropertysList1().get(groupPosition).getValues().size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            if (datas.getPropertysList2() != null) {
                return datas.getPropertysList2().get(groupPosition).getValues().get(position);
            } else if (datas.getPropertysList1() != null) {
                return datas.getPropertysList1().get(groupPosition).getValues().get(position);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View arg1, ViewGroup parent) {
            holder = new ViewHolder();
            if (arg1 == null) {
                arg1 = mInflater.inflate(R.layout.grid_item_industry_inside_common, null);

                holder.tv_grid_item_tag = arg1
                        .findViewById(R.id.tv_grid_item_tag);

                arg1.setTag(holder);
            } else {
                holder = (ViewHolder) arg1.getTag();
            }
            if (datas.getPropertysList2() != null) {
                if (!TextUtils.isEmpty(datas.getPropertysList2().get(groupPosition).getValues().get(position).getValue())) {
                    holder.tv_grid_item_tag.setText(datas.getPropertysList2().get(groupPosition).getValues().get(position).getValue());
                } else {
                    holder.tv_grid_item_tag.setText("");
                }
            } else if (datas.getPropertysList1() != null) {
                if (!TextUtils.isEmpty(datas.getPropertysList1().get(groupPosition).getValues().get(position).getValue())) {
                    holder.tv_grid_item_tag.setText(datas.getPropertysList1().get(groupPosition).getValues().get(position).getValue());
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

            ItemListener itemListener = new ItemListener(position, datas);
            holder.tv_grid_item_tag.setOnClickListener(itemListener);

            return arg1;
        }

        class ItemListener implements OnClickListener {
            private final DogetProductBasicProperty.DataBean datas;
            private int m_position;


            public ItemListener(int position, DogetProductBasicProperty.DataBean datas) {
                this.m_position = position;
                this.datas = datas;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tv_grid_item_tag:
                        map = new HashMap<Integer, Integer>(1);
//                        int index = datas.getPropertysList2().get(groupPosition).getChooseState();
                        int index = 0;
                        if (0 == tags.size()) { // 当前集合为null
                            map.put(groupPosition, m_position);
                            tags.add(0, map);
                            mTags = false;
                            v.setBackgroundDrawable(
                                    getResources().getDrawable(R.drawable.checked_bg));
                            tags.size();
                        } else { // 集合中已有数据
                            // 确定点击的标签是否被选中过
                            if (0 == index) { // 单选
                                for (int i = 0; i < tags.size(); i++) {
                                    if (tags.get(i).containsKey(groupPosition)) {
                                        if (tags.get(i).containsValue(m_position)) {
                                            v.setBackgroundDrawable(
                                                    getResources().getDrawable(R.drawable.normal_bg));

                                            tags.remove(i);
                                            mTags = true;
                                            break;
                                        }
                                        tags.remove(i);
                                    }
                                }
                                if (mTags != true) {
                                    map.put(groupPosition, m_position);
                                    tags.add(tags.size(), map);

                                }
                                mTags = false;

                            } else { // 多选 集合中已有数据
                                for (int i = 0; i < tags.size(); i++) {
                                    // 确定点击的标签是否被选中过
                                    if (tags.get(i).containsKey(groupPosition)
                                            && tags.get(i).containsValue(m_position)) { // 被选中过
                                        v.setBackgroundDrawable(
                                                getResources().getDrawable(R.drawable.normal_bg));
                                        tags.remove(i);
                                        mTags = true;
                                        break;
                                    }

                                }
                                if (mTags != true) {
                                    v.setBackgroundDrawable(
                                            getResources().getDrawable(R.drawable.checked_bg));

                                    map.put(groupPosition, m_position);
                                    tags.add(tags.size(), map);
                                    mTags = false;
                                }
                                mTags = false;
                                adapter.notifyDataSetChanged();
                                break;
                            }
                            adapter.notifyDataSetChanged();
                        }
                        adapter.notifyDataSetChanged();
                    default:
                        break;

                }

            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.tv_reset: // 重置
                et_low_price.setText("");
                et_high_price.setText("");
                tags.clear();
                try {
                    adapter.notifyDataSetChanged();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                sale.clear();
                try {
                    adapter_support.notifyDataSetChanged();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                ppid.clear();
                CustomToast.showToast(getContext(), "已重置", Toast.LENGTH_SHORT);

                break;
            case R.id.tv_complete: // 完成
                DoScreenPort();

                break;
            case R.id.rl_support:
                if (rl_support_hide.getVisibility() == View.GONE) {
                    rl_support_hide.setVisibility(View.VISIBLE);
                    img_support.setImageResource(R.mipmap.icon_fenleixiala_sel);
                } else {
                    rl_support_hide.setVisibility(View.GONE);
                    img_support.setImageResource(R.mipmap.icon_fenleixiala_nor);
                }
                for (int i = 0, count = elv_common
                        .getExpandableListAdapter().getGroupCount(); i < count; i++) {
                    elv_common.collapseGroup(i);
                }

                break;
            case R.id.rl_price:
                if (ll_price.getVisibility() == View.GONE) {
                    ll_price.setVisibility(View.VISIBLE);
                    img_supports.setImageResource(R.mipmap.icon_fenleixiala_sel);
                } else {
                    ll_price.setVisibility(View.GONE);
                    img_supports.setImageResource(R.mipmap.icon_fenleixiala_nor);
                }
                for (int i = 0, count = elv_common
                        .getExpandableListAdapter().getGroupCount(); i < count; i++) {
                    elv_common.collapseGroup(i);
                }

                break;

            default:
                break;
        }
    }

    private void DoScreenPort() {

        for (HashMap<Integer, Integer> hm : tags) {
            Set<Integer> set = hm.keySet();
            for (Integer key : set) {
                Integer Stringvalue = hm.get(key);
                String ppId = "";
                if (datas.getPropertysList2() != null) {
                    ppId = datas.getPropertysList2().get(key).getValues().get(Stringvalue).getPpId();
                } else if (datas.getPropertysList1() != null) {
                    ppId = datas.getPropertysList1().get(key).getValues().get(Stringvalue).getPpId();
                }
                ppid.add(ppId);
            }
        }
        if (tagss == 1) {
            if (param.getData().size() != 0) {
                for (int i = 0; i < param.getData().size(); i++) {
                    ppid.add(param.getData().get(i));
                }
            }

        }
        Gson gson = new Gson();
        String toJson = gson.toJson(ppid);
        String services = gson.toJson(sale);
        System.out.println(services);
//        //服务信息
//        String str = "";
//        for (String i : sale) {
//            str += i + ",";
//        }
//        if (str.length() > 0) {
//            str = str.substring(0, str.length() - 1);
//            System.out.println(str);
//        }

        String low = et_low_price.getText().toString();
        String high = et_high_price.getText().toString();
        if (!TextUtils.isEmpty(low)) {
            if (!TextUtils.isEmpty(high)) {
                double lowPrices = Double.parseDouble(low);
                double highPrices = Double.parseDouble(high);
                if (lowPrices > highPrices) {
                    CustomToast.showToast(getContext(), "最高价不能低于最低价", Toast.LENGTH_SHORT);
                    return;
                }
            } else {
                CustomToast.showToast(getContext(), "请输入最高价", Toast.LENGTH_SHORT);
                return;
            }
        }

        String lows = "\"" + low + "\"";
        String highs = "\"" + high + "\"";
//        String strs = "\"" + str + "\"";
        String brandNames = "\"" + brand + "\"";
        String change = "\"" + isChange + "\"";
        String service = "service";
        String lowPrice = "lowPrice";
        String highPrice = "highPrice";
        String properties = "properties";
        String brandName = "brandName";
        String parameterData = "{" + "\"" + service + "\"" + ":" + services + "," + "\"" + lowPrice + "\"" + ":" + lows + "," + "\"" + highPrice + "\"" + ":"
                + highs + "," + "\"" + properties + "\"" + ":" + toJson + "}";

        SelfrunGoodsListActivity selfrunGoodsList = (SelfrunGoodsListActivity) getContext();
        selfrunGoodsList.getSlidingMenu().toggle();
        selfrunGoodsList.setParameterData(parameterData);
        mapsTags = new ArrayList<>();
        for (HashMap<Integer, Integer> hm : tags) {
            Set<Integer> set = hm.keySet();
            for (Integer key : set) {
                Integer Stringvalue = hm.get(key);
                MapsTag mapsTag = new MapsTag();
                mapsTag.setKey(Stringvalue);
                mapsTag.setValue(key);
                mapsTags.add(mapsTag);
            }
        }
        HashMap<String, List> map = new HashMap();
        map.put("list", mapsTags);
        String tagss = gson.toJson(map);
        selfrunGoodsList.setTags(tagss);
    }


}

