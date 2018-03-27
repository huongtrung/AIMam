package vn.gmorunsystem.aimam.ui.adapter.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.ShopCouponBean;
import vn.gmorunsystem.aimam.callback.OnShareSocialListener;
import vn.gmorunsystem.aimam.callback.OnTakeCouponListener;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by HuongTrung on 6/5/2017.
 */

public class ShopCouponListViewHolder extends OnClickViewHolder {
    public static int LAYOUT_ID = R.layout.item_coupon_list;
    @BindView(R.id.iv_food)
    ImageView ivFood;
    @BindView(R.id.tv_coupon_name)
    TextView tvCouponName;
    @BindView(R.id.tv_coupon_content)
    TextView tvCouponContent;
    @BindView(R.id.tv_expiry_date)
    TextView tvExpiryDate;
    @BindView(R.id.btn_get_code)
    Button btnGetCode;

    private OnTakeCouponListener onItemClick;
    private OnShareSocialListener shareSocialListener;

    private String imgUrlForShare;
    private String couponCode;
    private int couponId;

    public void setOnItemClick(OnTakeCouponListener onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setShareSocialListener(OnShareSocialListener shareSocialListener) {
        this.shareSocialListener = shareSocialListener;
    }

    public ShopCouponListViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(ShopCouponBean shopCouponBean) {
        couponId = shopCouponBean.id;
        couponCode = shopCouponBean.code;
        ImageLoader.loadImage(itemView.getContext(), R.drawable.default_img, shopCouponBean.avatarImageUrl, ivFood);
        imgUrlForShare = shopCouponBean.avatarImageUrl;
        StringUtil.displayText(shopCouponBean.title, tvCouponName);
        StringUtil.displayText(shopCouponBean.content, tvCouponContent);
        StringUtil.displayText(itemView.getResources().getString(R.string.expiry_date) + shopCouponBean.expiryDate, tvExpiryDate);
    }

    @OnClick(R.id.btn_get_code)
    public void takeCoupon() {
        if (!UiUtil.isClickable()) {
            return;
        }
        if (onItemClick != null) {
            onItemClick.onTakeCoupon(getAdapterPosition(), couponId);
        }
    }

    @OnClick(R.id.iv_fb)
    public void shareFb() {
        if (!UiUtil.isClickable()) {
            return;
        }
        if (shareSocialListener != null) {
            shareSocialListener.onShareFacebook(imgUrlForShare);
        }
    }

    @OnClick(R.id.iv_instagram)
    public void shareInstagram() {
        if (!UiUtil.isClickable()) {
            return;
        }
        if (shareSocialListener != null) {
            String fileName = couponId + "_" + couponCode;
            shareSocialListener.onShareInstagram(imgUrlForShare, fileName);
        }
    }
}
