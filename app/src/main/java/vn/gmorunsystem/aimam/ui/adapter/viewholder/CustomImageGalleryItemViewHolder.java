package vn.gmorunsystem.aimam.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import vn.gmorunsystem.aimam.R;

/**
 * Created by Veteran Commander on 5/25/2017.
 */

public class CustomImageGalleryItemViewHolder extends RecyclerView.ViewHolder {

    public static final int LAYOUT_ID = R.layout.custom_gallery_item;

    public ImageView imgThumb;

    public CheckBox chkImage;

    public int id;

    public CustomImageGalleryItemViewHolder(View view) {
        super(view);
        imgThumb = (ImageView) view.findViewById(R.id.imgThumb);
        chkImage = (CheckBox) view.findViewById(R.id.chkImage);

    }
}
