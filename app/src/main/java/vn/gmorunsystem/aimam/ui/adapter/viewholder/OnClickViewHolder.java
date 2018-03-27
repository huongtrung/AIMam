package vn.gmorunsystem.aimam.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
import vn.gmorunsystem.aimam.callback.OnRecyclerViewItemLongClick;
import vn.gmorunsystem.aimam.ui.adapter.OnRecyclerViewItemClick;

/**
 * Created on 8/14/2015.
 */
public class OnClickViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    OnRecyclerViewItemClick onRecyclerViewItemClick;
    OnRecyclerViewItemLongClick onRecyclerViewItemLongClick;

    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    public void setOnRecyclerViewItemLongClick(OnRecyclerViewItemLongClick onRecyclerViewItemLongClick) {
        this.onRecyclerViewItemLongClick = onRecyclerViewItemLongClick;
    }

    public OnClickViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (onRecyclerViewItemClick != null) {
            onRecyclerViewItemClick.onItemClick(view, getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (onRecyclerViewItemLongClick != null) {
            onRecyclerViewItemLongClick.onItemLongClick(view, getAdapterPosition());
            return true;
        } else return false;
    }
}
