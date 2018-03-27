package vn.gmorunsystem.aimam.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.GetNearestShopRequest;
import vn.gmorunsystem.aimam.apis.request.ShopSearchRequest;
import vn.gmorunsystem.aimam.apis.request.UploadImageRequest;
import vn.gmorunsystem.aimam.apis.response.BlankResponse;
import vn.gmorunsystem.aimam.apis.response.GetNearestShopResponse;
import vn.gmorunsystem.aimam.apis.response.ShopSearchResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.ImageInfoUploadBean;
import vn.gmorunsystem.aimam.bean.ShopMenuItem;
import vn.gmorunsystem.aimam.bean.ShopSearchBean;
import vn.gmorunsystem.aimam.callback.OnChangeMenuItemListener;
import vn.gmorunsystem.aimam.callback.OnItemClickListener;
import vn.gmorunsystem.aimam.callback.OnMenuItemRepairListener;
import vn.gmorunsystem.aimam.constants.APIConstant;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.ui.adapter.AutoCompleteAdapter;
import vn.gmorunsystem.aimam.ui.adapter.UploadPhotoListAdapter;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.GPSTracker;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;


public class UploadPhotoFragment extends BaseFragment {
    public static final String IMAGE_LIST = "IMAGE_LIST";
    private final long DELAY = 500;

    @BindView(R.id.rc_list_photoUpload)
    RecyclerView rcUploadPhotoList;
    @BindView(R.id.tv_upphoto_addr)
    TextView shopAddress;
    @BindView(R.id.edt_search_shop_upload)
    AutoCompleteTextView autoTvShopSearch;
    @BindView(R.id.tv_not_result)
    TextView tvNotResult;

