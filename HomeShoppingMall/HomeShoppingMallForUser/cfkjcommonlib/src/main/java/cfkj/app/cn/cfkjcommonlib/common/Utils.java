package cfkj.app.cn.cfkjcommonlib.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.Random;

/**
 * Description
 * Data 2018/4/21-14:41
 * Content
 *
 * @author lzy
 */

public class Utils {

    /**
     * 是否调试模式
     */
    public static boolean isDebug = false;


    /**
     * Returns whether the network is available
     */
    public static boolean isNetworkAvailable(Context context) {
        if (null == context) {
            return false;
        }
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.w("TAG", "找不到网络服务！");
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0, length = info.length; i < length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getSDPath() {
        String sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory().toString();
        } else {
            sdDir = Environment.getDataDirectory().getPath();
        }
        return sdDir;
    }

    /**
     * 解析响应的json数据到实体类
     */
    public static <T> T parserJsonResult(String in, Class<T> cls) {
        // TODO Auto-generated method stub
        T t = null;
        if (in == null) {
            return null;
        }
        try {
            Gson gson = new Gson();
            t = gson.fromJson(in, cls);
        } catch (Exception e) {
            // TODO: handle exception
            Log.i("解析响应的json数据异常", e.getMessage());
        }
        return t;
    }
    public static String ObjectToJson(Object obj) {
        String result = null;
        if (null == obj) {
            return null;
        }
        try {
            Gson gson = new Gson();
            result = gson.toJson(obj);
        } catch (Exception e) {
            LogE("拼装json格式字符串异常:" + e.getMessage());
        }
        return result;
    }
    /**
     * 获取屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        int width = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 获取屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        int height = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public static void LogE(String msg) {
        if (isDebug) {
            Logger.e(msg);
        }
    }

    public static void LogE(Exception e, String msg) {
        if (isDebug) {
            Logger.e(e, msg);
        }
    }

    public static void LogJson(String json) {
        if (isDebug) {
            Logger.json(json);
        }
    }

    public static void LogXml(String xml) {
        if (isDebug) {
            Logger.xml(xml);
        }
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        boolean flag = false;
        if (!TextUtils.isEmpty(phoneNumber) && 11 == phoneNumber.length()) {
            String telRegex = "[1]\\d{10}";
            flag = phoneNumber.matches(telRegex);
        }
        return flag;
    }

    /**
     * 测量View 的宽和高
     *
     * @param view
     */
    public static void measureWidthAndHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
    }

    /**
     * 获得照片的输出保存Uri
     * @return Uri
     */
    public static Uri getImageCropUri(String path) {
        File file = new File(getSDPath(), path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return Uri.fromFile(file);
    }

    /**
     *
     * desc:获取min~max范围内的随机数
     * param:
     * return:int
     * time:2018年6月16日
     * author:L
     */
    public static int getLimitRandomNum(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    /**
     * 获取文件名字 没有后缀
     * @param pathandname
     * @return
     */
    public static String getFileName(String pathandname) {

        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }

    }
}
