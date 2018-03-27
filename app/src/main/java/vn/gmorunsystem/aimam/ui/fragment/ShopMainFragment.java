package vn.gmorunsystem.aimam.ui.fragment;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.GetShopRequest;
import vn.gmorunsystem.aimam.apis.request.ShopSlideShowRequest;
import vn.gmorunsystem.aimam.apis.request.SubscribedRequest;
import vn.gmorunsystem.aimam.apis.request.UnSubscribeListRequest;
import vn.gmorunsystem.aimam.apis.response.GetShopResponse;
import vn.gmorunsystem.aimam.apis.response.ShopSlideShowResponse;
import vn.gmorunsystem.aimam.apis.response.SubscribedResponse;
import vn.gmorunsystem.aimam.apis.response.UnSubscribeResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.ShopSlideShowBean;
import vn.gmorunsystem.aimam.callback.ImageUrlToSocialShareCallBack;
import vn.gmorunsystem.aimam.callback.ShopHotDataListener;
import vn.gmorunsystem.aimam.callback.SubscribedStatusCallBack;
import vn.gmorunsystem.aimam.callback.SuccessCallBack;
import vn.gmorunsystem.aimam.ui.adapter.ShopMainViewPagerAdapter;
import vn.gmorunsystem.aimam.ui.adapter.ShopSlideAdapter;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.EventBusHelper;
import vn.gmorunsystem.aimam.utils.IntentUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

public class ShopMainFragment extends BaseFragment {
    private static final String KEY_SHOP_ID = "KEY_SHOP_ID";

