package app.cn.extra.mall.user.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import java.util.ArrayList;

import app.cn.extra.mall.user.R;


public class PopMenu implements OnItemClickListener {
    public interface OnItemClickListener {
    	public void onItemClick(int index);
    }

    private ArrayList<String> itemList = new ArrayList<String>();
	private Context context;
	private PopupWindow popupWindow;
	private ListView listView;
	private OnItemClickListener listener;
	private LayoutInflater inflater;
	private final String ACTION_CHANGE_ARROW = "com.action.change.arrow";  // 改变MarketDetailActivity箭头方向
	private String[] colors = {"#FF7F6C", "#FFD801", "#05D9CA", "#FF7F6C"};
	
	public PopMenu(final Context context, int width, ArrayList<String> datas) {
		this.context = context;
		this.itemList = datas;
		this.inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.popmenu_common, null);

		listView = (ListView) view.findViewById(R.id.listView);
		listView.setAdapter(new PopAdapter(0)); // 背景为白色 字体为黑色
		listView.setOnItemClickListener(this);

		popupWindow = new PopupWindow(view, width,  //这里宽度需要自己指定，使用 WRAP_CONTENT 会很大
				LayoutParams.WRAP_CONTENT);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				Log.e("popupWindow", "消失");
				Intent mIntent = new Intent(ACTION_CHANGE_ARROW);
	    		// 发送广播
	    		context.sendBroadcast(mIntent);
			}
		});
	}
	
	// 背景颜色不同的popmenu bg: 1 背景为黑色  字体为黄色    2 背景为白色  字体为多色  3 字体为白色 背景为黑色
	@SuppressLint("NewApi") public PopMenu(final Context context, int width, ArrayList<String> datas, int bg) {
		this.context = context;
		this.itemList = datas;
		this.inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.popmenu_common, null);
		LinearLayout root = (LinearLayout) view.findViewById(R.id.root);
		if (1 == bg) {
			root.setBackgroundColor(Color.parseColor("#282828"));
			listView = (ListView) view.findViewById(R.id.listView);
			listView.setAdapter(new PopAdapter(bg)); // 背景为黑色 字体为黄色
		} else if (2 == bg) {
			listView = (ListView) view.findViewById(R.id.listView);
			listView.setDividerHeight(0);
			listView.setAdapter(new PopAdapter(bg)); 
		} else if (3 == bg) {
			listView = (ListView) view.findViewById(R.id.listView);
			listView.setDividerHeight(0);
			listView.setAdapter(new PopAdapter(bg)); // 背景为黑色 字体为黄色
		}
		
		listView.setOnItemClickListener(this);

		popupWindow = new PopupWindow(view, width,  //这里宽度需要自己指定，使用 WRAP_CONTENT 会很大
				LayoutParams.WRAP_CONTENT);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				Log.e("popupWindow", "消失");
				Intent mIntent = new Intent(ACTION_CHANGE_ARROW);
	    		// 发送广播
	    		context.sendBroadcast(mIntent);
			}
		});
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
		// TODO Auto-generated method stub
		if (listener != null) {
			listener.onItemClick(position);
		}
		dismiss();
		
	}
	
		public void setOnItemClickListener(OnItemClickListener listener) {
			 this.listener = listener;
		}

		public void addItems(String[] items) {
			for (String s : items) {
                itemList.add(s);
            }
		}

		public void addItem(String item) {
			itemList.add(item);
		}
		
		public void showAsDropDown(View parent) {
			popupWindow.showAsDropDown(parent, 10,
//			context.getResources().getDimensionPixelSize(R.dimen.popmenu_yoff)
					0);
			
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.update();
		}

		public void showAsCenter(View parent) {
			popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
			popupWindow.setFocusable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.update();
		}
		
		public void dismiss() {
			popupWindow.dismiss();
		}
		
		public boolean isShow() {
			return popupWindow.isShowing();
		}
		
		
	
		// ListView适配器
		private final class PopAdapter extends BaseAdapter {
			
			private int tag;
			public PopAdapter (int tag) {
				this.tag = tag;
			}
					@Override
					public int getCount() {
						return itemList.size();
					}

					@Override
					public Object getItem(int position) {
						return itemList.get(position);
					}

					@Override
					public long getItemId(int position) {
						return position;
					}

					@SuppressLint("ResourceAsColor") @Override
					public View getView(int position, View convertView, ViewGroup parent) {
						ViewHolder holder = new ViewHolder();;
						if (convertView == null) {
							convertView = inflater.inflate(R.layout.popmenu_item, null);
							holder.groupItem = (TextView) convertView.findViewById(R.id.textView);
							convertView.setTag(holder);
						} else {
							holder = (ViewHolder) convertView.getTag();
						}
						if (0 == tag) {
							holder.groupItem.setText(itemList.get(position));
							holder.groupItem.setTextSize(14);
						} else if (1 == tag) {
							holder.groupItem.setText(itemList.get(position));
							holder.groupItem.setTextSize(16);
							holder.groupItem.setBackgroundColor(Color.parseColor("#282828"));
							holder.groupItem.setTextColor(Color.parseColor("#FFD801"));
						} else if (2 == tag) {
							holder.groupItem.setText(itemList.get(position));
							holder.groupItem.setTextSize(12);
							holder.groupItem.setTextColor(Color.parseColor(colors[position]));
						} else if (3 == tag) {
							holder.groupItem.setText(itemList.get(position));
							holder.groupItem.setTextSize(14);
							holder.groupItem.setBackgroundColor(Color.parseColor("#282828"));
							holder.groupItem.setTextColor(Color.parseColor("#F2F2F2"));
						}
						
						return convertView;
					}

					private final class ViewHolder {
						TextView groupItem;
					}
				}



		
}