package vn.gmorunsystem.aimam.ui.adapter;

import android.view.ViewGroup;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ReviewBean;
import vn.gmorunsystem.aimam.callback.OnViewInReviewRecyclerClicked;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.CommentListItemViewHolder;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by Veteran Commander on 5/22/2017.
 */

public class CommentListAdapter extends AdapterWithItemClick<CommentListItemViewHolder> {

    private OnViewInReviewRecyclerClicked onViewInReviewRecyclerClicked;

    private List<ReviewBean> reviewBeanList;

    public CommentListAdapter(List<ReviewBean> beanList) {
        reviewBeanList = beanList;
    }

    public void setOnViewInReviewRecyclerClicked(OnViewInReviewRecyclerClicked onViewInReviewRecyclerClicked) {
        this.onViewInReviewRecyclerClicked = onViewInReviewRecyclerClicked;
    }

    @Override
    public CommentListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentListItemViewHolder(UiUtil.inflate(parent, CommentListItemViewHolder.LAYOUT_ID, false));
    }

    @Override
    public void onBindViewHolder(CommentListItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(reviewBeanList.get(position));
        holder.setOnViewInReviewRecyclerClicked(onViewInReviewRecyclerClicked);
    }

    public void addCommentList(List<ReviewBean> reviewBeanList) {
        this.reviewBeanList.addAll(reviewBeanList);
        notifyDataSetChanged();
    }

    public void addCommentItem(ReviewBean reviewBeanItem) {
        this.reviewBeanList.add(0, reviewBeanItem);
        notifyItemInserted(0);
    }

    public void removeCommentItem(int position) {
        reviewBeanList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, reviewBeanList.size());
    }

    public void updateCommentItem(int position, ReviewBean reviewBean) {
        reviewBeanList.set(position, reviewBean);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return reviewBeanList.size();
    }
}
