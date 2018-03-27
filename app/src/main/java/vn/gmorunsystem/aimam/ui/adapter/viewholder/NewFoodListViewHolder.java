package vn.gmorunsystem.aimam.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.ShopNewBean;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.StringUtil;

/**
 * Created by HuongTrung on 6/5/2017.
 */

public class NewFoodListViewHolder extends OnClickViewHolder {
    public static final int LAYOUT_ID = R.layout.item_list_food;

    @BindView(R.id.iv_food_list)
    ImageView ivFoodList;
    @BindView(R.id.tv_food_name)
    TextView tvFoodName;
    @BindView(R.id.tv_food_intro)
    TextView tvFoodIntro;
    @BindView(R.id.tv_like)
    TextView tvFoodLike;
    @BindView(R.id.tv_view)
    TextView tvFoodView;
    @BindView(R.id.rating_bar)
    SimpleRatingBar ratingBar;
    @BindView(R.id.tv_price)
    TextView tvPrice;

    public NewFoodListViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(ShopNewBean shopNewBean) {
        ratingBar.setIndicator(true);
        ratingBar.setRating(shopNewBean.vote);
        ImageLoader.loadImage(itemView.getContext(), R.drawable.default_img, shopNewBean.avatarImageUrl, ivFoodList);
        StringUtil.displayText(shopNewBean.name, tvFoodName);
        StringUtil.displayText(shopNewBean.description, tvFoodIntro);
        StringUtil.displayTextEqualZero(shopNewBean.view, tvFoodView);
        StringUtil.displayTextEqualZero(shopNewBean.like, tvFoodLike);
        StringUtil.displayText(StringUtil.formatNumber(shopNewBean.price), tvPrice);
    }
}
