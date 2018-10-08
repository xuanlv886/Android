package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.view.GestureImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;

public class PicShowActivity extends BaseActivty {
    @BindView(R.id.imageView)
    GestureImageView imageView;
    /**
     * 获取到的图片地址
     */
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_show);
        ButterKnife.bind(this);
        getUrl();
        initView();
    }

    private void getUrl() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
    }

    private void initView() {
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.ic_exception)
                .fallback(R.drawable.ic_exception);
        Glide.with(PicShowActivity.this)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }

    @OnClick({R.id.img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
    }
}
