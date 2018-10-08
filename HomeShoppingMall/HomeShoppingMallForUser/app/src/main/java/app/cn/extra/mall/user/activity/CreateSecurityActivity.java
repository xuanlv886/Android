package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import app.cn.extra.mall.user.R;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;

/**
 * 创建密保问题
 */
public class CreateSecurityActivity extends BaseActivty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_security);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_back, R.id.create_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.create_btn:
                Intent intent = new Intent(CreateSecurityActivity.this, SetSecurityActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
