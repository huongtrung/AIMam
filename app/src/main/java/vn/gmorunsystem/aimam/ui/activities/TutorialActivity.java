package vn.gmorunsystem.aimam.ui.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.CirclePageIndicator;

import butterknife.BindView;
import butterknife.OnClick;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.ui.adapter.TutorialPagerAdapter;
import vn.gmorunsystem.aimam.utils.IntentUtil;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;

/**
 * Created by adm on 9/14/2017.
 */

public class TutorialActivity extends BaseActivity {
    @BindView(R.id.vp_tut)
    ViewPager viewPagerTut;
    @BindView(R.id.tut_pager_indicator)
    CirclePageIndicator indicator;

    TutorialPagerAdapter tutorialPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        SharedPrefUtils.putBoolean(APPConstant.JUST_INSTALL_APP, false);
        tutorialPagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
        viewPagerTut.setAdapter(tutorialPagerAdapter);
        indicator.setViewPager(viewPagerTut);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    @OnClick(R.id.btn_tut_skip)
    public void onSkip() {
        //go to home screen
        IntentUtil.gotoLogin(this);
    }
}
