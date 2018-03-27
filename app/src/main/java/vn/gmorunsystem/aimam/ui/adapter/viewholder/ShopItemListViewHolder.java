package vn.gmorunsystem.aimam.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.ShopItemBean;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.StringUtil;

/**
 * Created by HuongTrung on 5/25/2017.
 */

public class ShopItemListViewHolder extends OnClickViewHolder {
    public static int LAYOUT_ID = R.layout.item_search_list;

    @BindView(R.id.tv_search_title)
    TextView tvTitle;
    @BindView(R.id.iv_shop_item)
    ImageView ivShopItem;
    @BindView(R.id.rtb_rate)
    SimpleRatingBar ratingBar;
    @BindView(R.id.tv_distance)
    TextView tvDistance;

    public ShopItemListViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(ShopItemBean shopItemBean, String distance) {
        ratingBar.setIndicator(true);
        ratingBar.setRating(shopItemBean.vote);
        ImageLoader.loadImage(itemView.getContext(), R.drawable.default_img, shopItemBean.avatarImageUrl, ivShopItem);
        StringUtil.displayText(shopItemBean.name, tvTitle);
        StringUtil.displayText(distance, tvDistance);
    }
}
