package vn.gmorunsystem.aimam.ui.activities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.callback.OnRefreshProfile;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.ui.fragment.BaseFragment;
import vn.gmorunsystem.aimam.ui.fragment.MainFragment;
import vn.gmorunsystem.aimam.ui.fragment.MenuDrawerFragment;
import vn.gmorunsystem.aimam.ui.fragment.NearByShopFragment;
import vn.gmorunsystem.aimam.ui.fragment.PhotoManageFragment;
import vn.gmorunsystem.aimam.ui.fragment.ShopFavoriteFragment;
import vn.gmorunsystem.aimam.ui.fragment.ShopSearchFragment;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

import static vn.gmorunsystem.aimam.R.id.nav_menu;

public class MainActivity extends BaseActivity implements OnRefreshProfile {
    private static int ADMOB_DELAY_TIME = 5;//minutes
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitleToolbar;
    @BindView(R.id.bottom_bar)
    LinearLayout bottomBar;
    @BindView(R.id.iv_search)
    RelativeLayout layoutSearch;
    @BindView(R.id.view_dummy)
    View viewDummy;
    @BindView(R.id.iv_home)
    ImageView ivHome;
    @BindView(R.id.iv_map)
    ImageView ivMap;
    @BindView(R.id.iv_favorite)
    ImageView ivFavorite;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.iv_list)
    ImageView ivList;


    DrawerLayout mDrawer;
    MenuDrawerFragment menuDrawer;
    ShopSearchFragment searchDrawer;
    FragmentManager manager;
    InterstitialAd interstitialAd;
    ScheduledExecutorService scheduler;
    boolean running = true;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, findViewById(R.id.nav_search));
        manager = getSupportFragmentManager();
        setSupportActionBar(toolbar);
        changeToFragment(MainFragment.newInstance(), APPConstant.MAIN_FRAG_TAG);
        setMenuItemSelected(R.id.rl_home);
        setUpLeftDrawer();
        runDelayAd();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        tvTitleToolbar.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        tvTitleToolbar.setText(titleId);
    }

    private void setUpLeftDrawer() {
        menuDrawer = (MenuDrawerFragment) getSupportFragmentManager().findFragmentById(nav_menu);
        menuDrawer.setUpDrawerMenu(nav_menu, mDrawer);
    }

    private void setUpRightDrawer() {
        searchDrawer = (ShopSearchFragment) getSupportFragmentManager().findFragmentById(R.id.nav_search);
        searchDrawer.setUpDrawerSearch(R.id.nav_search, mDrawer);
    }

    public void hideBottomBarAndSearchIcon() {
        UiUtil.hideView(bottomBar, true);
        UiUtil.hideView(layoutSearch, true);
        UiUtil.hideView(viewDummy, true);
    }

    public void showBottomBarAndSearchIcon() {
        UiUtil.showView(bottomBar);
        UiUtil.showView(layoutSearch);
        UiUtil.showView(viewDummy);
    }

    public void showOrHideSearchIcon(boolean status) {
        if (status) {
            UiUtil.showView(layoutSearch);
        } else {
            UiUtil.hideView(layoutSearch, true);
        }

    }

    @OnClick({R.id.rl_home, R.id.rl_favorite, R.id.rl_map, R.id.rl_camera, R.id.iv_back, R.id.iv_search})
    public void OnClick(View v) {
        closeDrawer();
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.rl_home:
                if (!UiUtil.isClickable()) {
                    return;
                }
                changeToFragment(MainFragment.newInstance(), APPConstant.MAIN_FRAG_TAG);
                setMenuItemSelected(v.getId());
                break;
            case R.id.rl_favorite:
                if (!UiUtil.isClickable()) {
                    return;
                }
                changeToFragment(ShopFavoriteFragment.newInstance(), APPConstant.SHOP_FAVORITE_FRAG_TAG);
                setMenuItemSelected(v.getId());
                break;
            case R.id.rl_map:
                if (!UiUtil.isClickable()) {
                    return;
                }
                changeToFragment(NearByShopFragment.newInstance(), APPConstant.NEAR_BY_FRAG_TAG);
                setMenuItemSelected(v.getId());
                break;
            case R.id.rl_camera:
                if (!UiUtil.isClickable()) {
                    return;
                }
                changeToFragment(PhotoManageFragment.newInstance(), APPConstant.PHOTO_MANAGER_BY_FRAG_TAG);
                setMenuItemSelected(v.getId());
                break;
            case R.id.iv_search:
                if (!UiUtil.isClickable()) {
                    return;
                }
                setUpRightDrawer();
                if (mDrawer.isDrawerOpen(GravityCompat.START) || mDrawer.isDrawerOpen(GravityCompat.END)) {
                    mDrawer.closeDrawers();
                } else {
                    mDrawer.openDrawer(GravityCompat.END);
                }
                invalidateOptionsMenu();
        }
    }

    private void setMenuItemSelected(int viewID) {
        if (viewID == R.id.rl_home) {
            ivHome.setImageResource(R.drawable.ic_home_green);
        } else {
            ivHome.setImageResource(R.drawable.ic_home);
        }
        if (viewID == R.id.rl_favorite) {
            ivFavorite.setImageResource(R.drawable.ic_favorite_green);
        } else {
            ivFavorite.setImageResource(R.drawable.ic_farvorite);
        }
        if (viewID == R.id.rl_map) {
            ivMap.setImageResource(R.drawable.ic_marker_green);
        } else {
            ivMap.setImageResource(R.drawable.ic_marker);
        }
        if (viewID == R.id.rl_camera) {
            ivCamera.setImageResource(R.drawable.ic_camera_green);
        } else {
            ivCamera.setImageResource(R.drawable.ic_camera);
        }
    }

    private void changeToFragment(BaseFragment fragment, String tag) {
        if (checkFragmentIsCurrentVisible(tag)) {
            if (tag.equalsIgnoreCase(APPConstant.MAIN_FRAG_TAG)) {
                MainFragment fragmentMain = (MainFragment) manager.findFragmentByTag(APPConstant.MAIN_FRAG_TAG);
                fragmentMain.scrollToTop();
            }
        } else {
            if (searchDrawer != null) {
                searchDrawer.setPrevScreenTitle("");
            }
            replaceFragment(R.id.container, fragment, tag, true);
        }
    }

    private boolean checkFragmentIsCurrentVisible(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null && fragment.isVisible()) {
            return true;
        } else return false;
    }

    public void closeDrawer() {
        mDrawer.closeDrawers();
    }

    @Override
    public void refreshProfile() {
        menuDrawer.setUpUserInfo();
    }

    @Override
    public void onBackPressed() {
        if (UiUtil.isClickable()) {
            if (!isCurrentFragmentFromBottom()) {
                getSupportFragmentManager().popBackStack();
            } else if (mDrawer.isDrawerOpen(GravityCompat.END) || mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawers();
            } else {
                DialogUtil.showDialogConfirmExit(this, getString(R.string.comfirm_exit_text), getString(R.string.title_yes), getString(R.string.title_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.exit(0);
                    }
                });
            }
        }
    }

    public boolean isCurrentFragmentFromBottom() {
        Fragment fragment = manager.findFragmentByTag(APPConstant.MAIN_FRAG_TAG);
        if (fragment != null && fragment.isVisible()) {
            return true;
        }
        fragment = manager.findFragmentByTag(APPConstant.SHOP_FAVORITE_FRAG_TAG);
        if (fragment != null && fragment.isVisible()) {
            return true;
        }
        fragment = manager.findFragmentByTag(APPConstant.NEAR_BY_FRAG_TAG);
        if (fragment != null && fragment.isVisible()) {
            return true;
        }
        fragment = manager.findFragmentByTag(APPConstant.PHOTO_MANAGER_BY_FRAG_TAG);
        if (fragment != null && fragment.isVisible()) {
            return true;
        }
        return false;
    }

    private void runDelayAd() {
        loadAdFullScreen();
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (interstitialAd.isLoaded() && running == true) {
                            interstitialAd.show();
                        } else {
                            Log.d("TAG", " Interstitial not loaded");
                        }
                        loadAdFullScreen();
                    }
                });
            }
        }, ADMOB_DELAY_TIME, ADMOB_DELAY_TIME, TimeUnit.MINUTES);
    }

    private void loadAdFullScreen() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.ad_interstitial_full_screen));
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("D5DA4822065F05FFADE2E7FE5275EEE8")
                .build();
        interstitialAd.loadAd(adRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        running = true;
    }
}
