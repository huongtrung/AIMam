package vn.gmorunsystem.aimam.ui.adapter;

import vn.gmorunsystem.aimam.callback.OnRecyclerViewItemLongClick;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.OnClickViewHolder;

/**
 * Created on 9/21/2015.
 */
public abstract class HeaderAdapterWithItemClick<VH extends OnClickViewHolder, H, T, F> extends HeaderRecyclerViewAdapter<VH, H, T, F> {

    OnRecyclerViewItemClick onRecyclerViewItemClick;
    OnRecyclerViewItemLongClick onRecyclerViewItemLongClick;

    public OnRecyclerViewItemClick getOnRecyclerViewItemClick() {
        return onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemLongClick(OnRecyclerViewItemLongClick onRecyclerViewItemLongClick) {
        this.onRecyclerViewItemLongClick = onRecyclerViewItemLongClick;
    }

    @Override
    protected void onBindItemViewHolder(VH holder, int position) {
        holder.setOnRecyclerViewItemClick(onRecyclerViewItemClick);
        holder.setOnRecyclerViewItemLongClick(onRecyclerViewItemLongClick);
    }
}
