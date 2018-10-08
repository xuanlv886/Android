package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.ybq.android.spinkit.style.Wave;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.event.PendingOrderDetailsEvent;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.PhoneUtil;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.view.PopMenu;
import app.cn.extra.mall.user.vo.AddUserRequirement;
import app.cn.extra.mall.user.vo.GetUserRequirementDetail;
import app.cn.extra.mall.user.vo.SelectRequirementType;
import app.cn.extra.mall.user.vo.UpdateUserRequirement;
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
 * 发布需求
 */
public class SendDemandActivity extends BaseActivty {
    @BindView(R.id.sp_one)
    Spinner spOne;
    @BindView(R.id.et_demand_title)
    EditText etDemandTitle;
    @BindView(R.id.et_demend_content)
    EditText etDemandContent;
    @BindView(R.id.et_demand_money)
    EditText etDemandMoney;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_user_phone)
    EditText etUserPhone;
    @BindView(R.id.et_user_address)
    EditText etUserAddress;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_appoint)
    TextView tvAppoint;
    @BindView(R.id.tv_release)
    TextView tvRelease;
    @BindView(R.id.tv_merchant_price)
    TextView tvMerchantPrice;
    @BindView(R.id.v_collect_good)
    View vCollectGood;
    @BindView(R.id.ll_demand_collect_good)
    LinearLayout llDemandCollectGood;
    @BindView(R.id.et_demand_collect_good)
    EditText etDemandCollectGood;
    private SharePreferenceUtil sharePreferenceUtil;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private VaryViewHelperController varyViewHelperController;
    /**
     * 类别弹出框
     */
    private PopMenu popMenu;
    /**
     * 分类数据
     */
    List<SelectRequirementType.DataBean.RequirementTypeListBean> requirementTypeListBeanList = new ArrayList<>();

    /**
     * 分类
     */
    String rtName = "";
    /**
     * 分类id
     */
    String rtId = "";

    /**
     * 店铺id
     */
    String sId = "";
    /**
     * 商家名
     */
    String sName = "";
    /**
     * 详情实体
     */
    GetUserRequirementDetail getUserRequirementDetail = null;
    /**
     * 判断是从哪个页面跳转过来
     */
    String into = "";
    /**
     * 0 用户报价
     * 1 商家报价
     */
    String urOfferType = "0";
    /**
     * 判断当前选择是否是取货送货
     */
    boolean spFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_demand);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        sId = intent.getStringExtra("sId");
        into = intent.getStringExtra("into");
        sName = intent.getStringExtra("sName");
        if (TextUtils.isEmpty(sName)) {
            tvAppoint.setVisibility(View.GONE);
        } else {
            tvAppoint.setVisibility(View.VISIBLE);
            tvAppoint.setText(sName);
        }
        if ("PendingOrderDetailsActivity".equals(into)) {
            getUserRequirementDetail = (GetUserRequirementDetail) intent.getSerializableExtra("userRequirement");
        }
        sharePreferenceUtil = new SharePreferenceUtil(SendDemandActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        selectRequirementType();
    }

    @OnClick({R.id.img_back, R.id.tv_release, R.id.tv_merchant_price})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_merchant_price:
                if ("0".equals(urOfferType)) {
                    tvMerchantPrice.setTextColor(getResources().getColor(R.color.blue));
                    tvMerchantPrice.setBackgroundResource(R.drawable.shape_text_blue_bg);
                    urOfferType = "1";
                } else if ("1".equals(urOfferType)) {
                    tvMerchantPrice.setTextColor(getResources().getColor(R.color.gray_text_color));
                    tvMerchantPrice.setBackgroundResource(R.drawable.shape_text_graw_bg);
                    urOfferType = "0";
                }
                break;
            case R.id.tv_release:
                String urTitle = etDemandTitle.getText().toString().trim();
                String urContent = etDemandContent.getText().toString().trim();
                String urOfferPrice = etDemandMoney.getText().toString().trim();
                String urTrueName = etUserName.getText().toString().trim();
                String urTel = etUserPhone.getText().toString().trim();
                String urAddress = etUserAddress.getText().toString().trim();
                String urGetAddress = etDemandCollectGood.getText().toString().trim();
                if (TextUtils.isEmpty(rtId)) {
                    showShortToast(SendDemandActivity.this, "请选择需求类型！");
                    break;
                }
                if (TextUtils.isEmpty(urTitle)) {
                    showShortToast(SendDemandActivity.this, "请填写需求标题！");
                    break;
                }
                if (TextUtils.isEmpty(urContent)) {
                    showShortToast(SendDemandActivity.this, "请填写需求内容！");
                    break;
                }
                if (TextUtils.isEmpty(urTrueName)) {
                    showShortToast(SendDemandActivity.this, "请填写发布人！");
                    break;
                }
                if (TextUtils.isEmpty(urTel)) {
                    showShortToast(SendDemandActivity.this, "请填写联系电话！");
                    break;
                }
                if (TextUtils.isEmpty(urAddress)) {
                    showShortToast(SendDemandActivity.this, "请填写居住地址！");
                    break;
                }
                if (spFlag) {
                    if (TextUtils.isEmpty(urGetAddress)) {
                        showShortToast(SendDemandActivity.this, "请填写取货地址！");
                        break;
                    }
                }
