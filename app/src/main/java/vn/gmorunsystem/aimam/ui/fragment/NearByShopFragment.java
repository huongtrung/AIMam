package vn.gmorunsystem.aimam.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.NearByShopRequest;
import vn.gmorunsystem.aimam.apis.response.NearByShopResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.ShopAdvBean;
import vn.gmorunsystem.aimam.bean.ShopSuggestBean;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.ui.customview.CustomInfoWindow;
import vn.gmorunsystem.aimam.utils.AppUtils;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;

/**
 * Created by Veteran Commander on 6/6/2017.
 */

public class NearByShopFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.adViewBanner)
    AdView adBanner;
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location myLocation;
    View view;
    double longitude;
    double latitude;
    LatLng topLeftLatLng;
    LatLng botRightLatLng;
    LatLngBounds latLngBounds;

    LatLng updatedCenterLocation;

    boolean isLoadingData = false;
    boolean canReload = true;

    NearByShopResponse nearByShopResponseData;
    Marker currentSelectMarker;


    public static NearByShopFragment newInstance() {
        NearByShopFragment fragment = new NearByShopFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nearby_shop;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapsInitializer.initialize(getActivity());
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        AppUtils.loadAdBanner(adBanner, getActivity());

    }

    @Override
    protected void initView(View root) {
        setScreenTitle(R.string.title_nearby);

    }

    @Override
    protected void initData() {
        nearByShopResponseData = new NearByShopResponse();
    }

    @Override
    public void onResume() {
        if (adBanner != null) {
            adBanner.resume();
        }
        if (mapView != null){
            mapView.onResume();
        }
        super.onResume();
        getMainActivity().showBottomBarAndSearchIcon();
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setOnInfoWindowClickListener(this);
        if (mapView != null) {
            mapView.onResume();
        }
        mMap.setOnInfoWindowCloseListener(new GoogleMap.OnInfoWindowCloseListener() {
            @Override
            public void onInfoWindowClose(Marker marker) {
                canReload = true;
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                currentSelectMarker = marker;
                canReload = false;
                return false;
            }
        });
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (currentSelectMarker != null && currentSelectMarker.isInfoWindowShown()) {
                    canReload = false;
                } else {
                    canReload = true;
                }
            }
        });
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                updatedCenterLocation = mMap.getCameraPosition().target;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (canReload) {
                            if (!isLoadingData) {
                                getLatLngBoundToRequestData(false);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.load_data_nearby), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, 1000);


            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                canReload = true;
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showSettingsAlert();
            return;
        }

        myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (myLocation != null) {
            latitude = myLocation.getLatitude();
            longitude = myLocation.getLongitude();
        }
        final LatLng latLng = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f));
        mMap.setMyLocationEnabled(true);

        if (nearByShopResponseData != null) {
            setUpMap(nearByShopResponseData);
        } else {
            getLatLngBoundToRequestData(true);
        }

    }

    public void showSettingsAlert() {
        DialogUtil.showDialogAsk(getContext(), getString(R.string.gps_enabled), getString(R.string.gps_setting),
                getString(R.string.setting), getString(R.string.title_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, APPConstant.REQUEST_CODE);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        onConnected(null);
                    }
                });
    }

    private void getLatLngBoundToRequestData(boolean showLoading) {
        latLngBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        topLeftLatLng = new LatLng(latLngBounds.northeast.latitude, latLngBounds.southwest.longitude);
        botRightLatLng = new LatLng(latLngBounds.southwest.latitude, latLngBounds.northeast.longitude);
        getShopNearByRequest(topLeftLatLng, botRightLatLng, showLoading);
    }

    private void getShopNearByRequest(LatLng topleft, LatLng botright, boolean isShowProgress) {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            NearByShopRequest request = new NearByShopRequest(topleft, botright);
            request.setRequestCallBack(new ApiObjectCallBack<NearByShopResponse>() {
                @Override
                public void onSuccess(final NearByShopResponse response) {
                    hideProgressBar();
                    isLoadingData = false;
                    if (response != null &&
                            (response.shopSuggests.size() > 0 || response.shopAdvs.size() > 0)) {
                        if (nearByShopResponseData != null) {
                            if (nearByShopResponseData.shopAdvs != null) {
                                nearByShopResponseData.shopAdvs.clear();
                            }
                            if (nearByShopResponseData.shopSuggests != null) {
                                nearByShopResponseData.shopSuggests.clear();
                            }
                        }
                        mMap.clear();
                        nearByShopResponseData = response;
                        setUpMap(nearByShopResponseData);
                    } else {
                        DialogUtil.showDialog(getContext(), getString(R.string.not_have_data));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                    isLoadingData = false;
                }
            });
            request.execute();
            if (isShowProgress) {
                showProgressBar();
            }
            isLoadingData = true;
        } else {
            DialogUtil.showDialog(getContext(), getString(R.string.no_connect_internet));
        }
    }

    private void setUpMap(NearByShopResponse data) {
        if (data.shopSuggests != null && data.shopSuggests.size() > 0) {
            List<ShopSuggestBean> suggestBeans = data.shopSuggests;
            Collections.sort(suggestBeans);
            for (int i = 0; i < data.shopSuggests.size(); i++) {
                CustomInfoWindow nearByShop = new CustomInfoWindow(getActivity());
                mMap.setInfoWindowAdapter(nearByShop);
                if (i == 0) {
                    loadSuggestCustomIcon(data.shopSuggests.get(i), R.layout.gold_custom_marker);
                } else if (i == 1) {
                    loadSuggestCustomIcon(data.shopSuggests.get(i), R.layout.silver_custom_marker);
                } else if (i == 2) {
                    loadSuggestCustomIcon(data.shopSuggests.get(i), R.layout.copper_custom_marker);
                } else {
                    loadSuggestCustomIcon(data.shopSuggests.get(i), R.layout.normal_custom_marker);
                }

            }
        }
        if (data.shopAdvs != null && data.shopAdvs.size() > 0) {
            for (int i = 0; i < data.shopAdvs.size(); i++) {
                loadAdvCustomIcon(data.shopAdvs.get(i), R.layout.normal_custom_marker);
            }
        }
    }


    private void loadSuggestCustomIcon(final ShopSuggestBean bean, int layoutId) {
        final View mCustomMarkerView;
        final CircleImageView mMarkerImageView;
        if (isAdded()) {
            mCustomMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
            mMarkerImageView = (CircleImageView) mCustomMarkerView.findViewById(R.id.dummy_marker);
            Glide.with(getActivity()).load(bean.avatarImageUrl).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    LatLng shopLocation = new LatLng(bean.mapLatitude, bean.mapLongtitude);
                    Marker mapMarker = mMap.addMarker(new MarkerOptions().position(shopLocation).title(bean.name).snippet(bean.avatarImageUrl));
                    mapMarker.setAnchor(0.5f, 1);
                    mapMarker.setTag(bean.id);
                    mapMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, mMarkerImageView, resource)));

                }

            });
        }
    }

    private void loadAdvCustomIcon(final ShopAdvBean bean, int layoutId) {
        final View mCustomMarkerView;
        final CircleImageView mMarkerImageView;
        if (isAdded()) {
            mCustomMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(layoutId, null);
            mMarkerImageView = (CircleImageView) mCustomMarkerView.findViewById(R.id.dummy_marker);
            Glide.with(getActivity()).load(bean.avatarImageUrl).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    LatLng shopLocation = new LatLng(bean.mapLatitude, bean.mapLongtitude);
                    Marker mapMarker = mMap.addMarker(new MarkerOptions().position(shopLocation).title(bean.name).snippet(bean.avatarImageUrl));
                    mapMarker.setAnchor(0.5f, 1);
                    mapMarker.setTag(bean.id);
                    mapMarker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, mMarkerImageView, resource)));

                }

            });
        }
    }


    private Bitmap getMarkerBitmapFromView(View view, CircleImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mapView != null){
            mapView.onStart();
        }
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        if (mapView != null){
            mapView.onStop();
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        if (adBanner != null) {
            adBanner.pause();
        }
        if (mapView != null){
            mapView.onPause();
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        replaceFragment(R.id.container, ShopMainFragment.newInstance((Integer) marker.getTag()), true);
    }


}
