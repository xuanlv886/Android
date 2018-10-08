package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.MyArrangeEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.vo.DelStoreArrange;
import app.cn.extra.mall.merchant.vo.UpdateMyArrange;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * 安排详情
 */
public class MyArrangeDetailActivity extends BaseActivty {
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;
    @BindView(R.id.et_content)
    EditText etContent;
    /**
     * 安排id
     */
    String saId = "";
    /**
     * 安排添加时间
     */
    String saCreateTime = "";
    /**
     * 安排内容
     */
    String saContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_arrange_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        saId = intent.getStringExtra("saId");
        saCreateTime = intent.getStringExtra("saCreateTime");
        saContent = intent.getStringExtra("saContent");

        tvCreateTime.setText(saCreateTime);
        etContent.setText(saContent);
    }

    @OnClick({R.id.img_back, R.id.rl_delete, R.id.rl_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_delete:
                delStoreArrange(saId);
                break;
            case R.id.rl_save:
//                saCreateTime = tvCreateTime.getText().toString().trim();
                saContent = etContent.getText().toString().trim();
                if (TextUtils.isEmpty(saContent)) {
                    showShortToast(MyArrangeDetailActivity.this,
                            "请输入安排内容！");
                    break;
                }
                updateMyArrange(saId, saContent);
                break;
            default:
                break;
        }
    }

    /**
     * 删除商户我的安排接口
     */
    private void delStoreArrange(String saId) {
        if (Utils.isNetworkAvailable(MyArrangeDetailActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.delStoreArrange)
                    .addParams("saId", saId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 接口调用出错
                             */
                            showShortToast(MyArrangeDetailActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            DelStoreArrange delStoreArrange = new DelStoreArrange();
                            delStoreArrange = Utils.parserJsonResult(response, DelStoreArrange.class);
                            if (Constants.OK.equals(delStoreArrange.getFlag())) {
                                if(delStoreArrange.getData().isStatus()) {
                                    showShortToast(MyArrangeDetailActivity.this,
                                            "删除成功");
                                    EventBus.getDefault().post(new MyArrangeEvent());
                                    finish();
                                }else {
                                    showShortToast(MyArrangeDetailActivity.this,
                                            delStoreArrange.getData().getErrorString());
                                }
                            } else {
                                showShortToast(MyArrangeDetailActivity.this,
                                        delStoreArrange.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(MyArrangeDetailActivity.this, Constants.NETWORK_ERROR);
        }
    }

    /**
     * 修改商户我的安排接口
     */
    private void updateMyArrange(String saId, String saContent) {
        if (Utils.isNetworkAvailable(MyArrangeDetailActivity.this)) {
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.updateMyArrange)
                    .addParams("saId", saId)
                    .addParams("saContent", saContent)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 接口调用出错
                             */
                            showShortToast(MyArrangeDetailActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            UpdateMyArrange updateMyArrange = new UpdateMyArrange();
                            updateMyArrange = Utils.parserJsonResult(response, UpdateMyArrange.class);
                            if (Constants.OK.equals(updateMyArrange.getFlag())) {
                                if(updateMyArrange.getData().isStatus()) {
                                    showShortToast(MyArrangeDetailActivity.this,
                                            "修改成功");
                                    EventBus.getDefault().post(new MyArrangeEvent());
                                    finish();
                                }else {
                                    showShortToast(MyArrangeDetailActivity.this,
                                            updateMyArrange.getData().getErrorString());
                                }
                            } else {
                                showShortToast(MyArrangeDetailActivity.this,
                                        updateMyArrange.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(MyArrangeDetailActivity.this, Constants.NETWORK_ERROR);
        }
    }
}
