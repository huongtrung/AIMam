package vn.gmorunsystem.aimam.ui.fragment;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.ShopSearchRequest;
import vn.gmorunsystem.aimam.apis.response.ShopSearchResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.ShopAdvBean;
import vn.gmorunsystem.aimam.bean.ShopItemBean;
import vn.gmorunsystem.aimam.bean.ShopSearchBean;
import vn.gmorunsystem.aimam.bean.ShopSuggestBean;
import vn.gmorunsystem.aimam.callback.ChangeFragmentListener;
import vn.gmorunsystem.aimam.callback.OnItemClickListener;
import vn.gmorunsystem.aimam.callback.OnLikeOrRateReviewItemListener;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.ui.adapter.AutoCompleteAdapter;
import vn.gmorunsystem.aimam.ui.adapter.OnRecyclerViewItemClick;
import vn.gmorunsystem.aimam.ui.adapter.ShopAdsViewAdapter;
import vn.gmorunsystem.aimam.ui.adapter.ShopItemListAdapter;
import vn.gmorunsystem.aimam.ui.adapter.ShopSuggestViewPagerAdapter;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DebugLog;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.EventBusHelper;
import vn.gmorunsystem.aimam.utils.GPSTracker;
import vn.gmorunsystem.aimam.utils.KeyboardUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

public class ShopSearchFragment extends BaseFragment {
    private final long DELAY = 500;
    private static final long SLEEP_TIME = 5000;
    @BindView(R.id.vp_search_suggestion)
    ViewPager vpSearchSuggestion;
    @BindView(R.id.vp_search_ads)
    ViewPager vpSearchAds;
    @BindView(R.id.rv_search)
    RecyclerView rvItemList;
    @BindView(R.id.et_search)
    AutoCompleteTextView etSearch;
    @BindView(R.id.ll_shop_suggest)
    LinearLayout llShopSuggest;
    @BindView(R.id.ll_shop_adv)
    LinearLayout llShopAdv;
    @BindView(R.id.tv_not_result)
    TextView tvNotResult;

    DrawerLayout mDrawerLayout;
    View mainView;
    View drawerContainerView;

    AutoCompleteAdapter mAutoCompleteAdapter;
    ShopSuggestViewPagerAdapter mShopSuggestAdapter;
    ShopAdsViewAdapter mShopAdsAdapter;
    ShopItemListAdapter mShopItemListAdapter;

    OnLikeOrRateReviewItemListener mListener;
    private Timer mTimer;
    private Location mUserLocation;
    private double mLatitude;
    private double mLongitude;
    private double mLatFirstShop;
    private double mLongFirstShop;
    private GPSTracker mGpsTracker;
    private String prevScreenTitle = "";
    private boolean isOpened;
    private boolean isReplaceFragment = false;

    @Override
    protected int getLayoutId() {
        return R.layout.search_drawer_item;
    }

    @Override
    protected void initView(View root) {
        EventBusHelper.register(this);
        KeyboardUtil.requestKeyboard(etSearch);
        mGpsTracker = new GPSTracker(getContext());
        doRequestLocation();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if (mTimer != null)
                    mTimer.cancel();
            }

