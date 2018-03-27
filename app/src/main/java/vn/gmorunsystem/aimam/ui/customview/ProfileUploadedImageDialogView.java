package vn.gmorunsystem.aimam.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import vn.gmorunsystem.aimam.R;

/**
 * Created by adm on 8/18/2017.
 */

public class ProfileUploadedImageDialogView extends Dialog {
    private String imageUrl;

    public ProfileUploadedImageDialogView(@NonNull Context context) {
        super(context);
    }

    public ProfileUploadedImageDialogView(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected ProfileUploadedImageDialogView(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_uploaded_item);
        ImageView imageView = (ImageView) findViewById(R.id.iv_image_uploaded);
        Glide.with(getContext()).load(imageUrl).placeholder(getContext().getResources().getDrawable(R.drawable.loading)).into(imageView);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
