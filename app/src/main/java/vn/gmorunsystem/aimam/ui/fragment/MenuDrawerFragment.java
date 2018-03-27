package vn.gmorunsystem.aimam.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.ProfileRequest;
import vn.gmorunsystem.aimam.apis.response.ProfileResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.data.ProfileData;
import vn.gmorunsystem.aimam.constants.APPConstant;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.IntentUtil;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.ToastUtil;

public class MenuDrawerFragment extends BaseFragment {
    @BindView(R.id.iv_Avatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tv_username)
    TextView tvUserName;

    private DrawerLayout mDrawerLayout;
    FragmentManager manager;
    View mainView;
    View drawerContainerView;

    ProfileData profileData;

    @OnClick({R.id.layout_profile, R.id.layout_coupon, R.id.layout_message, R.id.tv_editProfile,
            R.id.layout_invite, R.id.layout_setting, R.id.layout_about, R.id.layout_logout})
    public void onClicked(View v) {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawers();
        }
        switch (v.getId()) {
            case R.id.layout_profile:
                changeToFragment(ProfileFragment.newInstance(), APPConstant.PROFILE_FRAG_TAG);
                break;
            case R.id.layout_coupon:
                changeToFragment(CouponListFragment.newInstance(), APPConstant.COUPON_LIST_FRAG_TAG);
                break;
            case R.id.layout_message:
                changeToFragment(MessageListFragment.newInstance(), APPConstant.MESSAGE_LIST_FRAG_TAG);
                break;
            case R.id.layout_invite:
                changeToFragment(InviteFriendsFragment.newInstance(), APPConstant.INVITE_FRAG_TAG);
                break;
            case R.id.layout_setting:
                changeToFragment(SettingFragment.newInstance(), APPConstant.SETTING_FRAG_TAG);
                break;
            case R.id.layout_about:
                changeToFragment(AboutFragment.newInstance(), APPConstant.ABOUT_FRAG_TAG);
                break;
            case R.id.layout_logout:
                DialogUtil.showDialogFull(getContext(), "", getString(R.string.comfirm_logout_text), getString(R.string.title_no), getString(R.string.title_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logoutApp();
                    }
                });
                break;
            case R.id.tv_editProfile:
                if (checkFragmentIsCurrentVisible(APPConstant.UPDATE_PROFILE_FRAG_TAG)) {
                    //Do Nothing
                } else
                    getProfileData();
                break;
        }
    }

    private void changeToFragment(BaseFragment fragment, String tag) {
        String currentVisibleTag = isCurrentFragmentFromMenu();
        if (!TextUtils.isEmpty(currentVisibleTag) && !currentVisibleTag.equals(tag)) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            List<Fragment> fragments = manager.getFragments();

            for (int i = fragments.size() - 1; i >= 0; i--) {
                Fragment f = fragments.get(i);
                manager.popBackStack();
                if (f == null || currentVisibleTag.equals(f.getTag())) {
                    break;
                }
            }
        }

        if (!checkFragmentIsCurrentVisible(tag)) {
            replaceFragment(R.id.container, fragment, tag, true);
        }
    }

    public String isCurrentFragmentFromMenu() {
        FragmentManager manager = getActivity().getSupportFragmentManager();

        Fragment fragment = manager.findFragmentByTag(APPConstant.PROFILE_FRAG_TAG);
        if (fragment != null) {
            return APPConstant.PROFILE_FRAG_TAG;
        }
        fragment = manager.findFragmentByTag(APPConstant.COUPON_LIST_FRAG_TAG);
        if (fragment != null) {
            return APPConstant.COUPON_LIST_FRAG_TAG;
        }
        fragment = manager.findFragmentByTag(APPConstant.MESSAGE_LIST_FRAG_TAG);
        if (fragment != null) {
            return APPConstant.MESSAGE_LIST_FRAG_TAG;
        }
        fragment = manager.findFragmentByTag(APPConstant.INVITE_FRAG_TAG);
        if (fragment != null) {
            return APPConstant.INVITE_FRAG_TAG;
        }
        fragment = manager.findFragmentByTag(APPConstant.SETTING_FRAG_TAG);
        if (fragment != null) {
            return APPConstant.SETTING_FRAG_TAG;
        }
        fragment = manager.findFragmentByTag(APPConstant.ABOUT_FRAG_TAG);
        if (fragment != null) {
            return APPConstant.ABOUT_FRAG_TAG;
        }
        fragment = manager.findFragmentByTag(APPConstant.UPDATE_PROFILE_FRAG_TAG);
        if (fragment != null) {
            return APPConstant.UPDATE_PROFILE_FRAG_TAG;
        }
        return "";
    }

    private void logoutApp() {
        switch (SharedPrefUtils.getTypeLogin()) {
            case APPConstant.TYPE_LOCAL_ACCOUNT:
                SharedPrefUtils.removeUserInfo();
                break;
            case APPConstant.TYPE_FACEBOOK:
                SharedPrefUtils.removeUserInfo();
                LoginManager.getInstance().logOut();
                break;
            case APPConstant.TYPE_INSTAGRAM:
                SharedPrefUtils.removeCurrentInstagramUserInfo();
                SharedPrefUtils.removeUserInfo();
                break;
        }
        SharedPrefUtils.removeCurrentAccess();
        IntentUtil.gotoLogin(getActivity());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.drawer_item;
    }

    @Override
    protected void initView(View root) {
        setUpUserInfo();
    }

    @Override
    protected void initData() {
        manager = getActivity().getSupportFragmentManager();
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    public void setUpDrawerMenu(int fragmentDrawerId, final DrawerLayout drawerLayout) {
        drawerContainerView = getActivity().findViewById(fragmentDrawerId);
        mDrawerLayout = drawerLayout;
        mainView = getActivity().findViewById(R.id.container);
        mDrawerLayout.setScrimColor(getResources().getColor(R.color.black_opacity));
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    setMenuButtonState(true);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                setMenuButtonState(false);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        RelativeLayout rlList = (RelativeLayout) getActivity().findViewById(R.id.rl_list);
        rlList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START) || mDrawerLayout.isDrawerOpen(GravityCompat.END)) {
                    mDrawerLayout.closeDrawers();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                getActivity().invalidateOptionsMenu();
            }
        });
    }


    private void setMenuButtonState(boolean isShow) {
        ImageView menuItem = (ImageView) getActivity()
                .findViewById(R.id.iv_list);
        if (menuItem != null) {
            if (isShow) {
                menuItem.setImageResource(R.drawable.ic_list_green);
            } else {
                menuItem.setImageResource(R.drawable.ic_list);
            }
        }
    }


    public void setUpUserInfo() {
        String avatarURL = SharedPrefUtils.getAvatarUrl();
        String userName = SharedPrefUtils.getUserName();

        ImageLoader.loadImage(getContext(), R.drawable.default_img, avatarURL, ivAvatar);
        StringUtil.displayText(StringUtil.UppercaseFirstLetters(userName), tvUserName);
    }

    private boolean checkFragmentIsCurrentVisible(String tag) {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null && fragment.isVisible()) {
            return true;
        } else return false;
    }

    private void getProfileData() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            ProfileRequest profileRequest = new ProfileRequest();
            profileRequest.setRequestCallBack(new ApiObjectCallBack<ProfileResponse>() {
                @Override
                public void onSuccess(ProfileResponse data) {
                    if (data != null) {
                        profileData = new ProfileData(data);
                        if (profileData.gender == null) {
                            profileData.gender = 0;
                        }

                        String currentVisibleTag = isCurrentFragmentFromMenu();
                        if (!TextUtils.isEmpty(currentVisibleTag)) {
                            getActivity().getFragmentManager().popBackStack(currentVisibleTag, android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }

                        replaceFragment(R.id.container, UpdateProfileFragment.newInstance(profileData), APPConstant.UPDATE_PROFILE_FRAG_TAG, true);
                    }
                    hideProgressBar();
                }

                @Override
                public void onFail(int failCode, String message) {
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            profileRequest.execute();
            showProgressBar();
        } else ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

}
