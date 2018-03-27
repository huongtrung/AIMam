package vn.gmorunsystem.aimam.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.util.List;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.ShopSubscribedListRequest;
import vn.gmorunsystem.aimam.apis.request.UnSubscribeListRequest;
import vn.gmorunsystem.aimam.apis.response.ShopSubscribedListResponse;
import vn.gmorunsystem.aimam.apis.response.UnSubscribeResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.SubscribeBean;
import vn.gmorunsystem.aimam.callback.OnUnsubscribe;
import vn.gmorunsystem.aimam.ui.adapter.OnRecyclerViewItemClick;
import vn.gmorunsystem.aimam.ui.adapter.ShopFavoriteAdapter;
import vn.gmorunsystem.aimam.utils.AppUtils;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.ToastUtil;

public class ShopFavoriteFragment extends BaseFragment implements OnUnsubscribe {
    @BindView(R.id.rv_favorite)
    RecyclerView rvFavorite;
    @BindView(R.id.tv_favlist_empty)
    TextView tvNoData;
    @BindView(R.id.adViewBanner)
    AdView adBanner;

    ShopFavoriteAdapter favoriteAdapter;
    List<SubscribeBean> beanList;

    public static ShopFavoriteFragment newInstance() {
        ShopFavoriteFragment fragment = new ShopFavoriteFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_restaurant_favorite;
    }

    @Override
    protected void initView(View root) {
        setScreenTitle(R.string.app_name);
        getShopFavoriteRequest(false);
    }

    @Override
    public void onResume() {
        if (adBanner != null) {
            adBanner.resume();
        }
        super.onResume();
        getMainActivity().showBottomBarAndSearchIcon();
    }

    @Override
    public void onPause() {
        if (adBanner != null) {
            adBanner.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adBanner != null) {
            adBanner.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void initData() {
        AppUtils.loadAdBanner(adBanner,getActivity());
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    private void getShopFavoriteRequest(boolean isRefresh) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            if (!isRefresh)
                showProgressBar();
            ShopSubscribedListRequest request = new ShopSubscribedListRequest();
            request.setRequestCallBack(new ApiObjectCallBack<ShopSubscribedListResponse>() {
                @Override
                public void onSuccess(ShopSubscribedListResponse data) {
                    hideProgressBar();
                    if (data.subscribeBean != null && data.subscribeBean.size() > 0) {
                        beanList = data.subscribeBean;
                        setUpSubscribeList(data.subscribeBean);
                    } else {
                        if (rvFavorite != null) {
                            rvFavorite.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            request.execute();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }


    private void setUpSubscribeList(final List<SubscribeBean> subscribeBean) {
        favoriteAdapter = new ShopFavoriteAdapter(subscribeBean);
        favoriteAdapter.setOnUnsubscribe(this);
        rvFavorite.setAdapter(favoriteAdapter);
        favoriteAdapter.notifyDataSetChanged();
        favoriteAdapter.setOnRecyclerViewItemClick(new OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                replaceFragment(R.id.container, ShopMainFragment.newInstance(subscribeBean.get(position).shopId), true);
            }
        });
        rvFavorite.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFavorite.setNestedScrollingEnabled(false);

    }

    @Override
    public void unSubscribe(int subId, final int position) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            UnSubscribeListRequest unSubscribeRequest = new UnSubscribeListRequest(subId);
            unSubscribeRequest.setRequestCallBack(new ApiObjectCallBack<UnSubscribeResponse>() {
                @Override
                public void onSuccess(UnSubscribeResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        beanList.remove(position);
                        favoriteAdapter.notifyDataSetChanged();
                        if (beanList.size() == 0) {
                            rvFavorite.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                        }
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
            showProgressBar();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }


}
