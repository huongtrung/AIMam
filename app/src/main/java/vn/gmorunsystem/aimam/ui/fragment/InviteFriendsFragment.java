package vn.gmorunsystem.aimam.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.InviteFriendsRequest;
import vn.gmorunsystem.aimam.apis.response.InviteFriendResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.constants.APIConstant;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.DialogUtil;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

public class InviteFriendsFragment extends BaseFragment {
    @BindView(R.id.ll_expandable_email)
    LinearLayout llExpanableEmail;
    @BindView(R.id.ll_expandable_instagram)
    LinearLayout llExpanableIns;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.tv_error_email)
    TextView tvErrorEmail;

    private String mEmail;
    private CallbackManager mCallbackManager;

    public static InviteFriendsFragment newInstance() {
        InviteFriendsFragment fragment = new InviteFriendsFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invite_friends;
    }

    @Override
    protected void initView(View root) {
        setScreenTitle(R.string.nav_invite);
        getMainActivity().hideBottomBarAndSearchIcon();
        setUpUserInfo();
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    protected void initData() {
    }

    private void setUpUserInfo() {
        String avatarURL = SharedPrefUtils.getAvatarUrl();
        String userName = SharedPrefUtils.getUserName();

        ImageLoader.loadImage(getContext(), R.drawable.default_img, avatarURL, civAvatar);
        StringUtil.displayText(StringUtil.UppercaseFirstLetters(userName), tvUserName);
    }

    @Override
    protected void getArgument(Bundle bundle) {
    }

    @OnEditorAction({R.id.et_email, R.id.et_instagram})
    public boolean sendEmail(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            inviteFriendRequest();
            return true;
        }
        return false;
    }

    @OnClick({R.id.rl_email, R.id.rl_fb, R.id.rl_ins, R.id.btn_email, R.id.btn_instagram})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.rl_email:
                if (llExpanableIns.getVisibility() == View.VISIBLE) {
                    UiUtil.hideView(llExpanableIns, true);
                }
                showOrHideView(llExpanableEmail);
                break;
            case R.id.rl_fb:
                if (llExpanableEmail.getVisibility() == View.VISIBLE || llExpanableIns.getVisibility() == View.VISIBLE) {
                    UiUtil.hideView(llExpanableEmail, true);
                    UiUtil.hideView(llExpanableIns, true);
                }
                inviteFriendFacebook();
                break;
            case R.id.rl_ins:
                if (llExpanableEmail.getVisibility() == View.VISIBLE) {
                    UiUtil.hideView(llExpanableEmail, true);
                }
                showOrHideView(llExpanableIns);
                break;
            case R.id.btn_email:
                inviteFriendRequest();
                break;
            case R.id.btn_instagram:
                inviteFriendRequest();
                break;
        }
    }

    public void inviteFriendFacebook() {
        AppInviteContent content = new AppInviteContent.Builder()
                .setApplinkUrl(APIConstant.FACEBOOK_URL)
                .build();
        AppInviteDialog appInviteDialog = new AppInviteDialog(this);
        appInviteDialog.registerCallback(mCallbackManager,
                new FacebookCallback<AppInviteDialog.Result>() {
                    @Override
                    public void onSuccess(AppInviteDialog.Result result) {
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException e) {
                    }
                });
        AppInviteDialog.show(this, content);
    }

    private void inviteFriendRequest() {
        mEmail = etEmail.getText().toString().trim();
        if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
            if (validateEmail()) {
                showProgressBar();
                hideSoftKeyboard();
                InviteFriendsRequest inviteFriendsRequest = new InviteFriendsRequest(mEmail);
                inviteFriendsRequest.setRequestCallBack(new ApiObjectCallBack<InviteFriendResponse>() {
                    @Override
                    public void onSuccess(InviteFriendResponse data) {
                        hideProgressBar();
                        etEmail.setText("");
                        DialogUtil.showDialog(getContext(), getString(R.string.text_success_invite));
                    }

                    @Override
                    public void onFail(int failCode, String message) {
                        hideProgressBar();
                        CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                    }
                });
                inviteFriendsRequest.execute();
            }
        } else
            ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
    }

    private Boolean validateEmail() {
        if (!StringUtil.checkStringValid(mEmail) || !StringUtil.isValidEmail(mEmail)) {
            etEmail.setBackgroundResource(R.drawable.bg_error_invite);
            UiUtil.showView(tvErrorEmail);
            return false;
        } else {
            etEmail.setBackgroundResource(R.drawable.et_layout_expandable);
            UiUtil.hideView(tvErrorEmail, true);
            return true;
        }
    }
}
