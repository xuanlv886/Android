package cfkj.app.cn.cfkjcommonlib;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import cfkj.app.cn.cfkjcommonlib.common.CrashHandler;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.https.HttpsUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.log.LoggerInterceptor;
import okhttp3.OkHttpClient;
import okio.Buffer;

/**
 * Description
 * Data 2018/6/12-11:11
 * Content
 *
 * @author lzy
 */
public class CommonApplication extends Application{

    /**
     * 发起HTTPS 请求需要的证书
     */
    private String CER_CFKJ = "-----BEGIN CERTIFICATE-----\n" +
            "MIIDfzCCAmegAwIBAgIECO8e2jANBgkqhkiG9w0BAQsFADBwMQswCQYDVQQGEwJjbjEOMAwGA1UE\n" +
            "CBMFaGViZWkxFTATBgNVBAcTDHNoaWppYXpodWFuZzEVMBMGA1UEChMMY2VuZ2ZlbmdrZWppMRUw\n" +
            "EwYDVQQLEwxjZW5nZmVuZ2tlamkxDDAKBgNVBAMTA2xpdTAeFw0xNzAzMjEwNDQwMzFaFw0yNzAx\n" +
            "MjgwNDQwMzFaMHAxCzAJBgNVBAYTAmNuMQ4wDAYDVQQIEwVoZWJlaTEVMBMGA1UEBxMMc2hpamlh\n" +
            "emh1YW5nMRUwEwYDVQQKEwxjZW5nZmVuZ2tlamkxFTATBgNVBAsTDGNlbmdmZW5na2VqaTEMMAoG\n" +
            "A1UEAxMDbGl1MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn5Y8mOhVchq0+GJTYQc5\n" +
            "iRiCiADx5gIsdKFO/bEDQnM0YXoy9V2ewd7if4nnk2EtJCNgMb9VaU7DR6NzEdQ7Taj0XQ36kzTn\n" +
            "GD1a79JcBxlfsvkdN1+TDlqRxgORmMRMUTtZ7v4A5QKkz9VyV7UhwvSgZSDT0Zu9H41XT92HXM7e\n" +
            "y2b8OyWoqzhAgZoKL08+L6IQKAECVDzkHdtOZVmUYWIQ+rzQHQ4BDOq3HFCytev9ivSWIDXwBRuA\n" +
            "/D5gTpWt0UIuOYJfpkMn3W7dWU1HVgKSRIvmxBPdWAI+5ChFRzpL+kJF7d4w/vaSaN3fqmqAy1Dr\n" +
            "RAYGD8XN72+XzDQeiQIDAQABoyEwHzAdBgNVHQ4EFgQUGoXoi+w+3Mbvq4SLC1qEmO4bO8EwDQYJ\n" +
            "KoZIhvcNAQELBQADggEBABsbiKaJEnXdpboKHnnlmHtz6yZ+kQhAIhCN9FLg1TbwHiM2UArjtKoo\n" +
            "K8JmdQBswOM4ICEbnE47oRH0X4TnOqGNokf5kRMYX4+nmUfD9Zi51oTxWCzvbVJXI4U1qem3/if0\n" +
            "TRAZ5EMdBINbUFyAvHrOTvU7JZEJZtroDk9gJ89ydGfe/dQwlKOaTCitYsWJkjQtwM9+Rv+Rhe1v\n" +
            "7A0pbH98NUPL/K++wxsJAxr5iXEqhez290Q8QNZvSVswHyAEZ0k83+eT7Kutos1MkNgSqA2GctYT\n" +
            "HfClOLvR/wMeWgghg7gGPxnmNNszPo2k+j7KajVU0RS+aecU0BYl15S9EL8=" +
            "-----END CERTIFICATE-----";

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 初始化log
         */
        initLogger();
        /**
         * 初始化OkHttp
         */
        initOkHttp();
        /**
         * 异常收集类 调试时注释该部分代码即可
         */
        CrashHandler.getInstance().init(this);
    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true)
                .methodCount(2)
                .methodOffset(5)
                .tag("LOGGER")
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

    }

    private void initOkHttp() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(
                new Buffer().writeUtf8(CER_CFKJ).inputStream(), null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .hostnameVerifier(new HostnameVerifier()
                {
                    @Override
                    public boolean verify(String hostname, SSLSession session)
                    {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
