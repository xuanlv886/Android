package app.cn.extra.mall.merchant.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class NewsGridView extends GridView {

	public NewsGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NewsGridView(Context context) {
		super(context);
	}

	public NewsGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 计算 gridView 的高度
	 * @param widthMeasureSpec
	 * @param heightMeasureSpec
     */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}


}