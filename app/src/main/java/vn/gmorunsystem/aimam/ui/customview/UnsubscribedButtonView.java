package vn.gmorunsystem.aimam.ui.customview;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by adm on 8/17/2017.
 */

public class UnsubscribedButtonView extends android.support.v7.widget.AppCompatButton {
    public UnsubscribedButtonView(Context context) {
        super(context);
    }

    public UnsubscribedButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnsubscribedButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = widthSize * 26 / 85;
        int finalHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, finalHeightMeasureSpec);
    }
}
