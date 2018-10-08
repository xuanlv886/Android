package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;

/**
 * 安全中心
 */
public class SecurityCenterActivity extends BaseActivty {
    @BindView(R.id.tv_security_type)
    TextView tvSecurityType;
    private SharePreferenceUtil sharePreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_center);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        sharePreferenceUtil = new SharePreferenceUtil(SecurityCenterActivity.this,
                Constants.SAVE_USER);
        if ("".equals(sharePreferenceUtil.getSecurity())) {
            tvSecurityType.setText("未设置");
        } else {
            tvSecurityType.setText("已设置");
        }
    }

    @OnClick({R.id.img_back, R.id.rl_security})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.rl_security:
                if ("".equals(sharePreferenceUtil.getSecurity())) {
                    Intent intent = new Intent(SecurityCenterActivity.this, CreateSecurityActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SecurityCenterActivity.this, SetSecurityActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}
