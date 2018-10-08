package app.cn.extra.mall.user.utils.html;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.xml.sax.XMLReader;

import java.util.Locale;

import app.cn.extra.mall.user.R;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import uk.co.senab.photoview.PhotoViewAttacher;

public class URLTagHandler implements TagHandler {

    private final View views;
    private Context mContext;
    private PopupWindow popupWindow;
    //需要放大的图片
    private ImageView tecent_chat_image;

    public URLTagHandler(Context context) {

        mContext = context;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        views = inflater.inflate(R.layout.activity_goods_detail, null);
        View popView = LayoutInflater.from(context).inflate(R.layout.pub_zoom_popwindow_layout, null);
        tecent_chat_image = popView.findViewById(R.id.image_scale_image);

        popView.findViewById(R.id.image_scale_rll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        final PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(tecent_chat_image);
        photoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }

            @Override
            public void onOutsidePhotoTap() {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        popView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);//popupwindow设置焦点
        popupWindow.setOutsideTouchable(false);//点击外面窗口消失
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        popupWindow.setBackgroundDrawable(dw);



    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        // 处理标签<img>
        if (tag.toLowerCase(Locale.getDefault()).equals("img")) {
            // 获取长度
            int len = output.length();
            // 获取图片地址
            ImageSpan[] images = output.getSpans(len - 1, len, ImageSpan.class);
            String imgURL = images[0].getSource();
            // 使图片可点击并监听点击事件
            output.setSpan(new ClickableImage(mContext, imgURL), len - 1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private class ClickableImage extends ClickableSpan {
        private String url;
        private Context context;

        public ClickableImage(Context context, String url) {
            this.context = context;
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            // 进行图片点击之后的处理
            popupWindow.showAtLocation(widget, Gravity.CENTER, 0, 0);
            final Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) tecent_chat_image.getLayoutParams();
                    linearParams.height = Utils.getScreenHeight(mContext) - 1;
                    linearParams.width = Utils.getScreenWidth(mContext) - 1;
                    tecent_chat_image.setLayoutParams(linearParams);
                    tecent_chat_image.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    tecent_chat_image.setImageDrawable(errorDrawable);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
///                    tecent_chat_image.setImageDrawable(placeHolderDrawable);
                    tecent_chat_image.setImageDrawable(null);
                }
            };
            tecent_chat_image.setTag(target);
            ImageLoad.loadPlaceholder(context, url, target);

        }
    }
}
