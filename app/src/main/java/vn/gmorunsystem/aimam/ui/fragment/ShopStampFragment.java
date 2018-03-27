package vn.gmorunsystem.aimam.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.CheckInShopRequest;
import vn.gmorunsystem.aimam.apis.request.ShopStampGiftRequest;
import vn.gmorunsystem.aimam.apis.request.ShopStampRequest;
import vn.gmorunsystem.aimam.apis.response.CheckInShopResponse;
import vn.gmorunsystem.aimam.apis.response.ShopStampGiftResponse;
import vn.gmorunsystem.aimam.apis.response.ShopStampResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.ui.adapter.ShopStampAdapter;
import vn.gmorunsystem.aimam.ui.customview.SpacesItemDecoration;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.GPSTracker;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

import static vn.gmorunsystem.aimam.utils.DialogUtil.showDialog;

public class ShopStampFragment extends BaseFragment {
    private static final String KEY_SHOP_ID = "KEY_SHOP_ID";

    @BindView(R.id.ll_stamp)
    LinearLayout llStamp;
    @BindView(R.id.rv_stamps)
    RecyclerView rvStamps;
    @BindView(R.id.btn_check_in)
    Button btnCheckIn;
    @BindView(R.id.stamp_count)
    TextView tvStampCount;
    @BindView(R.id.tv_shop_stamp_empty)
    TextView tvEmpty;

    ShopStampResponse mShopStampResponse;
    ShopStampAdapter shopStampAdapter;
    private GPSTracker mGpsTracker;
    private int mShopId;
    private String typeError;
    private String valueError;

    private IGetShopStampGift iGetShopStampGift = new IGetShopStampGift() {
        @Override
        public void getShopStampGift(final int position) {
            getShopStampGiftRequest(position);
        }
    };

    public static ShopStampFragment newInstance(int shopId) {
        ShopStampFragment fragment = new ShopStampFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SHOP_ID, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_restaurant_stamp;
    }

    @Override
    protected void initView(View root) {
        mGpsTracker = new GPSTracker(getContext());
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mShopId = getArguments().getInt(KEY_SHOP_ID);
            if (mShopStampResponse == null)
                getShopStampRequest();
            else {
                handleShopStampResponse(mShopStampResponse);
            }
        }
    }

    @Override
    protected void getArgument(Bundle bundle) {
    }

    @OnClick(R.id.btn_check_in)
    public void onClick() {
        if (!UiUtil.isClickable()) {
            return;
        }
        checkInShopRequest();
    }

    private void getShopStampRequest() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            ShopStampRequest shopStampRequest = new ShopStampRequest(mShopId);
            shopStampRequest.setRequestCallBack(new ApiObjectCallBack<ShopStampResponse>() {
                @Override
                public void onSuccess(ShopStampResponse data) {
                    if (!isVisible()) {
                        return;
                    }
                    if (data != null) {
                        tvEmpty.setVisibility(View.GONE);
                        rvStamps.setVisibility(View.VISIBLE);
                        mShopStampResponse = data;
                        handleShopStampResponse(data);
                    } else {
                        tvEmpty.setVisibility(View.VISIBLE);
                        rvStamps.setVisibility(View.GONE);
                        tvStampCount.setText("0");
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            shopStampRequest.execute();
        } else {
            ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
        }
    }

    private void getShopStampGiftRequest(int position) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            showProgressBar();
            ShopStampGiftRequest shopStampGiftRequest = new ShopStampGiftRequest(mShopId, position);
            shopStampGiftRequest.setRequestCallBack(new ApiObjectCallBack<ShopStampGiftResponse>() {
                @Override
                public void onSuccess(ShopStampGiftResponse data) {
                    hideProgressBar();
                    if (!isVisible()) {
                        return;
                    }
                    if (data != null) {
                        mShopStampResponse.stampCount = data.stampCount;
                        shopStampAdapter.notifyDataSetChanged();
                        StringUtil.displayText("" + data.stampCount, tvStampCount);
                    } else {
                        DialogUtil.showDialog(getContext(), getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogSpecialError(getContext(), failCode, message, APPConstant.API_GET_GIFT);
                }
            });
            shopStampGiftRequest.execute();
        } else {
            ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
        }
    }

    private void checkInShopRequest() {
        if (mGpsTracker.canGetLocation()) {
            double lat = mGpsTracker.getLatitude();
            double lon = mGpsTracker.getLongitude();

            if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
                showProgressBar();
                CheckInShopRequest checkInShopRequest = new CheckInShopRequest(mShopId, lat, lon);
                checkInShopRequest.setRequestCallBack(new ApiObjectCallBack<CheckInShopResponse>() {
                    @Override
                    public void onSuccess(CheckInShopResponse data) {
                        if (!isVisible()) {
                            return;
                        }
                        hideProgressBar();
                        mShopStampResponse.stampCount++;
                        StringUtil.displayText("" + mShopStampResponse.stampCount, tvStampCount);
                        shopStampAdapter.notifyDataSetChanged();
                        if (data != null) {
                            showDialog(getContext(), getString(R.string.title_check_in_success));
                        } else {
                            showDialog(getContext(), getString(R.string.error_server));
                        }
                    }

                    @Override
                    public void onFail(int failCode, String message) {
                        hideProgressBar();
                        CheckErrorCodeUtil.showDialogSpecialError(getContext(), failCode, message, APPConstant.API_CHECK_IN);
                    }
                });
                checkInShopRequest.execute();
            } else {
                ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
            }
        } else {
            showSettingsAlert();
        }

    }

    public void showSettingsAlert() {
        DialogUtil.showDialogAsk(getContext(), getString(R.string.gps_enabled), getString(R.string.gps_setting), getString(R.string.setting), getString(R.string.title_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, APPConstant.REQUEST_CODE);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    private void handleShopStampResponse(ShopStampResponse data) {
        if (tvStampCount != null) {
            StringUtil.displayText("" + data.stampCount, tvStampCount);
        }
        shopStampAdapter = new ShopStampAdapter(getContext(), data, iGetShopStampGift);
        rvStamps.setAdapter(shopStampAdapter);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 5, GridLayoutManager.VERTICAL, false);
        rvStamps.setLayoutManager(manager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.stamp_item_spacing);
        rvStamps.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
    }

    public interface IGetShopStampGift {
        void getShopStampGift(int position);
    }

}
