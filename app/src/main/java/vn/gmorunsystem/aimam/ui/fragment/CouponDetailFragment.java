package vn.gmorunsystem.aimam.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.CouponDetailRequest;
import vn.gmorunsystem.aimam.apis.response.CouponDetailResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;

import static vn.gmorunsystem.aimam.utils.StringUtil.formatDateCoupon;

/**
 * Created by Veteran Commander on 5/18/2017.
 */

public class CouponDetailFragment extends BaseFragment {
    private static final String KEY_COUPON_ID = "KEY_COUPON_ID";
    @BindView(R.id.tv_label)
    TextView tvLabel;
    @BindView(R.id.tv_couponcontent)
    TextView tvContent;
    @BindView(R.id.tv_expire)
    TextView tvToExpireDay;
    @BindView(R.id.tv_coupondetail_storeName)
    TextView tvShopName;
    @BindView(R.id.tv_store_address)
    TextView tvShopAdd;
    @BindView(R.id.iv_coupondetail_head)
    ImageView ivLogo;
    @BindView(R.id.btn_coupon_code)
    Button btnCode;

    int couponId;
    int shopId;
    CouponDetailResponse couponData;
    boolean isFromListCoupon;
    final String dateHourFormat = "dd/MM/yyyy HH:mm:ss";

    public static CouponDetailFragment newInstance(int couponId, boolean isFromListCoupon) {
        CouponDetailFragment fragment = new CouponDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_COUPON_ID, couponId);
        fragment.setArguments(bundle);
        fragment.isFromListCoupon = isFromListCoupon;
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_coupondetail;
    }

    @Override
    protected void initView(View root) {
        if (isFromListCoupon) {
            getMainActivity().hideBottomBarAndSearchIcon();
        } else {
            getMainActivity().showBottomBarAndSearchIcon();
        }
    }

    @Override
    protected void initData() {
        if (couponData != null) {
            initCouponDetail(couponData);
        } else getCouponDetail();

    }

    @Override
    protected void getArgument(Bundle bundle) {
        couponId = bundle.getInt(KEY_COUPON_ID);
    }

    private void getCouponDetail() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            CouponDetailRequest couponDetailRequest = new CouponDetailRequest(couponId);
            couponDetailRequest.setRequestCallBack(new ApiObjectCallBack<CouponDetailResponse>() {
                @Override
                public void onSuccess(CouponDetailResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        couponData = data;
                        if (isAdded()) {
                            initCouponDetail(couponData);
                        }
                    } else
                        DialogUtil.showDialog(getContext(), getString(R.string.not_have_data));
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            couponDetailRequest.execute();
            showProgressBar();
        } else
            Toast.makeText(getActivity(), getString(R.string.no_connect_internet), Toast.LENGTH_SHORT).show();

    }

    public void initCouponDetail(CouponDetailResponse couponData) {
        if (couponData.shop != null) {
            setScreenTitle(couponData.shop.name);
            StringUtil.displayText(couponData.shop.name, tvShopName);
            StringUtil.displayText(couponData.shop.address, tvShopAdd);
        }
        StringUtil.displayText(couponData.title, tvLabel);
        StringUtil.displayText(couponData.content, tvContent);
        btnCode.setText(couponData.code);
        StringUtil.displayText(getString(R.string.expiry_date) + formatDateCoupon(couponData.expiryDate, dateHourFormat), tvToExpireDay);
        ImageLoader.loadImage(getActivity(), R.drawable.default_img, couponData.avatarImageUrl, ivLogo);
        shopId = couponData.shopId;
    }

    @OnClick(R.id.tv_coupondetail_storeName)
    public void onShopNameClicked(){
        replaceFragment(R.id.container,ShopMainFragment.newInstance(shopId));
    }
}
