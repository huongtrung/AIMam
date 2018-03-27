package vn.gmorunsystem.aimam.ui.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewParent;

import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;

/**
 * Created by HuongTrung on 22/03/17.
 */

public class ScrollMapView extends MapView {
    private ViewParent mViewParent;
    ScaleGestureDetector scaleGestureDetector;

    public ScrollMapView(Context context) {
        super(context);
        init();
    }

    public ScrollMapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ScrollMapView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public ScrollMapView(Context context, GoogleMapOptions googleMapOptions) {
        super(context, googleMapOptions);
        init();
    }

    public void setViewParent(@Nullable final ViewParent viewParent) {
        mViewParent = viewParent;
    }

    private void init() {
        scaleGestureDetector = new ScaleGestureDetector(getContext(),
                new ScaleGestureDetector.OnScaleGestureListener() {
                    @Override
                    public boolean onScale(ScaleGestureDetector detector) {
                        return false;
                    }

                    @Override
                    public boolean onScaleBegin(ScaleGestureDetector detector) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }

                    @Override
                    public void onScaleEnd(ScaleGestureDetector detector) {
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        scaleGestureDetector.onTouchEvent(ev);
        return super.onInterceptTouchEvent(ev);
    }
}
