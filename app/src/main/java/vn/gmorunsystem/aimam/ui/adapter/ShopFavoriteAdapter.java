package vn.gmorunsystem.aimam.ui.adapter;

import android.view.ViewGroup;

import java.util.List;

import vn.gmorunsystem.aimam.bean.SubscribeBean;
import vn.gmorunsystem.aimam.callback.OnUnsubscribe;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.RestaurantFavoriteViewHolder;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by HuongTrung on 5/24/2017.
 */

public class ShopFavoriteAdapter extends AdapterWithItemClick<RestaurantFavoriteViewHolder> {

    private OnUnsubscribe onUnsubscribe;


    public void setOnUnsubscribe(OnUnsubscribe onUnsubscribe) {
        this.onUnsubscribe = onUnsubscribe;
    }


    private List<SubscribeBean> subscribeBeanList;

    public ShopFavoriteAdapter(List<SubscribeBean> beanList) {
        this.subscribeBeanList = beanList;
    }

    @Override
    public RestaurantFavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RestaurantFavoriteViewHolder(UiUtil.inflate(parent, RestaurantFavoriteViewHolder.LAYOUT_ID, false));
    }

    @Override
    public void onBindViewHolder(RestaurantFavoriteViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(subscribeBeanList.get(position));
        holder.setOnUnsubscribe(onUnsubscribe);
    }

    @Override
    public int getItemCount() {
        return subscribeBeanList.size();
    }
}
