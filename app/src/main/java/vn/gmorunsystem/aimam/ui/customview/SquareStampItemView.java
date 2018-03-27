package vn.gmorunsystem.aimam.ui.customview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by HuongTrung on 07/05/2017.
 */

public class SquareStampItemView extends RelativeLayout {
    public SquareStampItemView(Context context) {
        super(context);
    }

    public SquareStampItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareStampItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SquareStampItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Below code change view to square. Please don't change
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
