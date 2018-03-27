package vn.gmorunsystem.aimam.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.ui.fragment.PagerTutorialFragment;

/**
 * Created by adm on 9/14/2017.
 */

public class TutorialPagerAdapter extends FragmentStatePagerAdapter {
    private static int pages = 4;

    public TutorialPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PagerTutorialFragment.newInstance(R.string.text_tut_green_1, R.string.text_tut_white_1, R.drawable.tutorial_page1);
            case 1:
                return PagerTutorialFragment.newInstance(R.string.text_tut_green_2, R.string.text_tut_white_2, R.drawable.tutorial_page2);
            case 2:
                return PagerTutorialFragment.newInstance(R.string.text_tut_green_3, R.string.text_tut_white_3, R.drawable.tutorial_page3);
            case 3:
                return PagerTutorialFragment.newInstance(R.string.text_tut_green_4, R.string.text_tut_white_4, R.drawable.tutorial_page4);
            default:
                return PagerTutorialFragment.newInstance(R.string.text_tut_green_1, R.string.text_tut_white_1, R.drawable.tutorial_page1);
        }
    }

    @Override
    public int getCount() {
        return pages;
    }
}
