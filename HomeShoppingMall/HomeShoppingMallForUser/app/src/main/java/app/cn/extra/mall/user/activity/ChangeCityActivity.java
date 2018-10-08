package app.cn.extra.mall.user.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.github.ybq.android.spinkit.style.Wave;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.adapter.HotCityAdapter;
import app.cn.extra.mall.user.adapter.SorAdapter;
import app.cn.extra.mall.user.event.IndexFragmentEvent;
import app.cn.extra.mall.user.utils.CharacterParser;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.PinyinComparator;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.view.SideBar;
import app.cn.extra.mall.user.vo.GetAreaCode;
import app.cn.extra.mall.user.vo.GetCityList;
import app.cn.extra.mall.user.vo.GetSearchCityList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import cfkj.app.cn.cfkjcommonlib.view.ReplaceViewHelper.VaryViewHelperController;
import okhttp3.Call;

/**
 * 切换城市
 */
public class ChangeCityActivity extends BaseActivty implements AMapLocationListener {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.rl_location)
    RelativeLayout rlLocation;
    @BindView(R.id.rl_choose)
    RelativeLayout rlChoose;
    @BindView(R.id.tv_choose)
    TextView tvChoose;
    @BindView(R.id.linear_location_failed)
    LinearLayout linearLocationFailed;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    @BindView(R.id.country_lvcountry)
    RecyclerView countryLvcountry;
    @BindView(R.id.sidrbar)
    SideBar sidrbar;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    /**
     * 接口请下来的数据
     */
    private List<GetCityList.DataBean.OpenCityListBean> SourceDateList;
    /**
     * 有城市名首字母的list
     */
    private List<GetCityList.DataBean.OpenCityListBean> cityNameList;
    private List<GetCityList.DataBean.OpenCityListBean> hotlist = null;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    /**
     * 布局管理器
     */
    LinearLayoutManager layoutManage;
    /**
     * 城市列表适配器
     */
    private SorAdapter adapter;
    /**
     * 热门城市适配器
     */
    private HotCityAdapter hotCityAdapter;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private VaryViewHelperController varyViewHelperController;
    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 声明mlocationClient对象
     */
    public AMapLocationClient mlocationClient;
    /**
     * 声明mLocationOption对象
     */
    public AMapLocationClientOption mLocationOption = null;
    String intentTag = "";
    /**
     * 定位
     */
    int LOCATION = 0;
    /**
     * 选择
     */
    int CHECK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);
        ButterKnife.bind(this);
        sharePreferenceUtil = new SharePreferenceUtil(ChangeCityActivity.this,
                Constants.SAVE_USER);
        initViews();
    }

    /**
     * 根据定位数据显示不同信息
     */
    public void locationView(String cityName) {
        if (!TextUtils.isEmpty(cityName)) {
            tvLocation.setText("当前定位城市：" + cityName);
            tvLocation.setVisibility(View.VISIBLE);
            linearLocationFailed.setVisibility(View.GONE);
        } else {
            tvLocation.setVisibility(View.GONE);
            linearLocationFailed.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 根据选择城市显示不同信息
     *
     * @param cityName
     */
    private void chooseView(String cityName) {
        if (!TextUtils.isEmpty(cityName)) {
            tvChoose.setText("当前选择城市：" + cityName);
        }
    }

    private void initViews() {
        Intent intent = getIntent();
        intentTag = intent.getStringExtra("intentTag");
        locationView("定位中...");
        if (!TextUtils.isEmpty(sharePreferenceUtil.getCheckCityName())) {
            chooseView(sharePreferenceUtil.getCheckCityName());
        }
        /**
         * 设置异常情况界面
         */
        varyViewHelperController = createCaseViewHelperController(refreshLayout);
        varyViewHelperController.setUpRefreshViews(varyViewHelperController.getErrorView(),
                varyViewHelperController.getNetworkPoorView(), varyViewHelperController.getEmptyView());
        initRefreshLayout();
        initRecyclerView();
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sidrbar = (SideBar) findViewById(R.id.sidrbar);

        //设置右侧触摸监听
        sidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                if ("热".equals(s)) {
                    layoutManage.scrollToPosition(0);
                } else {
                    //该字母首次出现的位置
                    int position = adapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        layoutManage.scrollToPosition(position);
                    }
                }

            }
        });
