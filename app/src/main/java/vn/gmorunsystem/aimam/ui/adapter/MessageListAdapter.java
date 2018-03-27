package vn.gmorunsystem.aimam.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.data.MessageListData;
import vn.gmorunsystem.aimam.callback.OnDeleteListener;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.MessageListViewHolder;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by HuongTrung on 6/12/2017.
 */

public class MessageListAdapter extends RecyclerSwipeAdapter<MessageListViewHolder> {
    private List<MessageListData> messageListData;
    private OnDeleteListener onDeleteListener;

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public MessageListAdapter(List<MessageListData> messageListData) {
        this.messageListData = messageListData;
    }

    public void addMessage(List<MessageListData> messageList) {
        this.messageListData.addAll(messageList);
        notifyDataSetChanged();
    }

    @Override
    public MessageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageListViewHolder(UiUtil.inflate(parent, MessageListViewHolder.LAYOUT_ID, false));
    }

    @Override
    public void onBindViewHolder(final MessageListViewHolder holder, final int position) {
        holder.bind(messageListData.get(position));
        holder.setOnDeleteListener(onDeleteListener);
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(holder.swipeLayout);
                mItemManger.closeAllItems();
                if (onDeleteListener != null)
                    onDeleteListener.onDeleteMessage(position, messageListData.get(position).id);
            }
        });
        mItemManger.bindView(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return messageListData.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout;
    }
}
