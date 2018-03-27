package vn.gmorunsystem.aimam.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import vn.gmorunsystem.aimam.ui.fragment.ShopAboutFragment;
import vn.gmorunsystem.aimam.ui.fragment.ShopHotFragment;
import vn.gmorunsystem.aimam.ui.fragment.ShopMenuFragment;
import vn.gmorunsystem.aimam.ui.fragment.ShopReviewFragment;
import vn.gmorunsystem.aimam.ui.fragment.ShopStampFragment;

/**
 * Created by HuongTrung on 5/18/2017.
 */

public class ShopMainViewPagerAdapter extends FragmentPagerAdapter {

    private String[] pageTitle;
    private int shopId;

    public ShopMainViewPagerAdapter(FragmentManager fm, String[] pagerTitle, int shopId) {
        super(fm);
        this.pageTitle = pagerTitle;
        this.shopId = shopId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ShopHotFragment.newInstance(shopId);
            case 1:
                return ShopMenuFragment.newInstance(shopId);
            case 2:
                return ShopAboutFragment.newInstance(shopId);
            case 3:
                return ShopReviewFragment.newInstance(shopId);
            case 4:
                return ShopStampFragment.newInstance(shopId);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitle[position];
    }

    @Override
    public int getCount() {
        return pageTitle.length;
    }
}
