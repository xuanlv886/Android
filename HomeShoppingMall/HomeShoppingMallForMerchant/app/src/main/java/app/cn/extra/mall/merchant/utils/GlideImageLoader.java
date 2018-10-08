package app.cn.extra.mall.merchant.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.youth.banner.loader.ImageLoader;

import app.cn.extra.mall.merchant.R;


/**
 * Description 重写com.youth.banner的图片加载器
 * Data 2018/6/12-16:47
 * Content
 *
 * @author lzy
 */
public class GlideImageLoader extends ImageLoader{
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
//                .placeholder(R.drawable.ic_exception)
                .error(R.drawable.ic_exception)
                .fallback(R.drawable.ic_exception);

        Glide.with(context)
                .load(path)
                .apply(requestOptions)
                .into(imageView);
    }
}
