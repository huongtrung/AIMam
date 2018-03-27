package vn.gmorunsystem.aimam.ui.adapter.viewholder;

import android.location.Location;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.NewFeedBean;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;


/**
 * Created by HuongTrung on 09/01/2017.
 */

public class NewFeedViewHolder extends OnClickViewHolder {
    public static final int LAYOUT_ID = R.layout.item_new_feed;

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.iv_type)
    ImageView ivType;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.ll_distance)
    LinearLayout llDistance;
    @BindView(R.id.tv_distance)
    TextView tvDistance;

    private Location mLocation;
    private NewFeedBean newFeedBean;
    private int mType;
    private OnItemClickListener mListener;

    public void setListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    public NewFeedViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(NewFeedBean data, Location location) {
        Float lat = data.shopLocation.mapLatitude;
        Float lng = data.shopLocation.mapLongtitude;
        this.newFeedBean = data;
        mLocation = location;
        mType = data.type;

        ImageLoader.loadImage(itemView.getContext(), R.drawable.default_img, data.imageUrl, ivImage);
        StringUtil.displayText(data.description, tvDescription);
        if (lat != null || lng != null)
            StringUtil.displayText(setShopLocation(lat, lng), tvDistance);
        else
            UiUtil.hideView(llDistance, true);

        switch (mType) {
            case APPConstant.TYPE_SHOP:
                ivType.setImageResource(R.drawable.ic_shop_white);
                ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                StringUtil.displayText(data.shopName, tvName);
                break;
            case APPConstant.TYPE_COUPON:
                ivType.setImageResource(R.drawable.ic_coupon_white);
                ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                StringUtil.displayText(data.couponName, tvName);
                break;
            case APPConstant.TYPE_ITEM:
                ivType.setImageResource(R.drawable.ic_item_white);
                ivImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                StringUtil.displayText(data.itemName, tvName);
                break;
        }
    }

    @OnClick(R.id.iv_image)
    public void OnClick(View v) {
        if (mListener != null) {
            switch (mType) {
                case APPConstant.TYPE_SHOP:
                    mListener.onClick(mType, newFeedBean.shopId);
                    break;
                case APPConstant.TYPE_COUPON:
                    mListener.onClick(mType, newFeedBean.couponId);
                    break;
                case APPConstant.TYPE_ITEM:
                    mListener.onClick(mType, newFeedBean.itemId);
                    break;
            }
        }
    }

    private String setShopLocation(double latitude, double longitude) {
        Float distance = 0.0f;
        Location shopLocation = new Location("");
        shopLocation.setLatitude(latitude);
        shopLocation.setLongitude(longitude);
        if (mLocation != null) {
            distance = mLocation.distanceTo(shopLocation);
        } else {
            UiUtil.hideView(llDistance, true);
        }
        return StringUtil.convertDistanceToString(distance);
    }

    public interface OnItemClickListener {
        void onClick(int type, int id);
    }
}
