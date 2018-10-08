package app.cn.extra.mall.user.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.vo.GetCityList;

public class SorAdapter extends RecyclerView.Adapter<SorAdapter.MyHolder> implements SectionIndexer {
    private List<GetCityList.DataBean.OpenCityListBean> list = null;
    private List<GetCityList.DataBean.OpenCityListBean> hotlist = null;
    private Context mContext;
    HotCityAdapter hotCityAdapter;
    OnItemClickListener mOnItemClickListener;
    HotCityAdapter.OnItemClickListener onHotItemClickListener;
    /**
     * 加载时判断时候是第一条
     */
    boolean isfrast = true;
    /**
     * gridview每行数目
     */
    private final int ONE_LINE_SHOW_NUMBER = 3;

    public interface OnItemClickListener {
        void onClick(int position);

        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public SorAdapter(Context mContext, List<GetCityList.DataBean.OpenCityListBean> list, HotCityAdapter hotCityAdapter, HotCityAdapter.OnItemClickListener onHotItemClickListener) {
        this.mContext = mContext;
        this.list = list;
        this.hotCityAdapter = hotCityAdapter;
        this.onHotItemClickListener = onHotItemClickListener;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<GetCityList.DataBean.OpenCityListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    //OnCreateViewHolder用来给rv创建缓存的
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder holder = null;
        if (isfrast) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.grid_hot_city, null, false);
            holder = new MyHolder(view);
            isfrast = false;
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
            holder = new MyHolder(view);
        }
        return holder;
    }

    //给缓存控件设置数据
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if (position == 0) {
//            holder.recyclerView.setLayoutManager(layoutManage);

            hotlist = new ArrayList<GetCityList.DataBean.OpenCityListBean>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getOcIsHot() == 1) {
                    hotlist.add(list.get(i));
                }
            }
            GridLayoutManager layoutManage = new GridLayoutManager(mContext, ONE_LINE_SHOW_NUMBER);
//            layoutManage.setScrollEnabled(false);
            holder.recyclerView.setLayoutManager(layoutManage);
//            holder.recyclerView.setHasFixedSize(true);
//            holder.recyclerView.setNestedScrollingEnabled(false);

            if (hotlist.size() > 0) {
                hotCityAdapter = new HotCityAdapter(mContext, hotlist);
                holder.recyclerView.setAdapter(hotCityAdapter);
                /**
                 * 热点城市点击事件
                 */
                hotCityAdapter.setOnItemClickListener(onHotItemClickListener);
            }
        } else {
            //根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);

            //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                holder.tvLetter.setVisibility(View.VISIBLE);
                holder.tvLetter.setText(list.get(position).getAcSortLetters());
                if (position == 1) {
                    holder.tvAll.setVisibility(View.VISIBLE);
                } else {
                    holder.tvAll.setVisibility(View.GONE);
                }
            } else {
                holder.tvLetter.setVisibility(View.GONE);
                holder.tvAll.setVisibility(View.GONE);
            }

            holder.tvTitle.setText(this.list.get(position).getAcCity());
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
    }

    /**
     * 将dp转化为px
     */
    private int dip2px(float dip) {
        float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, mContext.getResources().getDisplayMetrics());
        return (int) (v + 0.5f);
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

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public int getPositionForSection(int i) {
        for (int s = 0; s < list.size(); s++) {
            String sortStr = list.get(s).getAcSortLetters();
            if (s != 0) {
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == i) {
                    return s;
                }
            }
        }

        return -1;
    }

    @Override
    public int getSectionForPosition(int i) {
        return list.get(i).getAcSortLetters().charAt(0);
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tvLetter;
        TextView tvTitle;
        TextView tvAll;
        RecyclerView recyclerView;

        public MyHolder(View itemView) {
            super(itemView);
            if (isfrast) {
                recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerView);
            } else {
                tvTitle = (TextView) itemView.findViewById(R.id.title);
                tvLetter = (TextView) itemView.findViewById(R.id.catalog);
                tvAll = (TextView) itemView.findViewById(R.id.all);
            }
        }
    }
}