//                if (TextUtils.isEmpty(urOfferPrice)) {
//                    urOfferType = "1";
//                } else {
//                    urOfferType = "0";
//                }
                if ("0".equals(urOfferType)) {
                    if (TextUtils.isEmpty(urOfferPrice)) {
                        showShortToast(SendDemandActivity.this, "请填写悬赏金额！");
                        break;
                    }
                    if (urOfferPrice.startsWith(".")) {
                        showShortToast(SendDemandActivity.this, "请填写正确的金额！");
                        break;
                    }
                    if (urOfferPrice.contains(".")) {
                        String[] s = urOfferPrice.split("\\.");
                        if (s.length > 1) {
                            if (s[1].length() > 2) {
                                showShortToast(SendDemandActivity.this, "请填写正确的金额！");
                                break;
                            }
                        } else {
                            showShortToast(SendDemandActivity.this, "请填写正确的金额！");
                            break;
                        }
                    }
                } else if ("1".equals(urOfferType)) {
                    urOfferPrice = "0";
                }
                if (!PhoneUtil.isChinaPhoneLegal(urTel)) {
                    showShortToast(SendDemandActivity.this, "请填写正确的手机号！");
                    break;
                }
                if ("PendingOrderDetailsActivity".equals(into)) {
                    updateUserRequirement(urTitle, urContent, urOfferPrice, urTrueName, urTel, urAddress, urGetAddress);
                } else {
                    addUserRequirement(urTitle, urContent, urOfferPrice, urTrueName, urTel, urAddress, urGetAddress);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 设置修改数据
     */
    private void setChangeInfo(List<String> list) {
        String rtName = getUserRequirementDetail.getData().getRtName();
        String urTitle = getUserRequirementDetail.getData().getUrTitle();
        String urContent = getUserRequirementDetail.getData().getUrContent();
        String urOfferPrice = "" + getUserRequirementDetail.getData().getUrOfferPrice();
        String urTrueName = getUserRequirementDetail.getData().getUrTrueName();
        String urTel = getUserRequirementDetail.getData().getUrTel();
        String urAddress = getUserRequirementDetail.getData().getUrAddress();
        String urGetAddress = getUserRequirementDetail.getData().getUrGetAddress();
        for (int i = 0; i < list.size(); i++) {
            if (rtName.equals(list.get(i))) {
                spOne.setSelection(i);
            }
        }
        tvTitle.setText("修改需求");
        tvRelease.setText("修改");
        etDemandTitle.setText(urTitle);
        etDemandContent.setText(urContent);
        if (1 == getUserRequirementDetail.getData().getUrOfferType()) {
            etDemandMoney.setText("需商家报价");
            urOfferType = "1";
        } else {
            etDemandMoney.setText(urOfferPrice);
            urOfferType = "0";
        }
        etDemandMoney.setEnabled(false);
        tvMerchantPrice.setVisibility(View.GONE);
        etUserName.setText(urTrueName);
        etUserPhone.setText(urTel);
        etUserAddress.setText(urAddress);
        etDemandCollectGood.setText(urGetAddress);
    }

    /**
     * 获取需求类别
     */
    private void selectRequirementType() {
        if (Utils.isNetworkAvailable(SendDemandActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url(Constants.selectRequirementType)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
//                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            /**
                             * 恢复显示数据的View
                             */
//                            varyViewHelperController.restore();

                            SelectRequirementType selectRequirementType = new SelectRequirementType();
                            selectRequirementType = Utils.parserJsonResult(response,
                                    SelectRequirementType.class);
                            if (Constants.OK.equals(selectRequirementType.getFlag())) {
                                if (null != selectRequirementType.getData()
                                        .getRequirementTypeList()
                                        && 0 < selectRequirementType.getData()
                                        .getRequirementTypeList().size()) {
                                    List<String> list = new ArrayList<String>();
                                    requirementTypeListBeanList.clear();
                                    ArrayAdapter<String> adapter = null;
                                    for (int i = 0; i < selectRequirementType.getData()
                                            .getRequirementTypeList().size(); i++) {
                                        list.add(selectRequirementType.getData().getRequirementTypeList().get(i).getRtName());
                                        requirementTypeListBeanList.add(selectRequirementType.getData()
                                                .getRequirementTypeList().get(i));
                                    }
                                    adapter = new ArrayAdapter<String>(
                                            SendDemandActivity.this,
                                            android.R.layout.simple_spinner_item,
                                            list);
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spOne.setAdapter(adapter);
                                    spOne.setOnItemSelectedListener(upDateUIAboutSPCheck(list));
                                    if (requirementTypeListBeanList.size() > 0) {
                                        rtId = requirementTypeListBeanList.get(0).getRtId();
                                    }
                                    if ("PendingOrderDetailsActivity".equals(into) && getUserRequirementDetail != null) {
                                        setChangeInfo(list);
                                    }
                                }
                            } else {
                                showLongToast(SendDemandActivity.this, selectRequirementType.getErrorString());
                            }
                        }
                    });
        } else {
//            varyViewHelperController.showNetworkPoorView();
        }
    }

    /**
     * 根据需求类型选择更改布局
     */
    private AdapterView.OnItemSelectedListener upDateUIAboutSPCheck(List<String> list) {
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ("取货送货".equals(list.get(i))) {
                    vCollectGood.setVisibility(View.VISIBLE);
                    llDemandCollectGood.setVisibility(View.VISIBLE);
                    spFlag = true;
                } else {
                    vCollectGood.setVisibility(View.GONE);
                    llDemandCollectGood.setVisibility(View.GONE);
                    spFlag = false;
                }
                rtId = requirementTypeListBeanList.get(i).getRtId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        return itemSelectedListener;
    }

    /**
     * 修改需求
     */
    private void updateUserRequirement(String urTitle, String urContent, String urOfferPrice, String urTrueName, String urTel, String urAddress, String urGetAddress) {
        if (Utils.isNetworkAvailable(SendDemandActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            if (sId == null) {
                sId = "";
            }
            OkHttpUtils
                    .post()
                    .url(Constants.updateUserRequirement)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("rtId", rtId)
                    .addParams("urId", getUserRequirementDetail.getData().getUrId())
                    .addParams("urTitle", urTitle)
                    .addParams("urContent", urContent)
                    .addParams("urOfferType", getUserRequirementDetail.getData()
                            .getUrOfferType() + "")
                    .addParams("urOfferPrice", urOfferPrice)
                    .addParams("urTrueName", urTrueName)
                    .addParams("urTel", urTel)
                    .addParams("urAddress", urAddress)
                    .addParams("urGetAddress", urGetAddress)
                    .addParams("sId", sId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
//                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            /**
                             * 恢复显示数据的View
                             */
//                            varyViewHelperController.restore();

                            UpdateUserRequirement updateUserRequirement = new UpdateUserRequirement();
                            updateUserRequirement = Utils.parserJsonResult(response,
                                    UpdateUserRequirement.class);
                            if (Constants.OK.equals(updateUserRequirement.getFlag())) {
                                if (Constants.OK.equals(updateUserRequirement.getData().getStatus())) {
                                    showLongToast(SendDemandActivity.this, "修改成功！");
                                    EventBus.getDefault().post(new PendingOrderDetailsEvent());
                                    finish();
                                } else {
                                    showLongToast(SendDemandActivity.this, updateUserRequirement.getData().getErrorString());
                                }

                            } else {
                                showLongToast(SendDemandActivity.this, updateUserRequirement.getErrorString());
                            }
                        }
                    });
        } else {
//            varyViewHelperController.showNetworkPoorView();
        }
    }

    /**
     * 发布需求
     */
    private void addUserRequirement(String urTitle, String urContent, String urOfferPrice, String urTrueName, String urTel, String urAddress, String urGetAddress) {
        if (Utils.isNetworkAvailable(SendDemandActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            if (sId == null) {
                sId = "";
            }
            OkHttpUtils
                    .post()
                    .url(Constants.addUserRequirement)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("rtId", rtId)
                    .addParams("urTitle", urTitle)
                    .addParams("urContent", urContent)
                    .addParams("urOfferType", urOfferType)
                    .addParams("urOfferPrice", urOfferPrice)
                    .addParams("urTrueName", urTrueName)
                    .addParams("urTel", urTel)
                    .addParams("urAddress", urAddress)
                    .addParams("urGetAddress", urGetAddress)
                    .addParams("sId", sId)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
//                            varyViewHelperController.showErrorView();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            /**
                             * 恢复显示数据的View
                             */
//                            varyViewHelperController.restore();

                            AddUserRequirement addUserRequirement = new AddUserRequirement();
                            addUserRequirement = Utils.parserJsonResult(response,
                                    AddUserRequirement.class);
                            if (Constants.OK.equals(addUserRequirement.getFlag())) {
                                if (Constants.OK.equals(addUserRequirement.getData().getStatus())) {
                                    showLongToast(SendDemandActivity.this, "发布成功！");
                                    finish();
                                } else {
                                    showLongToast(SendDemandActivity.this, addUserRequirement.getData().getErrorString());
                                }

                            } else {
                                showLongToast(SendDemandActivity.this, addUserRequirement.getErrorString());
                            }
                        }
                    });
        } else {
//            varyViewHelperController.showNetworkPoorView();
            showLongToast(getApplicationContext(), "发布失败！" + Constants.NETWORK_ERROR);
        }
    }
}
