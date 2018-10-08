package app.cn.extra.mall.user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Wave;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.adapter.ShoppingCarAdapter;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.vo.DoDelMyTrolley;
import app.cn.extra.mall.user.vo.DogetMyProductTrolley;
import app.cn.extra.mall.user.vo.ShoppingCarProduct;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfkj.app.cn.cfkjcommonlib.common.BaseActivty;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import cfkj.app.cn.cfkjcommonlib.view.ReplaceViewHelper.VaryViewHelperController;
import okhttp3.Call;

/**
 * 购物车界面
 */
public class ShoppingCarActivity extends BaseActivty implements ShoppingCarAdapter.CheckInterface, ShoppingCarAdapter
        .ModifyCountInterface {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.tv_edit)
    TextView tvEdit;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.ck_all)
    CheckBox ckAll;
    @BindView(R.id.lv_shop)
    ListView lvShop;
    private ShoppingCarAdapter adapter;
    private SharePreferenceUtil mSharePreferenceUtil;
    private DogetMyProductTrolley.DataBean datas;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private List<ShoppingCarProduct> lists = new ArrayList<ShoppingCarProduct>();
    /**
     * 购买的商品总价
     */
    private double totalPrice = 0.00;
    /**
     * 购买的商品总数量
     */
    private int totalCount = 0;
    private String allPrice;
    /**
     * 0 结算  1 编辑
     */
    private int tag = 0;
    /**
     * 待删除集合
     */
    private ArrayList<String> pid;
    private ArrayList<ShoppingCarProduct> toBeDeleteProducts;
    private VaryViewHelperController varyViewHelperController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_car);
        ButterKnife.bind(this);
        initView();
        initData();

    }

    private void initView() {
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        mSharePreferenceUtil = new SharePreferenceUtil(ShoppingCarActivity.this, Constants.SAVE_USER);
    }

    private void initData() {
        //查看购物车接口
        dogetMyProductTrolleyPort();
    }


    private void dogetMyProductTrolleyPort() {
        String cid = mSharePreferenceUtil.getUID();
        if (Utils.isNetworkAvailable(ShoppingCarActivity.this)) {
            if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
            OkHttpUtils
                    .post()
                    .url("")
                    .addParams("cId", cid)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {

                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            DogetMyProductTrolley myProductTrolley = new DogetMyProductTrolley();
                            myProductTrolley = Utils.parserJsonResult(response, DogetMyProductTrolley.class);
                            if ("true".equals(myProductTrolley.getFlag())) {
                                datas = myProductTrolley.getData();
                                processData(datas);

                            } else {

                                showLongToast(ShoppingCarActivity.this, myProductTrolley.getErrorString());
                            }
                        }
                    });
        } else {
            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
            showLongToast(ShoppingCarActivity.this, Constants.NETWORK_ERROR);

        }
    }

    private void processData(DogetMyProductTrolley.DataBean datas) {

        for (int i = 0; i < datas.getUserTrolleyList().size(); i++) {
            ShoppingCarProduct product = new ShoppingCarProduct();
            product.setPro_id(datas.getUserTrolleyList().get(i).getPId());
            product.setPro_ctid(datas.getUserTrolleyList().get(i).getUtId());
            product.setPro_name(datas.getUserTrolleyList().get(i).getPName());
            product.setPic_url(datas.getUserTrolleyList().get(i).getPicName());
            product.setProperty(datas.getUserTrolleyList().get(i).getUtProductProperty());
            product.setisChange(1);
            product.setNum(1);
            if (!TextUtils.isEmpty(datas.getUserTrolleyList().get(i).getPNowPrice())) {
                product.setSprice(Double.valueOf(datas.getUserTrolleyList().get(i).getPNowPrice()));
            } else {
                product.setSprice(0.00);
            }
            if (!TextUtils.isEmpty(datas.getUserTrolleyList().get(i).getPOriginalPrice())) {
                product.setOnlinePrice(Double.valueOf(datas.getUserTrolleyList().get(i).getPOriginalPrice()));
            } else {
                product.setOnlinePrice(0.00);
            }
            if (!TextUtils.isEmpty(datas.getUserTrolleyList().get(i).getPNowPrice())) {
                product.setIntegralPrice(Double.valueOf(datas.getUserTrolleyList().get(i).getPNowPrice()));
            } else {
                product.setIntegralPrice(0.00);
            }
            lists.add(product);

        }
        adapter = new ShoppingCarAdapter(lists, this);
        adapter.setCheckInterface(this);//设置复选框接口
        adapter.setModifyCountInterface(this);//设置数量增减接口
        lvShop.setAdapter(adapter);
//        lvShop.setEmptyView(llServiceLoad);

    }


    @Override
    public void checkChild(int position, boolean isChecked) {
        if (isAllCheck()) {
            ckAll.setChecked(true);
        } else {
            ckAll.setChecked(false);
        }
        adapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {
        ShoppingCarProduct product = lists.get(position);
        int currentCount = product.getNum();
        currentCount++;
        product.setNum(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        adapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        ShoppingCarProduct product = lists.get(position);
        int currentCount = product.getNum();
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        product.setNum(currentCount);
        ((TextView) showCountView).setText(currentCount + "");

        adapter.notifyDataSetChanged();
        calculate();
    }

    /**
     * 判断是否都被未被选中
     */
    private boolean isAllCheck() {
        for (ShoppingCarProduct list : lists) {
            if (!list.isChoosed()) {
                return false;
            }
        }
        return true;
    }

    @OnClick({R.id.ck_all, R.id.tv_edit, R.id.tv_pay, R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ck_all:
                //所以的item 选中状态
                doCheckAll();
                break;
            case R.id.tv_edit:
                if (0 == tag) {
                    tvEdit.setText("取消");
                    tvPay.setBackgroundColor(Color.parseColor("#FF9485"));
                    tvPay.setText("删除" + "（" + totalCount + "）");

                    tag = 1;
                } else if (1 == tag) {
                    tvEdit.setText("编辑");
                    tvPay.setBackgroundColor(Color.parseColor("#ECC801"));
                    tvPay.setText("结算" + "（" + totalCount + "）");
                    tag = 0;
                }
                break;
            case R.id.tv_pay:
                if (1 == tag) {
                    if (totalCount == 0) {
                        CustomToast.showToast(ShoppingCarActivity.this,
                                "请选择要删除的商品！", Toast.LENGTH_SHORT);
                    } else {
                        if (null != progressBar) {                progressBar.setVisibility(View.VISIBLE);            }
                        DeleteShop();
                    }
                } else {
                    PayGoods();
                }
                break;
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 支付商品
     */
    private void PayGoods() {
        toBeDeleteProducts = new ArrayList<ShoppingCarProduct>();
        for (int j = 0; j < lists.size(); j++) {
            if (lists.get(j).isChoosed()) {
                toBeDeleteProducts.add(lists.get(j));

            }
        }
        if (toBeDeleteProducts.size() == 0) {
            CustomToast.showToast(ShoppingCarActivity.this,
                    "您还没有选择商品，无法结算！", Toast.LENGTH_SHORT);
            return;
        } else {
            StartPayActivity();
        }

    }

    private void StartPayActivity() {
        Intent intentbuy = new Intent();
        intentbuy.setClass(ShoppingCarActivity.this, CommodityBuyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("state", 1);
        bundle.putSerializable("goodsDetail", toBeDeleteProducts);
        intentbuy.putExtras(bundle);
        startActivity(intentbuy);
        finish();
        toBeDeleteProducts.clear();
    }

    /**
     * 删除商品
     */
    private void DeleteShop() {
        if (Utils.isNetworkAvailable(ShoppingCarActivity.this)) {
            String uid = mSharePreferenceUtil.getUID();
            // 待删除的子元素列表
            pid = new ArrayList<String>();


            for (int j = 0; j < lists.size(); j++) {
                if (lists.get(j).isChoosed()) {
                    pid.add(lists.get(j).getPro_ctid());
                }
            }
            if (pid.size() == 0) {
                if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                CustomToast.showToast(ShoppingCarActivity.this,
                        "没有要删除的商品！", Toast.LENGTH_SHORT);
                return;
            }
            Gson gson = new Gson();
            String jsonArr = gson.toJson(pid);
            OkHttpUtils
                    .post()
                    .url("")
                    .addParams("ctId", jsonArr)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            DoDelMyTrolley doDelMyTrolley = new DoDelMyTrolley();
                            doDelMyTrolley = Utils.parserJsonResult(response, DoDelMyTrolley.class);
                            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
                            if ("true".equals(doDelMyTrolley.getFlag())) {
                                doDelete();
                                CustomToast.showToast(ShoppingCarActivity.this,
                                        "删除商品成功！", Toast.LENGTH_SHORT);
                            } else {
                                CustomToast.showToast(ShoppingCarActivity.this,
                                        doDelMyTrolley.getErrorString(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            if (null != progressBar) {                progressBar.setVisibility(View.GONE);            }
            CustomToast.showToast(ShoppingCarActivity.this,
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);
        }
    }

    private void doDelete() {

        List<ShoppingCarProduct> toBeDeleteProducts = new ArrayList<ShoppingCarProduct>();// 待删除的子元素列表
        for (int j = 0; j < lists.size(); j++) {
            if (lists.get(j).isChoosed()) {
                toBeDeleteProducts.add(lists.get(j));
            }
        }
        pid.clear();
        lists.removeAll(toBeDeleteProducts);
        adapter.notifyDataSetChanged();
        lvShop.setAdapter(adapter);
        tvMoney.setText("¥" + 0);
        tvPay.setText("删除" + "（" + 0 + "）");
    }

    private void doCheckAll() {
        for (int i = 0; i < lists.size(); i++) {
            lists.get(i).setChoosed(ckAll.isChecked());
        }
        adapter.notifyDataSetChanged();
        calculate();
    }

    /**
     * 计算总价格
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < lists.size(); i++) {

            ShoppingCarProduct product = lists.get(i);
            if (product.isChoosed()) {
                totalCount++;
                totalPrice += Double.valueOf(product.getSprice()) * product.getNum();

            }
        }
        DecimalFormat df = new DecimalFormat("0.00");
        allPrice = df.format(totalPrice);
        tvMoney.setText("¥" + allPrice);
        if (1 == tag) {
            tvPay.setText("删除" + "（" + totalCount + "）");
        } else {
            tvPay.setText("结算" + "（" + totalCount + "）");
        }
    }

}
