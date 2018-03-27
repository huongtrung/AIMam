package vn.gmorunsystem.aimam.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by HuongTrung on 08/10/2017.
 */

public class ToastUtil {
    public static void makeText(final Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }
}
