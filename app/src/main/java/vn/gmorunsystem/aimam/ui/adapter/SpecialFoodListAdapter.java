package vn.gmorunsystem.aimam.ui.adapter;

import android.view.ViewGroup;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopSpecialBean;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.SpecialFoodListViewHolder;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by HuongTrung on 5/19/2017.
 */

public class SpecialFoodListAdapter extends AdapterWithItemClick<SpecialFoodListViewHolder> {

    public List<ShopSpecialBean> shopSpecialBeanList;

    public SpecialFoodListAdapter(List<ShopSpecialBean> shopSpecialBeanList) {
        this.shopSpecialBeanList = shopSpecialBeanList;
    }

    @Override
    public SpecialFoodListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SpecialFoodListViewHolder(UiUtil.inflate(parent, SpecialFoodListViewHolder.LAYOUT_ID, false));
    }

    @Override
    public void onBindViewHolder(SpecialFoodListViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(shopSpecialBeanList.get(position));
    }

    @Override
    public int getItemCount() {
        return shopSpecialBeanList.size();
    }
}
