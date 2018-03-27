package vn.gmorunsystem.aimam.ui.adapter;

import android.view.ViewGroup;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopNewBean;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.NewFoodListViewHolder;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by HuongTrung on 5/19/2017.
 */

public class NewFoodListAdapter extends AdapterWithItemClick<NewFoodListViewHolder> {

    private List<ShopNewBean> shopNewBeanList;

    public NewFoodListAdapter(List<ShopNewBean> shopNewBeanList) {
        this.shopNewBeanList = shopNewBeanList;
    }

    @Override
    public NewFoodListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewFoodListViewHolder(UiUtil.inflate(parent, NewFoodListViewHolder.LAYOUT_ID, false));
    }

    @Override
    public void onBindViewHolder(NewFoodListViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(shopNewBeanList.get(position));
    }

    @Override
    public int getItemCount() {
        return shopNewBeanList.size();
    }
}
