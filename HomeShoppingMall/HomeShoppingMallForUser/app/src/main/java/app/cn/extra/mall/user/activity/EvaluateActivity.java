package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.ybq.android.spinkit.style.Wave;

import org.greenrobot.eventbus.EventBus;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.CompletedDetailsEvent;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.AddRequirementEvaluate;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * 发表评论--需求
 */
public class EvaluateActivity extends BaseActivty {
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.iv_describe_star1)
    ImageView ivDescribeStar1;
    @BindView(R.id.iv_describe_star2)
    ImageView ivDescribeStar2;
    @BindView(R.id.iv_describe_star3)
    ImageView ivDescribeStar3;
    @BindView(R.id.iv_describe_star4)
    ImageView ivDescribeStar4;
    @BindView(R.id.iv_describe_star5)
    ImageView ivDescribeStar5;
    @BindView(R.id.et_roeContent)
    EditText etRoeContent;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 默认评价等级5
     */
    int roeLevel = 5;
    String roId = "";
    String sId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        Intent intent = getIntent();
        roId = intent.getStringExtra("roId");
        sId = intent.getStringExtra("sId");

        sharePreferenceUtil = new SharePreferenceUtil(EvaluateActivity.this, Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
    }

    private void addRequirementEvaluate(String roId, String sId, String roeContent, String roeLevel) {
        if (Utils.isNetworkAvailable(EvaluateActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.addRequirementEvaluate)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("roId", roId)
                    .addParams("sId", sId)
                    .addParams("roeContent", roeContent)
                    .addParams("roeLevel", roeLevel)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
//                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 恢复显示数据的View
                             */
//                            varyViewHelperController.restore();
                            AddRequirementEvaluate addRequirementEvaluate = new AddRequirementEvaluate();
                            addRequirementEvaluate = Utils.parserJsonResult(response,
                                    AddRequirementEvaluate.class);
                            if (Constants.OK.equals(addRequirementEvaluate.getFlag())) {
                                if (Constants.OK.equals(addRequirementEvaluate.getData().getStatus())) {
                                    showLongToast(EvaluateActivity.this, "发表成功！");
                                    EventBus.getDefault().post(new CompletedDetailsEvent());
                                    finish();
                                } else {
                                    showLongToast(EvaluateActivity.this, addRequirementEvaluate.getData().getErrorString());
                                }

                            } else {
                                showLongToast(EvaluateActivity.this, addRequirementEvaluate.getErrorString());
                            }
                        }
                    });
        } else {
//            varyViewHelperController.showNetworkPoorView();
            showLongToast(getApplicationContext(),"发表失败！"+Constants.NETWORK_ERROR);
        }
    }

    @OnClick({R.id.img_back, R.id.tv_release, R.id.iv_describe_star1, R.id.iv_describe_star2, R.id.iv_describe_star3, R.id.iv_describe_star4, R.id.iv_describe_star5})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_release:
                String roeContent = etRoeContent.getText().toString().trim();
                addRequirementEvaluate(roId, sId, roeContent, "" + roeLevel);
                break;
            case R.id.iv_describe_star1:
                ivDescribeStar1.setImageResource(R.mipmap.icon_s2);
                ivDescribeStar2.setImageResource(R.mipmap.icon_s1);
                ivDescribeStar3.setImageResource(R.mipmap.icon_s1);
                ivDescribeStar4.setImageResource(R.mipmap.icon_s1);
                ivDescribeStar5.setImageResource(R.mipmap.icon_s1);
                roeLevel = 1;
                break;
            case R.id.iv_describe_star2:
                ivDescribeStar1.setImageResource(R.mipmap.icon_s2);
                ivDescribeStar2.setImageResource(R.mipmap.icon_s2);
                ivDescribeStar3.setImageResource(R.mipmap.icon_s1);
                ivDescribeStar4.setImageResource(R.mipmap.icon_s1);
                ivDescribeStar5.setImageResource(R.mipmap.icon_s1);
                roeLevel = 2;
                break;
            case R.id.iv_describe_star3:
                ivDescribeStar1.setImageResource(R.mipmap.icon_s2);
                ivDescribeStar2.setImageResource(R.mipmap.icon_s2);
                ivDescribeStar3.setImageResource(R.mipmap.icon_s2);
                ivDescribeStar4.setImageResource(R.mipmap.icon_s1);
                ivDescribeStar5.setImageResource(R.mipmap.icon_s1);
                roeLevel = 3;
                break;
            case R.id.iv_describe_star4:
                ivDescribeStar1.setImageResource(R.mipmap.icon_s2);
                ivDescribeStar2.setImageResource(R.mipmap.icon_s2);
                ivDescribeStar3.setImageResource(R.mipmap.icon_s2);
                ivDescribeStar4.setImageResource(R.mipmap.icon_s2);
                ivDescribeStar5.setImageResource(R.mipmap.icon_s1);
                roeLevel = 4;
                break;
            case R.id.iv_describe_star5:
                ivDescribeStar1.setImageResource(R.mipmap.icon_s2);
                ivDescribeStar2.setImageResource(R.mipmap.icon_s2);
                ivDescribeStar3.setImageResource(R.mipmap.icon_s2);
                ivDescribeStar4.setImageResource(R.mipmap.icon_s2);
                ivDescribeStar5.setImageResource(R.mipmap.icon_s2);
                roeLevel = 5;
                break;
            default:
                break;
        }
    }
}
