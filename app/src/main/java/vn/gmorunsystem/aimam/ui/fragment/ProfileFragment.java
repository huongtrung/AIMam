package vn.gmorunsystem.aimam.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.ProfileRequest;
import vn.gmorunsystem.aimam.apis.response.ProfileResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.bean.ImageProfileBean;
import vn.gmorunsystem.aimam.bean.data.ProfileData;
import vn.gmorunsystem.aimam.callback.OnRefreshProfile;
import vn.gmorunsystem.aimam.ui.adapter.OnRecyclerViewItemClick;
import vn.gmorunsystem.aimam.ui.adapter.ProfileAdapter;
import vn.gmorunsystem.aimam.ui.customview.ProfileUploadedImageDialogView;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.ToastUtil;

/**
 * Created by Veteran Commander on 5/17/2017.
 */

public class ProfileFragment extends BaseFragment implements OnRecyclerViewItemClick {
    private static final int COLUMN_OF_IMAGE = 4;

    @BindView(R.id.recycerList)
    RecyclerView rvList;
    @BindView(R.id.tv_pf_Username)
    TextView tvUsername;
    @BindView(R.id.tv_pf_postcount)
    TextView tvPostCount;
    @BindView(R.id.tv_pf_useful)
    TextView tvUserful;
    @BindView(R.id.iv_pf_avatar)
    CircleImageView ivAvatar;

    ProfileAdapter profileAdapter;
    OnRefreshProfile onRefreshProfileAvatarUrl;
    ProfileData profileData;

    public static ProfileFragment newInstance() {
        ProfileFragment profileFragment = new ProfileFragment();
        return profileFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onRefreshProfileAvatarUrl = (OnRefreshProfile) context;
    }

    @Override
    protected void initView(View root) {
        setScreenTitle(R.string.title_pf_screen);
        getMainActivity().hideBottomBarAndSearchIcon();
    }

    @Override
    protected void initData() {
        getDataRequest();
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @OnClick(R.id.tv_pf_editpf)
    public void onClick() {
        //fix error gender null when write to parcel
        if (profileData.gender == null) {
            profileData.gender = 0;
        }
        replaceFragment(R.id.container, UpdateProfileFragment.newInstance(profileData));
    }

    public void getDataRequest() {
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            ProfileRequest profileRequest = new ProfileRequest();
            profileRequest.setRequestCallBack(new ApiObjectCallBack<ProfileResponse>() {
                @Override
                public void onSuccess(ProfileResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        setUpProfile(data);
                        //Luu avatar trong truong hop vua moi update avatar
                        if (!data.avatarImageUrl.equalsIgnoreCase(SharedPrefUtils.getAvatarUrl()) || !data.fullName.equalsIgnoreCase(SharedPrefUtils.getUserName())) {
                            SharedPrefUtils.saveUserName(data.fullName);
                            SharedPrefUtils.saveAvatarUrl(data.avatarImageUrl);
                            if (onRefreshProfileAvatarUrl != null) {
                                onRefreshProfileAvatarUrl.refreshProfile();
                            }
                        }
                        ImageLoader.loadImage(getActivity(), R.drawable.default_img, data.avatarImageUrl, ivAvatar);
                    }else {
                        DialogUtil.showDialog(getContext(),getString(R.string.not_have_data));
                    }
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

    private void setUpProfile(ProfileResponse data) {
        StringUtil.displayText(StringUtil.UppercaseFirstLetters(data.fullName), tvUsername);
        StringUtil.displayText(data.post + " " + getString(R.string.text_post), tvPostCount);
        StringUtil.displayText(data.useful + " " + getString(R.string.text_useful), tvUserful);
        profileData = new ProfileData(data);
        if (data.images != null && data.images.size() > 0) {
            setUserImageList(data.images);
        }
    }


    public void setUserImageList(final List<ImageProfileBean> imageList) {
        rvList.setLayoutManager(new GridLayoutManager(getActivity(), COLUMN_OF_IMAGE));
        rvList.setNestedScrollingEnabled(false);
        profileAdapter = new ProfileAdapter(imageList);
        profileAdapter.setOnRecyclerViewItemClick(new OnRecyclerViewItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                ImageProfileBean itemImage = imageList.get(position);
                showImage(itemImage.imageAvartar);
            }
        });
        rvList.setAdapter(profileAdapter);
    }

    public void showImage(String imageUrl) {
        final ProfileUploadedImageDialogView dialog = new ProfileUploadedImageDialogView(getActivity());
        dialog.setImageUrl(imageUrl);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }
}
