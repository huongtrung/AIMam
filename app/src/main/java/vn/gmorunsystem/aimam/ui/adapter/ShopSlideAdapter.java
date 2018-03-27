package vn.gmorunsystem.aimam.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.ShopSlideShowBean;
import vn.gmorunsystem.aimam.utils.ImageLoader;

/**
 * Created by HuongTrung on 5/18/2017.
 */

public class ShopSlideAdapter extends PagerAdapter {
    @BindView(R.id.iv_restaurant)
    ImageView ivRestaurant;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<ShopSlideShowBean> mShopSlideShow;

    public ShopSlideAdapter(Context context, List<ShopSlideShowBean> shopSlideShowBean) {
        mContext = context;
        this.mShopSlideShow = shopSlideShowBean;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mShopSlideShow.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_slide_restaurant, container, false);
        ButterKnife.bind(this, itemView);
        ImageLoader.loadImage(mContext, R.drawable.default_img, mShopSlideShow.get(position).url, ivRestaurant);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
