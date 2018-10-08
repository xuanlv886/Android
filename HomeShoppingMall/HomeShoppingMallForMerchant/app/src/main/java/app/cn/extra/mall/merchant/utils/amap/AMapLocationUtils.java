package app.cn.extra.mall.merchant.utils.amap;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import app.cn.extra.mall.merchant.R;
import cfkj.app.cn.cfkjcommonlib.common.Utils;

/**
 * Description
 * Data 2018/7/26-11:57
 * Content
 *
 * @author L
 */
public class AMapLocationUtils implements AMapLocationListener {

    /**
     * 声明mlocationClient对象
     */
    public AMapLocationClient mlocationClient = null;
    /**
     * 声明mLocationOption对象
     */
    public AMapLocationClientOption mLocationOption = null;
    /**
     * 回调
     */
    private AMapLocationCallBack aMapLocationCallBack;


    public AMapLocationUtils(AMapLocationCallBack aMapLocationCallBack) {
        this.aMapLocationCallBack = aMapLocationCallBack;
    }

    /**
     * 初始化定位相关设置
     * @param context
     * @param onceLocation 是否只定位一次
     */
    public void initLocationClient(Context context, boolean onceLocation) {
        mlocationClient = new AMapLocationClient(context);
        /**
         * 初始化定位参数
         */
        mLocationOption = new AMapLocationClientOption();
        mlocationClient.setLocationListener(this);
        /**
         * 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
         */
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        /**
         * 获取一次定位结果：
         * 该方法默认为false。
         */
        mLocationOption.setOnceLocation(onceLocation);
        /**
         * 获取最近3s内精度最高的一次定位结果：
         * 设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度
         * 最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，
         * 反之不会，默认为false。
         */
        mLocationOption.setOnceLocationLatest(onceLocation);
        mlocationClient.setLocationOption(mLocationOption);
    }

    /**
     * 开启定位服务
     */
    public void startLocation(Context context) {
        if (null != mlocationClient) {
            mlocationClient.startLocation();
        } else {
            Utils.LogE(context.getResources().getString(R.string.aMapLocationNotInit));
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                this.aMapLocationCallBack.onSuccess(aMapLocation);
            }else {
                this.aMapLocationCallBack.onError("AmapError:" + "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 定义回调接口
     */
    public interface AMapLocationCallBack {
        /**
         * 成功
         * @param aMapLocation
         */
        void onSuccess(AMapLocation aMapLocation);

        /**
         * 失败
         * @param errInfo
         */
        void onError(String errInfo);
    }
}
