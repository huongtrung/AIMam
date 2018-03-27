package vn.gmorunsystem.aimam.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.ImageProfileBean;
import vn.gmorunsystem.aimam.ui.adapter.OnRecyclerViewItemClick;
import vn.gmorunsystem.aimam.utils.ImageLoader;

/**
 * Created by Veteran Commander on 5/17/2017.
 */

public class ProfileAlbumItemViewHolder extends OnClickViewHolder {

    private OnRecyclerViewItemClick itemClick;

    public void setItemClick(OnRecyclerViewItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @BindView(R.id.iv_album_item)
    ImageView ivAlbumItem;

    public static final int LAYOUT_ID = R.layout.layout_pf_item_album;

    @Override
    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    public ProfileAlbumItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(ImageProfileBean bean) {
        ImageLoader.loadImageThumnail(itemView.getContext(), bean.imageAvartar, ivAlbumItem);
    }
}
