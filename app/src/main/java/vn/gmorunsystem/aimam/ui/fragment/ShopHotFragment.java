package vn.gmorunsystem.aimam.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.ShopHotRequest;
import vn.gmorunsystem.aimam.apis.request.TakeCouponRequest;
import vn.gmorunsystem.aimam.apis.response.ShopHotResponse;
import vn.gmorunsystem.aimam.apis.response.TakeCouponResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.ShopCouponBean;
import vn.gmorunsystem.aimam.bean.ShopNewBean;
import vn.gmorunsystem.aimam.bean.ShopSpecialBean;
import vn.gmorunsystem.aimam.callback.OnLikeOrRateReviewItemListener;
import vn.gmorunsystem.aimam.callback.OnShareSocialListener;
import vn.gmorunsystem.aimam.callback.OnTakeCouponListener;
import vn.gmorunsystem.aimam.callback.ShopHotDataListener;
import vn.gmorunsystem.aimam.callback.SuccessCallBack;
import vn.gmorunsystem.aimam.constants.APIConstant;
import vn.gmorunsystem.aimam.ui.adapter.NewFoodListAdapter;
import vn.gmorunsystem.aimam.ui.adapter.OnRecyclerViewItemClick;
import vn.gmorunsystem.aimam.ui.adapter.ShopCouponListAdapter;
import vn.gmorunsystem.aimam.ui.adapter.SpecialFoodListAdapter;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.EventBusHelper;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

public class ShopHotFragment extends BaseFragment {
    private static String KEY_SHOP_ID = "KEY_SHOP_ID_HOT";
    @BindView(R.id.rv_special_food)
    RecyclerView rvSpecialFood;
    @BindView(R.id.rv_new_food)
    RecyclerView rvNewFood;
    @BindView(R.id.rv_coupon_list)
    RecyclerView rvShopCoupon;
    @BindView(R.id.tv_special)
    TextView tvSpecial;
    @BindView(R.id.tv_new)
    TextView tvNew;
    @BindView(R.id.tv_shop_hot_empty)
    TextView tvEmpty;

    ShopCouponListAdapter mCouponListAdapter;
    SpecialFoodListAdapter mSpecialListAdapter;
    NewFoodListAdapter mNewListAdapter;

    List<ShopSpecialBean> specialBeanList;
    List<ShopNewBean> newBeanList;
    OnLikeOrRateReviewItemListener mListenerSpecial;
    OnLikeOrRateReviewItemListener mListenerNews;

    private int mShopId;
    private boolean isRequestHot;
    private ShopHotResponse mShopHotResponse;

    public static ShopHotFragment newInstance(int shopId) {
        ShopHotFragment fragment = new ShopHotFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SHOP_ID, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_hot_restaurant;
    }