    UploadPhotoListAdapter uploadPhotoListAdapter;
    List<String> imagePathList;
    private final Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC).create();
    Map<String, String> params = new HashMap<>();
    Map<String, File> files = new HashMap<>();
    AutoCompleteAdapter mAutoCompleteAdapter;
    List<ImageInfoUploadBean> imageInfoList;
    OnChangeMenuItemListener mListener;
    OnMenuItemRepairListener currentListener;

    private Location mLocation;
    private GPSTracker mGpsTracker;
    private Double mLat = 0.0;
    private Double mLong = 0.0;
    private Timer mTimer;

    public static UploadPhotoFragment newInstance(List<String> imagePaths) {
        UploadPhotoFragment fragment = new UploadPhotoFragment();
        Bundle listImage = new Bundle();
        listImage.putStringArrayList(IMAGE_LIST, (ArrayList<String>) imagePaths);
        fragment.setArguments(listImage);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_uploadphoto;
    }

    @Override
    protected void initView(View root) {
        setScreenTitle(R.string.title_upload_photo);
        getMainActivity().hideBottomBarAndSearchIcon();
        rcUploadPhotoList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGpsTracker = new GPSTracker(getContext());
        autoTvShopSearch.addTextChangedListener(new TextWatcher() {
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
                            doSearchShop(strSearch.toString());
                        }
                    }, DELAY);
                } else {
                    UiUtil.hideView(tvNotResult, true);
                }
            }
        });
        autoTvShopSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    doSearchShop(textView.getText().toString());
                }
                return true;
            }
        });
        autoTvShopSearch.setThreshold(1);
    }

    @Override
    protected void initData() {
        imageInfoList = new ArrayList<>();
        doRequestLocation();
        mListener = new OnChangeMenuItemListener() {
            @Override
            public void updateItem(int position, int itemId) {
                if (imageInfoList != null && imageInfoList.size() > 0) {
                    imageInfoList.get(position).itemId = itemId;
                }
            }

            @Override
            public void onRepairItem(OnMenuItemRepairListener listener) {
                if (currentListener != null) {
                    currentListener.repairListen();
                }
                currentListener = listener;
            }
        };
    }

    @Override
    protected void getArgument(Bundle bundle) {
        imagePathList = bundle.getStringArrayList(IMAGE_LIST);
    }

    @OnClick(R.id.btn_uploadImage)
    public void onClick() {
        setUpDataForUpload();
    }

    private void getShopNearest(Double latitude, Double longitude) {
        GetNearestShopRequest request = new GetNearestShopRequest(latitude, longitude);
        request.setRequestCallBack(new ApiObjectCallBack<GetNearestShopResponse>() {
            @Override
            public void onSuccess(GetNearestShopResponse data) {
                hideProgressBar();
                if (data != null) {
                    setUpInfoListForParams(data.id);
                    if (StringUtil.checkStringValid(data.address) || StringUtil.checkStringValid(data.name)) {
                        shopAddress.setText(data.name + "\n" + data.address);
                    }
                    if (data.items != null && data.items.size() > 0) {
                        setUpPhotoList(data.items);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.no_menu_items), Toast.LENGTH_SHORT).show();
                        setUpPhotoList(null);
                    }
                } else {
                    DialogUtil.showDialog(getActivity(), getString(R.string.not_have_data));
                }
            }

            @Override
            public void onFail(int failCode, String message) {
                hideProgressBar();
                CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                setUpPhotoList(null);
            }
        });
        request.execute();
        showProgressBar();
    }

    private void getShopNearest(int shopId) {
        GetNearestShopRequest request = new GetNearestShopRequest(shopId);
        request.setRequestCallBack(new ApiObjectCallBack<GetNearestShopResponse>() {
            @Override
            public void onSuccess(GetNearestShopResponse data) {
                hideProgressBar();
                if (data != null) {
                    changeShopIdForParams(data.id);
                    if (StringUtil.checkStringValid(data.address) || StringUtil.checkStringValid(data.name)) {
                        shopAddress.setText(data.name + "\n" + data.address);
                    }
                    if (data.items != null && data.items.size() > 0) {
                        setUpPhotoList(data.items);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.no_menu_items), Toast.LENGTH_SHORT).show();
                        setUpPhotoList(null);
                    }
                } else {
                    DialogUtil.showDialog(getActivity(), getString(R.string.not_have_data));
                }

            }

            @Override
            public void onFail(int failCode, String message) {
                hideProgressBar();
                setUpPhotoList(null);
                CheckErrorCodeUtil.showDialogError(getContext(), failCode);
            }
        });
        request.execute();
        showProgressBar();
    }

    private void uploadImageRequest() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            UploadImageRequest request = new UploadImageRequest(params, files);
            request.setRequestCallBack(new ApiObjectCallBack<BlankResponse>() {
                @Override
                public void onSuccess(BlankResponse data) {
                    hideProgressBar();
                    DialogUtil.showDialog(getActivity(), getString(R.string.text_success_upload));
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            request.execute();
            showProgressBar();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private void setUpPhotoList(List<ShopMenuItem> menuItems) {
        uploadPhotoListAdapter = new UploadPhotoListAdapter(getMainActivity(), imagePathList, menuItems);
        uploadPhotoListAdapter.notifyDataSetChanged();
        uploadPhotoListAdapter.setOnChangeMenuItemListener(mListener);
        rcUploadPhotoList.setAdapter(uploadPhotoListAdapter);
        rcUploadPhotoList.setNestedScrollingEnabled(false);
        rcUploadPhotoList.setHasFixedSize(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void doRequestLocation() {
        if (mGpsTracker.canGetLocation()) {
            mLocation = mGpsTracker.getLocation();
            if (mLocation != null) {
                getShopNearest(mLocation.getLatitude(), mLocation.getLongitude());
            } else {
                getShopNearest(mLat, mLong);
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
                getShopNearest(mLat, mLong);
            }
        });
    }

    private void doSearchShop(final String searchContent) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            showProgressBar();
            ShopSearchRequest shopSearchRequest;
            if (mLocation != null) {
                shopSearchRequest = new ShopSearchRequest(searchContent, mLocation.getLatitude(), mLocation.getLongitude());
            } else {
                shopSearchRequest = new ShopSearchRequest(searchContent, mLat, mLong);
            }
            shopSearchRequest.setRequestCallBack(new ApiObjectCallBack<ShopSearchResponse>() {
                @Override
                public void onSuccess(ShopSearchResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        if (data.shopSearch != null && !data.shopSearch.isEmpty() && data.shopSearch.size() > 0) {
                            UiUtil.hideView(tvNotResult, true);
                            setUpAutoCompleteSearchShop(data.shopSearch);
                        } else {
                            UiUtil.showView(tvNotResult);
                            if (autoTvShopSearch.isPopupShowing())
                                autoTvShopSearch.dismissDropDown();
                            StringUtil.displayText(getString(R.string.not_found_result) + " " + searchContent, tvNotResult);
                        }
                    } else {
                        DialogUtil.showDialog(getActivity(), getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
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

    private void setUpAutoCompleteSearchShop(List<ShopSearchBean> data) {
        mAutoCompleteAdapter = new AutoCompleteAdapter(getContext(), R.layout.item_result_search, (ArrayList<ShopSearchBean>) data);
        autoTvShopSearch.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.setListener(new OnItemClickListener() {
            @Override
            public void onClick(int position, int id) {
                getShopNearest(id);
                autoTvShopSearch.dismissDropDown();
                hideSoftKeyboard();
            }
        });
        autoTvShopSearch.showDropDown();
        hideSoftKeyboard();
    }

    private void setUpDataForUpload() {
        int size = imagePathList.size();
        for (int i = 0; i < size; i++) {
            int n = i + 1;
            files.put(APIConstant.IMAGE + n, new File(imagePathList.get(i)));
            ImageInfoUploadBean bean = imageInfoList.get(i);
            params.put(APIConstant.INFO + n, gson.toJson(bean));
        }
        uploadImageRequest();
    }

    private void setUpInfoListForParams(int shopId) {
        int size = imagePathList.size();
        if (imageInfoList.size() > 0) {
            imageInfoList.clear();
        }
        for (int i = 0; i < size; i++) {
            ImageInfoUploadBean bean = new ImageInfoUploadBean();
            bean.shopId = shopId;
            imageInfoList.add(bean);
        }
    }

    private void changeShopIdForParams(int shopId) {
        for (int i = 0; i < imageInfoList.size(); i++) {
            imageInfoList.get(i).shopId = shopId;
        }
    }

}
