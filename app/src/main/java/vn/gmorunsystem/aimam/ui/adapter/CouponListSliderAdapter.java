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

/**
 * Created by tiena on 5/19/2017.
 */

public class CouponListSliderAdapter extends PagerAdapter {

    @BindView(R.id.iv_item_header_cpList)
    ImageView ivItemHeader;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Integer> listImageId;

    public CouponListSliderAdapter(Context context, List<Integer> listImage) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listImageId = listImage;
    }


    @Override
    public int getCount() {
        return listImageId.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_slider_couponlist, container, false);
        ButterKnife.bind(this, itemView);
        ivItemHeader.setImageResource(listImageId.get(position));
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
