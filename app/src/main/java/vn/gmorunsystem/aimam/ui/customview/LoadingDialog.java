package vn.gmorunsystem.aimam.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import vn.gmorunsystem.aimam.R;

/**
 * Created by HuongTrung on 6/16/2017.
 */

public class LoadingDialog extends Dialog {

    public LoadingDialog(final Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_loading);
        final Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        setCancelable(false);

        ProgressBar v = (ProgressBar) findViewById(R.id.progress);
        v.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(), R.color.color_dark_green), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public void dismissDialogChecked() {
        try {
            if (isShowing()) {
                dismiss();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showDialogChecked() {
        try {
            if (!isShowing()) {
                show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
