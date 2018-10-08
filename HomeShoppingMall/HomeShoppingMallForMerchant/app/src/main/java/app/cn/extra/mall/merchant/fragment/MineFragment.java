package app.cn.extra.mall.merchant.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import app.cn.extra.mall.merchant.R;
import app.cn.extra.mall.merchant.activity.AccountInfoActivity;
import app.cn.extra.mall.merchant.activity.MyArrangeActivity;
import app.cn.extra.mall.merchant.activity.MyDemandActivity;
import app.cn.extra.mall.merchant.activity.MyWalletActivity;
import app.cn.extra.mall.merchant.activity.OrderManagementActivity;
import app.cn.extra.mall.merchant.activity.SettingActivity;
import app.cn.extra.mall.merchant.activity.ShopActivity;
import app.cn.extra.mall.merchant.utils.Constants;
import app.cn.extra.mall.merchant.utils.GlideCircleTransformWithBorder;
import app.cn.extra.mall.merchant.utils.SharePreferenceUtil;
import app.cn.extra.mall.merchant.vo.GetStoreUserInfo;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cfkj.app.cn.cfkjcommonlib.common.BaseFragment;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 我的页Fragment
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class MineFragment extends BaseFragment {
    @BindView(R.id.img_header)
    ImageView imgHeader;
    @BindView(R.id.tv_sname)
    TextView tvSname;
    @BindView(R.id.tv_nick_name)
    TextView tvNickName;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.rl_product_order)
    RelativeLayout rlProductOrder;
    @BindView(R.id.rl_requirement_order)
    RelativeLayout rlRequirementOrder;
    @BindView(R.id.rl_my_wallet)
    RelativeLayout rlMyWallet;
    @BindView(R.id.rl_my_arrange)
    RelativeLayout rlMyArrange;
    @BindView(R.id.rl_test)
    RelativeLayout rlTest;
    @BindView(R.id.rl_my_shop)
    RelativeLayout rlMyShop;
    Unbinder unbinder;
    private SharePreferenceUtil sharePreferenceUtil;
    private Activity activity;

    public static MineFragment newInstance(String param1) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        if(activity==null) {
            activity=getActivity();
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isAdded()) {
            initView();
        }
    }

    /**
     * 初始化界面内元素
     */
    private void initView() {
        sharePreferenceUtil = new SharePreferenceUtil(activity, Constants.SAVE_USER);
        /**
         * 商户身份为个人商户时隐藏商品订单项
         */
        if (Constants.M_PERSONAL.equals(sharePreferenceUtil.getSType())) {
            rlProductOrder.setVisibility(View.GONE);
            rlMyShop.setVisibility(View.GONE);
        } else {
            rlProductOrder.setVisibility(View.VISIBLE);
            rlMyShop.setVisibility(View.VISIBLE);
        }
        getStoreUserInfoPort();
    }

    /**
     * 获取商户个人、店铺相关信息接口
     */
    private void getStoreUserInfoPort() {
        if (Utils.isNetworkAvailable(activity)) {
            OkHttpUtils
                    .post()
                    .url(Constants.getStoreUserInfo)
                    .addParams("uId", sharePreferenceUtil.getUID())
                    .addParams("sId", sharePreferenceUtil.getSID())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            showShortToast(activity, getResources().getString(
                                    R.string.portError));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Utils.LogJson(response);
                            GetStoreUserInfo getStoreUserInfo = new GetStoreUserInfo();
                            getStoreUserInfo = Utils.parserJsonResult(response, GetStoreUserInfo.class);
                            if (Constants.OK.equals(getStoreUserInfo.getFlag())) {
                                if (Constants.OK.equals(getStoreUserInfo.getData().getStatus())) {
                                    /**
                                     * 填充数据
                                     */
                                    if (!TextUtils.isEmpty(getStoreUserInfo.getData().getProfile())) {
                                        RequestOptions requestOptions = new RequestOptions()
                                                .placeholder(R.mipmap.profile)
                                                .error(R.mipmap.profile)
                                                .fallback(R.mipmap.profile)
                                                .transform(new GlideCircleTransformWithBorder(activity, 2,
                                                        activity.getResources().getColor(R.color.blue)));
                                        Glide.with(activity).load(getStoreUserInfo.getData().getProfile() + "?x-oss-process=image/resize,h_150")
                                                .apply(requestOptions)
                                                .into(imgHeader);
                                    }
                                    tvSname.setText(getStoreUserInfo.getData().getSName());
                                    tvNickName.setText(getStoreUserInfo.getData().getUNickName());
                                } else {
                                    showShortToast(activity, getStoreUserInfo.getData()
                                            .getErrorString());
                                }
                            } else {
                                showShortToast(activity, getStoreUserInfo.getErrorString());
                            }
                        }
                    });
        } else {
            showShortToast(activity, Constants.NETWORK_ERROR);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        getStoreUserInfoPort();
    }

    @OnClick({R.id.img_header, R.id.tv_setting, R.id.rl_product_order, R.id.rl_requirement_order,
            R.id.rl_my_wallet, R.id.rl_my_arrange, R.id.rl_test, R.id.rl_my_shop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_header:
                gotoAccountInfoActivity();
                break;
            case R.id.tv_setting:
                gotoSettingActivity();
                break;
            case R.id.rl_product_order:
                gotoOrderManagementActivity();
                break;
            case R.id.rl_requirement_order:
                gotoMyDemandActivity();
                break;
            case R.id.rl_my_wallet:
                gotoMyWalletActivity();
                break;
            case R.id.rl_my_arrange:
                gotoMyArrangeActivity();
                break;
            case R.id.rl_my_shop:
                gotoShopActivity();
                break;
            case R.id.rl_test:
//                Intent intent = new Intent();
//                intent.setClass(activity(), TestAMapPoints.class);
//                startActivity(intent);
                break;
            default:
        }
    }

    /**
     * 跳转至账号信息界面
     */
    private void gotoAccountInfoActivity() {
        Intent intent = new Intent();
        intent.setClass(activity, AccountInfoActivity.class);
        startActivity(intent);
    }


    /**
     * 跳转店铺详情界面
     */
    private void gotoShopActivity() {
        Intent intent = new Intent();
        intent.setClass(activity, ShopActivity.class);
        intent.putExtra("sId", sharePreferenceUtil.getSID());
        startActivity(intent);
    }

    /**
     * 跳转至设置界面
     */
    private void gotoSettingActivity() {
        Intent intent = new Intent();
        intent.setClass(activity, SettingActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转至我的安排界面
     */
    private void gotoMyArrangeActivity() {
        Intent intent = new Intent();
        intent.setClass(activity, MyArrangeActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转至我的钱包界面
     */
    private void gotoMyWalletActivity() {
        Intent intent = new Intent();
        intent.setClass(activity, MyWalletActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转至需求订单界面
     */
    private void gotoMyDemandActivity() {
        Intent intent = new Intent();
        intent.setClass(activity, MyDemandActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转至商品订单界面
     */
    private void gotoOrderManagementActivity() {
        Intent intent = new Intent();
        intent.setClass(activity, OrderManagementActivity.class);
        startActivity(intent);
    }
}