//        countryLvcountry.setAdapter(adapter);
        SourceDateList = new ArrayList<GetCityList.DataBean.OpenCityListBean>();
        cityNameList = new ArrayList<GetCityList.DataBean.OpenCityListBean>();
        getCityList();
        initLocationClient();
    }

    /**
     * 初始化定位相关设置
     */
    private void initLocationClient() {
        mlocationClient = new AMapLocationClient(getApplicationContext());
        /**
         * 初始化定位参数
         */
        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(this);
        /**
         * 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
         */
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        /**
         * 获取一次定位结果：
         * 该方法默认为false。
         */
        mLocationOption.setOnceLocation(true);
        /**
         * 获取最近3s内精度最高的一次定位结果：
         * 设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度
         * 最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，
         * 反之不会，默认为false。
         */
        mLocationOption.setOnceLocationLatest(true);
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();
    }

    HotCityAdapter.OnItemClickListener onHotItemClickListener = new HotCityAdapter.OnItemClickListener() {
        @Override
        public void onClick(int position) {
            showRemoveDialog(hotlist.get(position).getAcCode(), hotlist.get(position).getAcCity(), CHECK);
        }

        @Override
        public void onLongClick(int position) {

        }
    };
    SorAdapter.OnItemClickListener onItemClickListener = new SorAdapter.OnItemClickListener() {
        @Override
        public void onClick(int position) {
            showRemoveDialog(cityNameList.get(position).getAcCode(), cityNameList.get(position).getAcCity(), CHECK);
        }

        @Override
        public void onLongClick(int position) {

        }
    };

    @OnClick({R.id.img_back, R.id.tv_sure, R.id.linear_location_failed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_sure:
                String cityName = etSearch.getText().toString().trim();
                if (TextUtils.isEmpty(cityName)) {
                    getCityList();
                    break;
                }
//                if (cityNameList != null && cityNameList.size() > 0) {
//                    if (!TextUtils.isEmpty(cityName)) {
                getSearchCityList(cityName);
//                    } else {
//                        getCityList();
//                    }
//                } else {
//                    getCityList();
//                }
                break;
            case R.id.linear_location_failed:
                if (mlocationClient != null) {
                    mlocationClient.startLocation();
                } else {
                    initLocationClient();
                }
                break;
            default:
                break;
        }
    }

    private void getSearchCityList(String keyWords) {
        if (Utils.isNetworkAvailable(ChangeCityActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getSearchCityList)
                    .addParams("keyWords", keyWords)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (cityNameList != null) {
                                cityNameList.clear();
                            }
                            if (SourceDateList != null) {
                                SourceDateList.clear();
                            }
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 恢复显示数据的View
                             */
                            varyViewHelperController.restore();
                            GetSearchCityList getCityList = new GetSearchCityList();
                            getCityList = Utils.parserJsonResult(response,
                                    GetSearchCityList.class);
                            if (Constants.OK.equals(getCityList.getFlag())) {
                                if (null != getCityList.getData()) {
                                    GetCityList.DataBean.OpenCityListBean openCityList = new GetCityList.DataBean.OpenCityListBean();
                                    if (getCityList.getData().getAcCity() != null && getCityList.getData().getAcId() != null && getCityList.getData().getAcCode() != null) {
//                                        getAreaCodePort(getCityList.getData().getAcCode(), getCityList.getData().getAcCity(), CHECK);
                                        openCityList.setAcCity(getCityList.getData().getAcCity());
                                        openCityList.setAcId(getCityList.getData().getAcId());
                                        openCityList.setAcCode(getCityList.getData().getAcCode());
                                        SourceDateList.add(openCityList);
                                        cityNameList = filledData(SourceDateList);
                                        // 根据a-z进行排序源数据
                                        Collections.sort(cityNameList, pinyinComparator);
                                        GetCityList.DataBean.OpenCityListBean openCityListBean = new GetCityList.DataBean.OpenCityListBean();
                                        cityNameList.add(0, openCityListBean);
//                                    hotCityAdapter = new HotCityAdapter(ChangeCityActivity.this, cityNameList);
//                                        adapter = new SorAdapter(ChangeCityActivity.this, cityNameList, hotCityAdapter, onHotItemClickListener);
//                                        countryLvcountry.setAdapter(adapter);
                                    }
                                    /**
                                     * 城市列表点击事件
                                     */
                                    adapter.setOnItemClickListener(onItemClickListener);
                                    adapter.notifyDataSetChanged();
                                }

//                                setRecyclerViewHeaderAndFooter(getMainInterface.getData());
                            } else {
                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
            varyViewHelperController.showNetworkPoorView();
//            Toast.makeText(getApplicationContext(), "城市切换失败！" + Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取城市列表接口
     * p--当前页
     * ps--页大小
     */
    private void getCityList() {
        if (Utils.isNetworkAvailable(ChangeCityActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getCityList)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 恢复显示数据的View
                             */
                            varyViewHelperController.restore();
                            GetCityList getCityList = new GetCityList();
                            getCityList = Utils.parserJsonResult(response,
                                    GetCityList.class);
                            if (Constants.OK.equals(getCityList.getFlag())) {
                                if (null != getCityList.getData().getOpenCityList() && 0 < getCityList.getData().getOpenCityList().size()) {
                                    if (cityNameList != null) {
                                        cityNameList.clear();
                                    }
                                    if (SourceDateList != null) {
                                        SourceDateList.clear();
                                    }
                                    for (int i = 0; i < getCityList.getData().getOpenCityList().size(); i++) {
                                        SourceDateList.add(getCityList.getData().getOpenCityList().get(i));
                                    }
                                    cityNameList = filledData(SourceDateList);
                                    // 根据a-z进行排序源数据
                                    Collections.sort(cityNameList, pinyinComparator);
                                    GetCityList.DataBean.OpenCityListBean openCityListBean = new GetCityList.DataBean.OpenCityListBean();
                                    cityNameList.add(0, openCityListBean);
                                    hotlist = new ArrayList<GetCityList.DataBean.OpenCityListBean>();
                                    for (int i = 0; i < cityNameList.size(); i++) {
                                        if (cityNameList.get(i).getOcIsHot() == 1) {
                                            hotlist.add(cityNameList.get(i));
                                        }
                                    }
                                    hotCityAdapter = new HotCityAdapter(ChangeCityActivity.this, cityNameList);
                                    adapter = new SorAdapter(ChangeCityActivity.this, cityNameList, hotCityAdapter, onHotItemClickListener);
                                    countryLvcountry.setAdapter(adapter);
                                    /**
                                     * 城市列表点击事件
                                     */
                                    adapter.setOnItemClickListener(onItemClickListener);
                                }

//                                setRecyclerViewHeaderAndFooter(getMainInterface.getData());
                            } else {
                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
            varyViewHelperController.showNetworkPoorView();
        }
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 线性布局
         */
        layoutManage = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        countryLvcountry.setLayoutManager(layoutManage);
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

    /**
     * 为List添加拼音首字母
     *
     * @param date 请求到的列表数据
     * @return
     */
    private List<GetCityList.DataBean.OpenCityListBean> filledData(List<GetCityList.DataBean.OpenCityListBean> date) {
        for (int i = 0; i < date.size(); i++) {
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date.get(i).getAcCity());
            if (pinyin.contains("zhongqingshi")) {
                pinyin = "chongqingshi";
            }
            if (pinyin.contains("zhangshashi")) {
                pinyin = "changshashi";
            }
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                date.get(i).setAcSortLetters(sortString.toUpperCase());
            } else {
                date.get(i).setAcSortLetters("#");
            }

        }
        return date;

    }

    /**
     * 根据输入框中的值来过滤数据并更新列表
     *
     * @param filterStr 城市名
     */
    private void filterData(String filterStr) {
        List<GetCityList.DataBean.OpenCityListBean> filterDateList = new ArrayList<GetCityList.DataBean.OpenCityListBean>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = cityNameList;
        } else {
            filterDateList.clear();
            for (GetCityList.DataBean.OpenCityListBean getCityList : cityNameList) {
                String name = getCityList.getAcCity();
                if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                    filterDateList.add(getCityList);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                getAreaCodePort(aMapLocation.getAdCode(), aMapLocation.getCity(), LOCATION,"");
            } else {
                /**
                 * 打印定位失败原因
                 */
                Utils.LogE("AmapError:" + "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 获取行政区划信息接口
     * tag 0定位 1选择
     */
    private void getAreaCodePort(String acCode, String acName, int tag,String intentTag) {
        if (null != progressBar) {
            progressBar.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(acCode)) {
            return;
        }
        if (Utils.isNetworkAvailable(ChangeCityActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getAreaCode)
                    .addParams("acCode", acCode)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            GetAreaCode getAreaCode = new GetAreaCode();
                            getAreaCode = Utils.parserJsonResult(response, GetAreaCode.class);
                            if (Constants.OK.equals(getAreaCode.getFlag())) {
                                if (Constants.OK.equals(getAreaCode.getData().getStatus())) {
                                    if (tag == 0) {
                                        sharePreferenceUtil.setAcId(getAreaCode.getData()
                                                .getAcId());
                                        sharePreferenceUtil.setCityName(acName);
                                        locationView(acName);
                                        if (TextUtils.isEmpty(sharePreferenceUtil.getCheckCityName())) {
                                            chooseView(acName);
                                        } else {
                                            chooseView(sharePreferenceUtil.getCheckCityName());
                                        }
                                    } else if (tag == 1) {
                                        sharePreferenceUtil.setCheckAcId(getAreaCode.getData()
                                                .getAcId());
                                        sharePreferenceUtil.setCheckCityName(acName);
                                        chooseView(acName);
//                                        showShortToast(ChangeCityActivity.this, "城市切换成功！");
                                        EventBus.getDefault().post(new IndexFragmentEvent(acName));
                                        finish();
                                    }

//                                    if ("1".equals(intentTag)) {
//                                        EventBus.getDefault().post(new AddAddressEvent(acName));
//                                    } else if ("2".equals(intentTag)) {
//                                        EventBus.getDefault().post(new EditAddressEvent(acName));
//                                    } else if ("3".equals(intentTag)) {
//                                        EventBus.getDefault().post(new IndexFragmentEvent(acName));
//                                    }
                                }
                            } else {
                                showShortToast(ChangeCityActivity.this, getAreaCode.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(ChangeCityActivity.this, "城市切换失败！" + Constants.NETWORK_ERROR);
        }
    }

    /**
     * 是否切换城市弹窗
     */
    private void showRemoveDialog(String acCode, String acName, int tag) {
        LayoutInflater layoutInflater = LayoutInflater.from(ChangeCityActivity.this);
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(ChangeCityActivity.this, R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        TextView tv_live_content = view.findViewById(R.id.tv_live_content);
        tv_live_content.setText("您确定切换到" + acName + "吗？");

        tv__live_never_reminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tv_live_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //删除信息
                getAreaCodePort(acCode, acName, tag,intentTag);
                alertDialog.dismiss();
            }
        });

    }
//    @Override
//    public void onRefresh(SwipyRefreshLayoutDirection direction) {
//        if (direction == SwipyRefreshLayoutDirection.TOP) {
////            currentPage = 1;
////            loadMore = false;
////            /**
////             * 下拉刷新首页数据
////             */
////            getMainInterfacePort();
//            refreshLayout.setRefreshing(false);
//        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
////            currentPage += 1;
////            loadMore = true;
////            /**
////             * 上拉加载首页数据
////             */
////            getMainInterfacePort();
//            refreshLayout.setRefreshing(false);
//        }
//    }
}
