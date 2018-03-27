package vn.gmorunsystem.aimam.ui.adapter;

import android.view.ViewGroup;

import java.util.List;

import vn.gmorunsystem.aimam.bean.CouponDetailBean;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.CouponListItemViewHolder;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by Veteran Commander on 5/18/2017.
 */

public class CouponListAdapter extends AdapterWithItemClick<CouponListItemViewHolder> {

    private List<CouponDetailBean> dataList;

    public CouponListAdapter(List<CouponDetailBean> beanList) {
        dataList = beanList;
    }

    public void addMoreCouponIntoList(List<CouponDetailBean> beanList) {
        this.dataList.addAll(beanList);
    }

    @Override
    public CouponListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CouponListItemViewHolder(UiUtil.inflate(parent, CouponListItemViewHolder.LAYOUT_ID, false));
    }

    @Override
    public void onBindViewHolder(CouponListItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
