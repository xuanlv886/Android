package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.MyArrangeEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.MsGetMyArrangeListBySId;
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
 * Description 我的安排页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class MyArrangeActivity extends BaseActivty {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private SharePreferenceUtil sharePreferenceUtil;
    private VaryViewHelperController varyViewHelperController;
    private CommonAdapter<MsGetMyArrangeListBySId.DataBean> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_arrange);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    /**
     * 消息处理
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MyArrangeEventBus(MyArrangeEvent event) {
        msGetMyArrangeListBySIdPort();
    }
    @Override
    protected void onResume() {
        super.onResume();
        msGetMyArrangeListBySIdPort();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(MyArrangeActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置异常情况界面
         */
        varyViewHelperController = createCaseViewHelperController(recyclerView);
        varyViewHelperController.setUpRefreshViews(varyViewHelperController.getErrorView(),
                varyViewHelperController.getNetworkPoorView(), varyViewHelperController.getEmptyView());
        initRecyclerView();
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        msGetMyArrangeListBySIdPort();
    }

    @Override
    public void clickEvent() {
        super.clickEvent();
        msGetMyArrangeListBySIdPort();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 设置RecyclerView管理器
         */
        recyclerView.setLayoutManager(new LinearLayoutManager(MyArrangeActivity.this,
                LinearLayoutManager.VERTICAL, false));
        /**
         * 添加分隔线
         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(MyArrangeActivity.this,
//                DividerItemDecoration.VERTICAL));
    }

    /**
     * 获取商户我的安排列表接口
     */
    private void msGetMyArrangeListBySIdPort() {
        if (Utils.isNetworkAvailable(MyArrangeActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.msGetMyArrangeListBySId)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
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
                            Utils.LogJson(response);
                            MsGetMyArrangeListBySId arranges = new MsGetMyArrangeListBySId();
                            arranges = Utils.parserJsonResult(response, MsGetMyArrangeListBySId.class);
                            if (Constants.OK.equals(arranges.getFlag())) {
                                if (null != arranges.getData() && 0 < arranges.getData().size()) {
                                    /**
                                     * 恢复显示数据的View
                                     */
                                    varyViewHelperController.restore();
                                    setMyArrangeData(arranges.getData());
                                } else {
                                    varyViewHelperController.showEmptyView();
                                }
                            } else {
                                varyViewHelperController.showErrorView();
                                showShortToast(MyArrangeActivity.this,
                                        arranges.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(MyArrangeActivity.this,
                    Constants.NETWORK_ERROR);
        }
    }

    /**
     * 填充我的安排数据
     */
    private void setMyArrangeData(List<MsGetMyArrangeListBySId.DataBean> datas) {
        adapter = new CommonAdapter<MsGetMyArrangeListBySId.DataBean>(
                MyArrangeActivity.this, R.layout.list_item_my_arrange, datas) {
            @Override
            protected void convert(ViewHolder holder,
                                   MsGetMyArrangeListBySId.DataBean
                                           dataBean, int position) {
                holder.setText(R.id.tv_item_my_arrange_content, dataBean.getSaContent());
                holder.setText(R.id.tv_item_my_arrange_time, dataBean.getSaCreateTime());
                holder.setText(R.id.tv_item_my_arrange_name, "发布人：" + dataBean.getUTrueName());
            }
        };
        /**
         * 我的安排点击事件
         */
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(MyArrangeActivity.this, MyArrangeDetailActivity.class);
                intent.putExtra("saId", datas.get(position).getSaId());
                intent.putExtra("saCreateTime", datas.get(position).getSaCreateTime());
                intent.putExtra("saContent", datas.get(position).getSaContent());
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
    }


    @OnClick({R.id.img_back, R.id.tv_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_add:
                gotoAddMyArrangeActivity();
                break;
            default:
        }
    }

    /**
     * 跳转至添加我的安排界面
     */
    private void gotoAddMyArrangeActivity() {
        Intent intent = new Intent();
        intent.setClass(MyArrangeActivity.this, AddMyArrangeActivity.class);
        startActivity(intent);
    }
}