            @Override
            public void afterTextChanged(final Editable strSearch) {
                if (strSearch.length() > 0) {
                    mTimer = new Timer();
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            doSearchShop(strSearch.toString(), true);
                        }
                    }, DELAY);
                } else {
                    UiUtil.hideView(tvNotResult, true);
                }
            }
        });
    }

    public void doRequestLocation() {
        if (mGpsTracker.canGetLocation()) {
            mUserLocation = mGpsTracker.getLocation();
            mLatitude = mGpsTracker.getLatitude();
            mLongitude = mGpsTracker.getLongitude();
            doSearchShop("", false);
            DebugLog.e("latlong : " + mLatitude + "," + mLongitude);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APPConstant.REQUEST_CODE && resultCode == APPConstant.RESULT_CODE) {
            showProgressBar();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mUserLocation = mGpsTracker.getLocation();
                    mLatitude = mGpsTracker.getLatitude();
                    mLongitude = mGpsTracker.getLongitude();
                    doSearchShop("", false);
                }
            }, SLEEP_TIME);
        }
    }

    @Override
    protected void initData() {
    }

    private void doSearchShop(final String searchContent, final boolean isSearch) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            final ShopSearchRequest shopSearchRequest = new ShopSearchRequest(searchContent, mLatitude, mLongitude);
            shopSearchRequest.setRequestCallBack(new ApiObjectCallBack<ShopSearchResponse>() {
                @Override
                public void onSuccess(ShopSearchResponse data) {
                    if (data != null) {
                        showOrHideWhenStateChanged(true);
                        if (data.shopSearch != null && !data.shopSearch.isEmpty() && !data.shopSearch.isEmpty()) {
                            UiUtil.hideView(tvNotResult, true);
                            setUpAutoComplete(data.shopSearch);
                        } else {
                            if (isSearch) {
                                UiUtil.showView(tvNotResult);
                                if (etSearch.isPopupShowing())
                                    etSearch.dismissDropDown();
                                StringUtil.displayText(getString(R.string.not_found_result) + " " + searchContent, tvNotResult);
                            }
                        }
                        if (data.shopSuggest != null && !data.shopSuggest.isEmpty()) {
                            if (mShopSuggestAdapter == null)
                                setUpShopSuggest(data.shopSuggest);
                        } else {
                            UiUtil.hideView(llShopSuggest, true);
                        }
                        if (data.shopAdv != null && !data.shopAdv.isEmpty()) {
                            if (mShopAdsAdapter == null)
                                setUpShopAdv(data.shopAdv);
                        } else {
                            UiUtil.hideView(llShopAdv, true);
                        }
                        if (data.shopItems != null && !data.shopItems.isEmpty()) {
                            if (mShopItemListAdapter == null)
                                setUpItemList(data.shopItems);
                        }
                    } else
                        DialogUtil.showDialog(getActivity(), getString(R.string.not_have_data));
                }

                @Override
                public void onFail(int failCode, String message) {
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            shopSearchRequest.execute();
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
                }
            });

        }
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    private void setUpAutoComplete(List<ShopSearchBean> data) {
        mAutoCompleteAdapter = new AutoCompleteAdapter(getContext(), R.layout.item_result_search, (ArrayList<ShopSearchBean>) data);
        etSearch.setAdapter(mAutoCompleteAdapter);
        etSearch.showDropDown();
        mAutoCompleteAdapter.setListener(new OnItemClickListener() {
            @Override
            public void onClick(int position, int id) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    isReplaceFragment = true;
                    mDrawerLayout.closeDrawers();
                }
                replaceFragment(R.id.container, ShopMainFragment.newInstance(id));
            }
        });
    }

    private void setUpItemList(final List<ShopItemBean> itemBeanList) {
        UiUtil.showView(rvItemList);
        rvItemList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItemList.setNestedScrollingEnabled(false);
        mShopItemListAdapter = new ShopItemListAdapter(itemBeanList, setShopLocation(mLatFirstShop, mLongFirstShop));
        rvItemList.setAdapter(mShopItemListAdapter);
        mShopItemListAdapter.setOnRecyclerViewItemClick(new OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    isReplaceFragment = true;
                    mDrawerLayout.closeDrawers();
                }
                addFragment(R.id.container, DetailItemFragment.newInstance(itemBeanList.get(position).id, mListener), DetailItemFragment.class.getSimpleName());
            }
        });

        mListener = new OnLikeOrRateReviewItemListener() {
            @Override
            public void upDateLikeAndRate(int itemId, int views, int likes, Float rate) {
                if (itemBeanList != null && !itemBeanList.isEmpty()) {
                    for (int i = 0; i < itemBeanList.size(); i++) {
                        if (itemBeanList.get(i).id == itemId) {
                            itemBeanList.get(i).vote = rate;
                            mShopItemListAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }
            }
        };
    }

    private void setUpShopSuggest(List<ShopSuggestBean> shopSuggestList) {
        UiUtil.showView(llShopSuggest);
        Collections.sort(shopSuggestList);
        mLatFirstShop = shopSuggestList.get(0).mapLatitude;
        mLongFirstShop = shopSuggestList.get(0).mapLongtitude;
        mShopSuggestAdapter = new ShopSuggestViewPagerAdapter(getChildFragmentManager(), shopSuggestList, mUserLocation, APPConstant.FRAG_SEARCH);
        vpSearchSuggestion.setAdapter(mShopSuggestAdapter);
    }

    private void setUpShopAdv(List<ShopAdvBean> shopAdvBeanList) {
        UiUtil.showView(llShopAdv);
        mShopAdsAdapter = new ShopAdsViewAdapter(getChildFragmentManager(), shopAdvBeanList, mUserLocation, APPConstant.FRAG_SEARCH);
        vpSearchAds.setAdapter(mShopAdsAdapter);
    }

    public void setUpDrawerSearch(int fragmentDrawerId, DrawerLayout drawerLayout) {
        drawerContainerView = getActivity().findViewById(fragmentDrawerId);
        mDrawerLayout = drawerLayout;
        mainView = getActivity().findViewById(R.id.container);
        mDrawerLayout.setScrimColor(getResources().getColor(R.color.black_opacity));
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.END) && !isOpened) {
                    prevScreenTitle = getMainActivity().getTitle().toString();
                    getMainActivity().setTitle(getString(R.string.title_search));
                    getMainActivity().showOrHideSearchIcon(false);
                    isOpened = true;
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (!mDrawerLayout.isDrawerOpen(GravityCompat.END) && isOpened) {
                    etSearch.setText("");
                    hideSoftKeyboard();
                    getMainActivity().showOrHideSearchIcon(true);
//                    prevScreenTitle = getMainActivity().getTitle().toString();
                    if ((!TextUtils.isEmpty(prevScreenTitle))
                            && (!isReplaceFragment)) {
                        getMainActivity().setTitle(prevScreenTitle);
                    }
                    isOpened = false;
                    isReplaceFragment = false;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                hideSoftKeyboard();
            }
        });

    }

    private void showOrHideWhenStateChanged(boolean isShow) {
        if (isShow) {
            UiUtil.showView(llShopAdv);
            UiUtil.showView(llShopSuggest);
        } else {
            UiUtil.hideView(llShopAdv, true);
            UiUtil.hideView(llShopSuggest, true);
        }
    }

    private String setShopLocation(double latitude, double longitude) {
        Float distance = 0.0f;
        Location shopLocation = new Location("");
        shopLocation.setLatitude(latitude);
        shopLocation.setLongitude(longitude);
        if (mUserLocation != null) {
            distance = mUserLocation.distanceTo(shopLocation);
        }
        return StringUtil.convertDistanceToString(distance);
    }

    public void setPrevScreenTitle(String prevScreenTitle) {
        this.prevScreenTitle = prevScreenTitle;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeFragmentListen(ChangeFragmentListener listener) {
        if (listener.isChangeFragmentFromSearch()) {
            isReplaceFragment = true;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBusHelper.unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBusHelper.unregister(this);
    }
}
