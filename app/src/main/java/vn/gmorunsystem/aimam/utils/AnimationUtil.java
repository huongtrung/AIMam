package vn.gmorunsystem.aimam.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import vn.gmorunsystem.aimam.R;

/**
 * Created by HuongTrung on 6/2/2017.
 */

public class AnimationUtil {

    public static void loadAnimationRotate(Context context, View view) {
        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.ic_refresh_rotation);
        view.startAnimation(rotation);
    }
}
