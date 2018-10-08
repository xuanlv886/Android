package app.cn.extra.mall.user.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

/**
 * Description APK下载安装工具类
 * Data 2018/1/3-15:41
 * Content
 * @author lzy
 */

public class Downloader {
    /**
     * 下载器
     */
    private DownloadManager downloadManager;
    private Context mContext;
    /**
     * 下载的ID
     */
    private long downloadId;

    public Downloader(Context context) {
        this.mContext = context;
    }

    /**
     * 下载apk
     * @param url
     * @param name
     */
    public void downloadAPK(String url, String name) {

        if (TextUtils.isEmpty(url)) {
            return;
        }
        /**
         * 创建下载任务
         */
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("安装程序");
        request.setDescription("正在下载...");
        request.setVisibleInDownloadsUi(true);

        //设置下载的路径
        request.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getAbsolutePath(), name);

        //获取DownloadManager
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        downloadId = downloadManager.enqueue(request);

        //注册广播接收者，监听下载状态
        mContext.registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * 广播监听下载的各个状态
     */
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };


    /**
     * 检查下载状态
     */
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(downloadId);
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成安装APK
                    File apkFile = queryDownloadedApk();
                    Intent intent = new Intent();
                    intent.setAction("action.down");
                    intent.putExtra("STATUS", 0);
                    intent.putExtra("FILEPATH", apkFile.getAbsolutePath());
                    mContext.sendBroadcast(intent);
                    mContext.unregisterReceiver(receiver);
                    installAPK();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    }

    /**
     * 下载到本地后执行安装
     */
    private void installAPK() {
        Intent intent = new Intent();
        File apkFile = queryDownloadedApk();

        Uri uri;
        /**
         * 适配7.0
         * 7.0以后，系统要求授予临时uri读取权限，安装完毕以后，系统会自动收回权限，次过程没有用户交互
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext, Constants.PROVIDER_NAME, apkFile);
            intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            /**
             * 7.0以下
             */
            uri = Uri.fromFile(apkFile);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    public File queryDownloadedApk() {
        File targetApkFile = null;
        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloadManager.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!uriString.isEmpty()) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;
    }
}
