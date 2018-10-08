package app.cn.extra.mall.merchant.utils.html;

import android.content.Context;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import app.cn.extra.mall.merchant.R;

/**
 * Created by shucheng.qu on 2017/8/24.
 */

public class ImageLoad {

    private static Picasso picasso = null;
    public static void loadPlaceholder(Context context, String url, Target target) {
        if(null == picasso) {
            picasso = new Picasso.Builder(context).loggingEnabled(true).build();
        }
        picasso.load(url)
                .placeholder(R.drawable.ic_exception)
                .error(R.drawable.ic_exception)
                .transform(new ImageTransform(context))
                .into(target);
    }

}
