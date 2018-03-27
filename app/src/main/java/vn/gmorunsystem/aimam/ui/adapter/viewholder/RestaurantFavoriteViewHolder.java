package vn.gmorunsystem.aimam.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.SubscribeBean;
import vn.gmorunsystem.aimam.callback.OnUnsubscribe;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.StringUtil;

/**
 * Created by HuongTrung on 5/24/2017.
 */

public class RestaurantFavoriteViewHolder extends OnClickViewHolder {
    public static final int LAYOUT_ID = R.layout.item_list_restaurant;

    private OnUnsubscribe onUnsubscribe;

    private SubscribeBean subscribeBean;

    public void setOnUnsubscribe(OnUnsubscribe onUnsubscribe) {
        this.onUnsubscribe = onUnsubscribe;
    }

    @BindView(R.id.iv_restaurant)
    ImageView ivRestaurant;
    @BindView(R.id.tv_restaurant_name)
    TextView tvName;
    @BindView(R.id.tv_sample)
    TextView tvSample;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_count_views)
    TextView tvViews;
    @BindView(R.id.tv_count_likes)
    TextView tvLikes;

    public RestaurantFavoriteViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(SubscribeBean bean) {
        subscribeBean = bean;
        StringUtil.displayText("AAAAAAA", tvSample);
        StringUtil.displayText("AAAAAAA", tvContent);
        StringUtil.displayText(bean.name, tvName);
        StringUtil.displayText(itemView.getResources().getString(R.string.txt_subscribe) + bean.like, tvLikes);
        StringUtil.displayText(itemView.getResources().getString(R.string.txt_view) + bean.view, tvViews);
        ImageLoader.loadImage(itemView.getContext(), R.drawable.default_img, bean.avatarImageUrl, ivRestaurant);
    }

    @OnClick(R.id.btn_unsubscribe)
    public void clickUnsub() {
        if (onUnsubscribe != null) {
            onUnsubscribe.unSubscribe(subscribeBean.shopId, getAdapterPosition());
        }
    }

}
