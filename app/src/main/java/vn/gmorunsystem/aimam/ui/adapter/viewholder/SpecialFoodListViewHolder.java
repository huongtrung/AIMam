package vn.gmorunsystem.aimam.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.ShopSpecialBean;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.StringUtil;

/**
 * Created by HuongTrung on 5/19/2017.
 */

public class SpecialFoodListViewHolder extends OnClickViewHolder {

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


    public SpecialFoodListViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(ShopSpecialBean shopSpecialBean) {
        ratingBar.setIndicator(true);
        ratingBar.setRating(shopSpecialBean.vote);
        ImageLoader.loadImage(itemView.getContext(), R.drawable.default_img, shopSpecialBean.avatarImageUrl, ivFoodList);
        StringUtil.displayText(shopSpecialBean.name, tvFoodName);
        StringUtil.displayText(shopSpecialBean.description, tvFoodIntro);
        StringUtil.displayTextEqualZero(shopSpecialBean.view, tvFoodView);
        StringUtil.displayTextEqualZero(shopSpecialBean.like, tvFoodLike);
        StringUtil.displayText(StringUtil.formatNumber(shopSpecialBean.price), tvPrice);
    }
}
