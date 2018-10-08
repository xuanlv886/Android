package app.cn.extra.mall.merchant.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import app.cn.extra.mall.merchant.utils.Constants;
import cfkj.app.cn.cfkjcommonlib.common.DateUtil;
import cfkj.app.cn.cfkjcommonlib.common.Utils;

/**
 * Description 后台计时服务，每1分钟向服务器提交一次商户所在位置的经纬度
 * Data 2018/7/26-9:55
 * Content
 *
 * @author L
 */
public class MovingTrajectoryService extends Service{

    private ScheduledExecutorService scheduledExecutorService = null;
    /**
     * 计数器
     */
    private int count = 0;

    /**
     * 需累计的时间
     */
    private static final int TIME_COUNT = 60;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.LogE("MovingTrajectoryService--onCreate");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Utils.LogE("MovingTrajectoryService--onStart");
        init();
    }

    /**
     * 初始化服务相关配置
     */
    private void init() {
        if (null == scheduledExecutorService) {
            scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
            scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 1,
                    TimeUnit.SECONDS);
        }
    }

    /**
     * 换行切换任务
     * @author Administrator
     */
    private class ScrollTask implements Runnable {

        @Override
        public void run() {
            synchronized (this) {
                Log.e("MovingTrajectoryService",
                        DateUtil.getDateTimeFromMillis(System.currentTimeMillis()));
                if (TIME_COUNT == count) {
                    count = 0;
                    sendTimeChangedBroadcast();
                } else {
                    count++;
                }
            }
        }
    }

    /**
     * 发送广播，通知时间已改变
     */
    private void sendTimeChangedBroadcast(){
        Intent timeIntent = new Intent();
        timeIntent.setAction(Constants.TIME_CHANGED_ACTION);
        timeIntent.setComponent(new ComponentName("app.cn.extra.mall.merchant",
                "app.cn.extra.mall.merchant.broadcast.MovingTrajectoryReceiver"));
        sendBroadcast(timeIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        Utils.LogE("MovingTrajectoryService--onDestroy");
        if (null != scheduledExecutorService) {
            scheduledExecutorService.shutdown();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Utils.LogE("MovingTrajectoryService--onBind");
        return null;
    }
}
