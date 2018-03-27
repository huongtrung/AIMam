package vn.gmorunsystem.aimam.ui.customview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by adm on 8/14/2017.
 */

public class ShopHomeItemView extends LinearLayout {

    public ShopHomeItemView(Context context) {
        super(context);
    }

    public ShopHomeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShopHomeItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ShopHomeItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = widthSize * 190 / 172;
        int finalHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, finalHeightMeasureSpec);
    }
}
