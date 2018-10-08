package app.cn.extra.mall.merchant.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.GetMerchantLonLatList;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 测试高德地图点平滑位移（用户端集成）
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class TestAMapPoints extends BaseActivty {

    MapView mMapView;

    /**
     * 初始化地图控制器对象
     */
    private AMap aMap = null;

    private Polyline mPolyline;

    private SharePreferenceUtil sharePreferenceUtil;

    private GetMerchantLonLatList list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_amap_points);
        mMapView =  findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(TestAMapPoints.this,
                Constants.SAVE_USER);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        getMerchantLonLatListPort();
    }

    /**
     * 获取某商户针对于特定需求订单的移动轨迹列表
     */
    private void getMerchantLonLatListPort() {
        if (Utils.isNetworkAvailable(TestAMapPoints.this)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getMerchantLonLatList)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    /**
                     * roId的值现为测试数据，需替换为真实的需求订单主键标识
                     */
                    .addParams("roId", "b762a848-4865-11e8-8390-4ccc6a70ac67")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            /**
                             * 接口调用出错
                             */
                            showShortToast(TestAMapPoints.this,
                                    getResources().getString(R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Utils.LogJson(response);
                            list = new GetMerchantLonLatList();
                            list = Utils.parserJsonResult(response, GetMerchantLonLatList.class);
                            if (Constants.OK.equals(list.getFlag())) {
                                if (0 < list.getData().size()) {
                                    addPolylineInPlayGround();
                                    startMove();
                                }
                            } else {
                                showShortToast(TestAMapPoints.this,
                                        list.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(TestAMapPoints.this, Constants.NETWORK_ERROR);
        }
    }



    /**
     * 添加轨迹线
     */
    private void addPolylineInPlayGround() {
        List<LatLng> list = readLatLngs();
        List<Integer> colorList = new ArrayList<Integer>();
        List<BitmapDescriptor> bitmapDescriptors = new ArrayList<BitmapDescriptor>();

        int[] colors = new int[]{Color.argb(255, 0, 255, 0),
                Color.argb(255, 255, 255, 0),
                Color.argb(255, 255, 0, 0)};

        //用一个数组来存放纹理
        List<BitmapDescriptor> textureList = new ArrayList<BitmapDescriptor>();
        textureList.add(BitmapDescriptorFactory.fromResource(R.mipmap.custtexture));

        List<Integer> texIndexList = new ArrayList<Integer>();
        texIndexList.add(0);//对应上面的第0个纹理
        texIndexList.add(1);
        texIndexList.add(2);

        Random random = new Random();
        for (int i = 0; i < list.size(); i++) {
            colorList.add(colors[random.nextInt(3)]);
            bitmapDescriptors.add(textureList.get(0));

        }

        mPolyline = aMap.addPolyline(new PolylineOptions().setCustomTexture(BitmapDescriptorFactory
                .fromResource(R.mipmap.custtexture)) //setCustomTextureList(bitmapDescriptors)
//				.setCustomTextureIndex(texIndexList)
                .addAll(list)
                .useGradient(true)
                .width(18));
        LatLngBounds bounds = new LatLngBounds(list.get(0), list.get(list.size() - 2));
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    /**
     * 设置坐标点
     * @return
     */
    private List<LatLng> readLatLngs() {
        List<LatLng> points = new ArrayList<LatLng>();
        for (int i = 0; i < list.getData().size(); i++) {
            points.add(new LatLng(Double.valueOf(list.getData().get(i).getMtLat()).doubleValue(),
                    Double.valueOf(list.getData().get(i).getMtLon()).doubleValue()));
        }
        return points;
    }

    /**
     * 开始移动
     */
    private void startMove() {
        if (mPolyline == null) {
            showShortToast(TestAMapPoints.this, "请先设置路线");
            return;
        }

        // 读取轨迹点
        List<LatLng> points = readLatLngs();
        // 构建 轨迹的显示区域
        LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        // 实例 SmoothMoveMarker 对象
        SmoothMoveMarker smoothMarker = new SmoothMoveMarker(aMap);
        // 设置 平滑移动的 图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.mipmap.icon_car));

        // 取轨迹点的第一个点 作为 平滑移动的启动
        LatLng drivePoint = points.get(0);
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
        points.set(pair.first, drivePoint);
        List<LatLng> subList = points.subList(pair.first, points.size());

        // 设置轨迹点
        smoothMarker.setPoints(subList);
        // 设置平滑移动的总时间  单位  秒
        smoothMarker.setTotalDuration(10);
        // 开始移动
        smoothMarker.startSmoothMove();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}
