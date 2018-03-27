package vn.gmorunsystem.aimam.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.HomeFavoriteBean;
import vn.gmorunsystem.aimam.ui.adapter.OnRecyclerViewItemClick;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.StringUtil;

/**
 * Created by Veteran Commander on 6/14/2017.
 */
public class HomeFavItemViewHolder extends OnClickViewHolder {
    public static final int LAYOUT_ID = R.layout.layout_home_item_fav;

    @BindView(R.id.iv_home_fav_item)
    ImageView ivHomeFavItem;
    @BindView(R.id.tv_food_name)
    TextView tvFoodName;

    @Override
    public void setOnRecyclerViewItemClick(OnRecyclerViewItemClick onRecyclerViewItemClick) {
        this.onRecyclerViewItemClick = onRecyclerViewItemClick;
    }

    public HomeFavItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(HomeFavoriteBean favoriteBean) {
        ImageLoader.loadImage(itemView.getContext(), R.drawable.default_img, favoriteBean.avatarImageUrl, ivHomeFavItem);
        StringUtil.displayText(favoriteBean.name, tvFoodName);
    }
}