    @BindView(R.id.vp_pager)
    ViewPager vpPager;
    @BindView(R.id.slide_pager)
    ViewPager vpSlider;
    @BindView(R.id.pager_indicator)
    CirclePageIndicator indicatorSlider;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.txtShopName)
    TextView mShopName;
    @BindView(R.id.txtShopAddr)
    TextView mAddress;
    @BindView(R.id.btn_subscribe)
    Button btnSub;
    @BindView(R.id.btn_call)
    Button btnCall;


    ShopSlideAdapter shopSlideAdapter;
    ShopMainViewPagerAdapter shopViewPagerAdapter;
    List<ShopSlideShowBean> shopSlideShowBean;
    GetShopResponse getShopResponse;

    private boolean isFirstLoad = true;

    private Boolean mIsSub;
    private int mShopId;

    public static ShopMainFragment newInstance(int shopId) {
        ShopMainFragment fragment = new ShopMainFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SHOP_ID, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_restaurant;
    }

    @Override
    protected void initView(View root) {
        initViewPager();
    }

    @Override
    protected void initData() {
        EventBusHelper.register(this);
        if (getShopResponse == null) {
            getShopRequest();
        } else {
            handleResponseSuccess(getShopResponse);
            if (shopSlideShowBean != null) {
                setUpSlideView(shopSlideShowBean);
            } else {
                getShopSlideShowRequest();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkHotData(ShopHotDataListener listener) {
        if (listener.checkHotData()) {
            vpPager.setCurrentItem(1);
        }
    }

    @Override
    protected void getArgument(Bundle bundle) {
        mShopId = bundle.getInt(KEY_SHOP_ID);
    }

    private void getShopSlideShowRequest() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            ShopSlideShowRequest shopSlideShowRequest = new ShopSlideShowRequest(mShopId);
            shopSlideShowRequest.setRequestCallBack(new ApiObjectCallBack<ShopSlideShowResponse>() {
                @Override
                public void onSuccess(ShopSlideShowResponse data) {
                    hideProgressBar();
                    if (!isVisible()) {
                        return;
                    }
                    EventBusHelper.post(new SuccessCallBack(true));
                    if (data != null && data.shopSlideShowBean.size() > 0) {
                        shopSlideShowBean = data.shopSlideShowBean;
                        setUpSlideView(data.shopSlideShowBean);
                    } else {
                        DialogUtil.showDialog(getContext(), getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    EventBusHelper.post(new SuccessCallBack(true));
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            shopSlideShowRequest.execute();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private void getShopRequest() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            if (isFirstLoad) {
                showProgressBar();
                isFirstLoad = false;
            }
            final GetShopRequest getShopRequest = new GetShopRequest(mShopId);
            getShopRequest.setRequestCallBack(new ApiObjectCallBack<GetShopResponse>() {
                @Override
                public void onSuccess(GetShopResponse data) {
                    if (!isVisible()) {
                        return;
                    }
                    if (data != null) {
                        getShopResponse = data;
                        handleResponseSuccess(data);
                        getShopSlideShowRequest();
                    } else {
                        DialogUtil.showDialog(getContext(), getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            getShopRequest.execute();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private void handleResponseSuccess(GetShopResponse response) {
        mIsSub = response.isSubscribed;
        setScreenTitle(response.name);
        setSubscribed(response.isSubscribed);
        if (mShopName != null) {
            mShopName.setText(response.name);
        }
        if (mAddress != null) {
            mAddress.setText(response.address);
        }
    }

    @OnClick(R.id.btn_subscribe)
    public void onClick(View v) {
        if (!UiUtil.isClickable()) {
            return;
        }
        if (mIsSub)
            getUnSubscribedRequest();
        else
            getSubscribedRequest();
    }

    @OnClick(R.id.btn_call)
    public void onBtnCallClick() {
        if (!UiUtil.isClickable()) {
            return;
        }

        if (!StringUtil.isEmpty(getShopResponse.phone)) {
            IntentUtil.openPhoneCall(getActivity(), getShopResponse.phone);
        }
    }

    private void getSubscribedRequest() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            showProgressBar();
            SubscribedRequest subscribedRequest = new SubscribedRequest(mShopId);
            subscribedRequest.setRequestCallBack(new ApiObjectCallBack<SubscribedResponse>() {
                @Override
                public void onSuccess(SubscribedResponse data) {
                    hideProgressBar();
                    if (!isVisible()) {
                        return;
                    }
                    if (data != null) {
                        setSubscribed(true);
                        EventBusHelper.post(new SubscribedStatusCallBack(true));
                        DialogUtil.showDialog(getContext(), getString(R.string.subscribe_success));
                    } else {
                        DialogUtil.showDialog(getContext(), getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    DialogUtil.showDialog(getContext(), message);
                }
            });
            subscribedRequest.execute();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private void getUnSubscribedRequest() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            showProgressBar();
            UnSubscribeListRequest unSubscribeRequest = new UnSubscribeListRequest(mShopId);
            unSubscribeRequest.setRequestCallBack(new ApiObjectCallBack<UnSubscribeResponse>() {
                @Override
                public void onSuccess(UnSubscribeResponse data) {
                    if (!isVisible()) {
                        return;
                    }
                    hideProgressBar();
                    if (data != null && data.messages != null) {
                        setSubscribed(false);
                        EventBusHelper.post(new SubscribedStatusCallBack(false));
                        DialogUtil.showDialog(getContext(), getString(R.string.un_subscribe_success));
                    } else {
                        DialogUtil.showDialog(getContext(), getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            unSubscribeRequest.execute();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private void setSubscribed(Boolean isSub) {
        mIsSub = isSub;
        if (btnSub != null) {
            if (isSub) {
                btnSub.setText(R.string.title_unsubscribe);
            } else {
                btnSub.setText(R.string.title_subscribe);
            }
        }
    }

    private void initViewPager() {
        String[] pageTitle = {getString(R.string.title_hot), getString(R.string.title_menu),
                getString(R.string.title_about), getString(R.string.title_review), getString(R.string.title_stamp)};
        shopViewPagerAdapter = new ShopMainViewPagerAdapter(getChildFragmentManager(), pageTitle, mShopId);
        vpPager.setOffscreenPageLimit(pageTitle.length);
        vpPager.setAdapter(shopViewPagerAdapter);
        tabLayout.setupWithViewPager(vpPager);

        // create divider tab layout
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.GRAY);
        drawable.setSize(1, 1);
        linearLayout.setDividerPadding(10);
        linearLayout.setDividerDrawable(drawable);
    }

    private void setUpSlideView(List<ShopSlideShowBean> shopSlideShowBeanList) {
        shopSlideAdapter = new ShopSlideAdapter(getContext(), shopSlideShowBeanList);
        vpSlider.setAdapter(shopSlideAdapter);
        vpSlider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicatorSlider.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        indicatorSlider.setViewPager(vpSlider);
        EventBusHelper.post(new ImageUrlToSocialShareCallBack(shopSlideShowBeanList.get(0).url));
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