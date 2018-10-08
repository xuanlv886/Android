package app.cn.extra.mall.merchant.utils.html;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

public class ImageTransform implements Transformation {

    private final Context context;
    private String Key = "ImageTransform";

    public ImageTransform(Context context) {
        this.context = context;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int targetWidth = context.getResources().getDisplayMetrics().widthPixels;
        if (source.getWidth() == 0) {
            return source;
        }

        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
        int targetHeight = (int) (targetWidth * aspectRatio);
        if (targetHeight != 0 && targetWidth != 0) {
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                source.recycle();
            }
            return result;
        } else {
            return source;
        }
    }

    @Override
    public String key() {
        return Key;
    }
}
