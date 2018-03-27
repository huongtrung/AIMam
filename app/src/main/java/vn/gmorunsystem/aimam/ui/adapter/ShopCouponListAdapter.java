package vn.gmorunsystem.aimam.ui.adapter;

import android.view.ViewGroup;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopCouponBean;
import vn.gmorunsystem.aimam.callback.OnShareSocialListener;
import vn.gmorunsystem.aimam.callback.OnTakeCouponListener;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.ShopCouponListViewHolder;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by HuongTrung on 6/5/2017.
 */

public class ShopCouponListAdapter extends AdapterWithItemClick<ShopCouponListViewHolder> {

    private List<ShopCouponBean> shopCouponBeanList;
    private OnTakeCouponListener onItemClickListener;
    private OnShareSocialListener onShareSocialListener;

    public void setOnItemClickListener(OnTakeCouponListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnShareSocialListener(OnShareSocialListener onShareSocialListener) {
        this.onShareSocialListener = onShareSocialListener;
    }

    public ShopCouponListAdapter(List<ShopCouponBean> shopCouponBeanList) {
        this.shopCouponBeanList = shopCouponBeanList;
    }

    @Override
    public ShopCouponListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShopCouponListViewHolder(UiUtil.inflate(parent, ShopCouponListViewHolder.LAYOUT_ID, false));
    }

    public void removeCoupon(int position) {
        shopCouponBeanList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, shopCouponBeanList.size());
    }

    @Override
    public void onBindViewHolder(ShopCouponListViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(shopCouponBeanList.get(position));
        holder.setOnItemClick(onItemClickListener);
        holder.setShareSocialListener(onShareSocialListener);
    }

    @Override
    public int getItemCount() {
        return shopCouponBeanList.size();
    }
}
