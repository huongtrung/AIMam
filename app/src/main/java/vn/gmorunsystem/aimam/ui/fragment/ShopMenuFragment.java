package vn.gmorunsystem.aimam.ui.fragment;


import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.ShopMenuRequest;
import vn.gmorunsystem.aimam.apis.response.ShopMenuResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.ShopMenuBean;
import vn.gmorunsystem.aimam.callback.OnLikeOrRateReviewItemListener;
import vn.gmorunsystem.aimam.ui.adapter.OnRecyclerViewItemClick;
import vn.gmorunsystem.aimam.ui.adapter.ShopMenuListAdapter;
import vn.gmorunsystem.aimam.ui.customview.EndlessParentScrollListener;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

public class ShopMenuFragment extends BaseFragment {
    private static String KEY_SHOP_ID = "KEY_SHOP_ID_MENU";
    private static final int DEFAULT_PAGER = 1;
    @BindView(R.id.rv_menu)
    RecyclerView rvShopMenu;
    @BindView(R.id.tv_shop_menu_empty)
    TextView tvEmpty;
    @BindView(R.id.scroll_view)
    NestedScrollView scrollView;

    ShopMenuListAdapter mMenuListAdapter;
    ShopMenuResponse mShopMenuResponse;
    OnLikeOrRateReviewItemListener mListener;
    private int mShopId;
    private int mTotalPage;
    private int mPage = 1;

    public static ShopMenuFragment newInstance(int shopId) {
        ShopMenuFragment fragment = new ShopMenuFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SHOP_ID, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_menu_restaurant;
    }

    @Override
    protected void initView(View root) {
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mShopId = getArguments().getInt(KEY_SHOP_ID);
            if (mShopMenuResponse == null) {
                getShopMenuRequest(false);
            } else {
                handleShopMenuResponse(mShopMenuResponse.shopMenuList);
            }
        }
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    private void getShopMenuRequest(boolean isLoadMore) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            if (isLoadMore) {
                showProgressBar();
                mPage++;
            } else {
                mPage = DEFAULT_PAGER;
            }
            ShopMenuRequest shopMenuRequest = new ShopMenuRequest(mShopId, mPage);
            shopMenuRequest.setRequestCallBack(new ApiObjectCallBack<ShopMenuResponse>() {
                @Override
                public void onSuccess(ShopMenuResponse data) {
                    hideProgressBar();
                    if (!isVisible()) {
                        return;
                    }
                    if (data != null && !data.shopMenuList.isEmpty()) {
                        mTotalPage = data.totalPages;
                        if (mPage == DEFAULT_PAGER) {
                            mShopMenuResponse = data;
                            handleShopMenuResponse(data.shopMenuList);
                        } else {
                            mMenuListAdapter.addItem(data.shopMenuList);
                        }
                    } else {
                        tvEmpty.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            shopMenuRequest.execute();
        } else {
            ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
        }
    }

    private void handleShopMenuResponse(final List<ShopMenuBean> shopMenuList) {
        rvShopMenu.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvShopMenu.setLayoutManager(layoutManager);
        mMenuListAdapter = new ShopMenuListAdapter(shopMenuList);
        rvShopMenu.setAdapter(mMenuListAdapter);

        scrollView.setOnScrollChangeListener(new EndlessParentScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (page < mTotalPage) {
                    getShopMenuRequest(true);
                }
            }
        });

        mMenuListAdapter.setOnRecyclerViewItemClick(new OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                if (!UiUtil.isClickable()) {
                    return;
                }
                replaceFragment(R.id.container, DetailItemFragment.newInstance(shopMenuList.get(position).id, mListener));
            }
        });
        mListener = new OnLikeOrRateReviewItemListener() {
            @Override
            public void upDateLikeAndRate(int itemId, int views, int likes,
                                          Float rate) {
                if (mShopMenuResponse != null && mShopMenuResponse.shopMenuList.size() > 0) {
                    for (int i = 0; i < mShopMenuResponse.shopMenuList.size(); i++) {
                        if (mShopMenuResponse.shopMenuList.get(i).id == itemId) {
                            mShopMenuResponse.shopMenuList.get(i).view = views;
                            mShopMenuResponse.shopMenuList.get(i).like = likes;
                            mShopMenuResponse.shopMenuList.get(i).vote = rate;
                            mMenuListAdapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }
            }
        };
    }
}
