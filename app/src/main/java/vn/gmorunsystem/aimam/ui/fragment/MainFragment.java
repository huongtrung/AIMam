package vn.gmorunsystem.aimam.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.GetHomeRequest;
import vn.gmorunsystem.aimam.apis.request.NewFeedRequest;
import vn.gmorunsystem.aimam.apis.response.GetHomeResponse;
import vn.gmorunsystem.aimam.apis.response.NewFeedResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.HomeFavoriteBean;
import vn.gmorunsystem.aimam.bean.NewFeedBean;
import vn.gmorunsystem.aimam.bean.ShopAdvBean;
import vn.gmorunsystem.aimam.bean.ShopSuggestBean;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.ui.adapter.HomeFavItemAdapter;
import vn.gmorunsystem.aimam.ui.adapter.NewFeedAdapter;
import vn.gmorunsystem.aimam.ui.adapter.OnRecyclerViewItemClick;
import vn.gmorunsystem.aimam.ui.adapter.ShopAdsViewAdapter;
import vn.gmorunsystem.aimam.ui.adapter.ShopSuggestViewPagerAdapter;
import vn.gmorunsystem.aimam.ui.adapter.viewholder.NewFeedViewHolder;
import vn.gmorunsystem.aimam.ui.customview.EndlessParentScrollListener;
import vn.gmorunsystem.aimam.ui.customview.WrapContentViewPager;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.GPSTracker;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

import static vn.gmorunsystem.aimam.constants.APPConstant.FRAG_MAIN;
import static vn.gmorunsystem.aimam.constants.APPConstant.REQUEST_CODE;
import static vn.gmorunsystem.aimam.constants.APPConstant.RESULT_CODE;

public class MainFragment extends BaseFragment implements LocationListener {
    private static final int COLUMN_OF_FAVORITE = 5;
    private static final int DEFAULT_PAGER = 1;
    private static final int MAX_PAGER = 10;
    private static final long SLEEP_TIME = 5000;

    @BindView(R.id.vp_shop_suggestion)
    WrapContentViewPager vpShopSuggestion;
    @BindView(R.id.vp_shop_ads)
    WrapContentViewPager vpShopAds;
    @BindView(R.id.rv_favorite)
    RecyclerView rvFavorite;
    @BindView(R.id.ll_shop_suggest)
    LinearLayout llShopSuggest;
    @BindView(R.id.ll_shop_adv)
    LinearLayout llShopAdv;
    @BindView(R.id.ll_shop_fav)
    LinearLayout llShopFav;
    @BindView(R.id.home_swipe_refresh_layout)
    SwipeRefreshLayout homeSwipeRefresh;
    @BindView(R.id.rv_new_feed)
    RecyclerView rvNewFeed;
    @BindView(R.id.ll_new_feed)
    LinearLayout llNewFeed;
    @BindView(R.id.nsv_main)
    NestedScrollView scrollView;
    @BindView(R.id.tv_not_data)
    TextView tvNotData;

    ShopSuggestViewPagerAdapter shopSuggestViewPagerAdapter;
    ShopAdsViewAdapter shopAdsViewPagerAdapter;
    HomeFavItemAdapter homeFavItemAdapter;
    NewFeedAdapter newFeedAdapter;

    GetHomeResponse homeResponseData;
    NewFeedResponse newFeedResponse;

