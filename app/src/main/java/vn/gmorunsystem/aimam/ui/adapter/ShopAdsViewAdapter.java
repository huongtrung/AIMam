package vn.gmorunsystem.aimam.ui.adapter;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import vn.gmorunsystem.aimam.bean.ShopAdvBean;
import vn.gmorunsystem.aimam.bean.data.ShopAdvListParcel;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.ui.fragment.HomeShopListFragment;
import vn.gmorunsystem.aimam.ui.fragment.SearchShopListFragment;

/**
 * Created by Veteran Commander on 6/16/2017.
 */

public class ShopAdsViewAdapter extends FragmentStatePagerAdapter {
    private List<ShopAdvBean> shopAdvBeanList;
    private ShopAdvListParcel shopAdvBeanList1;
    private ShopAdvListParcel shopAdvBeanList2;
    private ShopAdvListParcel shopAdvBeanList3;
    private Location mUserLocation;
    private int statusScreen;
    private int mSize;
    private int pageCount;

    public ShopAdsViewAdapter(FragmentManager fm, List<ShopAdvBean> beanList, Location userLocation, int status) {
        super(fm);
        statusScreen = status;
        mUserLocation = userLocation;
        shopAdvBeanList = beanList;
        mSize = shopAdvBeanList.size();
        for (int i = 0; i < mSize; i++) {
            ShopAdvBean item = shopAdvBeanList.get(i);
            if (i <= 2) {
                if (shopAdvBeanList1 == null) {
                    shopAdvBeanList1 = new ShopAdvListParcel();
                }
                shopAdvBeanList1.add(item);
            }
            if (i > 2 && i <= 5) {
                if (shopAdvBeanList2 == null) {
                    shopAdvBeanList2 = new ShopAdvListParcel();
                }
                shopAdvBeanList2.add(item);
            }
            if (i > 5) {
                if (shopAdvBeanList3 == null) {
                    shopAdvBeanList3 = new ShopAdvListParcel();
                }
                shopAdvBeanList3.add(item);
            }
        }

    }

    @Override
    public Fragment getItem(int position) {
        if (statusScreen == APPConstant.FRAG_MAIN) {
            switch (position) {
                case 0:
                    return HomeShopListFragment.newInstance(shopAdvBeanList1, mUserLocation, true);
                case 1:
                    return HomeShopListFragment.newInstance(shopAdvBeanList2, mUserLocation, false);
                case 2:
                    return HomeShopListFragment.newInstance(shopAdvBeanList3, mUserLocation, true);
                default:
                    return HomeShopListFragment.newInstance(shopAdvBeanList1, mUserLocation, false);
            }
        } else {
            switch (position) {
                case 0:
                    return SearchShopListFragment.newInstance(shopAdvBeanList1, mUserLocation, true);
                case 1:
                    return SearchShopListFragment.newInstance(shopAdvBeanList2, mUserLocation, false);
                case 2:
                    return SearchShopListFragment.newInstance(shopAdvBeanList3, mUserLocation, true);
                default:
                    return SearchShopListFragment.newInstance(shopAdvBeanList1, mUserLocation, false);
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
