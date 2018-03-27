package vn.gmorunsystem.aimam.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.OnClick;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.data.MessageListData;
import vn.gmorunsystem.aimam.callback.OnDeleteListener;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.StringUtil;

/**
 * Created by HuongTrung on 6/12/2017.
 */

public class MessageListViewHolder extends OnClickViewHolder {
    public static int LAYOUT_ID = R.layout.item_list_message;
    @BindView(R.id.swipe_layout)
    public SwipeLayout swipeLayout;
    @BindView(R.id.tv_delete)
    public TextView tvDelete;
    @BindView(R.id.iv_restaurant)
    ImageView ivShop;
    @BindView(R.id.tv_name)
    ExpandableTextView tvShopName;
    @BindView(R.id.tv_title)
    ExpandableTextView tvMessageTitle;
    @BindView(R.id.tv_content)
    ExpandableTextView tvMessageContent;
    @BindView(R.id.tv_time)
    TextView tvTime;

    private OnDeleteListener onDeleteListener;

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public MessageListViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(MessageListData data) {
        ImageLoader.loadImage(itemView.getContext(), R.drawable.default_img, data.shop.logo, ivShop);
        StringUtil.displayText(data.shop.name, tvShopName);
        StringUtil.displayText(data.title, tvMessageTitle);
        StringUtil.displayText(data.content, tvMessageContent);
        StringUtil.displayText(data.sentDate, tvTime);
    }

    @OnClick(R.id.rl_content)
    void onExpand() {
        setExpandable(tvShopName);
        setExpandable(tvMessageTitle);
        setExpandable(tvMessageContent);
    }

    private void setExpandable(ExpandableTextView expandableTextView) {
        if (expandableTextView.isExpanded()) {
            expandableTextView.collapse();
        } else {
            expandableTextView.expand();
        }
    }
}
