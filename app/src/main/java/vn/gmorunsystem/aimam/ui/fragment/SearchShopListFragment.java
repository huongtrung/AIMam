package vn.gmorunsystem.aimam.ui.fragment;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.bean.ShopAdvBean;
import vn.gmorunsystem.aimam.bean.ShopSuggestBean;
import vn.gmorunsystem.aimam.bean.data.ShopAdvListParcel;
import vn.gmorunsystem.aimam.bean.data.ShopSuggestListParcel;
import vn.gmorunsystem.aimam.callback.ChangeFragmentListener;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.utils.EventBusHelper;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

/**
 * Created by HuongTrung on 08/18/2017.
 */

public class SearchShopListFragment extends BaseFragment {
    private static final String KEY_SUGG_LIST = "KEY_SUGG_LIST";
    private static final String KEY_ADV_LIST = "ADV_LIST";
    private static final String KEY_USER_LOCATION = "KEY_USER_LOCATION";
    private static final String IS_ODD_PAGE = "is odd page";
    List<ShopSuggestBean> shopSuggestBeanList;
    List<ShopAdvBean> shopAdvBeanList;
    ShopSuggestListParcel listSuggest;
    ShopAdvListParcel listAdv;

    @BindView(R.id.iv_shop_one)
    ImageView ivShopOne;
    @BindView(R.id.iv_shop_two)
    ImageView ivShopTwo;
    @BindView(R.id.iv_shop_three)
    ImageView ivShopThree;
    @BindView(R.id.tv_shop_name_one)
    TextView tvNameOne;
    @BindView(R.id.tv_shop_name_two)
    TextView tvNameTwo;
    @BindView(R.id.tv_shop_name_three)
    TextView tvNameThree;
    @BindView(R.id.tv_percent_one)
    TextView tvPercentOne;
    @BindView(R.id.tv_percent_two)
    TextView tvPercentTwo;
    @BindView(R.id.tv_percent_three)
    TextView tvPercentThree;
    @BindView(R.id.rl_position_one)
    LinearLayout layoutItemOne;
    @BindView(R.id.rl_position_two)
    LinearLayout layoutItemTwo;
    @BindView(R.id.rl_position_three)
    LinearLayout layoutItemThree;
    @BindView(R.id.tv_distance_one)
    TextView tvDistanceOne;
    @BindView(R.id.tv_distance_two)
    TextView tvDistanceTwo;
    @BindView(R.id.tv_distance_three)
    TextView tvDistanceThree;
    @BindView(R.id.ll_distance_one)
    LinearLayout llDistanceOne;
    @BindView(R.id.ll_distance_two)
    LinearLayout llDistanceTwo;
    @BindView(R.id.ll_distance_three)
    LinearLayout llDistanceThree;
    @BindView(R.id.ic_marker1)
    ImageView icMarker1;
    @BindView(R.id.ic_marker2)
    ImageView icMarker2;
    @BindView(R.id.ic_marker3)
    ImageView icMarker3;

    DecimalFormat df = new DecimalFormat("##.##%");
    Location mUserLocation;
    boolean isOddPage = true;

