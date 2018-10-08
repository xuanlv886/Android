package app.cn.extra.mall.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.activity.GoodsDetailActivity;
import app.cn.extra.mall.user.utils.Constants;
import app.cn.extra.mall.user.utils.SharePreferenceUtil;
import app.cn.extra.mall.user.view.CustomDialog;
import app.cn.extra.mall.user.view.PopMenu;
import app.cn.extra.mall.user.vo.ShoppingCarProduct;


// 购物车适配器
public class ShoppingCarAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final List<ShoppingCarProduct> data;
    private CustomDialog customDialog = new CustomDialog();

    private OnClicked clListener;
    private Context mContext;
    private PopMenu popMenu;
    private String[] colors = {"#FF7F6C", "#FFD801", "#05D9CA", "#FF7F6C"};
    private final String ACTION_CHANGE_ARROW = "com.action.change.arrow";  // 改变箭头方向
    private ImageView img_item_shop_state;
    private ChildViewHolder holder;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private SharePreferenceUtil sp;

    public ShoppingCarAdapter(List<ShoppingCarProduct> list, Activity context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.data = list;
    }


    public interface OnClicked {
        public void getClicked(int positon, int which);
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public void setonClicked(OnClicked listener) {
        this.clListener = listener;
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(ACTION_CHANGE_ARROW);


    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ChildViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.list_item_shop_child_car, null);
            holder = new ChildViewHolder();
            holder.ck_item_shop_car = view.findViewById(R.id.ck_item_shop_car);
            holder.img_item_shop_pic = view.findViewById(R.id.img_item_shop_pic);
            holder.tv_item_shop_goods_name = view.findViewById(R.id.tv_item_shop_goods_name);
            holder.tv_item_shop_moneys = view.findViewById(R.id.tv_item_shop_moneys);
            holder.tv_item_shop_classify = view.findViewById(R.id.tv_item_shop_classify);
            holder.et_item_shop_num = view.findViewById(R.id.et_item_shop_num);
            holder.tv_item_shop_state = view.findViewById(R.id.tv_item_shop_state);
            holder.img_item_shop_state = view.findViewById(R.id.img_item_shop_state);
            holder.img_item_shop_jian = view.findViewById(R.id.img_item_shop_jian);
            holder.img_item_shop_add = view.findViewById(R.id.img_item_shop_add);
            holder.rl_state = view.findViewById(R.id.rl_state);


            view.setTag(holder);

        } else {
            holder = (ChildViewHolder) view.getTag();
        }


        holder.img_item_shop_jian.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyCountInterface.doDecrease(i, holder.et_item_shop_num, holder.ck_item_shop_car.isChecked());// 暴露删减接口
            }
        });
        holder.img_item_shop_add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyCountInterface.doIncrease(i, holder.et_item_shop_num, holder.ck_item_shop_car.isChecked());// 暴露增加接口
            }
        });
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                String pid = data.get(i).getPro_id();
                intent.putExtra("pid", pid);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        holder.ck_item_shop_car.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                data.get(i).setChoosed(((CheckBox) v).isChecked());
                holder.ck_item_shop_car.setChecked(((CheckBox) v).isChecked());
                checkInterface.checkChild(i, ((CheckBox) v).isChecked());// 暴露子选接口
            }
        });
        final ChildViewHolder finalHolder = holder;
        holder.rl_state.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                holder.img_item_shop_state.setImageResource(R.mipmap.icon_fenleixiala_sel);
                final ArrayList<String> datas = new ArrayList<String>();
                datas.add("未体验");
                datas.add("喜欢");
                datas.add("一般");
                datas.add("不喜欢");
                // 初始化弹出菜单	 POPMENU
                sp = new SharePreferenceUtil(mContext, Constants.SAVE_USER);
                popMenu = new PopMenu(mContext, holder.rl_state.getWidth(), datas, 2);
                popMenu.setOnItemClickListener(new PopMenu.OnItemClickListener() {

                    @Override
                    public void onItemClick(int index) {
                        finalHolder.tv_item_shop_state.setText(datas.get(index));
                        finalHolder.tv_item_shop_state.setTextColor(Color.parseColor(colors[index]));
                        holder.img_item_shop_state.setImageResource(R.mipmap.icon_fenleixiala_nor);
//                        customDialog.showRoundProcessDialog(mContext, R.layout.dialog_loading);
//                        ChangeUserProStatusPort(index, pid);
                    }
                });
                popMenu.showAsDropDown(v);
            }

        });
        String[] str = {"未体验", "喜欢", "一般", "不喜欢"};
//        holder.tv_item_shop_state.setText(str[product.getStatus()]);

        holder.ck_item_shop_car.setChecked(data.get(i).isChoosed());
        holder.et_item_shop_num.setText(data.get(i).getNum() + "");
        if (40 <= data.get(i).getPro_name().length()) {
            holder.tv_item_shop_goods_name.setText(data.get(i).getPro_name()
                    .substring(0, 35) + "···");
        } else {
            holder.tv_item_shop_goods_name.setText(data.get(i).getPro_name());
        }
        if (34 <= data.get(i).getProperty().length()) {
            holder.tv_item_shop_classify.setText(data.get(i).getProperty()
                    .substring(0, 30) + "···");
        } else {
            holder.tv_item_shop_classify.setText(data.get(i).getProperty());
        }

        holder.tv_item_shop_moneys.setText("¥" + data.get(i).getSprice());
        RequestOptions requestOptions = new RequestOptions()
                .error(R.drawable.ic_exception)
                .fallback(R.drawable.ic_exception);
        Glide.with(mContext)
                .load(data.get(i).getPic_url())
                .apply(requestOptions)
                .into(holder.img_item_shop_pic);
        return view;


    }


    class ChildViewHolder {
        //选中状态
        CheckBox ck_item_shop_car;
        //商品图片
        ImageView img_item_shop_pic;
        //商品名称
        TextView tv_item_shop_goods_name;
        //商品价格
        TextView tv_item_shop_moneys;
        //商品信息
        TextView tv_item_shop_classify;
        RelativeLayout rl_state;
        TextView tv_item_shop_state;
        ImageView img_item_shop_state;
        //减
        ImageView img_item_shop_jian;
        //加
        ImageView img_item_shop_add;
        //商品数量
        EditText et_item_shop_num;
    }


    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 状态改变时触发的事件
         *
         * @param position  位置
         * @param isChecked 选中状态
         */
        public void checkChild(int position, boolean isChecked);


    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        public void doIncrease(int position, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        public void doDecrease(int position, View showCountView, boolean isChecked);
    }


}
