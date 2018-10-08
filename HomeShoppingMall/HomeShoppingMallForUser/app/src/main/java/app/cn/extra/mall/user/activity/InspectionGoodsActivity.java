package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.DemandDetailsEvent;
import app.cn.extra.mall.user.event.PendingInspectionGoodsFragmentEvent;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.StayInspectionProduct;
import app.cn.extra.mall.user.vo.UpdateRequirementOrderStatus;
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
 * 验货页面
 */
public class InspectionGoodsActivity extends BaseActivty {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipyRefreshLayout refreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.sure_btn)
    Button sureBtn;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private VaryViewHelperController varyViewHelperController;
    private CommonAdapter<StayInspectionProduct.DataBean.PicListBean> adapter;
    /**
     * 用于存放临时数据
     */
    List<StayInspectionProduct.DataBean.PicListBean> picListBeanList = null;
    private SharePreferenceUtil sharePreferenceUtil;
    String roId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspection_goods);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        Intent intent = getIntent();
        roId = intent.getStringExtra("roId");
        sharePreferenceUtil = new SharePreferenceUtil(InspectionGoodsActivity.this,
                Constants.SAVE_USER);
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
        stayInspectionProduct(roId);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 网格布局
         */
        GridLayoutManager layoutManage = new GridLayoutManager(InspectionGoodsActivity.this, 3);
        recyclerView.setLayoutManager(layoutManage);
        /**
         * 添加分隔线
         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(InspectionGoodsActivity.this,
//                DividerItemDecoration.VERTICAL));
    }

    /**
     * 初始化SwipeRefreshLayout
     */
    private void initRefreshLayout() {
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        refreshLayout.setEnabled(false);
    }

    /**
     * 获取页面相关数据接口
     * p--当前页
     * ps--页大小
     */
    private void stayInspectionProduct(String roId) {
        if (Utils.isNetworkAvailable(InspectionGoodsActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.stayInspectionProduct)
                    .addParams("roId", roId)
                    .addParams("uId", sharePreferenceUtil.getUID())
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
                            StayInspectionProduct stayInspectionProduct = new StayInspectionProduct();
                            stayInspectionProduct = Utils.parserJsonResult(response,
                                    StayInspectionProduct.class);
                            if (Constants.OK.equals(stayInspectionProduct.getFlag())) {
                                picListBeanList = stayInspectionProduct.getData().getPicList();
                                if (null != picListBeanList && 0 < picListBeanList.size()) {
                                    setRecommendProductData();
//                                    setRecyclerViewHeaderAndFooter(stayInspectionProduct.getData());
                                } else {
                                    varyViewHelperController.showEmptyView();
                                }
                            } else {
                                varyViewHelperController.showErrorView();
                            }
                        }
                    });
        } else {
            varyViewHelperController.showNetworkPoorView();
        }
    }

    private int getScreenWidth() {
        // TODO Auto-generated method stub
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        return dm.widthPixels; // 屏幕宽（像素，如：480px）
    }

    /**
     * 填充数据
     */
    private void setRecommendProductData() {
        adapter = new CommonAdapter<StayInspectionProduct.DataBean.PicListBean>(
                InspectionGoodsActivity.this, R.layout.grid_item_inspection_goods, picListBeanList) {
            @Override
            protected void convert(ViewHolder holder,
                                   StayInspectionProduct.DataBean.PicListBean
                                           picListBean, int position) {
                RequestOptions requestOptions = new RequestOptions()
                        .error(R.drawable.ic_exception)
                        .fallback(R.drawable.ic_exception);
                Glide.with(InspectionGoodsActivity.this)
                        .load(picListBean.getUrl() + "?x-oss-process=image/resize,w_"
                                + (Utils.getScreenWidth(InspectionGoodsActivity.this)))
                        .apply(requestOptions)
                        .into((ImageView) holder.getView(R.id.iv_pic));
            }
        };
        /**
         * 点击事件
         */
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (picListBeanList.size() > 0) {
                    Intent intent = new Intent(InspectionGoodsActivity.this,
                            PicShowActivity.class);
                    intent.putExtra("url", picListBeanList.get(position).getUrl());
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
    }


//    /**
//     * 设置RecyclerViewHeaderAndFooter
//     */
//    private void setRecyclerViewHeaderAndFooter(StayInspectionProduct.DataBean datas) {
//        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(adapter);
//        /**
//         * 设置RecyclerViewFooter
//         */
//        View footerView = LayoutInflater.from(InspectionGoodsActivity.this).inflate(
//                R.layout.inspection_goods_footer, null);
//        TextView tvInspectionGoodsTime = footerView.findViewById(R.id.tv_inspection_goods_time);
//        tvInspectionGoodsTime.setText("待验货时间:" + datas.getRoConfirmTime());
//        mHeaderAndFooterWrapper.addFootView(footerView);
//        recyclerView.setAdapter(mHeaderAndFooterWrapper);
//    }

    @OnClick({R.id.img_back, R.id.sure_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.sure_btn:
                setSureBtnClickable(false);
                rorderVerification(roId);
                break;
            default:
                break;
        }
    }

    /**
     * 设置确认验货按钮显示效果与是否可以点击
     *
     * @param i i=true可以点击；i=false不可点击
     */
    private void setSureBtnClickable(boolean i) {
        if (i) {
            sureBtn.setBackgroundResource(R.drawable.shape_btn_blue);
            sureBtn.setTextColor(getResources().getColor(R.color.white));
            sureBtn.setEnabled(true);
        } else {
            sureBtn.setBackgroundResource(R.drawable.shape_btn_gray);
            sureBtn.setTextColor(getResources().getColor(R.color.white));
            sureBtn.setEnabled(false);
        }
    }

    /**
     * 确认验货
     */
    private void rorderVerification(String roId) {
        if (Utils.isNetworkAvailable(InspectionGoodsActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.rorderVerification)
                    .addParams("roId", roId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            e.printStackTrace();
                            setSureBtnClickable(true);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            setSureBtnClickable(true);
                            UpdateRequirementOrderStatus updateRequirementOrderStatus = new UpdateRequirementOrderStatus();
                            updateRequirementOrderStatus = Utils.parserJsonResult(response,
                                    UpdateRequirementOrderStatus.class);
                            if (Constants.OK.equals(updateRequirementOrderStatus.getFlag())) {
                                if (Constants.OK.equals(updateRequirementOrderStatus.getData().getStatus())) {
                                    showLongToast(InspectionGoodsActivity.this, "验货成功！");
                                    EventBus.getDefault().post(new PendingInspectionGoodsFragmentEvent());
                                    EventBus.getDefault().post(new DemandDetailsEvent());
                                    finish();
                                } else {
                                    showLongToast(InspectionGoodsActivity.this, updateRequirementOrderStatus.getData().getErrorString());
                                }

                            } else {
                                showLongToast(InspectionGoodsActivity.this, updateRequirementOrderStatus.getErrorString());
                            }
                        }
                    });
        } else {
            showLongToast(InspectionGoodsActivity.this, Constants.NETWORK_ERROR);
            setSureBtnClickable(true);
        }
    }
}
