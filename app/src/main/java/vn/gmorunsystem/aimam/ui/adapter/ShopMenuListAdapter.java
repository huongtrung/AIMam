package vn.gmorunsystem.aimam.ui.adapter;

import android.view.ViewGroup;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopMenuBean;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.ShopMenuListViewHolder;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by HuongTrung on 5/19/2017.
 */

public class ShopMenuListAdapter extends AdapterWithItemClick<ShopMenuListViewHolder> {

    public List<ShopMenuBean> shopMenuBeanList;

    public ShopMenuListAdapter(List<ShopMenuBean> shopMenuBeanList) {
        this.shopMenuBeanList = shopMenuBeanList;
    }

    @Override
    public ShopMenuListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShopMenuListViewHolder(UiUtil.inflate(parent, ShopMenuListViewHolder.LAYOUT_ID, false));
    }

    @Override
    public void onBindViewHolder(ShopMenuListViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(shopMenuBeanList.get(position));
    }

    public void addItem(List<ShopMenuBean> shopMenuList) {
        this.shopMenuBeanList.addAll(shopMenuList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return shopMenuBeanList.size();
    }
}
