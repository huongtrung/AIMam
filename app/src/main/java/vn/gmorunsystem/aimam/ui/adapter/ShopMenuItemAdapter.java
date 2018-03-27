package vn.gmorunsystem.aimam.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.ShopMenuItem;
import vn.gmorunsystem.aimam.callback.OnItemClickListener;
import vn.gmorunsystem.aimam.utils.StringUtil;

/**
 * Created by adm on 8/30/2017.
 */

public class ShopMenuItemAdapter extends ArrayAdapter<ShopMenuItem> {

    private ArrayList<ShopMenuItem> items;
    private ArrayList<ShopMenuItem> itemsAll;
    private ArrayList<ShopMenuItem> suggestions;
    private int viewResourceId;

    private OnItemClickListener mListener;

    public void setListener(OnItemClickListener mListener) {
        this.mListener = mListener;
    }

    @SuppressWarnings("unchecked")
    public ShopMenuItemAdapter(Context context, int viewResourceId,
                               ArrayList<ShopMenuItem> items) {
        super(context, viewResourceId, items);
        if (items != null) {
            this.items = items;
            this.itemsAll = (ArrayList<ShopMenuItem>) items.clone();
        }
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
            final ShopMenuItem shopMenuItem = items.get(position);
            if (shopMenuItem != null) {
                final LinearLayout llShopMenuItem = (LinearLayout) view.findViewById(R.id.ll_menu_item);
                TextView tvShopItemName = (TextView) view.findViewById(R.id.tv_shop_item_name);
                StringUtil.displayText(shopMenuItem.getName(), tvShopItemName);
                llShopMenuItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.onClick(position, shopMenuItem.id);
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
            return ((ShopMenuItem) (resultValue)).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (ShopMenuItem shop : itemsAll) {
                    if (shop.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(shop);
                    }
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
            ArrayList<ShopMenuItem> filteredList = (ArrayList<ShopMenuItem>) results.values;
            if (results.count > 0) {
                clear();
                for (ShopMenuItem c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };
}
