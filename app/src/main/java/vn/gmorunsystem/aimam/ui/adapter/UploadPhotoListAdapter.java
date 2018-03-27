package vn.gmorunsystem.aimam.ui.adapter;

import android.view.ViewGroup;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopMenuItem;
import vn.gmorunsystem.aimam.callback.OnChangeMenuItemListener;
import vn.gmorunsystem.aimam.ui.activities.MainActivity;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.UploadPhotoListViewHolder;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by Veteran Commander on 5/24/2017.
 */

public class UploadPhotoListAdapter extends AdapterWithItemClick<UploadPhotoListViewHolder> {

    private List<String> imagePath;
    private MainActivity mContext;
    private List<ShopMenuItem> menuItems;

    OnChangeMenuItemListener onChangeMenuItemListener;

    public void setOnChangeMenuItemListener(OnChangeMenuItemListener onChangeMenuItemListener) {
        this.onChangeMenuItemListener = onChangeMenuItemListener;
    }

    public UploadPhotoListAdapter(MainActivity activity, List<String> listPath, List<ShopMenuItem> shopMenuItems) {
        imagePath = listPath;
        mContext = activity;
        menuItems = shopMenuItems;
    }

    @Override
    public UploadPhotoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UploadPhotoListViewHolder(UiUtil.inflate(parent, UploadPhotoListViewHolder.LAYOUT_ID, false));
    }

    @Override
    public void onBindViewHolder(UploadPhotoListViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(mContext, imagePath.get(position), menuItems, position);
        holder.setOnChangeMenuItemListener(onChangeMenuItemListener);
    }

    @Override
    public int getItemCount() {
        return imagePath.size();
    }

}
