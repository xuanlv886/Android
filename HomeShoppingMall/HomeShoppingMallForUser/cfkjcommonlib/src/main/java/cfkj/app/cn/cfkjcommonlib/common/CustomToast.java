package cfkj.app.cn.cfkjcommonlib.common;

import android.app.ActivityManager;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Administrator on 2017/11/1 0001.
 */

public class CustomToast {
    private static Toast mToast;
    public static void showToast(Context context, String text, int duration) {
        if (mToast != null) {
            mToast.setText(text);
        } else {
            mToast = Toast.makeText(context, text, duration);
            mToast.setGravity(Gravity.CENTER,0,0);
        }


        if(isActivityRunning(context.getPackageName(), context)){
            mToast.show();
        }
    }

    public static boolean isActivityRunning(String packagename, Context context){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = am.getRunningTasks(1);
        String cmpNameTemp = null;
        if(null != runningTaskInfos){
            cmpNameTemp = runningTaskInfos.get(0).topActivity.toString();
        }
        if(null != cmpNameTemp){
            return cmpNameTemp.contains(packagename);
        }
        return false;
    }
}