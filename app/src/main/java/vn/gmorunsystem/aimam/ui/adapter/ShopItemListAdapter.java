package vn.gmorunsystem.aimam.ui.adapter;

import android.view.ViewGroup;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopItemBean;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.ShopItemListViewHolder;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by HuongTrung on 5/25/2017.
 */

public class ShopItemListAdapter extends AdapterWithItemClick<ShopItemListViewHolder> {
    private List<ShopItemBean> shopItemBean;
    private String mDistance;

    public ShopItemListAdapter(List<ShopItemBean> shopItemBean, String distance) {
        this.shopItemBean = shopItemBean;
        this.mDistance = distance;
    }

    @Override
    public ShopItemListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShopItemListViewHolder(UiUtil.inflate(parent, ShopItemListViewHolder.LAYOUT_ID, false));
    }

    @Override
    public void onBindViewHolder(ShopItemListViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(shopItemBean.get(position), mDistance);
    }

    @Override
    public int getItemCount() {
        return shopItemBean.size();
    }
}
