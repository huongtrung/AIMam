package vn.gmorunsystem.aimam.ui.adapter;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopSuggestBean;
import vn.gmorunsystem.aimam.bean.data.ShopSuggestListParcel;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.ui.fragment.HomeShopListFragment;
import vn.gmorunsystem.aimam.ui.fragment.SearchShopListFragment;

/**
 * Created by Veteran Commander on 6/16/2017.
 */

public class ShopSuggestViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<ShopSuggestBean> shopSuggestBeanList;
    private ShopSuggestListParcel shopSuggestBeanList1;
    private ShopSuggestListParcel shopSuggestBeanList2;
    private ShopSuggestListParcel shopSuggestBeanList3;
    private Location mUserLocation;
    private int statusScreen;
    private int mSize;
    private int pageCount;

    public ShopSuggestViewPagerAdapter(FragmentManager fm, List<ShopSuggestBean> beanList, Location userLocation, int status) {
        super(fm);
        statusScreen = status;
        mUserLocation = userLocation;
        shopSuggestBeanList = beanList;
        mSize = shopSuggestBeanList.size();
        for (int i = 0; i < mSize; i++) {
            ShopSuggestBean item = shopSuggestBeanList.get(i);
            if (i <= 2) {
                if (shopSuggestBeanList1 == null) {
                    shopSuggestBeanList1 = new ShopSuggestListParcel();
                }
                shopSuggestBeanList1.add(item);
            }
            if (i > 2 && i <= 5) {
                if (shopSuggestBeanList2 == null) {
                    shopSuggestBeanList2 = new ShopSuggestListParcel();
                }
                shopSuggestBeanList2.add(item);
            }
            if (i > 5) {
                if (shopSuggestBeanList3 == null) {
                    shopSuggestBeanList3 = new ShopSuggestListParcel();
                }
                shopSuggestBeanList3.add(item);
            }
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (statusScreen == APPConstant.FRAG_MAIN) {
            switch (position) {
                case 0:
                    return HomeShopListFragment.newInstance(shopSuggestBeanList1, mUserLocation, true);
                case 1:
                    return HomeShopListFragment.newInstance(shopSuggestBeanList2, mUserLocation, false);
                case 2:
                    return HomeShopListFragment.newInstance(shopSuggestBeanList3, mUserLocation, true);
                default:
                    return HomeShopListFragment.newInstance(shopSuggestBeanList1, mUserLocation, true);
            }
        } else {
            switch (position) {
                case 0:
                    return SearchShopListFragment.newInstance(shopSuggestBeanList1, mUserLocation, true);
                case 1:
                    return SearchShopListFragment.newInstance(shopSuggestBeanList2, mUserLocation, false);
                case 2:
                    return SearchShopListFragment.newInstance(shopSuggestBeanList3, mUserLocation, true);
                default:
                    return SearchShopListFragment.newInstance(shopSuggestBeanList1, mUserLocation, true);
            }
        }
    }

    @Override
    public int getCount() {
        // page count max value = 9 and each page have 3 element
        switch (mSize) {
            case 1:
            case 2:
            case 3:
                pageCount = 1;
                break;
            case 4:
            case 5:
            case 6:
                pageCount = 2;
                break;
            case 7:
            case 8:
            case 9:
                pageCount = 3;
                break;
        }
        return pageCount;
    }
}
