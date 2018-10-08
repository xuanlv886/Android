package app.cn.extra.mall.user.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;

// 数据加载等待窗口
public class CustomDialog {

	private Dialog mDialog;
	private Context context;

	public void showRoundProcessDialog(final Context mContext, int layout) {

		OnKeyListener keyListener = new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_HOME
						|| keyCode == KeyEvent.KEYCODE_SEARCH) {
					return true;
				}
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (isShow()) {
						dismissDialog();
//						Toast.makeText(mContext, "数据加载已取消！", Toast.LENGTH_LONG).show();
					} 
				}
				return false;
			}
		};
		context = mContext;
		if (isValidContext(mContext)) {
			mDialog = new AlertDialog.Builder(mContext).create();
			mDialog.setOnKeyListener(keyListener);
			mDialog.setCancelable(false);
			mDialog.show();

			mDialog.setContentView(layout);
		}
	}

	private boolean isValidContext (Context c){
		Activity a = (Activity)c;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			if (a.isDestroyed() || a.isFinishing()){
                Log.i("CustomDialog", "Activity is invalid." + " isDestoryed-->" + a.isDestroyed() + " isFinishing-->" + a.isFinishing());
                return false;
            }else{
                return true;
            }
		}
		return true;
	}



	public boolean isShow() {
		 
		if (mDialog.isShowing()) {
			return true;
		}
		return false;
	
	}

	public void dismissDialog() {
		if (isValidContext(context)) {
			mDialog.dismiss();
		}
	}

}
