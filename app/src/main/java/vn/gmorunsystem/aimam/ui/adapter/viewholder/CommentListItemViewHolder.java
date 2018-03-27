package vn.gmorunsystem.aimam.ui.adapter.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.ReviewBean;
import vn.gmorunsystem.aimam.callback.OnViewInReviewRecyclerClicked;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.StringUtil;

/**
 * Created by Veteran Commander on 5/22/2017.
 */

public class CommentListItemViewHolder extends OnClickViewHolder {
    public static final int LAYOUT_ID = R.layout.layout_review_cmlist_item;
    @BindView(R.id.iv_cmter_ava)
    CircleImageView ivCmterAva;
    @BindView(R.id.tv_cmter_name)
    ExpandableTextView tvCmterName;
    @BindView(R.id.tv_cm_content)
    ExpandableTextView tvCmContent;
    @BindView(R.id.tv_date_cm)
    TextView tvDate;
    @BindView(R.id.bt_useful)
    Button btUseful;

    private ReviewBean reviewBean;
    private boolean mUseful;

    private OnViewInReviewRecyclerClicked onViewInReviewRecyclerClicked;

    public void setOnViewInReviewRecyclerClicked(OnViewInReviewRecyclerClicked onViewInReviewRecyclerClicked) {
        this.onViewInReviewRecyclerClicked = onViewInReviewRecyclerClicked;
    }

    public CommentListItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(ReviewBean bean) {
        this.reviewBean = bean;
        if (reviewBean.votedUseful == APPConstant.FRAG_ZERO) {
            setUseful(false);
        } else {
            setUseful(true);
        }
        StringUtil.displayText(bean.userName, tvCmterName);
        StringUtil.displayText(bean.content, tvCmContent);
        StringUtil.displayText(bean.reviewDate, tvDate);
        ImageLoader.loadImage(itemView.getContext(), R.drawable.default_img, bean.userAvartarUrl, ivCmterAva);
    }

    @OnClick({R.id.tv_cm_content, R.id.tv_cmter_name, R.id.bt_useful})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cm_content:
                setExpandable(tvCmContent);
                break;
            case R.id.tv_cmter_name:
                setExpandable(tvCmterName);
                break;
            case R.id.bt_useful:
                if (onViewInReviewRecyclerClicked != null) {
                    onViewInReviewRecyclerClicked.onUseful(reviewBean.id, getAdapterPosition(), mUseful);
                }
        }
    }

    private void setUseful(boolean isUseful) {
        mUseful = isUseful;
        if (btUseful != null) {
            if (isUseful) {
                StringUtil.displayText(itemView.getResources().getString(R.string.title_un_useful) + " " + reviewBean.useful, btUseful);
            } else {
                StringUtil.displayText(itemView.getResources().getString(R.string.title_useful) + " " + reviewBean.useful, btUseful);
            }
        }
    }

    @OnLongClick(R.id.tv_cm_content)
    public boolean UpdateComment() {
        return onLongClick(tvCmContent);
    }

    private void setExpandable(ExpandableTextView expandableTextView) {
        if (expandableTextView.isExpanded()) {
            expandableTextView.collapse();
        } else {
            expandableTextView.expand();
        }
    }
}
