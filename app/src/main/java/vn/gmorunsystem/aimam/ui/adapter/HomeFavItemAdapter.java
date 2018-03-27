package vn.gmorunsystem.aimam.ui.adapter;

import android.view.ViewGroup;

import java.util.List;

import vn.gmorunsystem.aimam.bean.HomeFavoriteBean;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.HomeFavItemViewHolder;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by Veteran Commander on 6/14/2017.
 */

public class HomeFavItemAdapter extends AdapterWithItemClick<HomeFavItemViewHolder> {
    List<HomeFavoriteBean> beanList;

    public HomeFavItemAdapter(List<HomeFavoriteBean> beanList) {
        this.beanList = beanList;
    }

    @Override
    public HomeFavItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeFavItemViewHolder(UiUtil.inflate(parent, HomeFavItemViewHolder.LAYOUT_ID, false));
    }

    @Override
    public void onBindViewHolder(HomeFavItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(beanList.get(position));
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }
}
