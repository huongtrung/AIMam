package vn.gmorunsystem.aimam.ui.customview;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import vn.gmorunsystem.aimam.R;

/**
 * Created by Veteran Commander on 9/21/2016.
 */
public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    private Activity activity;
    private TextView tvTitle;
    private ImageView ivShopLogo;

    public CustomInfoWindow(Activity activity) {
        this.activity = activity;
    }


    @Override
    public View getInfoWindow(final Marker marker) {
        View content = activity.getLayoutInflater().inflate(R.layout.detail_maker, null);
        tvTitle = (TextView) content.findViewById(R.id.tvtitle);
        ivShopLogo = (ImageView) content.findViewById(R.id.iv_nearShop_logo);
        Glide.with(activity).load(marker.getSnippet()).thumbnail(0.3f).listener(
                new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (!isFromMemoryCache) marker.showInfoWindow();
                        return false;
                    }
                }
        ).centerCrop().into(ivShopLogo);
        tvTitle.setText(marker.getTitle());
        return content;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
