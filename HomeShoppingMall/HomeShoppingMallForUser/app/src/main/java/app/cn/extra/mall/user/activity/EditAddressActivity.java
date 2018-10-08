package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.AddressEvent;
import app.cn.extra.mall.user.event.EditAddressEvent;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.UpdateUserDeliverAddress;
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
 * 修改收货地址
 */
public class EditAddressActivity extends BaseActivty {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_region)
    TextView tvRegion;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.tv_region_content)
    TextView tvRegionContent;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.save_btn)
    Button saveBtn;
    private VaryViewHelperController varyViewHelperController;
    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 收货地址主键
     */
    String udaId = "";
    String udaAddress = "";
    String udaTel = "";
    String udaTrueName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        init();
    }

    private void init() {
        sharePreferenceUtil = new SharePreferenceUtil(EditAddressActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);

        Intent intent = getIntent();
        udaId = intent.getStringExtra("udaId");
        udaAddress = intent.getStringExtra("udaAddress");
        udaTel = intent.getStringExtra("udaTel");
        udaTrueName = intent.getStringExtra("udaTrueName");

        etName.setText(udaTrueName);
        etAddress.setText(udaAddress);
        etPhone.setText(udaTel);
    }

    @OnClick({R.id.img_back, R.id.rl_region, R.id.save_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_region:
                Intent intent = new Intent(EditAddressActivity.this, ChangeCityActivity.class);
                intent.putExtra("intentTag", "2");
                startActivity(intent);
                break;
            case R.id.save_btn:
                setSaveBtnClickable(false);
                String udaTrueName = etName.getText().toString().trim();
                String udaTel = etPhone.getText().toString().trim();
                String udaAddress = etAddress.getText().toString().trim();
                if (TextUtils.isEmpty(udaTrueName)) {
                    showShortToast(EditAddressActivity.this, "请输入收货人！");
                    setSaveBtnClickable(true);
                    break;
                }
                if (TextUtils.isEmpty(udaTel)) {
                    showShortToast(EditAddressActivity.this, "请输入联系电话！");
                    setSaveBtnClickable(true);
                    break;
                }
                if (TextUtils.isEmpty(udaAddress)) {
                    showShortToast(EditAddressActivity.this, "请输入详细地址！");
                    setSaveBtnClickable(true);
                    break;
                }
                updateUserDeliverAddress(udaTrueName, udaTel, udaAddress, udaId);
                break;
            default:
                break;
        }
    }

    /**
     * 设置保存按钮是否可以点击与显示效果
     *
     * @param b b=true可以点击；b=false不可点击
     */
    private void setSaveBtnClickable(boolean b) {
        if (b) {
            saveBtn.setBackgroundResource(R.drawable.shape_btn_blue);
            saveBtn.setTextColor(getResources().getColor(R.color.white));
            saveBtn.setEnabled(true);
        } else {
            saveBtn.setBackgroundResource(R.drawable.gray_bg_circle);
            saveBtn.setTextColor(getResources().getColor(R.color.white));
            saveBtn.setEnabled(false);
        }
    }

    /**
     * 修改收货地址
     */
    private void updateUserDeliverAddress(String udaTrueName, String udaTel, String udaAddress, String udaId) {
        if (Utils.isNetworkAvailable(EditAddressActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.updateUserDeliverAddress)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("udaTrueName", udaTrueName)
                    .addParams("udaTel", udaTel)
                    .addParams("udaAddress", udaAddress)
                    .addParams("udaId", udaId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
//                            varyViewHelperController.showErrorView();
                            setSaveBtnClickable(true);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            setSaveBtnClickable(true);
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            /**
                             * 恢复显示数据的View
                             */
//                            varyViewHelperController.restore();
                            UpdateUserDeliverAddress updateUserDeliverAddress = new UpdateUserDeliverAddress();
                            updateUserDeliverAddress = Utils.parserJsonResult(response,
                                    UpdateUserDeliverAddress.class);
                            if (Constants.OK.equals(updateUserDeliverAddress.getFlag())) {
                                if (Constants.OK.equals(updateUserDeliverAddress.getData().getStatus())) {
                                    showLongToast(EditAddressActivity.this, "修改成功！");
                                    EventBus.getDefault().post(new AddressEvent());
                                    finish();
                                } else {
                                    showLongToast(EditAddressActivity.this, updateUserDeliverAddress.getData().getErrorString());
                                }
                            } else {
                                showLongToast(EditAddressActivity.this, updateUserDeliverAddress.getErrorString());
                            }
                        }
                    });
        } else {
//            varyViewHelperController.showNetworkPoorView();
            showLongToast(getApplicationContext(), "修改失败！" + Constants.NETWORK_ERROR);
            setSaveBtnClickable(true);
        }
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
    public void editEventBus(EditAddressEvent event) {
        if (!TextUtils.isEmpty(event.getAcCity())) {
            tvRegionContent.setText(event.getAcCity());
        } else {
            tvRegionContent.setText("请选择所在地区");
        }
    }
}
