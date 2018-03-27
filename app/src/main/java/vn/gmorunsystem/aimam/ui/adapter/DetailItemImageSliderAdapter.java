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
import vn.gmorunsystem.aimam.bean.data.ImageBean;
import vn.gmorunsystem.aimam.utils.ImageLoader;

/**
 * Created by Veteran Commander on 6/15/2017.
 */

public class DetailItemImageSliderAdapter extends PagerAdapter {
    List<ImageBean> imageSliderDetailBeanList;
    Context mContext;
    LayoutInflater mLayoutInflater;
    @BindView(R.id.iv_restaurant)
    ImageView ivRestaurant;

    public DetailItemImageSliderAdapter(Context mContext, List<ImageBean> imageSliderDetailBeanList) {
        this.imageSliderDetailBeanList = imageSliderDetailBeanList;
        this.mContext = mContext;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageSliderDetailBeanList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_slide_detail_item, container, false);
        ButterKnife.bind(this, itemView);
        ImageLoader.loadImage(mContext, R.drawable.default_img, imageSliderDetailBeanList.get(position).avatarImageUrl, ivRestaurant);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
