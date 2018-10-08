package cfkj.app.cn.cfkjcommonlib.common;

import android.app.TabActivity;
import android.content.Context;
import android.os.Bundle;

/**
 * Description TabActivity基类
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */

public class BaseTabActivity extends TabActivity {



    /* (non-Javadoc)
     * @see android.app.ActivityGroup#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        final Context context = getApplicationContext();
//        PushAgent.getInstance(context).onAppStart();
    }

    /* (non-Javadoc)
     * @see android.app.ActivityGroup#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();

        final Context context = getApplicationContext();

    }

    /* (non-Javadoc)
     * @see android.app.ActivityGroup#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onLowMemory()
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    public static void closeAll() {
        ActivityCollector.finishAll();
    }


}