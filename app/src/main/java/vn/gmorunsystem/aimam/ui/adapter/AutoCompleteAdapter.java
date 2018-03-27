package vn.gmorunsystem.aimam.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.ShopSearchBean;
import vn.gmorunsystem.aimam.callback.OnItemClickListener;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.StringUtil;

/**
 * Created by HuongTrung on 6/20/2017.
 */

public class AutoCompleteAdapter extends ArrayAdapter<ShopSearchBean> {
    private ArrayList<ShopSearchBean> items;
    private ArrayList<ShopSearchBean> itemsAll;
    private ArrayList<ShopSearchBean> suggestions;
    private int viewResourceId;

    private OnItemClickListener mListener;

    public void setListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    @SuppressWarnings("unchecked")
    public AutoCompleteAdapter(Context context, int viewResourceId,
                               ArrayList<ShopSearchBean> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<ShopSearchBean>) items.clone();
        this.suggestions = new ArrayList<>();
        this.viewResourceId = viewResourceId;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(viewResourceId, null);
        }
        if (!items.isEmpty()) {
            final ShopSearchBean shopSearchBean = items.get(position);
            if (shopSearchBean != null) {
                LinearLayout llShopSearch = (LinearLayout) view.findViewById(R.id.ll_shop_search);
                ImageView ivShop = (ImageView) view.findViewById(R.id.iv_shop);
                TextView tvShopName = (TextView) view.findViewById(R.id.tv_shop_name);
                ImageLoader.loadImage(getContext(), R.drawable.default_img, shopSearchBean.getAvatarImageUrl(), ivShop);
                StringUtil.displayText(shopSearchBean.getName(), tvShopName);

                llShopSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.onClick(position, shopSearchBean.id);
                        }
                    }
                });
            }
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private Filter nameFilter = new Filter() {
        public String convertResultToString(Object resultValue) {
            return ((ShopSearchBean) (resultValue)).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ShopSearchBean shop : itemsAll) {
                    suggestions.add(shop);
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            @SuppressWarnings("unchecked")
            ArrayList<ShopSearchBean> filteredList = (ArrayList<ShopSearchBean>) results.values;
            if (results.count > 0) {
                clear();
                for (ShopSearchBean c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}