    public static SearchShopListFragment newInstance(ShopSuggestListParcel beanList, Location userLocation, boolean isPageOdd) {
        SearchShopListFragment fragment = new SearchShopListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_SUGG_LIST, beanList);
        bundle.putParcelable(KEY_USER_LOCATION, userLocation);
        bundle.putBoolean(IS_ODD_PAGE, isPageOdd);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SearchShopListFragment newInstance(ShopAdvListParcel beanList, Location userLocation, boolean isPageOdd) {
        SearchShopListFragment fragment = new SearchShopListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_ADV_LIST, beanList);
        bundle.putParcelable(KEY_USER_LOCATION, userLocation);
        bundle.putBoolean(IS_ODD_PAGE, isPageOdd);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void getArgument(Bundle bundle) {
        mUserLocation = bundle.getParcelable(KEY_USER_LOCATION);
        isOddPage = bundle.getBoolean(IS_ODD_PAGE);
        if (bundle.containsKey(KEY_SUGG_LIST)) {
            listSuggest = bundle.getParcelable(KEY_SUGG_LIST);
            shopSuggestBeanList = listSuggest.beanList;
        } else if (bundle.containsKey(KEY_ADV_LIST)) {
            listAdv = bundle.getParcelable(KEY_ADV_LIST);
            shopAdvBeanList = listAdv.beanList;
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_shop_search;
    }

    @Override
    protected void initView(View root) {
        if (isOddPage) {
            layoutItemOne.setBackgroundColor(getResources().getColor(R.color.color_white));
            layoutItemTwo.setBackgroundColor(getResources().getColor(R.color.color_black));
            layoutItemThree.setBackgroundColor(getResources().getColor(R.color.color_white));
        } else {
            layoutItemOne.setBackgroundColor(getResources().getColor(R.color.color_black));
            layoutItemTwo.setBackgroundColor(getResources().getColor(R.color.color_white));
            layoutItemThree.setBackgroundColor(getResources().getColor(R.color.color_black));
            tvPercentOne.setTextColor(getResources().getColor(R.color.color_white));
            tvPercentTwo.setTextColor(getResources().getColor(R.color.color_black));
            tvPercentThree.setTextColor(getResources().getColor(R.color.color_white));
            tvDistanceOne.setTextColor(getResources().getColor(R.color.color_white));
            tvDistanceTwo.setTextColor(getResources().getColor(R.color.color_black));
            tvDistanceThree.setTextColor(getResources().getColor(R.color.color_white));
            icMarker1.setImageDrawable(getResources().getDrawable(R.drawable.ic_marker));
            icMarker2.setImageDrawable(getResources().getDrawable(R.drawable.ic_marker_black));
            icMarker3.setImageDrawable(getResources().getDrawable(R.drawable.ic_marker));
        }
        initShopSuggestion();
        initShopAdv();

    }


    private void initShopSuggestion() {
        if (shopSuggestBeanList != null) {
            // default_img list have size = 1
            ImageLoader.loadImage(getActivity(), R.drawable.default_img, shopSuggestBeanList.get(0).avatarImageUrl, ivShopOne);
            StringUtil.displayText(shopSuggestBeanList.get(0).name, tvNameOne);
            StringUtil.displayText(String.valueOf(df.format(shopSuggestBeanList.get(0).matchingRate)), tvPercentOne);
            StringUtil.displayText(setShopLocation(shopSuggestBeanList.get(0).mapLatitude, shopSuggestBeanList.get(0).mapLongtitude), tvDistanceOne);
            switch (shopSuggestBeanList.size()) {
                case APPConstant.SIZE_LIST_IS_ONE:
                    UiUtil.hideView(layoutItemTwo, false);
                    UiUtil.hideView(layoutItemThree, false);
                    break;
                case APPConstant.SIZE_LIST_IS_TWO:
                    ImageLoader.loadImage(getActivity(), R.drawable.default_img, shopSuggestBeanList.get(1).avatarImageUrl, ivShopTwo);
                    StringUtil.displayText(shopSuggestBeanList.get(1).name, tvNameTwo);
                    StringUtil.displayText(String.valueOf(df.format(shopSuggestBeanList.get(1).matchingRate)), tvPercentTwo);
                    StringUtil.displayText(setShopLocation(shopSuggestBeanList.get(1).mapLatitude, shopSuggestBeanList.get(1).mapLongtitude), tvDistanceTwo);
                    UiUtil.hideView(layoutItemThree, false);
                    break;
                case APPConstant.SIZE_LIST_IS_THREE:
                    ImageLoader.loadImage(getActivity(), R.drawable.default_img, shopSuggestBeanList.get(1).avatarImageUrl, ivShopTwo);
                    StringUtil.displayText(shopSuggestBeanList.get(1).name, tvNameTwo);
                    StringUtil.displayText(String.valueOf(df.format(shopSuggestBeanList.get(1).matchingRate)), tvPercentTwo);
                    StringUtil.displayText(setShopLocation(shopSuggestBeanList.get(1).mapLatitude, shopSuggestBeanList.get(1).mapLongtitude), tvDistanceTwo);

                    ImageLoader.loadImage(getActivity(), R.drawable.default_img, shopSuggestBeanList.get(2).avatarImageUrl, ivShopThree);
                    StringUtil.displayText(shopSuggestBeanList.get(2).name, tvNameThree);
                    StringUtil.displayText(String.valueOf(df.format(shopSuggestBeanList.get(2).matchingRate)), tvPercentThree);
                    StringUtil.displayText(setShopLocation(shopSuggestBeanList.get(2).mapLatitude, shopSuggestBeanList.get(2).mapLongtitude), tvDistanceThree);
                    break;
            }
        }
    }

    private void initShopAdv() {
        if (shopAdvBeanList != null) {
            // default_img list have size = 1 and hide text percent
            hidePercentLayout();
            ImageLoader.loadImage(getActivity(), R.drawable.default_img, shopAdvBeanList.get(0).avatarImageUrl, ivShopOne);
            StringUtil.displayText(shopAdvBeanList.get(0).name, tvNameOne);
            StringUtil.displayText(setShopLocation(shopAdvBeanList.get(0).mapLatitude, shopAdvBeanList.get(0).mapLongtitude), tvDistanceOne);

            switch (shopAdvBeanList.size()) {
                case APPConstant.SIZE_LIST_IS_ONE:
                    UiUtil.hideView(layoutItemTwo, false);
                    UiUtil.hideView(layoutItemThree, false);
                    break;
                case APPConstant.SIZE_LIST_IS_TWO:
                    ImageLoader.loadImage(getActivity(), R.drawable.default_img, shopAdvBeanList.get(1).avatarImageUrl, ivShopTwo);
                    StringUtil.displayText(shopAdvBeanList.get(1).name, tvNameTwo);
                    StringUtil.displayText(setShopLocation(shopAdvBeanList.get(1).mapLatitude, shopAdvBeanList.get(1).mapLongtitude), tvDistanceTwo);
                    UiUtil.hideView(layoutItemThree, false);
                    break;
                case APPConstant.SIZE_LIST_IS_THREE:
                    ImageLoader.loadImage(getActivity(), R.drawable.default_img, shopAdvBeanList.get(1).avatarImageUrl, ivShopTwo);
                    StringUtil.displayText(shopAdvBeanList.get(1).name, tvNameTwo);
                    StringUtil.displayText(setShopLocation(shopAdvBeanList.get(1).mapLatitude, shopAdvBeanList.get(1).mapLongtitude), tvDistanceTwo);

                    ImageLoader.loadImage(getActivity(), R.drawable.default_img, shopAdvBeanList.get(2).avatarImageUrl, ivShopThree);
                    StringUtil.displayText(shopAdvBeanList.get(2).name, tvNameThree);
                    StringUtil.displayText(setShopLocation(shopAdvBeanList.get(2).mapLatitude, shopAdvBeanList.get(2).mapLongtitude), tvDistanceThree);
            }
        }
    }

    private String setShopLocation(double latitude, double longitude) {
        Float distance = 0.0f;
        Location shopLocation = new Location("");
        shopLocation.setLatitude(latitude);
        shopLocation.setLongitude(longitude);
        if (mUserLocation != null) {
            distance = mUserLocation.distanceTo(shopLocation);
        } else hideDistanceLayout();
        return StringUtil.convertDistanceToString(distance);
    }

    private void hidePercentLayout() {
        UiUtil.hideView(tvPercentOne, false);
        UiUtil.hideView(tvPercentTwo, false);
        UiUtil.hideView(tvPercentThree, false);
    }

    private void hideDistanceLayout() {
        UiUtil.hideView(llDistanceOne, true);
        UiUtil.hideView(llDistanceTwo, true);
        UiUtil.hideView(llDistanceThree, true);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_shop_one, R.id.iv_shop_two, R.id.iv_shop_three})
    public void onClick(View v) {
        getMainActivity().closeDrawer();
        if (shopSuggestBeanList != null) {
            switch (v.getId()) {
                case R.id.iv_shop_one:
                    onChangeFragmentListen();
                    replaceFragment(R.id.container, ShopMainFragment.newInstance(shopSuggestBeanList.get(0).id), true);
                    break;
                case R.id.iv_shop_two:
                    onChangeFragmentListen();
                    replaceFragment(R.id.container, ShopMainFragment.newInstance(shopSuggestBeanList.get(1).id), true);
                    break;
                case R.id.iv_shop_three:
                    onChangeFragmentListen();
                    replaceFragment(R.id.container, ShopMainFragment.newInstance(shopSuggestBeanList.get(2).id), true);
                    break;
            }
        } else if (shopAdvBeanList != null) {
            switch (v.getId()) {
                case R.id.iv_shop_one:
                    onChangeFragmentListen();
                    replaceFragment(R.id.container, ShopMainFragment.newInstance(shopAdvBeanList.get(0).id), true);
                    break;
                case R.id.iv_shop_two:
                    onChangeFragmentListen();
                    replaceFragment(R.id.container, ShopMainFragment.newInstance(shopAdvBeanList.get(1).id), true);
                    break;
                case R.id.iv_shop_three:
                    onChangeFragmentListen();
                    replaceFragment(R.id.container, ShopMainFragment.newInstance(shopAdvBeanList.get(2).id), true);
                    break;
            }
        }

    }

    private void onChangeFragmentListen(){
        EventBusHelper.post(new ChangeFragmentListener(true));
    }

}
