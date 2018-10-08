package app.cn.extra.mall.user.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.style.Wave;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.activity.AccountMessageActivity;
import app.cn.extra.mall.user.activity.AddressActivity;
import app.cn.extra.mall.user.activity.CollectionActivity;
import app.cn.extra.mall.user.activity.FollowActivity;
import app.cn.extra.mall.user.activity.FootprintActivity;
import app.cn.extra.mall.user.activity.MyDemandActivity;
import app.cn.extra.mall.user.activity.OrderManagementActivity;
import app.cn.extra.mall.user.activity.SettingActivity;
import app.cn.extra.mall.user.event.MineFragmentEvent;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.GlideCircleTransformWithBorder;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.utils.Util;
import app.cn.extra.mall.user.vo.GetPersonCenter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cfkj.app.cn.cfkjcommonlib.common.BaseFragment;
import cfkj.app.cn.cfkjcommonlib.common.CustomToast;
import cfkj.app.cn.cfkjcommonlib.common.Utils;
import cfkj.app.cn.cfkjcommonlib.okhttp.OkHttpUtils;
import cfkj.app.cn.cfkjcommonlib.okhttp.callback.StringCallback;
import okhttp3.Call;

/**
 * Description 我的Fragment
 * Data 2018/6/11-10:53
 * Content
 *
 * @author lzy
 */
public class MineFragment extends BaseFragment {
    private View mView;
    Unbinder unbinder;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.rl_collection)
    RelativeLayout rlCollection;
    @BindView(R.id.tv_collection_num)
    TextView tvCollectionNum;
    @BindView(R.id.rl_follow)
    RelativeLayout rlFollow;
    @BindView(R.id.tv_follow_num)
    TextView tvFollowNum;
    @BindView(R.id.rl_footprint)
    RelativeLayout rlFootprint;
    @BindView(R.id.tv_footprint_num)
    TextView tvFootprintNum;
    @BindView(R.id.rl_order_management)
    RelativeLayout rlOrderManagement;
    @BindView(R.id.rl_my_demand)
    RelativeLayout rlMyDemand;
    @BindView(R.id.rl_collect_goods_address)
    RelativeLayout rlCollectGoodsAddress;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    SharePreferenceUtil sp = null;

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
        mView = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, mView);
        sp = new SharePreferenceUtil(getActivity(), Constants.SAVE_USER);
        /**
         * 设置加载动画展示样式
         */
        Wave wave = new Wave();
        progressBar.setIndeterminateDrawable(wave);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 消息处理
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MineFragmentEventBus(MineFragmentEvent event) {
        getPersonCenter();
    }

    private void initData() {
        getPersonCenter();
    }

    /**
     * 获取个人中心数据
     */
    private void getPersonCenter() {
        if (Utils.isNetworkAvailable(getActivity())) {
            if (null != progressBar) {
                progressBar.setVisibility(View.VISIBLE);
            }
            OkHttpUtils
                    .post()
                    .url(Constants.getPersonCenter)
                    .addParams("uId", sp.getUID())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Util.logE("GetPersonCenter--------", response);
                            GetPersonCenter getPersonCenter = new GetPersonCenter();
                            getPersonCenter = Utils.parserJsonResult(response, GetPersonCenter.class);
                            if (null != progressBar) {
                                progressBar.setVisibility(View.GONE);
                            }
                            if ("true".equals(getPersonCenter.getFlag())) {
                                tvCollectionNum.setText("" + getPersonCenter.getData().getUserCollectionNum());
                                tvFollowNum.setText("" + getPersonCenter.getData().getUserAttentionNum());
                                tvFootprintNum.setText("" + getPersonCenter.getData().getFootPrintNum());
                                tvName.setText(getPersonCenter.getData().getUNickName());
                                RequestOptions requestOptions = new RequestOptions()
                                        .placeholder(R.mipmap.profile)
                                        .error(R.mipmap.profile)
                                        .fallback(R.mipmap.profile)
                                        .transform(new GlideCircleTransformWithBorder(getActivity(), 2,
                                                getActivity().getResources().getColor(R.color.blue)));
                                Glide.with(getActivity())
                                        .load(getPersonCenter.getData().getUrl()
                                                + "?x-oss-process=image/resize,h_150")
                                        .apply(requestOptions)
                                        .into(ivHead);

                            } else {
                                CustomToast.showToast(getActivity(),
                                        getPersonCenter.getErrorString(), Toast.LENGTH_SHORT);
                            }
                        }
                    });
        } else {
            CustomToast.showToast(getActivity(),
                    Constants.NETWORK_ERROR, Toast.LENGTH_SHORT);
        }
    }

    @OnClick({R.id.iv_head, R.id.tv_setting, R.id.rl_collection, R.id.rl_follow, R.id.rl_footprint, R.id.rl_order_management, R.id.rl_my_demand, R.id.rl_collect_goods_address})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_head:
                intent = new Intent();
                intent.setClass(getActivity(), AccountMessageActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_setting:
                intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_collection:
                intent = new Intent(getActivity(), CollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_follow:
                intent = new Intent(getActivity(), FollowActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_footprint:
                intent = new Intent(getActivity(), FootprintActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_order_management:
                intent = new Intent(getActivity(), OrderManagementActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_demand:
                intent = new Intent(getActivity(), MyDemandActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_collect_goods_address:
                intent = new Intent(getActivity(), AddressActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
