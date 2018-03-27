package vn.gmorunsystem.aimam.ui.customview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by HuongTrung on 09/21/2017.
 */

public class ImageViewResize extends android.support.v7.widget.AppCompatImageView {

    public ImageViewResize(Context context) {
        super(context);
    }

    public ImageViewResize(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewResize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasure = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasure = widthMeasure / 2;
        int finalHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightMeasure, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, finalHeightMeasureSpec);
    }
}
