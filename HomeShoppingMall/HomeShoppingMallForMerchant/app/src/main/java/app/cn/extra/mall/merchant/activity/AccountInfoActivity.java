package app.cn.extra.mall.merchant.activity;

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
import android.widget.ImageView;
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
import com.bumptech.glide.request.RequestOptions;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.SingleInfoEvent;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.GlideCircleTransformWithBorder;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.utils.osshelper.PutObjectSamples;
import app.cn.extra.mall.merchant.vo.CommonVo;
import app.cn.extra.mall.merchant.vo.GetStoreUserInfo;
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

/**
 * Description 账号信息页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class AccountInfoActivity extends TakePhotoActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.img_header)
    ImageView imgHeader;
    @BindView(R.id.tv_sLeader)
    TextView tvSLeader;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.rl_sex)
    RelativeLayout rlSex;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.rl_email)
    RelativeLayout rlEmail;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.rl_birthday)
    RelativeLayout rlBirthday;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.rl_tel)
    RelativeLayout rlTel;
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
     * 临时存储商户头像图片路径
     */
    private String imgHeaderPath = "";
    /**
     * 图片上传服务
     */
    private OSS oss;
    private SharePreferenceUtil sharePreferenceUtil;
    /**
     * 临时存储商户账号信息
     */
    private GetStoreUserInfo getStoreUserInfo = null;
    /**
     * 临时存储商户头像图片名称
     */
    private String pName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化配置项
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(AccountInfoActivity.this,
                Constants.SAVE_USER);
        rxPermissions = new RxPermissions(AccountInfoActivity.this);
        /**
         * 注册订阅者
         */
        EventBus.getDefault().register(this);
        initTakePhoto();
        getStoreUserInfoPort();
    }

    /**
     * 获取商户个人、店铺相关信息接口
     */
    private void getStoreUserInfoPort() {
        if (Utils.isNetworkAvailable(AccountInfoActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getStoreUserInfo)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            CustomToast.showToast(AccountInfoActivity.this,
                                    getResources().getString(
                                            R.string.portError), Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Utils.LogJson(response);
                            getStoreUserInfo = new GetStoreUserInfo();
                            getStoreUserInfo = Utils.parserJsonResult(response, GetStoreUserInfo.class);
                            if (Constants.OK.equals(getStoreUserInfo.getFlag())) {
                                if (Constants.OK.equals(getStoreUserInfo.getData().getStatus())) {
                                    /**
                                     * 填充数据
                                     */
                                    if (!TextUtils.isEmpty(getStoreUserInfo.getData().getProfile())) {
                                        RequestOptions requestOptions = new RequestOptions()
                                                .placeholder(R.mipmap.profile)
                                                .error(R.mipmap.profile)
                                                .fallback(R.mipmap.profile)
                                                .transform(new GlideCircleTransformWithBorder(AccountInfoActivity.this, 2,
                                                        getResources().getColor(R.color.white)));
                                        Glide.with(AccountInfoActivity.this).load(getStoreUserInfo.getData().getProfile()+"?x-oss-process=image/resize,h_150")
                                                .apply(requestOptions)
                                                .into(imgHeader);
                                        pName = Utils.getFileName(getStoreUserInfo.getData()
                                                .getProfile()) + Constants.IMG_TYPE;
                                    }
                                    if (!TextUtils.isEmpty(getStoreUserInfo.getData().getUNickName())) {
                                        tvNickName.setTextColor(getResources().getColor(
                                                R.color.black));
                                        tvNickName.setText(getStoreUserInfo.getData().getUNickName());
                                    } else {
                                        tvNickName.setText(getResources().getString(
                                                R.string.unSetting));
                                        tvNickName.setTextColor(getResources().getColor(
                                                R.color.graytext));
                                    }

                                    tvSLeader.setText(getStoreUserInfo.getData().getSLeader());
                                    uSex = "0";
                                    if (uSex.equals(getStoreUserInfo.getData().getUSex() + "")) {
                                        tvSex.setText(getResources().getString(R.string.woman));
                                    } else {
                                        uSex = "1";
                                        tvSex.setText(getResources().getString(R.string.man));
                                    }
                                    if (!TextUtils.isEmpty(getStoreUserInfo.getData().getUEmail())) {
                                        tvEmail.setTextColor(getResources().getColor(
                                                R.color.black));
                                        tvEmail.setText(getStoreUserInfo.getData().getUEmail());
                                    } else {
                                        tvEmail.setText(getResources().getString(
                                                R.string.unSetting));
                                        tvEmail.setTextColor(getResources().getColor(
                                                R.color.graytext));
                                    }
                                    if (!TextUtils.isEmpty(getStoreUserInfo.getData().getUBirthday())) {
                                        tvBirthday.setTextColor(getResources().getColor(
                                                R.color.black));
                                        tvBirthday.setText(getStoreUserInfo.getData().getUBirthday());
                                    } else {
                                        tvBirthday.setText(getResources().getString(
                                                R.string.unSetting));
                                        tvBirthday.setTextColor(getResources().getColor(
                                                R.color.graytext));
                                    }
                                    tvTel.setText(getStoreUserInfo.getData().getUTel());
                                } else {
                                    CustomToast.showToast(AccountInfoActivity.this,
                                            getStoreUserInfo.getData()
                                                    .getErrorString(), Toast.LENGTH_SHORT);
                                }
                            } else {
                                CustomToast.showToast(AccountInfoActivity.this,
                                        getStoreUserInfo.getErrorString(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            CustomToast.showToast(AccountInfoActivity.this,
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);
        }
    }

    /**
     * 修改商户个人、店铺相关信息接口
     */
    private void changeStoreUserInfoPort() {
        if (Utils.isNetworkAvailable(AccountInfoActivity.this)) {
            String uEmail = tvEmail.getText().toString().trim();
            String uBirthday = tvBirthday.getText().toString().trim();
            if (TextUtils.isEmpty(uEmail)
                    || getResources().getString(R.string.unSetting).equals(uEmail)) {
                uEmail = "";
            }
            if (TextUtils.isEmpty(uBirthday)
                    || getResources().getString(R.string.unSetting).equals(uBirthday)) {
                uBirthday = "";
            }
            OkHttpUtils
                    .post()
                    .url(Constants.changeStoreUserInfo)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
                    .addParams("which", "0")
                    .addParams("uNickName", tvNickName.getText().toString())
                    .addParams("pTag", getStoreUserInfo.getData().getPTag())
                    .addParams("pName", pName)
                    .addParams("uSex", uSex)
                    .addParams("uEmail", uEmail)
                    .addParams("uBirthday", uBirthday)
                    .addParams("uTel", tvTel.getText().toString().trim())
                    .addParams("sName", "")
                    .addParams("sDescribe", "")
                    .addParams("sTel", "")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            CustomToast.showToast(AccountInfoActivity.this,
                                    getResources().getString(
                                            R.string.portError), Toast.LENGTH_SHORT);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Utils.LogJson(response);
                            CommonVo commonVo = new CommonVo();
                            commonVo = Utils.parserJsonResult(response, CommonVo.class);
                            if (Constants.OK.equals(commonVo.getFlag())) {
                                if (Constants.OK.equals(commonVo.getData().getStatus())) {
//                                    CustomToast.showToast(AccountInfoActivity.this,
//                                            getResources().getString(R.string.updateSuccess),
//                                            Toast.LENGTH_SHORT);
                                } else {
                                    CustomToast.showToast(AccountInfoActivity.this,
                                            commonVo.getData()
                                                    .getErrorString(), Toast.LENGTH_SHORT);
                                }
                            } else {
                                CustomToast.showToast(AccountInfoActivity.this,
                                        commonVo.getErrorString(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            CustomToast.showToast(AccountInfoActivity.this,
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);
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

    @OnClick({R.id.img_back, R.id.tv_nick_name, R.id.img_header, R.id.rl_sex, R.id.rl_email, R.id.rl_birthday, R.id.rl_tel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_nick_name:
                gotoChangeSingleInfoActivity("uNickName", tvNickName.getText().toString().trim());
                break;
            case R.id.img_header:
                showCommonBottomDialog(1);
                break;
            case R.id.rl_sex:
                showCommonBottomDialog(0);
                break;
            case R.id.rl_email:
                String uEmail = tvEmail.getText().toString().trim();
                if (TextUtils.isEmpty(uEmail)
                        || getResources().getString(R.string.unSetting).equals(uEmail)) {
                    uEmail = "";
                }
                gotoChangeSingleInfoActivity("uEmail", uEmail);
                break;
            case R.id.rl_birthday:
                String uBirthday = tvBirthday.getText().toString().trim();
                if (TextUtils.isEmpty(uBirthday)
                        || getResources().getString(R.string.unSetting).equals(uBirthday)) {
                    uBirthday = "";
                }
                gotoChangeSingleInfoActivity("uBirthday", uBirthday);
                break;
            case R.id.rl_tel:
                gotoChangeSingleInfoActivity("uTel", tvTel.getText().toString().trim());
                break;
            default:
        }
    }

    /**
     * 跳转至修改单条信息页
     *
     * @param tag     标识要修改的单条信息的内容
     * @param content 原内容
     */
    private void gotoChangeSingleInfoActivity(String tag, String content) {
        Intent intent = new Intent();
        intent.setClass(AccountInfoActivity.this, ChangeSingleInfoActivity.class);
        intent.putExtra("tag", tag);
        intent.putExtra("content", content);
        startActivity(intent);
    }

    /**
     * 消息处理
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SingleInfoEventBus(SingleInfoEvent event) {
        if (!TextUtils.isEmpty(event.getuNickName())) {
            tvNickName.setText(event.getuNickName());
        }
        if (!TextUtils.isEmpty(event.getuEmail())) {
            tvEmail.setTextColor(getResources().getColor(
                    R.color.black));
            tvEmail.setText(event.getuEmail());
        }
        if (!TextUtils.isEmpty(event.getuBirthday())) {
            tvBirthday.setTextColor(getResources().getColor(
                    R.color.black));
            tvBirthday.setText(event.getuBirthday());
        }
        if (!TextUtils.isEmpty(event.getuTel())) {
            tvTel.setText(event.getuTel());
        }
        changeStoreUserInfoPort();
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
                                        tvSex.setText(getResources().getString(R.string.man));
                                        uSex = "1";
                                        popupWindow.dismiss();
                                        changeStoreUserInfoPort();
                                    }
                                });
                                secondBtn.setOnClickListener(v -> {
                                    if (popupWindow != null) {
                                        tvSex.setText(getResources().getString(R.string.woman));
                                        uSex = "0";
                                        popupWindow.dismiss();
                                        changeStoreUserInfoPort();
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
                        CustomToast.showToast(AccountInfoActivity.this,
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
                        CustomToast.showToast(AccountInfoActivity.this,
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
        imgHeaderPath = result.getImage().getOriginalPath();
        Utils.LogE("imgHeaderPath:" + imgHeaderPath);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.profile)
                .error(R.mipmap.profile)
                .fallback(R.mipmap.profile)
                .transform(new GlideCircleTransformWithBorder(AccountInfoActivity.this, 2,
                        getResources().getColor(R.color.white)));
        Glide.with(AccountInfoActivity.this).load(imgHeaderPath)
                .apply(requestOptions)
                .into(imgHeader);
        pName = Utils.getFileName(imgHeaderPath) + Constants.IMG_TYPE;
        uploadImgToOSS();
        changeStoreUserInfoPort();
    }

    /**
     * 上传图片至OSS
     */
    private void uploadImgToOSS() {
        /**
         * 判断是否有图片 有则先将图片上传至阿里云OSS
         */
        if (!TextUtils.isEmpty(imgHeaderPath)) {
            initOSS();
            /**
             * 图片在OSS上的路径
             */
            String objectName = Constants.PROFILE_PIC + Utils.getFileName(
                    imgHeaderPath) + Constants.IMG_TYPE;
            ThreadPoolManager.getInstance().execute(() -> new PutObjectSamples(oss,
                    Constants.bucketName, objectName, imgHeaderPath).asyncPutObjectFromLocalFile());
            imUpdateUserInfo("pic", Constants.PIC_BASE_URL + objectName);
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
        CustomToast.showToast(AccountInfoActivity.this,
                "Error:" + msg, Toast.LENGTH_SHORT);
    }

    /**
     * 用户取消拍照或从相册中选取图片
     */
    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 注销注册
         */
        EventBus.getDefault().unregister(this);
    }

    private void imUpdateUserInfo(String tag, String value) {
        Map<UserInfoFieldEnum, Object> fields = new HashMap<>(1);
        if ("uNickName".equals(tag)) {
            fields.put(UserInfoFieldEnum.Name, value);
        } else if ("uEmail".equals(tag)) {
            fields.put(UserInfoFieldEnum.EMAIL, value);
        } else if ("uBirthday".equals(tag)) {
            fields.put(UserInfoFieldEnum.BIRTHDAY, value);
        } else if ("uTel".equals(tag)) {
            fields.put(UserInfoFieldEnum.MOBILE, value);
        } else if ("pic".equals(tag)) {
            fields.put(UserInfoFieldEnum.AVATAR, value);
        }
        NIMClient.getService(UserService.class).updateUserInfo(fields)
                .setCallback(new RequestCallbackWrapper<Void>() {
                    @Override
                    public void onResult(int i, Void aVoid, Throwable throwable) {

                    }
                });
    }
}
