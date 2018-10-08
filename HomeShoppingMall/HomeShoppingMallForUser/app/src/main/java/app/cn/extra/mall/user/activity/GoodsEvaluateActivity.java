package app.cn.extra.mall.user.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.google.gson.Gson;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TResult;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.utils.osshelper.PutObjectSamples;
import app.cn.extra.mall.user.vo.DoAddEvaluate;
import app.cn.extra.mall.user.vo.GetProductOrderDetail;
import app.cn.extra.mall.user.vo.GoodsEvaluate;
import app.cn.extra.mall.user.vo.Pic;
import app.cn.extra.mall.user.vo.PicsBean;
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
 * 发表商品评价
 */
public class GoodsEvaluateActivity extends TakePhotoActivity {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.btn_evaluate)
    Button btnEvaluate;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    /**
     * 商品订单主键标识
     */
    private String poId = "";
    /**
     * 商品详情
     */
    private GetProductOrderDetail getProductOrderDetail;
    private SharePreferenceUtil sharePreferenceUtil;

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
     * 图片上传服务
     */
    private OSS oss;
    private CommonAdapter<GetProductOrderDetail.DataBean.OrderDetailsBean> adapter;
    private CommonAdapter<Pic> picAdapter;
    private ArrayList<PicsBean> datas = new ArrayList<>();
    /**
     * 点击的图片的位置
     */
    private int pos = 0;
    private int mMaxPhotoNum = 8;
    /**
     * 图片保存路径
     */
    private Uri imageUri;
    /**
     * 评价等级
     */
    private List<Integer> evaluateLevel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_goods);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        poId = intent.getStringExtra("poId");
        Bundle bundle = intent.getExtras();
        getProductOrderDetail = (GetProductOrderDetail) bundle.getSerializable("getProductOrderDetail");
        initView();
    }

    private void initView() {
        initRecyclerView();
        initOSS();
        rxPermissions = new RxPermissions(this);
        initTakePhoto();
        sharePreferenceUtil = new SharePreferenceUtil(GoodsEvaluateActivity.this,
                Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        initData();

    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        /**
         * 线性布局
         */
        LinearLayoutManager layoutManage = new LinearLayoutManager(GoodsEvaluateActivity.this);
        recyclerView.setLayoutManager(layoutManage);
        /**
         * 添加分隔线
         */
//        recyclerView.addItemDecoration(new DividerItemDecoration(GoodsEvaluateActivity.this,
//                DividerItemDecoration.VERTICAL));
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
        conf.setMaxConcurrentRequest(9);
        /**
         * 失败后最大重试次数，默认2次
         */
        conf.setMaxErrorRetry(2);
        OSSLog.enableLog();
        oss = new OSSClient(getApplicationContext(), Constants.endpoint,
                credentialProvider, conf);
    }

    /**
     * 初始化界面元素
     */
    private void initData() {
        for (int i = 0; i < getProductOrderDetail.getData().getOrderDetails().size(); i++) {
            PicsBean bean = new PicsBean();
            List<Pic> temp = new ArrayList<Pic>();
            Pic pic = new Pic();
            pic.setLocalPicName("null");
            pic.setUploadPicName("null");
            temp.add(pic);
            bean.setPicList(temp);
            datas.add(bean);
        }
        adapter = new CommonAdapter<GetProductOrderDetail.DataBean.OrderDetailsBean>(
                GoodsEvaluateActivity.this,
                R.layout.list_item_evaluate_goods,
                getProductOrderDetail.getData().getOrderDetails()) {
            @Override
            protected void convert(ViewHolder holder, GetProductOrderDetail.DataBean
                    .OrderDetailsBean orderDetailsBean, int position) {
                RequestOptions requestOptions = new RequestOptions()
                        .error(R.drawable.ic_exception)
                        .fallback(R.drawable.ic_exception);
                Glide.with(GoodsEvaluateActivity.this)
                        .load(orderDetailsBean.getPicName())
                        .apply(requestOptions)
                        .into((ImageView) holder.getView(R.id.img_pic));
                /**
                 * 默认好评
                 */
                if (evaluateLevel.size() != getProductOrderDetail.getData().getOrderDetails().size()) {
                    goodEvaluate(holder, position, 0);
                }
                /**
                 * 评价等级按钮点击
                 */
                holder.setOnClickListener(R.id.rl_bad, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        badEvaluate(holder, position);
                    }
                });
                holder.setOnClickListener(R.id.rl_normal, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        normalEvaluate(holder, position);
                    }
                });
                holder.setOnClickListener(R.id.rl_good, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goodEvaluate(holder, position, 1);
                    }
                });
                /**
                 * 图片
                 */
                RecyclerView pics = holder.getView(R.id.pics);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(GoodsEvaluateActivity.this,
                        4);
                pics.setLayoutManager(gridLayoutManager);
                picAdapter = new CommonAdapter<Pic>(GoodsEvaluateActivity.this,
                        R.layout.grid_item_pic, datas.get(position).getPicList()) {
                    @Override
                    protected void convert(ViewHolder holder, Pic pic, int p) {
                        RequestOptions requestOptions = new RequestOptions()
                                .error(R.mipmap.button_shangchuantupian_nor)
                                .fallback(R.mipmap.button_shangchuantupian_nor);
                        Glide.with(GoodsEvaluateActivity.this)
                                .load(pic.getLocalPicName())
                                .apply(requestOptions)
                                .into((ImageView) holder.getView(R.id.iv_take_photo));
                        if ("null".equals(pic.getLocalPicName())) {
                            holder.setVisible(R.id.iv_delete, false);
                        } else {
                            holder.setVisible(R.id.iv_delete, true);
                        }
                        holder.setOnClickListener(R.id.iv_delete, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                pos = position;
                                showRemoveDialog(p);
                            }
                        });
                    }
                };
                pics.setAdapter(picAdapter);
                picAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int p) {
                        pos = position;
                        if (datas.get(position).getPicList().size() < mMaxPhotoNum
                                && 0 < datas.get(position).getPicList().size()
                                && p == datas.get(position).getPicList().size() - 1
                                && datas.get(position).getPicList().get(datas.get(position).getPicList()
                                .size() - 1).getLocalPicName().equals("null")) {
                            showCommonBottomDialog();
                        }
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
    }

    /**
     * 删除已选择的图片
     *
     * @param position 选择的图片位置
     */
    private void showRemoveDialog(final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_remove_stutas, null);
        final Dialog alertDialog = new AlertDialog.Builder(this, R.style.Theme_Transparent).
                setView(view).
                create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_live_yes = view.findViewById(R.id.tv_live_yes);
        TextView tv__live_never_reminds = view.findViewById(R.id.tv__live_never_reminds);
        tv__live_never_reminds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        tv_live_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (datas.get(pos).getPicList() != null) {
                    datas.get(pos).getPicList().remove(position);
                }
                adapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });

    }

    /**
     * 弹出底部选择窗口
     */
    private void showCommonBottomDialog() {
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


    @OnClick({R.id.img_back, R.id.btn_evaluate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_evaluate:
                setSaveBtnClickable(false);
                List<String> strings = new ArrayList<>();
                GoodsEvaluate goodsEvaluate = new GoodsEvaluate();
                GoodsEvaluate.EvaluateDataBean evaluateDataBean = null;
                GoodsEvaluate.EvaluateDataBean.PicListBean picListBean = null;
                List<GoodsEvaluate.EvaluateDataBean> evaluateDataBeanList = new ArrayList<>();
                List<GoodsEvaluate.EvaluateDataBean.PicListBean> picListBeanList = null;
                for (int i = 0; i < getProductOrderDetail.getData().getOrderDetails().size(); i++) {
                    if (datas.get(i).getPicList().size() < 9) {
                        datas.get(i).getPicList().remove(datas.get(i).getPicList().size() - 1);
                    }
                    strings.add(((EditText) recyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.et_evaluate)).getText().toString().trim());
                    evaluateDataBean = new GoodsEvaluate.EvaluateDataBean();
                    evaluateDataBean.setPodId(getProductOrderDetail.getData().getOrderDetails().get(i).getPodId());
                    evaluateDataBean.setPeContent(strings.get(i));
                    evaluateDataBean.setPeLevel("" + evaluateLevel.get(i));
                    evaluateDataBean.setPId(getProductOrderDetail.getData().getOrderDetails().get(i).getPId());
                    picListBeanList = new ArrayList<>();
                    for (int j = 0; j < datas.get(i).getPicList().size(); j++) {
                        picListBean = new GoodsEvaluate.EvaluateDataBean.PicListBean();
                        picListBean.setPNo("" + j);
                        picListBean.setPName(datas.get(i).getPicList().get(j).getUploadPicName());
                        picListBeanList.add(picListBean);
                    }
                    evaluateDataBean.setPicList(picListBeanList);
                    evaluateDataBeanList.add(evaluateDataBean);
                }
                goodsEvaluate.setEvaluateData(evaluateDataBeanList);
                Gson gsons = new Gson();
                String evaluateData = gsons.toJson(goodsEvaluate);
                for (int i = 0; i < goodsEvaluate.getEvaluateData().size(); i++) {
                    if (TextUtils.isEmpty(goodsEvaluate.getEvaluateData().get(i).getPeContent())) {
                        Toast.makeText(getApplicationContext(), "请输入评价内容!", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                addProductEvaluate(sharePreferenceUtil.getUID(), poId, evaluateData);
                break;
            default:
                break;
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
         * 设置裁剪参数
         * 身份证为1.6:1  故剪裁参数为16：10
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
//                     takePhoto.onPickFromCapture(imageUri);
                } else {
                    CustomToast.showToast(GoodsEvaluateActivity.this,
                            getResources().getString(R.string.permissionError),
                            Toast.LENGTH_SHORT);
                }
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
        imageUri = Utils.getImageCropUri("/" + Constants.SAVE_USER + "/photo/"
                + System.currentTimeMillis() + Utils.getLimitRandomNum(1000, 9999) + ".jpg");
        /**
         * 申请权限
         */
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
//                        /**
//                         * 从相册中选取多张图片并裁剪
//                         */
//                        takePhoto.onPickMultiple(mMaxPhotoNum - datas.get(pos).getPicList().size());
                        // 从相册中选取不裁剪
                        takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
                    } else {
                        CustomToast.showToast(GoodsEvaluateActivity.this,
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
        //当前点击item的图片list
//        List<String> temp = datas.get(pos).getPicList();
        /**
         * 更新图片列表数据
         */
        for (int i = 0; i < result.getImages().size(); i++) {
            //商品评价图片路径
            String imgIdCardPath = result.getImages().get(i).getOriginalPath();
            Utils.LogE("imgIdCardPath:" + imgIdCardPath);
//            temp.add(imgIdCardPath);
            Pic pic = new Pic();
            pic.setUploadPicName(Utils.getFileName(imgIdCardPath) + Constants.IMG_TYPE);
            pic.setLocalPicName(imgIdCardPath);
            datas.get(pos).getPicList().add(datas.get(pos).getPicList().size() - 1, pic);
            uploadImgToOSS(imgIdCardPath);
        }
        /**
         * 根据当前图片list中的条数，判断是否显示添加图片
         */
        if (datas.get(pos).getPicList().size() > mMaxPhotoNum) {
            for (int i = 0; i < datas.get(pos).getPicList().size(); i++) {
                if ("null".equals(datas.get(pos).getPicList().get(i).getLocalPicName())) {
                    datas.get(pos).getPicList().remove(i);
                }
            }
//            temp.add("null");
//            datas.get(pos).setPicList(temp);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 上传图片至OSS
     */
    private void uploadImgToOSS(String imgIdCardPath) {
        /**
         * 判断是否有图片 有则先将图片上传至阿里云OSS
         */
        if (!TextUtils.isEmpty(imgIdCardPath)) {
            /**
             * 图片在OSS上的路径
             */
            String objectName = Constants.EVALUATE_PIC + Utils.getFileName(
                    imgIdCardPath) + Constants.IMG_TYPE;
            ThreadPoolManager.getInstance().execute(() -> new PutObjectSamples(oss,
                    Constants.bucketName, objectName, imgIdCardPath).asyncPutObjectFromLocalFile());
        }
    }

    /**
     * 发布评价
     */
    private void addProductEvaluate(String uId, String poId, String evaluateData) {
        if (Utils.isNetworkAvailable(GoodsEvaluateActivity.this)) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.addProductEvaluate)
                    .addParams("uId", uId)
                    .addParams("poId", poId)
                    .addParams("evaluateData", evaluateData)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            setSaveBtnClickable(true);
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            setSaveBtnClickable(true);
                            DoAddEvaluate doAddEvaluate = new DoAddEvaluate();
                            doAddEvaluate = Utils.parserJsonResult(response, DoAddEvaluate.class);
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            if ("true".equals(doAddEvaluate.getFlag())) {
                                if (Constants.OK.equals(doAddEvaluate.getData().getStatus())) {
                                    Intent intent = new Intent();
                                    intent.setClass(GoodsEvaluateActivity.this, EvaluateResultActivity.class);
                                    intent.putExtra("TITLE", "评价成功");
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(GoodsEvaluateActivity.this, doAddEvaluate.getErrorString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            setSaveBtnClickable(true);
        }
    }

    /**
     * 设置发布评价按钮是否可以点击与显示效果
     *
     * @param b b=true可以点击；b=false不可点击
     */
    private void setSaveBtnClickable(boolean b) {
        if (b) {
            btnEvaluate.setBackgroundResource(R.color.blue);
            btnEvaluate.setTextColor(getResources().getColor(R.color.white));
            btnEvaluate.setEnabled(true);
        } else {
            btnEvaluate.setBackgroundResource(R.color.gray);
            btnEvaluate.setTextColor(getResources().getColor(R.color.white));
            btnEvaluate.setEnabled(false);
        }
    }

    /**
     * 中评
     */
    private void normalEvaluate(ViewHolder holder, int position) {
        if (evaluateLevel.size() > position) {
            evaluateLevel.set(position, 1);
        } else {
            for (int i = 0; i < position; i++) {
                if (evaluateLevel.size() < position + 1) {
                    if (i == position - 1) {
                        evaluateLevel.add(1);
                    } else {
                        evaluateLevel.add(2);
                    }
                }
            }
        }
        holder.setTextColor(R.id.tv_good, Color.parseColor("#333333"));
        holder.setImageResource(R.id.img_good, R.mipmap.icon_pingjia);
        holder.setTextColor(R.id.tv_normal, Color.parseColor("#FFD801"));
        holder.setImageResource(R.id.img_normal, R.mipmap.icon_zhongping_sel);
        holder.setTextColor(R.id.tv_bad, Color.parseColor("#333333"));
        holder.setImageResource(R.id.img_bad, R.mipmap.icon_chaping);
    }

    /**
     * 好评
     */
    private void goodEvaluate(ViewHolder holder, int position, int flag) {
        /**
         * flag=0 默认；falg=1 点击
         */
        if (flag == 0) {
            evaluateLevel.add(2);
        } else if (flag == 1) {
            if (evaluateLevel.size() > position) {
                evaluateLevel.set(position, 2);
            } else {
                for (int i = 0; i < position; i++) {
                    if (evaluateLevel.size() < position + 1) {
                        if (i == position - 1) {
                            evaluateLevel.add(2);
                        } else {
                            evaluateLevel.add(2);
                        }
                    }
                }
            }
        }
        holder.setTextColor(R.id.tv_good, Color.parseColor("#00D8C9"));
        holder.setImageResource(R.id.img_good, R.mipmap.icon_haoping_sel);
        holder.setTextColor(R.id.tv_normal, Color.parseColor("#333333"));
        holder.setImageResource(R.id.img_normal, R.mipmap.icon_zhongping);
        holder.setTextColor(R.id.tv_bad, Color.parseColor("#333333"));
        holder.setImageResource(R.id.img_bad, R.mipmap.icon_chaping);
    }

    /**
     * 差评
     */
    private void badEvaluate(ViewHolder holder, int position) {
        if (evaluateLevel.size() > position) {
            evaluateLevel.set(position, 0);
        } else {
            for (int i = 0; i < position; i++) {
                if (evaluateLevel.size() < position + 1) {
                    if (i == position - 1) {
                        evaluateLevel.add(0);
                    } else {
                        evaluateLevel.add(2);
                    }
                }
            }
        }
        holder.setTextColor(R.id.tv_good, Color.parseColor("#333333"));
        holder.setImageResource(R.id.img_good, R.mipmap.icon_pingjia);
        holder.setTextColor(R.id.tv_normal, Color.parseColor("#333333"));
        holder.setImageResource(R.id.img_normal, R.mipmap.icon_zhongping);
        holder.setTextColor(R.id.tv_bad, Color.parseColor("#666666"));
        holder.setImageResource(R.id.img_bad, R.mipmap.icon_chaping_sel);
    }
}