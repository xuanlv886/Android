package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.dao.SelfMarketSearchDAO;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.view.NewsListView;
import app.cn.extra.mall.user.view.TagFlowLayout;
import app.cn.extra.mall.user.view.ViewHolder;
import app.cn.extra.mall.user.view.flowlayout.FlowLayout;
import app.cn.extra.mall.user.view.flowlayout.TagAdapter;
import app.cn.extra.mall.user.vo.GetHotSearch;
import app.cn.extra.mall.user.vo.SelfMarketSearch;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * 商品搜索页面
 */
public class SearchActivity extends BaseActivty {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tag_hots)
    TagFlowLayout tagHots;
    @BindView(R.id.tag_history)
    NewsListView tagHistory;
    private SelfMarketSearchDAO dao;
    private List<SelfMarketSearch> lists;
    private HistoryAdapater adapater;
    private static Pattern pattern = Pattern.compile("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]");
    private GetHotSearch.DataBean hotDatas;
    /**
     * 0--商品热搜,1--店铺热搜
     */
    String TYPE = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        dao = new SelfMarketSearchDAO(this);
        lists = dao.getAll();
        Collections.reverse(lists);
        if (lists.size() == 0) {
            tvEmpty.setVisibility(View.GONE);
        }
        InputFilter inputFilter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
                Matcher matcher = pattern.matcher(charSequence);
                if (!matcher.find()) {
                    return null;
                } else {
                    Toast.makeText(SearchActivity.this, "不能输入表情!", Toast.LENGTH_SHORT).show();
                    return "";
                }

            }
        };

        etSearch.setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(100)});
    }

    private void initData() {
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                //是否是回车键
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                    //隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //搜索
                    toOtherActivity();
                }
                return false;
            }
        });
        getHotSearch(TYPE);

    }

    /**
     * 热门搜索接口
     *
     * @param type 0--商品热搜,1--店铺热搜
     */
    private void getHotSearch(String type) {
        if (Utils.isNetworkAvailable(SearchActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getHotSearch)
                    .addParams("type", type)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            GetHotSearch hotSearch = new GetHotSearch();
                            hotSearch = Utils.parserJsonResult(response, GetHotSearch.class);
                            if ("true".equals(hotSearch.getFlag())) {
                                hotDatas = hotSearch.getData();
                                processData(hotDatas);

                            } else {
                                showLongToast(SearchActivity.this, hotSearch.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(SearchActivity.this, Constants.NETWORK_ERROR);

        }
    }

    private void processData(final GetHotSearch.DataBean hotDatas) {
        adapater = new HistoryAdapater();
        tagHistory.setAdapter(adapater);

        tagHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                etSearch.setText(lists.get(i).getNumber());
                toOtherActivity();
            }
        });
        final LayoutInflater mInflater = LayoutInflater.from(SearchActivity.this);
        if (hotDatas != null && hotDatas.getHotSearchList().size() > 0) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < hotDatas.getHotSearchList().size(); i++) {
                list.add(hotDatas.getHotSearchList().get(i).getPsName());
            }
            tagHots.setAdapter(new TagAdapter<String>(list) {

                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) mInflater.inflate(R.layout.item_textview,
                            tagHots, false);
                    tv.setText(s);
                    return tv;
                }
            });

            tagHots.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    etSearch.setText(list.get(position));
                    toOtherActivity();
                    //view.setVisibility(View.GONE);
                    return true;
                }
            });
        }
    }

    private void toOtherActivity() {
        Intent intent = new Intent();
        String shopName = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(shopName)) {
            Toast.makeText(SearchActivity.this, "商品信息不能为空！", Toast.LENGTH_SHORT).show();
            return;
        } else {
            boolean hasisData = dao.hasisData(shopName);
            if (!hasisData) {
                //将shopName封装在record
                SelfMarketSearch record = new SelfMarketSearch(-1, shopName);
                dao.add(record);
                lists = dao.getAll();
                Collections.reverse(lists);
                adapater.notifyDataSetChanged();
                etSearch.setText("");
                textView2.setVisibility(View.VISIBLE);
                tvEmpty.setVisibility(View.VISIBLE);
            }
            intent.putExtra("shopName", shopName);
            intent.setClass(SearchActivity.this, SearchGoodActivity.class);
            startActivity(intent);
        }
    }

    @OnClick({R.id.img_back, R.id.tv_search, R.id.tv_empty})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            //搜索
            case R.id.tv_search:
                toOtherActivity();
                break;
            case R.id.tv_empty:
                //清空历史
                deleteData();
                break;
            default:
                break;
        }
    }

    private void deleteData() {
        dao.deleteadd();
        lists.clear();
        adapater.notifyDataSetChanged();
        textView2.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.GONE);
    }

    private class HistoryAdapater extends BaseAdapter {
        private final LayoutInflater mInflater;
        private ViewHolder holder;

        public HistoryAdapater() {
            this.mInflater = LayoutInflater.from(SearchActivity.this);
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int i) {
            return lists.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            holder = new ViewHolder();
            if (view == null) {
                view = mInflater.inflate(R.layout.list_item_self_history, null);
                holder.list_item_history = (TextView) view.findViewById(R.id.list_item_history);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.list_item_history.setText(lists.get(i).getNumber());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etSearch.setText(lists.get(i).getNumber());
                    toOtherActivity();
                }
            });
            return view;
        }
    }
}
