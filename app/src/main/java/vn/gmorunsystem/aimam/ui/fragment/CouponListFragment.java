package vn.gmorunsystem.aimam.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.CouponListRequest;
import vn.gmorunsystem.aimam.apis.response.CouponListResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.CouponDetailBean;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.ui.adapter.CouponListAdapter;
import vn.gmorunsystem.aimam.ui.adapter.OnRecyclerViewItemClick;
import vn.gmorunsystem.aimam.ui.customview.EndlessRecyclerOnScrollListener;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by Veteran Commander on 5/18/2017.
 */

public class CouponListFragment extends BaseFragment {
    private static final int DEFAULT_PAGER = 1;

    @BindView(R.id.rc_couponlist)
    RecyclerView rcCoupon;
    @BindView(R.id.swipeRF_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.tv_couponlist_empty)
    TextView tvNoData;

    CouponListAdapter couponListAdapter;
    int totalPage = 1;
    private int mPage = 1;
    List<CouponDetailBean> couponList;

    public static CouponListFragment newInstance() {
        CouponListFragment fragment = new CouponListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_couponlist;
    }

    @Override
    protected void initView(View root) {
        setScreenTitle(getString(R.string.txt_coupon_list));
        getMainActivity().hideBottomBarAndSearchIcon();
        rcCoupon.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_light_green));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCouponListRequest(true, false);
            }
        });

    }

    @Override
    protected void initData() {
        if (couponList != null && couponList.size() > 0) {
            setUpCouponList(couponList);
        } else {
            getCouponListRequest(false, false);
        }
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    private void getCouponListRequest(final boolean isRefresh, boolean isLoadmore) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            if (isLoadmore)
                mPage++;
            else {
                mPage = DEFAULT_PAGER;
            }
            if (mPage <= totalPage) {
                CouponListRequest couponListRequest = new CouponListRequest(mPage);
                couponListRequest.setRequestCallBack(new ApiObjectCallBack<CouponListResponse>() {
                    @Override
                    public void onSuccess(CouponListResponse data) {
                        hideProgressBar();
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        if (data != null && data.couponDetailList.size() > 0) {
                            totalPage = data.totalPages;
                            if (mPage == DEFAULT_PAGER) {
                                couponList = data.couponDetailList;
                                setUpCouponList(couponList);
                            } else {
                                couponList.addAll(data.couponDetailList);
                                couponListAdapter.notifyDataSetChanged();
                            }
                        } else {
                            rcCoupon.setVisibility(View.GONE);
                            tvNoData.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFail(int failCode, String message) {
                        hideProgressBar();
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                    }
                });
                couponListRequest.execute();
                if (!isRefresh) {
                    showProgressBar();
                }
            }

        } else {
            Toast.makeText(getActivity(), getString(R.string.no_connect_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpCouponList(final List<CouponDetailBean> beanList) {
        couponListAdapter = new CouponListAdapter(beanList);
        couponListAdapter.setOnRecyclerViewItemClick(new OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                if (UiUtil.isClickable()) {
                    replaceFragment(R.id.container, CouponDetailFragment.newInstance(beanList.get(position).id, true), APPConstant.COUPON_DETAIL_FRAG_TAG, true);
                }
            }
        });

        rcCoupon.setAdapter(couponListAdapter);
        rcCoupon.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(int currentPage) {
                if (currentPage <= totalPage) {
                    getCouponListRequest(false, true);
                }
            }
        });
    }
}