    private Location mLocation;
    private GPSTracker mGpsTracker;
    private Double mLat;
    private Double mLong;
    private int mPage = 1;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView(View root) {
        setScreenTitle(R.string.app_name);
        getMainActivity().showBottomBarAndSearchIcon();
        mGpsTracker = new GPSTracker(getContext());

        homeSwipeRefresh.setColorSchemeColors(getResources().getColor(R.color.color_light_green));
        homeSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHomeData(mLat, mLong, true);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initData() {
        if (homeResponseData == null || newFeedResponse == null) {
            doRequestLocation();
        } else {
            setUpNewFeedData(newFeedResponse.data);
            setUpHomeData(homeResponseData);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void doRequestLocation() {
        if (mGpsTracker.canGetLocation()) {
            mLocation = mGpsTracker.getLocation();
            mLat = mGpsTracker.getLatitude();
            mLong = mGpsTracker.getLongitude();
            getHomeData(mLat, mLong, false);
        } else {
            showSettingsAlert();
        }
    }

    public void showSettingsAlert() {
        DialogUtil.showDialogAsk(getContext(), getString(R.string.gps_enabled), getString(R.string.gps_setting), getString(R.string.setting), getString(R.string.title_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, REQUEST_CODE);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getHomeData(mLat, mLong, false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            showProgressBar();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLocation = mGpsTracker.getLocation();
                    mLat = mGpsTracker.getLatitude();
                    mLong = mGpsTracker.getLongitude();
                    getHomeData(mLat, mLong, false);
                }
            }, SLEEP_TIME);
        }
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    private void getHomeData(Double latitude, Double longitude, boolean isRefresh) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            if (!isRefresh) {
                showProgressBar();
            }
            GetHomeRequest getHomeRequest = new GetHomeRequest(latitude, longitude);
            getHomeRequest.setRequestCallBack(new ApiObjectCallBack<GetHomeResponse>() {
                @Override
                public void onSuccess(GetHomeResponse data) {
                    removeIconRefreshLoading();
                    getNewFeedRequest(false);
                    if (!isVisible()) {
                        return;
                    }
                    if (data != null) {
                        homeResponseData = data;
                        setUpHomeData(data);
                    } else {
                        DialogUtil.showDialog(getContext(), getString(R.string.not_have_data));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    removeIconRefreshLoading();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            getHomeRequest.execute();
        } else {
            ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
            removeIconRefreshLoading();
        }
    }

    private void getNewFeedRequest(boolean isLoadMore) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            if (isLoadMore) {
                showProgressBar();
                mPage++;
            } else {
                mPage = DEFAULT_PAGER;
            }
            final NewFeedRequest newFeedRequest = new NewFeedRequest(mPage);
            newFeedRequest.setRequestCallBack(new ApiObjectCallBack<NewFeedResponse>() {
                @Override
                public void onSuccess(NewFeedResponse response) {
                    hideProgressBar();
                    removeIconRefreshLoading();
                    if (!isVisible()) {
                        return;
                    }
                    if (response != null && !response.data.isEmpty()) {
                        if (mPage == DEFAULT_PAGER) {
                            newFeedResponse = response;
                            setUpNewFeedData(response.data);
                        } else {
                            newFeedAdapter.addItemNewFeed(response.data);
                        }
                    } else {
                        UiUtil.showView(tvNotData);
                        UiUtil.hideView(rvNewFeed, true);
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    removeIconRefreshLoading();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            newFeedRequest.execute();
        } else {
            ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
        }
    }

    private void setUpNewFeedData(final List<NewFeedBean> newFeedList) {
        UiUtil.showView(llNewFeed);
        rvNewFeed.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvNewFeed.setLayoutManager(layoutManager);
        newFeedAdapter = new NewFeedAdapter(newFeedList, mLocation);
        rvNewFeed.setAdapter(newFeedAdapter);

        scrollView.setOnScrollChangeListener(new EndlessParentScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page < MAX_PAGER) {
                    if (!newFeedList.isEmpty()) {
                        getNewFeedRequest(true);
                    }
                }
            }
        });

        newFeedAdapter.setOnItemClickListener(new NewFeedViewHolder.OnItemClickListener() {
            @Override
            public void onClick(int type, int id) {
                if (!UiUtil.isClickable()) {
                    return;
                }
                switch (type) {
                    case APPConstant.TYPE_SHOP:
                        replaceFragment(R.id.container, ShopMainFragment.newInstance(id), true);
                        break;
                    case APPConstant.TYPE_COUPON:
                        replaceFragment(R.id.container, CouponDetailFragment.newInstance(id, true), true);
                        break;
                    case APPConstant.TYPE_ITEM:
                        replaceFragment(R.id.container, DetailItemFragment.newInstance(id), true);
                        break;
                }
            }
        });
    }

    private void setUpHomeData(GetHomeResponse homeResponseData) {
        if (homeResponseData.shopSuggests != null && homeResponseData.shopSuggests.size() > 0) {
            UiUtil.showView(llShopSuggest);
            setUpShopSuggestList(homeResponseData.shopSuggests);
        }
        if (homeResponseData.shopAdvs != null && homeResponseData.shopAdvs.size() > 0) {
            UiUtil.showView(llShopAdv);
            setUpShopAdv(homeResponseData.shopAdvs);
        }
        if (homeResponseData.favouriteItems != null && homeResponseData.favouriteItems.size() > 0) {
            UiUtil.showView(llShopFav);
            setUpFavList(homeResponseData.favouriteItems);
        }
    }

    private void setUpShopSuggestList(List<ShopSuggestBean> beanList) {
        Collections.sort(beanList);
        shopSuggestViewPagerAdapter = new ShopSuggestViewPagerAdapter(getChildFragmentManager(), beanList, mLocation, FRAG_MAIN);
        vpShopSuggestion.setAdapter(shopSuggestViewPagerAdapter);
    }

    private void setUpShopAdv(List<ShopAdvBean> beanList) {
        shopAdsViewPagerAdapter = new ShopAdsViewAdapter(getChildFragmentManager(), beanList, mLocation, FRAG_MAIN);
        vpShopAds.setAdapter(shopAdsViewPagerAdapter);
    }

    private void setUpFavList(final List<HomeFavoriteBean> beanList) {
        rvFavorite.setLayoutManager(new GridLayoutManager(getActivity(), COLUMN_OF_FAVORITE));
        homeFavItemAdapter = new HomeFavItemAdapter(beanList);
        homeFavItemAdapter.setOnRecyclerViewItemClick(new OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                replaceFragment(R.id.container, DetailItemFragment.newInstance(beanList.get(position).id));
            }
        });
        rvFavorite.setAdapter(homeFavItemAdapter);
        rvFavorite.setNestedScrollingEnabled(true);
    }

    private void removeIconRefreshLoading() {
        if (homeSwipeRefresh != null) {
            homeSwipeRefresh.setRefreshing(false);
        }
    }

    public void scrollToTop() {
        scrollView.fullScroll(View.FOCUS_UP);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            mLat = location.getLatitude();
            mLong = location.getLongitude();
            getHomeData(mLat, mLong, false);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
