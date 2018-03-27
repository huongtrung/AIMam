package vn.gmorunsystem.aimam.ui.fragment;


import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import de.hdodenhof.circleimageview.CircleImageView;
import vn.gmorunsystem.aimam.R;
import vn.gmorunsystem.aimam.apis.request.FeedBackRequest;
import vn.gmorunsystem.aimam.apis.response.FeedBackResponse;
import vn.gmorunsystem.aimam.apis.volley.callback.ApiObjectCallBack;
import vn.gmorunsystem.aimam.utils.AnimationUtil;
import vn.gmorunsystem.aimam.utils.CheckErrorCodeUtil;
import vn.gmorunsystem.aimam.utils.ImageLoader;
import vn.gmorunsystem.aimam.utils.NetworkUtils;
import vn.gmorunsystem.aimam.utils.SharedPrefUtils;
import vn.gmorunsystem.aimam.utils.StringUtil;
import vn.gmorunsystem.aimam.utils.ToastUtil;
import vn.gmorunsystem.aimam.utils.UiUtil;

import static vn.gmorunsystem.aimam.utils.DialogUtil.showDialog;

public class FeedbackFragment extends BaseFragment {
    @BindView(R.id.et_input_code)
    EditText etInputCode;
    @BindView(R.id.tv_code)
    TextView tvRandomCode;
    @BindView(R.id.iv_refresh_code)
    ImageView ivRefreshCode;
    @BindView(R.id.et_feedback)
    EditText etFeedback;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.civ_avatar)
    CircleImageView civAvatar;
    @BindView(R.id.tv_error_content)
    TextView tvErrorContent;

    String mContent;
    String randomCode;
    String randomCodeInput;

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_feedback;
    }

    @Override
    protected void initView(View root) {
        setScreenTitle(R.string.txt_setting_feedback);
        getMainActivity().hideBottomBarAndSearchIcon();
    }

    @Override
    protected void initData() {
        setUpUserInfo();
        StringUtil.displayText(StringUtil.randomString(), tvRandomCode);
    }

    private void setUpUserInfo() {
        etInputCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        String avatarURL = SharedPrefUtils.getAvatarUrl();
        String userName = SharedPrefUtils.getUserName();

        ImageLoader.loadImage(getContext(), R.drawable.default_img, avatarURL, civAvatar);
        StringUtil.displayText(StringUtil.UppercaseFirstLetters(userName), tvUserName);
    }

    @Override
    protected void getArgument(Bundle bundle) {

    }

    @OnEditorAction(R.id.et_feedback)
    public boolean sendFeedback(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            getFeedbackRequest();
            return true;
        }
        return false;
    }

    @OnClick({R.id.iv_refresh_code, R.id.btn_send})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.iv_refresh_code:
                clearInputCode();
                AnimationUtil.loadAnimationRotate(getContext(), ivRefreshCode);
                break;
            case R.id.btn_send:
                randomCode = tvRandomCode.getText().toString().trim();
                randomCodeInput = etInputCode.getText().toString().trim();
                if (NetworkUtils.getInstance(getContext()).isNetworkConnected()) {
                    getFeedbackRequest();
                } else
                    ToastUtil.makeText(getContext(), getString(R.string.no_connect_internet));
                break;
        }
    }

    private void clearInputCode() {
        etInputCode.setText("");
        StringUtil.displayText(StringUtil.randomString(), tvRandomCode);
    }

    private void getFeedbackRequest() {
        mContent = etFeedback.getText().toString().trim();
        if (validateInput()) {
            showProgressBar();
            hideSoftKeyboard();
            FeedBackRequest feedBackRequest = new FeedBackRequest(mContent);
            feedBackRequest.setRequestCallBack(new ApiObjectCallBack<FeedBackResponse>() {
                @Override
                public void onSuccess(FeedBackResponse data) {
                    hideProgressBar();
                    if (data != null) {
                        clearInputCode();
                        showDialog(getContext(), getString(R.string.text_feedback_success));
                    }
                    else {
                        showDialog(getContext(), getString(R.string.error_server));
                    }
                }

                @Override
                public void onFail(int failCode, String message) {
                    clearInputCode();
                    hideProgressBar();
                    CheckErrorCodeUtil.showDialogError(getContext(), failCode);
                }
            });
            feedBackRequest.execute();
        }
    }

    private boolean validateInput() {
        if (StringUtil.isEmpty(mContent) || mContent.length() > 200) {
            clearInputCode();
            etFeedback.setBackgroundResource(R.drawable.bg_error_invite);
            UiUtil.showView(tvErrorContent);
            return false;
        } else if (!randomCodeInput.equalsIgnoreCase(randomCode)) {
            if (!StringUtil.isEmpty(mContent) || mContent.length() < 200) {
                UiUtil.hideView(tvErrorContent, true);
                etFeedback.setBackgroundResource(R.drawable.et_feedback_bg);
            }
            clearInputCode();
            etInputCode.setBackgroundResource(R.drawable.bg_error_invite);
            return false;
        } else {
            UiUtil.hideView(tvErrorContent, true);
            etFeedback.setBackgroundResource(R.drawable.et_feedback_bg);
            etInputCode.setBackgroundResource(R.drawable.et_feedback_bg);
            return true;
        }
    }
}
