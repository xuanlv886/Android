package app.cn.extra.mall.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.vo.GetCityList;

public class HotCityAdapter extends RecyclerView.Adapter<HotCityAdapter.MyHolder> {
    private List<GetCityList.DataBean.OpenCityListBean> list = null;
    private Context mContext;
    OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);

        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public HotCityAdapter(Context mContext, List<GetCityList.DataBean.OpenCityListBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    //OnCreateViewHolder用来给rv创建缓存的
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_item_hot_city, null);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    //给缓存控件设置数据
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

//        //根据position获取分类的首字母的Char ascii值
//        int section = getSectionForPosition(position);
//
//        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
//        if (position == getPositionForSection(section)) {
//            holder.tvLetter.setVisibility(View.VISIBLE);
//            holder.tvLetter.setText(list.get(position).getAcSortLetters());
//            if (position == 1) {
//                holder.tvAll.setVisibility(View.VISIBLE);
//            } else {
//                holder.tvAll.setVisibility(View.GONE);
//            }
//        } else {
//            holder.tvLetter.setVisibility(View.GONE);
//            holder.tvAll.setVisibility(View.GONE);
//        }
//
//        holder.tvTitle.setText(this.list.get(position).getAcCity());
        holder.tv_cityName.setText(list.get(position).getAcCity());
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
    }

    //获取记录数
    @Override
    public int getItemCount() {
        try {
            return list.size();
        } catch (NullPointerException e) {
            return 0;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_cityName;

        public MyHolder(View itemView) {
            super(itemView);
            tv_cityName = (TextView) itemView.findViewById(R.id.tv_cityName);
        }
    }
}
