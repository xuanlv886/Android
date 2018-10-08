package app.cn.extra.mall.merchant.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class NewsExpandableListView extends ExpandableListView {
    public NewsExpandableListView(Context context) {
        super(context);
    }

    public NewsExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
