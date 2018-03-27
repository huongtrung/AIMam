package vn.gmorunsystem.aimam.ui.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.CouponDetailBean;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.StringUtil;

/**
 * Created by Veteran Commander on 5/18/2017.
 */

public class CouponListItemViewHolder extends OnClickViewHolder {

    public static final int LAYOUT_ID = R.layout.layout_couponlist_item;

    @BindView(R.id.iv_imgsample)
    ImageView ivSample;
    @BindView(R.id.tv_title_orange)
    TextView tvTitle;
    @BindView(R.id.tv_content_white)
    TextView tvContent;
    @BindView(R.id.tv_restaurant)
    TextView tvRestaurant;

    public CouponListItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(CouponDetailBean data) {
        StringUtil.displayText(data.shopBean.name, tvRestaurant);
        StringUtil.displayText(data.title, tvTitle);
        StringUtil.displayText(data.content, tvContent);
        if (StringUtil.checkStringValid(data.shopBean.logo)) {
            ImageLoader.loadImage(itemView.getContext(), R.drawable.default_img, data.shopBean.logo, ivSample);
        }

    }

}
