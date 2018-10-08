package app.cn.extra.mall.merchant.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.Wave;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.CityEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.vo.GetCityList;
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
 * Description 选择商户所在城市页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class SelectCityActivity extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private VaryViewHelperController varyViewHelperController;
    private List<GetCityList.DataBean.OpenCityListBean> cityList;
    private CommonAdapter<GetCityList.DataBean.OpenCityListBean> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        /**
         * 设置异常情况界面
         */
        varyViewHelperController = createCaseViewHelperController(recyclerView);
        varyViewHelperController.setUpRefreshViews(varyViewHelperController.getErrorView(),
                varyViewHelperController.getNetworkPoorView(),varyViewHelperController.getEmptyView());
        initRecyclerView();
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        getCityListPort();
    }

    /**
     * 获取已开通的城市列表接口
     */
    private void getCityListPort() {
        if (Utils.isNetworkAvailable(SelectCityActivity.this)) {
            if (progressBar != null) {
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
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 恢复显示数据的View
                             */
                            varyViewHelperController.restore();
                            Utils.LogJson(response);

                            GetCityList getCityList = new GetCityList();
                            getCityList = Utils.parserJsonResult(response,
                                    GetCityList.class);
                            if (Constants.OK.equals(getCityList.getFlag())) {
                                cityList = getCityList.getData().getOpenCityList();
                                setCityData();
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
     * 向recyclerView中填充城市数据
     */
    private void setCityData() {
        adapter = new CommonAdapter<GetCityList.DataBean.OpenCityListBean>(
                SelectCityActivity.this, R.layout.list_item_city,
                cityList) {
            @Override
            protected void convert(ViewHolder holder,
                                   GetCityList.DataBean.OpenCityListBean
                                           openCityListBean, int position) {
                if (!TextUtils.isEmpty(openCityListBean.getAcCity())) {
                    holder.setText(R.id.tv_item_city, openCityListBean.getAcCity());
                } else {
                    holder.setText(R.id.tv_item_city, "暂无");
                }
            }
        };
        /**
         * 点击事件
         */
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Utils.LogE(cityList.get(position).getAcCity());
                EventBus.getDefault().post(new CityEvent(cityList.get(position).getAcCity(),
                        cityList.get(position).getAcId()));
                finish();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化recyclerView
     */
    private void initRecyclerView() {
        /**
         * 设置RecyclerView管理器
         */
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        /**
         * 添加分隔线
         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(SelectCityActivity.this,
//                DividerItemDecoration.VERTICAL));
    }

    @OnClick(R.id.img_back)
    public void onViewClicked() {
        finish();
    }

}
