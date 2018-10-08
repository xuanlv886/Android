package app.cn.extra.mall.merchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import app.cn.extra.mall.merchant.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;

/**
 * Description 选择要注册的商户身份类型页
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class SelectMerchantTypeActivity extends BaseActivty {

    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.btn_personal)
    Button btnPersonal;
    @BindView(R.id.btn_company)
    Button btnCompany;
    /**
     * 用以标识用户选择的商户类型 0--尚未进行选择 1--个人商户，2--企业商户
     */
    private int whichSelect = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_merchant_type);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_next, R.id.img_back, R.id.btn_personal, R.id.btn_company})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                doNext();
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_personal:
                btnPersonal.setBackgroundResource(R.drawable.shape_btn_blue);
                btnCompany.setBackgroundResource(R.drawable.shape_btn_gray);
                btnPersonal.setTextColor(getResources().getColor(R.color.white));
                btnCompany.setTextColor(getResources().getColor(R.color.black));
                whichSelect = 1;
                break;
            case R.id.btn_company:
                btnCompany.setBackgroundResource(R.drawable.shape_btn_blue);
                btnPersonal.setBackgroundResource(R.drawable.shape_btn_gray);
                btnCompany.setTextColor(getResources().getColor(R.color.white));
                btnPersonal.setTextColor(getResources().getColor(R.color.black));
                whichSelect = 2;
                break;
            default:
        }
    }

    /**
     * 执行后续逻辑，根据注册的商户类型跳转至对应的注册界面
     */
    private void doNext() {
        switch (whichSelect) {
            /**
             * 尚未进行选择
             */
            case 0:
                showShortToast(SelectMerchantTypeActivity.this, getResources().getString(
                        R.string.notSelect));
                break;
            /**
             * 个人商户
             */
            case 1:
                gotoRegisterForPersonalActivity();
                break;
            /**
             * 企业商户
             */
            case 2:
                gotoRegisterForCompanyActivity();
                break;
            default:
        }
    }

    /**
     * 跳转至个人商户注册界面
     */
    private void gotoRegisterForPersonalActivity() {
        Intent intent = new Intent();
        intent.setClass(SelectMerchantTypeActivity.this,
                RegisterForPersonalActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转至企业商户注册界面
     */
    private void gotoRegisterForCompanyActivity() {
        Intent intent = new Intent();
        intent.setClass(SelectMerchantTypeActivity.this,
                RegisterForCompanyActivity.class);
        startActivity(intent);
    }

}
