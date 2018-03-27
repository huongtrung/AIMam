package vn.gmorunsystem.aimam.ui.adapter;

import android.view.ViewGroup;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ImageProfileBean;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.ProfileAlbumItemViewHolder;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by Veteran Commander on 5/17/2017.
 */

public class ProfileAdapter extends AdapterWithItemClick<ProfileAlbumItemViewHolder> {

    public List<ImageProfileBean> imageList;

    public ProfileAdapter(List<ImageProfileBean> listImage) {
        this.imageList = listImage;
    }

    @Override
    public ProfileAlbumItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProfileAlbumItemViewHolder(UiUtil.inflate(parent, ProfileAlbumItemViewHolder.LAYOUT_ID, false));
    }

    @Override
    public void onBindViewHolder(ProfileAlbumItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }
}
