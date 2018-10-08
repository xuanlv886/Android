package app.cn.extra.mall.merchant.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.style.Wave;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.CityEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.utils.osshelper.PutObjectSamples;
import app.cn.extra.mall.merchant.vo.GetCaptcha;
import app.cn.extra.mall.merchant.vo.GetLonLat;
import app.cn.extra.mall.merchant.vo.StorePersonRegistered;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.CountDownTimerUtil;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.ThreadPoolManager;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import cfkj.app.cn.cfkjcommonlib.view.CommonPopupWindow;
import okhttp3.Call;

/**
 * Description 企业商户注册页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class RegisterForCompanyActivity extends TakePhotoActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.et_tel)
    EditText etTel;
    @BindView(R.id.btn_captcha)
    Button btnCaptcha;
    @BindView(R.id.et_captcha)
    EditText etCaptcha;
    @BindView(R.id.et_sname)
    EditText etSname;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.rl_city)
    RelativeLayout rlCity;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.rl_sex)
    RelativeLayout rlSex;
    @BindView(R.id.et_id_card)
    EditText etIdCard;
    @BindView(R.id.img_id_card)
    ImageView imgIdCard;
    @BindView(R.id.rl_id_img)
    RelativeLayout rlIdImg;
    @BindView(R.id.et_sLegal)
    EditText etSLegal;
    @BindView(R.id.et_sLegal_id_card)
    EditText etSLegalIdCard;
    @BindView(R.id.img_legal_id_card)
    ImageView imgLegalIdCard;
    @BindView(R.id.rl_legal_id_img)
    RelativeLayout rlLegalIdImg;
    @BindView(R.id.img_business_license_card)
    ImageView imgBusinessLicenseCard;
    @BindView(R.id.rl_business_license_img)
    RelativeLayout rlBusinessLicenseImg;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.et_psw_again)
    EditText etPswAgain;

    /**
     * 声明倒计时工具类对象
     */
    private CountDownTimerUtil mCountDownTimerUtil;
    /**
     * 临时存储短信验证码
     */
    private String captchaCode = "";

    /**
     * 临时存储商户选择的城市的acId
     */
    private String acId = "";
    /**
     * 临时存储商户地址所在的经度
     */
    private String sLon = "";
    /**
     * 临时存储商户地址所在的纬度
     */
    private String sLat = "";
    /**
     * 临时存储用户性别
     * 0--女，1--男
     */
    private String uSex = "0";
    private CommonPopupWindow popupWindow;

    /**
     * 动态申请权限
     */
    RxPermissions rxPermissions;
    private TakePhoto takePhoto;
    /**
     * 裁剪参数
     */
    private CropOptions cropOptions;
    /**
     * 压缩参数
     */
    private CompressConfig compressConfig;
    /**
     * 图片保存路径
     */
    private Uri imageUri;

    /**
     * 临时存储商户负责人证件照图片路径
     */
    private String imgIdCardPath = "";

    /**
     * 临时存储商户法人证件照图片路径
     */
    private String imgLegalIdCardPath = "";

    /**
     * 临时存储商户营业执照图片路径
     */
    private String imgBussinessLicensePath = "";

    /**
     * 图片上传服务
     */
    private OSS oss;
    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 标识上传的图片的类型 1--商户负责人证件照，2--商户法人证件照，3--商户营业执照
     */
    private int imgId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_company);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化配置项
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(RegisterForCompanyActivity.this,
                Constants.SAVE_USER);
        rxPermissions = new RxPermissions(this);
        /**
         * 注册订阅者
         */
        EventBus.getDefault().register(this);
        initTakePhoto();
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
    }

    /**
     * 初始化第三方工具库TakePhoto
     */
    private void initTakePhoto() {
        /**
         * 获取TakePhoto实例
         */
        takePhoto = getTakePhoto();
        /**
         * 设置裁剪参数
         * 身份证为1.6:1  故剪裁参数为16：10
         */
        cropOptions = new CropOptions.Builder().setAspectX(16).setAspectY(10)
                .setWithOwnCrop(false).create();
        /**
         * 设置压缩参数
         */
        compressConfig = new CompressConfig.Builder().setMaxSize(2 * 1024).setMaxPixel(800).create();
        /**
         * 设置为需要压缩
         */
        takePhoto.onEnableCompress(compressConfig, true);
    }

    @OnClick({R.id.tv_submit, R.id.img_back, R.id.btn_captcha, R.id.rl_city, R.id.rl_sex, R.id.rl_id_img, R.id.rl_legal_id_img, R.id.rl_business_license_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                doSubmit();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_captcha:
                doGetCaptcha();
                break;
            case R.id.rl_city:
                startActivity(new Intent(RegisterForCompanyActivity.this,
                        SelectCityActivity.class));
                break;
            case R.id.rl_sex:
                showCommonBottomDialog(0);
                break;
            case R.id.rl_id_img:
                showCommonBottomDialog(1);
                break;
            case R.id.rl_legal_id_img:
                showCommonBottomDialog(2);
                break;
            case R.id.rl_business_license_img:
                showCommonBottomDialog(3);
                break;
            default:
        }
    }

    /**
     * 提交相关注册信息
     */
    private void doSubmit() {
        /**
         * 验证手机号合法性
         */
        if (!Utils.isValidPhoneNumber(etTel.getText().toString().trim())) {
            CustomToast.showToast(RegisterForCompanyActivity.this,
                    getResources().getString(R.string.phoneNumError), Toast.LENGTH_LONG);
            return;
        }
        /**
         * 验证验证码
         */
        if (TextUtils.isEmpty(etCaptcha.getText().toString().trim())) {
            CustomToast.showToast(RegisterForCompanyActivity.this,
                    getResources().getString(R.string.captchaEmpty), Toast.LENGTH_LONG);
            return;
        } else {
            if (!captchaCode.equals(etCaptcha.getText().toString().trim())) {
                CustomToast.showToast(RegisterForCompanyActivity.this,
                        getResources().getString(R.string.captchaError), Toast.LENGTH_LONG);
                return;
            }
        }
        /**
         * 验证商户名称
         */
        if (TextUtils.isEmpty(etSname.getText().toString().trim())) {
            CustomToast.showToast(RegisterForCompanyActivity.this,
                    getResources().getString(R.string.sNameEmpty), Toast.LENGTH_LONG);
            return;
        }
        /**
         * 验证商户所在城市
         */
        if (TextUtils.isEmpty(acId)) {
            CustomToast.showToast(RegisterForCompanyActivity.this,
                    getResources().getString(R.string.acIdEmpty), Toast.LENGTH_LONG);
            return;
        }
        /**
         * 验证商户详细地址
         */
        if (TextUtils.isEmpty(etAddress.getText().toString().trim())) {
            CustomToast.showToast(RegisterForCompanyActivity.this,
                    getResources().getString(R.string.sAddressEmpty), Toast.LENGTH_LONG);
            return;
        }
        /**
         * 验证商户负责人姓名
         */
        if (TextUtils.isEmpty(etUserName.getText().toString().trim())) {
            CustomToast.showToast(RegisterForCompanyActivity.this,
                    getResources().getString(R.string.sLeaderEmpty), Toast.LENGTH_LONG);
            return;
        }
        /**
         * 验证商户法人姓名
         */
        if (TextUtils.isEmpty(etSLegal.getText().toString().trim())) {
            CustomToast.showToast(RegisterForCompanyActivity.this,
                    getResources().getString(R.string.sLegalEmpty), Toast.LENGTH_LONG);
            return;
        }
        /**
         * 验证密码
         */
        if (TextUtils.isEmpty(etPsw.getText().toString().trim())) {
            CustomToast.showToast(RegisterForCompanyActivity.this,
                    getResources().getString(R.string.passwordError), Toast.LENGTH_LONG);
            return;
        }
        if (TextUtils.isEmpty(etPswAgain.getText().toString().trim())) {
            CustomToast.showToast(RegisterForCompanyActivity.this,
                    getResources().getString(R.string.pswAgainEmpty), Toast.LENGTH_LONG);
            return;
        }
        if (!etPsw.getText().toString().trim().equals(
                etPswAgain.getText().toString().trim())) {
            CustomToast.showToast(RegisterForCompanyActivity.this,
                    getResources().getString(R.string.pswError), Toast.LENGTH_LONG);
            return;
        }
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        uploadImgToOSS();
        getLonLatPort();
    }

    /**
     * 企业身份商户注册接口
     */
    private void storeCompanyRegisteredPort() {
        if (Utils.isNetworkAvailable(RegisterForCompanyActivity.this)) {
            String sLeaderPic = Utils.getFileName(imgIdCardPath) + Constants.IMG_TYPE;
            if (Utils.getFileName(imgIdCardPath) == null || "".equals(Utils.getFileName(imgIdCardPath)) || "null".equals(Utils.getFileName(imgIdCardPath))) {
                sLeaderPic = "";
            }
            String sLegalPic = Utils.getFileName(imgLegalIdCardPath) + Constants.IMG_TYPE;
            if (Utils.getFileName(imgLegalIdCardPath) == null || "".equals(Utils.getFileName(imgLegalIdCardPath)) || "null".equals(Utils.getFileName(imgLegalIdCardPath))) {
                sLegalPic = "";
            }
            String sBussinessLicensePic = Utils.getFileName(imgBussinessLicensePath) + Constants.IMG_TYPE;
            if (Utils.getFileName(imgBussinessLicensePath) == null || "".equals(Utils.getFileName(imgBussinessLicensePath)) || "null".equals(Utils.getFileName(imgBussinessLicensePath))) {
                sBussinessLicensePic = "";
            }
            OkHttpUtils
                    .post()
                    .url(Constants.storeCompanyRegistered)
                    .addParams("sTel", etTel.getText().toString().trim())
                    .addParams("sName", etSname.getText().toString().trim())
                    .addParams("acId", acId)
                    .addParams("sAddress", etAddress.getText().toString().trim())
                    .addParams("sLon", sLon)
                    .addParams("sLat", sLat)
                    .addParams("sLeader", etUserName.getText().toString().trim())
                    .addParams("uSex", uSex)
                    .addParams("sLeaderIdCard", etIdCard.getText().toString().trim())
                    .addParams("sLeaderPic", sLeaderPic)
                    .addParams("sLegal", etSLegal.getText().toString().trim())
                    .addParams("sLegalIdCard", etSLegalIdCard.getText().toString().trim())
                    .addParams("sLegalPic", sLegalPic)
                    .addParams("sBussinessLicensePic", sBussinessLicensePic)
                    .addParams("sPassword", etPswAgain.getText().toString().trim())
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
                            CustomToast.showToast(RegisterForCompanyActivity.this,
                                    getResources().getString(R.string.portError), Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            StorePersonRegistered storePersonRegistered = new StorePersonRegistered();
                            storePersonRegistered = Utils.parserJsonResult(response,
                                    StorePersonRegistered.class);
                            if (Constants.OK.equals(storePersonRegistered.getFlag())) {
                                if (Constants.OK.equals(storePersonRegistered.getData().getStatus())) {
                                    /**
                                     * 保存商户相关数据
                                     */
                                    sharePreferenceUtil.setUID(storePersonRegistered.getData().getuId());
                                    sharePreferenceUtil.setPhoneId(storePersonRegistered.getData()
                                            .getUPhoneId());
                                    sharePreferenceUtil.setStoreAcId(acId);
                                    sharePreferenceUtil.setUAccount(etTel.getText().toString().trim());
                                    /**
                                     * 网易云
                                     */
                                    sharePreferenceUtil.saveUserToken(storePersonRegistered.getData().getToken());
                                    sharePreferenceUtil.saveAccid(storePersonRegistered.getData().getAccid());

                                    CustomToast.showToast(RegisterForCompanyActivity.this,
                                            getResources().getString(R.string.registerSuccess),
                                            Toast.LENGTH_LONG);
                                    gotoLoginActivity();
                                } else {
                                    /**
                                     * 接口调用出错
                                     */
                                    CustomToast.showToast(RegisterForCompanyActivity.this,
                                            storePersonRegistered.getData().getErrorString(), Toast.LENGTH_SHORT);
                                }

                            } else {
                                /**
                                 * 接口调用出错
                                 */
                                CustomToast.showToast(RegisterForCompanyActivity.this,
                                        getResources().getString(R.string.portError), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            CustomToast.showToast(RegisterForCompanyActivity.this,
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);
        }
    }

    /**
     * 跳转至登录界面
     */
    private void gotoLoginActivity() {
        Intent intent = new Intent();
        intent.setClass(RegisterForCompanyActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 上传图片至OSS
     */
    private void uploadImgToOSS() {
        /**
         * 判断是否有图片 有则先将图片上传至阿里云OSS
         */
        if (!TextUtils.isEmpty(imgIdCardPath)) {
            initOSS();
            /**
             * 图片在OSS上的路径
             */
            String objectName = Constants.STORE_PIC + Utils.getFileName(
                    imgIdCardPath) + Constants.IMG_TYPE;
            ThreadPoolManager.getInstance().execute(() -> new PutObjectSamples(oss,
                    Constants.bucketName, objectName, imgIdCardPath).asyncPutObjectFromLocalFile());
        }
        if (!TextUtils.isEmpty(imgLegalIdCardPath)) {
            initOSS();
            /**
             * 图片在OSS上的路径
             */
            String objectName = Constants.STORE_PIC + Utils.getFileName(
                    imgLegalIdCardPath) + Constants.IMG_TYPE;
            ThreadPoolManager.getInstance().execute(() -> new PutObjectSamples(oss,
                    Constants.bucketName, objectName, imgLegalIdCardPath)
                    .asyncPutObjectFromLocalFile());
        }
        if (!TextUtils.isEmpty(imgBussinessLicensePath)) {
            initOSS();
            /**
             * 图片在OSS上的路径
             */
            String objectName = Constants.STORE_PIC + Utils.getFileName(
                    imgBussinessLicensePath) + Constants.IMG_TYPE;
            ThreadPoolManager.getInstance().execute(() -> new PutObjectSamples(oss,
                    Constants.bucketName, objectName, imgBussinessLicensePath)
                    .asyncPutObjectFromLocalFile());
        }
    }

    /**
     * 弹出底部选择窗口
     *
     * @param tag 0--选择性别，1--负责人证件图片上传，2--法人证件图片上传，3--营业执照图片上传
     */
    private void showCommonBottomDialog(int tag) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        View upView = LayoutInflater.from(this).inflate(R.layout.dialog_common_bottom_pop, null);
        //测量View的宽高
        Utils.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.dialog_common_bottom_pop)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, upView.getMeasuredHeight())
                /**
                 * 取值范围0.0f-1.0f 值越小越暗
                 */
                .setBackGroundLevel(0.5f)
                .setAnimationStyle(R.style.PopupAnimation)
                .setViewOnclickListener((view, layoutResId) -> {
                    switch (layoutResId) {
                        case R.layout.dialog_common_bottom_pop:
                            Button firstBtn = view.findViewById(R.id.firstBtn);
                            Button secondBtn = view.findViewById(R.id.secondBtn);
                            Button cancleBtn = view.findViewById(R.id.cancelBtn);
                            if (0 == tag) {
                                firstBtn.setText(getResources().getString(R.string.man));
                                secondBtn.setText(getResources().getString(R.string.woman));
                                firstBtn.setOnClickListener(v -> {
                                    if (popupWindow != null) {
                                        tvSex.setText(getResources().getString(R.string.man));
                                        uSex = "1";
                                        popupWindow.dismiss();
                                    }
                                });
                                secondBtn.setOnClickListener(v -> {
                                    if (popupWindow != null) {
                                        tvSex.setText(getResources().getString(R.string.woman));
                                        uSex = "0";
                                        popupWindow.dismiss();
                                    }
                                });
                            } else {
                                firstBtn.setText(getResources().getString(R.string.camera));
                                secondBtn.setText(getResources().getString(R.string.album));
                                firstBtn.setOnClickListener(v -> {
                                    if (popupWindow != null) {
                                        imgId = tag;
                                        pickFromCamera();
                                        popupWindow.dismiss();
                                    }
                                });
                                secondBtn.setOnClickListener(v -> {
                                    if (popupWindow != null) {
                                        imgId = tag;
                                        pickFromAlbum();
                                        popupWindow.dismiss();
                                    }
                                });
                            }
                            cancleBtn.setOnClickListener(v -> {
                                if (popupWindow != null) {
                                    popupWindow.dismiss();
                                }
                            });
                            break;
                        default:
                            break;
                    }
                })
                .create();
        popupWindow.showAtLocation(findViewById(android.R.id.content), Gravity.BOTTOM, 0, 0);
    }

    /**
     * 拍照并剪裁
     */
    private void pickFromCamera() {
        /**
         * 设置图片保存路径(切换图片上传方式时需重新赋值，否则图片无法展示)
         * /HMMERCHANT/photo/xxx.jpg
         */
        imageUri = Utils.getImageCropUri("/" + Constants.SAVE_USER + "/" + Constants.PHOTO_DIR
                + System.currentTimeMillis() + Utils.getLimitRandomNum(1000, 9999) + Constants.IMG_TYPE);
        /**
         * 申请权限
         */
        rxPermissions
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        /**
                         * 拍照并裁剪
                         */
                        takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
                        //// 仅仅拍照不裁剪
                        //// takePhoto.onPickFromCapture(imageUri);
                    } else {
                        CustomToast.showToast(RegisterForCompanyActivity.this,
                                getResources().getString(R.string.permissionError),
                                Toast.LENGTH_SHORT);
                    }
                });
    }

    /**
     * 从相册中选取图片并剪裁
     */
    private void pickFromAlbum() {
        /**
         * 设置图片保存路径(切换图片上传方式时需重新赋值，否则图片无法展示)
         * /HMMERCHANT/photo/xxx.jpg
         */
        imageUri = Utils.getImageCropUri("/" + Constants.SAVE_USER + "/" + Constants.PHOTO_DIR
                + System.currentTimeMillis() + Utils.getLimitRandomNum(1000, 9999) + Constants.IMG_TYPE);
        /**
         * 申请权限
         */
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        /**
                         * 从相册中选取图片并裁剪
                         */
                        takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
                        //// 从相册中选取不裁剪
                        //// takePhoto.onPickFromGallery();
                    } else {
                        CustomToast.showToast(RegisterForCompanyActivity.this,
                                getResources().getString(R.string.permissionError),
                                Toast.LENGTH_SHORT);
                    }
                });
    }


    /**
     * 通过地址获取经纬度接口
     */
    private void getLonLatPort() {
        OkHttpUtils
                .get()
                .url(Constants.getLonLat)
                .addParams("address", etAddress.getText().toString().trim())
                .addParams("city", tvCity.getText().toString().trim())
                .addParams("output", Constants.DATA_TYPE)
                .addParams("key", Constants.AMAP_WEB_KEY)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Utils.LogE("AMAP ERROR:" + e);
                        storeCompanyRegisteredPort();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Utils.LogJson(response);
                        GetLonLat getLonLat = new GetLonLat();
                        getLonLat = Utils.parserJsonResult(response, GetLonLat.class);
                        if ("1".equals(getLonLat.getStatus())
                                && 0 < Integer.valueOf(getLonLat.getCount()).intValue()) {
                            String[] lonLat = getLonLat.getGeocodes().get(0)
                                    .getLocation().split(",");
                            if (2 == lonLat.length) {
                                sLon = lonLat[0];
                                sLat = lonLat[1];
                            }
                        }
                        storeCompanyRegisteredPort();
                    }
                });
    }


    /**
     * 拍照或从相册中选取图片成功
     *
     * @param result
     */
    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        switch (imgId) {
            case 1:
                imgIdCardPath = result.getImage().getOriginalPath();
                Utils.LogE("imgIdCardPath:" + imgIdCardPath);
                Glide.with(RegisterForCompanyActivity.this)
                        .load(imgIdCardPath)
                        .into(imgIdCard);
                break;
            case 2:
                imgLegalIdCardPath = result.getImage().getOriginalPath();
                Utils.LogE("imgLegalIdCardPath:" + imgLegalIdCardPath);
                Glide.with(RegisterForCompanyActivity.this)
                        .load(imgLegalIdCardPath)
                        .into(imgLegalIdCard);
                break;
            case 3:
                imgBussinessLicensePath = result.getImage().getOriginalPath();
                Utils.LogE("imgBussinessLicensePath:" + imgBussinessLicensePath);
                Glide.with(RegisterForCompanyActivity.this)
                        .load(imgBussinessLicensePath)
                        .into(imgBusinessLicenseCard);
                break;
            default:
        }


    }

    /**
     * 初始化OSS
     */
    private void initOSS() {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
                Constants.accessKeyId, Constants.accessKeySecret);
        ClientConfiguration conf = new ClientConfiguration();
        /**
         * 连接超时，默认15秒
         */
        conf.setConnectionTimeout(15 * 1000);
        /**
         * socket超时，默认15秒
         */
        conf.setSocketTimeout(15 * 1000);
        /**
         * 最大并发请求书，默认5个
         */
        conf.setMaxConcurrentRequest(5);
        /**
         * 失败后最大重试次数，默认2次
         */
        conf.setMaxErrorRetry(2);
        OSSLog.enableLog();
        oss = new OSSClient(getApplicationContext(), Constants.endpoint,
                credentialProvider, conf);
    }

    /**
     * 拍照或从相册中选取图片失败
     *
     * @param result
     * @param msg
     */
    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        CustomToast.showToast(RegisterForCompanyActivity.this,
                "Error:" + msg, Toast.LENGTH_SHORT);
    }

    /**
     * 用户取消拍照或从相册中选取图片
     */
    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    /**
     * 消息处理
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cityEventBus(CityEvent event) {
        Utils.LogE("cityName:" + event.getCityName() + " acId:" + event.getAcId());
        tvCity.setText(event.getCityName());
        acId = event.getAcId();
    }

    /**
     * 点击获取验证码执行的操作
     */
    private void doGetCaptcha() {
        /**
         * 验证手机号合法性
         */
        if (Utils.isValidPhoneNumber(etTel.getText().toString().trim())) {
            initCountDownTimer();
            getCaptchaPort();
        } else {
            CustomToast.showToast(RegisterForCompanyActivity.this,
                    getResources().getString(R.string.phoneNumError), Toast.LENGTH_LONG);
        }
    }

    /**
     * 初始化计时器相关配置
     */
    @SuppressLint("DefaultLocale")
    private void initCountDownTimer() {
        // 倒计时总时间为60S
        int millisInFuture = 60;
        // 将时间显示到界面
        btnCaptcha.setText(millisInFuture + "秒后重新获取");
        // 开始倒计时,传入总时间和每一秒要倒计时的时间
        countDown(millisInFuture, 1);
        // 开始执行
        mCountDownTimerUtil.start();
    }

    /**
     * 开始倒计时,传入总时间和每一秒要倒计时的时间
     */
    @SuppressLint("DefaultLocale")
    private void countDown(final long millisInFuture, long countDownInterval) {
        mCountDownTimerUtil = CountDownTimerUtil.getCountDownTimer()
                // 倒计时总时间
                .setMillisInFuture(millisInFuture * 1000)
                // 每隔多久回调一次onTick
                .setCountDownInterval(countDownInterval * 1000)
                // 每回调一次onTick执行
                .setTickDelegate(pMillisUntilFinished -> {
                    // 将时间显示到界面
                    btnCaptcha.setTextColor(getResources().getColor(R.color.graytext));
                    btnCaptcha.setBackgroundResource(R.drawable.shape_btn_gray);
                    btnCaptcha.setClickable(false);
                    btnCaptcha.setText(pMillisUntilFinished / 1000 + "秒后重新获取");
                })
                // 倒计时结束
                .setFinishDelegate(() -> {
                    btnCaptcha.setTextColor(getResources().getColor(R.color.white));
                    btnCaptcha.setBackgroundResource(R.drawable.shape_btn_blue);
                    btnCaptcha.setClickable(true);
                    btnCaptcha.setText("重新获取验证码");
                });
    }

    /**
     * 获取短信验证码接口
     */
    private void getCaptchaPort() {
        if (Utils.isNetworkAvailable(RegisterForCompanyActivity.this)) {
            CustomToast.showToast(RegisterForCompanyActivity.this,
                    getResources().getString(R.string.captchaTip), Toast.LENGTH_SHORT);
            OkHttpUtils
                    .post()
                    .url(Constants.getCaptcha)
                    .addParams("uTel", etTel.getText().toString().trim())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            /**
                             * 接口调用出错
                             */
                            CustomToast.showToast(RegisterForCompanyActivity.this,
                                    getResources().getString(R.string.portError), Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Utils.LogJson(response);
                            GetCaptcha getCaptcha = new GetCaptcha();
                            getCaptcha = Utils.parserJsonResult(response, GetCaptcha.class);
                            if (Constants.OK.equals(getCaptcha.getFlag())) {
                                captchaCode = getCaptcha.getData().getCAPTCHA();
                            }
                        }
                    });
        } else {
            CustomToast.showToast(RegisterForCompanyActivity.this,
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mCountDownTimerUtil) {
            mCountDownTimerUtil.cancel();
        }
        /**
         * 注销注册
         */
        EventBus.getDefault().unregister(this);
    }
}
