package app.cn.extra.mall.user.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import app.cn.extra.mall.user.R;
import app.cn.extra.mall.user.view.PayKeyboardView;

public class PayKeyboardFragment extends DialogFragment {

    Dialog mDialog;
    PayKeyboardView panel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mDialog == null) {
            mDialog = new Dialog(getActivity(), R.style.KeyboardDialog);
            panel = new PayKeyboardView(getActivity());
            panel.setTitle(title);
            panel.setOnKeyboardListener(listener);
            mDialog.setContentView(panel);
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.alpha = 7f; // 透明度
            lp.gravity = Gravity.BOTTOM;
            mDialog.getWindow().setAttributes(lp);
        }
        panel.clear();
        return mDialog;
    }

    PayKeyboardView.OnKeyboardListener listener;
    String title;

    public void setTitle(String title) {
        this.title = title;
        if (panel != null) {
            panel.setTitle(title);
        }
    }

    public void setOnKeyboardListener(PayKeyboardView.OnKeyboardListener l) {
        this.listener = l;
        if (panel != null) {
            panel.setOnKeyboardListener(l);
        }
    }

}
