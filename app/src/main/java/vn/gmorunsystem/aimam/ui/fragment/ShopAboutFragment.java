package vn.gmorunsystem.aimam.ui.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.ContactUsRequest;
import vn.gmorunsystem.aimam.apis.request.GetShopRequest;
import vn.gmorunsystem.aimam.apis.response.ContactUsResponse;
import vn.gmorunsystem.aimam.apis.response.GetShopResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.ui.customview.ScrollMapView;
import vn.gmorunsystem.aimam.utils.AnimationUtil;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.IntentUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

public class ShopAboutFragment extends BaseFragment implements OnMapReadyCallback {
    private static String KEY_SHOP_ID = "KEY_SHOP_ID_ABOUT";

    @BindView(R.id.bt_contact_us)
    Button btnContactUs;
    @BindView(R.id.ll_contact_text)
    LinearLayout llContactText;
    @BindView(R.id.ll_contact_input)
    LinearLayout llContactInput;
    @BindView(R.id.tv_code)
    TextView tvRandomCode;
    @BindView(R.id.et_input_code)
    EditText etInputCode;
    @BindView(R.id.iv_refresh_code)
    ImageView ivRefreshCode;
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.et_title_mess)
    EditText etTitle;
    @BindView(R.id.et_content_mess)
    EditText etContent;
    @BindView(R.id.tv_address)
    TextView tvShopAddress;
    @BindView(R.id.tv_opening)
    TextView tvShopOpen;
    @BindView(R.id.tv_hours)
    TextView tvHours;
    @BindView(R.id.tv_website)
    TextView tvShopWebsite;
    @BindView(R.id.tv_phone)
    TextView tvShopPhone;
    @BindView(R.id.tv_email)
    TextView tvShopEmail;
    @BindView(R.id.txt_about)
    TextView tvAbout;
    @BindView(R.id.txt_about_content)
    TextView tvAboutContent;
    @BindView(R.id.scroll_map)
    ScrollMapView scrollMapView;
    @BindView(R.id.sv_map)
    ScrollView svMap;
    @BindView(R.id.ll_about)
    LinearLayout llAbout;
    @BindView(R.id.tv_error_phone)
    TextView tvErrorPhone;
    @BindView(R.id.tv_error_title)
    TextView tvErrorTitle;
    @BindView(R.id.tv_error_content)
    TextView tvErrorContent;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.ll_open_day)
    LinearLayout llOpenDay;
    @BindView(R.id.ll_hour)
    LinearLayout llHour;
    @BindView(R.id.ll_website)
    LinearLayout llWebsite;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;

    private GoogleMap mMap;
    private String mUserName;
    private String mPhoneNum;
    private String mTitle;
    private String mContent;
    private int mShopId;
    private String mPhone;
    private String mEmail;
    private String mWebsite;
    private Float mLat;
    private Float mLong;
    GetShopResponse mShopAboutResponse;

    public static ShopAboutFragment newInstance(int shopId) {
        ShopAboutFragment fragment = new ShopAboutFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_SHOP_ID, shopId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_restaurant_about;
    }

    @Override
    protected void initView(View root) {
    }

    @Override
    protected void initData() {
        etInputCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        StringUtil.displayText(StringUtil.randomString(), tvRandomCode);
    }

    @Override
    protected void getArgument(Bundle bundle) {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mShopId = getArguments().getInt(KEY_SHOP_ID);
            if (mShopAboutResponse == null)
                getShopAboutRequest();
            else
                handleGetShopSuccess(mShopAboutResponse);
        }
    }

    @OnClick({R.id.bt_contact_us, R.id.bt_cancel, R.id.btn_send, R.id
            .iv_refresh_code, R.id.ll_phone, R.id.ll_email, R.id.ll_website})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.bt_contact_us:
                showOrHideWhenClick();
                break;
            case R.id.bt_cancel:
                clearTextWhenClick();
                showOrHideWhenClick();
                break;
            case R.id.btn_send:
                if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
                    getContactUsRequest();
                } else {
                    ToastUtil.makeText(getContext(),
                            getString(R.string.no_connect_internet));
                }
                break;
            case R.id.iv_refresh_code:
                clearInputCode();
                AnimationUtil.loadAnimationRotate(getContext(), ivRefreshCode);
                break;
            case R.id.ll_phone:
                if (!StringUtil.isEmpty(mPhone))
                    IntentUtil.openPhoneCall(getActivity(), mPhone);
                break;
            case R.id.ll_email:
                if (!StringUtil.isEmpty(mEmail))
                    IntentUtil.openSendMail(getActivity(), mEmail);
                break;
            case R.id.ll_website:
                if (!StringUtil.isEmpty(mWebsite))
                    IntentUtil.openWebPage(getActivity(), mWebsite);
                break;
        }
    }

    private void getShopAboutRequest() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            GetShopRequest getShopRequest = new GetShopRequest(mShopId);
            getShopRequest.setRequestCallBack(new ApiObjectCallBack<GetShopResponse>() {
                @Override
                public void onSuccess(GetShopResponse data) {
                    if (!isVisible()) {
                        return;
                    }
                    if (data != null) {
                        mShopAboutResponse = data;
                        handleGetShopSuccess(data);
                    } else {
                        DialogUtil.showDialog(getContext(), getString(R.string.not_have_data));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            getShopRequest.execute();
        } else {
            ToastUtil.makeText(getContext(),
                    getString(R.string.no_connect_internet));
        }
    }

    private void handleGetShopSuccess(GetShopResponse inData) {
        mPhone = inData.phone;
        mEmail = inData.email;
        mWebsite = inData.website;
        mLat = inData.mapLatitude;
        mLong = inData.mapLongtitude;

        setUpLocation();
        UiUtil.showView(llAbout);
        StringUtil.displayText(getString(R.string.about_title) + " " + inData.name, tvAbout);
        StringUtil.displayText(inData.description, tvAboutContent);
        if (TextUtils.isEmpty(inData.address)) {
            UiUtil.hideView(llAddress, true);
        } else {
            StringUtil.displayText(inData.address, tvShopAddress);
        }


        if (TextUtils.isEmpty(inData.website)) {
            UiUtil.hideView(llWebsite, true);
        } else {
            StringUtil.displayText(inData.website, tvShopWebsite);
        }


        if (TextUtils.isEmpty(inData.openingDays) && TextUtils
                .isEmpty(inData.closingDays)) {
            UiUtil.hideView(llOpenDay, true);
        } else {
            String datetime = StringUtil
                    .formatDatetime(inData.openingDays) + " - " + StringUtil
                    .formatDatetime(inData.closingDays);
            StringUtil.displayText(datetime, tvShopOpen);
        }

        if (TextUtils.isEmpty(inData.openingHours)) {
            UiUtil.hideView(llHour, true);
        } else {
            StringUtil.displayText(inData.openingHours, tvHours);
        }

        if (TextUtils.isEmpty(inData.phone)) {
            UiUtil.hideView(llPhone, true);
        } else {
            StringUtil.displayText(inData.phone, tvShopPhone);
        }

        if (TextUtils.isEmpty(inData.email)) {
            UiUtil.hideView(llEmail, true);
        } else {
            StringUtil.displayText(inData.email, tvShopEmail);
        }
    }

    private void getContactUsRequest() {
        mUserName = SharedPrefUtils.getUserName();
        mPhoneNum = etPhoneNum.getText().toString().trim();
        mTitle = etTitle.getText().toString().trim();
        mContent = etContent.getText().toString().trim();

        if (validateInput()) {
            showProgressBar();
            hideSoftKeyboard();
            ContactUsRequest contactUsRequest = new ContactUsRequest();
            contactUsRequest.setUsername(mUserName);
            contactUsRequest.setPhoneNum(mPhoneNum);
            contactUsRequest.setShopId(mShopId);
            contactUsRequest.setTitleMess(mTitle);
            contactUsRequest.setContentMess(mContent);
            contactUsRequest.setRequestCallBack(new ApiObjectCallBack<ContactUsResponse>() {
                @Override
                public void onSuccess(ContactUsResponse data) {
                    hideProgressBar();
                    clearInputCode();
                    if (data != null) {
                        clearTextWhenClick();
                        showOrHideWhenClick();
                        DialogUtil.showDialog(getContext(), getString(R.string.text_contact_success));
                    } else {
                        DialogUtil.showDialog(getContext(), getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    clearInputCode();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            contactUsRequest.execute();
        }
    }

    private void clearInputCode() {
        etInputCode.setText("");
        StringUtil.displayText(StringUtil.randomString(), tvRandomCode);
    }

    private boolean validateInput() {
        tvErrorPhone.setVisibility(View.GONE);
        tvErrorTitle.setVisibility(View.GONE);
        tvErrorContent.setVisibility(View.GONE);
        etPhoneNum.setBackgroundResource(R.color.gray_dark);
        etTitle.setBackgroundResource(R.color.gray_dark);
        etContent.setBackgroundResource(R.color.gray_dark);
        etInputCode.setBackgroundResource(R.color.gray_dark);

        if (!TextUtils.isEmpty(mPhoneNum) && !mPhoneNum.matches(StringUtil.PHONE_NUMBER_REGREX)) {
            clearInputCode();
            tvErrorPhone.setVisibility(View.VISIBLE);
            etPhoneNum.setBackgroundResource(R.drawable.bg_error_edit_text_gray);
            return false;
        } else if (!StringUtil.checkStringValid(mTitle) || mTitle.length() < 4) {
            clearInputCode();
            tvErrorTitle.setVisibility(View.VISIBLE);
            etTitle.setBackgroundResource(R.drawable.bg_error_edit_text_gray);
            return false;
        } else if (!StringUtil.checkStringValid(mContent)) {
            clearInputCode();
            tvErrorContent.setVisibility(View.VISIBLE);
            etContent.setBackgroundResource(R.drawable.bg_error_edit_text_gray);
            return false;
        } else {
            String randomCode = tvRandomCode.getText().toString().trim();
            String randomCodeInput = etInputCode.getText().toString().trim();
            if (!randomCodeInput.equalsIgnoreCase(randomCode)) {
                clearInputCode();
                etInputCode.setBackgroundResource(R.drawable.bg_error_edit_text_gray);
                return false;
            }
        }

        return true;
    }

    private void showOrHideWhenClick() {
        showOrHideView(llContactInput);
        showOrHideView(llContactText);
        showOrHideView(btnContactUs);
    }

    private void clearTextWhenClick() {
        etPhoneNum.setText("");
        etTitle.setText("");
        etContent.setText("");
        clearInputCode();
    }

    @OnEditorAction(R.id.et_input_code)
    public boolean actionDone(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            getContactUsRequest();
            return true;
        }
        return false;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handleMapScroll(savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (scrollMapView != null) {
            scrollMapView.onResume();
        }
    }

    private void setUpLocation() {
        if (mMap != null && mLat != null && mLong != null) {
            LatLng latLng = new LatLng(mLat, mLong);
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
        }
    }

    private void handleMapScroll(Bundle stateInstanceState) {
        scrollMapView.onCreate(stateInstanceState);
        scrollMapView.setViewParent(svMap);
        scrollMapView.getMapAsync(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        scrollMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        scrollMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (scrollMapView != null) {
            scrollMapView.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (scrollMapView != null) {
            scrollMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (scrollMapView != null) {
            scrollMapView.onLowMemory();
        }
    }
}