    @Override
    protected void initView(View root) {
        if (mShopHotResponse == null)
            EventBusHelper.register(this);
        else {
            handleShopHotResponse(mShopHotResponse);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkSuccess(SuccessCallBack successCallBack) {
        if (successCallBack.isCheckSuccessApi()) {
            isRequestHot = true;
            getShopHotRequest();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mShopId = getArguments().getInt(KEY_SHOP_ID);
            if (isRequestHot) {
                if (mShopHotResponse == null)
                    getShopHotRequest();
                else {
                    handleShopHotResponse(mShopHotResponse);
                }
            }
        }
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void getArgument(Bundle bundle) {
    }

    private void getShopHotRequest() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            ShopHotRequest shopHotRequest = new ShopHotRequest(mShopId);
            shopHotRequest.setRequestCallBack(new ApiObjectCallBack<ShopHotResponse>() {
                @Override
                public void onSuccess(ShopHotResponse data) {
                    if (!isVisible()) {
                        return;
                    }
                    if (data != null) {
                        mShopHotResponse = data;
                        handleShopHotResponse(data);
                    } else {
                        DialogUtil.showDialog(getContext(), getString(R.string.not_have_data));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            shopHotRequest.execute();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private void handleShopHotResponse(ShopHotResponse data) {
        boolean isNotEmpty = false;
        if (data.coupons != null && data.coupons.size() > 0) {
            setUpShopCoupon(data.coupons);
            isNotEmpty = true;
        }

        if (data.specialItems != null && data.specialItems.size() > 0) {
            specialBeanList = data.specialItems;
            setUpSpecialList(specialBeanList);
            isNotEmpty = true;
        }
        if (data.newItems != null && data.newItems.size() > 0) {
            newBeanList = data.newItems;
            setUpNewList(newBeanList);
            isNotEmpty = true;
        }

        if (!isNotEmpty && tvEmpty != null) {
            tvEmpty.setVisibility(View.VISIBLE);
            EventBusHelper.post(new ShopHotDataListener(true));
        }
    }

    private void setUpShopCoupon(final List<ShopCouponBean> shopCoupon) {
        if (rvShopCoupon == null) return;
        rvShopCoupon.setLayoutManager(new LinearLayoutManager(getContext()));
        rvShopCoupon.setNestedScrollingEnabled(false);
        mCouponListAdapter = new ShopCouponListAdapter(shopCoupon);
        rvShopCoupon.setAdapter(mCouponListAdapter);
        mCouponListAdapter.setOnRecyclerViewItemClick(new OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                if (!UiUtil.isClickable()) {
                    return;
                }
                addFragment(R.id.container, CouponDetailFragment
                                .newInstance(shopCoupon.get(position).id, false),
                        DetailItemFragment.class.getSimpleName());
            }
        });
        mCouponListAdapter.setOnItemClickListener(new OnTakeCouponListener() {
            @Override
            public void onTakeCoupon(int position, int couponId) {
                getTakeCouponRequest(position, couponId);
            }
        });
        mCouponListAdapter.setOnShareSocialListener(new OnShareSocialListener() {
            @Override
            public void onShareFacebook(String imgUrl) {
                shareInFacebook(APIConstant.WEB_URL);
            }

            @Override
            public void onShareInstagram(String imgUrl, String fileName) {
                shareInInstagram(imgUrl, fileName);
            }
        });
    }

    private void setUpSpecialList(final List<ShopSpecialBean> specialItems) {
        if (rvSpecialFood == null) return;
        rvSpecialFood.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSpecialFood.setNestedScrollingEnabled(false);
        UiUtil.showView(tvSpecial);
        mSpecialListAdapter = new SpecialFoodListAdapter(specialItems);
        mSpecialListAdapter.setOnRecyclerViewItemClick(new OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                if (!UiUtil.isClickable()) {
                    return;
                }
                addFragment(R.id.container, DetailItemFragment.newInstance(specialItems.get(position).id, mListenerSpecial),
                        DetailItemFragment.class.getSimpleName());
            }
        });
        rvSpecialFood.setAdapter(mSpecialListAdapter);
        mListenerSpecial = new OnLikeOrRateReviewItemListener() {
            @Override
            public void upDateLikeAndRate(int itemId, int views, int likes, Float rate) {
                if (specialBeanList != null && specialBeanList.size() > 0) {
                    for (int i = 0; i < specialBeanList.size(); i++) {
                        if (specialBeanList.get(i).id == itemId) {
                            specialBeanList.get(i).view = views;
                            specialBeanList.get(i).like = likes;
                            specialBeanList.get(i).vote = rate;
                            mSpecialListAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }
            }
        };
    }

    private void setUpNewList(final List<ShopNewBean> newItems) {
        if (rvNewFood == null) return;
        rvNewFood.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNewFood.setNestedScrollingEnabled(false);
        mNewListAdapter = new NewFoodListAdapter(newItems);
        mNewListAdapter.setOnRecyclerViewItemClick(new OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                if (!UiUtil.isClickable()) {
                    return;
                }
                addFragment(R.id.container, DetailItemFragment.newInstance(newItems.get(position).id, mListenerNews),
                        DetailItemFragment.class.getSimpleName());
            }
        });
        rvNewFood.setAdapter(mNewListAdapter);
        mListenerNews = new OnLikeOrRateReviewItemListener() {
            @Override
            public void upDateLikeAndRate(int itemId, int views, int likes, Float rate) {
                if (newBeanList != null && newBeanList.size() > 0) {
                    for (int i = 0; i < newBeanList.size(); i++) {
                        if (newBeanList.get(i).id == itemId) {
                            newBeanList.get(i).view = views;
                            newBeanList.get(i).like = likes;
                            newBeanList.get(i).vote = rate;
                            mNewListAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }
            }
        };
        UiUtil.showView(tvNew);
    }

    private void getTakeCouponRequest(final int position, int couponId) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            showProgressBar();
            TakeCouponRequest takeCouponRequest = new TakeCouponRequest(couponId);
            takeCouponRequest.setRequestCallBack(new ApiObjectCallBack<TakeCouponResponse>() {
                @Override
                public void onSuccess(TakeCouponResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        DialogUtil.showDialog(getContext(), getString(R.string.take_coupon_success));
                        mCouponListAdapter.removeCoupon(position);
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
            takeCouponRequest.execute();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBusHelper.unregister(this);
    }
}
