package app.cn.extra.mall.merchant.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.donkingliang.imageselector.utils.ImageSelectorUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.event.PickUpGoodsFragmentEvent;
import app.cn.extra.mall.merchant.utils.Bimp;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.FileUtils;
import app.cn.extra.mall.merchant.utils.ImageItem;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.utils.Util;
import app.cn.extra.mall.merchant.utils.osshelper.PutObjectSamples;
import app.cn.extra.mall.merchant.vo.GoodsPictures;
import app.cn.extra.mall.merchant.vo.InformInspection;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.ThreadPoolManager;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 通知用户验货
 */
public class NotificationPickUpGoodsActivity extends BaseActivty {
    @BindView(R.id.gv_pic)
    GridView gvPic;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.parent)
    RelativeLayout parentView;
    private SharePreferenceUtil sp;
    private OSS oss;
    private static final String endpoint = "oss-cn-beijing.aliyuncs.com";
    private static final String accessKeyId = "LTAI8nxqAwt2ldSW";
    private static final String accessKeySecret = "ZcV18JFSwuy782bySD1FdHaQolSehh";
    private PopupWindow pop = null;
    private LinearLayout llPopup;
    private PicAdapter adapter;
    public int picNum = 9;
    private int iconNum = 9;
    private ArrayList<String> picturePaths = new ArrayList<String>();
    private HashMap<String, Integer> url = new HashMap<>();
    private List<String> names = new ArrayList<>();
    private String uploadObject = "";
    private String uploadFilePath = "";
    String roId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_pick_up_goods);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        roId = intent.getStringExtra("roId");
        sp = new SharePreferenceUtil(NotificationPickUpGoodsActivity.this, Constants.SAVE_USER);
        // 上传图片
        pop = new PopupWindow(NotificationPickUpGoodsActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);
        llPopup = view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        //阿里云oss 上传图片
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(
                accessKeyId, accessKeySecret);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(getApplicationContext(), endpoint, credentialProvider, conf);

        RelativeLayout parent = view.findViewById(R.id.parent);
        Button bt1 = view
                .findViewById(R.id.item_popupwindows_camera);
        Button bt2 = view
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = view
                .findViewById(R.id.item_popupwindows_cancel);
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pop.dismiss();
                llPopup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCamera();

                pop.dismiss();
                llPopup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(NotificationPickUpGoodsActivity.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //先判断有没有权限 ，没有就在这里进行权限的申请
                    ActivityCompat.requestPermissions(NotificationPickUpGoodsActivity.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.TAKE_PHOTO);
                } else {
                    iconNum = 9 - Bimp.SelectBitmap.size();
                    ImageSelectorUtils.openPhoto(NotificationPickUpGoodsActivity.this, Constants.REQUEST_CODE, false, iconNum);

                }
                pop.dismiss();
                llPopup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                llPopup.clearAnimation();
            }
        });


        adapter = new PicAdapter(this);
        adapter.update();
        gvPic.setAdapter(adapter);
        gvPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == Bimp.SelectBitmap.size() && Bimp.SelectBitmap.size() != 9) {
                    Log.i("ddddddd", "----------");
                    Util.colseKeyBoard(NotificationPickUpGoodsActivity.this);
                    llPopup.startAnimation(AnimationUtils.loadAnimation(NotificationPickUpGoodsActivity.this, R.anim.activity_translate_in));
                    pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
                } else if (arg2 == Bimp.SelectBitmap.size() && Bimp.SelectBitmap.size() == 9) {
                    CustomToast.showToast(NotificationPickUpGoodsActivity.this,
                            "最多只能上传9张图片哦！", Toast.LENGTH_SHORT);
                } else {
                    Intent intent = new Intent(NotificationPickUpGoodsActivity.this,
                            GalleryActivity.class);
                    intent.putExtra("position", "2");
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
//        for (int i = 0; i < picturePaths.size(); i++) {
//            String fileNames = String.valueOf(System.currentTimeMillis() + i)
//                    + "." + Util.getFileType(picturePaths.get(i));
//            names.add(fileNames);
//        }
//        for (int i = 0; i < Bimp.SelectBitmap.size(); i++) {
//            uploadFilePath = picturePaths.get(i);
//            uploadObject = "HomeShoppingMall/requirement" + "/" + names.get(i);
//
//            ThreadPoolManager.getInstance().execute(new Runnable() {
//                @Override
//                public void run() {
//                    new PutObjectSamples(oss, testBucket,
//                            uploadObject, uploadFilePath).asyncPutObjectFromLocalFile();
//                }
//            });
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void toCamera() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //检查是否获取该权限
        if (EasyPermissions.hasPermissions(this, perms)) {
            //启动相机
            Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, Constants.TAKE_CAMERA);
        } else {
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限
            EasyPermissions.requestPermissions(this, "必要的权限", Constants.TAKE_CAMERA, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constants.TAKE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toCamera();
            } else {
                // Permission Denied
                showPowerDialog();
            }
            return;
        } else if (requestCode == Constants.TAKE_PHOTO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), Constants.TAKE_PHOTO);
            } else {
                // Permission Denied
                showPowerDialog();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.TAKE_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (Bimp.SelectBitmap.size() < picNum) {
                        Bundle extras = data.getExtras();
                        Bitmap biamap = (Bitmap) extras.get("data");
                        String fileName = String.valueOf(System.currentTimeMillis());
                        FileUtils.saveBitmap(biamap, fileName);
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setBitmap(biamap);
                        takePhoto.setImagePath(FileUtils.SDPATH + "/" + fileName + ".png");
                        Bimp.SelectBitmap.add(takePhoto);

                        adapter.notifyDataSetChanged();
                    } else {
                        CustomToast.showToast(NotificationPickUpGoodsActivity.this,
                                "最多可传9张照片！", Toast.LENGTH_SHORT);
                    }
                }
                break;
            case Constants.REQUEST_CODE:
                if (data != null) {
                    ArrayList<String> images = data.getStringArrayListExtra(ImageSelectorUtils.SELECT_RESULT);

                    for (int i = 0; i < images.size(); i++) {
                        ImageItem takePhoto = new ImageItem();
                        takePhoto.setImagePath(images.get(i));
                        Bimp.SelectBitmap.add(takePhoto);
                    }
                    Bimp.SelectBitmap.toString();
                    adapter.notifyDataSetChanged();
                    Log.e("SelectBitmap", Bimp.SelectBitmap.toString());

                }
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bimp.SelectBitmap.clear();
        Bimp.tempSelectBitmap.clear();
    }

    @OnClick({R.id.img_back, R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_submit:
                informInspection(roId);
                break;
            default:
                break;
        }
    }

    /**
     * 通知验货接口
     */
    private void informInspection(String roId) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < picturePaths.size(); i++) {
            String fileNames = String.valueOf(System.currentTimeMillis() + i)
                    + "." + Util.getFileType(picturePaths.get(i));
            names.add(fileNames);
        }

        Gson gsons = new Gson();
        for (int i = 0; i < names.size(); i++) {
            url.put(names.get(i), i);
        }
        List<GoodsPictures.PicsBean> beanList = new ArrayList<GoodsPictures.PicsBean>();
        GoodsPictures pictures = new GoodsPictures();
        pictures.setPTag("");
        for (HashMap.Entry<String, Integer> entry : url.entrySet()) {
            Log.e("Key =", entry.getKey() + "" + ", Value = " + entry.getValue() + "");
            GoodsPictures.PicsBean picsBean = new GoodsPictures.PicsBean();
            picsBean.setPNo(entry.getValue());
            picsBean.setPName(entry.getKey());
            beanList.add(picsBean);
        }
        pictures.setPics(beanList);
        String urls = gsons.toJson(pictures);
        System.out.println(urls);
        for (int i = 0; i < Bimp.SelectBitmap.size(); i++) {
            uploadFilePath = picturePaths.get(i);
            uploadObject = "requirement" + "/" + names.get(i);

            ThreadPoolManager.getInstance().execute(new Runnable() {
                @Override
                public void run() {
                    new PutObjectSamples(oss, Constants.bucketName,
                            uploadObject, uploadFilePath).asyncPutObjectFromLocalFile();
                }
            });
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (Utils.isNetworkAvailable(NotificationPickUpGoodsActivity.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.informInspection)
                    .addParams("roId", roId)
                    .addParams("picData", urls)
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
                            showShortToast(NotificationPickUpGoodsActivity.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                            Utils.LogJson(response);
                            InformInspection informInspection = new InformInspection();
                            informInspection = Utils.parserJsonResult(response, InformInspection.class);
                            if (Constants.OK.equals(informInspection.getFlag())) {
                                if (informInspection.getData().isStatus()) {
                                    showShortToast(NotificationPickUpGoodsActivity.this,
                                            "通知成功");
                                    EventBus.getDefault().post(new PickUpGoodsFragmentEvent());
                                    finish();
                                } else {
                                    showShortToast(NotificationPickUpGoodsActivity.this,
                                            informInspection.getData().getErrorString());
                                }
                            } else {
                                showShortToast(NotificationPickUpGoodsActivity.this,
                                        informInspection.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(NotificationPickUpGoodsActivity.this, Constants.NETWORK_ERROR);
        }
    }

    private void showPowerDialog() {
        // 取得自定义View
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_power_manage, null);
        view.setAlpha(0.8f);
        final Dialog alertDialog = new AlertDialog.Builder(this, R.style.Theme_Transparent).
                setView(view).
                create();
//                alertDialog.setCancelable(false);
//                alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        alertDialog.getWindow().setAttributes(lp);
        TextView tv_yes = (TextView) view.findViewById(R.id.tv_yes);
        TextView tv_never_remind = (TextView) view.findViewById(R.id.tv_never_remind);
        tv_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alertDialog.dismiss();
            }
        });
        tv_never_remind.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alertDialog.dismiss();
                getAppDetailSettingIntent(NotificationPickUpGoodsActivity.this);
            }
        });
    }

    private void getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }

    /**
     * 上传图片适配器
     */
    @SuppressLint("HandlerLeak")
    public class PicAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private int selectedPosition = -1;
        private boolean shape;

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public PicAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void update() {
//			loading();
            adapter.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (Bimp.SelectBitmap.size() == picNum) {
                return picNum;
            }
            return (Bimp.SelectBitmap.size() + 1);
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_published_grida_goods,
                        parent, false);
                holder = new ViewHolder();
                holder.image = convertView
                        .findViewById(R.id.item_grida_image_goods);
                holder.iv_delete = convertView
                        .findViewById(R.id.iv_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.SelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.mipmap.button_shangchuantupian_nor));
                holder.iv_delete.setVisibility(View.GONE);

            } else {
                holder.iv_delete.setVisibility(View.VISIBLE);
                holder.image.setImageBitmap(Bimp.SelectBitmap.get(position).getBitmap());
                picturePaths.add(Bimp.SelectBitmap.get(position).getImagePath());
                ArrayList<String> temp = new ArrayList<String>();
                temp = Util.removeDuplicate(picturePaths);
                picturePaths.clear();
                picturePaths.addAll(temp);

            }
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showRemoveDialog(position);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            public ImageView image;
            public ImageView iv_delete;
        }

    }

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
                Bimp.SelectBitmap.remove(position);
                adapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }
        });

    }
}
