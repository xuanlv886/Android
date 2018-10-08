package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.cn.extra.mall.user.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;

public class EvaluateResultActivity extends BaseActivty {
    @BindView(R.id.img_back)
    ImageView ibBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.btn_back)
    Button btnBack;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_result);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        title = intent.getStringExtra("TITLE");
        initView();
    }

    private void initView() {
        tvTitle.setText(title);
        if ("评价成功".equals(title)) {
            tvContent.setText("您已评价成功。");
        } else if ("提交成功".equals(title)) {
            tvContent.setText("您已提交退货/退款申请，请耐心等候。");
        }
    }

    @OnClick({R.id.btn_back, R.id.img_back})
    public void onViewClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_back:
                intent = new Intent();
                intent.setClass(EvaluateResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.img_back:
                intent = new Intent();
                intent.setClass(EvaluateResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            intent.setClass(EvaluateResultActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
