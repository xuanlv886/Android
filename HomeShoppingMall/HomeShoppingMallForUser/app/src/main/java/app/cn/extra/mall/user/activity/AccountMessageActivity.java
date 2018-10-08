package app.cn.extra.mall.user.activity;

import android.Manifest;
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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.GlideCircleTransformWithBorder;
import app.cn.extra.mall.user.utils.PhoneUtil;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.utils.osshelper.PutObjectSamples;
import app.cn.extra.mall.user.utils.widget.CustomDatePicker;
import app.cn.extra.mall.user.vo.GetUserInfo;
import app.cn.extra.mall.user.vo.UpdateUserAvatar;
import app.cn.extra.mall.user.vo.UpdateUserInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.ThreadPoolManager;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import cfkj.app.cn.cfkjcommonlib.view.CommonPopupWindow;
import okhttp3.Call;
import rx.Subscriber;

/**
 * 账户信息
 */
public class AccountMessageActivity extends TakePhotoActivity {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_set_sex)
    TextView tvSetSex;
    @BindView(R.id.et_set_email)
    EditText etSetEmail;
    @BindView(R.id.tv_set_birthday)
    TextView tvSetBirthday;
    @BindView(R.id.tv_set_collect_goods_address)
    TextView tvSetCollectGoodsAddress;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
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
     * 图片上传服务
     */
    private OSS oss;
    private SharePreferenceUtil sharePreferenceUtil;
    private CustomDatePicker customDatePicker1;
    /**
     * 生日
     */
    String sBirthday = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_message);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化配置项
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(AccountMessageActivity.this,
                Constants.SAVE_USER);
        rxPermissions = new RxPermissions(this);
        initTakePhoto();
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        getUserInfo();
    }

    /**
     * 获取账户信息
     */
    private void getUserInfo() {
        if (Utils.isNetworkAvailable(AccountMessageActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getUserInfo)
                    .addParams("uId", sharePreferenceUtil.getUID())
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
                            GetUserInfo getUserInfo = new GetUserInfo();
                            getUserInfo = Utils.parserJsonResult(response,
                                    GetUserInfo.class);
                            if (Constants.OK.equals(getUserInfo.getFlag())) {
                                RequestOptions requestOptions = new RequestOptions()
                                        .placeholder(R.mipmap.profile)
                                        .error(R.mipmap.profile)
                                        .fallback(R.mipmap.profile)
                                        .transform(new GlideCircleTransformWithBorder(AccountMessageActivity.this, 2,
                                                getResources().getColor(R.color.white)));
                                Glide.with(AccountMessageActivity.this)
                                        .load(getUserInfo.getData().getPicName() + "?x-oss-process=image/resize,h_150")
                                        .apply(requestOptions)
                                        .into(ivHead);
                                etName.setText(getUserInfo.getData().getUNickName());
                                tvSetBirthday.setText(getUserInfo.getData().getUBirthday());
                                etSetEmail.setText(getUserInfo.getData().getUEmail());
                                switch (getUserInfo.getData().getUSex()) {
                                    case 0:
                                        tvSetSex.setText(R.string.woman);
                                        uSex = "0";
                                        break;
                                    case 1:
                                        tvSetSex.setText(R.string.man);
                                        uSex = "1";
                                        break;
                                    default:
                                        break;
                                }
                                sBirthday = getUserInfo.getData().getUBirthday();
                            }
                            initDatePicker();
                        }
                    });
        } else {
            if (null != progressBar) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 修改个人信息
     */
    private void updateUserAvatar(String pName) {
        if (Utils.isNetworkAvailable(AccountMessageActivity.this)) {

            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.updateUserAvatar)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("pName", pName)
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
                            UpdateUserAvatar updateUserAvatar = new UpdateUserAvatar();
                            updateUserAvatar = Utils.parserJsonResult(response,
                                    UpdateUserAvatar.class);
                            if (Constants.OK.equals(updateUserAvatar.getFlag())) {
                            }
                        }
                    });
        }
    }

    /**
     * 修改个人信息
     *
     * @param uNickName
     * @param uSex
     * @param uEmail
     * @param uBirthday
     */
    private void updateUserInfo(String uNickName, String uSex, String uEmail, String uBirthday) {
        if (Utils.isNetworkAvailable(AccountMessageActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.updateUserInfo)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("uNickName", uNickName)
                    .addParams("uSex", uSex)
                    .addParams("uEmail", uEmail)
                    .addParams("uBirthday", uBirthday)
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
                            UpdateUserInfo updateUserInfo = new UpdateUserInfo();
                            updateUserInfo = Utils.parserJsonResult(response,
                                    UpdateUserInfo.class);
                            if (Constants.OK.equals(updateUserInfo.getFlag())) {
                                if (Constants.OK.equals(updateUserInfo.getData().getStatus())) {
                                    if (!TextUtils.isEmpty(imgIdCardPath)) {
                                        updateUserAvatar(Utils.getFileName(imgIdCardPath)
                                                + Constants.IMG_TYPE);
                                        imUpdateUserInfo("pic", Constants.PROFILE_PIC
                                                + Utils.getFileName(imgIdCardPath)
                                                + Constants.IMG_TYPE, uNickName, uEmail, sBirthday);
                                    } else {
                                        imUpdateUserInfo("", "", uNickName, uEmail, sBirthday);
                                    }
                                } else {
                                    CustomToast.showToast(AccountMessageActivity.this,
                                            updateUserInfo.getData().getErrorString(),
                                            Toast.LENGTH_SHORT);
                                }
                            } else {
                                CustomToast.showToast(AccountMessageActivity.this,
                                        updateUserInfo.getErrorString(),
                                        Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            CustomToast.showToast(getApplicationContext(), "修改失败！" + Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);
            if (null != progressBar) {
                progressBar.setVisibility(View.GONE);
            }
        }
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
         * 设置裁剪参数 1:1
         */
        cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1)
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
            String objectName = Constants.PROFILE_PIC + Utils.getFileName(
                    imgIdCardPath) + Constants.IMG_TYPE;
            ThreadPoolManager.getInstance().execute(() -> new PutObjectSamples(oss,
                    Constants.bucketName, objectName, imgIdCardPath).asyncPutObjectFromLocalFile());
//            updateUserAvatar(Utils.getFileName(imgIdCardPath) + Constants.IMG_TYPE);
//            imUpdateUserInfo("pic", objectName, "", "", "");
        }
    }

    /**
     * 弹出底部选择窗口
     *
     * @param tag 0--选择性别，1--选择图片上传方式
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
                                        tvSetSex.setText(getResources().getString(R.string.man));
                                        uSex = "1";
                                        popupWindow.dismiss();
                                    }
                                });
                                secondBtn.setOnClickListener(v -> {
                                    if (popupWindow != null) {
                                        tvSetSex.setText(getResources().getString(R.string.woman));
                                        uSex = "0";
                                        popupWindow.dismiss();
                                    }
                                });
                            } else if (1 == tag) {
                                firstBtn.setText(getResources().getString(R.string.camera));
                                secondBtn.setText(getResources().getString(R.string.album));
                                firstBtn.setOnClickListener(v -> {
                                    if (popupWindow != null) {
                                        pickFromCamera();
                                        popupWindow.dismiss();
                                    }
                                });
                                secondBtn.setOnClickListener(v -> {
                                    if (popupWindow != null) {
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
        imageUri = Utils.getImageCropUri("/" + Constants.SAVE_USER + "/photo/"
                + System.currentTimeMillis() + Utils.getLimitRandomNum(1000, 9999) + ".jpg");
        /**
         * 申请权限
         */
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    /**
                     * 拍照并裁剪
                     */
                    takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
                    //// 仅仅拍照不裁剪
