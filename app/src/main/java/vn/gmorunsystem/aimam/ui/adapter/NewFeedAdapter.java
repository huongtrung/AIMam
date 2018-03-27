package vn.gmorunsystem.aimam.ui.adapter;

import android.location.Location;
import android.view.ViewGroup;

import java.util.List;

import vn.gmorunsystem.aimam.bean.NewFeedBean;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.NewFeedViewHolder;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by HuongTrung on 09/01/2017.
 */

public class NewFeedAdapter extends AdapterWithItemClick<NewFeedViewHolder> {
    private List<NewFeedBean> newFeedList;
    private NewFeedViewHolder.OnItemClickListener onItemClickListener;
    private Location location;

    public NewFeedAdapter(List<NewFeedBean> newFeedList, Location location) {
        this.location = location;
        this.newFeedList = newFeedList;
    }

    public void setOnItemClickListener(NewFeedViewHolder.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public NewFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewFeedViewHolder(UiUtil.inflate(parent, NewFeedViewHolder.LAYOUT_ID, false));
    }

    @Override
    public void onBindViewHolder(NewFeedViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(newFeedList.get(position), location);
        holder.setListener(onItemClickListener);
    }

    public void addItemNewFeed(List<NewFeedBean> newFeedList) {
        this.newFeedList.addAll(newFeedList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return newFeedList.size();
    }
}
