package vn.gmorunsystem.aimam.ui.adapter;

import android.support.v7.widget.RecyclerView;

import vn.gmorunsystem.aimam.callback.OnRecyclerViewItemLongClick;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.OnClickViewHolder;


/**
 * Created on 9/21/2015.
 */
public abstract class AdapterWithItemClick<VH extends OnClickViewHolder> extends RecyclerView.Adapter<VH> {

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
    public void onBindViewHolder(VH holder, int position) {
        holder.setOnRecyclerViewItemClick(onRecyclerViewItemClick);
        holder.setOnRecyclerViewItemLongClick(onRecyclerViewItemLongClick);
    }
}