//                    takePhoto.onPickFromCapture(imageUri);
                } else {
                    CustomToast.showToast(AccountMessageActivity.this,
                            getResources().getString(R.string.permissionError),
                            Toast.LENGTH_SHORT);
                }
            }
        });
//        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
//            if (granted) {
//                /**
//                 * 拍照并裁剪
//                 */
//                takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
//                //// 仅仅拍照不裁剪
//                //// takePhoto.onPickFromCapture(imageUri);
//            } else {
//                CustomToast.showToast(AccountMessageActivity.this,
//                        getResources().getString(R.string.permissionError),
//                        Toast.LENGTH_SHORT);
//            }
//        });
    }

    /**
     * 从相册中选取图片并剪裁
     */
    private void pickFromAlbum() {
        /**
         * 设置图片保存路径(切换图片上传方式时需重新赋值，否则图片无法展示)
         * /HMMERCHANT/photo/xxx.jpg
         */
        imageUri = Utils.getImageCropUri("/" + Constants.SAVE_USER + "/photo/"
                + System.currentTimeMillis() + Utils.getLimitRandomNum(1000, 9999) + ".jpg");
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
                        CustomToast.showToast(AccountMessageActivity.this,
                                getResources().getString(R.string.permissionError),
                                Toast.LENGTH_SHORT);
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
        imgIdCardPath = result.getImage().getOriginalPath();
        Utils.LogE("imgIdCardPath:" + imgIdCardPath);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.profile)
                .error(R.mipmap.profile)
                .fallback(R.mipmap.profile)
                .transform(new GlideCircleTransformWithBorder(AccountMessageActivity.this, 2,
                        getResources().getColor(R.color.white)));
        Glide.with(AccountMessageActivity.this).load(imgIdCardPath)
                .apply(requestOptions)
                .into(ivHead);
        uploadImgToOSS();
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
        CustomToast.showToast(AccountMessageActivity.this,
                "Error:" + msg, Toast.LENGTH_SHORT);
    }

    /**
     * 用户取消拍照或从相册中选取图片
     */
    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @OnClick({R.id.img_back, R.id.iv_head, R.id.rl_sex, R.id.rl_birthday, R.id.tv_revise, R.id.rl_collect_goods_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.iv_head:
                PhoneUtil.hideInput(AccountMessageActivity.this, view);
                showCommonBottomDialog(1);
                break;
            case R.id.rl_sex:
                PhoneUtil.hideInput(AccountMessageActivity.this, view);
                showCommonBottomDialog(0);
                break;
            case R.id.rl_birthday:
                // 日期格式为yyyy-MM-dd
                customDatePicker1.show(tvSetBirthday.getText().toString());
                break;
            case R.id.tv_revise:
                String uNickName = etName.getText().toString().trim();
                String uEmail = etSetEmail.getText().toString().trim();
                sBirthday = tvSetBirthday.getText().toString().trim();
                if (TextUtils.isEmpty(uNickName)) {
                    CustomToast.showToast(AccountMessageActivity.this, "请输入昵称！", Toast.LENGTH_SHORT);
                    break;
                }
                if (!TextUtils.isEmpty(uEmail)) {
                    if (!PhoneUtil.isEmail(uEmail)) {
                        CustomToast.showToast(AccountMessageActivity.this, "邮箱格式不正确！", Toast.LENGTH_SHORT);
                        break;
                    }
                }
                updateUserInfo(uNickName, uSex, uEmail, sBirthday);
                break;
            case R.id.rl_collect_goods_address:
                Intent intent = new Intent(AccountMessageActivity.this, AddressActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
        if (TextUtils.isEmpty(sBirthday)) {
            tvSetBirthday.setText(now.split(" ")[0]);
        } else {
            tvSetBirthday.setText(sBirthday);
        }
        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvSetBirthday.setText(time.split(" ")[0]);
            }
        }, "1950-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker1.showSpecificTime(false); // 不显示时和分
        customDatePicker1.setIsLoop(false); // 不允许循环滚动
    }

    private void imUpdateUserInfo(String tag, String value, String Name, String EMAIL, String BIRTHDAY) {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>();
        if ("pic".equals(tag)) {
            fields.put(UserInfoFieldEnum.AVATAR, Constants.PICS_URL + value);
        } else {
            fields.put(UserInfoFieldEnum.Name, Name);
            fields.put(UserInfoFieldEnum.EMAIL, EMAIL);
            fields.put(UserInfoFieldEnum.BIRTHDAY, BIRTHDAY);
        }
        NIMClient.getService(UserService.class).updateUserInfo(fields)
                .setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int i, Void aVoid, Throwable throwable) {
                        CustomToast.showToast(AccountMessageActivity.this,
                                "修改成功！",
                                Toast.LENGTH_SHORT);
                        finish();
                    }
                });
    }
}
